package com.wuai.company.entity.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hyf on 2018/1/19.
 */
@Getter
@Setter
@ToString
public class TrystOrdersRequest implements Serializable{
    private String scene; //场景名称
    private String time;//时间
    private Double duration;//时长
    private String place;//地点
    private Integer sex;//性别
    private Double money;// 每人金额金额
    private Double advanceMoney;//预付金额
//    private Double advanceMoneyPercentage;//预付金额比例
    private Double longitude;//经度
    private Double latitude;//纬度
    private Integer personCount;//人数
    private Integer showName;//0显示 昵称 1 匿名发单
}
