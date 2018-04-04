package com.wuai.company.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.TimeTask;
import com.wuai.company.enums.RabbitTypeEnum;
import com.wuai.company.enums.ScheduleOperaEnum;
import com.wuai.company.message.RabbitMqPublishImpl;
import com.wuai.company.message.TransferData;
import com.wuai.company.entity.Orders;
import com.wuai.company.util.comon.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Created by Ness on 2017/6/1.
 */
//@Configuration
public class Test {
    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;

    @PostConstruct
    public void init() {
        TransferData data = new TransferData();
        TimeTask task = new TimeTask();
        task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        task.setTimeTaskName("test");
        task.setExecuteTime("0/10 * * * * ?");
        Orders r = new Orders();
        r.setId(1);
//        r.setDeleted(Boolean.FALSE);
        r.setDeleted(0);
        r.setPerhaps(1);
        task.setParams(JSON.toJSONString(r));
        data.setData(JSONObject.toJSONString(task));

        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data);
    }

    public static void main(String[] args) {
        String time = TimeUtil.getCurrentAfterCron(20);
        String date = TimeUtil.getfrontTimeCron("2018-04-04 00:59",1.0);
        TimeUtil.getfrontTimeCron("2018-04-04 23:59",1.0);
        System.out.println(date);
    }
}
