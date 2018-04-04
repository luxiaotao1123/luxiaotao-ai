package com.wuai.company.entity.request;

import java.io.Serializable;

/**
 * Created by 97947 on 2017/9/27.
 */
public class ScenesRequest implements Serializable{

    /**
     * 伪ID
    */
    private String uuid;
    /**
     * 场景的key值
    * */
    private String key;
    /**
     * 场景的value值
     */
    private String value;
    /**
     * 约会时长
     */
    private String timeInterval;
    /**
     * 默认场景消费 每小时金额
     */
    private Double hourlyFee;

    /**
     * 温馨提示
     */
    private String tips;
    /**
     * 感谢费
     */
    private String gratefulFree;


    /**
     * 达成比例
     */
    private Integer proportion;

    /**
     * 最早开始时间 间隔
     * @return
     */
    private Integer firstStart;

    //商店场景图片
    private String pic;
    //高德地图参数
    private String ctgr;

    public ScenesRequest(){}

    public ScenesRequest(String uuid, String key, String value, String timeInterval, Double hourlyFee, String tips, String gratefulFree, Integer proportion, Integer firstStart, String pic, String ctgr) {
        this.uuid = uuid;
        this.key = key;
        this.value = value;
        this.timeInterval = timeInterval;
        this.hourlyFee = hourlyFee;
        this.tips = tips;
        this.gratefulFree = gratefulFree;
        this.proportion = proportion;
        this.firstStart = firstStart;
        this.pic = pic;
        this.ctgr = ctgr;
    }

    public String getUuid() {

        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Double getHourlyFee() {
        return hourlyFee;
    }

    public void setHourlyFee(Double hourlyFee) {
        this.hourlyFee = hourlyFee;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getGratefulFree() {
        return gratefulFree;
    }

    public void setGratefulFree(String gratefulFree) {
        this.gratefulFree = gratefulFree;
    }

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    public Integer getFirstStart() {
        return firstStart;
    }

    public void setFirstStart(Integer firstStart) {
        this.firstStart = firstStart;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCtgr() {
        return ctgr;
    }

    public void setCtgr(String ctgr) {
        this.ctgr = ctgr;
    }
}
