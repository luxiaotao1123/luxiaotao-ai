package com.wuai.company.store.dao;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.ShippingAddressRequest;
import com.wuai.company.store.entity.*;
import com.wuai.company.store.entity.response.ComboResponse;
import com.wuai.company.store.entity.response.StoresResponse;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface StoreDao {
    /**
     *  根据场景 查找商城
     * @param scene 场景
     * @param pageNum 页码 例：1
     */
    List<StoresResponse> findStoreByScene(Double longitude, Double latitude, String scene, Integer pageNum);
    /**
     * 商城首页
     * @param longitude 经度
     * @param latitude 纬度
     * @param pageNum 页码
     */
    List<Store> home(Double longitude, Double latitude, Integer pageNum);

    Maps findMapsLoAndLaById(Integer mapsId);
    /**
     * 套餐详情
     * @param uid 商店id
     * @param pageNum 例：pageNum=0
     */
    List<Combo> storeDetails(String uid, Integer pageNum);
    /**
     * 套餐内 商品详情
     * @param uid
     */
    List<CombodityResponse> comboDetails(String uid);
    //根据uid查找套餐
    Combo findComboByUid(String uid);
    //加入 商城账单明细
    void addStoreDetails(String uid,String comboId,Integer userId, Double price,Integer type,Integer isStore);

    void merchantBanner(String uid, String banner);

    Merchant findMerchantByUid(String uid);

    List<String> findStorePicturesByUid(String uid);

    /**
     * 指定场景下 查询商家
     * @param value 场景
     * @param name 搜索的商家名
     */
    List<StoresResponse> selectStore(Double longitude,Double latitude,String value, String name);

    MerchantUser findMerchantUser(String username, String password);

    List<Maps> findData(String name);

    Merchant findMerchantByUser(String username);

    void insertMerchantUser(String uid,String username, String password);

    void insertStore(String uid, Integer mapsId, String name, String address, String phone);

    void updateMerchantUser(String uid, String uuid);

    Store findStoreByMapsId(Integer mapsId);

    void merchantUpdatePass(String name, String newPass);

    void addCombo(String uid, String storeId, String combo, Double price,String summary);

    void addCommodity(String id, String uid, String commodityName, Double commodityPri,Integer commoditySi);

    List<ComboResponse> findComboByName(String name);

    List<Commodity> findCommodityByComboId(String uid);

    void upCombo(String uid,  String combo, Double price);

    String findStoreByName(String name);

    void upCommodity(String uid, String commodityName, Double commodityPri, Integer commoditySi);

    void delCombo(String uid);

    Store findStore(String name);

    void storeBanner(String storeId, String banner);

    void addStoreOrders(String uuid, String uid, Integer userId, Double price, Integer merchantId,String storeId,Integer payType);

    List<User> findAllUser();

    List<MerchantUser> findAllManage();

    void comboPic( String pic, String comboId);

    List<ActiveContent> activeContent(Integer pageNum);

    List<StoreTaskResponse> task(Integer pageNum);

    StoreTaskDetailedResponse taskDetailed(String uuid);

    void taskPay(String payId, Integer id, Double money, String uuid,String upUuid,String topic,Integer size,String note);

    List<TaskMineResponse> taskMine(Integer pageNum, Integer userId);

    ShippingAddressResponse findAddressById(Integer userId);

    void updateShippingAddress(ShippingAddressRequest addressRequest);

    void addShippingAddress(ShippingAddressRequest addressRequest);

    Integer findTaskPayUpUserId(String upUuid);

    String findTaskPayByUserIdUpUuId(Integer id, String upUuid);

    Integer findAllMyTaskPayByUpUuid(String upUuid);

    Integer findTaskPayByUpUuid(String uuid);

    StoreOrders findStoreByUuid(String uuid);

    void backMoney(Integer userId, Double money);

    void cancelStoreOrders(Integer type, String uuid);

    CouponDetailsResponse couponDetails(String uuid);

    void upMerchantIcon(String name, String icon,String nickname,String phoneNum);
}
