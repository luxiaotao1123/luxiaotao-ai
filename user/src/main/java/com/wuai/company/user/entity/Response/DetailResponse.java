package com.wuai.company.user.entity.Response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/19.
 */
public class DetailResponse implements Serializable{
    /**
     * 订单明细id
     */
    private String uuid;
    /**
     * 订单id
     */
    private String ordersId;

    /**
     * 金额
     */
    private Double money;
    /**
     * 支付类型
     */
    private Integer type;
    /**
     * 用户id
     */
    private Integer payId;

    public DetailResponse(){}

    public DetailResponse(String uuid, String ordersId, Double money, Integer type, Integer payId) {
        this.uuid = uuid;
        this.ordersId = ordersId;
        this.money = money;
        this.type = type;
        this.payId = payId;

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

    public Integer getPayId() {
        return payId;
    }

    public void setPayId(Integer payId) {
        this.payId = payId;
    }

    @Override
    public String toString() {
        return "DetailResponse{" +
                "uuid='" + uuid + '\'' +
                ", ordersId='" + ordersId + '\'' +
                ", money=" + money +
                ", type=" + type +
                ", payId=" + payId +
                '}';
    }
}
