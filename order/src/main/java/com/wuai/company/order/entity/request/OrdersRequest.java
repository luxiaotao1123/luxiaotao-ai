package com.wuai.company.order.entity.request;

import com.wuai.company.entity.Response.PageRequest;

/**
 * Created by Administrator on 2017/5/27.
 * 订单请求
 */
public class OrdersRequest extends PageRequest {
    /**
     * 场景选择 -1 全部
     */
    private Integer scenes;
    /**
     * 邀约或者应约 0 邀约，1应约
     */
    private Integer perhaps;

    public OrdersRequest(){}

    public OrdersRequest(Integer scenes, Integer perhaps) {
        this.scenes = scenes;
        this.perhaps = perhaps;
    }

    public OrdersRequest(Integer pageNum, Integer scenes, Integer perhaps) {
        super(pageNum);
        this.scenes = scenes;
        this.perhaps = perhaps;
    }

    public OrdersRequest(String uid, Integer pageNum, Integer scenes, Integer perhaps) {
        super(uid, pageNum);
        this.scenes = scenes;
        this.perhaps = perhaps;
    }

    public Integer getScenes() {
        return scenes;
    }

    public void setScenes(Integer scenes) {
        this.scenes = scenes;
    }

    public Integer getPerhaps() {
        return perhaps;
    }

    public void setPerhaps(Integer perhaps) {
        this.perhaps = perhaps;
    }
}
