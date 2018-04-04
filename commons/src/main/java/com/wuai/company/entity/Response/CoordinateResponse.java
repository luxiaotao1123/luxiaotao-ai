package com.wuai.company.entity.Response;

/**
 * Created by hyf on 2017/10/17.
 */
public class CoordinateResponse {
    private Integer id;
    private String uuid;
    private Double longitude;
    private Double latitude;
    private Integer type;
    private String time;
    private Integer userId;

    public CoordinateResponse(){}

    public CoordinateResponse(Integer id, String uuid, Double longitude, Double latitude, Integer type, String time, Integer userId) {
        this.id = id;
        this.uuid = uuid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.type = type;
        this.time = time;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
