package com.wuai.company.exceptions;

import com.wuai.company.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常
 * Created by Ness on 2017/5/25.
 */
@ResponseBody
@RestControllerAdvice
public class GlobalExceptionsController {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionsController.class);


    @ExceptionHandler(Exception.class)
    public Response recordGlobal(Exception e) {
        logger.warn("全局异常", e);
        return Response.errorByArray();
    }

}
