package com.wuai.company.user.mapper;

//import com.wuai.company.entity.User;
import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.ActiveEnterRequest;
import com.wuai.company.entity.request.IdentityProof;
import com.wuai.company.entity.request.UploadWorkProofRequest;
import com.wuai.company.user.entity.Response.*;
import com.wuai.company.util.Response;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单信息的mapper层
 * Created by Ness on 2017/5/25.
 */
@Mapper
public interface UserMapper {

    String selectCidById(@Param("userId")Integer userId);

    Integer selectTypeById(@Param("userId")Integer userId);

    /**
     * 根据Uuid查询用户信息
     *
     * @param id 用户的id
     * @return
     */
    User findUserOneById(@Param("id") Integer id);
    /**
     * 根据手机号查询用户信息
     *
     * @param phone 用户的手机
     * @return
     */
    User findUserOneByPhone(@Param("phone") String phone);


    /**
     * 保存用户信息
     *
     * @param user
     */
    void saveUser(User user);


    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateUserOneById(User user);
    //根据用户userid查询用户
    User findUserByUserId(@Param("userId")Integer userId);

    void updateMoney(@Param("money") Double money, @Param("uid")Integer uid);

    void insertUserBasicData(@Param("uid")Integer uid, @Param("nickName")String nickName, @Param("gender")Integer gender, @Param("age")String age, @Param("occupation")String occupation, @Param("height")String height, @Param("weight")String weight, @Param("city")String city, @Param("zodiac")String zodiac,@Param("label")String label,@Param("signature")String signature);

    void addPicture(@Param("uid")Integer uid, @Param("picture")String picture);

    //查询旧密码
    User selectPass(@Param("uid")Integer uid, @Param("oldPass")String oldPass);
    //修改密码
    void updatePass(@Param("uid")Integer uid, @Param("newPass")String newPass);

    //提交反馈意见
    void addFeedback(@Param("uid")String uid,@Param("userId")Integer userId, @Param("feedback")String feedback,@Param("nickName") String nickName, @Param("phoneNum")String phoneNum);
    //绑定支付宝
    void bindingAliPay(@Param("uid")Integer uid, @Param("realName")String realName, @Param("accountNum")String accountNum);
    //明细
    List<BillDetailResponse> findBillDetail(@Param("uid")Integer uid,@Param("pageNum")Integer pageNum);

    void register(@Param("uuid")String uuid, @Param("phoneNum")String phoneNum,@Param("pass") String pass,@Param("loadName") String loadName,@Param("userGrade") Integer userGrade,@Param("defaultNickName") String defaultNickName);

    void forgetPass(@Param("phoneNum") String phoneNum, @Param("pass") String pass);

    void bindingEquipment(@Param("id")Integer id, @Param("equipment")String equipment,@Param("sendDeviceType")Integer sendDeviceType);

    void addIcon(@Param("uid")Integer uid, @Param("icon")String icon);

    DetailResponse findDetailByUid(@Param("orderNo")String orderNo);

    void pay(@Param("orderNo")String orderNo);

    User personalDetails(@Param("uid")String uid);

    List<StoreBillDetailResponse> findStoreBillDetail(@Param("uid")Integer uid,@Param("pageNum")Integer pageNum);

    List<OrdersUResponse> findByUuid(@Param("ordersId")String ordersId);

    Orders findOrderByOrderNo(@Param("orderNo")String orderNo);

    void addRechargeSheet(@Param("userId")Integer userId,@Param("orderNo")String orderNo, @Param("money")Double money, @Param("uuid")String uuid,
                          @Param("detailType")Integer detailType, @Param("detailTypeValue") String detailTypeValue,
                          @Param("payTypeCode")Integer payTypeCode,@Param("payTypeValue")String payTypeValue,@Param("success")Boolean success);

    void payOrder(@Param("orderNo")String orderNo, @Param("payType")Integer payType);

    StoreOrders findStoreOrders(@Param("orderNo")String orderNo);

    void payStoreOrders(@Param("orderNo")String orderNo, @Param("payType")Integer payType);

    void addDetails(@Param("orderNo")String orderNo, @Param("userId")Integer userId,
                    @Param("merchantId")String merchantId, @Param("money")Double money,@Param("type")Integer type);

    MerchantUser findMerchantByUid(@Param("merchantId")String merchantId);

    MerchantUser findMerchantByName(@Param("name")String name);

    void bindingManageEquipment(@Param("uuid")String uuid, @Param("uid")String uid,@Param("sendDeviceType")Integer sendDeviceType);

    void withdrawCash(@Param("id") Integer id, @Param("money") Double money, @Param("type") Integer type, @Param("uuid")String uuid);

    void subtractMoney(@Param("id") Integer id, @Param("money") Double money);

    Scene findSceneByKey(@Param("s")String s);

    void addOrdersDetail(@Param("detailId")String detailId, @Param("uuid")String uuid,
                         @Param("countMoney")Double countMoney, @Param("userId")Integer userId,@Param("type")Integer type,
                         @Param("payedId")Integer payedId,@Param("note")String note,@Param("ordersType")Integer ordersType);

    void bindingUserCid(@Param("userId")Integer userId, @Param("cid")String cid);


    List<User> findUserByCid(@Param("cid")String cid);

    Integer logout(@Param("id")Integer id);

    void manageLogout(@Param("id")Integer id);

    User findUserByRealNameAndIdCard(@Param("name")String name, @Param("idCard")String idCard);

    Scene findSceneByValue(@Param("scenes") String scenes);

    void bindingManageCid(@Param("id")Integer id, @Param("cid")String cid);

    MerchantUser findMerchantByCid(@Param("cid")String cid);

    Orders findOrdersByVersion(@Param("version") String version);

    void cancelReceiveOrder(@Param("uuid")String uuid);

    void updateOrdersUpdateMoney(@Param("uuid")String uuid, @Param("money")Double money);

    void resetUpdateMoney(@Param("uuid")String uuid, @Param("resetUpdateMoney")Double resetUpdateMoney);

    Orders findStartTimeLimitOne(@Param("userId")Integer userId,@Param("code") Integer code,@Param("scenes") String scenes);

    void onOff(@Param("id")Integer id, @Param("onOff")Integer onOff);

    void activeEnter(@Param("nickName")String nickName , @Param("gender")String gender,@Param("age")String age,
                         @Param("height")String height, @Param("weight")String weight,@Param("labels")String labels,@Param("uuid")String uuid);

    void bindingAttestation(@Param("id") Integer id, @Param("name") String name, @Param("accountName") String accountName,@Param("idCard") String idCard);

    void uploadWorkProof(@Param("uploadWorkProofRequest") UploadWorkProofRequest uploadWorkProofRequest);

    String versionManage();

    AdminUser findAdminByName(@Param("name") String name);

    void registerPms(@Param("uuid")String uuid,@Param("username")String username, @Param("password")String password,  @Param("grade") Integer grade);

    void addOperationNote(@Param("operationId")String operationId, @Param("id")Integer id, @Param("uuid")String uuid, @Param("code")Integer code, @Param("value")String value);

    void pmsUpdatePass(@Param("name")String name, @Param("password")String password);

    BoundMemberResponse findBoundMemberUser(@Param("phoneNum")String phoneNum);

    void addBoundMember(@Param("phoneNum")String phoneNum, @Param("userId")Integer userId,@Param("uuid")String uuid);

    void upBoundMember(@Param("uuid")String uuid, @Param("userId")Integer userId);

    void registerInputPass(@Param("uuid")String uuid, @Param("phoneNum")String phoneNum,@Param("pass") String pass,@Param("loadName") String loadName,@Param("userGrade") Integer userGrade,@Param("defaultNickName") String defaultNickName, @Param("userId")Integer userId);

    List<User> findUsersForBackMoney(@Param("stopMoney")Double stopMoney, @Param("code")int code);

    void updateConsumeMoney(@Param("id")Integer id, @Param("consumeMoney")Double consumeMoney);

    void updatePayPass(@Param("id")Integer id, @Param("md5Pass")String md5Pass);

    void taskPayed(@Param("payedId")String payedId);

    void addOrdersDetailForConsumeMoney(@Param("detailId")String detailId, @Param("uuid")String uuid,
                                        @Param("countMoney")Double countMoney, @Param("userId")Integer userId,@Param("type")Integer type,
                                        @Param("payedId")Integer payedId,@Param("note")String note,@Param("ordersType")Integer ordersType);

    User findUserByUuid(@Param("uuid")String uuid);

    void balanceRecharge(@Param("id")Integer id,@Param("money") Double money);

    String getSysParameter(@Param("key")String key);

    DetailResponse findDetailByUidAndType(@Param("payedId")String payedId,@Param("key") Integer key);

    void updateComboEndTime(@Param("orderNo")String orderNo, @Param("date")String date);

    void insertVideo(@Param("id")Integer id, @Param("video")String video);

    StoreTaskPayResponse findTaskPay(@Param("payedId")String payedId);

    PartyOrdersResponse findPartyByUid(@Param("orderNo")String orderNo);

    void upPartyPay(@Param("partyId")String partyId, @Param("code")Integer code);

    List<PartyDetailedResponse> findPartyBillDetail(@Param("id")Integer id, @Param("pageNum")Integer pageNum);

    void upUserGrade(@Param("uid")Integer uid, @Param("grade")int grade);

    List<UserVideoResponse> findVideoCheck(@Param("id")Integer id, @Param("videoType")int videoType);

    List<UserVideoResponse> selectUserVideoByCheckAndUserId(@Param("videoCheck")Integer videoCheck, @Param("userId")Integer userId);

    void videoAdd(@Param("uuid")String uuid, @Param("id")Integer id, @Param("video")String video,
                  @Param("videoPic")String videoPic, @Param("videoType")Integer videoType);

    UserVideoResponse findVideoByUuid(@Param("uuid")String uuid);

    void videoUp(@Param("id")Integer id,@Param("video") String video,@Param("videoPic") String videoPic, @Param("uuid")String uuid);

    void videoDel(@Param("uuid")String uuid);

    List<UserVideoResponse> findVideos(@Param("userId")Integer userId);

    void addRechargeOrders(@Param("uuid")String uuid, @Param("uid")Integer uid, @Param("money")Double money, @Param("grade")Integer grade);

    RechargeOrdersResponse findRechargeOrders(@Param("orderNo")String orderNo);

    Coupons findCoupons(@Param("code")Integer code);

    List<CouponsOrders> findCouponsOrdersByCode(@Param("userId")Integer userId, @Param("code")Integer code);

    void addCouponsOrders(@Param("uuid")String uuid, @Param("couponsId")String couponsId, @Param("userId")Integer userId);

    void updateGoldMoney(@Param("totalFee")Double totalFee, @Param("id")Integer id);

    List<CouponsOrdersResponse> findCouponsOrdersById(@Param("id")Integer id);

    int upTrystOrdersPay(@Param("trystId")String trystId, @Param("code")Integer code, @Param("value")String value);

    TrystOrders findTrystOrdersByUid(@Param("trystId")String trystId);

    String selectScenePicByScene(@Param("sceneName")String sceneName);

    String selectSceneContentByScene(@Param("sceneName")String sceneName);

    List<TrystReceive> selectReceiveByTrystId(@Param("trystId")String trystId);

    int delectReceiveByUserId(@Param("userId")Integer userId,@Param("trystId")String trystId);

    int reduceTrystPersonCount(@Param("trystId")String trystId);

    int updateCidByUserId(@Param("userId")Integer userId, @Param("cid")String cid);

    List<User> selectUsersFromReceiveByTrystId(@Param("trystId")String trystId);
    
    Integer findUserIdByUuId(String uuid);
    
    int insertIdentityProof(IdentityProof proof);
    
    List<IdentityProof> selectIdentityProof(IdentityProof proof);
    
    List<IdentityProof> selectMyIdentityProof(IdentityProof proof);

    int logicDelectReceiveByUserId(@Param("userId")Integer userId,@Param("trystId")String trystId);

    void registerVersionTwo(@Param("uuid")String uuid, @Param("phoneNum")String phoneNum,@Param("pass") String pass,@Param("nickname") String nickname,@Param("gender") Integer gender,@Param("birthday") String birthday,@Param("age") Integer age);

    String selectPhoneByUserId(@Param("userId")Integer userId);

    void insertEaseMob(@Param("uuid")String uuid, @Param("trystId")String trystId, @Param("groupId")String groupId);

    String selectGroupIdByTrystId(@Param("trystId")String trystId);

    void deleteEaseMob(@Param("trystId")String trystId);

    int deductMoney(@Param("payMoney")Double payMoney, @Param("userId")Integer userId);

    void insertTrystReceive(@Param("uuid")String uuid, @Param("trystId")String trystId, @Param("userId")Integer userId,@Param("money") Double money);

    void exitTrystReceive(@Param("deleted")Boolean deleted, @Param("trystId")String trystId);
}
