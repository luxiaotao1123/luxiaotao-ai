package com.wuai.company.order.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.order.entity.TrystScenes;
import com.wuai.company.entity.VideoHome;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.enums.TrystScenesEnum;
import com.wuai.company.enums.VideoTypeEnum;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.order.entity.TrystScene;
import com.wuai.company.order.entity.TrystSceneExample;
import com.wuai.company.order.mapper.TrystSceneMapper;
import com.wuai.company.order.service.TrystSceneService;
import com.wuai.company.user.domain.Push;
import com.wuai.company.user.push.PushUtils;
import com.wuai.company.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("trystSceneServiceImpl")
@Transactional
public class TrystSceneServiceImpl implements TrystSceneService {

    Logger logger = LoggerFactory.getLogger(TrystOrdersServiceImpl.class);

    @Autowired
    private TrystOrdersDao trystOrdersDao;

    @Autowired
    private TrystSceneMapper trystSceneMapper;

    @Override
    public Response homeTrystScenes(Integer id) {
        if (id == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //首页轮播图
        List<VideoHome> videoHomes =  trystOrdersDao.findVideoHome(VideoTypeEnum.HOME.getCode());
        //场景
        List<TrystScene> list = trystSceneMapper.selectByExample(new TrystSceneExample());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scenes",list);
        jsonObject.put("video",videoHomes);
        return Response.success(jsonObject);
    }

    @Override
    public Response moreTrystScenes(Integer id) {
        if (id==null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        List<TrystScenes> list =  trystOrdersDao.trystScenes(TrystScenesEnum.MORE.getCode());
        return Response.success(list);
    }

    /*
    获取一个场景的详情
     */
    @Override
    public Response getSceneDetails(Integer id, String uuid) {
        if (id==null || uuid == null){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        Map<String, Object> map = new HashMap<>();
        TrystSceneExample example = new TrystSceneExample();
        example.or().andUuidEqualTo(uuid);
        List<TrystScene> trystScene = trystSceneMapper.selectByExample(example);
        map.put("scene",trystScene.get(0));
        map.put("tips",TrystScene.TIPS);
        /*Push push = new Push();
        push.setDeviceNum("cef357c13be22d79b72601d9abdbfffe");
        push.setSendDeviceType(0);
        push.setSendTopic("您有一个附近的订单");
        push.setSendContent("测试");
        //                    push.setSendContent(String.valueOf(user2.getId()));
        PushUtils.userPush.getInstance().sendPush(push);*/
        return Response.success(map);
    }

}
