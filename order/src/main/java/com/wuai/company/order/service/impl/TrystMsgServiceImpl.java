package com.wuai.company.order.service.impl;

import com.wuai.company.entity.Orders;
import com.wuai.company.enums.InvitationTypeEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.order.entity.OrdersReceive;
import com.wuai.company.order.service.TrystMsgService;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("trystMsgServiceImpl")
@Transactional
public class TrystMsgServiceImpl implements TrystMsgService {

    Logger logger = LoggerFactory.getLogger(TrystMsgServiceImpl.class);

    private final String USER_MSG_TRYST = "tryst:%s:msg"; //用户id--信息列表

    @Resource
    private ZSetOperations<String,String> msgValueTemplate;

    @Autowired
    private UserDao userDao;

    /*
    系统消息
     */
    @Override
    public Response msgDetailAuth(Integer id, Integer pageNum) {
        if (id==null||pageNum==null){
            logger.warn("消息列表参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
//        Long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,id));
//        if (size==null||size==0){
//            return Response.success(new String());
//        }
        Integer start = (pageNum-1)*10;
        Integer end = pageNum*10-1;
        Set<String> set = msgValueTemplate.range(String.format(USER_MSG_TRYST,id),start,end);
        List<Map<String,String>> list = new ArrayList<>();
        Object[] strs=set.toArray();
        for (int i = strs.length-1 ; i >= 0 ; i--){
            String str =(String) strs[i];
            Map<String,String> map = new HashMap<String,String>();
            String[] strings = str.split(":");
            map.put("userId",strings[0]);
            map.put("type",strings[1]);
            map.put("nickname",strings[2]);
            map.put("trystId", strings[3]);
            map.put("time",strings[4] + ":" + strings[5]);
//            if (str.length() != 4){
//                String isRead = strings[4];
//                map.put("isRead",isRead);
//            }
            list.add(map);
        }
        return Response.success(list);
    }

    /*
    获取系统消息数量（用于比较）
     */
    @Override
    public Response msgCountAuth(Integer id) {
        if (id==null){
            logger.warn("消息列表参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,id));
        Set<String> set = msgValueTemplate.range(String.format(USER_MSG_TRYST,id),size-1,size);
        return Response.success(msgValueTemplate.size(String.format(USER_MSG_TRYST,id))) ;
    }

    /*
    获取groupId，（通过订单id）
     */
    @Override
    public Response getGroupIdByTrystIdAuth(Integer id, String trystId) {
        if (id==null || StringUtils.isEmpty(trystId)){
            logger.warn("消息列表参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String groupId = userDao.selectGroupIdByTrystId(trystId);
        if (groupId == null){
            return Response.success();
        }
        return Response.success(groupId);
    }
}
