package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/12/11.
 */
public class StoreTaskPayResponse implements Serializable{
    private String uuid;
    private Integer userId;
    private Double money;
    private String taskDetailedId;
    private String upUUid;
    private String topic;
    private Integer size;
    private String note;


    public StoreTaskPayResponse(String uuid, Integer userId, Double money, String taskDetailedId, String upUUid, String topic, Integer size, String note) {
        this.uuid = uuid;
        this.userId = userId;
        this.money = money;
        this.taskDetailedId = taskDetailedId;
        this.upUUid = upUUid;
        this.topic = topic;
        this.size = size;
        this.note = note;
    }

    public StoreTaskPayResponse() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getTaskDetailedId() {
        return taskDetailedId;
    }

    public void setTaskDetailedId(String taskDetailedId) {
        this.taskDetailedId = taskDetailedId;
    }

    public String getUpUUid() {
        return upUUid;
    }

    public void setUpUUid(String upUUid) {
        this.upUUid = upUUid;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
