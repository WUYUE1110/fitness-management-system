package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.work.reservationsystem.entity.Appointment;
import com.work.reservationsystem.util.DBUtil;
import com.work.reservationsystem.vo.AppointmentVO;

public class AppointmentDao {
    public int count() {
        String sql = "SELECT COUNT(*) FROM appointment";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("予約総数の取得に失敗しました", e);
        }
        return 0;
    }

    public List<AppointmentVO> findPage(int offset, int pageSize) {
        String sql = "SELECT a.*, c.name as customer_name, h.name as coach_name, e.name as equipment_name " +
                    "FROM appointment a " +
                    "LEFT JOIN customer c ON a.customer_id = c.id " +
                    "LEFT JOIN coach h ON a.coach_id = h.id " +
                    "LEFT JOIN equipment e ON a.equipment_id = e.id " +
                    "ORDER BY a.start_time DESC " +
                    "LIMIT ?, ?";
        List<AppointmentVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractAppointmentVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("予約のページネーション取得に失敗しました", e);
        }
        return list;
    }

    public AppointmentVO findById(Integer id) {
        String sql = "SELECT a.*, c.name as customer_name, h.name as coach_name, e.name as equipment_name " +
                    "FROM appointment a " +
                    "LEFT JOIN customer c ON a.customer_id = c.id " +
                    "LEFT JOIN coach h ON a.coach_id = h.id " +
                    "LEFT JOIN equipment e ON a.equipment_id = e.id " +
                    "WHERE a.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractAppointmentVO(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("予約の取得に失敗しました", e);
        }
        return null;
    }

    public List<AppointmentVO> findByCustomerId(Integer customerId) {
        String sql = "SELECT a.*, c.name as customer_name, h.name as coach_name, e.name as equipment_name " +
                    "FROM appointment a " +
                    "LEFT JOIN customer c ON a.customer_id = c.id " +
                    "LEFT JOIN coach h ON a.coach_id = h.id " +
                    "LEFT JOIN equipment e ON a.equipment_id = e.id " +
                    "WHERE a.customer_id = ? " +
                    "ORDER BY a.start_time DESC";
        return findByCondition(sql, customerId);
    }

    public List<AppointmentVO> findByCoachId(Integer coachId) {
        String sql = "SELECT a.*, c.name as customer_name, h.name as coach_name, e.name as equipment_name " +
                    "FROM appointment a " +
                    "LEFT JOIN customer c ON a.customer_id = c.id " +
                    "LEFT JOIN coach h ON a.coach_id = h.id " +
                    "LEFT JOIN equipment e ON a.equipment_id = e.id " +
                    "WHERE a.coach_id = ? " +
                    "ORDER BY a.start_time DESC";
        return findByCondition(sql, coachId);
    }

    public List<AppointmentVO> findByEquipmentId(Integer equipmentId) {
        String sql = "SELECT a.*, c.name as customer_name, h.name as coach_name, e.name as equipment_name " +
                    "FROM appointment a " +
                    "LEFT JOIN customer c ON a.customer_id = c.id " +
                    "LEFT JOIN coach h ON a.coach_id = h.id " +
                    "LEFT JOIN equipment e ON a.equipment_id = e.id " +
                    "WHERE a.equipment_id = ? " +
                    "ORDER BY a.start_time DESC";
        return findByCondition(sql, equipmentId);
    }

    private List<AppointmentVO> findByCondition(String sql, Integer param) {
        List<AppointmentVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, param);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractAppointmentVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("予約の取得に失敗しました", e);
        }
        return list;
    }

    private AppointmentVO extractAppointmentVO(ResultSet rs) throws SQLException {
        AppointmentVO vo = new AppointmentVO();
        vo.setId(rs.getInt("id"));
        vo.setCustomerId(rs.getInt("customer_id"));
        vo.setCustomerName(rs.getString("customer_name"));
        vo.setCoachId(rs.getInt("coach_id"));
        vo.setCoachName(rs.getString("coach_name"));
        vo.setEquipmentId(rs.getInt("equipment_id"));
        vo.setEquipmentName(rs.getString("equipment_name"));
        vo.setStartTime(rs.getTimestamp("start_time"));
        vo.setEndTime(rs.getTimestamp("end_time"));
        vo.setFinishTime(rs.getTimestamp("finish_time"));
        vo.setStatus(rs.getInt("status"));
        vo.setRemark(rs.getString("remark"));
        vo.setCreateTime(rs.getTimestamp("create_time"));
        vo.setUpdateTime(rs.getTimestamp("update_time"));
        return vo;
    }

    public void insert(Appointment appointment) {
        String sql = "INSERT INTO appointment (customer_id, coach_id, equipment_id, start_time, end_time, status, remark, create_time, update_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointment.getCustomerId());
            stmt.setInt(2, appointment.getCoachId());
            stmt.setInt(3, appointment.getEquipmentId());
            stmt.setTimestamp(4, new Timestamp(appointment.getStartTime().getTime()));
            stmt.setTimestamp(5, new Timestamp(appointment.getEndTime().getTime()));
            stmt.setInt(6, 0);  // 0-未開始 1-完了 2-キャンセル済み
            stmt.setString(7, appointment.getRemark());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(8, now);
            stmt.setTimestamp(9, now);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("予約の作成に失敗しました", e);
        }
    }

    public boolean existsByCustomerAndSchedule(Integer customerId, Integer scheduleId) {
        String sql = "SELECT COUNT(*) FROM appointment WHERE customer_id = ? AND schedule_id = ? AND status != 2";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, scheduleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询预约记录失败", e);
        }
        return false;
    }

    public boolean existsByCustomerAndTime(Integer customerId, Date startTime, Date endTime) {
        String sql = "SELECT COUNT(*) FROM appointment " +
                    "WHERE customer_id = ? AND status != 2 " +
                    "AND ((start_time <= ? AND end_time > ?) OR " +
                    "(start_time < ? AND end_time >= ?) OR " +
                    "(start_time >= ? AND end_time <= ?))";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setTimestamp(2, new Timestamp(endTime.getTime()));
            stmt.setTimestamp(3, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(4, new Timestamp(endTime.getTime()));
            stmt.setTimestamp(5, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(6, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(7, new Timestamp(endTime.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("予約の取得に失敗しました", e);
        }
        return false;
    }

    public boolean existsByCoachAndTime(Integer coachId, Date startTime, Date endTime) {
        String sql = "SELECT COUNT(*) FROM appointment " +
                    "WHERE coach_id = ? AND status != 2 " +
                    "AND ((start_time <= ? AND end_time > ?) OR " +
                    "(start_time < ? AND end_time >= ?) OR " +
                    "(start_time >= ? AND end_time <= ?))";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            stmt.setTimestamp(2, new Timestamp(endTime.getTime()));
            stmt.setTimestamp(3, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(4, new Timestamp(endTime.getTime()));
            stmt.setTimestamp(5, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(6, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(7, new Timestamp(endTime.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("予約の取得に失敗しました", e);
        }
        return false;
    }
} 