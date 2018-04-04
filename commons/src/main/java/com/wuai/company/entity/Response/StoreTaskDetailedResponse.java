package com.wuai.company.entity.Response;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/2.
 */
public class StoreTaskDetailedResponse implements Serializable{
    private String uuid;
    private String content;
    private String topic;
    private String type;
    private Integer size;
    private Double money;
    private Double backMoney;
    private String pic;
    private String shopPic;
    private String tip;

    public StoreTaskDetailedResponse(){}

    public StoreTaskDetailedResponse(String uuid, String content, String topic, String type, Integer size, Double money, Double backMoney, String pic, String shopPic, String tip) {
        this.uuid = uuid;
        this.content = content;
        this.topic = topic;
        this.type = type;
        this.size = size;
        this.money = money;
        this.backMoney = backMoney;
        this.pic = pic;
        this.shopPic = shopPic;
        this.tip = tip;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public Double getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(Double backMoney) {
        this.backMoney = backMoney;
    }
}

