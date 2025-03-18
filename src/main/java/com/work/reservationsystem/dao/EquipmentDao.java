package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.work.reservationsystem.entity.Equipment;
import com.work.reservationsystem.util.DBUtil;
import com.work.reservationsystem.vo.EquipmentVO;

public class EquipmentDao {
    
    /**
     * 器具の総数を取得
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM equipment";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("レッスンの総数取得に失敗しました", e);
        }
        return 0;
    }

    /**
     * ページングで器具情報(VO)を取得
     */
    public List<EquipmentVO> findPage(int offset, int pageSize) {
        String sql = "SELECT e.*, c.name as category_name " +
                     "FROM equipment e " +
                     "LEFT JOIN equipment_category c ON e.category_id = c.id " +
                     "LIMIT ?, ?";
        List<EquipmentVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractEquipmentVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("レッスンのページング取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 器具を新規追加
     */
    public void insert(Equipment equipment) {
        String sql = "INSERT INTO equipment (name, category_id, description, status, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipment.getName());
            stmt.setInt(2, equipment.getCategoryId());
            stmt.setString(3, equipment.getDescription());
            stmt.setInt(4, equipment.getStatus());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(5, now);
            stmt.setTimestamp(6, now);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("レッスンの追加に失敗しました", e);
        }
    }

    /**
     * IDで器具情報(VO)を検索
     */
    public EquipmentVO findById(Integer id) {
        String sql = "SELECT e.*, c.name as category_name " +
                     "FROM equipment e " +
                     "LEFT JOIN equipment_category c ON e.category_id = c.id " +
                     "WHERE e.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractEquipmentVO(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("レッスンの取得に失敗しました", e);
        }
        return null;
    }

    /**
     * 器具を更新
     */
    public void update(Equipment equipment) {
        String sql = "UPDATE equipment SET name = ?, category_id = ?, description = ?, status = ?, update_time = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, equipment.getName());
            stmt.setInt(2, equipment.getCategoryId());
            stmt.setString(3, equipment.getDescription());
            stmt.setInt(4, equipment.getStatus());
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(6, equipment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("レッスンの更新に失敗しました", e);
        }
    }

    /**
     * 器具を削除
     */
    public void delete(Integer id) {
        String sql = "DELETE FROM equipment WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("レッスンの削除に失敗しました", e);
        }
    }

    /**
     * 全ての使用可能な器具を取得 (status=1)
     */
    public List<Equipment> findAll() {
        String sql = "SELECT * FROM equipment WHERE status = 1 ORDER BY id";
        List<Equipment> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Equipment equipment = new Equipment();
                equipment.setId(rs.getInt("id"));
                equipment.setName(rs.getString("name"));
                equipment.setCategoryId(rs.getInt("category_id"));
                equipment.setDescription(rs.getString("description"));
                equipment.setStatus(rs.getInt("status"));
                equipment.setCreateTime(rs.getTimestamp("create_time"));
                equipment.setUpdateTime(rs.getTimestamp("update_time"));
                list.add(equipment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("実施可能なレッスンの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * ResultSetからEquipmentVOオブジェクトを抽出
     */
    private EquipmentVO extractEquipmentVO(ResultSet rs) throws SQLException {
        EquipmentVO vo = new EquipmentVO();
        vo.setId(rs.getInt("id"));
        vo.setName(rs.getString("name"));
        vo.setCategoryId(rs.getInt("category_id"));
        vo.setCategoryName(rs.getString("category_name"));
        vo.setDescription(rs.getString("description"));
        vo.setStatus(rs.getInt("status"));
        vo.setCreateTime(rs.getTimestamp("create_time"));
        vo.setUpdateTime(rs.getTimestamp("update_time"));
        return vo;
    }
}