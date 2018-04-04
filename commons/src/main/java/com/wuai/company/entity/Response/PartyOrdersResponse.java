package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/12/20.
 */
public class PartyOrdersResponse implements Serializable {
    private String uuid;
    private String time;
    private String date;
    private String money;
    private String topic;
    private String phoneNum;
    private String partyUuid;
    private Integer userId;
    private String storePhone;
    private Double endTime;
    private Integer payCode;
    private Integer boySize;
    private Integer girlSize;

    public PartyOrdersResponse() {
    }

    public PartyOrdersResponse(String uuid, String time, String date, String money, String topic, String phoneNum, String partyUuid, Integer userId, String storePhone, Double endTime, Integer payCode, Integer boySize, Integer girlSize) {
        this.uuid = uuid;
        this.time = time;
        this.date = date;
        this.money = money;
        this.topic = topic;
        this.phoneNum = phoneNum;
        this.partyUuid = partyUuid;
        this.userId = userId;
        this.storePhone = storePhone;
        this.endTime = endTime;
        this.payCode = payCode;
        this.boySize = boySize;
        this.girlSize = girlSize;
    }

    public Integer getBoySize() {
        return boySize;
    }

    public void setBoySize(Integer boySize) {
        this.boySize = boySize;
    }

    public Integer getGirlSize() {
        return girlSize;
    }

    public void setGirlSize(Integer girlSize) {
        this.girlSize = girlSize;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPartyUuid() {
        return partyUuid;
    }

    public void setPartyUuid(String partyUuid) {
        this.partyUuid = partyUuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public Integer getPayCode() {
        return payCode;
    }

    public void setPayCode(Integer payCode) {
        this.payCode = payCode;
    }
}
