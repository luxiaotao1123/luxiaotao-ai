package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/8/28.
 */
public class UuidResponse extends IdResponse implements Serializable {
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UuidResponse(){

    }

    public UuidResponse(String uuid) {
        this.uuid = uuid;
    }

    public UuidResponse(Integer id, String uuid) {
        super(id);
        this.uuid = uuid;
    }
}
