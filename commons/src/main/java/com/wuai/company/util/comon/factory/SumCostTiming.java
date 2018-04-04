package com.wuai.company.util.comon.factory;

import com.wuai.company.util.Arith;

/**
 * Created by Administrator on 2017/6/1.
 */
public class SumCostTiming extends Arith implements Cost {
    @Override
    public Double calculate(Double hourlyFee, Integer personCount, Double timeInterval, String startTime, Double gratefulFree) {
        return null;
    }

    @Override
    public Double invitation(Double hourlyFee, Double timeInterval, String startTime, Double gratefulFree) {
        System.out.println(hourlyFee+"---"+timeInterval+"---"+gratefulFree);
        Double mul = Arith.multiplys(3,hourlyFee,timeInterval);
        if (gratefulFree!=null){
            Double sum = Arith.add(3,mul,gratefulFree);
            return sum;
        }
        return mul;
    }
}
