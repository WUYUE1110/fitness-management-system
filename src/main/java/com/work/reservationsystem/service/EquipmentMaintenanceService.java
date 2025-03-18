package com.work.reservationsystem.service;

import java.util.List;

import com.work.reservationsystem.constant.PageConstant;
import com.work.reservationsystem.dao.EquipmentMaintenanceDao;
import com.work.reservationsystem.entity.EquipmentMaintenance;
import com.work.reservationsystem.vo.EquipmentMaintenanceVO;

public class EquipmentMaintenanceService {
    private final EquipmentMaintenanceDao maintenanceDao = new EquipmentMaintenanceDao();
    private final EquipmentService equipmentService = new EquipmentService();

    /**
     * メンテナンスレコードの総数を取得
     */
    public int count() {
        return maintenanceDao.count();
    }

    /**
     * 指定されたページ番号・ページサイズでメンテナンスレコードを取得
     */
    public List<EquipmentMaintenanceVO> findPage(int pageNum, int pageSize) {
        if (pageNum < 1) {
            pageNum = PageConstant.DEFAULT_PAGE_NUM;
        }
        if (pageSize < 1) {
            pageSize = PageConstant.DEFAULT_PAGE_SIZE;
        }

        int offset = (pageNum - 1) * pageSize;
        return maintenanceDao.findPage(offset, pageSize);
    }

    /**
     * 新しいメンテナンス情報を追加
     */
    public void add(EquipmentMaintenance maintenance) {
        // パラメータ検証
        if (maintenance.getEquipmentId() == null) {
            throw new RuntimeException("レッスンを選択してください");
        }
        if (maintenance.getDescription() == null || maintenance.getDescription().trim().isEmpty()) {
            throw new RuntimeException("レッスン内容を入力してください");
        }

        // 器具が存在するか確認
        if (equipmentService.findById(maintenance.getEquipmentId()) == null) {
            throw new RuntimeException("選択されたレッスンは存在しません");
        }

        maintenanceDao.insert(maintenance);
    }

    /**
     * 既存のメンテナンス情報を更新
     */
    public void update(EquipmentMaintenance maintenance) {
        // パラメータ検証
        if (maintenance.getId() == null) {
            throw new RuntimeException("IDは必須です");
        }
        if (maintenance.getEquipmentId() == null) {
            throw new RuntimeException("レッスンを選択してください");
        }
        if (maintenance.getDescription() == null || maintenance.getDescription().trim().isEmpty()) {
            throw new RuntimeException("レッスン内容を入力してください");
        }

        // レコードが存在するか確認
        if (findById(maintenance.getId()) == null) {
            throw new RuntimeException("実施対象のレッスンは存在しません");
        }

        // 器具が存在するか確認
        if (equipmentService.findById(maintenance.getEquipmentId()) == null) {
            throw new RuntimeException("選択されたレッスンは存在しません");
        }

        maintenanceDao.update(maintenance);
    }

    /**
     * メンテナンスを完了状態に変更
     */
    public void finish(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }

        // レコードが存在するか確認
        EquipmentMaintenanceVO maintenance = findById(id);
        if (maintenance == null) {
            throw new RuntimeException("実施対象のレッスンは存在しません");
        }

        // ステータスチェック
        if (maintenance.getStatus() == 1) {
            throw new RuntimeException("このレッスンはすでに開始しています");
        }

        maintenanceDao.finish(id);
    }

    /**
     * IDによりメンテナンスレコードを取得
     */
    public EquipmentMaintenanceVO findById(Integer id) {
        if (id == null) {
            throw new RuntimeException("IDは必須です");
        }
        return maintenanceDao.findById(id);
    }
}