package com.wuai.company.store.controller;

import com.wuai.company.entity.Response.PageRequest;
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
@RequestMapping("merchant")
public class MerchantController {

    @Autowired
    private StoreService storeService;

    /******************************************************************************/
    /******************************    商家后台    ********************************/
    /******************************************************************************/

    /**
     * 商家 登录
     *
     * @param name
     * @param password
     * @return
     */
    @PostMapping("load/{name}")
    public Response merchantLogin(@PathVariable("name") String name, String password) {
        return storeService.merchantLogin(name, password);
    }

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

    /**
     * 修改密码
     *
     * @param name
     * @param newPass
     * @return
     */
    @PostMapping("reset/load/{name}")
    public Response merchantUpdatePass(@PathVariable("name") String name, String newPass, String pass) {
        return storeService.merchantUpdatePass(name, newPass,pass);
    }

    /**
     * 上传 店铺首页banner图
     *
     * @param name   商家 用户名
     * @param banner banner图
     * @return
     */
    @PostMapping("banner/load/{name}")
    public Response merchantBanner(@PathVariable("name") String name, String banner) {
        return storeService.merchantBanner(name, banner);
    }

    /**
     * 上传套餐图
     *
     * @param name   商家 用户名
     * @param pic banner图
     * @return
     */
    @PostMapping("combo/pic/load/{name}")
    public Response comboPic(@PathVariable("name") String name, String pic,String comboId) {
        return storeService.comboPic(name, pic,comboId);
    }

    /**
     * 上传 店铺内banner图
     *
     * @param name   商家名称
     * @param banner banner图
     * @return
     */
    @PostMapping("store/banner/load/{name}")
    public Response storeBanner(@PathVariable("name") String name, String banner) {
        return storeService.storeBanner(name, banner);
    }

    /**
     * 添加商品
     *
     * @param name
     * @param storeId
     * @param combo
     * @param price
     * @param commodity
     * @param commodityPrice
     * @param commoditySize
     * @return
     */
    @PostMapping("add/combo/load/{name}")
    public Response addCombo(@PathVariable("name") String name, String storeId, String combo, String price, String commodity, String commodityPrice, String commoditySize,String summary) {
        return storeService.addCombo(name, storeId, combo, price, commodity, commodityPrice, commoditySize,summary);
    }

    /**
     * 修改套餐
     *
     * @param name           用户名
     * @param combo          套餐名
     * @param uid            套餐id
     * @param price          套餐价格
     * @param commodity      商品名
     * @param commodityPrice 商品价格
     * @param commoditySize  商品数量
     * @return
     */
    @PostMapping("up/combo/load/{name}")
    public Response upCombo(@PathVariable("name") String name, String uid, String combo, String price, String commodity, String commodityPrice, String commoditySize,String summary) {
//
        return storeService.upCombo(name, uid, combo, price, commodity, commodityPrice, commoditySize,summary);
    }

    /**
     * 删除套餐
     *
     * @param name 用户名
     * @param uid  套餐id
     * @return
     */
    @PostMapping("del/combo/load/{name}")
    public Response delCombo(@PathVariable("name") String name, String uid) {
//
        return storeService.delCombo(name, uid);
    }

    /**
     * <p>
     * <span>API说明：商家 修改密码<a style="color:blue"></ a></span>
     * <br/>
     * <span>请求方式：POST<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/store/up/pass/load</ a></span>
     * <br/>
     * </p >
     *
     * @param name
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
    @PostMapping("up/pass/load/{name}")
    public Response upPass(@PathVariable("name") String name, String pass) {
        return storeService.upPass(name, pass);
    }

    /**
     * 根据商家 用户名查找 商品
     *
     * @param name
     * @return
     */
    @PostMapping("show/combo/load/{name}")
    public Response showCombo(@PathVariable("name") String name) {
        return storeService.findStoreByName(name);
    }


    /**
     * <p>
     * <span>API说明：展示banner图以及详情图<a style="color:blue"></ a></span>
     * <br/>
     * <span>请求方式：POST<a style="color:blue"></ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/merchant/show/pictures/load/{name}</ a></span>
     * <br/>
     * </p >
     *
     * @param name
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
    @PostMapping("show/pictures/load/{name}")
    public Response showPictures(@PathVariable("name") String name) {

        return storeService.showPictures(name);
    }

    @PostMapping("del/banner/load/{name}")
    public Response delBanner(@PathVariable("name")String name,String pic){

        return storeService.delBanner(name,pic);
    }
    @PostMapping("del/pic/load/{name}")
    public Response delPic(@PathVariable("name")String name,String pic){
        return storeService.delPic(name,pic);
    }

    /**
     * 添加修改商家昵称 icon
     * @param name
     * @param icon
     * @param nickname
     * @return
     */
    @RequestMapping("up/icon/load/{name}")
    public Response upMerchantIcon(@PathVariable("name")String name,String icon,String nickname,String phoneNum){
        return storeService.upMerchantIcon(name,icon,nickname,phoneNum);
    }
    /**************************************************/
    /*********************管理员************************/
    /**************************************************/

    @PostMapping("propelling/user/load/{name}")
    public Response propellingUser(@PathVariable("name")String name,String topic,String content){
        return storeService.propellingUser(name,topic,content);
    }

    @PostMapping("propelling/manage/load/{name}")
    public Response propellingManage(@PathVariable("name")String name,String topic,String content){
        return storeService.propellingManage(name,topic,content);
    }
    @PostMapping("propelling/all/load/{name}")
    public Response propellingAll(@PathVariable("name")String name,String topic,String content){
        return storeService.propellingAll(name,topic,content);
    }
    @PostMapping("information/load/{name}")
    public Response information(@PathVariable("name")String name){
        return storeService.information(name);
    }
    @GetMapping("/load/{name}")
    public void test(@PathVariable("name") String name) {
        System.out.println("ok.............................");
        System.out.println("ok.............................");
        System.out.println("ok.............................");
        System.out.println("ok.............................");

    }

}
