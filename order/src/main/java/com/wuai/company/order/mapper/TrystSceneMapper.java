package com.wuai.company.order.mapper;

import com.wuai.company.order.entity.TrystScene;
import com.wuai.company.order.entity.TrystSceneExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TrystSceneMapper {
    long countByExample(TrystSceneExample example);

    int deleteByExample(TrystSceneExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TrystScene record);

    int insertSelective(TrystScene record);

    List<TrystScene> selectByExample(TrystSceneExample example);

    TrystScene selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TrystScene record, @Param("example") TrystSceneExample example);

    int updateByExample(@Param("record") TrystScene record, @Param("example") TrystSceneExample example);

    int updateByPrimaryKeySelective(TrystScene record);

    int updateByPrimaryKey(TrystScene record);
}