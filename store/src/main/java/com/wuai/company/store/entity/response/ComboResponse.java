package com.wuai.company.store.entity.response;

import com.wuai.company.store.entity.Commodity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
public class ComboResponse implements Serializable {
    /**
     * 伪id
     */
    private String uuid;
    /**
     * 套餐名称
     */
    private String combo;
    private String summary;
    /**
     * 套餐价格
     */
    private Double price;

//    private List<String> commodity;
//    private List<String> commodityPrice;
//    private List<String> commoditySize;
    private List<Commodity> commodity;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Commodity> getCommodity() {
        return commodity;
    }

    public void setCommodity(List<Commodity> commodity) {
        this.commodity = commodity;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "ComboResponse{" +
                "uid='" + uuid + '\'' +
                ", combo='" + combo + '\'' +
                ", price=" + price +
                ", commodity=" + commodity +
                '}';
    }
}
