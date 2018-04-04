package com.wuai.company.store.manage.mapper;

import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.Response.MessageAllResponse;
import com.wuai.company.entity.Response.PartyDetailedInformationResponse;
import com.wuai.company.entity.StoreOrders;
import com.wuai.company.store.manage.entity.request.PartyUpRequest;
import com.wuai.company.store.manage.entity.response.StoreDetailsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/6/29.
 */
@Mapper
public interface PartyManageMapper {

    String sysValue(@Param("key")String key);

    void upParty(@Param("request")PartyUpRequest request);

    void addParty(@Param("request")PartyUpRequest request);

    MerchantUser findMerchantUserByName(@Param("name")String name);

    List<PartyDetailedInformationResponse> findPartyAll(@Param("name")String name, @Param("pageNum")Integer pageNum,@Param("value")String value);

    Integer sizeOfParty(@Param("name")String name,@Param("value")String value);

    void delParty(@Param("uuid")String uuid);

    List<MessageAllResponse> findAllMessages(@Param("uuid")String uuid);
}
