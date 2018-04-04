package com.wuai.company.enums;

/**
 * Created by hyf on 2017/12/18.
 */
public enum PartyPayCodeEnum{
    PARTY_PAY_WAIT("待支付",0),
    PARTY_WAIT_CONFIRM("待确定",1),
    PARTY_SUCCESS("已完成",2),
    PARTY_CANCEL("已取消",3)
    ;
    private String value;
    private Integer code;

    PartyPayCodeEnum(String value, Integer code) {
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
