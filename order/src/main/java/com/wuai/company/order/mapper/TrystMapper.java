package com.wuai.company.order.mapper;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.Response.UserVideoResponse;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.order.entity.DistancePo;
import com.wuai.company.order.entity.TrystScenes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by hyf on 2018/1/17.
 */
@Mapper
public interface TrystMapper {
    List<TrystScenes> trystScenes(@Param("code") Integer code);

    void createTrystOrders(@Param("id")Integer id, @Param("uuid")String uuid, @Param("trystOrdersRequest")TrystOrdersRequest trystOrdersRequest, @Param("code")Integer code, @Param("value")String value);

    NearBodyResponse findNearBody(@Param("id")Integer id);

    List<UserVideoResponse> findUserVides(@Param("id")Integer id);

    User findUserById(@Param("userId")Integer userId);

    TrystOrders findTrystOrdersByUid(@Param("trystId")String trystId);

    List<TrystOrders> findTrystOrdersByUserId(@Param("userId")Integer userId);

    List<TrystOrders> findTrystOrdersList(@Param("gender")Integer gender, @Param("payCode")Integer payCode);

    void addTrystOrdersSnatch(@Param("id")Integer id, @Param("uuid")String uuid, @Param("trystId")String trystId);

    Integer findOrdersSnatchSize(@Param("trystId")String trystId);

    Integer findTrystSnatch(@Param("id")Integer id, @Param("uuid")String uuid);

    List<TrystOrders> findTrystOrdersListById(@Param("id")Integer id);

    UserVideoResponse findUserVideoById(@Param("id")Integer id);

    List<Coupons> findMyCoupons(@Param("id")Integer id);

    void addTrystOrdersReceive(@Param("uuid")String uuid, @Param("trystId")String trystId, @Param("userId")Integer userId,@Param("money") Double money,@Param("deleted")Boolean deleted);

    List<VideoHome> findVideoHome(@Param("code")Integer code);

    void upTrystPersonCount(@Param("uuid")String uuid, @Param("personCount")int personCount);

    int calcelTrystOrders(@Param("uuid")String uuid, @Param("code")int code, @Param("value")String value);

    void upMoney(@Param("id")Integer id, @Param("money")Double money);

    List<TrystCancel> findCancelTryst(@Param("id")Integer id, @Param("type") Integer type, @Param("today")String today);

    void cancelTryst(@Param("uid")String uid, @Param("userId")Integer id, @Param("uuid")String uuid, @Param("today")String today, @Param("date")String date, @Param("value")String value, @Param("code")Integer code, @Param("time")Integer time, @Param("type")Integer type, @Param("reason")String reason);

    TrystCancel findCancelTrystFir(@Param("id")Integer id, @Param("type")Integer type, @Param("code")Integer code);

    List<TrystReceive> findTrystReceive(@Param("uuid")String uuid);

    TrystOrders selectTrystOrders(TrystOrders trystOrders);

    List<DistancePo> selectDistanceAndIdByAsc(Map map);

    int upTrystOrdersPay(@Param("trystId")String trystId, @Param("code")Integer code, @Param("value")String value);

    int delectReceiveByUserId(@Param("userId")Integer userId,@Param("trystId")String trystId);

    int logicDelectReceiveByUserId(@Param("userId")Integer userId,@Param("trystId")String trystId);

    int reduceTrystPersonCount(@Param("trystId")String trystId);

    int addTimeByCancelTryst(@Param("uuid")String uuid, @Param("trystId")String trystId, @Param("reason")String reason);

    List<TrystOrders> selectTrystOrderByReceiveUserId(@Param("userId")Integer userId, @Param("pageNum")Integer pageNum);

    int updateTimeAndDurAndShowByTrystId(@Param("trystId")String trystId, @Param("time")String time, @Param("duration")Double duration, @Param("showName")Integer showName);

    int selectCancelCountByService(@Param("userId")Integer userId);

    List<TrystOrders> selectTrystOrdersByReceivAndUserId(@Param("userId")Integer userId);

    List<Integer> selectUsersByCancel(@Param("trystId")String trystId);

    List<TrystReceive> selectReceiveByTryst(@Param("trystId")String trystId);
}
