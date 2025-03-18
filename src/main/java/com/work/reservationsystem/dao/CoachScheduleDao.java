package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.work.reservationsystem.entity.CoachSchedule;
import com.work.reservationsystem.util.DBUtil;
import com.work.reservationsystem.vo.CoachScheduleVO;

public class CoachScheduleDao {

    /**
     * コーチIDと日付範囲でスケジュールを検索
     */
    public List<CoachScheduleVO> findByCoachAndDateRange(Integer coachId, Date startDate, Date endDate) {
        String sql = "SELECT s.*, c.name as coach_name, sh.name as shift_name " +
                     "FROM coach_schedule s " +
                     "LEFT JOIN coach c ON s.coach_id = c.id " +
                     "LEFT JOIN shift sh ON s.shift_id = sh.id " +
                     "WHERE s.coach_id = ? AND s.schedule_date BETWEEN ? AND ? " +
                     "ORDER BY s.schedule_date";
        List<CoachScheduleVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractScheduleVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("スケジュール情報の取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 新しいスケジュールを挿入
     */
    public void insert(CoachSchedule schedule) {
        String sql = "INSERT INTO coach_schedule (coach_id, shift_id, schedule_date, status, create_time, update_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, schedule.getCoachId());
            stmt.setInt(2, schedule.getShiftId());
            stmt.setDate(3, new java.sql.Date(schedule.getScheduleDate().getTime()));
            stmt.setInt(4, schedule.getStatus());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(5, now);
            stmt.setTimestamp(6, now);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("スケジュールの追加に失敗しました", e);
        }
    }

    /**
     * ステータスを更新
     */
    public void updateStatus(Integer id, Integer status) {
        String sql = "UPDATE coach_schedule SET status = ?, update_time = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, status);
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("スケジュールのステータス更新に失敗しました", e);
        }
    }

    /**
     * スケジュールを削除
     */
    public void delete(Integer id) {
        String sql = "DELETE FROM coach_schedule WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("スケジュールの削除に失敗しました", e);
        }
    }

    /**
     * IDからスケジュールを取得
     */
    public CoachSchedule findById(Integer id) {
        String sql = "SELECT * FROM coach_schedule WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractSchedule(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("スケジュールの取得に失敗しました", e);
        }
        return null;
    }

    /**
     * コーチIDと日付でスケジュールが存在するかを確認
     */
    public boolean existsByCoachAndDate(Integer coachId, Date scheduleDate) {
        String sql = "SELECT COUNT(*) FROM coach_schedule WHERE coach_id = ? AND schedule_date = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            stmt.setDate(2, new java.sql.Date(scheduleDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("スケジュール情報の取得に失敗しました", e);
        }
        return false;
    }

    /**
     * コーチIDと日付からスケジュールを削除
     */
    public void deleteByCoachAndDate(Integer coachId, Date scheduleDate) {
        String sql = "DELETE FROM coach_schedule WHERE coach_id = ? AND schedule_date = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            stmt.setDate(2, new java.sql.Date(scheduleDate.getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("スケジュールの削除に失敗しました", e);
        }
    }

    /**
     * 指定日付範囲のスケジュールを取得
     */
    public List<CoachScheduleVO> findByDateRange(Date startDate, Date endDate) {
        String sql = "SELECT s.*, c.name as coach_name, sh.name as shift_name " +
                     "FROM coach_schedule s " +
                     "LEFT JOIN coach c ON s.coach_id = c.id " +
                     "LEFT JOIN shift sh ON s.shift_id = sh.id " +
                     "WHERE s.schedule_date BETWEEN ? AND ? " +
                     "ORDER BY s.schedule_date, c.name";
        List<CoachScheduleVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractScheduleVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("スケジュールの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 指定日付のスケジュールを取得
     */
    public List<CoachScheduleVO> findByDate(Date date) {
        String sql = "SELECT cs.*, c.name AS coach_name, s.name AS shift_name " +
                     "FROM coach_schedule cs " +
                     "LEFT JOIN coach c ON cs.coach_id = c.id " +
                     "LEFT JOIN shift s ON cs.shift_id = s.id " +
                     "WHERE cs.schedule_date = ? " +
                     "ORDER BY s.start_time";
        List<CoachScheduleVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractScheduleVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("スケジュールの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * コーチIDと日付でスケジュールを取得
     */
    public List<CoachScheduleVO> findByCoachAndDate(Integer coachId, Date date) {
        String sql = "SELECT cs.*, c.name AS coach_name, s.name AS shift_name " +
                     "FROM coach_schedule cs " +
                     "LEFT JOIN coach c ON cs.coach_id = c.id " +
                     "LEFT JOIN shift s ON cs.shift_id = s.id " +
                     "WHERE cs.coach_id = ? AND cs.schedule_date = ? " +
                     "ORDER BY s.start_time";
        List<CoachScheduleVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractScheduleVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("スケジュールの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * ResultSetからCoachScheduleオブジェクトを抽出
     */
    private CoachSchedule extractSchedule(ResultSet rs) throws SQLException {
        CoachSchedule schedule = new CoachSchedule();
        schedule.setId(rs.getInt("id"));
        schedule.setCoachId(rs.getInt("coach_id"));
        schedule.setShiftId(rs.getInt("shift_id"));
        schedule.setScheduleDate(rs.getDate("schedule_date"));
        schedule.setStatus(rs.getInt("status"));
        schedule.setCreateTime(rs.getTimestamp("create_time"));
        schedule.setUpdateTime(rs.getTimestamp("update_time"));
        return schedule;
    }

    /**
     * ResultSetからCoachScheduleVOオブジェクトを抽出
     */
    private CoachScheduleVO extractScheduleVO(ResultSet rs) throws SQLException {
        CoachScheduleVO vo = new CoachScheduleVO();
        vo.setId(rs.getInt("id"));
        vo.setCoachId(rs.getInt("coach_id"));
        vo.setCoachName(rs.getString("coach_name"));
        vo.setShiftId(rs.getInt("shift_id"));
        vo.setShiftName(rs.getString("shift_name"));
        vo.setScheduleDate(rs.getDate("schedule_date"));
        vo.setStatus(rs.getInt("status"));
        vo.setCreateTime(rs.getTimestamp("create_time"));
        vo.setUpdateTime(rs.getTimestamp("update_time"));
        return vo;
    }
}