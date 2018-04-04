package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/10/11.
 */
public class SystemResponse implements Serializable{
    private String key;
    private String value;
    private String note;
    private String explain;

    private SystemResponse(){}

    public SystemResponse(String key, String value, String note, String explain) {
        this.key = key;
        this.value = value;
        this.note = note;
        this.explain = explain;
    }

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
}
