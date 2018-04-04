package com.wuai.company.user.controller;


import com.wuai.company.entity.Response.PageRequest;
//import com.wuai.company.user.auth.MyRealm;
import com.wuai.company.entity.request.ActiveEnterRequest;
import com.wuai.company.entity.request.UploadWorkProofRequest;
import com.wuai.company.user.service.UserService;
import com.wuai.company.util.Response;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.ParseException;
import java.util.Date;

import static com.wuai.company.util.JwtToken.ID;

/**
 * 订单的controller
 * Created by Ness on 2017/5/25.
 */
@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    private UserService userService;

//    @Autowired
//    private MyRealm myRealm;
//
//
//    @GetMapping("index")
//    public Response findOrdersOneByUuid(Integer id, HttpServletResponse response) {
//
//        myRealm.load("15924179757", "123456", "12345", response);
//
//        return userService.findUserOneById(id);
//    }

    //    @GetMapping("/test/auth")
//    public String test(HttpServletRequest request) {
//
//        return "OK:" + request.getAttribute(ID);
//    }
    @GetMapping("/test/auth")
    public String test(HttpServletRequest request, Date name) {
        System.out.println(name);
        return "OK:" + request.getAttribute(ID);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">获取用户我的信息</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/mine/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request 请求头
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("mine/auth")
    public Response findUserByUserId(HttpServletRequest request) {
        //(Integer) request.getAttribute(ID)
        return userService.findUserByUserId((Integer) request.getAttribute(ID));
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">用户中心模块--钱包--获取金额，以及支付宝账号刷新</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/money/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("money/auth")
    public Response findMoney(HttpServletRequest request) {

        return userService.findMoney((Integer) request.getAttribute(ID));
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">添加/修改 用户信息</a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></a></span>
     * <br/>
     * <span>URL地址： <a href="">http://52woo.com:9203/user/insert/data/auth</a></span>
     * <br/>
     * </p>
     *
     * @param request
     * @param nickName   昵称
     * @param gender     性别
     * @param age        年龄
     * @param occupation 职业
     * @param height     身高
     * @param weight     体重
     * @param city       城市
     * @param zodiac     星座
     * @param label      标签
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p>
     */
    @PostMapping("insert/data/auth")
    public Response insertUserBasicData(HttpServletRequest request, String nickName, Integer gender, String age, String occupation, String height, String weight, String city, String zodiac, String label,String signature) {

        return userService.insertUserBasicData((Integer) request.getAttribute(ID), nickName, gender, age, occupation, height, weight, city, zodiac, label,signature);
    }

    // TODO: 2017/6/8 暂用一个接口   看前端传的 数据形式 再定

    /**
     * <p>
     * <span>API说明：<a style="color:blue">用户添加/修改/删除  照片</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/add/picture/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param picture 照片
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("add/picture/auth")
    public Response addPicture(HttpServletRequest request, String picture) {

        return userService.addPicture((Integer) request.getAttribute(ID), picture);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">用户添加  头像</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/add/icon/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param icon    头像
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("add/icon/auth")
    public Response addIcon(HttpServletRequest request, String icon) {
        return userService.addIcon((Integer) request.getAttribute(ID), icon);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">个人中心--我的密码--修改密码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/feedback/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param oldPass 旧密码
     * @param newPass 新密码
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("update/pass/auth")
    public Response updatePass(HttpServletRequest request, String oldPass, String newPass) {

        return userService.updatePass((Integer) request.getAttribute(ID), oldPass, newPass);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">提交反馈意见</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/feedback/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param feedback 反馈意见
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */

    @PostMapping("feedback/auth")
    public Response addFeedback(HttpServletRequest request, String feedback) {

        return userService.addFeedback((Integer) request.getAttribute(ID), feedback);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">充值</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/recharge/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param money   充值金额
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("recharge/auth")
    public Response recharge(HttpServletRequest request, Double money) {

        return userService.recharge((Integer) request.getAttribute(ID), money);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">用户中心模块--钱包--修改绑定支付宝</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/binding/alipay/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param realName   真实姓名
     * @param accountNum 支付宝账号
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("binding/alipay/auth")
    public Response bindingAlipay(HttpServletRequest request, String realName, String accountNum) {

        return userService.bindingAliPay((Integer) request.getAttribute(ID), realName, accountNum);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">我的订单模块 订单明细-->约会订单&商城订单</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/bill/detail/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param pageRequest 页码 pageNum
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("bill/detail/auth")
    public Response billDetail(HttpServletRequest request, PageRequest pageRequest) {

        return userService.billDetail((Integer) request.getAttribute(ID), pageRequest.getPageNum());
    }

    /**
     * 约会订单列表
     * @param request
     * @param pageNum
     * @return
     */
    @PostMapping("orders/bill/detail/auth")
    public Response ordersBillDetail(HttpServletRequest request, PageRequest pageNum) {

        return userService.ordersBillDetail((Integer) request.getAttribute(ID), pageNum.getPageNum());
    }

    /**
     * STORE订单列表
     * @param request
     * @param pageNum
     * @return
     */
    @PostMapping("store/bill/detail/auth")
    public Response storeBillDetail(HttpServletRequest request, PageRequest pageNum) {

        return userService.storeBillDetail((Integer) request.getAttribute(ID), pageNum.getPageNum());
    }

    /**
     * PARTY订单列表
     * @param request
     * @param pageNum
     * @return
     */
    @PostMapping("party/bill/detail/auth")
    public Response partyBillDetail(HttpServletRequest request, PageRequest pageNum) {

        return userService.partyBillDetail((Integer) request.getAttribute(ID), pageNum.getPageNum());
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">登录</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/login</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum       手机号
     * @param pass           登录密码
     * @param uid            设备id
     * @param sendDeviceType 设备标识 1 ios  2 android
     * @param response
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("login")
    public Response login(String phoneNum, String pass, String uid, Integer sendDeviceType, HttpServletResponse response) {

        return userService.login(phoneNum, pass, uid, sendDeviceType, response);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">发送验证码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/send/msg</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum 手机号
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("send/msg")
    public Response sendMsg(String phoneNum) {
        return userService.sendMsg(phoneNum);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">注册发送验证码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/register/send/msg</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("register/send/msg")

    public Response registerSendMsg(String phoneNum) {
        return userService.registerSendMsg(phoneNum);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">忘记密码/找回密码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/forget/pass</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum 用户手机号
     * @param pass     用户密码
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("forget/pass")
    public Response forgetPass(String phoneNum, String pass) {

        return userService.forgetPass(phoneNum, pass);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">验证验证码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/verification/code</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum 手机号
     * @param code     验证码
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("verification/code")
    public Response verificationCode(String phoneNum, String code) {

        return userService.verificationCode(phoneNum, code);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">用户提现</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/withdraw/cash/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param realName   真实姓名
     * @param accountNum 支付宝账号
     * @param money      金额
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("withdraw/cash/auth")
    public Response withdrawCash(HttpServletRequest request, String realName, String accountNum, Double money) {
        return userService.withdrawCash((Integer) request.getAttribute(ID), realName, accountNum, money);
    }


    @GetMapping("credit")
//    @PostMapping("credit/auth")
//    public Response credit(HttpServletRequest request,String certNo,String name,Integer admittanceScore){
    public Response credit(String certNo, String name) {
//        return userService.credit((Integer)request.getAttribute(ID),certNo,name,admittanceScore);
        Integer admittanceScore = 650;
        return userService.credit(certNo, name, admittanceScore);
    }

    @PostMapping("logout/auth")
    public Response logout(HttpServletRequest request) {
        return userService.logout((Integer) request.getAttribute(ID));
    }
    /*=====================================================================*/
    /*===========================商家后台登录===============================*/
    /*=====================================================================*/

    /**
     * <p>
     * <span>API说明：<a style="color:blue">管理后台登录</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/manage/login</ a></span>
     * <br/>
     * </p >
     *
     * @param name           用户名
     * @param pass           密码
     * @param uid            设备id
     * @param sendDeviceType 设备编号 1 ios 0 android
     * @param response
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("/manage/login")
    public Response manageLogin(String name, String pass, String uid, Integer sendDeviceType, HttpServletResponse response) {
        return userService.manageLogin(name, pass, uid, sendDeviceType, response);
    }

    @PostMapping("manage/logout/auth")
    public Response manageLogout(HttpServletRequest request) {
        return userService.manageLogout((Integer) request.getAttribute(ID));
    }

    @PostMapping("binding/user/cid/auth")
    public Response bindingUserCid(HttpServletRequest request, String cid) {
        return userService.bindingUserCid((Integer) request.getAttribute(ID), cid);
    }

    @PostMapping("binding/manage/cid/auth")
    public Response bindingManageCid(HttpServletRequest request, String cid) {
        return userService.bindingManageCid((Integer) request.getAttribute(ID), cid);
    }

    /***********************************************VERSION-1.2******************************************************/
    /**
     * <p>
     * <span>API说明：<a style="color:blue">版本控制</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/version/manage</ a></span>
     * <br/>
     * </p >
     *
     * @param version
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("version/manage")
    public Response versionManage(String version) {

        return userService.versionManage(version);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">个人中心--推送开关</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/on/off/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request 请求头
     * @param onOff   1 开 2关
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明
     * {
     * "msg": "",
     * "code": 200,
     * "data": []
     * }
     * </span>
     * <p>
     * </p >
     */
    @PostMapping("on/off/auth")
    public Response onOff(HttpServletRequest request, Integer onOff) {

        return userService.onOff((Integer) request.getAttribute(ID), onOff);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">个人中心--根据id查询用户信息</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/other/user/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param userId  需要查询个人信息的用户id
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */

    @PostMapping("other/user/auth")
    public Response otherUsers(HttpServletRequest request, Integer userId) {
        return userService.otherUsers((Integer) request.getAttribute(ID), userId);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">活动页</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/active/enter</ a></span>
     * <br/>
     * </p >
     *
     * @param activeEnterRequest
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("active/enter")
    public Response activeEnter(ActiveEnterRequest activeEnterRequest) {

        return userService.activeEnter(activeEnterRequest);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">上传工作证明</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/upload/work/proof</ a></span>
     * <br/>
     * </p >
     *
     * @param uploadWorkProofRequest
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("upload/work/proof")
    public Response uploadWorkProof(UploadWorkProofRequest uploadWorkProofRequest) {

        return userService.uploadWorkProof(uploadWorkProofRequest);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">注册模块---version-1.2</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/register</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum       手机号
     * @param pass           登录密码
     * @param code           验证码
     * @param invitationCode 邀请码 区分vip等级 若有则传 没有不传
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("register")
    public Response register(String phoneNum, String pass, String code, String invitationCode) {
        return userService.register(phoneNum, pass, code, invitationCode);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">芝麻认证</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/binding/attestation/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param name       真实姓名
     * @param idCard 身份证
     * @param accountNum 支付宝账号
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("binding/attestation/auth")
    public Response bindingAttestation(HttpServletRequest request, String name, String accountNum, String idCard) {

        return userService.bindingAttestation((Integer) request.getAttribute(ID), name, accountNum,idCard);
    }
    /***********************************************VERSION-1.2******************************************************/
    /***********************************************VERSION-2.0******************************************************/
    /**
     * <p>
     * <span>API说明：<a style="color:blue">短信登录</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/msg/login</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum
     * @param msg
     * @param uid
     * @param sendDeviceType
     * @param response
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("msg/login")
    public Response msgLogin(String phoneNum, String msg, String uid, Integer sendDeviceType, HttpServletResponse response) {

        return userService.msgLogin(phoneNum, msg, uid, sendDeviceType, response);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">第一次注册登录 设置密码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/register/pass</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum
     * @param pass     密码
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("register/pass")
    public Response registerInputPass(String phoneNum, String pass) {

        return userService.registerInputPass(phoneNum, pass);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">二维码---邀请注册</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/binding/invitation</ a></span>
     * <br/>
     * </p >
     *
     * @param phoneNum 手机号
     * @param userId   邀请方用户id
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("binding/invitation")
    public Response invitationRegister(String phoneNum, Integer userId) {

        return userService.bindingInvitation(phoneNum, userId);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">余额支付</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/orders/pay/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request  请求头
     * @param money    金额
     * @param ordersId 订单号
     * @param pass     支付密码
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("orders/pay/auth")
    public Response ordersPay(HttpServletRequest request, Double money, String ordersId, String pass) {

        return userService.ordersPay((Integer) request.getAttribute(ID), money, ordersId, pass);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">验证 支付密码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/pay/pass/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request 请求头
     * @param pass    支付密码
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("pay/pass/auth")
    public Response payPass(HttpServletRequest request, String pass) {

        return userService.payPass((Integer) request.getAttribute(ID), pass);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">修改支付密码</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/up/pay/pass/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param pass
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("up/pay/pass/auth")
    public Response upPayPass(HttpServletRequest request, String pass) {

        return userService.upPayPass((Integer) request.getAttribute(ID), pass);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">获取消费余额</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/get/pay/money/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("get/pay/money/auth")
    public Response getPayMoney(HttpServletRequest request) {
        return userService.getPayMoney((Integer) request.getAttribute(ID));
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">视频地址 增删改</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/user/change/video/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param video 视频地址
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @PostMapping("change/video/auth")
    public Response insertVideo(HttpServletRequest request, String video) {

        return userService.insertVideo((Integer) request.getAttribute(ID), video);
    }

    /**
     * 成为黄金会员
     * @param request
     * @param money
     * @return
     */
    @PostMapping("gold/user/auth")
    public Response goldUser(HttpServletRequest request,Double money){
        return userService.goldUser((Integer) request.getAttribute(ID), money);
    }

    /**
     * 视频上传  videoType 0 审核视频  1 普通展示视频
     * @param request
     * @param video
     * @param videoType
     * @return
     */
    @PostMapping("video/add/auth")
    public Response videoAdd(HttpServletRequest request,String video,String videoPic,Integer videoType){
        return userService.videoAdd((Integer) request.getAttribute(ID),video,videoPic,videoType);
    }

    /**
     * 视频修改
     * @param request
     * @param video
     * @return
     */
    @PostMapping("video/up/auth")
    public Response videoUp(HttpServletRequest request,String video,String videoPic,String uuid){
        return userService.videoUp((Integer) request.getAttribute(ID),video,videoPic,uuid);
    }
    /**
     * 视频删除
     * @param request

     * @return
     */
    @PostMapping("video/del/auth")
    public Response videoDel(HttpServletRequest request,String uuid){
        return userService.videoDel((Integer) request.getAttribute(ID),uuid);
    }

    /**
     * 我的优惠券
     * @param request
     * @return
     */
    @PostMapping("my/coupons/auth")
    public Response myCoupons(HttpServletRequest request,Integer pageNum){
        return userService.myCoupons((Integer)request.getAttribute(ID),pageNum);
    }

    /**
     * 芝麻认证--是否已 认证
     * @param request
     * @return
     */
    @PostMapping("certification/success/auth")
    public Response certificationSuccess(HttpServletRequest request){
        return userService.certificationSuccess((Integer)request.getAttribute(ID));

    }

    /***********************************************VERSION-2.0******************************************************/

    /**
     * 获取个人详情
     * @param request
     * @return
     */
    @PostMapping("details/auth")
    public Response detailsAuth(HttpServletRequest request,
                                @RequestParam(required = false) String userId){
        return userService.detailsAuth((Integer)request.getAttribute(ID),userId);
    }

    /**
     * 支付宝转账  测试用
     * @param 
     * @return
     */
//    @GetMapping("/ali/transfer")
//    public String aliTransfer(String out_biz_no, String payee_account,
//			String amount, String payer_show_name, String payee_real_name,
//			String remark) {
//        return userService.aliTransfer(out_biz_no, payee_account, amount, payer_show_name, payee_real_name, remark);
//    }

     /**
      * 提交身份审核
      * @param request
      * @param proof
      * @return
      */
    @PostMapping("identityProof/auth")
    public Response identityProof(HttpServletRequest request, String proof){
        return userService.insertIdentityProof((Integer)request.getAttribute(ID), proof);
    }
    
    
    
    /**
     * 获取身份审核列表
     * @param deleted
     * @param state
     * @return
     */
    @PostMapping("identityProof/find/auth")
    public Response findidentityProof(HttpServletRequest request,Boolean deleted,Integer state){
        return userService.selectIdentityProof((Integer)request.getAttribute(ID), deleted, state);
    }
    /**
     * 获取当前用户的身份认证状态
     * @param request
     * @return
     */
    @PostMapping("identityProof/getState/auth")
    public Response findMineByUserId(HttpServletRequest request) {
        return userService.getIdentityProofState((Integer) request.getAttribute(ID));
    }
    /**
     * version2.0 注册
     * @param phoneNum
     * @param pass
     * @param code
     * @param gender
     * @param nickName
     * @param birthday
     * @return
     */
    @PostMapping("register/versionTwo")
    public Response register(String phoneNum,String pass, String code, Integer gender,String nickName,String birthday) {
        return userService.register(phoneNum, pass, code, gender, nickName, birthday);
    }
    
    /**
     * 注册短信验证码校验
     * @param phoneNum
     * @param code
     * @return
     */
    @PostMapping("checkCode")
    public Response checkCode(String phoneNum, String code) {
		return userService.checkCode(phoneNum, code);
       
    }
    
}
