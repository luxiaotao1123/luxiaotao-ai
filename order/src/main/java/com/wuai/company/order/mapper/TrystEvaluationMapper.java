package com.wuai.company.order.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wuai.company.order.entity.TrystEvaluation;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zTerry on 2018/3/14.
 * 用户评价
 */
@Mapper
public interface TrystEvaluationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrystEvaluation record);

    int insertSelective(TrystEvaluation record);

    TrystEvaluation selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrystEvaluation record);

    int updateByPrimaryKey(TrystEvaluation record);

    List<TrystEvaluation> selectEvaluationByTrystId(@Param("trystId")String trystId, @Param("targetUserId")Integer targetUserId);

    List<TrystEvaluation> selectEvaluationByTrystIdAndOwn(@Param("trystId")String trystId, @Param("ownUserId")Integer ownUserId);

    List<TrystEvaluation> selectEvaluationByTrystIdAndOwnAndTarget(@Param("trystId")String trystId, @Param("ownUserId")Integer ownUserId,  @Param("targetUserId")Integer targetUserId);
    
    int updateByPrimaryKeySelectiveByUuid(TrystEvaluation record);
    
    TrystEvaluation selectByUuid(String uuid);
}