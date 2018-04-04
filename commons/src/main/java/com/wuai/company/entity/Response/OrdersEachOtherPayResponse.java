package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/20.
 */
public class OrdersEachOtherPayResponse implements Serializable{
    private String ordersId;
    private Double money;

    public OrdersEachOtherPayResponse() {
    }

    public OrdersEachOtherPayResponse(String ordersId, Double money) {
        this.ordersId = ordersId;
        this.money = money;
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
}
