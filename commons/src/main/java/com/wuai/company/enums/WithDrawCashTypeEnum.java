package com.wuai.company.enums;

/**
 * Created by Administrator on 2017/6/30.
 */
public enum WithDrawCashTypeEnum {
    COMMON_MEMBER(0),
    GRADE_MEMBER(1),
    MERCHANT_MEMBER(2);
    private int code;

    WithDrawCashTypeEnum(int code) {
        this.code = code;
    }

    public int toCode() {
        return code;
    }
}
