package com.wuai.company.enums;

/**
 * Created by hyf on 2017/12/18.
 */
public enum GenderEnum {
   BOY(0),GIRL(1);
   private Integer code;

    GenderEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
