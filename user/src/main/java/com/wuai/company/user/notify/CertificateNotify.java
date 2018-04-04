package com.wuai.company.user.notify;

/**
 * 芝麻信用查询回调接口
 * Created by Ness on 2017/7/6.
 */


import com.antgroup.zmxy.openplatform.api.DefaultZhimaClient;
import com.antgroup.zmxy.openplatform.api.ZhimaApiException;
import com.antgroup.zmxy.openplatform.api.request.ZhimaAuthInfoAuthorizeRequest;
import com.antgroup.zmxy.openplatform.api.request.ZhimaCreditScoreGetRequest;
import com.antgroup.zmxy.openplatform.api.response.ZhimaCreditScoreGetResponse;
import com.wuai.company.entity.User;
import com.wuai.company.enums.CertNotifyResultTypeEnum;
import com.wuai.company.enums.ResponseTypeEnum;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.user.service.UserService;
import com.wuai.company.util.Regular;
import com.wuai.company.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//@Controller
@RestController
@RequestMapping("cert")
public class CertificateNotify {
    @Autowired
    private UserDao userDao;
    //    //芝麻开放平台地址
//    @Value("${pay.gatewayUrl}")
//    private String gatewayUrl;
//    //商户应用 Id
//    @Value("${pay.appId}")
//    private String appId;
//    //商户 RSA 私钥
//    @Value("${pay.privateKey}")
//    private String privateKey;
//    //芝麻 RSA 公钥
//    @Value("${pay.publicKey}")
//    private String zhimaPublicKey;
    //芝麻开放平台地址
    private String gatewayUrl = "https://zmopenapi.zmxy.com.cn/openapi.do";
    //商户应用 Id
    private String appId = "1003663";
    //商户 RSA 私钥
    private String privateKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMbLIYFHEadlnbx3/9ny8ClDm3am1dWEkbCGnB5F9U52SbtKpck67II3j22Z4nKcRcUd98iCzpc84X8gHZmMSuYyOcw4Nx9ME+SjyKS5jHUT+HUaQoMj/zdXH9TsEs0iqElzSZk366FgrpwfloxNOB0ShwLwuMbp9wOUVePGfoUHAgMBAAECgYEAhSl0nCB7FuLesmy/mcM+VKXQ8b335zsSTGkfErhSBej+otLyEsXBv8hv7z7xjUxZAB1+6XWV5YEGnB1rNnvffv0U6AFq7v6eSEP0ngqljfGcCkZFTeDr9qjp9nH4fkQKfZYNrZBCMH19K/2wNM0pmMLKnsJkv1i+akks0uF1ZeECQQDq17OwLWZE4llKtXGuC57Z3JMXw8s1zN2OLeGYIwhCCAIVD0wj3sXvGMpMsB/8wQIzGWHp5GhPJ8e+izD+1TuLAkEA2LQCbamqOwQQzgZttqWGV1kLKow5JF8jej2Njt0VcQINAP/BdmSasXzyHQE7sgvRlL4p62bAco/052htO4q79QJATg9U0wLEM7FpLMQS/V8vdgszeXrDWSa3nkLx8bfzTr+KsaVcEbBC6q66z7LbbxBYwtN1EGGzqlv2sQdjSdFm8QJBALIPJzeog6G+c3h2O8wqMjqvhcdONN239tTIovpSmv2ia8D4ZaS8gYd5XQ+MJdrkby9DGfq9EUCRHPDQE4ErO8kCQQDa4gBXy28xmtRXz0385gr3PkYW6tnYGuzMmqrvOf4ikhuiyXQBwVwyGdvcMxMTM/rESP4Psl6tf+zUvsowf2GK";
    //芝麻 RSA 公钥
    private String zhimaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD7JVq/eQArkEjuoOPXHzg/zz1029ayuucKDweeZZBVJiSDGZjqcsm+/TyIDm+CKx0bfBYXScOO6qsIfXxMGeT4gUPH7YdtdhdMLfBai2EcGTl4aZHaEVLclA1d4llxgG+TrFg18t8SI+PfjtfhN1mUlXITVN0QedVBXK7xxPGWZQIDAQAB";

    //平台默认的芝麻信用分数
    private int DEFAUT_ZHIMA_SCORE = 640;

    private final String LINK="_";

    private static Map<String, Boolean> registerUserMaps = new ConcurrentHashMap<>();

    /**
     * 检测分数是否达标
     *
     * @param openId 用户Id
     * @param name   用户姓名
     * @param idCard 用户身份证号
     */
    private boolean checkScore(String openId, String name, String idCard) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(idCard)||StringUtils.isEmpty(openId)){
            logger.warn("检测分数是否达标 参数为空");
            return Boolean.FALSE;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSS");

        ZhimaCreditScoreGetRequest req = new ZhimaCreditScoreGetRequest();
        req.setChannel("apppc");
        req.setPlatform("zmop");
        //后十三位13位为自增数字。
        req.setTransactionId(format.format(new Date()) + UUID.randomUUID().toString().substring(0, 13));// 必要参数
        req.setProductCode("w1010100100000000001");// 必要参数
        req.setOpenId(openId);// 必要参数
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, privateKey, zhimaPublicKey);
        try {
            ZhimaCreditScoreGetResponse response = client.execute(req);
            if (response.isSuccess()) {

                if (Integer.parseInt(response.getZmScore()) > DEFAUT_ZHIMA_SCORE) {
                    registerUserMaps.put(idCard+":"+name,Boolean.TRUE );

                    return true;
                } else {
                    registerUserMaps.put(idCard+":"+name,Boolean.FALSE );
                    logger.info("用户={}的分数={},没有达到规定分数={}", openId, response.getZmScore(), DEFAUT_ZHIMA_SCORE);

                }
            }
        } catch (ZhimaApiException e) {
            logger.warn("用户={}芝麻信用获取分数失败", openId, e);
        }
        return false;
    }

    /**
     * 获取授权登陆地址
     *
     * @param name   姓名
     * @param idCard 身份证号
     */
    @PostMapping("herf")
    public Response getAuth(String name, String idCard) {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(idCard)){
            logger.warn("获取授权登陆地址 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        User user = userDao.findUserByRealNameAndIdCard(name,idCard);
        if (user!=null){
           return Response.error(ResponseTypeEnum.ERROR_CODE.toCode(),"已认证");
        }
        String url = "";
        StringBuilder builder = new StringBuilder();
        ZhimaAuthInfoAuthorizeRequest req = new ZhimaAuthInfoAuthorizeRequest();
        req.setChannel("apppc");
        req.setPlatform("zmop");
        req.setIdentityType("2");// 必要参数
        builder.append("{\"name\":\"");
        builder.append(name);
        builder.append("\",\"certType\":\"IDENTITY_CARD\",\"certNo\":\"");
        builder.append(idCard);
        builder.append("\"}");
        req.setIdentityParam(builder.toString());// 必要参数
        builder.delete(0, builder.length());
        builder.append("{\"auth_code\":\"M_H5\",\"channelType\":\"app\",\"state\":\"");
        builder.append(name);
        builder.append(LINK);
        builder.append(idCard);
        builder.append("\"}");
        req.setBizParams(builder.toString());
        DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, privateKey, zhimaPublicKey);
        try {
            url = client.generatePageRedirectInvokeUrl(req);

        } catch (ZhimaApiException e) {
            logger.warn("用户授权获取失败", e);
        }
        if (!Regular.checkUrl(url)) {
            logger.warn("用户授权获取的不是url 被过滤");
            return Response.success(url);
        }

//        registerUsers.compute()

        return Response.success(url);


    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        CertificateNotify notify = new CertificateNotify();
//        notify.testZhimaAuthInfoAuthquery();
//        System.out.println(notify.getAuth("黄杨烽", "330683199309040811"));
//        System.out.println(notify.check("https://zmopenapi.zmxy.com.cn/openapi.do?charset=UTF-8&method=zhima.auth.info.authorize&channel=apppc&sign=EKOpkXJqkfsKY3Wo8OWZwezmkIrC5%2FxQKbFoDvWmkpMGH5XtT9DCDMMvN4jfhVNZFHemE%2BU9NQG%2FVfXKeAY2fndDX8AqH%2FnFYbExA0yJyxE4Z0BNz6DxUdsNfUPWyc6kxXC90xf7kuK6p%2BDNca8z1H2UNKAhnYLdrjIE1f3mEac%3D&version=1.0&app_id=1003663&sign_type=RSA&platform=zmop&params=6JqDtm72QWCsmySIiL9AgYgmnbXn4PWGs%2FqUWDwICEmxKu%2BWrAHZ%2BMu%2FZPEcdITC6BKNwN3fhMA13uIXT5inw%2FFuVh2GhrGNOEyfOnAxLEhkRk%2FVgbMIkdDU6z%2FAq95yh56D0QD1XOsUQKmhLOsw8usLHhW8VTy8E3XJVW23mVzLhk7%2FQQhYvaCLCO0SL%2FCd60kmWfEapFcVduAJOB0hx6aVPJuHugvkWIoCZA6ga0XYMoSAE7Fbi4Bsu3EjV%2BP7ZIBvcjY0RLvWLZx7D9ljTZ4eaI%2Fr8ZnqIuHHi1cE3jbGUClsfJzxwYYMolDc7%2FLEclFsuI7XjI8e5w71owQeodB9Y4HvGOv5m4%2FGyrPn3adi54pvsbJKXeXeGdKkCcd1JYbEAqAoWY83oBMvgAYEBYXu1VghRxs1Jry0hOqC%2BB9bHOPoIxTs7cekcNu%2F781NVdIOt3WH7B7zSiLkHrUguw3Gxt3Bq%2FNMT4lPwQ%2Fb%2FKeQlz0aaXxXs2dv7z3zezXr\n"));
//        notify.check("http://52woo.com:9203/cert/notify?params=C0wncoCgMP7jSbR15GACvEB6c0vPLDrCTtoUDpQEdZByQyHQ98FIclcs8de8AMfbTcDswnIAZexOtgiaRC%2BhvYst86AGmiWJfXCnIZ2VNr6HkChm%2BkA%2FkRVt2inlc5JQSGUIbBBVmgaBVXrMeMf2X7MU0fFFiRCRbzOizlkezsk0seuZWWxS%2Bc%2Fy0xRk0vfDA7Lj1zplW%2Fu1rC66MT8YWyx5qrVya18XPANXaWg7Zbcw7JJPHPm2Gm64HnFE0tYn4%2FN5DrERi%2FGwfAczZJQNyvBOL2gYCmLGhLuFxDftbifPbT7ybSaRmq7UFjVz2NktFQ4P7q%2BRw%2BpSXdtVDRHJ9g%3D%3D&sign=BvJI3IG9dAnj9WeQ3GlrlPJuc5%2F8IKO4VmHFtbvw4IHMKHTEPWibXFaDCrJmrJ3oupJA62wg8IR7X4Siv5jsasd37KQjcZNiZld5B1l8JMUNrLxTC5KWZM9vhVXgFNG8M5P0GUaeVJ9jBhV4%2B5ppBZ%2FKBNhorf%2Fq1tJNKvubfA8%3D");

    }

    private Logger logger = LoggerFactory.getLogger(CertificateNotify.class);


    /**
     * 用户检测是否是获得权限
     *
     * @param name   用户的姓名
     * @param idCard 用户的Id
     * @return
     */
    @RequestMapping("/check")
    public Response check(String name, String idCard) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(idCard)){
            logger.warn("用户检测是否是获得权限 参数为空");
            return Response.error(ResponseTypeEnum.EMPTY_PARAM.toCode());
        }
        Integer result = CertNotifyResultTypeEnum.FAIL.toCode();
        Boolean isAuthSuccess = registerUserMaps.get((idCard+":"+URLEncoder.encode(name,"utf-8")).intern());
        if (isAuthSuccess !=null) {
            if (isAuthSuccess) {
                result = CertNotifyResultTypeEnum.SUCCESS_PASS.toCode();
            }else{
                result=CertNotifyResultTypeEnum.SUCCESS_DEPASS.toCode();
                //验证未通过 移除map里的值
                registerUserMaps.remove(idCard+":"+name);
            }
        }


        return Response.success(result);

    }


    @RequestMapping("/notify")
    public void certNotify(String params, String sign) {

        if (StringUtils.isEmpty(params)||StringUtils.isEmpty(sign)){
            logger.warn("芝麻信用 支付宝回调 参数为空");
            return;
        }
        logger.info("开始接受回调");
        try {

            if (StringUtils.isEmpty(params) || StringUtils.isEmpty(sign)) {
                logger.warn("用户所传的信息不符合规范");
                return;
            }

            //判断串中是否有%，有则需要decode
            if (params.indexOf("%") != -1) {
                params = URLDecoder.decode(params, "utf-8");
            }
            if (sign.indexOf("%") != -1) {
                sign = URLDecoder.decode(sign, "utf-8");
            }
            DefaultZhimaClient client = new DefaultZhimaClient(gatewayUrl, appId, privateKey, zhimaPublicKey);
            String result = client.decryptAndVerifySign(params, sign);
            System.out.println(result);
            if (result.contains("open_id")) {
                String[] combine = (result.split("state=")[1].split("&")[0]).split(LINK);
                checkScore(result.split("open_id=")[1].split("&")[0], combine[0], combine[1]);
            }
        } catch (Exception e) {
            logger.warn("用户params={}解析openId参数失败", params, e);
        }
        System.out.println();

    }

}
