package com.wuai.company.marking.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * Created by Administrator on 2017/6/14.
 */
@Mapper
public interface MarkingMapper {


    void addAppraise(@Param("userId") Integer userId, @Param("id")Integer id, @Param("uuid")String uuid,@Param("ordersId") String ordersId,
                     @Param("grade")Integer grade, @Param("content")String content, @Param("type")Integer type);
}
