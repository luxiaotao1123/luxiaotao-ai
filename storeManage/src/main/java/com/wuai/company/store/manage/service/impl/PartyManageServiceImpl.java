package com.wuai.company.store.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.Response.MessageAllResponse;
import com.wuai.company.entity.Response.PartyDetailedInformationResponse;
import com.wuai.company.entity.Response.PartyOrdersResponse;
import com.wuai.company.entity.StoreOrders;
import com.wuai.company.enums.*;
import com.wuai.company.rpc.mobile.ServerHandler;
import com.wuai.company.store.manage.dao.PartyManageDao;
import com.wuai.company.store.manage.dao.StoreManageDao;
import com.wuai.company.store.manage.entity.request.PartyUpRequest;
import com.wuai.company.store.manage.entity.response.StoreDetailsResponse;
import com.wuai.company.store.manage.service.PartyManageService;
import com.wuai.company.store.manage.service.StoreManageService;
import com.wuai.company.util.LocationUtils;
import com.wuai.company.util.Response;
import com.wuai.company.util.SysUtils;
import com.wuai.company.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Service
@Transactional
public class PartyManageServiceImpl implements PartyManageService {

    @Autowired
    private PartyManageDao partyManageDao;


    Logger logger = LoggerFactory.getLogger(PartyManageServiceImpl.class);


    @Override
    public Response upParty(String name, PartyUpRequest request) throws Exception {
//        if (StringUtils.isEmpty(request.getVideo())){
//            request.setType(0);
//        }else {
//            request.setType(1);
//
//        }
        MerchantUser merchantUser = partyManageDao.findMerchantUserByName(name);
        if (merchantUser==null){
            logger.warn("用户不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户不存在");
        }
        if (StringUtils.isEmpty(request.getAddress())){
            logger.warn("地址为空");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"地址为空");
        }else {
            LocationUtils.LocationQueryResult result = LocationUtils.query(request.getAddress(), LocationUtils.QueryMethod.GAO_DE);
            if (result.getSuccess()==false){
                logger.warn("地址输入有误");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"地址输入有误");
            }
            request.setLongitude(result.getLongitude());
            request.setLatitude(result.getLatitude());

        }
        request.setIcon(merchantUser.getIcon());
        request.setUserName(merchantUser.getNickname());
        request.setName(name);
        request.setPhoneNum(merchantUser.getPhoneNum());
        request.setCxamineAndVerify(CxamineAndVerify.WAIT_CONFIRM.getValue());
        if(StringUtils.isEmpty(request.getUuid())){
            logger.info("添加活动");
            String uuid = UserUtil.generateUuid();
            request.setUuid(uuid);
            addParty(name,request);
            return Response.successByArray();
        }
//        if (StringUtils.isEmpty(name)|| SysUtils.checkObjFieldIsNull(request)){
//            logger.warn("参数为空");
//            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
//        }

        partyManageDao.upParty(request);
        return Response.successByArray();
    }

    public Response addParty(String name, PartyUpRequest request) throws Exception {
//        if (StringUtils.isEmpty(name)|| SysUtils.checkObjFieldIsNull(request)){
//            logger.warn("参数为空");
//            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
//        }
        partyManageDao.addParty(request);
        return Response.successByArray();
    }
    @Override
    public Response classify() {
        String value = partyManageDao.sysValue(SysKeyEnum.PARTY_CALSSIFY.getKey());
        return Response.success(value);
    }

    @Override
    public Response findPartyAll(String name, Integer pageNum,String value) {
        if (StringUtils.isEmpty(name)||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }

        List<PartyDetailedInformationResponse> response = partyManageDao.findPartyAll(name,pageNum,value);
        Integer size = partyManageDao.sizeOfParty(name,value);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",response);
        jsonObject.put("size",size);
        return Response.success(jsonObject);
    }

    @Override
    public Response delParty(String name, String uuid) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        partyManageDao.delParty(uuid);
        return Response.successByArray();
    }

    @Override
    public Response informationParty(String name) {
        MerchantUser merchantUser = partyManageDao.findMerchantUserByName(name);
        return Response.success(merchantUser);
    }

    @Override
    public Response messages(String name, String uuid, Integer pageNum) {

        PageHelper.startPage(pageNum,10);
        List<MessageAllResponse> messageAllResponse = partyManageDao.findAllMessages(uuid);
        //用PageInfo对结果进行包装
        PageInfo<MessageAllResponse> page = new PageInfo<>(messageAllResponse);
        return Response.success(page);
    }


}
