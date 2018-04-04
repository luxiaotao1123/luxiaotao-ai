package com.wuai.company.store.manage.dao;

import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.Response.MessageAllResponse;
import com.wuai.company.entity.Response.PartyDetailedInformationResponse;
import com.wuai.company.store.manage.entity.request.PartyUpRequest;

import java.util.List;

/**
 * Created by hyf on 2017/12/19.
 */
public interface PartyManageDao {
    String sysValue(String key);

    void upParty(PartyUpRequest request);

    void addParty(PartyUpRequest request);

    MerchantUser findMerchantUserByName(String name);

    List<PartyDetailedInformationResponse> findPartyAll(String name, Integer pageNum,String value);

    Integer sizeOfParty(String name,String value);

    void delParty(String uuid);

    List<MessageAllResponse> findAllMessages(String uuid);
}
