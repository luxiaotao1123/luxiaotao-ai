package com.wuai.company.scheduler.service.impl;



import com.wuai.company.scheduler.dao.TimeTaskDao;
import com.wuai.company.entity.TimeTask;
import com.wuai.company.scheduler.service.TimeTaskServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 任务时刻的serviceImpl
 */
@Transactional
@Service
public class TimeTaskServerImpl implements TimeTaskServer {

    private Logger logger = LoggerFactory.getLogger(TimeTaskServerImpl.class);

    @Autowired
    private TimeTaskDao timeTaskDao;



    @Override
    public List<TimeTask> findTimeTaskAll() {

        return timeTaskDao.findTimeTaskAll();
    }

    @Override
    public void saveTimeTask(TimeTask task) {
        timeTaskDao.saveTimeTask(task);
    }

    @Override
    public void updateTimeTask(TimeTask task) {
        timeTaskDao.updateTimeTask(task);
    }


    @Override
    public void deleteTimeTask(TimeTask task) {
        timeTaskDao.deleteTimeTask(task);
    }

    @Override
    public void autoDelete(String timeTaskName) {
        TimeTask task = new TimeTask();
        task.setTimeTaskName(timeTaskName);
        task.setDeleted(Boolean.TRUE);
        timeTaskDao.deleteTimeTask(task);
        logger.warn("定时任务timeTaskName = {}已从持久层清除！",timeTaskName);
    }


}
