package com.wuai.company.order.service;

import com.wuai.company.entity.Orders;
import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.util.Response;

import java.text.ParseException;

/**
 * 订单的servi层
 * Created by Ness on 2017/5/25.
 */
public interface OrdersService {

    /**
     * 根据Uuid查询订单信息
     *
     * @param uuid 订单的id
     * @return
     */
    Response findOrdersOneByUuid(String uuid);


    /**
     * 根据用户Id查询所有隶属于该用户的所用的订单
     *
     * @param userId    用户的Id
     * @param orderType 订单的类型
     * @return
     */
    Response findOrdersByUserId( Integer userId, Integer orderType);


    /**
     * 保存用户订单信息
     *
     * @param orders
     */
    Response saveOrders(Orders orders);



    /**
     * 更新用户的订单信息
     *
     * @param orders
     */
    Response updateOrdersOneByUuid(Orders orders);

    /**
     * 查询订单
     */
    Response findOtherOrders(String scenes,Integer perhaps,Integer uid,Integer pageNum) throws ParseException;


    /**
     * 修改邀约/应约
     * @return
     */
    Response updateInvitation(String uid,Integer userId,String startTime,String place, Integer selTimeType,Double orderPeriod,
                              Integer personCount, Double gratefulFree,String label,Integer perhaps, Integer scenes,
                              Double money,Double longitude,Double latitude,String address) throws ParseException;

    /**
     * 根据性别返回个性标签
     * @return
     */
    Response findLabel( );
    /**
     * 邀请/应邀界面  地点选择
     */
    Response invitationFindPlace(Double longitude,Double latitude);
    /**
     * 相应场景参数
//     * @param request
     */
    Response invitationChooseScenes(Integer scene,Integer userId);

    /**
     * 根据uid查询订单
     * @param uid 订单id
     */
    Response invitationSure(Integer id,String uid);
    /**
     *  删除订单
     */
    Response invitationDeleted(Integer id,String uid);
    //应约
    Response invitationReceive(String uid,Integer userId) throws ParseException;

    Response advance(Integer uuid ,String ordersId);
//    Response advance(Integer uuid ,Double longitude,Double latitude,Integer scenes,Integer perhaps);

    //查询约会订单
    Response findAllOfMyOrders(Integer uid, Integer pageNum);
    /**
     * 查询订单详情
     * @param uid
     * @return
     */
    Response findDetailedData(Integer userId,String uid);

    Response findMaps(String name,Double longitude,Double latitude,Integer pageNum,String scene);

    Response personalDetails(String uid);

    Response bill(Integer attribute, Integer pageNum);

    Response createOrders(Integer userId,String startTime,String place, Integer selTimeType,Double orderPeriod,
                          Integer personCount, Double gratefulFree,String label,Integer perhaps,
                          Integer scenes,Double money,Double latitude,Double longitude,String address) throws ParseException;

    Response agreeInvitation(Integer attribute, String orderId,String uuid) throws ParseException;

    Response advertisementPic();

    Response advertisementMap();

    Response seekOtherOrders(String scenes, Integer perhaps, Integer attribute, Integer pageNum);

    Response seekOrdersDetailed(Integer attribute, String ordersId);

    Response invitationAgree(String uuid,String ordersId,Integer userId, Integer attribute);

    Response invitationRefuse(String ordersId, Integer attribute,Integer userId);

    Response invitationReceiveSure(String ordersId,Integer userId2, Integer userId);

    Response invitationReceiveRefuse(String ordersId, Integer id,Integer userId);

    Response tessss();

    Response addAppraise(Integer attribute, String appraiseRequest);

    Response findAllOrdersUser(Integer attribute, String ordersId);

    Response version(Integer device);

    Response activePic();

    Response findAllUndoneOrders(Integer id, Integer pageNum,String scenes);

    Response te();

    Response joinOrders(Integer attribute, String ordersId) throws ParseException;

    Response undoneOrdersDetails(Integer id, Integer pageNum,Integer scenes,Integer perhaps);

    Response msgDetail(Integer attribute, Integer pageNum);

    Response expelUser(Integer attribute, Integer userId, String ordersId);

    Response arrivedPlace(Integer attribute, String ordersId, Double longitude, Double latitude);

    Response commonTalk(Integer id,String ordersId) throws ParseException;

    Response invitationArrived(Integer attribute, String ordersId, Integer userId);

    Response newMsg(Integer attribute);

    Response refuseJoin(Integer attribute, String ordersId);

    Response activeList(Integer pageNum);

    Response nearbyBody(Integer id);

    Response addLongitudeAndLatitude(Integer id, LongitudeAndLatitudeRequest request) throws IllegalAccessException;

    Response updateLongitudeAndLatitude(Integer id, LongitudeAndLatitudeRequest request) throws IllegalAccessException;

    Response joinLimit(Integer id,String ordersId,Integer perhaps,String scenes);

    Response balanceRecharge(Integer attribute, Double money);

    Response inputExcel(String url);

    Response nearbyOrders(Integer id,Integer pageNum);

    Response createOrdersEachOther(Integer id, String startTime, String place,  Double orderPeriod,
                                   Double gratefulFree, String scenes, Double money,
                                   Double latitude, Double longitude, String address,Integer userId);

    Response findAloneOrders(Integer attribute, String uuid);

    Response findAllAloneOrders(Integer attribute, Integer userId);

    Response acceptAloneOrder(Integer attribute, String ordersId);

//    Response invitationChooseScenes(InvitationRequest request);
//    Response invitationChooseScenes();

}
