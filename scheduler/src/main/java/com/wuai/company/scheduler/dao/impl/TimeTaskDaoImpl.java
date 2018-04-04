package com.wuai.company.scheduler.dao.impl;


import com.wuai.company.scheduler.dao.TimeTaskDao;
import com.wuai.company.entity.TimeTask;
import com.wuai.company.scheduler.mapper.TimeTaskMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 任务时刻dao
 */
@Repository
public class TimeTaskDaoImpl implements TimeTaskDao {
    @Autowired
    private TimeTaskMapper timeTaskMapper;

    private Logger logger = LoggerFactory.getLogger(TimeTaskDaoImpl.class);

    @Override
    public List<TimeTask> findTimeTaskAll() {
        logger.info("开始查询所有的任务的列表");
        return timeTaskMapper.findTimeTaskAll();
    }

    @Override
    public void saveTimeTask(TimeTask task) {
        if (task == null || StringUtils.isEmpty(task.getTimeTaskName())) {
            logger.info("开始保存任务列表参数为空");
            return;
        }
        logger.info("开始保存任务列表={}", task.getTimeTaskName());
        timeTaskMapper.saveTimeTask(task);
    }

    @Override
    public void updateTimeTask(TimeTask task) {
        if (task == null || StringUtils.isEmpty(task.getTimeTaskName())) {
            logger.info("开始修改任务列表参数为空");
            return;
        }
        logger.info("开始修改任务列表={}", task.getTimeTaskName());
        timeTaskMapper.updateTimeTask(task);
    }


    @Override
    public void deleteTimeTask(TimeTask task) {
        if (task == null || StringUtils.isEmpty(task.getTimeTaskName())) {
            logger.info("开始删除任务列表参数为空");
            return;
        }
        logger.info("开始删除任务列表={}", task.getTimeTaskName());
        task.setDeleted(Boolean.TRUE);
        timeTaskMapper.updateTimeTask(task);
    }
}
