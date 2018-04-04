package com.wuai.company.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 订单类型的枚举
 * Created by Administrator on 2017/7/5.
 */
public enum OrdersTypeEnum {
    STORE_ORDER(0, '0'),
    INVITE_ORDER(1, '1'),
    STORE_TASK_PAY(2,'t'),//任务支付
    CONSUME_MONEY(3,'m') ,//消费余额充值
    PARTY_PAY(4,'p'), //PARTY支付
    RECHARGE(5,'r'), //余额充值
    GOLD_USER(6,'g'), //黄金会员
    TRYST_ORDERS(7,'o'), //             第一次付款：约会订单 确认，支付全部余额
    TRYST_ADVANCE_MONEY(8,'a'), //约会订单advanceMoney 预付金额支付
    CANCEL_TRYST_SERVICE(9,'c'),     //取消订单
    CANCEL_ROLE_USER(10,'u'),        //用户为赴约方
    COMPLETE_TRYST_SERVICE(11,'s');
    ;
    private int type;
    private char quote;

    OrdersTypeEnum(int type, char quote) {
        this.quote = quote;
        this.type = type;
    }
    public int toCode() {
        return type;
    }
    public char getQuote() {
        return quote;
    }

    /**
     * 根据订单判断所属订单类型
     * @param orderNO 订单编号
     * @return
     */
    public static OrdersTypeEnum judgeOrderTypeByOrders(String orderNO) {
        if (StringUtils.isEmpty(orderNO)) {
            return STORE_ORDER;
        }
        OrdersTypeEnum[] values = OrdersTypeEnum.values();
        int len = values.length;
        for (int i = 0; i < len; i++) {
            if (orderNO.charAt(0) == values[i].quote) {
                return values[i];
            }
        }
        return STORE_ORDER;
    }


    public static void main(String[] args) {
        System.out.println(OrdersTypeEnum.CONSUME_MONEY.getQuote());
    }
}
