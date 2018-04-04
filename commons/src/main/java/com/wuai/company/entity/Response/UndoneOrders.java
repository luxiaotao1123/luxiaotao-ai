package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/8/28.
 */
public class UndoneOrders extends UuidResponse implements Serializable {
    private String startTime;
    private String place;
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public UndoneOrders(){

    }

    public UndoneOrders(String startTime, String place) {
        this.startTime = startTime;
        this.place = place;
    }

    public UndoneOrders(String uuid, String startTime, String place) {
        super(uuid);
        this.startTime = startTime;
        this.place = place;
    }

    public UndoneOrders(Integer id, String uuid, String startTime, String place) {
        super(id, uuid);
        this.startTime = startTime;
        this.place = place;
    }
}
