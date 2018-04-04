package com.wuai.company.entity.request;

import java.io.Serializable;

/**
 * Created by hyf on 2017/10/18.
 */
public class LongitudeAndLatitudeRequest implements Serializable{
    private Double longitude;
    private Double latitude;

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
