package com.wuai.company.order.util.factory;

import com.wuai.company.entity.Orders;

import java.text.ParseException;

/**
 * Created by 97947 on 2017/7/27.
 */
public interface ReplaceNewShow {
    Boolean replaceNew(Integer userId, String scene, Integer selTimeType, Orders orders) throws ParseException;
}
