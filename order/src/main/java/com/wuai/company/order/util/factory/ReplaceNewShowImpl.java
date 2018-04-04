package com.wuai.company.order.util.factory;

import com.wuai.company.entity.Orders;
import com.wuai.company.enums.PayTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * Created by 97947 on 2017/7/27.
 */
public class ReplaceNewShowImpl implements ReplaceNewShow {
    @Resource
    private HashOperations<String, String, Orders> taskHashRedisTemplate;
    private final String SCENENAME_ORDERS_PERHAPS_SELTIMETYPE = "%s:%s:orders:%s:%s";  // 用户id--场景名--订单--邀约/应约--固定时间/特定时间


    Logger logger = LoggerFactory.getLogger(ReplaceNewShowImpl.class);
    @Override
    public Boolean replaceNew(Integer userId, String scenes, Integer selTimeType, Orders orders) throws ParseException {
        Boolean flag=false;
        if (userId==null|| StringUtils.isEmpty(scenes)||selTimeType==null||orders==null){
            logger.warn("更新最新的订单显示到主页中 参数为空");
            return flag;
        }
        logger.info("将最新订单存入redis中。。。");

        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date=simpleDateFormat.parse(orders.getStartTime());
        Long timeStemp = date.getTime();
        //根据订单 hashkey, 开始时间戳 获取 orders
        Orders valueOrders =null;
        Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()));
        String key =null;
        for (String str :set){
            key = String.valueOf(str);
        }
        if (key==null){
            logger.info("redis中的订单为空 存入新订单");
            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),String.valueOf(timeStemp),orders);
        }else
        if (taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),key)!=null){
            valueOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),key);
        };
//        orders.setHourlyFee( scene.getHourlyFee());
        orders.setType(PayTypeEnum.STR_WAIT_CONFIRM.getValue());

            Date ordersInRedis = simpleDateFormat.parse(valueOrders.getStartTime());
            Long preTime = ordersInRedis.getTime();
            //若 订单的开始时间 <= 已存在 redis里的 订单开始时间则 更新

            if (timeStemp<=preTime){
                taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,valueOrders.getUserId(),valueOrders.getScenes(),valueOrders.getPerhaps(),valueOrders.getSelTimeType()),String.valueOf(preTime));
                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),String.valueOf(timeStemp),orders);
            }

        return Boolean.TRUE;
    }
}
