package com.wuai.company.store.dao.impl;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.ShippingAddressRequest;
import com.wuai.company.store.dao.StoreDao;
import com.wuai.company.store.entity.*;
import com.wuai.company.store.entity.response.ComboResponse;
import com.wuai.company.store.entity.response.StoresResponse;
import com.wuai.company.store.mapper.ComboMapper;
import com.wuai.company.store.mapper.CommodityMapper;
import com.wuai.company.store.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/6/14.
 */
@Repository

public class StoreDaoImpl implements StoreDao {
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private ComboMapper comboMapper;
    @Autowired
    private CommodityMapper commodityMapper;

    /**
     *  根据场景 查找商城
     * @param scene 场景
     * @param pageNum 页码 例：1
     */
    @Override
    public List<StoresResponse> findStoreByScene(Double longitude, Double latitude, String scene, Integer pageNum) {

        return storeMapper.findStoreByScene(longitude,latitude,scene,pageNum);
    }
    /**
     * 商城首页
     * @param longitude 经度
     * @param latitude 纬度
     * @param pageNum 页码
     */
    @Override
    public List<Store> home(Double longitude, Double latitude, Integer pageNum) {
        return storeMapper.home(longitude,latitude,pageNum);
    }

    @Override
    public Maps findMapsLoAndLaById(Integer mapsId) {
        return storeMapper.findMapsLoAndLaById(mapsId);
    }
    /**
     * 套餐详情
     * @param uid 商店id
     * @param pageNum 例：pageNum=0
     */
    @Override
    public List<Combo> storeDetails(String uid, Integer pageNum) {
        return comboMapper.storeDetails(uid,pageNum);
    }
    /**
     * 套餐内 商品详情
     * @param uid
     */
    @Override
    public List<CombodityResponse> comboDetails(String uid) {
        return storeMapper.comboDetails(uid);
    }
    //根据uid查找套餐
    @Override
    public Combo findComboByUid(String uid) {
        return comboMapper.findComboByUid(uid);
    }
    //加入 商城账单明细
    @Override
    public void addStoreDetails(String uid,String comboId,Integer userId, Double price,Integer type,Integer isStore) {
        storeMapper.addStoreDetails(uid,comboId,userId,price,type,isStore);
    }

    @Override
    public void merchantBanner(String uid, String banner) {
        System.out.println("uid="+uid);
        storeMapper.merchantBanner(uid,banner);
    }

    @Override
    public Merchant findMerchantByUid(String uid) {
        return storeMapper.findMerchantByUid(uid);
    }

    @Override
    public List<String> findStorePicturesByUid(String uid) {
        return storeMapper.findStorePicturesByUid(uid);
    }

    /**
     * 指定场景下 查询商家
     * @param value 场景
     * @param name 搜索的商家名
     */
    @Override
    public List<StoresResponse> selectStore(Double longitude,Double latitude,String value, String name) {
        return storeMapper.selectStore(longitude,latitude,value,name);
    }

    @Override
    public MerchantUser findMerchantUser(String username, String password) {
        return storeMapper.findMerchantUser(username,password);
    }

    @Override
    public List<Maps> findData(String name) {
        return storeMapper.findData(name);
    }

    @Override
    public Merchant findMerchantByUser(String username) {
        return storeMapper.findMerchantByUser(username);
    }

    @Override
    public void insertMerchantUser(String uid,String username, String password) {
        storeMapper.insertMerchantUser(uid,username,password);
    }

    @Override
    public void insertStore(String uid, Integer mapsId, String name, String address, String phone) {
        storeMapper.insertStore(uid,mapsId,name,address,phone);
    }

    @Override
    public void updateMerchantUser(String uid, String uuid) {
        storeMapper.updateMerchantUser(uid,uuid);
    }

    @Override
    public Store findStoreByMapsId(Integer mapsId) {
        return storeMapper.findStoreByMapsId(mapsId);
    }

    @Override
    public void merchantUpdatePass(String name, String newPass) {
        storeMapper.merchantUpdatePass(name,newPass);
    }

    @Override
    public void addCombo(String uid, String storeId, String combo, Double price,String summary) {
        storeMapper.addCombo(uid,storeId,combo,price,summary);
    }

    @Override
    public void addCommodity(String id, String uid, String commodityName, Double commodityPri,Integer commoditySi) {
        storeMapper.addCommodity(id,uid,commodityName,commodityPri,commoditySi);
    }

    @Override
    public List<ComboResponse> findComboByName(String name) {
       return storeMapper.findComboByName(name);
    }

    @Override
    public List<Commodity> findCommodityByComboId(String uid) {
        return storeMapper.findCommodityByName(uid);
    }

    @Override
    public void upCombo(String uid, String combo, Double price) {
        storeMapper.upCombo(uid, combo, price);
    }

    @Override
    public String findStoreByName(String name) {
        return storeMapper.findStoreByName(name);
    }

    @Override
    public void upCommodity(String uid, String commodityName, Double commodityPri, Integer commoditySi) {
        storeMapper.upCommodity(uid,commodityName,commodityPri,commoditySi);
    }

    @Override
    public void delCombo(String uid) {
        storeMapper.delCombo(uid);
        storeMapper.delCommodity(uid);
    }

    @Override
    public Store findStore(String name) {
        return storeMapper.findStore(name);
    }

    @Override
    public void storeBanner(String storeId, String picture) {
        storeMapper.storeBanner(storeId,picture);
    }

    @Override
    public void addStoreOrders(String uuid, String uid, Integer userId, Double price, Integer merchantId,String storeId,Integer payType) {
        storeMapper.addStoreOrders(uuid,uid,userId,price,merchantId,storeId,payType);
    }

    @Override
    public List<User> findAllUser() {
        return storeMapper.findAllUser();
    }

    @Override
    public List<MerchantUser> findAllManage() {
        return storeMapper.findAllManage();
    }

    @Override
    public void comboPic(String pic, String comboId) {
        storeMapper.comboPic(pic,comboId);
    }
    @Override
    public  List<ActiveContent> activeContent(Integer pageNum) {
        return  storeMapper.activeContent(pageNum);
    }

    @Override
    public List<StoreTaskResponse> task(Integer pageNum) {

        return  storeMapper.task(pageNum);
    }

    @Override
    public StoreTaskDetailedResponse taskDetailed(String uuid) {
        return storeMapper.taskDetailed(uuid);
    }

    @Override
    public void taskPay(String payId, Integer id, Double money, String uuid,String upUuid,String topic,Integer size,String note) {
        storeMapper.taskPay( payId,  id,  money,  uuid,upUuid,topic,size,note);
    }

    @Override
    public List<TaskMineResponse> taskMine(Integer pageNum, Integer userId) {
        return storeMapper.taskMine( pageNum,  userId);
    }

    @Override
    public ShippingAddressResponse findAddressById(Integer userId) {
        return storeMapper.findAddressById(userId);
    }

    @Override
    public void updateShippingAddress(ShippingAddressRequest request) {
        storeMapper.updateShippingAddress(request);
    }

    @Override
    public void addShippingAddress(ShippingAddressRequest request) {
        storeMapper.addShippingAddress(request);
    }



    @Override
    public Integer findTaskPayUpUserId(String upUuid) {
        return storeMapper.findTaskPayUpUserId(upUuid);
    }

    @Override
    public String findTaskPayByUserIdUpUuId(Integer id, String upUuid) {
        return storeMapper.findTaskPayByUserIdUpUuId(id,upUuid);
    }

    @Override
    public Integer findAllMyTaskPayByUpUuid(String upUuid) {
        return storeMapper.findAllMyTaskPayByUpUuid(upUuid);
    }

    @Override
    public Integer findTaskPayByUpUuid(String uuid) {
        return storeMapper.findTaskPayByUpUuid(uuid);
    }

    @Override
    public StoreOrders findStoreByUuid(String uuid) {
        return storeMapper.findStoreByUuid(uuid);
    }

    @Override
    public void backMoney(Integer userId, Double money) {
        storeMapper.backMoney(userId,money);
    }

    @Override
    public void cancelStoreOrders(Integer type, String uuid) {
        storeMapper.cancelStoreOrders(type,uuid);
    }

    @Override
    public CouponDetailsResponse couponDetails(String uuid) {
        return storeMapper.couponDetails(uuid);
    }

    @Override
    public void upMerchantIcon(String name, String icon,String nickname,String phoneNum) {
        storeMapper.upMerchantIcon(name,icon,nickname,phoneNum);
    }


}
