package com.wuai.company.user.util;

import com.gexin.fastjson.JSON;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.SnatchUser;
import com.wuai.company.entity.TrystOrders;
import com.wuai.company.entity.TrystReceive;
import com.wuai.company.entity.User;
import com.wuai.company.enums.MsgTypeEnum;
import com.wuai.company.enums.RpcAllowMsgEnum;
import com.wuai.company.enums.ServerHandlerTypeEnum;
import com.wuai.company.enums.VideoCheckEnum;
import com.wuai.company.rpc.mobile.ServerHandler;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.entity.Response.GeTuiRequest;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.util.UserUtil;
import com.wuai.company.util.comon.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by hyf on 2018/1/23.
 */
@Slf4j
@Component
@EnableAsync
public class NoticeUtil {

    @Resource
    private UserDao userDao;

    @Resource
    private ZSetOperations<String,String> msgValueTemplate;

    private final String USER_MSG = "%s:msg"; //用户id--信息列表
    private final String USER_TYPE_CONTEN = "%s:%s:%s"; //用户id--类型--内容

    /*
    创建订单时，个推给附件用户
     */
    public void createSendGetuiAuth(List<NearBodyResponse> nearBodyResponses, TrystOrders trystOrders){
        new Thread(() -> {
            for (NearBodyResponse nearBodyResponse : nearBodyResponses) {
                User user = userDao.findUserByUserId(nearBodyResponse.getId());
                if (user == null) continue;
                if (user.getId().equals(trystOrders.getUserId())) continue;
                if (userDao.findVideoCheck(user.getId(), VideoCheckEnum.SUCCESS_CHECK.getCode()) == null) continue;
                if (trystOrders.getSex() != 2){
                    if (!nearBodyResponse.getGender().equals(trystOrders.getSex())) continue;;
                }
                //可抢订单的详情
//                GeTuiRequest geTuiRequest = new GeTuiRequest();
//                geTuiRequest.setDemanderName(user.getNickname());
//                geTuiRequest.setDemanderHeadUrl(user.getIcon());
//                geTuiRequest.setDemanderSex(user.getGender());
//                geTuiRequest.setScenePic(userDao.selectScenePicByScene(trystOrders.getScene()));
//                geTuiRequest.setDistance(getDistance(nearBodyResponse.getLatitude(), nearBodyResponse.getLongitude(), trystOrders.getLatitude(), trystOrders.getLongitude()));
//                geTuiRequest.setSceneContent(userDao.selectSceneContentByScene(trystOrders.getScene()));
//                geTuiRequest.setTime(trystOrders.getTime());
//                geTuiRequest.setPlace(trystOrders.getPlace());
//                geTuiRequest.setPersonSex(trystOrders.getSex());
//                geTuiRequest.setPersonCount(trystOrders.getPersonCount());
//                geTuiRequest.setReward(trystOrders.getMoney());
//                String json = JSON.toJSONString(geTuiRequest);
                String content = UserUtil.jsonPare(
                        "type,"+ ServerHandlerTypeEnum.SNACTH_TRYST.getType()
                );
                push(user.getCid(), user.getType(), "您有一个附近的订单", content);
                msgValueTemplate.add(String.format(USER_MSG, nearBodyResponse.getId()), String.format(USER_TYPE_CONTEN, nearBodyResponse.getId(), MsgTypeEnum.SYS_MSG.getCode(), content), System.currentTimeMillis());
            }
         log.warn("订单id={}个推创建后，附近人推送已完成",trystOrders.getUuid());
        }).start();
    }

    /*
    需求方取消订单时，个推给已抢单用户
     */
    public void cancelCreateGetuiAuth(List<SnatchUser> snatchUserList){
        new Thread(()->{
            for (SnatchUser snatchUser : snatchUserList){
                User user = userDao.findUserByUuid(snatchUser.getUuid());
                String content = UserUtil.jsonPare(
                        "type,"+ ServerHandlerTypeEnum.SNACTH_TRYST.getType()
                );
                push(user.getCid(),user.getType(),"订单已取消",content);
            }
            log.warn("需求方取消订单时，个推给已抢单用户已完成");
        }).start();

    }

    /*
    个推通知抢单用户，该订单已生效
     */
    @Async
    public void waitTrystGetuiAuth(List<TrystReceive> trystReceiveList){
        for (TrystReceive trystReceive : trystReceiveList){
            String content = UserUtil.jsonPare(
                    "type,"+ ServerHandlerTypeEnum.WAIT_TRYST.getType(),
                    "trystId,"+ trystReceive.getTrystId()
            );
            push(userDao.selectCidById(trystReceive.getUserId()),userDao.selectTypeById(trystReceive.getUserId()),"订单已生效",content);
        }
        log.warn("订单id={}已生效，推送给赴约方");
    }

    /*
    个推给赴约方（订单已完成）
     */
    @Async
    public void completeTrystGetuiAuth(List<TrystReceive> trystReceiveList){
        for (TrystReceive trystReceive : trystReceiveList){
            String content = UserUtil.jsonPare(
                    "type,"+ ServerHandlerTypeEnum.COMPLETE_TRYST.getType(),
                    "trystId,"+ trystReceive.getTrystId()
            );
            push(userDao.selectCidById(trystReceive.getUserId()),userDao.selectTypeById(trystReceive.getUserId()),"订单已生效",content);
        }
        log.warn("订单已完成，推送给所有赴约方");
    }

    /*
    赴约方取消订单后，个推给发单方
    */
    public void cancelTrystUser(TrystOrders trystOrders,Integer serviceId, String cid,Integer type){
        new Thread(()->{
            String content = UserUtil.jsonPare(
                    "type,"+ ServerHandlerTypeEnum.CANCEL_SERVICE_TRYST.getType(),
                    "trystId,"+ trystOrders.getUuid(),
                    "serviceId,"+ serviceId
            );
            push(cid,type,"您有一位赴约方已取消订单，点击查看详情",content);
            log.warn("某用户已取消订单id={}，推送给发单方id={}",trystOrders.getUuid(),serviceId);
        }).start();
    }

    /*
    个推给所有赴约方,订单已取消
     */
    @Async
    public void cancelDemandGetuiAuth(List<TrystReceive> trystReceiveList){

        for (TrystReceive trystReceive : trystReceiveList){
            String content = UserUtil.jsonPare(
                    "type,"+ ServerHandlerTypeEnum.CANCEL_DEMAND_TRYST.getType(),
                    "trystId,"+ trystReceive.getTrystId()
            );
            push(userDao.selectCidById(trystReceive.getUserId()),userDao.selectTypeById(trystReceive.getUserId()),"订单已取消",content);
        }
        log.warn("个推给所有赴约方,订单已取消");
    }

    /*
    个推给所有赴约方，订单时间已修改
     */
    public void updateTrystGetuiAuth(List<TrystReceive> trystReceiveList){
        new Thread(()->{
            for (TrystReceive trystReceive : trystReceiveList){
                String content = UserUtil.jsonPare(
                        "type,"+ ServerHandlerTypeEnum.UPDATE_TRYST.getType(),
                        "trystId,"+ trystReceive.getTrystId()
                );
                push(userDao.selectCidById(trystReceive.getUserId()),userDao.selectTypeById(trystReceive.getUserId()),"订单已修改",content);
            }
            log.warn("个推给所有赴约方,订单已修改");
        }).start();
    }

    /*
    个推方法
     */
    private void push(String cid, Integer type, String topic, String content){
        Push push = new Push();
        push.setDeviceNum(cid);
        push.setSendDeviceType(type);
        push.setSendTopic(topic);
        push.setSendContent(content);
        PushUtils.userPush.getInstance().sendPush(push);
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

    public static void main(String[] args) {
        System.out.println(TimeUtil.currentTime());
    }
}
