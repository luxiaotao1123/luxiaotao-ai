package com.wuai.company.party.mapper;


import com.wuai.company.entity.Response.MessageAllResponse;
import com.wuai.company.entity.Response.PartyDetailedInformationResponse;
import com.wuai.company.entity.Response.PartyHomePageResponse;
import com.wuai.company.entity.Response.PartyOrdersResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Created by Administrator on 2017/6/14.
 */
@Mapper
public interface PartyMapper {


    List<PartyHomePageResponse> findAll(@Param("id")Integer id, @Param("pageNum") Integer pageNum,@Param("classify")String classify);

    PartyDetailedInformationResponse findDetailedInformation(@Param("uuid") String uuid);
    PartyDetailedInformationResponse findDetailedInformation2(@Param("id")Integer id,@Param("uuid") String uuid);

    void partyPay(@Param("partyId")String partyId, @Param("id")Integer id, @Param("uuid")String uuid, @Param("date")String date,@Param("time") String time,
                  @Param("money")Double money, @Param("phoneNum")String phoneNum, @Param("topic")String topic, @Param("storePhone")String storePhone
            ,@Param("payCode")Integer payCode,@Param("boySize")Integer boySize,@Param("girlSize")Integer girlSize);

    PartyOrdersResponse findPartyOrderByUidAndId(@Param("id")Integer id, @Param("uuid")String uuid);

    Integer findPartyByDateAndUid(@Param("uuid")String uuid, @Param("date")String date);

    String findSysValue(@Param("key")String key);

    PartyOrdersResponse findPartyOrdersByUid(@Param("uuid")String uuid);

    void addCollection(@Param("id")Integer id, @Param("uuid")String uuid);

    void delCollection(@Param("id")Integer id, @Param("uuid")String uuid);

    void addMessage(@Param("id")Integer id, @Param("uuid")String uuid, @Param("message")String message, @Param("partyId") String partyId, @Param("date") String date);

    Integer myMessageSize(@Param("id")Integer id,@Param("partyId") String partyId);

    List< MessageAllResponse> messageAll(@Param("uuid")String uuid,@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);

    List<PartyHomePageResponse> myCollections(@Param("id")Integer id,@Param("pageNum") Integer pageNum);
}
