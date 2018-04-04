package com.wuai.company.store.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.*;
import com.wuai.company.entity.request.ShippingAddressRequest;
import com.wuai.company.enums.OrdersTypeEnum;
import com.wuai.company.enums.PayTypeEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.enums.SysKeyEnum;
import com.wuai.company.store.dao.StoreDao;
import com.wuai.company.store.entity.*;
import com.wuai.company.store.entity.response.ComboResponse;
import com.wuai.company.store.entity.response.StoresResponse;
import com.wuai.company.store.service.StoreService;
import com.wuai.company.store.util.MerchantRandom;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/14.
 */
@Service
@Transactional
public class StoreServiceImpl implements StoreService{
    @Autowired
    private StoreDao storeDao;

    @Resource
    private HashOperations<String, String, String> storeHashRedisTemplate;

    @Value("${scenes.bar}")
    private String bar;
    @Value("${scenes.ktv}")
    private String ktv;
    @Value("${scenes.gym}")
    private String gym;
    @Value("${scenes.theatre}")
    private String theatre;

    private Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);
    /**
     *  根据场景 查找商城
     * @param scene 场景 例：0
     * @param pageNum 页码 例：1
     */
    @Override
    public Response findStoreByScene(Double longitude,Double latitude,Integer scene, Integer pageNum) {
        //从缓存中获取场景地址
//        String value = storeHashRedisTemplate.get(uid,String.valueOf(scene));
        String value = null;
        if (scene==0){
            value = bar;
        }else if (scene==1){
            value = ktv;
        }else if (scene==2){
            value = gym;
        } else if (scene==3){
            value = theatre;
        }

        //查找 所有该场景下的商城

        List<StoresResponse> list = storeDao.findStoreByScene(longitude,latitude,value,pageNum);
        for (int i=0;i<list.size();i++){
            StoresResponse store = list.get(i);
            //根据id查找 经纬度
            Maps maps =  storeDao.findMapsLoAndLaById(store.getMapsId());
            DecimalFormat df = new DecimalFormat("######0.00");
            Double distanceKm = DistanceTools.getKmByPoint(maps.getLatitude(),maps.getLongitude(),latitude,longitude);
            String distance = df.format(distanceKm)+"km";
            store.setDistance(distance);
        }
        return Response.success(list);
    }

    /**
     * 商城首页
     * @param longitude 经度
     * @param latitude 纬度
     * @param pageNum 页码
     */
    @Override
    public Response home(Double longitude, Double latitude, Integer pageNum) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(pageNum);
        List<Store> list = storeDao.home(longitude,latitude,pageRequest.getPageNum());
        return Response.success(list);
    }
    /**
     * 套餐详情
     * @param uid 商店id
     * @param pageNum 例：pageNum=0
     */
    @Override
    public Response storeDetails(String uid, Integer pageNum) {
        if(StringUtils.isEmpty(uid)){
            logger.warn("套餐详情 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        List<String> pictures = storeDao.findStorePicturesByUid(uid);

        List<Combo> list = storeDao.storeDetails(uid,pageNum);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("combo",list);
        map.put("pictures",pictures);
        return Response.success(map);
    }
    /**
     * 套餐内 商品详情
     * @param uid
     */
    @Override
    public Response comboDetails(String uid) {
        if (StringUtils.isEmpty(uid)){
            logger.warn("商品详情 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"");
        }
        Combo combo = storeDao.findComboByUid(uid);
        List<CombodityResponse> list = storeDao.comboDetails(uid);
       JSONObject map = new JSONObject();
//        Double sumPrice =0.0;
//        for (int i=0;i<list.size();i++){
//            Commodity co = list.get(i);
//            Double price = co.getPrice();
//            Double size = Double.valueOf(co.getSize());
//            sumPrice= Arith.add(2,sumPrice,Arith.multiplys(2,price,size));
//        }
        Combo com  = storeDao.findComboByUid(uid);
        map.put("sumPrice",combo.getPrice());
        map.put("combo",list);
        map.put("comboName",com.getCombo());
        map.put("picture",com.getPicture());
        map.put("summary",com.getSummary());
        return Response.success(map);
    }
    /**
     * 用户 购买套餐
     * @param uid 套餐id
     * @param userId    用户id
     */
    @Override
    public Response pay(String uid, Integer userId) {
        //根据uid查找套餐
        Combo combo = storeDao.findComboByUid(uid);
        if (combo!=null){
            //加入 商城记录 uuid，套餐id，用户id，价格,商家id
            String uuid =UserUtil.generateUuid();
            Merchant merchant = storeDao.findMerchantByUid(combo.getStoreId());
            if (merchant==null){
                return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
            }

            storeDao.addStoreOrders(uuid,uid,userId,combo.getPrice(),merchant.getId(),merchant.getStoreId(),PayTypeEnum.STORE_WAIT_PAY.toCode());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("money",combo.getPrice());
            jsonObject.put("uid",uuid);
            return Response.success(jsonObject);
        }
        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"购买套餐失败");
    }
    /**
     * 上传 店铺banner图
     * @param name 商家用户名
     * @param banner banner图
     */
    @Override
    public Response merchantBanner(String name, String banner) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(banner)){
            logger.warn("上传店铺banner图 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //查找 该商家的店铺
        Merchant merchant = storeDao.findMerchantByUser(name);
        Store store = storeDao.findStore(name);
        if (merchant.getStoreId()==null||store==null){
            logger.warn("该用户店铺不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户店铺不存在");
        }
        //添加banner图
        storeDao.merchantBanner(merchant.getStoreId(),banner);
        return Response.successByArray();
    }

    /**
     * 详情图
     * @param name
     * @param banner
     * @return
     */
    @Override
    public Response storeBanner(String name,String banner) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(banner)){
            logger.warn("上传详情图 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //查找 该商家的店铺
        Merchant merchant = storeDao.findMerchantByUser(name);
        Store store = storeDao.findStore(name);
        if (merchant.getStoreId()==null||store==null){
            logger.warn("该用户店铺不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户店铺不存在");
        }
        //添加详情图
        if (store.getPictures()==null||store.getPictures().isEmpty()){
            storeDao.storeBanner(merchant.getStoreId(),banner);
            return Response.successByArray();
        }else {
            if (store.getPictures().split(",").length>=10){
                logger.warn("详情图数量 最多只能放10张");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"详情图最多只能放10张");
            }
            String picture = store.getPictures()+","+banner;
            storeDao.storeBanner(merchant.getStoreId(),picture);
            return Response.successByArray();
        }


    }

    /**
     * 指定场景下 查询商家
     * @param scene 场景
     * @param name 搜索的商家名
     */
    @Override
    public Response selectStore(Double longitude ,Double latitude,Integer scene, String name) {
        String value = null;
        if (scene==0){
            value = bar;
        }else if (scene==1){
            value = ktv;
        }else if (scene==2){
            value = gym;
        } else if (scene==3){
            value = theatre;
        }
        List<StoresResponse> list = storeDao.selectStore(longitude,latitude,value,name);
//        List<StoresResponse> list = storeDao.findStoreByScene(longitude,latitude,value,pageNum);
        for (int i=0;i<list.size();i++){
            StoresResponse store = list.get(i);
            //根据id查找 经纬度
            Maps maps =  storeDao.findMapsLoAndLaById(store.getMapsId());
            DecimalFormat df = new DecimalFormat("######0.00");
            Double distanceKm = DistanceTools.getKmByPoint(maps.getLatitude(),maps.getLongitude(),latitude,longitude);
            String distance = df.format(distanceKm)+"km";
            store.setDistance(distance);
        }


        return Response.success(list);
    }

    @Override
    public Response merchantLogin(String username, String password) {
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String pass = MD5.encryption(MD5.encryption(password));
        MerchantUser merchantUser = storeDao.findMerchantUser(username,pass);
        if (merchantUser!=null){
            return Response.success(merchantUser);
        }
        logger.warn("用户名或密码错误");
        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户名或密码错误");
    }

    @Override
    public Response findData(String name) {
        List<Maps> list = storeDao.findData(name);
        return Response.success(list);
    }

    @Override
    public Response generateUsername() {
        String username = MerchantRandom.randomByLength(11);
        Merchant merchant = storeDao.findMerchantByUser(username);
        while (merchant!=null){
            username =  MerchantRandom.randomByLength(11);
            merchant = storeDao.findMerchantByUser(username);
        }
        String uid = UserUtil.generateUuid();
        storeDao.insertMerchantUser(uid,username,MD5.encryption(MD5.encryption(username)));
        return Response.success(username);
    }

    @Override
    public Response merchantRegister(String uid,Integer mapsId, String name, String address, String phone) {
        String uuid = UserUtil.generateUuid();
        Store store = storeDao.findStoreByMapsId(mapsId);
        if (store==null){
            //添加商店
            storeDao.insertStore(uuid,mapsId,name,address,phone);
            //binding商店
            storeDao.updateMerchantUser(uid,uuid);
            return Response.successByArray();
        }
       return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该商铺已存在");
    }

    @Override
    public Response merchantUpdatePass(String name, String newPass, String pass) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(newPass)||StringUtils.isEmpty(pass)){
            logger.warn("修改密码 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Merchant merchant = storeDao.findMerchantByUser(name);
        String passw = MD5.encryption(MD5.encryption(pass));
        if (merchant!=null) {

            if (merchant.getPassword().equals(passw)){
                storeDao.merchantUpdatePass(name,MD5.encryption(MD5.encryption(newPass)));
                return Response.successByArray();

            }else {
                logger.warn("修改密码 请输入正确的密码");
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"请输入正确的密码");
            }
        }
        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"用户不存在");
    }

    @Override
    public Response addCombo(String name, String storeId, String combo, String price, String commodity, String commodityPrice,String commoditySize,String summary) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(storeId)||StringUtils.isEmpty(combo)||StringUtils.isEmpty(price)
                ||StringUtils.isEmpty(commodity)||StringUtils.isEmpty(commodityPrice)||StringUtils.isEmpty(commoditySize)||StringUtils.isEmpty(summary)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        String uid = UserUtil.generateUuid();
        storeDao.addCombo(uid,storeId,combo,Double.valueOf(price),summary);
        String[] com = commodity.split(",");
        String[] pri = commodityPrice.split(",");
        String[] size = commoditySize.split(",");
        for (int i=0;i<com.length;i++){
            String commodityName =com[i];
            Double commodityPri = Double.valueOf(pri[i]);
            Integer commoditySi = Integer.valueOf(size[i]);
            String id = UserUtil.generateUuid();
           storeDao.addCommodity(id,uid,commodityName,commodityPri,commoditySi);

        }
        return Response.success(uid);
    }

    @Override
    public Response findStoreByName(String name) {
        List<ComboResponse> list= storeDao.findComboByName(name);
        for (int i=0;i<list.size();i++){
            ComboResponse combo = list.get(i);
            List<Commodity> commodity = storeDao.findCommodityByComboId(combo.getUuid());
            combo.setCommodity(commodity);
        }
        return Response.success(list);
    }

    @Override
    public Response upCombo(String name,String uid, String combo, String price, String commodity, String commodityPrice, String commoditySize,String summary) {
        if (StringUtils.isEmpty(uid)||StringUtils.isEmpty(name)||StringUtils.isEmpty(combo)||StringUtils.isEmpty(commodity)||StringUtils.isEmpty(commodityPrice)||StringUtils.isEmpty(commoditySize)||StringUtils.isEmpty(summary)){
            logger.warn("修改套餐 参数为空");
            return  Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
//        String storeId = storeDao.findStoreByName(name);
//        storeDao.upCombo(uid,combo,Double.valueOf(price));
        Combo combo1 = storeDao.findComboByUid(uid);
//        List<Commodity> list = storeDao.findCommodityByComboId(uid);
        String[] com = commodity.split(",");
        String[] pri = commodityPrice.split(",");
        String[] size = commoditySize.split(",");
//        for (int i=0;i<com.length;i++){
//            String commodityName =com[i];
//            Double commodityPri = Double.valueOf(pri[i]);
//            Integer commoditySi = Integer.valueOf(size[i]);
//            storeDao.upCommodity(list.get(i).getUid(),commodityName,commodityPri,commoditySi);
//        }
        if (com.length!=pri.length||com.length!=size.length||pri.length!=size.length){
            logger.warn("套餐 数量不一致");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"套餐 商品数量不一致");
        }
        storeDao.delCombo(uid);
        storeDao.addCombo(uid,combo1.getStoreId(),combo, Double.valueOf(price),summary);
        if (StringUtils.isNotEmpty(combo1.getPicture())){
            storeDao.comboPic(combo1.getPicture(),uid);
        }
        for (int i=0;i<com.length;i++){
            String commodityName =com[i];
            Double commodityPri = Double.valueOf(pri[i]);
            Integer commoditySi = Integer.valueOf(size[i]);
            String id = UserUtil.generateUuid();
            storeDao.addCommodity(id,uid,commodityName,commodityPri,commoditySi);
        }

        return Response.successByArray();
    }

    @Override
    public Response delCombo(String name, String uid) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(uid)){
            logger.warn("删除订单");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        storeDao.delCombo(uid);
        return Response.successByArray();
    }

    @Override
    public Response upPass(String name, String pass) {
        if(StringUtils.isEmpty(name)||StringUtils.isEmpty(pass)){
            logger.warn("修改密码 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Merchant m = storeDao.findMerchantByUser(name);
        if (m==null){
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode());
        }
        storeDao.merchantUpdatePass(name, pass);
        return Response.successByArray();
    }
    @Override
    public Response showPictures(String name) {
        Store store = storeDao.findStore(name);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("banner",store.getBanner());
        String[] p = store.getPictures().split(",");
        List<String> list = new ArrayList<String>();
        for (int i=0;i<p.length;i++){
            list.add(p[i]);
        }
        map.put("pictures",list);
        return Response.success(map);
    }

    @Override
    public Response delPic(String name, String pic) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(pic)){
            logger.warn("删除详情图 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Merchant merchant = storeDao.findMerchantByUser(name);
        if (merchant==null){
            logger.warn("删除详情图 店铺不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode());
        }
        Store store = storeDao.findStore(name);
        String[] p = store.getPictures().split(",");
        List<String> list = new ArrayList<String>();
        if (p.length==1){
            storeDao.storeBanner(merchant.getStoreId(),"");
            return Response.successByArray();
        }else {

            for (int i=0;i<p.length;i++){
                if (!p[i].equals(pic)) {
                    list.add(p[i]);
                }
            }
        }

        System.out.println("lists="+list);
        if (list!=null){
            //定义一个存放最终字符串的StringBuffer
            StringBuffer str = new StringBuffer();
            for (int j = 0; j < list.size(); j++) {
                String a = list.get(j).toString();
                //如果不是？号就把这个字符加在上面定义的StringBuffer
                if (str.length()==0){
                    str.append(a);
                }else if (str.length()!=0){
                    str.append(","+a);
                }
            }
            storeDao.storeBanner(merchant.getStoreId(),str.toString());
            return Response.successByArray();
        }


        return Response.error(ResponseTypeEnum.ERROR_CODE.toCode());

    }

    @Override
    public Response delBanner(String name, String pic) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(pic)){
            logger.warn("删除店铺banner图 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //查找 该商家的店铺
        Merchant merchant = storeDao.findMerchantByUser(name);
        Store store = storeDao.findStore(name);
        if (merchant.getStoreId()==null||store==null){
            logger.warn("该用户店铺不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"该用户店铺不存在");
        }
        //添加banner图
        storeDao.merchantBanner(merchant.getStoreId(),"");
        return Response.successByArray();
    }

    @Override
    public Response propellingUser(String name, String topic, String content) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(topic) || StringUtils.isEmpty(content)) {
            logger.warn("推送 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<User> user = storeDao.findAllUser();
        for (int i=0;i<user.size();i++){
            if (!user.get(i).getCid().equals("")&&user.get(i).getCid()!=null){
                Push push = new Push();
                push.setDeviceNum(user.get(i).getCid());
                Integer type;
                if (user.get(i).getType()==2){
                    type = 0;
                }else {
                    type=user.get(i).getType();
                }
                push.setSendDeviceType(type);
                push.setSendTopic(topic);
//                JSONObject jsonObject = new JSONObject();
//                JSONObject jsonObject2 = new JSONObject();
//                jsonObject2.put("1","1");
//                jsonObject2.put("2","1");
//                jsonObject2.put("3","1");
//                jsonObject2.put("4","1");
//                jsonObject.put("type","0");
//                jsonObject.put("data",jsonObject2);
//                push.setSendContent(String.valueOf(jsonObject));
                push.setSendContent(content);
                PushUtils.userPush.getInstance().sendManagePush(push);
            }
        }


        return Response.successByArray();
    }

    @Override
    public Response propellingManage(String name, String topic, String content) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(topic) || StringUtils.isEmpty(content)) {
            logger.warn("推送 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<MerchantUser> user = storeDao.findAllManage();

        for (int i=0;i<user.size();i++){
            if (!user.get(i).getCid().equals("")&&user.get(i).getCid()!=null){
                Push push = new Push();
                push.setDeviceNum(user.get(i).getCid());
                push.setSendDeviceType(user.get(i).getType());
                push.setSendTopic(topic);
                push.setSendContent(content);
                PushUtils.storePush.getInstance().sendPush(push);
            }

        }
        return Response.successByArray();
    }

    @Override
    public Response propellingAll(String name, String topic, String content) {
        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(topic) || StringUtils.isEmpty(content)) {
            logger.warn("推送 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
       propellingUser(name, topic, content);
        propellingManage( name,  topic,  content);
        return Response.successByArray();
    }

    @Override
    public Response comboPic(String name, String pic, String comboId) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(pic)||StringUtils.isEmpty(comboId)){
            logger.warn("上传套餐图 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Merchant merchant = storeDao.findMerchantByUser(name);
        if (merchant==null){
            logger.warn("上传套餐图 用户不存在");
            return Response.error(ResponseTypeEnum.ERROR_CODE.toCode());
        }
        storeDao.comboPic(pic,comboId);
        return Response.successByArray();
    }

    @Override
    public Response active(Integer pageNum) {
        List<ActiveContent> activeContent = storeDao.activeContent(pageNum);
//        for (int i=0;i<activeContent.size();i++){
//           List<ActivePic> activePics = storeDao.activePic(activeContent.get(i).getId());
//            activeContent.get(i).setActivePic(activePics);
//        }
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("active",activeContent);
        return Response.success(activeContent);
    }

    @Override
    public Response information(String name) {
        if (StringUtils.isEmpty(name)){
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Store store = storeDao.findStore(name);
        return Response.success(store);
    }

    @Override
    public Response task(Integer pageNum) {
        List<StoreTaskResponse> list = storeDao.task(pageNum);
        return Response.success(list);
    }

    @Override
    public Response taskDetailed(String uuid) {
        if(StringUtils.isEmpty(uuid)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        StoreTaskDetailedResponse response = storeDao.taskDetailed(uuid);
        return Response.success(response);
    }

    @Override
    public Response taskPay(String uuid,Integer id,String upUuid,String note) {
        if(StringUtils.isEmpty(uuid)||id==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        StoreTaskDetailedResponse response = storeDao.taskDetailed(uuid);
        Integer upUserId = null;
        if (StringUtils.isNotEmpty(upUuid)){
            upUserId = storeDao.findTaskPayUpUserId(upUuid);
            String exit = storeDao.findTaskPayByUserIdUpUuId(id,upUuid);
            //判断是否已邀请该用户购买
            // 是否为自己本人 若是 则清空upUuid 视为重新购买
            if (id.equals(upUserId)||StringUtils.isNotEmpty(exit)){
                upUuid=null;
            }
        }

        String payId = UserUtil.generateUuid();


        storeDao.taskPay(payId,id,response.getMoney(),uuid,upUuid,response.getTopic(),response.getSize(),note);
        char c = OrdersTypeEnum.STORE_TASK_PAY.getQuote();
        String type = String.valueOf(c);
        StringBuffer stringBuffer = new StringBuffer(type);
        if (upUuid!=null){
            //根据上级订单id 获取该订单已完成人数
            Integer size = storeDao.findAllMyTaskPayByUpUuid(upUuid);
            if (size==null||size<=0){
                stringBuffer.append(upUserId);
                stringBuffer.append("t");
                stringBuffer.append(response.getBackMoney());
                stringBuffer.append("m");
            }else {
                stringBuffer.append("f");
            }

        }
        if (upUuid==null){
            stringBuffer.append("f");
        }
        stringBuffer.append(payId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("payId",stringBuffer.toString());
        jsonObject.put("money",response.getMoney());
        return Response.success(jsonObject);
    }

    @Override
    public Response taskMine(Integer pageNum, Integer userId) {
        if (pageNum==null||userId==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<TaskMineResponse> list = storeDao.taskMine(pageNum,userId);
        List<TaskMineResponse> list2 = new ArrayList<TaskMineResponse>();
        for(int i=0;i<list.size();i++){
            TaskMineResponse response = list.get(i);
            Integer downSize = storeDao.findTaskPayByUpUuid(response.getUuid());
            response.setDownSize(downSize);
            list2.add(response);
        }
        return Response.success(list2);
    }

    @Override
    public Response shippingAddress(Integer userId) {
        if (userId==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        ShippingAddressResponse address = storeDao.findAddressById(userId);
        return Response.success(address);
    }

    @Override
    public Response updateShippingAddress(ShippingAddressRequest addressRequest, Integer id) throws IllegalAccessException {
        addressRequest.setUserId(id);
        int type=0;
        if (StringUtils.isEmpty(addressRequest.getUuid())){

            String uuid = UserUtil.generateUuid();
            addressRequest.setUuid(uuid);
            type++;
        }
        if (SysUtils.checkObjFieldIsNull(addressRequest)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        if (type==0) {
            logger.info("修改收货地址userId={}",id);
            storeDao.updateShippingAddress(addressRequest);
        }else {
            logger.info("添加收货地址userId={}",id);
            ShippingAddressResponse address = storeDao.findAddressById(id);
            if (address!=null){
                logger.warn("重复添加地址,userId={}",address.getUserId());
                return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"重复添加地址");
            }
            storeDao.addShippingAddress(addressRequest);
        }
        return  Response.successByArray();
    }

    @Override
    public Response cancelStoreOrders(Integer id, String uuid) {
        if (id==null||StringUtils.isEmpty(uuid)) {
            logger.warn("取消商城订单参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(), "参数为空");
        }
        StoreOrders storeOrders = storeDao.findStoreByUuid(uuid);
        //取消订单
        storeDao.cancelStoreOrders(PayTypeEnum.STORE_CANCEL.toCode(),uuid);
        //返还金额
        storeDao.backMoney(id,storeOrders.getMoney());
        return Response.successByArray();
    }

    @Override
    public Response couponDetails(Integer id, String uuid) {
        if (id==null||StringUtils.isEmpty(uuid)) {
            logger.warn("优惠券详情 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(), "参数为空");
        }
        //优惠券详情
        CouponDetailsResponse response = storeDao.couponDetails(uuid);
        return Response.success(response);
    }

    @Override
    public Response upMerchantIcon(String name, String icon,String nickname,String phoneNum) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(icon)||StringUtils.isEmpty(nickname)||StringUtils.isEmpty(phoneNum)) {
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(), "参数为空");
        }
        storeDao.upMerchantIcon(name,icon,nickname,phoneNum);
        return Response.successByArray();
    }

    public static void main(String[] args) {
//        DecimalFormat     df   = new DecimalFormat("######0.00");
//        double d1 = 3.23456;
//        double d2 = 0.0;
//        double d3 = 2.0;
//        String a= df.format(d1);
//        df.format(d2);
//        df.format(d3);
        String a ="aaa";
        String b = "aab";
        a.equals(b);
    }
}

