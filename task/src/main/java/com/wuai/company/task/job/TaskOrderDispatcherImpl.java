package com.wuai.company.task.job;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.Orders;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单任务的调度中心
 * Created by Ness on 2017/6/6.
 */
@Component
public class TaskOrderDispatcherImpl implements TaskDispatcherFactory {


    @Autowired
    private TaskDispatcherPublisher publisher;


    private Logger logger = LoggerFactory.getLogger(TaskOrderDispatcherImpl.class);


    @Override
    public void allot(String msg) {

        if (StringUtils.isEmpty(msg)) {
            logger.warn("所传的参数为空,不参与调度分配");
            return;
        }
        logger.info("开始接受参数={}进行分配", msg);

        Class<?> clazz = Orders.class;


        publisher.publish(JSONObject.parseObject(msg, clazz));

        logger.info("结束接受参数={}进行分配", msg);

    }

    @Override
    public void calc() {
        throw new RuntimeException("not implements this method");
    }

    @Override
    public void sum() {

    }
}
