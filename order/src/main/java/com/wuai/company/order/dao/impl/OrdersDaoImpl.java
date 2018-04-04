package com.wuai.company.order.dao.impl;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.enums.InvitationTypeEnum;
import com.wuai.company.order.dao.OrdersDao;
import com.wuai.company.order.entity.Labels;
import com.wuai.company.order.entity.OrdersReceive;
import com.wuai.company.order.entity.response.OrdersDataResponse;
import com.wuai.company.order.entity.response.OrdersResponse;
import com.wuai.company.order.entity.response.OrdersUserResponse;
import com.wuai.company.order.mapper.LabelMapper;
import com.wuai.company.order.mapper.MapsMapper;
import com.wuai.company.order.mapper.OrdersMapper;
import com.wuai.company.order.mapper.OrdersReceiveMapper;
import com.wuai.company.order.service.OrdersService;
import com.wuai.company.util.Response;
import com.wuai.company.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

//import com.wuai.company.entity.User;

/**
 * 订单的dao具体实现层
 * Created by Ness on 2017/5/25.
 */
@Repository
public class OrdersDaoImpl implements OrdersDao {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private MapsMapper mapsMapper;
    @Autowired
    private OrdersReceiveMapper ordersReceiveMapper;

    private Logger logger = LoggerFactory.getLogger(OrdersDaoImpl.class);


    /**
     * 根据Uuid查询订单信息
     *
     * @param uuid 订单的id
     * @return
     */
    @Override
    public Orders findOrdersOneByUuid(String uuid) {


//        orderHashRedisTemplate.put("1", "1", "1");

        if (StringUtils.isEmpty(uuid)) {
            logger.warn("根据Uuid查询订单信息 所传id为空");
            return null;
        }

        if (logger.isInfoEnabled()) {
            logger.info("id={}开始查询订单信息", uuid);
        }

        return ordersMapper.findOrdersOneByUuid(uuid);
    }


    /**
     * 根据用户Id查询所有隶属于该用户的所用的订单
     *
     * @param userId    用户的Id
     * @param orderType 订单的类型
     * @return
     */
    @Override
    public List<Orders> findOrdersByUserId(Integer userId, Integer orderType) {


        if (userId==null || orderType == null) {
            logger.warn("根据用户Id查询所有隶属于该用户的所用的订单 所传参数为空 userId={} orderType={}", userId, orderType);
            return null;
        }

        if (logger.isInfoEnabled()) {
            logger.info("userId={}开始查询所有订单信息", userId);
        }

        return ordersMapper.findOrdersByUserId(userId, orderType);
    }

    /**
     * 保存用户订单信息
     *
     * @param orders
     */
    @Override
    public void saveOrders(Orders orders) {
        if (orders == null || orders.getUserId()==null) {
            logger.warn("保存用户订单信息 所传参数为空 ");
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("userId={}开始保存订单信息", orders.getUserId());
        }

        ordersMapper.saveOrders(orders);


    }

    /**
     * 更新用户的订单信息
     *
     * @param orders
     */
    @Override
    public void updateOrdersOneByUuid(Orders orders) {


        if (orders == null || orders.getUserId()==null || StringUtils.isEmpty(orders.getUuid())) {
            logger.warn("保存用户订单信息 所传参数为空 ");
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("userId={}开始更新订单={}信息", orders.getUserId(), orders.getUuid());
        }

        ordersMapper.updateOrdersOneByUuid(orders);


    }

    /**
     * 查询订单信息
     * @return
     */
    @Override
    public List<OrdersResponse> findOtherOrders(String scenes,Integer perhaps,Integer uid,Integer pageNum) {
        if (Objects.equals(perhaps, InvitationTypeEnum.SERVICE.getCode())){
            perhaps=InvitationTypeEnum.DEMAND.getCode();
        }else if (Objects.equals(perhaps, InvitationTypeEnum.DEMAND.getCode())){
            perhaps=InvitationTypeEnum.SERVICE.getCode();
        }
        return ordersMapper.findOtherOrders(scenes,perhaps,uid,pageNum);
    }
    /**
     * 查询个人订单信息
     * @return
     */
    @Override
    public List<OrdersResponse> findMyOrder(String scenes, Integer perhaps, Integer uid,Integer selTimeType) {


        return ordersMapper.findMyOrder(scenes,uid,perhaps,selTimeType);
    }

    /**
     * 根据性别返回个性标签
     * @return
     */
    @Override
    public List<Labels> findLabel() {

        return labelMapper.findLabel();

    }
    /**
     * 发布邀约/应约
     */
    @Override
    public void addInvitation(Double money,String uuid,Integer userId, String startTime, String place, Integer selTimeType,
                              Double orderPeriod, Integer personCount, Double gratefulFree, String label, Integer perhaps,
                              String scenes,Double longitude,Double latitude,Integer payType,String version,Long startTimeStamp,String address) {
            String userInvitation="";

        ordersMapper.addInvitation(money,uuid,userId, startTime, place, selTimeType,
                orderPeriod, personCount, gratefulFree, label, perhaps,
                scenes,userInvitation,longitude,latitude,payType,version,startTimeStamp,address);
    }



    /**
     * 修改邀约/应约
     * @return
     */
    @Override
    public void updateInvitation(Double money,String uid,Integer userId,String startTime,String place, Integer selTimeType,Double orderPeriod,
                                 Integer personCount, Double gratefulFree,String label,Integer perhaps, String scenes,
                                 Double longitude,Double latitude,Integer payType,String version,Long cycLong,String address) {
        ordersMapper.updateInvitation(money,uid,userId, startTime, place, selTimeType, orderPeriod,
                personCount, gratefulFree, label, perhaps, scenes,longitude,latitude,payType,version,cycLong,address);
    }


    @Override
    public List<Maps> invitationFindPlace(Double longitude,Double latitude) {
        return mapsMapper.invitationFindPlace(longitude, latitude);
//        return null;
    }


    /**
     * 完成订单
     * @param uid
     */
    @Override
    public void updateOrdersByUid(String uid,String orderType) {
        ordersMapper.updateOrdersByUid(uid,orderType);
    }

    @Override
    public void invitationDeleted(String uid,Integer type) {
        ordersMapper.invitationDeleted(uid,type);
    }

    //根据用户userId查 订单
    @Override
    public List<Orders> findOneByUserId(Integer userId) {
        return ordersMapper.findOneByUserId(userId);
    }

    //接收订单
    @Override
    public void invitationReceive(String uid,String userId) {
        ordersMapper.invitationReceive(uid,userId);
    }

    @Override
    public void addInvitationReceive(String ordersId, Integer userId) {
        String uuid = UserUtil.generateUuid();
        ordersReceiveMapper.addInvitationReceive(uuid,ordersId,userId);
    }

    @Override
    public OrdersReceive findInvitationReceive(String uid, Integer userId) {
        return ordersReceiveMapper.findInvitationReceive(uid,userId);
    }
    //根据用户id和订单 查询订单
    @Override
    public List<User> findReceives(String uid) {
        return ordersMapper.findReceives(uid);
    }

    @Override
    public  List<OrdersReceive> findOrdersReceive(String uid) {
        return ordersReceiveMapper.findOrdersReceive(uid);
    }

    @Override
    public void deletedReceive(String uuid) {
        ordersReceiveMapper.deletedReceive(uuid);
    }

    @Override
    public void ordersStart(String uuid,Integer payType) {
        ordersMapper.ordersStart(uuid,payType);
    }

    @Override
    public List<OrdersUserResponse> findByUuid(String ordersId) {
        return ordersMapper.findByUuid(ordersId);
    }

    @Override
    public List<OrdersDataResponse> findAllOfMyOrders(Integer uid,Integer pageNum) {
        return ordersMapper.findAllOfMyOrders(uid,pageNum);
    }

    @Override
    public void calculation(String uid, Double money) {
        ordersMapper.calculation(uid,money);
    }

    @Override
    public OrdersDataResponse findDetailedData(String uid) {
        return ordersMapper.findDetailedData(uid);
    }

    @Override
    public List<Maps> findMaps(String maps,Double longitude,Double latitude,Integer pageNum,String scene) {
        return mapsMapper.findMaps(maps,longitude,latitude,pageNum,scene);
    }

    @Override
    public Maps findMap(String maps) {
         List<Maps> mapsList = mapsMapper.findMap(maps);
//         if (mapsList.size()>1){
//             mapsList.get(0);
//         }
        return mapsList.get(0);
    }


    @Override
    public List<Orders> findOrderByUserId(Integer id, Integer pageNum) {
        return ordersMapper.findOrderByUserId(id,pageNum);
    }

    @Override
    public void addCycOrders(String uuid, Integer userId, String startTime, String place,
                             Integer selTimeType, Double orderPeriod,
                             String label, Integer perhaps, String scenes, Long cycLong,String address) {
        ordersMapper.addCycOrders(uuid,userId,startTime,place,selTimeType,orderPeriod,label,perhaps,scenes,cycLong,address);
    }

    @Override
    public Orders findCycOrders(String uuid) {
        return ordersMapper.findCycOrders(uuid);
    }

    @Override
    public List<Orders> findAllOfMyReceive(Integer userId) {
        return ordersMapper.findAllOyMyReceive(userId);
    }

    @Override
    public List<Orders> findMyPeriodOrder(String value, Integer uid, Integer perhaps, int selTimeType) {
        return ordersMapper.findMyPeriodOrder(value,uid,perhaps,selTimeType);
    }

    @Override
    public List<Orders> findMyOrdersByUserId(Integer id) {
        return ordersMapper.findMYOrdersByUserId(id);
    }

    @Override
    public void startUpCycleOrders(String uuid) {
        ordersMapper.startUpCycleOrders(uuid);
    }

    @Override
    public void stopCycleOrders(Integer userId) {
        ordersMapper.stopCycleOrders(userId);
    }

    @Override
    public List<Orders> findAllCycleOrders(Integer userId,Integer selTimeType) {
        return ordersMapper.findAllCycleOrders(userId,selTimeType);
    }

    @Override
    public OrdersResponse findOrdersByOrdersId(String ordersId) {
        return ordersMapper.findOrdersByOrdersId(ordersId);
    }

    @Override
    public void updateInvitationOnTheWait(String uid,Integer type) {
        ordersMapper.updateInvitationOnTheWait(uid,type);
    }

    @Override
    public void updateDifferenceMoney(Double countMoney, String uid, Integer userId, String startTime, String place, Integer selTimeType,
                                      Double orderPeriod, Integer personCount, Double gratefulFree, String label, Integer perhaps, String scenes,
                                      Double longitude, Double latitude, int payType, String version,Long startLong,String address) {
        ordersMapper.updateDifferenceMoney(countMoney,uid,userId, startTime, place, selTimeType, orderPeriod,
                personCount, gratefulFree, label, perhaps, scenes,longitude,latitude,payType,version, startLong, address);
    }

    @Override
    public Orders findStartTimeLimitOne(Integer userId, Integer selTimeType,String scnes) {
        return ordersMapper.findStartTimeLimitOne( userId,  selTimeType,scnes);
    }

    @Override
    public Orders findReceiveTimeLimitOne(Integer userId, Integer selTimeType,Integer payType,Integer perhaps,String scene) {
//        return ordersMapper.findReceiveTimeLimitOne( userId,  selTimeType);
        return ordersMapper.findReceiveTimeLimitOne( userId,  selTimeType,payType,perhaps,scene);
    }

    @Override
    public List<OrdersReceive> findOrdersReceiveByUserId(Integer id,String scenes) {
        return ordersReceiveMapper.findOrdersReceiveByUserId(id,scenes);
    }

    @Override
    public String getSysParameter(String key) {
        return ordersMapper.getSysParameter(key);
    }

    @Override
    public List<Orders> findOrdersByUserIdPerhaps(Integer userId, Integer code) {
        return ordersMapper.findOrdersByUserIdPerhaps(userId,code);
    }

    @Override
    public void updateCycTime(String uuid, Long cycLong) {
        ordersMapper.updateCycTime(uuid,cycLong);
    }

    @Override
    public List<Orders> findAllMyOrders(Integer userId) {
        return ordersMapper.findAllMyOrders(userId);
    }

    @Override
    public void addAppraise(Integer userId,Integer id,String uuid, String ordersId, Integer star, String content) {
        ordersMapper.addAppraise(userId,id,uuid,ordersId,star,content);
    }

    @Override
    public Orders findOrders(String uid) {
        return ordersMapper.findOrders(uid);
    }

    @Override
    public List<Appraise> findAllAppraiseByUid(Integer uuid) {
        return ordersMapper.findAllAppraiseByUid(uuid);
    }

    @Override
    public List<Orders> findStartTimeLimitTen(Integer userId2, Integer code, String scenes) {
        return ordersMapper.findStartTimeLimitTen( userId2,  code,  scenes);
    }

    @Override
    public List<Orders> findOrdersByUserIdDemand(Integer userId) {
        return ordersMapper.findOrdersByUserIdDemand(userId);
    }

    @Override
    public List<UndoneOrders> findAllUndoneOrders(Integer id, Integer pageNum,String value) {
        return ordersMapper.findAllUndoneOrders(id,pageNum,value);
    }

    @Override
    public List<Orders> findUndoneOrdersLimitPage(Integer id, Integer pageNum, String scenes, Integer pageSize,Integer perhaps) {
        return ordersMapper.findUndoneOrdersLimitPage( id,  pageNum,  scenes,  pageSize ,perhaps);
    }

    @Override
    public void deletedReceiver(String ordersId, Integer userId) {
        ordersMapper.deletedReceiver( ordersId,  userId);
    }

    @Override
    public void arrivedPlace(Integer id, String ordersId, Double longitude, Double latitude) {
        ordersMapper.arrivedPlace(id,ordersId,longitude,latitude);
    }

    @Override
    public List<CommonTalkResponse> commonTalk(Integer code) {
        return ordersMapper.commonTalk(code);
    }

    @Override
    public void serverArrived(String ordersId, Integer userId, Boolean aTrue) {
        ordersMapper.serverArrived(ordersId,userId,aTrue);
    }

    @Override
    public void serviceAllArrived(String uid, Boolean aTrue) {
        ordersMapper.serviceAllArrived(uid,aTrue);
    }

    @Override
    public ServiceArrivedPlaceResponse findServiceArrived(Integer id, String ordersId) {
        return ordersMapper.findServiceArrived(id,ordersId);
    }

    @Override
    public List<ServiceArrivedPlaceResponse> findAllServiceArrived(String uid) {
        return ordersMapper.findAllServiceArrived(uid);
    }

    @Override
    public Orders findOrdersByStartTimeStemp(Integer userId, String startTimeStamp) {
        return ordersMapper.findOrdersByStartTimeStemp( userId,  startTimeStamp);
    }

    @Override
    public Orders findLastestOne(Integer uid, Integer code, String value) {
        return ordersMapper.findLastestOne( uid,  code,value);
    }

    @Override
    public void deletedOrders(String uuid) {
        ordersMapper.deletedOrders(uuid);
    }

    @Override
    public List<ActiveResponse> activeList(Integer pageNum) {
        return ordersMapper.activeList( pageNum);
    }

    @Override
    public void updatePayType(String uid, Integer type) {
        ordersMapper.updatePayType( uid,  type);
    }

    @Override
    public List<CoordinateResponse> nearbyBody(Integer id, Integer pageNum) {
        return ordersMapper.nearbyBody(id,pageNum);
    }

    @Override
    public NearbyBody nearbyBodyById(Integer id) {
        return ordersMapper.nearbyBodyById(id);
    }

    @Override
    public void addLongitudeAndLatitude(String uuid, LongitudeAndLatitudeRequest request, Integer id) {
        ordersMapper.addLongitudeAndLatitude(uuid,request,id);
    }

    @Override
    public NearbyBody findNearbyByUserId(Integer id) {
        return  ordersMapper.findNearbyByUserId(id);
    }

    @Override
    public void updateLongitudeAndLatitude(Integer id, LongitudeAndLatitudeRequest request,String icon) {
        ordersMapper.updateLongitudeAndLatitude(id,request,icon);
    }

    @Override
    public void balanceRecharge(Integer id, Double money) {
        ordersMapper.balanceRecharge(id, money);
    }

    @Override
    public List<OrdersResponse> findOrdersByLoLa(Integer id, Double latitude, Double longitude, Integer pageNum) {
        return  ordersMapper.findOrdersByLoLa(id,latitude,longitude,pageNum);
    }

    @Override
    public void createOrdersEachOther(String uuid, Integer id, String startTime, String place, Double orderPeriod, Double gratefulFree, String scenes, Double money, Double latitude, Double longitude, String address, Integer userId) {
         ordersMapper.createOrdersEachOther(uuid,id,startTime,place,orderPeriod,gratefulFree,scenes,money,latitude,longitude,address,userId);
    }

    @Override
    public OrdersAloneResponse findAloneOrders(String uuid) {
        return  ordersMapper.findAloneOrders(uuid);
    }

    @Override
    public List<OrdersAloneResponse> findAllAloneOrders(Integer userId) {
        return  ordersMapper.findAllAloneOrders(userId);
    }

    @Override
    public void acceptAloneOrder(Integer id, String orderId) {
        ordersMapper.acceptAloneOrder(id,orderId);
    }




}
