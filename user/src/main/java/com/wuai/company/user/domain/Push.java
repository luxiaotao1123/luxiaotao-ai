package com.wuai.company.user.domain;

import java.sql.Timestamp;

/**
 * Created by Ness on 2017/1/10.
 */

public class Push {
    private Integer id;

    /**
     * 发送的设备
     */
    private Integer sendDeviceType;

    /**
     * 发送的设备编号
     */
    private String deviceNum;


    /**
     * 关联用户的id
     */
    private String uuid;

    /**
     * 发送的消息主题
     */
    private String sendTopic;

    /**
     * 发送的消息内容
     */
    private String sendContent;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 是否删除 false不是  true删除
     */
    private Boolean deleted;

    /**
     * 修改时间
     */
    private Timestamp updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSendDeviceType() {
        return sendDeviceType;
    }

    public void setSendDeviceType(Integer sendDeviceType) {
        this.sendDeviceType = sendDeviceType;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSendTopic() {
        return sendTopic;
    }

    public void setSendTopic(String sendTopic) {
        this.sendTopic = sendTopic;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Push{" +
                "id=" + id +
                ", sendDeviceType=" + sendDeviceType +
                ", deviceNum='" + deviceNum + '\'' +
                ", uuid='" + uuid + '\'' +
                ", sendTopic='" + sendTopic + '\'' +
                ", sendContent='" + sendContent + '\'' +
                ", createTime=" + createTime +
                ", deleted=" + deleted +
                ", updateTime=" + updateTime +
                '}';
    }
}
