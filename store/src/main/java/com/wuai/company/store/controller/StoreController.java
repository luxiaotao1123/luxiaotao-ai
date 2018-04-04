package com.wuai.company.store.controller;

import com.wuai.company.config.FilterConfig;
import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.entity.request.ShippingAddressRequest;
import com.wuai.company.store.entity.request.ComboListRequest;
import com.wuai.company.store.service.StoreService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wuai.company.util.JwtToken.ID;

/**
 * Created by Administrator on 2017/6/14.
 */
@RestController
@RequestMapping("store")
public class StoreController {

    @Autowired
    private StoreService storeService;


    /**
     * <p>
     * <span>API说明：<a style="color:blue">根据场景 查找商城</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/scene</ a></span>
     * <br/>
     * </p >
     *
     * @param longitude   经度
     * @param latitude    纬度
     * @param scene       场景 例：0
     * @param pageRequest 例： pageNum 页码 1
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
    @PostMapping("scene")
    public Response findStoreByScene(Double longitude, Double latitude, Integer scene, PageRequest pageRequest) {

        return storeService.findStoreByScene(longitude, latitude, scene, pageRequest.getPageNum());
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">商城首页</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/home</ a></span>
     * <br/>
     * </p >
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param pageNum   页码
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
    @PostMapping("home")
    public Response home(Double longitude, Double latitude, @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum) {

        return storeService.home(longitude, latitude, pageNum);
    }


    @GetMapping("active")
    public Response active(PageRequest pageNum) {

        return storeService.active(pageNum.getPageNum());
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">商店内 套餐详情</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/store/details</ a></span>
     * <br/>
     * </p >
     *
     * @param uid         商店id
     * @param pageRequest 页码 参数pageNum
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
    @PostMapping("store/details")
    public Response storeDetails(String uid, PageRequest pageRequest) {

        return storeService.storeDetails(uid, pageRequest.getPageNum());
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue"> 套餐内 商品详情</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/combo/details</ a></span>
     * <br/>
     * </p >
     *
     * @param uid 套餐id
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
    @PostMapping("combo/details")
    public Response comboDetails(String uid) {

        return storeService.comboDetails(uid);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">用户 购买套餐 提交订单</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/pay/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param ordersId 套餐id
     * @param request  请求头
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
    @PostMapping("pay/auth")
    public Response pay(String ordersId, HttpServletRequest request) {

        return storeService.pay(ordersId, (Integer) request.getAttribute(ID));
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">指定场景下 查询商家</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/find/store</ a></span>
     * <br/>
     * </p >
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @param scene     场景
     * @param name      搜索的商家名
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
    @PostMapping("find/store")
    public Response selectStore(Double longitude, Double latitude, Integer scene, String name) {

        return storeService.selectStore(longitude, latitude, scene, name);
    }

/***********************************************************************************************/
    /**
     * 根据酒吧名 模糊搜索
     *
     * @param name
     * @return
     */
    @PostMapping("find/data")
    public Response findData(String name) {
        return storeService.findData(name);
    }

    /**
     * 生成随机 用户名 账户
     *
     * @return
     */
    @GetMapping("generate/username")
    public Response generateUsername() {
        return storeService.generateUsername();
    }

    /**
     * 绑定 商店
     *
     * @param name
     * @return
     */
    @PostMapping("register")
    public Response merchantRegister(String uid, Integer mapsId, String name, String address, String phone) {
        return storeService.merchantRegister(uid, mapsId, name, address, phone);
    }

    /***************************************************VERSION-2.0*******************************************************/
    /**
     * <p>
     * <span>API说明：<a style="color:blue">获取任务获得列表</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/task</ a></span>
     * <br/>
     * </p >
     *
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
    @RequestMapping("task")
    public Response task(PageRequest pageNum) {
        return storeService.task(pageNum.getPageNum());
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">任务详情</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/task/detailed</ a></span>
     * <br/>
     * </p >
     *
     * @param uuid 任务uuid
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
    @RequestMapping("task/detailed")
    public Response taskDetailed(String uuid) {
        return storeService.taskDetailed(uuid);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">支付宝支付 任务支付</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/task/pay/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param uuid    订单号
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
    @PostMapping("/task/pay/auth")
    public Response taskPay(String uuid, HttpServletRequest request, String upUuid, String note) {
        return storeService.taskPay(uuid, (Integer) request.getAttribute(ID), upUuid, note);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">我的任务</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/task/mine/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param pageNum
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
    @PostMapping("/task/mine/auth")
    public Response taskMine(PageRequest pageNum, HttpServletRequest request) {

        return storeService.taskMine(pageNum.getPageNum(), (Integer) request.getAttribute(ID));
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">获取收货地址</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/shipping/address/auth</ a></span>
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
    @PostMapping("shipping/address/auth")
    public Response shippingAddress(HttpServletRequest request) {

        return storeService.shippingAddress((Integer) request.getAttribute(ID));
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">修改收货地址</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/update/address/auth</ a></span>
     * <br/>
     * </p >
     *
     * @param addressRequest
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
    @PostMapping("update/address/auth")
    public Response updateShippingAddress(ShippingAddressRequest addressRequest, HttpServletRequest request) throws IllegalAccessException {

        return storeService.updateShippingAddress(addressRequest, (Integer) request.getAttribute(ID));
    }

    /*
           取消商城订单
             */
    @PostMapping("cancel/orders/auth")
    public Response cancelStoreOrders(HttpServletRequest request, String uuid) {

        return storeService.cancelStoreOrders((Integer) request.getAttribute(ID), uuid);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">优惠券详情</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/coupon/details/auth</ a></span>
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
    @PostMapping("coupon/details/auth")
    public Response couponDetails(HttpServletRequest request, String uuid) {

        return storeService.couponDetails((Integer) request.getAttribute(ID), uuid);
    }
    /***************************************************VERSION-2.0*******************************************************/
}
