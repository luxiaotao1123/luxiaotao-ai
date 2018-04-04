package com.wuai.company.util.comon.factory;

/**
 * Created by Administrator on 2017/6/1.
 */
public class CalculationFactory {
    public static Cost hand(){
        return new SumCostHand();
    }
    public static Cost timing(){
        return new SumCostTiming();
    }
}
