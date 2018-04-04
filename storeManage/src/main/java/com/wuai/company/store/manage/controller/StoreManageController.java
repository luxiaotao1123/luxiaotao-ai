package com.wuai.company.store.manage.controller;

import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.store.manage.service.StoreManageService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.wuai.company.util.JwtToken.ID;

/**
 * Created by Administrator on 2017/6/29.
 */
@RestController
@RequestMapping("manage")
public class StoreManageController {

    @Autowired
    private StoreManageService storeManageService;
//    @PostMapping("login")
//    public Response manageLogin(String name,String pass){
//
//    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">绑定支付宝</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/manage/binding/alipay/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request    请求头
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

        return storeManageService.bindingAlipay((Integer) request.getAttribute(ID), realName, accountNum);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">订单--查询该商家所有订单</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/manage/find/details/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request 请求头
     * @param pageNum 页码
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
    @PostMapping("find/details/auth")
    public Response findDetails(HttpServletRequest request, PageRequest pageNum) {

        return storeManageService.findAllDetails((Integer) request.getAttribute(ID), pageNum.getPageNum());
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">商家后台--确认订单</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/manage/sure/order/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param orderId
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

    @PostMapping("sure/order/auth")
    public Response sureOrder(HttpServletRequest request,String orderId) {
        return storeManageService.sureOrder((Integer)request.getAttribute(ID),orderId);
    }
    /**
     * <p>
     * <span>API说明：<a style="color:blue">订单详情</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/manage/detail/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param orderId
     *
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
    @PostMapping("detail/auth")
    public Response detail(HttpServletRequest request,String orderId) {

        return storeManageService.detail((Integer)request.getAttribute(ID),orderId);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">商家后台--取消订单</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/manage/cancel/order/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param orderId 订单号
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
    @PostMapping("cancel/order/auth")
    public Response cancelOrder(HttpServletRequest request,String orderId) {

        return storeManageService.cancelOrder((Integer)request.getAttribute(ID),orderId);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">刷新余额</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/manage/renovate/money/auth</ a></span>
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
    @PostMapping("renovate/money/auth")
    public Response renovateMoney(HttpServletRequest request) {

        return storeManageService.renovateMoney((Integer) request.getAttribute(ID));
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">商家提现</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/manage/withdraw/cash/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request    请求头
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

        return storeManageService.withdrawCash((Integer) request.getAttribute(ID), realName, accountNum, money);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">打开app商家后台 发送所有待处理订单</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/manage/waiting/orders/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request 请求头
     * @param pageNum 页码
     * @param cid 个推id
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
    @PostMapping("waiting/orders/auth")
    public Response waitingOrders(String cid,HttpServletRequest request, PageRequest pageNum) {
        return storeManageService.waitingOrders(cid,(Integer) request.getAttribute(ID), pageNum.getPageNum());
    }

    /**
     * 确定party订单
     * @param request
     * @param uuid
     * @return
     */
    @PostMapping("party/sure/auth")
    public Response partySure(HttpServletRequest request,String uuid ){
        return storeManageService.partySure(uuid,(Integer) request.getAttribute(ID));
    }

    /**
     * party订单详情
     * @param request
     * @param uuid
     * @return
     */
    @PostMapping("party/detailed/sure/auth")
    public Response partyDetailed(HttpServletRequest request,String uuid ){
        return storeManageService.partyDetailed(uuid,(Integer) request.getAttribute(ID));
    }

}
