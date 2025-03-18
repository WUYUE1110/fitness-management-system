package com.work.reservationsystem.dao;

import com.work.reservationsystem.entity.Admin;
import com.work.reservationsystem.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDao {
    
    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE (username = ? OR email = ?) AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractAdmin(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("管理者のログイン取得に失敗しました", e);
        }
        return null;
    }

    public List<Admin> findAll() {
        String sql = "SELECT * FROM admin";
        List<Admin> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractAdmin(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("管理者リストの取得に失敗しました", e);
        }
        return list;
    }

    public Admin findById(Integer id) {
        String sql = "SELECT * FROM admin WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractAdmin(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("管理者の取得に失敗しました", e);
        }
        return null;
    }

    public void insert(Admin admin) {
        String sql = "INSERT INTO admin (username, password, real_name, phone, email, create_time, update_time) " +
                    "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getRealName());
            stmt.setString(4, admin.getPhone());
            stmt.setString(5, admin.getEmail());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                admin.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("管理者の追加に失敗しました", e);
        }
    }

    public void update(Admin admin) {
        String sql = "UPDATE admin SET username = ?, password = ?, real_name = ?, phone = ?, email = ?, update_time = NOW() " +
                    "WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getRealName());
            stmt.setString(4, admin.getPhone());
            stmt.setString(5, admin.getEmail());
            stmt.setInt(6, admin.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("管理者の更新に失敗しました", e);
        }
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM admin WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("管理者の削除に失敗しました", e);
        }
    }

    public int count() {
        String sql = "SELECT COUNT(*) FROM admin";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("管理者の総数取得に失敗しました", e);
        }
        return 0;
    }

    public List<Admin> findPage(int offset, int pageSize) {
        String sql = "SELECT * FROM admin LIMIT ?, ?";
        List<Admin> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractAdmin(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("管理者のページネーション取得に失敗しました", e);
        }
        return list;
    }

    private Admin extractAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getInt("id"));
        admin.setUsername(rs.getString("username"));
        admin.setPassword(rs.getString("password"));
        admin.setRealName(rs.getString("real_name"));
        admin.setPhone(rs.getString("phone"));
        admin.setEmail(rs.getString("email"));
        admin.setCreateTime(rs.getTimestamp("create_time"));
        admin.setUpdateTime(rs.getTimestamp("update_time"));
        return admin;
    }
} 