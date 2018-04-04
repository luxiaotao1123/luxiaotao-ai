package com.wuai.company.user.notify;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.wuai.company.enums.DetailTypeEnum;
import com.wuai.company.enums.OrdersTypeEnum;
import com.wuai.company.enums.PayTypeEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.user.service.UserService;
import com.wuai.company.util.HttpTools;
import com.wuai.company.util.Regular;
import com.wuai.company.util.Response;
import com.wuai.company.util.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


/**
 * Created by Ness on 2017/6/8.
 */
@Controller
@RequestMapping("/pay")
public class AliNotify {

    private Logger logger = LoggerFactory.getLogger(AliNotify.class);
    @Value("${aliPay.app.id}")
    private String app_id;
    @Value("${aliPay.partner}")
    private String partner;
    @Value("${aliPay.notifyUrl}")
    private String notifyUrl;
    @Value("${aliPay.public.key}")
    private String aliPay_public_key;
    @Value("${aliPay.private.key}")
    private String aliPay_private_key;

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    /**
     * <p>
     * <span>API说明：<a style="color:blue">APP手机支付的api</a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">GET</a></span>
     * <br/>
     * <span>URL地址： <a href="http://52woo.com:9203/user/insert/data/auth">http://52woo.com:9203/pay/getAuth</a></span>
     * <br/>
     * </p>
     *
     * @param orderNo  订单信息
     * @param bugInfo  购买的物品名称
     * @param payMoney 支付的金钱
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p>
     */
    @GetMapping("getAuth")
    @ResponseBody
    public Response getAuth(String orderNo, String bugInfo, String payMoney) throws AlipayApiException {
        if (StringUtils.isEmpty(orderNo) || StringUtils.isEmpty(bugInfo) || StringUtils.isEmpty(payMoney)) {
            logger.warn("获取订单信息的参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode(), "获取订单信息的参数为空");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String body = UUID.randomUUID().toString();
        Map<String, String> map = new TreeMap();
        map.put("app_id", app_id);
        map.put("method", "alipay.trade.app.pay");
        map.put("format", "json");
        map.put("charset", "utf-8");
        map.put("sign_type", "RSA");
        map.put("timestamp", LocalDateTime.now().format(formatter));
        map.put("version", "1.0");
        map.put("notify_url", notifyUrl);

        JSONObject json = new JSONObject();
        json.put("app_id", app_id);
        json.put("method", "alipay.trade.app.pay");
        json.put("format", "json");
        json.put("charset", "utf-8");
        json.put("sign_type", "RSA");
        json.put("timestamp", LocalDateTime.now().format(formatter));
        json.put("version", "1.0");
        json.put("notify_url", notifyUrl);

        Map<String, String> content = new TreeMap();
        content.put("body", body);
        content.put("subject", bugInfo);
        content.put("out_trade_no", orderNo);
        content.put("timeout_express", "30m");
        content.put("total_amount", payMoney);
        content.put("product_code", "QUICK_MSECURITY_PAY");
        //content.put("random_code", random_code);
        String contentJson = JSONObject.toJSONString(content);
        json.put("biz_content", contentJson);
        map.put("biz_content", contentJson);
        String rsaSign = null;
        try {
            rsaSign = AlipaySignature.rsaSign(map, aliPay_private_key, "utf-8");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        json.put("sign", rsaSign);
        //json.put("random_code", random_code);
        json.put("out_trade_no", orderNo);

        StringBuilder builder_biz = new StringBuilder();
        builder_biz.append("{").append("\"body\":").append("\"").append(body).append("\",").append("\"subject\":").append("\"").append(bugInfo).append("\",")
                .append("\"out_trade_no\":").append("\"").append(orderNo).append("\",").append("\"timeout_express\":").append("\"30m\",").append("\"total_amount\":").append(payMoney)
                .append(",").append("\"product_code\":").append("\"QUICK_MSECURITY_PAY\"").append("}");

        StringBuilder builder_url = new StringBuilder();
        builder_url.append("app_id=").append(app_id).append("&biz_content=").append(builder_biz.toString()).append("&charset=").append("utf-8").append("&format=").append("json").append("&method=").append("alipay.trade.app.pay")
                .append("&notify_url=").append(notifyUrl).append("&sign=").append(rsaSign).append("&sign_type=").append("RSA").append("&timestamp=").append(LocalDateTime.now().format(formatter))
                .append("&version=").append("1.0");

        json.put("url", builder_url.toString());
        //用于测试环境
//        payByAli(orderNo,"222",0.01,"20333@qq.com","1233");
        return Response.success(json);
    }

    /**
     * 验证是否正确
     *
     * @param notifyId
     * @return
     */
    public boolean verifyPay(String notifyId) {
        String result = HttpTools.doGet("https://mapi.alipay.com/gateway.do", ("service=notify_verify&partner=" + partner + "&notify_id=").intern() + notifyId);
        if (result.contains("true")) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * <p>
     * <span>API说明：<a style="color:blue">支付宝支付</ a></span>
     * <br/>
     * <span>请求方式：<a style="color:blue">GET</ a></span>
     * <br/>
     * <span>URL地址： < a href=" ">http://52woo.com:9203/pay/aliPayNotify</ a></span>
     * <br/>
     * </p >
     *
     * @param orderNo 订单号
     * @param sign
     * @param total_fee
     * @param buyer_email
     * @param notifyId
     * @return <table>
     * <thead>
     * <tr>
     * <th style="text-align:left">返回结果</th>
     * <th style="text-align:left">状态标识</th>
     * <th style="text-align:left">说明</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td style="text-align:left">{"code":200,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">成功</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":201,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">参数不对</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":208,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">请求失败</td>
     * </tr>
     * <tr>
     * <td style="text-align:left">{"code":500,"msg":"",data;""}</td>
     * <td style="text-align:left">200</td>
     * <td style="text-align:left">服务器出错</td>
     * </tr>
     * </tbody>
     * </table>
     * <p>
     * <span>举例说明</span>
     * <p>
     * </p >
     */
    @RequestMapping("/aliPayNotify")
    @ResponseBody
    public String payByAli(@RequestParam("out_trade_no") String orderNo, @RequestParam("sign") String sign,
                           @RequestParam(value = "invoice_amount", required = false) Double total_fee, @RequestParam(value = "seller_email") String buyer_email,
                           @RequestParam(value = "notify_id", required = false) String notifyId) {

        String echo = "";
        try {
            logger.info("开始进行支付宝回调函数的处理");
        //测试环境注释
//            if (!verifyPay(notifyId)) {
//                logger.warn("notifyId={}支付宝回调不正确", notifyId);
//                return echo;
//            }

            if (StringUtils.isNotEmpty(orderNo) && StringUtils.isNotEmpty(sign) && total_fee != null
                    && Regular.checkEmail(buyer_email)) {
                OrdersTypeEnum o  = OrdersTypeEnum.judgeOrderTypeByOrders(orderNo);
                //需要做的事情
               switch (o){
                   // TODO: 2018/1/12 充值明细 消费明细  待测试
                   case STORE_ORDER:
                       return userService.findDetailByUid(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                   case INVITE_ORDER:
                       return userService.addInvitaion(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                   case STORE_TASK_PAY:
                       return userService.taskPayed(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                   case CONSUME_MONEY:
                       return userService.consumeMoney(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                   case PARTY_PAY:
                       return userService.partyPay(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                   case RECHARGE:
                       return userService.rechargeMoney(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                   case GOLD_USER:
                       return userService.beGoldUser(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                   //约会付款
                   case TRYST_ORDERS:
                       return userService.trystOrdersPay(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                   case CANCEL_TRYST_SERVICE:
                       return userService.cancelTrystUser(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
//                   case TRYST_ADVANCE_MONEY:
//                       return userService.trystAdvanceMoneyPay(orderNo,total_fee, PayTypeEnum.PAY_ZFB.toCode());
                    default:
                        break;
               }

            }

        } catch (Exception e) {
            logger.info("进行支付宝回调函数的处理 异常 订单为{}", orderNo, e);
        }
        return echo;
    }

    public static void main(String[] args) {
        String n = "m123"+ ","+"u456";
        List<String> one = Stream.of(n.split(",")).map(str->str.substring(1,str.length())).collect(toList());
        System.out.println(one.get(1));
    }
}
