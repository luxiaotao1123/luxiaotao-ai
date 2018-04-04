package com.wuai.company.marking.dao;


/**
 * Created by Administrator on 2017/6/14.
 */
public interface MarkingDao {

    void addAppraise(Integer userId, Integer id, String uuid, String ordersId, Integer grade, String content, Integer key);
}
