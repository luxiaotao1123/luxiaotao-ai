package com.wuai.company.enums;

public enum MoneyRateEnum {
    FIRST_CANCEL("首次取消",0.05),
    NORMAL_CANCEL("常规取消",0.15),
    COMPLETE_RATE("订单完成手续费",0.2),
    GOOD_PRAISE_RATE("好评返现率",0.1),
    MEDIUN_PRAISE_RATE("中评返现率",0.05)
    ;


    private String value;
    private Double key;

    public String getValue() {
        return value;
    }

    public Double getKey() {
        return key;
    }

    MoneyRateEnum(String value, Double key){
        this.value = value;
        this.key = key;
    }

}
