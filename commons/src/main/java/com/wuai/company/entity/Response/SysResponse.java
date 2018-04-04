package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/9/26.
 */
public class SysResponse implements Serializable{
    private String key;
    private String value;

    public SysResponse(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public SysResponse(){}
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
