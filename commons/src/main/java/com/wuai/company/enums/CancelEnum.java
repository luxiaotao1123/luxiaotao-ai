package com.wuai.company.enums;

/**
 * Created by hyf on 2018/1/25.
 * tryst 取消订单
 */
public enum CancelEnum {
    TRYST("发单用户",0),
    USER("认证用户",1),
    CHOOSE("未选人",0),
    DOWN("已选人",1),
    NOBODY("没有用户抢单",2);
    private String value;
    private Integer code;

    CancelEnum(String value, Integer code) {
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
