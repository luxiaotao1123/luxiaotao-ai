package com.wuai.company.order.dao;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.Response.UserVideoResponse;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.order.entity.DistancePo;
import com.wuai.company.order.entity.TrystScenes;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;
import java.util.Map;

/**
 * Created by hyf on 2018/1/17.
 */
public interface TrystOrdersDao {
    List<TrystScenes> trystScenes(Integer code);

    void createTrystOrders(Integer id, String uuid, TrystOrdersRequest trystOrdersRequest,Integer code,String value);

    NearBodyResponse findNearBody(Integer id);

    List<UserVideoResponse> findUserVides(Integer id);

    User findUserById(Integer userId);

    TrystOrders findTrystOrdersByUid(String trystId);

    List<TrystOrders> findTrystOrdersByUserId(Integer userId);

    List<TrystOrders> findTrystOrdersList(Integer gender, Integer payCode);

    void addTrystOrdersSnatch(Integer id, String uuid, String trystId);

    Integer findOrdersSnatchSize(String trystId);

    Integer findTrystSnatch(Integer id, String uuid);

    List<TrystOrders> findTrystOrdersListById(Integer id);

    UserVideoResponse findUserVideoById(Integer id);

    List<Coupons> findMyCoupons(Integer id);

    void addTrystOrdersReceive(String uuid,String trystId, Integer userId, Double money, Boolean deleted);

    List<VideoHome> findVideoHome(Integer code);

    int calcelTrystOrders(String uuid, int i, String value);

    void upMoney(Integer id, Double money);

    List<TrystCancel> findCancelTryst(Integer id,Integer type, String today);

    void cancelTryst(String uid,Integer id, String uuid, Integer code, Integer code1, String value, String today, String date, Integer time, String reason);

    TrystCancel findCancelTrystFir(Integer id, Integer code, Integer code1);

    void upTrystPersonCount(String uuid, int length);

    List<TrystReceive> findTrystReceive(String uuid);

    TrystOrders selectTrystOrders(TrystOrders trystOrders);

    List<DistancePo> selectDistanceAndIdByAsc(Map map);

    int upTrystOrdersPay(String trystId, Integer code, String value);

    int delectReceiveByUserId(Integer userId, String trystId);

    int logicDelectReceiveByUserId(Integer userId, String trystId);

    int reduceTrystPersonCount(String trystId);

    int addTimeByCancelTryst(String uuid,String trystId,String reason);

    List<TrystOrders> selectTrystOrderByReceiveUserId(Integer userId, Integer pageNum);

    int updateTimeAndDurByTrystId(String trystId, String time, Double duration, Integer showName);

    int selectCancelCountByService(Integer userId);

    List<TrystOrders> selectTrystOrdersByReceivAndUserId(Integer userId);

    List<Integer> selectUsersByCancel(String trystId);

    List<TrystReceive> selectReceiveByTryst(String trystId);
}
