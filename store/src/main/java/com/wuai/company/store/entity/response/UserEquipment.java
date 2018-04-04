package com.wuai.company.store.entity.response;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by 97947 on 2017/7/5.
 */
public class UserEquipment implements Serializable {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户伪Id
     */
    private String uuid;
    /**
     * 昵称
     */
    private String nickname;


    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号
     */
    private String phoneNum;
//    private Integer phoneNum;


    /**
     * 用户的登录名称
     */
    private String loadName;

    /**
     * 用户的登录密码
     */
    private String loadPass;

    /**
     * 用户头像
     */
    private String icon;

    private String cid;

    private Double money;

    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后修改时间
     */
    private Timestamp updateTime;
    /**
     * 是否删除，true：是，false：fou
     */
    private Integer deleted;
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    //    public Integer getPhoneNum() {
//        return phoneNum;
//    }
//
//    public void setPhoneNum(Integer phoneNum) {
//        this.phoneNum = phoneNum;
//    }

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

    public String getLoadName() {
        return loadName;
    }

    public void setLoadName(String loadName) {
        this.loadName = loadName;
    }

    public String getLoadPass() {
        return loadPass;
    }

    public void setLoadPass(String loadPass) {
        this.loadPass = loadPass;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}

