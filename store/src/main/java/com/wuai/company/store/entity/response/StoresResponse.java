package com.wuai.company.store.entity.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/14.
 */
public class StoresResponse implements Serializable{
    /**
     * id
     */
    private Integer id;
    /**
     * 伪id
     */
    private String uuid;
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
     * 广告位
     */
    private String banner;
    /**
     * 电话
     */
    private String phone;
    /**
     * 距离 km
     */
    private String distance;


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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
