package com.wuai.company.entity.Response;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/5/26.
 * 场景表
 */
public class Scene implements Serializable {
    /*
        自增ID
     */
    private Integer id;
    /*
    * 伪ID
    * */
    private String uuid;
    /*
    * 场景的key值
    * */
    private String key;
    /*
    * 场景的value值
    * */
    private String value;
    /*
       约会时长
    */
    private String timeInterval;
    /*
      默认场景消费 每小时金额
   */
    private Double hourlyFee;

    /*
     温馨提示
  */
    private String tips;
    /**
     * 感谢费
     */
    private String gratefulFree;
    /*
    * 创建时间
    * */
    private Timestamp createTime;
    /*
    * 最后修改时间
    * */
    private Timestamp updateTime;
    /*
    * 是否删除
    * */
    private Boolean deleted;

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

    private String ctgr; //高德地图参数

    public String getCtgr() {
        return ctgr;
    }

    public void setCtgr(String ctgr) {
        this.ctgr = ctgr;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Integer getFirstStart() {
        return firstStart;
    }

    public void setFirstStart(Integer firstStart) {
        this.firstStart = firstStart;
    }

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    public String getGratefulFree() {
        return gratefulFree;
    }

    public void setGratefulFree(String gratefulFree) {
        this.gratefulFree = gratefulFree;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public String toString() {
        return "Scene{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", timeInterval=" + timeInterval +
                ", hourlyFee=" + hourlyFee +
                ", tips='" + tips + '\'' +
                ", gratefulFree='" + gratefulFree + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                '}';
    }
}
