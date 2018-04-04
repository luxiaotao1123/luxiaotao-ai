package com.wuai.company.enums;

/**
 * Created by Administrator on 2017/6/28.
 * 支付类型
 */
public enum PayTypeEnum {
    SEND_INVITATION("邀请",-1),
    SEND_JOIN("加入",-2),
    PARTY_IN_ADVANCE("待选人",1),         //暂时用不到
    PARTY_WAIT_CONFIRM("等待抢单",2),
    TRYST_SNATCH_CONFIRM("等待抢单",1),           //第一步、创建订单后
    STR_WAIT_PAY("待支付",0),  //待支付状态
    STORE_WAIT_PAY("待支付",0),  //待支付状态
    STR_WAIT_CONFIRM("等待接单",1),
    STORE_WAIT_CONFIRM("待消费",1),
    TRYST_WAIT_CONFIRM("待完成",2),            //第二步、需求方付款后，等待约会开始
    TRYST_SUCCESS("已完成",5),                 //第三步、订单完成
    STR_SUCCESS("已完成",2),
    STORE_SUCCESS("已消费",2),
    STR_CANCEL("已取消",3),
    BEFORE_CANCEL_TRYST("已取消",3),              //分支一、需求方付款前取消订单
    AFTER_CANCEL_TRYST("已取消",4),              //分支二、需求方付款后取消订单
    STORE_CANCEL("已取消",3),
    STORE_OUT_OF_TIME("已过期",4),
    STR_ON_THE_WAY("已接单",5),
    STR_START("进行中",6),
    STR_WAIT_START("待进行",7),

    PAY_ZFB("支付宝支付",1000),
    PAY_YE("余额支付",1001),
    PAY_WX("微信支付",1002),
    PAY(-1), //支出
    TAKE_IN(1); //收入
//    WAIT_PAY(0), //等待支付
//    WAIT_CONFIRM("待确认",1), //支付完成 等待确认
//    SUCCESS("已完成",2), //确认成功
//    CANCEL("已取消",3), //取消
//    ON_THE_WAY(5), //进行中
//    STORE_WAIT_PAY("待支付",4); //商城订单待付款
    private String value;
    private int code;

    PayTypeEnum(int code) {
        this.code = code;
    }

    PayTypeEnum(String value,int code){
        this.value=value;
        this.code=code;
    }

    public int toCode() {
        return code;
    }
    public String getValue() {
        return value;
    }







}
