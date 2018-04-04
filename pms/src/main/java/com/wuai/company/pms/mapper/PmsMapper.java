package com.wuai.company.pms.mapper;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.*;
import com.wuai.company.pms.entity.request.TrystSceneRequest;
import com.wuai.company.pms.entity.request.VideoHomeRequest;
import com.wuai.company.pms.entity.response.PartiesResponse;
import com.wuai.company.pms.entity.response.TrystSceneResponse;
import com.wuai.company.pms.entity.response.TrystVideoHomeResponse;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 97947 on 2017/9/22.
 */
@Mapper
@Transactional
public interface PmsMapper {

    List<ActiveContent> allActiveContent(@Param("pageNum") Integer pageNum);

    void addActive(@Param("uuid") String uuid, @Param("pic")String pic, @Param("topic")String topic,
                   @Param("content") String content, @Param("time")String time);

    void addOperationNote(@Param("operationId") String operationId, @Param("id") Integer id, @Param("uuid") String uuid,
                          @Param("code") Integer code, @Param("value") String value);

    void updateActive(@Param("uuid") String uuid, @Param("pic")String pic, @Param("topic")String topic,
                      @Param("content") String content, @Param("time")String time);

    void deletedActive(@Param("uuid") String uuid);

    List<User> findUser(@Param("phone") String phone,@Param("start")  String start,@Param("end")  String end, @Param("pageNum") Integer pageNum);

    List<InvitationDetailResponse> findInvitationDetail(@Param("type") Integer type, @Param("code") Integer code, @Param("name")String name,
                                                        @Param("startTime")String startTime,  @Param("endTime")String endTime,  @Param("pageNum")Integer pageNum);

    List<Orders> findOrders(@Param("pageNum")Integer pageNum, @Param("name")String name,  @Param("startTime")String startTime, @Param("endTime")String endTime, @Param("type") Integer type);

    List<AdminUser> showAdmin( @Param("pageNum")Integer pageNum);

    void deletedAdmin(@Param("uuid")String uuid);

    List<SysResponse> showLabel(@Param("key")String key, @Param("key1")String key1, @Param("key2") String key2,
                                @Param("key3") String key3, @Param("key4") String key4, @Param("key5")String key5);

    void updateLabel(@Param("key")String key,@Param("label") String label);

    List<Scene> findScene();

    void updateScene(@Param("scene")Scene scene);

    void insertScene(@Param("scene")ScenesRequest scene);

    Scene findSceneByUid(@Param("uuid") String uuid);

    void deletedScene(@Param("uuid") String uuid);

    void updateKey(@Param("uuid")String uuid, @Param("key")String key);

    List<TalkResponse> showTalk();

    AdminUser findAdminByName(@Param("name") String name);

    Integer sizeOfActive();

    Integer sizeOfUser(@Param("phone")String phone,@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("pageNum")Integer pageNum);

    List<InvitationDetailResponse> sizeOfDetails(@Param("name") String name, @Param("startTime") String startTime, @Param("endTime") String endTime,
                         @Param("code") Integer code);

    Integer sizeOfOrdersMange(@Param("pageNum")Integer pageNum, @Param("name")String name,@Param("startTime") String startTime,
                              @Param("endTime") String endTime,@Param("type") Integer type);

    Integer sizeOfAdmin();

    List<SystemResponse> showSys();

    void updateSystem(@Param("key") String key,@Param("value")String value);

    void stopBackMoney(@Param("userId") Integer userId, @Param("key") String key);

    /*MyBatis注解形式
    @Select("SELECT * from t_store_active where deleted = false limit #{pageNum} , 10")
    @Results({
     @Result(property="uuid",column = "uuid"),
     @Result(property="content",column = "content"),
     @Result(property="topic",column = "topic"),
     @Result(property="type",column = "type"),
     @Result(property="size",column = "size"),
     @Result(property="money",column = "money"),
     @Result(property="shopPic",column = "shop_pic"),
     @Result(property="tip",column = "tip")
    })*/
    List<StoreTaskDetailedResponse> findAllTasks(@Param("pageNum") Integer pageNum);

    void updateTask(@Param("request")StoreActiveRequest request);

    void addTask(@Param("request")StoreActiveRequest request);

    void deletedTask(@Param("uuid")String uuid);

    void inputUserExcel(@Param("list")List<UserExcelRequest> list);

    void inputOrdersExcel(@Param("list")List<OrdersExcelRequest> list);

    User findUserByUuid(@Param("uuid")String uuid);

    List<ActiveOrdersResponse> activeDetails(@Param("type")Integer type,@Param("pageNum") Integer pageNum,
                                             @Param("value") String value,@Param("startTime") String startTime,@Param("endTime") String endTime);

    Integer activeDetailsSize(@Param("type")Integer type,@Param("value") String value,@Param("startTime") String startTime,@Param("endTime") String endTime);

    void upActiveDetails(@Param("uuid")String uuid, @Param("expressName")String expressName, @Param("expressNum")String expressNum,@Param("send")Integer send);

    MerchantUser findMerchantByUser(@Param("username")String username);

    void insertMerchantUser(@Param("uid")String uid, @Param("username")String username, @Param("pass")String pass, @Param("type")Integer type);

    List<MerchantUser>  findAllManage(@Param("pageNum")Integer pageNum);

    List<PartiesResponse> showAllParties(@Param("pageNum")Integer pageNum, @Param("value")String value);

    void partyConfirm(@Param("value")String value, @Param("uuid")String uuid, @Param("note")String note);

    Integer partiesSize(@Param("value")String value);


    List<UserVideoResponse> findAllUserVideo(@Param("videoCheck")Integer videoCheck);

    void checkVideo(@Param("uuid")String uuid, @Param("videoCheck")Integer videoCheck, @Param("note")String note);

    List<WithdrawCashResponse> findAllCash(@Param("cash") Integer cash);

    void upCash(@Param("uuid") String uuid, @Param("note") String note, @Param("cash") Integer cash);

    UserVideoResponse findVideoByUuid(@Param("uuid")String uuid);

    WithdrawCashResponse findCashByUuid(@Param("uuid")String uuid);

    List<Coupons> findAllCoupons();

    void couponsAdd(@Param("couponsRequest")CouponsRequest couponsRequest);

    void couponsUp(@Param("couponsRequest")CouponsRequest couponsRequest);

    void couponsDel(@Param("uuid")String uuid);

    void rechargeWallet(@Param("userId")Integer userId, @Param("money")Double money);

    void addTrystScene(@Param("request")TrystSceneRequest request);

    void addTrystVideo(@Param("video")String video,@Param("videoPic")String videoPic,@Param("typeValue")String typeValue,@Param("typeCode")Integer typeCode,@Param("uuid")String uuid);

    List<TrystSceneResponse> showTrystScene();

    List<TrystVideoHomeResponse> trystVideoShow();

    void trystVideoDel(@Param("uuid")String uuid);

    void trystSceneDel(@Param("uuid")String uuid);
    
    int insertVideoHomeSelective(VideoHome videoHome);
    
    List<IdentityProof> findIdentityProof(IdentityProof proof);
    
    int updateIdentityProofSelective(IdentityProof proof);
}
