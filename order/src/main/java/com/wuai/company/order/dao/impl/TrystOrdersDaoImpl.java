package com.wuai.company.order.dao.impl;

import com.wuai.company.entity.*;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.Response.UserVideoResponse;
import com.wuai.company.entity.request.TrystOrdersRequest;
import com.wuai.company.order.dao.TrystOrdersDao;
import com.wuai.company.order.entity.DistancePo;
import com.wuai.company.order.entity.TrystScenes;
import com.wuai.company.order.mapper.TrystMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by hyf on 2018/1/17.
 */
@Repository
public class TrystOrdersDaoImpl implements TrystOrdersDao {
    @Resource
    private TrystMapper trystMapper;
    @Override
    public List<TrystScenes> trystScenes(Integer code) {
        return trystMapper.trystScenes(code);
    }

    @Override
    public void createTrystOrders(Integer id, String uuid, TrystOrdersRequest trystOrdersRequest,Integer code,String value) {
        trystMapper.createTrystOrders(id,uuid,trystOrdersRequest,code,value);
    }

    @Override
    public NearBodyResponse findNearBody(Integer id) {
        return trystMapper.findNearBody(id);
    }

    @Override
    public List<UserVideoResponse> findUserVides(Integer id) {
        return trystMapper.findUserVides(id);
    }

    @Override
    public User findUserById(Integer userId) {
        return trystMapper.findUserById(userId);
    }

    @Override
    public TrystOrders findTrystOrdersByUid(String trystId) {
        return trystMapper.findTrystOrdersByUid(trystId);
    }

    @Override
    public List<TrystOrders> findTrystOrdersByUserId(Integer userId){
        return trystMapper.findTrystOrdersByUserId(userId);
    }

    @Override
    public List<TrystOrders> findTrystOrdersList(Integer gender, Integer payCode) {
        return trystMapper.findTrystOrdersList(gender,payCode);
    }

    @Override
    public void addTrystOrdersSnatch(Integer id, String uuid, String trystId) {
        trystMapper.addTrystOrdersSnatch(id,uuid,trystId);
    }

    @Override
    public Integer findOrdersSnatchSize(String trystId) {
        return trystMapper.findOrdersSnatchSize(trystId);
    }

    @Override
    public Integer findTrystSnatch(Integer id, String uuid) {
        return trystMapper.findTrystSnatch(id,uuid);
    }

    @Override
    public List<TrystOrders> findTrystOrdersListById(Integer id) {
        return trystMapper.findTrystOrdersListById(id);
    }

    @Override
    public UserVideoResponse findUserVideoById(Integer id) {
        return trystMapper.findUserVideoById(id);
    }

    @Override
    public List<Coupons> findMyCoupons(Integer id) {
        return trystMapper.findMyCoupons(id);
    }

    @Override
    public void addTrystOrdersReceive(String uuid,String trystId,  Integer userId, Double money, Boolean deleted) {
        trystMapper.addTrystOrdersReceive(uuid,trystId,userId,money,deleted);
    }

    @Override
    public List<VideoHome> findVideoHome(Integer code) {
        return trystMapper.findVideoHome(code);
    }

    @Override
    public int calcelTrystOrders(String uuid, int code, String value) {
        return trystMapper.calcelTrystOrders(uuid,code,value);
    }

    @Override
    public void upMoney(Integer id, Double money) {
        trystMapper.upMoney(id,money);
    }

    @Override
    public List<TrystCancel> findCancelTryst(Integer id, Integer type, String today) {
        return trystMapper.findCancelTryst(id,type,today);
    }

    @Override
    public void cancelTryst(String uid,Integer id, String uuid, Integer type, Integer code, String value, String today, String date, Integer time, String reason) {
        trystMapper.cancelTryst(uid,id,uuid,today,date,value,code,time,type,reason);
    }

    @Override
    public TrystCancel findCancelTrystFir(Integer id, Integer type, Integer code) {
        return trystMapper.findCancelTrystFir(id,type,code);
    }

    @Override
    public void upTrystPersonCount(String uuid, int personCount) {
        trystMapper.upTrystPersonCount(uuid,personCount);
    }

    @Override
    public List<TrystReceive> findTrystReceive(String uuid) {
        return trystMapper.findTrystReceive(uuid);
    }

    @Override
    public TrystOrders selectTrystOrders(TrystOrders trystOrders) {
        return trystMapper.selectTrystOrders(trystOrders);
    }

    @Override
    public List<DistancePo> selectDistanceAndIdByAsc(Map map) {
        return trystMapper.selectDistanceAndIdByAsc(map);
    }

    @Override
    public int upTrystOrdersPay(String trystId, Integer code, String value) {
        return trystMapper.upTrystOrdersPay(trystId,code,value);
    }

    @Override
    public int delectReceiveByUserId(Integer userId, String trystId){
        return trystMapper.delectReceiveByUserId(userId, trystId);
    }

    @Override
    public int logicDelectReceiveByUserId(Integer userId, String trystId) {
        return trystMapper.logicDelectReceiveByUserId(userId,trystId);
    }

    @Override
    public int reduceTrystPersonCount(String trystId){
        return trystMapper.reduceTrystPersonCount(trystId);
    }

    @Override
    public int addTimeByCancelTryst(String uuid, String trystId, String reason){
        return trystMapper.addTimeByCancelTryst(uuid,trystId,reason);
    }

    @Override
    public List<TrystOrders> selectTrystOrderByReceiveUserId(Integer userId, Integer pageNum){
        return trystMapper.selectTrystOrderByReceiveUserId(userId, pageNum);
    }

    @Override
    public int updateTimeAndDurByTrystId(String trystId, String time, Double duration,Integer showName) {
        return trystMapper.updateTimeAndDurAndShowByTrystId(trystId,time,duration,showName);
    }

    @Override
    public int selectCancelCountByService(Integer userId) {
        return trystMapper.selectCancelCountByService(userId);
    }

    @Override
    public List<TrystOrders> selectTrystOrdersByReceivAndUserId(Integer userId) {
        return trystMapper.selectTrystOrdersByReceivAndUserId(userId);
    }

    @Override
    public List<Integer> selectUsersByCancel(String trystId) {
        return trystMapper.selectUsersByCancel(trystId);
    }

    @Override
    public List<TrystReceive> selectReceiveByTryst(String trystId) {
        return trystMapper.selectReceiveByTryst(trystId);
    }
}
