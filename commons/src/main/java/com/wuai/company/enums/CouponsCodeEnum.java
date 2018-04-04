package com.wuai.company.enums;

/**
 * Created by hyf on 2018/1/12.
 * 优惠券类型
 */
public enum CouponsCodeEnum {
    DEFAULT("默认所有",0000),
    FIRST_RECHARGE_MONEY("首充优惠券",1001)
    ;
    private String value;
    private Integer code;

    CouponsCodeEnum(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }
}
