package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/8/3.
 */
public enum  SysKeyEnum {
    STOP_BACK("停止返现","1"),
    START_BACK("开启返现","0"),
    ADVERTISING_MAP("广告宣传","advertising_map"),
    NEARBY_BODY_DISTANCE("附近的人 距离配置","nearby_body_distance"),
    NEARBY_BODY_MAX_DISTANCE("最大距离 不更新 经纬度 ","nearby_body_max_distance"),
    ADVERTISING_PIC("首页打开图片","advertising_pic"),
    BROKERAGE("约会佣金","brokerage"),
    BEFORE_END_TIME("订单开始前多久可取消订单","before_end_time"),
    ANDROID_VERSION("安卓版本控制","android_version"),
    IOS_VERSION("IOS版本控制","ios_version"),
    ACTIVE_PIC("活动图片","active_pic"),
    LABEL_BOY_BUSINESS("男生职业","app_boy_business"),
    LABEL_GIRL_BUSINESS("女生职业","app_girl_business"),
    LABEL_BOY_FEATURE("男生性格","app_prefer_boy_feature"),
    LABEL_GIRL_FEATURE("女生性格","app_prefer_girl_feature"),
    LABEL_BOY_INSTEREST("男生爱好","app_prefer_boy_interest"),
    LABEL_GIRL_INSTEREST("女生爱好","app_prefer_girl_interest"),
    BALANCE_RECHARGE("消费余额充值活动","balance_recharge"),
    HIGHER_INCOME("上级收益","higher_income"),
    ON_HIGHER_INCOME("上上级收益","on_higher_income"),
    MIN_WITHDRAW_MONEY("最低提现金额","min_withdraw_money"),
    MIN_BACK_MONEY("最低可返现金额","min_back_money"),
    STOP_BACK_MONEY("返现限制金额","stop_back_money"),
    COMBO_END_TIME("套餐周期","combo_end_time"),
    PARTY_CALSSIFY("派对分类","party_classify"),
    GOLD_USER("黄金会员","gold_user"),
    BACK_MONEY_PERCENTAGE("每天返现百分比","back_money_percentage");


    private String value;
    private String key;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    SysKeyEnum(String value, String key) {
        this.value = value;
        this.key = key;
    }

    @Override
    public String toString() {
        return "SysKeyEnum{" +
                "value='" + value + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    SysKeyEnum(String key) {
        this.key = key;
    }
    
}
