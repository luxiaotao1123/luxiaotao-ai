package com.wuai.company.enums;

/**
 * 订单发布的状态
 * Created by Ness on 2017/6/6.
 */
public enum OrderPublishTypeEnum {
    SERVICE("service", 0), DEMAND("demand", 1);
    private String value;
    private Integer code;

    OrderPublishTypeEnum(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public String toValue() {
        return value;
    }

    public int toCode() {
        return code;
    }

    public String toOppositeValue() {
        OrderPublishTypeEnum[] values = OrderPublishTypeEnum.values();

        int len = values.length;
        for (int i = 0; i < len; i++) {
            if (!values[i].value.equals(value)) {
                return values[i].value;
            }
        }
        return value;

    }


    public static OrderPublishTypeEnum getEnums(Integer code) {

        OrderPublishTypeEnum[] values = OrderPublishTypeEnum.values();

        int len = values.length;
        for (int i = 0; i < len; i++) {
            if (values[i].code.equals(code)) {
                return values[i];
            }
        }
        return null;

    }


}
