package com.wuai.company.enums;

/**
 * Created by hyf on 2017/12/27.
 * 审核
 */
public enum  CxamineAndVerify {
    WAIT_CONFIRM("待审核"),
    PASS_THROUGH("通过"),
    UN_PASS_THROUGH("未通过");
    private String value;

    CxamineAndVerify(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
