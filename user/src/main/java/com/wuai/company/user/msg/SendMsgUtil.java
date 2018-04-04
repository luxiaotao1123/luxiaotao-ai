package com.wuai.company.user.msg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by p on 2016/2/18.
 */
public class SendMsgUtil {

    private  static  Logger logger = LoggerFactory.getLogger(SendMsgUtil.class);

    private static  Properties properties=new Properties();

    static {
        try {
            properties.load(new InputStreamReader(SendMsgUtil.class.getClassLoader().getResourceAsStream("defaultLink.properties"),"utf-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     *
     * @param url 应用地址，类似于http://ip:port/msg/
     * @param un 账号
     * @param pw 密码
     * @param phone 手机号码，多个号码使用","分割
     * @param msg 短信内容
     * @param rd 是否需要状态报告，需要1，不需要0
     * @return 返回值定义参见HTTP协议文档
     * @throws Exception
     */
    private static String batchSend(String url, String un, String pw, String phone, String msg,
                                   String rd, String ex) throws Exception {
        HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
        GetMethod method = new GetMethod();
        try {

            URI base = new URI(url, false);
            method.setURI(new URI(base, "send", false));
            method.setQueryString(new NameValuePair[] {
                    new NameValuePair("un", un),
                    new NameValuePair("pw", pw),
                    new NameValuePair("phone", phone),
                    new NameValuePair("rd", rd),
                    new NameValuePair("msg", msg),
                    new NameValuePair("ex", ex),
            });
            int result = client.executeMethod(method);
            if (result == HttpStatus.SC_OK) {
                InputStream in = method.getResponseBodyAsStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                return URLDecoder.decode(baos.toString(), "UTF-8");
            } else {
                throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
            }
        } finally {
            method.releaseConnection();
        }
    }


    public static boolean sendMsg(String phone, String num) {
        String url = properties.getProperty("msg.url");
        String un = properties.getProperty("msg.un");
        String pw =  properties.getProperty("msg.pw");
        String msg = properties.getProperty("msg.msgBefore")+num+properties.getProperty("msg.msgAfter");
        String rd =  properties.getProperty("msg.rd");
        String ex = properties.getProperty("msg.ex");

        try {
            logger.info("开始发送号码{} 验证码{} ",phone,num);
             batchSend(url, un, pw, phone, msg, rd, ex);
            logger.info("结束发送号码{} 验证码{} ",phone,num);
        } catch (Exception e) {
            logger.warn("发送号码{} 验证码{} 出错 ",phone,num,e);
            return false;
        }
        return true;

    }

    public static void main(String[] args) {

//        String mobile = "15924179757";// 手机号码，多个号码使用","分割
//        sendMsg(mobile, "111");
        SendMsgUtil.sendMsg("13857513104","1212");
    }
}
