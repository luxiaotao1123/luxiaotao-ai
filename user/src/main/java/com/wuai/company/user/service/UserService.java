package com.wuai.company.user.service;

//import com.wuai.company.entity.User;
import com.wuai.company.entity.User;
import com.wuai.company.entity.request.ActiveEnterRequest;
import com.wuai.company.entity.request.IdentityProof;
import com.wuai.company.entity.request.UploadWorkProofRequest;
import com.wuai.company.util.Response;

import javax.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.util.List;

/**
 * 订单的servi层
 * Created by Ness on 2017/5/25.
 */
public interface UserService {

    /**
     * 根据Uuid查询用户信息
     *
     * @param id 用户的id
     * @return
     */
    Response findUserOneById(Integer id);


    /**
     * 保存用户信息
     *
     * @param user
     */
    Response saveUser(User user);


    /**
     * 更新用户信息
     *
     * @param user
     */
    Response updateUserOneById(User user);

    //根据用户UserId查询
    Response findUserByUserId(Integer userId);

    //添加用户基本信息
    Response insertUserBasicData(Integer uid, String nickName, Integer gender, String age, String occupation, String height, String weight, String city, String zodiac,String lable,String signature);

    //添加 照片
    Response addPicture(Integer uid, String picture);

    //修改密码
    Response updatePass(Integer uid, String oldPass, String newPass);

    //提交反馈意见
    Response addFeedback(Integer uid, String feedback);

    //充值
    Response recharge(Integer uid, Double money);

    //绑定支付宝
    Response bindingAliPay(Integer uid, String realName, String accountNum);
    //账单明细
    Response billDetail(Integer uid,Integer pageNum) ;

    //登录
    Response login(String phoneNum, String pass,String uid,Integer sendDeviceType,HttpServletResponse response);

    Response register(String phoneNum,String pass, String code, String invitationCode);

    Response sendMsg(String phoneNum);

    Response forgetPass(String phoneNum, String pass);

    Response findMoney(Integer uid);


    Response verificationCode(String phoneNum, String code);

    Response addIcon(Integer uid, String icon);

    String findDetailByUid(String orderNo,Double money,Integer payTypeCode);

    Response manageLogin(String name, String pass, String uid, Integer sendDeviceType, HttpServletResponse response);

    Response bill(Integer attribute, Integer pageNum);

    Response withdrawCash(Integer attribute, String realName, String accountNum, Double money);

    String addInvitaion(String orderId,Double money,Integer payTypeCode) throws ParseException;

    Response credit(String certNo, String name, Integer admittanceScore);
//    Response credit(Integer id,String certNo, String name, Integer admittanceScore);

    Response bindingUserCid(Integer attribute, String cid);

    Response logout(Integer attribute);

    Response manageLogout(Integer attribute);

    Response bindingManageCid(Integer attribute, String cid);

    String test(String orderNo);

    Response onOff(Integer attribute, Integer onOff);

    Response otherUsers(Integer attribute, Integer userId);

    Response activeEnter(ActiveEnterRequest activeEnterRequest);

    Response bindingAttestation(Integer attribute, String name, String accountName, String idCard);

    Response uploadWorkProof(UploadWorkProofRequest uploadWorkProofRequest);

    Response versionManage(String version);

    Response pmsLogin(String name, String password);

    Response pmsRegister(String name,String username, String password, Integer grade);


    Response registerSendMsg(String phoneNum);

    Response pmsUpdatePass(String name, String password);

    Response msgLogin(String phoneNum, String msg, String uid, Integer sendDeviceType, HttpServletResponse response);

    Response bindingInvitation(String phoneNum, Integer userId);

    Response registerInputPass(String phoneNum, String pass);

    Response ordersPay(Integer id, Double money, String ordersId,String pass);

    Response payPass(Integer attribute, String pass);

    Response upPayPass(Integer attribute, String pass);

    Response getPayMoney(Integer attribute);

    String payBalanceRecharge(String orderNo);

    String taskPayed(String orderNo,Double totalFee,Integer payTypeCode);

    String consumeMoney(String orderNo,Double totalFee,Integer payTypeCode);

    Response insertVideo(Integer attribute, String video);

    Response ordersBillDetail(Integer attribute, Integer pageNum);

    Response storeBillDetail(Integer attribute, Integer pageNum);

    String partyPay(String orderNo, Double total_fee,Integer payTypeCode);

    Response partyBillDetail(Integer attribute, Integer pageNum);

    Response goldUser(Integer attribute, Double money);

    Response videoAdd(Integer attribute, String video,String videoPic, Integer videoType);

    Response videoUp(Integer attribute, String video, String videoPic, String uuid);

    Response videoDel(Integer attribute, String uuid);

    String rechargeMoney(String orderNo, Double total_fee,Integer payTypeCode);

    String beGoldUser(String orderNo, Double total_fee, Integer payTypeCode);

    Response myCoupons(Integer id,Integer pageNum);

    Response certificationSuccess(Integer id);

    String trystOrdersPay(String orderNo, Double total_fee, int i);

    String cancelTrystUser(String orderNo, Double total_fee, int i);

//    String trystAdvanceMoneyPay(String orderNo, Double total_fee, int i);

    Response detailsAuth(Integer id, String userId);


    Boolean aliTransfer(String out_biz_no,String payee_account,String amount,String payer_show_name,String payee_real_name,String remark);
    
    Response insertIdentityProof(Integer id,String proof);
    
    
    Response selectIdentityProof(Integer userId,Boolean deleted,Integer state);
    
  //根据用户UserId查询自身信息
    Response getIdentityProofState(Integer userId);
    //version2.0 注册
    Response register(String phoneNum,String pass, String code, Integer gender,String nickName,String birthday);
    
    Response checkCode(String phoneNum, String code);
}
