package com.wuai.company.store.manage.dao.impl;

import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.Response.PartyDetailedResponse;
import com.wuai.company.entity.StoreOrders;
import com.wuai.company.store.manage.dao.StoreManageDao;
import com.wuai.company.store.manage.entity.response.StoreDetailsResponse;
import com.wuai.company.store.manage.mapper.StoreManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Repository
public class StoreManageDaoImpl implements StoreManageDao {

    @Autowired
    private StoreManageMapper storeManageMapper;
    @Override
    public void bindingAlipay(Integer manageId, String realName, String accountNum) {
        storeManageMapper.bindingAlipay(manageId,realName,accountNum);
    }

    @Override
    public List<StoreDetailsResponse> findAllDetails(Integer id,Integer pageNum) {
        return storeManageMapper.findAllDetails(id,pageNum);
    }

    @Override
    public void sureOrder(String orderId,Integer payCode) {
        storeManageMapper.sureOrder(orderId,payCode);
    }

    @Override
    public void cancelOrder(String orderId, Integer payCode) {
        storeManageMapper.cancelOrder(orderId,payCode);
    }

    @Override
    public StoreOrders findOrdersByOrderId(String orderId) {
        return storeManageMapper.findOrdersByOrderId(orderId);
    }

    @Override
    public void backMoney(Integer userId, Double money) {
        storeManageMapper.backMoney(userId,money);
    }

    @Override
    public MerchantUser finMerchantUserById(Integer id) {
        return storeManageMapper.findMerchantUserById(id);
    }

    @Override
    public void withdrawCash(Integer id, Double money, Integer type,String uuid) {
        storeManageMapper.withdrawCash(id,money,type,uuid);
    }

    @Override
    public List<StoreDetailsResponse> findWaitingDetails(Integer id, Integer pageNum) {
        return storeManageMapper.findWaitingDetails(id,pageNum);
    }

    @Override
    public void subtractMoney(Integer id, Double money) {
        storeManageMapper.subtractMoney(id,money);
    }

    @Override
    public MerchantUser findManage(Integer id) {
        return storeManageMapper.findManage(id);
    }

    @Override
    public void bindingCid(Integer id, String cid) {
        storeManageMapper.bindingCid(id,cid);
    }

    @Override
    public void updateMoney(Double money, Integer id) {
        storeManageMapper.updateMoney(money,id);
    }

    @Override
    public StoreDetailsResponse findDetailByOrderId(String orderId) {
        return storeManageMapper.findDetailByOrderId(orderId);
    }

    @Override
    public void surePartyOrders(String uuid, Integer code) {
        storeManageMapper.surePartyOrders(uuid,code);
    }

    @Override
    public PartyDetailedResponse partyDetailed(String uuid) {
        return storeManageMapper.partyDetailed(uuid);
    }
}
