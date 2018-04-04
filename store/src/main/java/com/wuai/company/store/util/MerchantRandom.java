package com.wuai.company.store.util;

import java.util.Random;

/**
 * Created by Administrator on 2017/6/20.
 */
public class MerchantRandom {
    /**
     * 根据长度生成随机数
     * @param length
     * @return
     */
    public static String randomByLength(int length) {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<length;i++)
        {
            sb.append(rand.nextInt(10));
        }
        String data=sb.toString();
        return data;
    }
}
