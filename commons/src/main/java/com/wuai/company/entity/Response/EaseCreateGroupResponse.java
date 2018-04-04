package com.wuai.company.entity.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class EaseCreateGroupResponse {
    private String action;
    private String application;
    private String uri;
    private String entities;
    private Data data;
    private Long timestamp;
    private Integer duration;
    private String organization;
    private String applicationName;


    public class Data{
        String groupid;

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getEntities() {
        return entities;
    }

    public void setEntities(String entities) {
        this.entities = entities;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public String toString() {
        return "EaseCreateGroupResponse{" +
                "action='" + action + '\'' +
                ", application='" + application + '\'' +
                ", uri='" + uri + '\'' +
                ", entities='" + entities + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                ", duration=" + duration +
                ", organization='" + organization + '\'' +
                ", applicationName='" + applicationName + '\'' +
                '}';
    }
}
