package com.wuai.company.pms.dao.impl;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.*;
import com.wuai.company.pms.dao.PmsDao;
import com.wuai.company.pms.entity.request.TrystSceneRequest;
import com.wuai.company.pms.entity.request.VideoHomeRequest;
import com.wuai.company.pms.entity.response.PartiesResponse;
import com.wuai.company.pms.entity.response.TrystSceneResponse;
import com.wuai.company.pms.entity.response.TrystVideoHomeResponse;
import com.wuai.company.pms.mapper.PmsMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 97947 on 2017/9/22.
 */
@Repository
public class PmsDaoImpl implements PmsDao {
    @Autowired
    private PmsMapper pmsMapper;
    @Override
    public List<ActiveContent> allActiveContent(Integer pageNum) {
        return pmsMapper.allActiveContent(pageNum);
    }

    @Override
    public void addActive(String uuid, String pic, String topic, String content, String time) {
        pmsMapper.addActive(uuid,pic,topic,content,time);
    }

    @Override
    public void addOperationNote(String operationId, Integer id, String uuid, Integer code, String value) {
        pmsMapper.addOperationNote(operationId,id,uuid,code,value);
    }

    @Override
    public void updateActive(String uuid, String pic, String topic, String content, String time) {
        pmsMapper.updateActive(uuid,pic,topic,content,time);
    }

    @Override
    public void deletedActive(String uuid) {
        pmsMapper.deletedActive(uuid);
    }

    @Override
    public List<User> findUser( String phone, String start, String end, Integer pageNum) {
        return pmsMapper.findUser(phone,start,end,pageNum);
    }

    @Override
    public List<InvitationDetailResponse> findInvitationDetail(Integer type, Integer code, String name, String startTime, String endTime, Integer pageNum) {
        return pmsMapper.findInvitationDetail(type,code,name,startTime,endTime,pageNum);
    }

    @Override
    public List<Orders> findOrders(Integer pageNum, String name, String startTime, String endTime, Integer type) {
        return pmsMapper.findOrders(pageNum,name,startTime,endTime,type);
    }

    @Override
    public List<AdminUser> showAdmin(Integer pageNum) {
        return pmsMapper.showAdmin(pageNum);
    }

    @Override
    public void deletedAdmin(String uuid) {
        pmsMapper.deletedAdmin(uuid);
    }

    @Override
    public List<SysResponse> showLabel(String key, String key1, String key2, String key3, String key4, String key5) {
        return pmsMapper.showLabel(key,key1,key2,key3,key4, key5);
    }

    @Override
    public void updateLabel(String key, String label) {
        pmsMapper.updateLabel( key,  label);
    }

    @Override
    public List<Scene> findScene() {
        return pmsMapper.findScene();
    }

    @Override
    public void updateScene(Scene scene) {
        pmsMapper.updateScene(scene);
    }

    @Override
    public void insertScene(ScenesRequest scene) {
        pmsMapper.insertScene(scene);
    }

    @Override
    public Scene findSceneByUid(String uuid) {
        return pmsMapper.findSceneByUid(uuid);
    }

    @Override
    public void deletedScene(String uuid) {
        pmsMapper.deletedScene(uuid);
    }

    @Override
    public void updateKey(String uuid, String key) {
        pmsMapper.updateKey(uuid,key);
    }

    @Override
    public List<TalkResponse> showTalk() {
        return pmsMapper.showTalk();
    }

    @Override
    public AdminUser findAdminByName(String name) {
        return pmsMapper.findAdminByName(name);
    }

    @Override
    public Integer sizeOfActive() {
        return pmsMapper.sizeOfActive();
    }

    @Override
    public Integer sizeOfUser(String phone, String startTime, String endTime,Integer pageNum) {
        return pmsMapper.sizeOfUser(phone,startTime,endTime,pageNum);
    }

    @Override
    public List<InvitationDetailResponse> sizeOfDetails(String name, String startTime, String endTime,  Integer code) {
        return pmsMapper.sizeOfDetails(name,startTime,endTime,code);
    }

    @Override
    public Integer sizeOfOrdersMange(Integer pageNum, String name, String startTime, String endTime, Integer type) {
        return pmsMapper.sizeOfOrdersMange( pageNum,  name,  startTime,  endTime,  type);
    }

    @Override
    public Integer sizeOfAdmin() {
        return pmsMapper.sizeOfAdmin();
    }
    @Override
    public List<SystemResponse>  showSys() {
        return pmsMapper.showSys();
    }

    @Override
    public void updateSystem(String key,String value) {
        pmsMapper.updateSystem(key,value);
    }

    @Override
    public void stopBackMoney( Integer userId, String key) {
        pmsMapper.stopBackMoney(userId,key);
    }

    @Override
    public List<StoreTaskDetailedResponse> findAllTasks(Integer pageNum) {
        return pmsMapper.findAllTasks(pageNum);
    }

    @Override
    public void updateTask(StoreActiveRequest request) {
        pmsMapper.updateTask(request);
    }

    @Override
    public void addTask(StoreActiveRequest request) {
        pmsMapper.addTask(request);
    }

    @Override
    public void deletedTask(String uuid) {
        pmsMapper.deletedTask(uuid);
    }

    @Override
    public void inputUserExcel(List<UserExcelRequest> list) {
        pmsMapper.inputUserExcel(list);
    }

    @Override
    public void inputOrdersExcel(List<OrdersExcelRequest> list) {
        pmsMapper.inputOrdersExcel(list);
    }

    @Override
    public User findUserByUuid(String uuid) {
       return pmsMapper.findUserByUuid(uuid);
    }

    @Override
    public List<ActiveOrdersResponse> activeDetails(Integer type, Integer pageNum,String value,String startTime,String endTime) {
        return pmsMapper.activeDetails(type,pageNum,value,startTime,endTime);
    }

    @Override
    public Integer activeDetailsSize(Integer type,String value,String startTime,String endTime) {
        return pmsMapper.activeDetailsSize(type,value,startTime,endTime);
    }

    @Override
    public void upActiveDetails(String uuid, String expressName, String expressNum,Integer send) {
        pmsMapper.upActiveDetails(uuid,expressName,expressNum,send);
    }

    @Override
    public MerchantUser findMerchantByUser(String username) {
        return pmsMapper.findMerchantByUser(username);
    }

    @Override
    public void insertMerchantUser(String uid, String username, String pass, Integer type) {
        pmsMapper.insertMerchantUser(uid,username,pass,type);
    }

    @Override
    public List<MerchantUser>  findAllManage(Integer pageNum) {
        return pmsMapper.findAllManage(pageNum);
    }

    @Override
    public List<PartiesResponse> showAllParties(Integer pageNum, String value) {
        return pmsMapper.showAllParties(pageNum,value);
    }

    @Override
    public void partyConfirm(String value, String uuid, String note) {
        pmsMapper.partyConfirm(value,uuid,note);
    }

    @Override
    public Integer partiesSize(String value) {
        return  pmsMapper.partiesSize(value);
    }

    @Override
    public List<UserVideoResponse> findAllUserVideo(Integer videoCheck) {
        return pmsMapper.findAllUserVideo(videoCheck);
    }

    @Override
    public void checkVideo(String uuid, Integer videoCheck,String note) {
        pmsMapper.checkVideo(uuid,videoCheck,note);
    }

    @Override
    public List<WithdrawCashResponse> findAllCash(Integer cash) {
        return  pmsMapper.findAllCash(cash);
    }

    @Override
    public void upCash(String uuid, String note, Integer cash) {
        pmsMapper.upCash(uuid,note,cash);
    }

    @Override
    public UserVideoResponse findVideoByUuid(String uuid) {
        return  pmsMapper.findVideoByUuid(uuid);
    }

    @Override
    public WithdrawCashResponse findCashByUuid(String uuid) {
        return pmsMapper.findCashByUuid(uuid);
    }

    @Override
    public List<Coupons> findAllCoupons() {
        return  pmsMapper.findAllCoupons();
    }

    @Override
    public void couponsAdd(CouponsRequest couponsRequest) {
        pmsMapper.couponsAdd(couponsRequest);
    }

    @Override
    public void couponsUp(CouponsRequest couponsRequest) {
        pmsMapper.couponsUp(couponsRequest);
    }

    @Override
    public void couponsDel(String uuid) {
        pmsMapper.couponsDel(uuid);
    }

    @Override
    public void rechargeWallet(Integer userId, Double money) {
        pmsMapper.rechargeWallet(userId,money);
    }

    @Override
    public void addTrystScene(TrystSceneRequest request) {
        pmsMapper.addTrystScene(request);
    }

    @Override
    public void addTrystVideo(String video, String videoPic, String typeValue, Integer typeCode, String uuid) {
        pmsMapper.addTrystVideo(video, videoPic, typeValue, typeCode, uuid);
    }

    @Override
    public List<TrystSceneResponse> showTrystScene() {
        return pmsMapper.showTrystScene();

    }

    @Override
    public List<TrystVideoHomeResponse> trystVideoShow() {
        return pmsMapper.trystVideoShow();
    }

    @Override
    public void trystVideoDel(String uuid) {
        pmsMapper.trystVideoDel(uuid);
    }

    @Override
    public void trystSceneDel(String uuid) {
        pmsMapper.trystSceneDel(uuid);

    }

	@Override
	public int insertVideoHomeSelective(VideoHome videoHome) {
		
		return pmsMapper.insertVideoHomeSelective(videoHome);
	}

	@Override
	public List<IdentityProof> findIdentityProof(IdentityProof proof) {
		return pmsMapper.findIdentityProof(proof);
	}

	@Override
	public int updateIdentityProofSelective(IdentityProof proof) {
		return pmsMapper.updateIdentityProofSelective(proof);
	}

}
