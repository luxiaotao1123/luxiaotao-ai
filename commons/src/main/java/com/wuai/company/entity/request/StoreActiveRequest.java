package com.wuai.company.entity.request;

import java.io.Serializable;

/**
 * Created by hyf on 2017/11/6.
 */
public class StoreActiveRequest implements Serializable {
    private String uuid;
    private String pic;
    private String topic;
    private String content;
    private Double money;
    private Double backMoney;
    private String size;
    private String shopPic;

    public StoreActiveRequest(){}

    public StoreActiveRequest(String uuid, String pic, String topic, String content, Double money, Double backMoney, String size, String shopPic) {
        this.uuid = uuid;
        this.pic = pic;
        this.topic = topic;
        this.content = content;
        this.money = money;
        this.backMoney = backMoney;
        this.size = size;
        this.shopPic = shopPic;
    }

    public Double getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(Double backMoney) {
        this.backMoney = backMoney;
    }

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
