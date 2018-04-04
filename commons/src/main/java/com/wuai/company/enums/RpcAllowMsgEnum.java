package com.wuai.company.enums;

/**
 * prc的允许的枚举
 * Created by Ness on 2017/7/6.
 */
public enum RpcAllowMsgEnum {


    NOTIFY("notify"), MSG("msg"), TRYST("tryst");

    private String key;

    RpcAllowMsgEnum(String key) {
        this.key = key;
    }


    public String toKey() {
        return key;
    }

}
