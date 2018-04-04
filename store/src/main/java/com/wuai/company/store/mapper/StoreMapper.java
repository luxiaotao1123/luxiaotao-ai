package com.wuai.company.store.mapper;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.ShippingAddressRequest;
import com.wuai.company.store.entity.*;
import com.wuai.company.store.entity.response.ComboResponse;
import com.wuai.company.store.entity.response.StoresResponse;
import com.wuai.company.store.entity.response.UserEquipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
@Mapper
public interface StoreMapper {

    /**
     * 根据场景 查找商城
     * @param scene 场景 例
     * @param pageNum 页码 例：1
     */
    List<StoresResponse> findStoreByScene(@Param("longitude")Double longitude, @Param("latitude")Double latitude, @Param("scene") String scene, @Param("pageNum")Integer pageNum);
    /**
     * 商城首页
     * @param longitude 经度
     * @param latitude 纬度
     * @param pageNum 页码
     */
    List<Store> home(@Param("longitude")Double longitude, @Param("latitude")Double latitude, @Param("pageNum")Integer pageNum);

    Maps findMapsLoAndLaById(@Param("mapsId")Integer mapsId);
    //加入 商城账单明细
    void addStoreDetails(@Param("uid")String uid,@Param("comboId")String comboId,@Param("userId")Integer userId, @Param("price")Double price, @Param("type")Integer type, @Param("isStore")Integer isStore);

    void merchantBanner(@Param("uid")String uid, @Param("banner")String banner);

    Merchant findMerchantByUid(@Param("uid")String uid);

    List<String> findStorePicturesByUid(@Param("uid")String uid);

    List<StoresResponse> selectStore(@Param("longitude")Double longitude ,@Param("latitude")Double latitude,@Param("value")String value, @Param("name")String name);

    MerchantUser findMerchantUser(@Param("username")String username, @Param("password")String password);

    List<Maps> findData(@Param("name") String name);

    Merchant findMerchantByUser(@Param("username") String username);

    void insertMerchantUser(@Param("uid")String uid,@Param("username")String username, @Param("password")String password);

    void insertStore(@Param("uid")String uid, @Param("mapsId")Integer mapsId, @Param("name")String name, @Param("address")String address, @Param("phone")String phone);

    void updateMerchantUser(@Param("uid")String uid,@Param("uuid") String uuid);

    Store findStoreByMapsId(@Param("mapsId")Integer mapsId);

    void merchantUpdatePass(@Param("name")String name, @Param("newPass")String newPass);

    void addCombo(@Param("uid")String uid, @Param("storeId")String storeId, @Param("combo")String combo, @Param("price")Double price,@Param("summary")String summary);

    void addCommodity(@Param("id")String id, @Param("uid")String uid, @Param("commodityName")String commodityName, @Param("commodityPri")Double commodityPri,@Param("commoditySi")Integer commoditySi);

    List<ComboResponse> findComboByName(@Param("name")String name);
    List<Commodity> findCommodityByName(@Param("uid")String uid);

    void upCombo(@Param("uid")String uid,@Param("combo") String combo,@Param("price") Double price);

    String findStoreByName(@Param("name")String name);

    void upCommodity(@Param("uid")String uid, @Param("commodityName")String commodityName, @Param("commodityPri")Double commodityPri, @Param("commoditySi")Integer commoditySi);

    void delCombo(@Param("uid")String uid);

    void delCommodity(@Param("uid")String uid);

    Store findStore(@Param("name")String name);

    void storeBanner(@Param("storeId")String storeId, @Param("pictures")String pictures);

    void addStoreOrders(@Param("uuid")String uuid, @Param("uid")String uid,
                        @Param("userId")Integer userId, @Param("price")Double price,
                        @Param("merchantId")Integer merchantId,@Param("storeId")String storeId,@Param("payType") Integer payType);

    List<User> findAllUser();

    List<MerchantUser> findAllManage();

    void comboPic(@Param("pic")String pic, @Param("comboId")String comboId);

    List<ActiveContent> activeContent(@Param("pageNum") Integer pageNum);

    List<StoreTaskResponse> task(@Param("pageNum")Integer pageNum);

    StoreTaskDetailedResponse taskDetailed(@Param("uuid")String uuid);

    void taskPay(@Param("payId")String payId, @Param("id")Integer id, @Param("money")Double money, @Param("uuid")String uuid,
                 @Param("upUuid")String upUuid,@Param("topic")String topic,@Param("size")Integer size,@Param("note")String note);

    List<TaskMineResponse> taskMine(@Param("pageNum")Integer pageNum, @Param("userId")Integer userId);

    ShippingAddressResponse findAddressById(@Param("userId")Integer userId);

    void updateShippingAddress(@Param("request")ShippingAddressRequest request);
    void addShippingAddress(@Param("request")ShippingAddressRequest request);
    Integer findTaskPayUpUserId(@Param("upUuid")String upUuid);

    String findTaskPayByUserIdUpUuId(@Param("id")Integer id,@Param("upUuid") String upUuid);

    Integer findAllMyTaskPayByUpUuid(@Param("upUuid") String upUuid);

    Integer findTaskPayByUpUuid(@Param("uuid") String uuid);

    List<CombodityResponse> comboDetails(@Param("uid")String uid);

    StoreOrders findStoreByUuid(@Param("uuid") String uuid);

    void backMoney(@Param("userId")Integer userId, @Param("money")Double money);

    void cancelStoreOrders(@Param("type")Integer type, @Param("uuid")String uuid);

    CouponDetailsResponse couponDetails(@Param("uuid")String uuid);

    void upMerchantIcon(@Param("name")String name,@Param("icon") String icon,@Param("nickname")String nickname,@Param("phoneNum")String phoneNum);
}
