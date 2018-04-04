package com.wuai.company.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wuai.company.enums.OpenLocalTypeEnum;
import com.wuai.company.enums.OrderPublishTypeEnum;
import com.wuai.company.enums.PayTypeEnum;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import static com.wuai.company.enums.SceneSelEnum.getSceneSelByChineseKey;

/**
 * 订单的信息表
 * Created by Ness on 2017/5/25.
 */
public class Orders  implements Serializable {

    private static final long serialVersionUID = 2148635951433502307L;


    /**
     * 自增Id
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 伪Id
     */
    private String uuid;
    /**
     * 响应接单id
     */
    private String userInvitation;


    /**
     * 开始的时间
     */
    private String startTime;


    /**
     * 选择时间类型
     */
    private Integer selTimeType;


    /**
     * 订单的周期
     */
    private Double orderPeriod;

    /**
     * 订单的类型
     */
    private Integer orderType;


    /**
     * 人数的数量
     */
    private Integer personCount;


    /**
     * 感谢费
     */
    private Double gratefulFree;


    /**
     * 个性的标签
     */
    private String label;


    /**
     * 开始时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp updateTime;


    /**
     * 是否删除 true是 false不是
     */
    private Integer deleted;

    /**
     * 区分是邀约还是应约订单 0 应约 1邀约
     */
    private Integer perhaps;

    /**
     * 场景选择
     */
    private String scenes;

    private String place;


    /**
     * 匹配度
     */
    private Double matchRate;


    /**
     * 距离
     */
    private Double distance;

    /**
     * 金额
     */
    private Double money;


    /**
     * 订单操作类型
     */
    private String orderOperateEnum;

    /**
     *订单支付类型 0 待支付， 1 已支付 2 取消
     * @return
     */
    private int payType;

    /**
     * 状态类型
     */
    private String type;

    /**
     * 每小时费用
     */
    private Double hourlyFee;

    /**
     * 用户集合
     */
    private List<User> users;

    /**
     * 版本号
     */
    private String version;


    /**
     * update_money 订单修改后需要支付的金额
     * @return
     */
    private Double updateMoney;


    private String startTimeStamp;

    private Integer size;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getSize() {
        if (size==null){
            size=0;
        }
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(String startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public Double getUpdateMoney() {
        return updateMoney;
    }

    public void setUpdateMoney(Double updateMoney) {
        this.updateMoney = updateMoney;
    }

    public String getType() {
        if (type==null||type.equals("")){
            type= PayTypeEnum.STR_WAIT_CONFIRM.getValue();
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Double getHourlyFee() {
        return hourlyFee;
    }

    public void setHourlyFee(Double hourlyFee) {
        this.hourlyFee = hourlyFee;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }



    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public String getOrderOperateEnum() {
        return orderOperateEnum;
    }

    public void setOrderOperateEnum(String orderOperateEnum) {
        this.orderOperateEnum = orderOperateEnum;
    }

    public Double getMatchRate() {
        return matchRate;
    }

    public void setMatchRate(Double matchRate) {
        this.matchRate = matchRate;
    }

    public String getUserInvitation() {
        return userInvitation;
    }

    public void setUserInvitation(String userInvitation) {
        this.userInvitation = userInvitation;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getPerhaps() {
        return perhaps;
    }

    public void setPerhaps(Integer perhaps) {
        this.perhaps = perhaps;
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes;
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


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getSelTimeType() {
        return selTimeType;
    }

    public void setSelTimeType(Integer selTimeType) {
        this.selTimeType = selTimeType;
    }

    public Double getOrderPeriod() {
        return orderPeriod;
    }

    public void setOrderPeriod(Double orderPeriod) {
        this.orderPeriod = orderPeriod;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getPersonCount() {
        return personCount;
    }

    public void setPersonCount(Integer personCount) {
        this.personCount = personCount;
    }

    public Double getGratefulFree() {
        return gratefulFree;
    }

    public void setGratefulFree(Double gratefulFree) {
        this.gratefulFree = gratefulFree;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    /**
     * 用户Id
     */
    private Integer uid;


    /**
     * 用户的经度
     */
    private Double longitude;

    /**
     * 用户的纬度
     */
    private Double latitude;


    /**
     * 开设的地区的记录
     */
    private OpenLocalTypeEnum openLocalTypeEnum;


    private String sceneSelEnum;

    private String publishType;

    private String oppositePublishType;


    public String getPublishType() {
        return publishType;
    }

    public void setPublishType(OrderPublishTypeEnum typeEnum) {
        if (sceneSelEnum == null || typeEnum == null) {
            throw new RuntimeException("please add sceneSelEnum at first");
        }
        this.publishType = (getSceneSelByChineseKey(sceneSelEnum).toCode() + ":" + typeEnum.toValue()).intern();
        this.oppositePublishType = (getSceneSelByChineseKey(sceneSelEnum).toCode() + ":" + typeEnum.toOppositeValue()).intern();
    }

    public void setPublishType(String publishType) {
        this.publishType = publishType;
    }

    public void setOppositePublishType(String oppositePublishType) {
        this.oppositePublishType = oppositePublishType;
    }

    public String getOppositePublishType() {
        return oppositePublishType;
    }

    public String getSceneSelEnum() {
        return sceneSelEnum;
    }

    public void setSceneSelEnum(String sceneSelEnum) {
        this.sceneSelEnum = sceneSelEnum;
    }

    @JsonIgnore
    public boolean isEmpty() {
        if (uid==null) return true;
        if (longitude == null) return true;
        if (latitude == null) return true;
        if (openLocalTypeEnum == null) return true;
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Orders)) return false;

        Orders orders1 = (Orders) o;

        return getUuid().equals(orders1.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }

    public OpenLocalTypeEnum getOpenLocalTypeEnum() {
        return openLocalTypeEnum;
    }

    public void setOpenLocalTypeEnum(OpenLocalTypeEnum openLocalTypeEnum) {
        this.openLocalTypeEnum = openLocalTypeEnum;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }


    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", userInvitation='" + userInvitation + '\'' +
                ", startTime='" + startTime + '\'' +
                ", selTimeType=" + selTimeType +
                ", orderPeriod=" + orderPeriod +
                ", orderType=" + orderType +
                ", personCount=" + personCount +
                ", gratefulFree=" + gratefulFree +
                ", label='" + label + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                ", perhaps=" + perhaps +
                ", scenes=" + scenes +
                ", place='" + place + '\'' +
                ", orderOperateEnum=" + orderOperateEnum +
                '}';
    }
}
