package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/9/27.
 */
public class TalkResponse implements Serializable{
    private Integer id;
    private Integer key;
    private String value;

    public TalkResponse(){}

    public TalkResponse(Integer id, Integer key, String value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
