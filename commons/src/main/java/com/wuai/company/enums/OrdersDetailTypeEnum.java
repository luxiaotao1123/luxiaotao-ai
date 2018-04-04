package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/9/25.
 * 订单明细枚举
 */
public enum OrdersDetailTypeEnum {
    /**
     * 约会订单 code: 1100
     * 商城订单 code: 1200
     * 约会支付 code: 1001 （现金支付/支付宝/微信/银行卡等）
     * 用户提现 code: 1002 （提现到/支付宝/微信/银行卡等）
     * 商城支付 code: 1003 （现金支付/支付宝/微信/银行卡等）
     * 商家提现 code: 1004 （提现到/支付宝/微信/银行卡等）
     * 退还金额 均退还至 余额
     */
    DEFAULT_ACCOUNT_NUMBER("系统默认账号",0),
    ORDERS("约会订单明细",1),
    STORE("商城订单明细",2),
    RECHARGE("充值消费余额收益",3),
    STORE_TASK("商城活动",4),
    PARTY("PARTY",5),
    RECHARGE_WALLET("钱包充值",6),
    ORDERS_PAY("支付约会订单",1001),
    USER_WITHDRAW("用户提现",1002),
    STORE_PAY("支付商城订单",1003),
    STORE_WITHDRAW("商家提现",1004),
    INSERT_CONSUME_MONEY("充值消费余额",1005),
    HIGHER_INCOME("上级会员收益",1006),
    ON_HIGHER_INCOME("上上级会员收益",1007),
    RECHARGE_MONEY("充值钱包余额",1008),
    ORDERS_RETURN_MONEY("退还约会金额到余额",1101),
    ORDERS_REVENUE("约会订单收入",1102),
    STORE_TASK_PAY("商城活动支付",1103),
    STORE_BACK_MONEY("商城活动返现",1104),
    PARTY_PAY("支付PARTY",1105),


    STORE_RETURN_MONEY("退还商城金额到余额",1201);

    private String value;
    private Integer key;

    public String getValue() {
        return value;
    }

    public Integer getKey() {
        return key;
    }

    OrdersDetailTypeEnum(){}
    OrdersDetailTypeEnum(String value, Integer key) {
        this.value = value;
        this.key = key;
    }
}
