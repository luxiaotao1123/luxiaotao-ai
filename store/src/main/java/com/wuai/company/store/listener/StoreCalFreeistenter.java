package com.wuai.company.store.listener;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.StoreOrders;
import com.wuai.company.enums.PayTypeEnum;
import com.wuai.company.enums.TimeTaskTypeEnum;
import com.wuai.company.message.StoreSubscriber;
import com.wuai.company.message.TransferData;
import com.wuai.company.store.dao.StoreDao;
import com.wuai.company.user.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by hyf on 2017/11/29.
 * store的监听类
 */

@Component
public class StoreCalFreeistenter implements StoreSubscriber{
    private Logger logger = LoggerFactory.getLogger(StoreCalFreeistenter.class);
    @Autowired
    private StoreDao storeDao;
    @Autowired
    private UserDao userDao;
    @Override
    public void subscribe(TransferData transferData) {
        try {

            if (transferData == null || transferData.getData() == null) {
                logger.warn("接受监听计算费用的jms消息参数为空");
                return;
            }
            logger.info("受监听计算费用的jms消息={}", transferData.getData());

            StoreOrders storeOrders = JSONObject.parseObject(transferData.getData(), StoreOrders.class);

            //其他待校验的-------
            if (storeOrders == null) {
                logger.warn("接受监听计算费用的storeOrders消息参数为空");
                return;
            }
            //获取订单uuid
            Integer size = storeOrders.getUuid().length();
            String uuid =null;
            //若订单的uuid 有拼接 “:” 则获取 其拼接头 以及订单号
            if (size>1){
                uuid = storeOrders.getUuid().split(":")[1];
            }
            String timeTaskName= storeOrders.getUuid().split(":")[0];

            if (timeTaskName.equals(TimeTaskTypeEnum.STORE_CYCLE.getValue())){
                logger.info("--->商城订单周期 时间后 若未 使用 返还金额<---");
                //订单已完成不操作
                StoreOrders store = storeDao.findStoreByUuid(uuid);
                if (store.getType().equals(PayTypeEnum.STORE_WAIT_CONFIRM.toCode())){
                    storeDao.backMoney(store.getUserId(),store.getMoney());
                    userDao.payStoreOrders(store.getUuid(),PayTypeEnum.STORE_OUT_OF_TIME.toCode());
                    // TODO: 2017/11/29 写个 金额退还记录 
                }
            }


        } catch (Exception e) {
            logger.warn("接受监听计算费用的jms消息{}出错", transferData.getData(), e);
        }

    }



}

