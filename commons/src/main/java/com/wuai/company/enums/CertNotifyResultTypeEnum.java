package com.wuai.company.enums;

/**
 * Created by 97947 on 2017/7/10.
 */
public enum CertNotifyResultTypeEnum {
    FAIL(0),  //失败
    SUCCESS_PASS(1), //成功
    SUCCESS_DEPASS(2); //信用分不通过

    CertNotifyResultTypeEnum(Integer code) {
        this.code = code;
    }

    private Integer code;

    public Integer toCode() {
        return code;
    }
}
