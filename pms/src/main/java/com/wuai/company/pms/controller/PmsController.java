package com.wuai.company.pms.controller;

import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.entity.Response.Scene;
import com.wuai.company.entity.request.CouponsRequest;
import com.wuai.company.entity.request.ScenesRequest;
import com.wuai.company.entity.request.StoreActiveRequest;
import com.wuai.company.entity.request.SysRequest;
import com.wuai.company.pms.entity.request.TrystSceneRequest;
import com.wuai.company.pms.entity.request.VideoHomeRequest;
import com.wuai.company.pms.service.PmsService;
import com.wuai.company.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by 97947 on 2017/9/22.
 * 运维管理系统
 */
@RestController
@RequestMapping("pms")
public class PmsController {

    @Autowired
    private PmsService pmsService;


    /**
     * <p>
     * <span>API说明：<a style="color:blue">显示所有活动列表</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/show/active</ a></span>
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
    @RequestMapping("show/active")
    public Response showActive(PageRequest pageNum) {
        return pmsService.showActive(pageNum.getPageNum());
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">添加活动</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/add/active/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param pic     图片地址
     * @param topic   表土
     * @param content 内容：活动链接
     * @param time    活动时间
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
    @RequestMapping("add/active/load/{name}")
    public Response addActive(@PathVariable("name") String name, String pic, String topic, String content, String time) {
        return pmsService.addActive(name, pic, topic, content, time);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">修改活动</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pmsupdate/active/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param uuid    activeid  活动id
     * @param pic     图片
     * @param topic   标题
     * @param content 活动链接url
     * @param time    活动时间
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
    @RequestMapping("update/active/load/{name}")
    public Response updateActive(@PathVariable("name") String name, String uuid, String pic, String topic, String content, String time) {
        return pmsService.updateActive(name, uuid, pic, topic, content, time);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">删除活动</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/deleted/active/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param uuid 活动id
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
    @RequestMapping("deleted/active/load/{name}")
    public Response deletedActive(@PathVariable("name") String name, String uuid) {
        return pmsService.deletedActive(name, uuid);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">用户管理--查询 显示 用户</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/show/user</ a></span>
     * <br/>
     * </p >
     *
     * @param phone     用户手机号 可为空
     * @param startTime 开始日期 可为空
     * @param endTime   结束日期 可为空
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
    @RequestMapping("show/user")
    public Response showUser(String phone, String startTime, String endTime, PageRequest pageNum) {

        return pmsService.showUser(phone, startTime, endTime, pageNum.getPageNum());
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">财务管理 -- 约会/商城</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/details</ a></span>
     * <br/>
     * </p >
     *
     * @param name      搜索：手机号/用户名/
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageNum   页码
     * @param type      类型： 约会：1  or  商城：2
     * @param code
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
    @RequestMapping("details")
    public Response detail(String name, String startTime, String endTime, PageRequest pageNum, Integer type, Integer code) {

        return pmsService.details(name, startTime, endTime, pageNum.getPageNum(), type, code);
//        return null;
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">订单管理--约会订单</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/orders/manage</ a></span>
     * <br/>
     * </p >
     * <p>
     * 请求头
     *
     * @param pageNum   页码
     * @param name      搜索 内容
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param type      订单状态
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
    @RequestMapping("orders/manage")
    public Response ordersManage(PageRequest pageNum, String name, String startTime, String endTime, Integer type) {

        return pmsService.ordersManage(pageNum.getPageNum(), name, startTime, endTime, type);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">管理员管理</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/show/admin</ a></span>
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
    @RequestMapping("show/admin")
    public Response showAdmin(PageRequest pageNum) {

        return pmsService.showAdmin(pageNum.getPageNum());
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">删除管理员</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/deleted/admin/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param uuid 管理员uuid
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

    @RequestMapping("deleted/admin/load/{name}")
    public Response deletedAdmin(@PathVariable("name") String name, String uuid) {

        return pmsService.deletedAdmin(name, uuid);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">展示所有LABEL标签</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/show/label</ a></span>
     * <br/>
     * </p >
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
    @RequestMapping("show/label")
    public Response showLabel() {

        return pmsService.showLabel();
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">修改 删除 添加标签</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/update/label/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param label
     * @param key
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
    @RequestMapping("update/label/load/{name}")
    public Response updateLabel(@PathVariable("name") String name, String label, String key) {
        return pmsService.updateLabel(name, label, key);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">展示场景系统参数</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/scene</ a></span>
     * <br/>
     * </p >
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
    @RequestMapping("scene")
    public Response showScene() {
        return pmsService.showScene();
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">对于场景系统参数进行 增删改</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/update/scene/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param scene
     * @param type  操作类型
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
    @RequestMapping("update/scene/load/{name}")
    public Response updateScene(@PathVariable("name") String name, Scene scene, Integer type) {

        return pmsService.updateScene(name, scene, type);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">增加场景</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/insert/scene/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param scene
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
    @RequestMapping("insert/scene/load/{name}")
    public Response insertScene(@PathVariable("name") String name, ScenesRequest scene) {

        return pmsService.insertScene(name, scene);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">删除场景</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/deleted/scene/load/{name} </ a></span>
     * <br/>
     * </p >
     *
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
    @RequestMapping("deleted/scene/load/{name}")
    public Response deletedScene(@PathVariable("name") String name, String uuid) {

        return pmsService.deletedScene(name, uuid);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">展示所有聊天语</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/show/talk</ a></span>
     * <br/>
     * </p >
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
    @RequestMapping("show/talk")
    public Response showTalk() {

        return pmsService.showTalk();
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">显示系统参数</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/show/system</ a></span>
     * <br/>
     * </p >
     *
     * @param
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
    @RequestMapping("show/system")
    public Response showSys() {
        return pmsService.showSys();
    }

    /**
     * 修改 系统参数
     *
     * @param sysRequest
     * @return
     */
    @RequestMapping("update/system")
    public Response updateSystem(SysRequest sysRequest) {
        return pmsService.updateSystem(sysRequest);
    }

    /**********************************************--->version-2.0<---*****************************************************/
    /**
     * <p>
     * <span>API说明：<a style="color:blue">停止返现</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/stop/back/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param name
     * @param userId
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
    @RequestMapping("stop/back/load/{name}")
    public Response stopBackMoney(@PathVariable("name") String name, Integer userId) {

        return pmsService.stopBackMoney(name, userId);
    }


    /**
     * <p>
     * <span>API说明：<a style="color:blue">开启返现</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">POST</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/start/back/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param name
     * @param userId
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
    @RequestMapping("start/back/load/{name}")
    public Response startBackMoney(@PathVariable("name") String name, Integer userId) {

        return pmsService.startBackMoney(name, userId);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">获取商城所有活动</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/task/all</ a></span>
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
    @RequestMapping("task/all")
    public Response taskAll(PageRequest pageNum) {

        return pmsService.taskAll(pageNum.getPageNum());
    }

    /**
     * 添加修改商城活动
     *
     * @param name
     * @param request
     * @return
     * @throws IllegalAccessException
     */
    @RequestMapping("update/task/load/{name}")
    public Response updateTask(@PathVariable("name") String name, StoreActiveRequest request) throws IllegalAccessException {
        return pmsService.updateTask(name, request);
    }

    /**
     * 删除商城活动
     *
     * @param name
     * @param uuid
     * @return
     */
    @RequestMapping("deleted/task/load/{name}")
    public Response deletedTask(@PathVariable("name") String name, String uuid) {
        return pmsService.deletedTask(name, uuid);
    }

    @RequestMapping("input/user/excel")
    public Response inputUserExcel() throws IOException {
        String name = "aaa";
        String url = "G:\\Woo\\用户_数据导入模板(2)500后.xlsx";
        return pmsService.inputUserExcel(name, url);
    }

    @RequestMapping("input/orders/excel")
    public Response inputOrdersExcel() throws IOException {
        String name = "aaa";
        String url = "G:\\Woo\\订单_数据导入模板.xlsx";
        return pmsService.inputOrdersExcel(name, url);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">活动订单</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/active/details/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param name
     * @param type
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
    @RequestMapping("active/details/load/{name}")
    public Response activeDetails(@PathVariable("name") String name, Integer type, PageRequest pageNum,String value,String startTime,String endTime) {
        return pmsService.activeDetails(name, type, pageNum.getPageNum(),value,startTime,endTime);
    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">提交快递单号，快递名称</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pms/active/up/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param name
     * @param expressNum
     * @param expressName
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
    @RequestMapping("active/up/load/{name}")
    public Response upActiveDetails(@PathVariable("name") String name, String expressNum, String expressName, String uuid,Integer send) {

        return pmsService.upActiveDetails(name, expressNum, expressName, uuid,send);
    }


    /**
     * 一键生成 商家 party all用户
     * @param name
     * @param type
     * @return
     */
    @RequestMapping("generate/user/load/{name}")
    public Response generateUser(@PathVariable String name, Integer type){
        return pmsService.generateUser(name,type);
    }

    /**
     * 查询所有商家用户
     * @param name
     * @param pageNum
     * @return
     */
    @RequestMapping("find/manage/load/{name}")
    public Response findAllManage(@PathVariable String name, Integer pageNum){
        return pmsService.findAllManage(name,pageNum);
    }

    /**
     * 查询审核party
     * @param name
     * @param pageNum
     * @return
     */
    @RequestMapping("show/parties/load/{name}")
    public Response showAllParties(@PathVariable String name, PageRequest pageNum,String value){
        return pmsService.showAllParties(name,pageNum.getPageNum(),value);
    }
    /**
     * 审核party
     * @param name
     * @param value
     * @return
     */
    @RequestMapping("party/confirm/load/{name}")
    public Response partyConfirm(@PathVariable String name,String value,String uuid,String note){
        return pmsService.partyConfirm(name,value,uuid,note);
    }

    /**
     * 审核 视频
     * @param name
     * @param videoCheck
     * @return
     */
    @RequestMapping("check/video/load/{name}")
    public Response checkVideo(@PathVariable String name,String uuid,Integer videoCheck,String note){
        return pmsService.checkVideo(name,uuid,videoCheck,note);
    }

    /**
     * 显示所有审核视频
     * @param name
     * @param pageNum
     * @param videoCheck
     * @return
     */
    @RequestMapping("show/video/load/{name}")
    public Response showVideos(@PathVariable String name,Integer pageNum,Integer videoCheck){
       return pmsService.showVideos(name,pageNum,videoCheck);
    }

    /**
     * 展示所有提现申请
     * @param name
     * @param pageNum
     * @param cash
     * @return
     */
    @RequestMapping("show/cash/load/{name}")
    public Response showCash(@PathVariable String name,Integer pageNum,Integer cash){
        return pmsService.showCash(name,pageNum,cash);
    }
    /**
     * 操作提现申请
     * @param name
     * @param uuid
     * @param cash
     * @return
     */
    @RequestMapping("up/cash/load/{name}")
    public Response upCash(@PathVariable String name,String uuid,String note,Integer cash){
        return pmsService.upCash(name,uuid,note,cash);
    }

    /**
     * 展示所有优惠券
     * @param name
     * @param pageNum
     * @return
     */
    @PostMapping("coupons/show/load/{name}")
    public Response couponsShow(@PathVariable String name ,Integer pageNum){
        return pmsService.couponsShow(name,pageNum);
    }
    /**
     * 添加优惠券
     * @param name
     * @return
     */
    @PostMapping("coupons/add/load/{name}")
    public Response couponsAdd(@PathVariable String name , CouponsRequest couponsRequest) throws Exception {
        return  pmsService.couponsAdd(name,couponsRequest);
    }
    /**
     * 修改优惠券
     * @param name
     * @return
     */
    @PostMapping("coupons/up/load/{name}")
    public Response couponsUp(@PathVariable String name , CouponsRequest couponsRequest) throws Exception {
        return  pmsService.couponsUp(name,couponsRequest);
    }
    /**
     * 删除优惠券
     * @param name
     * @return
     */
    @PostMapping("coupons/del/load/{name}")
    public Response couponsDel(@PathVariable String name , String uuid) throws Exception {
        return  pmsService.couponsDel(name,uuid);
    }

    /**
     * 钱包充值
     * @param name
     * @return
     */
    @PostMapping("recharge/wallet/load/{name}")
    public Response rechargeWallet(@PathVariable String name , Integer userId,Double money) throws Exception {
        return  pmsService.rechargeWallet(name,userId,money);
    }
    /**********************************************--->version-2.0<---*****************************************************/
    /**********************************************--->tryst-1.0<---*****************************************************/
    /**
     * 约吧场景--添加
     * @param name
     * @return
     */
    @PostMapping("tryst/scene/add/load/{name}")
    public Response trystSceneAdd(@PathVariable String name , TrystSceneRequest request) throws Exception {
        return  pmsService.trystSceneAdd(name,request);
    }

    /**
     * 约吧场景--展示
     * @param name
     * @return
     */
    @PostMapping("tryst/scene/show/load/{name}")
    public Response trystSceneShow(@PathVariable String name,
                                   @RequestParam(defaultValue = "1") Integer pageNum) throws Exception {
        return  pmsService.trystSceneShow(name,pageNum);
    }
    /**
     * 约吧场景--删除
     * @param name
     * @return
     */
    @PostMapping("tryst/scene/del/load/{name}")
    public Response trystSceneDel(@PathVariable String name , String uuid) throws Exception {
        return  pmsService.trystSceneDel(name,uuid);
    }

    /**
     * 首页--展示视频
     * @param name
     * @return
     */
    @PostMapping("tryst/video/show/load/{name}")
    public Response trystVideoShow(@PathVariable String name,Integer pageNum) throws Exception {
        return  pmsService.trystVideoShow(name, pageNum);
    }
    /**
     * 首页--展示视频添加
     * @param name
     * @return
     */
    @PostMapping("tryst/video/add/load/{name}")
    public Response trystVideoAdd(@PathVariable String name ,
                                  @RequestParam(required = false)String video,
                                  @RequestParam(required = false)String videoPic,
                                  @RequestParam(required = false)String typeValue,
                                  @RequestParam(required = false)Integer typeCode){
        return pmsService.trystVideoAdd(name,video,videoPic,typeValue,typeCode);
    }
    /**
     * 首页--展示视频删除
     * @param name
     * @return
     */
    @PostMapping("tryst/video/del/load/{name}")
    public Response trystVideoDel(@PathVariable String name , String uuid) throws Exception {
        return  pmsService.trystVideoDel(name,uuid);
    }
    /**********************************************--->tryst-1.0<---*****************************************************/
   
    /**
     * 首页--展示视频添加
     * @param name
     * @return
     */
    @PostMapping("tryst/video/insert/{name}")
    public Response inserttrystVideoHome(@PathVariable String name ,String video , String videoPic) throws Exception {
        return  pmsService.insertVideoHomeSelective(name,video, videoPic);
    }
    
    /**
     * 查询某种状态下的身份认证信息
     * @param state
     * @param useId
     * @param pageNum
     * @return
     */
    @PostMapping("tryst/identity/select/{name}")
    public Response findIdentityProof(@PathVariable String name ,Integer state,Integer useId,Integer pageNum){
		return pmsService.findIdentityProof(name,state, useId, pageNum);
    	
    }
    
    /**
     * 审核身份认证信息
     * @param uuid
     * @param state
     * @param idNumber
     * @param realName
     * @param satrtDate
     * @param endDate
     * @return
     */
    @PostMapping("tryst/identity/audit/{name}")
    public Response auditIdentityProof(@PathVariable String name ,String uuid, Integer state,
			String idNumber, String realName, String satrtDate, String endDate){
				return pmsService.auditIdentityProof(name,uuid, state, idNumber, realName, satrtDate, endDate);
    	
    }

    public static void main(String[] args) {
        char c = (char) (Math.random() * 26 + 'a');
        System.out.println(c);
    }
}
