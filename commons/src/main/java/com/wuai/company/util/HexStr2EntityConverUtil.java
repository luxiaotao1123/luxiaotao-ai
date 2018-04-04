package com.wuai.company.util;

import com.wuai.company.entity.Orders;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 转实体类
 * Created by 97947 on 2017/7/20.
 */
public class HexStr2EntityConverUtil {

    private static Logger logger = LoggerFactory.getLogger(HexStr2EntityConverUtil.class);

    public static Orders obj2Orders(Object obj) {
        Orders orders = new Orders();

        try {
            BeanUtils.copyProperties(orders, obj);
        } catch (Exception e) {
            logger.warn("类型转换出错", e);
        }
        return orders;
    }

}
