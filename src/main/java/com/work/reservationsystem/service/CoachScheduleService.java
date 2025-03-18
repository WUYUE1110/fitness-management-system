package com.work.reservationsystem.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.work.reservationsystem.dao.CoachScheduleDao;
import com.work.reservationsystem.dao.HelpScheduleDao;
import com.work.reservationsystem.entity.CoachSchedule;
import com.work.reservationsystem.vo.CoachScheduleVO;

public class CoachScheduleService {
    private final CoachScheduleDao scheduleDao = new CoachScheduleDao();
    private final HelpScheduleDao helpScheduleDao = new HelpScheduleDao();

    /**
     * 指定コーチID、日付範囲でスケジュールを検索
     */
    public List<CoachScheduleVO> findByCoachAndDateRange(Integer coachId, Date startDate, Date endDate) {
        if (coachId == null) {
            throw new RuntimeException("コーチIDは必須です");
        }
        if (startDate == null || endDate == null) {
            throw new RuntimeException("日付範囲は必須です");
        }
        if (startDate.after(endDate)) {
            throw new RuntimeException("開始日を終了日より後に設定することはできません");
        }

        return scheduleDao.findByCoachAndDateRange(coachId, startDate, endDate);
    }

    /**
     * 新しいスケジュールを追加
     */
    public void add(CoachSchedule schedule) {
        // パラメータ検証
        if (schedule.getCoachId() == null) {
            throw new RuntimeException("コーチIDは必須です");
        }
        if (schedule.getShiftId() == null) {
            throw new RuntimeException("シフトIDは必須です");
        }
        if (schedule.getScheduleDate() == null) {
            throw new RuntimeException("日付は必須です");
        }

        // 過去の日付を指定できないようにする
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (schedule.getScheduleDate().before(today.getTime())) {
            throw new RuntimeException("過去の日付を選択することはできません");
        }

        // 1ヶ月先までのみ登録可能
        Calendar oneMonthLater = Calendar.getInstance();
        oneMonthLater.add(Calendar.MONTH, 1);
        oneMonthLater.set(Calendar.HOUR_OF_DAY, 23);
        oneMonthLater.set(Calendar.MINUTE, 59);
        oneMonthLater.set(Calendar.SECOND, 59);
        oneMonthLater.set(Calendar.MILLISECOND, 999);

        if (schedule.getScheduleDate().after(oneMonthLater.getTime())) {
            throw new RuntimeException("1ヶ月以内のシフトしか登録できません");
        }

        // 既にスケジュールがあるかどうかをチェック
        if (scheduleDao.existsByCoachAndDate(schedule.getCoachId(), schedule.getScheduleDate())) {
            throw new RuntimeException("この日付には既にスケジュールが存在します");
        }

        // 初期ステータス設定
        schedule.setStatus(0);
        scheduleDao.insert(schedule);

        // もしHelpマークが登録されていれば削除
        helpScheduleDao.delete(schedule.getScheduleDate());
    }

    /**
     * スケジュールを確定状態に更新
     */
    public void confirm(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }

        CoachSchedule schedule = scheduleDao.findById(id);
        if (schedule == null) {
            throw new RuntimeException("スケジュールが存在しません");
        }

        if (schedule.getStatus() == 1) {
            throw new RuntimeException("このスケジュールはすでに確定済みです");
        }

        scheduleDao.updateStatus(id, 1);
    }

    /**
     * スケジュールをキャンセル
     */
    public void cancel(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }

        CoachSchedule schedule = scheduleDao.findById(id);
        if (schedule == null) {
            throw new RuntimeException("スケジュールが存在しません");
        }

        // 過去のスケジュールはキャンセルできないようにする
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (schedule.getScheduleDate().before(today.getTime())) {
            throw new RuntimeException("過去のスケジュールはキャンセルできません");
        }

        scheduleDao.delete(id);
    }

    /**
     * コーチIDと指定日付のスケジュールをキャンセル
     */
    public void cancelByCoachAndDate(Integer coachId, Date scheduleDate) {
        if (coachId == null) {
            throw new RuntimeException("コーチIDは必須です");
        }
        if (scheduleDate == null) {
            throw new RuntimeException("日付は必須です");
        }

        // 過去のスケジュールはキャンセルできないようにする
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (scheduleDate.before(today.getTime())) {
            throw new RuntimeException("過去のスケジュールはキャンセルできません");
        }

        scheduleDao.deleteByCoachAndDate(coachId, scheduleDate);
    }

    /**
     * 指定日付範囲でスケジュールを検索
     */
    public List<CoachScheduleVO> findByDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new RuntimeException("日付範囲は必須です");
        }
        if (startDate.after(endDate)) {
            throw new RuntimeException("開始日を終了日より後に設定することはできません");
        }
        return scheduleDao.findByDateRange(startDate, endDate);
    }

    /**
     * 指定日付のスケジュールを取得
     */
    public List<CoachScheduleVO> findByDate(Date date) {
        if (date == null) {
            throw new RuntimeException("日付は必須です");
        }
        return scheduleDao.findByDate(date);
    }

    /**
     * 指定日付にHelpマークを追加
     */
    public void addHelp(Date scheduleDate) {
        if (scheduleDate == null) {
            throw new RuntimeException("日付は必須です");
        }

        // 過去の日付にHelpを追加不可
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (scheduleDate.before(today.getTime())) {
            throw new RuntimeException("過去の日付にHelpを追加することはできません");
        }

        // Helpレコードを作成
        CoachSchedule schedule = new CoachSchedule();
        schedule.setCoachId(-1); // -1を使ってHelpを示す
        schedule.setShiftId(-1); // -1を使ってHelpを示す
        schedule.setScheduleDate(scheduleDate);
        schedule.setStatus(2);   // 2をHelpのステータスとして使用
        scheduleDao.insert(schedule);
    }

    /**
     * コーチIDと日付でスケジュールを検索
     */
    public List<CoachScheduleVO> findByCoachAndDate(Integer coachId, Date date) {
        if (coachId == null) {
            throw new RuntimeException("コーチIDは必須です");
        }
        if (date == null) {
            throw new RuntimeException("日付は必須です");
        }
        return scheduleDao.findByCoachAndDate(coachId, date);
    }

    /**
     * スケジュールIDでスケジュールを検索
     */
    public CoachSchedule findById(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }
        return scheduleDao.findById(id);
    }

    /**
     * スケジュールのステータスを更新
     */
    public void updateStatus(Integer id, Integer status) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }
        if (status == null) {
            throw new RuntimeException("ステータスは必須です");
        }
        scheduleDao.updateStatus(id, status);
    }

    /**
     * 指定されたコーチ・シフト・日付でスケジュールを割り当て
     */
    public void assignShift(Integer coachId, Integer shiftId, Date scheduleDate) {
        if (coachId == null) {
            throw new RuntimeException("コーチIDは必須です");
        }
        if (shiftId == null) {
            throw new RuntimeException("シフトIDは必須です");
        }
        if (scheduleDate == null) {
            throw new RuntimeException("日付は必須です");
        }

        // 過去の日付に対して割り当てできないようにする
        if (scheduleDate.before(new Date())) {
            throw new RuntimeException("過去の日付にスケジュールを割り当てることはできません");
        }

        // 既にスケジュールが存在する場合はエラー
        if (scheduleDao.existsByCoachAndDate(coachId, scheduleDate)) {
            throw new RuntimeException("このコーチは既に指定日付のスケジュールが存在します");
        }

        // スケジュールを新規作成
        CoachSchedule schedule = new CoachSchedule();
        schedule.setCoachId(coachId);
        schedule.setShiftId(shiftId);
        schedule.setScheduleDate(scheduleDate);
        schedule.setStatus(1); // 管理者指定のスケジュールはデフォルトで確定済み

        scheduleDao.insert(schedule);
    }
}