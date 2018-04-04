package com.wuai.company.entity.Response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hyf on 2018/1/15.
 */
@Getter
@Setter
public class CouponsOrdersResponse implements Serializable {
    private String uuid ;
    private String couponsUuid;//优惠券id
    private Integer userId;//用户id
    private String termOfValidity;//使用期限
    private String createTime;//优惠券获取时间
    private Integer status;//使用状态
    private String couponsName;//优惠券名称
    private Integer couponsCode;//优惠券Code
    private String note;//使用提示语句
    private Double delMoney;//减去的金额
    private Double contentMoney;//满足的金额
    private String content;//  满减
}
