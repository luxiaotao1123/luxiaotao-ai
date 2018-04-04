package com.wuai.company.task.job;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.wuai.company.entity.Orders;
import com.wuai.company.enums.SceneSelEnum;
import com.wuai.company.task.concurrent.Executors;
import com.wuai.company.task.concurrent.OrderTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 任务调度的订阅者
 * Created by Ness on 2017/6/6.
 */
@Component
public class TaskDispatcherOrdersSubscriber {

    private Logger logger = LoggerFactory.getLogger(TaskDispatcherOrdersSubscriber.class);

    @Autowired
    private Executors executors;

    @Autowired
    private TaskOrderCalcBus bus;


    @Subscribe
    @AllowConcurrentEvents
    public void subscribe(Orders orders) {

        if (orders == null || orders.getSceneSelEnum() == null || orders.getOrderOperateEnum() == null) {
            logger.info("进行计算订单匹配逻辑所传参数不符合");
            return;
        }

        logger.info("开始进行计算订单匹配逻辑");
        executors.getInstance(SceneSelEnum.getSceneSelByChineseKey((orders.getSceneSelEnum()))).execute(new OrderTask(orders, bus));


    }

}
