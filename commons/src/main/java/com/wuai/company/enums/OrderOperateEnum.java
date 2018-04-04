package com.wuai.company.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单操作的枚举
 * Created by Ness on 2017/6/6.
 */
public enum OrderOperateEnum {

    ADD("add"), UPDATE("update"), DELETE("detete");
    private String key;

    OrderOperateEnum(String key) {
        this.key = key;
    }

    public String toKey() {
        return key;
    }
    private static Map<String, OrderOperateEnum> allCate = new ConcurrentHashMap<>();

    public static OrderOperateEnum getOperate(String key) {

        if (StringUtils.isEmpty(key)) {
            return null;
        }

        OrderOperateEnum operateEnum = allCate.get(key);

        if (operateEnum != null) {
            return operateEnum;
        }

        OrderOperateEnum[] values = OrderOperateEnum.values();

        int len = values.length;
        for (int i = 0; i < len; i++) {
            if (values[i].key.equals(key)) {
                allCate.put(key, values[i]);
                return values[i];
            }
        }
        return null;
    }


}
