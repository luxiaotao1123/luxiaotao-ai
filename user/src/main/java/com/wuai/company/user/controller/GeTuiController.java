package com.wuai.company.user.controller;

import com.wuai.company.user.service.GeTuiService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.wuai.company.util.JwtToken.ID;

@RestController
@RequestMapping("user/")
public class GeTuiController {

    @Autowired
    GeTuiService geTuiService;

    /**
     * 创建订单后推送消息给各用户
     * @param request
     * @param trystId   订单Id
     * @return
     */
    @PostMapping("create/send/getui/auth")
    public Response createSendGetuiAuth(HttpServletRequest request, String trystId){
        return geTuiService.createSendGetuiAuth((Integer) request.getAttribute(ID),trystId);
    }

    /**
     * 需求方未付款时，取消订单，个推到各个已抢单的用户
     * @param request
     * @param trystId   订单Id
     * @return
     */
    @PostMapping("cancel/create/getui/auth")
    public Response cancelCreateGetuiAuth(HttpServletRequest request, String trystId){
         return geTuiService.cancelCreateGetuiAuth((Integer) request.getAttribute(ID),trystId);
    }

    /**
     * 需求方付款后，通知用户等待约会开始
     * @param request
     * @param trystId
     * @return
     */
    @PostMapping("wait/tryst/getui/auth")
    public Response waitTrystGetuiAuth(HttpServletRequest request, String trystId){
        return geTuiService.waitTrystGetuiAuth((Integer) request.getAttribute(ID),trystId);
    }

    /**
     * 个推给赴约方（订单已完成）
     * @param request
     * @return
     */
    @PostMapping("complete/tryst/getui/auth")
    public Response completeTrystGetuiAuth(HttpServletRequest request, String trystId){
        return geTuiService.completeTrystGetuiAuth((Integer) request.getAttribute(ID),trystId);
    }

    /**
     * 赴约方取消订单，通知发单方
     * @param request
     * @param trystId
     * @return
     */
    @PostMapping("cancel/service/getui/auth")
    public Response cancelUserGetuiAuth(HttpServletRequest request, String trystId){
        return geTuiService.cancelUserGetuiAuth((Integer) request.getAttribute(ID),trystId);
    }

    /**
     *需求方取消订单，个推给所有赴约方,订单已取消
     * @param request
     * @param trystId
     * @return
     */
    @PostMapping("cancel/demand/getui/auth")
    public Response cancelDemandGetuiAuth(HttpServletRequest request, String trystId){
        return geTuiService.cancelDemandGetuiAuth((Integer) request.getAttribute(ID),trystId);
    }

    /**
     * 个推给所有赴约方，订单时间已修改
     * @param request
     * @param trystId
     * @return
     */
    @PostMapping("update/tryst/getui/auth")
    public Response updateTrystGetuiAuth(HttpServletRequest request, String trystId){
        return geTuiService.updateTrystGetuiAuth((Integer) request.getAttribute(ID),trystId);
    }

}
