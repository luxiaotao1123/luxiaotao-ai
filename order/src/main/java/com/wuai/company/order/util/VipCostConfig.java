package com.wuai.company.order.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 97947 on 2017/9/21.
 */
@ConfigurationProperties(prefix="vipCost")
public class VipCostConfig {
    private String place;
    private String time;
    private String label;
    private String star;
    private String gratefulFee;

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getGratefulFee() {
        return gratefulFee;
    }

    public void setGratefulFee(String gratefulFee) {
        this.gratefulFee = gratefulFee;
    }
}
