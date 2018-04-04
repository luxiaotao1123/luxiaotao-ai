package com.wuai.company.store.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/14.
 * 商店类
 */
public class Store implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 伪id
     */
    private String uid;
    /**
     * 店铺地址id
     */
    private Integer mapsId;
    /**
     * 店铺名称
     */
    private String name;
    /**
     * 店铺地址
     */
    private String address;
    /**
     * 店铺广告位
     */
    private String banner;
    /**
     * 详情图
     */
    private String pictures;

    /**
     * 电话
     */
    private String phone;


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

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
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

    public Integer getMapsId() {
        return mapsId;
    }

    public void setMapsId(Integer mapsId) {
        this.mapsId = mapsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
