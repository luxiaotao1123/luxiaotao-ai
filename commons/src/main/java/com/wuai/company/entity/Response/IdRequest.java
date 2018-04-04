package com.wuai.company.entity.Response;

/**
 * Created by Administrator on 2017/5/26.
 * uid
 */
public class IdRequest {
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public IdRequest(){}

    public IdRequest(String uid) {
        this.uid = uid;
    }
}
