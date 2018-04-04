package com.wuai.company.enums;

/**
 * 用户等级枚举
 * Created by Ness on 2017/6/13.
 */
public enum UserGradeEnum {
    NORMAL_PERSON(0), //普通用户
    GOLD_USER(1); //黄金会员

    private int code;

    UserGradeEnum(int code) {
        this.code = code;
    }

    public int toCode() {
        return code;
    }

}
