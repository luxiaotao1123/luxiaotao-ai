package com.wuai.company.order.entity.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/22.
 */
public class MapsResponse implements Serializable {
    private String name;
    private String distance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
