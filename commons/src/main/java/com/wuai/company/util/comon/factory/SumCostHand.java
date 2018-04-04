package com.wuai.company.util.comon.factory;

import com.wuai.company.util.Arith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/6/1.
 */
public class SumCostHand extends Arith implements Cost {
    private Logger logger = LoggerFactory.getLogger(SumCostHand.class);
    @Override
    public Double calculate(Double hourlyFee, Integer personCount, Double timeInterval, String startTime,Double gratefulFree) {
        Double mul = Arith.multiplys(3,hourlyFee,personCount,timeInterval);

        if (gratefulFree!=null){
            Double sum = Arith.add(3,mul,gratefulFree);
            return sum;
        }
       return mul;
    }

    @Override
    public Double invitation(Double hourlyFee, Double timeInterval, String startTime, Double gratefulFree) {
        Double mul = Arith.multiplys(3,hourlyFee,timeInterval);
        if (gratefulFree!=null){
            mul= Arith.add(3,mul,gratefulFree);
        }
        return mul;
    }
}
