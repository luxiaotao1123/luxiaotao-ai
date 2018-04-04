package com.wuai.company.scheduler.mapper;


import com.wuai.company.entity.TimeTask;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单信息的mapper层
 * Created by Ness on 2017/5/25.
 */
@Mapper
public interface TimeTaskMapper {


    /**
     * 查找所有的任务列表
     *
     * @return
     */
    List<TimeTask> findTimeTaskAll();


    /**
     * 保存任务
     *
     * @param timeTask
     */
    void saveTimeTask(TimeTask timeTask);


    /**
     * 修改任务
     *
     * @param timeTask
     */
    void updateTimeTask(TimeTask timeTask);


}
