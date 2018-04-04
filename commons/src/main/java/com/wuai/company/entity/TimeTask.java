package com.wuai.company.entity;


import com.wuai.company.enums.ScheduleOperaEnum;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 定时任务的类
 */
public class TimeTask implements Serializable {

    private static final long serialVersionUID = -7615812218479904641L;
    private Integer id;
    private String scheduleTaskIndex;
    /**
     * 任务名称
     */
    private String timeTaskName;

    /**
     * 执行时间
     */
    private String executeTime;
    /**
     * 任务参数
     */
    private String params;


    /**
     * 开始时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp updateTime;


    /**
     * 是否删除 true是 false不是
     */
    private Boolean deleted;


    private ScheduleOperaEnum scheduleOperaEnum;


    public ScheduleOperaEnum getScheduleOperaEnum() {
        return scheduleOperaEnum;
    }

    public void setScheduleOperaEnum(ScheduleOperaEnum scheduleOperaEnum) {
        this.scheduleOperaEnum = scheduleOperaEnum;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }


    public String getTimeTaskName() {
        return timeTaskName;
    }

    public void setTimeTaskName(String timeTaskName) {
        this.timeTaskName = timeTaskName;
    }


    public String getScheduleTaskIndex() {
        return scheduleTaskIndex;
    }

    public void setScheduleTaskIndex(String scheduleTaskIndex) {
        this.scheduleTaskIndex = scheduleTaskIndex;
    }


    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        if (params.contains("\"")) {
            params = params.replaceAll("\"", "'");
        }
        this.params = params;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    @Override
    public String toString() {
        return "TimeTask{" +
                "id=" + id +
                ", scheduleTaskIndex='" + scheduleTaskIndex + '\'' +
                ", timeTaskName='" + timeTaskName + '\'' +
                ", executeTime='" + executeTime + '\'' +
                ", params='" + params + '\'' +
                ", scheduleOperaEnum=" + scheduleOperaEnum +
                '}';
    }
}
