package com.wuai.company.order.entity.request;

import com.wuai.company.entity.Response.IdRequest;

/**
 * Created by Administrator on 2017/5/27.
 */
public class SceneRequest extends IdRequest {
    private String num;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
