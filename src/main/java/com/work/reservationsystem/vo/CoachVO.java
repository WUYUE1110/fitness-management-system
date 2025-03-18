package com.work.reservationsystem.vo;

import java.util.Date;
import java.util.List;

public class CoachVO {
    private Integer id;
    private String name;        // 氏名
    private Integer gender;     // 性別：0-女性 1-男性
    private String phone;       // 電話番号
    private Integer equipmentId;// 設備ID
    private String equipmentName; // 設備名
    private Date hireDate;     // 入社日
    private Integer status;     // ステータス：0-退職 1-在職
    private String description; // プロフィール
    private String avatar;      // アバター画像パス
    private Date createTime;    // 作成日時
    private Date updateTime;    // 更新日時
    private String email;
    private Integer type;
    private List<CoachScheduleSimpleVO> schedules; // 排班信息

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public List<CoachScheduleSimpleVO> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<CoachScheduleSimpleVO> schedules) {
        this.schedules = schedules;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CoachVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", equipmentId=" + equipmentId +
                ", equipmentName='" + equipmentName + '\'' +
                ", hireDate=" + hireDate +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", schedules=" + schedules +
                '}';
    }
}