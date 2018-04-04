package com.wuai.company.entity.request;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by 97947 on 2017/10/11.
 */
public class SysRequest implements Serializable{
    private String key;
    private String value;
    private Integer status;
    private String note;
    private String explain;

    public SysRequest(){}

    public SysRequest(String key, String value, Integer status, String note, String explain) {
        this.key = key;
        this.value = value;
        this.status = status;
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
}
