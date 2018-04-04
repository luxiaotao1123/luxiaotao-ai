package com.wuai.company.order.controller;

import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.order.service.TrystNearBodyService;
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
public class TrystNearBodyController {

    @Autowired
    private TrystNearBodyService trystNearBodyService;

    /**
     * 打开app添加经纬度---心跳添加经纬度
     * @param req
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("nearby/body/add/auth")
    public Response addNearbyBody(HttpServletRequest req,
                                  @RequestParam(required = false) Double longitude,
                                  @RequestParam(required = false) Double latitude,
                                  String cid) throws IllegalAccessException {
        return trystNearBodyService.addNearbyBody((Integer) req.getAttribute(ID), latitude, longitude, cid);
    }


}
