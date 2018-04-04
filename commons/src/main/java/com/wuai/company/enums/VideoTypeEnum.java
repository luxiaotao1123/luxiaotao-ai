package com.wuai.company.enums;

/**
 * Created by hyf on 2018/1/29.
 */
public enum VideoTypeEnum {
    HOME("首页",0);
    private String value;
    private Integer code;

    VideoTypeEnum(String value, Integer code) {
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

