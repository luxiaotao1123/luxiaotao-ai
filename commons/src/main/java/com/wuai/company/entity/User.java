package com.wuai.company.entity;

import com.wuai.company.entity.Response.UserVideoResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 * 用户类
 */
@Getter
@Setter
@ToString
public class User implements Serializable {
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

    /**
     * 个推id
     */
    private String cid;
    /**
     * 设备id
     */
    private String equipmentId;
    /**
     * 设备标识 0 安卓 1 ios
     */
    private Integer type;

    private Double money;
    private Double goldMoney;

    private String picture;

    private String age;

    private String occupation;
    private String height;
    private String weight;
    private String city;
    private String zodiac;
    private String birthday;
    private String label;
    private String invitationCode;

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

    private Integer userGrade;

    private String realName;
    private String accountNum;

    private Integer onOff;
    //家乡
    private String hometown;
    private String idCard;

    //上级用户id
    private Integer superiorUser;
    //支付密码
    private String payPass;
    private String videoTime;

    private Integer videoCheck;

    //消费余额
    private Double consumeMoney;
    //是否停止返现
    private Integer stopBackMoney;

    private String video;
    private String signature;//个性签名
    private Integer accumulatePoints ;//积分

    private List<UserVideoResponse> videos;//所有视频


    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public User() {
    }

    public User(Integer id, String uuid, String nickname, Integer gender, String phoneNum, String loadName, String loadPass, String icon, String cid, String equipmentId, Integer type, Double money, Double goldMoney, String picture, String age, String occupation, String height, String weight, String city, String zodiac,String birthday, String label, String invitationCode, Timestamp createTime, Timestamp updateTime, Integer deleted, Integer userGrade, String realName, String accountNum, Integer onOff, String hometown, String idCard, Integer superiorUser, String payPass, String videoTime, Double consumeMoney, Integer stopBackMoney, String video, List<UserVideoResponse> videos) {
        this.id = id;
        this.uuid = uuid;
        this.nickname = nickname;
        this.gender = gender;
        this.phoneNum = phoneNum;
        this.loadName = loadName;
        this.loadPass = loadPass;
        this.icon = icon;
        this.cid = cid;
        this.equipmentId = equipmentId;
        this.type = type;
        this.money = money;
        this.goldMoney = goldMoney;
        this.picture = picture;
        this.age = age;
        this.occupation = occupation;
        this.height = height;
        this.weight = weight;
        this.city = city;
        this.zodiac = zodiac;
        this.birthday = birthday;
        this.label = label;
        this.invitationCode = invitationCode;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
        this.userGrade = userGrade;
        this.realName = realName;
        this.accountNum = accountNum;
        this.onOff = onOff;
        this.hometown = hometown;
        this.idCard = idCard;
        this.superiorUser = superiorUser;
        this.payPass = payPass;
        this.videoTime = videoTime;
        this.consumeMoney = consumeMoney;
        this.stopBackMoney = stopBackMoney;
        this.video = video;
        this.videos = videos;
    }
}
