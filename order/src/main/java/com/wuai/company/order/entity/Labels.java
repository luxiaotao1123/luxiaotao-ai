package com.wuai.company.order.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/5/27.
 * 标签库
 */
public class Labels implements Serializable {
    private String key;

    private String value;

    private Timestamp time;

    private Integer status;

    private String note;

    private String explain;

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

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Labels(){}

    public Labels(String key, String value, Timestamp time, Integer status, String note, String explain) {
        this.key = key;
        this.value = value;
        this.time = time;
        this.status = status;
        this.note = note;
        this.explain = explain;
    }
}
