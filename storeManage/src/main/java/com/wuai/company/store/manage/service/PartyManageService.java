package com.wuai.company.store.manage.service;


import com.wuai.company.store.manage.entity.request.PartyUpRequest;
import com.wuai.company.util.Response;

/**
 * Created by Administrator on 2017/6/29.
 */
public interface PartyManageService {

    Response upParty(String name, PartyUpRequest request) throws Exception;

    Response classify();

    Response findPartyAll(String name, Integer pageNum,String value);

    Response delParty(String name, String uuid);

    Response informationParty(String name);

    Response messages(String name, String uuid, Integer pageNum);
}
