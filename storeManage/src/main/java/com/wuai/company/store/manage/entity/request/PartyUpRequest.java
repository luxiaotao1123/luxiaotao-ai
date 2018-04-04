package com.wuai.company.store.manage.entity.request;

import java.io.Serializable;

/**
 * Created by hyf on 2017/12/19.
 */
public class PartyUpRequest implements Serializable {
    private String uuid;
    private String topic;
    private Double boyMoney;
    private Double girlMoney;
    private Integer minStart;
    private String date;
    private String time;
    private String week;
    private Double endTime;
    private String content;
    private String pictures;
    private String video;
    private String classify;
    private String icon;
    private String userName;
    private String phoneNum;
    private String name;
    private String cxamineAndVerify;//审核
    private String address;
    private Integer type;
    private Double longitude;
    private Double latitude;


    public String getCxamineAndVerify() {
        return cxamineAndVerify;
    }

    public void setCxamineAndVerify(String cxamineAndVerify) {
        this.cxamineAndVerify = cxamineAndVerify;
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

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

    public Integer getMinStart() {
        return minStart;
    }

    public void setMinStart(Integer minStart) {
        this.minStart = minStart;
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

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
