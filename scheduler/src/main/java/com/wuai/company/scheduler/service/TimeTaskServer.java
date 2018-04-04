package com.wuai.company.scheduler.service;


import com.wuai.company.entity.TimeTask;

import java.util.List;

/**
 * 定时任务的server
 */
public interface TimeTaskServer {
    List<TimeTask> findTimeTaskAll();

    void saveTimeTask(TimeTask task);

    void updateTimeTask(TimeTask task);

    void deleteTimeTask(TimeTask task);

    void autoDelete(String timeTaskName);
}
