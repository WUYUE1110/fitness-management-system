package com.work.reservationsystem.vo;

import java.util.Date;

public class EquipmentMaintenanceVO {
    private Integer id;
    private Integer equipmentId;  // 設備ID
    private String equipmentName; // 設備名
    private Integer adminId;      // 登録管理者ID
    private String adminName;     // 管理者名
    private String description;   // メンテナンス内容
    private Integer status;       // ステータス：0-修理中 1-完了
    private Date finishTime;      // 完了日時
    private Date createTime;      // 作成日時
    private Date updateTime;      // 更新日時

    // getterとsetter
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

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
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
        return "EquipmentMaintenanceVO{" +
                "id=" + id +
                ", equipmentId=" + equipmentId +
                ", equipmentName='" + equipmentName + '\'' +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", finishTime=" + finishTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 