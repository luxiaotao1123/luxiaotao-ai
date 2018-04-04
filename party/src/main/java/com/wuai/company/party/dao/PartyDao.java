package com.wuai.company.party.dao;


import com.wuai.company.entity.Response.MessageAllResponse;
import com.wuai.company.entity.Response.PartyDetailedInformationResponse;
import com.wuai.company.entity.Response.PartyHomePageResponse;
import com.wuai.company.entity.Response.PartyOrdersResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface PartyDao {


    List<PartyHomePageResponse> findAll(Integer id, Integer pageNum,String classify);

    PartyDetailedInformationResponse findDetailedInformation(String uuid);

    void partyPay(String partyId, Integer id, String uuid, String date, String time, Double money,
                  String phoneNum, String topic, String phoneNum1,Integer payCode,Integer boySize,Integer girlSize);

    PartyOrdersResponse findPartyOrderByUidAndId(Integer id, String uuid);

    Integer findPartyByDateAndUid(String uuid, String date);

    String findSysValue(String key);

    PartyOrdersResponse findPartyOrdersByUid(String uuid);

    void delCollection(Integer id, String uuid);

    void addCollection(Integer id, String uuid);

    PartyDetailedInformationResponse findDetailedInformation2(Integer id, String uuid);

    void addMessage(Integer id, String uuid, String message, String partyId,String date);

    Integer myMessageSize(Integer id, String partyId);

    List< MessageAllResponse> messageAll(String uuid,Integer pageNum,Integer pageSize);

    List<PartyHomePageResponse> myCollections(Integer id, Integer pageNum);
}
