package com.wuai.company.marking.controller;


import com.wuai.company.marking.service.MarkingService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.wuai.company.util.JwtToken.ID;

/**
 * Created by Administrator on 2017/6/14.
 */
@RestController
@RequestMapping("marking")
public class MarkingController {

    @Autowired
    private MarkingService markingService;

    @PostMapping("show/auth")
    public Response markingShow(HttpServletRequest request,Integer userId){
        return null;
    }


    @PostMapping("appraise/auth")
    public Response appraise(HttpServletRequest request){
        String appraiseRequest = request.getParameter("appraise");
        return markingService.addAppraise((Integer) request.getAttribute(ID), appraiseRequest);
    }


}
