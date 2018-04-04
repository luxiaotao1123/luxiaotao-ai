package com.wuai.company.order.service;

import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.util.Response;

/**
 * Created by hyf on 2018/1/17.
 */
public interface TrystOrdersService {

    Response createTrystOrders(Integer attribute, TrystOrdersRequest trystOrdersRequest)  throws Exception;

    Response snatchableListAuth(Integer id, Integer pageNum,Double longitude, Double latitude);

    Response roomTrystAuth(Integer id, String uuid);

    Response findTrystOrders(Integer attribute, String uuid ) throws Exception;

    Response snatchOrder(Integer attribute, String uuid)  throws Exception;

    Response listTrystAuth(Integer id, Integer pageNum);

    Response modifyTrystAuth(Integer id, String uuid, String time, Double duration);

    Response completeDemandAuth(Integer id, String uuid);

    Response choiceSnatchUser(Integer attribute, String uuid) throws Exception;

    Response cancelTrystAuth(Integer id, String uuid, Integer passive,String reason);

    Response cancelTrystUser(Integer id, String uuid, Integer payed, String reason);

    Response noticeSize(Integer attribute, String uuid);

    Response myTrystOrders(Integer attribute, Integer pageNum);

    Response sureUser(Integer attribute, String uuid, String userIds);

    Response payMoneyAuth(Integer id, String uuid, Double money, String password);

    Response snatchUserList(Integer attribute, String uuid);

    Response grabOrders(Integer attribute, Double longitude, Double latitude);
}
