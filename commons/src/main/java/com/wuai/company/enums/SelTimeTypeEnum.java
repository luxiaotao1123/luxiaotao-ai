package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/7/22.
 */
public enum SelTimeTypeEnum {
    FIXED("特定时间",0),
    PERIOD("固定周期",1);
    private String value;
    private Integer code;

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }

    SelTimeTypeEnum(String value, Integer code) {
        this.value = value;
        this.code = code;
    }
}
