package com.wuai.company.scheduler.dao;


import com.wuai.company.entity.TimeTask;

import java.util.List;


/**
 * 定时任务的dao
 */
public interface TimeTaskDao {
    List<TimeTask> findTimeTaskAll();

    void saveTimeTask(TimeTask task);

    void updateTimeTask(TimeTask task);

    void deleteTimeTask(TimeTask task);

}
