package com.wuai.company.pms.entity.response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/12/27.
 */
public class PartiesResponse implements Serializable {
    private String uuid;
    private String topic;
    private String pictures;
    private String video;
    private Double boyMoney;
    private Double girlMoney;
    private String date;//日期
    private String time;//时间
    private String week;//周
    private Integer minStart;//最小开始人数
    private Double endTime;//截止最迟报名时间
    private String icon;//头像
    private String username;//用户名
    private String content;// 店铺简介内容
    private String phoneNum; //电话
    private Integer type;  //图片标记
    private Integer timeType; //时间标记
    private String classify; //分类
    private String address; //地址
    private String cxamineAndVerify; //审核
    private String note; //理由
    private Double longitude ; //经度
    private Double latitude ; //纬度

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Double getBoyMoney() {
        return boyMoney;
    }

    public void setBoyMoney(Double boyMoney) {
        this.boyMoney = boyMoney;
    }

    public Double getGirlMoney() {
        return girlMoney;
    }

    public void setGirlMoney(Double girlMoney) {
        this.girlMoney = girlMoney;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public Integer getMinStart() {
        return minStart;
    }

    public void setMinStart(Integer minStart) {
        this.minStart = minStart;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCxamineAndVerify() {
        return cxamineAndVerify;
    }

    public void setCxamineAndVerify(String cxamineAndVerify) {
        this.cxamineAndVerify = cxamineAndVerify;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
