package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/20.
 */
public class OrdersAloneResponse implements Serializable{
    private String uuid;
    private Integer userId;
    private String startTime;
    private Double orderPeriod;
    private Double gratefulFree;
    private Integer orderType;
    private Integer userInvitation;
    private String scenes;
    private String place;
    private Double money;
    private Double longitude;
    private Double latitude;
    private String address;
    private String icon;
    private String nickName;
    private String picture;

    public OrdersAloneResponse() {
    }

    public OrdersAloneResponse(String uuid, Integer userId, String startTime, Double orderPeriod, Double gratefulFree, Integer orderType, Integer userInvitation, String scenes, String place, Double money, Double longitude, Double latitude, String address, String icon, String nickName, String picture) {
        this.uuid = uuid;
        this.userId = userId;
        this.startTime = startTime;
        this.orderPeriod = orderPeriod;
        this.gratefulFree = gratefulFree;
        this.orderType = orderType;
        this.userInvitation = userInvitation;
        this.scenes = scenes;
        this.place = place;
        this.money = money;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.icon = icon;
        this.nickName = nickName;
        this.picture = picture;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Double getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(Double orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public Double getGratefulFree() {
        return gratefulFree;
    }

    public void setGratefulFree(Double gratefulFree) {
        this.gratefulFree = gratefulFree;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getUserInvitation() {
        return userInvitation;
    }

    public void setUserInvitation(Integer userInvitation) {
        this.userInvitation = userInvitation;
    }

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "OrdersAloneResponse{" +
                "uuid='" + uuid + '\'' +
                ", userId=" + userId +
                ", startTime='" + startTime + '\'' +
                ", orderPeriod=" + orderPeriod +
                ", gratefulFree=" + gratefulFree +
                ", orderType=" + orderType +
                ", userInvitation=" + userInvitation +
                ", scenes='" + scenes + '\'' +
                ", place='" + place + '\'' +
                ", money=" + money +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", address='" + address + '\'' +
                '}';
    }
}
