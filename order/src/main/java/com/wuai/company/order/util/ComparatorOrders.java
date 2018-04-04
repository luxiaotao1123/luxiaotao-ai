package com.wuai.company.order.util;

import com.wuai.company.entity.Orders;

import java.util.Comparator;

/**
 * Created by 97947 on 2017/9/6.
 */
public class ComparatorOrders implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        Orders orders1 = (Orders)o1;
        Orders orders2 = (Orders)o2;
        int flag = Long.valueOf(orders1.getStartTimeStamp()).compareTo(Long.valueOf(orders2.getStartTimeStamp()));
        return flag;
    }


}
