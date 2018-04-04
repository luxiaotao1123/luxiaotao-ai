package com.wuai.company.order.controller;


import com.wuai.company.order.service.TrystOrdersService;
import com.wuai.company.order.service.TrystSceneService;
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
public class TrystSceneController {

    @Autowired
    private TrystSceneService trystSceneService;

    /**
     * 获取Home主页场景
     * @param request
     * @return
     */
    @PostMapping("home/scenes/auth")
    public Response homeTrystScenes(HttpServletRequest request){
        return trystSceneService.homeTrystScenes((Integer)request.getAttribute(ID));
    }

    /**
     * 获取更多场景
     * @param request
     * @return
     */
    @PostMapping("more/scenes/auth")
    public Response moreTrystScenes(HttpServletRequest request){
        return trystSceneService.moreTrystScenes((Integer)request.getAttribute(ID));
    }

    /**
     * 根据场景id获取详情
     * @param uuid  场景id
     * @param request
     * @return
     */
    @PostMapping("scene/auth")
    public Response getSceneDetails(@RequestParam String uuid, HttpServletRequest request){
        return trystSceneService.getSceneDetails((Integer)request.getAttribute(ID),uuid);
    }
}
