package com.work.reservationsystem.vo;

public class CoachScheduleSimpleVO {
    private Integer id;           // スケジュールID
    private String avatar;        // トレーナーアバター
    private String coachName;     // トレーナー名
    private String shiftName;     // シフト名
    private String startTime;     // 開始時間
    private String endTime;       // 終了時間
    private Integer status;       // ステータス：0-未確認 1-確認済み

    // getter及びsetter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}