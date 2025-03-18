package com.work.reservationsystem.service;

import java.math.BigDecimal;
import java.util.List;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.dao.ShiftDao;
import com.work.reservationsystem.entity.Shift;

public class ShiftService {
    private final ShiftDao shiftDao = new ShiftDao();

    /**
     * シフトの総数を取得
     */
    public int count() {
        return shiftDao.count();
    }

    /**
     * ページングに応じてシフト一覧を取得
     */
    public List<Shift> findPage(int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }
        
        int offset = (pageNum - 1) * pageSize;
        return shiftDao.findPage(offset, pageSize);
    }

    /**
     * 新しいシフトを追加
     */
    public void add(Shift shift) {
        // パラメータ検証
        if (shift.getName() == null || shift.getName().trim().isEmpty()) {
            throw new RuntimeException("シフト名は空にできません");
        }
        if (shift.getStartTime() == null) {
            throw new RuntimeException("開始時刻は必須です");
        }
        if (shift.getEndTime() == null) {
            throw new RuntimeException("終了時刻は必須です");
        }
        if (shift.getSalary() == null || shift.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("給与は0より大きい必要があります");
        }
        if (shift.getEndTime().before(shift.getStartTime())) {
            throw new RuntimeException("終了時刻を開始時刻より前に設定することはできません");
        }

        shiftDao.insert(shift);
    }

    /**
     * 既存のシフトを更新
     */
    public void update(Shift shift) {
        // パラメータ検証
        if (shift.getId() == null) {
            throw new RuntimeException("IDは必須です");
        }
        if (shift.getName() == null || shift.getName().trim().isEmpty()) {
            throw new RuntimeException("シフト名は空にできません");
        }
        if (shift.getStartTime() == null) {
            throw new RuntimeException("開始時刻は必須です");
        }
        if (shift.getEndTime() == null) {
            throw new RuntimeException("終了時刻は必須です");
        }
        if (shift.getSalary() == null || shift.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("給与は0より大きい必要があります");
        }
        if (shift.getEndTime().before(shift.getStartTime())) {
            throw new RuntimeException("終了時刻を開始時刻より前に設定することはできません");
        }

        // レコードが存在するか確認
        if (findById(shift.getId()) == null) {
            throw new RuntimeException("変更対象のシフトが存在しません");
        }

        shiftDao.update(shift);
    }

    /**
     * シフトを削除
     */
    public void delete(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }

        // レコードが存在するか確認
        if (findById(id) == null) {
            throw new RuntimeException("削除対象のシフトが存在しません");
        }

        shiftDao.delete(id);
    }

    /**
     * IDによりシフトを検索
     */
    public Shift findById(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }
        return shiftDao.findById(id);
    }

    /**
     * 全シフトを取得
     */
    public List<Shift> findAll() {
        return shiftDao.findAll();
    }
}