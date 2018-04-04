package com.wuai.company.pms.service;

import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.entity.Response.Scene;
import com.wuai.company.entity.request.CouponsRequest;
import com.wuai.company.entity.request.IdentityProof;
import com.wuai.company.entity.request.ScenesRequest;
import com.wuai.company.entity.request.StoreActiveRequest;
import com.wuai.company.entity.request.SysRequest;
import com.wuai.company.pms.entity.request.TrystSceneRequest;
import com.wuai.company.pms.entity.request.VideoHomeRequest;
import com.wuai.company.util.Response;

import java.io.IOException;

/**
 * Created by 97947 on 2017/9/22.
 */
public interface PmsService {
    Response showActive(Integer pageNum);

    Response addActive(String name, String pic, String topic, String content, String time);

    Response updateActive(String attribute, String operationId, String pic, String topic, String content, String time);

    Response deletedActive( String name, String uuid);

    Response showUser( String phone, String startTime, String endTime,Integer pageNum);

    Response details(String name, String startTime, String endTime, Integer pageNum, Integer type,Integer code);

    Response ordersManage( Integer pageNum, String name, String startTime, String endTime, Integer type);

    Response showAdmin(Integer pageNum);

    Response deletedAdmin(String id, String uuid);

    Response showLabel();

    Response updateLabel(String id, String label, String key);

    Response showScene();

    Response updateScene(String attribute, Scene scene, Integer type);

    Response insertScene(String attribute, ScenesRequest scene);

    Response deletedScene(String attribute, String uuid);

    Response showTalk();

    Response showSys();

    Response updateSystem(SysRequest sysRequest);

    Response stopBackMoney(String name, Integer userId);

    Response startBackMoney(String name, Integer userId);

    Response taskAll(Integer pageNum);

    Response updateTask(String name, StoreActiveRequest request) throws IllegalAccessException;

    Response deletedTask(String name, String uuid);

    Response inputUserExcel(String name, String url) throws IOException;

    Response inputOrdersExcel(String name, String url) throws IOException;

    Response activeDetails(String name, Integer type, Integer pageNum,String value,String startTime,String endTime);

    Response upActiveDetails(String name, String expressNum, String expressName, String uuid,Integer send);

    Response generateUser(String name, Integer type);

    Response findAllManage(String name, Integer pageNum);

    Response showAllParties(String name, Integer pageNum, String value);

    Response partyConfirm(String name, String value, String uuid, String note);

    Response showVideos(String name,Integer pageNum,Integer videoCheck);

    Response checkVideo(String name,String uuid, Integer videoCheck,String note);

    Response showCash(String name, Integer pageNum, Integer cash);

    Response upCash(String name, String uuid,String note, Integer cash);

    Response couponsShow(String name, Integer pageNum);

    Response couponsAdd(String name, CouponsRequest couponsRequest) throws Exception;

    Response couponsUp(String name, CouponsRequest couponsRequest);

    Response couponsDel(String name, String uuid);

    Response rechargeWallet(String name, Integer userId, Double money);

    Response trystSceneAdd(String name, TrystSceneRequest request);

    Response trystVideoAdd(String name, String video, String videoPic, String typeValue, Integer typeCode);

    Response trystSceneShow(String name,Integer pageNum);

    Response trystVideoShow(String name,Integer pageNum);

    Response trystVideoDel(String name, String uuid);

    Response trystSceneDel(String name, String uuid);
    
    Response insertVideoHomeSelective(String name,String video,String videoPic);
    
    Response findIdentityProof(String name,Integer state,Integer useId,Integer pageNum);
    
    Response auditIdentityProof(String name,String uuid,Integer state,String idNumber,String realName,String satrtDate,String endDate);
}
