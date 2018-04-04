package com.wuai.company.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 * 商店套餐类
 */
public class Combo implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 伪id
     */
    private String uid;
    /**
     * 商店id
     */
    private String storeId;
    /**
     * 套餐名称
     */
    private String combo;
    /**
     * 套餐内banner
     */
    private String picture;
    /**
     * 套餐价格
     */
    private Double price;
    /**
     * 优惠
     */
    private String privilege;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后修改时间
     */
    private Timestamp updateTime;
    /**
     * 是否删除 0 false 1 true
     */
    private Integer deleted;

    private String time;//套餐周期
    private String summary;//套餐简介

    public Combo() {
    }

    public Combo(Integer id, String uid, String storeId, String combo, String picture, Double price, String privilege, Timestamp createTime, Timestamp updateTime, Integer deleted, String time, String summary) {
        this.id = id;
        this.uid = uid;
        this.storeId = storeId;
        this.combo = combo;
        this.picture = picture;
        this.price = price;
        this.privilege = privilege;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
        this.time = time;
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
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
