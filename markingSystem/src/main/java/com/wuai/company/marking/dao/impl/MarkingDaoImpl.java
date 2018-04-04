package com.wuai.company.marking.dao.impl;


import com.wuai.company.marking.dao.MarkingDao;
import com.wuai.company.marking.mapper.MarkingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/6/14.
 */
@Repository

public class MarkingDaoImpl implements MarkingDao {
    @Autowired
    private MarkingMapper markingMapper;
    @Override
    public void addAppraise(Integer userId, Integer id, String uuid, String ordersId, Integer grade, String content, Integer key) {
        markingMapper.addAppraise( userId,  id,  uuid,  ordersId,  grade,  content,  key);
    }
}
