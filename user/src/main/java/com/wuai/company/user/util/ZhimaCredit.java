package com.wuai.company.user.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.ZhimaCreditScoreBriefGetRequest;
import com.alipay.api.response.ZhimaCreditScoreBriefGetResponse;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/4.
 */

public class ZhimaCredit {

    public static final String appId = "2017060807450301";
    public static final String alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    public static final String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMlVI52n1syjau3YuTQtwFk4wjWJxjso6/Mcc3lUwYPtPnLKlSKG7pxl8PB2EGL7QkD6Ekqrd4Ljt6c6fm4RSyGZ7/DGgOzTgK5kprCnhz0DfUfDnxFfc+i6Lue6DE+vpQIIwH3ZtXxVx8SxWvTmwTngxuDYh6TQJDz1ukwAeukJAgMBAAECgYEAoqu/0ypBS6beRr7NJXOx7mUFxexD1pkBy7Q5ONdaZBXUVzuHtBYBxSw4uJEsPE13ArkK/nn8Xi1PvjZs4NHoORaTQIFxXojJxXvQ8uSX+c99OTBkg6eUkC2yrg4xkoaSMThjPLiUcdr/AiEkYhxfz7fuxYoCytoj9T7c6aiNFYECQQDv4c44TobJdG4cQBoXcGlQGOZU7caxROd0+yDapSyTjaD+dHweFSb5dhPicusoNLfJrH6sO1LAFW1eDHUemSKtAkEA1tw/J0UAiqVXWH9sugoSSC93wqG5ZDunQlONAiLenOZqYzFVfOMnm+YvDDdFQ4xVa4qOw2ffmFIwO0U3gjPHTQJAFV1qLY+o55ZsHC4FZOMJLi4ZjaTj1nQJnVykj3S4pKevmBot18wQfZVrrfaIRTt0xwyK97IM67i81eyMHd6LvQJAJAHfF46py5Jv8+XO69KaQ7yovlZqAlE485Wa8Pu1AOhgdeUjpX1P+wF1av+sPOi8u/wJdtfmkdlcOeKcrdZNlQJAbFxTJD0aqUWQN2P+pgEj6KZGaY4edfxtgw4lCOslGN0G14NLrhcfa9XI9iHJuOehPs5+ZgvlmJwn2XJAB4LLzg==";
    public static final String productCode = "w1010100000000002733";

    public  Boolean zhimaCredit( String certType, String certNo, String name,Integer admittanceScore) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",appId,privateKey,"json","UTF-8",alipayPublicKey,"RSA");
        ZhimaCreditScoreBriefGetRequest request = new ZhimaCreditScoreBriefGetRequest();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS");
//        System.out.println(simpleDateFormat.format(new Date()));
        int n = 13;
        if(n<1){
            throw new IllegalArgumentException("随机数位数必须大于0");
        }
        Long random = (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);
        String transactionId = simpleDateFormat.format(new Date())+String.valueOf(random);

//        String certType = "IDENTITY_CARD";
//        String certNo = "542521197000090009";
//        String name = "张三";
//        Integer admittanceScore = 650;
        request.setBizContent("{" +
                "\"transaction_id\":\""+transactionId+"\"," +
                "\"product_code\":\""+productCode+"\"," +
                "\"cert_type\":\""+certType+"\"," +
                "\"cert_no\":\""+certNo+"\"," +
                "\"name\":\""+name+"\"," +
                "\"admittance_score\":"+admittanceScore+"" +
                "  }");
        ZhimaCreditScoreBriefGetResponse response = null;
        try {
            response = alipayClient.execute(request);

        } catch (AlipayApiException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        if(response.isSuccess()){
            System.out.println(alipayClient.execute(request));
            System.out.println(alipayClient+"调用成功");
            return true;
        } else {
            System.out.println("调用失败");
            return false;
        }
    }

    public static void main(String[] args) throws AlipayApiException {
        ZhimaCredit zhimaCredit = new ZhimaCredit() ;
        String certType = "IDENTITY_CARD";
        String certNo = "542521197000090009";
        String name = "张三";
        Integer admittanceScore = 650;
        zhimaCredit.zhimaCredit(certType,certNo,name,admittanceScore);
    }
}
