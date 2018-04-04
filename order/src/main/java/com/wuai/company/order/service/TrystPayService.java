package com.wuai.company.order.service;

import com.wuai.company.util.Response;

public interface TrystPayService {

    Response taskPay(String trystId, Integer id);

}
