package com.wuai.company.user.entity.Response;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/27.
 */
public class StoreBillDetailResponse implements Serializable{
    /**
     * 订单号
     */
    private String uuid;
    /**
     * 购买时间
     */
    private String time;

    /**
     * 地点名称
     */
    private String name;
    /**
     * 套餐名称
     */
    private String combo;

    private Integer type;

    private String payType;

    /**
     * 金额
     * @return
     */
    private Double money;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
