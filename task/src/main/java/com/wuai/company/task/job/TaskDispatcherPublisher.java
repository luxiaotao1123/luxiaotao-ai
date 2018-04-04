package com.wuai.company.task.job;

import com.google.common.eventbus.AsyncEventBus;
import com.wuai.company.entity.Orders;
import com.wuai.company.task.concurrent.Executors;
import org.springframework.stereotype.Component;

import static com.wuai.company.task.concurrent.Executors.getCache;

/**
 * 任务的调度的发布者
 * Created by Ness on 2017/6/6.
 */
@Component
public class TaskDispatcherPublisher {

    public TaskDispatcherPublisher(TaskDispatcherOrdersSubscriber ordersSubscriber) {
        asyncEventBus = new AsyncEventBus(getCache());
        register(ordersSubscriber);
    }

    private AsyncEventBus asyncEventBus;


    private void register(TaskDispatcherOrdersSubscriber ordersSubscriber) {
        asyncEventBus.register(ordersSubscriber);

    }


    public void publish(Object obj) {
        asyncEventBus.post(obj);
    }




}
