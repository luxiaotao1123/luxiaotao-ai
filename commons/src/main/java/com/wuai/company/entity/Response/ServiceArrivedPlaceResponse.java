package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/8/31.
 */
public class ServiceArrivedPlaceResponse extends UuidResponse implements Serializable {
    private String ordersId;
    private Integer userId;
    private Double longitude;
    private Double latitude;

    public ServiceArrivedPlaceResponse(){}

    public ServiceArrivedPlaceResponse(String ordersId, Integer userId, Double longitude, Double latitude) {
        this.ordersId = ordersId;
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public ServiceArrivedPlaceResponse(String uuid, String ordersId, Integer userId, Double longitude, Double latitude) {
        super(uuid);
        this.ordersId = ordersId;
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public ServiceArrivedPlaceResponse(Integer id, String uuid, String ordersId, Integer userId, Double longitude, Double latitude) {
        super(id, uuid);
        this.ordersId = ordersId;
        this.userId = userId;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
