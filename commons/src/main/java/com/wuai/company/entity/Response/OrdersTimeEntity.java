package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/5.
 */
public class OrdersTimeEntity implements Serializable{
    private String uid;
    private Integer userId;
    private Integer orderUserId;
    private Integer proportion;
    private Double hourlyFee;
    private Integer selTimeType;
    private Integer orderPeriod;
    private Double gratefulFree;
    private Integer perhaps;
    private String scenes;
    private String startTime;
    private Integer personCount;

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    public Integer getOrderUserId() {
        return orderUserId;
    }

    public void setOrderUserId(Integer orderUserId) {
        this.orderUserId = orderUserId;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getHourlyFee() {
        return hourlyFee;
    }

    public void setHourlyFee(Double hourlyFee) {
        this.hourlyFee = hourlyFee;
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

    public Double getGratefulFree() {
        return gratefulFree;
    }

    public void setGratefulFree(Double gratefulFree) {
        this.gratefulFree = gratefulFree;
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

    @Override
    public String toString() {
        return "OrdersTimeEntity{" +
                "uid='" + uid + '\'' +
                ", userId='" + userId + '\'' +
                ", hourlyFee=" + hourlyFee +
                ", selTimeType=" + selTimeType +
                ", orderPeriod=" + orderPeriod +
                ", gratefulFree=" + gratefulFree +
                ", perhaps=" + perhaps +
                ", scenes=" + scenes +
                ", startTime='" + startTime + '\'' +
                '}';
    }
}
