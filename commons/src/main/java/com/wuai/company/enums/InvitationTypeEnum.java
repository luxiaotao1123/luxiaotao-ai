package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/7/21.
 */
public enum  InvitationTypeEnum {
    SERVICE("service", 1),
    DEMAND("demand", 2);
    private String value;
    private Integer code;

    InvitationTypeEnum(String value, Integer code) {
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
