package com.wuai.company.store.manage.dao;

import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.Response.PartyDetailedResponse;
import com.wuai.company.entity.StoreOrders;
import com.wuai.company.store.manage.entity.response.StoreDetailsResponse;
import com.wuai.company.util.Response;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
public interface StoreManageDao{
    void bindingAlipay(Integer manageId, String realName, String accountNum);

    List<StoreDetailsResponse> findAllDetails(Integer id,Integer pageNum);

    void sureOrder(String orderId,Integer payCode);

    void cancelOrder(String orderId, Integer payCode);

    StoreOrders findOrdersByOrderId(String orderId);

    void backMoney(Integer userId, Double money);

    MerchantUser finMerchantUserById(Integer id);

    void withdrawCash(Integer id, Double money, Integer type,String uuid);

    List<StoreDetailsResponse> findWaitingDetails(Integer id, Integer pageNum);

    void subtractMoney(Integer id, Double money);

    MerchantUser findManage(Integer id);

    void bindingCid(Integer id, String cid);

    void updateMoney(Double money, Integer id);

    StoreDetailsResponse findDetailByOrderId(String orderId);

    void surePartyOrders(String uuid, Integer code);

    PartyDetailedResponse partyDetailed(String uuid);
}
