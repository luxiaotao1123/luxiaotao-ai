package com.wuai.company.party.listener;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.Response.PartyOrdersResponse;
import com.wuai.company.enums.PartyPayCodeEnum;
import com.wuai.company.enums.TimeTaskTypeEnum;
import com.wuai.company.message.PartySubscriber;
import com.wuai.company.message.StoreSubscriber;
import com.wuai.company.message.TransferData;
import com.wuai.company.party.dao.PartyDao;
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
public class PartyCalFreeistenter implements PartySubscriber {
    private Logger logger = LoggerFactory.getLogger(PartyCalFreeistenter.class);
    @Autowired
    private PartyDao partyDao;
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

            PartyOrdersResponse partyOrdersResponse = JSONObject.parseObject(transferData.getData(), PartyOrdersResponse.class);

            //其他待校验的-------
            if (partyOrdersResponse == null) {
                logger.warn("接受监听计算费用的partyOrdersResponse消息参数为空");
                return;
            }
            //获取订单uuid
            Integer size = partyOrdersResponse.getUuid().length();
            String uuid =null;
            //若订单的uuid 有拼接 “:” 则获取 其拼接头 以及订单号
            if (size>1){
                uuid = partyOrdersResponse.getUuid().split(":")[1];
            }
            String timeTaskName= partyOrdersResponse.getUuid().split(":")[0];

            if (timeTaskName.equals(TimeTaskTypeEnum.PARTY_CANCEL.getValue())){
                logger.info("--->PARTY订单 到达 时间后 若未 使用 返还金额<---");
                PartyOrdersResponse response =  userDao.findPartyByUid(uuid);
                if (response.getPayCode().equals(PartyPayCodeEnum.PARTY_WAIT_CONFIRM.getCode())){
                    userDao.upPartyPay(uuid,PartyPayCodeEnum.PARTY_CANCEL.getCode());
                    userDao.updateMoney(Double.valueOf(response.getMoney()),response.getUserId());
                }
            }


        } catch (Exception e) {
            logger.warn("接受监听计算费用的jms消息{}出错", transferData.getData(), e);
        }

    }



}

