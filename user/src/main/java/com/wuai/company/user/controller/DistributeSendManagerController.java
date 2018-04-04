package com.wuai.company.user.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wuai.company.entity.User;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.user.service.UserService;
import com.wuai.company.user.util.MobileMessageSend;
import com.wuai.company.util.DesUtil;
import com.wuai.company.util.Rc4Utils;
import com.wuai.company.util.Regular;
import com.wuai.company.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分配发送的控制类
 * Created by Ness on 2017/6/13.
 */
@RestController
@RequestMapping("distribute")
public class DistributeSendManagerController {
    @Resource
    private HashOperations<String, String, String> orderHashRedisTemplate;

    @Autowired
    private UserDao userDao;
    private Logger logger = LoggerFactory.getLogger(DistributeSendManagerController.class);

    /**
     * ==========================
     * key(设备号:)---value（设备号+ : + 首次得到时间 + :+ 得到的次数);
     * ==========================
     */
    private final Map<String, String> distributeALlMap = new ConcurrentHashMap<>();


    private final String LINK = ":";


    private final String DEVICE_LOCK_KEY = "wCompany";
    private final String SEND_LOCK_KEY = "com.wuai.company";
    // TODO: 2017/6/13 假数据 先 后期改为5次 
    private final int MAX_SEND_COUNT = 20;
    private final long EXPIRE_TIME = 1000 * 60 * 60 * 24;


    /**
     * 获取验证码需要发送的key
     *
     * @param secretKey
     * @return
     */
    @PostMapping("get")
    public Response get(String secretKey) {

        if (StringUtils.isEmpty(secretKey)) {
            logger.warn("用户所传的secretKey为空");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());

        }

        return Response.success(getKey(DesUtil.decrypt(secretKey, DEVICE_LOCK_KEY)));


    }


    /**
     * 发送验证码
     *
     * @param secretKey 得到的key
     * @param phone     手机号
     * @return
     */
    @PostMapping("register/send")
    public Response registerSendMsg(String secretKey, String phone) {
        User user = userDao.findUserOneByPhone(phone);
        if (user!=null){
            logger.warn("用户已注册");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户已注册");
        }
       return sendMsg(secretKey,phone);

    }

    /**
     * 发送验证码
     *
     * @param secretKey 得到的key
     * @param phone     手机号
     * @return
     */
    @PostMapping("send")
    public Response sendMsg(String secretKey, String phone) {
        if (StringUtils.isEmpty(secretKey)) {
            logger.warn("用户所传的secretKey为空");

            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());

        }
        if (!Regular.checkPhone(phone)) {
            logger.warn("用户所传的手机号不正确");
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());

        }


        //检验
        String result = validate(secretKey);

        if (StringUtils.isEmpty(result)) {
            logger.warn("用户发送次数超出当天最大值");

            return Response.response(ResponseTypeEnum.ERROR_CODE.toCode(),"用户发送次数超出当天最大值");

        }
        try {
            if (MobileMessageSend.sendMsg(phone)){
                return Response.successByArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO:发送短信
//        Double a = new Random().nextDouble();
//        String b = String.valueOf(a);
//        String c =  b.substring(b.length()-4,b.length());
//        logger.info("=========================模拟短信验证码============================");
//        logger.info("code="+c);
//        logger.info("=========================模拟短信验证码============================");
//        if (orderHashRedisTemplate.get(phone,phone)!=null){
//            orderHashRedisTemplate.delete(phone,phone);
//        }
//        orderHashRedisTemplate.put(phone,phone,c);
//        Map<String,String> map = new HashMap<String,String>();
//        map.put("code",c);
//        map.put("result",result);
        //        SendMsgUtil.sendMsg(phoneNum,c);
        return Response.successByArray();
//        return Response.success(result);


    }


    private String validate(String value) {
        String result = Rc4Utils.toString(value, SEND_LOCK_KEY);
        String[] array = result.split(LINK);

        //检测内存是否有值--若有用内存中值
        result = distributeALlMap.get(array[0]);

        if (StringUtils.isNotEmpty(result)) {
            array = result.split(LINK);
        }


        //超过最大次数
        int increment = Integer.parseInt(array[2]);

        long sendTime = Long.parseLong(array[1]);

        long currentTime = System.currentTimeMillis();
        long timeInterval = currentTime - sendTime;


        if (increment > MAX_SEND_COUNT && timeInterval < EXPIRE_TIME) {
            return null;
        }


        if (timeInterval > EXPIRE_TIME) {
            logger.info("设备={}的秘钥已经超过规定时间 重新生成", array[0], EXPIRE_TIME);
            sendTime = currentTime;
        }

        value = array[0] + LINK + sendTime + LINK + (++increment);

        distributeALlMap.put(array[0], value);

        return Rc4Utils.toHexString(value, SEND_LOCK_KEY);


    }


    private String getKey(String key) {
        String value = distributeALlMap.get(key);
        int increment = 0;
        if (StringUtils.isNotEmpty(value)) {
            increment = Integer.parseInt(value.split(LINK)[2]);
        }

        value = key + LINK + System.currentTimeMillis() + LINK + (++increment);

        distributeALlMap.put(key, value);

        return Rc4Utils.toHexString(value, SEND_LOCK_KEY);


    }

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1","2","3","4","5","6","7");
        List<List<String>> partition = Lists.partition(list, 2);
        System.out.println(JSON.toJSONString(partition));
    }




}
