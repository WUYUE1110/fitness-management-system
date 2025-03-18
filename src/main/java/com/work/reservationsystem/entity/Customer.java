package com.work.reservationsystem.entity;

import java.util.Date;

public class Customer {
    private Integer id;
    private String name;            // 顧客名
    private String phone;           // 電話番号
    private Integer gender;         // 性別 0-女性 1-男性
    private Integer age;            // 年齢
    private Integer memberFlag;     // 会員フラグ 0-いいえ 1-はい
    private Date memberExpireTime;  // 会員有効期限
    private Date createTime;        // 作成日時
    private Date updateTime;        // 更新日時
    private String password;        // ログインパスワード
    private String email;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getMemberFlag() {
        return memberFlag;
    }

    public void setMemberFlag(Integer memberFlag) {
        this.memberFlag = memberFlag;
    }

    public Date getMemberExpireTime() {
        return memberExpireTime;
    }

    public void setMemberExpireTime(Date memberExpireTime) {
        this.memberExpireTime = memberExpireTime;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", memberFlag=" + memberFlag +
                ", memberExpireTime=" + memberExpireTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}