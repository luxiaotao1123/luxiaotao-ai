package com.wuai.company.order.service;

import com.wuai.company.util.Response;

public interface TrystMsgService {

    Response msgDetailAuth(Integer id,Integer pageNum);

    Response msgCountAuth(Integer id);

    Response getGroupIdByTrystIdAuth(Integer id, String trystId);

}
