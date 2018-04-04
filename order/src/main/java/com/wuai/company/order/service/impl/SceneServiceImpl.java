package com.wuai.company.order.service.impl;

import com.wuai.company.entity.Response.Scene;
import com.wuai.company.order.dao.SceneDao;
import com.wuai.company.order.service.SceneService;
import com.wuai.company.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * Created by Administrator on 2017/5/26.
 */
@Service
@Transactional
public class SceneServiceImpl implements SceneService {
    @Resource
    private HashOperations<String, String, String> orderHashRedisTemplate;

    @Autowired
    private SceneDao sceneDao;

    private Logger logger = LoggerFactory.getLogger(SceneServiceImpl.class);

    /**
     * 选择场景
     * 默认(把用户的)最后一次发布拉取到最前面，其余顺寻延后
     */

    @Override
    public Response choose(Integer uid) {

        List<Scene> scenes = sceneDao.findAllScene();
        Map<String,String> sceneMap = orderHashRedisTemplate.entries(String.valueOf(uid));
        //获取所有的keys
        Set<String> keysSet = orderHashRedisTemplate.keys(String.valueOf(uid));
        for (String val : keysSet) {
            //返回缓存值
            if (sceneMap.get(val)==null||sceneMap.get(val).equals("")){
                //删除value为空的值
                orderHashRedisTemplate.delete(String.valueOf(uid),val);
            }
        }


        if (orderHashRedisTemplate.keys(String.valueOf(uid)).size() == 0) {
            Map<String, String> map1 = new LinkedHashMap<>();
            logger.info(uid + ":第一次选择场景");
            //从数据库查询所有场景

            for (int i = 0; i < scenes.size(); i++) {
                Scene scene = scenes.get(i);
                //返回默认的场景
                map1.put(scene.getKey(), scene.getValue());
//                if (scene.getKey().equals("0")){
//                    orderHashRedisTemplate.put(String.valueOf(uid),scene.getKey(),defaultValue);
//                }else if (scene.getKey().equals(num)){
//                    orderHashRedisTemplate.put(String.valueOf(uid),scene.getKey(),upValue);
//                }else {
//                    orderHashRedisTemplate.put(String.valueOf(uid), scene.getKey(), scene.getValue());
//                }
                orderHashRedisTemplate.put(String.valueOf(uid), scene.getKey(), scene.getValue());
            }

            return Response.success(map1);
        }
        //当 redis 存储的数据不正常时 重新从数据库获取
        else if (orderHashRedisTemplate.keys(String.valueOf(uid)).size() != scenes.size()) {
            Map<String, String> map3 = new LinkedHashMap<>();

            for (int i = 0; i < scenes.size(); i++) {
                Scene scene = scenes.get(i);
                //返回默认的场景
                map3.put(scene.getKey(), scene.getValue());

                orderHashRedisTemplate.put(String.valueOf(uid), scene.getKey(), scene.getValue());
            }

            return Response.success(map3);
        }
        else {
//            Map<String, String> map2 = new LinkedHashMap<>();

            //获取所有的keys
//            Set<String> keys = orderHashRedisTemplate.keys(String.valueOf(uid));
//
//            for (String val : keys) {
//                //返回缓存值
//                map2.put(val, orderHashRedisTemplate.get(String.valueOf(uid), val));
//
//            }
//            Map<String,String>  maps = orderHashRedisTemplate.entries(String.valueOf(uid));
//            Collection<String> collection = maps.values();
//            Object[] values =collection.toArray();
//            for (int i=0;i<values.length;i++){
//                String value = (String) values[i];
//                if (value.equals())
//            }
            Set<String> set = orderHashRedisTemplate.keys(String.valueOf(uid));
            for (String str:set){
//                Integer key = Integer.parseInt(str);
//                if (key.intValue()!=)
                    for (int i=0;i<scenes.size();i++){
                        Scene  scene = scenes.get(i);
                        if (scene.getKey().equals(str)){
                            String value = orderHashRedisTemplate.get(String.valueOf(uid),str);
                            if (!value.equals(scene.getValue())){
                                orderHashRedisTemplate.delete(String.valueOf(uid),str);
                                orderHashRedisTemplate.put(String.valueOf(uid),scene.getKey(),scene.getValue());
                            }
                        }
                    }
            }
            return Response.success(orderHashRedisTemplate.entries(String.valueOf(uid)));
        }
    }


    @Override
    public Response storeScene() {
        List<Scene> scene = sceneDao.findAllScene();
        return Response.success( scene);
    }
}
