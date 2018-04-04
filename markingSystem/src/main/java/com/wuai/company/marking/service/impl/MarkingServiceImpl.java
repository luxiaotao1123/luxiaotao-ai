package com.wuai.company.marking.service.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wuai.company.entity.request.AppraiseRequest;
import com.wuai.company.enums.MarkingSysEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.marking.dao.MarkingDao;
import com.wuai.company.marking.service.MarkingService;
import com.wuai.company.util.Response;
import com.wuai.company.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by Administrator on 2017/6/14.
 */
@Service
@Transactional
public class MarkingServiceImpl implements MarkingService {
    @Autowired
    private MarkingDao markingDao;
    Logger logger = LoggerFactory.getLogger(MarkingServiceImpl.class);

    @Override
    public Response addAppraise(Integer id, String appraiseRequest) {
        if (id==null){
            logger.warn("评价参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(),"评价 参数为空");
        }
        logger.info(appraiseRequest);
        JSONArray jsonArray = JSONArray.parseArray(appraiseRequest);
        String uuid = UserUtil.generateUuid();
        for (int i=0;i<jsonArray.size();i++){
            AppraiseRequest appraise = JSONObject.parseObject(jsonArray.get(i).toString(),AppraiseRequest.class);
            String content=null;
            if (appraise.getContent()==null||appraise.getContent().equals("")){
                content="用户未评论";
            }else {
                content=appraise.getContent();
            }

            Integer grade = appraise.getStar()/5*100;

            markingDao.addAppraise(appraise.getUserId(),id,uuid,appraise.getOrdersId(),grade,content, MarkingSysEnum.APPRAISE_MARK.getKey());
//            ordersDao.addAppraise(appraise.getUserId(),id,uuid,appraise.getOrdersId(),appraise.getStar(),content);
        }

        return Response.successByArray();

    }
}

