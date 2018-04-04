package com.wuai.company.order.entity.request;

import com.wuai.company.entity.Response.IdRequest;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/5/27.
 * 邀约请求
 */
public class InvitationRequest extends IdRequest {
    /**
     * 用户Id
     */
    private String userId;


    /**
     * 开始的时间
     */
    private String startTime;



    /**
     * 选择时间类型  0：固定时间 1：周期时间
     */
    private Integer selTimeType;


    /**
     * 订单的周期
     */
    private Integer orderPeriod;

    /**
     * 订单的类型 是否失效
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
    private Boolean deleted;

    /**
     *区分是邀约还是应约订单 0 应约 1邀约
     */
    private Integer perhaps;

    /**
     *场景选择
     */
    private Integer scenes;
    /**
     *每小时费用
     */
    private Double hourlyFee;

    public Double getHourlyFee() {
        return hourlyFee;
    }

    public void setHourlyFee(Double hourlyFee) {
        this.hourlyFee = hourlyFee;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getPerhaps() {
        return perhaps;
    }

    public void setPerhaps(Integer perhaps) {
        this.perhaps = perhaps;
    }

    public Integer getScenes() {
        return scenes;
    }

    public void setScenes(Integer scenes) {
        this.scenes = scenes;
    }

}
