package com.wuai.company.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * rabbitMq的推送实现类
 * Created by Ness on 2017/5/30.
 */
@Component
public class RabbitMqPublishImpl implements Publisher {

    @Resource
    private RabbitTemplate orderRabbitTemplate;
    @Resource
    private RabbitTemplate storeRabbitTemplate;
    @Resource
    private RabbitTemplate timeTaskRabbitTemplate;
    @Resource
    private RabbitTemplate taskRabbitTemplate;
    @Resource
    private RabbitTemplate partyRabbitTemplate;
    @Resource
    private RabbitTemplate trystRabbitTemplate;



    private Logger logger = LoggerFactory.getLogger(RabbitMqPublishImpl.class);


    @Override
    public boolean publish(TransferData transferData) {

        if (transferData == null || transferData.getData() == null || transferData.getData().isEmpty()) {
            logger.warn("开始发送推送消息,参数为空");
            return false;
        }

        try {
            logger.info("开始发送推送消息={}", transferData.getData());

            switch (transferData.getRabbitTypeEnum()) {
                case ORDER:
                    orderRabbitTemplate.convertAndSend(transferData.getData());
                    break;
                case STORE:
                    storeRabbitTemplate.convertAndSend(transferData.getData());
                    break;
                case TIME_TASK:

                    timeTaskRabbitTemplate.convertAndSend(transferData.getData());
                    break;
                case TASK:
                    taskRabbitTemplate.convertAndSend(transferData.getData());
                    break;
                case PARTY:
                    partyRabbitTemplate.convertAndSend(transferData.getData());
                    break;
                case TRYST:
                    trystRabbitTemplate.convertAndSend(transferData.getData());
                default:
                    logger.warn("开始发送推送消息,传递的操作类型不对", transferData.getData());
                    return false;
            }


            logger.info("结束发送推送消息={}", transferData.getData());
            return true;
        } catch (Exception e) {
            logger.warn("发送推送消息={}失败", transferData.getData(), e);

        }
        return false;
    }


}
