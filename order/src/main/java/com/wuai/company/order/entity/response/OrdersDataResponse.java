package com.wuai.company.order.entity.response;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/6/11.
 */
public class OrdersDataResponse implements Serializable{
    /**
     * 自增Id
     */
    private Integer id;

    /**
     * 伪Id
     */
    private String uuid;
    /**
     * 响应接单id
     */
    private String userInvitation;

    /**
     * 用户Id
     */
    private String userId;


    /**
     * 开始的时间
     */
    private String startTime;


    /**
     * 选择时间类型
     */
    private Integer selTimeType;

    private String address;

    /**
     * 订单的周期
     */
    private Integer orderPeriod;

    /**
     * 订单的类型
     */
    private Integer orderType;


    /**
     * 人数的数量
     */
    private Integer personCount;


    /**
     * 感谢费
     */
    private Double gratefulFree;


    /**
     * 个性的标签
     */
    private String label;


    /**
     * 开始时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp updateTime;


    /**
     * 是否删除 true是 false不是
     */
    private Integer deleted;

    /**
     *区分是邀约还是应约订单 0 应约 1邀约
     */
    private Integer perhaps;

    /**
     *场景选择
     */
    private String scenes;
    private Integer gender;

    private String place;

    private String nickName;

    private String icon;

    private Double hourlyFee;

    private Double money;

    private List<OrdersUserResponse> orderUser;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public List<OrdersUserResponse> getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(List<OrdersUserResponse> orderUser) {
        this.orderUser = orderUser;
    }

    public Double getHourlyFee() {
        return hourlyFee;
    }

    public void setHourlyFee(Double hourlyFee) {
        this.hourlyFee = hourlyFee;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserInvitation() {
        return userInvitation;
    }

    public void setUserInvitation(String userInvitation) {
        this.userInvitation = userInvitation;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }


}
