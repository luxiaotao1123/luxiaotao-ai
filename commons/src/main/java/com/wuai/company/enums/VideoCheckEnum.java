package com.wuai.company.enums;

import lombok.Getter;

/**
 * Created by hyf on 2018/1/12.
 */

public enum  VideoCheckEnum {
    WAIT_PASS("审核视频",0),
    COMMON_VIDEO("普通视频",1),
    WAIT_CHECK("待审核",0),
    SUCCESS_CHECK("审核成功",1),
    FAIL_CHECK("审核失败",2);
    private String value;
    private Integer code;

    VideoCheckEnum(String value, Integer code) {
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
