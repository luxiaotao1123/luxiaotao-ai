package com.wuai.company.order.service.impl;

import com.wuai.company.entity.TrystOrders;
import com.wuai.company.enums.OrdersTypeEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.order.mapper.TrystMapper;
import com.wuai.company.order.service.TrystPayService;
import com.wuai.company.util.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


@Service("trystPayServiceImpl")
@Transactional
public class TrystPayServiceImpl implements TrystPayService {

    private Logger logger = LoggerFactory.getLogger(TrystPayServiceImpl.class);

    @Autowired
    private TrystMapper trystMapper;

    /*
    获取payId
     */
    @Override
    public Response taskPay(String trystId, Integer id) {
        if (StringUtils.isEmpty(trystId) || id == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String uuid = trystId.substring(0,trystId.length()-1);
        TrystOrders trystOrders = new TrystOrders();
        trystOrders.setUuid(uuid);
        TrystOrders res = trystMapper.selectTrystOrders(trystOrders);
        if (res == null){
            return Response.error("未找到指定订单");
        }
        char c = OrdersTypeEnum.STORE_TASK_PAY.getQuote();
        StringBuilder stringBuilder = new StringBuilder(c);
        stringBuilder.append(uuid);
        stringBuilder.append("_");
        stringBuilder.append(res.getAdvanceMoney());
        Map<String, Object> map = new HashMap<>();
        map.put("payId", stringBuilder.toString());
        map.put("advanceMoney",res.getAdvanceMoney());
        return Response.success(map);
    }

}
