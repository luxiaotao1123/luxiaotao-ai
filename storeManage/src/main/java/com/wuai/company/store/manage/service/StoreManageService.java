package com.wuai.company.store.manage.service;

import com.wuai.company.util.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/6/29.
 */
public interface StoreManageService {
    Response bindingAlipay(Integer manageId, String realName, String accountNum);

    Response findAllDetails(Integer id,Integer pageNum);

    Response sureOrder(Integer id,String orderId);

    Response cancelOrder(Integer id,String orderId);

    Response renovateMoney(Integer id);

    Response withdrawCash(Integer attribute, String realName, String accountNum, Double money);

    Response waitingOrders(String cid,Integer id,Integer pageNum);

    Response detail(Integer attribute, String orderId);

    Response partySure(String uuid, Integer attribute);

    Response partyDetailed(String uuid, Integer attribute);
}
