package com.wuai.company.util;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.Appraise;
import com.wuai.company.enums.OrdersTypeEnum;
import com.wuai.company.enums.PayTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by Ness on 2017/6/2.
 */
public class UserUtil {


    private static final String name = "default";

    private static final AtomicLong atomicLong = new AtomicLong();


    /**
     * 生成自增Id
     *
     * @return
     */
    public static String generateUuid() {

        while (atomicLong.compareAndSet(0, 1)) {
            String idBefore = System.currentTimeMillis() + "";
            String idAfter = Instant.now().toString().replaceAll("[-:]", "").replaceAll("[.Z]", "");
            StringBuilder builder = new StringBuilder(idBefore.substring(0, idBefore.length() >> 1));
            builder.append(idBefore.substring(idBefore.length() >> 1, idBefore.length()));
            builder.append(idAfter.substring(idAfter.length() >> 1, idAfter.length()));
            atomicLong.set(0);
            return builder.reverse().toString();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        return generateUuid();

    }

    /**
     * 生成无序姓名
     *
     * @return
     */
    public static String generateName() {
        synchronized (name) {

            StringBuilder builder = new StringBuilder(Instant.now().toString().replaceAll("[-:]", "").replaceAll("[.TZ]", ""));
            try {
                TimeUnit.MICROSECONDS.sleep(5);
            } catch (InterruptedException e) {
            }
            return builder.reverse().toString();
        }
    }

    private static LongAdder _orderNoLock = new LongAdder();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final int WAITTING_TIME = 500;
    private static final int MAX_ORDER_LENGTH = 30;
    private static final int MAX_USER_LENGTH = 11;
    private static final int ORDERNO_HOLD_LOCK = 1;
    private static ThreadLocalRandom random = ThreadLocalRandom.current();
    private static final int CUT_LENGTH = 4;
    private static final int INITLIAZE_ORDERS_NUM = 0;
    private static final int ENDWITH_ORDERS_NUM = 9;
    private static final int DEFAULT_ORDER_PREFIX = 0;


    /**
     * 生成订单号
     *
     * @return
     */
    public static String generateOrderNo() {
        return increaseGenerateOrderNo(DEFAULT_ORDER_PREFIX, null);

    }


    /**
     * 根据用户Id生成订单号
     *
     * @return
     */
    public static String generateOrderNo(OrdersTypeEnum typeEnum, Integer uid) {


        if (uid == null) {
            return null;
        }
        return increaseGenerateOrderNo(typeEnum.toCode(), uid);

    }

    /**
     * 根据用户Id生成订单号
     *
     * @return
     */
    public static String generateOrderNo(String orderNo) {

        return increaseGenerateOrderNoVersion(orderNo);

    }


    /**
     * 解析订单
     *@param orderNo 原始订单
     * @return
     */
    public static String parseOrderNo(String orderNo) {
        String _parseOrderNo = orderNo;
        if (StringUtils.isEmpty(_parseOrderNo)) {
            return _parseOrderNo;
        }

        return _parseOrderNo.substring(0, _parseOrderNo.length() - 1);
    }

    public static String parseLastOrderNo(String orderNo) {
        String _parseOrderNo = orderNo;
        if (StringUtils.isEmpty(_parseOrderNo)) {
            return _parseOrderNo;
        }

        return _parseOrderNo.substring( _parseOrderNo.length() - 1,_parseOrderNo.length());
    }





    /**
     * 增长的订单版本信息
     *
     * @param orderNo 订单号
     * @return
     */
    private static String increaseGenerateOrderNoVersion(String orderNo) {
        String _newOrderNo = orderNo;

        if (StringUtils.isEmpty(orderNo) || orderNo.length() < MAX_ORDER_LENGTH) {

            return _newOrderNo;
        }


            int _incVersion = Integer.parseInt(orderNo.substring(orderNo.length() - 1, orderNo.length()));
            _incVersion++;
            _newOrderNo = orderNo.substring(0, orderNo.length() - 1) + _incVersion;

            if (_incVersion > ENDWITH_ORDERS_NUM) {
                return orderNo;
            } else {
                return _newOrderNo;
            }


    }


    /**
     * 自增长的订单内容
     *
     * @param prefix 订单前缀
     * @param uid    用户ID
     * @return
     */
    private static String increaseGenerateOrderNo(int prefix, Integer uid) {
        _orderNoLock.increment();
        while (_orderNoLock.intValue() == ORDERNO_HOLD_LOCK) {

            StringBuilder bulid = new StringBuilder(String.valueOf(prefix));
            LocalDateTime time = LocalDateTime.now();
            String format = time.format(formatter);

            String id = UUID.randomUUID().toString().replaceAll("-", "").replaceAll("[a-zA-Z]+", "");
            if (uid != null) {
                String uidPrefix;
                if (uid.toString().length() > CUT_LENGTH) {
                    uidPrefix = uid.toString().substring(0, CUT_LENGTH);
                } else {
                    uidPrefix = String.format("%04d", uid);
                }
                bulid.append(uidPrefix);
            }
            bulid.append(format);
            bulid.append(id);


            if (bulid.length() < MAX_ORDER_LENGTH) {
                int len = MAX_ORDER_LENGTH - bulid.length();
                for (int i = 0; i < len; i++) {
                    bulid.append(random.nextInt(10));
                }
            } else {
                bulid.delete(MAX_ORDER_LENGTH, bulid.length());

            }
            bulid.append(INITLIAZE_ORDERS_NUM);
            _orderNoLock.reset();
            return bulid.toString();


        }

        try {
            Thread.sleep(WAITTING_TIME);
        } catch (InterruptedException e) {

        }

        return increaseGenerateOrderNo(prefix, uid);
    }

    /**
     * 解密
     *
     * @param userGradeKey
     * @return
     */
    public static Integer decrypt(String userGradeKey) {


        return Integer.parseInt(new String(Base64.getDecoder().decode(userGradeKey)).split(LINK)[0]);


    }

    private static final String LINK = ":";

    /**
     * 加密
     *
     * @param userGrade 用户等级
     * @return
     */
    public static String encrypt(Integer userGrade) {

        return Base64.getEncoder().encodeToString((userGrade + LINK + String.format("%04d", new Random().nextInt(1000))).intern().getBytes());

    }

    public static String jsonPare(Object...objects){
        JSONObject jsonObject = new JSONObject();
        JSONObject json = new JSONObject();
        for (int i=0;i<objects.length;i++){
            String split = (String) objects[i];
            String pre = split.split(",")[0];
            String last = split.split(",")[1];
            if (pre.equals("type")){
                json.put(pre,last);
            }else {
                jsonObject.put(pre, last);
            }
        }
        json.put("data",jsonObject);
        return String.valueOf(json);
    }
    public static String valueById(int code){
        PayTypeEnum[] payTypeEnums = PayTypeEnum.values();
        String value=null;
        for (int i=0;i<payTypeEnums.length;i++){
            if (code==PayTypeEnum.STR_WAIT_START.toCode()){
                value=PayTypeEnum.STR_ON_THE_WAY.getValue();
                break;
            }
            if (code==payTypeEnums[i].toCode()){
                value = payTypeEnums[i].getValue();
                break;
            }
        }
        return value;
    }
    public static Double appraiseGrade(List<Appraise> list){
        Double initNum=0.0;
        if (list==null||list.size()==0){
            return 4.5;
        }
        int j = 0 ;
        for (int i=0;i<list.size();i++){
           Double star = list.get(i).getStar();
           if (star!=1) {
               initNum = Arith.add(1, initNum, star);
           }else {
               j++;
           }
        }
        Integer len = list.size()-j;

        Double subtract =  Arith.subtract(1,initNum,len);


        return subtract;
    }

    /** 产生一个随机的字符串*/
    public static String randomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        buf.append("用户");
        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }
        return buf.toString();
    }
    public static Long exchangeChar(String ordersNo){
        String userId = null;
        for (int i=0;i<ordersNo.length();i++){
            if (ordersNo.charAt(i)>=65&&ordersNo.charAt(i)<=122){
                int num = ordersNo.charAt(i);
                if (userId==null){
                    userId = String.valueOf(num);
                }else {
                    userId += String.valueOf(num);
                }
            }
        }
        return Long.valueOf(userId);
    }
    //身份证脱敏
    public static String idCardNum(String id) {
        if (StringUtils.isBlank(id)) {
            return "";
        }
//        String left = StringUtils.left(id, 4);
        String num = StringUtils.right(id, 4);
        String idNum = StringUtils.leftPad(num, StringUtils.length(id), "*");
        return idNum;
    }

    public static void main(String[] args) {
        System.out.println(UserUtil.generateUuid());

//        System.out.println(randomString(8));
//        Object[] obj = {"b","a"};
//        jsonPare("uid:111","aaa:111");
//        System.out.println(increaseGenerateOrderNoVersion("222222222222222222222222222222222213"));
//        String a = UserUtil.generateOrderNo();
//        System.out.println(a);
//        System.out.println(parseOrderNo(a));
//        System.out.println(generateOrderNo(a));
//        System.out.println(parseLastOrderNo(a));
    }

}
