package com.wuai.company.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.EaseCreateGroupResponse;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.enums.*;
import com.wuai.company.message.RabbitMqPublishImpl;
import com.wuai.company.message.TransferData;
import com.wuai.company.order.dao.SceneDao;
import com.wuai.company.order.dao.TrystEvaluationDao;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.order.entity.DistancePo;
import com.wuai.company.order.entity.TrystEvaluation;
import com.wuai.company.order.entity.TrystScene;
import com.wuai.company.order.entity.TrystSceneExample;
import com.wuai.company.order.mapper.TrystSceneMapper;
import com.wuai.company.order.service.TrystOrdersService;
import com.wuai.company.user.EaseMob.api.ChatGroupAPI;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.util.*;
import com.wuai.company.util.comon.TimeUtil;
import io.swagger.client.model.Group;
import io.swagger.client.model.UserName;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hyf on 2018/1/17.
 */
@Service
@Transactional
public class TrystOrdersServiceImpl implements TrystOrdersService {

    Logger logger = LoggerFactory.getLogger(TrystOrdersServiceImpl.class);

    @Autowired
    private TrystOrdersDao trystOrdersDao;

    @Autowired
    private SceneDao sceneDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TrystSceneMapper trystSceneMapper;

    @Autowired
    private TrystEvaluationDao trystEvaluationDao;

    @Autowired
    private ChatGroupAPI chatGroupAPI;

    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;

    @Resource
    private RedisTemplate removeRedisTemplate;

    @Resource
    private HashOperations<String,String,SnatchUser> snatchUserTemplate;
    @Resource
    private HashOperations<String, String, NearBodyResponse[]> nearBodyTemplate;
    @Resource
    private ZSetOperations<String,String> msgValueTemplate;

    private final String USER_MSG_TRYST = "tryst:%s:msg"; //用户id--信息列表
    private final String MSG_CONTENT_TRYST = "%s:%s:%s:%s:%s";//用户id--类型--对方昵称--订单号--当前时间
    private final String SNATCH_USER_TRYST = "snatch:user:%s"; //tryst 订单号
    private final String NEARBY_ID_BODY_TRYST="nearby:%s:body:%s";//用户id--约会id
    private final String DEVICE_PASS_KEY = "MyPassWo";

    /*
        创建订单
     */
    @Override
    public Response createTrystOrders(Integer id, TrystOrdersRequest trystOrdersRequest) throws Exception{
        if(id == null || SysUtils.checkObjFieldIsNull(trystOrdersRequest)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //当前时间 大于约会时间
        if (TimeUtil.compareTime(TimeUtil.currentTime(),trystOrdersRequest.getTime())>=0){
            logger.warn("当前时间大于约会时间currentTime={}",TimeUtil.currentTime());
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"当前时间大于约会时间");
        }

        //至少一个半小时的空余时间
        String beforeTime = TimeUtil.beforeTime(trystOrdersRequest.getTime(),1,30);
        if(!(TimeUtil.compareTime(beforeTime,TimeUtil.currentTime())>0&&TimeUtil.compareTime(trystOrdersRequest.getTime(),TimeUtil.currentTime())>0)){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单至少在一个半小时后开始");
        }

        String startTime = trystOrdersRequest.getTime();
        String mm = String.valueOf(trystOrdersRequest.getDuration()).split("\\.")[0];
        String dd = String.valueOf(trystOrdersRequest.getDuration()).split("\\.")[1];
        String endTime = TimeUtil.afterTime(trystOrdersRequest.getTime(),Integer.parseInt(mm),Integer.parseInt(dd));
        //今日订单中已存在该时间段订单
        //查询我的所有未完成的订单
        List<TrystOrders> list = trystOrdersDao.findTrystOrdersListById(id);
        Integer exit = 0;
        for (TrystOrders trystOrders : list){
            String start = trystOrders.getTime();
            String m = String.valueOf(trystOrders.getDuration()).split("\\.")[0];
            String d = String.valueOf(trystOrders.getDuration()).split("\\.")[1];
            String end = TimeUtil.afterTime(trystOrders.getTime(),Integer.parseInt(m),Integer.parseInt(d));
            // 订单开始 时间大于开始并小于结束
            if (TimeUtil.compareTime(startTime,start)>=0&&TimeUtil.compareTime(startTime,end)<=0){
                logger.warn("当前订单开始时间在 已有订单的 时间段内  uuid = {}", trystOrders.getUuid());
                exit++;
                break;
            }
            //订单结束时间 大于开始小于结束
            if (TimeUtil.compareTime(endTime,start)>=0&&TimeUtil.compareTime(endTime,end)<=0){
                logger.warn("当前订单结束时间在 已有订单的 时间段内  uuid = {}", trystOrders.getUuid());
                exit++;
                break;
            }
        }
        if (exit>0){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该时间段已存在订单");
        }
        String today = TimeUtil.today();
        //查找今日 取消订单列表
        List<TrystCancel> trystCancelList = trystOrdersDao.findCancelTryst(id,CancelEnum.TRYST.getCode(),today);
        if(trystCancelList != null){
            if (trystCancelList.size() == 3){
                logger.warn("今日已选人的取消订单次数已达三次");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"今日已选人的取消订单次数已达三次");
            }
        }
        String uuid = UserUtil.generateUuid();
        //创建 约会订单
        trystOrdersDao.createTrystOrders(id,uuid,trystOrdersRequest,PayTypeEnum.TRYST_SNATCH_CONFIRM.toCode(),PayTypeEnum.TRYST_SNATCH_CONFIRM.getValue());

        /*****************************************************定时任务发布：自动取消*******************************************************/
        TimeTask timeTask = new TimeTask();
        timeTask.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        timeTask.setTimeTaskName("trystOrder:endSnatch:" + uuid);
        timeTask.setExecuteTime(TimeUtil.getCurrentAfterCron(10));
        TrystOrders entity = new TrystOrders();
        entity.setUuid("trystOrder:endSnatch:" + uuid);
        timeTask.setParams(JSON.toJSONString(entity));
        TransferData data = new TransferData();
        data.setData(JSONObject.toJSONString(timeTask));
        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data);
        logger.warn("订单 id={} 自动取消定时任务已发布！",uuid);

        //经匹配的订单人存入redis
        /*TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        nearbyUtil.findTodayTotalNearBodies(trystOrders,trystOrdersRequest.getSex());*/
        Map<String, Object> map = new HashMap<>();
        //map.put("trystId",OrdersTypeEnum.TRYST_ADVANCE_MONEY.getQuote() + uuid);
        map.put("trystId", uuid);
        map.put("money",trystOrdersRequest.getAdvanceMoney());
        map.put("bugInfo",trystOrdersRequest.getScene());
        return Response.success(map);
    }


    /*
        显示可抢单列表
     */
    @Override
    public Response snatchableListAuth(Integer id, Integer pageNum, Double longitude, Double latitude){
        //不显示人数已满足的。。不显示已抢的
        //是否仅显示 推送
        if (null == id || null == pageNum){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = trystOrdersDao.findUserById(id);
//        List<String> list = orderHashRedisTemplate.values(String.format(NEARBY_ID_TRYST,id));
        //根据性别要求 查询 等待抢单的订单
        PageHelper.startPage(pageNum,10);
        List<TrystOrders> trystOrdersList = trystOrdersDao.findTrystOrdersList(user.getGender(),PayTypeEnum.TRYST_SNATCH_CONFIRM.toCode());   //按最新发布的排序
        List<Map<String, Object>> res = new ArrayList<>();
        for (TrystOrders trystOrder : trystOrdersList){
            //判断是否已抢单
            if (snatchUserTemplate.get(String.format(SNATCH_USER_TRYST,trystOrder.getUuid()),String.valueOf(id)) != null) continue;
            //判断是否是自己的单
            if (trystOrder.getUserId().equals(id)) continue;

            Map<String, Object> map = new HashMap<>();
            User demander = trystOrdersDao.findUserById(trystOrder.getUserId());
            map.put("uuid",trystOrder.getUuid());
            map.put("demanderName",demander.getNickname());
            map.put("demanderHeadUrl",demander.getIcon());
            map.put("demanderSex",demander.getGender());
            map.put("scenePic",sceneDao.selectScenePicByScene(trystOrder.getScene()));
            map.put("sceneName",trystOrder.getScene());
            map.put("distance",getDistance(latitude,longitude,trystOrder.getLatitude(),trystOrder.getLongitude()));
            map.put("sceneContent",sceneDao.selectSceneContentByScene(trystOrder.getScene()));
            map.put("time",TimeUtil.getTrystTimeFormat(trystOrder.getTime(),trystOrder.getDuration()));
            map.put("place",trystOrder.getPlace());
            map.put("personSex",trystOrder.getSex());
            map.put("personCount",trystOrder.getPersonCount());
            map.put("reward",trystOrder.getMoney());
            res.add(map);
        }
        return Response.success(res);
    }


    /*
        服务方抢单
    */
    @Override
    public Response snatchOrder(Integer id, String uuid){
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }

        //判断该订单是否有效
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        if (trystOrders.getPayCode() != PayTypeEnum.TRYST_SNATCH_CONFIRM.toCode()){
            return Response.error("该订单已失效");
        }

        //检查三次违规
        if (trystOrdersDao.selectCancelCountByService(id) >= 3){
            return Response.error("您因为多次取消订单已违规，请交纳保证金后继续抢单");
        }

        //检查视频审核是否已通过
        if (userDao.selectUserVideoByCheckAndUserId(VideoCheckEnum.SUCCESS_CHECK.getCode(),id).size() == 0){
            return Response.error("视频审核通过之后才能抢单哦");
        }

        //查看当前时间段是否已经存在订单
        String startTime = trystOrders.getTime();
        String mm = String.valueOf(trystOrders.getDuration()).split("\\.")[0];
        String dd = String.valueOf(trystOrders.getDuration()).split("\\.")[1];
        String endTime = TimeUtil.afterTime(trystOrders.getTime(),Integer.parseInt(mm),Integer.parseInt(dd));
        List<TrystOrders> demandList = trystOrdersDao.findTrystOrdersListById(id);
        List<TrystOrders> serviceList = trystOrdersDao.selectTrystOrdersByReceivAndUserId(id);
        demandList.addAll(serviceList);
        Integer exit = 0;
        for (TrystOrders trystOrder : demandList){
            String start = trystOrder.getTime();
            String m = String.valueOf(trystOrder.getDuration()).split("\\.")[0];
            String d = String.valueOf(trystOrder.getDuration()).split("\\.")[1];
            String end = TimeUtil.afterTime(trystOrder.getTime(),Integer.parseInt(m),Integer.parseInt(d));
            // 订单开始 时间大于开始并小于结束
            if (TimeUtil.compareTime(startTime,start)>=0&&TimeUtil.compareTime(startTime,end)<=0){
                logger.warn("当前订单开始时间在 已有订单的 时间段内  uuid = {}", trystOrder.getUuid());
                exit++;
                break;
            }
            //订单结束时间 大于开始小于结束
            if (TimeUtil.compareTime(endTime,start)>=0&&TimeUtil.compareTime(endTime,end)<=0){
                logger.warn("当前订单结束时间在 已有订单的 时间段内  uuid = {}", trystOrder.getUuid());
                exit++;
                break;
            }
        }
        if (exit>0){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"抢单失败，该时间段已存在订单");
        }


        //服务方用户
        User user = trystOrdersDao.findUserById(id);
        //添加到 该订单 抢单列表中
        SnatchUser u = snatchUserTemplate.get(String.format(SNATCH_USER_TRYST,uuid),String.valueOf(id));
        if (u != null){
            logger.warn("已抢订单");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已抢订单");
        }

        /**********************************************如果是第一个抢单人，重刷自动取消时间****************************************************/

        //判断是否是第一个抢单人员
        if (snatchUserTemplate.values(String.format(SNATCH_USER_TRYST,uuid)).size()==0){
            TimeTask timeTask = new TimeTask();
            timeTask.setScheduleOperaEnum(ScheduleOperaEnum.UPDATE_TASK);
            timeTask.setTimeTaskName("trystOrder:endSnatch:" + uuid);
            timeTask.setExecuteTime(TimeUtil.getCurrentAfterCron(20));
            TrystOrders entity = new TrystOrders();
            entity.setUuid("trystOrder:endSnatch:" + uuid);
            timeTask.setParams(JSON.toJSONString(entity));
            TransferData data = new TransferData();
            data.setData(JSONObject.toJSONString(timeTask));
            data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
            rabbitMqPublish.publish(data);
            logger.warn("订单 id={} 自动取消定时任务已更新！",uuid);
        }

        //添加到抢单列表
        SnatchUser snatchUser = new SnatchUser();
        snatchUser.setUserId(user.getId());
        snatchUser.setUuid(user.getUuid());
        snatchUser.setHeadUrl(user.getIcon());
        snatchUser.setNickName(user.getNickname());
        snatchUser.setGender(user.getGender());
        snatchUser.setPhone(user.getPhoneNum());
        snatchUser.setAge(user.getAge());
        snatchUser.setZodiac(user.getZodiac());
        snatchUser.setHeight(user.getHeight());
        snatchUser.setWeight(user.getWeight());
        snatchUser.setSignature(user.getSignature());
        snatchUser.setVideos(userDao.findVideos(user.getId()));
        //功能未完成
        snatchUser.setLocation("杭州");
        snatchUser.setComment("暂无评论");
        snatchUserTemplate.put(String.format(SNATCH_USER_TRYST,uuid),String.valueOf(id),snatchUser);

        return Response.success();
    }


    /*
        需求方显示已抢单人员列表
     */
    @Override
    public Response snatchUserList(Integer id, String uuid) {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        Map<String, Object> res = new HashMap<>();
        List<SnatchUser> list = snatchUserTemplate.values(String.format(SNATCH_USER_TRYST,uuid));
        res.put("snatchUsers",list);

        //判断订单是否已取消
        if (trystOrders.getPayCode().equals(PayTypeEnum.BEFORE_CANCEL_TRYST.toCode())){
            res.put("isCancel","true");
        }else{
            res.put("isCancel","false");
        }

        return Response.success(res);
    }


    /*
        选择用户，确认订单
     */
    @Override
    public Response sureUser(Integer id, String uuid, String userIds) {
        if (null == id || StringUtils.isEmpty(uuid) || StringUtils.isEmpty(userIds)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String[] ids =  userIds.split(",");
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        User demand = userDao.findUserByUserId(trystOrders.getUserId());
        Map<String, Object> res = new HashMap<>();

        //判断订单是否已取消
        if (trystOrders.getPayCode().equals(PayTypeEnum.BEFORE_CANCEL_TRYST.toCode())){
            res.put("isCancel","true");
            return Response.success(res);
        }
        for (String id1 : ids) {
            //添加到 约吧订单 加入订单
            //即认证用户添加到与约吧订单相关联表中
            //Integer userId = userDao.findUserByUserId(id).getId();
            String uid = UserUtil.generateUuid();
            //新增反馈表数据      --       tryst_receive           uid对于表uuid  uuid对应表trystId  userId对应表userId(user表的Id主键）
            trystOrdersDao.addTrystOrdersReceive(uid, uuid, Integer.valueOf(id1), trystOrders.getMoney(), Boolean.TRUE);
            //修改人数  --      tryst_orders
            trystOrdersDao.upTrystPersonCount(uuid, ids.length);
        }

        res.put("isCancel","false");
        res.put("money",trystOrders.getMoney() * ids.length);
        res.put("bugInfo",trystOrders.getScene());
        res.put("payId",OrdersTypeEnum.TRYST_ORDERS.getQuote() + trystOrders.getUuid());
        return Response.success(res);
    }

    /*
        余额支付
     */
    @Override
    public Response payMoneyAuth(Integer id, String uuid, Double money, String password) {
        Map<String, Object> res = new HashMap<>(16);
        if (null == id || StringUtils.isEmpty(uuid) || null == money){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User demand = userDao.findUserByUserId(id);

        //检查密码
        if (demand.getPayPass()==null){
            return Response.error("未设置支付密码");
        }
        String dePass =  DesUtil.decrypt(password,DEVICE_PASS_KEY);
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));
        if (!demand.getPayPass().equals(MD5Pass)){
            return Response.error("密码错误");
        }
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);


        if (trystOrders == null){
            return Response.error("该订单不存在");
        }

        Double payMoney = trystOrders.getMoney() * trystOrders.getPersonCount();
        if (!payMoney.equals(money)){
            logger.warn("订单id={}支付金额错误",uuid);
            return Response.error("支付金额错误");
        }

        if (userDao.upTrystOrdersPay(uuid,PayTypeEnum.TRYST_WAIT_CONFIRM.toCode(),PayTypeEnum.TRYST_WAIT_CONFIRM.getValue()) == 0){
            return Response.error("修改订单状态错误");
        }

        //查看余额
        if (demand.getMoney()<money){
            logger.warn("用户id={}支付订单id={}时金额不足",uuid);
            return Response.error("余额不足，请充值");
        }

        //账户余额扣款
        if (userDao.deductMoney(money,id) == 0){
            return Response.error("扣除余额失败");
        }

        userDao.exitTrystReceive(Boolean.FALSE,uuid);

        List<User> serviceList = userDao.selectUsersFromReceiveByTrystId(uuid);

        //清除redis抢单用户
        removeRedisTemplate.delete(String.format(SNATCH_USER_TRYST,uuid));

        UserName userName = new UserName();

        for (User user : serviceList){
            long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,user.getId()));
            String content = String.format(MSG_CONTENT_TRYST,user.getId(),MsgTypeEnum.EFFECT_TRYST.getCode(),demand.getNickname(),uuid,TimeUtil.currentTime());
            msgValueTemplate.add(String.format(USER_MSG_TRYST,user.getId()),content ,size+1);

            userName.add(user.getPhoneNum());
        }

        long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,demand.getId()));
        String content = String.format(MSG_CONTENT_TRYST,demand.getId(),MsgTypeEnum.DEMAND_EFFECT_TRYST.getCode(),demand.getNickname(),uuid,TimeUtil.currentTime());
        msgValueTemplate.add(String.format(USER_MSG_TRYST,demand.getId()),content ,size+1);

        logger.warn("订单id={}生效后后，系统消息已送达",uuid);

        if (userName.size()>1){

            /*********************************************************创建群聊**********************************************************************/
            Group group = new Group();
            group.setGroupname(demand.getNickname()+"的"+trystOrders.getScene()+"群组");
            group.setDesc(demand.getNickname()+"创建");
            group.setPublic(Boolean.TRUE);
            group.setMaxusers(100);
            group.setApproval(Boolean.TRUE);
            group.setOwner(demand.getPhoneNum());
            group.setMembers(userName);

            //聊天室持久化
            String groupId = JSON.parseObject(chatGroupAPI.createChatGroup(group).toString(),EaseCreateGroupResponse.class).getData().getGroupid();
            String easeUUid = UserUtil.generateUuid();
            userDao.insertEaseMob(easeUUid,uuid,groupId);

            logger.warn("订单id={}已创建群聊id={}",uuid,groupId);

            res.put("groupId",groupId);
        }else {
            res.put("groupId",null);
        }

        /**********************************************发布定时任务：订单时间已结束，可以手动完成订单*********************************************************/
        TimeTask timeTask = new TimeTask();
        timeTask.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        timeTask.setTimeTaskName("trystOrder:advanceOneHour:" + uuid);
        timeTask.setExecuteTime(TimeUtil.getfrontTimeCron(trystOrders.getTime(),1.0));
        TrystOrders entity = new TrystOrders();
        entity.setUuid("trystOrder:advanceOneHour:"+trystOrders.getUuid());
        timeTask.setParams(JSON.toJSONString(entity));
        TransferData data = new TransferData();
        data.setData(JSONObject.toJSONString(timeTask));
        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data);
        logger.warn("订单 id={} 提前一小时通知所有用户将开始 定时任务已发布！",uuid);

        res.put("trystId",uuid);

        return Response.success(res);
    }


    /*
        聊天室订单简要
     */
    @Override
    public Response roomTrystAuth(Integer id, String uuid){
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        if (trystOrders == null){
            return Response.error("未找到订单");
        }
        Map<String, Object> res = new HashMap<>();
        res.put("trystId",trystOrders.getUuid());
        res.put("scene",trystOrders.getScene());
        res.put("place",trystOrders.getPlace());
        res.put("time",TimeUtil.getTrystTimeFormat(trystOrders.getTime(),trystOrders.getDuration()));
        res.put("money",trystOrders.getMoney());
        return Response.success(res);
    }


    /*
        通过订单id拿到订单详情
     */
    @Override
    public Response findTrystOrders(Integer id, String uuid) throws Exception {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        if (trystOrders == null){
            return Response.error("未找到订单");
        }
        //id主人
        User mine = userDao.findUserByUserId(id);

        //判断是否已取消   已取消true   未取消false
        boolean isCancel = trystOrdersDao.selectUsersByCancel(trystOrders.getUuid()).contains(id);

        TrystSceneExample example = new TrystSceneExample();
        TrystSceneExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(trystOrders.getScene());
        TrystScene trystScene = trystSceneMapper.selectByExample(example).get(0);
        //发单方
        User user = userDao.findUserByUserId(trystOrders.getUserId());
        Map<String, Object> res = new HashMap<>();
        res.put("trystId",trystOrders.getUuid());
        res.put("scenePic",trystScene.getPic());
        res.put("sceneName",trystScene.getName());
        if (isCancel){
            res.put("state","已取消");
        }else {
            res.put("state",trystOrders.getPayValue());
        }
        res.put("place",trystOrders.getPlace());
        res.put("time",TimeUtil.getTrystTimeFormat(trystOrders.getTime(),trystOrders.getDuration()));
        res.put("personCount",trystOrders.getPersonCount());
        res.put("personSex",trystOrders.getSex());
        res.put("money",trystOrders.getMoney());

        //显示用户
        //发单方页面
        if (trystOrders.getUserId().equals(id)){
            res.put("role",TrystRole.TRYST_ROLE_DEMAND.getValue());
            List<Map<String, Object>> usersList = new ArrayList<>();
            List<User> users = userDao.selectUsersFromReceiveByTrystId(trystOrders.getUuid());
            for (User user1 : users){
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userId",user1.getUuid());
                //查看是否已对他/她评价
                if (trystEvaluationDao.selectEvaluationByTrystIdAndOwnAndTarget(trystOrders.getUuid(),id,user1.getId()).size()>0){
                    userMap.put("isEvaluate","true");
                }else {
                    userMap.put("isEvaluate","false");
                }
                userMap.put("headUrl",user1.getIcon());
                userMap.put("nickName",user1.getNickname());
                userMap.put("phone",user1.getPhoneNum());
                userMap.put("userSex",user1.getGender());
                userMap.put("age",user1.getAge());
                usersList.add(userMap);
            }
            res.put("users",usersList);
        //赴约方页面
        }else {
            res.put("role",TrystRole.TRYST_ROLE_SERVICE.getValue());
            List<Map<String, Object>> demandList = new ArrayList<>();
            Map<String, Object> demandMap = new HashMap<>();
            demandMap.put("userId",user.getUuid());
            //查看是否已对他/她评价
            if (trystEvaluationDao.selectEvaluationByTrystIdAndOwnAndTarget(trystOrders.getUuid(),id,user.getId()).size()>0){
                demandMap.put("isEvaluate","true");
            }else {
                demandMap.put("isEvaluate","false");
            }
            demandMap.put("headUrl",user.getIcon());
            demandMap.put("nickName",user.getNickname());
            demandMap.put("phone",user.getPhoneNum());
            demandMap.put("userSex",user.getGender());
            demandMap.put("age",user.getAge());
            demandList.add(demandMap);
            res.put("users",demandList);
        }
//        res.put("distance",getDistance(trystOrders.getLatitude(),trystOrders.getLongitude(),))

        //其他人对 id 拥有者的评价
        List<TrystEvaluation> trystEvaluations = trystEvaluationDao.selectEvaluationByTrystId(trystOrders.getUuid(), mine.getId());

        //判断是否有评价
        if (trystEvaluations != null){
            List<Map<String, Object>> list = new ArrayList<>();
            for (TrystEvaluation trystEvaluation : trystEvaluations){
                Map<String, Object> map = new HashMap<>();
                User evaluator = userDao.findUserByUserId(trystEvaluation.getOwnUserId());
                map.put("userId",evaluator.getId());
                map.put("userUuid",evaluator.getUuid());
                map.put("userName",evaluator.getNickname());
                map.put("userSex",mine.getGender());
                map.put("time",new SimpleDateFormat("yyyy-MM-dd HH:mm").format(trystEvaluation.getCreateTime()));
                map.put("serviceAttitude",trystEvaluation.getServiceAttitude());
                map.put("serviceLabel",trystEvaluation.getServiceLabel());
                map.put("similarity",trystEvaluation.getSimilarity());
                list.add(map);
            }
            res.put("evaluation",list);
        }else {
            res.put("evaluation",null);
        }

        //取消按钮是否可使用
        if (isCancel){
            res.put("canceling","不可取消");
        }else if (trystOrders.getPayCode().equals(PayTypeEnum.TRYST_WAIT_CONFIRM.toCode())){
            res.put("canceling","可取消");
            String afterTime = TimeUtil.afterTime(TimeUtil.currentTime(),1,0);
            if (TimeUtil.compareTime(afterTime,trystOrders.getTime())>0&&TimeUtil.compareTime(trystOrders.getTime(),TimeUtil.currentTime())>0){
                res.put("canceling","不可取消");
            }else {
                res.put("canceling","可取消");
            }
        }else {
            res.put("canceling","不可取消");
        }

        //订单是否可修改   true不可修改    false可修改
        if (trystOrders.getShowName() == 2){
            res.put("isUpdated","true");
        }else {
            res.put("isUpdated","false");
        }

        String groupId = userDao.selectGroupIdByTrystId(uuid);
        if (groupId == null){
            res.put("groupId",null);
        }else {
            res.put("groupId",groupId);
        }

        return Response.success(res);
    }


    /*
        遍历出待消费后的订单
     */
    @Override
    public Response listTrystAuth(Integer id, Integer pageNum){
        if (null == id || pageNum == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<TrystOrders> trystOrdersList = trystOrdersDao.selectTrystOrderByReceiveUserId(id,pageNum);
        if (trystOrdersList == null){
            return Response.success("暂无相关订单");
        }
        List<Map<String, Object>> res = new ArrayList<>();
        String today = TimeUtil.today();
        for (TrystOrders trystOrders : trystOrdersList){
            Map<String, Object> map = new HashMap<>(16);
            map.put("uuid",trystOrders.getUuid());
            map.put("scene",trystOrders.getScene());
            if (trystOrdersDao.selectUsersByCancel(trystOrders.getUuid()).contains(id)){
                map.put("state","已取消");
            }else {
                map.put("state",trystOrders.getPayValue());
            }
            map.put("place",trystOrders.getPlace());
            map.put("money",trystOrders.getMoney());
            if (trystOrders.getPayCode() == PayTypeEnum.TRYST_WAIT_CONFIRM.toCode()){
                map.put("time",TimeUtil.getTrystTimeFormat(trystOrders.getTime(),trystOrders.getDuration()));
            }else{
                map.put("time","有效期至 " + trystOrders.getTime().substring(0,10));
            }
            if (trystOrders.getTime().substring(0,10).equals(today)){
                map.put("color","red");
            }else {
                map.put("color","normal");
            }
            res.add(map);
        }
        return Response.success(res);
    }


    /*
        修改订单（只能修改时间，且修改次数为1）
     */
    @Override
    public Response modifyTrystAuth(Integer id, String uuid, String time, Double duration){
        if (null == id || StringUtils.isEmpty(uuid) || StringUtils.isEmpty(time) || duration ==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }

        User demand = userDao.findUserByUserId(id);

        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);

        if (trystOrders == null || trystOrders.getShowName() == 2){
            return Response.error("未找到订单，或者订单已修改过");
        }

        String afterTime = TimeUtil.afterTime(TimeUtil.currentTime(),2,0);
        if(TimeUtil.compareTime(afterTime,trystOrders.getTime())>0&&TimeUtil.compareTime(trystOrders.getTime(),TimeUtil.currentTime())>0){
            return Response.error("订单开始前两个小时不可修改订单");
        }

        if (TimeUtil.compareTime(time,trystOrders.getTime())<=0){
            return Response.error("对不起，您只能推迟约会时间");
        }

        //修改订单信息
        if (trystOrdersDao.updateTimeAndDurByTrystId(uuid,time,duration,2) == 0){
            return Response.error("修改订单信息失败");
        }

        //系统消息通知给所有赴约方，订单已修改
        List<User> userList = userDao.selectUsersFromReceiveByTrystId(uuid);
        for (User service : userList){
            long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,service.getId()));
            String content = String.format(MSG_CONTENT_TRYST,id,MsgTypeEnum.UPDATE_TRYST.getCode(),demand.getNickname(),uuid,TimeUtil.currentTime());
            msgValueTemplate.add(String.format(USER_MSG_TRYST,service.getId()),content,size+1);
        }

        long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,demand.getId()));
        String content = String.format(MSG_CONTENT_TRYST,id,MsgTypeEnum.DEMAND_UPDATE_TRYST.getCode(),demand.getNickname(),uuid,TimeUtil.currentTime());
        msgValueTemplate.add(String.format(USER_MSG_TRYST,demand.getId()),content,size+1);

        logger.warn("订单id={}已修改约会时间",uuid);

        /****************************************更新trystOrder:advanceOneHour:定时器****************************************************************/
        TimeTask timeTask = new TimeTask();
        timeTask.setScheduleOperaEnum(ScheduleOperaEnum.UPDATE_TASK);
        timeTask.setTimeTaskName("trystOrder:advanceOneHour:" + uuid);
        timeTask.setExecuteTime(TimeUtil.getfrontTimeCron(time,1.0));
        TrystOrders entity = new TrystOrders();
        entity.setUuid("trystOrder:advanceOneHour:" + uuid);
        timeTask.setParams(JSON.toJSONString(entity));
        TransferData data = new TransferData();
        data.setData(JSONObject.toJSONString(timeTask));
        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data);
        logger.warn("订单 id={} 提前一小时通知所有用户将开始 定时任务已发布！",uuid);


        return Response.success("订单修改成功");
    }

    /*
        发单方确认完成订单
     */
    @Override
    public Response completeDemandAuth(Integer id, String uuid){
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }

        User demand = userDao.findUserByUserId(id);

        if (trystOrdersDao.findTrystOrdersByUid(uuid).getPayCode().equals(PayTypeEnum.TRYST_SUCCESS.toCode())){
            return Response.error("此订单已完成");
        }

        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);

        if (trystOrders == null){
            return Response.error("订单为空");
        }

        //更新订单状态
        if (trystOrdersDao.calcelTrystOrders(uuid,PayTypeEnum.TRYST_SUCCESS.toCode(),PayTypeEnum.TRYST_SUCCESS.getValue())==0){
            return Response.error("修改订单未完成状态失败");
        }


        List<User> userList = userDao.selectUsersFromReceiveByTrystId(uuid);

        for (User service : userList){

            //支付宝分发打赏
            //userService.aliTransfer(OrdersTypeEnum.COMPLETE_TRYST_SERVICE.getQuote()+uuid, user.getAccountNum(), amount, null,null,"备注");
            //平台钱包
            trystOrdersDao.upMoney(service.getId(),trystOrders.getMoney() * (1 - MoneyRateEnum.COMPLETE_RATE.getKey()));
            logger.warn("id={}用户钱包已收款{}元",service.getId(),trystOrders.getMoney() * (1 - MoneyRateEnum.COMPLETE_RATE.getKey()));

            //系统消息通知给所有用户（关于订单）订单已完成，钱已到账
            long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,service.getId()));
            String content = String.format(MSG_CONTENT_TRYST,id,MsgTypeEnum.COMPLETE_TRYST.getCode(),demand.getNickname(),uuid,TimeUtil.currentTime());
            msgValueTemplate.add(String.format(USER_MSG_TRYST,service.getId()),content ,size + 1);

        }
        return Response.success("订单已完成");
    }


    /*
        需求方取消订单
     */
    @Override
    public Response cancelTrystAuth(Integer id, String uuid, Integer passive, String reason) {

        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }

        String uid = UserUtil.generateUuid();
        String date = TimeUtil.afterTime(TimeUtil.currentTime(),2,0);
        String today = TimeUtil.today();
        String afterTime = TimeUtil.afterTime(TimeUtil.currentTime(),1,0);

//        List<TrystCancel>  trystCancelList = trystOrdersDao.findCancelTryst(id,CancelEnum.TRYST.getCode(),today);
        TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
        User user = userDao.findUserByUserId(id);


        //长时间没有用户抢单     ||      未付款
        if (passive == 1 || trystOrders.getPayCode().equals(PayTypeEnum.TRYST_SNATCH_CONFIRM.toCode())){

            //如果已有人抢单
            if (snatchUserTemplate.values(String.format(SNATCH_USER_TRYST,uuid)).size()>0){
                //取消订单列表 --  create t_tryst_cancel              id 需求方userId         uuid 订单号
                trystOrdersDao.cancelTryst(uid, id, uuid, CancelEnum.TRYST.getCode(), CancelEnum.CHOOSE.getCode(), CancelEnum.CHOOSE.getValue(), today, date, 1,reason);
                logger.warn("取消订单id={}已生成",uid);


                //系统通知给所有抢单方，订单已取消
                Set<String> keys = snatchUserTemplate.keys(String.format(SNATCH_USER_TRYST, uuid));

                for (String userId : keys){
                    long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,userId));
                    String content = String.format(MSG_CONTENT_TRYST,id,MsgTypeEnum.CANCEL_DEMAND_UNPAY_TRYST.getCode(),user.getNickname(),uuid,TimeUtil.currentTime());
                    msgValueTemplate.add(String.format(USER_MSG_TRYST,userId),content ,size + 1);
                }

                logger.warn("订单id={}取消后，系统消息已送达",uuid);

            }

            //返还预付金额
            //trystOrdersDao.upMoney(id,trystOrders.getAdvanceMoney());

            //未付款前修改订单订单状态为已取消
            if (trystOrdersDao.calcelTrystOrders(uuid,PayTypeEnum.BEFORE_CANCEL_TRYST.toCode(),PayTypeEnum.BEFORE_CANCEL_TRYST.getValue()) == 0){
                return Response.error("将订单状态修改为”已取消“失败");
            }

            logger.warn("订单id={}状态——》已取消",uuid);

        //已付款
        }else if (trystOrders.getPayCode().equals(PayTypeEnum.TRYST_WAIT_CONFIRM.toCode())){
            //当前时间一个小时候大于订单时间 且 当前时间小于订单时间
            if(TimeUtil.compareTime(afterTime,trystOrders.getTime())>0&&TimeUtil.compareTime(trystOrders.getTime(),TimeUtil.currentTime())>0){
                logger.warn("订单开始前一个小时不可取消订单");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单开始前一个小时不可取消订单");
            }

            //系统消息通知给所有赴约方，订单已取消
            List<User> userList = userDao.selectUsersFromReceiveByTrystId(uuid);
            for (User service : userList){
                long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,service.getId()));
                String content = String.format(MSG_CONTENT_TRYST,id,MsgTypeEnum.CANCEL_DEMAND_PAY_TRYST.getCode(),user.getNickname(),uuid,TimeUtil.currentTime());
                msgValueTemplate.add(String.format(USER_MSG_TRYST,service.getId()),content ,size+1);
            }


            //判断是否为首次取消订单
            TrystCancel trystCancelFir = trystOrdersDao.findCancelTrystFir(id,CancelEnum.TRYST.getCode(),CancelEnum.DOWN.getCode());
            //若不是首次取消订单
            Double delMoney;
            if (trystCancelFir!=null){
                delMoney = MoneyRateEnum.NORMAL_CANCEL.getKey();
            }else {
                delMoney = MoneyRateEnum.FIRST_CANCEL.getKey();
            }

            //总金额
            Double totalAmount = trystOrders.getMoney() * trystOrders.getPersonCount();
            //发单用户返还金额
            Double refund = totalAmount * (1 - delMoney);
            //更改发单方账户余额
            trystOrdersDao.upMoney(id,refund);
            logger.warn("发单用户id={}已退还，金额为refund={}",id,refund);

            //违约金
            Double damages = totalAmount - refund;
            //所有赴约方拿到的违约金
            List<TrystReceive> trystReceiveList = trystOrdersDao.selectReceiveByTryst(uuid);
            Double damagesAvg = damages / trystReceiveList.size();

            for (TrystReceive trystReceive:trystReceiveList){
                trystOrdersDao.upMoney(trystReceive.getUserId(),damagesAvg);
            }
            logger.warn("赴约用户拿到违约金为damages={}",damagesAvg);

            //取消订单列表insert
            trystOrdersDao.cancelTryst(uid,id,uuid,CancelEnum.TRYST.getCode(),CancelEnum.DOWN.getCode(),CancelEnum.DOWN.getValue(),today,date,1,reason);
            //修改订单状态
            trystOrdersDao.calcelTrystOrders(uuid,PayTypeEnum.AFTER_CANCEL_TRYST.toCode(),PayTypeEnum.AFTER_CANCEL_TRYST.getValue());

            //删除群组
            new Thread(()->{
                chatGroupAPI.deleteChatGroup(userDao.selectGroupIdByTrystId(uuid));
                userDao.deleteEaseMob(uuid);
                logger.warn("群组id={}已被群主id={}取消",userDao.selectGroupIdByTrystId(uuid),id);
            }).start();

            logger.warn("已付款订单id={}已取消",uuid);
        }

        return Response.success("订单已取消");
    }


    /*
        赴约用户 取消订单
     */
    @Override
    public Response cancelTrystUser(Integer id, String uuid, Integer payed, String reason) {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }

        User mine = userDao.findUserByUserId(id);

        //未付款
        if (payed == 0){
            snatchUserTemplate.delete(String.format(SNATCH_USER_TRYST,uuid),id);

        //已付款
        }else if (payed == 1){
            TrystOrders trystOrders = trystOrdersDao.findTrystOrdersByUid(uuid);
            String afterTime = TimeUtil.afterTime(TimeUtil.currentTime(),1,0);

            if(TimeUtil.compareTime(afterTime,trystOrders.getTime())>0&&TimeUtil.compareTime(trystOrders.getTime(),TimeUtil.currentTime())>0){
                logger.warn("订单开始前一个小时不可取消订单");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单开始前一个小时不可取消订单");
            }

            //系统通知给发单方
            User demand = userDao.findUserByUserId(trystOrders.getUserId());
            long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,demand.getId()));
            String content = String.format(MSG_CONTENT_TRYST,id,MsgTypeEnum.CANCEL_SERVICE_PAY_TRYST.getCode(),mine.getNickname(),uuid,TimeUtil.currentTime());
            msgValueTemplate.add(String.format(USER_MSG_TRYST,demand.getId()),content ,size+1);

            String uid = UserUtil.generateUuid();
            String date = TimeUtil.afterTime(TimeUtil.currentTime(),2,0);
            String today = TimeUtil.today();

            //删除反馈表 tryst_receive
            trystOrdersDao.logicDelectReceiveByUserId(id,uuid);


            String groupId = userDao.selectGroupIdByTrystId(uuid);

            //如果最后一人只剩发单方，则修改订单状态
            if (userDao.selectUsersFromReceiveByTrystId(uuid).size() == 0){
                trystOrdersDao.calcelTrystOrders(uuid,PayTypeEnum.AFTER_CANCEL_TRYST.toCode(),PayTypeEnum.AFTER_CANCEL_TRYST.getValue());

                logger.warn("由于最后一位赴约方取消，订单id={}已被取消",uuid);

                //删除群组
                new Thread(()->{
                    chatGroupAPI.deleteChatGroup(groupId);
                    userDao.deleteEaseMob(uuid);
                }).start();
                logger.warn("由于最后一位赴约方取消，群组id={}已被取消",groupId);

            }else {
                //从群组中移除
                chatGroupAPI.removeSingleUserFromChatGroup(groupId,mine.getPhoneNum());
                logger.warn("用户id={}已从群组id={}中移除",id,groupId);
            }

            //修改订单表的 person_count - 1
            trystOrdersDao.reduceTrystPersonCount(uuid);

            //增加tryst_cancel数据
            trystOrdersDao.cancelTryst(uid,id,uuid,CancelEnum.USER.getCode(),CancelEnum.DOWN.getCode(),CancelEnum.DOWN.getValue(),today,date,1,reason);

            //退回一份tryst money给发单方
            trystOrdersDao.upMoney(trystOrders.getUserId(),trystOrders.getMoney());
            logger.warn("发单用户id={}已退还，金额为{}",trystOrders.getUserId(),trystOrders.getMoney());

        }

        return Response.success("取消成功");
    }

    /**
     * 可抢单列表
     * @param attribute
     * @return
     */
    @Override
    public Response grabOrders(Integer attribute, Double longitude, Double latitude) {
        if (attribute == null || longitude == null || latitude == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"参数为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("payCode", PayTypeEnum.TRYST_SNATCH_CONFIRM.toCode());
        //按距离由近到远排序
        List<DistancePo> distancePos = trystOrdersDao.selectDistanceAndIdByAsc(params);
        List<Map> res = new ArrayList<>();
        for (DistancePo distancePo : distancePos){
            Map<String, Object> map = new HashMap<>();
            TrystOrders trystOrders = new TrystOrders();
            trystOrders.setId(distancePo.getId());
            map.put("trystOrders",trystOrdersDao.selectTrystOrders(trystOrders));
            map.put("distance",distancePo.getDistance());
            res.add(map);
        }
        return Response.success(res);
    }

    @Override
    public Response choiceSnatchUser(Integer id, String uuid) throws Exception {
        if (null==id|| StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<SnatchUser> listUser =  snatchUserTemplate.values(String.format(SNATCH_USER_TRYST,uuid));
//        List<User> list =listUser.subList(0,2);
        return Response.success(listUser);
    }



    @Override
    public Response noticeSize(Integer id, String uuid) {
        if (null == id || StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<NearBodyResponse[]> nearbyBodyList = nearBodyTemplate.values(String.format(NEARBY_ID_BODY_TRYST,String.valueOf(uuid)));
        List<Coupons> coupons = trystOrdersDao.findMyCoupons(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("size",nearbyBodyList.size());
        jsonObject.put("coupons",coupons);
        return Response.success(nearbyBodyList.size());
    }

    @Override
    public Response myTrystOrders(Integer attribute, Integer pageNum) {
        return null;
    }

    private static double getDistance(double lat1, double lng1, double lat2, double lng2){
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        double EARTH_RADIUS = 6371.393;
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
