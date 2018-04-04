package com.wuai.company.order.dao;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.order.entity.Labels;
import com.wuai.company.order.entity.OrdersReceive;
import com.wuai.company.order.entity.response.OrdersDataResponse;
import com.wuai.company.order.entity.response.OrdersResponse;
import com.wuai.company.order.entity.response.OrdersUserResponse;
import com.wuai.company.order.service.OrdersService;
import com.wuai.company.util.Response;
//import com.wuai.company.entity.User;

import java.util.List;

/**
 * 订单的dao层
 * Created by Ness on 2017/5/25.
 */
public interface OrdersDao {

    /**
     * 根据Uuid查询订单信息
     *
     * @param uuid 订单的id
     * @return
     */
    Orders findOrdersOneByUuid(String uuid);


    /**
     * 根据用户Id查询所有隶属于该用户的所用的订单
     *
     * @param userId    用户的Id
     * @param orderType 订单的类型
     * @return
     */
    List<Orders> findOrdersByUserId(Integer userId,  Integer orderType);


    /**
     * 保存用户订单信息
     *
     * @param orders
     */
    void saveOrders(Orders orders);


    /**
     * 更新用户的订单信息
     *
     * @param orders
     */
    void updateOrdersOneByUuid(Orders orders);

    /**
     * 查询订单信息
     * @return
     */
    List<OrdersResponse> findOtherOrders(String scenes, Integer perhaps, Integer uid, Integer pageNum);

    List<OrdersResponse> findMyOrder(String scenes,Integer perhaps,Integer uid,Integer selTimeType);

    /**
     * 根据性别返回个性标签
     * @param
     * @return
     */
    List<Labels> findLabel();

    /**
     * 发布邀约/应约
//     * @param invitationRequest
     */
    void addInvitation(Double money,String uuid,Integer userId,String startTime,String place,
                       Integer selTimeType,Double orderPeriod,
                       Integer personCount,
                       Double gratefulFree,String label,Integer perhaps,
                       String scenes,Double longitude,Double latitude,Integer payType,
                       String version,Long startTimeStamp,String address);
//    void addInvitation(InvitationRequest invitationRequest);

    /**
     * 修改邀约/应约
     * @return
     */
    void updateInvitation(Double money,String uid,Integer userId,String startTime,String place, Integer selTimeType,Double orderPeriod,
                          Integer personCount, Double gratefulFree,String label,Integer perhaps,
                          String scenes,Double longitude,Double latitude,Integer payType,
                          String version,Long cycLong,String address);

    List<Maps> invitationFindPlace(Double longitude,Double latitude);

    //完成订单
    void updateOrdersByUid(String uid,String orderType);

    void invitationDeleted(String uid,Integer type);

    List<Orders> findOneByUserId(Integer userId);

    //接收订单
    void invitationReceive(String uid,String userId);
    //接收订单
    void addInvitationReceive(String ordersId, Integer userId);
    //根据用户id和订单 查询订单
    OrdersReceive findInvitationReceive(String uid, Integer userId);
    //根据订单id获取所有响应的人
    List<User> findReceives(String uid);
    //根据订单 查询订单
    List<OrdersReceive> findOrdersReceive(String uid);

    void deletedReceive(String uuid);

    void ordersStart(String uuid,Integer payType);

    List<OrdersUserResponse> findByUuid(String ordersId);

    List<OrdersDataResponse> findAllOfMyOrders(Integer uid,Integer pageNum);

    void calculation(String uid, Double money);

    OrdersDataResponse findDetailedData(String uid);


    List<Maps> findMaps(String maps,Double longitude,Double latitude,Integer pageNum,String scene);
    Maps findMap(String maps);


    List<Orders> findOrderByUserId(Integer id, Integer pageNum);

    void addCycOrders(String uuid, Integer userId, String startTime, String place, Integer selTimeType, Double orderPeriod,
                      String label, Integer perhaps, String scenes, Long cycLong,String address);

    Orders findCycOrders(String uuid);

    List<Orders> findAllOfMyReceive(Integer userId);

    List<Orders> findMyPeriodOrder(String value,  Integer uid,Integer perhaps, int selTimeType);


    List<Orders> findMyOrdersByUserId(Integer id);

    void startUpCycleOrders(String uuid);

    void stopCycleOrders(Integer userId);

    List<Orders> findAllCycleOrders(Integer userId,Integer selTimeType);

    OrdersResponse findOrdersByOrdersId(String ordersId);

    void updateInvitationOnTheWait(String uid,Integer type);

    void updateDifferenceMoney(Double countMoney, String uid, Integer userId, String startTime, String place,
                               Integer selTimeType, Double orderPeriod, Integer personCount, Double gratefulFree, String label,
                               Integer perhaps, String value, Double longitude, Double latitude, int i, String version,Long startLong,String address);

    Orders findStartTimeLimitOne(Integer userId, Integer selTimeType,String scnes);

    Orders findReceiveTimeLimitOne(Integer userId, Integer selTimeType,Integer payType,Integer perhaps,String scene);


    List<OrdersReceive> findOrdersReceiveByUserId(Integer id,String scenes);

    String getSysParameter(String key);

    List<Orders> findOrdersByUserIdPerhaps(Integer userId, Integer code);

    void updateCycTime(String uuid, Long cycLong);
    //查找我所有的订单 无论邀约或者应约
    List<Orders> findAllMyOrders(Integer userId);

    void addAppraise(Integer userId,Integer id, String uuid,String ordersId, Integer star, String content);

    Orders findOrders(String uid);

    List<Appraise> findAllAppraiseByUid(Integer id);

    List<Orders> findStartTimeLimitTen(Integer userId2, Integer code, String scenes);

    List<Orders> findOrdersByUserIdDemand(Integer userId2);

    List<UndoneOrders> findAllUndoneOrders(Integer id, Integer pageNum,String value);

    List<Orders> findUndoneOrdersLimitPage(Integer id, Integer pageNum, String value, Integer length,Integer perhaps);

    void deletedReceiver(String ordersId, Integer userId);

    void arrivedPlace(Integer id, String ordersId, Double longitude, Double latitude);

    List<CommonTalkResponse> commonTalk(Integer code);

    void serverArrived(String ordersId, Integer userId, Boolean aTrue);

    void serviceAllArrived(String uid, Boolean aTrue);

    ServiceArrivedPlaceResponse findServiceArrived(Integer id, String ordersId);

    List<ServiceArrivedPlaceResponse> findAllServiceArrived(String uid);

    Orders findOrdersByStartTimeStemp(Integer userId2, String startTimeStamp);

    Orders findLastestOne(Integer uid, Integer code, String value);

    void deletedOrders(String uuid);

    List<ActiveResponse> activeList(Integer pageNum);

    void updatePayType(String uid, Integer type);

    List<CoordinateResponse> nearbyBody(Integer id, Integer pageNum);

    NearbyBody nearbyBodyById(Integer id);

    void addLongitudeAndLatitude(String uuid, LongitudeAndLatitudeRequest request, Integer id);

    NearbyBody findNearbyByUserId(Integer id);

    void updateLongitudeAndLatitude(Integer id, LongitudeAndLatitudeRequest request,String icon);

    void balanceRecharge(Integer id, Double money);

    List<OrdersResponse> findOrdersByLoLa(Integer id, Double latitude, Double longitude,Integer pageNum);

    void createOrdersEachOther(String uuid, Integer id, String startTime, String place, Double orderPeriod, Double gratefulFree, String scenes, Double money, Double latitude, Double longitude, String address, Integer userId);

    OrdersAloneResponse findAloneOrders(String uuid);

    List<OrdersAloneResponse> findAllAloneOrders(Integer userId);

    void acceptAloneOrder(Integer id, String orderId);

}
