package com.wuai.company.enums;

/**
 * 自定义返回状态的枚举
 * Created by Ness on 2017/5/25.
 */
public enum  ResponseTypeEnum {
    EMPTY_PARAM(201),
    ERROR_CODE(208),
    ORDERS_FAIL(209),
    ORDERS_FAIL2(210),
    RESET_LOAD_CODE(320),
    ATTESTATION_FAIL_CODE(206),//未进行芝麻认证
    PAY_PASS(207),//
    LOGIN_ERROR(601),REGISTER_ERROR(602)
    ;

    private int code;

    ResponseTypeEnum(int code) {
        this.code = code;
    }

    public int toCode() {
        return code;
    }

}
