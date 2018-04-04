package com.wuai.company.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 场景的选择
 * Created by Ness on 2017/6/6.
 */
public enum SceneSelEnum {
//    KTV("KTV", 0), BAR("酒吧", 1), GYM("健身房", 2), CINEMA("电影院", 3);
    KTV("KTV", 1), BAR("酒吧", 0), GYM("健身房", 2), CINEMA("电影院", 3);
    private String scene;
    private Integer code;

    SceneSelEnum(String scene, Integer code) {
        this.scene = scene;
        this.code = code;
    }

    public String toScene() {
        return scene;
    }


    public String toRedisKey(String redisKey) {
        return (code + ":" + redisKey).intern();
    }

    public Integer toCode() {
        return code;
    }


    private static Map<String, SceneSelEnum> allCate = new ConcurrentHashMap<>();

    public static SceneSelEnum getSceneSelByChineseKey(String key) {


        if (StringUtils.isEmpty(key)) {
            return null;
        }

        SceneSelEnum sceneSelEnum = allCate.get(key);

        if (sceneSelEnum != null) {
            return sceneSelEnum;
        }


        SceneSelEnum[] values = SceneSelEnum.values();

        int len = values.length;
        for (int i = 0; i < len; i++) {
            if (values[i].scene.equals(key)) {
                allCate.put(key, values[i]);
                return values[i];
            }
        }
        return null;


    }


}
