package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/7/22.
 */
public enum TimeTaskTypeEnum {
    ORDERS("orders"),
    CYCLE_ORDERS("cycleOrders"), //周期订单 用于更新 周期时间戳
    MISS_CATCH_PERSON_COUNT("missCatchPersonCount"),//邀请订单开始时间
    DEMAND_MISS_CATCH_PERSON_COUNT("demandMissCatchPersonCount"),//应约订单开始时间
    UNDONE_ORDERS("undoneOrders"),//发出邀请、参加订单 2小时候取消订单
    DELAY_TIME_ORDERS("delayTime"),// 手动 确认 延时 计算费用
    ROBOTIC_DELAY_TIME("roboticDelayTime"),// 自动确认 延时 计算费用
    SETTLE_ACCOUNTS_INVITATION("settleAccountsInvitation"),//到达订单结束时间后
    BACK_MONEY_TIME_TASK("backMoneyTimeTask"),//定时返还金额
    STORE_CYCLE("storeCycle"),//商城订单周期 时间后 若未 使用 返还金额
    PARTY_CANCEL("partyCancel"),//Party订单到期后 未使用 取消 订单
    TRYST_ORDERS("trystOrder")//约会订单
    ;
    private String value;

    public String getValue() {
        return value;
    }

    TimeTaskTypeEnum(String value){
        this.value=value;
    }
}
