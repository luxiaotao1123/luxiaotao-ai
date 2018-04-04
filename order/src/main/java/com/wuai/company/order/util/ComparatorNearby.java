package com.wuai.company.order.util;


import com.wuai.company.entity.NearbyBody;
import com.wuai.company.util.DistanceTools;

import java.util.Comparator;

/**
 * Created by 97947 on 2017/9/6.
 */
public class ComparatorNearby implements Comparator {
    @Override
    public  int compare(Object o1, Object o2) {
        NearbyBody nearbyBody = (NearbyBody)o1;
        NearbyBody nearbyBody2 = (NearbyBody)o2;
        int flag = nearbyBody.getDistance().compareTo(nearbyBody2.getDistance());
        return flag;
    }


}
