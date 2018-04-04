package com.wuai.company.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by hyf on 2018/1/12.
 */
@Getter
@Setter
public class Coupons implements Serializable{
    private String uuid;
    private String couponsName;//优惠券类型名称
    private Integer couponsCode;//优惠券类型
    private String note;//使用条件
    private Double delMoney;//满足条件 减去的金额
    private Double contentMoney;//需要满足的金额
}
