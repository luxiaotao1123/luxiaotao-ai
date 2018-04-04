package com.wuai.company.user.entity.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */
public class BillDetailResponse implements Serializable {
    private String ordersId;//订单号
    private String startTime;//开始时间
    private String orderPeriod; //时长
//    private Integer type;//类型
    private Integer payType;//支付状态 码
    private String payed;//支付状态 码
    private Double money;//金额
    private String place; //地点名称
    private String address; //地点
    private String scene; //场景
    private Integer perhaps; //邀约或应约
    private String icon;  //头像
    private List<OrdersUResponse> orderUser;


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }

    public List<OrdersUResponse> getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(List<OrdersUResponse> orderUser) {
        this.orderUser = orderUser;
    }

    public Integer getPerhaps() {
        return perhaps;
    }

    public void setPerhaps(Integer perhaps) {
        this.perhaps = perhaps;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(String orderPeriod) {
        this.orderPeriod = orderPeriod;
    }



    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
