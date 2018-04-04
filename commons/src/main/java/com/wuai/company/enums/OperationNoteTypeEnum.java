package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/9/22.
 * 管理员操作记录
 */
public enum OperationNoteTypeEnum {
    ADMIN_LOGIN("登录",0),
    ADMIN_LOGOUT("退出",-1),

    ACTIVE_ADD("添加活动",1),
    ACTIVE_DELETE("删除活动",2),
    ACTIVE_UPDATE("修改活动",3),
    ADMIN_DELETED("删除管理员",4),
    LABEL_DELETED("标签删除",5),
    LABEL_UPDATE("标签修改",6),
    LABEL_ADD("标签添加",7),
    SCENE_UPDATE("场景系统参数修改",8),
    SCENE_INSERT("添加场景",9),
    SCENE_DELETE("删除场景",10),
    ADMIN_REGISTER("添加管理员",11),
    STOP_BACK_MONEY("停止用户返现",12),
    START_BACK_MONEY("开启用户返现",13),
    ADD_REGISTER("添加商城活动",14),
    UPDATE_REGISTER("修改商城活动",15),
    DELETED_REGISTER("删除商城活动",16),
    UPDATE_EXPRESS("更新发货信息",17),
    SUCCESS_VIDEO("视频审核成功",18),
    FAIL_VIDEO("视频审核失败",19),
    CASH_FAIL("提现操作成功",20),
    CASH_SUCCESS("提现操作失败",21),
    COUPONS_ADD("添加优惠券",22),
    COUPONS_UP("修改优惠券",23),
    COUPONS_DEL("删除优惠券",24),
    RECHARGE_WALLET("钱包充值",25),
    ;
    private String value;
    private Integer code;

    public String getValue() {
        return value;
    }
    OperationNoteTypeEnum() {}
    OperationNoteTypeEnum(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

}
