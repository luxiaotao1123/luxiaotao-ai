package com.wuai.company.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by 97947 on 2017/8/17.
 */
public class Appraise implements Serializable {
    private Integer id ;
    private String uuid;
    private Double star;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Integer deleted;
    private String ordersId;
    private Integer userId;
    private Integer appraiserId;

    public Integer getAppraiserId() {
        return appraiserId;
    }

    public void setAppraiserId(Integer appraiserId) {
        this.appraiserId = appraiserId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
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
}
