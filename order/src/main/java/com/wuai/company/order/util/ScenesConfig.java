package com.wuai.company.order.util;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 * Created by 97947 on 2017/9/21.
 */
@ConfigurationProperties(prefix="scenes")
public class ScenesConfig {
    private String bar;
    private String ktv;
    private String gym;
    private String theatre;

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public String getKtv() {
        return ktv;
    }

    public void setKtv(String ktv) {
        this.ktv = ktv;
    }

    public String getGym() {
        return gym;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    public String getTheatre() {
        return theatre;
    }

    public void setTheatre(String theatre) {
        this.theatre = theatre;
    }
}
