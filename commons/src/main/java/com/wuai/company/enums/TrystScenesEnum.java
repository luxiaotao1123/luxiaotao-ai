package com.wuai.company.enums;

/**
 * Created by hyf on 2018/1/19.
 */
public enum TrystScenesEnum {
    HOME("首页",0),
    MORE("更多",1);
    private String value;
    private Integer code;

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }

    TrystScenesEnum(String value, Integer code) {
        this.value = value;
        this.code = code;
    }
}
