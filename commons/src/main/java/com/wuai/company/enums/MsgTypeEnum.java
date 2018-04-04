package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/8/28.
 */
public enum MsgTypeEnum {
    INVITATION_SEND_MSG("发出信息",1),  // 邀请 发送信息
    INVITATION_RECEIVE_MSG("接收信息",2),//邀请 接收信息
    JOIN_SEND_MSG("发出信息",3),//参加 接收信息
    JOIN_RECEIVE_MSG("接收信息",4),//参加 接收信息
    EXPEL_USER_MSG("踢出用户",5),//踢出用户 信息expelUser
    ARRIVED_PLACE_MSG("用户到达",6),//踢出用户 信息expelUser
    REFUSE_JOIN_MSG("拒绝",8),//拒绝
    SECEDE_MSG("退出订单",9),//退出订单secede
    SYS_MSG("系统消息",10000),//系统消息

    UPDATE_TRYST("赴约方订单修改",10),//赴约方订单修改通知
    COMPLETE_TRYST("赴约方完成订单",11),//赴约方订单完成通知
    CANCEL_DEMAND_UNPAY_TRYST("发单方未付款取消订单",12),//发单方未付款取消订单
    CANCEL_DEMAND_PAY_TRYST("发单方已付款取消订单",13),//发单方已付款取消订单
    CANCEL_SERVICE_PAY_TRYST("赴约方已付款取消订单",14),//赴约方已付款取消订单
    CANCEL_SNATCH_TRYST("发单方长时间未确认，订单已自动取消",15),//发单方长时间未确认，订单已自动取消
    ADVANCE_TRYST("赴约方订单开始前通知",16),//赴约方订单开始前通知
    TOBE_COMPLETE_TRYST("订单已到结束时间",17),//订单已到结束时间
    PLAY_MONEY_TRYST("订单分发赏金",18),//订单分发赏金
    EFFECT_TRYST("赴约方订单生效",19),//赴约方订单生效
    EVALUATION_MONEY_TRYST("评论打赏",20),//评论打赏
    DEMAND_EFFECT_TRYST("发单方订单生效",21),//发单方订单生效
    DEMAND_UPDATE_TRYST("发单方订单修改",22),//发单方订单修改
    DEMAND_ADVANCE_TRYST("发单方订单开始前通知",23),//发单方订单开始前通知
    ;

    private String value;
    private Integer code;

    public String getValue() {
        return value;
    }
    public Integer getCode() {
        return code;
    }

    MsgTypeEnum(){}

    MsgTypeEnum(String value, Integer code) {
        this.code = code;
        this.value = value;
    }
}
