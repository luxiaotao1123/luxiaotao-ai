package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/8/30.
 */
public enum CommonTalkEnum {
    THIRTY_MINUTES_TALK("30分钟前常用语",1),
    COMMON_TALK("普通常用语",2);
    private String value;
    private Integer code;

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }
    CommonTalkEnum(String value,Integer code){
        this.value=value;
        this.code=code;
    }
}
