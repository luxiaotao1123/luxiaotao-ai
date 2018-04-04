package com.wuai.company.order.controller;

import com.wuai.company.order.service.TrystPayService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.wuai.company.util.JwtToken.ID;

@RestController
@RequestMapping("tryst")
public class TrystPayController {

    @Autowired
    TrystPayService trystPayService;

    /**
     * 拿到Ali交互的订单ID
     * @param trystId   我方订单id +“a”
     * @param request
     * @return
     */
    @PostMapping("task/pay/auth")
    public Response taskPay(@RequestParam String trystId, HttpServletRequest request){
        return trystPayService.taskPay(trystId, (Integer) request.getAttribute(ID));
    }


}
