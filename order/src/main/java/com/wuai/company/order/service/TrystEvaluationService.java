package com.wuai.company.order.service;

import java.util.List;

import com.wuai.company.order.entity.TrystEvaluation;
import com.wuai.company.util.Response;

/**
 * Created by zTerry on 2018/3/14.
 * 用户评价service
 */
public interface TrystEvaluationService {
	
	Response createEvaluation(Integer id,String targetUserId,Integer type,String serviceAttitude,String serviceLabel,
			String similarity,String similarityLabel,String trystId);
	
    
	Response selectEvaluationByTrystId(String trystId, Integer ownUserId);
	
	Response  deleteEvaluation(Integer id,String uuid);	
	
	
}
