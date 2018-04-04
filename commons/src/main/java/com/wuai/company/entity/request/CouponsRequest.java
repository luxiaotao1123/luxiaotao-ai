package com.wuai.company.entity.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by hyf on 2018/1/15.
 */
@Getter
@Setter
@ToString
public class CouponsRequest implements Serializable {
    private String uuid;
    @NonNull
    private String couponsName;//优惠券名称
    @NonNull
    private Integer couponsCode;//优惠券code
    @NonNull
    private String note;//提示
    @NonNull
    private Double delMoney;//满减需要减去的金额
    @NonNull
    private Double contentMoney;//满足的金额

}
