package com.wuai.company.order.mapper;

import com.wuai.company.entity.Response.Scene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 * 场景mapper层
 */
@Mapper
public interface SceneMapper {
    //保存场景
    void saveScene(Scene scene);

    //查询所有场景
    List<Scene> findAllScene();

    Scene findSceneByKey(@Param("key") String key);

    /**
     * 通过场景名拿到场景背景图
     * @param sceneName
     * @return
     */
    String selectScenePicByScene(@Param("sceneName")String sceneName);

    /**
     * 通过场景名拿到场景内容
     * @param sceneName
     * @return
     */
    String selectSceneContentByScene(@Param("sceneName")String sceneName);
}
