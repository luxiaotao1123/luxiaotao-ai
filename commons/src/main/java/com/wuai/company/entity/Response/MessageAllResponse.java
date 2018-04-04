package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2018/1/4.
 */
public class MessageAllResponse implements Serializable{
    private String uuid;
    private String partyId;
    private String userId;
    private String nickName;
    private String message;
    private String date;
    private String icon;

    public MessageAllResponse() {
    }

    public MessageAllResponse(String uuid, String partyId, String userId, String nickName, String message, String date, String icon) {
        this.uuid = uuid;
        this.partyId = partyId;
        this.userId = userId;
        this.nickName = nickName;
        this.message = message;
        this.date = date;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageAllResponse{" +
                "uuid='" + uuid + '\'' +
                ", partyId='" + partyId + '\'' +
                ", userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
