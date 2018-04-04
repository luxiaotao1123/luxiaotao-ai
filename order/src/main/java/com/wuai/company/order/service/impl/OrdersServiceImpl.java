package com.wuai.company.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.enums.*;
import com.wuai.company.message.RabbitMqPublishImpl;
import com.wuai.company.message.TransferData;
import com.wuai.company.order.dao.OrdersDao;
import com.wuai.company.order.dao.SceneDao;
import com.wuai.company.order.entity.Labels;
import com.wuai.company.order.entity.OrdersReceive;
import com.wuai.company.order.entity.response.MapsResponse;
import com.wuai.company.order.entity.response.OrdersDataResponse;
import com.wuai.company.order.entity.response.OrdersResponse;
import com.wuai.company.order.entity.response.OrdersUserResponse;
import com.wuai.company.entity.request.AppraiseRequest;
import com.wuai.company.order.service.OrdersService;
import com.wuai.company.order.util.ComparatorOrders;
import com.wuai.company.order.util.NearbyUtil;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.util.*;
import com.wuai.company.util.comon.factory.CalculationFactory;
import com.wuai.company.util.comon.factory.Cost;
import com.wuai.company.util.comon.SimpDate;
import com.wuai.company.util.comon.SimpDateFactory;
import com.wuai.company.task.job.TaskOrderCalcBus;
import com.wuai.company.user.dao.UserDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.wuai.company.util.UserUtil.generateOrderNo;


/**
 * 订单的service具体实现层
 * Created by Ness on 2017/5/25.
 */
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private SceneDao sceneDao;
    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TaskOrderCalcBus taskOrderCalcBus;

    @Autowired
    private NearbyUtil nearbyUtil;
    @Value("${sys.delayed}")
    private Integer delayed;


    @Resource
    private HashOperations<String, String, String> orderHashRedisTemplate;
    @Resource
    private ZSetOperations<String,String> msgValueTemplate;
    @Resource
    private ZSetOperations<String,String> undoneRedisTemplate;
    @Resource
    private HashOperations<String, String, Orders> taskHashRedisTemplate;
    @Resource
    private HashOperations<String, String, NearbyBody[]> nearbyTemplate;
    @Resource
    private HashOperations<String, String, NearbyBody> totalNearbyTemplate;


    private final String NEARBY_TOTAL_BODY="nearby:total:body";//用户id
    private final String NEARBY_ID_BODY="nearby:%s:body";//用户id
    private final String SCENENAME_ORDERS_PERHAPS_SELTIMETYPE = "%s:%s:orders:%s:%s";  // 用户id--场景名--订单--邀约/应约--固定时间/特定时间
    private final String SCENENAME_ORDERS = "%s:orders";  // 用户id--场景名--订单--邀约/应约--固定时间/特定时间
    private final String ORDERS_ORDERSID = "orders:%s"; //订单id
    private final String ORDERS_ORDERSID_PAY = "orders:%s:%s"; //订单id-支付状态
    private final String ORDERS_ORDERSID_USERID = "orders:%s:%s"; //订单id--userId
    private final String USER_TYPE_ORDERS = "%s:%s:%s"; //用户id--类型:发出 0 或接受 1--订单号
    private final String USER_MSG = "%s:msg"; //用户id--信息列表
    private final String MSG_USER = "msg:%s"; //信息列表--用户id
    private final String MSG_USER_SIZE = "msg:%s:size"; //信息列表--用户id
    private final String USER_UNDONE_PERHAPS_SCENES = "%s:undone:%s:%s"; //用户id--未完成--邀约或应约--场景
//    private final String USER_UNDONE_PERHAPS = "%s:undone:%s"; //用户id--未完成--邀约或应约
//    private final String USER_INVITATION_ORDER_SELTIMETYPE = "%s:%s:orders:%s:%s";  // 用户id--场景名--订单--邀约/应约--固定时间/特定时间
    private final static Integer ISPRESS=2;
    private  static Boolean ISSHARE=Boolean.TRUE;

    private Logger logger = LoggerFactory.getLogger(OrdersServiceImpl.class);


    /**
     * 根据Uuid查询订单信息
     *
     * @param uuid 订单的id
     * @return
     */
    @Override
    public Response findOrdersOneByUuid(String uuid) {

        if (StringUtils.isEmpty(uuid)) {
            logger.warn("根据Uuid查询订单信息 所传id为空");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }

        if (logger.isInfoEnabled()) {
            logger.info("id={}开始查询订单信息", uuid);
        }

        return Response.success(ordersDao.findOrdersOneByUuid(uuid));
    }

    /**
     * 根据用户Id查询所有隶属于该用户的所用的订单
     *
     * @param userId    用户的Id
     * @param orderType 订单的类型
     * @return
     */
    @Override
    public Response findOrdersByUserId(Integer userId, Integer orderType) {


        if (userId==null|| orderType == null) {
            logger.warn("根据用户Id查询所有隶属于该用户的所用的订单 所传参数为空 userId={} orderType={}", userId, orderType);
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }

        if (logger.isInfoEnabled()) {
            logger.info("userId={}开始查询所有订单信息", userId);
        }

        return Response.success(ordersDao.findOrdersByUserId(userId, orderType));
    }

    /**
     * 保存用户订单信息
     *
     * @param orders
     */
    @Override
    public Response saveOrders(Orders orders) {
        if (orders == null || orders.getUserId()==null) {
            logger.warn("保存用户订单信息 所传参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }

        if (logger.isInfoEnabled()) {
            logger.info("userId={}开始保存订单信息", orders.getUserId());
        }

        ordersDao.saveOrders(orders);

        return Response.successByArray();
    }

    /**
     * 更新用户的订单信息
     *
     * @param orders
     */
    @Override
    public Response updateOrdersOneByUuid(Orders orders) {


        if (orders == null || orders.getUserId()==null || StringUtils.isEmpty(orders.getUuid())) {
            logger.warn("保存用户订单信息 所传参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }

        if (logger.isInfoEnabled()) {
            logger.info("userId={}开始更新订单={}信息", orders.getUserId(), orders.getUuid());
        }

        ordersDao.updateOrdersOneByUuid(orders);
        return Response.successByArray();

    }

    /**
     * 查询订单 首页
     * @param perhaps 1邀约 2应约
     * @return
     */
    @Override
    public Response findOtherOrders(String scenes,Integer perhaps,Integer uid,Integer pageNum) throws ParseException {
        // TODO: 2017/6/12 vip规则暂定
       User user =  userDao.findUserByUserId(uid);
       if (user==null){
           return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
       }

       Integer code = user.getUserGrade();

        if (uid==null|| "".equals(uid)){
            logger.warn("查询订单信息 所传参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }


        Map<String,Object> map = new HashMap<String,Object>();
        String value = orderHashRedisTemplate.get(String.valueOf(uid),scenes);
        List<OrdersResponse> orders = ordersDao.findOtherOrders(value, perhaps, uid,pageNum);
        //订单详情中的应约用户信息
        // 加入 应约 用户信息 2017/7/10
        //根据用户id 和 选择的场景 获取 redis中存储的场景值 2017/7/11

        for (int i=0;i<orders.size();i++){
            OrdersResponse ordersResponse = orders.get(i);
            //将数据库中存储的值 转换成key 2017/7/11
            //            if(value.equals(ordersResponse.getScenes())){
            //                ordersResponse.setScenes(String.valueOf(uid));
            //            }
            List<OrdersUserResponse> list = ordersDao.findByUuid(ordersResponse.getUuid());
            ordersResponse.setOrderUser(list);
        }

        List<Object> list = new ArrayList<>();

/*
        //发布的最近的
        Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,uid,value,perhaps,SelTimeTypeEnum.FIXED.getCode()));
        String key=null;
       for (String str:set) {
           key=str;
       }
       if (key!=null){
           Orders o = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,uid,value,perhaps,SelTimeTypeEnum.FIXED.getCode()),key);
//           Orders or = ordersDao.findOrdersOneByUuid(o.getUuid());
//           String type= UserUtil.valueById(or.getPayType());
//           if (or.getPerhaps()==InvitationTypeEnum.DEMAND.getCode()&&or.getPayType()==PayTypeEnum.STR_WAIT_PAY.toCode()){
//               o.setType(PayTypeEnum.STR_WAIT_CONFIRM.getValue());
//           }else {
//               o.setType(type);
//           }
           if(o!=null){
//               List<User> li = ordersDao.findReceives(o.getUuid());
//               User u = userDao.findUserByUserId(o.getUserId());
//               List<User> lis = new ArrayList<User>();
//               if (li != null) {
//                   o.setSize(li.size());
//                   if (perhaps.intValue()==InvitationTypeEnum.DEMAND.getCode().intValue()) {
//                       lis.add(u);
//                       o.setUsers(lis);
//                   }else {
//                       o.setUsers(li);
//                   }
//               }
               List<User> userList =ordersDao.findReceives(o.getUuid());
               Scene scene = userDao.findSceneByValue(value);
               o.setHourlyFee(scene.getHourlyFee());
               o.setSize(userList.size());
               list.add(o);


           }else {
               taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,uid,value,perhaps,SelTimeTypeEnum.FIXED.getCode()),key);
               Orders upToDateOrders = ordersDao.findStartTimeLimitOne(uid,SelTimeTypeEnum.FIXED.getCode(),value);
               if (upToDateOrders!=null)
                   taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,uid,value,perhaps,SelTimeTypeEnum.FIXED.getCode()),upToDateOrders.getStartTimeStamp(),upToDateOrders);
           }

       }
       */
       if (Objects.equals(perhaps, InvitationTypeEnum.SERVICE.getCode())){
           Orders inOrders = ordersDao.findLastestOne(uid,SelTimeTypeEnum.FIXED.getCode(),value);
           if (inOrders!=null){
               List<User> userList =ordersDao.findReceives(inOrders.getUuid());
               Scene scene = userDao.findSceneByValue(value);
               inOrders.setHourlyFee(scene.getHourlyFee());
               if (userList.size()!=0){
                   inOrders.setSize(userList.size());
                   inOrders.setUsers(userList);
               }
               String type = UserUtil.valueById(inOrders.getPayType());
               inOrders.setType(type);
               if(inOrders!=null){
                   list.add(inOrders);

               }
           }

       }else {
           Orders joOrders = ordersDao.findReceiveTimeLimitOne(uid,SelTimeTypeEnum.FIXED.getCode(),PayTypeEnum.STR_WAIT_PAY.toCode(),InvitationTypeEnum.DEMAND.getCode(),value);
           List<OrdersReceive> ordersServiceList = ordersDao.findOrdersReceiveByUserId(uid,value);
           Orders joinOrders = getLatestOrders(joOrders,ordersServiceList);
           List<User> lisU = new ArrayList<>();
           if (joinOrders!=null){
               if (Objects.equals(joinOrders.getPerhaps(), InvitationTypeEnum.SERVICE.getCode())){
                   User use = userDao.findUserByUserId(joinOrders.getUserId());
                   lisU.add(use);
                   joinOrders.setSize(lisU.size());
                   joinOrders.setUsers(lisU);
               }
               String type = UserUtil.valueById(joinOrders.getPayType());
               joinOrders.setType(type);
//           List<User> userList =ordersDao.findReceives(joinOrders.getUuid());
               Scene scene = userDao.findSceneByValue(value);
               joinOrders.setHourlyFee(scene.getHourlyFee());
               if(joinOrders!=null) {
                   list.add(joinOrders);
               }
           }

       }
//       Map myFixedOrdersMap = taskHashRedisTemplate.entries(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,uid,value,perhaps,SelTimeTypeEnum.PERIOD.getCode()));
//        Set<String> set2 = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,uid,value,perhaps,SelTimeTypeEnum.PERIOD.getCode()));
//        List<Object> list2 = new ArrayList<>();
//        for (String key2:set2) {
//            list2.add(myFixedOrdersMap.get(key2));
//        }

        //查看已启动的 固定 周期定时任务
        List<Orders> myPeriodOrder = ordersDao.findMyPeriodOrder(value, uid,perhaps, 1);
       for (int i=0;i<myPeriodOrder.size();i++){
           Orders ordersResponse = myPeriodOrder.get(i);
           String type = UserUtil.valueById(ordersResponse.getPayType());
           ordersResponse.setType(type);
       }
        for (int j=0;j<orders.size();j++){
            String ordersId = orders.get(j).getUuid();
            orders.get(j).setOrderUser(ordersDao.findByUuid(ordersId));;
        }

        Scene sc = userDao.findSceneByValue(value);
//        taskOrderCalcBus.getOrders()
        /**if (list.size()==0){
            Set<String> sets =undoneRedisTemplate.range(String.format(USER_UNDONE_PERHAPS_SCENES,uid,perhaps,value),0,1);
            String ordersId =null;
            for (String strs: sets){
                ordersId=strs;
            }
            if (ordersId!=null) {
                Scene scene = userDao.findSceneByValue(value);
                String[] spOrders = ordersId.split(":");
                if (spOrders.length==2){
                    ordersId=spOrders[0];
                }
                Orders orders2 = ordersDao.findOrdersOneByUuid(ordersId);
                if (orders2!=null){
                    orders2.setHourlyFee(scene.getHourlyFee());
                    String type = UserUtil.valueById(orders2.getPayType());
                    orders2.setType(type);
                    if (Objects.equals(Integer.valueOf(spOrders[1]), InvitationTypeEnum.DEMAND.getCode())){
                        orders2.setType(PayTypeEnum.SEND_JOIN.getValue());
                    }else {
                        orders2.setType(PayTypeEnum.SEND_INVITATION.getValue());

                    }
                }
                if(orders2!=null) {
                    list.add(orders2);
                }
            }

        }*/

            map.put("my", list);

        map.put("myPeriod", myPeriodOrder);
//        map.put("my",order);
        map.put("other",orders);
//        map.put("common",taskOrderCalcBus.getOrders());
        map.put("scene",sc);
        return  Response.success(map);
    }


    /**
     * 修改邀约/应约
     * @return
     */
    @Override
    public Response updateInvitation(String uid,Integer userId,String startTime,String place, Integer selTimeType,Double orderPeriod,
                                     Integer personCount, Double gratefulFree,String label,Integer perhaps,
                                     Integer scenes,Double money,Double longitude,Double latitude,String address) throws ParseException {
        if(userId==null|| startTime==null|| place==null|| selTimeType==null|| orderPeriod==null|| personCount==null||  perhaps==null|| scenes==null){
            logger.warn("修改邀约/应约 所传参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        //根据订单号查询订单
        Orders myOrders = ordersDao.findOrdersOneByUuid(uid);
        if (myOrders==null){
            logger.warn("订单信息为空");
            return Response.response(ResponseTypeEnum.ERROR_CODE.toCode(),"订单信息不存在");
        }
        Map<String ,Object> map = new HashMap<String,Object>();
        // 已提交的 场所 同一时间段内 不能提交订单
        String sceness= orderHashRedisTemplate.get(String.valueOf(userId),String.valueOf(scenes));
        Scene sc = userDao.findSceneByValue(sceness);  //获取该场景的参数
//        Maps places = ordersDao.findMap(place);
        SimpDate simple = SimpDateFactory.endDate();
        //通过userId查找 所有相关订单
        List<Orders> list = ordersDao.findOneByUserId(userId);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpDate simpDate = SimpDateFactory.endDate();

        if (selTimeType==1){
            //转换时间
            Long cycLong = simpleDateFormat.parse(simpDate.cycleTimeChangeCommon(startTime)).getTime();
            ordersDao.updateInvitation(0.0,uid,userId, startTime, place, selTimeType, orderPeriod, personCount, gratefulFree, label, perhaps,
                    sc.getValue(),longitude,latitude,PayTypeEnum.STR_WAIT_PAY.toCode(),myOrders.getVersion(),cycLong,address);
            //周期时间转换
            Map<String,Object> cycleTimeChangeMap = simple.cycleTimeChange(startTime);
            TransferData data = new TransferData();
            TimeTask task = new TimeTask();
            List<Orders> list2 = ordersDao.findAllCycleOrders(userId,2);
            for (int j=0;j<list2.size();j++){
                if (uid.equals(list2.get(j).getUuid())){
                    task.setScheduleOperaEnum(ScheduleOperaEnum.UPDATE_TASK);
                }else {
                    task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
                }
            }
//            task.setScheduleOperaEnum(ScheduleOperaEnum.UPDATE_TASK);
            task.setTimeTaskName("cycleOrders:"+uid);
            //秒 分 时 日 月 周
            task.setExecuteTime("0 "+cycleTimeChangeMap.get("mm")+" "+cycleTimeChangeMap.get("HH")+" ? * "+cycleTimeChangeMap.get("weekDay")+"");
            Orders orders = new Orders();
            orders.setUuid("cycleOrders:"+uid);
            task.setParams(JSON.toJSONString(orders));
            data.setData(JSONObject.toJSONString(task));
            data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
            rabbitMqPublish.publish(data);
        }


        Date start = simpleDateFormat.parse(startTime);
        Date now = new Date();
        String now2 = simpleDateFormat.format(now);
        Map<String,String> nowTimeMap =simple.transformTime(now2);
        //约会开始 时间必须在__时间以后
        String now3 = simple.endDate(now2,0.0, sc.getFirstStart());
        Date now4 = simpleDateFormat.parse(now3);

        int time = now4.compareTo(start);
        if (time >= 0) {
            logger.info("预定时间必须大于当前时间={}分钟", sc.getFirstStart());
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "预定时间必须大于当前时间"+sc.getFirstStart()+"分钟");
        }


        if (ordersDao.findOrdersReceive(uid).size()>0){
            logger.warn("已有人应约 不可修改订单");
            return Response.error(ResponseTypeEnum.ORDERS_FAIL2.toCode(),"已有人应约 不可修改订单");
        }

        for (int i = 0; i < list.size(); i++) {
            Orders orders = list.get(i); //获取订单

            if (!orders.getUuid().equals(uid)){
                String star = orders.getStartTime();  //开始时间
                Double period = orders.getOrderPeriod();  //时间间隔
                //订单结束时间
                String end = simple.endDate(star, period,0);

                Date star1 = simpleDateFormat.parse(star);
                Date end1 = simpleDateFormat.parse(end);

//            Date start = simpleDateFormat.parse(startTime);

                //判断用户输入的 开始时间是否在 已发布的订单 约会时间内
                int starBefore = star1.compareTo(start);
                int endAfter = end1.compareTo(start);

                if (starBefore <= 0 && endAfter >= 0) {
                    logger.warn("订单开始时间 已存在约会");
                    return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "请选择合理的时间范围");
                }
            }

        }





        Double countMoney =null;
           if(Objects.equals(perhaps, InvitationTypeEnum.SERVICE.getCode())) {  //当选择的是固定周期，邀约的时候进行 计算费用
               Double hourlyFee = sc.getHourlyFee();  //该场景需要的 每小时每人 费用
               Cost cost = CalculationFactory.hand();
               //现需支付的金额
               countMoney = cost.calculate(hourlyFee, personCount, orderPeriod, startTime, gratefulFree);
               //添加订单


//               Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,myOrders.getUserId(),myOrders.getScenes(),myOrders.getPerhaps(),myOrders.getSelTimeType()));
//                String key = null;
//               for (String str : set ){
//                   key=str;
//               }

               Date date=simpleDateFormat.parse(myOrders.getStartTime());
               Long timeStemp = date.getTime();
               /**
               Orders valueOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,myOrders.getUserId(),myOrders.getScenes(),myOrders.getPerhaps(),myOrders.getSelTimeType()),key);
//               Orders valueOrders = obj2Orders(taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,myOrders.getUserId(),myOrders.getScenes(),myOrders.getPerhaps(),myOrders.getSelTimeType()),String.valueOf(timeStemp)));
               //原订单中的金额
               Double preMoney = valueOrders.getMoney();
               if (preMoney < 0) {
                   preMoney = -preMoney;
               }
               if (valueOrders==null){
                   logger.warn("发布的信息 未存入缓存中");
                   return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"修改失败,请重新发布");
               }
                */
//               Date ordersInRedis = simpleDateFormat.parse(valueOrders.getStartTime());
//               Long preTime = ordersInRedis.getTime();
               Double preMoney = Math.abs(myOrders.getMoney());
               myOrders.setHourlyFee(sc.getHourlyFee());
               String version=myOrders.getVersion();
               if (countMoney - preMoney > 0) {
                   logger.info("价格高与原先价格——需补差价");
                   version=generateOrderNo(myOrders.getVersion());
                   //修改后的价格 高于原先发布的价格 -- 补差价
//                   ordersDao.updateInvitation(countMoney, uid, userId, startTime, place, selTimeType, orderPeriod, personCount, gratefulFree, label, perhaps, sc.getValue(), places.getLongitude(), places.getLatitude(), PayTypeEnum.STR_WAIT_PAY.toCode(),version);
                   countMoney = countMoney - preMoney;
                   Long startLong = simpleDateFormat.parse(startTime).getTime();
                   ordersDao.updateDifferenceMoney(countMoney, uid, userId, startTime, place, selTimeType, orderPeriod, personCount, gratefulFree, label,
                           perhaps, sc.getValue(), longitude, latitude, PayTypeEnum.STR_WAIT_PAY.toCode(),version,startLong,address);
                   map.put("pay", 1);
               } else if (countMoney - preMoney < 0) {
                   logger.info("价格低与原先价格——需返还差价");
                    Double backMoney=preMoney - countMoney;
                   //修改后的价格 低于原先发布的价格 -- 返还差价
                   userDao.updateMoney(backMoney, userId);
                   Double upMoney=-countMoney;
                   Long startLong = simpleDateFormat.parse(startTime).getTime();
                   ordersDao.updateInvitation(upMoney, uid, userId, startTime, place, selTimeType, orderPeriod, personCount, gratefulFree, label, perhaps, sc.getValue(),
                           longitude, latitude, PayTypeEnum.STR_WAIT_CONFIRM.toCode(),version,startLong,address);
                   countMoney = countMoney - preMoney;
                   map.put("pay", 2);
                   //添加 订单明细
                   String detailId = UserUtil.generateUuid();
                   //                订单明细id--订单号--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
                   userDao.addOrdersDetail(detailId,uid,backMoney,OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getKey(),userId, OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getValue(),OrdersDetailTypeEnum.ORDERS.getKey());

               } else if (countMoney - preMoney == 0) {
                   logger.info("价格与原先价格相等");
                   Double upMoney=-countMoney;
                   Long startLong = simpleDateFormat.parse(startTime).getTime();
                   ordersDao.updateInvitation(upMoney, uid, userId, startTime, place, selTimeType, orderPeriod, personCount, gratefulFree, label, perhaps, sc.getValue(),
                           longitude,latitude, PayTypeEnum.STR_WAIT_CONFIRM.toCode(),version,startLong,address);
                   map.put("pay", 3);
               }
               /**
               if (countMoney-preMoney<=0){
                   taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,myOrders.getUserId(),myOrders.getScenes(),myOrders.getPerhaps(),myOrders.getSelTimeType()),key);
                   Orders upToDateOrders = ordersDao.findStartTimeLimitOne(myOrders.getUserId(),SelTimeTypeEnum.FIXED.getCode(),myOrders.getScenes());
                   if(upToDateOrders!=null) {
                       upToDateOrders.setHourlyFee(sc.getHourlyFee());
                       String type = UserUtil.valueById(upToDateOrders.getPayType());
                       upToDateOrders.setType(type);
                       taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, upToDateOrders.getUserId(), upToDateOrders.getScenes(), upToDateOrders.getPerhaps(), upToDateOrders.getSelTimeType()), String.valueOf(timeStemp), upToDateOrders);
                   }
               }*/
               map.put("userId",userId);
               map.put("uid",version);
               map.put("money",String.valueOf(countMoney));
               map.put("proportion",sc.getProportion());
//               map.put("places", places);
           }else {
               Long startLong = simpleDateFormat.parse(startTime).getTime();
               ordersDao.updateInvitation(0.0, uid, userId, startTime, place, selTimeType, orderPeriod, personCount, gratefulFree,
                       label, perhaps, sc.getValue(), longitude, latitude, PayTypeEnum.STR_WAIT_PAY.toCode(),myOrders.getVersion(),startLong,address);

//               Orders orders1 = ordersDao.findOrdersOneByUuid(uid);
               /**Set<String> set =taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,sc.getValue(),InvitationTypeEnum.DEMAND.getCode(),selTimeType));
               String key = null;
               for (String str : set){
                   key=str;
               }
               taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,sc.getValue(),InvitationTypeEnum.DEMAND.getCode(),selTimeType),key);

               Orders orders1  = ordersDao.findReceiveTimeLimitOne(userId,selTimeType,PayTypeEnum.STR_WAIT_PAY.toCode(),InvitationTypeEnum.DEMAND.getCode(),sc.getValue());
               if (orders1!=null){
                   orders1.setType(PayTypeEnum.STR_WAIT_CONFIRM.getValue());
                   taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders1.getUserId(),orders1.getScenes(),InvitationTypeEnum.DEMAND.getCode(),orders1.getSelTimeType()),orders1.getStartTimeStamp(),orders1);
               }*/
           }
               /*************************************************************/
               /*****************订单时效 5分钟后取消订单**************************/
               /**************************************/
               TransferData data = new TransferData();
               TimeTask task = new TimeTask();
               task.setScheduleOperaEnum(ScheduleOperaEnum.UPDATE_TASK);
               task.setTimeTaskName("orderTime:"+uid);
               //当前时间5分钟后
               task.setExecuteTime("0 "+nowTimeMap.get("mm")+" "+nowTimeMap.get("HH")+" "+nowTimeMap.get("dd")+" "+nowTimeMap.get("MM")+" ?");
               Orders o = new Orders();
               o.setUuid("orders:"+uid);
               task.setParams(JSON.toJSONString(o));
               data.setData(JSONObject.toJSONString(task));

               data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
               rabbitMqPublish.publish(data);
               /*************************************************************/
               /****************************订单时效**************************/
               /*************************************************************/
//添加订单 待支付状态
               //修改 明细
               // TODO: 2017/7/3  修改 明细
//               ordersDao.addOrdersDetail(detailId,uuid,countMoney,userId);

        Orders orders = ordersDao.findOrdersOneByUuid(uid);
        logger.info("修改预加载中的该订单。。。");
        orders.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
        orders.setUid(orders.getUserId());
        orders.setSceneSelEnum(sc.getValue());
//                orders.setMatchRate(90.0);
        /*
                orders.setSceneSelEnum(sc.getValue());
                orders.setOppositePublishType(String.valueOf(perhaps));
                orders.setUid(userId);

                orders.setLatitude(places.getLatitude());
                orders.setLongitude(places.getLongitude());
                if (perhaps==1){
                    orders.setPublishType(OrderPublishTypeEnum.SERVICE);
                    orders.setOppositePublishType(OrderPublishTypeEnum.SERVICE.toOppositeValue());
                }else {
                    orders.setPublishType(OrderPublishTypeEnum.DEMAND);
                    orders.setOppositePublishType(OrderPublishTypeEnum.DEMAND.toOppositeValue());
                }
                orders.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
                taskOrderCalcBus.addOrders(orders);*/
        if (orders.getPerhaps().equals(InvitationTypeEnum.SERVICE.getCode())){
            orders.setPublishType(OrderPublishTypeEnum.SERVICE);
        }else if (orders.getPerhaps().equals(InvitationTypeEnum.DEMAND.getCode())){
            orders.setPublishType(OrderPublishTypeEnum.DEMAND);
        }

        if (taskOrderCalcBus.updateOrders(orders)){
            logger.info("添加订单成功");
        }else {
            logger.info("添加订单失败");
        }
        map.put("tips","订单开始前30分钟内不可取消订单");

        return Response.success(map);

//        ordersDao.updateInvitation(uid,userId, startTime, place, selTimeType, orderPeriod, personCount, gratefulFree, label, perhaps, scenes);
    }

    /**
     * 根据性别返回个性标签
     * @return
     */
    @Override
    public Response findLabel() {
        List<Labels> labels = ordersDao.findLabel();
        return Response.success(labels);
    }

    /**
     * 邀请/应邀界面  地点选择
     * @return
     */
    @Override
    public Response invitationFindPlace(Double longitude,Double latitude) {
        List<Maps> maps = ordersDao.invitationFindPlace(longitude, latitude);
        return Response.success(maps);
    }

    /**
     * 相应场景参数
//     * @param request
     */
    @Override
    public Response invitationChooseScenes(Integer scene,Integer userId) {
        if (userId == null) {
            logger.warn(" 相应场景参数 所传参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        String scenes = String.valueOf(scene);
        List<Scene> list = sceneDao.findAllScene();
        String scene2 = orderHashRedisTemplate.get(String.valueOf(userId), scenes);
            /**
             * 选择的场景 与 数据库场景对比
             * 若相等 则返回 场景参数
             * hourlyFee    默认场景消费 每小时金额
             *defaultTime   默认最小时长
             *tips   温馨提示
             */
            List<Labels> labels = ordersDao.findLabel();
            Map<String,Object> map = new LinkedHashMap<String,Object>();
            for (int i = 0; i < list.size(); i++) {
                String scene1 = list.get(i).getValue();
                if (scene2==null){
                    String key = list.get(i).getKey();
                    //获取该场景参数
                    Scene sc = sceneDao.findSceneByKey(key);
                    map.put("scene",sc);
                    map.put("labels",labels);
                    System.out.println(map);
                    return Response.success(map);
                }
                else if (scene2.equals(scene1)) {
                    String key = list.get(i).getKey();
                    //获取该场景参数
                    Scene sc = sceneDao.findSceneByKey(key);
                    map.put("scene",sc);
                    map.put("labels",labels);
                    System.out.println(map);
                    return Response.success(map);
                }
            }
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "非法场景选择");
        }
    /**
     * 根据uid查询订单 确认订单完成
     * @param uid 订单id
     */
    @Override
    public Response invitationSure(Integer id,String uid) {
        if (StringUtils.isEmpty(uid)){
            logger.warn(" 完成订单 所传参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        //查询订单
        Orders orders = ordersDao.findOrdersOneByUuid(uid);

        //根据该用户选择的场景 拿到场景参数
        Scene scene = userDao.findSceneByValue(orders.getScenes());
        if (orders==null){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该订单不存在");
        }
        //确定完成订单（手动确认）
        ordersDao.updateOrdersByUid(uid, String.valueOf(PayTypeEnum.STR_SUCCESS.toCode()));

        //如果 需求方  未确认到达 则默认全部到达
        List<OrdersReceive> listReceive =ordersDao.findOrdersReceive(uid);
//        List<ServiceArrivedPlaceResponse> isArrivedType = ordersDao.findAllServiceArrived(uid);
        Integer isTrue=0;
        for (OrdersReceive s:listReceive){
            if (s.getArrived().equals(Boolean.TRUE)) {
                isTrue++;
                break;
            }
        }
        if (isTrue==0){
            ordersDao.serviceAllArrived(uid,Boolean.TRUE);
        }
        /**************************延时 计算费用***********************************/

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Date date = new Date();
//        String nowTime = simpleDateFormat.format(date);
//        SimpDate simpDate = SimpDateFactory.endDate();
//        Map<String,String> cancelTimeMap=null;
//        try {
//            String delayedTime = simpDate.endDate(nowTime,delayed,0);
//            cancelTimeMap = simpDate.transformTime(delayedTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        TransferData data = new TransferData();
//        TimeTask task = new TimeTask();
//        task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
//        task.setTimeTaskName("delayTime:"+uid);
//        //延时计算费用
//        task.setExecuteTime("0 "+cancelTimeMap.get("mm")+" "+cancelTimeMap.get("HH")+" "+cancelTimeMap.get("dd")+" "+cancelTimeMap.get("MM")+" ?");
//        Orders o = new Orders();
//        o.setUuid("delayTime:"+uid);
//        task.setParams(JSON.toJSONString(o));
//        data.setData(JSONObject.toJSONString(task));
//        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
//        rabbitMqPublish.publish(data);
        /**************************延时 计算费用***********************************/
        Cost cost = CalculationFactory.timing();
        Double gratefulFee =Arith.divides(3,orders.getGratefulFree(),orders.getPersonCount());
        Double money = cost.invitation(scene.getHourlyFee(),orders.getOrderPeriod(),orders.getStartTime(),gratefulFee);
        List<OrdersReceive> receive = ordersDao.findOrdersReceive(uid);
        for (int i=0;i<receive.size();i++){
            if (receive.get(i).getArrived()){
                //  计算到 个人用户上
                Integer receiveUserId= receive.get(i).getUserId();
                logger.info("--->手动确认---计算费用到应邀者receiveUserId={}<---",receiveUserId);
                userDao.updateMoney(money,receiveUserId);
            }
        }
        /*
        //计算 应邀者费用
        Cost cost = CalculationFactory.hand();
        Double money = cost.invitation(scene.getHourlyFee(),orders.getOrderPeriod(),orders.getStartTime(),orders.getGratefulFree());
        // 计算各 应邀者费用 更新到 应邀者订单中
        ordersDao.calculation(uid,money);
        List<OrdersReceive> list = ordersDao.findOrdersReceive(uid);

        User user = userDao.findUserByUserId(orders.getUserId());
        //若 人数未满 则 退回 多出部分金额
        if (orders.getPersonCount()>list.size()){
            Double reBackMoney = Arith.multiplys(2,orders.getPersonCount()-list.size());
            userDao.addDetails(uid,0,user.getUuid(),reBackMoney,1);
        }
        //添加到 明细中 其中user_id 为uuid
        for (int i = 0;i<list.size();i++){
            User payedUser = userDao.findUserByUserId(list.get(i).getUserId());
            userDao.addDetails(uid,user.getId(),payedUser.getUuid(),money,1);
        }
*/

        return Response.successByArray();
    }

    /**
     *  删除订单
     * @param uid
     * @return
     */
    @Override
    public Response invitationDeleted(Integer id,String uid) {
        if (StringUtils.isEmpty(uid)||id==null){
            logger.warn("删除订单 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        //获取佣金参数
//        Double brokerage = Double.valueOf(ordersDao.getBrokerage(SysKeyEnum.BROKERAGE.getKey()));
//        if (ordersDao.findOrdersReceive(uid).size()>0){
//            logger.warn("该订单已有人响应 不可删除");
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该订单已有人响应 不可删除");
//        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Orders o  = ordersDao.findOrdersOneByUuid(uid);
        if(o.getPayType()==PayTypeEnum.STR_CANCEL.toCode()){
            logger.info("订单已取消");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单已取消");
        }
        List<OrdersReceive> ordersReceiveList = ordersDao.findOrdersReceive(uid);
       if (ordersReceiveList.size()>0){
           SimpDate simpDate = SimpDateFactory.endDate();
           Long endTime = null;
           Integer ti =null;
           try {
               ti = -Integer.valueOf(ordersDao.getSysParameter(SysKeyEnum.BEFORE_END_TIME.getKey()));
               String  endDate = simpDate.endDate(o.getStartTime(),0.0,ti);
               endTime = simpleDateFormat.parse(endDate).getTime();
           } catch (ParseException e) {
               e.printStackTrace();
           }
           Date now = new Date();
           Long nowTime = now.getTime();
           if (nowTime>endTime){
               logger.warn("开始前"+ti+"分钟内不可取消订单");
               return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"开始前"+ti+"分钟内不可取消订单");
           }
       }
        if (o==null){
            logger.warn("订单信息不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单信息不存在");

        }
//        String tim =null;
        if (o.getStartTime().contains("周")){
            ordersDao.invitationDeleted(uid,PayTypeEnum.STR_CANCEL.toCode());
            return Response.successByArray();
//            tim =o.getStartTime();
        }/**else {
            Date date= null;
            try {
                date = simpleDateFormat.parse(o.getStartTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long timeStemp = date.getTime();
            tim=String.valueOf(timeStemp);
        }*/

        /*Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,id,o.getScenes(),o.getPerhaps(),o.getSelTimeType()));
        if (set==null){
            logger.info("删除redis中的订单 订单不存在");
            return Response.error("删除错误，请联系客服");
        }
        String keys=null;
        for (String str :set){
            keys=str;
        }
*/
        if(o.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode().intValue()&&o.getUserId().intValue()==id.intValue()){
            logger.info("删除邀约单");
            /*if (keys!=null){
                taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,id,o.getScenes(),o.getPerhaps(),o.getSelTimeType()),keys);
            }*/
            //将 db中 订单取消
            ordersDao.invitationDeleted(uid,PayTypeEnum.STR_CANCEL.toCode());
            //查找出最新的单子 并存入
            /*
            Orders or = ordersDao.findStartTimeLimitOne(id,o.getSelTimeType(),o.getScenes());
            if (or!=null){
               List<User> listU = ordersDao.findReceives(or.getUuid());
               Integer size = 0;
                if (listU!=null){
                    size=listU.size();
                }
                String payType = UserUtil.valueById(or.getPayType());
                if (or.getPerhaps()==InvitationTypeEnum.DEMAND.getCode()&&or.getPayType()==PayTypeEnum.STR_WAIT_PAY.toCode()){
                    or.setType(PayTypeEnum.STR_WAIT_CONFIRM.getValue());
                }else {
                    or.setType(payType);
                }
                or.setSize(size);
                or.setUsers(listU);
                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,or.getUserId(),or.getScenes(),or.getPerhaps(),or.getSelTimeType()), or.getStartTimeStamp(),or);
            }
*/
            Scene scene= userDao.findSceneByValue(o.getScenes());
            Double mu = Arith.multiplys(2,o.getOrderPeriod(),scene.getHourlyFee());
            Double money = Arith.add(2,mu,o.getGratefulFree());
//            Double money = Math.abs(o.getMoney());
            logger.info("删除订单 返还金额 money={} ",money);
            userDao.updateMoney(money,id);

            //添加 订单明细
            String detailId = UserUtil.generateUuid();
            //订单明细id--订单号--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
            userDao.addOrdersDetail(detailId,uid,money,OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getKey(),id, OrdersDetailTypeEnum.ORDERS_RETURN_MONEY.getValue(),OrdersDetailTypeEnum.ORDERS.getKey());
            if (ordersReceiveList==null||ordersReceiveList.size()==0){

            }else {
                for (int i =0;i<ordersReceiveList.size();i++) {
                    OrdersReceive ordersReceive = ordersReceiveList.get(i);
                    logger.info("删除邀约单的共享单 userId=", ordersReceive.getUserId());
//                    Orders orders1 = ordersDao.findOrdersOneByUuid(ordersReceive.getOrdersId());
                    /*Set<String> set1 = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, ordersReceive.getUserId(), o.getScenes(), InvitationTypeEnum.DEMAND.getCode(),SelTimeTypeEnum.FIXED.getCode()));
                    String key=null;
                        for (String str: set1){
                            key=str;
                        }
                    if (key!=null){
                        Orders redOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, ordersReceive.getUserId(), o.getScenes(), InvitationTypeEnum.DEMAND.getCode(),SelTimeTypeEnum.FIXED.getCode()),key);
//                        if (orders1.getUuid().equals(redOrders.getUuid())){
                            taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, ordersReceive.getUserId(), o.getScenes(), InvitationTypeEnum.DEMAND.getCode(), SelTimeTypeEnum.FIXED.getCode()), key);
//                        }
                    }
                    */
                        //将 db中 订单取消
//                        ordersDao.invitationDeleted(uid, PayTypeEnum.STR_CANCEL.toCode());
                       /* Orders ord = ordersDao.findReceiveTimeLimitOne(ordersReceive.getUserId(), o.getSelTimeType(),PayTypeEnum.STR_WAIT_PAY.toCode(),InvitationTypeEnum.DEMAND.getCode(),o.getScenes());
                    List<OrdersReceive> ordersServiceList = ordersDao.findOrdersReceiveByUserId(ordersReceive.getUserId(),o.getScenes());
                    Orders lastOrders = getLatestOrders(ord,ordersServiceList);
                    if (lastOrders != null) {
                            String payType = UserUtil.valueById(lastOrders.getPayType());
//                            if (or!=null){
                                if (lastOrders.getPerhaps()==InvitationTypeEnum.DEMAND.getCode()){
//                                if (lastOrders.getPerhaps()==InvitationTypeEnum.DEMAND.getCode()&&or.getPayType()==PayTypeEnum.STR_WAIT_PAY.toCode()){
                                    lastOrders.setType(PayTypeEnum.STR_WAIT_CONFIRM.getValue());
                                }else {
                                    lastOrders.setType(payType);
                                }
//                            }
                            lastOrders.setType(payType);
                            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, lastOrders.getUserId(), lastOrders.getScenes(), InvitationTypeEnum.DEMAND.getCode(), SelTimeTypeEnum.FIXED.getCode()), lastOrders.getStartTimeStamp(), lastOrders);

                    }
                    */
                        User user = userDao.findUserByUserId(o.getUserId());
                        msgValueTemplate.add(String.format(USER_MSG,ordersReceive.getUserId()),String.format(USER_TYPE_ORDERS,o.getUserId(),MsgTypeEnum.EXPEL_USER_MSG.getCode(),user.getNickname()),1);
                }
            }
        }else if (o.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode().intValue()&&o.getUserId().intValue()!=id.intValue()){
            logger.info("取消共享订单");
            ordersDao.deletedReceiver(uid,id);
            List<OrdersReceive> ordersReceiveList1 = ordersDao.findOrdersReceive(uid);
            if (ordersReceiveList1.size()>=1){
                ordersDao.updatePayType(uid,PayTypeEnum.STR_ON_THE_WAY.toCode());
            }else {
                ordersDao.updatePayType(uid,PayTypeEnum.STR_WAIT_CONFIRM.toCode());
            }
            //服务方
            User user2= userDao.findUserByUserId(id);
            //添加删除信息到服务方消息列表
            String name;
            if (StringUtils.isEmpty(user2.getNickname())){
                name=user2.getUuid();
            }else {
                name=user2.getNickname();
            }
            String nameValue = name+":"+uid;
//        Long size = msgValueTemplate.size(String.valueOf(id));
            Long size = msgValueTemplate.size(String.format(USER_MSG,o.getUserId()));
            Long score = size+1;
            msgValueTemplate.add(String.format(USER_MSG,o.getUserId()),String.format(USER_TYPE_ORDERS,o.getUserId(),MsgTypeEnum.SECEDE_MSG.getCode(),nameValue) ,score);
        }else {
            logger.info("删除应约单");
            /*if (keys!=null){
                taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,id,o.getScenes(),InvitationTypeEnum.DEMAND.getCode(),o.getSelTimeType()),tim);
            }*/
            userDao.payOrder(o.getUuid(),PayTypeEnum.STR_CANCEL.toCode());
            //查找出最新的单子 并存入
            //查询出 我所有的 应约的 单子
            /*
            Orders oooo = ordersDao.findReceiveTimeLimitOne(o.getUserId(),o.getSelTimeType(),PayTypeEnum.STR_WAIT_PAY.toCode(),InvitationTypeEnum.DEMAND.getCode(),o.getScenes());
            List<OrdersReceive> ordersServiceList = ordersDao.findOrdersReceiveByUserId(o.getUserId(),o.getScenes());
            Orders lastOrders = getLatestOrders(oooo,ordersServiceList);
            if (lastOrders!=null){
                List<User> list =ordersDao.findReceives(lastOrders.getUuid());
                String type = UserUtil.valueById(lastOrders.getPayType());
                lastOrders.setType(type);
                if (lastOrders.getPerhaps()==InvitationTypeEnum.SERVICE.getCode()&&list!=null){
                    lastOrders.setUsers(list);
                }
                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,o.getUserId(),lastOrders.getScenes(),InvitationTypeEnum.DEMAND.getCode(),
                        lastOrders.getSelTimeType()), String.valueOf(lastOrders.getStartTimeStamp()),lastOrders);

            }
            */
        }



        o.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
        o.setUid(o.getUserId());
        o.setSceneSelEnum(o.getScenes());
//                orders.setMatchRate(90.0);
        if (o.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode()){
            logger.info("删除 需求订单");
            o.setPublishType(OrderPublishTypeEnum.SERVICE);
        }else if (o.getPerhaps().intValue()==InvitationTypeEnum.DEMAND.getCode()){
            logger.info("删除 服务订单");

            o.setPublishType(OrderPublishTypeEnum.DEMAND);
        }
        if (taskOrderCalcBus.deleteOrders(o)){
            logger.info("删除 预加载中的订单成功");
        }else {
            logger.info("删除 预加载中的订单失败");
        };
        return Response.successByArray();

    }

    //接收订单
    /**
     *@param uid 被响应的订单id -- 需求方订单id
     *@param userId  发起应约方的id -- 服务方id
     */
    @Override
    public Response invitationReceive(String uid,Integer userId) throws ParseException {
        logger.info("发起应约");
        if (StringUtils.isEmpty(uid)||userId==null){
            logger.warn("应约订单 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"应约订单 参数为空");
        }
       /*****----*****/   //-->邀约单
//        Orders orders = ordersDao.findOrdersOneByUuid(uid);
        Orders orders = ordersDao.findOrders(uid);
       // 应约方 最新订单
//        String compire = orderHashRedisTemplate.get(String.format(ORDERS_ORDERSID,uid),String.valueOf(userId));
//        if (compire!=null){
//            logger.info("该订单已被拒绝");
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "已应约 请勿重复操作");
//        }
//        orderHashRedisTemplate.put(String.format(ORDERS_ORDERSID,uid),String.valueOf(userId),uid);


        /*****--应约方Id--*****/
        orders.setUid(orders.getUserId());
//        logger.info("应约--邀约单--订单号uid--->"+uid);
//        logger.info("应约--邀约方Id--orders.getUid--->"+orders.getUid());
//        logger.info("发起应约的userId--->"+userId);
        if (orders==null){
            logger.warn("接收订单，订单为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        //根据用户id和订单 查询订单
        OrdersReceive ordersReceive = ordersDao.findInvitationReceive(uid,userId);
        if (ordersReceive!=null){
            logger.warn("接收订单，订单已存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已应约订单，请勿重复操作");
        }

        if (ordersDao.findOrdersReceive(uid).size()>=orders.getPersonCount()){
            logger.warn("接收订单，接单人数已完成");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已到达指定接单人数");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpDate simpDate = SimpDateFactory.endDate();
        List<Orders> listOrders = ordersDao.findAllOfMyReceive(userId);
        Integer exit = 0;
        for (int i=0;i<listOrders.size();i++){
            listOrders.get(i).getStartTime();
            //时间计算
              try {
                  String end = simpDate.endDate(orders.getStartTime(),orders.getOrderPeriod(),0);
                  Date start = simpleDateFormat.parse(orders.getStartTime());
                  Date ed2 = simpleDateFormat.parse(end);
                  String endDate =simpDate.endDate(listOrders.get(i).getStartTime(),listOrders.get(i).getOrderPeriod(),0);
                  Date end1=simpleDateFormat.parse(endDate);
                  Date start1 = simpleDateFormat.parse(listOrders.get(i).getStartTime());

                  if (start1.getTime()<=start.getTime()&&start.getTime()<=end1.getTime()){
                      logger.warn("应约订单 该时间段已存在约会");
                      exit = 1;
                  }
                  if (start1.getTime()<=ed2.getTime()&&start.getTime()<=end1.getTime()){
                      logger.warn("应约订单 该时间段已存在约会");
                      exit = 1;
                  }

            } catch (ParseException e) {
                  e.printStackTrace();
              }
        }
//        List<Orders> list1 = ordersDao.findOrdersByUserIdPerhaps(userId,InvitationTypeEnum.SERVICE.getCode());
        //  应约方 所 发布的 所有的订单
        List<Orders> list1 = ordersDao.findAllMyOrders(userId);
        logger.info("应约方发布的数量="+list1.size());
        String end2 = simpDate.endDate(orders.getStartTime(),orders.getOrderPeriod(),0);
        Long endTime2 = simpleDateFormat.parse(end2).getTime();
        String eOrdersId = null;
        for (int i=0;i<list1.size();i++){
            Orders ods = list1.get(i);
            String end = simpDate.endDate(ods.getStartTime(),ods.getOrderPeriod(),0);
            Long endTime = simpleDateFormat.parse(end).getTime();
            if (!ods.getScenes().equals(orders.getScenes())){
                //                开始时间在 已发 订单内
                if (Long.valueOf(orders.getStartTimeStamp())>=Long.valueOf(ods.getStartTimeStamp())&&Long.valueOf(orders.getStartTimeStamp())<=endTime){
                    exit=1;
                    break;
                }
                //                结束时间在 已发 邀约订单内
                if (endTime2<=endTime&&endTime2>=Long.valueOf(ods.getStartTimeStamp())){
                    exit=1;
                    break;
                }
            }else {
                if (Long.valueOf(orders.getStartTimeStamp())>=Long.valueOf(ods.getStartTimeStamp())&&Long.valueOf(orders.getStartTimeStamp())<=endTime){
                    eOrdersId=ods.getUuid();
                    break;
                }
                if (endTime2<=endTime&&endTime2>=Long.valueOf(ods.getStartTimeStamp())){
                    eOrdersId=ods.getUuid();
                    break;
                }
            }
        }

        if (exit==1){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该时间段已存在订单");
        }
        if (eOrdersId!=null){
            userDao.payOrder(eOrdersId,PayTypeEnum.STR_CANCEL.toCode());
        }

//        List<Orders> myOrders = ordersDao.findOneByUserId(userId);
        List<Orders> ods = ordersDao.findOrdersByUserIdPerhaps(userId,InvitationTypeEnum.DEMAND.getCode());

        for (int j=0;j<ods.size();j++){
            ods.get(j).getStartTime();
            //时间计算

            try {
                Date start = simpleDateFormat.parse(orders.getStartTime());
                String endDate =simpDate.endDate(ods.get(j).getStartTime(),ods.get(j).getOrderPeriod(),0);
                Date end1=simpleDateFormat.parse(endDate);
                Date start1 = simpleDateFormat.parse(ods.get(j).getStartTime());
                if (start1.getTime()<=start.getTime()&&start.getTime()<=end1.getTime()){
                    logger.warn("邀約订单 该时间段已存在约会");
                    exit=2;
                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (exit==2) {
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "该时间段已存在订单");
        }

        /****--对重复邀请进行限制 start--****/
        String compareId = orderHashRedisTemplate.get(String.format(ORDERS_ORDERSID,uid),String.valueOf(userId));
        if (compareId!=null){
            logger.warn("用户userId={}已发出参加信息",userId);
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已发出参加或邀请信息，请勿频繁操作");
        }
        orderHashRedisTemplate.put(String.format(ORDERS_ORDERSID,uid),String.valueOf(userId),uid);
        /****--对重复邀请进行限制 end--****/

        /************************************* 消息列表--将邀请信息 存入redis中 start****************************************/
        User user2 = userDao.findUserByUserId(userId);
        User user1 = userDao.findUserByUserId(orders.getUserId());
        String name;
        if (user2.getNickname()==null|| "".equals(user2.getNickname())){
            name=user2.getUuid();
        }else {
            name=user2.getNickname();
        }

        String nameValue = name+":"+uid+","+orders.getStartTime()+","+orders.getPlace();
        String nameValue1 = user1.getNickname()+":"+uid+","+orders.getStartTime()+","+orders.getPlace();

//        Long size = msgValueTemplate.size(String.valueOf(id));
        Long size = msgValueTemplate.size(String.format(USER_MSG,userId));
        Long size1 = msgValueTemplate.size(String.format(USER_MSG,orders.getUserId()));
        Long score = size+1;
        Long score1 = size1+1;
        msgValueTemplate.add(String.format(USER_MSG,userId),String.format(USER_TYPE_ORDERS,orders.getUserId(),MsgTypeEnum.JOIN_SEND_MSG.getCode(),nameValue1) ,score);
        msgValueTemplate.add(String.format(USER_MSG,orders.getUserId()),String.format(USER_TYPE_ORDERS,userId,MsgTypeEnum.JOIN_RECEIVE_MSG.getCode(),nameValue) ,score1);
        /************************************* 消息列表--将邀请信息 存入redis中 end****************************************/
        /************************************* 订单列表--将邀请信息 存入redis中 start****************************************/
        /**Long size2 = undoneRedisTemplate.size(String.format(USER_UNDONE_PERHAPS_SCENES,userId,InvitationTypeEnum.DEMAND.getCode(),orders.getScenes()));
        Long score2 = size2+1;
        orders.setType(PayTypeEnum.SEND_JOIN.getValue());
        String uValue = orders.getUuid()+":"+InvitationTypeEnum.DEMAND.getCode();
        undoneRedisTemplate.add(String.format(USER_UNDONE_PERHAPS_SCENES,userId,InvitationTypeEnum.DEMAND.getCode(),orders.getScenes()),uValue,score2);
*/
        /*****************订单时效 2小时后 取消订单**************************/
        Date date = new Date();
        String now = simpleDateFormat.format(date);
        String twoHoursAfter = simpDate.endDate(now,2.0,0);
        Map<String,String> cancelTimeMap2=simpDate.transformTime(twoHoursAfter);
        TransferData data2 = new TransferData();
        TimeTask task2 = new TimeTask();
        task2.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        task2.setTimeTaskName("undoneOrders:"+orders.getUuid());
        //当前时间2小时后
        task2.setExecuteTime("0 "+cancelTimeMap2.get("mm")+" "+cancelTimeMap2.get("HH")+" "+cancelTimeMap2.get("dd")+" "+cancelTimeMap2.get("MM")+" ?");
        Orders o2 = new Orders();
        o2.setUuid("undoneOrders:"+orders.getUuid());
        o2.setUid(userId);
        task2.setParams(JSON.toJSONString(o2));
        data2.setData(JSONObject.toJSONString(task2));
        data2.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data2);
        /****************************订单时效**************************/
        User user = userDao.findUserByUserId(userId);
//        if (ServerHandler.sendInvitationNotify(RpcAllowMsgEnum.NOTIFY,orders,user,ServerHandlerTypeEnum.ACCEPT.getType())==false){
            logger.info("应约--用户不在线 发送个推");
            String content = UserUtil.jsonPare("userId,"+user.getId(),"icon,"+user.getIcon(),"ordersId,"+orders.getUuid(),"type,"+ServerHandlerTypeEnum.ACCEPT.getType(),"startTime,"+orders.getStartTime(),"place,"+orders.getPlace());
            User user23 = userDao.findUserByUserId(orders.getUserId());
            Push push = new Push();
            push.setDeviceNum(user23.getCid());
            push.setSendDeviceType(user23.getType());
            push.setSendTopic("订单消息");
            push.setSendContent(content);
//            push.setSendContent(String.valueOf(user2.getId()));
            PushUtils.userPush.getInstance().sendPush(push);
//        }

        return Response.successByArray();
    }

    @Override
//    public Response advance(Integer uuid ,Double longitude,Double latitude,Integer scenes,Integer perhaps) {
    public Response advance(Integer uuid ,String ordersId) {
        if(1==1){
            logger.info("*****--进入预加载--*****");
            if (uuid==null||StringUtils.isEmpty(ordersId)){
                return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"精准匹配 参数为空");
            }
            Orders orders1 = ordersDao.findOrdersOneByUuid(ordersId);
            if (orders1==null){
                logger.warn("该订单不存在 ordersId={}",ordersId);
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该订单不存在");
            }
//            Scene scene = userDao.findSceneByValue(value);
//
//            List<OrdersResponse> list = ordersDao.findOtherOrders(scene.getValue(), perhaps, uuid, pageNum);
//            for (int i=0;i<list.size();i++){
//               Double random = Arith.getRandom();
//               list.get(i).setMatchRate(random);
//            }
//            return Response.success(list);
//            Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,uuid,orders1.getScenes(),orders1.getPerhaps(),orders1.getSelTimeType()));
//
//            Orders orders = null;
//            for (String str:set){
//                orders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,uuid,orders1.getScenes(),orders1.getPerhaps(),orders1.getSelTimeType()),str);
//            }
            Orders orders =null;
            if (Objects.equals(orders1.getPerhaps(), InvitationTypeEnum.SERVICE.getCode())){
                orders = ordersDao.findStartTimeLimitOne(uuid,SelTimeTypeEnum.FIXED.getCode(),orders1.getScenes());
//                if (orders!=null){
//                    orders.setPublishType(OrderPublishTypeEnum.SERVICE);
//                }
            }else {
                Orders ods = ordersDao.findReceiveTimeLimitOne(uuid,SelTimeTypeEnum.FIXED.getCode(),PayTypeEnum.STR_WAIT_PAY.toCode(),orders1.getPerhaps(),orders1.getScenes());
                //根据 用户id查询所有 应约的单子
                List<OrdersReceive> ordersServiceList1 = ordersDao.findOrdersReceiveByUserId(uuid,orders1.getScenes());
                orders =getLatestOrders(ods,ordersServiceList1);
//                if (orders!=null){
//                    orders.setPublishType(OrderPublishTypeEnum.DEMAND);
//                }
            }
            if (orders!=null){
                orders.setSceneSelEnum(orders1.getScenes());
                if (Objects.equals(orders.getPerhaps(), InvitationTypeEnum.SERVICE.getCode())){
                    orders.setPublishType(OrderPublishTypeEnum.SERVICE);
                }else {
                    orders.setPublishType(OrderPublishTypeEnum.DEMAND);
                }
                orders.setUid(orders.getUserId());
                orders.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
            }
            Orders[] taskOrders = taskOrderCalcBus.getOrders(orders);
            for (int i=0;i<taskOrders.length;i++){
                Orders order = taskOrders[i];
               User us =  userDao.findUserByUserId(order.getUserId());
               List<User> userList = new ArrayList<User>();
                if (us!=null){
                    userList.add(us);
                }
               order.setUsers(userList);
            }
            return Response.success(taskOrders);
        }
        /*
       List<Orders> list = ordersDao.findOneByUserId(uuid);
       User user = userDao.findUserByUserId(uuid);
       String s =orderHashRedisTemplate.get(String.valueOf(uuid),String.valueOf(scenes));
       Scene scene = userDao.findSceneByValue(s);

        Orders orders = new Orders();

//        userPosition.setLongitude(longitude);
//        userPosition.setLatitude(latitude);
        if (list.size()==0){

        }else {

        }


        orders.setPerhaps(perhaps);
        orders.setUid(user.getId());
        orders.setUuid(user.getUuid());
//        orders.setLongitude(longitude);
//        orders.setLatitude(latitude);
        if (list.size()==0){
            orders.setSceneSelEnum(scene.getValue());
            orders.setOrderOperateEnum(OrderOperateEnum.ADD.toKey());
        }else {
            orders.setSceneSelEnum(scene.getValue());
            orders.setOrderOperateEnum(OrderOperateEnum.UPDATE.toKey());
        }

        orders.setPublishType(OrderPublishTypeEnum.SERVICE);

//        Orders[] orderList = taskOrderCalcBus.getOrders(orders);
//        if (orderList!=null) {

            return Response.success(taskOrderCalcBus.getOrders(orders));
//        }
*/
        return null;
    }

    @Override
    public Response findAllOfMyOrders(Integer uid,Integer pageNum) {
        if (uid==null){
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        List<OrdersDataResponse> orders = ordersDao.findAllOfMyOrders(uid,pageNum);

        for (int i=0;i<orders.size();i++){
            OrdersDataResponse ordersDataResponse = orders.get(i);
            List<OrdersUserResponse> list = ordersDao.findByUuid(ordersDataResponse.getUuid());
            ordersDataResponse.setOrderUser(list);
//            System.out.println("list="+list);
//            System.out.println("uid="+uid);
//            System.out.println("uuid="+ordersDataResponse.getUuid());
        }
//        OrdersDataResponse ordersDataResponse = ordersDao.findDetailedData(uid);
//        List<OrdersUserResponse> list = ordersDao.findByUuid(uid);
//        ordersDataResponse.setOrderUser(list);
        return Response.success(orders);
    }

    @Override
    public Response findDetailedData(Integer userId,String ordersId) {
        if (StringUtils.isEmpty(ordersId)||userId==null){
            logger.warn("查询订单详情 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        OrdersDataResponse ordersDataResponse = ordersDao.findDetailedData(ordersId);
        List<OrdersUserResponse> list = ordersDao.findByUuid(ordersId);
        ordersDataResponse.setOrderUser(list);
        return Response.success(ordersDataResponse);
    }

    /**
     * 匹配地址
     * @param name
     * @return
     */

    @Override
    public Response findMaps(String name,Double longitude,Double latitude,Integer pageNum,String scene) {
        if (longitude==null||latitude==null||pageNum==null||StringUtils.isEmpty(scene)){
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<Maps> list= ordersDao.findMaps(name,longitude,latitude,pageNum,scene);
        List<MapsResponse> lis= new ArrayList<MapsResponse>();
        for (int i=0;i<list.size();i++){
            Maps maps = list.get(i);
//            Maps maps =  storeDao.findMapsLoAndLaById(store.getMapsId());
            DecimalFormat df = new DecimalFormat("######0.00");
            Double distanceKm = DistanceTools.getKmByPoint(maps.getLatitude(),maps.getLongitude(),latitude,longitude);
            System.out.println(maps.getLatitude()+"=="+maps.getLongitude()+"=="+latitude+"=="+longitude);
            String distance = df.format(distanceKm)+"km";
            MapsResponse mapsResponse = new MapsResponse();
            mapsResponse.setDistance(distance);
            mapsResponse.setName(maps.getName());
            lis.add(mapsResponse);
        }
        return Response.success(lis);
    }

    @Override
    public Response personalDetails(String uid) {
        User user = userDao.personalDetails(uid);

        return Response.success(user);
    }

    @Override
    public Response bill(Integer id, Integer pageNum) {
        Map<String, Object> map = new HashMap<>(16);
       List<Orders> orders =  ordersDao.findOrderByUserId(id,pageNum);
        return Response.success(orders);
    }


    @Override
    public Response createOrders(Integer userId, String startTime, String place, Integer selTimeType, Double orderPeriod,
                                 Integer personCount, Double gratefulFree, String label, Integer perhaps, Integer scenes,
                                 Double money,Double latitude,Double longitude,String address) throws ParseException {

        if (userId == null || startTime == null || place == null || selTimeType == null || orderPeriod == null || personCount == null ||
                perhaps == null || scenes == null||money==null||latitude==null||longitude==null||StringUtils.isEmpty(address)) {
            logger.warn("邀约/应约 所传参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserByUserId(Integer.valueOf(userId));
        /*暂不使用认证
        if (StringUtils.isEmpty(user.getRealName())||StringUtils.isEmpty(user.getIdCard())|| "未绑定".equals(user.getIdCard())){
            logger.warn("用户未认证");
            return Response.response(ResponseTypeEnum.ATTESTATION_FAIL_CODE.toCode(),"请先进行芝麻认证");
        }*/
        String uuid = generateOrderNo(OrdersTypeEnum.INVITE_ORDER,user.getId());

        // 已提交的 场所 同一时间段内 不能提交订单
        String value = orderHashRedisTemplate.get(String.valueOf(userId),String.valueOf(scenes));
        if (value==null){
            logger.warn("场景信息为空");
            return Response.error("场景信息获取失败");
        }
        Scene sc = userDao.findSceneByValue(value);  //获取该场景的参数
        SimpDate simpDate = SimpDateFactory.endDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (Objects.equals(selTimeType, SelTimeTypeEnum.PERIOD.getCode())){
            logger.info("发布 周期订单");
            //周期时间转换
            Map<String,Object> cycleTimeChangeMap = simpDate.cycleTimeChange(startTime);
            //将周期时间转换为 正常  时间
            String cycleToCommon = simpDate.cycleTimeChangeCommon(startTime);
            Date cycDate = simpleDateFormat.parse(cycleToCommon);
            Long cycLong = cycDate.getTime();

            TransferData data = new TransferData();
            TimeTask task = new TimeTask();
            task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
            ordersDao.addCycOrders(uuid, userId, startTime, place, selTimeType, orderPeriod,  label, perhaps,sc.getValue(),cycLong,address);
            Orders o = ordersDao.findCycOrders(uuid);
            o.setHourlyFee(sc.getHourlyFee());
            o.setType(PayTypeEnum.STR_WAIT_CONFIRM.getValue());
            //若 为空 则 放入
            if (taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,value,perhaps,SelTimeTypeEnum.PERIOD.getCode())).size()==0){
                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,value,perhaps,SelTimeTypeEnum.PERIOD.getCode()),startTime,o);
            }else {
                Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,value,perhaps,1));

                String keys=null;
                for (String key:set){
                    keys=key;
                }
                Orders orders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,value,perhaps,1),keys);

               if (Long.valueOf(orders.getStartTimeStamp())<= cycLong){
                   taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,value,perhaps,SelTimeTypeEnum.PERIOD.getCode()),keys);
                   taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,value,perhaps,SelTimeTypeEnum.PERIOD.getCode()),startTime,o);
               }
            }
            //            task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
            task.setTimeTaskName("cycleOrders:"+uuid);
            //秒 分 时 日 月 周
            task.setExecuteTime("0 "+cycleTimeChangeMap.get("mm")+" "+cycleTimeChangeMap.get("HH")+" ? * "+cycleTimeChangeMap.get("weekDay")+"");
            Orders orders = new Orders();
            orders.setUuid("cycleOrders:"+uuid);
            task.setParams(JSON.toJSONString(orders));
            data.setData(JSONObject.toJSONString(task));
            data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
            rabbitMqPublish.publish(data);

            return Response.success(o);
        }

        if (startTime.contains("周")){
            startTime=simpDate.cycleTimeChangeCommon(startTime);
        }
        //时间计算
        Date start = simpleDateFormat.parse(startTime);
        String endTime =simpDate.endDate(startTime,orderPeriod,0);
        Date endDate = simpleDateFormat.parse(endTime);
        Date now = new Date();
        String now2 = simpleDateFormat.format(now);
        //约会开始 时间必须在__时间以后
        String now3 = simpDate.endDate(now2,0.0, sc.getFirstStart());
        String cancelTime = simpDate.endDate(now2,0.0, 5);
        Map<String,String> cancelTimeMap = simpDate.transformTime(cancelTime);
        Date now4 = simpleDateFormat.parse(now3);
        int time = now4.compareTo(start);
        if (time >= 0) {
            logger.info("预定时间必须大于当前时间={}分钟", sc.getFirstStart());
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "预定时间必须大于当前时间"+sc.getFirstStart()+"分钟");
        }

        List<Orders> list1 = ordersDao.findAllOfMyReceive(userId);
        Integer exit=0;
        //查找我所有的订单 无论邀约或者应约
        List<Orders> li = ordersDao.findAllMyOrders(userId);
        for (int i = 0; i < li.size(); i++) {
            Orders orders = li.get(i); //获取订单
            String star = orders.getStartTime();  //开始时间
            if (star.contains("周")){
                star=simpDate.cycleTimeChangeCommon(orders.getStartTime());
            }
            Double period = orders.getOrderPeriod();  //时间间隔
            //订单结束时间
            String end = simpDate.endDate(star, period,0);

            Date star1 = simpleDateFormat.parse(star);
            Date end1 = simpleDateFormat.parse(end);

//            Date start = simpleDateFormat.parse(startTime);

            //判断用户输入的 开始时间是否在 已发布的订单 约会时间内
            if (star1.getTime()<=start.getTime()&&start.getTime()<=end1.getTime()){
                logger.warn("订单开始时间 已存在约会");
                exit++;
                break;
            }
            if (star1.getTime()<=endDate.getTime()&&start.getTime()<=end1.getTime()){
                logger.warn("订单结束时间 已存在约会");
                exit++;
                break;
            }
        }
        if (exit!=0){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "已存在约会 请选择合理的时间范围");
        }
        for (int j = 0; j < list1.size(); j++) {
            Orders orders = list1.get(j); //获取订单
            String star = orders.getStartTime();  //开始时间
            if (star.contains("周")){
                star=simpDate.cycleTimeChangeCommon(orders.getStartTime());
            }
            Double period = orders.getOrderPeriod();  //时间间隔
            //订单结束时间
            String end = simpDate.endDate(star, period,0);

            Date star1 = simpleDateFormat.parse(star);
            Date end1 = simpleDateFormat.parse(end);

//            Date start = simpleDateFormat.parse(startTime);

            //判断用户输入的 开始时间是否在 已发布的订单 约会时间内
            if (star1.getTime()<=start.getTime()&&start.getTime()<=end1.getTime()){
                logger.warn("已存在应约单 不可发布邀约");
                exit++;
                break;
            }
            if (star1.getTime()<=endDate.getTime()&&start.getTime()<=end1.getTime()){
                logger.warn("订单结束时间 已存在约会");
                exit++;
                break;
            }
        }
        if (exit!=0){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "已存在应约单 不可发布邀约");
        }
//        Maps places = ordersDao.findMap(place);

        if (selTimeType == 0 ) {  //当选择的是固定周期，邀约的时候进行 计算费用

            Double hourlyFee = sc.getHourlyFee();  //该场景需要的 每小时每人 费用
            Cost cost = CalculationFactory.hand();
            Double countMoney = cost.calculate(hourlyFee, personCount, orderPeriod, startTime, gratefulFree);

            //添加订单 待支付状态
            if (perhaps==1) {
//                countMoney = countMoney * PayTypeEnum.PAY.toCode();
            }else if (perhaps==2){
                countMoney = sc.getHourlyFee();
            }
            if (startTime.contains("周")){
                startTime=simpDate.cycleTimeChangeCommon(startTime);
            }
            //开始时间时间戳

            Long startTimeStamp =simpleDateFormat.parse(startTime).getTime();
            ordersDao.addInvitation(countMoney, uuid, userId, startTime, place, selTimeType, orderPeriod, personCount, gratefulFree, label, perhaps, sc.getValue(), longitude, latitude, PayTypeEnum.STR_WAIT_PAY.toCode(),uuid,startTimeStamp,address);
            Orders orders = ordersDao.findOrdersOneByUuid(uuid);
            Date date=simpleDateFormat.parse(orders.getStartTime());
            Long timeStemp = date.getTime();
            if (Objects.equals(perhaps, InvitationTypeEnum.DEMAND.getCode())){
                logger.info("发布应约单ordersId={}",uuid);
              /** Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,sc.getValue(),perhaps,selTimeType));
               String key =null;
               for (String str :set){
                   key =str;
               }
                Orders valueOrders =null;
                if (key!=null){
                    valueOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,sc.getValue(),perhaps,selTimeType),key);
                }
                orders.setHourlyFee( sc.getHourlyFee());
                orders.setType(PayTypeEnum.STR_WAIT_CONFIRM.getValue());
                if (valueOrders==null){
                    taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),String.valueOf(timeStemp),orders);
                }else {
                    Date ordersInRedis = simpleDateFormat.parse(valueOrders.getStartTime());
                    Long preTime = ordersInRedis.getTime();
                    //若 订单的开始时间 <= 已存在 redis里的 订单开始时间则 更新

                    if (timeStemp<=preTime){
                        taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,valueOrders.getScenes(),InvitationTypeEnum.DEMAND.getCode(),valueOrders.getSelTimeType()),String.valueOf(preTime));
                        taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,orders.getScenes(),InvitationTypeEnum.DEMAND.getCode(),orders.getSelTimeType()),String.valueOf(timeStemp),orders);
                    }
                }*/
                logger.info("添加的订单用户id={},setUid={}",orders.getUserId(),userId);
                orders.setSceneSelEnum(sc.getValue());
                orders.setUid(userId);
                orders.setLatitude(latitude);
                orders.setLongitude(longitude);
                if (Objects.equals(perhaps, InvitationTypeEnum.SERVICE.getCode())){
                    orders.setPublishType(OrderPublishTypeEnum.SERVICE);
                }else {
                    orders.setPublishType(OrderPublishTypeEnum.DEMAND);
                }
                orders.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
                taskOrderCalcBus.addOrders(orders);

                /*==========================================================================================*/
                /*================================存====入====redis===str===================================*/
                /*==========================================================================================*/
                logger.info("调整习惯场景排序");
                Map<String, String> map2 = new LinkedHashMap<String, String>();

                //获取所有的keys
                Set<String> keys = orderHashRedisTemplate.keys(String.valueOf(orders.getUserId()));
                String scen = null;
                for (String val : keys) {
                    //返回缓存值
                    if (orderHashRedisTemplate.get(String.valueOf(orders.getUserId()),val).equals(orders.getScenes())){
                        scen=val;
                    };
                    map2.put(val, orderHashRedisTemplate.get(String.valueOf(orders.getUserId()), val));
                }
                //重新排序 存入redis默认值 习惯排序
                Map<String,String> maps = new LinkedHashMap<>();
                maps.put("0",orderHashRedisTemplate.get(String.valueOf(orders.getUserId()),scen));

                for (String va : keys) {

                    if (Integer.valueOf(scen)>Integer.valueOf(va)){
                        maps.put(String.valueOf(Integer.valueOf(va)+1),orderHashRedisTemplate.get(String.valueOf(orders.getUserId()),va));
                    }
                    if (Integer.valueOf(scen)<Integer.valueOf(va)){
                        maps.put(String.valueOf(Integer.valueOf(va)),orderHashRedisTemplate.get(String.valueOf(orders.getUserId()),va));
                    }

                }
                orderHashRedisTemplate.putAll(String.valueOf(orders.getUserId()),maps);
                /*==========================================================================================*/
                /*================================存====入====redis===end===================================*/
                /*==========================================================================================*/
                Map<String,String> startTransformTime = simpDate.transformTime(orders.getStartTime());

                //若开始时间前 响应人数未 到达指定 人数 取消订单
                TransferData data1 = new TransferData();
                TimeTask task1 = new TimeTask();
                task1.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
                task1.setTimeTaskName("demandMissCatchPersonCount:"+orders.getUuid());
                task1.setExecuteTime("0 "+startTransformTime.get("mm")+" "+startTransformTime.get("HH")+" "+startTransformTime.get("dd")+" "+startTransformTime.get("MM")+" ?");
                Orders ordersTimeEntity = new Orders();
                ordersTimeEntity.setUserId(orders.getUserId());
                //未达到人数要求
                ordersTimeEntity.setUuid("demandMissCatchPersonCount:"+orders.getUuid());
                ordersTimeEntity.setScenes(scen);
                ordersTimeEntity.setGratefulFree(orders.getGratefulFree());
                ordersTimeEntity.setHourlyFee(sc.getHourlyFee());
                ordersTimeEntity.setOrderPeriod(orders.getOrderPeriod());
                ordersTimeEntity.setSelTimeType(orders.getSelTimeType());
                ordersTimeEntity.setPerhaps(orders.getPerhaps());
                ordersTimeEntity.setStartTime(orders.getStartTime());
                task1.setParams(JSON.toJSONString(ordersTimeEntity));
                data1.setData(JSONObject.toJSONString(task1));
                data1.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
                rabbitMqPublish.publish(data1);
            }

            /*************************************************************/
            /*****************订单时效 5分钟后取消订单**************************/
            /**************************************/
            TransferData data = new TransferData();
            TimeTask task = new TimeTask();
            task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
            task.setTimeTaskName("orderTime:"+uuid);
            //当前时间5分钟后
            task.setExecuteTime("0 "+cancelTimeMap.get("mm")+" "+cancelTimeMap.get("HH")+" "+cancelTimeMap.get("dd")+" "+cancelTimeMap.get("MM")+" ?");
            Orders o = new Orders();
            o.setUuid("orders:"+uuid);
            task.setParams(JSON.toJSONString(o));
            data.setData(JSONObject.toJSONString(task));

            data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
            rabbitMqPublish.publish(data);
            /*************************************************************/
            /****************************订单时效**************************/
            /*************************************************************/


//            orders.setPerhaps(1);
//            if(taskOrderCalcBus.addOrders(orders)){
                Map<String,String> jsonObject = new  HashMap<String,String> ();
                jsonObject.put("ordersId",uuid);
                if (perhaps.intValue()==InvitationTypeEnum.DEMAND.getCode()){
                    countMoney=0.0;
                }else {
                    countMoney=Math.abs(countMoney);
                }
                logger.info("=====================================================");
                logger.info("=====需支付金额======"+countMoney);
                jsonObject.put("money",String.valueOf(countMoney));
                jsonObject.put("tips","订单开始前30分钟内不可取消订单");




            return Response.success(jsonObject);


        }
        logger.warn("发布失败");
        return Response.error(ResponseTypeEnum.ERROR_CODE);
    }

    @Override
    public Response agreeInvitation(Integer id, String orderId,String uuid) throws ParseException {
        logger.info("发出邀请");
        if (id==null||StringUtils.isEmpty(orderId)||StringUtils.isEmpty(uuid)){
            logger.info("邀请接单 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }

        //需求方用户
        User user = userDao.findUserByUserId(id);
        /*暂时不用 芝麻信用认证
        if (StringUtils.isEmpty(user.getRealName())||StringUtils.isEmpty(user.getIdCard())|| "未绑定".equals(user.getIdCard())){
            logger.warn("用户未认证");
            return Response.response(ResponseTypeEnum.ATTESTATION_FAIL_CODE.toCode(),"请先进行芝麻认证");
        }*/
        // TODO: 2017/9/6  测试 芝麻信用
//        Integer userGrade = user.getUserGrade();

        // TODO: 2017/8/28 会员用户也可发布，视为普通订单
        //场景相同  单子未完成  筛选 时间在 邀请者的订单时间内
//        if (userGrade == UserGradeEnum.A_PERSON.toCode() && userGrade == UserGradeEnum.B_PERSON.toCode()) {
//            logger.warn("签约用户不可邀请用户接单");
//           return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "签约用户不可邀请用户接单");
//        }
        //服务方订单
        Orders orders =ordersDao.findOrdersOneByUuid(orderId);
        //服务方用户
        User user2 = userDao.findUserByUserId(orders.getUserId());

        //需求方订单
        Orders invitationOrders = ordersDao.findOrdersOneByUuid(uuid);
        //邀请方 邀约单
//        Orders orders1 = ordersDao.findStartTimeLimitOne(id,orders.getSelTimeType(),orders.getScenes());
//        String compireId = orderHashRedisTemplate.get(orderId,String.valueOf(id));
//        orderHashRedisTemplate.put(orderId,String.valueOf(id),orders1.getUuid());
//        if (orders1==null){
//            ordersDao.addInvitation(id,orders.getStartTime(),orders.getPlace(),orders.getSelTimeType(),orders.getOrderPeriod(),
//                    1,0.0,orders.getLabel(),InvitationTypeEnum.SERVICE.getCode(),orders.getScenes(),orders.getMoney());
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"请先发布邀约单");
//        }
        /****--对重复邀请进行限制 start--****/
        String compareId = orderHashRedisTemplate.get(String.format(ORDERS_ORDERSID,orderId),String.valueOf(id));
        if (compareId!=null){
            logger.warn("用户userId={}已发出邀请",id);
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已发出邀请，请勿频繁操作");
        }
        orderHashRedisTemplate.put(String.format(ORDERS_ORDERSID,orderId),String.valueOf(id),orderId);
        /****--对重复邀请进行限制 end--****/

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpDate simpDate = SimpDateFactory.endDate();
        List<Orders> list1 = ordersDao.findOrdersByUserIdPerhaps(id,InvitationTypeEnum.SERVICE.getCode());
        logger.info("邀约发布的数量="+list1.size());
        Integer exit=0;
        String end2 = simpDate.endDate(orders.getStartTime(),orders.getOrderPeriod(),0);
        Long endTime2 = simpleDateFormat.parse(end2).getTime();
        for (int i=0;i<list1.size();i++){
            Orders ods = list1.get(i);
            String end = simpDate.endDate(ods.getStartTime(),ods.getOrderPeriod(),0);
            Long endTime = simpleDateFormat.parse(end).getTime();
            if (ods.getUuid()!=orderId&&!ods.getScenes().equals(ods.getScenes())){
                //                开始时间在 已发 邀约订单内
                if (Long.valueOf(orders.getStartTimeStamp())>=Long.valueOf(ods.getStartTimeStamp())&&Long.valueOf(orders.getStartTimeStamp())<=endTime){
                    exit=1;
                }
                //                结束时间在 已发 邀约订单内
                if (endTime2<=endTime&&endTime2>=Long.valueOf(ods.getStartTimeStamp())){
                    exit=1;
                }
            }
        }
        List<Orders> list2 = ordersDao.findOrdersByUserIdPerhaps(id,InvitationTypeEnum.DEMAND.getCode());
        logger.info("应约发布的数量="+list2.size());
        for (int j=0;j<list2.size();j++){
            Orders ods = list1.get(j);
            String end = simpDate.endDate(ods.getStartTime(),ods.getOrderPeriod(),0);
            Long endTime = simpleDateFormat.parse(end).getTime();
            if (ods.getUuid()!=orderId&&!ods.getScenes().equals(ods.getScenes())){
                //                开始时间在 已发 邀约订单内
                if (Long.valueOf(orders.getStartTimeStamp())>=Long.valueOf(ods.getStartTimeStamp())&&Long.valueOf(orders.getStartTimeStamp())<=endTime){
                    exit=1;
                }
                //                结束时间在 已发 邀约订单内
                if (endTime2<=endTime&&endTime2>=Long.valueOf(ods.getStartTimeStamp())){
                    exit=1;
                }
            }
        }
        if (exit==1){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该时间段已发布订单");
        }
        Date date = new Date();
        String now = simpleDateFormat.format(date);
        String twoHoursAfter = simpDate.endDate(now,2.0,0);
        Map<String,String> cancelTimeMap2=simpDate.transformTime(twoHoursAfter);
       /************************************* 消息列表--将邀请信息 存入redis中 start****************************************/
        String name;
        if (user.getNickname()==null|| "".equals(user.getNickname())){
            name=user.getUuid();
        }else {
            name=user.getNickname();
        }
        User user1 = userDao.findUserByUserId(orders.getUserId());
        //需求方昵称-需求方订单号-服务方订单号
        String nameValue = name+":"+uuid+":"+orderId;
        String nameValue1 = user1.getNickname()+":"+uuid+","+orders.getStartTime()+","+orders.getPlace();
//        String nameValue1 = user1.getNickname()+":"+uid+","+orders.getStartTime()+","+orders.getPlace();
//        Long size = msgValueTemplate.size(String.valueOf(id));
        Long size = msgValueTemplate.size(String.format(USER_MSG,id));
        Long size1 = msgValueTemplate.size(String.format(USER_MSG,orders.getUserId()));
        Long score = size+1;
        Long score1 = size1+1;
        msgValueTemplate.add(String.format(USER_MSG,id),String.format(USER_TYPE_ORDERS,orders.getUserId(),MsgTypeEnum.INVITATION_SEND_MSG.getCode(),nameValue1) ,score);
        msgValueTemplate.add(String.format(USER_MSG,orders.getUserId()),String.format(USER_TYPE_ORDERS,id,MsgTypeEnum.INVITATION_RECEIVE_MSG.getCode(),nameValue) ,score1);
        /************************************* 消息列表--将邀请信息 存入redis中 end****************************************/
        /************************************* 订单列表--将邀请信息 存入redis中 start****************************************/
        /**Long size2 = undoneRedisTemplate.size(String.format(USER_UNDONE_PERHAPS_SCENES,id,InvitationTypeEnum.SERVICE.getCode(),orders.getScenes()));
        Long score2 = size2+1;
        orders.setType(PayTypeEnum.SEND_INVITATION.getValue());
        String uValue = uuid+":"+InvitationTypeEnum.SERVICE.getCode();

        undoneRedisTemplate.add(String.format(USER_UNDONE_PERHAPS_SCENES,id,InvitationTypeEnum.SERVICE.getCode(),orders.getScenes()),uValue,score2);
        */orders.setUid(orders.getUserId());
        /*****************订单时效 2小时后 取消订单**************************/
        TransferData data2 = new TransferData();
        TimeTask task2 = new TimeTask();
        task2.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        task2.setTimeTaskName("undoneOrders:"+orders.getUuid());
        //当前时间2小时后
        task2.setExecuteTime("0 "+cancelTimeMap2.get("mm")+" "+cancelTimeMap2.get("HH")+" "+cancelTimeMap2.get("dd")+" "+cancelTimeMap2.get("MM")+" ?");
        Orders o2 = new Orders();
        o2.setUuid("undoneOrders:"+orders.getUuid());
        o2.setUid(id);
        task2.setParams(JSON.toJSONString(o2));
        data2.setData(JSONObject.toJSONString(task2));
        data2.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data2);
        /****************************订单时效**************************/
        /************************************* 订单列表--将邀请信息 存入redis中 end****************************************/

//        if (!ServerHandler.sendInvitationNotify(RpcAllowMsgEnum.NOTIFY,orders,user,ServerHandlerTypeEnum.INVITATION.getType())){
            logger.info("邀请--rpc 不在线");
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("userId",user.getId());
//            jsonObject.put("icon",user.getIcon());
//            jsonObject.put("type", ServerHandlerTypeEnum.INVITATION.getType());
//            jsonObject.put("ordersId",orders.getUuid());
//            String tips=user.getNickname()+"想加入您的"+orders.getStartTime()+"开始的"+orders.getPlace()+"的订单";
            String content = UserUtil.jsonPare("userId,"+user.getId(),"icon,"+user.getIcon(),"ordersId,"+uuid,"serviceOrdersId,"+orderId,"type,"+ ServerHandlerTypeEnum.INVITATION.getType(),"invitationId,"+uuid);
//            User user2 = userDao.findUserByUserId(orders.getUid());
            Push push = new Push();
            push.setDeviceNum(user2.getCid());
            push.setSendDeviceType(user2.getType());
            push.setSendTopic("订单消息");
            push.setSendContent(content);
//            push.setSendContent(String.valueOf(user2.getId()));
            PushUtils.userPush.getInstance().sendPush(push);
//        }
        return Response.successByArray();


//        Orders orders = ordersDao.findOrdersOneByUuid(orderId);
        //查询我发布的订单、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、
//        List<Orders> listMyOrders = ordersDao.findMyOrdersByUserId(id);
//        Orders orders1 = ordersDao.findStartTimeLimitOne(id,SelTimeTypeEnum.FIXED.getCode(),orders.getScenes());
//
//        if (orders1==null){
//
//        }




    }

    @Override
    public Response advertisementPic() {
        String advertisementPic =  ordersDao.getSysParameter(SysKeyEnum.ADVERTISING_PIC.getKey());
        return Response.success(advertisementPic);
    }

    @Override
    public Response advertisementMap() {
        String advertisementMap =  ordersDao.getSysParameter(SysKeyEnum.ADVERTISING_MAP.getKey());
        return Response.success(advertisementMap);
    }

    @Override
    public Response seekOtherOrders(String scenes, Integer perhaps, Integer id, Integer pageNum) {
        if (StringUtils.isEmpty(scenes)||perhaps==null||id==null||pageNum==null){
            logger.warn("查找他人订单 参数为空");
            return Response.success(ResponseTypeEnum.EMPTY_PARAM.toCode(),"查找他人订单 参数为空");
        }
        User user =  userDao.findUserByUserId(id);
        if (user==null){
            logger.warn("查找他人订单 该用户不存在");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"用户信息错误");
        }

        Integer code = user.getUserGrade();


        String value = orderHashRedisTemplate.get(String.valueOf(id),scenes);
        List<OrdersResponse> list = ordersDao.findOtherOrders(value, perhaps, id,pageNum);
        return Response.success(list);
    }

    @Override
    public Response seekOrdersDetailed(Integer id, String ordersId) {
        if (id==null||StringUtils.isEmpty(ordersId)){
            logger.warn("订单详情 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"订单详情参数为空");
        }
        OrdersResponse ordersResponse = ordersDao.findOrdersByOrdersId(ordersId);
        if(ordersResponse==null){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该订单不存在");
        }
        List<OrdersUserResponse> list = ordersDao.findByUuid(ordersId);
        List<OrdersUserResponse> list1 = new ArrayList<OrdersUserResponse>();

        User user = userDao.findUserByUserId(ordersResponse.getUserId());
        OrdersUserResponse ordersUserResponse = new OrdersUserResponse();
        ordersUserResponse.setNickName(user.getNickname());
        ordersUserResponse.setPhoneNum(user.getPhoneNum());
        ordersUserResponse.setOrdersId(ordersResponse.getUuid());
        ordersUserResponse.setIcon(user.getIcon());
        ordersUserResponse.setId(user.getId());
        ordersUserResponse.setAge(user.getAge());
        ordersUserResponse.setGender(String.valueOf(user.getGender()));
        ordersUserResponse.setOccupation(user.getOccupation());
        ordersUserResponse.setAddress(ordersResponse.getAddress());
        list1.add(ordersUserResponse);



        if (ordersResponse!=null) {
            if (id.intValue() == ordersResponse.getUserId().intValue()) {
//                for (OrdersUserResponse os : list) {
//                    User us = userDao.findUserOneByPhone(os.getPhoneNum());
//                    ServiceArrivedPlaceResponse isArrived = ordersDao.findServiceArrived(us.getId(), ordersId);
//                    if (isArrived != null)
//                        os.setIsPress(ISPRESS);
//                }
                ordersResponse.setOrderUser(list);
            } else {
//                for (OrdersUserResponse os : list1) {
//                    User us = userDao.findUserOneByPhone(os.getPhoneNum());
//                    ServiceArrivedPlaceResponse isArrived = ordersDao.findServiceArrived(us.getId(), ordersId);
//                    if (isArrived != null)
//                        os.setIsPress(ISPRESS);
//                }
                ordersResponse.setOrderUser(list1);
            }
        }
        OrdersReceive ordersReceive = ordersDao.findInvitationReceive(ordersId, id);
        Orders odr = ordersDao.findOrdersOneByUuid(ordersId);
//        if (ordersReceive!=null){
//            ordersResponse.setPerhaps(InvitationTypeEnum.DEMAND.getCode());
//        }else if (odr.getUserId()!=id){
//            ordersResponse.setPerhaps(InvitationTypeEnum.DEMAND.getCode());
         if (odr==null){
            ordersResponse.setPerhaps(InvitationTypeEnum.DEMAND.getCode());
        }
        return Response.success(ordersResponse);
    }

    /**
     *
     * @param uuid 需求方订单id
     * @param uid 服务方订单id
     * @param userId2 邀请方id
     * @param userId 接收方id
     * @return
     */
    @Override
    public Response invitationAgree(String uuid,String uid,Integer userId2, Integer userId) {
        if (StringUtils.isEmpty(uuid)||StringUtils.isEmpty(uid)||userId==null||userId2==null){
            logger.warn("邀请接收 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        logger.info("邀请 接收订单--服务方userId={}",userId2);
        Map<String,Object> map = new HashMap<String,Object>();
        //服务方 订单信息 查询该订单
        Orders orders = ordersDao.findOrdersOneByUuid(uid);
        //查询 需求方订单
        Orders orders1 = ordersDao.findOrdersOneByUuid(uuid);
        userDao.payOrder(orders.getUuid(),PayTypeEnum.STR_CANCEL.toCode());
        if (orders1==null){
            logger.info("邀约方已取消发布的邀请订单");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"邀约方已取消发布的邀请订单");
        }
        Scene scene = userDao.findSceneByValue(orders.getScenes());

        //邀请方 用户
        User user = userDao.findUserByUserId(userId2);

        if (orders==null){
            logger.warn("接收订单，订单为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"发布的应约单已删除");
        }else {
            orders.setUid(userId2);
        }
        //根据用户id和订单 查询订单
        OrdersReceive ordersReceive = ordersDao.findInvitationReceive(orders1.getUuid(),userId);
        if (ordersReceive!=null){
            logger.warn("接收订单，订单已存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已应约订单，请勿重复操作");
        }

        if (ordersDao.findOrdersReceive(orders1.getUuid()).size()>=orders1.getPersonCount()){
            logger.warn("接收订单，接单人数已完成");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已到达指定接单人数");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpDate simple = SimpDateFactory.endDate();
        Date start = null;
        Date end =null;
        try {
            start = simpleDateFormat.parse(orders1.getStartTime());
            String endTime = simple.endDate(orders1.getStartTime(),orders1.getOrderPeriod(),0);
            end=simpleDateFormat.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //添加 到接受订单
        ordersDao.addInvitationReceive(orders1.getUuid(),userId);
        Orders orders2 = ordersDao.findOrdersOneByUuid(uuid);
        //设置订单的状态 start
        List<OrdersReceive> ordersReceiveList = ordersDao.findOrdersReceive(orders1.getUuid());
        String type;
        if (orders2.getPersonCount().intValue()==ordersReceiveList.size()){
            userDao.payOrder(orders2.getUuid(),PayTypeEnum.STR_WAIT_START.toCode());
            type = UserUtil.valueById(PayTypeEnum.STR_WAIT_START.toCode());
            try {
                //订单状态修改为 待进行，进行中的时候 删除精准匹配中的订单
                deleteTaskOrders(orders);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            userDao.payOrder(orders2.getUuid(),PayTypeEnum.STR_ON_THE_WAY.toCode());
            type = UserUtil.valueById(PayTypeEnum.STR_ON_THE_WAY.toCode());
        }
        Long startTimeLong = start.getTime();
        Long endTimeLong = end.getTime();
        orders.setType(type);
        orders1.setType(type);
        //设置订单的状态 end
        //放入邀请方 用户
        List<User> al = new ArrayList<>();
        List<User> list = ordersDao.findReceives(orders1.getUuid());

        al.add(user);
        orders1.setUsers(al);
        if (list!=null) {
            orders1.setSize(al.size());
        }

            //修改 邀约方 状态
//            userDao.payOrder(orders1.getUuid(),PayTypeEnum.STR_ON_THE_WAY.toCode());



        //根据订单id获取所有响应的人
        List<User> list2 = ordersDao.findReceives(uid);
        Integer personCount = orders.getPersonCount();
        Integer proportion = scene.getProportion();

        if (list2.size()>=personCount*proportion/100&&list2.size()<personCount){
            map.put("tip", OrdersEnum.BASIC_CODE.toCode());
        }else if (list2.size()<personCount*proportion/100){
            map.put("tip",OrdersEnum.NONE_CODE.toCode());
        }else {
            map.put("tip",OrdersEnum.COMPLETE_CODE.toCode());
        }
//        List<User> arList = new ArrayList<>();
//        arList.add(user);
//        map.put("users",arList);
        map.put("users",list2);

        //应约完成 回传头像
//        orders.setUsers(arList);
        orders.setUsers(list2);
//        if (!ServerHandler.sendNotify(RpcAllowMsgEnum.NOTIFY,orders,ServerHandlerTypeEnum.INVITATION.getType())){
//            User user2 = userDao.findUserByUserId(orders.getUserId());
//            Push push = new Push();
//            push.setDeviceNum(user.getCid());
//            push.setSendDeviceType(user.getType());
//            push.setSendTopic("订单消息");
//            push.setSendContent(String.valueOf(user2.getId()));
//            PushUtils.userPush.getInstance().sendPush(push);
//        }

        return  Response.successByArray();
    }

    @Override
    public Response invitationRefuse(String ordersId, Integer id,Integer userId) {
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
        // TODO: 2017/7/18    拒绝 邀请

        //删除 订单列表订单
//        String uValue = orders.getUuid()+":"+InvitationTypeEnum.SERVICE.getCode();
//
//        undoneRedisTemplate.remove(String.format(USER_UNDONE_PERHAPS_SCENES,userId,orders.getPerhaps(),orders.getScenes()),uValue);
        orders.setUid(id);
        //添加删除 信息到服务方消息列表
        User user2 = userDao.findUserByUserId(id);
        String name;
        if (StringUtils.isEmpty(user2.getNickname())){
            name=user2.getUuid();
        }else {
            name=user2.getNickname();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String  now = simpleDateFormat.format(new Date());
        String nameValue = name+":"+ordersId+","+now;
//        String nameValue = name+":"+ordersId;
//        Long size = msgValueTemplate.size(String.valueOf(id));
        Long size = msgValueTemplate.size(String.format(USER_MSG,orders.getUserId()));
        Long score = size+1;
        msgValueTemplate.add(String.format(USER_MSG,orders.getUserId()),String.format(USER_TYPE_ORDERS,id,MsgTypeEnum.REFUSE_JOIN_MSG.getCode(),nameValue),score);
//        if (!ServerHandler.sendRefuseNotify(RpcAllowMsgEnum.NOTIFY,orders,ServerHandlerTypeEnum.INVITATION.getType())){
//            User user = userDao.findUserByUserId(userId);
//            Push push = new Push();
//            push.setDeviceNum(user.getCid());
//            push.setSendDeviceType(user.getType());
//            push.setSendTopic("订单消息");
//            String content = UserUtil.jsonPare("userId,"+user.getId(),"icon,"+user.getIcon(),"ordersId,"+orders.getUuid(),"type,"+ ServerHandlerTypeEnum.REFUSE_INV.getType());
//            push.setSendContent(content);
//            PushUtils.userPush.getInstance().sendPush(push);
//        }
        return Response.successByArray();
    }

    /**
     * 订单为 发布的邀约单参加时
     * @param ordersId 邀请方 订单
     * @param userId2 服务方id
     * @param userId 需求方 id
     * @return
     */
    @Override
    public Response invitationReceiveSure(String ordersId,Integer userId2, Integer userId) {
        logger.info("*****--应约--确认接受--*****");
        if (StringUtils.isEmpty(ordersId)||userId==null||userId2==null){
            logger.info("接受应约 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        /******--需求方--发布的邀约单--*****/
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
        //判断 userId2 该用户 是服务方 还是需求方
//        Orders isService = ordersDao.findOrdersByStartTimeStemp(userId2,orders.getStartTimeStamp());
//        if (isService!=null){
        if (orders.getUserId().intValue()==userId2.intValue()){
            logger.info("需求方参加 服务方订单");
            Integer change= userId;
            userId=userId2; //服务方id
            userId2=change; //需求方id

        }
        List<Orders> ordersList = ordersDao.findAllMyOrders(userId2) ;
        String exitId = null;
        Integer exit = 0;
        Orders lOrders =null;
        for (int i=0;i<ordersList.size();i++){
             lOrders = ordersList.get(i);
            if (lOrders.getPayType()==PayTypeEnum.STR_WAIT_CONFIRM.toCode()||lOrders.getPayType()==PayTypeEnum.STR_WAIT_PAY.toCode()){
                if (Long.valueOf(lOrders.getStartTimeStamp())>=Long.valueOf(orders.getStartTimeStamp())&&Long.valueOf(lOrders.getStartTimeStamp())<=Long.valueOf(orders.getStartTimeStamp())+orders.getOrderPeriod()*1000*3600){
                    exitId=lOrders.getUuid();
                }
                if (Long.valueOf(lOrders.getStartTimeStamp())+lOrders.getOrderPeriod()*1000*3600>=Long.valueOf(orders.getStartTimeStamp())&&Long.valueOf(lOrders.getStartTimeStamp())+lOrders.getOrderPeriod()*1000*3600<=Long.valueOf(orders.getStartTimeStamp())+orders.getOrderPeriod()*1000*3600){
                    exitId=lOrders.getUuid();
                }
            }else {
                if (Long.valueOf(lOrders.getStartTimeStamp())>=Long.valueOf(orders.getStartTimeStamp())&&Long.valueOf(lOrders.getStartTimeStamp())<=Long.valueOf(orders.getStartTimeStamp())+orders.getOrderPeriod()*1000*3600){
                    exitId=lOrders.getUuid();
                    exit++;
                    break;
                }
                if (Long.valueOf(lOrders.getStartTimeStamp())*1000*3600>=Long.valueOf(orders.getStartTimeStamp())&&Long.valueOf(lOrders.getStartTimeStamp())*1000*3600<=Long.valueOf(orders.getStartTimeStamp())+orders.getOrderPeriod()*1000*3600){
                    exitId=lOrders.getUuid();
                    exit++;
                    break;
                }

            }

        }
        if (exit!=0){
            logger.warn("该时间段存在 进行中的订单 id={}",exitId);
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该时段已存在进行中订单");
        }
        if (exitId!=null){
            logger.info("取消相冲突的 未开始的订单 id={}",exitId);
            userDao.payOrder(exitId,PayTypeEnum.STR_CANCEL.toCode());
        }
        if(lOrders!=null){
            try {
                //订单状态修改为 待进行，进行中的时候 删除精准匹配中的订单
                deleteTaskOrders(lOrders);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (orders==null){
            Integer userIdLength = Integer.valueOf(ordersId.substring(ordersId.length()-1));
            Integer len = String.valueOf(userIdLength).length();
            Integer length = userIdLength+len;
            ordersId = ordersId.substring(0,ordersId.length()-length);
            orders = userDao.findOrdersByVersion(ordersId);
            if (orders==null){
                logger.info("接受应约 该订单不存在");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"发布的订单已撤销");
            }
        }

        // 将 应约方的 用户id  添加到 应约表中
        logger.info("添加到 应约表中。。。");
        //查询 该 邀约 单的 所有 应约响应单
        List<OrdersReceive> ordersReceiveList = ordersDao.findOrdersReceive(ordersId);
        //响应状态
        String type;
        OrdersReceive ordersReceive = ordersDao.findInvitationReceive(ordersId,userId2);
        if (ordersReceive!=null){
            logger.info("已参加");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户已参加");
        }
        if (ordersReceiveList.size()<orders.getPersonCount()){
            ordersDao.addInvitationReceive(ordersId,userId2);
            List<OrdersReceive> ordersReceiveList2 = ordersDao.findOrdersReceive(ordersId);
            if (ordersReceiveList2.size()==orders.getPersonCount()){
                ordersDao.updateInvitationOnTheWait(ordersId,PayTypeEnum.STR_WAIT_START.toCode());

            }else {
                ordersDao.updateInvitationOnTheWait(ordersId,PayTypeEnum.STR_ON_THE_WAY.toCode());
            }

        }else {
            logger.info("人数已达到要求 请选择其他订单");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"人数已达到要求");
        }
        //若最新的订单为共享订单 则比较 发布的应约单 是否在这个时间段内
//        if (orders.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode().intValue()){
//            logger.info("该订单为共享订单 ordersId={}",orders.getUuid());
//            List<Orders> ordersList = ordersDao.findOrdersByUserIdDemand(userId2);
//            for (int i=0;i<ordersList.size();i++){
//                Orders ordersInList = ordersList.get(i);
//                try {
//                    String endTime = simpDate.endDate(ordersInList.getStartTime(),ordersInList .getOrderPeriod(),0);
//                    Long endLong = simpleDateFormat.parse(endTime).getTime();
//                    if (Long.valueOf(orders.getStartTimeStamp())>=Long.valueOf(ordersInList.getStartTimeStamp())
//                            &&Long.valueOf(orders.getStartTimeStamp())<=endLong){
//                        logger.info("共享订单的开始时间在已发布的应约单时间段内 取消该应约单");
//                        userDao.payOrder(ordersInList.getUuid(),PayTypeEnum.STR_CANCEL.toCode());
//                    }
//                    String endTime2 = simpDate.endDate(orders.getStartTime(),orders .getOrderPeriod(),0);
//                    Long endLong2 = simpleDateFormat.parse(endTime2).getTime();
//                    if (endLong2>=Long.valueOf(ordersInList.getStartTimeStamp())&&endLong2<=endLong){
//                        logger.info("共享订单的结束时间在已发布的应约单时间段内 取消该应约单");
//                        userDao.payOrder(ordersInList.getUuid(),PayTypeEnum.STR_CANCEL.toCode());
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//  * @param ordersId 邀请方 订单
//  * @param userId2 应约方id
//  * @param userId 邀请方 id

        Orders oooo1 = ordersDao.findReceiveTimeLimitOne(userId2,SelTimeTypeEnum.FIXED.getCode(),PayTypeEnum.STR_WAIT_PAY.toCode(),InvitationTypeEnum.DEMAND.getCode(), orders.getScenes());
        //根据 用户id查询所有 应约的单子
        List<OrdersReceive> ordersServiceList1 = ordersDao.findOrdersReceiveByUserId(userId2,orders.getScenes());

        Orders lastOrders1 =getLatestOrders(oooo1,ordersServiceList1);
        //发布邀约单的 用户
        User users = userDao.findUserByUserId(orders.getUserId());
        List<User> userListReceive = ordersDao.findReceives(ordersId);

        List<User> userList = new ArrayList<User>();
        userList.add(users);
        //在订单中 邀约方 添加头像列表
        orders.setUsers(userList);
        orders.setSize(userListReceive.size());
        //查询我的应约单
//        List<OrdersResponse> listOrdersResponse = ordersDao.findMyOrder(orders.getScenes(),InvitationTypeEnum.DEMAND.getCode(),userId,SelTimeTypeEnum.FIXED.getCode());
//        Orders lastOrders = ordersDao.findReceiveTimeLimitOne(userId2,SelTimeTypeEnum.FIXED.getCode());

        //邀约方 发现 首页 redis中 订单

        /*  Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()));
        String key = null;
        for (String str : set){
            key=str;
        }
        Orders redisOrders =null;
        Orders startOrders = ordersDao.findStartTimeLimitOne(userId,SelTimeTypeEnum.FIXED.getCode(),orders.getScenes());
      if (key!=null) {
            redisOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId, orders.getScenes(), orders.getPerhaps(), orders.getSelTimeType()), key);
            redisOrders.setType(type);
            redisOrders.setUsers(userListReceive);
            redisOrders.setSize(userListReceive.size());

            if (startOrders!=null&&Long.valueOf(redisOrders.getStartTimeStamp())>Long.valueOf(startOrders.getStartTimeStamp())){
                startOrders.setType(type);
                startOrders.setUsers(userListReceive);
                startOrders.setSize(userListReceive.size());
                taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId, orders.getScenes(), orders.getPerhaps(), orders.getSelTimeType()), key);
                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId, orders.getScenes(), orders.getPerhaps(), orders.getSelTimeType()), startOrders.getStartTimeStamp(),startOrders);
            }else {
                taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId, orders.getScenes(), orders.getPerhaps(), orders.getSelTimeType()), key);
                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId, orders.getScenes(), orders.getPerhaps(), orders.getSelTimeType()), key,redisOrders);
            }
        }else {
            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId, orders.getScenes(), orders.getPerhaps(), orders.getSelTimeType()), startOrders.getStartTimeStamp(),startOrders);
        }
        */

//        if (key==null){
//            logger.info("邀约方 发现 首页中的订单已取消");
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"邀约单已撤销");
//        }
//        if (redisOrders.getUuid().equals(orders.getUuid())) {
//            logger.info("应约的订单是 最新发布的邀约订单");
//            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId, orders.getScenes(), orders.getPerhaps(), orders.getSelTimeType()), key,redisOrders);
//        }
        //应约方 最新的 订单 共享订单
//        Orders lastOrders = ordersDao.findReceiveTimeLimitOne(userId2,SelTimeTypeEnum.FIXED.getCode());

//        Map<String,Orders> map2 = taskUtilCommon.getLatestOrders(userId2,SelTimeTypeEnum.FIXED.getCode());
        Orders oooo = ordersDao.findReceiveTimeLimitOne(userId2,SelTimeTypeEnum.FIXED.getCode(),PayTypeEnum.STR_WAIT_PAY.toCode(),InvitationTypeEnum.DEMAND.getCode(), orders.getScenes());
        //根据 用户id查询所有 应约的单子
        List<OrdersReceive> ordersServiceList = ordersDao.findOrdersReceiveByUserId(userId2,orders.getScenes());

        Orders lastOrders =getLatestOrders(oooo,ordersServiceList);

//        if (lastOrders1!=null){
//            if (Long.valueOf(lastOrders.getStartTimeStamp())<=Long.valueOf(lastOrders1.getStartTimeStamp())&&Long.valueOf(lastOrders1.getStartTimeStamp())<=Long.valueOf(lastOrders.getStartTimeStamp())*lastOrders.getOrderPeriod()*1000*3600) {
//                userDao.payOrder(lastOrders1.getUuid(),PayTypeEnum.STR_CANCEL.toCode());
//            }
//            if (Long.valueOf(lastOrders1.getStartTimeStamp())*1000*3600>=Long.valueOf(lastOrders.getStartTimeStamp())&&Long.valueOf(lastOrders1.getStartTimeStamp())*1000*3600<=Long.valueOf(lastOrders.getStartTimeStamp())*1000*3600){
//                userDao.payOrder(lastOrders1.getUuid(),PayTypeEnum.STR_CANCEL.toCode());
//            }
//        }
        /*
       if (lastOrders!=null){
           lastOrders.setUsers(userList);
           lastOrders.setType(type);
           lastOrders.setUserId(userId2);
           lastOrders.setSize(1);

       } else {
            logger.warn("最新的订单 和 共享订单 都为空 则放入 共享订单 ");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"应约失败 请稍后再试");
        }
        Set<String> sets = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId2,orders.getScenes(),InvitationTypeEnum.DEMAND.getCode(),orders.getSelTimeType()));
        String keys =null;
        for (String str : sets){
            keys = str;
        }
        Orders receiveOrders = null;
        if (keys!=null) {
            receiveOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId2, orders.getScenes(), InvitationTypeEnum.DEMAND.getCode(), orders.getSelTimeType()), keys);
        }
        if (receiveOrders==null){
            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId2, orders.getScenes(), InvitationTypeEnum.DEMAND.getCode(), orders.getSelTimeType()), lastOrders.getStartTimeStamp(),lastOrders);
        } else // 应约成功后 的 最新订单 开始时间 小于 应约成功前的 最新订单 开始时间
            if (Long.valueOf(lastOrders.getStartTimeStamp())<=Long.valueOf(receiveOrders.getStartTimeStamp())) {
                if(isService!=null){
                    logger.info("取消订单ordersId={}",receiveOrders.getUuid());
                    userDao.payOrder(receiveOrders.getUuid(),PayTypeEnum.STR_CANCEL.toCode());
                }
                taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId2, receiveOrders.getScenes(), InvitationTypeEnum.DEMAND.getCode(), receiveOrders.getSelTimeType()), receiveOrders.getStartTimeStamp());
                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId2, orders.getScenes(), InvitationTypeEnum.DEMAND.getCode(), orders.getSelTimeType()), lastOrders.getStartTimeStamp(),lastOrders);
        }
*/

//        Orders ordersRedis = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),String.valueOf(timeStemp));


       /*
        //获取场景
        Scene scene = userDao.findSceneByValue(orders.getScenes());
        //根据订单id获取所有响应的人
        List<User> list = ordersDao.findReceives(ordersId);
        Integer personCount = orders.getPersonCount();
        Integer proportion = scene.getProportion();

        if (ordersRedis!=null){
            ordersRedis.setType(PayTypeEnum.STR_ON_THE_WAY.getValue());
            ordersRedis.setUsers(list);
            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),String.valueOf(timeStemp),ordersRedis);

        }
        if (list.size()>=personCount*proportion/100&&list.size()<personCount){
            map.put("tip", OrdersEnum.BASIC_CODE.toCode());
        }else if (list.size()<personCount*proportion/100){
            map.put("tip",OrdersEnum.NONE_CODE.toCode());
        }else {
            map.put("tip",OrdersEnum.COMPLETE_CODE.toCode());
        }
        //应约完成 回传头像
        orders.setUsers(list);
//        map.put("users",list);
        map.put("orders",orders);

*/
//        if (!ServerHandler.sendNotify(RpcAllowMsgEnum.NOTIFY,orders,ServerHandlerTypeEnum.ACCEPT.getType())){
//            User user = userDao.findUserByUserId(orders.getUserId());
//            Push push = new Push();
//            push.setDeviceNum(user.getCid());
//            push.setSendDeviceType(user.getType());
//            push.setSendTopic("订单消息");
//            push.setSendContent(String.valueOf(user.getId()));
//            PushUtils.userPush.getInstance().sendPush(push);
//        }
        //删除 服务方 订单列表
//        String uValue = ordersId+":"+InvitationTypeEnum.DEMAND.getCode();
//        undoneRedisTemplate.remove(String.format(USER_UNDONE_PERHAPS_SCENES,userId2,InvitationTypeEnum.DEMAND.getCode(),orders.getScenes(),orders.getUuid()),uValue);
//        undoneRedisTemplate.add(String.format(USER_UNDONE_PERHAPS_SCENES,id,InvitationTypeEnum.SERVICE.getCode(),orders.getScenes()),orders,score2);

        return  Response.successByArray();
    }

    /**
     *
     * @param ordersId 需求方订单
     * @param id 需求方 id
     * @param userId 服务方id
     * @return
     */
    @Override
    public Response invitationReceiveRefuse(String ordersId, Integer id,Integer userId) {
        if (StringUtils.isEmpty(ordersId)||userId==null||id==null){
            logger.info("拒绝应约 订单参数为空");
        }
//        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
//        if (orders==null){
//            logger.info("该订单不存在");
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该订单不存在");
//        }
//        orderHashRedisTemplate.put(ordersId, String.valueOf(userId),ordersId);
        if (id==null||StringUtils.isEmpty(ordersId)){
            logger.warn("拒绝参加 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
        orders.setUid(id);
        User user2= userDao.findUserByUserId(id);

        String uValue = orders.getUuid()+":"+InvitationTypeEnum.DEMAND.getCode();
        if (orders.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode()&&orders.getUserId().intValue()!=id.intValue()){
            //删除 订单列表订单
//            String uValue = orders.getUuid()+":"+InvitationTypeEnum.DEMAND.getCode();
//            undoneRedisTemplate.remove(String.format(USER_UNDONE_PERHAPS_SCENES,orders.getUserId(),orders.getPerhaps(),orders.getScenes()),uValue);
            //取消订单
            ordersDao.ordersStart(ordersId,PayTypeEnum.STR_CANCEL.toCode());
            //返还金额
            Scene scene = userDao.findSceneByValue(orders.getScenes());
            Double mu = Arith.multiplys(2,scene.getHourlyFee(),orders.getOrderPeriod(),orders.getPersonCount());
            Double money = Arith.add(2,mu,orders.getGratefulFree());
            userDao.updateMoney(money,orders.getUserId());
            String uuid1 = UserUtil.generateUuid();
            logger.info(" 订单号={},金额={}",orders.getUuid(),money);
            //添加到明细
            userDao.addRechargeSheet(orders.getUserId(),orders.getUuid(),money,uuid1, DetailTypeEnum.INVITATION.toCode(), DetailTypeEnum.INVITATION.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
            Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),SelTimeTypeEnum.FIXED.getCode()));
            logger.info("userId={},scene={},perhaps={}",orders.getUserId(),orders.getScenes(),orders.getPerhaps());
            String key = null;
            for (String str:set){
                key=str;
            }
            if (key!=null){
                taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),SelTimeTypeEnum.FIXED.getCode()),key);
            }
//            Orders ods = ordersDao.findStartTimeLimitOne(orders.getUserId(),SelTimeTypeEnum.FIXED.getCode(),orders.getScenes());
//            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,ods.getUserId(),ods.getScenes(),ods.getPerhaps(),SelTimeTypeEnum.FIXED.getCode()),ods.getStartTimeStamp(),ods);
//            undoneRedisTemplate.remove(String.format(USER_UNDONE_PERHAPS_SCENES,userId,InvitationTypeEnum.SERVICE.getCode(),orders.getScenes()),uValue);
            //添加删除信息到服务方消息列表
            String name;
            if (StringUtils.isEmpty(user2.getNickname())){
                name=user2.getUuid();
            }else {
                name=user2.getNickname();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String  now = simpleDateFormat.format(new Date());
            String nameValue = name+":"+ordersId+","+now;
//        Long size = msgValueTemplate.size(String.valueOf(id));
            Long size = msgValueTemplate.size(String.format(USER_MSG,orders.getUserId()));
            Long score = size+1;
            msgValueTemplate.add(String.format(USER_MSG,orders.getUserId()),String.format(USER_TYPE_ORDERS,id,MsgTypeEnum.REFUSE_JOIN_MSG.getCode(),nameValue),score);
        }else {
            Integer changeId = id;
            id=userId;
            userId=changeId;
//            undoneRedisTemplate.remove(String.format(USER_UNDONE_PERHAPS_SCENES,id,InvitationTypeEnum.DEMAND.getCode(),orders.getScenes()),uValue);
            //服务方
            User user= userDao.findUserByUserId(userId);
            //添加删除信息到服务方消息列表
            String name;
            if (StringUtils.isEmpty(user2.getNickname())){
                name=user2.getUuid();
            }else {
                name=user2.getNickname();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String  now = simpleDateFormat.format(new Date());
            String nameValue = name+":"+ordersId+","+now;
//            String nameValue = name+":"+ordersId;
//        Long size = msgValueTemplate.size(String.valueOf(id));
            Long size = msgValueTemplate.size(String.format(USER_MSG,id));
            Long score = size+1;
            msgValueTemplate.add(String.format(USER_MSG,id),String.format(USER_TYPE_ORDERS,userId,MsgTypeEnum.REFUSE_JOIN_MSG.getCode(),nameValue),score);

        }




        return Response.successByArray();
//        return Response.successByArray();
    }
    @Override
    public Response addAppraise(Integer id,String appraiseRequest) {
        if (id==null){
            logger.warn("评价参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"评价 参数为空");
        }
        logger.info(appraiseRequest);
        JSONArray jsonArray = JSONArray.parseArray(appraiseRequest);
        String uuid = UserUtil.generateUuid();
        for (int i=0;i<jsonArray.size();i++){
//            AppraiseRequest appraise =  jsonArray.get(i).to;
            AppraiseRequest appraise = JSONObject.parseObject(jsonArray.get(i).toString(),AppraiseRequest.class);
            String content=null;
            if (appraise.getContent()==null|| "".equals(appraise.getContent())){
                content="用户未评论";
            }else {
                content=appraise.getContent();
            }
//            Orders orders1 = ordersDao.findOrdersOneByUuid(appraise.getOrdersId());
            ordersDao.addAppraise(appraise.getUserId(),id,uuid,appraise.getOrdersId(),appraise.getStar(),content);
        }
//        logger.info("获取被评价用户 所有评价");
//        List<Appraise> appraiseList = ordersDao.findAllAppraiseByUid(orders1.getUserId());
//        Double grade = UserUtil.appraiseGrade(appraiseList);
//        Appraise appraise = new Appraise();
//        appraise.setUserId(orders1.getUserId());
//        appraise.setStar(grade);

//        undoneRedisTemplate.remove(String.format(USER_UNDONE_PERHAPS_SCENES,userId2,InvitationTypeEnum.SERVICE.getCode(),orders.getScenes(),orders));

        return Response.successByArray();
//        return Response.success(appraise);
    }

    @Override
    public Response findAllOrdersUser(Integer userId, String ordersId) {
        if (userId==null||StringUtils.isEmpty(ordersId)){
            logger.warn("查找 所有 该订单用户 参数为空");
            return  Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
        List<User> userList=new ArrayList<User>();
        if (orders.getUserId().intValue()==userId.intValue()){
            userList = ordersDao.findReceives(ordersId);
        }else {
            User user=userDao.findUserByUserId(userId);
            userList.add(user);
        }
        return Response.success(userList);
    }

    @Override
    public Response version(Integer device) {
        JSONObject jsonObject = new JSONObject();
        if (device == 0) {
            String versionAndUrl = ordersDao.getSysParameter(SysKeyEnum.ANDROID_VERSION.getKey());
            String version = versionAndUrl.split(",")[0];
            String url = versionAndUrl.split(",")[1];
            jsonObject.put("version", version);
            jsonObject.put("url", url);
            return Response.success(jsonObject);
        }else {
            String version = ordersDao.getSysParameter(SysKeyEnum.IOS_VERSION.getKey());
            jsonObject.put("version",version);
            return Response.success(jsonObject);
        }
    }

    @Override
    public Response activePic() {
        String activePicAndUrl = ordersDao.getSysParameter (SysKeyEnum.ACTIVE_PIC.getKey());
        String activePic = activePicAndUrl.split(",")[0];
        String url = activePicAndUrl.split(",")[1];
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pic",activePic);
        jsonObject.put("url",url);
        return Response.success(jsonObject);
    }

    @Override
    public Response findAllUndoneOrders(Integer id, Integer pageNum,String scenes) {
        if (id==null||pageNum==null||scenes==null){
            logger.warn("查找所有 未完成订单 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
//        String value = orderHashRedisTemplate.get(String.valueOf(id),String.valueOf(scenes));
        List<UndoneOrders> undoneOrdersList = ordersDao.findAllUndoneOrders(id,pageNum,scenes);
//        if (undoneOrdersList==null||undoneOrdersList.size()==0){
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"未完成订单列表为空");
//        }
        return Response.success(undoneOrdersList);
    }

    @Override
    public Response te() {
        Orders orders = new Orders();
        orders.setPerhaps(1);
        orders.setUserId(1111);
        orders.setUuid("1");
        orders.setType("真的");
//        undoneRedisTemplate.add("3333",orders,111);
//        undoneRedisTemplate.add("3333",orders,111);
        Orders  orders1 = new Orders();
        orders1.setUuid("1");
//        undoneRedisTemplate.remove("3333",orders1);
//        undoneRedisTemplate.remove("3333",orders);
        String a="a";
        String b="a";
        msgValueTemplate.add("3333",a,111);
        msgValueTemplate.remove("3333",b);
//        msgValueTemplate.add("msg:100000","1:100:100",1);
//        msgValueTemplate.add("msg:100000","2:100:100",2);
//        msgValueTemplate.add("msg:100000","3:100:100",3);
//        msgValueTemplate.add("msg:100000","4:100:100",4);
//        msgValueTemplate.add("msg:100000","5:100:100",5);
//        Long size = msgValueTemplate.size("msg:100000");
//        Set set= msgValueTemplate.range("msg:100000",0,4);
//        msgValueTemplate.remove("msg:100000",orders.toString());
//        System.out.println(set);
//        System.out.println(size);
        return null;
    }

    /**
     *
     * @param id 需求方id
     * @param ordersId 服务方订单号
     * @return
     */
    @Override
    public Response joinOrders(Integer id, String ordersId) throws ParseException {
        if (id==null||StringUtils.isEmpty(ordersId)){
            logger.warn("参加 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //需求方
        User user = userDao.findUserByUserId(id);
//        if (StringUtils.isEmpty(user.getRealName())||StringUtils.isEmpty(user.getIdCard())|| "未绑定".equals(user.getIdCard())){
//            logger.warn("用户未认证");
//            return Response.response(ResponseTypeEnum.ATTESTATION_FAIL_CODE.toCode(),"请先进行芝麻认证");
//        }
        logger.debug("参加订单 id={}",id);


        //        服务方订单
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);

        logger.info("参加 ordersPerhaps={}",orders.getPerhaps());
        if(orders.getPerhaps().intValue()!=InvitationTypeEnum.DEMAND.getCode()){
            logger.info("此订单为应约={}",orders.getUuid());
            return  invitationReceive(ordersId,id);
        }
        /****--对重复邀请进行限制 start--****/
        String compareId = orderHashRedisTemplate.get(String.format(ORDERS_ORDERSID,ordersId),String.valueOf(id));
        if (compareId!=null){
            logger.warn("用户userId={}已发出参加信息",id);
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已发出参加或邀请信息，请勿频繁操作");
        }
        orderHashRedisTemplate.put(String.format(ORDERS_ORDERSID,ordersId),String.valueOf(id),ordersId);
        /****--对重复邀请进行限制 end--****/


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpDate simpDate = SimpDateFactory.endDate();

//        服务方用户
        User user2 = userDao.findUserByUserId(orders.getUserId());
        Date domainEndDate =null;
        try {
            String domainEndTime = simpDate.endDate(orders.getStartTime(),orders.getOrderPeriod(),0);
            domainEndDate=simpleDateFormat.parse(domainEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Orders> ordersList = ordersDao.findOneByUserId(id);
        Integer exit=0;
        String alreadyExit=null;
        for (int i=0;i<ordersList.size();i++){
            Orders ordersL= ordersList.get(i);
             Date serverEndDate=null ;
            try {
                String serverEndTime = simpDate.endDate(ordersL.getStartTime(),ordersL.getOrderPeriod(),0);
                serverEndDate=simpleDateFormat.parse(serverEndTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (Long.valueOf(ordersL.getStartTimeStamp())<=Long.valueOf(orders.getStartTimeStamp())&&Long.valueOf(orders.getStartTimeStamp())<=serverEndDate.getTime()){
                exit++;
                alreadyExit=ordersL.getUuid();
                break;
            }
            if (Long.valueOf(ordersL.getStartTimeStamp())<=domainEndDate.getTime()&&Long.valueOf(orders.getStartTimeStamp())<=serverEndDate.getTime()){
                exit++;
                alreadyExit=ordersL.getUuid();
                break;
            }

        }
        if (exit!=0){
            logger.info("该服务订单时间段内已存在订单 uuid={}",alreadyExit);
            Orders deletedOrders = ordersDao.findOrdersOneByUuid(alreadyExit);
            ordersDao.invitationDeleted(alreadyExit,PayTypeEnum.STR_CANCEL.toCode());
            userDao.updateMoney(Math.abs(deletedOrders.getMoney()),deletedOrders.getUserId());
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该服务订单时间段内已存在订单 ={}");
        }

        /************************************* 订单列表--将邀请信息 存入redis中 start****************************************/
        /**Long size2 = undoneRedisTemplate.size(String.format(USER_UNDONE_PERHAPS_SCENES,id,InvitationTypeEnum.SERVICE.getCode(),orders.getScenes()));
        Long score2 = size2+1;
        orders.setType(PayTypeEnum.SEND_INVITATION.getValue());
        String uValue = ordersId+":"+InvitationTypeEnum.SERVICE.getCode();

        undoneRedisTemplate.add(String.format(USER_UNDONE_PERHAPS_SCENES,id,InvitationTypeEnum.SERVICE.getCode(),orders.getScenes()),uValue,score2);
         */
        /************************************* 订单列表--将邀请信息 存入redis中 start****************************************/
        String uuid = generateOrderNo(OrdersTypeEnum.INVITE_ORDER,id);
        Scene scene = userDao.findSceneByValue(orders.getScenes());
        Double countMoney =Arith.multiplys(2,scene.getHourlyFee(),orders.getOrderPeriod(),orders.getPersonCount());
        countMoney = Arith.add(2,countMoney,orders.getGratefulFree());
        ordersDao.addInvitation(countMoney,uuid,id,orders.getStartTime(),orders.getPlace(),SelTimeTypeEnum.FIXED.getCode(),orders.getOrderPeriod(),
                orders.getPersonCount(),orders.getGratefulFree(),orders.getLabel(),InvitationTypeEnum.SERVICE.getCode(),orders.getScenes(),orders.getLongitude(),orders.getLatitude(),
                PayTypeEnum.STR_WAIT_PAY.toCode(),uuid,Long.valueOf(orders.getStartTimeStamp()),orders.getAddress());
        Date date = new Date();
        String now  = simpleDateFormat.format(date);
        String now2=null;
        String twoHourAfter=null;
        try {
            now2 = simpDate.endDate(now,0.0,5);
            twoHourAfter = simpDate.endDate(now,2.0,0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Map<String,String> cancelTimeMap = new HashMap<String,String>();
        Map<String,String> cancelTimeMap2 = new HashMap<String,String>();
        try {
            cancelTimeMap = simpDate.transformTime(now2);
            cancelTimeMap2 = simpDate.transformTime(twoHourAfter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*************************************************************/
        /*****************订单时效 5分钟后取消订单**************************/
        /**************************************/
        TransferData data = new TransferData();
        TimeTask task = new TimeTask();
        task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        task.setTimeTaskName("orderTime:"+uuid);
        //当前时间5分钟后
        task.setExecuteTime("0 "+cancelTimeMap.get("mm")+" "+cancelTimeMap.get("HH")+" "+cancelTimeMap.get("dd")+" "+cancelTimeMap.get("MM")+" ?");
        Orders o = new Orders();
        o.setUuid("orders:"+uuid);
        task.setParams(JSON.toJSONString(o));
        data.setData(JSONObject.toJSONString(task));
        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data);
        /*************************************************************/
        /****************************订单时效**************************/
        /*************************************************************/

        JSONObject jsonObject = new JSONObject ();
        Integer length = orders.getUserId().toString().length();
        String uuids = uuid + orders.getUserId()+length;
        jsonObject.put("ordersId",uuids);
//        Double countMoney = Math.abs(orders.getMoney());

        jsonObject.put("money",String.valueOf(countMoney));


//        }
//        JoinOrdersResponse joinOrdersResponse = new JoinOrdersResponse();
//        joinOrdersResponse.setUuid(uuid);
//        joinOrdersResponse.setPlace(orders.getPlace());
//        joinOrdersResponse.setScenes(orders.getScenes());
//        joinOrdersResponse.setStartTime(orders.getStartTime());
//        joinOrdersResponse.setOrderPeriod(orders.getOrderPeriod());
//        joinOrdersResponse.setPersonCount(orders.getPersonCount());
//        joinOrdersResponse.setMoney(orders.getMoney());
        return Response.success(jsonObject);
    }

    @Override
    public Response undoneOrdersDetails(Integer id, Integer pageNum,Integer scenes,Integer perhaps) {
        if (id==null||pageNum==null||scenes==null||perhaps==null){
            logger.warn("获取 时效订单 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String value = orderHashRedisTemplate.get(String.valueOf(id),String.valueOf(scenes));

        if(1==1){
            List<Orders> ordersList = ordersDao.findUndoneOrdersLimitPage(id,pageNum,value,10,perhaps);
            List<Orders> list = new ArrayList<>();
            for (int i=0;i<ordersList.size();i++){
                Orders ordersTemp = ordersList.get(i);
                if (!ordersTemp.getType().equals(PayTypeEnum.SEND_INVITATION.getValue())&&!ordersTemp.getType().equals(PayTypeEnum.SEND_JOIN.getValue())){
                    Integer code = ordersTemp.getPayType();
                    String type = UserUtil.valueById(code);
                    ordersTemp.setType(type);
                    List<User> users = ordersDao.findReceives(ordersTemp.getUuid());
                    ordersTemp.setUsers(users);
                }
                if (ordersTemp.getUserId().intValue()!=id.intValue()){
                    ordersTemp.setPerhaps(InvitationTypeEnum.DEMAND.getCode());
                }
                list.add(ordersTemp);
            }
            return Response.success(list);
        }
        Integer size = undoneRedisTemplate.size(String.format(USER_UNDONE_PERHAPS_SCENES,id,perhaps,value)).intValue();
        List<Orders> ordersList = new ArrayList<Orders>();
        Set<String> set =null;
        //当前页 显示有多少条数
        Integer length;
        Integer start = pageNum>0?pageNum*10:0;
//        Integer start = (pageNum-1)*10-1;
        Integer end=pageNum>0?(pageNum+1)*10-1:9;
//        Integer end=pageNum*10-1;
        Integer page =size/10+1;
        if (size==0){ //第一页 并且redis中 数据为空
            pageNum=pageNum-1;
            ordersList = ordersDao.findUndoneOrdersLimitPage(id,pageNum,value,10,perhaps);
        }else if (pageNum==1&&size<10){ //第一页 并且 redis中 数据少于一页
            length =10-size;
            set =undoneRedisTemplate.range(String.format(USER_UNDONE_PERHAPS_SCENES,id,perhaps,value),0,size-1);
            pageNum=pageNum-1;
            List<Orders> list = ordersDao.findUndoneOrdersLimitPage(id,pageNum,value,length,perhaps);
            if (set!=null){
                for (String orders:set){
                    String uValue = orders.split(":")[1];
                    Orders ods = ordersDao.findOrdersOneByUuid(orders.split(":")[0]);

                    if (Integer.valueOf(uValue).intValue()==InvitationTypeEnum.SERVICE.getCode()){
                        ods.setType(PayTypeEnum.SEND_INVITATION.getValue());
                    }else {
                        ods.setType(PayTypeEnum.SEND_JOIN.getValue());
                    }

                    ordersList.add(ods);
                }
            }
            ordersList.addAll(list);
        }else {
            if (page>pageNum){
                set =undoneRedisTemplate.range(String.format(USER_UNDONE_PERHAPS_SCENES,id,perhaps,value),start,end);
                if (set!=null){
                    for (String orders:set){
                        String uValue = orders.split(":")[1];
                        Orders ods = ordersDao.findOrdersOneByUuid(orders.split(":")[0]);

                        if (Integer.valueOf(uValue).intValue()==InvitationTypeEnum.SERVICE.getCode()){
                            ods.setType(PayTypeEnum.SEND_INVITATION.getValue());
                        }else {
                            ods.setType(PayTypeEnum.SEND_JOIN.getValue());
                        }
                        ordersList.add(ods);
                    }
                }
            }else if (Objects.equals(page, pageNum)){
                length =10-( size - 10*pageNum);
                set =undoneRedisTemplate.range(String.format(USER_UNDONE_PERHAPS_SCENES,id,perhaps,value),start,size-1);
                Integer startLen=0;
                List<Orders> list = ordersDao.findUndoneOrdersLimitPage(id,startLen,value,length,perhaps);
                if (set!=null){
                    for (String orders:set){
                        String uValue = orders.split(":")[1];
                        Orders ods = ordersDao.findOrdersOneByUuid(orders.split(":")[0]);

                        if (Integer.valueOf(uValue).intValue()==InvitationTypeEnum.SERVICE.getCode()){
                            ods.setType(PayTypeEnum.SEND_INVITATION.getValue());
                        }else {
                            ods.setType(PayTypeEnum.SEND_JOIN.getValue());

                        }
                        ordersList.add(ods);
                    }
                }
                ordersList.addAll(list);
            }else {
                pageNum=pageNum-1;

                Integer startLen=pageNum;
                ordersList = ordersDao.findUndoneOrdersLimitPage(id,startLen,value,10,perhaps);
            }
        }

        ComparatorOrders comparatorOrders = new ComparatorOrders();
        Collections.sort(ordersList,comparatorOrders);
        List<Orders> list = new ArrayList<>();
        for (int i=0;i<ordersList.size();i++){
            Orders ordersTemp = ordersList.get(i);
            if (!ordersTemp.getType().equals(PayTypeEnum.SEND_INVITATION.getValue())&&!ordersTemp.getType().equals(PayTypeEnum.SEND_JOIN.getValue())){
                Integer code = ordersTemp.getPayType();
                String type = UserUtil.valueById(code);
                ordersTemp.setType(type);
                List<User> users = ordersDao.findReceives(ordersTemp.getUuid());
                ordersTemp.setUsers(users);
            }
            if (ordersTemp.getUserId().intValue()!=id.intValue()){
                ordersTemp.setPerhaps(InvitationTypeEnum.DEMAND.getCode());
            }
            list.add(ordersTemp);
        }
        return Response.success(list);
    }



    @Override
    public Response msgDetail(Integer id, Integer pageNum) {
        if (id==null||pageNum==null){
            logger.warn("消息列表参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Long size = msgValueTemplate.size(String.format(USER_MSG,id));
        if (size==null||size==0){
            return Response.successByArray();
        }
        Integer start = (pageNum-1)*10;
        Integer end = pageNum*10-1;
        Set<String> set = msgValueTemplate.range(String.format(USER_MSG,id),start,end);
        List<Map<String,String>> list = new ArrayList<>();
        Object[] strs=set.toArray();
        for (int i=strs.length-1;i>=0;i--){
            String str =(String) strs[i];
            Map<String,String> map = new HashMap<String,String>();
            String[] strings = str.split(":");
            String userId =strings[0];
            map.put("userId",userId);
            map.put("type",strings[1]);
            map.put("nickname",strings[2]);
            //如果格式大于三个
           if (strings.length!=3){
               String[] ordersIds = strings[3].split(",");
               String ordersId;
               if (ordersIds.length==2){
                   ordersId=ordersIds[0];
               }else {
                   ordersId=strings[3];
               }
               map.put("ordersId",ordersId);
               Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
               OrdersReceive ordersReceive = ordersDao.findInvitationReceive(ordersId,Integer.parseInt(userId));
               Integer per=1;
               if(orders!=null){
                   if (Objects.equals(orders.getPerhaps(), InvitationTypeEnum.SERVICE.getCode())){
                       per=InvitationTypeEnum.DEMAND.getCode();
                   }else {
                       per=InvitationTypeEnum.SERVICE.getCode();
                   }
               }

               if (strings.length==5&&ordersIds.length<2){//为邀约单的时候 显示 服务方订单
                   per = orders.getPerhaps();
                   map.put("ordersId",ordersId);
                   map.put("serviceOrdersId",strings[4]);
               }
                if (ordersReceive!=null){
                   map.put("join","1");
                }else {
                    map.put("join","0");
                }
               String[] joinStrings = str.split(",");
               if (joinStrings.length>1){//若为参加
                   if (joinStrings.length==2){
                       map.put("startTime",joinStrings[1]);
                   }else {
                       map.put("startTime",joinStrings[1]);
                       map.put("place",joinStrings[2]);
                   }

               }
           }
            list.add(map);
        }

        Long msgSize = msgValueTemplate.size(String.format(MSG_USER_SIZE,id));
        Set<String> sets = msgValueTemplate.range(String.format(MSG_USER_SIZE,id),msgSize-1,msgSize);
        String values =null;
        for (String str:sets){
            values=str;
        }
        if (values!=null){
            if (Objects.equals(Long.valueOf(values), size)){
                return Response.success(list);
            }
            msgValueTemplate.remove(String.format(MSG_USER_SIZE,id),values);
        }
        msgValueTemplate.add(String.format(MSG_USER_SIZE,id), String.valueOf(size),1);
        return Response.success(list);
    }

    /**
     *
     * @param id 需求方
     * @param userId 服务方 即被 踢出用户
     * @param ordersId 订单号
     * @return
     */
    @Override
    public Response expelUser(Integer id, Integer userId, String ordersId) {
        if (id==null||userId==null||StringUtils.isEmpty(ordersId)){
            logger.warn("踢出用户 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
        Long time = Long.valueOf(orders.getStartTimeStamp())-System.currentTimeMillis()/1000/60;
        if (time<30){
            logger.warn("订单开始前30分钟 不可踢出用户");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单开始前30分钟 不可踢出用户");
        }
//        OrdersReceive ordersReceive = ordersDao.findInvitationReceive(ordersId,userId);
        //删除 需求方订单中 的服务方
        ordersDao.deletedReceiver(ordersId,userId);
        List<OrdersReceive> ordersReceiveList = ordersDao.findOrdersReceive(ordersId);
        if (ordersReceiveList.size()>=1){
            ordersDao.updatePayType(ordersId,PayTypeEnum.STR_ON_THE_WAY.toCode());
        }else {
            ordersDao.updatePayType(ordersId,PayTypeEnum.STR_WAIT_PAY.toCode());
        }
        //删除 服务方 redis 中数据
        Set<String> set =taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,userId,orders.getScenes(),InvitationTypeEnum.DEMAND.getCode(),SelTimeTypeEnum.FIXED.getCode()));
        String key = null;
        for (String str : set) {
            key=str;
        }
        if (key!=null){
            taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, userId, orders.getScenes(), InvitationTypeEnum.DEMAND.getCode(), SelTimeTypeEnum.FIXED.getCode()), key);
        }
        //服务方
        User user2= userDao.findUserByUserId(id);
        //添加删除信息到服务方消息列表
        String name;
        if (StringUtils.isEmpty(user2.getNickname())){
            name=user2.getUuid();
        }else {
            name=user2.getNickname();
        }
        String nameValue = name+":"+ordersId;
//        Long size = msgValueTemplate.size(String.valueOf(id));
        Long size = msgValueTemplate.size(String.format(USER_MSG,userId));
        Long score = size+1;
        msgValueTemplate.add(String.format(USER_MSG,userId),String.format(USER_TYPE_ORDERS,id,MsgTypeEnum.EXPEL_USER_MSG.getCode(),nameValue) ,score);

        return Response.successByArray();
    }

    /**
     *
     * @param id 服务方id
     * @param ordersId 需求方订单
     * @param longitude 经度
     * @param latitude 纬度
     * @return
     */
    @Override
    public Response arrivedPlace(Integer id, String ordersId, Double longitude, Double latitude) {
        if (id==null||StringUtils.isEmpty(ordersId)||longitude==null||latitude==null){
            logger.warn("服务方确认到达  参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
        //添加 到达地点信息
        ordersDao.arrivedPlace(id,ordersId,longitude,latitude);
//        将到达信息 发送到 需求方 消息列表

        //服务方
        User user2= userDao.findUserByUserId(id);
        //添加删除信息到服务方消息列表
        String name;
        if (StringUtils.isEmpty(user2.getNickname())){
            name=user2.getUuid();
        }else {
            name=user2.getNickname();
        }
        String nameValue = name+":"+ordersId;
//        Long size = msgValueTemplate.size(String.valueOf(id));
        Long size = msgValueTemplate.size(String.format(USER_MSG,orders.getUserId()));
        Long score = size+1;
        msgValueTemplate.add(String.format(USER_MSG,orders.getUserId()),String.format(USER_TYPE_ORDERS,id,MsgTypeEnum.EXPEL_USER_MSG.getCode(),nameValue),score);
        return Response.successByArray();
    }

    @Override
    public Response commonTalk(Integer id,String ordersId) throws ParseException {
        if (id==null||StringUtils.isEmpty(ordersId)){
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
        if (orders==null){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单不存在");
        }
        SimpDate simpDate = SimpDateFactory.endDate();
        Double hourly = orders.getOrderPeriod()-1;
        String thirtyMin =  simpDate.endDate(orders.getStartTime(),hourly,30);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = simpleDateFormat.parse(thirtyMin);
        Long currentTime = System.currentTimeMillis();
        List<CommonTalkResponse> list = null;
        if (currentTime>=date.getTime()){
            list = ordersDao.commonTalk(CommonTalkEnum.THIRTY_MINUTES_TALK.getCode());

        }else {
            list = ordersDao.commonTalk(CommonTalkEnum.COMMON_TALK.getCode());
        }
        return Response.success(list);
    }

    /**
     * 需求方 确认到达
     * @param id 需求方id
     * @param ordersId 需求方订单
     * @param userId 服务方用户di
     * @return
     */
    @Override
    public Response invitationArrived(Integer id, String ordersId, Integer userId) {
        if (id==null||userId==null||StringUtils.isEmpty(ordersId)){
            logger.warn("需求方确认订单 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        logger.info("需求方确认订单");
        OrdersReceive ordersReceive = ordersDao.findInvitationReceive(ordersId,userId);
        if (ordersReceive!=null){
            ordersDao.serverArrived(ordersId,userId,Boolean.TRUE);
        }
        return Response.successByArray();
    }

    @Override
    public Response newMsg(Integer id) {
        Long size =msgValueTemplate.size(String.format(USER_MSG,id));
        Long newMsgSize = msgValueTemplate.size(String.format(MSG_USER_SIZE,id));
        Set<String> set = msgValueTemplate.range(String.format(MSG_USER_SIZE,id),newMsgSize-1,newMsgSize);
        Long setSize=null;
        for (String str: set ){
            setSize=Long.valueOf(str);
        }
        if (setSize!=null) {
            if (size > setSize) {
                return Response.success(Boolean.TRUE);
            }
        }
        return Response.success(Boolean.FALSE);
    }

    /**
     *
     * @param id 服务方id
     * @param ordersId 需求方订单
     * @return
     */
    @Override
    public Response refuseJoin(Integer id, String ordersId) {
        if (id==null||StringUtils.isEmpty(ordersId)){
            logger.warn("拒绝参加 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Orders orders = ordersDao.findOrdersOneByUuid(ordersId);
        orders.setUid(id);
        if (orders.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode()){
            //删除 订单列表订单
//            String uValue = orders.getUuid()+":"+InvitationTypeEnum.DEMAND.getCode();
//            undoneRedisTemplate.remove(String.format(USER_UNDONE_PERHAPS_SCENES,orders.getUserId(),orders.getPerhaps(),orders.getScenes()),uValue);
            //取消订单
            ordersDao.ordersStart(ordersId,PayTypeEnum.STR_CANCEL.toCode());
            //返还金额
            userDao.updateMoney(orders.getMoney(),orders.getUserId());
            logger.info("拒绝 参加 退还金额 userId={}",orders.getUserId());
            Set<String> set = orderHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),SelTimeTypeEnum.FIXED));
            String key = null;
            for (String str:set){
                key=str;
            }
            if (key!=null){
                orderHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),SelTimeTypeEnum.FIXED),key);
            }
        }

        //服务方
        User user2= userDao.findUserByUserId(id);
        //添加删除信息到服务方消息列表
        String name;
        if (StringUtils.isEmpty(user2.getNickname())){
            name=user2.getUuid();
        }else {
            name=user2.getNickname();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String  now = simpleDateFormat.format(new Date());
        String nameValue = name+":"+ordersId+","+now;
//        Long size = msgValueTemplate.size(String.valueOf(id));
        Long size = msgValueTemplate.size(String.format(USER_MSG,orders.getUserId()));
        Long score = size+1;
        msgValueTemplate.add(String.format(USER_MSG,orders.getUserId()),String.format(USER_TYPE_ORDERS,id,MsgTypeEnum.REFUSE_JOIN_MSG.getCode(),nameValue),score);
//        if (!ServerHandler.sendRefuseNotify(RpcAllowMsgEnum.NOTIFY,orders,ServerHandlerTypeEnum.JOIN.getType())){
            User user = userDao.findUserByUserId(orders.getUserId());
            Push push = new Push();
            push.setDeviceNum(user.getCid());
            push.setSendDeviceType(user.getType());
            push.setSendTopic("订单消息");
            String content = UserUtil.jsonPare("userId,"+user.getId(),"icon,"+user.getIcon(),"ordersId,"+orders.getUuid(),"type,"+ ServerHandlerTypeEnum.ACCEPT.getType());
            push.setSendContent(content);
            PushUtils.userPush.getInstance().sendPush(push);
//        }
        return Response.successByArray();
    }

    @Override
    public Response activeList(Integer pageNum) {
         List<ActiveResponse> list = ordersDao.activeList( pageNum);
        return Response.success(list);
    }


    @Override
    public Response nearbyBody(Integer id) {
        List<NearbyBody[]> nearbyBodyList = nearbyTemplate.values(String.format(NEARBY_ID_BODY,String.valueOf(id)));
        NearbyBody[] nearbyBodies = null;
        if(nearbyBodyList.size()>0){
            nearbyBodies = nearbyBodyList.get(0);

        }
        return Response.success(nearbyBodies);
    }

    @Override
    public Response addLongitudeAndLatitude(Integer id, LongitudeAndLatitudeRequest request) throws IllegalAccessException {
        if(id==null|| SysUtils.checkObjFieldIsNull(request)){
          logger.warn("参数为空");
          return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String nearbyBodyMaxDistance = ordersDao.getSysParameter(SysKeyEnum.NEARBY_BODY_MAX_DISTANCE.getKey());
        //最大显示附近的人的距离
        String nearbyBodyDistance = ordersDao.getSysParameter(SysKeyEnum.NEARBY_BODY_DISTANCE.getKey());

        String uuid = UserUtil.generateUuid();
        NearbyBody nearbyBody = ordersDao.findNearbyByUserId(id);
        User user = userDao.findUserByUserId(id);
        //若 未记录经纬度则添加
        //若 已记录则更新
        if(nearbyBody==null){
            logger.info("经纬度已添加 userId={}",id);
            //添加到用户经纬度表 -经纬度-用户id-头像
            ordersDao.addLongitudeAndLatitude(uuid,request,id);
            NearbyBody nearby = ordersDao.findNearbyByUserId(id);

            nearbyUtil.addNearbyBody(nearby,Double.valueOf(nearbyBodyDistance));

        }else {
            Double distance = DistanceTools.getKmByPoint(nearbyBody.getLatitude(),nearbyBody.getLongitude(),request.getLatitude(),request.getLongitude());
            if (distance<Double.valueOf(nearbyBodyMaxDistance)){
                logger.info("userId={}在经纬度最大更新距离distance={}内,不更新经纬度",id,distance);
                return Response.successByArray();
            }
            updateLongitudeAndLatitude( id, request);
        }
        return Response.successByArray();
    }

    @Override
    public Response updateLongitudeAndLatitude(Integer id, LongitudeAndLatitudeRequest request) throws IllegalAccessException {
        if(id==null|| SysUtils.checkObjFieldIsNull(request)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        NearbyBody nearbyBody = ordersDao.findNearbyByUserId(id);
        if (nearbyBody==null){
//            logger.info("经纬度不存在 userId={}",id);
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"经纬度不存在");
            addLongitudeAndLatitude(id, request);
        }
        //最大不更新距离
        String nearbyBodyMaxDistance = ordersDao.getSysParameter(SysKeyEnum.NEARBY_BODY_MAX_DISTANCE.getKey());
        Double distance = DistanceTools.getKmByPoint(nearbyBody.getLatitude(),nearbyBody.getLongitude(),request.getLatitude(),request.getLongitude());
        if (distance>=Double.valueOf(nearbyBodyMaxDistance)){
            logger.info("经纬度已更新 userId={}",id);
            //添加到用户经纬度表 -经纬度-用户id-头像
            User user = userDao.findUserByUserId(id);
            String icon = user.getIcon();
            ordersDao.updateLongitudeAndLatitude( id, request,icon);
            NearbyBody nearby = ordersDao.findNearbyByUserId(id);
            //最大显示附近的人的距离
            String nearbyBodyDistance = ordersDao.getSysParameter(SysKeyEnum.NEARBY_BODY_DISTANCE.getKey());
            nearbyUtil.addNearbyBody(nearby,Double.valueOf(nearbyBodyDistance));
            return Response.successByArray();
        }
//        nearbyUtil.addNearbyBody(nearbyBody,Double.valueOf(nearbyBodyMaxDistance));
        logger.info("userId={}在经纬度最大更新距离distance={}内,不更新经纬度",id,distance);
        return Response.successByArray();
    }

    @Override
    public Response joinLimit(Integer id,String ordersId,Integer perhaps,String value) {
        if (1==1){
            String compareId = orderHashRedisTemplate.get(String.format(ORDERS_ORDERSID,ordersId),String.valueOf(id));
            JSONObject jsonObject = new JSONObject();
            if (compareId==null){
                jsonObject.put("press",0);
            }else {
                jsonObject.put("press",1);

            }
            return Response.success(jsonObject);

        }

        Integer size = undoneRedisTemplate.size(String.format(USER_UNDONE_PERHAPS_SCENES,id,1,value)).intValue();
        Integer size2 = undoneRedisTemplate.size(String.format(USER_UNDONE_PERHAPS_SCENES,id,2,value)).intValue();
        Set<String> set =  undoneRedisTemplate.range(String.format(USER_UNDONE_PERHAPS_SCENES,id,1,value),0,size);
        Set<String> set2 =  undoneRedisTemplate.range(String.format(USER_UNDONE_PERHAPS_SCENES,id,2,value),0,size2);
        Integer exit=0;
        for (String str : set){
            String string = str.split(":")[0];
            if (string.equals(ordersId)){
                exit++;
                break;
            }
        }
        for (String str : set2){
            String string = str.split(":")[0];
            if (string.equals(ordersId)){
                exit++;
                break;
            }
        }
        Orders orders1 = ordersDao.findOrdersOneByUuid(ordersId);
        Orders orders2 = ordersDao.findOrdersByStartTimeStemp(id,orders1.getStartTimeStamp());
        JSONObject jsonObject = new JSONObject();
        if (exit==0&&orders2==null){
            jsonObject.put("press",0);
        }else {
            jsonObject.put("press",1);
        }
        return Response.success(jsonObject);
    }

    @Override
    public Response balanceRecharge(Integer id, Double money) {
        if(id==null||money==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String key = ordersDao.getSysParameter(SysKeyEnum.BALANCE_RECHARGE.getKey());
        if (money<Double.valueOf(key)){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"输入金额应大于"+key+"以上");
        }
        Double countMoney=Arith.add(2,money,Double.valueOf(key));
        User user = userDao.findUserByUserId(id);
        //添加 订单明细
        String detailId = UserUtil.generateUuid();
        //订单明细id--用户uuid--金额--用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
        //充值余额 等待支付宝回调
        userDao.addOrdersDetailForConsumeMoney(detailId,user.getUuid(),countMoney,id, OrdersDetailTypeEnum.INSERT_CONSUME_MONEY.getKey(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.INSERT_CONSUME_MONEY.getValue(),OrdersDetailTypeEnum.RECHARGE.getKey());
        char c = OrdersTypeEnum.CONSUME_MONEY.getQuote();
        String type = String.valueOf(c);

        StringBuffer stringBuffer = new StringBuffer(type);
        stringBuffer.append(detailId);
        String str = stringBuffer.toString();
        return Response.success(str);
    }

    @Override
    public Response inputExcel(String url) {

        return null;
    }

    @Override
    public Response nearbyOrders(Integer id,Integer pageNum) {
        if (id==null||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        NearbyBody nearbyBody = ordersDao.findNearbyByUserId(id);
        List<OrdersResponse> list = ordersDao.findOrdersByLoLa(id,nearbyBody.getLatitude(),nearbyBody.getLongitude(),pageNum);
        return Response.success(list);
    }

    @Override
    public Response createOrdersEachOther(Integer id, String startTime, String place,Double orderPeriod,Double gratefulFree, String scenes, Double money, Double latitude, Double longitude, String address,Integer userId) {
        if (userId == null ||id == null || startTime == null || place == null || orderPeriod == null ||
                scenes == null||money==null||latitude==null||longitude==null||StringUtils.isEmpty(address)) {
            logger.warn("参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String uuid = generateOrderNo(OrdersTypeEnum.INVITE_ORDER,id);
        Scene sc = userDao.findSceneByValue(scenes);
        Double countMoney = Arith.add(2,gratefulFree,Arith.multiplys(2,sc.getHourlyFee(),orderPeriod));
        ordersDao.createOrdersEachOther(uuid,id, startTime, place, orderPeriod, gratefulFree, scenes, money,latitude,longitude,address,userId);

        OrdersEachOtherPayResponse ordersEachOtherPayResponse = new OrdersEachOtherPayResponse();
        ordersEachOtherPayResponse.setOrdersId(uuid);
        ordersEachOtherPayResponse.setMoney(countMoney);
        return Response.success(ordersEachOtherPayResponse);
    }

    @Override
    public Response findAloneOrders(Integer id, String uuid) {
        if(id==null||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空 ");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        OrdersAloneResponse response = ordersDao.findAloneOrders(uuid);
        return  Response.success(response);
    }

    @Override
    public Response findAllAloneOrders(Integer id, Integer userId) {
        if(id==null||userId==null){
            logger.warn("参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<OrdersAloneResponse> list = ordersDao.findAllAloneOrders(userId);
        return Response.success(list);
    }

    @Override
    public Response acceptAloneOrder(Integer id, String orderId) {
        if(id == null || StringUtils.isEmpty(orderId)){
            logger.warn("参数为空 ");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        OrdersAloneResponse response = ordersDao.findAloneOrders(orderId);
        if (!response.getUserInvitation().equals(id)){
            logger.warn("订单出错 userId = {}",id,"用户与订单不符 orderId = {}",orderId);
            return Response.response(ResponseTypeEnum.ERROR_CODE.toCode(),"用户与订单不符");
        }
        ordersDao.acceptAloneOrder(id,orderId);
        return Response.successByArray();
    }




    @Override
    public Response tessss() {
        TransferData data = new TransferData();
        TimeTask task = new TimeTask();
        task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        task.setTimeTaskName("backMoneyTimeTask");
        //当前时间5分钟后
        task.setExecuteTime("*/10 * * * * ?");
        Orders user = new Orders();
        user.setUuid("backMoneyTimeTask");
        task.setParams(JSON.toJSONString(user));
        data.setData(JSONObject.toJSONString(task));

        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data);
        return null;
    }



    /**
     *
     * 获取最新的 订单或者共享订单
     * @return
     */
//    public  Map<String,Orders> getLatestOrders(Integer userId,Integer selTimeType){
    public  Orders getLatestOrders( Orders or ,List<OrdersReceive> ordersServiceList){
        //查找出最新的单子 并存入
        // 最新的 应约的订单
//        Orders or = ordersDao.findReceiveTimeLimitOne(userId,selTimeType);
        List<Orders> ordersList = new ArrayList<Orders>();
        //查询出 该用户所有的 应约的成功 单子
//        List<OrdersReceive> ordersServiceList = ordersDao.findOrdersReceiveByUserId(userId);
//        Map<String,Orders> map = new HashMap<String,Orders>();
        if (or==null){
            if (ordersServiceList.size()==0){
                return null;
            }
            for (int i=0;i<ordersServiceList.size();i++){
                OrdersReceive ordersReceive = ordersServiceList.get(i);
                String ordersId = ordersReceive.getOrdersId();
                Orders ord = ordersDao.findOrdersOneByUuid(ordersId);
                if (ord.getPayType()!=PayTypeEnum.STR_CANCEL.toCode()) {
                    ordersList.add(ord);
                }
            }
        }
        //比较 共享订单 与已发布的 服务单的 开始时间
        // 若 共享订单时间早于 服务订单 则加入集合中
        for (int i=0;i<ordersServiceList.size();i++){
            OrdersReceive ordersReceive = ordersServiceList.get(i);
            Orders ord = ordersDao.findOrdersOneByUuid(ordersReceive.getOrdersId());
            if (or==null){
                if (ord.getPayType()!=PayTypeEnum.STR_CANCEL.toCode()) {
                    ordersList.add(ord);
                }
            }else
            if (Long.valueOf(ord.getStartTimeStamp())<=Long.valueOf(or.getStartTimeStamp())){
                if (ord.getPayType()!=PayTypeEnum.STR_CANCEL.toCode()) {
                    ordersList.add(ord);
                }
            }
        }

        //遍历 出时间最近的 那个 订单
        Long lastStartTime = null;
        Orders lastOrders = null;
        //若 共享订单 集合为空 则 最新订单未 服务订单
        if (ordersList==null||ordersList.size()==0){
            lastOrders=or;
        }else {
            for (int j = 0; j < ordersList.size(); j++) {
                Long lo = Long.valueOf(ordersList.get(j).getStartTimeStamp());
                if (lastStartTime == null) {
                    lastStartTime = lo;
                    lastOrders = ordersList.get(j);
                } else if (lastStartTime > lo) {
                    lastStartTime = lo;
                    lastOrders = ordersList.get(j);
                }
            }
        }
        return lastOrders;
    }
    public void deleteTaskOrders(Orders orderType)throws Exception{
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
    public static void main(String[] args) throws ParseException {

//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        Date date = new Date();
//        String time =simpleDateFormat.format(date);
//        SimpDate simpDate = SimpDateFactory.endDate();
//        String edddddd= simpDate.endDate(time,2.5,0);
//        System.out.println(edddddd);
    }

}
