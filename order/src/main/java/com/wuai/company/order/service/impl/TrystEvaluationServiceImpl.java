package com.wuai.company.order.service.impl;

import java.util.List;
import java.util.UUID;

import com.wuai.company.entity.TrystOrders;
import com.wuai.company.entity.TrystReceive;
import com.wuai.company.entity.User;

import com.wuai.company.enums.MsgTypeEnum;
import com.wuai.company.util.comon.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.wuai.company.enums.MoneyRateEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.order.dao.TrystEvaluationDao;
import com.wuai.company.order.entity.TrystEvaluation;
import com.wuai.company.order.service.TrystEvaluationService;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.util.Response;
import com.wuai.company.util.UserUtil;

import javax.annotation.Resource;

/**
 * Created by zTerry on 2018/3/14.
 * 用户评价service实现类
 */
@Service
@Transactional
public class TrystEvaluationServiceImpl implements TrystEvaluationService{

	@Autowired
    private TrystEvaluationDao trystEvaluationDao;
	
	@Autowired
    private UserDao userDao;

	@Resource
	private ZSetOperations<String,String> msgValueTemplate;

	private Logger logger = LoggerFactory.getLogger(TrystEvaluationServiceImpl.class);
	private final String MSG_CONTENT_TRYST = "%s:%s:%s:%s:%s";
	private final String USER_MSG_TRYST = "tryst:%s:msg"; //用户id--信息列表
	
	@Override
	public Response createEvaluation(Integer id, String targetUserId,
			Integer type, String serviceAttitude, String serviceLabel,
			String similarity, String similarityLabel,String trystId) {
		//判断必要参数

		if(null == id || null == targetUserId || null == serviceAttitude || "".equals(serviceAttitude) || null == type){
			logger.warn("创建新的评价 所传参数为空 ownUserId={} targetUserId={} serviceAttitude={} type={}", id, targetUserId,serviceAttitude,type);
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
		}
		
		//通过目标用户的uuid查询真实id
	    Integer  tUId = userDao.findUserIdByUuId(targetUserId);
	    if(null == tUId){
	    	logger.warn("参数不合法 targetUserId={}", targetUserId);
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
	    }
	    
	    //判断是否已经评价该用户
	    List<TrystEvaluation> record = trystEvaluationDao.selectEvaluationByTrystId(trystId, tUId);
		if(0 != record.size()){
			logger.warn("订单已评价");
            return Response.response(ResponseTypeEnum.ERROR_CODE.toCode());
		}

		User user = userDao.findUserByUserId(id);
	    
	    
		TrystEvaluation evaluation = new TrystEvaluation();
		evaluation.setUuid(UserUtil.generateUuid());
		evaluation.setOwnUserId(id);
		evaluation.setTargetUserId(tUId);
		evaluation.setType(type);
		evaluation.setServiceAttitude(serviceAttitude);
		evaluation.setTrystId(trystId);
		if(null != serviceLabel && !"".equals(serviceLabel)){
			evaluation.setServiceLabel(serviceLabel);
		}
		
		if(serviceAttitude.equals("好评")){
			//服务态度为好评的情况下 才有相似度评价
			if(null != similarity && !"".equals(similarity)){
				evaluation.setSimilarity(similarity);
			}
			if(null != similarityLabel && !"".equals(similarityLabel)){
				evaluation.setSimilarityLabel(similarityLabel);
			}
		}
		
		int i = trystEvaluationDao.insertSelective(evaluation);
		if(0 >= i){
			//插入失败
            return Response.response(ResponseTypeEnum.ERROR_CODE.toCode());
		}
		
		
		//成功评价之后 如果是发单方评价赴约方  用户钱包金额根据评价进行最后的分配 好评 10% 中评 5%   差评 0%
		
		if(1 == type){
			
			Double restMoney = 0.0;
			//查询当前目标用户(赴约方)  
			//User targetUser = userDao.findUserByUuid(targetUserId);
			//Double userMoney = targetUser.getMoney();
			TrystOrders order = userDao.findTrystOrdersByUid(trystId);
			if(serviceAttitude.equals("好评")){
				//10%
				restMoney = order.getMoney()*MoneyRateEnum.GOOD_PRAISE_RATE.getKey();
			}else if(serviceAttitude.equals("中评")){
				//5%
				restMoney = order.getMoney()*MoneyRateEnum.MEDIUN_PRAISE_RATE.getKey();
			}
			//更新用户金额
			userDao.updateMoney(restMoney, tUId);
		}
		

		//系统消息通知
		long size = msgValueTemplate.size(String.format(USER_MSG_TRYST,tUId));
		String content = String.format(MSG_CONTENT_TRYST,id, MsgTypeEnum.EVALUATION_MONEY_TRYST.getCode(),user.getNickname(),trystId, TimeUtil.currentTime());
		msgValueTemplate.add(String.format(USER_MSG_TRYST,tUId),content ,size+1);

		return Response.success();
	}


	 /**
     * 根据用户Id查询所有隶属于该用户指定订单的评价
     *
     * @param trystId    订单uuid
     * @param ownUserId  评价者的Id
     * @return
     */
	@Override
	public Response selectEvaluationByTrystId(String trystId, Integer ownUserId) {
		if(StringUtil.isEmpty(trystId) || null == ownUserId){
			logger.warn("评价查询 所传参数为空 ownUserId={} trystId={} ", ownUserId, trystId);
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
		}

		return Response.success();
	}
	 /**
     * 更新评价记录
     *
     * @param id    当前操作者的id
     * @return
     */
	@Override
	public Response deleteEvaluation(Integer id,String uuid) {
		
		if( null == uuid || "".equals(uuid) || null == id){
			logger.warn("评价查询 所传参数为空 Uuid={} id={}",uuid,id);
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
		}
	    //根据UUID查询记录	
		TrystEvaluation record = trystEvaluationDao.selectByUuid(uuid);
		
		if(null == record){
			logger.warn("评价查询 所传参数不合法",uuid);
            return Response.response(ResponseTypeEnum.EMPTY_PARAM.toCode());
		}
		
		//只允许删除自己的评价
		if(!id.equals(record.getOwnUserId())){
			logger.warn("只允许删除自己的评价");
            return Response.response(ResponseTypeEnum.ERROR_CODE.toCode());
		}
		
		//只允许删除自己的评价
		if(true == record.getDeleted()){
			logger.warn("当前评价已删除");
		    return Response.response(ResponseTypeEnum.ERROR_CODE.toCode());
		}
		
		//将当前要修改的评价记录状态修改为删除
		record.setDeleted(true);
		int i = trystEvaluationDao.updateByPrimaryKeySelectiveByUuid(record);
		if(0 >= i){
			logger.warn("评价更新失败");
            return Response.response(ResponseTypeEnum.ERROR_CODE.toCode());
		}
		return Response.success();
	}


}
