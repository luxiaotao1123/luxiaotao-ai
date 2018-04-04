package com.wuai.company.order.util.common;

import com.wuai.company.entity.Orders;
import com.wuai.company.order.dao.OrdersDao;
import com.wuai.company.order.entity.OrdersReceive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 97947 on 2017/8/2.
 */

@Service
@Transactional
public class TaskUtilCommon {
    @Autowired
    private OrdersDao ordersDao;


}
