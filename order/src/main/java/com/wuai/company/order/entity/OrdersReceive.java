package com.wuai.company.order.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/5.
 */
public class OrdersReceive implements Serializable{

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
    private String ordersId;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 开始时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp updateTime;
    /**
     * 是否 已确认服务方到达
     */
    private Boolean  isArrived;
    /**
     * 是否删除 true是 false不是
     */
    private Integer deleted;


    public Boolean getArrived() {
        return isArrived;
    }

    public void setArrived(Boolean arrived) {
        isArrived = arrived;
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

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
