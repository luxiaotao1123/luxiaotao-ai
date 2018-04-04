package com.wuai.company.order.service.impl;

import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.Response.UserVideoResponse;
import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.enums.PayTypeEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.order.service.TrystNearBodyService;
import com.wuai.company.order.util.NearbyUtil;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.util.Response;
import com.wuai.company.util.SysUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("trystNearBodyServiceImpl")
@Transactional
public class TrystNearBodyServiceImpl implements TrystNearBodyService {

    Logger logger = LoggerFactory.getLogger(TrystOrdersServiceImpl.class);

    @Autowired
    private TrystOrdersDao trystOrdersDao;

    @Autowired
    private NearbyUtil nearbyUtil;

    @Autowired
    private UserDao userDao;

    /*
    到redis里面添加附件的人
     */
    @Override
    public Response addNearbyBody(Integer id, Double latitude, Double longitude, String cid) throws IllegalAccessException {
        if(id==null|| SysUtils.checkObjFieldIsNull(longitude)){
            logger.warn("参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"参数为空");
        }
        //认证用户则添加
        UserVideoResponse user = trystOrdersDao.findUserVideoById(id);
//        if (user!=null&&user.getVideoCheck()==1){
        Optional<NearBodyResponse> optionalNearBodyResponse = Optional.ofNullable(trystOrdersDao.findNearBody(id));
        optionalNearBodyResponse.ifPresent( nearBodyResponse -> {
            nearBodyResponse.setVideos(trystOrdersDao.findUserVides(id));
            nearBodyResponse.setLatitude(latitude);
            nearBodyResponse.setLongitude(longitude);
        });
        //更新个推账号
        if (userDao.updateCidByUserId(id,cid) == 0){
            return Response.error("更新cid失败");
        }
        //trystOrdersDao.upTrystOrdersPay(trystId, PayTypeEnum.PARTY_IN_ADVANCE.toCode(),PayTypeEnum.PARTY_IN_ADVANCE.getValue());
        nearbyUtil.addTotalNearbyBody(optionalNearBodyResponse.orElse(null));
//        }
        return Response.successByArray();
    }
}
