package com.wuai.company.order.util;

import com.wuai.company.entity.NearbyBody;
import com.wuai.company.entity.Response.GaoDeResponse;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.TrystOrders;
import com.wuai.company.entity.User;
import com.wuai.company.enums.RpcAllowMsgEnum;
import com.wuai.company.enums.ServerHandlerTypeEnum;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.rpc.mobile.ServerHandler;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.util.Arith;
import com.wuai.company.util.DistanceTools;
import com.wuai.company.util.LocationUtils;
import com.wuai.company.util.UserUtil;
import com.wuai.company.util.comon.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by hyf on 2017/10/18.
 */
@Component
public class NearbyUtil{
    @Resource
    private HashOperations<String, String, NearbyBody[]> nearbyTemplate;
    @Resource
    private HashOperations<String, String, NearBodyResponse[]> nearBodyTemplate;
    @Resource
    private HashOperations<String, String, NearbyBody> totalNearbyTemplate;
    @Resource
    private HashOperations<String, String, NearBodyResponse> totalNearBodyTemplate;
    @Resource
    private HashOperations<String, String, String> orderHashRedisTemplate;

    @Autowired
    private TrystOrdersDao trystOrdersDao;

    Logger logger = LoggerFactory.getLogger(NearbyBody.class);


    private final String NEARBY_TOTAL_BODY="nearby:total:body";//用户id
    private final String NEARBY_TOTAL_BODY_DATE="nearby:total:body:%s";//用户id
    private final String NEARBY_TOTAL_BODY_ADDRESS="nearby:body:%s";//地区
    private final String NEARBY_TOTAL_CITY_DISTRICT="nearby:total:%s:%s";//地区
    private final String NEARBY_TOTAL_CITY="nearby:total:%s";//地区
    private final String NEARBY_TOTAL_BODY_GENDER="nearby:total:body:%s";//性别
    private final String NEARBY_TOTAL_BODY_DATE_GENDER="nearby:total:body:%s:%s";//当前日期--性别
    private final String NEARBY_ID_BODY_GENDER="nearby:%s:body:%s";//用户id-性别
    private final String NEARBY_ID_BODY="nearby:%s:body";//用户id
    private final String NEARBY_ID_BODY_TRYST="nearby:%s:body:%s";//用户id--约会id
    private final String NEARBY_ID_TRYST="nearby:%s";//用户id
    private final String NEARBY_ID_BODY_DATE_GENDER="nearby:%s:body:%s:%s";//用户id--当前日期--性别


    /**
     * 添加到今日总的附近的人中
     */
    public void addTotalNearbyBody(NearBodyResponse nearbyBody){
        Thread tr1 = new Thread(() -> {
            String date = TimeUtil.today();
            //添加到今日总的附近的人--日期时间
//                totalNearBodyTemplate.put(String.format(NEARBY_TOTAL_BODY_DATE,date),String.valueOf(nearbyBody.getId()),nearbyBody);
            logger.info("添加经纬度");
            GaoDeResponse gaoDeResponse = LocationUtils.GaoDeAddress(nearbyBody.getLongitude(),nearbyBody.getLatitude());
            String city = gaoDeResponse.getRegeocode().getAddressComponent().getCity();
            String district = gaoDeResponse.getRegeocode().getAddressComponent().getDistrict();
            if ("[]".equals(city)){
                logger.info("直辖市 添加");
                city = gaoDeResponse.getRegeocode().getAddressComponent().getProvince();
            }
            totalNearBodyTemplate.put(String.format(NEARBY_TOTAL_CITY_DISTRICT,city,district), String.valueOf(nearbyBody.getId()), nearbyBody);
            totalNearBodyTemplate.put(String.format(NEARBY_TOTAL_CITY,city), String.valueOf(nearbyBody.getId()), nearbyBody);

            //添加到今日总的附近的人 --日期时间--男女分区
//                totalNearBodyTemplate.put(String.format(NEARBY_TOTAL_BODY_DATE_GENDER,date,nearbyBody.getGender()),String.valueOf(nearbyBody.getId()),nearbyBody);
        });
        tr1.start();
    }
    /**
     * 删除今日总的附近的人
     */
    public void deleteTotalNearbyBody(NearBodyResponse nearbyBody){
        String date = TimeUtil.today();
        //删除今日总的附近的人--日期时间
        totalNearBodyTemplate.delete(String.format(NEARBY_TOTAL_BODY_DATE,date),String.valueOf(nearbyBody.getId()));
        //删除今日总的附近的人 --日期时间--男女分区
        totalNearBodyTemplate.delete(String.format(NEARBY_TOTAL_BODY_DATE_GENDER,date,nearbyBody.getGender()),String.valueOf(nearbyBody.getId()));
    }

    /**
     * 查询今日我的订单附近的人
     * @param trystOrders 约吧订单
     * @param gender 性别 0-男 1-女

     * @return
     */
    public List<NearBodyResponse> findTodayTotalNearBodies(TrystOrders trystOrders,Integer gender){
        // TODO: 2018/1/22 距离限制 
        Double  nearbyBodyDistance = 20.0;
        String date = TimeUtil.today();
        List<NearBodyResponse> nearBodyResponses = new ArrayList<NearBodyResponse>();
        //根据用户积分等级 设置匹配人数上限
        User user = trystOrdersDao.findUserById(trystOrders.getUserId());
        //当性别为null的时候
        Set<String> sets;
        if (gender==null){
            //今日总的我的订单的人
            sets = totalNearBodyTemplate.keys(String.format(NEARBY_TOTAL_BODY_DATE,date));
        }else {
            //今日总的我的订单的人 分性别
            sets = totalNearBodyTemplate.keys(String.format(NEARBY_TOTAL_BODY_DATE_GENDER,date,gender));
        }
        nearBodyResponses = totalNearBoySets(sets,gender,date,trystOrders,nearbyBodyDistance);
        // TODO: 2018/1/22 人数若少于该用户的积分等级 所拥有的匹配人数则 扩大距离范围 扩大范围的次数若超出则停止
        Integer circulate=0;
        while (nearBodyResponses.size()<3&&circulate<=2){
            circulate++;
            nearbyBodyDistance= Arith.multiplys(2,nearbyBodyDistance,2);
            nearBodyResponses= totalNearBoySets(sets,gender,date,trystOrders,nearbyBodyDistance);
        }

        //消息推送
//        noticeUser(nearBodyResponses);
        //根据距离进行排序
        ComparatorNearBody comparatorNearBody = new ComparatorNearBody();
        Collections.sort(nearBodyResponses,comparatorNearBody);
        //添加到 今日我的订单匹配
        NearBodyResponse[] nearbyBodies = nearBodyResponses.toArray(new NearBodyResponse[nearBodyResponses.size()]);
        nearBodyTemplate.put(String.format(NEARBY_ID_BODY_TRYST,trystOrders.getUserId(),trystOrders.getUuid()),String.valueOf(trystOrders.getUuid()),nearbyBodies);
        return nearBodyResponses;
    }

    public void noticeUser(List<NearBodyResponse> nearBodyResponses){
        Thread tr1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i =0;i<nearBodyResponses.size();i++){
                    User user = trystOrdersDao.findUserById(nearBodyResponses.get(i).getId());
//                    if (ServerHandler.sendTrystNotify(RpcAllowMsgEnum.NOTIFY,user)==false){
                        logger.info("应约--用户userId={}不在线 发送个推",nearBodyResponses.get(i).getId());
                        String content="content";
    //                    String content = UserUtil.jsonPare("userId,"+user.getId(),"icon,"+user.getIcon(),"ordersId,"+orders.getUuid(),"type,"+ ServerHandlerTypeEnum.ACCEPT.getType(),"startTime,"+orders.getStartTime(),"place,"+orders.getPlace());

                        Push push = new Push();
                        push.setDeviceNum(user.getCid());
                        push.setSendDeviceType(user.getType());
                        push.setSendTopic("您有一个附近的订单");
                        push.setSendContent(content);
    //                    push.setSendContent(String.valueOf(user2.getId()));
                        PushUtils.userPush.getInstance().sendPush(push);
//                    }
                }
            }
        });
        tr1.start();
    }

    /**
     * 筛选出合适的我的订单相符合的附近的人
     * @param sets
     * @param gender
     * @param date
     * @param trystOrders
     * @param nearbyBodyDistance
     * @return
     */
    public List<NearBodyResponse> totalNearBoySets(Set<String> sets,Integer gender,String date,TrystOrders trystOrders,Double nearbyBodyDistance){
        List<NearBodyResponse> nearBodyResponses = new ArrayList<NearBodyResponse>();
        if (gender==null){
                for (String str:sets){
                    NearBodyResponse nearBodyResponse = totalNearBodyTemplate.get(String.format(NEARBY_TOTAL_BODY_DATE,date),str);
                    //check 是否符合 距离小于10km
                    Double kmDistance = DistanceTools.getKmByPoint(trystOrders.getLatitude(),trystOrders.getLongitude(),nearBodyResponse.getLatitude(),nearBodyResponse.getLongitude());
                    //排除自己
                    if (kmDistance<=nearbyBodyDistance&&!nearBodyResponse.getId().equals(trystOrders.getUserId())){
                        //设置与我的距离
                        nearBodyResponse.setDistance(kmDistance);
                        // 添加到我的附近的人
                        nearBodyResponses.add(nearBodyResponse);
                    }
                    //认证用户的匹配到的订单
//                    orderHashRedisTemplate.put(String.format(NEARBY_ID_TRYST,nearBodyResponse.getId()), String.valueOf(nearBodyResponse.getId()),trystOrders.getUuid());

                }
         return nearBodyResponses;
        }else {
            for (String str:sets){
                NearBodyResponse nearBodyResponse = totalNearBodyTemplate.get(String.format(NEARBY_TOTAL_BODY_DATE_GENDER,date,gender),str);
                //check 是否符合 距离小于10km
                Double kmDistance = DistanceTools.getKmByPoint(trystOrders.getLatitude(),trystOrders.getLongitude(),nearBodyResponse.getLatitude(),nearBodyResponse.getLongitude());
                //排除自己
                if (kmDistance<=nearbyBodyDistance&&!nearBodyResponse.getId().equals(trystOrders.getUserId())){
                    //设置与我的距离
                    nearBodyResponse.setDistance(kmDistance);
                    // 添加到我的附近的人
                    nearBodyResponses.add(nearBodyResponse);
                }
                //认证用户的匹配到的订单
//                orderHashRedisTemplate.put(String.format(NEARBY_ID_TRYST,nearBodyResponse.getId()), String.valueOf(nearBodyResponse.getId()),trystOrders.getUuid());
            }
            return nearBodyResponses;
        }
    }

    /**
     * 添加附近的人
     * 匹配限制条件 距离20km，性别
     * 若距离不够则扩充
     * @param nearbyBody 我的附近的人的参数
     * @param nearbyBodyDistance 距离限制
     */
    public void addNearbyBody(NearbyBody nearbyBody,Double  nearbyBodyDistance){
        logger.info("删除我的附近的人userId={}",nearbyBody.getUserId());
        Set<String> idSets = totalNearbyTemplate.keys(String.format(NEARBY_TOTAL_BODY));
        //添加到总的附近的人
        totalNearbyTemplate.put(String.format(NEARBY_TOTAL_BODY),String.valueOf(nearbyBody.getUserId()),nearbyBody);
//        List<NearbyBody[]> list = nearbyTemplate.values(String.format(NEARBY_ID_BODY,nearbyBody.getUserId()));
        Set<String> key = nearbyTemplate.keys(String.format(NEARBY_ID_BODY,nearbyBody.getUserId()));
        if (key.size()>0){
            for (String str:key){
                nearbyTemplate.delete(String.format(NEARBY_ID_BODY,nearbyBody.getUserId()),str);
            }
        }

        logger.info("添加我的附近的人userId={}",nearbyBody.getUserId());
        List<NearbyBody> nearbyBodyList = new ArrayList<NearbyBody>();
        for (String str:idSets){
            NearbyBody totalNearbyBody =  totalNearbyTemplate.get(String.format(NEARBY_TOTAL_BODY),str);
            //check 是否符合 距离小于10km
            Double kmDistance = DistanceTools.getKmByPoint(nearbyBody.getLatitude(),nearbyBody.getLongitude(),totalNearbyBody.getLatitude(),totalNearbyBody.getLongitude());
            //排除自己
            if (kmDistance<=nearbyBodyDistance&&!totalNearbyBody.getUserId().equals(nearbyBody.getUserId())){
                //设置与我的距离
                totalNearbyBody.setDistance(kmDistance);
                // 添加到我的附近的人
                nearbyBodyList.add(totalNearbyBody);
            }
        }
        ComparatorNearby comparatorNearby = new ComparatorNearby();
        Collections.sort(nearbyBodyList,comparatorNearby);
        //添加到我的附近的人
        NearbyBody[] nearbyBodies = nearbyBodyList.toArray(new NearbyBody[nearbyBodyList.size()]);
        nearbyTemplate.put(String.format(NEARBY_ID_BODY,nearbyBody.getUserId()),String.valueOf(nearbyBody.getUserId()),nearbyBodies);
    }


}
