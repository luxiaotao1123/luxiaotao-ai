package com.wuai.company.order.service;

import com.wuai.company.util.Response;

public interface TrystSceneService {

    Response homeTrystScenes(Integer id);

    Response moreTrystScenes(Integer attribute);

    Response getSceneDetails(Integer id, String uuid);

}
