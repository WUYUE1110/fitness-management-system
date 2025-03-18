package com.work.reservationsystem.entity;

import java.util.Date;

public class EquipmentMaintenance {
    private Integer id;private Integer equipmentId;   // 器具ID
    private Integer adminId;       // 登録管理者ID
    private String description;    // 修理内容
    private Integer status;        // ステータス：0-修理中 1-完了
    private Date finishTime;       // 完了日時
    private Date createTime;       // 作成日時
    private Date updateTime;       // 更新日時
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "EquipmentMaintenance{" +
                "id=" + id +
                ", equipmentId=" + equipmentId +
                ", adminId=" + adminId +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", finishTime=" + finishTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 