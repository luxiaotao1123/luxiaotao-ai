package com.wuai.company.store.manage.entity.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/29.
 */
public class StoreDetailsResponse implements Serializable {
    private String nickName; //用户名
    private String time; //订单下单时间
    private String storeNo; //订单号
    private String comboName; //套餐名称
    private Double money; //套餐金额
    private Integer payType; //支付标识
    private String type; //支付状态

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}
