package com.wuai.company.store.service;

import com.wuai.company.entity.StoreOrders;
import com.wuai.company.entity.request.ShippingAddressRequest;
import com.wuai.company.util.Response;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface StoreService {

    Response findStoreByScene(Double longitude,Double latitude,Integer scene,Integer pageNum);
    /**
     * 商城首页
     * @param longitude 经度
     * @param latitude 纬度
     * @param pageNum 页码
     */
    Response home(Double longitude, Double latitude,Integer pageNum);

    /**
     * 套餐详情
     * @param uid 商店id
     * @param pageNum 例：pageNum=0
     */
    Response storeDetails(String uid, Integer pageNum);
    /**
     * 套餐内 商品详情
     * @param uid
     */
    Response comboDetails(String uid);
    /**
     * 用户 购买套餐
     * @param uid 套餐id
     * @param userId    用户id
     */
    Response pay(String uid, Integer userId);

    /**
     * 上传 店铺banner图
     * @param name 商家用户名
     * @param banner banner图
     */
    Response merchantBanner(String name, String banner);

    Response storeBanner(String name, String banner);

    /**
     * 指定场景下 查询商家
     * @param scene 场景
     * @param name 搜索的商家名
     */
    Response selectStore(Double longitude,Double latitude,Integer scene, String name);

    Response merchantLogin(String username, String password);

    Response findData(String name);

    Response generateUsername();

    Response merchantRegister(String uid,Integer mapsId, String name, String address, String phone);

    Response merchantUpdatePass(String name, String newPass, String pass);

    Response addCombo(String name, String storeId, String combo, String price, String commodity, String commodityPrice,String commoditySize,String summary);

    Response findStoreByName(String name);

    Response upCombo(String name,String uid, String combo, String price, String commodity, String commodityPrice, String commoditySize,String summary);

    Response delCombo(String name, String uid);

    Response upPass(String name, String pass);

    Response showPictures(String name);

    Response delPic(String name, String pic);

    Response delBanner(String name, String pic);

    Response propellingUser(String name, String topic, String content);

    Response propellingManage(String name, String topic, String content);

    Response propellingAll(String name, String topic, String content);

    Response comboPic(String name, String pic, String comboId);
    Response active(Integer pageNum);

    Response information(String name);

    Response task(Integer pageNum);

    Response taskDetailed(String uuid);

    Response taskPay(String uuid, Integer id,String upUuid,String note);

    Response taskMine(Integer pageNum, Integer userId);

    Response shippingAddress(Integer attribute);

    Response updateShippingAddress(ShippingAddressRequest addressRequest, Integer attribute) throws IllegalAccessException;

    Response cancelStoreOrders(Integer attribute, String uuid);

    Response couponDetails(Integer attribute, String uuid);

    Response upMerchantIcon(String name, String icon,String nickname,String phoneNum);
}
