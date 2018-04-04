package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/9/5.
 */
public enum MarkingSysEnum {
    APPRAISE_MARK("评价",1),
    SURE_MARK("主动确认",2),
    BREAK_TIME("爽约次数限制",3),
    COMPLAIN_TIME("投诉次数限制",3),
    BREAK_APPOINTMENT_MARK("爽约",3),
    COMPLAIN_MARK("投诉",4);
    private String value;
    private Integer key;

    public String getValue() {
        return value;
    }

    public Integer getKey() {
        return key;
    }

    MarkingSysEnum(){}

    MarkingSysEnum(String value, Integer key) {
        this.value = value;
        this.key = key;
    }
}
