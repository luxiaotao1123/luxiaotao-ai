package com.wuai.company.order.entity.response;

import com.wuai.company.entity.Response.UuidResponse;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/7.
 */
public class OrdersUserResponse extends UuidResponse implements Serializable {
    private String nickName;
    private String icon;
    private String ordersId;
    private String phoneNum;
    private String address;
    private Integer isPress;
    private String occupation;//职业
    private String age;//年龄
    private String gender;//性别
    private static Integer isPressed=1;
    private static Integer isNotPressed=2;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getIsPress() {
        if (isPress==null){
            return isPressed;
        }
        return isPress==0?isPressed:isNotPressed;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setIsPress(Integer isPress) {
        this.isPress = isPress;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public OrdersUserResponse(){};

    public OrdersUserResponse(String nickName, String icon, String ordersId, String phoneNum, String address, Integer isPress) {
        this.nickName = nickName;
        this.icon = icon;
        this.ordersId = ordersId;
        this.phoneNum = phoneNum;
        this.address = address;
        this.isPress = isPress;
    }

    public OrdersUserResponse(String uuid, String nickName, String icon, String ordersId, String phoneNum, String address, Integer isPress) {
        super(uuid);
        this.nickName = nickName;
        this.icon = icon;
        this.ordersId = ordersId;
        this.phoneNum = phoneNum;
        this.address = address;
        this.isPress = isPress;
    }

    public OrdersUserResponse(Integer id, String uuid, String nickName, String icon, String ordersId, String phoneNum, String address, Integer isPress) {
        super(id, uuid);
        this.nickName = nickName;
        this.icon = icon;
        this.ordersId = ordersId;
        this.phoneNum = phoneNum;
        this.address = address;
        this.isPress = isPress;
    }

    public OrdersUserResponse(String nickName, String icon, String ordersId, String phoneNum, String address, Integer isPress, String occupation, String age) {
        this.nickName = nickName;
        this.icon = icon;
        this.ordersId = ordersId;
        this.phoneNum = phoneNum;
        this.address = address;
        this.isPress = isPress;
        this.occupation = occupation;
        this.age = age;
    }

    public OrdersUserResponse(String uuid, String nickName, String icon, String ordersId, String phoneNum, String address, Integer isPress, String occupation, String age) {
        super(uuid);
        this.nickName = nickName;
        this.icon = icon;
        this.ordersId = ordersId;
        this.phoneNum = phoneNum;
        this.address = address;
        this.isPress = isPress;
        this.occupation = occupation;
        this.age = age;
    }

    public OrdersUserResponse(Integer id, String uuid, String nickName, String icon, String ordersId, String phoneNum, String address, Integer isPress, String occupation, String age) {
        super(id, uuid);
        this.nickName = nickName;
        this.icon = icon;
        this.ordersId = ordersId;
        this.phoneNum = phoneNum;
        this.address = address;
        this.isPress = isPress;
        this.occupation = occupation;
        this.age = age;
    }
}
