package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/12/12.
 */
public class ActiveOrdersResponse implements Serializable {
    private String uuid;//活动订单uuid
    private String topic;//标题
    private Double money;//金额
    private String nickName;//收货名称
    private String phoneNum;//用户手机号
    private String upPhoneNum;//上级用户手机号
    private String expressNum ;//快递编号
    private String expressName ;//快递名称
    private String province ;//
    private String city ;//
    private String address ;//地址
    private Integer send ;//状态
    private String time ;//时间

    public ActiveOrdersResponse() {
    }

    public ActiveOrdersResponse(String uuid, String topic, Double money, String nickName, String phoneNum, String upPhoneNum, String expressNum, String expressName, String province, String city, String address, Integer send, String time) {
        this.uuid = uuid;
        this.topic = topic;
        this.money = money;
        this.nickName = nickName;
        this.phoneNum = phoneNum;
        this.upPhoneNum = upPhoneNum;
        this.expressNum = expressNum;
        this.expressName = expressName;
        this.province = province;
        this.city = city;
        this.address = address;
        this.send = send;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUpPhoneNum() {
        return upPhoneNum;
    }

    public void setUpPhoneNum(String upPhoneNum) {
        this.upPhoneNum = upPhoneNum;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSend() {
        return send;
    }

    public void setSend(Integer send) {
        this.send = send;
    }
}
