package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.work.reservationsystem.entity.Shift;
import com.work.reservationsystem.util.DBUtil;

public class ShiftDao {

    /**
     * シフトの総数を取得
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM shift";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("シフト数の取得に失敗しました", e);
        }
        return 0;
    }

    /**
     * ページングを使ってシフトを取得
     */
    public List<Shift> findPage(int offset, int pageSize) {
        String sql = "SELECT * FROM shift ORDER BY create_time DESC LIMIT ?, ?";
        List<Shift> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractShift(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("シフトのページング取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 新しいシフトを追加
     */
    public void insert(Shift shift) {
        String sql = "INSERT INTO shift (name, start_time, end_time, salary, description, create_time, update_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, shift.getName());
            stmt.setTime(2, new Time(shift.getStartTime().getTime()));
            stmt.setTime(3, new Time(shift.getEndTime().getTime()));
            stmt.setBigDecimal(4, shift.getSalary());
            stmt.setString(5, shift.getDescription());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(6, now);
            stmt.setTimestamp(7, now);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("シフトの追加に失敗しました", e);
        }
    }

    /**
     * 既存シフトを更新
     */
    public void update(Shift shift) {
        String sql = "UPDATE shift SET name = ?, start_time = ?, end_time = ?, salary = ?, description = ?, update_time = ? " +
                     "WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, shift.getName());
            stmt.setTime(2, new Time(shift.getStartTime().getTime()));
            stmt.setTime(3, new Time(shift.getEndTime().getTime()));
            stmt.setBigDecimal(4, shift.getSalary());
            stmt.setString(5, shift.getDescription());
            stmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(7, shift.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("シフトの更新に失敗しました", e);
        }
    }

    /**
     * シフトを削除
     */
    public void delete(Integer id) {
        String sql = "DELETE FROM shift WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("シフトの削除に失敗しました", e);
        }
    }

    /**
     * IDによるシフトを検索
     */
    public Shift findById(Integer id) {
        String sql = "SELECT * FROM shift WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractShift(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("シフトの取得に失敗しました", e);
        }
        return null;
    }

    /**
     * すべてのシフトを取得
     */
    public List<Shift> findAll() {
        String sql = "SELECT * FROM shift ORDER BY create_time DESC";
        List<Shift> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractShift(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("すべてのシフトの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * ResultSetからShiftオブジェクトを抽出
     */
    private Shift extractShift(ResultSet rs) throws SQLException {
        Shift shift = new Shift();
        shift.setId(rs.getInt("id"));
        shift.setName(rs.getString("name"));
        shift.setStartTime(rs.getTime("start_time"));
        shift.setEndTime(rs.getTime("end_time"));
        shift.setSalary(rs.getBigDecimal("salary"));
        shift.setDescription(rs.getString("description"));
        shift.setCreateTime(rs.getTimestamp("create_time"));
        shift.setUpdateTime(rs.getTimestamp("update_time"));
        return shift;
    }
}