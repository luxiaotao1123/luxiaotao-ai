package com.wuai.company.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by hyf on 2017/10/18.
 */
@Getter
@Setter
@ToString
public class NearbyBody implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 附近的人id
     */
    private String uuid;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 用户id
     */
    private Integer userId;

    private Double distance;

    private String icon;

}
