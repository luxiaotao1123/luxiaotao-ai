package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/2.
 */
public class StoreTaskResponse implements Serializable{
    private String uuid ;
    private String topic;
    private Integer type;
    private Integer size;
    private String pic;
    private Double backMoney;
    private String note;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public StoreTaskResponse() {
    }

    public StoreTaskResponse(String uuid, String topic, Integer type, Integer size, String pic, Double backMoney, String note) {
        this.uuid = uuid;
        this.topic = topic;
        this.type = type;
        this.size = size;
        this.pic = pic;
        this.backMoney = backMoney;
        this.note = note;
    }

    public Double getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(Double backMoney) {
        this.backMoney = backMoney;
    }
}
