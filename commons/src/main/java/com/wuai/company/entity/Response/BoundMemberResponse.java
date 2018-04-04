package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/10/23.
 */
public class BoundMemberResponse implements Serializable{
    private String uuid;
    private String phoneNum;
    private Integer type;
    private Integer userId;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
