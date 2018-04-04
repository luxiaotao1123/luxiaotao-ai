package com.wuai.company.entity.request;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/8.
 */
public class OrdersExcelRequest implements Serializable {
    private Integer userUuid ; // 用户id
    private String uuid ; // uuid
    private String startTime ; // 订单开始时间
    private String place ; // 场景地址

    private Integer selTimeType ; //
    private Integer payType ; //
    private Integer orderPeriod ; // 时长
    private Integer personCount ; // 人数

    private Double gratefulFree ; // 感谢费
    private String label ; // 标签
    private Integer perhaps; // 要约或应约

    private String scenes ; // 场景
    private Double money ; // 金额
    private Double latitude ; // 纬度

    private Double longitude ; // 纬度
    private String address ; // 地点

    public OrdersExcelRequest(){}


    public OrdersExcelRequest(Integer userUuid, String uuid, String startTime, String place, Integer selTimeType, Integer payType, Integer orderPeriod, Integer personCount, Double gratefulFree, String label, Integer perhaps, String scenes, Double money, Double latitude, Double longitude, String address) {
        this.userUuid = userUuid;
        this.uuid = uuid;
        this.startTime = startTime;
        this.place = place;
        this.selTimeType = selTimeType;
        this.payType = payType;
        this.orderPeriod = orderPeriod;
        this.personCount = personCount;
        this.gratefulFree = gratefulFree;
        this.label = label;
        this.perhaps = perhaps;
        this.scenes = scenes;
        this.money = money;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(Integer userUuid) {
        this.userUuid = userUuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getSelTimeType() {
        return selTimeType;
    }

    public void setSelTimeType(Integer selTimeType) {
        this.selTimeType = selTimeType;
    }

    public Integer getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(Integer orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public Double getGratefulFree() {
        return gratefulFree;
    }

    public void setGratefulFree(Double gratefulFree) {
        this.gratefulFree = gratefulFree;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getPerhaps() {
        return perhaps;
    }

    public void setPerhaps(Integer perhaps) {
        this.perhaps = perhaps;
    }

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "OrdersExcelRequest{" +
                "userUuid='" + userUuid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", startTime='" + startTime + '\'' +
                ", place='" + place + '\'' +
                ", selTimeType=" + selTimeType +
                ", orderPeriod=" + orderPeriod +
                ", personCount=" + personCount +
                ", gratefulFree=" + gratefulFree +
                ", label='" + label + '\'' +
                ", perhaps=" + perhaps +
                ", scenes='" + scenes + '\'' +
                ", money=" + money +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                '}';
    }
}
