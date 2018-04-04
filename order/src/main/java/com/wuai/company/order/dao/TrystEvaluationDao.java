package com.wuai.company.order.dao;

import com.wuai.company.order.entity.TrystEvaluation;

import java.util.List;

/**
 * Created by zTerry on 2018/3/14.
 * 用户评价dao层
 */
public interface TrystEvaluationDao {
   
	int insertSelective(TrystEvaluation record);

	List<TrystEvaluation> selectEvaluationByTrystId(String trystId, Integer targetUserId);

	List<TrystEvaluation> selectEvaluationByTrystIdAndOwn(String trystId, Integer ownUserId);

	List<TrystEvaluation> selectEvaluationByTrystIdAndOwnAndTarget(String trystId, Integer ownUserId, Integer targetUserId);
	
	int updateByPrimaryKeySelective(TrystEvaluation record);
	
	int updateByPrimaryKeySelectiveByUuid(TrystEvaluation record);
	
	TrystEvaluation selectByUuid(String uuid);
} 
