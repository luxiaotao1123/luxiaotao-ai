package com.wuai.company.entity.Response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hyf on 2018/1/22.
 */

@Getter
@Setter
@ToString
public class NearBodyResponse implements Serializable {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    private Double distance;//距离
    private String icon;//头像
    private String nickName;//昵称
    private Integer gender;//性别  0男 1女
    private String zodiac;//星座
    private String age;//年龄
    private String height;//身高
    private String weight;//体重
    private String city;//城市
    private String signature;//个性签名
    private String time;//更新状态的时间
    private List<UserVideoResponse> videos;//视频


}

