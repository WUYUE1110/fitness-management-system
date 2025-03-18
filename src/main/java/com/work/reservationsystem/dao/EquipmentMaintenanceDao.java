package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.work.reservationsystem.entity.EquipmentMaintenance;
import com.work.reservationsystem.util.DBUtil;
import com.work.reservationsystem.vo.EquipmentMaintenanceVO;

public class EquipmentMaintenanceDao {

    /**
     * メンテナンスレコードの総数を取得
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM equipment_maintenance";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("進行記録数の取得に失敗しました", e);
        }
        return 0;
    }

    /**
     * ページングによるメンテナンスレコードの取得
     */
    public List<EquipmentMaintenanceVO> findPage(int offset, int pageSize) {
        String sql = "SELECT m.*, e.name as equipment_name, a.real_name as admin_name "
                   + "FROM equipment_maintenance m "
                   + "LEFT JOIN equipment e ON m.equipment_id = e.id "
                   + "LEFT JOIN admin a ON m.admin_id = a.id "
                   + "ORDER BY m.create_time DESC "
                   + "LIMIT ?, ?";
        List<EquipmentMaintenanceVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractMaintenanceVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("進行記録のページング取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 新しいメンテナンスレコードを追加
     */
    public void insert(EquipmentMaintenance maintenance) {
        String sql = "INSERT INTO equipment_maintenance (equipment_id, admin_id, description, status, create_time, update_time) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maintenance.getEquipmentId());
            stmt.setInt(2, maintenance.getAdminId());
            stmt.setString(3, maintenance.getDescription());
            stmt.setInt(4, 0); // デフォルトステータス：修理中
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(5, now);
            stmt.setTimestamp(6, now);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("進行レコードの追加に失敗しました", e);
        }
    }

    /**
     * 既存のメンテナンスレコードを更新
     */
    public void update(EquipmentMaintenance maintenance) {
        String sql = "UPDATE equipment_maintenance SET equipment_id = ?, description = ?, update_time = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, maintenance.getEquipmentId());
            stmt.setString(2, maintenance.getDescription());
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(4, maintenance.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("進行レコードの更新に失敗しました", e);
        }
    }

    /**
     * メンテナンスを完了扱いに更新
     */
    public void finish(Integer id) {
        String sql = "UPDATE equipment_maintenance SET status = 1, finish_time = ?, update_time = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stmt.setTimestamp(1, now);
            stmt.setTimestamp(2, now);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("進行レコードの完了更新に失敗しました", e);
        }
    }

    /**
     * メンテナンスレコードをIDで検索
     */
    public EquipmentMaintenanceVO findById(Integer id) {
        String sql = "SELECT m.*, e.name as equipment_name, a.real_name as admin_name "
                   + "FROM equipment_maintenance m "
                   + "LEFT JOIN equipment e ON m.equipment_id = e.id "
                   + "LEFT JOIN admin a ON m.admin_id = a.id "
                   + "WHERE m.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractMaintenanceVO(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("進行レコードの取得に失敗しました", e);
        }
        return null;
    }

    /**
     * ResultSetからEquipmentMaintenanceVOオブジェクトを抽出
     */
    private EquipmentMaintenanceVO extractMaintenanceVO(ResultSet rs) throws SQLException {
        EquipmentMaintenanceVO vo = new EquipmentMaintenanceVO();
        vo.setId(rs.getInt("id"));
        vo.setEquipmentId(rs.getInt("equipment_id"));
        vo.setEquipmentName(rs.getString("equipment_name"));
        vo.setAdminId(rs.getInt("admin_id"));
        vo.setAdminName(rs.getString("admin_name"));
        vo.setDescription(rs.getString("description"));
        vo.setStatus(rs.getInt("status"));
        vo.setFinishTime(rs.getTimestamp("finish_time"));
        vo.setCreateTime(rs.getTimestamp("create_time"));
        vo.setUpdateTime(rs.getTimestamp("update_time"));
        return vo;
    }
}