package com.wuai.company.user.dao;


//import com.wuai.company.entity.User;
import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.ActiveEnterRequest;
import com.wuai.company.entity.request.IdentityProof;
import com.wuai.company.entity.request.UploadWorkProofRequest;
import com.wuai.company.user.entity.Response.*;
import com.wuai.company.util.Response;





import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 用户的dao层
 * Created by Ness on 2017/5/25.
 */
public interface UserDao {

    String selectCidById(Integer userId);

    Integer selectTypeById(Integer userId);

    /**
     * 根据Uuid查询用户信息
     *
     * @param id 用户的id
     * @return
     */
    User findUserOneById(Integer id);
    /**
     * 根据手机号查询用户信息
     *
     * @param phone 用户的手机
     * @return
     */
    User findUserOneByPhone(String phone);


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

//根据用户userid查询
    User findUserByUserId(Integer userId);

    //收入
    void updateMoney(Double money,Integer uid);

    void insertUserBasicData(Integer uid, String nickName, Integer gender, String age, String occupation, String height, String weight, String city, String zodiac,String label,String signature);


    void addPicture(Integer uid, String picture);

    //修改密码
    void updatePass(Integer uid, String newPass);
    //查询旧密码是否正确
    User selectPass(Integer uid, String oldPass);
    //提交反馈意见
    void addFeedback(String uid,Integer userId ,String feedback, String nickName, String phoneNum);
    //绑定支付宝
    void bindingAliPay(Integer uid, String realName, String accountNum);

    List<BillDetailResponse> findBillDetail(Integer uid,Integer pageNum);

    void register(String uuid, String phoneNum,String loadName, String pass,Integer userGrade,String defaultNickName);

    void forgetPass(String phoneNum, String pass);

    void bindingEquipment(Integer id, String equipment,Integer sendDeviceType);


    void addIcon(Integer uid, String icon);

    DetailResponse findDetailByUid(String orderNo);

    void pay(String orderNo);

    User personalDetails(String uid);

    List<StoreBillDetailResponse> findStoreBillDetail(Integer uid,Integer pageNum);

    List<OrdersUResponse> findByUuid(String ordersId);

    Orders findOrderByOrderNo(String orderNo);

    void addRechargeSheet(Integer userId,String orderNo, Double money, String uuid,Integer detailType, String detailTypeValue, Integer payTypeCode, String payTypeValue,Boolean success);


    void payOrder(String orderNo, Integer payType);

    StoreOrders findStoreOrders(String orderNo);

    void payStoreOrders(String orderNo, Integer payType);

    void addDetails(String orderNo, Integer userId, String merchantId, Double money,Integer type);

    MerchantUser findMerchantByUid(String merchantId);

    MerchantUser findMerchantByName(String name);

    void bindingManageEquipment(String uuid, String uid, Integer sendDeviceType);

    void subtractMoney(Integer id, Double money);

    void withdrawCash(Integer id, Double money, int i, String uuid);

    Scene findSceneByKey(String s);

    void addOrdersDetail(String detailId, String uuid, Double countMoney, Integer userId,Integer type,Integer payedId,String note,Integer ordersType);

    void bindingUserCid(Integer userId, String cid);


    List<User> findUserByCid(String cid);

    void logout(Integer id);

    void manageLogout(Integer id);

    User findUserByRealNameAndIdCard(String name, String idCard);

    Scene findSceneByValue(String scenes);

    void bindingManageCid(Integer id, String cid);

    MerchantUser findMerchantByCid(String cid);

    Orders findOrdersByVersion(String version);

    void cancelReceiveOrder(String uuid);

    void updateOrdersUpdateMoney(String uuid, Double money);

    void resetUpdateMoney(String uuid, Double resetUpdateMoney);

    Orders findStartTimeLimitOne(Integer userId, Integer code, String scenes);

    void onOff(Integer id, Integer onOff);

    void activeEnter(ActiveEnterRequest activeEnterRequest);

    void bindingAttestation(Integer id, String name, String accountName, String idCard);

    void uploadWorkProof(UploadWorkProofRequest uploadWorkProofRequest);

    String versionManage();

    AdminUser findAdminByName(String name);

    void registerPms(String uuid,String username, String password,  Integer grade);

    void addOperationNote(String operationId, Integer id,String uuid, Integer code, String value);

    void pmsUpdatePass(String name, String password);

    BoundMemberResponse findBoundMemberUser(String phoneNum);

    void addBoundMember(String phoneNum, Integer userId,String uuid);

    void upBoundMember(String uuid, Integer userId);

    void registerInputPass(String uuid, String phoneNum, String loadName, String md5Pass, int userGrade, String defaultNickName, Integer userId);

    List<User> findUsersForBackMoney(Double stopMoney, int i);

    void updateConsumeMoney(Integer id, Double consumeMoney);

    void updatePayPass(Integer id, String md5Pass);

    void taskPayed(String payedId);

    void addOrdersDetailForConsumeMoney(String detailId, String uuid, Double countMoney, Integer id, Integer key, Integer key1, String value, Integer key2);

    User findUserByUuid(String uuid);

    void balanceRecharge(Integer id, Double money);

    String getSysParameter(String key);

    DetailResponse findDetailByUidAndType(String payedId, Integer key);


    void updateComboEndTime(String orderNo, String date);

    void insertVideo(Integer id, String video);

    StoreTaskPayResponse findTaskPay(String payedId);

    PartyOrdersResponse findPartyByUid(String orderNo);

    void upPartyPay(String partyId, Integer code);

    List<PartyDetailedResponse> findPartyBillDetail(Integer id, Integer pageNum);

    void upUserGrade(Integer uid, int grade);

    List<UserVideoResponse> findVideoCheck(Integer id, int i);

    List<UserVideoResponse> selectUserVideoByCheckAndUserId(Integer videoCheck, Integer userId);

    void videoAdd(String uuid, Integer id, String video, String videoPic, Integer videoType);

    UserVideoResponse findVideoByUuid(String uuid);

    void videoUp(Integer id, String video, String videoPic, String uuid);

    void videoDel(String uuid);

    List<UserVideoResponse> findVideos(Integer userId);

    void addRechargeOrders(String uuid, Integer uid, Double money,Integer grade);

    RechargeOrdersResponse findRechargeOrders(String orderNo);

    Coupons findCoupons(Integer code);

    List<CouponsOrders> findCouponsOrdersByCode(Integer userId, Integer code);

    void addCouponsOrders(String uuid, String couponsId, Integer userId);

    void updateGoldMoney(Double totalFee, Integer id);

    List<CouponsOrdersResponse> findCouponsOrdersById(Integer id);

    TrystOrders findTrystOrdersByUid(String trystId);

    int upTrystOrdersPay(String trystId, Integer code, String value);

    String selectScenePicByScene(String sceneName);

    String selectSceneContentByScene(String sceneName);

    List<TrystReceive> selectReceiveByTrystId(String trystId);

    int delectReceiveByUserId(Integer userId, String trystId);

    int reduceTrystPersonCount(String trystId);

    int updateCidByUserId(Integer userId, String cid);

    List<User> selectUsersFromReceiveByTrystId(String trystId);
    
    Integer findUserIdByUuId(String uuid);
    
    int insertIdentityProof(IdentityProof proof);
    
    
    List<IdentityProof> selectIdentityProof(IdentityProof proof);
    
    List<IdentityProof> selectMyIdentityProof(IdentityProof proof);

    int logicDelectReceiveByUserId(Integer userId, String trystId);

    void registerVersionTwo(@Param("uuid")String uuid, @Param("phoneNum")String phoneNum,@Param("pass") String pass,@Param("nickname") String nickname,@Param("gender") Integer gender,@Param("birthday") String birthday,@Param("age") Integer age);

    String selectPhoneByUserId(Integer userId);

    void insertEaseMob(String uuid, String trystId, String groupId);

    String selectGroupIdByTrystId(String trystId);

    void deleteEaseMob(String trystId);

    int deductMoney(Double payMoney, Integer userId);

    void insertTrystReceive(String uuid, String trystId, Integer userId, Double money);

    void exitTrystReceive(Boolean deleted, String trystId);
}
