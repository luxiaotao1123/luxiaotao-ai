package com.wuai.company.order.controller;

import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.order.service.TrystMsgService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.wuai.company.util.JwtToken.ID;

@RestController
@RequestMapping("tryst")
public class TrystMsgController {

    @Autowired
    private TrystMsgService trystMsgService;

    /**
     * 系统消息
     * @param req
     * @param pageNum
     * @return
     */
    @PostMapping("msg/detail/auth")
    public Response msgDetailAuth(HttpServletRequest req,PageRequest pageNum){
        return trystMsgService.msgDetailAuth((Integer) req.getAttribute(ID), pageNum.getPageNum());
    }

    /**
     * 获取系统消息数量（用于比较）
     * @param req
     * @return
     */
    @PostMapping("msg/count/auth")
    public Response msgCountAuth(HttpServletRequest req){
        return trystMsgService.msgCountAuth((Integer) req.getAttribute(ID));
    }

    /**
     * 获取groupId，（通过订单id）
     * @param req
     * @param trystId
     * @return
     */
    @PostMapping("easemob/tryst/auth")
    public Response getGroupIdByTrystIdAuth(HttpServletRequest req, String trystId){
        return trystMsgService.getGroupIdByTrystIdAuth((Integer) req.getAttribute(ID),trystId);
    }

}
