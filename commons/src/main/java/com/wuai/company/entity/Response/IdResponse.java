package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/8/28.
 */
public class IdResponse implements Serializable{
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public IdResponse() {

    }

    public IdResponse(Integer id) {
        this.id = id;
    }
}
