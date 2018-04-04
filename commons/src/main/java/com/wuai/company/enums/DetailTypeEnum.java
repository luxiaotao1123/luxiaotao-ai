package com.wuai.company.enums;

/**
 * Created by Administrator on 2017/6/28.
 * 账单类型
 */
public enum  DetailTypeEnum {
    INVITATION("约会订单",0),
    STORE("商城订单",1),
    OR_CODE("",2),
    RECHARGE("余额充值",3),
    TASK_PAY("活动订单",4),
    CONSUME_MONEY("消费余额充值",5),
    PARTY("PARTY订单",6),
    BE_GOLD_USER("充值金卡会员",7)
    ;

    private String value;
    private int code;

    DetailTypeEnum(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public int toCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
