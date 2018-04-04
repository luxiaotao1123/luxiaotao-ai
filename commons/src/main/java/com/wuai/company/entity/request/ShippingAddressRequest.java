package com.wuai.company.entity.request;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/3.
 */
public class ShippingAddressRequest implements Serializable {

    private String uuid;
    private String province;
    private String city;
    private String phoneNum;
    private String nickName;
    private String address;
//    private Integer defaultValue;
    private Integer userId;

    public ShippingAddressRequest(){}

    public ShippingAddressRequest(String uuid, String province, String city, String phoneNum, String nickName, String address, Integer userId) {
        this.uuid = uuid;
        this.province = province;
        this.city = city;
        this.phoneNum = phoneNum;
        this.nickName = nickName;
        this.address = address;
        this.userId = userId;
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
