package com.work.reservationsystem.service;

import java.util.Date;
import java.util.List;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.dao.AppointmentDao;
import com.work.reservationsystem.entity.Appointment;
import com.work.reservationsystem.vo.AppointmentVO;

public class AppointmentService {
    private final AppointmentDao appointmentDao = new AppointmentDao();

    /**
     * 予約の総数を取得
     */
    public int count() {
        return appointmentDao.count();
    }

    /**
     * ページングに応じて予約情報を取得
     */
    public List<AppointmentVO> findPage(int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }

        int offset = (pageNum - 1) * pageSize;
        return appointmentDao.findPage(offset, pageSize);
    }

    /**
     * 予約IDにより予約情報(VO)を検索
     */
    public AppointmentVO findById(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }
        return appointmentDao.findById(id);
    }

    /**
     * 顧客IDにより予約情報を取得
     */
    public List<AppointmentVO> findByCustomerId(Integer customerId) {
        if (customerId == null) {
            throw new RuntimeException("会員IDは必須です");
        }
        return appointmentDao.findByCustomerId(customerId);
    }

    /**
     * 予約を作成
     */
    public void makeAppointment(Appointment appointment) {
        // パラメータ検証
        if (appointment.getCustomerId() == null) {
            throw new RuntimeException("顧客IDは必須です");
        }
        if (appointment.getCoachId() == null) {
            throw new RuntimeException("コーチIDは必須です");
        }
        if (appointment.getStartTime() == null) {
            throw new RuntimeException("開始時刻は必須です");
        }
        if (appointment.getEndTime() == null) {
            throw new RuntimeException("終了時刻は必須です");
        }
        if (appointment.getEquipmentId() == null) {
            throw new RuntimeException("レッスンIDは必須です");
        }

        // 時間の妥当性検証
        if (appointment.getStartTime().after(appointment.getEndTime())) {
            throw new RuntimeException("開始時刻を終了時刻より後に設定することはできません");
        }

        // 過去の時間かどうかをチェック
        if (appointment.getStartTime().before(new Date())) {
            throw new RuntimeException("過去の時間帯を予約することはできません");
        }

        // 重複予約のチェック（同じ顧客・時間帯）
        if (appointmentDao.existsByCustomerAndTime(
            appointment.getCustomerId(),
            appointment.getStartTime(),
            appointment.getEndTime()
        )) {
            throw new RuntimeException("この時間帯はすでに予約済みです");
        }

        // コーチが他の予約を持っていないかチェック
        if (appointmentDao.existsByCoachAndTime(
            appointment.getCoachId(),
            appointment.getStartTime(),
            appointment.getEndTime()
        )) {
            throw new RuntimeException("この時間帯にはコーチの別の予約が既に存在します");
        }

        // 予約を作成
        appointmentDao.insert(appointment);
    }
}