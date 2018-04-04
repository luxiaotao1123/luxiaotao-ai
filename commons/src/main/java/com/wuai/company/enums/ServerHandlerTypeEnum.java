package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/7/18.
 */
public enum ServerHandlerTypeEnum {
    INVITATION("邀请",1),//邀请接单---邀请
    ACCEPT("应约",2),//接受邀请---应约
    REFUSE_INV("拒绝邀请",5),//拒绝邀请
    REFUSE_JOIN("拒绝加入",6),//拒绝加入
//    JOIN("参加",3),//接受邀请---参加
    STORE_SURE("确认",3),//商家确认订单
    STORE_CANCEL("取消",4),//商家取消订单
    SNACTH_TRYST("抢单页面",7),//可抢单通知
    WAIT_TRYST("待完成",8),//需求方付款后，待完成通知赴约方
    COMPLETE_TRYST("已完成",9),//订单完成后，个推给赴约方
    CANCEL_SERVICE_TRYST("赴约方取消订单",10),//赴约方取消订单，个推给发单方
    CANCEL_DEMAND_TRYST("发单方取消订单",11),//发单方取消订单，个推给赴约方
    UPDATE_TRYST("发单方修改订单时间",12),//发单方修改订单时间，个推给所有赴约方
    ADVANCE_TRYST("订单开始前通知",13)//订单开始前一个小时通知所有人
    ;
    private Integer type;
    private String code;

    public String getCode() {
        return code;
    }

    public Integer getType() {
        return type;
    }

    ServerHandlerTypeEnum(Integer type) {
        this.type = type;
    }

    ServerHandlerTypeEnum( String code,Integer type) {
        this.type = type;
        this.code = code;
    }
}
