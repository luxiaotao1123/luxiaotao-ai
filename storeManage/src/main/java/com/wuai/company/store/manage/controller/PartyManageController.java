package com.wuai.company.store.manage.controller;

import com.wuai.company.entity.Response.PageRequest;
import com.wuai.company.store.manage.entity.request.PartyUpRequest;
import com.wuai.company.store.manage.service.PartyManageService;
import com.wuai.company.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hyf on 2017/12/19.
 */
@RestController
@RequestMapping("manage/party")
public class PartyManageController {
    @Autowired
    private PartyManageService partyManageService;

    /**
     * 修改添加 party
     * @param name
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("up/party/load/{name}")
    public Response upParty(@PathVariable("name") String name,PartyUpRequest request) throws Exception {
        return partyManageService.upParty(name,request);
    }

    /**
     * 删除party
     * @param name
     * @param uuid
     * @return
     * @throws Exception
     */
    @RequestMapping("del/party/load/{name}")
    public Response delParty(@PathVariable("name") String name,String uuid) throws Exception {
        return partyManageService.delParty(name,uuid);
    }
    /**
     * 获取party分类
     * @return
     * @throws Exception
     */
    @RequestMapping("classify")
    public Response classify() throws Exception {
        return partyManageService.classify();
    }

    /**
     * 查询该商家所有发布的party订单
     * @param name
     * @param pageRequest
     * @return
     * @throws Exception
     */
    @RequestMapping("party/all/load/{name}")
    public Response party(@PathVariable("name") String name,PageRequest pageRequest,String value)throws Exception{
        return partyManageService.findPartyAll(name,pageRequest.getPageNum(),value);
    }

    /**
     * 商家个人信息
     * @param name
     * @return
     */
    @RequestMapping("information/load/{name}")
    public Response informationParty(@PathVariable("name")String name){
        return partyManageService.informationParty(name);
    }

    /**
     * 展示该pary下所有留言
     */
    @RequestMapping("messages/load/{name}")
    public Response messages(@PathVariable String name,String uuid, Integer pageNum){
        return partyManageService.messages(name,uuid, pageNum);
    }

}
