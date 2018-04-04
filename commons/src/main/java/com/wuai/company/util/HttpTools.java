package com.wuai.company.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

import static com.wuai.company.util.Regular.checkUrl;


/**
 * 链接访问工具类
 */
public class HttpTools {
    private static Logger logger = LoggerFactory.getLogger(HttpTools.class);

    /**
     * get请求访问
     *
     * @param url    访问链接
     * @param params 访问参数
     * @return
     */
    public static String doGet(String url, String params) {

        String result = "";
        try {


            //检测网址和参数
            if (!checkUrl(url) || StringUtils.isEmpty(params)) {
                return result;
            }


            HttpClient httpClient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url + "?" + params);
            HttpResponse response = httpClient.execute(get);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                result = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));

            }

        } catch (Exception e) {
            System.out.println(e);
            logger.warn("地址={},参数={},访问出错", url, params, e);
        }
        return result;

    }


}
