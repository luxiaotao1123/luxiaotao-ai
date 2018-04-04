package com.wuai.company.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.RechargeBill;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.gexin.rp.sdk.base.uitls.Base64Util;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mysql.jdbc.util.Base64Decoder;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.*;
import com.wuai.company.entity.request.ActiveEnterRequest;
import com.wuai.company.entity.request.IdentityProof;
import com.wuai.company.entity.request.UploadWorkProofRequest;
import com.wuai.company.enums.*;
import com.wuai.company.message.RabbitMqPublishImpl;
import com.wuai.company.message.TransferData;
import com.wuai.company.task.job.TaskOrderCalcBus;
import com.wuai.company.user.EaseMob.api.ChatGroupAPI;
import com.wuai.company.user.auth.MyRealm;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.entity.Response.BillDetailResponse;
import com.wuai.company.user.entity.Response.DetailResponse;
import com.wuai.company.user.entity.Response.OrdersUResponse;
import com.wuai.company.user.entity.Response.StoreBillDetailResponse;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.user.service.UserService;
import com.wuai.company.user.util.MobileMessageCheck;
import com.wuai.company.user.util.MobileMessageSend;
import com.wuai.company.user.util.NoticeUtil;
import com.wuai.company.user.util.ZhimaCredit;
import com.wuai.company.util.*;
import com.wuai.company.util.comon.SimpDate;
import com.wuai.company.util.comon.SimpDateFactory;
import com.wuai.company.util.comon.TimeUtil;
import com.wuai.company.util.comon.factory.CalculationFactory;
import com.wuai.company.util.comon.factory.Cost;

import io.swagger.client.model.Group;
import io.swagger.client.model.UserName;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.wuai.company.util.UserUtil.parseLastOrderNo;
import static java.util.stream.Collectors.toList;


//import com.wuai.company.entity.User;

/**
 * 订单的service具体实现层
 * Created by Ness on 2017/5/25.
 */


@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ChatGroupAPI chatGroupAPI;
    @Resource
    private HashOperations<String, String, String> orderHashRedisTemplate;
    @Autowired
    private MyRealm myRealm;
    @Autowired
    private RabbitMqPublishImpl rabbitMqPublish;
    @Resource
    private HashOperations<String, String, Orders> taskHashRedisTemplate;
    @Resource
    private ZSetOperations<String,String> undoneRedisTemplate;

    @Autowired
    private TaskOrderCalcBus taskOrderCalcBus;
    @Resource
    private ZSetOperations<String,String> msgValueTemplate;
    @Resource
    private HashOperations<String, String, NearBodyResponse[]> nearBodyTemplate;
    @Resource
    private HashOperations<String, String, NearBodyResponse> totalNearBodyTemplate;
    @Resource
    private HashOperations<String,String,SnatchUser> snatchUserTemplate;
    @Resource
    private RedisTemplate removeRedisTemplate;

    @Autowired
    private  NoticeUtil noticeUtil ;
    @Value("${sys.robotization}")
    private Integer robotization;
    
    @Value("${aliPay.app.id}")
    private String app_id;
    @Value("${aliPay.partner}")
    private String partner;
    @Value("${aliPay.notifyUrl}")
    private String notifyUrl;
    @Value("${aliPay.public.key}")
    private String aliPay_public_key;
    @Value("${aliPay.private.key}")
    private String aliPay_private_key;
    
    @Value("${sys.defaultVideo}")
    private String defaultVideo; //默认视频地址

    private final String SCENENAME_ORDERS_PERHAPS_SELTIMETYPE = "%s:%s:orders:%s:%s";  // 用户id--场景名--订单--邀约/应约--固定时间/特定时间
    private final String USER_UNDONE_PERHAPS_SCENES = "%s:undone:%s:%s"; //用户id--未完成--邀约或应约--场景
    private final String USER_MSG = "%s:msg"; //用户id--信息列表
    private final String USER_TYPE_ORDERS = "%s:%s:%s"; //用户id--类型:发出 0 或接受 1--订单号
    private final String SNATCH_USER_TRYST = "snatch:user:%s"; //tryst 订单号
    private final static String DEFAULT_ACCOUNT_NUM="未绑定";
    private final String NEARBY_ID_BODY_TRYST="nearby:%s:body:%s";//用户id--约会id
    private final String NEARBY_TOTAL_BODY_ADDRESS="nearby:body:%s";//地区
    private final String NEARBY_TOTAL_CITY_DISTRICT="nearby:total:%s:%s";//地区
    private final String NEARBY_TOTAL_CITY="nearby:total:%s";//地区
    private final String MSG_CONTENT_TRYST = "%s:%s:%s:%s:%s";
    private final String USER_MSG_TRYST = "tryst:%s:msg"; //用户id--信息列表
    private final String TOBECOMPLETE_TRYST = "您抢的%s订单已生效，请准时赴约";

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static Integer defaultInt=8;
    private final String DEVICE_PASS_KEY = "MyPassWo";
    /**
     * 根据Uuid查询用户信息
     *
     * @param id 用户的id
     * @return
     */
    public Response findUserOneById(Integer id) {
        if (id==null) {
            logger.warn("查询用户信息Id为空");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }


        logger.info("开始查询用户Id为={}的用户信息", id);

        User user = userDao.findUserOneById(id);
        logger.info("结束查询用户Id为={}的用户信息", id);

        return user == null ? Response.successByArray() : Response.success(user);


    }


    /**
     * 保存用户信息
     *
     * @param user
     */
    public Response saveUser(User user) {


        if (user == null || StringUtils.isEmpty(user.getUuid())) {
            logger.warn("保存用户信息的实体类为空");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }

        logger.info("开始保存Id={}用户信息", user.getUuid());

        userDao.saveUser(user);

        logger.info("结束保存Id={}用户信息", user.getUuid());


        return Response.successByArray();


    }


    /**
     * 更新用户信息
     *
     * @param user
     */
    public Response updateUserOneById(User user) {

        if (user == null || StringUtils.isEmpty(user.getUuid())) {
            logger.warn("更新用户信息的实体类为空");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }

        logger.info("开始更新Id={}用户信息", user.getUuid());

        userDao.updateUserOneById(user);

        logger.info("结束更新Id={}用户信息", user.getUuid());


        return Response.successByArray();

    }

    @Override
    public Response findUserByUserId(Integer userId) {
        User user = userDao.findUserByUserId(userId);
        //我的所有 视频
        List<UserVideoResponse> list = userDao.findVideos(userId);
        user.setVideos(list);
        //当视频集为空时将 user中的video换为默认视频
        if(0 == list.size()){
        	user.setVideo(defaultVideo);
        }
        if (user==null){
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        return Response.success(user);
    }


    //添加用户基础信息
    @Override
    public Response insertUserBasicData(Integer uid, String nickName, Integer gender, String age, String occupation, String height, String weight, String city, String zodiac,String label,String signature) {

        if (uid==null)
        {
            logger.warn("添加用户基础信息为空");
            Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        if(signature.length() > 15){
        	logger.warn("个性签名长度不合法");
            Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
            userDao.insertUserBasicData(uid, nickName, gender, age, occupation, height, weight, city, zodiac,label,signature);
        return Response.successByArray();
    }

    //添加照片
    @Override
    public Response addPicture(Integer uid, String picture) {
        if (uid==null||StringUtils.isEmpty(picture))
        {
            logger.warn("添加照片信息为空");
            Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
            userDao.addPicture(uid,picture);
        return Response.successByArray();
    }

    //修改密码
    @Override
    public Response updatePass(Integer uid, String oldPass, String newPass) {
        if (uid==null || StringUtils.isEmpty(oldPass) || StringUtils.isEmpty(newPass)) {
            logger.warn("修改密码 信息为空");
            Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(), "");
        }
        //解密旧密码
        String dePass =  DesUtil.decrypt(oldPass,DEVICE_PASS_KEY);
        //解密新密码
        String deNewPass =  DesUtil.decrypt(newPass,DEVICE_PASS_KEY);
        if (!Regular.checkPass(dePass)||!Regular.checkPass(deNewPass)){
            logger.warn("密码格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "密码格式错误");
        }
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));

        User user = userDao.selectPass(uid, MD5Pass);
        if (user == null || user.equals("")) {
            logger.warn("修改密码 密码错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "密码错误");
        } else {
            String newP = MD5.encryption(MD5.encryption(deNewPass));
            userDao.updatePass(uid, newP);
            return Response.successByArray();
        }
    }
    //提交反馈意见
    @Override
    public Response addFeedback(Integer uid, String feedback) {
        if (uid==null||StringUtils.isEmpty(feedback)){
            logger.warn("添加反馈 信息为空");
            Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(), "");
        }
            User user = userDao.findUserByUserId(uid);
        if (user==null){
            logger.warn("用户不存在");
            Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户信息不存在");
        }
            userDao.addFeedback( UserUtil.generateUuid(),uid,feedback,user.getNickname(),user.getPhoneNum());
        return Response.successByArray();
    }

    @Override
    public Response recharge(Integer uid, Double money) {
        if (uid==null){
            logger.warn("充值 信息为空");
            Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        User user = userDao.findUserByUserId(uid);
        if (user==null){
            logger.warn("充值 账号不存在");
            Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"账号 不存在");
        }

        //充值订单
        String uuid = UserUtil.generateUuid();
        userDao.addRechargeOrders(uuid,uid,money,UserGradeEnum.NORMAL_PERSON.toCode());
        //当前金额加充值金额
//        Double amountMoney = Arith.add(2,user.getMoney(),money);
//        userDao.updateMoney(money,uid);
//        User userM = userDao.findUserByUserId(uid);
//        return Response.success(userM.getMoney());

        char c = OrdersTypeEnum.RECHARGE.getQuote();
        String type = String.valueOf(c);
        StringBuffer stringBuffer = new StringBuffer(type);
        stringBuffer.append(uuid);
        return Response.success(stringBuffer);
    }

    //绑定支付宝
    @Override
    public Response bindingAliPay(Integer uid, String realName, String accountNum) {
        if (uid==null||StringUtils.isEmpty(realName)||StringUtils.isEmpty(accountNum)){
            logger.warn("绑定支付宝 信息为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        User user = userDao.findUserByUserId(uid);
        if (user==null){
            logger.warn("用户不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"");
        }
        userDao.bindingAliPay(uid,realName,accountNum);
        return Response.successByArray();
    }

    //账单明细
    @Override
    public Response billDetail(Integer uid,Integer pageNum) {
        if (uid==null){
            logger.warn("账单明细 信息为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        //约会订单明细
        List<BillDetailResponse> list = userDao.findBillDetail(uid,pageNum);
        for (int i=0;i<list.size();i++){
            BillDetailResponse billDetailResponse = list.get(i);
            String type = UserUtil.valueById(billDetailResponse.getPayType());
            if (billDetailResponse.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode()&&billDetailResponse.getPayType().intValue()==PayTypeEnum.STR_WAIT_PAY.toCode()){
                type="待支付";
            }
            billDetailResponse.setPayed(type);
            List<OrdersUResponse> invitation = userDao.findByUuid(billDetailResponse.getOrdersId());

            billDetailResponse.setIcon(userDao.findUserByUserId(uid).getIcon());
            billDetailResponse.setOrderUser(invitation);
        }

        //商城订单
        List<StoreBillDetailResponse> store = userDao.findStoreBillDetail(uid,pageNum);
        for (int j =0;j<store.size();j++){
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date time = null;
//            try {
//                time = simpleDateFormat.parse(store.get(j).getTime());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            String ti=simpleDateFormat.format(time);
//            store.get(j).setTime(ti);
            if (store.get(j).getType()==PayTypeEnum.STORE_WAIT_PAY.toCode()){
                store.get(j).setPayType(PayTypeEnum.STORE_WAIT_PAY.getValue());
            }
            if (store.get(j).getType()==PayTypeEnum.STORE_WAIT_CONFIRM.toCode()){
                store.get(j).setPayType(PayTypeEnum.STORE_WAIT_CONFIRM.getValue());
            }
            if (store.get(j).getType()==PayTypeEnum.STORE_SUCCESS.toCode()){
                store.get(j).setPayType(PayTypeEnum.STORE_SUCCESS.getValue());
            }
            if (store.get(j).getType()==PayTypeEnum.STORE_CANCEL.toCode()){
                store.get(j).setPayType(PayTypeEnum.STORE_CANCEL.getValue());
            }
//            if (store.get(j).getType()==PayTypeEnum.STR_ON_THE_WAY.toCode()){
//                store.get(j).setPayType(PayTypeEnum.STR_ON_THE_WAY.getValue());
//            }
//            if (store.get(j).getType()==PayTypeEnum.STR_START.toCode()){
//                store.get(j).setPayType(PayTypeEnum.STR_START.getValue());
//            }

        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("invitation",list);

        map.put("store",store);
        return Response.success(map);
    }

    @Override
    public Response login(String phoneNum, String pass,String equipment,Integer sendDeviceType,HttpServletResponse response) {
        if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(pass)|StringUtils.isEmpty(equipment)|sendDeviceType==null){
            logger.warn("登录 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if (!Regular.checkPhone(phoneNum)){
            logger.warn("登陆 手机号格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"手机号格式错误");
        }

        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);
        if (!Regular.checkPass(dePass)){
            logger.warn("登陆 密码格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码格式错误");
        }
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));

        User user = userDao.findUserOneByPhone(phoneNum);
        if (user==null){
            logger.warn("登录 用户不存在");
            return Response.error(ResponseTypeEnum.LOGIN_ERROR.toCode(),"用户名不存在");
        }
        if (myRealm.load(phoneNum,MD5Pass,equipment,response)){
            userDao.bindingEquipment(user.getId(),equipment,sendDeviceType);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("userGrade",user.getUserGrade());
            map.put("userId",user.getId());
            map.put("nickname",user.getNickname());
            return Response.success(map);
        };
//        if (user.getLoadPass().equals(MD5Pass)){
//
//
//        }

        logger.warn("登录 密码错误");
        return Response.error(ResponseTypeEnum.LOGIN_ERROR.toCode(),"用户名或密码错误");
    }

    //注册
    @Override
    public Response register(String phoneNum, String pass, String code, String invitationCode) {
        if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(pass)||StringUtils.isEmpty(code)){
            logger.warn("注册参数 信息为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"注册参数 信息为空");
        }
        if(!Regular.checkPhone(phoneNum)){
            logger.warn("注册 账号不符合要求");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"注册 账号不符合要求");
        }
        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);


        //解密用户等级 默认为普通用户：0
        Integer userGrade =0;
        if (StringUtils.isNotEmpty(invitationCode)){
            userGrade =  UserUtil.decrypt(invitationCode);
        }
        try {
            if (MobileMessageCheck.checkMsg(phoneNum,code)){
                logger.info("验证码验证成功");
                assert dePass != null;
                String MD5Pass = MD5.encryption(MD5.encryption(dePass));
                User user = userDao.findUserOneByPhone(phoneNum);
                if (user == null){
                    String uuid = UserUtil.generateUuid();
                    String loadName = UserUtil.generateName();
                    //默认昵称
                    String defaultNickName = UserUtil.randomString(defaultInt);
                    userDao.register(uuid,phoneNum,loadName,MD5Pass,userGrade,defaultNickName);
                    return Response.successByArray();
                }else
                if (user.getPhoneNum().equals(phoneNum)){
                    logger.warn("注册参数 账号已存在");
                    return Response.error(ResponseTypeEnum.REGISTER_ERROR.toCode(),"注册失败，该手机号已存在");
                }
            }else {
                logger.warn("验证码验证失败");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"验证码错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.warn("注册失败");
        return Response.error("注册失败");


//        if (Regular.checkPhone(phoneNum)){
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"手机号格式错误");
//        }
        //获取存储的验证码
//        String verificationCode = orderHashRedisTemplate.get(phoneNum,phoneNum);
//        if (verificationCode==null){
//            logger.warn("注册参数 验证码不存在");
//            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"验证码不存在");
//        }
//        if (code.equals(verificationCode)){
//            // 验证码验证完成删除 验证码
//            orderHashRedisTemplate.delete(phoneNum,phoneNum);

//            String uuid = UserUtil.generateUuid();
//            userDao.register(uuid,phoneNum,MD5Pass,userGrade);


//        }else {
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"验证码不匹配");
//        }


    }

    //发送 验证码 生成验证码
    @Override
    public Response sendMsg(String phoneNum) {
        try {
            if (MobileMessageSend.sendMsg(phoneNum)){
                return Response.successByArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.success(ResponseTypeEnum.EMPTY_PARAM.toCode());

      /**
        Double a = new  Random().nextDouble();
        String b = String.valueOf(a);
        String c =  b.substring(b.length()-4,b.length());
        logger.info("=========================模拟短信验证码============================");
        logger.info("code="+c);
        logger.info("=========================模拟短信验证码============================");

        if (orderHashRedisTemplate.get(phoneNum,phoneNum)!=null){
            orderHashRedisTemplate.delete(phoneNum,phoneNum);
        }
        orderHashRedisTemplate.put(phoneNum,phoneNum,c);
        Map<String,String> map = new HashMap<String,String>();
        map.put("code",c);
        return Response.success(map);
       */
    }

    //忘记密码
    @Override
    public Response forgetPass(String phoneNum, String pass) {
        if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(pass)){
            logger.warn("忘记密码 信息为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);

        if (!Regular.checkPass(dePass)){
            logger.warn("忘记密码 密码格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码格式错误");
        }
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));


        if (userDao.findUserOneByPhone(phoneNum)!=null){
            userDao.forgetPass(phoneNum,MD5Pass);
            return Response.successByArray();
        }else {
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(), "该用户不存在");

        }
    }

    @Override
    public Response findMoney(Integer uid) {
        User userM = userDao.findUserByUserId(uid);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("money",String.valueOf(userM.getMoney()));
        map.put("phone",userM.getAccountNum());

        return Response.success(map);
    }

    //验证码验证
    @Override
    public Response verificationCode(String phoneNum, String code) {
        try {
           if ( MobileMessageCheck.checkMsg(phoneNum,code)){
               logger.info("验证码验证成功");
               return Response.successByArray();
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if (code.equals(orderHashRedisTemplate.get(phoneNum,phoneNum))){
//            orderHashRedisTemplate.delete(phoneNum,phoneNum);
//            return Response.successByArray();
//        };
        logger.warn("验证码验证失败 phoneNum={}",phoneNum);
        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"验证码验证失败");
    }

    @Override
    public Response addIcon(Integer uid, String icon) {
        if (uid==null||StringUtils.isEmpty(icon))
        {
            logger.warn("添加头像 信息为空");
            Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        userDao.addIcon(uid,icon);
        return Response.successByArray();
    }

    /**
     * 根据订单id 查询订单 并支付
     * @param orderNo
     * @return
     */
    @Override
    public String findDetailByUid(String orderNo,Double totalFee,Integer payTypeCode) {
        logger.info("--->商城订单 支付<---");
        //根据订单id查找 待支付的商城订单
        StoreOrders storeOrders = userDao.findStoreOrders(orderNo);
        if (storeOrders!=null){
            //将商城订单 支付状态改为 1
            userDao.payStoreOrders(orderNo,PayTypeEnum.STORE_WAIT_CONFIRM.toCode());

            Double money = -Math.abs(storeOrders.getMoney());
            //添加到明细表 订单号，用户id，商家id，金额 收入 0 或者支出 1
            //添加 订单明细
            User user = userDao.findUserByUserId(storeOrders.getUserId());
            String detailId2 = UserUtil.generateUuid();
            //订单明细id--用户uuid--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
            userDao.addOrdersDetail(detailId2,user.getUuid(),money,storeOrders.getUserId(), OrdersDetailTypeEnum.STORE_PAY.getKey(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.STORE_PAY.getValue(),OrdersDetailTypeEnum.STORE.getKey());

//            userDao.addDetails(orderNo,storeOrders.getUserId(),storeOrders.getMerchantId(),storeOrders.getMoney(),0);
            /*MerchantUser merchantUser = userDao.findMerchantByUid(storeOrders.getMerchantId());
            //用户 购买成功 推送商家
            if (merchantUser.getCid()!=null) {
                Push push = new Push();
                push.setDeviceNum(merchantUser.getCid());
                push.setSendDeviceType(merchantUser.getType());
                push.setSendTopic("您有订单需要确认");
                push.setSendContent("请确认订单");
                PushUtils.storePush.getInstance().sendPush(push);
            }*/
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
            SimpDate simple = SimpDateFactory.endDate();
            Date today = new Date();
            //七天后
            String key =  userDao.getSysParameter(SysKeyEnum.COMBO_END_TIME.getKey());
            Integer afterDate = Integer.parseInt(key);
            //更新 有效期时间
            String date = simpleDateFormat.format(new Date(today.getTime()+afterDate * 24 * 60 * 60 * 1000));
            userDao.updateComboEndTime(orderNo,date);

            Map<String,String> transformTime = null;
            try {
                transformTime=simple.transformTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TransferData data = new TransferData();
            TimeTask task = new TimeTask();
            task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
            task.setTimeTaskName("storeCycle:"+orderNo);
            task.setExecuteTime("0 "+transformTime.get("mm")+" "+transformTime.get("HH")+" "+transformTime.get("dd")+" "+transformTime.get("MM")+" ?");
            storeOrders.setUuid("storeCycle:"+orderNo);
            task.setParams(JSON.toJSONString(storeOrders));
            data.setData(JSONObject.toJSONString(task));
            data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
            rabbitMqPublish.publish(data);
            if(payTypeCode.equals(PayTypeEnum.PAY_ZFB.toCode())) {
                String uuid = UserUtil.generateUuid();
                Double moneyFee = Math.abs(totalFee);
                logger.info("添加到充值明细 订单号={},金额={},充值类型={}",orderNo,moneyFee,DetailTypeEnum.STORE.toCode());
                //用户id---订单号---金额---充值明细订单号---订单类型code---订单类型---支付类型code---支付类型---充值成功或失败
                userDao.addRechargeSheet(storeOrders.getUserId(),orderNo,moneyFee,uuid, DetailTypeEnum.STORE.toCode(),DetailTypeEnum.STORE.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
            }
            return "ok";
        }

        return "fail";
    }

    @Override
    public Response manageLogin(String name, String pass, String uid, Integer sendDeviceType, HttpServletResponse response) {
        if(StringUtils.isEmpty(name)||StringUtils.isEmpty(pass)|StringUtils.isEmpty(uid)|sendDeviceType==null){
            logger.warn("登录 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM);
        }
        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));
        Boolean bo = myRealm.load(name,MD5Pass,uid,response);
        if (bo){
            MerchantUser merchantUser = userDao.findMerchantByName(name);
            if (merchantUser==null){
                logger.warn("登录 用户不存在");
                return Response.error(ResponseTypeEnum.LOGIN_ERROR.toCode(),"用户名或密码错误");
            }
            if (merchantUser.getPassword().equals(MD5Pass)){
                userDao.bindingManageEquipment(merchantUser.getUuid(),uid,sendDeviceType);

                return Response.success(merchantUser);
            }
        }

        logger.warn("登录 密码错误");
        return Response.error(ResponseTypeEnum.LOGIN_ERROR.toCode(),"用户名或密码错误");
    }

    @Override
    public Response bill(Integer attribute, Integer pageNum) {

        return null;
    }

    @Override
    public Response withdrawCash(Integer id, String realName, String accountNum, Double money) {
        if (id==null||money==null||StringUtils.isEmpty(realName)||StringUtils.isEmpty(accountNum)){
            logger.warn("用户 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserByUserId(id);
        if (user.getPayPass()==null){
            return Response.error(ResponseTypeEnum.PAY_PASS.toCode(),"未设置支付密码");
        }
        if (!user.getRealName().equals(realName)||!user.getAccountNum().equals(accountNum)){
            logger.warn("用户 提现失败");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"提现失败,请核对姓名及支付宝账号");
        }
        if (user.getMoney()<money){
            logger.warn("用户 提现金额超出");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"提现失败,提现金额超出");
        }
        //提交提现 申请
        String uuid = UserUtil.generateUuid();

        userDao.subtractMoney(id,money);
        userDao.withdrawCash(id,money, WithDrawCashTypeEnum.COMMON_MEMBER.toCode(),uuid);
        return Response.successByArray();
    }

    @Override
    public String addInvitaion(String orderNo,Double totalFee,Integer payTypeCode) throws ParseException {

        logger.info("--->支付完成 开始订单处理<---");
//        String[] ordersIds = orderNo.split(":");
        String tab = null;
//        if (ordersIds.length==2){
//            orderNo=ordersIds[0];
//            tab=ordersIds[1];
//        }
        //根据订单id查找 待支付约会订单；
//        Orders orders = userDao.findOrderByOrderNo(orderNo);
        Orders orders = userDao.findOrdersByVersion(orderNo);
        if (orders==null){
            Integer userIdLength = Integer.valueOf(orderNo.substring(orderNo.length()-1));
            Integer len = String.valueOf(userIdLength).length();
            Integer length = userIdLength+len;
            tab =  orderNo.substring(orderNo.length()-length,orderNo.length()-len);
            orderNo = orderNo.substring(0,orderNo.length()-length);
            orders = userDao.findOrdersByVersion(orderNo);
            if (orders==null){
                logger.warn("订单信息为空orderNo="+orderNo);
                return "fail";
            }
        }
        // TODO: 2018/1/12 测试关闭 
//        if (orders.getUpdateMoney()==0&&totalFee!=orders.getMoney()){
//            logger.warn("应付金额与实付金额不相符");
//            return "fail";
//        }
//        if (orders.getUpdateMoney()!=0&&totalFee!=orders.getUpdateMoney()){
//            logger.warn("应付金额与实付金额不相符");
//            return "fail";
//        }
        if (orders.getUpdateMoney()!=0){
            Double lastMoney =null;
            if (orders.getMoney()<0){
                lastMoney=-orders.getMoney();
            }else {
                lastMoney=orders.getMoney();
            }
            Double money = -(orders.getUpdateMoney()+lastMoney);
            orders.setMoney(money);
            logger.info("将updateMoney的金额重新清零");
            Double resetUpdateMoney=0.0;
            userDao.resetUpdateMoney(orders.getUuid(),resetUpdateMoney);
        }

        String preNo=null;

        if (orders==null){
            logger.warn("订单信息为空");
            return "fail";
        }
        else  if (!orders.getUuid().equals(orders.getVersion())){
            logger.info("订单格式转换uuid={}"+parseLastOrderNo(orders.getVersion()));
            preNo=parseLastOrderNo(orders.getVersion());
        }
        //将约会订单 支付状态改为 1
        userDao.payOrder(orders.getUuid(), PayTypeEnum.STR_WAIT_CONFIRM.toCode());
        userDao.updateOrdersUpdateMoney(orders.getUuid(),orders.getMoney());

        /*************************** -- 存储我的最接近当前时间的订单 -- *************************************/
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date=simpleDateFormat.parse(orders.getStartTime());
        Long timeStemp = date.getTime();
        //根据订单 hashkey, 开始时间戳 获取 orders
        Scene scene = userDao.findSceneByValue(orders.getScenes());
        Orders valueOrders =null;
       Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()));
       String key =null;
       for (String str :set){
           key = String.valueOf(str);
       }
        Orders upToDateOrders = userDao.findStartTimeLimitOne(orders.getUserId(),SelTimeTypeEnum.FIXED.getCode(),orders.getScenes());
       if (key!=null){
             valueOrders = taskHashRedisTemplate.get(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),key);
        };
            if (upToDateOrders!=null){
                upToDateOrders.setHourlyFee( scene.getHourlyFee());
                String type = UserUtil.valueById(upToDateOrders.getPayType());
                upToDateOrders.setType(type);
                upToDateOrders.setType(PayTypeEnum.STR_WAIT_CONFIRM.getValue());
            }
        if (valueOrders==null||
                key==null){
            taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getUserId(),orders.getScenes(),orders.getPerhaps(),orders.getSelTimeType()),String.valueOf(timeStemp),upToDateOrders);
        }else {
            Date ordersInRedis = simpleDateFormat.parse(valueOrders.getStartTime());
            Long preTime = ordersInRedis.getTime();
            //若 订单的开始时间 <= 已存在 redis里的 订单开始时间则 更新
            taskHashRedisTemplate.delete(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,valueOrders.getUserId(),valueOrders.getScenes(),valueOrders.getPerhaps(),valueOrders.getSelTimeType()),String.valueOf(preTime));
            if (upToDateOrders!=null) {
                taskHashRedisTemplate.put(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE, orders.getUserId(), orders.getScenes(), orders.getPerhaps(), orders.getSelTimeType()), String.valueOf(timeStemp), upToDateOrders);
            }
        }
        /*************************** -- 存储我的最接近当前时间的订单 -- *************************************/
        if (orders!=null){
            /***********************加入预加载计算 Start *****************************/
//            TaskOrderCalcBus taskOrderCalcBus = new TaskOrderCalcBus();
//            taskOrderCalcBus.addOrders(orders);
            /***********************加入预加载计算  End  *****************************/

//            Scene sc = userDao.findSceneByKey(String.valueOf(orders.getScenes()));  //获取该场景的参数
            Scene sc = userDao.findSceneByValue(orders.getScenes());  //获取该场景的参数
            //时间计算
            SimpDate simple = SimpDateFactory.endDate();

        /*==========================================================================================*/
       /*================================存====入====redis===str===================================*/
       /*==========================================================================================*/
           logger.info("调整习惯场景排序");
            Map<String, String> map2 = new LinkedHashMap<String, String>();

            //获取所有的keys
            Set<String> keys = orderHashRedisTemplate.keys(String.valueOf(orders.getUserId()));
            String scenes = null;
            for (String val : keys) {
                //返回缓存值
                if (orderHashRedisTemplate.get(String.valueOf(orders.getUserId()),val).equals(orders.getScenes())){
                    scenes=val;
                };
                map2.put(val, orderHashRedisTemplate.get(String.valueOf(orders.getUserId()), val));
            }
            //重新排序 存入redis默认值 习惯排序
            Map<String,String> maps = new LinkedHashMap<>();
            maps.put("0",orderHashRedisTemplate.get(String.valueOf(orders.getUserId()),scenes));

            for (String va : keys) {

                if (Integer.valueOf(scenes)>Integer.valueOf(va)){
                    maps.put(String.valueOf(Integer.valueOf(va)+1),orderHashRedisTemplate.get(String.valueOf(orders.getUserId()),va));
                }
                if (Integer.valueOf(scenes)<Integer.valueOf(va)){
                    maps.put(String.valueOf(Integer.valueOf(va)),orderHashRedisTemplate.get(String.valueOf(orders.getUserId()),va));
                }

            }
            orderHashRedisTemplate.putAll(String.valueOf(orders.getUserId()),maps);
        /*==========================================================================================*/
       /*================================存====入====redis===end===================================*/
       /*==========================================================================================*/



            //创建订单id
            User user = userDao.findUserByUserId(Integer.valueOf(orders.getUserId()));
            String uuid = UserUtil.generateOrderNo(OrdersTypeEnum.INVITE_ORDER,user.getId());
            //截取结束时间的时分秒
//            Integer endHourly = robotization+orders.getOrderPeriod();
            String endTime = simple.endDate(orders.getStartTime(),orders.getOrderPeriod(),0);
            Map<String,String> startTransformTime=simple.transformTime(orders.getStartTime());
            Map<String,String> sendTimeTransformTime=simple.transformTime(endTime);
            //若开始时间前 响应人数未 到达指定 人数 取消订单
            TransferData data1 = new TransferData();
            TimeTask task1 = new TimeTask();
            if (preNo==null) {
                task1.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
            }else {
                task1.setScheduleOperaEnum(ScheduleOperaEnum.UPDATE_TASK);
            }
            task1.setTimeTaskName("lastInvitation:"+orders.getUuid());
            task1.setExecuteTime("0 "+startTransformTime.get("mm")+" "+startTransformTime.get("HH")+" "+startTransformTime.get("dd")+" "+startTransformTime.get("MM")+" ?");
            Orders ordersTimeEntity = new Orders();
            ordersTimeEntity.setUserId(orders.getUserId());
            //未达到人数要求
            ordersTimeEntity.setUuid("missCatchPersonCount:"+orders.getUuid());
            ordersTimeEntity.setScenes(scenes);
            ordersTimeEntity.setGratefulFree(orders.getGratefulFree());
            ordersTimeEntity.setHourlyFee(sc.getHourlyFee());
            ordersTimeEntity.setOrderPeriod(orders.getOrderPeriod());
            ordersTimeEntity.setSelTimeType(orders.getSelTimeType());
            ordersTimeEntity.setPerhaps(orders.getPerhaps());
            ordersTimeEntity.setStartTime(orders.getStartTime());
            task1.setParams(JSON.toJSONString(ordersTimeEntity));
            data1.setData(JSONObject.toJSONString(task1));
            data1.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
            rabbitMqPublish.publish(data1);


            //到达结束时间自动结算
            TransferData data = new TransferData();
            TimeTask task = new TimeTask();
            if (preNo==null) {
                task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
            }else {
                task.setScheduleOperaEnum(ScheduleOperaEnum.UPDATE_TASK);
            }
            task.setTimeTaskName("settleAccountsInvitation:"+orders.getUuid());
            task.setExecuteTime("0 "+sendTimeTransformTime.get("mm")+" "+sendTimeTransformTime.get("HH")+" "+sendTimeTransformTime.get("dd")+" "+sendTimeTransformTime.get("MM")+" ?");
            Orders r = new Orders();
            r.setUserId(orders.getUserId());
            r.setUuid("settleAccountsInvitation:"+orders.getUuid());
            r.setScenes(scenes);
            r.setGratefulFree(orders.getGratefulFree());
            r.setHourlyFee(sc.getHourlyFee());
            r.setOrderPeriod(orders.getOrderPeriod());
            r.setSelTimeType(orders.getSelTimeType());
            r.setPerhaps(orders.getPerhaps());
            r.setStartTime(orders.getStartTime());
            task.setParams(JSON.toJSONString(r));
            data.setData(JSONObject.toJSONString(task));

            data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
            rabbitMqPublish.publish(data);


            if (Objects.equals(orders.getSelTimeType(), SelTimeTypeEnum.FIXED.getCode()) && Objects.equals(orders.getPerhaps(), InvitationTypeEnum.SERVICE.getCode())) {  //当选择的是固定周期，邀约的时候进行 计算费用

                Double hourlyFee = sc.getHourlyFee();  //该场景需要的 每小时每人 费用
                Cost cost = CalculationFactory.hand();
                Double countMoney = cost.calculate(hourlyFee, orders.getPersonCount(), orders.getOrderPeriod(), orders.getStartTime(), orders.getGratefulFree());

                if (tab!=null){
                    Integer id = Integer.valueOf(tab);
                    User user2= userDao.findUserByUserId(id);
                    /************************************* 消息列表--将参加信息 存入redis中 start****************************************/
                    String name;
                    if (StringUtils.isEmpty(user.getNickname())){
                        name=user.getUuid();
                    }else {
                        name=user.getNickname();
                    }
//        "startTime,"+orders.getStartTime(),"place,"+orders.getPlace()
                    String nameValue = name+":"+orderNo+","+orders.getStartTime()+","+orders.getPlace();
//        Long size = msgValueTemplate.size(String.format(USER_MSG,id));
                    Long size1 = msgValueTemplate.size(String.format(USER_MSG,id));
//        Long score = size+1;
                    Long score1 = size1+1;
//        msgValueTemplate.add(String.format(USER_MSG,id),String.format(USER_TYPE_ORDERS,orders.getUserId(),MsgTypeEnum.JOIN_SEND_MSG.getCode(),nameValue) ,score);
                    msgValueTemplate.add(String.format(USER_MSG,id),String.format(USER_TYPE_ORDERS,orders.getUserId(),MsgTypeEnum.JOIN_RECEIVE_MSG.getCode(),nameValue) ,score1);
                    /************************************* 消息列表--将参加信息 存入redis中 end****************************************/

                    /************************************* 订单列表--将参加信息 存入redis中 start****************************************/
//                    Long size2 = undoneRedisTemplate.size(String.format(USER_UNDONE_PERHAPS_SCENES,orders.getUserId(),InvitationTypeEnum.SERVICE.getCode(),orders.getScenes()));
//                    Long score2 = size2+1;
//                    orders.setType(PayTypeEnum.SEND_JOIN.getValue());
//                    String uValue = orders.getUuid()+":"+InvitationTypeEnum.SERVICE.getCode();
//                    undoneRedisTemplate.add(String.format(USER_UNDONE_PERHAPS_SCENES,orders.getUserId(),InvitationTypeEnum.SERVICE.getCode(),orders.getScenes()),uValue,score2);
                    /*****************订单时效 2小时后 取消订单**************************/
//                    SimpDate simpDate = SimpDateFactory.endDate();
//                    Date date2 = new Date();
//                    String now  = simpleDateFormat.format(date2);
//                    String twoHourAfter=null;
//                    try {
//                        twoHourAfter = simpDate.endDate(now,2.0,0);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    Map<String,String> cancelTimeMap2 = new HashMap<String,String>();
//                    try {
//                        cancelTimeMap2 = simpDate.transformTime(twoHourAfter);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    TransferData data2 = new TransferData();
//                    TimeTask task2 = new TimeTask();
//                    task2.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
//                    task2.setTimeTaskName("undoneOrders:"+orders.getUuid());
//                    //当前时间2小时后
//                    task2.setExecuteTime("0 "+cancelTimeMap2.get("mm")+" "+cancelTimeMap2.get("HH")+" "+cancelTimeMap2.get("dd")+" "+cancelTimeMap2.get("MM")+" ?");
//                    Orders o2 = new Orders();
//                    o2.setUuid("undoneOrders:"+orders.getUuid());
//                    o2.setUid(orders.getUserId());
//                    task2.setParams(JSON.toJSONString(o2));
//                    data2.setData(JSONObject.toJSONString(task2));
//                    data2.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
//                    rabbitMqPublish.publish(data2);
                    /****************************订单时效**************************/

                    /************************************* 订单列表--将参加信息 存入redis中 end****************************************/
//        if (!ServerHandler.sendInvitationNotify(RpcAllowMsgEnum.NOTIFY,orders,user,ServerHandlerTypeEnum.INVITATION.getType())){
                    logger.info("参加--rpc 不在线");
//        String tips=user.getNickname()+"想加入您的"+orders.getStartTime()+"开始的"+orders.getPlace()+"的订单";

                    String content = UserUtil.jsonPare("nickname,"+user.getNickname(),"startTime,"+orders.getStartTime(),"place,"+orders.getPlace(),"userId,"+user.getId(),"icon,"+user.getIcon(),"ordersId,"+orders.getUuid(),"type,"+ ServerHandlerTypeEnum.ACCEPT.getType(),"invitationId,"+uuid);
                    Push push = new Push();
                    //服务方
                    push.setDeviceNum(user2.getCid());
                    push.setSendDeviceType(user2.getType());
                    push.setSendTopic("订单消息");
                    push.setSendContent(content);
//            push.setSendContent(String.valueOf(user2.getId()));
                    PushUtils.userPush.getInstance().sendPush(push);
                }

                //添加 订单明细
                String detailId = UserUtil.generateUuid();
                logger.info("添加明细 明细id={},金额={}",detailId,countMoney);
//                订单明细id--订单号--金额--用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
                Double payedMoney = -Math.abs(countMoney);
                userDao.addOrdersDetail(detailId,uuid,payedMoney,orders.getUserId(), OrdersDetailTypeEnum.ORDERS_PAY.getKey(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.ORDERS_PAY.getValue(),OrdersDetailTypeEnum.ORDERS.getKey());

                orders.setOpenLocalTypeEnum(OpenLocalTypeEnum.HANG_ZHOU);
                orders.setUid(orders.getUserId());
                orders.setSceneSelEnum(sc.getValue());
//                orders.setMatchRate(90.0);
                if (orders.getPerhaps().equals(InvitationTypeEnum.SERVICE.getCode())){
                    orders.setPublishType(OrderPublishTypeEnum.SERVICE);
                }else if (orders.getPerhaps().equals(InvitationTypeEnum.DEMAND.getCode())){
                    orders.setPublishType(OrderPublishTypeEnum.DEMAND);
                }
                if (taskOrderCalcBus.addOrders(orders)){
                    logger.info("添加订单成功");
                }else {
                    logger.info("添加订单失败");
                }

                if(payTypeCode.equals(PayTypeEnum.PAY_ZFB.toCode())) {
                    String uuid2 = UserUtil.generateUuid();
                    Double moneyFee = Math.abs(totalFee);
                    logger.info("添加到充值明细 订单号={},金额={},充值类型={}",orderNo,moneyFee,DetailTypeEnum.INVITATION.toCode());
                    //用户id---订单号---金额---充值明细订单号---订单类型code---订单类型---支付类型code---支付类型---充值成功或失败
                    userDao.addRechargeSheet(orders.getUserId(),orderNo,moneyFee,uuid2, DetailTypeEnum.INVITATION.toCode(),DetailTypeEnum.INVITATION.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
                }
                return "ok";
            }

            if(payTypeCode.equals(PayTypeEnum.PAY_ZFB.toCode())) {
                String uuid2 = UserUtil.generateUuid();
                Double moneyFee = Math.abs(totalFee);
                logger.info("添加到充值明细 订单号={},金额={},充值类型={}",orderNo,moneyFee,DetailTypeEnum.INVITATION.toCode());
                //用户id---订单号---金额---充值明细订单号---订单类型code---订单类型---支付类型code---支付类型---充值成功或失败
                userDao.addRechargeSheet(orders.getUserId(),orderNo,moneyFee,uuid2, DetailTypeEnum.INVITATION.toCode(),DetailTypeEnum.INVITATION.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
            }
            return "ok";


        }
        
        return "fail";


    }

    @Override
    public Response credit( String certNo, String name, Integer admittanceScore) {
//        User user =userDao.findUserByUserId(id);
//        if (user==null||StringUtils.isEmpty(certNo)||StringUtils.isEmpty(name)){
//            logger.warn("芝麻信用 参数为空");
//            return Response.error(ResponseTypeEnum.EMPTY_PARAM);
//        }

        ZhimaCredit zhimaCredit = new ZhimaCredit();
        try {
           Boolean credits =  zhimaCredit.zhimaCredit("IDENTITY_CARD",certNo,name,admittanceScore);
            if (credits==true){
                return Response.successByArray();
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return Response.error(ResponseTypeEnum.ERROR_CODE);
    }

    @Override
    public Response bindingUserCid(Integer attribute, String cid) {
        //根据cid查询用户
//        List<User> user = userDao.findUserByCid(cid);
//        if (user!=null){
//            userDao.bindingUserCid(user.get(0).getId(),"");
//        }
        userDao.bindingUserCid(attribute,cid);
        return Response.successByArray();
    }

    @Override
    public Response logout(Integer id) {
        userDao.logout(id);
        return Response.successByArray();
    }

    @Override
    public Response manageLogout(Integer id) {
        userDao.manageLogout(id);
        return Response.successByArray();
    }

    @Override
    public Response bindingManageCid(Integer id, String cid) {
        //根据cid查询用户
        MerchantUser merchantUser = userDao.findMerchantByCid(cid);
        if (merchantUser!=null){
            userDao.bindingManageCid(merchantUser.getId(),"");
        }
        userDao.bindingManageCid(id,cid);
        return Response.successByArray();
    }

    @Override
    public String test(String orderNo) {
        Orders orders = userDao.findOrderByOrderNo(orderNo);
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date= null;
        try {
            date = simpleDateFormat .parse(orders.getStartTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long timeStemp = date.getTime();
        Set<String> set = taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getScenes(),orders.getPerhaps(),0));
        for (String str : set){
            System.out.println(str);
        }
        Map myOrdersMap = taskHashRedisTemplate.entries(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getScenes(),orders.getPerhaps(),0));
        System.out.println(taskHashRedisTemplate.keys(String.format(SCENENAME_ORDERS_PERHAPS_SELTIMETYPE,orders.getScenes(),orders.getPerhaps(),0)));
        System.out.println(myOrdersMap);

        System.out.println(myOrdersMap.get(String .valueOf(timeStemp)));
        return null;
    }

    @Override
    public Response onOff(Integer id, Integer onOff) {
        logger.info("设置 推送开关");
        if (id==null||onOff==null){
            logger.warn("设置 推送开关 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        userDao.onOff(id,onOff);
        return Response.successByArray();
    }

    @Override
    public Response otherUsers(Integer id, Integer userId) {
        if (id==null||userId==null){
            logger.warn("查询用户信息 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserByUserId(userId);
        if (user==null){
            logger.warn("查询的用户 id={}不存在",userId);
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"查询的用户不存在");
        }
        return Response.success(user);
    }

    @Override
    public Response activeEnter(ActiveEnterRequest activeEnterRequest) {
        if (activeEnterRequest==null||activeEnterRequest.getAge()==null||activeEnterRequest.getGender()==null||activeEnterRequest.getHeight()==null||
                activeEnterRequest.getWeight()==null||StringUtils.isEmpty(activeEnterRequest.getLabels())||StringUtils.isEmpty(activeEnterRequest.getNickName())){
            logger.warn("活动注册 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String uuid = UserUtil.generateUuid();
        activeEnterRequest.setUuid(uuid);
        userDao.activeEnter(activeEnterRequest);
        User user = userDao.findUserOneByPhone(activeEnterRequest.getPhoneNum());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId",user.getId());
        return Response.success(jsonObject);
    }

    @Override
    public Response bindingAttestation(Integer id, String name, String accountName, String idCard) {
        if (id==null||StringUtils.isEmpty(name)||StringUtils.isEmpty(accountName)||StringUtils.isEmpty(idCard)){
            logger.warn("芝麻认证 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        userDao.bindingAttestation(id,name,accountName,idCard);
        return Response.successByArray();
    }

    @Override
    public Response uploadWorkProof(UploadWorkProofRequest uploadWorkProofRequest) {
        if (StringUtils.isEmpty(uploadWorkProofRequest.getCompany())||StringUtils.isEmpty(uploadWorkProofRequest.getJob())||StringUtils.isEmpty(uploadWorkProofRequest.getProof())||uploadWorkProofRequest.getUserId()==null){
            logger.warn("上传工作证明 参数为空");
            Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
            String uuid = UserUtil.generateUuid();
            uploadWorkProofRequest.setUuid(uuid);
            userDao.uploadWorkProof( uploadWorkProofRequest);
        return Response.successByArray();
    }

    @Override
    public Response versionManage(String version) {
        String v = userDao.versionManage();
        Map<String,Object> map = new HashMap<String,Object>();
        if (!v.equals(version)){
            map.put("choose","no");
            return Response.success(map);
        }
        map.put("choose","yes");
        return Response.success(map);
    }

    @Override
    public Response pmsLogin(String username, String password) {
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            logger.warn("pms 登录 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        AdminUser adminUser = userDao.findAdminByName(username);
        if (adminUser==null){
            logger.warn("用户不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户不存在");
        }
        String pass = MD5.encryption(MD5.encryption(password));
       if(adminUser.getPassword().equals(pass)){
           //添加操作记录
           String operationId = UserUtil.generateUuid();
           //操作记录id-- 操作人id--被操作订单id--操作标识--note
           userDao.addOperationNote(operationId,adminUser.getId(),adminUser.getUuid(), OperationNoteTypeEnum.ADMIN_LOGIN.getCode(),OperationNoteTypeEnum.ADMIN_LOGIN.getValue());

           return Response.success(adminUser);

       }

        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"登录失败");
    }

    @Override
    public Response pmsRegister(String name,String username,  String password, Integer grade) {
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)||grade==null){
            logger.warn("添加 用户参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if (grade<2){
            logger.warn("用户等级错误 不可添加超级权限");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户等级错误 不可添加超级权限");
        }
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher matcher = pattern.matcher(username);
        if (username.length()<6||username.length()>=18){
            logger.warn("用户名长度不符合");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户名必须长度6-18位");
        }
        if (matcher.find()==false){
            logger.warn("用户名格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户名必须为全英文");
        }
        if (password.length()<8||password.length()>=18){
            logger.warn("密码长度不符合");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码必须长度6-18位");
        }
        Pattern pat = Pattern.compile("^[A-Za-z][A-Za-z0-9]+$");
        Matcher mat = pat.matcher(password);
        if (mat.find()==false){
            logger.warn("密码格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码格式错误");
        }
        AdminUser adminUser = userDao.findAdminByName(username);
        if (adminUser!=null){
            logger.warn("该用户名已存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户名已存在");
        }
        String uuid = UserUtil.generateUuid();
        String pass = MD5.encryption(MD5.encryption(password));
        userDao.registerPms(uuid,username,pass,grade);
        AdminUser admin = userDao.findAdminByName(username);

        //添加操作记录
        String operationId = UserUtil.generateUuid();
        //操作记录id-- 操作人id--被操作订单id--操作标识--note
        userDao.addOperationNote(operationId,admin.getId(),uuid, OperationNoteTypeEnum.ADMIN_REGISTER.getCode(),OperationNoteTypeEnum.ADMIN_REGISTER.getValue());

        return Response.successByArray();
    }

    @Override
    public Response registerSendMsg(String phoneNum) {
        if (StringUtils.isEmpty(phoneNum)){
            logger.warn("注册 验证码发送 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserOneByPhone(phoneNum);
        if (user!=null){
            logger.warn("用户已注册");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户已注册");
        }
        try {
            if (MobileMessageSend.sendMsg(phoneNum)){
                return Response.successByArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"发送短信失败 请稍后重试");
    }

    @Override
    public Response pmsUpdatePass(String name, String password) {
        if(StringUtils.isEmpty(name)||StringUtils.isEmpty(password)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if (password.length()<8||password.length()>=18){
            logger.warn("密码长度不符合");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码必须长度6-18位");
        }
        Pattern pat = Pattern.compile("^[A-Za-z][A-Za-z0-9]+$");
        Matcher mat = pat.matcher(password);
        if (mat.find()==false){
            logger.warn("密码格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码格式错误");
        }
        String pass = MD5.encryption(MD5.encryption(password));
        userDao.pmsUpdatePass(name,pass);
        return Response.successByArray();
    }

    @Override
    public Response msgLogin(String phoneNum, String msg, String uid, Integer sendDeviceType, HttpServletResponse response) {
        if (StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(msg)||StringUtils.isEmpty(uid)||sendDeviceType==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if (!Regular.checkPhone(phoneNum)){
            logger.warn("登陆 手机号格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"手机号格式错误");
        }

        try {
            if (MobileMessageCheck.checkMsg(phoneNum,msg)==false){
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"验证码错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //验证用户是否为 第一次注册登录
        BoundMemberResponse boundMemberResponse = userDao.findBoundMemberUser(phoneNum);
        if (boundMemberResponse!=null){
            User user = userDao.findUserOneByPhone(boundMemberResponse.getPhoneNum());
            if (user==null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("register","1");
                return Response.success(jsonObject);
            }
        }
        User user = userDao.findUserOneByPhone(phoneNum);
        if (user==null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("register","1");
            return Response.success(jsonObject);
        }


        if (myRealm.load(user.getPhoneNum(),user.getLoadPass(),uid,response)){
            userDao.bindingEquipment(user.getId(),uid,sendDeviceType);
            JSONObject map = new JSONObject();
            map.put("userGrade",user.getUserGrade());
            map.put("userId",user.getId());
            map.put("nickname",user.getNickname());
            return Response.success(map);
        }
        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"登录失败");
    }

    @Override
    public Response bindingInvitation(String phoneNum, Integer userId) {
        if (StringUtils.isEmpty(phoneNum)||userId==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserOneByPhone(phoneNum);
        if (user!=null){
            logger.warn("该用户已注册");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户已注册");
        }
        BoundMemberResponse boundMemberResponse = userDao.findBoundMemberUser(phoneNum);
        if (boundMemberResponse==null){
            String uuid = UserUtil.generateUuid();
            userDao.addBoundMember(phoneNum,userId,uuid);

        }else {
            userDao.upBoundMember(boundMemberResponse.getUuid(),userId);
        }
        return Response.successByArray();
//        return null;
    }

    @Override
    public Response registerInputPass(String phoneNum, String pass) {
        if (StringUtils.isEmpty(pass)||StringUtils.isEmpty(phoneNum)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if(!Regular.checkPhone(phoneNum)){
            logger.warn("注册 账号不符合要求");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"注册 账号不符合要求");
        }
        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);
        if (Regular.checkPass(dePass)==false){
            logger.warn("注册 密码不符合要求");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"密码为数字字母组合，6-18位");
        }
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));
        User user = userDao.findUserOneByPhone(phoneNum);
        BoundMemberResponse boundMemberResponse = userDao.findBoundMemberUser(phoneNum);
        if (user==null) {
            String uuid = UserUtil.generateUuid();
            String loadName = UserUtil.generateName();
            //默认昵称
            String defaultNickName = UserUtil.randomString(defaultInt);
            Integer superUserId ;
            if (boundMemberResponse==null){
                superUserId = null;
            }else {
                superUserId = boundMemberResponse.getUserId();
            }
            userDao.registerInputPass(uuid, phoneNum, loadName, MD5Pass, 0, defaultNickName,superUserId);
            User user1 = userDao.findUserOneByPhone(phoneNum);
            return Response.success(user1);
        }else {
            logger.warn("注册参数 账号已存在");
            return Response.error(ResponseTypeEnum.REGISTER_ERROR.toCode(),"注册失败，该手机号已存在");
        }
    }

    @Override
    public Response ordersPay(Integer id, Double money, String ordersId,String pass) {
        if (StringUtils.isEmpty(ordersId)||id==null||money==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserByUserId(id);
        if (user.getPayPass()==null){
            return Response.error(ResponseTypeEnum.PAY_PASS.toCode(),"未设置支付密码");
        }
        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));
        if (!user.getPayPass().equals(MD5Pass)){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码错误");
        }
        if(user.getMoney()<money){
            logger.warn("余额不足");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"余额不足");
        }
        //账户余额扣款
        if (userDao.deductMoney(money,id) == 0){
            return Response.error("扣除余额失败");
        }
        OrdersTypeEnum o  = OrdersTypeEnum.judgeOrderTypeByOrders(ordersId);
        try {

            switch (o){
                case STORE_ORDER:
                    findDetailByUid(ordersId,money,PayTypeEnum.PAY_YE.toCode());
                    break;
                case INVITE_ORDER:
                    addInvitaion(ordersId,money,PayTypeEnum.PAY_YE.toCode());
                    break;
                case STORE_TASK_PAY:
                    taskPayed(ordersId,money,PayTypeEnum.PAY_YE.toCode());
                    break;
                case PARTY_PAY:
                    partyPay(ordersId,money,PayTypeEnum.PAY_YE.toCode());
                    break;
               
                default:
                        break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Response.successByArray();
    }

    @Override
    public Response payPass(Integer id, String pass) {
        if (StringUtils.isEmpty(pass)||id==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserByUserId(id);
        if (user.getPayPass()==null){
            logger.warn("未设置支付密码");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"未设置支付密码");
        }
        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));
        if (user.getPayPass().equals(MD5Pass)){
            return Response.successByArray();
        }
        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码错误");

    }

    @Override
    public Response upPayPass(Integer id, String pass) {
        if (StringUtils.isEmpty(pass)||id==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);
        if (Regular.checkPayPass(dePass)==false){
            logger.warn("注册 密码不符合要求");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码为数字6位");
        }
        String MD5Pass = MD5.encryption(MD5.encryption(dePass));
        User user = userDao.findUserByUserId(id);
        if (user==null){
            logger.warn("用户不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户不存在");
        }
        userDao.updatePayPass(id,MD5Pass);
        return Response.successByArray();
    }

    @Override
    public Response getPayMoney(Integer id) {
        if (id==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserByUserId(id);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("payMoney",user.getConsumeMoney());
        return Response.success(jsonObject);
    }

    @Override
    public String payBalanceRecharge(String orderNo) {
        // TODO: 2017/11/2 余额充值操作
        return null;
    }

    @Override
    public String taskPayed(String orderNo,Double totalFee,Integer payTypeCode) {
        if (StringUtils.isEmpty(orderNo)){
            logger.warn("参数为空");
            return "fail";
        }
        String uuid=orderNo.substring(1,orderNo.length());
        String tag = uuid.substring(0,1);
        String payedId=null;
        if (!tag.equals("f")){
            //获取上级用户id
            String upUserId = uuid.split("t")[0];
            //获取返现金额
             String getBackMoney=uuid.split("t")[1].split("m")[0];
             //获取订单号
             payedId=uuid.split("t")[1].split("m")[1];
             //判断是否 获得过 返现
             DetailResponse response = userDao.findDetailByUidAndType(payedId,OrdersDetailTypeEnum.STORE_TASK.getKey());
             if(response!=null){
                 logger.info("用户返现userId={}",upUserId);
                 //给予上级用户返现
                 userDao.updateMoney(Double.valueOf(getBackMoney),Integer.valueOf(upUserId));
                 //添加 订单明细
                 String detailId2 = UserUtil.generateUuid();
                 //订单明细id--用户uuid--金额--用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
                 Double payedMoney = -Math.abs(Double.valueOf(getBackMoney));
                 userDao.addOrdersDetail(detailId2,payedId,payedMoney,Integer.valueOf(upUserId), OrdersDetailTypeEnum.STORE_TASK_PAY.getKey(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.STORE_TASK_PAY.getValue(),OrdersDetailTypeEnum.STORE_TASK.getKey());
             }

        }else {
            payedId=uuid.split("f")[1];
        }
        StoreTaskPayResponse response =  userDao.findTaskPay(payedId);
        if(response==null){
            logger.warn("活动订单不存在");
            return "fail";
        }
        //添加 订单明细
        String detailId2 = UserUtil.generateUuid();
        //订单明细id--用户uuid--金额--用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
        userDao.addOrdersDetail(detailId2,payedId,Double.valueOf(response.getMoney()),Integer.valueOf(response.getUserId()), OrdersDetailTypeEnum.STORE_TASK_PAY.getKey(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.STORE_TASK_PAY.getValue(),OrdersDetailTypeEnum.STORE_TASK.getKey());

        userDao.taskPayed(payedId);
        if(payTypeCode.equals(PayTypeEnum.PAY_ZFB.toCode())) {
            String uuid2 = UserUtil.generateUuid();
            Double moneyFee = Math.abs(totalFee);
            logger.info("添加到充值明细 订单号={},金额={},充值类型={}",orderNo,moneyFee,DetailTypeEnum.TASK_PAY.toCode());
            //用户id---订单号---金额---充值明细订单号---订单类型code---订单类型---支付类型code---支付类型---充值成功或失败
            userDao.addRechargeSheet(response.getUserId(),orderNo,moneyFee,uuid2, DetailTypeEnum.TASK_PAY.toCode(),DetailTypeEnum.TASK_PAY.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
        }
        return "ok";
    }

    @Override
    public String consumeMoney(String orderNo,Double totalFee,Integer payTypeCode) {
        if (StringUtils.isEmpty(orderNo)){
            logger.warn("参数为空");
            return "fail";
        }
        String payedId=orderNo.substring(1,orderNo.length());
        DetailResponse response = userDao.findDetailByUid(payedId);
        User user = userDao.findUserByUserId(response.getPayId());

        Double money = response.getMoney();
        userDao.balanceRecharge(user.getId(),money);

        //若拥有上级 则上级获得充值收益
        if (user.getSuperiorUser()!=null){
            String key2 = userDao.getSysParameter(SysKeyEnum.HIGHER_INCOME.getKey());
            Double higherMoney = Math.abs(Arith.multiplys(2,money,Arith.divides(2,Double.valueOf(key2),100)));
            userDao.balanceRecharge(user.getSuperiorUser(),higherMoney);
            User user2 = userDao.findUserByUserId(user.getSuperiorUser());
            //添加 订单明细
            String detailId2 = UserUtil.generateUuid();
            //订单明细id--用户uuid--金额--用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
            userDao.addOrdersDetail(detailId2,user2.getUuid(),higherMoney,OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.HIGHER_INCOME.getKey(),user2.getId(), OrdersDetailTypeEnum.HIGHER_INCOME.getValue(),OrdersDetailTypeEnum.RECHARGE.getKey());

            //若拥有上上级 则上上级获得充值收益
            if (user2.getSuperiorUser()!=null){
                User user3 = userDao.findUserByUserId(user2.getSuperiorUser());
                String key3 = userDao.getSysParameter(SysKeyEnum.ON_HIGHER_INCOME.getKey());
                Double onHigherMoney = Math.abs(Arith.multiplys(2,money,Arith.divides(2,Double.valueOf(key3),100)));
                userDao.balanceRecharge(user2.getSuperiorUser(),onHigherMoney);
                //添加 订单明细
                String detailId3 = UserUtil.generateUuid();
                //订单明细id--用户uuid--金额--用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
                userDao.addOrdersDetail(detailId3,user3.getUuid(),onHigherMoney,OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.ON_HIGHER_INCOME.getKey(),user3.getId(), OrdersDetailTypeEnum.ON_HIGHER_INCOME.getValue(),OrdersDetailTypeEnum.RECHARGE.getKey());

            }
        }
        if(payTypeCode.equals(PayTypeEnum.PAY_ZFB.toCode())) {
            String uuid = UserUtil.generateUuid();
            Double moneyFee = Math.abs(totalFee);
            logger.info("添加到充值明细 订单号={},金额={},充值类型={}",orderNo,moneyFee,DetailTypeEnum.CONSUME_MONEY.toCode());
            //用户id---订单号---金额---充值明细订单号---订单类型code---订单类型---支付类型code---支付类型---充值成功或失败
            userDao.addRechargeSheet(response.getPayId(),orderNo,moneyFee,uuid, DetailTypeEnum.CONSUME_MONEY.toCode(),DetailTypeEnum.CONSUME_MONEY.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
        }
        return "ok";
    }

    @Override
    public Response insertVideo(Integer id, String video) {
        if (id==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        userDao.insertVideo(id,video);
        return Response.successByArray();
    }

    @Override
    public Response ordersBillDetail(Integer uid, Integer pageNum) {
        //约会订单明细
        List<BillDetailResponse> list = userDao.findBillDetail(uid,pageNum);
        for (int i=0;i<list.size();i++){
            BillDetailResponse billDetailResponse = list.get(i);
            String type = UserUtil.valueById(billDetailResponse.getPayType());
            if (billDetailResponse.getPerhaps().intValue()==InvitationTypeEnum.SERVICE.getCode()&&billDetailResponse.getPayType().intValue()==PayTypeEnum.STR_WAIT_PAY.toCode()){
                type="待支付";
            }
            billDetailResponse.setPayed(type);
            List<OrdersUResponse> invitation = userDao.findByUuid(billDetailResponse.getOrdersId());

            billDetailResponse.setIcon(userDao.findUserByUserId(uid).getIcon());
            billDetailResponse.setOrderUser(invitation);
        }

        return Response.success(list);
    }

    @Override
    public Response storeBillDetail(Integer uid, Integer pageNum) {
        //商城订单
        List<StoreBillDetailResponse> store = userDao.findStoreBillDetail(uid,pageNum);
        for (int j =0;j<store.size();j++){
            if (store.get(j).getType()==PayTypeEnum.STORE_WAIT_PAY.toCode()){
                store.get(j).setPayType(PayTypeEnum.STORE_WAIT_PAY.getValue());
            }
            if (store.get(j).getType()==PayTypeEnum.STORE_WAIT_CONFIRM.toCode()){
                store.get(j).setPayType(PayTypeEnum.STORE_WAIT_CONFIRM.getValue());
            }
            if (store.get(j).getType()==PayTypeEnum.STORE_SUCCESS.toCode()){
                store.get(j).setPayType(PayTypeEnum.STORE_SUCCESS.getValue());
            }
            if (store.get(j).getType()==PayTypeEnum.STORE_CANCEL.toCode()){
                store.get(j).setPayType(PayTypeEnum.STORE_CANCEL.getValue());
            }


        }
        return Response.success(store);
    }

    /**
     * party支付
     * @param orderNo
     * @param total_fee
     * @return
     */
    @Override
    public String partyPay(String orderNo, Double total_fee,Integer payTypeCode) {
        String partyId = orderNo.substring(1,orderNo.length());
        PartyOrdersResponse response = userDao.findPartyByUid(partyId);
        if (response==null){
            logger.warn("订单不存在");
            return "fail";
        }


        //添加 订单明细
        String detailId = UserUtil.generateUuid();
        logger.info("添加明细 明细id={},金额={}",detailId,response.getMoney());
//                订单明细id--订单号--金额--用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
        Double payedMoney = -Math.abs(Double.parseDouble(response.getMoney()));
        userDao.addOrdersDetail(detailId,response.getUuid(),payedMoney,response.getUserId(), OrdersDetailTypeEnum.PARTY_PAY.getKey(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.PARTY_PAY.getValue(),OrdersDetailTypeEnum.PARTY.getKey());

        /**
         * party订单到期后未使用取消订单
         */
        TransferData data = new TransferData();
        TimeTask task = new TimeTask();
        task.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        task.setTimeTaskName("partyCancel:"+response.getUuid());
           String m = response.getDate().split("-")[1];
           String d = response.getDate().split("-")[2];
        String time = response.getTime().split("\\|")[0];
        String mm = time.split(":")[1];
        String hh = time.split(":")[0];
        task.setExecuteTime("0 "+mm+" "+hh+" "+d+" "+m+" ?");
        PartyOrdersResponse party = new PartyOrdersResponse();
        party.setUuid("partyCancel:"+partyId);
        task.setParams(JSON.toJSONString(party));
        data.setData(JSONObject.toJSONString(task));
        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data);
        userDao.upPartyPay(partyId,PartyPayCodeEnum.PARTY_WAIT_CONFIRM.getCode());
        if(payTypeCode.equals(PayTypeEnum.PAY_ZFB.toCode())) {
            String uuid = UserUtil.generateUuid();
            Double moneyFee = Math.abs(total_fee);
            logger.info("添加到充值明细 订单号={},金额={},充值类型={}",orderNo,moneyFee,DetailTypeEnum.PARTY.toCode());
            //用户id---订单号---金额---充值明细订单号---订单类型code---订单类型---支付类型code---支付类型---充值成功或失败
            userDao.addRechargeSheet(response.getUserId(),orderNo,moneyFee,uuid, DetailTypeEnum.PARTY.toCode(),DetailTypeEnum.PARTY.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
        }
        return "ok";
    }

    @Override
    public Response partyBillDetail(Integer id, Integer pageNum) {
        if (id==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<PartyDetailedResponse> response = userDao.findPartyBillDetail(id,pageNum);
        for (int i=0;i<response.size();i++){
            PartyDetailedResponse partyDetailedResponse =  response.get(i);
            String type = SysUtils.partyEnumIntToString(partyDetailedResponse.getPayCode());
            partyDetailedResponse.setType(type);
        }
//        int j=0;
//        while(j<=response.size()){
//            j++;
//        }
        return Response.success(response);
    }

    @Override
    public Response goldUser(Integer id, Double money) {
        if(id==null||money==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = userDao.findUserByUserId(id);
        if (user==null){
            logger.warn("充值 账号不存在");
            Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"账号 不存在");
        }

//            userDao.updateMoney(money,id);
        String uuid = UserUtil.generateUuid();
        userDao.addRechargeOrders(uuid,id,money,UserGradeEnum.GOLD_USER.toCode());
        char c = OrdersTypeEnum.RECHARGE.getQuote();
        String type = String.valueOf(c);
        StringBuffer stringBuffer = new StringBuffer(type);
        stringBuffer.append(uuid);

        return Response.success(stringBuffer);
    }

    @Override
    public Response videoAdd(Integer id, String video,String videoPic, Integer videoType) {
        if(id==null||StringUtils.isEmpty(video)||StringUtils.isEmpty(videoPic)||videoType==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String uuid = UserUtil.generateUuid();
        //添加的视频若为审核视频 则判断 是否已存在审核视频
        List<UserVideoResponse> list =  userDao.findVideoCheck(id,VideoCheckEnum.WAIT_PASS.getCode());
        if (list.size()>0&&videoType.equals(VideoCheckEnum.WAIT_PASS.getCode())){
        	//允许修改待审核状态下的视频地址
        	UserVideoResponse videoResponse = list.get(0);
        	videoResponse.setVideo(video);
        	videoResponse.setVideoPic(videoPic);
        	userDao.videoUp(videoResponse.getId(), video, videoPic, videoResponse.getUuid());
//            logger.warn("审核视频已存在");
//            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"审核视频已存在");
        }
        List<UserVideoResponse> commonVideoList =  userDao.findVideoCheck(id,VideoCheckEnum.COMMON_VIDEO.getCode());
        if(commonVideoList.size()>=3&&videoType.equals(VideoCheckEnum.COMMON_VIDEO.getCode())){
            logger.warn("普通视频上限3个");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"普通视频上限3个");
        }
        userDao.videoAdd(uuid,id,video,videoPic,videoType);
        return Response.successByArray();
    }

    @Override
    public Response videoUp(Integer id, String video, String videoPic, String uuid) {
        if(id==null||StringUtils.isEmpty(video)||StringUtils.isEmpty(uuid)||StringUtils.isEmpty(videoPic)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        UserVideoResponse userVideoResponse = userDao.findVideoByUuid(uuid);
        if (userVideoResponse==null){
            logger.warn("该视频不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该视频不存在");
        }
        if(Objects.equals(userVideoResponse.getVideoCheck(), VideoCheckEnum.SUCCESS_CHECK.getCode())&&userVideoResponse.getVideoType().equals(VideoCheckEnum.WAIT_PASS.getCode())){
            logger.warn("该认证视频已通过 请勿重复修改");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该认证视频已通过 请勿重复修改");
        }
        userDao.videoUp(id,  video,  videoPic,  uuid);
        return Response.successByArray();
}

    @Override
    public Response videoDel(Integer id, String uuid) {
        if(id==null||StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        UserVideoResponse userVideoResponse = userDao.findVideoByUuid(uuid);
        if (userVideoResponse==null){
            logger.warn("该视频不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该视频不存在");
        }
        if(Objects.equals(userVideoResponse.getVideoCheck(), VideoCheckEnum.SUCCESS_CHECK.getCode())&&userVideoResponse.getVideoType().equals(VideoCheckEnum.WAIT_PASS.getCode())){
            logger.warn("该认证视频已通过审核");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该认证视频已通过审核");
        }
        userDao.videoDel(uuid);
        return Response.successByArray();
    }

    @Override
    public String rechargeMoney(String orderNo, Double total_fee,Integer payTypeCode) {

        RechargeOrdersResponse response =  userDao.findRechargeOrders(orderNo.substring(1,orderNo.length()));
        if (response==null){
            logger.warn("充值订单不存在");
            return "fail";
        }
        //根据 优惠券类型获取 优惠券
        List<CouponsOrders> couponsOrders = userDao.findCouponsOrdersByCode(response.getUserId(),CouponsCodeEnum.FIRST_RECHARGE_MONEY.getCode());
        if(couponsOrders.size()==0){
            Coupons coupons = userDao.findCoupons(CouponsCodeEnum.FIRST_RECHARGE_MONEY.getCode());
            String uuid = UserUtil.generateUuid();
            //添加到用户优惠券列表中
            userDao.addCouponsOrders(uuid,coupons.getUuid(),response.getUserId());
        }

        Double money = -Math.abs(total_fee);
        //添加到明细表 订单号，用户id，商家id，金额 收入 0 或者支出 1
        //添加 订单明细
        User user = userDao.findUserByUserId(response.getUserId());
        String detailId2 = UserUtil.generateUuid();
        //订单明细id--用户uuid--金额--支出方用户id--type--收入方id--备注--ordersType（确认是约会还是商城订单）
        userDao.addOrdersDetail(detailId2,user.getUuid(),money,response.getUserId(), OrdersDetailTypeEnum.RECHARGE_MONEY.getKey(),OrdersDetailTypeEnum.DEFAULT_ACCOUNT_NUMBER.getKey(), OrdersDetailTypeEnum.RECHARGE_MONEY.getValue(),OrdersDetailTypeEnum.RECHARGE_WALLET.getKey());
        Double moneyFee = Math.abs(total_fee);
        if(payTypeCode.equals(PayTypeEnum.PAY_ZFB.toCode())) {
            String uuid = UserUtil.generateUuid();
            logger.info("添加到充值明细 订单号={},金额={},充值类型={}",orderNo,moneyFee,DetailTypeEnum.RECHARGE.toCode());
            //用户id---订单号---金额---充值明细订单号---订单类型code---订单类型---支付类型code---支付类型---充值成功或失败
            userDao.addRechargeSheet(response.getUserId(),orderNo,moneyFee,uuid, DetailTypeEnum.RECHARGE.toCode(),DetailTypeEnum.RECHARGE.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
        }
        userDao.updateMoney(moneyFee,response.getUserId());
        return "ok";
    }

    @Override
    public String beGoldUser(String orderNo, Double totalFee, Integer payTypeCode) {
        if(StringUtils.isEmpty(orderNo)||totalFee==null||payTypeCode==null){
            return "fail";
        }
        String value = userDao.getSysParameter(SysKeyEnum.GOLD_USER.getKey());
        RechargeOrdersResponse response =  userDao.findRechargeOrders(orderNo);
        if(response==null){
            return "fail";
        }
        User user = userDao.findUserByUserId(response.getUserId());
        if(user==null){
            return "fail";
        }

        if(totalFee>=Double.valueOf(value)&&user.getUserGrade()==UserGradeEnum.NORMAL_PERSON.toCode()){
            userDao.upUserGrade(user.getId(),UserGradeEnum.GOLD_USER.toCode());
        }
        if(payTypeCode.equals(PayTypeEnum.PAY_ZFB.toCode())) {
            String uuid = UserUtil.generateUuid();
            logger.info("添加到充值明细 订单号={},金额={},充值类型={}",orderNo,totalFee,DetailTypeEnum.BE_GOLD_USER.toCode());
            //用户id---订单号---金额---充值明细订单号---订单类型code---订单类型---支付类型code---支付类型---充值成功或失败
            userDao.addRechargeSheet(response.getUserId(),orderNo,totalFee,uuid, DetailTypeEnum.BE_GOLD_USER.toCode(),DetailTypeEnum.BE_GOLD_USER.getValue(),PayTypeEnum.PAY_ZFB.toCode(),PayTypeEnum.PAY_ZFB.getValue(),Boolean.TRUE);
        }
        userDao.updateGoldMoney(totalFee,user.getId());
        return "ok";
    }

    @Override
    public Response myCoupons(Integer id,Integer pageNum) {
        if(id==null||pageNum==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //根据 优惠券类型获取 优惠券
        PageHelper.startPage(pageNum,10);
        List<CouponsOrdersResponse> couponsOrders = userDao.findCouponsOrdersById(id);
        return Response.success(couponsOrders);
    }

    @Override
    public Response certificationSuccess(Integer id) {
        User user = userDao.findUserByUserId(id);
        if (user==null){
            Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户不存在");
        }
        if(StringUtils.isEmpty(user.getRealName())||StringUtils.isEmpty(user.getIdCard())){
            return Response.success(Boolean.FALSE);
        }
        return Response.success(Boolean.TRUE);

    }

    /**
     * 订单付款后的回调处理
     * @param orderNo       订单ID（含 o 首字母）
     * @param total_fee     支付费用
     * @param i             当前为支付宝支付（所以为1000）
     * @return
     */
    @Override
    public String trystOrdersPay(String orderNo, Double total_fee, int i) {
        String trystId = orderNo.substring(1,orderNo.length());
        logger.warn("订单id={}开始支付宝回调",trystId);
        TrystOrders trystOrders = userDao.findTrystOrdersByUid(trystId);
        User demand = userDao.findUserByUserId(trystOrders.getUserId());
        if (trystOrders == null){
            logger.warn("订单不存在");
            return "fail";
        }
        // total_fee金额 不等于总金额-预付金额
        //总金额 =  每人金额 * 已选择人数
        //需支付金额 = 总金额-预付金额
       //Double money = Arith.subtract(2, Arith.multiplys(2,trystOrders.getMoney(),trystOrders.getPersonCount()),trystOrders.getAdvanceMoney());
        Double money = trystOrders.getMoney() * trystOrders.getPersonCount();
        //测试
//        if(total_fee.equals(money)){
        //正式
        if(!total_fee.equals(money)){
            logger.warn("订单 id={} 金额错误total_fee={},money={},advanceMoney={}",trystId,total_fee,trystOrders.getMoney(),trystOrders.getAdvanceMoney());
            return "fail";
        }
        if (userDao.upTrystOrdersPay(trystId,PayTypeEnum.TRYST_WAIT_CONFIRM.toCode(),PayTypeEnum.TRYST_WAIT_CONFIRM.getValue()) == 0){
            return "fail";
        }

        userDao.exitTrystReceive(Boolean.FALSE,trystId);

        List<User> serviceList = userDao.selectUsersFromReceiveByTrystId(trystId);

        //清除redis抢单用户
        removeRedisTemplate.delete(String.format(SNATCH_USER_TRYST,trystId));

        UserName userName = new UserName();

        //系统消息通知各抢单人员，订单已生效
        for (User user : serviceList ){
            long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,user.getId()));
            String content = String.format(MSG_CONTENT_TRYST,user.getId(),MsgTypeEnum.EFFECT_TRYST.getCode(),demand.getNickname(),trystId,TimeUtil.currentTime());
            msgValueTemplate.add(String.format(USER_MSG_TRYST,user.getId()),content ,size+1);

            userName.add(user.getPhoneNum());
        }

        long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,demand.getId()));
        String content = String.format(MSG_CONTENT_TRYST,demand.getId(),MsgTypeEnum.DEMAND_EFFECT_TRYST.getCode(),demand.getNickname(),trystId,TimeUtil.currentTime());
        msgValueTemplate.add(String.format(USER_MSG_TRYST,demand.getId()),content ,size+1);

        logger.warn("订单id={}生效后后，系统消息已送达",trystId);

        //如果赴约方人数大于1，则创建群聊
        if (userName.size()>1){

            /*********************************************************创建群聊**********************************************************************/
            Group group = new Group();
            group.setGroupname(demand.getNickname()+"的"+trystOrders.getScene()+"群组");
            group.setDesc(demand.getNickname()+"创建");
            group.setPublic(Boolean.TRUE);
            group.setMaxusers(100);
            group.setApproval(Boolean.TRUE);
            group.setOwner(demand.getPhoneNum());
            group.setMembers(userName);

            //聊天室持久化
            String groupId = JSON.parseObject(chatGroupAPI.createChatGroup(group).toString(),EaseCreateGroupResponse.class).getData().getGroupid();
            String uuid = UserUtil.generateUuid();
            userDao.insertEaseMob(uuid,trystId,groupId);

            logger.warn("订单id={}已创建群聊id={}",trystId,groupId);
        }


        /**********************************************发布定时任务：订单时间已结束，可以手动完成订单*********************************************************/
        TimeTask timeTask = new TimeTask();
        timeTask.setScheduleOperaEnum(ScheduleOperaEnum.ADD_TASK);
        timeTask.setTimeTaskName("trystOrder:advanceOneHour:" + trystId);
        timeTask.setExecuteTime(TimeUtil.getfrontTimeCron(trystOrders.getTime(),1.0));
        TrystOrders entity = new TrystOrders();
        entity.setUuid("trystOrder:advanceOneHour:"+trystOrders.getUuid());
        timeTask.setParams(JSON.toJSONString(entity));
        TransferData data = new TransferData();
        data.setData(JSONObject.toJSONString(timeTask));
        data.setRabbitTypeEnum(RabbitTypeEnum.TIME_TASK);
        rabbitMqPublish.publish(data);
        logger.warn("订单 id={} 提前一小时通知所有用户将开始 定时任务已发布！",trystId);

        return "ok";
    }

    /**
     * 赴约方取消订单，支付违约金后的回调处理
     * @param orderNo
     * @param total_fee
     * @param i
     * @return
     */
    @Override
    public String cancelTrystUser(String orderNo, Double total_fee, int i) {
        List<String> payId = Stream.of(orderNo.split(",")).map( str -> str.substring(1,str.length())).collect(toList());
        String trystId = payId.get(0);
        Integer userId = Integer.valueOf(payId.get(1));
        TrystOrders trystOrders = userDao.findTrystOrdersByUid(trystId);
        if (trystOrders == null){
            logger.warn("订单不存在");
            return "fail";
        }
        Double money = trystOrders.getMoney() * 0.1;
        if(!total_fee.equals(money)){
            logger.warn("订单 id={} 金额错误total_fee={},money={},advanceMoney={}",trystId,total_fee,trystOrders.getMoney(),trystOrders.getAdvanceMoney());
            return "fail";
        }
        try {
            //删除反馈表 t_tryst_receive
            userDao.logicDelectReceiveByUserId(userId,trystId);
            //修改订单表的 person_count -1
            userDao.reduceTrystPersonCount(trystId);
            //个推给发单方：有人取消赴约
//            noticeUtil.cancelTrystUser(userDao.selectCidById(trystOrders.getUserId()),userDao.selectTypeById(trystOrders.getUserId()));
            //TODO  退回一份tryst money给发单方
        }catch (Exception e){
            throw new RuntimeException("赴约方取消订单异常，已回滚！");
        }
        return "ok";
    }

    /**
     * 获取个人详情
     * @param id
     * @return
     */
    @Override
    public Response detailsAuth(Integer id, String uuid){
        if (id == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        User user = null;
        if (uuid == null){
            user = userDao.findUserByUserId(id);
        }else {
            user = userDao.findUserByUuid(uuid);
        }
        SnatchUser snatchUser = new SnatchUser();
        snatchUser.setUserId(user.getId());
        snatchUser.setUuid(user.getUuid());
        snatchUser.setHeadUrl(user.getIcon());
        snatchUser.setNickName(user.getNickname());
        snatchUser.setGender(user.getGender());
        snatchUser.setPhone(user.getPhoneNum());
        snatchUser.setAge(user.getAge());
        snatchUser.setZodiac(user.getZodiac());
        snatchUser.setHeight(user.getHeight());
        snatchUser.setWeight(user.getWeight());
        snatchUser.setLocation("杭州");
        snatchUser.setSignature(user.getSignature());
        snatchUser.setComment("暂无评论");
        snatchUser.setVideos(userDao.findVideos(user.getId()));
        return Response.success(snatchUser);
    }


    /**
     * 支付宝转账功能
     * @param out_biz_no        支付宝订单号
     * @param payee_account     收款人账号
     * @param amount                金额
     * @param payer_show_name
     * @param payee_real_name
     * @param remark                备注
     * @return
     */
    @Async
	@Override
	public Boolean aliTransfer(String out_biz_no, String payee_account,
			String amount, String payer_show_name, String payee_real_name,
			String remark) {
		//payee_type 判断支付宝账号是手机号还是纯支付宝号2088开头(必要)
		// 参数解释:out_biz_no我方转账订单号(必要)   payee_account收款人支付宝账号(必要)   amount金额(必要)   payer_show_name付款人信息(可选)  payee_real_name收款人真实姓名(可选) remark备注(可选)
		
		//进行参数判断
		if(StringUtils.isEmpty(out_biz_no) || StringUtils.isEmpty(payee_account) || StringUtils.isEmpty(amount)){
            logger.warn("参数为空");
            return false;
        }
		
		//判断支付宝账号类型
		String payee_type = "ALIPAY_LOGONID";//默认为支付宝号为手机号
		String judge = payee_account.substring(0, 3);
		if(judge.equals("2088")){//支付宝账号不是手机号
			payee_type = "ALIPAY_USERID";
		}
		
		
		//
		AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",app_id ,aliPay_private_key,"json","GBK",aliPay_public_key,"RSA");
		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		request.setBizContent("{" +
		"\"out_biz_no\":\""+out_biz_no+"\"," +
		"\"payee_type\":\""+payee_type+"\"," +
		"\"payee_account\":\""+payee_account+"\"," +
		"\"amount\":\""+amount+"\"," +
		"\"payer_show_name\":\""+payer_show_name+"\"," +
		"\"payee_real_name\":\""+payee_real_name+"\"," +
		"\"remark\":\""+remark+"\"" +
		"}");
		AlipayFundTransToaccountTransferResponse response = null;
		try {
			response = alipayClient.execute(request);
			
		} catch (AlipayApiException e) {
			
			e.printStackTrace();
		}
		if(!response.isSuccess()){
            logger.warn("支付宝转账调用失败");
            return false;
		}
        logger.warn("支付宝转账给 id={} 成功,金额为 amount={} 元",payee_account,amount);
		
		return true;
	}


	@Override
	public Response insertIdentityProof(Integer id,String proof) {
		//进行参数判断
		if(StringUtils.isEmpty(proof) || null == id){
		       logger.warn("参数为空");
		       return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
		  }
		//已有待审核的身份认证信息 不允许在提交
		IdentityProof identityProof = new IdentityProof();
        identityProof.setUserId(id);
        identityProof.setState(0);
        
        List<IdentityProof> tempList = userDao.selectIdentityProof(identityProof);
        if(0 != tempList.size()){
        	logger.warn("已有待认证的身份信息");
		       return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已有待认证的身份信息");
        }
		
		//判断参数合法性 照片最多两张 以逗号隔开
		if(-1 == proof.indexOf(",")){
			   logger.warn("参数不合法");
		       return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数不合法");
		}
		
		
		String[] strArray = null;   
        strArray = proof.split(",");
        if(strArray.length != 2){
        	  logger.warn("参数不合法");
		      return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数不合法");
        }
        //正面(国徽面试第一张)  
//        String positive,reverse;
//        positive = strArray[0];//身份证正面
//        reverse = strArray[1];//身份证反面
        
        //通过阿里云图像识别api进行信息读取  以信息读取是否成功为标准判定审核是否通过
         
       // Map map = AliyunUtil.AliyunImageOrc(positive);
        //Map map = AliyunUtil.AliyunImageOrcrev(reverse);
        		
        
		
		//校验用户的正确性
		User user = userDao.findUserByUserId(id);
        if (user==null){
            Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户不存在");
        }
        
        String UUID = UserUtil.generateUuid();
        
        //进行插入动作
        identityProof.setUserId(id);
        identityProof.setUuid(UUID);
        identityProof.setProof(proof);
        
        
        
        //插入时状态为 3:审核中
        identityProof.setState(0);
        int i = userDao.insertIdentityProof(identityProof);
        if(0 >= i){
        	 logger.warn("身份证明创建记录失败");
        	 Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"身份证明创建记录失败");
        }
		return Response.successByArray();
	}


	@Override
	public Response selectIdentityProof(Integer userId,Boolean deleted,Integer state) {
		//校验参数
		if(null == userId){
		   logger.warn("参数为空");
		   return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
		}
		IdentityProof proof = new IdentityProof();
		proof.setUserId(userId);
	
		if(null != deleted)
		{
			proof.setDeleted(deleted);
		}
		if(null != state){
			
			proof.setState(state);
		}
		
		
		List<IdentityProof> pList = userDao.selectIdentityProof(proof);
//		Gson gsonSerializeNull = new GsonBuilder().serializeNulls().create();
//	    String gson = gsonSerializeNull.toJson(pList);
		return Response.success(pList);
	}


	@Override
	public Response getIdentityProofState(Integer userId) {
		
        //判断有没有进行身份认证
        IdentityProof proof = new IdentityProof();
		proof.setUserId(userId);
		proof.setDeleted(false);
		List<IdentityProof> pList = userDao.selectMyIdentityProof(proof);
		
		Map map =new HashMap();
        if(pList.size() == 0){
        	return Response.success(3);
        }
        
        return Response.success(pList.get(0).getState());
	}


	@Override
	public Response register(String phoneNum, String pass, String code,
			Integer gender, String nickName, String birthday) {
		if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(pass)||StringUtils.isEmpty(code) ||null == gender
				||StringUtils.isEmpty(nickName) ||StringUtils.isEmpty(birthday)){
            logger.warn("注册参数 信息为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"注册参数 信息为空");
        }
        if(!Regular.checkPhone(phoneNum)){
            logger.warn("注册 账号不符合要求");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"注册 账号不符合要求");
        }
        String dePass =  DesUtil.decrypt(pass,DEVICE_PASS_KEY);
        if (!Regular.checkPass(dePass)){
            logger.warn("登陆 密码格式错误");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"密码格式错误");
        }

        //解密用户等级 默认为普通用户：0
//        Integer userGrade =0;
//        if (StringUtils.isNotEmpty(invitationCode)){
//            userGrade =  UserUtil.decrypt(invitationCode);
//        }
        try {
            if (MobileMessageCheck.checkMsg(phoneNum,code)){
                logger.info("验证码验证成功");
                assert dePass != null;
                String MD5Pass = MD5.encryption(MD5.encryption(dePass));
                User user = userDao.findUserOneByPhone(phoneNum);
                if (user == null){
                    String uuid = UserUtil.generateUuid();
//                    String loadName = UserUtil.generateName();
//                    //默认昵称
//                    String defaultNickName = UserUtil.randomString(defaultInt);
                    //userDao.register(uuid,phoneNum,loadName,MD5Pass,userGrade,defaultNickName);
                    //通过出生日期计算年龄
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
					try {
						date = format.parse(birthday);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                   Integer age =  TimeUtil.getAgeByBirth(date);
                    
                    userDao.registerVersionTwo(uuid, phoneNum, MD5Pass, nickName, gender, birthday,age);
                    return Response.successByArray();
                }else
                if (user.getPhoneNum().equals(phoneNum)){
                    logger.warn("注册参数 账号已存在");
                    return Response.error(ResponseTypeEnum.REGISTER_ERROR.toCode(),"注册失败，该手机号已存在");
                }
            }else {
                logger.warn("验证码验证失败");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"验证码错误");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.warn("注册失败");
        return Response.error("注册失败");



	}


	@Override
	public Response checkCode(String phoneNum, String code) {
		if(StringUtils.isEmpty(phoneNum)||StringUtils.isEmpty(code)){
            logger.warn("参数 信息为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数 信息为空");
        }
		try {
			if (!MobileMessageCheck.checkMsg(phoneNum,code)){
				 logger.info("验证码验证失败");
				 return Response.error("验证码验证失败");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.successByArray();
	}

        /*
        预付金板块（暂时取消）
         */
//    @Override
//    public String trystAdvanceMoneyPay(String orderNo, Double total_fee, int i) {
//        String trystId = orderNo.substring(1,orderNo.length());
//        TrystOrders trystOrders = userDao.findTrystOrdersByUid(trystId);
//        if (trystOrders ==null){
//            logger.warn("订单不存在");
//            return "fail";
//        }
//        //测试
//        if(trystOrders.getAdvanceMoney().equals(total_fee)){
//            logger.warn("订单 id={} 预付金额错误");
//            return "fail";
//        }
//        //正式
//        /*if(!trystOrders.getAdvanceMoney().equals(total_fee)){
//            logger.warn("订单 id={} 预付金额错误");
//            return "fail";
//        }*/
//        //支付订金等待选人
//        userDao.upTrystOrdersPay(trystId,PayTypeEnum.PARTY_IN_ADVANCE.toCode(),PayTypeEnum.PARTY_IN_ADVANCE.getValue());
//        // TODO: 2018/1/19 通知认证用户
//       GaoDeResponse gaoDeResponse =  LocationUtils.GaoDeAddress(trystOrders.getLongitude(),trystOrders.getLatitude());
//        String city = gaoDeResponse.getRegeocode().getAddressComponent().getCity();
//        String district = gaoDeResponse.getRegeocode().getAddressComponent().getDistrict();
//        if ("[]".equals(city)){
//            logger.info("直辖市 添加");
//            String province = gaoDeResponse.getRegeocode().getAddressComponent().getProvince();
//            city = province;
//        }
//
////        List<NearBodyResponse[]> nearbyBodyList = nearBodyTemplate.values(String.format(NEARBY_ID_BODY_TRYST,String.valueOf(trystOrders.getUserId()),String.valueOf(trystId)));
////        List<NearBodyResponse> nearbyBodyList = totalNearBodyTemplate.values(String.format(NEARBY_TOTAL_BODY_ADDRESS,city));
//        List<NearBodyResponse> nearbyBodyList = totalNearBodyTemplate.values( String.format(NEARBY_TOTAL_CITY_DISTRICT,city,district));
//        if(nearbyBodyList.size()<10){
//            nearbyBodyList=totalNearBodyTemplate.values( String.format(NEARBY_TOTAL_CITY,city));
//        }
//
//        System.out.println(nearbyBodyList);
////        NearBodyResponse nearBodys = totalNearBodyTemplate.get(String.format(NEARBY_TOTAL_BODY_ADDRESS,city),district);
////        NearBodyResponse nearbyBodies = null;
////        if(nearbyBodyList.size()>0){
////            nearbyBodies = nearbyBodyList.get(0);
////
////        }
//        noticeUtil.noticeUser(nearbyBodyList,trystOrders);
//        return "ok";
//    }

}
