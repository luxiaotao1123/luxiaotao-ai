package com.wuai.company.entity;

import com.wuai.company.entity.Response.UserVideoResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SnatchUser {
    //用户主id
    private Integer userId;
    //用户id
    private String uuid;
    //头像
    private String headUrl;
    //昵称
    private String nickName;
    //性别
    private Integer gender;
    //电话
    private String phone;
    //年龄
    private String age;
    //星座
    private String zodiac;
    //身高
    private String height;
    //体重
    private String weight;
    //定位
    private String location;
    //个性签名
    private String signature;
    //评价
    private String comment;
    //视频
    private List<UserVideoResponse> videos;

}
