package com.work.reservationsystem.entity;

import java.util.Date;

public class Coach {
    private Integer id;
    private String name;         // 名前
    private Integer gender;      // 性別：0-女性 1-男性
    private String phone;        // 携帯番号
    private Integer equipmentId; // 関連する器具ID
    private Date hireDate;       // 入社日
    private Integer status;      // ステータス：0-退職 1-在職
    private String description;  // 自己紹介
    private String avatar;       // アバターのパス
    private String email;        // メールアドレス
    private Integer type;        // タイプ：1-臨時社員 2-正社員
    private Date createTime;     // 作成日時
    private Date updateTime;     // 更新日時

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", equipmentId=" + equipmentId +
                ", hireDate=" + hireDate +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}