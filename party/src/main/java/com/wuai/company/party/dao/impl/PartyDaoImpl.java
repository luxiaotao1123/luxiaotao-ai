package com.wuai.company.party.dao.impl;



import com.wuai.company.entity.Response.MessageAllResponse;
import com.wuai.company.entity.Response.PartyDetailedInformationResponse;
import com.wuai.company.entity.Response.PartyHomePageResponse;
import com.wuai.company.entity.Response.PartyOrdersResponse;
import com.wuai.company.party.dao.PartyDao;
import com.wuai.company.party.mapper.PartyMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
@Repository

public class PartyDaoImpl implements PartyDao {
    @Autowired
    private PartyMapper partyMapper;

    @Override
    public List<PartyHomePageResponse> findAll( Integer id, Integer pageNum,String classify) {
        return partyMapper.findAll(id,pageNum,classify);
    }

    @Override
    public PartyDetailedInformationResponse findDetailedInformation(String uuid) {
        return partyMapper.findDetailedInformation(uuid);
    }
    @Override
    public PartyDetailedInformationResponse findDetailedInformation2(Integer id,String uuid) {
        return partyMapper.findDetailedInformation2(id,uuid);
    }

    @Override
    public void addMessage(Integer id, String uuid, String message, String partyId,String date) {
        partyMapper.addMessage(id,uuid,message,partyId,date);
    }

    @Override
    public Integer myMessageSize(Integer id, String partyId) {
        return partyMapper.myMessageSize(id,partyId);
    }

    @Override
    public List< MessageAllResponse> messageAll(String uuid,Integer pageNum,Integer pageSize) {
        return partyMapper.messageAll(uuid,pageNum,pageSize);
    }

    @Override
    public List<PartyHomePageResponse> myCollections(Integer id, Integer pageNum) {
        return partyMapper.myCollections(id,pageNum);
    }

    @Override
    public void partyPay(String partyId, Integer id, String uuid, String date, String time, Double money, String phoneNum,
                         String topic, String storePhone,Integer payCode,Integer boySize,Integer girlSize) {
        partyMapper.partyPay(partyId,id,uuid,date,time,money,phoneNum,topic,storePhone,payCode,boySize,girlSize);
    }

    @Override
    public PartyOrdersResponse findPartyOrderByUidAndId(Integer id, String uuid) {
        return partyMapper.findPartyOrderByUidAndId(id,uuid);
    }

    @Override
    public Integer findPartyByDateAndUid(String uuid, String date) {
        return partyMapper.findPartyByDateAndUid(uuid,date);
    }

    @Override
    public String findSysValue(String key) {
        return partyMapper.findSysValue(key);
    }

    @Override
    public PartyOrdersResponse findPartyOrdersByUid(String uuid) {
        return partyMapper.findPartyOrdersByUid(uuid);
    }

    @Override
    public void delCollection(Integer id, String uuid) {
        partyMapper.delCollection(id,uuid);
    }

    @Override
    public void addCollection(Integer id, String uuid) {
        partyMapper.addCollection(id,uuid);
    }
}
