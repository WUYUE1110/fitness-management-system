package com.work.reservationsystem.entity;

import java.util.Date;

public class HelpSchedule {
    private Integer id;
    private Date scheduleDate;    // Helpが必要な日付
    private Date createTime;      // 作成日時

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
} 