package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.work.reservationsystem.entity.EquipmentCategory;
import com.work.reservationsystem.util.DBUtil;

public class EquipmentCategoryDao {

    /**
     * 器具カテゴリーの総数を取得
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM equipment_category";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("レッスンカテゴリー数の取得に失敗しました", e);
        }
        return 0;
    }

    /**
     * ページングによる器具カテゴリーの取得
     */
    public List<EquipmentCategory> findPage(int offset, int pageSize) {
        String sql = "SELECT * FROM equipment_category LIMIT ?, ?";
        List<EquipmentCategory> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractEquipmentCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("レッスンカテゴリーのページング取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 新しい器具カテゴリーを追加
     */
    public void insert(EquipmentCategory category) {
        String sql = "INSERT INTO equipment_category (name, description, create_time, update_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(3, now);
            stmt.setTimestamp(4, now);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("レッスンカテゴリーの追加に失敗しました", e);
        }
    }

    /**
     * IDにより器具カテゴリーを検索
     */
    public EquipmentCategory findById(Integer id) {
        String sql = "SELECT * FROM equipment_category WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractEquipmentCategory(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("レッスンカテゴリーの取得に失敗しました", e);
        }
        return null;
    }

    /**
     * 既存の器具カテゴリーを更新
     */
    public void update(EquipmentCategory category) {
        String sql = "UPDATE equipment_category SET name = ?, description = ?, update_time = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(4, category.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("レッスンカテゴリーの更新に失敗しました", e);
        }
    }

    /**
     * 器具カテゴリーを削除
     */
    public void delete(Integer id) {
        String sql = "DELETE FROM equipment_category WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("レッスンカテゴリーの削除に失敗しました", e);
        }
    }

    /**
     * 全ての器具カテゴリーを取得
     */
    public List<EquipmentCategory> findAll() {
        String sql = "SELECT * FROM equipment_category ORDER BY id";
        List<EquipmentCategory> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractEquipmentCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("すべてのレッスンカテゴリーの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * ResultSetからEquipmentCategoryオブジェクトを生成
     */
    private EquipmentCategory extractEquipmentCategory(ResultSet rs) throws SQLException {
        EquipmentCategory category = new EquipmentCategory();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setCreateTime(rs.getTimestamp("create_time"));
        category.setUpdateTime(rs.getTimestamp("update_time"));
        return category;
    }
}