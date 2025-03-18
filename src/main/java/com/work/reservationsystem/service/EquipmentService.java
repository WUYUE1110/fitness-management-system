package com.work.reservationsystem.service;

import java.util.List;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.dao.EquipmentDao;
import com.work.reservationsystem.entity.Equipment;
import com.work.reservationsystem.vo.EquipmentVO;

public class EquipmentService {
    private final EquipmentDao equipmentDao = new EquipmentDao();
    private final EquipmentCategoryService categoryService = new EquipmentCategoryService();

    /**
     * 器具の総数を取得
     */
    public int count() {
        return equipmentDao.count();
    }

    /**
     * ページングに応じて器具一覧(VO)を取得
     */
    public List<EquipmentVO> findPage(int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }
        
        int offset = (pageNum - 1) * pageSize;
        return equipmentDao.findPage(offset, pageSize);
    }

    /**
     * 新しい器具を追加
     */
    public void add(Equipment equipment) {
        // パラメータ検証
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new RuntimeException("レッスン名は空にできません");
        }
        if (equipment.getCategoryId() == null) {
            throw new RuntimeException("レッスンカテゴリーを選択してください");
        }
        if (equipment.getDescription() == null || equipment.getDescription().trim().isEmpty()) {
            throw new RuntimeException("レッスンの説明は空にできません");
        }
        if (equipment.getStatus() == null) {
            equipment.setStatus(1); // デフォルトは使用可能
        }

        // カテゴリーが存在するか確認
        if (categoryService.findById(equipment.getCategoryId()) == null) {
            throw new RuntimeException("選択したカテゴリーは存在しません");
        }

        equipmentDao.insert(equipment);
    }

    /**
     * IDにより器具を取得 (VO形式)
     */
    public EquipmentVO findById(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }
        return equipmentDao.findById(id);
    }

    /**
     * 器具情報を更新
     */
    public void update(Equipment equipment) {
        // パラメータ検証
        if (equipment.getId() == null) {
            throw new RuntimeException("IDは必須です");
        }
        if (equipment.getName() == null || equipment.getName().trim().isEmpty()) {
            throw new RuntimeException("レッスン名は空にできません");
        }
        if (equipment.getCategoryId() == null) {
            throw new RuntimeException("レッスンカテゴリーを選択してください");
        }
        if (equipment.getDescription() == null || equipment.getDescription().trim().isEmpty()) {
            throw new RuntimeException("レッスンの説明は空にできません");
        }
        if (equipment.getStatus() == null) {
            throw new RuntimeException("レッスンの状態を選択してください");
        }

        // カテゴリーが存在するか確認
        if (categoryService.findById(equipment.getCategoryId()) == null) {
            throw new RuntimeException("選択したカテゴリーは存在しません");
        }

        // レコードが存在するかどうかをチェック
        if (findById(equipment.getId()) == null) {
            throw new RuntimeException("更新対象のレッスンが存在しません");
        }

        equipmentDao.update(equipment);
    }

    /**
     * 器具を削除
     */
    public void delete(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }

        // レコードが存在するかどうかをチェック
        if (findById(id) == null) {
            throw new RuntimeException("削除対象のレッスンが存在しません");
        }

        equipmentDao.delete(id);
    }

    /**
     * 全ての器具を取得
     */
    public List<Equipment> findAll() {
        return equipmentDao.findAll();
    }
}