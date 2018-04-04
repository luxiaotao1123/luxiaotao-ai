package com.wuai.company.store.manage.mapper;

import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.Response.PartyDetailedResponse;
import com.wuai.company.entity.StoreOrders;
import com.wuai.company.store.manage.entity.response.StoreDetailsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Mapper
public interface StoreManageMapper {
    void bindingAlipay(@Param("manageId") Integer manageId, @Param("realName") String realName, @Param("accountNum") String accountNum);

    List<StoreDetailsResponse> findAllDetails(@Param("id") Integer id,@Param("pageNum")Integer pageNum);

    void sureOrder(@Param("orderId") String orderId,@Param("payCode") Integer payCode);

    void cancelOrder(@Param("orderId") String orderId,@Param("payCode") Integer payCode);

    StoreOrders findOrdersByOrderId(@Param("orderId")String orderId);

    void backMoney(@Param("userId")Integer userId, @Param("money")Double money);

    MerchantUser findMerchantUserById(@Param("id")Integer id);

    void withdrawCash(@Param("id") Integer id, @Param("money") Double money, @Param("type") Integer type, @Param("uuid")String uuid);

    List<StoreDetailsResponse> findWaitingDetails(@Param("id") Integer id,@Param("pageNum")Integer pageNum);

    void subtractMoney(@Param("id") Integer id, @Param("money") Double money);

    MerchantUser findManage(@Param("id") Integer id);

    void bindingCid(@Param("id")Integer id, @Param("cid")String cid);

    void updateMoney(@Param("money")Double money, @Param("id")Integer id);

    StoreDetailsResponse findDetailByOrderId(@Param("orderId")String orderId);

    void surePartyOrders(@Param("uuid")String uuid, @Param("code")Integer code);

    PartyDetailedResponse partyDetailed(@Param("uuid")String uuid);
}
