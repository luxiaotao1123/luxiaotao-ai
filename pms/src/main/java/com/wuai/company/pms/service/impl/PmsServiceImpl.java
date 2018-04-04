package com.wuai.company.pms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.*;
import com.wuai.company.enums.*;
import com.wuai.company.pms.dao.PmsDao;
import com.wuai.company.pms.entity.request.TrystSceneRequest;
import com.wuai.company.pms.entity.request.VideoHomeRequest;
import com.wuai.company.pms.entity.response.PartiesResponse;
import com.wuai.company.pms.entity.response.TrystSceneResponse;
import com.wuai.company.pms.entity.response.TrystVideoHomeResponse;
import com.wuai.company.pms.service.PmsService;
import com.wuai.company.pms.util.ImportExecl;
import com.wuai.company.util.MD5;
import com.wuai.company.util.Regular;
import com.wuai.company.util.Response;
import com.wuai.company.util.SysUtils;
import com.wuai.company.util.UserUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
/**
 * Created by 97947 on 2017/9/22.
 */
@Service
@Transactional
public class PmsServiceImpl implements PmsService {
    @Autowired
    private PmsDao pmsDao;
    @Resource
    private ImportExecl importExecl;
    @Resource
    private HashOperations<String, String, String> orderHashRedisTemplate;
    @Resource
    private ZSetOperations<String,String> msgValueTemplate;
    private final String USER_MSG = "%s:msg"; //用户id--信息列表
    private final String USER_TYPE_CONTEN = "%s:%s:%s"; //用户id--类型--内容
    Logger logger = LoggerFactory.getLogger(PmsServiceImpl.class);
    @Override
    public Response showActive(Integer pageNum) {
        if (pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<ActiveContent> list = pmsDao.allActiveContent(pageNum);
        Integer size = pmsDao.sizeOfActive();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",list);
        jsonObject.put("size",size);
              Integer page = 0;
        if (size/10*10<size){
            page = size/10+1;
        }else {
            page = size/10;
        }
            jsonObject.put("page",page);
        return Response.success(jsonObject);
    }

    @Override
    public Response addActive(String name, String pic, String topic, String content, String time) {
        if (StringUtils.isEmpty(name)|| StringUtils.isEmpty(pic)|| StringUtils.isEmpty(topic)|| StringUtils.isEmpty(content)|| StringUtils.isEmpty(time)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        String uuid = UserUtil.generateUuid();
        //添加 活动
        pmsDao.addActive(uuid,  pic,  topic,  content,  time);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作订单id--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(),uuid, OperationNoteTypeEnum.ACTIVE_ADD.getCode(),OperationNoteTypeEnum.ACTIVE_ADD.getValue());

        return Response.successByArray();
    }

    @Override
    public Response updateActive(String name, String uuid, String pic, String topic, String content, String time) {
        if (StringUtils.isEmpty(uuid)||StringUtils.isEmpty(name)|| StringUtils.isEmpty(pic)|| StringUtils.isEmpty(topic)|| StringUtils.isEmpty(content)|| StringUtils.isEmpty(time)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //修改 活动
        pmsDao.updateActive(uuid,pic,topic,content,time);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作订单id--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(),uuid, OperationNoteTypeEnum.ACTIVE_UPDATE.getCode(),OperationNoteTypeEnum.ACTIVE_UPDATE.getValue());

        return Response.successByArray();
    }

    @Override
    public Response deletedActive( String name, String uuid) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //删除活动
        pmsDao.deletedActive(uuid);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作订单id--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(),uuid, OperationNoteTypeEnum.ACTIVE_UPDATE.getCode(),OperationNoteTypeEnum.ACTIVE_UPDATE.getValue());

        return Response.successByArray();
    }

    @Override
    public Response showUser( String phone, String startTime, String endTime,Integer pageNum) {
        List<User> list = pmsDao.findUser(phone,startTime,endTime,pageNum);

        for (int i=0;i<list.size();i++){
            User user = list.get(i);
            if (user.getIdCard()!=null||!user.getIdCard().equals("")){
                user.setIdCard(UserUtil.idCardNum(user.getIdCard()));
            }
        }
        Integer size =  pmsDao.sizeOfUser(phone,startTime,endTime,pageNum);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",list);
        jsonObject.put("size",size);
        Integer page = 0;
        if (size/10*10<size){
            page = size/10+1;
        }else {
            page = size/10;
        }
        jsonObject.put("page",page);
        return Response.success(jsonObject);
    }

    @Override
    public Response details( String name, String startTime, String endTime, Integer pageNum, Integer type,Integer code) {
        if (pageNum==null||type==null||code==null){
            logger.warn("参数为空");
           return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if (type.intValue()==OrdersDetailTypeEnum.ORDERS.getKey()){
            logger.info("约会订单明细");
            List<InvitationDetailResponse> list = pmsDao.findInvitationDetail(type,code,  name,  startTime,  endTime,  pageNum);
            JSONObject jsonObject = new JSONObject();
            List<InvitationDetailResponse> list2 = pmsDao.sizeOfDetails( name,  startTime,  endTime, code);
            Integer size = list2.size();
            jsonObject.put("list",list);
            jsonObject.put("size",size);
              Integer page = 0;
            if (size/10*10<size){
                page = size/10+1;
            }else {
                page = size/10;
            }
            jsonObject.put("page",page);
            return Response.success(jsonObject);
        }
        if (type.intValue()==OrdersDetailTypeEnum.STORE.getKey()){
            logger.info("商城订单明细");
            // TODO: 2017/9/25 商城明细
            return null;
        }
       return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"查询失败type="+type);
    }

    @Override
    public Response ordersManage( Integer pageNum, String name, String startTime, String endTime, Integer type) {
        if (type==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<Orders> list = pmsDao.findOrders(pageNum,name,startTime,endTime,type);
        JSONObject jsonObject = new JSONObject();
        Integer size = pmsDao.sizeOfOrdersMange( pageNum,  name,  startTime,  endTime,  type);
        jsonObject.put("list",list);
        jsonObject.put("size",size);
          Integer page = 0;
        if (size/10*10<size){
            page = size/10+1;
        }else {
            page = size/10;
        }
        jsonObject.put("page",page);
        return Response.success(jsonObject);
    }

    @Override
    public Response showAdmin(Integer pageNum) {
        if (pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<AdminUser> list = pmsDao.showAdmin(pageNum);
        JSONObject jsonObject = new JSONObject();
        Integer size = pmsDao.sizeOfAdmin();
        jsonObject.put("list",list);
        jsonObject.put("size",size);
          Integer page = 0;
        if (size/10*10<size){
            page = size/10+1;
        }else {
            page = size/10;
        }
        jsonObject.put("page",page);
        return Response.success(jsonObject);

    }

    @Override
    public Response deletedAdmin(String name, String uuid) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        pmsDao.deletedAdmin(uuid);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作订单id--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(),uuid, OperationNoteTypeEnum.ADMIN_DELETED.getCode(),OperationNoteTypeEnum.ADMIN_DELETED.getValue());

        return Response.successByArray();
    }

    @Override
    public Response showLabel() {
        List<SysResponse> list = pmsDao.showLabel(SysKeyEnum.LABEL_BOY_BUSINESS.getKey(),SysKeyEnum.LABEL_BOY_FEATURE.getKey(),
                SysKeyEnum.LABEL_BOY_INSTEREST.getKey(),SysKeyEnum.LABEL_GIRL_BUSINESS.getKey(),
                SysKeyEnum.LABEL_GIRL_FEATURE.getKey(),SysKeyEnum.LABEL_GIRL_INSTEREST.getKey());
        return Response.success(list);
    }

    @Override
    public Response updateLabel(String name, String label,String key) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(label)){
                logger.warn("参数为空");
                return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");

        }
        String[] labels = label.split(",");
        int exit=0;
        for (int i=0;i<labels.length;i++){
            String compare = labels[i];
            for (int j=0;j<labels.length;j++){
                 if (compare.equals(labels[j])){
                     exit++;
                     break;
                 }
            }
        }
//        if (exit!=0){
//            logger.info("存在相同的标签");
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"存在相同标签");
//        }
        pmsDao.updateLabel(key,label);
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        Integer code = OperationNoteTypeEnum.LABEL_UPDATE.getCode();
        String value= OperationNoteTypeEnum.LABEL_UPDATE.getValue();

        pmsDao.addOperationNote(operationId,adminUser.getId(),key,code,value);

        return Response.successByArray();
    }

    @Override
    public Response showScene() {
        List<Scene> list = pmsDao.findScene();
        return Response.success(list);
    }

    @Override
    public Response updateScene(String name, Scene scene, Integer type) {
        if (StringUtils.isEmpty(name)||type==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(),scene.getUuid(),OperationNoteTypeEnum.SCENE_UPDATE.getCode(),OperationNoteTypeEnum.SCENE_UPDATE.getValue());
        pmsDao.updateScene(scene);
        return Response.successByArray();
    }

    @Override
    public Response insertScene(String name, ScenesRequest scene) {
        if (StringUtils.isEmpty(name)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        try {
            if (SysUtils.checkObjFieldIsNull(scene)&&scene.getKey()==null&&scene.getUuid()==null){

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        String uuid = UserUtil.generateUuid();
        pmsDao.addOperationNote(operationId,adminUser.getId(),uuid,OperationNoteTypeEnum.SCENE_INSERT.getCode(),OperationNoteTypeEnum.SCENE_INSERT.getValue());
        scene.setUuid(uuid);
        List<Scene> list = pmsDao.findScene();
        scene.setKey(String.valueOf(list.size()));
        pmsDao.insertScene(scene);
        return Response.successByArray();
    }

    @Override
    public Response deletedScene(String name, String uuid) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<Scene> list = pmsDao.findScene();
        //获取要被删除的 场景的 key
        Scene scene = pmsDao.findSceneByUid(uuid);
        int key = Integer.parseInt(scene.getKey());
//        若删除的场景不是为最后一个场景
        pmsDao.deletedScene(uuid);
        int keyNum=key+1;
        if (list.size()>keyNum){  //7>6
            for (int i=0;i<list.size();i++){
//             size = 删除的场景的下一个场景的 key
                if(i>=keyNum){  //如果 为下一个场景 更新该场景的 key
                    pmsDao.updateKey(list.get(i).getUuid(),String.valueOf(key));
                    key++;
                }
//                if (i>=keyNum){  // 6
//                    size = i+1;// 1
//                }
////                删除场景的下一个场景 key-1
//                if (size!=null&&size.intValue()==i+1){
//                    int key2 = Integer.parseInt(list.get(size).getKey())-1;
//                    pmsDao.updateKey(list.get(size).getUuid(),String.valueOf(key2));
//                }
            }
        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(),uuid,OperationNoteTypeEnum.SCENE_DELETE.getCode(),OperationNoteTypeEnum.SCENE_DELETE.getValue());
        return Response.successByArray();
    }

    @Override
    public Response showTalk() {

        List<TalkResponse> list = pmsDao.showTalk();
        return Response.success(list);
    }
    @Override
    public Response showSys() {
        List<SystemResponse> list = pmsDao.showSys();
        return Response.success(list);
    }

    @Override
    public Response updateSystem(SysRequest sysRequest) {

        if (StringUtils.isEmpty(sysRequest.getKey())||StringUtils.isEmpty(sysRequest.getValue())) {
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String[] keys = sysRequest.getKey().split(",");
        String[] values = sysRequest.getValue().split(",");
        if (keys.length!=values.length){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"缺少键值");

        }
        for (int i=0;i<keys.length;i++){
            String key = keys[i];
            String value = values[i];
            pmsDao.updateSystem(key,value);
        }

        return Response.successByArray();
    }

    @Override
    public Response stopBackMoney(String name, Integer userId) {
        if (StringUtils.isEmpty(name)||userId==null){
            logger.warn("参数为空");
        }
        pmsDao.stopBackMoney(userId,SysKeyEnum.STOP_BACK.getKey());
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), String.valueOf(userId),OperationNoteTypeEnum.STOP_BACK_MONEY.getCode(),OperationNoteTypeEnum.STOP_BACK_MONEY.getValue());
        return Response.successByArray();
    }

    @Override
    public Response startBackMoney(String name, Integer userId) {
        if (StringUtils.isEmpty(name)||userId==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        pmsDao.stopBackMoney(userId,SysKeyEnum.START_BACK.getKey());
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), String.valueOf(userId),OperationNoteTypeEnum.START_BACK_MONEY.getCode(),OperationNoteTypeEnum.START_BACK_MONEY.getValue());
        return Response.successByArray();
    }

    @Override
    public Response taskAll(Integer pageNum) {
        if (pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<StoreTaskDetailedResponse> list = pmsDao.findAllTasks(pageNum);
        return Response.success(list);
    }

    @Override
    public Response updateTask(String name, StoreActiveRequest request) throws IllegalAccessException {
        int type=0;
        if (StringUtils.isEmpty(request.getUuid())){

            String uuid = UserUtil.generateUuid();
            request.setUuid(uuid);
            type++;
        }
        if (SysUtils.checkObjFieldIsNull(request)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if (type==0) {
            logger.info("修改商城活动name={}",name);
            pmsDao.updateTask(request);
            AdminUser adminUser = pmsDao.findAdminByName(name);
            //添加操作记录
            String operationId = UserUtil.generateUuid();
            //操作记录id-- 操作人id--被操作对象--操作标识--note
            pmsDao.addOperationNote(operationId,adminUser.getId(), request.getUuid(),OperationNoteTypeEnum.UPDATE_REGISTER.getCode(),OperationNoteTypeEnum.UPDATE_REGISTER.getValue());

        }else {
            logger.info("添加商城活动name={}",name);
            pmsDao.addTask(request);
            AdminUser adminUser = pmsDao.findAdminByName(name);
            //添加操作记录
            String operationId = UserUtil.generateUuid();
            //操作记录id-- 操作人id--被操作对象--操作标识--note
            pmsDao.addOperationNote(operationId,adminUser.getId(), request.getUuid(),OperationNoteTypeEnum.ADD_REGISTER.getCode(),OperationNoteTypeEnum.ADD_REGISTER.getValue());

        }
        return  Response.successByArray();
    }

    @Override
    public Response deletedTask(String name, String uuid) {
        logger.info("添加商城活动name={}",name);
        pmsDao.deletedTask(uuid);
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), uuid,OperationNoteTypeEnum.DELETED_REGISTER.getCode(),OperationNoteTypeEnum.DELETED_REGISTER.getValue());
        return Response.successByArray();
    }

    @Override
    public Response inputUserExcel(String name, String url) throws IOException {
        System.out.println(url);
        List<UserExcelRequest> list = ImportExecl.inputUserExcel(url);
        pmsDao.inputUserExcel(list);
        return Response.successByArray();
    }

    @Override
    public Response inputOrdersExcel(String name, String url) throws IOException {

        List<OrdersExcelRequest> list = importExecl.inputOrdersExcel(url);
        pmsDao.inputOrdersExcel(list);
        return Response.successByArray();
    }

    @Override
    public Response activeDetails(String name, Integer type, Integer pageNum,String value,String startTime,String endTime) {
        if(StringUtils.isEmpty(name)||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<ActiveOrdersResponse> orders = pmsDao.activeDetails(type,pageNum,value,startTime,endTime);
        Integer size = pmsDao.activeDetails(type,null,value,startTime,endTime).size();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("activeDetails",orders);
        jsonObject.put("size",size);
        return Response.success(jsonObject);
    }

    @Override
    public Response upActiveDetails(String name, String expressNum, String expressName, String uuid,Integer send) {
        if(StringUtils.isEmpty(name)||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), uuid,OperationNoteTypeEnum.ADD_REGISTER.getCode(),OperationNoteTypeEnum.ADD_REGISTER.getValue());
        pmsDao.upActiveDetails(uuid,expressName,expressNum,send);
        return Response.successByArray();
    }

    @Override
    public Response generateUser(String name, Integer type) {
        if (StringUtils.isEmpty(name)||type==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String username = SysUtils.randomByLength(9);
        MerchantUser merchant = pmsDao.findMerchantByUser(username);
        while (merchant!=null){
            username =  SysUtils.randomByLength(9);
            merchant = pmsDao.findMerchantByUser(username);
        }
        String uid = UserUtil.generateUuid();
        pmsDao.insertMerchantUser(uid,username, MD5.encryption(MD5.encryption(username)),type);
        return Response.success(username);
    }

    @Override
    public Response findAllManage(String name, Integer pageNum) {
        if (StringUtils.isEmpty(name)||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PageHelper.startPage(pageNum,10);
        List<MerchantUser> merchantUser = pmsDao.findAllManage(pageNum);
        //用PageInfo对结果进行包装
        PageInfo<MerchantUser> page = new PageInfo<>(merchantUser);
        return Response.success(page);
    }

    @Override
    public Response showAllParties(String name, Integer pageNum, String value) {
        if (StringUtils.isEmpty(name)||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<PartiesResponse> list = pmsDao.showAllParties(pageNum,value);
        Integer size = pmsDao.partiesSize(value);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list",list);
        jsonObject.put("size",size);
        return Response.success(jsonObject);
    }

    @Override
    public Response partyConfirm(String name, String value, String uuid, String note) {
        pmsDao.partyConfirm(value,uuid,note);
//        Long size = msgValueTemplate.size(String.format(USER_MSG,id));
//        Long score = size+1;
//        msgValueTemplate.add(String.format(USER_MSG,id),String.format(USER_TYPE_ORDERS,userId,MsgTypeEnum.REFUSE_JOIN_MSG.getCode(),nameValue),score);
        return Response.successByArray();
    }

    @Override
    public Response showVideos(String name,Integer pageNum,Integer videoCheck) {
        if (pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PageHelper.startPage(pageNum,10);
        List<UserVideoResponse> list = pmsDao.findAllUserVideo(videoCheck);
        PageInfo<UserVideoResponse> pageInfo = new PageInfo<>(list);

        return Response.success(pageInfo);
    }

    @Override
    public Response checkVideo(String name,String uuid, Integer videoCheck,String note) {
        if (videoCheck==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        UserVideoResponse userVideoResponse = pmsDao.findVideoByUuid(uuid);
        if (userVideoResponse==null){
            logger.warn("订单不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"订单不存在");
        }
        Integer code=null;
        String value=null;
        switch (videoCheck){
            case 1:
               code= OperationNoteTypeEnum.SUCCESS_VIDEO.getCode();
               value=OperationNoteTypeEnum.SUCCESS_VIDEO.getValue();
                Long size = msgValueTemplate.size(String.format(USER_MSG,userVideoResponse.getUserId()));
                Long score = size+1;
                String content="您的视频已审核通过";
                if (StringUtils.isEmpty(note)){
                    note=content;
                }
                msgValueTemplate.add(String.format(USER_MSG,userVideoResponse.getUserId()),String.format(USER_TYPE_CONTEN,userVideoResponse.getUserId(),MsgTypeEnum.SYS_MSG.getCode(),content),score);
               break;
            case 2:
                Long size2 = msgValueTemplate.size(String.format(USER_MSG,userVideoResponse.getUserId()));
                Long score2 = size2+1;
                String content2="您的视频审核失败";
                if (StringUtils.isEmpty(note)){
                    note=content2;
                }
                msgValueTemplate.add(String.format(USER_MSG,userVideoResponse.getUserId()),String.format(USER_TYPE_CONTEN,userVideoResponse.getUserId(),MsgTypeEnum.SYS_MSG.getCode(),content2),score2);

                code= OperationNoteTypeEnum.FAIL_VIDEO.getCode();
                value=OperationNoteTypeEnum.FAIL_VIDEO.getValue();
                break;
            default:
                return  Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"videoCheck="+videoCheck+" 参数不正确");

        }

        pmsDao.checkVideo(uuid,videoCheck,note);
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), uuid,code,value);
        return Response.successByArray();
    }

    @Override
    public Response showCash(String name, Integer pageNum, Integer cash) {
        if (pageNum==null||cash==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PageHelper.startPage(pageNum,10);
        List<WithdrawCashResponse> response = pmsDao.findAllCash(cash);
        PageInfo<WithdrawCashResponse> pageInfo = new PageInfo<>(response);
        return Response.success(pageInfo);
    }

    @Override
    public Response upCash(String name, String uuid, String note, Integer cash) {
        if (StringUtils.isEmpty(uuid)||StringUtils.isEmpty(note)||cash==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }

        WithdrawCashResponse response = pmsDao.findCashByUuid(uuid);
        if (response==null){
            logger.warn("提现申请为空");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"");
        }
        Integer code=null;
        String value=null;
        switch (cash){
            case 1:
                code= OperationNoteTypeEnum.CASH_SUCCESS.getCode();
                value=OperationNoteTypeEnum.CASH_SUCCESS.getValue();
                Long size = msgValueTemplate.size(String.format(USER_MSG,response.getUserId()));
                Long score = size+1;
                String content="您的提现申请已成功";
                msgValueTemplate.add(String.format(USER_MSG,response.getUserId()),String.format(USER_TYPE_CONTEN,response.getUserId(),MsgTypeEnum.SYS_MSG.getCode(),content),score);

                break;
            case 2:
                code= OperationNoteTypeEnum.CASH_FAIL.getCode();
                value=OperationNoteTypeEnum.CASH_FAIL.getValue();
                Long size2 = msgValueTemplate.size(String.format(USER_MSG,response.getUserId()));
                Long score2 = size2+1;
                String content2="您的提现申请已失败";
                msgValueTemplate.add(String.format(USER_MSG,response.getUserId()),String.format(USER_TYPE_CONTEN,response.getUserId(),MsgTypeEnum.SYS_MSG.getCode(),content2),score2);

                break;
            default:
                return  Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"cash="+cash+" 参数不正确");

        }
        pmsDao.upCash(  uuid,  note,  cash);
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), uuid,code,value);
        return Response.successByArray();
    }

    @Override
    public Response couponsShow(String name, Integer pageNum) {
        if(StringUtils.isEmpty(name)||pageNum==null){
          logger.warn("参数为空");
          return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PageHelper.startPage(pageNum,10);
       List<Coupons> list =  pmsDao.findAllCoupons();
       PageInfo<Coupons> pageInfo = new PageInfo<>(list);
        return Response.success(pageInfo);
    }

    @Override
    public Response couponsAdd(String name, CouponsRequest couponsRequest) throws Exception {
        String uuid = UserUtil.generateUuid();
        couponsRequest.setUuid(uuid);
//        if (SysUtils.checkObjFieldIsNull(couponsRequest)){
//            logger.warn("参数为空");
//            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
//        }
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), uuid,OperationNoteTypeEnum.COUPONS_ADD.getCode(),OperationNoteTypeEnum.COUPONS_ADD.getValue());
        pmsDao.couponsAdd(couponsRequest);
        return Response.successByArray();
    }

    @Override
    public Response couponsUp(String name, CouponsRequest couponsRequest) {
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), couponsRequest.getUuid(),OperationNoteTypeEnum.COUPONS_UP.getCode(),OperationNoteTypeEnum.COUPONS_UP.getValue());
        pmsDao.couponsUp(couponsRequest);
        return Response.successByArray();
    }

    @Override
    public Response couponsDel(String name, String uuid) {
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), uuid,OperationNoteTypeEnum.COUPONS_DEL.getCode(),OperationNoteTypeEnum.COUPONS_DEL.getValue());
        pmsDao.couponsDel(uuid);
        return Response.successByArray();
    }

    @Override
    public Response rechargeWallet(String name, Integer userId, Double money) {
        AdminUser adminUser = pmsDao.findAdminByName(name);
        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作对象--操作标识--note
        pmsDao.addOperationNote(operationId,adminUser.getId(), String.valueOf(userId),OperationNoteTypeEnum.RECHARGE_WALLET.getCode(),OperationNoteTypeEnum.RECHARGE_WALLET.getValue());
        pmsDao.rechargeWallet(userId,money);
        return Response.successByArray();
    }

    @Override
    public Response trystSceneAdd(String name, TrystSceneRequest request) {
        try {
            if(StringUtils.isEmpty(name)||SysUtils.checkObjFieldIsNull(request)){
                logger.warn("参数为空");
              return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        String uuid = UserUtil.generateUuid();
        request.setUuid(uuid);
        pmsDao.addTrystScene(request);
        return Response.success();
    }

    @Override
    public Response trystVideoAdd(String name, String video, String videoPic, String typeValue, Integer typeCode) {
        if(StringUtils.isEmpty(name)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String uuid = UserUtil.generateUuid();
        pmsDao.addTrystVideo(video, videoPic, typeValue, typeCode, uuid);
        return Response.successByArray();
    }

    @Override
    public Response trystSceneShow(String name,Integer pageNum) {
        if(StringUtils.isEmpty(name)||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PageHelper.startPage(pageNum,10);
        List<TrystSceneResponse> list = pmsDao.showTrystScene();
        PageInfo<TrystSceneResponse> pageInfo = new PageInfo<>(list);

        return Response.success(pageInfo);
    }

    @Override
    public Response trystVideoShow(String name,Integer pageNum) {
        if(StringUtils.isEmpty(name)||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PageHelper.startPage(pageNum,10);
        List<TrystVideoHomeResponse> list = pmsDao.trystVideoShow();
        PageInfo<TrystVideoHomeResponse> pageInfo = new PageInfo<>(list);
        return Response.success(pageInfo);

    }

    @Override
    public Response trystVideoDel(String name, String uuid) {
        if(StringUtils.isEmpty(name)||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        pmsDao.trystVideoDel(uuid);
        return Response.successByArray();
    }

    @Override
    public Response trystSceneDel(String name, String uuid) {
        if(StringUtils.isEmpty(name)||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        pmsDao.trystSceneDel(uuid);
        return Response.successByArray();
    }


    public static void main(String[] args) {
        String  a = "abcdefg";
        System.out.println( a.replace("a","d"));
        StringBuffer stringBuffer = new StringBuffer(a);
        stringBuffer.reverse();
        System.out.println(stringBuffer);

//        Integer a = 15;
//
//        System.out.println(a/4*4);

    }

	@Override
	public Response insertVideoHomeSelective(String name,String video, String videoPic) {
		
		 //判断参数
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(video)||StringUtils.isEmpty(videoPic)){
			logger.warn("参数为空");
		    return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
		}
		
		String uuid = UserUtil.generateUuid();
		VideoHome videoHome = new VideoHome();
		videoHome.setUuid(uuid);
		videoHome.setVideoPic(videoPic);
		videoHome.setVideo(video);
		
		int i = pmsDao.insertVideoHomeSelective(videoHome);
		if(0 >= i){
			 return  Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "添加视频失败");
		}
		return Response.successByArray();
	}

	@Override
	public Response findIdentityProof(String name,Integer state,Integer useId,Integer pageNum) {
		if(StringUtils.isEmpty(name) ||pageNum == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
		
		IdentityProof proof = new IdentityProof();
		if(null != state){
			proof.setState(state);
		}
		if(null != useId){
			proof.setUserId(useId);
		}
		
        PageHelper.startPage(pageNum,10);
        List<IdentityProof> pList = pmsDao.findIdentityProof(proof);
        PageInfo<IdentityProof> pageInfo = new PageInfo<>(pList);
		
		
		return Response.success(pageInfo);
	}

	@Override
	public Response auditIdentityProof(String name,String uuid, Integer state,
			String idNumber, String realName, String satrtDate, String endDate) {
		if(StringUtils.isEmpty(name) || StringUtils.isEmpty(uuid) || state == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
		
		//查询指定的记录
		//根据UUID查询身份认证记录
				IdentityProof proof = new IdentityProof();
				proof.setUuid(uuid);
				proof.setState(0);
				List<IdentityProof> pList = pmsDao.findIdentityProof(proof);
				if(0 == pList.size()){
				 logger.warn("无效参数");
		       	 Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"无效参数");
				}
				
				proof = pList.get(0);//指定记录
		
		switch (state){
        case 1:
        	//认证成功 更新信息
           if(StringUtils.isEmpty(idNumber) || StringUtils.isEmpty(realName) || StringUtils.isEmpty(satrtDate) || StringUtils.isEmpty(endDate)){
        	   logger.warn("参数为空");
               return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
           }
           
           //校验时间格式
           SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
           boolean flag = true;
			try {
				format.parse(satrtDate);
				format.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
				flag = false;
			}
			
			//对身份证号和时间格式进行判断
	        if(!Regular.checkIdCardMatch(idNumber) || false == flag){
	        	   logger.warn("参数不合法");
	               return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数不合法");
	           }
	        //更新记录信息
	        proof.setIdNumber(idNumber);
	        proof.setRealName(realName);
	        proof.setSatrtDate(satrtDate);
	        proof.setEndDate(endDate);
	        proof.setState(1);
	        
	        int i = pmsDao.updateIdentityProofSelective(proof);
	        if(0 > i){
	        	return  Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "更新身份认证信息失败");
	        }
	        
           break;
        case 2:
             //认证失败 更新信息
        	proof.setState(2);
        	int j = pmsDao.updateIdentityProofSelective(proof);
	        if(0 > j){
	        	return  Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "更新身份认证信息失败");
	        }
            break;
        default:
            return  Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"state="+state+" 参数不正确");

       }
		return Response.successByArray();
	}
}
