package com.wuai.company.store.manage.service.impl;

import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.Response.PartyDetailedResponse;
import com.wuai.company.entity.StoreOrders;
import com.wuai.company.entity.User;
import com.wuai.company.enums.*;
import com.wuai.company.rpc.mobile.ServerHandler;
import com.wuai.company.store.manage.dao.StoreManageDao;
import com.wuai.company.store.manage.entity.response.StoreDetailsResponse;
import com.wuai.company.store.manage.service.StoreManageService;
import com.wuai.company.util.Response;
import com.wuai.company.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Service
@Transactional
public class StoreManageServiceImpl implements StoreManageService {

    Logger logger = LoggerFactory.getLogger(StoreManageServiceImpl.class);
    @Autowired
    private StoreManageDao storeManageDao;
    @Override
    public Response bindingAlipay(Integer manageId, String realName, String accountNum) {
        if (StringUtils.isEmpty(realName)||StringUtils.isEmpty(accountNum)){
            logger.warn("商家绑定支付宝 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        storeManageDao.bindingAlipay(manageId,realName,accountNum);
        return Response.successByArray();
    }

    @Override
    public Response findAllDetails(Integer id,Integer pageNum) {
        if (id==null||pageNum==null){
            logger.warn("商家订单 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"商家订单 参数为空");
        }
        List<StoreDetailsResponse> list = storeManageDao.findAllDetails(id,pageNum);
        for (int i=0;i<list.size();i++){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = null;
            try {
                time = simpleDateFormat.parse(list.get(i).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String ti=simpleDateFormat.format(time);
            list.get(i).setTime(ti);
            if (list.get(i).getPayType().equals(PayTypeEnum.STORE_WAIT_PAY.toCode())){
                list.get(i).setType(PayTypeEnum.STORE_WAIT_PAY.getValue());
            }
            if (list.get(i).getPayType().equals(PayTypeEnum.STORE_WAIT_CONFIRM.toCode())){
                list.get(i).setType(PayTypeEnum.STORE_WAIT_CONFIRM.getValue());
            }
            if (list.get(i).getPayType().equals(PayTypeEnum.STORE_SUCCESS.toCode())){
                list.get(i).setType(PayTypeEnum.STORE_SUCCESS.getValue());
            }
            if (list.get(i).getPayType().equals(PayTypeEnum.STORE_CANCEL.toCode())){
                list.get(i).setType(PayTypeEnum.STORE_CANCEL.getValue());
            }
           if (list.get(i).getPayType().equals(PayTypeEnum.STORE_OUT_OF_TIME.toCode())){
                list.get(i).setType(PayTypeEnum.STORE_OUT_OF_TIME.getValue());
            }

        }
        return Response.success(list);
    }

    @Override
    public Response sureOrder(Integer id,String orderId) {
        if (StringUtils.isEmpty(orderId)){
            logger.warn("商家确认订单 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        StoreOrders storeOrders = storeManageDao.findOrdersByOrderId(orderId);
        if (storeOrders==null){
            logger.warn("商家确认订单 订单为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        storeManageDao.sureOrder(orderId, PayTypeEnum.STR_SUCCESS.toCode());
//        商家确认后 将资金打入商家账号
        storeManageDao.updateMoney(storeOrders.getMoney(),id);
        //添加 订单明细
//        User user = userDao.findUserByUserId(storeOrders.getUserId());
//        String detailId2 = UserUtil.generateUuid();
//        //订单明细id--用户uuid--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
//        userDao.addOrdersDetail(detailId2,user.getUuid(),money,storeOrders.getUserId(), OrdersDetailTypeEnum.STORE_PAY.getKey(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.STORE_PAY.getValue(),OrdersDetailTypeEnum.STORE.getKey());

//        ServerHandler.sendStoreNotify(RpcAllowMsgEnum.NOTIFY,storeOrders, ServerHandlerTypeEnum.STORE_SURE.getType());
        return Response.successByArray();
    }

    @Override
    public Response cancelOrder(Integer id,String orderId) {
        if (StringUtils.isEmpty(orderId)){
            logger.warn("商家订单撤销 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        //查询订单
        StoreOrders storeOrders = storeManageDao.findOrdersByOrderId(orderId);
        if (storeOrders==null){
            logger.warn("商家取消订单 订单为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        //退回用户 订单金额
        storeManageDao.backMoney(storeOrders.getUserId(),storeOrders.getMoney());
        //撤销订单
        storeManageDao.cancelOrder(orderId,PayTypeEnum.STR_CANCEL.toCode());
        ServerHandler.sendStoreNotify(RpcAllowMsgEnum.NOTIFY,storeOrders, ServerHandlerTypeEnum.STORE_CANCEL.getType());
        return Response.successByArray();
    }

    @Override
    public Response renovateMoney(Integer id) {
        MerchantUser merchantUser = storeManageDao.finMerchantUserById(id);
        return Response.success(merchantUser);
    }

    @Override
    public Response withdrawCash(Integer id, String realName, String accountNum, Double money) {
        if (StringUtils.isEmpty(realName)||StringUtils.isEmpty(accountNum)){
            logger.warn("商家提现 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        MerchantUser merchantUser = storeManageDao.finMerchantUserById(id);
//        System.out.println("id = [" + id + "], realName = [" + realName + "], accountNum = [" + accountNum + "], money = [" + money + "]");
        if (!merchantUser.getRealName().equals(realName)||!merchantUser.getAccountNum().equals(accountNum)){
            logger.warn("商家提现 提现失败");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"提现失败,请核对姓名及支付宝账号");
        }
        if (merchantUser.getMoney()<money){
            logger.warn("商家提现 提现金额超出");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"提现失败,提现金额超出");
        }
        //提交提现 申请
        String uuid = UserUtil.generateUuid();

        storeManageDao.subtractMoney(id,money);
        storeManageDao.withdrawCash(id,money, WithDrawCashTypeEnum.MERCHANT_MEMBER.toCode(),uuid);
        return Response.successByArray();
    }

    @Override
    public Response waitingOrders(String cid,Integer id,Integer pageNum) {
        if (StringUtils.isEmpty(cid)||id==null||pageNum==null){

                logger.warn("获取未完成订单 参数为空");
                return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());

        }
        MerchantUser merchantUser = storeManageDao.findManage(id);
        if (merchantUser!=null) {
            storeManageDao.bindingCid(id, cid);
            List<StoreDetailsResponse> list = storeManageDao.findWaitingDetails(id, pageNum);
            for (int i=0;i<list.size();i++){
                ;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                   String time = simpleDateFormat.format(simpleDateFormat.parse(list.get(i).getTime()));
                    list.get(i).setTime(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            return Response.success(list);
        }
        return Response.error(ResponseTypeEnum.ERROR_CODE);
    }

    @Override
    public Response detail(Integer id, String orderId) {
        if (StringUtils.isEmpty(orderId)||id==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        StoreDetailsResponse response =  storeManageDao.findDetailByOrderId(orderId);
        if (response.getPayType().equals(PayTypeEnum.STORE_WAIT_PAY.toCode())){
            response.setType(PayTypeEnum.STORE_WAIT_PAY.getValue());
        }
        if (response.getPayType().equals(PayTypeEnum.STORE_WAIT_CONFIRM.toCode())){
            response.setType(PayTypeEnum.STORE_WAIT_CONFIRM.getValue());
        }
        if (response.getPayType().equals(PayTypeEnum.STORE_SUCCESS.toCode())){
            response.setType(PayTypeEnum.STORE_SUCCESS.getValue());
        }
        if (response.getPayType().equals(PayTypeEnum.STORE_CANCEL.toCode())){
            response.setType(PayTypeEnum.STORE_CANCEL.getValue());
        }
        if (response.getPayType().equals(PayTypeEnum.STORE_OUT_OF_TIME.toCode())){
            response.setType(PayTypeEnum.STORE_OUT_OF_TIME.getValue());
        }

        return Response.success(response);
    }

    @Override
    public Response partySure(String uuid, Integer id) {
        storeManageDao.surePartyOrders(uuid,PartyPayCodeEnum.PARTY_SUCCESS.getCode());
        return Response.successByArray();
    }

    @Override
    public Response partyDetailed(String uuid, Integer id) {
        PartyDetailedResponse partyDetailedResponse = storeManageDao.partyDetailed(uuid);
        return Response.success(partyDetailedResponse);
    }
}
