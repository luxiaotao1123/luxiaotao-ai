package com.wuai.company.task.listener;

import com.wuai.company.message.TaskSubscriber;
import com.wuai.company.message.TransferData;
import com.wuai.company.task.job.TaskDispatcherFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * timTask的监听类
 */
@Component
public class TaskListenter implements TaskSubscriber {
    private Logger logger = LoggerFactory.getLogger(TaskListenter.class);


    @Autowired
    private TaskDispatcherFactory dispatcher;

    @Override
    public void subscribe(TransferData transferData) {
        try {



            if (transferData == null || transferData.getData() == null) {
                logger.warn("接受监听任务的jms消息参数为空");
                return;
            }

            logger.info("接受监听任务的jms消息={}", transferData.getData());

            dispatcher.allot(transferData.getData());

        } catch (Exception e) {
            logger.warn("监听任务的jms消息{}出错", transferData.getData(), e);
        }
    }
}
