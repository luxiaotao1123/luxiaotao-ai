package com.wuai.company.order.dao.impl;

import com.alibaba.druid.util.StringUtils;
import com.wuai.company.entity.Response.Scene;
import com.wuai.company.order.dao.SceneDao;
import com.wuai.company.order.mapper.SceneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 * 场景具体实现层
 */
@Repository
public class SceneDaoImpl implements SceneDao{
    @Autowired
    private SceneMapper sceneMapper;

    private Logger logger = LoggerFactory.getLogger(SceneDaoImpl.class);
    @Override
    public void saveScene(Scene scene) {
        if (scene==null|| StringUtils.isEmpty(scene.getUuid())){
            logger.warn("所传场景参数为空");
        }
        sceneMapper.saveScene(scene);
    }

    @Override
    public List<Scene> findAllScene() {
        return sceneMapper.findAllScene();
    }

    @Override
    public Scene findSceneByKey(String  key) {
        return sceneMapper.findSceneByKey(key);
    }

    @Override
    public String selectScenePicByScene(String sceneName) {
        return sceneMapper.selectScenePicByScene(sceneName);
    }

    @Override
    public String selectSceneContentByScene(String sceneName) {
        return sceneMapper.selectSceneContentByScene(sceneName);
    }
}
