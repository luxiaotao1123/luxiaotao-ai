package com.wuai.company.enums;

/**
 * 自定义返回状态的枚举
 */
public enum  OrdersEnum {
    ACTIVE_CODE(0),
    HAND_DEAD(1),
    COMPLETE_CODE(301),
    BASIC_CODE(300), //
    NONE_CODE(299),
    AUTO_DEAD(2);

    private int code;

    OrdersEnum(int code) {
        this.code = code;
    }

    public int toCode() {
        return code;
    }

}
