package com.wuai.company.order.controller;

import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.entity.request.LongitudeAndLatitudeRequest;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.order.service.TrystOrdersService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.wuai.company.util.JwtToken.ID;

/**
 * Created by hyf on 2018/1/17.
 * 约吧 Controller
 */
@RestController
@RequestMapping("tryst")
public class TrystOrdersController {

    @Autowired
    private TrystOrdersService trystOrdersService;

    /**
     * 创建订单
     * @param request
     * @param trystOrdersRequest
     * @return
     * @throws Exception
     */
    @PostMapping("create/orders/auth")
    public Response createTrystOrders(HttpServletRequest request,TrystOrdersRequest trystOrdersRequest) throws Exception{
        return trystOrdersService.createTrystOrders((Integer)request.getAttribute(ID),trystOrdersRequest);
    }

    /**
     * 可抢单列表
     * @param req
     * @param pageNum   页码  ( 非必须，默认为1 ）
     * @return
     * @throws Exception
     */
    @PostMapping("snatchable/list/auth")
    public Response snatchableListAuth(HttpServletRequest req,
                                       @RequestParam(defaultValue = "1",required = false) Integer pageNum,
                                       @RequestParam(defaultValue = "120.094491",required = false) Double longitude,
                                       @RequestParam(defaultValue = "30.150197",required = false) Double latitude){
        return trystOrdersService.snatchableListAuth((Integer) req.getAttribute(ID), pageNum, longitude, latitude);
    }

    /**
     * 服务方抢单
     * @param req
     * @param uuid      订单id
     * @return
     * @throws Exception
     */
    @PostMapping("snatch/order/auth")
    public Response snatchOrder(HttpServletRequest req,String uuid) throws Exception{
        return trystOrdersService.snatchOrder((Integer) req.getAttribute(ID), uuid);
    }

    /**
     * 需求方显示已抢单人员列表
     * @param req
     * @param uuid      订单id
     * @return
     */
    @PostMapping("snatch/user/list/auth")
    public Response snatchUserList(HttpServletRequest req,String uuid){
        return trystOrdersService.snatchUserList((Integer) req.getAttribute(ID), uuid);
    }

    /**
     * 发单用户---选择用户---确认订单
     * @param req
     * @param uuid      订单id
     * @param userIds       选中用户们的id，用'，'隔开
     * @return
     */
    @PostMapping("sure/user/auth")
    public Response sureUser(HttpServletRequest req,String uuid,String userIds){
        return trystOrdersService.sureUser((Integer) req.getAttribute(ID), uuid,userIds);
    }

    /**
     * 余额支付
     * @param req
     * @param uuid
     * @param money
     * @return
     */
    @PostMapping("pay/money/auth")
    public Response payMoneyAuth(HttpServletRequest req, String uuid, String money, String password){
        return trystOrdersService.payMoneyAuth((Integer) req.getAttribute(ID),uuid.substring(1,uuid.length()), Double.valueOf(money),password);
    }

    /**
     * 订单生效后，在聊天室显示订单简要
     * @param req
     * @param uuid
     * @return
     */
    @PostMapping("room/tryst/auth")
    public Response roomTrystAuth(HttpServletRequest req, String uuid){
        return trystOrdersService.roomTrystAuth((Integer) req.getAttribute(ID), uuid);
    }

    /**
     * 订单详情
     * @param req
     * @param uuid      订单id
     * @return
     * @throws Exception
     */
    @PostMapping("tryst/auth")
    public Response findTrystOrders(HttpServletRequest req,String uuid) throws  Exception{
        return trystOrdersService.findTrystOrders((Integer) req.getAttribute(ID), uuid);
    }

    /**
     * 遍历出待消费后的订单
     * @param req
     * @return
     */
    @PostMapping("list/tryst/auth")
    public Response listTrystAuth(HttpServletRequest req, PageRequest pageRequest){
        return trystOrdersService.listTrystAuth((Integer) req.getAttribute(ID), pageRequest.getPageNum());
    }

    /**
     * 修改订单（只能修改时间，且修改次数为1）
     * @param req
     * @param uuid
     * @param time
     * @param duration
     * @return
     */
    @PostMapping("modify/tryst/auth")
    public Response modifyTrystAuth(HttpServletRequest req, String uuid, String time, Double duration){
        return trystOrdersService.modifyTrystAuth((Integer) req.getAttribute(ID), uuid, time, duration);
    }

    /**
     * 发单方确认订单
     * @param req
     * @param uuid
     * @return
     */
    @PostMapping("complete/demand/auth")
    public Response completeDemandAuth(HttpServletRequest req,String uuid){
        return trystOrdersService.completeDemandAuth((Integer) req.getAttribute(ID),uuid);
    }

    /**
     * 发单用户 取消订单
     * @param req
     * @param uuid      订单Id
     * @return
     */
    @PostMapping("cancel/tryst/auth")
    public Response cancelTrystAuth(HttpServletRequest req, String uuid, @RequestParam(defaultValue = "0", required = false)Integer passive, String reason){
        return trystOrdersService.cancelTrystAuth((Integer) req.getAttribute(ID),uuid,passive,reason);
    }

    /**
     * 赴约用户 取消订单
     * @param req
     * @param uuid      订单Id
     * @param reason        原因
     * @return
     */
    @PostMapping("cancel/user/auth")
    public Response cancelTrystUser(HttpServletRequest req, String uuid, Integer payed, @RequestParam(required = false)String reason){
        return trystOrdersService.cancelTrystUser((Integer) req.getAttribute(ID),uuid,payed,reason);
    }

    /**
     * 通知用户的(人数、优惠券)
     */
    @PostMapping("notice/size/auth")
    public Response noticeSize(HttpServletRequest req,String uuid){
        return trystOrdersService.noticeSize((Integer)req.getAttribute(ID),uuid);
    }

    /**
     * 约吧--- 已匹配到的用户
     */
    @PostMapping("choice/user/auth")
    public Response choiceSnatchUser(HttpServletRequest req, String uuid) throws Exception{
        return trystOrdersService.choiceSnatchUser((Integer) req.getAttribute(ID),uuid);
    }


    /**
     * 我的订单
     */
    @PostMapping("my/orders/auth")
    public Response myTrystOrders(HttpServletRequest req,Integer pageNum){
        return trystOrdersService.myTrystOrders((Integer) req.getAttribute(ID),pageNum);
    }

    /**
     * 抢单页面遍历
     * @param req
     * @param longitude     经度
     * @param latitude      纬度
     * @return
     */
    @PostMapping("grab/orders/auth")
    public Response grabOrders(HttpServletRequest req, Double longitude, Double latitude){
        return trystOrdersService.grabOrders((Integer) req.getAttribute(ID), longitude ,latitude);
    }
}
