package com.wuai.company.order.dao;

import com.wuai.company.entity.Response.Scene;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 * 场景dao层
 */
public interface SceneDao {
    //保存 场景
    void saveScene(Scene scene);

    List<Scene> findAllScene();

    Scene findSceneByKey(String key);

    String selectScenePicByScene(String sceneName);

    String selectSceneContentByScene(String sceneName);
}
