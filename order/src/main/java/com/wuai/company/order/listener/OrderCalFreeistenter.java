package com.wuai.company.order.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.Maps;
import com.wuai.company.entity.Orders;
import com.wuai.company.entity.Response.Scene;
import com.wuai.company.entity.TimeTask;
import com.wuai.company.entity.User;
import com.wuai.company.enums.*;
import com.wuai.company.message.OrderSubscriber;
import com.wuai.company.message.RabbitMqPublishImpl;
import com.wuai.company.message.TransferData;
import com.wuai.company.order.dao.OrdersDao;
import com.wuai.company.order.entity.OrdersReceive;
import com.wuai.company.order.service.impl.OrdersServiceImpl;
import com.wuai.company.task.job.TaskOrderCalcBus;
import com.wuai.company.user.service.UserService;
import com.wuai.company.util.Arith;
import com.wuai.company.util.UserUtil;
import com.wuai.company.util.comon.SimpDate;
import com.wuai.company.util.comon.SimpDateFactory;
import com.wuai.company.util.comon.factory.CalculationFactory;
import com.wuai.company.util.comon.factory.Cost;
import com.wuai.company.user.dao.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * orders的监听类
 */
@Component
public class OrderCalFreeistenter implements OrderSubscriber {
    private Logger logger = LoggerFactory.getLogger(OrderCalFreeistenter.class);
    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TaskOrderCalcBus taskOrderCalcBus;
    @Resource
    private ZSetOperations<String,Orders> undoneRedisTemplate;
    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;

    @Value("${sys.robotization}")
    private Double robotization;

    @Value("${invitation.personCount.proportion}")
    private Double proportion;
    @Resource
    private HashOperations<String, String, Orders> taskHashRedisTemplate;
    private final String SCENENAME_ORDERS_PERHAPS_SELTIMETYPE = "%s:%s:orders:%s:%s";  // 用户id--场景名--订单--邀约/应约--固定时间/特定时间
    private final String USER_UNDONE_PERHAPS_SCENES = "%s:undone:%s:%s"; //用户id--未完成--邀约或应约--场景

    private final String ORDERS_ORDERSID = "orders:%s";
    private final String USER_UNDONE = "%s:msg"; //用户id--未完成


    @Override
    public void subscribe(TransferData transferData) {

        try {

            if (transferData == null || transferData.getData() == null) {
                logger.warn("接受监听计算费用的jms消息参数为空");
                return;
            }
            logger.info("受监听计算费用的jms消息={}", transferData.getData());

            Orders order = JSONObject.parseObject(transferData.getData(), Orders.class);

            //其他待校验的-------
            if (order == null) {
                logger.warn("接受监听计算费用的orders消息参数为空");


                return;
            }
            //获取订单uuid
            Integer size = order.getUuid().length();
            String uuid =null;
            //若订单的uuid 有拼接 “:” 则获取 其拼接头 以及订单号
            if (size>1){
                 uuid = order.getUuid().split(":")[1];
            }
            String timeTaskName= order.getUuid().split(":")[0];

            if (timeTaskName.equals(TimeTaskTypeEnum.BACK_MONEY_TIME_TASK.getValue())){
                logger.info("--->开启 返现<---");
                //每天返现百分比
                String backMoneyPercentage = ordersDao.getSysParameter(SysKeyEnum.BACK_MONEY_PERCENTAGE.getKey());
                //返现限制
                String stopBackMoney = ordersDao.getSysParameter(SysKeyEnum.STOP_BACK_MONEY.getKey());
                Double percentage= Arith.divides(2,Double.valueOf(backMoneyPercentage),100);
                Double stopMoney=Double.valueOf(stopBackMoney);
                List<User> userList = userDao.findUsersForBackMoney(stopMoney,Integer.parseInt(SysKeyEnum.STOP_BACK.getKey()));
                for (int i=0;i<userList.size();i++){
                    User user = userList.get(i);
                    Double moneyAdd = Arith.multiplys(2,user.getConsumeMoney(),percentage);
                    Double consumeMoney = Arith.subtract(2,user.getConsumeMoney(),moneyAdd) ;
                    userDao.updateConsumeMoney(user.getId(),consumeMoney);
                    userDao.updateMoney(moneyAdd,user.getId());
                }
                logger.info("--->结束返现 返现<---");

            }


                //响应人数
                List<OrdersReceive> ordersReceive = ordersDao.findOrdersReceive(uuid);
                //响应  该订单的用户
                List<User>  userReceiveList = ordersDao.findReceives(uuid);
                Orders orderType = ordersDao.findOrdersOneByUuid(uuid);
//            Maps places = ordersDao.findMap(orderType.getPlace());

                if (orderType==null){
                    logger.warn("定时任务订单信息为空uuid={}");
                    return;
                }
            Scene scene = userDao.findSceneByValue(orderType.getScenes());



                if (timeTaskName.equals(TimeTaskTypeEnum.CYCLE_ORDERS.getValue())){
                    logger.info("--->定期 更新 周期订单时间戳<---");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    SimpDate simpDate = SimpDateFactory.endDate();
                    String cycTime = simpDate.cycleTimeChangeCommon(orderType.getStartTime());
                    Long cycLong = simpleDateFormat.parse(cycTime).getTime();
                    ordersDao.updateCycTime(orderType.getUuid(),cycLong);
                }

                if (timeTaskName.equals(TimeTaskTypeEnum.UNDONE_ORDERS.getValue())){
                    logger.info("--->将邀请或参加 移除用户id={}的订单列表<--- ",order.getUid());
                    String uValue = order.getUuid()+":"+InvitationTypeEnum.DEMAND.getCode();
                    undoneRedisTemplate.remove(String.format(USER_UNDONE_PERHAPS_SCENES,order.getUid(),InvitationTypeEnum.DEMAND.getCode(),order.getScenes()),uValue);
//                    undoneRedisTemplate.remove(String.valueOf(order.getUid()),orderType);

                }
                if (timeTaskName.equals(TimeTaskTypeEnum.ORDERS.getValue())){
                    // 提交订单 但是没有付钱的 则取消订单
                    if (orderType.getPayType()==PayTypeEnum.STR_WAIT_PAY.toCode()&&orderType.getPerhaps()==InvitationTypeEnum.SERVICE.getCode()){
                        logger.info("--->订单未付款 取消订单<---");
                        if (orderType.getUpdateMoney()!=0) {
                            logger.info("--->更新订单 支付未完成 <---");
                            userDao.updateMoney(orderType.getMoney(), orderType.getUserId());
//                            ordersDao.rebackOrders(orderType.getUuid(),DEFAULT_MONEY,PayTypeEnum.STR_WAIT_CONFIRM.toCode());
                        }
                            // 删除 redis 中的 数据
                            /*
                            Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()));
                            String key =null;
                            for (String str :set){
                                key = String.valueOf(str);
                            }
                            taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()),key);
                           */
                            //取消订单
                            userDao.payOrder(orderType.getUuid(), PayTypeEnum.STR_CANCEL.toCode());
                            //查找我的最新的 未完成的订单
                            Orders o = ordersDao.findStartTimeLimitOne(orderType.getUserId(),orderType.getSelTimeType(),orderType.getScenes());
                            //删除后 更新 最新的订单
                            /*
                            String type = UserUtil.valueById(o.getPayType());
                            if (type!=null){
                                o.setType(type);
                            }
                            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,o.getUserId(),o.getScenes(),o.getPerhaps(),o.getSelTimeType()), o.getStartTimeStamp(),o);
                            */
//                        }
                        //移除 精准匹配
                        deleteAdvanceOrders(orderType);
//                        //添加 订单明细
//                        String detailId = UserUtil.generateOrderNo();
//                        //                订单明细id--订单号--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
//                        userDao.addOrdersDetail(detailId,orderType.getUuid(),orderType.getMoney(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getKey(),orderType.getUserId(), OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getValue(),OrdersDetailTypeEnum.ORDERS.getKey());

                    }

                }
            if (timeTaskName.equals(TimeTaskTypeEnum.DEMAND_MISS_CATCH_PERSON_COUNT.getValue())){
                logger.info("--->应约单 未接单取消订单<---");
                /*Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()));
                String key =null;
                for (String str :set){
                    key = String.valueOf(str);
                }
                if (key!=null){
                    taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()),key);
                }
                */
                userDao.payOrder(orderType.getUuid(), PayTypeEnum.STR_CANCEL.toCode());
 /*
                Orders o = ordersDao.findReceiveTimeLimitOne(orderType.getUserId(),orderType.getSelTimeType(),PayTypeEnum.STR_WAIT_PAY.toCode(),orderType.getPerhaps(),orderType.getScenes());
               if (o!=null){
                    //删除后 更新 最新的订单
                    String type = UserUtil.valueById(o.getPayType());
                    if (type!=null){
                        o.setType(type);
                    }
                    if (o.getPerhaps()==InvitationTypeEnum.SERVICE.getCode()){
                        List<User> list = ordersDao.findReceives(o.getUuid());
                        if (list!=null){
                            o.setUsers(list);
                        }
                    }
                    taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,o.getUserId(),o.getScenes(),o.getPerhaps(),o.getSelTimeType()), o.getStartTimeStamp(),o);

                }
                */
                logger.info("--->应约单未接单取消 匹配里的订单<---");
                Orders orders = new Orders();
                orders.setUuid(orderType.getUuid());
                orders.setSceneSelEnum(orderType.getScenes());
                orders.setUid(orderType.getUserId());
                orders.setLatitude(orderType.getLatitude());
                orders.setLongitude(orderType.getLongitude());
                if (orderType.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode().intValue()){
                    orders.setPublishType(OrderPublishTypeEnum.SERVICE);
                }else {
                    orders.setPublishType(OrderPublishTypeEnum.DEMAND);
                }
                orders.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
                taskOrderCalcBus.deleteOrders(orders);
                //移除 精准匹配
                deleteAdvanceOrders(orderType);
            }
                if (timeTaskName.equals(TimeTaskTypeEnum.MISS_CATCH_PERSON_COUNT.getValue())){
                    logger.info("--->已达到订单开始时间<---");
                    if (orderType.getPayType()==PayTypeEnum.STR_WAIT_CONFIRM.toCode()){
//                    if (orderType.getPayType()==PayTypeEnum.STR_CANCEL.toCode()){
                        //取消订单
                        logger.info("--->该订单ordersId={}无人响应 自动取消<---",orderType.getUuid());
                        Double money = Math.abs(orderType.getMoney());
                        logger.info("金额={}",money);
                        logger.info("用户={}",orderType.getUserId());
                        userDao.updateMoney(money,orderType.getUserId());
                        //添加 订单明细
                        String detailId = UserUtil.generateUuid();
                        //                订单明细id--订单号--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
                        userDao.addOrdersDetail(detailId,orderType.getUuid(),orderType.getMoney(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getKey(),orderType.getUserId(), OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getValue(),OrdersDetailTypeEnum.ORDERS.getKey());

                        // 删除 redis 中的 数据
                        /*Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()));
                        String key =null;
                        for (String str :set){
                            key = String.valueOf(str);
                        }
                        if (key!=null){
                            taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()),key);
                        }*/
                        userDao.payOrder(orderType.getUuid(), PayTypeEnum.STR_CANCEL.toCode());
                       /* Orders o = ordersDao.findStartTimeLimitOne(orderType.getUserId(),orderType.getSelTimeType(),orderType.getScenes());
                        if (o!=null){
                            //删除后 更新 最新的订单
                            String type = UserUtil.valueById(o.getPayType());
                            if (type!=null){
                                o.setType(type);
                            }
                            if (o.getPerhaps()==InvitationTypeEnum.SERVICE.getCode()){
                                List<User> list = ordersDao.findReceives(o.getUuid());
                                if (list!=null){
                                    o.setUsers(list);
                                }
                            }
                            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,o.getUserId(),o.getScenes(),o.getPerhaps(),o.getSelTimeType()), o.getStartTimeStamp(),o);

                        }*/
                        Orders orders = new Orders();
                        orders.setUuid(orderType.getUuid());
                        orders.setSceneSelEnum(orderType.getScenes());
                        orders.setUid(orderType.getUserId());
                        orders.setLatitude(orderType.getLatitude());
                        orders.setLongitude(orderType.getLongitude());
                        if (orderType.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode().intValue()){
                            orders.setPublishType(OrderPublishTypeEnum.SERVICE);
                        }else {
                            orders.setPublishType(OrderPublishTypeEnum.DEMAND);
                        }
                        orders.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
                        taskOrderCalcBus.deleteOrders(orders);

                    }
//                    if ( orderType.getPayType()!=0){
                    if ( orderType.getPayType()==PayTypeEnum.STR_ON_THE_WAY.toCode()||orderType.getPayType()==PayTypeEnum.STR_WAIT_START.toCode()){
                        if (ordersReceive.size()>0&&Double.valueOf(orderType.getPersonCount())*scene.getProportion()/100<=ordersReceive.size()){
                            logger.info("--->人数符合要求 开始订单<---");
                            ordersDao.ordersStart(uuid,PayTypeEnum.STR_START.toCode());
                            Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()));
                            String key =null;
                            for (String str :set){
                                key = str;
                            }
                            if (key!=null){
                              Orders orders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()),key);
                              orders.setType(PayTypeEnum.STR_START.getValue());
                              taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()),key,orders);
                            }
                            for (int i=0;i<userReceiveList.size();i++){
                                Set<String> setReceive1= taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userReceiveList.get(i).getId(),orderType.getScenes(),InvitationTypeEnum.DEMAND.getCode(),orderType.getSelTimeType()));
                                String keyReceive1 = null;
                                for (String str:setReceive1){
                                    keyReceive1 = str;
                                }
                                if (keyReceive1!=null){
                                    Orders shareOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userReceiveList.get(i).getId(),orderType.getScenes(),InvitationTypeEnum.DEMAND.getCode(),orderType.getSelTimeType()),keyReceive1);
                                    shareOrders.setType(PayTypeEnum.STR_START.getValue());
                                    taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userReceiveList.get(i).getId(),orderType.getScenes(),InvitationTypeEnum.DEMAND.getCode(),orderType.getSelTimeType()),keyReceive1,shareOrders);
                                }
                            }
                        }else {
                            logger.info("--->人数不符合要求 取消订单<---");
                            // 删除 redis 中的 数据
                            /*
                            Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()));
                            String key =null;
                            for (String str :set){
                                key = String.valueOf(str);
                            }
                            if (key!=null) {
                                taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, orderType.getUserId(), orderType.getScenes(), orderType.getPerhaps(), orderType.getSelTimeType()), key);
                            }*/
                            userDao.payOrder(uuid, PayTypeEnum.STR_CANCEL.toCode());
                            Double money = Math.abs(orderType.getMoney());
                            logger.info("退款={}",money);
                            userDao.updateMoney(money,orderType.getUserId());
                            //添加 订单明细
                            String detailId = UserUtil.generateUuid();
                            //订单明细id--订单号--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
                            userDao.addOrdersDetail(detailId,orderType.getUuid(),orderType.getMoney(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getKey(),orderType.getUserId(), OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getValue(),OrdersDetailTypeEnum.ORDERS.getKey());

                            //查找我的最新的 未完成的订单
                           /* Orders o = ordersDao.findStartTimeLimitOne(orderType.getUserId(),orderType.getSelTimeType(),orderType.getScenes());

                            if (o!=null){
                                //删除后 更新 最新的订单
                                String type = UserUtil.valueById(o.getPayType());
                                if (type!=null){
                                    o.setType(type);
                                }

                                if (o.getPerhaps()==InvitationTypeEnum.SERVICE.getCode()){
                                    List<User> list = ordersDao.findReceives(o.getUuid());
                                    if (list!=null){
                                        o.setUsers(list);
                                    }
                                }
                                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,o.getUserId(),o.getScenes(),o.getPerhaps(),o.getSelTimeType()), o.getStartTimeStamp(),o);

                            }
*/
                            //将 所有 共享订单 删除
                            for (int i=0;i<ordersReceive.size();i++){
                                Integer userId = ordersReceive.get(i).getUserId();
                                logger.info("--->删除共享订单userId={}<---",userId);
                               /* Set<String> set2 = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,orderType.getScenes(), InvitationTypeEnum.DEMAND.getCode(),orderType.getSelTimeType()));
                                String key2 =null;
                                for (String str2 :set2){
                                    key2 = String.valueOf(str2);
                                }
                                if (key2!=null) {
                                    taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId, orderType.getScenes(), InvitationTypeEnum.DEMAND.getCode(), orderType.getSelTimeType()), key2);
//                                    userDao.payOrder(ordersReceive.get(i).getOrdersId(), PayTypeEnum.STR_CANCEL.toCode());
                                }*/

                                //放入最新订单
                                /*Orders orders = ordersDao.findReceiveTimeLimitOne(userId,orderType.getSelTimeType(),PayTypeEnum.STR_WAIT_PAY.toCode(),InvitationTypeEnum.DEMAND.getCode(),orderType.getScenes());
                                List<OrdersReceive> ordersServiceList = ordersDao.findOrdersReceiveByUserId(userId,orderType.getScenes());
                                OrdersServiceImpl ordersServiceImpl = new OrdersServiceImpl();
                                Orders lastOrders = ordersServiceImpl.getLatestOrders(orders,ordersServiceList);
                                if (lastOrders!=null){
                                    String type = UserUtil.valueById(lastOrders.getPayType());
                                    lastOrders.setType(type);
                                }
                                if (lastOrders.getPerhaps()==InvitationTypeEnum.SERVICE.getCode()){
                                    List<User> list = ordersDao.findReceives(lastOrders.getUuid());
                                    if (list!=null){
                                        lastOrders.setUsers(list);
                                    }
                                }
                                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,lastOrders.getScenes(),InvitationTypeEnum.DEMAND.getCode(),lastOrders.getSelTimeType()),lastOrders.getStartTimeStamp(),lastOrders);
*/
                                Orders orders1 = new Orders();
                                orders1.setUuid(orderType.getUuid());
                                orders1.setSceneSelEnum(orderType.getScenes());
                                orders1.setUid(orderType.getUserId());
                                orders1.setLatitude(orderType.getLatitude());
                                orders1.setLongitude(orderType.getLongitude());
                                if (orderType.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode().intValue()){
                                    orders1.setPublishType(OrderPublishTypeEnum.SERVICE);
                                }else {
                                    orders1.setPublishType(OrderPublishTypeEnum.DEMAND);
                                }
                                orders1.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
                                taskOrderCalcBus.deleteOrders(orders1);
                            }

//                            userDao.payOrder(uuid, PayTypeEnum.STR_CANCEL.toCode());
//                            userDao.cancelReceiveOrder(uuid);

//                            for (int i=0;i<ordersReceive.size();i++){
//                                logger.info("人数不符合要求 取消应约的订单 ordersId={}",ordersReceive.get(i).getOrdersId());
//                                userDao.payOrder(ordersReceive.get(i).getOrdersId(), PayTypeEnum.STR_CANCEL.toCode());
//                            }

                        }
                    }
                    //移除 精准匹配
                    deleteAdvanceOrders(orderType);

                }

            if (timeTaskName.equals(TimeTaskTypeEnum.DELAY_TIME_ORDERS.getValue())||timeTaskName.equals(TimeTaskTypeEnum.ROBOTIC_DELAY_TIME.getValue())){
                    logger.info("--->自动确认到达---延时计算费用<---");
                Cost cost = CalculationFactory.timing();
                Double gratefulFee = Arith.divides(3,orderType.getGratefulFree(),orderType.getPersonCount());
                Double money = cost.invitation(scene.getHourlyFee(),orderType.getOrderPeriod(),orderType.getStartTime(),gratefulFee);
                if (timeTaskName.equals(TimeTaskTypeEnum.ROBOTIC_DELAY_TIME.getValue())){
                    ordersDao.serviceAllArrived(uuid,Boolean.TRUE);
                }
                for (int i=0;i<ordersReceive.size();i++){
                    logger.info("ordersReceive.get(i).getArrived()={}",ordersReceive.get(i).getArrived());
                    if (ordersReceive.get(i).getArrived()){
                        //  计算到 个人用户上
                        Integer receiveUserId= ordersReceive.get(i).getUserId();
                        logger.info("--->计算费用到应邀者receiveUserId={}<---",receiveUserId);
                        userDao.updateMoney(money,receiveUserId);
                        //添加 订单明细
                        String detailId = UserUtil.generateUuid();
                        //订单明细id--订单号--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
                        userDao.addOrdersDetail(detailId,orderType.getUuid(),money,orderType.getUserId(), OrdersDetailTypeEnum.ORDERS_REVENUE.getKey(),receiveUserId, OrdersDetailTypeEnum.ORDERS_REVENUE.getValue(),OrdersDetailTypeEnum.ORDERS.getKey());

                    }
                }

                //移除 精准匹配
                deleteAdvanceOrders(orderType);

            }

            if (timeTaskName.equals(TimeTaskTypeEnum.SETTLE_ACCOUNTS_INVITATION.getValue())){
                    logger.info("--->订单 自动结束---延时计算费用<---");
//                if (orderType.getPayType()!=0){
                if (orderType.getPayType()==PayTypeEnum.STR_START.toCode()){
                    // TODO: 2017/8/31 是否到达 情况 
//                    List<OrdersReceive> listRec = ordersDao.findOrdersReceive(uuid);
//                    Integer isArrive=0;
//                    for (OrdersReceive arrive:listRec){
//                        if (arrive.getArrived()==Boolean.TRUE){
//                            isArrive++;
//                        }
//                    }
//                    if (isArrive==0){
//                      ordersDao.serviceAllArrived(uuid,Boolean.TRUE);
//                    }
//                    Cost cost = CalculationFactory.timing();
//                    Double money = cost.invitation(scene.getHourlyFee(),orderType.getOrderPeriod(),orderType.getStartTime(),orderType.getGratefulFree());
                    for (int i=0;i<ordersReceive.size();i++){
                        //  计算到 个人用户上
//                        Integer receiveUserId= ordersReceive.get(i).getUserId();
//                        logger.info("计算费用到应邀者receiveUserId={}",receiveUserId);
//                        userDao.updateMoney(money,receiveUserId);
//                        userDao.payOrder(uuid, PayTypeEnum.STR_SUCCESS.toCode());
                        // 删除 redis 中的 数据
                        /*Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,receiveUserId,orderType.getScenes(),InvitationTypeEnum.DEMAND.getCode(),orderType.getSelTimeType()));
                        String key =null;
                        for (String str :set){
                            key = str;
                        }
                        if (key!=null){
                            Orders redisOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,receiveUserId,orderType.getScenes(),InvitationTypeEnum.DEMAND.getCode(),orderType.getSelTimeType()),key);
                            taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,receiveUserId,redisOrders.getScenes(),InvitationTypeEnum.DEMAND.getCode(),redisOrders.getSelTimeType()),key);
                        }*/
                        userDao.payOrder(uuid, PayTypeEnum.STR_SUCCESS.toCode());
                        //                查找我的最新的 未完成的   应约订单
//                        Orders o = ordersDao.findStartTimeLimitOne(receiveUserId,orderType.getSelTimeType(),orderType.getScenes());
                        /*Orders o =  ordersDao.findReceiveTimeLimitOne(receiveUserId,orderType.getSelTimeType(),PayTypeEnum.STR_WAIT_PAY.toCode(),InvitationTypeEnum.DEMAND.getCode(),orderType.getScenes());

                        if (o!=null){
                            if (o.getPayType()!=PayTypeEnum.STR_WAIT_PAY.toCode()){
                                Orders orders1 = ordersDao.findOrdersOneByUuid(o.getUuid());
                                User user = userDao.findUserByUserId(orders1.getUserId());
                                List<User> list = new ArrayList<>();
                                list.add(user);
                                o.setUsers(list);
                                String type = UserUtil.valueById(o.getPayType());
                                if (type!=null){
                                    o.setType(type);
                                }
                            }
                            //删除后 更新 最新的订单
                            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,o.getUserId(),o.getScenes(),o.getPerhaps(),o.getSelTimeType()), o.getStartTimeStamp(),o);
                        }
                        */
                    }
                }

                // 删除 redis 中的 数据
               /* Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()));
                String key =null;
                for (String str :set){
                    key = str;
                }
                if (key!=null){
                    Orders redisOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orderType.getUserId(),orderType.getScenes(),orderType.getPerhaps(),orderType.getSelTimeType()),key);
                    taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,redisOrders.getUserId(),redisOrders.getScenes(),redisOrders.getPerhaps(),redisOrders.getSelTimeType()),key);
                }
                */
               //                查找我的最新的 未完成的订单
               /* Orders o = ordersDao.findStartTimeLimitOne(orderType.getUserId(),orderType.getSelTimeType(),orderType.getScenes());
                if (o!=null){
                    String type = UserUtil.valueById(o.getPayType());
                    if (type!=null){
                        o.setType(type);
                    }
                    //删除后 更新 最新的订单
                    taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,o.getUserId(),o.getScenes(),o.getPerhaps(),o.getSelTimeType()), o.getStartTimeStamp(),o);
                }
                */
                /**********************************订单完成后---自动结算**********************************/
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date = new Date();
                String nowTime = simpleDateFormat.format(date);
                SimpDate simpDate = SimpDateFactory.endDate();
                Map<String,String> cancelTimeMap=null;
                try {
                    // TODO: 2017/9/20 用于测试
                    String delayedTime = simpDate.endDate(nowTime,0.0,0);
//                    String delayedTime = simpDate.endDate(nowTime,robotization,0);
                    cancelTimeMap = simpDate.transformTime(delayedTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TransferData data = new TransferData();
                TimeTask task = new TimeTask();
                task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
                task.setTimeTaskName("roboticDelayTime:"+uuid);
                //延时计算费用
                task.setExecuteTime("0 "+cancelTimeMap.get("mm")+" "+cancelTimeMap.get("HH")+" "+cancelTimeMap.get("dd")+" "+cancelTimeMap.get("MM")+" ?");
                Orders timeTaskOrders = new Orders();
                timeTaskOrders.setUuid("roboticDelayTime:"+uuid);
                task.setParams(JSON.toJSONString(timeTaskOrders));
                data.setData(JSONObject.toJSONString(task));
                data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
                rabbitMqPublish.publish(data);
            }
            logger.info("orders={}", order.toString());


        } catch (Exception e) {
            logger.warn("接受监听计算费用的jms消息{}出错", transferData.getData(), e);
        }

    }

    public void deleteAdvanceOrders(Orders orderType)throws Exception{
        Orders orders1 = new Orders();
        orders1.setUuid(orderType.getUuid());
        orders1.setSceneSelEnum(orderType.getScenes());
        orders1.setUid(orderType.getUserId());
        orders1.setLatitude(orderType.getLatitude());
        orders1.setLongitude(orderType.getLongitude());
        if (orderType.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode().intValue()){
            orders1.setPublishType(OrderPublishTypeEnum.SERVICE);
        }else {
            orders1.setPublishType(OrderPublishTypeEnum.DEMAND);
        }
        orders1.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
        taskOrderCalcBus.deleteOrders(orders1);
    }
    public static void main(String[] args) {
        PayTypeEnum[] s = PayTypeEnum.values();
        for (int i=0;i<s.length;i++){
            PayTypeEnum a = s[i];
            System.out.println(a.getValue());
        }
    }

}
