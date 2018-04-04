package com.wuai.company.entity.request;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/8/18.
 */
public class AppraiseRequest implements Serializable {
    private Integer userId;
    private Integer star;
    private String content;
    private String ordersId;

    public AppraiseRequest(){}

    public AppraiseRequest(Integer userId, Integer star, String content) {
        this.userId = userId;
        this.star = star;
        this.content = content;
    }

    public String getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(String ordersId) {
        this.ordersId = ordersId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
