package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/3.
 */
public class ShippingAddressResponse implements Serializable{
    private String uuid;
    private String province;
    private String city;
    private String address;
    private Integer defaultValue;
    private Integer userId;
    private String phoneNum;
    private String nickName;

    ShippingAddressResponse(){}

    public ShippingAddressResponse(String uuid, String province, String city, String address, Integer defaultValue, Integer userId, String phoneNum, String nickName) {
        this.uuid = uuid;
        this.province = province;
        this.city = city;

        this.address = address;
        this.defaultValue = defaultValue;
        this.userId = userId;
        this.phoneNum = phoneNum;
        this.nickName = nickName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
