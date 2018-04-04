package com.wuai.company.user.dao.impl;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.ActiveEnterRequest;
import com.wuai.company.entity.request.IdentityProof;
import com.wuai.company.entity.request.UploadWorkProofRequest;
import com.wuai.company.user.dao.UserDao;
//import com.wuai.company.entity.User;
import com.wuai.company.user.entity.Response.*;
import com.wuai.company.user.mapper.UserMapper;
import com.wuai.company.util.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单的dao具体实现层
 * Created by Ness on 2017/5/25.
 */
@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserMapper userMapper;


    private Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public String selectCidById(Integer userId) {
        return userMapper.selectCidById(userId);
    }

    @Override
    public Integer selectTypeById(Integer userId) {
        return userMapper.selectTypeById(userId);
    }

    /**
     * 根据Uuid查询用户信息
     *
     * @param id 用户的id
     * @return
     */
    @Override
    public User findUserOneById(Integer id) {

        if (id==null) {
            logger.warn("查询用户信息Id为空");
            return null;
        }

        logger.info("Id={}查询用户信息", id);


        return userMapper.findUserOneById(id);


    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone 用户的手机
     * @return
     */
    @Override
    public User findUserOneByPhone(String phone) {

        if (StringUtils.isEmpty(phone)) {
            logger.warn("查询用户信息phone为空");
            return null;
        }

        logger.info("phone={}查询用户信息", phone);


        return userMapper.findUserOneByPhone(phone);


    }

    /**
     * 保存用户信息
     *
     * @param user
     */
    @Override
    public void saveUser(User user) {

        if (user == null || StringUtils.isEmpty(user.getUuid())) {
            logger.warn("保存用户信息的实体类为空");
            return;
        }

        logger.info("Id={}开始保存用户信息", user.getUuid());

        userMapper.saveUser(user);
        logger.info("Id={}结束保存用户信息", user.getUuid());


    }


    /**
     * 更新用户信息
     *
     * @param user
     */
    @Override
    public void updateUserOneById(User user) {
        if (user == null || StringUtils.isEmpty(user.getUuid())) {
            logger.warn("更新用户信息的实体类为空");
            return;
        }

        logger.info("Id={}开始更新用户信息", user.getUuid());

        userMapper.updateUserOneById(user);

        logger.info("Id={}结束保存用户信息", user.getUuid());


    }

    @Override
    public User findUserByUserId(Integer userId) {
        return userMapper.findUserByUserId(userId);
    }

    @Override
    public void updateMoney(Double money, Integer uid) {
        userMapper.updateMoney(money, uid);
    }

    @Override
    public void insertUserBasicData(Integer uid, String nickName, Integer gender, String age, String occupation, String height, String weight, String city, String zodiac,String label,String signature) {
        userMapper.insertUserBasicData(uid,nickName,gender,age,occupation,height,weight,city,zodiac,label,signature);
    }

    @Override
    public void addPicture(Integer uid, String picture) {
        userMapper.addPicture(uid,picture);
    }

    @Override
    public void updatePass(Integer uid, String newPass) {
        userMapper.updatePass(uid,newPass);
    }

    @Override
    public User selectPass(Integer uid, String oldPass) {
        return userMapper.selectPass(uid,oldPass);
    }
    //提交反馈意见
    @Override
    public void addFeedback(String uid,Integer userId, String feedback, String nickName, String phoneNum) {
        userMapper.addFeedback(uid,userId,feedback,nickName,phoneNum);
    }
    //绑定支付宝
    @Override
    public void bindingAliPay(Integer uid, String realName, String accountNum) {
        userMapper.bindingAliPay(uid,realName,accountNum);
    }

    @Override
    public List<BillDetailResponse> findBillDetail(Integer uid,Integer pageNum) {
        return userMapper.findBillDetail(uid,pageNum);
    }

    @Override
    public void register(String uuid, String phoneNum,String loadName, String pass,Integer userGrade,String defaultNickName) {
        userMapper.register(uuid,phoneNum,pass,loadName,userGrade,defaultNickName);
    }

    @Override
    public void forgetPass(String phoneNum, String pass) {
        userMapper.forgetPass(phoneNum,pass);
    }

    @Override
    public void bindingEquipment(Integer uuid, String equipment,Integer sendDeviceType) {
        userMapper.bindingEquipment(uuid,equipment,sendDeviceType);
    }

    @Override
    public void addIcon(Integer uid, String icon) {
        userMapper.addIcon(uid,icon);
    }

    @Override
    public DetailResponse findDetailByUid(String orderNo) {
        return userMapper.findDetailByUid(orderNo);
    }

    @Override
    public void pay(String orderNo) {
        userMapper.pay(orderNo);
    }

    @Override
    public User personalDetails(String uid) {
        return userMapper.personalDetails(uid);
    }

    @Override
    public List<StoreBillDetailResponse> findStoreBillDetail(Integer uid,Integer pageNum) {
        return userMapper.findStoreBillDetail(uid,pageNum);
    }

    @Override
    public List<OrdersUResponse> findByUuid(String ordersId) {
        return userMapper.findByUuid(ordersId);
    }

    @Override
    public Orders findOrderByOrderNo(String orderNo) {
        return userMapper.findOrderByOrderNo(orderNo);
    }

    @Override
    public void addRechargeSheet(Integer userId,String orderNo, Double money, String uuid, Integer detailType, String detailTypeValue, Integer payTypeCode, String payTypeValue,Boolean success) {
        userMapper.addRechargeSheet(userId ,orderNo,money,uuid,detailType,detailTypeValue,payTypeCode,payTypeValue,success);
    }

    @Override
    public void payOrder(String orderNo, Integer payType) {
        userMapper.payOrder(orderNo,payType);
    }

    @Override
    public StoreOrders findStoreOrders(String orderNo) {
        return userMapper.findStoreOrders(orderNo);
    }

    @Override
    public void payStoreOrders(String orderNo, Integer payType) {
        userMapper.payStoreOrders(orderNo,payType);
    }

    @Override
    public void addDetails(String orderNo, Integer userId, String merchantId, Double money,Integer type) {
        userMapper.addDetails(orderNo,userId,merchantId,money,type);
    }

    @Override
    public MerchantUser findMerchantByUid(String merchantId) {
        return userMapper.findMerchantByUid(merchantId);
    }

    @Override
    public MerchantUser findMerchantByName(String name) {
        return userMapper.findMerchantByName(name);
    }

    @Override
    public void bindingManageEquipment(String uuid, String uid, Integer sendDeviceType) {
        userMapper.bindingManageEquipment(uuid,uid,sendDeviceType);
    }

    @Override
    public void subtractMoney(Integer id, Double money) {
        userMapper.subtractMoney(id,money);
    }

    @Override
    public void withdrawCash(Integer id, Double money, int type, String uuid) {
        userMapper.withdrawCash(id,money,type,uuid);
    }

    @Override
    public Scene findSceneByKey(String s) {
        return userMapper.findSceneByKey(s);
    }

    @Override
    public void addOrdersDetail(String detailId, String uuid, Double countMoney, Integer userId,Integer type,Integer payedId,String note,Integer ordersType) {
        userMapper.addOrdersDetail(detailId,uuid,countMoney,userId,type,payedId,note,ordersType);
    }

    @Override
    public void bindingUserCid(Integer userId, String cid) {
        userMapper.bindingUserCid(userId,cid);
    }

    @Override
    public List<User> findUserByCid(String cid) {
        return userMapper.findUserByCid(cid);
    }

    @Override
    public void logout(Integer id) {

         Integer a =userMapper.logout(id);
        System.out.println("out="+a);
    }

    @Override
    public void manageLogout(Integer id) {
         userMapper.manageLogout(id);
    }

    @Override
    public User findUserByRealNameAndIdCard(String name, String idCard) {
        return userMapper.findUserByRealNameAndIdCard(name,idCard);
    }

    @Override
    public Scene findSceneByValue(String scenes) {
        return userMapper.findSceneByValue(scenes);
    }

    @Override
    public void bindingManageCid(Integer id, String cid) {
        userMapper.bindingManageCid(id,cid);
    }

    @Override
    public MerchantUser findMerchantByCid(String cid) {
        return userMapper.findMerchantByCid(cid);
    }

    @Override
    public Orders findOrdersByVersion(String version) {
        return userMapper.findOrdersByVersion(version);
    }

    @Override
    public void cancelReceiveOrder(String uuid) {
        userMapper.cancelReceiveOrder(uuid);
    }

    @Override
    public void updateOrdersUpdateMoney(String uuid, Double money) {
        userMapper.updateOrdersUpdateMoney( uuid,  money);
    }

    @Override
    public void resetUpdateMoney(String uuid, Double resetUpdateMoney) {
        userMapper.resetUpdateMoney(uuid,resetUpdateMoney);
    }

    @Override
    public Orders findStartTimeLimitOne(Integer userId, Integer code, String scenes) {
        return userMapper.findStartTimeLimitOne( userId,  code,scenes);
    }

    @Override
    public void onOff(Integer id, Integer onOff) {
        userMapper.onOff(id,onOff);
    }

    @Override
    public void activeEnter(ActiveEnterRequest activeEnterRequest) {
         userMapper.activeEnter(activeEnterRequest.getNickName(),activeEnterRequest.getGender(),activeEnterRequest.getAge(),activeEnterRequest.getHeight(),activeEnterRequest.getWeight(),activeEnterRequest.getLabels(),activeEnterRequest.getUuid());
    }

    @Override
    public void bindingAttestation(Integer id, String name, String accountName, String idCard) {
        userMapper.bindingAttestation(id,name,accountName, idCard);
    }

    @Override
    public void uploadWorkProof(UploadWorkProofRequest uploadWorkProofRequest) {
        userMapper.uploadWorkProof(uploadWorkProofRequest);
    }

    @Override
    public String versionManage() {

        return userMapper.versionManage();
    }

    @Override
    public AdminUser findAdminByName(String name) {
        return userMapper.findAdminByName(name);
    }

    @Override
    public void registerPms(String uuid,String username, String password,  Integer grade) {
        userMapper.registerPms(uuid,username,password,grade);
    }

    @Override
    public void addOperationNote(String operationId, Integer id, String uuid, Integer code, String value) {
        userMapper.addOperationNote(operationId,id,uuid,code,value);
    }

    @Override
    public void pmsUpdatePass(String name, String password) {
        userMapper.pmsUpdatePass(name,password);
    }

    @Override
    public BoundMemberResponse findBoundMemberUser(String phoneNum) {
        return userMapper.findBoundMemberUser(phoneNum);
    }

    @Override
    public void addBoundMember(String phoneNum, Integer userId,String uuid) {
        userMapper.addBoundMember( phoneNum,  userId, uuid);
    }

    @Override
    public void upBoundMember(String uuid, Integer userId) {
        userMapper.upBoundMember( uuid,  userId);
    }

    @Override
    public void registerInputPass(String uuid, String phoneNum, String loadName, String pass, int userGrade, String defaultNickName, Integer userId) {
        userMapper.registerInputPass(uuid,phoneNum,pass,loadName,userGrade,defaultNickName,userId);
    }

    @Override
    public List<User> findUsersForBackMoney(Double stopMoney, int code) {
        return userMapper.findUsersForBackMoney(stopMoney,code);
    }

    @Override
    public void updateConsumeMoney(Integer id, Double consumeMoney) {
        userMapper.updateConsumeMoney(id,consumeMoney);
    }

    @Override
    public void updatePayPass(Integer id, String md5Pass) {
        userMapper.updatePayPass(id,md5Pass);
    }

    @Override
    public void taskPayed(String payedId) {
        userMapper.taskPayed(payedId);
    }

    @Override
    public void addOrdersDetailForConsumeMoney(String detailId, String uuid, Double countMoney, Integer userId, Integer type, Integer payedId, String note, Integer ordersType) {
        userMapper.addOrdersDetailForConsumeMoney(detailId,uuid,countMoney,userId,type,payedId,note,ordersType);
    }

    @Override
    public User findUserByUuid(String uuid) {
        return userMapper.findUserByUuid(uuid);
    }

    @Override
    public void balanceRecharge(Integer id, Double money) {
        userMapper.balanceRecharge(id,money);
    }

    @Override
    public String getSysParameter(String key) {
        return  userMapper.getSysParameter(key);
    }

    @Override
    public DetailResponse findDetailByUidAndType(String payedId, Integer key) {
        return userMapper.findDetailByUidAndType( payedId,  key);
    }

    @Override
    public void updateComboEndTime(String orderNo, String date) {
        userMapper.updateComboEndTime(orderNo,date);
    }

    @Override
    public void insertVideo(Integer id, String video) {
        userMapper.insertVideo(id,video);
    }

    @Override
    public StoreTaskPayResponse findTaskPay(String payedId) {
        return userMapper.findTaskPay(payedId);
    }

    @Override
    public PartyOrdersResponse findPartyByUid(String orderNo) {
        return userMapper.findPartyByUid(orderNo);
    }

    @Override
    public void upPartyPay(String partyId, Integer code) {
        userMapper.upPartyPay(partyId,code);
    }

    @Override
    public List<PartyDetailedResponse> findPartyBillDetail(Integer id, Integer pageNum) {
        return userMapper.findPartyBillDetail(id,pageNum);
    }

    @Override
    public void upUserGrade(Integer uid, int grade) {
        userMapper.upUserGrade(uid,grade);
    }

    @Override
    public List<UserVideoResponse>  findVideoCheck(Integer id, int i) {
        return userMapper.findVideoCheck(id,i);
    }

    @Override
    public List<UserVideoResponse> selectUserVideoByCheckAndUserId(Integer videoCheck, Integer userId) {
        return userMapper.selectUserVideoByCheckAndUserId(videoCheck,userId);
    }

    @Override
    public void videoAdd(String uuid, Integer id, String video, String videoPic, Integer videoType) {
        userMapper.videoAdd(uuid,id,video,videoPic,videoType);
    }

    @Override
    public UserVideoResponse findVideoByUuid(String uuid) {
        return userMapper.findVideoByUuid(uuid);
    }

    @Override
    public void videoUp(Integer id, String video, String videoPic, String uuid) {
        userMapper.videoUp( id,  video,  videoPic,  uuid);
    }

    @Override
    public void videoDel(String uuid) {
        userMapper.videoDel( uuid);
    }

    @Override
    public List<UserVideoResponse> findVideos(Integer userId) {
        return userMapper.findVideos(userId);
    }

    @Override
    public void addRechargeOrders(String uuid, Integer uid, Double money,Integer grade) {
        userMapper.addRechargeOrders(uuid,uid,money,grade);
    }

    @Override
    public RechargeOrdersResponse findRechargeOrders(String orderNo) {
        return userMapper.findRechargeOrders(orderNo);
    }

    @Override
    public Coupons findCoupons(Integer code) {
        return userMapper.findCoupons(code);
    }

    @Override
    public List<CouponsOrders> findCouponsOrdersByCode(Integer userId, Integer code) {
        return userMapper.findCouponsOrdersByCode(userId,code);
    }

    @Override
    public void addCouponsOrders(String uuid, String couponsId, Integer userId) {
        userMapper.addCouponsOrders(uuid,couponsId,userId);
    }

    @Override
    public void updateGoldMoney(Double totalFee, Integer id) {
        userMapper.updateGoldMoney(totalFee,id);
    }

    @Override
    public List<CouponsOrdersResponse> findCouponsOrdersById(Integer id) {
        return userMapper.findCouponsOrdersById(id);
    }

    @Override
    public TrystOrders findTrystOrdersByUid(String trystId) {
        return userMapper.findTrystOrdersByUid(trystId);
    }

    @Override
    public int upTrystOrdersPay(String trystId, Integer code, String value) {
        return userMapper.upTrystOrdersPay(trystId,code,value);
    }

    @Override
    public String selectScenePicByScene(String sceneName) {
        return userMapper.selectScenePicByScene(sceneName);
    }

    @Override
    public String selectSceneContentByScene(String sceneName) {
        return userMapper.selectSceneContentByScene(sceneName);
    }

    @Override
    public List<TrystReceive> selectReceiveByTrystId(String trystId) {
        return userMapper.selectReceiveByTrystId(trystId);
    }

    @Override
    public int delectReceiveByUserId(Integer userId, String trystId){
        return userMapper.delectReceiveByUserId(userId, trystId);
    }

    @Override
    public int reduceTrystPersonCount(String trystId){
        return userMapper.reduceTrystPersonCount(trystId);
    }

    @Override
    public int updateCidByUserId(Integer userId, String cid){
        return userMapper.updateCidByUserId(userId,cid);
    }

    @Override
    public List<User> selectUsersFromReceiveByTrystId(String trystId){
        return userMapper.selectUsersFromReceiveByTrystId(trystId);
    }

	@Override
	public Integer findUserIdByUuId(String uuid) {
		return userMapper.findUserIdByUuId(uuid);
	}

	@Override
	public int insertIdentityProof(IdentityProof proof) {
		return userMapper.insertIdentityProof(proof);
	}

	

	@Override
	public List<IdentityProof> selectIdentityProof(IdentityProof proof) {
		return userMapper.selectIdentityProof(proof);
	}

	@Override
	public List<IdentityProof> selectMyIdentityProof(IdentityProof proof) {
		return userMapper.selectMyIdentityProof(proof);
	}

    @Override
    public int logicDelectReceiveByUserId(Integer userId, String trystId) {
        return userMapper.logicDelectReceiveByUserId(userId,trystId);
    }

    @Override
    public void registerVersionTwo(String uuid, String phoneNum, String pass, String nickname, Integer gender, String birthday, Integer age) {
        userMapper.registerVersionTwo(uuid,phoneNum,pass,nickname,gender,birthday,age);
    }

    @Override
    public String selectPhoneByUserId(Integer userId) {
        return userMapper.selectPhoneByUserId(userId);
    }

    @Override
    public void insertEaseMob(String uuid, String trystId, String groupId) {
        userMapper.insertEaseMob(uuid,trystId,groupId);
    }

    @Override
    public String selectGroupIdByTrystId(String trystId) {
        return userMapper.selectGroupIdByTrystId(trystId);
    }

    @Override
    public void deleteEaseMob(String trystId) {
        userMapper.deleteEaseMob(trystId);
    }

    @Override
    public int deductMoney(Double payMoney, Integer userId){
        return userMapper.deductMoney(payMoney,userId);
    }

    @Override
    public void insertTrystReceive(String uuid, String trystId, Integer userId, Double money) {
        userMapper.insertTrystReceive(uuid,trystId,userId,money);
    }

    @Override
    public void exitTrystReceive(Boolean deleted, String trystId) {
        userMapper.exitTrystReceive(deleted,trystId);
    }

}
