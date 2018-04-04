package com.wuai.company.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;

/**
 * Created by hyf on 2017/11/2.
 */
@Controller
@RequestMapping("apk")
public class ApkDownloadController extends HttpServlet {
    @RequestMapping("down")
    public String downloads(){
        return "/download.html";
    }


}
