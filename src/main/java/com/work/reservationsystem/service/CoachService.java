package com.work.reservationsystem.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.dao.CoachDao;
import com.work.reservationsystem.entity.Coach;
import com.work.reservationsystem.util.DBUtil;
import com.work.reservationsystem.vo.CoachScheduleSimpleVO;
import com.work.reservationsystem.vo.CoachVO;

public class CoachService {
    private final CoachDao coachDao = new CoachDao();
    private final EquipmentService equipmentService = new EquipmentService();

    // 電話番号用の正規表現
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9-]{10,13}$");

    /**
     * コーチの総数を取得
     */
    public int count() {
        return coachDao.count();
    }

    /**
     * ページングでコーチのリストを取得
     */
    public List<CoachVO> findPage(int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }

        int offset = (pageNum - 1) * pageSize;
        return coachDao.findPage(offset, pageSize);
    }

    /**
     * 新しいコーチを追加
     */
    public void add(Coach coach) {
        // パラメータ検証
        validateCoach(coach);

        // 器具が存在するか確認
        if (coach.getEquipmentId() != null && equipmentService.findById(coach.getEquipmentId()) == null) {
            throw new RuntimeException("選択したレッスンは存在しません");
        }

        coachDao.insert(coach);
    }

    /**
     * コーチのIDをもとに情報を取得 (VO)
     */
    public CoachVO findById(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }
        return coachDao.findById(id);
    }

    /**
     * コーチ情報を更新
     */
    public void update(Coach coach) {
        // パラメータ検証
        if (coach.getId() == null) {
            throw new RuntimeException("IDは必須です");
        }
        validateCoach(coach);

        // レコードが存在するか確認
        if (findById(coach.getId()) == null) {
            throw new RuntimeException("更新対象のコーチは存在しません");
        }

        // 器具が存在するか確認
        if (coach.getEquipmentId() != null && equipmentService.findById(coach.getEquipmentId()) == null) {
            throw new RuntimeException("選択したレッスンは存在しません");
        }

        coachDao.update(coach);
    }

    /**
     * コーチを削除
     */
    public void delete(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }

        // レコードが存在するか確認
        if (findById(id) == null) {
            throw new RuntimeException("削除対象のコーチは存在しません");
        }

        coachDao.delete(id);
    }

    /**
     * 電話番号でコーチを検索
     */
    public Coach findByPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new RuntimeException("電話番号は必須です");
        }
        return coachDao.findByPhone(phone);
    }

    /**
     * 全コーチを取得
     */
    public List<Coach> findAll() {
        return coachDao.findAll();
    }

    /**
     * コーチのパラメータを検証
     */
    private void validateCoach(Coach coach) {
        if (coach.getName() == null || coach.getName().trim().isEmpty()) {
            throw new RuntimeException("名前は必須です");
        }
        if (coach.getGender() == null || (coach.getGender() != 0 && coach.getGender() != 1)) {
            throw new RuntimeException("正しい性別を選択してください");
        }
        if (coach.getPhone() == null || !PHONE_PATTERN.matcher(coach.getPhone()).matches()) {
            throw new RuntimeException("正しい電話番号を入力してください");
        }
        if (coach.getEmail() == null || coach.getEmail().trim().isEmpty()) {
            throw new RuntimeException("メールアドレスは必須です");
        }
        if (coach.getHireDate() == null) {
            throw new RuntimeException("入社日を入力してください");
        }
        if (coach.getType() == null || (coach.getType() != 1 && coach.getType() != 2)) {
            throw new RuntimeException("正しい従業員タイプを選択してください");
        }
        if (coach.getStatus() == null || (coach.getStatus() != 0 && coach.getStatus() != 1)) {
            throw new RuntimeException("正しいステータスを選択してください");
        }
        if (coach.getDescription() == null || coach.getDescription().trim().isEmpty()) {
            throw new RuntimeException("自己紹介を入力してください");
        }
    }

    /**
     * 器具IDによるコーチリストを取得
     */
    public List<CoachVO> findByEquipmentId(Integer equipmentId) {
        return coachDao.findByEquipmentId(equipmentId);
    }

    /**
     * 指定日付とページングでコーチリストを取得
     */
    public List<CoachVO> findByDatePage(Date date, int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }

        int offset = (pageNum - 1) * pageSize;
        return coachDao.findByDatePage(date, offset, pageSize);
    }

    /**
     * 指定日付のコーチ数をカウント
     */
    public int countByDate(Date date) {
        return coachDao.countByDate(date);
    }

    /**
     * 器具IDと日付でページングしてコーチリストを取得
     */
    public List<CoachVO> findByEquipmentIdAndDatePage(Integer equipmentId, Date date, int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }

        int offset = (pageNum - 1) * pageSize;
        return coachDao.findByEquipmentIdAndDatePage(equipmentId, date, offset, pageSize);
    }

    /**
     * 器具IDと日付によるコーチ数をカウント
     */
    public int countByEquipmentIdAndDate(Integer equipmentId, Date date) {
        return coachDao.countByEquipmentIdAndDate(equipmentId, date);
    }

    /**
     * 指定コーチと日付のスケジュール簡易情報を取得
     */
    public List<CoachScheduleSimpleVO> findScheduleByDate(Integer coachId, Date date) {
        if (coachId == null) {
            throw new RuntimeException("コーチIDは必須です");
        }
        if (date == null) {
            throw new RuntimeException("日付は必須です");
        }

        String sql = "SELECT cs.id, s.name AS shift_name, s.start_time, s.end_time, " +
                     "cs.status " +
                     "FROM coach_schedule cs " +
                     "JOIN shift s ON cs.shift_id = s.id " +
                     "WHERE cs.coach_id = ? " +
                     "AND DATE(cs.schedule_date) = DATE(?) " +
                     "ORDER BY s.start_time";

        List<CoachScheduleSimpleVO> schedules = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, coachId);
            stmt.setDate(2, new java.sql.Date(date.getTime()));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CoachScheduleSimpleVO vo = new CoachScheduleSimpleVO();
                vo.setId(rs.getInt("id"));
                vo.setShiftName(rs.getString("shift_name"));
                vo.setStartTime(rs.getString("start_time"));
                vo.setEndTime(rs.getString("end_time"));
                vo.setStatus(rs.getInt("status"));
                schedules.add(vo);
            }
        } catch (SQLException e) {
            throw new RuntimeException("コーチのスケジュールを取得中にエラーが発生しました", e);
        }

        return schedules;
    }

    /**
     * メールアドレスでコーチを検索
     */
    public Coach findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("メールアドレスは必須です");
        }
        return coachDao.findByEmail(email);
    }
}