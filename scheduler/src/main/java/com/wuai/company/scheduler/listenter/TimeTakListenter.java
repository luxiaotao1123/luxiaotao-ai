package com.wuai.company.scheduler.listenter;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.TimeTask;
import com.wuai.company.message.TimeTaskSubscriber;
import com.wuai.company.message.TransferData;
import com.wuai.company.scheduler.utils.TimeTaskBus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * timTask的监听类
 */
@Component
public class TimeTakListenter implements TimeTaskSubscriber {
    private Logger logger = LoggerFactory.getLogger(TimeTakListenter.class);
    @Autowired
    private TimeTaskBus timeTaskBus;



    @Override
    public void subscribe(TransferData transferData) {
        try {

            if (transferData == null || transferData.getData() == null) {
                logger.warn("接受监听定时任务的jms消息参数为空");
                return;
            }


            logger.info("接受监听定时任务的jms消息={}", transferData.getData());


            TimeTask timeTask = JSONObject.parseObject(transferData.getData(), TimeTask.class);



            if (timeTask == null || timeTask.getScheduleOperaEnum() == null ||
                    StringUtils.isEmpty(timeTask.getTimeTaskName())||StringUtils.isEmpty(timeTask.getExecuteTime())) {
                logger.warn("接受监听定时任务的timeTask消息参数为空");
                return;
            }


            timeTaskBus.execute(timeTask);

        } catch (Exception e) {
            logger.warn("监听定时任务的jms消息{}出错", transferData.getData(), e);
        }
    }
}
