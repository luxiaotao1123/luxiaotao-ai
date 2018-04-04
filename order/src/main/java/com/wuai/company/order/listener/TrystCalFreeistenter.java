package com.wuai.company.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.*;
import com.wuai.company.enums.*;
import com.wuai.company.message.RabbitMqPublishImpl;
import com.wuai.company.message.TransferData;
import com.wuai.company.message.TrystSubscriber;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.scheduler.service.TimeTaskServer;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.util.UserUtil;
import com.wuai.company.util.comon.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Auth vincent 2018/3/15
 * tryst监听
 */
@Component
public class TrystCalFreeistenter implements TrystSubscriber {

    private Logger logger = LoggerFactory.getLogger(TrystCalFreeistenter.class);

    private final String SNATCH_USER_TRYST = "snatch:user:%s"; //tryst 订单号
    private final String USER_MSG_TRYST = "tryst:%s:msg"; //用户id--信息列表
    private final String MSG_CONTENT_TRYST = "%s:%s:%s:%s:%s";

    @Autowired
    private TrystOrdersDao trystOrdersDao;

    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TimeTaskServer timeTaskServer;

    @Resource
    private HashOperations<String,String,SnatchUser> snatchUserTemplate;

    @Resource
    private ZSetOperations<String,String> msgValueTemplate;

    @Override
    public void subscribe(TransferData transferData) {
        if (transferData == null || transferData.getData() == null) {
            logger.warn("接受监听计算费用的jms消息参数为空");
            return;
        }
        logger.info("受监听计算费用的jms消息={}", transferData.getData());
        String timeTaskName = JSONObject.parseObject(transferData.getData(), TrystOrders.class).getUuid();

        String[] params = timeTaskName.split(":");
        String trystId = params[2];
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(trystId);
        if (trystOrders == null){
            logger.warn("接受监听计算费用的trystOrders消息参数为空");
            return;
        }
        User demand = userDao.findUserByUserId(trystOrders.getUserId());
        List<User> userList = userDao.selectUsersFromReceiveByTrystId(trystId);


        //删除过时的可抢订单
        switch (params[1]) {
            case "endSnatch":

                //如果订单还是等待抢单状态，执行
                if (trystOrders.getPayCode().equals(PayTypeEnum.TRYST_SNATCH_CONFIRM.toCode())) {
                    String uid = UserUtil.generateUuid();
                    String date = TimeUtil.afterTime(TimeUtil.currentTime(), 2, 0);
                    String today = TimeUtil.today();

                    //如果已有人抢单
                    if (snatchUserTemplate.values(String.format(SNATCH_USER_TRYST, trystId)).size() > 0) {

                        //创建取消订单 --  create t_tryst_cancel              id 需求方userId         uuid 订单号
                        trystOrdersDao.cancelTryst(uid, trystOrders.getUserId(), trystId, CancelEnum.TRYST.getCode(), CancelEnum.CHOOSE.getCode(), CancelEnum.CHOOSE.getValue(), today, date, 1, "到时间自动取消");
                        logger.warn("取消订单id={}已生成", uid);

                        //系统通知给所有抢单方，订单已取消
                        Set<String> keys = snatchUserTemplate.keys(String.format(SNATCH_USER_TRYST, trystId));

                        for (String userId : keys) {
                            long size = msgValueTemplate.size(String.format(USER_MSG_TRYST, userId));
                            String content = String.format(MSG_CONTENT_TRYST, userId, MsgTypeEnum.CANCEL_SNATCH_TRYST.getCode(), demand.getNickname(), trystId, TimeUtil.currentTime());
                            msgValueTemplate.add(String.format(USER_MSG_TRYST, userId), content, size + 1);
                        }

                    }


                    //未付款前修改订单订单状态为已取消
                    if (trystOrdersDao.calcelTrystOrders(trystId, PayTypeEnum.BEFORE_CANCEL_TRYST.toCode(), PayTypeEnum.BEFORE_CANCEL_TRYST.getValue()) == 0) {
                        return;
                    }

                    logger.warn("订单id={}状态——》已取消", trystId);

                }

                //订单开始前一个小时，通知所有用户
                break;
            case "advanceOneHour":

                if (trystOrders.getPayCode().equals(PayTypeEnum.TRYST_WAIT_CONFIRM.toCode())) {

                    for (User service : userList) {
                        //系统通知所有赴约方
                        long size = msgValueTemplate.size(String.format(USER_MSG_TRYST, service.getId()));
                        String content1 = String.format(MSG_CONTENT_TRYST, service.getId(), MsgTypeEnum.ADVANCE_TRYST.getCode(), demand.getNickname(), trystId, TimeUtil.currentTime());
                        msgValueTemplate.add(String.format(USER_MSG_TRYST, service.getId()), content1, size + 1);

                        //个推
                        Push push = new Push();
                        push.setDeviceNum(service.getCid());
                        push.setSendDeviceType(service.getType());
                        push.setSendTopic("订单开始提前通知");
                        String content = UserUtil.jsonPare(
                                "type," + ServerHandlerTypeEnum.ADVANCE_TRYST.getType(),
                                "trystId," + trystOrders.getUuid()
                        );
                        push.setSendContent(content);
                        PushUtils.userPush.getInstance().sendPush(push);
                    }

                    //系统通知发单方
                    long size = msgValueTemplate.size(String.format(USER_MSG_TRYST, demand.getId()));
                    String content1 = String.format(MSG_CONTENT_TRYST, demand.getId(), MsgTypeEnum.DEMAND_ADVANCE_TRYST.getCode(), demand.getNickname(), trystId, TimeUtil.currentTime());
                    msgValueTemplate.add(String.format(USER_MSG_TRYST, demand.getId()), content1, size + 1);

                    //个推
                    Push push = new Push();
                    push.setDeviceNum(demand.getCid());
                    push.setSendDeviceType(demand.getType());
                    push.setSendTopic("订单开始提前通知");
                    String content = UserUtil.jsonPare(
                            "type," + ServerHandlerTypeEnum.ADVANCE_TRYST.getType(),
                            "trystId," + trystOrders.getUuid()
                    );
                    push.setSendContent(content);
                    PushUtils.userPush.getInstance().sendPush(push);

                    logger.warn("订单id={}将在一小时后开始，系统消息已送达", trystId);


                    /*******************************************trystOrder:endTime:定时器****************************************************************/
                    TimeTask timeTask = new TimeTask();
                    timeTask.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
                    timeTask.setTimeTaskName("trystOrder:endTime:" + trystId);
                    timeTask.setExecuteTime(TimeUtil.getCurrentAfterCron(trystOrders.getDuration() + 1.0));
                    TrystOrders entity = new TrystOrders();
                    entity.setUuid("trystOrder:endTime:" + trystOrders.getUuid());
                    timeTask.setParams(JSON.toJSONString(entity));
                    TransferData data = new TransferData();
                    data.setData(JSONObject.toJSONString(timeTask));
                    data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
                    rabbitMqPublish.publish(data);
                    logger.warn("订单 id={} 结束时间已到 定时任务已发布！", trystId);

                }


                //通知发单方（订单时间已结束，可以手动完成订单。否则将在24小时后自动完成）任务
                break;
            case "endTime":

                //如果订单还是未完成状态，执行
                if (trystOrders.getPayCode().equals(PayTypeEnum.TRYST_WAIT_CONFIRM.toCode())) {

                    //系统通知发单方
                    long size = msgValueTemplate.size(String.format(USER_MSG_TRYST, demand.getId()));
                    String content = String.format(MSG_CONTENT_TRYST, demand.getId(), MsgTypeEnum.TOBE_COMPLETE_TRYST.getCode(), demand.getNickname(), trystId, TimeUtil.currentTime());
                    msgValueTemplate.add(String.format(USER_MSG_TRYST, demand.getId()), content, size + 1);


                    /******************************************自动完成定时器***************************************/

                    TimeTask timeTask = new TimeTask();
                    timeTask.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
                    timeTask.setTimeTaskName("trystOrder:autoComplete:" + trystId);
                    timeTask.setExecuteTime(TimeUtil.getCurrentAfterCron(24.0));
                    TrystOrders entity = new TrystOrders();
                    entity.setUuid("trystOrder:autoComplete:" + trystId);
                    timeTask.setParams(JSON.toJSONString(entity));
                    TransferData data = new TransferData();
                    data.setData(JSONObject.toJSONString(timeTask));
                    data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
                    rabbitMqPublish.publish(data);
                    logger.warn("订单 id={} 自动完成定时任务已发布！", trystId);

                }

                //自动完成订单任务
                break;
            case "autoComplete":

                //是否依旧是支付状态
                if (trystOrders.getPayCode().equals(PayTypeEnum.TRYST_WAIT_CONFIRM.toCode())) {

                    //修改订单状态
                    if (trystOrdersDao.calcelTrystOrders(params[2], PayTypeEnum.TRYST_SUCCESS.toCode(), PayTypeEnum.TRYST_SUCCESS.getValue()) == 0) {
                        return;
                    }

                    logger.warn("订单 id={} 定时任务已完成", trystId);

                    //每人份的金额
//                String amount = String.valueOf(trystOrders.getMoney());

                    //支付宝分发打赏
                    for (User user : userList) {

                        //平台钱包
                        trystOrdersDao.upMoney(user.getId(), trystOrders.getMoney() * (1 - MoneyRateEnum.COMPLETE_RATE.getKey()));
                        logger.warn("id={}用户钱包已收款{}元", user.getId(), trystOrders.getMoney());

                        //系统消息通知给所有用户（关于订单）订单已完成，钱已到账
                        long size = msgValueTemplate.size(String.format(USER_MSG_TRYST, user.getId()));
                        String content = String.format(MSG_CONTENT_TRYST, user.getId(), MsgTypeEnum.PLAY_MONEY_TRYST.getCode(), demand.getNickname(), trystId, TimeUtil.currentTime());
                        msgValueTemplate.add(String.format(USER_MSG_TRYST, user.getId()), content, size + 1);


                    }


                }

                break;
        }

        //删除持久层job
        timeTaskServer.autoDelete(timeTaskName);

    }

    public static void main(String[] args) {
    }
}
