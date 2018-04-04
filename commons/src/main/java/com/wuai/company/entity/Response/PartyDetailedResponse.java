package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/12/21.
 */
public class PartyDetailedResponse implements Serializable {
        private String uuid;
        private String topic;
        private String date;//日期
        private String time;//时间
        private Double money;//金额
        private Integer payCode;//订单状态
        private Integer boySize;//男生人数
        private Integer girlSize;//女生人数
        private String type;//订单状态
        private String nickName;//用户昵称




    public PartyDetailedResponse() {
    }

    public PartyDetailedResponse(String uuid, String topic, String date, String time, Double money, Integer payCode, Integer boySize, Integer girlSize, String type, String nickName) {
        this.uuid = uuid;
        this.topic = topic;
        this.date = date;
        this.time = time;
        this.money = money;
        this.payCode = payCode;
        this.boySize = boySize;
        this.girlSize = girlSize;
        this.type = type;
        this.nickName = nickName;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getPayCode() {
        return payCode;
    }

    public void setPayCode(Integer payCode) {
        this.payCode = payCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


