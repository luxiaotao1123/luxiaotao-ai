package com.wuai.company.order.util;

import com.wuai.company.entity.Response.NearBodyResponse;

import java.util.Comparator;

/**
 * Created by hyf on 2018/1/22.
 */
public class ComparatorNearBody implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        NearBodyResponse nearbyBody = (NearBodyResponse)o1;
        NearBodyResponse nearbyBody2 = (NearBodyResponse)o2;
        int flag = nearbyBody.getDistance().compareTo(nearbyBody2.getDistance());
        return flag;
    }
}
