package com.wuai.company.util;


import com.wuai.company.enums.OrdersTypeEnum;
import com.wuai.company.enums.PartyPayCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Random;

import static com.wuai.company.enums.PartyPayCodeEnum.*;

/**
 * Created by 97947 on 2017/10/12.
 */
public class SysUtils {
    @Resource
    private HashOperations<String, String, String> orderHashRedisTemplate;

    private final String ORDERS_ORDERSID = "orders:%s"; //订单id

    Logger logger = LoggerFactory.getLogger(SysUtils.class);

    /**
     * 重复 邀请 参加限制
     * @param ordersId
     * @param id
     * @return
     */
    public  Boolean repeated(String ordersId,Integer id){
        Boolean flag=Boolean.FALSE;
        String compareId = orderHashRedisTemplate.get(String.format(ORDERS_ORDERSID,ordersId),String.valueOf(id));
        if (compareId!=null){
            logger.warn("用户userId={}已发出参加信息",id);
//          return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已发出参加或邀请信息，请勿频繁操作");
            return flag;
        }
        orderHashRedisTemplate.put(String.format(ORDERS_ORDERSID,ordersId),String.valueOf(id),ordersId);
        return Boolean.TRUE;
    }

    /**
     * 校验 实体类 是否有空 参数
     *
     * 使用java中的反射机制，来获取对象的属性清单，进而获取该属性的值。
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException {
        boolean flag = false;
        for(Field f : obj.getClass().getDeclaredFields()){
            f.setAccessible(true);
            if(f.get(obj) == null){
                flag = true;
                return flag;
            }
        }
        return flag;
    }
    /**
     * 根据长度生成随机数
     * @param length
     * @return
     */
    public static String randomByLength(int length) {
        StringBuilder sb=new StringBuilder();
        Random rand=new Random();
        for(int i=0;i<length;i++)
        {
            sb.append(rand.nextInt(10));
        }
        String data=sb.toString();
        return data;
    }

    public static String partyEnumIntToString(Integer enums){

        switch (enums){
            case 0:
                return PARTY_PAY_WAIT.getValue();
            case 1:
                return PARTY_WAIT_CONFIRM.getValue();
            case 2:
                return PARTY_SUCCESS.getValue();
            case 3:
                return PARTY_CANCEL.getValue();
            default:
                break;
        }
        return null;
    }

    public static String UuidChange( String uuid,char c){
//        String uuid = UserUtil.generateUuid();
//        char c = OrdersTypeEnum.PARTY_PAY.getQuote();
        String type = String.valueOf(c);
        StringBuffer stringBuffer = new StringBuffer(type);
        stringBuffer.append(uuid);
        String id = stringBuffer.toString();
        return id;
    }

    public static void main(String[] args) throws IllegalAccessException {
//        System.out.println(      randomByLength(9)
//        );

    }
}
