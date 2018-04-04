package com.wuai.company.party.service;


import com.wuai.company.util.Response;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface PartyService {

    Response findAll(Integer id, Integer pageNum,String classify);

    Response detailedInformation(Integer attribute, String uuid);

    Response partyPay(Integer attribute, String uuid, String time, Double money,Integer boySize,Integer girlSize);

    Response groupBuying(Integer attribute, String uuid);

    Response cancelParty(Integer attribute, String uuid);

    Response classify();

    Response detailedInformationWeb(String uuid);

    Response collection(Integer attribute, String uuid,Integer collect);

    Response addMessage(Integer attribute, String uuid, String message);

    Response messageAll(Integer attribute, String uuid,Integer pageNum);

    Response myCollections(Integer attribute, Integer pageNum);
}
