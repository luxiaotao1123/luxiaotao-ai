package com.wuai.company.util.comon.factory;

/**
 * Created by Administrator on 2017/6/1.
 */
public interface Cost {
    Double calculate(Double hourlyFee,Integer personCount,Double timeInterval,String startTime,Double gratefulFree);
    Double invitation(Double hourlyFee,Double timeInterval,String startTime,Double gratefulFree);
}
