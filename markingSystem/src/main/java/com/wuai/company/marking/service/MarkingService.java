package com.wuai.company.marking.service;


import com.wuai.company.util.Response;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface MarkingService {

    Response addAppraise(Integer attribute, String appraiseRequest);
}
