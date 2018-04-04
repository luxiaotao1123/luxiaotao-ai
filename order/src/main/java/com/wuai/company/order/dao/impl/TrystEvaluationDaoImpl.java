package com.wuai.company.order.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wuai.company.order.dao.TrystEvaluationDao;
import com.wuai.company.order.entity.TrystEvaluation;
import com.wuai.company.order.mapper.TrystEvaluationMapper;

import java.util.List;

/**
 * Created by zTerry on 2018/3/14.
 * 用户评价dao层实现类
 */
@Repository
public class TrystEvaluationDaoImpl implements TrystEvaluationDao{
   
	@Autowired
	private TrystEvaluationMapper trystEvaluationMapper;

	@Override
	public int insertSelective(TrystEvaluation record) {
		return trystEvaluationMapper.insertSelective(record);
	}

	@Override
	public List<TrystEvaluation> selectEvaluationByTrystId(String trystId, Integer targetUserId) {
		return trystEvaluationMapper.selectEvaluationByTrystId(trystId,targetUserId);
	}

	@Override
	public List<TrystEvaluation> selectEvaluationByTrystIdAndOwn(String trystId, Integer ownUserId) {
		return trystEvaluationMapper.selectEvaluationByTrystIdAndOwn(trystId, ownUserId);
	}

	@Override
	public List<TrystEvaluation> selectEvaluationByTrystIdAndOwnAndTarget(String trystId, Integer ownUserId, Integer targetUserId) {
		return trystEvaluationMapper.selectEvaluationByTrystIdAndOwnAndTarget(trystId,ownUserId,targetUserId);
	}

	@Override
	public int updateByPrimaryKeySelective(TrystEvaluation record) {
		return trystEvaluationMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeySelectiveByUuid(TrystEvaluation record) {
		return trystEvaluationMapper.updateByPrimaryKeySelectiveByUuid(record);
	}

	@Override
	public TrystEvaluation selectByUuid(String uuid) {
		return trystEvaluationMapper.selectByUuid(uuid);
	}


}
