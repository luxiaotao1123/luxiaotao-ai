package com.wuai.company.order.mapper;

import com.wuai.company.entity.Appraise;
import com.wuai.company.entity.NearbyBody;
import com.wuai.company.entity.Orders;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.User;
import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.order.entity.response.OrdersDataResponse;
import com.wuai.company.order.entity.response.OrdersResponse;
import com.wuai.company.order.entity.response.OrdersUserResponse;
//import com.wuai.company.entity.User;
import com.wuai.company.order.service.OrdersService;
import com.wuai.company.util.Response;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单信息的mapper层
 * Created by Ness on 2017/5/25.
 */
@Mapper
public interface OrdersMapper {
    /**
     * 根据Uuid查询订单信息
     *
     * @param uuid 订单的id
     * @return
     */
    Orders findOrdersOneByUuid(@Param("uuid") String uuid);


    /**
     * 根据用户Id查询所有隶属于该用户的所用的订单
     *
     * @param userId    用户的Id
     * @param orderType 订单的类型
     * @return
     */
    List<Orders> findOrdersByUserId(@Param("userId") Integer userId, @Param("orderType") Integer orderType);


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
     * 查询订单
     * @return
     */
    List<OrdersResponse> findOtherOrders(@Param("scenes") String scenes, @Param("perhaps")Integer perhaps, @Param("uid")Integer uid, @Param("pageNum") Integer pageNum);

    /**
     * 发布邀约/应约
     */
    void addInvitation(@Param("money")Double money, @Param("uuid") String uuid,@Param("userId") Integer userId, @Param("startTime") String startTime,
                        @Param("place") String place, @Param("selTimeType") Integer selTimeType,
                        @Param("orderPeriod") Double orderPeriod, @Param("personCount") Integer personCount,
                        @Param("gratefulFree") Double gratefulFree, @Param("label") String label,
                        @Param("perhaps") Integer perhaps, @Param("scenes") String scenes,
                       @Param("userInvitation")String userInvitation,@Param("longitude")Double longitude,
                       @Param("latitude")Double latitude,@Param("payType") Integer payType,
                       @Param("version") String version,@Param("startTimeStamp") Long startTimeStamp,@Param("address")String address);
//    void addInvitation(InvitationRequest invitationRequest);

    /**
     * 修改邀约/应约
     * @return
     */
    void updateInvitation(@Param("money")Double money,@Param("uuid") String uuid,@Param("userId") Integer userId, @Param("startTime") String startTime,
                          @Param("place") String place, @Param("selTimeType") Integer selTimeType,
                          @Param("orderPeriod") Double orderPeriod, @Param("personCount") Integer personCount,
                          @Param("gratefulFree") Double gratefulFree, @Param("label") String label,
                          @Param("perhaps") Integer perhaps, @Param("scenes") String scenes,
                          @Param("longitude")Double longitude,@Param("latitude")Double latitude,
                          @Param("payType")Integer payType,@Param("version")String version,@Param("cycLong") Long cycLong,
                          @Param("address") String address);

    List<OrdersResponse> findMyOrder(@Param("scenes") String scenes, @Param("uid")Integer uid, @Param("perhaps")Integer perhaps,@Param("selTimeType")Integer selTimeType);
    //完成订单
    void updateOrdersByUid(@Param("uid") String uid,@Param("orderType") String orderType);

    void invitationDeleted(@Param("uid") String uid,@Param("type") Integer type);

    //根据用户userId查询 出所有 订单
    List<Orders> findOneByUserId(@Param("userId")Integer userId);

    //接收订单
    void invitationReceive(@Param("uid") String uid,@Param("userId") String userId);

    //根据用户id和订单 查询订单
    List<User> findReceives(@Param("uid") String uid);

    void ordersStart(@Param("uuid")String uuid,@Param("payType")Integer payType);

    List<OrdersUserResponse> findByUuid(@Param("ordersId")String ordersId);

    List<OrdersDataResponse> findAllOfMyOrders(@Param("uid")Integer uid, @Param("pageNum")Integer pageNum);

    void calculation(@Param("uid")String uid,@Param("money") Double money);

    OrdersDataResponse findDetailedData(@Param("uid")String uid);



    List<Orders> findOrderByUserId(@Param("id")Integer id, @Param("pageNum")Integer pageNum);

    void addCycOrders(@Param("uuid")String uuid, @Param("userId")Integer userId, @Param("startTime")String startTime,
                      @Param("place")String place, @Param("selTimeType")Integer selTimeType,
                      @Param("orderPeriod")Double orderPeriod, @Param("label")String label,
                      @Param("perhaps")Integer perhaps, @Param("scenes")String scenes, @Param("cycLong") Long cycLong,@Param("address") String address);

    Orders findCycOrders(@Param("uuid")String uuid);

    List<Orders> findAllOyMyReceive(@Param("userId") Integer userId);

    List<Orders> findMyPeriodOrder(@Param("value") String value, @Param("uid")Integer uid, @Param("perhaps")Integer perhaps,@Param("selTimeType")Integer selTimeType);

    List<Orders> findMYOrdersByUserId(@Param("id")Integer id);

    void stopCycleOrders(@Param("userId")Integer userId);

    void startUpCycleOrders(@Param("uuid")String uuid);

    List<Orders> findAllCycleOrders(@Param("userId")Integer userId,@Param("selTimeType")Integer selTimeType);

    OrdersResponse findOrdersByOrdersId(@Param("ordersId")String ordersId);

    void updateInvitationOnTheWait(@Param("uid")String uid,@Param("type")Integer type);

    void updateDifferenceMoney(@Param("money")Double money,@Param("uuid") String uuid,@Param("userId") Integer userId, @Param("startTime") String startTime,
                               @Param("place") String place, @Param("selTimeType") Integer selTimeType,
                               @Param("orderPeriod") Double orderPeriod, @Param("personCount") Integer personCount,
                               @Param("gratefulFree") Double gratefulFree, @Param("label") String label,
                               @Param("perhaps") Integer perhaps, @Param("scenes") String scenes,
                               @Param("longitude")Double longitude,@Param("latitude")Double latitude,
                               @Param("payType")Integer payType,@Param("version")String version,@Param("startLong") Long startLong,
                               @Param("address")String address);

    Orders findStartTimeLimitOne(@Param("userId")Integer userId, @Param("selTimeType")Integer selTimeType,@Param("scenes") String scenes);

    Orders findReceiveTimeLimitOne(@Param("userId")Integer userId, @Param("selTimeType")Integer selTimeType,@Param("payType")Integer payType,
                                   @Param("perhaps")Integer perhaps,@Param("scenes")String scenes);
    Orders seek(@Param("userId")Integer userId, @Param("selTimeType")Integer selTimeType);


    String getSysParameter(@Param("key") String key);

    List<Orders> findOrdersByUserIdPerhaps(@Param("userId") Integer userId, @Param("code") Integer code);

    void updateCycTime(@Param("uuid") String uuid,@Param("cycLong")  Long cycLong);

    List<Orders> findAllMyOrders(@Param("userId")Integer userId);

    void addAppraise(@Param("userId")Integer userId,@Param("id")Integer id,@Param("uuid") String uuid, @Param("ordersId")String ordersId, @Param("star")Integer star, @Param("content")String content);

    Orders findOrders(@Param("uid")String uid);

    List<Appraise> findAllAppraiseByUid(@Param("id")Integer id);

    List<Orders> findStartTimeLimitTen(@Param("userId")Integer userId, @Param("code")Integer code, @Param("scenes")String scenes);

    List<Orders> findOrdersByUserIdDemand(@Param("userId")Integer userId);

    List<UndoneOrders> findAllUndoneOrders(@Param("id")Integer id, @Param("pageNum")Integer pageNum,@Param("value") String value);

    List<Orders> findUndoneOrdersLimitPage(@Param("id")Integer id,@Param("pageNum") Integer pageNum,@Param("scenes") String scenes, @Param("pageSize")Integer pageSize,@Param("perhaps") Integer perhaps);

    void deletedReceiver(@Param("ordersId")String ordersId, @Param("userId")Integer userId);

    void arrivedPlace(@Param("id")Integer id, @Param("ordersId")String ordersId,@Param("longitude") Double longitude, @Param("latitude")Double latitude);

    List<CommonTalkResponse> commonTalk(@Param("code") Integer code);

    void serverArrived(@Param("ordersId")String ordersId, @Param("userId")Integer userId,@Param("aTrue") Boolean aTrue);

    void serviceAllArrived(@Param("uid")String uid, @Param("aTrue")Boolean aTrue);

    ServiceArrivedPlaceResponse findServiceArrived(@Param("id")Integer id, @Param("ordersId")String ordersId);

    List<ServiceArrivedPlaceResponse> findAllServiceArrived(@Param("uid")String uid);

    Orders findOrdersByStartTimeStemp(@Param("userId")Integer userId, @Param("startTimeStamp")String startTimeStamp);

    Orders findLastestOne(@Param("uid")Integer uid,@Param("code") Integer code, @Param("value")String value);

    void deletedOrders(@Param("uuid")String uuid);

    List<ActiveResponse> activeList(@Param("pageNum")Integer pageNum);

    void updatePayType(@Param("uid")String uid, @Param("type")Integer type);

    List<CoordinateResponse> nearbyBody(@Param("id")Integer id,@Param("pageNum") Integer pageNum);

    NearbyBody nearbyBodyById(@Param("id")Integer id);

    void addLongitudeAndLatitude(@Param("uuid")String uuid,@Param("request") LongitudeAndLatitudeRequest request, @Param("id")Integer id);

    NearbyBody findNearbyByUserId(@Param("id")Integer id);

    void updateLongitudeAndLatitude(@Param("id")Integer id, @Param("request")LongitudeAndLatitudeRequest request, @Param("icon")String icon);

    void balanceRecharge(@Param("id")Integer id, @Param("money")Double money);

    List<OrdersResponse> findOrdersByLoLa(@Param("id")Integer id,@Param("latitude") Double latitude, @Param("longitude")Double longitude, @Param("pageNum")Integer pageNum);

    void createOrdersEachOther(@Param("uuid")String uuid, @Param("id")Integer id, @Param("startTime")String startTime, @Param("place")String place,
                               @Param("orderPeriod")Double orderPeriod, @Param("gratefulFree")Double gratefulFree, @Param("scenes")String scenes, @Param("money")Double money,
                               @Param("latitude")Double latitude, @Param("longitude")Double longitude, @Param("address")String address, @Param("userId")Integer userId);

    OrdersAloneResponse findAloneOrders(@Param("uuid")String uuid);

    List<OrdersAloneResponse> findAllAloneOrders(@Param("userId")Integer userId);

    void acceptAloneOrder(@Param("id")Integer id, @Param("orderId")String orderId);

}
