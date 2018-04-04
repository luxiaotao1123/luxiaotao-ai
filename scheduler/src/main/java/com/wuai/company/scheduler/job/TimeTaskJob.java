package com.wuai.company.scheduler.job;


import com.wuai.company.entity.TimeTask;
import com.wuai.company.enums.RabbitTypeEnum;
import com.wuai.company.enums.TimeTaskTypeEnum;
import com.wuai.company.message.RabbitMqPublishImpl;
import com.wuai.company.message.TransferData;
import com.wuai.company.scheduler.utils.TimeTaskBus;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * timerTask的具体实现子类
 */
@Component
public class TimeTaskJob implements Job {
    private Logger logger = LoggerFactory.getLogger(TimeTaskJob.class);

    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;



    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Collection<Object> values = jobExecutionContext.getMergedJobDataMap().values();
        TimeTask task;

        for (Object obj : values.toArray()) {
            task = (TimeTask) obj;
            if (StringUtils.isEmpty(task.getParams())) {
                continue;
            }
            logger.info("开始执行定时任务{}", task);

            TransferData transferData = new TransferData();
            transferData.setData(task.getParams());
            String params = task.getParams();
            if (params.contains(TimeTaskTypeEnum.TRYST_ORDERS.getValue())){
                transferData.setRabbitTypeEnum(RabbitTypeEnum.TRYST);
            }else if (params.contains(TimeTaskTypeEnum.STORE_CYCLE.getValue())){
                transferData.setRabbitTypeEnum(RabbitTypeEnum.STORE);
            }else {
                transferData.setRabbitTypeEnum(RabbitTypeEnum.ORDER);
            }
            rabbitMqPublish.publish(transferData);

            logger.info("结束执行定时任务{}", task);
        }
    }


}
