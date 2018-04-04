package com.wuai.company.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.wuai.company.entity.Response.EaseCreateGroupResponse;
import com.wuai.company.entity.TrystCancel;
import com.wuai.company.entity.TrystOrders;
import com.wuai.company.entity.User;
import com.wuai.company.enums.CancelEnum;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.order.mapper.TrystSceneMapper;
import com.wuai.company.order.service.OrdersService;
import com.wuai.company.order.service.TrystOrdersService;
import com.wuai.company.party.dao.PartyDao;
import com.wuai.company.pms.service.PmsService;
import com.wuai.company.user.EaseMob.api.AuthTokenAPI;
import com.wuai.company.user.EaseMob.api.ChatGroupAPI;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.user.service.UserService;
import io.swagger.client.model.Group;
import io.swagger.client.model.UserName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class OrdersServiceImplTest {

        private final String USER_MSG = "%s:msg"; //用户id--信息列表

        @Autowired
        private OrdersService ordersService;

        @Autowired
        private PmsService pmsService;

        @Autowired
        private PartyDao partyDao;

        @Autowired
        private UserService userService;

        @Autowired
        private TrystOrdersService trystOrdersService;

        @Autowired
        private TrystOrdersDao trystOrdersDao;

        @Autowired
        TrystSceneMapper trystSceneMapper;

        @Resource
        private ZSetOperations<String,String> msgValueTemplate;


        @Autowired
        private AuthTokenAPI authTokenAPI;

        @Autowired
        private ChatGroupAPI chatGroupAPI;

        @Autowired
        private UserDao userDao;

        @Test
        public void activePic() throws Exception {
                String trystId = "6909133206909936362251";
                userDao.exitTrystReceive(Boolean.FALSE,trystId);

                List<User> serviceList = userDao.selectUsersFromReceiveByTrystId(trystId);
                System.out.println(serviceList.size()+"==========================================");
        }

        @Test
        public void grabTest(){
                Map<String, String> map  = new HashMap<>();
                map.put("1","1");
                map.put("1","2");
                System.out.println(map.get("1"));
        }

}