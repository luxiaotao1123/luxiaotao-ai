package com.wuai.company.rpc.mobile;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.wuai.company.entity.Orders;
import com.wuai.company.entity.StoreOrders;
import com.wuai.company.entity.TrystOrders;
import com.wuai.company.entity.User;
import com.wuai.company.enums.RpcAllowMsgEnum;
import com.wuai.company.util.JwtToken;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 建立处理器
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {


    private static Map<String, Channel> onlineSubscribers;

    private final static String LINK = ":";

    private static Multimap<String, Boolean> allowList;

    static {
        //全部在线的用户
        onlineSubscribers = new ConcurrentHashMap<>();

        HashMultimap<String, Boolean> prcMap = HashMultimap.create();

        //及时通讯的key
        prcMap.put(RpcAllowMsgEnum.NOTIFY.toKey(), Boolean.TRUE);
        //约吧抢单的key
        prcMap.put(RpcAllowMsgEnum.TRYST.toKey(), Boolean.TRUE);
        //消息的key
        prcMap.put(RpcAllowMsgEnum.MSG.toKey(), Boolean.TRUE);
        //允许的名单列表
        allowList = Multimaps.unmodifiableMultimap(prcMap);


    }


    /**
     * 测试
     */

    public static void sendAll() {
        for (Channel channel : onlineSubscribers.values()) {
            channel.writeAndFlush("我是测试");


        }


    }


    /**
     * 日志文件
     */
    private static Logger logger = LoggerFactory.getLogger(ServerHandler.class);


    /**
     * 传递信息
     *
     * @param allowMsgEnum 允许的传递类型
     * @param orders       订单的类型
     */
    public static boolean sendNotify(RpcAllowMsgEnum allowMsgEnum, Orders orders, Integer type) {

        //检测是否存在
        Channel channel = onlineSubscribers.get((orders.getUserId() + LINK + allowMsgEnum.toKey()).intern());
        if (channel == null) {
            logger.warn("所发送用户已经不存在列表中");
            return false;
        }
        JSONObject json = new JSONObject();
        List<User> userList = orders.getUsers();
        String icon = null;
        String nickname = null;
        for (int i = 0; i < userList.size(); i++) {
            if (i == 0) {
                icon = userList.get(i).getIcon();
                nickname = userList.get(i).getNickname();
            } else {
                icon = icon + "," + userList.get(i).getIcon();
                nickname = nickname + "," + userList.get(i).getNickname();
            }
        }
        json.put("icon", icon);
        json.put("nickname", nickname);
        json.put("ordersId", orders.getUuid());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("data", json);

//        json.put("receive", orders);

        channel.writeAndFlush(jsonObject.toJSONString());
        return true;

    }

    /**
     * 传递信息
     *
     * @param allowMsgEnum 允许的传递类型
     * @param orders       订单的类型
     * @param user         接收邀请的用户id
     */
    public static boolean sendInvitationNotify(RpcAllowMsgEnum allowMsgEnum, Orders orders, User user, Integer type) {
        logger.info("---rpc---");
        //检测是否存在
        logger.info("---type---" + type);
        Channel channel = onlineSubscribers.get((orders.getUid() + LINK + allowMsgEnum.toKey()).intern());
        if (channel == null) {
            logger.warn("所发送用户已经不存在列表中");
            return false;
        }
        JSONObject json = new JSONObject();
        json.put("userId", user.getId());
        json.put("icon", user.getIcon());
        json.put("ordersId", orders.getUuid());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("data", json);
        channel.writeAndFlush(jsonObject.toJSONString());

        logger.info("客戶端={},发送订单ID={} success...", channel.remoteAddress(), orders.getUuid());
        return true;

    }

    /**
     * 传递信息 抢单用户
     *
     * @param allowMsgEnum 允许的传递类型
     * @param userId         接收邀请的用户id
     * @param user    抢单的用户
     */
    public static boolean sendTrystNotify(RpcAllowMsgEnum allowMsgEnum,Integer userId, User user) {
        logger.info("---rpc---tryst---");
        //检测是否存在
        Channel channel = onlineSubscribers.get((userId + LINK + allowMsgEnum.toKey()).intern());
        if (channel == null) {
            logger.warn("所发送用户已经不存在列表中");
            return false;
        }
        JSONObject json = new JSONObject();
        json.put("user", user);
        channel.writeAndFlush(json.toJSONString());

        logger.info("客戶端={},用户id={} success...", channel.remoteAddress(),userId);
        return true;

    }

    public static boolean sendRefuseNotify(RpcAllowMsgEnum allowMsgEnum, Orders invitationOrders, Integer type) {

        //检测是否存在
        Channel channel = onlineSubscribers.get((invitationOrders.getUserId() + LINK + allowMsgEnum.toKey()).intern());
        if (channel == null) {
            logger.warn("所发送用户已经不存在列表中");
            return false;
        }

        JSONObject json = new JSONObject();
        json.put("userId", type);
        json.put("ordersId", invitationOrders.getUuid());
        channel.writeAndFlush(json.toJSONString());
        return true;

    }

//    public static void main(String[] args) {
//        Orders orders = new Orders();
//        orders.setUserId(1040);
//        ServerHandler.sendNotify(RpcAllowMsgEnum.NOTIFY, orders);
//    }

    /**
     * 建立连接  发送的信息格式为token+":"+uid+":"+":"+订阅信息
     * <p>
     * 订阅信息具体可以参照枚举 RpcAllowMsgEnum
     * <p>
     * </p>
     *
     * @param ctx
     * @param msg 发送的格式
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        Channel channel = ctx.channel();
        if (StringUtils.isEmpty(msg) || !msg.contains(":")) {
            logger.warn("客戶端={},所传订阅类型不符合规则", channel.remoteAddress());
            return;
        }

        String[] values = msg.split(":");
        logger.info("===>len="+String.valueOf(values.length));
        if (values.length != 3) {
            logger.warn("客戶端={},所传订阅类型长度不够", channel.remoteAddress());
            return;
        }

        Integer id = JwtToken.parseToken(values[0], values[1]);

        if (id == null) {
            logger.warn("客戶端={},解析后得不到用户信息", channel.remoteAddress());
            return;
        }


        logger.info("客戶端{},已经建立连接{}类型频道", channel.remoteAddress(), values[1]);
        addConnect(channel, id, values[2]);


    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        discardChannel(ctx.channel(), CancelLink.NORMAL_EXIT);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        discardChannel(ctx.channel(), CancelLink.EXCEPTION_EXIT);
        super.exceptionCaught(ctx, cause);
    }

    private enum CancelLink {
        NORMAL_EXIT("正常退出"), EXCEPTION_EXIT("异常退出");
        private String echo;

        CancelLink(String echo) {
            this.echo = echo;
        }

        public String toEcho() {
            return echo;
        }

    }

    /**
     * 失去连接
     *
     * @param ch
     */
    private void discardChannel(Channel ch, CancelLink cancelLink) {
        Set<Map.Entry<String, Channel>> entries = onlineSubscribers.entrySet();
        Channel channel;
        for (Map.Entry<String, Channel> entry : entries) {
            channel = entry.getValue();
            if (channel != null) {
                if (channel.remoteAddress().equals(ch.remoteAddress())) {
                    onlineSubscribers.remove(entry.getKey(), channel);
                    logger.info("客戶端{},{} 取消频道为{}", ch.remoteAddress(), cancelLink.toEcho(), entry.getKey());
                }
            }

        }


    }

    /**
     * 建立连接
     *
     * @param ch           客户端通道
     * @param id           用户Id
     * @param orderChannel 订阅的场景
     */
    private void addConnect(Channel ch, Integer id, String orderChannel) {
        //校验是否符合规定
        if (!allowList.containsKey(orderChannel)) {
            logger.warn("用户={} 所订阅的频道{}不符合", ch.remoteAddress(), orderChannel);
        }

        onlineSubscribers.put((id + LINK + orderChannel).intern(), ch);
        logger.info("用户={} 成功订阅的频道{}", ch.remoteAddress(), orderChannel);
    }


    public static Boolean sendStoreNotify(RpcAllowMsgEnum allowMsgEnum, StoreOrders orders, Integer type) {
        //检测是否存在
        logger.info("在线用户=" + onlineSubscribers);
        Channel channel = onlineSubscribers.get((orders.getUserId() + LINK + allowMsgEnum.toKey()).intern());
        if (channel == null) {
            logger.warn("所发送用户已经不存在列表中");
            return false;
        }
        JSONObject json = new JSONObject();

        json.put("type", type);
        json.put("ordersId", orders.getUuid());
        json.put("money", String.valueOf(orders.getMoney()));

//        json.put("receive", orders);

        channel.writeAndFlush(json.toJSONString());
        return true;
    }
}
