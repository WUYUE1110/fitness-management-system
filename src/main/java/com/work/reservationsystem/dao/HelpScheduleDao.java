package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.work.reservationsystem.util.DBUtil;

public class HelpScheduleDao {
    
    /**
     * Helpスケジュールを挿入
     */
    public void insert(Date scheduleDate) {
        String sql = "INSERT INTO help_schedule (schedule_date, create_time) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(scheduleDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Helpレコードの追加に失敗しました", e);
        }
    }

    /**
     * Helpスケジュールを削除
     */
    public void delete(Date scheduleDate) {
        String sql = "DELETE FROM help_schedule WHERE schedule_date = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(scheduleDate.getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Helpレコードの削除に失敗しました", e);
        }
    }

    /**
     * 指定範囲の日付でHelpスケジュールを検索
     */
    public List<Date> findByDateRange(Date startDate, Date endDate) {
        String sql = "SELECT schedule_date FROM help_schedule WHERE schedule_date BETWEEN ? AND ? ORDER BY schedule_date";
        List<Date> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getDate("schedule_date"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Helpレコードの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 指定日付にHelpスケジュールが存在するかチェック
     */
    public boolean existsByDate(Date date) {
        String sql = "SELECT COUNT(*) FROM help_schedule WHERE schedule_date = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Helpレコードのチェックに失敗しました", e);
        }
        return false;
    }
}