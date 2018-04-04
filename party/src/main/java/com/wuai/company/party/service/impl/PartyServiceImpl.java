package com.wuai.company.party.service.impl;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.Response.MessageAllResponse;
import com.wuai.company.entity.Response.PartyDetailedInformationResponse;
import com.wuai.company.entity.Response.PartyHomePageResponse;
import com.wuai.company.entity.Response.PartyOrdersResponse;
import com.wuai.company.entity.TimeTask;
import com.wuai.company.entity.User;
import com.wuai.company.enums.*;

import com.wuai.company.message.RabbitMqPublishImpl;
import com.wuai.company.message.TransferData;
import com.wuai.company.party.dao.PartyDao;
import com.wuai.company.party.service.PartyService;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.util.Arith;
import com.wuai.company.util.Response;
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
 * Created by Administrator on 2017/6/14.
 */
@Service
@Transactional
public class PartyServiceImpl implements PartyService {
    @Autowired
    private PartyDao partyDao;
    @Autowired
    private UserDao userDao;
    Logger logger = LoggerFactory.getLogger(PartyServiceImpl.class);
    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;

    private static Integer pageSize=10;

    @Override
    public Response findAll(Integer id, Integer pageNum,String classify) {
        if (id==null||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        //根据 用户id 页码查询 party home-page
        List<PartyHomePageResponse> response = partyDao.findAll(id,pageNum,classify);

        return Response.success(response);
    }

    @Override
    public Response detailedInformation(Integer id, String uuid) {
        if (id==null|| StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PartyDetailedInformationResponse response = partyDao.findDetailedInformation2(id,uuid);
        List< MessageAllResponse> list = partyDao.messageAll(uuid,0,3);
        response.setMessages(list);
        return Response.success(response);
    }

    @Override
    public Response partyPay(Integer id, String uuid, String date, Double money,Integer boySize,Integer girlSize) {
        if (id==null||StringUtils.isEmpty(uuid)||StringUtils.isEmpty(date)||money==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PartyOrdersResponse response = partyDao.findPartyOrderByUidAndId(id,uuid);
        if (response!=null){
            logger.warn("已参加过party");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已参加过party");
        }
        String partyId = UserUtil.generateUuid();
        User user =  userDao.findUserByUserId(id);
        PartyDetailedInformationResponse party =  partyDao.findDetailedInformation(uuid);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date d = new Date();
        String currentDate = simpleDateFormat.format(d);
        String currentTime = timeFormat.format(d);
        if (currentDate.equals(date)){
            String time = party.getTime().split("\\|")[0];
            Long maxTime = null; //最大时间
            Long currentLong = null; //当前时间
            try {
                maxTime = timeFormat.parse(time).getTime();
                currentLong=timeFormat.parse(currentTime).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Double endTime = party.getEndTime()*60*1000*60; //最迟 报名时间限制
            //最大时间 - 最迟 报名时间 <= 当前时间 ---->则 不可报名参加
            long l = maxTime - endTime.longValue();
            if (l<=currentLong){
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已超过报名时间");
            }
        }
        Double boyMoney = Arith.multiplys(3,boySize,party.getBoyMoney());
        Double girlMoney = Arith.multiplys(3,girlSize,party.getGirlMoney());
        Double realMoney = Arith.add(3,boyMoney,girlMoney);
        if (!realMoney.equals(money)){
            logger.warn("Id={}金额错误 realMoney={},money={}",id,realMoney,money);
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"金额错误");
        }
        //添加订单到 party订单  partyID--用户id--party活动id--日期--金额--用户手机号--party标题--商家电话--支付状态
        partyDao.partyPay(partyId,id,uuid,date,party.getTime(),money,user.getPhoneNum(),party.getTopic(),party.getPhoneNum(), PartyPayCodeEnum.PARTY_PAY_WAIT.getCode(),boySize,girlSize);
        char c = OrdersTypeEnum.PARTY_PAY.getQuote();
        String type = String.valueOf(c);
        StringBuffer stringBuffer = new StringBuffer(type);
        stringBuffer.append(partyId);
        if(money==0){
//            String partyId = stringBuffer.substring(1,stringBuffer.length());
            PartyOrdersResponse res = userDao.findPartyByUid(partyId);

            /**
             * party订单到期后未使用取消订单
             */
            TransferData data = new TransferData();
            TimeTask task = new TimeTask();
            task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
            task.setTimeTaskName("partyCancel:"+res.getUuid());
            String m = res.getDate().split("-")[1];
            String dd = res.getDate().split("-")[2];
            String time = res.getTime().split("\\|")[0];
            String mm = time.split(":")[1];
            String hh = time.split(":")[0];
            task.setExecuteTime("0 "+mm+" "+hh+" "+dd+" "+m+" ?");
            PartyOrdersResponse partys = new PartyOrdersResponse();
            partys.setUuid(party.getUuid());
            party.setUuid("partyCancel:"+party.getUuid());
            task.setParams(JSON.toJSONString(partys));
            data.setData(JSONObject.toJSONString(task));
            data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
            rabbitMqPublish.publish(data);
            userDao.upPartyPay(partyId,PartyPayCodeEnum.PARTY_WAIT_CONFIRM.getCode());
        }
        return Response.success(stringBuffer);
    }

    @Override
    public Response groupBuying(Integer id, String uuid) {
        if (id==null||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PartyOrdersResponse response = partyDao.findPartyOrderByUidAndId(id,uuid);
       if (response==null){
           return Response.successByArray();
       }
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
//        String currentDate = simpleDateFormat.format(new Date());
//        String currentTime = timeFormat.format(new Date());
//        if (currentDate.equals(date)){
//            String time = response.getTime().split("/")[1];
//            Long maxTime = null; //最大时间
//            Long currentLong = null; //当前时间
//            try {
//                 maxTime = timeFormat.parse(time).getTime();
//                 currentLong=timeFormat.parse(currentTime).getTime();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            Double endTime = response.getEndTime()*60*1000*60; //最迟 报名时间限制
//            //最大时间 - 最迟 报名时间 <= 当前时间 ---->则 可报名参加
//            if (Arith.subtract(2,maxTime,endTime)<=currentLong){
//                return Response.successByArray();
//            }
//        }
        Integer personCount = partyDao.findPartyByDateAndUid(uuid,response.getDate());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date",response.getDate());
        jsonObject.put("time",response.getTime());
        jsonObject.put("boySize",response.getBoySize());
        jsonObject.put("girlSize",response.getGirlSize());
        jsonObject.put("personCount",personCount);
        return Response.success(jsonObject);
    }

    @Override
    public Response cancelParty(Integer id, String uuid) {
        if (id==null||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        PartyOrdersResponse partyOrdersResponse = partyDao.findPartyOrdersByUid(uuid);

        userDao.upPartyPay(uuid,PartyPayCodeEnum.PARTY_CANCEL.getCode());
        userDao.updateMoney(Double.valueOf(partyOrdersResponse.getMoney()),id);
        return Response.successByArray();
    }

    @Override
    public Response classify() {
        String value = partyDao.findSysValue(SysKeyEnum.PARTY_CALSSIFY.getKey());
        return Response.success(value);
    }

    @Override
    public Response detailedInformationWeb(String uuid) {
        PartyDetailedInformationResponse response = partyDao.findDetailedInformation(uuid);
        return Response.success(response);
    }

    @Override
    public Response collection(Integer id, String uuid,Integer collect) {
        if (id==null||StringUtils.isEmpty(uuid)){
            logger.warn("收藏 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        switch (collect){
            case 0:
                partyDao.delCollection(id,uuid);
                break;
            case 1:
                partyDao.addCollection(id,uuid);
                break;
        }


        return Response.successByArray();
    }

    @Override
    public Response addMessage(Integer id, String partyId, String message) {
        if (id==null||StringUtils.isEmpty(partyId)||StringUtils.isEmpty(message)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Integer messageSize = partyDao.myMessageSize(id,partyId);
        // TODO: 2018/1/4 个人最多回复条数待定

        Integer num = 3;
        if (messageSize<num){
//            synchronized (this){
                String uuid = UserUtil.generateUuid();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = simpleDateFormat.format(new Date());
                partyDao.addMessage(id,uuid,message,partyId,date);
//            }

            return Response.successByArray();
        }
        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"最多回复"+num+"条");

    }

    @Override
    public Response messageAll(Integer id, String uuid,Integer pageNum) {
        if (id==null||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
       List< MessageAllResponse> messageAllResponse = partyDao.messageAll(uuid,pageNum,pageSize);
        return Response.success(messageAllResponse);
    }

    @Override
    public Response myCollections(Integer id, Integer pageNum) {
        if (id==null||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<PartyHomePageResponse> list = partyDao.myCollections(id,pageNum);
        return Response.success(list);
    }

    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
//        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
//        String currentDate = simpleDateFormat.format(new Date());
//        String tim = timeFormat.format(new Date());
//        timeFormat.parse(tim).getTime();
//
//        System.out.println(currentDate);
//        System.out.println(tim);
//        Double a = 0.5*60*1000*60;
//        Long b = a.longValue();
//        Long s =timeFormat.parse("17:15").getTime()+b;
//        System.out.println(timeFormat.parse("17:15").getTime());
//        System.out.println(s);
//        System.out.println(timeFormat.parse("17:45").getTime());
        String party = "20:00|22:10";
        String time = party.split("\\|")[0];
        System.out.println(time);
    }
}

