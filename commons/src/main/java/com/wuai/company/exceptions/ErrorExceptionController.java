package com.wuai.company.exceptions;


import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/error")
public class ErrorExceptionController implements ErrorController {


    @Override
    public String getErrorPath() {
        return null;
    }
    @RequestMapping("/400")
    public String error400() {
        return "400";
    }


    @RequestMapping("/404")
    public String error404() {
        return "404";
    }

    @RequestMapping("/500")
    public String error500() {
        return "500";
    }


}
