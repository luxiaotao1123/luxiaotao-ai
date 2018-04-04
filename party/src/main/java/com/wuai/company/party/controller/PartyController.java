package com.wuai.company.party.controller;


import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.enums.PayTypeEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.party.service.PartyService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.wuai.company.util.JwtToken.ID;

/**
 * Created by Administrator on 2017/6/14.
 */
@RestController
@RequestMapping("party")
public class PartyController {

    @Autowired
    private PartyService partyService;


    /**
     * <p>
     * <span>API说明：<a style="color:blue">展示所有party</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/party/find/all/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param pageNum
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
    @PostMapping("find/all/auth")
    public Response findAll(HttpServletRequest request, PageRequest pageNum, String classify) {

        return partyService.findAll((Integer) request.getAttribute(ID), pageNum.getPageNum(), classify);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">party详情</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/party/detailed/information/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param request
     * @param uuid
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
    @PostMapping("detailed/information/auth")
    public Response detailedInformation(HttpServletRequest request, String uuid) {

        return partyService.detailedInformation((Integer) request.getAttribute(ID), uuid);
    }

    /**
     * party分享页面
     * @param uuid
     * @return
     */
    @PostMapping("detailed/web")
    public Response detailedInformationWeb(String uuid) {

        return partyService.detailedInformationWeb( uuid);
    }

    /**
     * 生成订单支付
     * @param request
     * @param date
     * @param uuid
     * @param money
     * @return
     */
    @PostMapping("pay/auth")
    public Response partyPay(HttpServletRequest request,String date,String uuid,Double money,Integer boySize,Integer girlSize){
        return partyService.partyPay((Integer) request.getAttribute(ID), uuid,date,money,boySize,girlSize);
    }

    /**
     * party拼团 是否参加
     * @param request
     * @param uuid
     * @return
     */
    @PostMapping("group/buying/auth")
    public Response groupBuying(HttpServletRequest request,String uuid){
        return partyService.groupBuying((Integer)request.getAttribute(ID),uuid);
    }

    /**
     * 取消party拼团
     * @param request
     * @param uuid
     * @return
     */
    @PostMapping("cancel/auth")
    public Response cancelParty(HttpServletRequest request,String uuid){
        return partyService.cancelParty((Integer)request.getAttribute(ID),uuid);
    }

    /**
     * 添加/取消--感兴趣--我的收藏
     * @param request
     * @param uuid
     * @return
     */
    @PostMapping("collection/auth")
    public Response collection(HttpServletRequest request,String uuid,Integer collect){
        return partyService.collection((Integer)request.getAttribute(ID),uuid,collect);
    }


    /**
     * 添加留言
     * @param request
     * @param uuid
     * @param message
     * @return
     */
    @PostMapping("add/message/auth")
    public Response addMessage(HttpServletRequest request,String uuid,String message){
        return partyService.addMessage((Integer)request.getAttribute(ID),uuid,message);
    }

    /**
     * 留言列表
     * @param request
     * @param uuid
     * @return
     */
    @PostMapping("message/all/auth")
    public Response messageAll(HttpServletRequest request,String uuid,PageRequest pageNum){
        return partyService.messageAll((Integer)request.getAttribute(ID),uuid,pageNum.getPageNum());
    }
    /**
     * 我感兴趣的列表--收藏列表
     * @param request

     * @return
     */
    @PostMapping("my/collections/auth")
    public Response myCollections(HttpServletRequest request,PageRequest pageNum){
        return partyService.myCollections((Integer)request.getAttribute(ID),pageNum.getPageNum());
    }
    /**
     * 获取所有分类类型
     * @return
     */
    @PostMapping("classify/auth")
    public Response classify(){
        return partyService.classify();
    }
}
