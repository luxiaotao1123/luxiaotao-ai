package com.wuai.company.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hyf on 2018/1/12.
 */
@Getter
@Setter
public class CouponsOrders implements Serializable{
    private String uuid ;
    private String couponsUuid;//优惠券id
    private Integer userId;//用户id
    private String termOfValidity;//使用期限
    private String createTime;//优惠券获取时间
}
