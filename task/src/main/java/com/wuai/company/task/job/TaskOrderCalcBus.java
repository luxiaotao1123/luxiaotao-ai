package com.wuai.company.task.job;

import com.wuai.company.entity.Orders;
import com.wuai.company.enums.PayTypeEnum;
import com.wuai.company.util.Arith;
import com.wuai.company.util.DistanceTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订单总处理bus
 * <p>
 * 使用redis作为数据库,redis的数据结构示意图
 * <p>
 * *******************----------------------总的订单记录--1----------------*******************************
 * 数据结构 ：选择hash
 * 表结构 ： 特定城市的总的订单key(特定城市_USER_TOTAL_SCENE_ORDER_KEY)   _____   订单的Id(防止重复) _____  订单的详细信息
 * 特别说明 ：关于 特定城市_USER_TOTAL_SCENE_ORDER_KEY  中SCENE有两个 一个是服务方 一个是需求方 所有总的task在特定城市下有两种表
 * <p>
 * *******************----------------------总的订单记录------------------*******************************
 * =========================================我是分割线===================================================
 * <p>
 * *******************----------------------总的成功匹配订单记录----2--------------*******************************
 * 数据结构 ：选择hash
 * 表结构 : 特定城市的总的匹配的订单key(特定城市_ORDERS_SUCCESS_MATCH_KEY) _____  匹配成功用户的Id()  _____  匹配成功用户的订单详细信息
 * <p>
 * *******************----------------------总的成功匹配订单记录------------------*******************************
 * =========================================我是分割线===================================================
 * <p>
 * *******************----------------------用户成功匹配订单总记录----3--------------*******************************
 * 数据结构 ：选择hash
 * 表结构 :  _____  匹配成功用户的Id(特定城市_USER_MATCHES_ORDERS_PREFIX_用户Id)  _____  具体的场景+":"+具体的形式(需求方或服务方) _____  所有匹配成功的订单数组
 * <p>
 * ******************----------------------用户成功匹配订单总记录------------------*******************************
 * <p>
 * ========================================我是分割线===================================================
 * <p>
 * *******************----------------------订单匹配用户总记录---------4---------*******************************
 * 数据结构 ：选择hash
 * 表结构 :  _____  特定城市_ORDERS_MATCHES_USER_PREFIX_订单Id)  _____ 匹配用户Id() _____  匹配该用户的订单的详细信息
 * <p>
 * *******************----------------------订单匹配总用户记录------------------*******************************
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * </p>
 */
@Component
public class TaskOrderCalcBus {
    @Resource
    private HashOperations<String, String, Orders> taskHashRedisTemplate;
    @Resource
    private HashOperations<String, String, Orders[]> taskUserArrayRedisTemplate;

    @Resource
    private RedisTemplate removeRedisTemplate;

    private final String CITYNAME_USER_TOTAL_SCENE_ORDERS_KEY = "%s:user:total:scene:%s:orders:key";
    private final String CITYNAME_ORDERS_SUCCESS_MATCH_KEY = "%s:orders:success:match:key";
    private final String CITYNAME_USER_MATCHES_ORDERS_PREFIX = "%s:user:matches:orders:%s";
    private final String CITYNAME_ORDERS_MATCHES_USER_PREFIX = "%s:orders:matches:user:%s";

    private Logger logger = LoggerFactory.getLogger(TaskOrderCalcBus.class);

    private final double DEFAULT_DISTANCE = 3000;

    private final double ERROR = 0;

    private final int DEFAULT_MATCHING_COUNT = 10;


    /**
     * 添加订单
     *
     * @param orders 订单的详细信息
     * @return
     */
    public boolean addOrders(Orders orders) {

        boolean _addSuccessFLag = false;

        if (checkIllegalOrders(orders, OperateTypeEnum.ADD_ORDERS)) {
            return _addSuccessFLag;
        }


        //更新添加成功标识
        _addSuccessFLag = true;

        return _addSuccessFLag;


    }


    /**
     * 更新订单
     *
     * @param orders 待更新的订单的详细信息
     * @return
     */
    public boolean updateOrders(Orders orders) {

        boolean _updateSuccessFLag = false;

        if (checkIllegalOrders(orders, OperateTypeEnum.UPDATE_ORDERS)) {
            return _updateSuccessFLag;
        }


        //更新修改成功标识
        _updateSuccessFLag = true;


        return _updateSuccessFLag;
    }

    /**
     * 删除订单
     *
     * @param orders 待删除的订单的详细信息
     * @return
     */
    public boolean deleteOrders(Orders orders) {

        boolean _deleteSuccessFLag = false;

        if (checkIllegalOrders(orders, OperateTypeEnum.DELETE_ORDERS)) {
            return _deleteSuccessFLag;
        }


        //更新删除成功标识
        _deleteSuccessFLag = true;


        return _deleteSuccessFLag;
    }


    /**
     * 获取用户订单信息
     *
     * @param orders
     * @return
     */
    public Orders[] getOrders(Orders orders) {

        if (checkIllegalOrders(orders, OperateTypeEnum.QUERY_ORDERS)) {
            return new Orders[0];
        }
        logger.info("开始获取用户={}订单={}的匹配信息", orders.getUid(), orders.getUuid());

        //取出用户订单的匹配结果   数据结构==>   匹配成功用户的Id(特定城市_USER_MATCHES_ORDERS_PREFIX_用户Id)  _____  具体的场景+":"+具体的形式(需求方或服务方) _____  所有匹配成功的订单数组
        return objArr2OrderArr(taskUserArrayRedisTemplate.get(String.format(CITYNAME_USER_MATCHES_ORDERS_PREFIX, orders.getOpenLocalTypeEnum().toSimpleName(), orders.getUid()), orders.getPublishType()));
    }

    /**
     * 根据obj数组转成array数组
     *
     * @param ordersArray 需要转换的数据
     * @return
     */
    private Orders[] objArr2OrderArr(Object[] ordersArray) {
        //保存排序的集合  根据匹配率 从小到达依次排序
        Set<Orders> result = getCompareSet();

        if (ordersArray != null) {
            int len = ordersArray.length;
            Orders order;
            for (int i = 0; i < len; i++) {
                order = (Orders) ordersArray[i];
                if (order == null || order.isEmpty()) {
                    continue;
                }
                result.add(order);

            }
        }
        return result.toArray(new Orders[result.size()]);
    }

    /**
     * 操作订单的枚举
     */
    private enum OperateTypeEnum {
        ADD_ORDERS("[添加]"), UPDATE_ORDERS("[修改]"), DELETE_ORDERS("[删除]"), QUERY_ORDERS("[查询]");

        private String operate;

        OperateTypeEnum(String operate) {
            this.operate = operate;
        }

        public String toOperate() {
            return operate;
        }

    }


    /**
     * 获得有序的set去重集合
     *
     * @return
     */
    private Set<Orders> getCompareSet() {
        //保存排序的集合  根据匹配率 从小到达依次排序
        return new TreeSet<>(
                (o1, o2) -> {
                    if (o1.getUuid().equals(o2.getUuid())) {
                        return 0;
                    } else {
                        return (o1.getMatchRate() < o2.getMatchRate() ? 1 : -1);
                    }
                }
        );
    }

    /**
     * 检测非法订单的信息
     *
     * @param orders          带检测订单
     * @param operateTypeEnum 订单操作类型
     * @return
     */
    private boolean checkIllegalOrders(Orders orders, OperateTypeEnum operateTypeEnum) {

        if (operateTypeEnum == null || orders == null || orders.isEmpty() || orders.getOpenLocalTypeEnum() == null) {
            logger.warn("用户所的{}订单不符合规则 请仔细检查", operateTypeEnum == null ? "{订单类型为空}" : operateTypeEnum.toOperate());
            return true;
        }

        switch (operateTypeEnum) {
            case DELETE_ORDERS:
                //删除订单
                removeOrders(orders);
                break;
            case QUERY_ORDERS:
                break;
            default:
                //添加到总的订单中
                addTotalUserOrders(orders);
                //影响到该地区下的其他的订单
                effectSameAreaMatchedOrders(orders);
                //计算自己的订单
                sortOrders(orders);

        }

        return false;


    }


    /**
     * 添加到总的订单中
     *
     * @param orders 待添加的订单信息
     */
    private void addTotalUserOrders(Orders orders) {
        //添加到总的订单中 数据格式==>特定城市的总的订单key(特定城市_USER_TOTAL_SCENE_ORDER_KEY)   _____   订单的Id(防止重复) _____  订单的详细信息
        taskHashRedisTemplate.put(String.format(CITYNAME_USER_TOTAL_SCENE_ORDERS_KEY, orders.getOpenLocalTypeEnum().toSimpleName(), orders.getPublishType()), orders.getUuid(), orders);

    }


    /**
     * 根据订单信息移除 他在总订单的记录
     *
     * @param orders 待移除的订单信息
     */
    private void removeTotalUserOrdersByOrders(Orders orders) {

        //移除到总的订单中 数据格式==>特定城市的总的订单key(特定城市_USER_TOTAL_SCENE_ORDER_KEY)   _____   订单的Id(防止重复) _____  订单的详细信息
        taskHashRedisTemplate.delete(String.format(CITYNAME_USER_TOTAL_SCENE_ORDERS_KEY, orders.getOpenLocalTypeEnum().toSimpleName(), orders.getPublishType()), orders.getUuid());

    }


    /**
     * 影响相同地区的已经匹配订单信息
     *
     * @param compare 需要影响的订单信息
     */
    private void effectSameAreaMatchedOrders(Orders compare) {

        //查询该区域下是否有成功匹配的订单列表

        Map<String, Orders> matchOrdersTotal = taskHashRedisTemplate.entries(String.format(CITYNAME_ORDERS_SUCCESS_MATCH_KEY, compare.getOpenLocalTypeEnum().toSimpleName()));


        if (matchOrdersTotal == null || matchOrdersTotal.isEmpty()) {
            logger.info("城市={}下没有已经匹配的订单信息", compare.getOpenLocalTypeEnum().toCityName());
            return;
        }

        Orders original;
        for (Map.Entry<String, Orders> entry : matchOrdersTotal.entrySet()) {


            original = entry.getValue();

            if (original == null || original.isEmpty()) {
                continue;
            }

            //排除自己
            if (compare.getUid().equals(original.getUid())) {
                continue;
            }

            //先计算二者订单是否匹配
            if (isMatching(original, compare)) {
                logger.info("订单 ={} 符合场景订单={}的匹配 计算 现在进行重新 匹配场景订单={}的记录结果", compare.getUuid(), original.getUuid(), original.getUuid());
                //取出之前已经匹配的记录进行重新匹配
                restSortOrders(original, compare);

            }


        }


    }


    /**
     * 重新排序订单
     *
     * @param original      待排序的订单
     * @param waitAddOrders 待添加的订单
     */
    private void restSortOrders(Orders original, Orders waitAddOrders) {

        //取出用户订单的匹配结果   数据结构==>   匹配成功用户的Id(特定城市_USER_MATCHES_ORDERS_PREFIX_用户Id)  _____  具体的场景+":"+具体的形式(需求方或服务方) _____  所有匹配成功的订单数组
        Orders[] ordersArr = taskUserArrayRedisTemplate.get(String.format(CITYNAME_USER_MATCHES_ORDERS_PREFIX, original.getOpenLocalTypeEnum().toSimpleName(), original.getUid()), original.getPublishType());


        Orders[] addResultArr;


        if (ordersArr == null || ordersArr.length == 0) {
            //把需要排序的订单添加进去
            addResultArr = new Orders[]{waitAddOrders};

        } else {
            //保存排序的集合  根据匹配率 从小到达依次排序
            Set<Orders> _sortOrdersResult = getCompareSet();


            int len = ordersArr.length;

            //倒序循环遍历 找到合适的位置 放置匹配的订单列表
            Orders _tmp;
            for (int i = 0; i < len; i++) {

                //过滤没用的数据
                if (ordersArr[i] == null || ordersArr[i].isEmpty()) {
                    continue;
                }

                _tmp = ordersArr[i];
                //去除 已取消 已完成 进行中 待进行的订单
                if (_tmp.getPayType() == PayTypeEnum.STR_CANCEL.toCode()||_tmp.getPayType()==PayTypeEnum.STR_WAIT_START.toCode()||_tmp.getPayType()==PayTypeEnum.STR_SUCCESS.toCode()||_tmp.getPayType()==PayTypeEnum.STR_START.toCode()){

                    continue;
                }


                //过滤空白的订单 --因为创建数组 默认为特定大小 可能为null --可能这个条件永远走不到 因为默认是从高到低排序
                // 寻找的时候只要找到一个比数据中大的就可以插进去
                if (_tmp == null || _tmp.isEmpty()) {
                    continue;
                }


                //因为默认是按照从高到低排序
                // TODO: 2017/8/10 假数据
//                waitAddOrders.setMatchRate(90.0);
//                _tmp.setMatchRate(90.0);
                if (waitAddOrders.getMatchRate() > _tmp.getMatchRate()) {
                    _sortOrdersResult.add(waitAddOrders);
                    break;
                }

                //如果不比他大 则默认进行添加即可
                _sortOrdersResult.add(_tmp);


            }


            addResultArr = _sortOrdersResult.toArray(new Orders[ordersArr.length]);
            //检测是否是最后一个 如果不是则剩余的拷贝
            if (_sortOrdersResult.size() != DEFAULT_MATCHING_COUNT) {
                //剩余拷贝
                System.arraycopy(ordersArr, _sortOrdersResult.size() - 1, addResultArr, _sortOrdersResult.size(), ordersArr.length - _sortOrdersResult.size());


            }


        }
        //校验是否为空
        if (isOrdersMatchRateEmpty(original)) {
            return;
        }
        //重新存储用户订单的匹配结果   数据结构==>   匹配成功用户的Id(特定城市_USER_MATCHES_ORDERS_PREFIX_用户Id)  _____  具体的场景+":"+具体的形式(需求方或服务方) _____  所有匹配成功的订单数组
        taskUserArrayRedisTemplate.put(String.format(CITYNAME_USER_MATCHES_ORDERS_PREFIX, original.getOpenLocalTypeEnum().toSimpleName(), original.getUserId()), original.getPublishType(), addResultArr);


    }

    /**
     * 排序订单
     *
     * @param original 待排序的订单
     */
    private void sortOrders(Orders original) {
        //取出总的订单列表中数据 (相反的订单)  数据格式==>特定城市的总的订单key(特定城市_USER_TOTAL_SCENE_ORDER_KEY)   _____   订单的Id(防止重复) _____  订单的详细信息
        Map<String, Orders> totalOrdersMap = taskHashRedisTemplate.entries(String.format(CITYNAME_USER_TOTAL_SCENE_ORDERS_KEY, original.getOpenLocalTypeEnum().toSimpleName(), original.getOppositePublishType()));

        if (totalOrdersMap == null || totalOrdersMap.isEmpty()) {
           Orders[] set = taskUserArrayRedisTemplate.get(String.format(CITYNAME_USER_MATCHES_ORDERS_PREFIX, original.getOpenLocalTypeEnum().toSimpleName(),original.getUid()),original.getPublishType());
            if (set!=null&&set.length>0){
                taskUserArrayRedisTemplate.delete(String.format(CITYNAME_USER_MATCHES_ORDERS_PREFIX, original.getOpenLocalTypeEnum().toSimpleName(),original.getUid()),original.getPublishType());
            }
            logger.warn("当前总的订单记录为空");
            return;
        }

        Orders compare;

        //保存排序的集合  根据匹配率 从小到达依次排序
        Set<Orders> _sortOrdersResult = getCompareSet();

        for (Map.Entry<String, Orders> entry : totalOrdersMap.entrySet()) {

//            compare = obj2Orders(entry.getValue());
            compare = entry.getValue();
            if (compare == null || compare.isEmpty()) {
                continue;
            }
            // TODO: 2017/9/19  需要测试
            //如果是自己略过
            if (compare.getUserId().equals(original.getUserId())) {
//            if (compare.getUid().equals(original.getUid())) {
                continue;
            }


            //判断是否符合匹配
            if (isMatching(original, compare)) {
                _sortOrdersResult.add(compare);
                //这里加上双向匹配 即通过循环遍历 也能得到 相反订单满足该需求 则避免每次所有的订单全部遍历
                //当有新的订单变动时候 及把他符合的添加到对方的订单列表中
                restSortOrders(compare, original);
                logger.info("订单={},被加入原订单={}的符合列表中", original.getUuid(), compare.getUuid());
                //记录用户订单匹配成功  数据结构==>特定城市的总的匹配的订单key(特定城市_ORDERS_SUCCESS_MATCH_KEY) _____  匹配成功用户的Id()  _____  匹配成功用户的订单详细信息
                //如果存在不需要进行插入
                taskHashRedisTemplate.putIfAbsent(String.format(CITYNAME_ORDERS_SUCCESS_MATCH_KEY, compare.getOpenLocalTypeEnum().toSimpleName()), compare.getUserId().toString(), compare);

            }


        }


        //检测计算结果 保留特定的匹配大小限制

        if (_sortOrdersResult.isEmpty()) {
            logger.info("用户={}下的订单={}暂时无匹配结果", original.getUserId(), original.getUuid());

            return;
        }

        //校验是否为空
        if (isOrdersMatchRateEmpty(original)) {
            return;
        }

        //存储用户订单的匹配结果   数据结构==>   匹配成功用户的Id(特定城市_USER_MATCHES_ORDERS_PREFIX_用户Id)  _____  具体的场景+":"+具体的形式(需求方或服务方) _____  所有匹配成功的订单数组
        taskUserArrayRedisTemplate.put(String.format(CITYNAME_USER_MATCHES_ORDERS_PREFIX, original.getOpenLocalTypeEnum().toSimpleName(), original.getUserId()), original.getPublishType(), _sortOrdersResult.toArray(new Orders[DEFAULT_MATCHING_COUNT]));


        //记录用户订单匹配成功  数据结构==>特定城市的总的匹配的订单key(特定城市_ORDERS_SUCCESS_MATCH_KEY) _____  匹配成功用户的Id()  _____  匹配成功用户的订单详细信息
        taskHashRedisTemplate.put(String.format(CITYNAME_ORDERS_SUCCESS_MATCH_KEY, original.getOpenLocalTypeEnum().toSimpleName()), original.getUserId().toString(), original);

        logger.info("用户={}下的订单={}匹配结果记录成功", original.getUserId(), original.getUuid());

    }


    /**
     * 移除订单信息
     *
     * @param orders 待移除的订单信息
     */
    private void removeOrders(Orders orders) {

        logger.info("开始做订单={}删除的工作", orders.getUuid());
        //先移除总订单列表中的记录
        removeTotalUserOrdersByOrders(orders);

        //获取现有该订单匹配的记录   数据结构===> 特定城市_ORDERS_MATCHES_USER_PREFIX_订单Id)  _____ 匹配用户Id() _____  该匹配订单的详细信息
        List<Orders> matchOrders = taskHashRedisTemplate.values(String.format(CITYNAME_ORDERS_MATCHES_USER_PREFIX, orders.getOpenLocalTypeEnum().toSimpleName(), orders.getUuid()));

        Orders _tempOrders;


        int len = matchOrders.size();

        //依次删除
        for (int i = 0; i < len; i++) {

            if (matchOrders.get(i) == null || matchOrders.get(i).isEmpty()) {
                continue;
            }

            _tempOrders = matchOrders.get(i);




            //删除该订单匹配的记录   数据结构===> 特定城市_ORDERS_MATCHES_USER_PREFIX_订单Id)  _____ 匹配用户Id() _____  该匹配订单的详细信息
            removeRedisTemplate.delete(String.format(CITYNAME_ORDERS_MATCHES_USER_PREFIX, orders.getOpenLocalTypeEnum().toSimpleName(), orders.getUuid()));
            // TODO: 2017/9/18  CITYNAME_ORDERS_SUCCESS_MATCH_KEY  CITYNAME_USER_MATCHES_ORDERS_PREFIX 中 未删除 debug查看
            taskHashRedisTemplate.delete(String.format(CITYNAME_ORDERS_SUCCESS_MATCH_KEY, orders.getOpenLocalTypeEnum().toSimpleName()),String.valueOf(orders.getUid()));
            //重新匹配用户匹配的列表数据
            //todo 待 审核通过  修改
//            _tempOrders.setUid(orders.getUserId());
            sortOrders(_tempOrders);
        }

        //删除该订单的匹配结果   数据结构==>   匹配成功用户的Id(特定城市_USER_MATCHES_ORDERS_PREFIX_用户Id)  _____  具体的场景+":"+具体的形式(需求方或服务方) _____  所有匹配成功的订单数组
        logger.info("CITYNAME_USER_MATCHES_ORDERS_PREFIX getOpenLocalTypeEnum={},getUid={},getPublishType={}",orders.getOpenLocalTypeEnum().toSimpleName(), orders.getUid(), orders.getPublishType());
        taskUserArrayRedisTemplate.delete(String.format(CITYNAME_USER_MATCHES_ORDERS_PREFIX, orders.getOpenLocalTypeEnum().toSimpleName(), orders.getUid()), orders.getPublishType());

        logger.info("订单={}删除的工作success", orders.getUuid());
    }


    /**
     * 是否匹配
     *
     * @param original 原始订单
     * @param compare  需要比配的订单
     * @return
     */
    private boolean isMatching(Orders original, Orders compare) {
        boolean matchFlag = false;
        if (original == null || original.isEmpty() || compare == null
                || compare.isEmpty() ||
                StringUtils.isEmpty(original.getPublishType()) || StringUtils.isEmpty(compare.getPublishType())
                || compare.getSelTimeType() == null) {
            logger.warn("所传的订单信息不符合规范");
            return matchFlag;
        }

        //先判断是否是属于同一个类型，不是的则过滤
        if (original.getPublishType().equals(compare.getPublishType())) {
            logger.info("用户添加的订单={} 跟原的订单={}类型都是={} 被过滤", compare.getUuid(), original.getUuid(), original.getPublishType());
            return matchFlag;
        }

        //是否是频率订单  是的则过滤
        if (compare.getSelTimeType().equals("1")) {
            logger.info("订单={}是频率订单,不进行匹配  被过滤", compare.getUuid());
            return matchFlag;
        }


        double comparePoint = DistanceTools.getMByPoint(original.getLatitude(), original.getLongitude(), compare.getLatitude(), compare.getLongitude());
        if (comparePoint <= DEFAULT_DISTANCE) {
            logger.info("订单={}计符合距离 被列入用户={}的场景={}下", compare.getUuid(), original.getUid(), original.getPublishType());
            //设置计算出的距离
            compare.setDistance(comparePoint);
            double matchRate = calcMatchRatio(original, compare);
            if (matchRate == ERROR) {
                logger.warn("订单={} 匹配订单={} 匹配率计算错误", compare.getUuid(), original.getUuid());
                return matchFlag;
            }
            //设定匹配率
            compare.setMatchRate(matchRate);

            original.setMatchRate(matchRate);


            //存入订单匹配用户中  数据结构===> 特定城市_ORDERS_MATCHES_USER_PREFIX_订单Id)  _____ 匹配用户Id() _____  该匹配订单的详细信息
            taskHashRedisTemplate.put(String.format(CITYNAME_ORDERS_MATCHES_USER_PREFIX, compare.getOpenLocalTypeEnum().toSimpleName(), compare.getUuid()), original.getUid().toString(), original);
            taskHashRedisTemplate.put(String.format(CITYNAME_ORDERS_MATCHES_USER_PREFIX, original.getOpenLocalTypeEnum().toSimpleName(), original.getUuid()), compare.getUid().toString(), compare);
            logger.info("用户={}下的订单={}匹配订单={}成功", original.getUid(), original.getUuid(), compare.getUuid());
            matchFlag = true;

        }

        return matchFlag;

    }

    /**
     * 计算匹配率
     *
     * @param original 原始订单
     * @param compare  需要比配的订单
     * @return
     */
    private double calcMatchRatio(Orders original, Orders compare) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        double matchRate = 0.00;
//        //TODO:匹配率的计算
//        Random random = new Random();
//        matchRate = random.nextDouble();
        Double distanceGrade = 85.0 / DEFAULT_DISTANCE * (DEFAULT_DISTANCE - DistanceTools.getMByPoint(original.getLatitude(), original.getLongitude(), compare.getLatitude(), compare.getLongitude()));
        Double timeGrade = 0.0;
        try {
            //相隔多少分钟的绝对值 3 小时 180分钟

            Long abs = Math.abs(simpleDateFormat.parse(compare.getStartTime()).getTime() - simpleDateFormat.parse(original.getStartTime()).getTime()) / (1000 * 60);
            timeGrade = Double.valueOf(15 / 180 * (180 - abs));
            logger.info("compare_first="+String.valueOf(timeGrade));
            logger.info("compare_scond="+String.valueOf(abs));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        matchRate = Arith.add(2,distanceGrade , timeGrade);
        return matchRate;

    }

    /**
     * 判断订单的匹配率是否为空
     *
     * @param orders
     * @return
     */
    private boolean isOrdersMatchRateEmpty(Orders orders) {
        if (orders == null || orders.getMatchRate() == null) {
            logger.warn("订单={}的匹配率为空 不允许添加", orders == null ? "" : orders.getUuid());
            return true;
        }

        return false;
    }



}
