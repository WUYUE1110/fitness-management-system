package com.work.reservationsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.work.reservationsystem.entity.Coach;
import com.work.reservationsystem.util.DBUtil;
import com.work.reservationsystem.vo.CoachScheduleSimpleVO;
import com.work.reservationsystem.vo.CoachVO;

public class CoachDao {

    /**
     * コーチの総数を取得
     */
    public int count() {
        String sql = "SELECT COUNT(*) FROM coach";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチ総数の取得に失敗しました", e);
        }
        return 0;
    }

    /**
     * ページングに応じてコーチ一覧(VO)を取得
     */
    public List<CoachVO> findPage(int offset, int pageSize) {
        String sql = "SELECT c.*, e.name as equipment_name " +
                     "FROM coach c " +
                     "LEFT JOIN equipment e ON c.equipment_id = e.id " +
                     "ORDER BY c.id " +
                     "LIMIT ?, ?";
        List<CoachVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, offset);
            stmt.setInt(2, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCoachVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチのページング取得に失敗しました", e);
        }
        return list;
    }

    /**
     * コーチを新規追加
     */
    public void insert(Coach coach) {
        String sql = "INSERT INTO coach (name, gender, phone, email, equipment_id, hire_date, type, status, description, " +
                     "avatar, create_time, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, coach.getName());
            stmt.setInt(2, coach.getGender());
            stmt.setString(3, coach.getPhone());
            stmt.setString(4, coach.getEmail());
            stmt.setInt(5, coach.getEquipmentId());
            stmt.setDate(6, new java.sql.Date(coach.getHireDate().getTime()));
            stmt.setInt(7, coach.getType());
            stmt.setInt(8, coach.getStatus());
            stmt.setString(9, coach.getDescription());
            stmt.setString(10, coach.getAvatar());
            stmt.executeUpdate();
            
            // 生成されたIDを取得
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                coach.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチの追加に失敗しました", e);
        }
    }

    /**
     * IDによってコーチ(VO)を取得
     */
    public CoachVO findById(Integer id) {
        String sql = "SELECT c.*, e.name as equipment_name " +
                     "FROM coach c " +
                     "LEFT JOIN equipment e ON c.equipment_id = e.id " +
                     "WHERE c.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCoachVO(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチ情報の取得に失敗しました", e);
        }
        return null;
    }

    /**
     * コーチ情報を更新
     */
    public void update(Coach coach) {
        String sql = "UPDATE coach SET name = ?, gender = ?, phone = ?, email = ?, " +
                     "equipment_id = ?, hire_date = ?, type = ?, status = ?, " +
                     "description = ?, avatar = ?, update_time = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, coach.getName());
            stmt.setInt(2, coach.getGender());
            stmt.setString(3, coach.getPhone());
            stmt.setString(4, coach.getEmail());
            stmt.setInt(5, coach.getEquipmentId());
            stmt.setDate(6, new java.sql.Date(coach.getHireDate().getTime()));
            stmt.setInt(7, coach.getType());
            stmt.setInt(8, coach.getStatus());
            stmt.setString(9, coach.getDescription());
            stmt.setString(10, coach.getAvatar());
            stmt.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
            stmt.setInt(12, coach.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("コーチ情報の更新に失敗しました", e);
        }
    }

    /**
     * コーチを削除
     */
    public void delete(Integer id) {
        String sql = "DELETE FROM coach WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("コーチの削除に失敗しました", e);
        }
    }

    /**
     * 電話番号でコーチを検索
     */
    public Coach findByPhone(String phone) {
        String sql = "SELECT * FROM coach WHERE phone = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCoach(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチ情報の取得に失敗しました", e);
        }
        return null;
    }

    /**
     * 全コーチを取得
     */
    public List<Coach> findAll() {
        String sql = "SELECT * FROM coach ORDER BY name";
        List<Coach> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCoach(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチリストの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 器具IDに基づいてコーチを取得
     */
    public List<CoachVO> findByEquipmentId(Integer equipmentId) {
        String sql = "SELECT c.*, e.name as equipment_name FROM coach c " +
                     "LEFT JOIN equipment e ON c.equipment_id = e.id " +
                     "WHERE c.equipment_id = ? AND c.status = 1 " +
                     "ORDER BY c.name";
        List<CoachVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractCoachVO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチリストの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 指定日のスケジュール簡易情報を取得
     */
    public List<CoachScheduleSimpleVO> findScheduleByDate(Integer coachId, Date date) {
        String sql = "SELECT c.avatar, c.name as coach_name, s.name as shift_name, " +
                     "s.start_time, s.end_time, cs.status " +
                     "FROM coach_schedule cs " +
                     "JOIN coach c ON cs.coach_id = c.id " +
                     "JOIN shift s ON cs.shift_id = s.id " +
                     "WHERE cs.coach_id = ? AND DATE(cs.schedule_date) = DATE(?) " +
                     "ORDER BY s.start_time";
        
        List<CoachScheduleSimpleVO> list = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CoachScheduleSimpleVO vo = new CoachScheduleSimpleVO();
                    vo.setAvatar(rs.getString("avatar"));
                    vo.setCoachName(rs.getString("coach_name"));
                    vo.setShiftName(rs.getString("shift_name"));
                    vo.setStartTime(rs.getString("start_time"));
                    vo.setEndTime(rs.getString("end_time"));
                    vo.setStatus(rs.getInt("status"));
                    list.add(vo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチのスケジュール取得に失敗しました", e);
        }
        
        return list;
    }

    /**
     * 指定日付でページングされたコーチ一覧(VO)を取得
     */
    public List<CoachVO> findByDatePage(Date date, int offset, int pageSize) {
        String sql = "SELECT DISTINCT c.*, e.name as equipment_name " +
                     "FROM coach c " +
                     "LEFT JOIN equipment e ON c.equipment_id = e.id " +
                     "JOIN coach_schedule cs ON c.id = cs.coach_id " +
                     "WHERE c.status = 1 " +
                     "AND DATE(cs.schedule_date) = DATE(?) " +
                     "ORDER BY c.name LIMIT ?, ?";
        
        List<CoachVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            stmt.setInt(2, offset);
            stmt.setInt(3, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CoachVO vo = extractCoachVO(rs);
                vo.setSchedules(findScheduleByDate(vo.getId(), date));
                list.add(vo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチリストの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 指定日付に基づくコーチ数を取得
     */
    public int countByDate(Date date) {
        String sql = "SELECT COUNT(DISTINCT c.id) " +
                     "FROM coach c " +
                     "JOIN coach_schedule cs ON c.id = cs.coach_id " +
                     "WHERE c.status = 1 " +
                     "AND DATE(cs.schedule_date) = DATE(?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチ数の取得に失敗しました", e);
        }
        return 0;
    }

    /**
     * 指定の器具IDと日付でページングされたコーチ一覧(VO)を取得
     */
    public List<CoachVO> findByEquipmentIdAndDatePage(Integer equipmentId, Date date, int offset, int pageSize) {
        String sql = "SELECT DISTINCT c.*, e.name as equipment_name " +
                     "FROM coach c " +
                     "LEFT JOIN equipment e ON c.equipment_id = e.id " +
                     "JOIN coach_schedule cs ON c.id = cs.coach_id " +
                     "WHERE c.status = 1 " +
                     "AND c.equipment_id = ? " +
                     "AND DATE(cs.schedule_date) = DATE(?) " +
                     "ORDER BY c.name LIMIT ?, ?";
        
        List<CoachVO> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipmentId);
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            stmt.setInt(3, offset);
            stmt.setInt(4, pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CoachVO vo = extractCoachVO(rs);
                vo.setSchedules(findScheduleByDate(vo.getId(), date));
                list.add(vo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチリストの取得に失敗しました", e);
        }
        return list;
    }

    /**
     * 指定の器具IDと日付に基づくコーチ数を取得
     */
    public int countByEquipmentIdAndDate(Integer equipmentId, Date date) {
        String sql = "SELECT COUNT(DISTINCT c.id) " +
                     "FROM coach c " +
                     "JOIN coach_schedule cs ON c.id = cs.coach_id " +
                     "WHERE c.status = 1 " +
                     "AND c.equipment_id = ? " +
                     "AND DATE(cs.schedule_date) = DATE(?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, equipmentId);
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチ数の取得に失敗しました", e);
        }
        return 0;
    }

    /**
     * コーチのログイン (メールアドレス + パスワード)
     */
    public Coach login(String username, String password) {
        String sql = "SELECT * FROM coach WHERE email = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCoach(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチ情報の取得に失敗しました", e);
        }
        return null;
    }

    /**
     * メールアドレスからコーチを取得
     */
    public Coach findByEmail(String email) {
        String sql = "SELECT * FROM coach WHERE email = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractCoach(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチ情報の取得に失敗しました", e);
        }
        return null;
    }

    /**
     * ResultSetからCoachVOオブジェクトを抽出
     */
    private CoachVO extractCoachVO(ResultSet rs) throws SQLException {
        CoachVO vo = new CoachVO();
        vo.setId(rs.getInt("id"));
        vo.setName(rs.getString("name"));
        vo.setGender(rs.getInt("gender"));
        vo.setPhone(rs.getString("phone"));
        vo.setEmail(rs.getString("email"));
        vo.setEquipmentId(rs.getInt("equipment_id"));
        vo.setEquipmentName(rs.getString("equipment_name"));
        vo.setHireDate(rs.getDate("hire_date"));
        vo.setType(rs.getInt("type"));
        vo.setStatus(rs.getInt("status"));
        vo.setDescription(rs.getString("description"));
        vo.setAvatar(rs.getString("avatar"));
        vo.setCreateTime(rs.getTimestamp("create_time"));
        vo.setUpdateTime(rs.getTimestamp("update_time"));
        return vo;
    }

    /**
     * ResultSetからCoachエンティティを抽出
     */
    private Coach extractCoach(ResultSet rs) throws SQLException {
        Coach coach = new Coach();
        coach.setId(rs.getInt("id"));
        coach.setName(rs.getString("name"));
        coach.setGender(rs.getInt("gender"));
        coach.setPhone(rs.getString("phone"));
        coach.setEmail(rs.getString("email"));
        coach.setEquipmentId(rs.getInt("equipment_id"));
        coach.setHireDate(rs.getDate("hire_date"));
        coach.setType(rs.getInt("type"));
        coach.setStatus(rs.getInt("status"));
        coach.setDescription(rs.getString("description"));
        coach.setAvatar(rs.getString("avatar"));
        coach.setCreateTime(rs.getTimestamp("create_time"));
        coach.setUpdateTime(rs.getTimestamp("update_time"));
        return coach;
    }
}