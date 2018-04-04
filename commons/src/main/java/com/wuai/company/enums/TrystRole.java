package com.wuai.company.enums;

/**
 * @Auth    vincent     2018/3/14
 */
public enum TrystRole {
    TRYST_ROLE_DEMAND("发单方",0),
    TRYST_ROLE_SERVICE("赴约方",1);

    private String value;
    private int code;

    TrystRole(String value,int code){
        this.value = value;
        this.code = code;
    }

    public int toCode() {
        return code;
    }
    public String getValue() {
        return value;
    }

}
