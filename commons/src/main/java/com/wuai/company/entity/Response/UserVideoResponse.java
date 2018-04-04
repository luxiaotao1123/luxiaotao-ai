package com.wuai.company.entity.Response;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by hyf on 2018/1/12.
 */
@Getter
@Setter
@ToString
public class UserVideoResponse implements Serializable {
    private Integer id;
    private String uuid;//视频地址id
    private String nickName;//用户昵称
    private String phoneNum;//用户手机号
    private Integer userId;//用户id
    private String video;//视频地址
    private String videoPic;//视频封面
    private Integer videoType;//视频类型 0 ：审核视频  1：普通视频
    private String createTime;// 提交审核时间
    private Integer videoCheck;// 审核code 0待审核 1 成功 2失败
    private String note;// 审核理由

}
