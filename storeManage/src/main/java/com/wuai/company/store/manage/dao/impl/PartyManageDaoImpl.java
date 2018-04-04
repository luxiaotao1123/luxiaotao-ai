package com.wuai.company.store.manage.dao.impl;

import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.Response.MessageAllResponse;
import com.wuai.company.entity.Response.PartyDetailedInformationResponse;
import com.wuai.company.store.manage.dao.PartyManageDao;
import com.wuai.company.store.manage.entity.request.PartyUpRequest;
import com.wuai.company.store.manage.mapper.PartyManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by hyf on 2017/12/19.
 */
@Repository
public class PartyManageDaoImpl implements PartyManageDao {
    @Autowired
    private PartyManageMapper partyManageMapper;
    @Override
    public String sysValue(String key) {
        return partyManageMapper.sysValue(key);
    }

    @Override
    public void upParty(PartyUpRequest request) {
        partyManageMapper.upParty(request);
    }

    @Override
    public void addParty(PartyUpRequest request) {
        partyManageMapper.addParty(request);
    }

    @Override
    public MerchantUser findMerchantUserByName(String name) {
        return partyManageMapper.findMerchantUserByName(name);
    }

    @Override
    public List<PartyDetailedInformationResponse> findPartyAll(String name, Integer pageNum,String value) {
        return partyManageMapper.findPartyAll(name,pageNum,value);
    }

    @Override
    public Integer sizeOfParty(String name,String value) {
        return partyManageMapper.sizeOfParty(name,value);
    }

    @Override
    public void delParty(String uuid) {
        partyManageMapper.delParty(uuid);
    }

    @Override
    public List<MessageAllResponse> findAllMessages(String uuid) {
        return  partyManageMapper.findAllMessages(uuid);
    }
}
