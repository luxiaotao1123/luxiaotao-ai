package com.wuai.company.order.service;

import com.wuai.company.util.Response;

/**
 * Created by Administrator on 2017/5/26.
 * 场景
 */
public interface SceneService {
    //选择场景
    Response choose(Integer uid);

    Response storeScene();
}
