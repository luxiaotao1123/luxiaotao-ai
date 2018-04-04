package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/29.
 */
public class CombodityResponse implements Serializable{
    private Integer id;
    /**
     * 伪id
     */
    private String uid;

    /**
     * 套餐id
     */
    private String comboId;
    /**
     * 商品名
     */
    private String name;

    /**
     * 数量
     */
    private Integer size;
    /**
     * 价格
     */
    private Double price;

    private String combo; //套餐名

    private String picture;//图片

    private String summary;//



    public CombodityResponse() {
    }

    public CombodityResponse(Integer id, String uid, String comboId, String name, Integer size, Double price, String combo, String picture, String summary) {
        this.id = id;
        this.uid = uid;
        this.comboId = comboId;
        this.name = name;
        this.size = size;
        this.price = price;
        this.combo = combo;
        this.picture = picture;
        this.summary = summary;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
