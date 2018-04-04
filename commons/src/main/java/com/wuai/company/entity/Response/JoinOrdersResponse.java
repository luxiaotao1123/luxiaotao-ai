package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/8/28.
 * 参加订单 实体类
 */
public class JoinOrdersResponse extends UuidResponse implements Serializable {
        private String Place;
        private String startTime;
        private Integer orderPeriod;
        private String scenes;
        private Integer personCount;
        private Double money;

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(Integer orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public JoinOrdersResponse(){}



    public JoinOrdersResponse(String place, String startTime, Integer orderPeriod, String scenes, Integer personCount, Double money) {
        Place = place;
        this.startTime = startTime;
        this.orderPeriod = orderPeriod;
        this.scenes = scenes;
        this.personCount = personCount;
        this.money = money;
    }

    public JoinOrdersResponse(String uuid, String place, String startTime, Integer orderPeriod, String scenes, Integer personCount, Double money) {
        super(uuid);
        Place = place;
        this.startTime = startTime;
        this.orderPeriod = orderPeriod;
        this.scenes = scenes;
        this.personCount = personCount;
        this.money = money;
    }

    public JoinOrdersResponse(Integer id, String uuid, String place, String startTime, Integer orderPeriod, String scenes, Integer personCount, Double money) {
        super(id, uuid);
        Place = place;
        this.startTime = startTime;
        this.orderPeriod = orderPeriod;
        this.scenes = scenes;
        this.personCount = personCount;
        this.money = money;
    }
}
