package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/9/25.
 * 约会订单明细
 */
public class InvitationDetailResponse implements Serializable{
    private String realName;//真实姓名
    private Integer userId;//用户id
    private String phoneNum;//手机号
    private Double money;//金额
    private Integer type;//明细状态
    private Integer ordersType;//明细类型
    private String note;//备注
    private String createTime;//明细生成时间
    private String ordersId;//订单号

    public InvitationDetailResponse(){}

    public InvitationDetailResponse(String realName, Integer userId, String phoneNum, Double money, Integer type, Integer ordersType, String note, String createTime) {
        this.realName = realName;
        this.userId = userId;
        this.phoneNum = phoneNum;
        this.money = money;
        this.type = type;
        this.ordersType = ordersType;
        this.note = note;
        this.createTime = createTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrdersType() {
        return ordersType;
    }

    public void setOrdersType(Integer ordersType) {
        this.ordersType = ordersType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }
}
