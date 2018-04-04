package com.wuai.company.pms.dao;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.*;
import com.wuai.company.pms.entity.request.TrystSceneRequest;
import com.wuai.company.pms.entity.request.VideoHomeRequest;
import com.wuai.company.pms.entity.response.PartiesResponse;
import com.wuai.company.pms.entity.response.TrystSceneResponse;
import com.wuai.company.pms.entity.response.TrystVideoHomeResponse;

import java.util.List;

/**
 * Created by 97947 on 2017/9/22.
 */
public interface PmsDao {
    List<ActiveContent> allActiveContent(Integer pageNum);

    void addActive(String uuid, String pic, String topic, String content, String time);

    void addOperationNote(String operationId, Integer id, String uuid, Integer code, String value);

    void updateActive(String uuid, String pic, String topic, String content, String time);

    void deletedActive(String uuid);

    List<User> findUser( String phone, String start, String end, Integer pageNum);

    List<InvitationDetailResponse> findInvitationDetail(Integer type, Integer code, String name, String startTime, String endTime, Integer pageNum);

    List<Orders> findOrders(Integer pageNum, String name, String startTime, String endTime, Integer type);

    List<AdminUser> showAdmin(Integer pageNum);

    void deletedAdmin(String uuid);

    List<SysResponse> showLabel(String key, String key1, String key2, String key3, String key4, String key5);

    void updateLabel(String key, String label);

    List<Scene> findScene();

    void updateScene(Scene scene);

    void insertScene(ScenesRequest scene);

    Scene findSceneByUid( String uuid);

    void deletedScene(String uuid);

    void updateKey(String uuid, String key);

    List<TalkResponse> showTalk();

    AdminUser findAdminByName(String name);

    Integer sizeOfActive();

    Integer sizeOfUser(String phone, String startTime, String endTime,Integer pageNum);

    List<InvitationDetailResponse> sizeOfDetails(String name, String startTime, String endTime, Integer code);

    Integer sizeOfOrdersMange(Integer pageNum, String name, String startTime, String endTime, Integer type);

    Integer sizeOfAdmin();

    List<SystemResponse> showSys();

    void updateSystem(String key,String value);

    void stopBackMoney( Integer userId, String key);

    List<StoreTaskDetailedResponse> findAllTasks(Integer pageNum);

    void updateTask(StoreActiveRequest request);

    void addTask(StoreActiveRequest request);

    void deletedTask(String uuid);

    void inputUserExcel(List<UserExcelRequest> list);

    void inputOrdersExcel(List<OrdersExcelRequest> list);

    User findUserByUuid(String userUuid);

    List<ActiveOrdersResponse> activeDetails(Integer type, Integer pageNum,String value,String startTime,String endTime);

    Integer activeDetailsSize(Integer type,String value,String startTime,String endTime);

    void upActiveDetails(String uuid, String expressName, String expressNum,Integer send);

    MerchantUser findMerchantByUser(String username);

    void insertMerchantUser(String uid, String username, String encryption, Integer type);

    List<MerchantUser>  findAllManage(Integer pageNum);

    List<PartiesResponse> showAllParties(Integer pageNum, String value);

    void partyConfirm(String value, String uuid, String note);

    Integer partiesSize(String value);


    List<UserVideoResponse>  findAllUserVideo(Integer videoCheck);

    void checkVideo(String uuid, Integer videoCheck,String note);

    List<WithdrawCashResponse> findAllCash(Integer cash);

    void upCash(String uuid, String note, Integer cash);

    UserVideoResponse findVideoByUuid(String uuid);

    WithdrawCashResponse findCashByUuid(String uuid);

    List<Coupons> findAllCoupons();

    void couponsAdd(CouponsRequest couponsRequest);

    void couponsUp(CouponsRequest couponsRequest);

    void couponsDel(String uuid);

    void rechargeWallet(Integer userId, Double money);

    void addTrystScene(TrystSceneRequest request);

    void addTrystVideo(String video, String videoPic, String typeValue, Integer typeCode, String uuid);

    List<TrystSceneResponse> showTrystScene();

    List<TrystVideoHomeResponse> trystVideoShow();

    void trystVideoDel(String uuid);

    void trystSceneDel(String uuid);
    
    int insertVideoHomeSelective(VideoHome videoHome);
    
    List<IdentityProof> findIdentityProof(IdentityProof proof);
    
    int updateIdentityProofSelective(IdentityProof proof);
}
