package com.wuai.company.order.service;

import com.wuai.company.util.Response;

public interface TrystNearBodyService {

    Response addNearbyBody(Integer id, Double latitude, Double longitude, String cid) throws IllegalAccessException;

}
