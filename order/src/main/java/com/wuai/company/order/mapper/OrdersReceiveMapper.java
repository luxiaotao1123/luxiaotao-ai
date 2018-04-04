package com.wuai.company.order.mapper;

import com.wuai.company.order.entity.OrdersReceive;
import com.wuai.company.order.service.OrdersService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */
@Mapper
public interface OrdersReceiveMapper {
    void addInvitationReceive(@Param("uuid") String uuid, @Param("ordersId")String ordersId, @Param("userId")Integer userId);

    OrdersReceive findInvitationReceive(@Param("uid") String uid,  @Param("userId")Integer userId);

    List<OrdersReceive> findOrdersReceive(@Param("uid")String uid);

    void deletedReceive(@Param("uuid") String uuid);

    List<OrdersReceive> findOrdersReceiveByUserId(@Param("userId")Integer userId,@Param("scenes") String scenes);
}
