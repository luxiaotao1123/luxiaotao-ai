package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/2.
 */
public class TaskMineResponse implements Serializable {
    private String uuid;
    private String topic;
    private String taskDetailedId;
    private Integer size;
    private Integer downSize;
    public TaskMineResponse(){}

    public TaskMineResponse(String uuid, String topic, String taskDetailedId, Integer size, Integer downSize) {
        this.uuid = uuid;
        this.topic = topic;
        this.taskDetailedId = taskDetailedId;
        this.size = size;
        this.downSize = downSize;
    }

    public String getTaskDetailedId() {
        return taskDetailedId;
    }

    public void setTaskDetailedId(String taskDetailedId) {
        this.taskDetailedId = taskDetailedId;
    }

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

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getDownSize() {
        return downSize;
    }

    public void setDownSize(Integer downSize) {
        this.downSize = downSize;
    }

}
