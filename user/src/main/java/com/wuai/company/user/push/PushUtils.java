package com.wuai.company.user.push;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.wuai.company.user.domain.Push;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class PushUtils {


    public final int ANDROID_DEVICE = 0;
    public final int IOS_DEVICE = 1;


    private String appId;
    private String appkey;
    private String master;


    private String host;
    private String url;

    private Logger logger = LoggerFactory.getLogger(PushUtils.class);

    private IGtPush push;

    private Properties properties = new Properties();

    {
        try {
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("defaultLink.properties"));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private PushUtils() {

    }

    @Override
    public String toString() {
        return "PushUtils{" +
                "appId='" + appId + '\'' +
                ", appkey='" + appkey + '\'' +
                ", master='" + master + '\'' +
                ", host='" + host + '\'' +
                ", url='" + url + '\'' +
                ", push=" + push +
                '}';
    }

    public PushUtils(String appId, String appkey, String master) {
        this.appId = properties.getProperty(appId);
        this.appkey = properties.getProperty(appkey);
        this.master = properties.getProperty(master);
        this.host = properties.getProperty("gettui.host");
        this.url = properties.getProperty("gettui.url");
        this.push = new IGtPush(this.host, this.appkey, this.master);
    }


    /**
     * 商家的推送
     */
    public static enum storePush {
        INSTANCE;

        public static PushUtils getInstance() {

            return new PushUtils("gettui.storeAppId", "gettui.storeAppkey", "gettui.storeMaster");
        }


    }

    /**
     * 用户推送
     */
    public static enum userPush {
        INSTANCE;

        public static PushUtils getInstance() {

            return new PushUtils("gettui.appId", "gettui.appkey", "gettui.master");

        }


    }


    /**
     * 发送给安桌设备
     *
     * @param title 安桌设备的通知栏标题
     * @param msg   安桌设备的通知栏信息文本
     * @param cid   设备标识
     * @return
     */
    private String sendAndroid(String title, String msg, String cid) {

        try {
            logger.info("安桌推送开始,设备={}", cid);

         // 透传
            TransmissionTemplate template = new TransmissionTemplate();
            template.setAppId(appId);
            template.setAppkey(appkey);
            // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
            template.setTransmissionType(2);
            template.setTransmissionContent(msg);


//            NotificationTemplate template = null;
//            try {
//                template = NotificationTemplateDemo(title, msg);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }

            SingleMessage message = new SingleMessage();
            message.setOffline(true);
            message.setOfflineExpireTime(1000 * 60 * 1);
            message.setData(template);

            List<Target> targets = new ArrayList<Target>();
            Target target1 = new Target();
            Target target2 = new Target();
            target1.setAppId(appId);
            target1.setClientId(cid);

            try {
                IPushResult ret = push.pushMessageToSingle(message, target1);
                logger.info("安桌推送结束,设备={}", cid);
                return ret.getResponse().toString();
            } catch (RequestException e) {
                String requstId = e.getRequestId();
                IPushResult ret = push.pushMessageToSingle(message, target1, requstId);
                logger.info("安桌推送结束,设备={}", cid);
                return ret.getResponse().toString();
            }

        } catch (Exception ex) {
            logger.warn("发送给安桌出错 设备={}", cid, ex);
            return "";
        }
    }

    /**
     * 发送给安桌设备
     *
     * @param title 安桌设备的通知栏标题
     * @param msg   安桌设备的通知栏信息文本
     * @param cid   设备标识
     * @return
     */
    private String sendManageAndroid(String title, String msg, String cid) {

        try {
            logger.info("安桌推送开始,设备={}", cid);

            // 透传
//            TransmissionTemplate template = new TransmissionTemplate();
//            template.setAppId(appId);
//            template.setAppkey(appkey);
//            // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
//            template.setTransmissionType(2);
//            template.setTransmissionContent(msg);


            NotificationTemplate template = null;
            try {
                template = NotificationTemplateDemo(title, msg);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            SingleMessage message = new SingleMessage();
            message.setOffline(true);
            message.setOfflineExpireTime(1000 * 60 * 1);
            message.setData(template);

            List<Target> targets = new ArrayList<Target>();
            Target target1 = new Target();
            Target target2 = new Target();
            target1.setAppId(appId);
            target1.setClientId(cid);

            try {
                IPushResult ret = push.pushMessageToSingle(message, target1);
                logger.info("安桌推送结束,设备={}", cid);
                return ret.getResponse().toString();
            } catch (RequestException e) {
                String requstId = e.getRequestId();
                IPushResult ret = push.pushMessageToSingle(message, target1, requstId);
                logger.info("安桌推送结束,设备={}", cid);
                return ret.getResponse().toString();
            }

        } catch (Exception ex) {
            logger.warn("发送给安桌出错 设备={}", cid, ex);
            return "";
        }
    }
    private NotificationTemplate NotificationTemplateDemo(String title, String msg) throws Exception {
        NotificationTemplate template = new NotificationTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        template.setTitle(title);
        template.setText(msg);
        template.setLogo("push.png");
        template.setTransmissionType(1);
        template.setTransmissionContent(msg);
        return template;
    }

    /**
     * 发送给苹果设备
     *
     * @param msg    苹果的透传消息的内容（应用里面）
     * @param cid    设备标识
     * @param tips   苹果的下拉通知标题
     * @param apns   苹果的下拉通知的文本(应用外)
     * @param isOpen 是否是打开自定义声音控制(true 是 打开 ,false 为不是打开)
     * @return
     */
    private String sendIos(String msg, String cid, String tips, String apns, Boolean isOpen) {
        try {
            logger.info("IOS推送开始,设备={}", cid);

            TransmissionTemplate template = null;
            try {
                template = getTemplate(msg, tips, apns, isOpen);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            SingleMessage message = new SingleMessage();
            message.setOffline(true);
            message.setOfflineExpireTime(1000 * 60 * 7);
            message.setData(template);

            Target target1 = new Target();
            target1.setAppId(appId);
            target1.setClientId(cid);
            try {
                IPushResult ret = push.pushMessageToSingle(message, target1);
                logger.info("IOS推送结束,设备={}", cid);
//                System.out.println(ret.getResponse().toString());
                return ret.getResponse().toString();
            } catch (RequestException e) {
                String requstId = e.getRequestId();
                IPushResult ret = push.pushMessageToSingle(message, target1, requstId);
                logger.info("IOS推送结束,设备={}", cid);
//                System.out.println(ret.getResponse().toString());
                return ret.getResponse().toString();
            }

        } catch (Exception ex) {
            logger.warn("发送给IOS出错 设备={}", cid, ex);
            return "";
        }

    }


    /**
     * 发送消息
     *
     * @param pushs 发送的消息数组
     * @return
     */
    public boolean sendPush(Push... pushs) {

        boolean flag = false;
        if (pushs == null || pushs.length == 0) {
            return false;
        }
        try {
            logger.info("开始进行推送消息组发送", Arrays.asList(pushs));
            for (Push push : pushs) {
                if (ANDROID_DEVICE == push.getSendDeviceType()) {
                    sendAndroid(push.getSendTopic(), push.getSendContent(), push.getDeviceNum());
                    flag = true;
                } else if (IOS_DEVICE == push.getSendDeviceType()) {
                    sendIos(push.getSendContent(), push.getDeviceNum(), push.getSendTopic(), push.getSendContent(), false);
                    flag = true;
                }
            }
            logger.info("结束进行推送消息组发送", Arrays.asList(pushs));
        } catch (Exception e) {
            logger.warn("进行推送消息组发送出错", Arrays.asList(pushs), e);
        }
        return flag;
    }

    /**
     * 发送消息
     *
     * @param pushs 发送的消息数组
     * @return
     */
    public boolean sendManagePush(Push... pushs) {

        boolean flag = false;
        if (pushs == null || pushs.length == 0) {
            return false;
        }
        try {
            logger.info("开始进行推送消息组发送", Arrays.asList(pushs));
            for (Push push : pushs) {
                if (ANDROID_DEVICE == push.getSendDeviceType()) {
                    sendManageAndroid(push.getSendTopic(), push.getSendContent(), push.getDeviceNum());
                    flag = true;
                } else if (IOS_DEVICE == push.getSendDeviceType()) {
                    sendIos(push.getSendContent(), push.getDeviceNum(), push.getSendTopic(), push.getSendContent(), false);
                    flag = true;
                }
            }
            logger.info("结束进行推送消息组发送", Arrays.asList(pushs));
        } catch (Exception e) {
            logger.warn("进行推送消息组发送出错", Arrays.asList(pushs), e);
        }
        return flag;
    }

    public static void main(String[] args) {

        PushUtils pushUtils = new PushUtils();

        Map<String, Object> map = new HashMap<>();
        map.put("acceptIsContract", "false");
        map.put("acceptRoom", 947);
        map.put("acceptPhoneId", 771);

        for (int i = 0; i < 2; i++)

        {
//            System.out.println(pushUtils.SendIos(map.toString(), "13d8a5d91c4071d2d94ed6e40166b817", "test", map.toString(), true));

//            String result = SendAndroid("111",map.toString(), "a70a55c054fa729d14fa99ad2114af89");
            String result = pushUtils.sendAndroid("111", map.toString(), "42cb591eb874ba005cd53aa0ea3e4893");

//            System.out.println(result);
        }

    }

    private TransmissionTemplate getTemplate(String msg, String tips, String apns, Boolean isOpen) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appkey);
        template.setTransmissionContent(msg);
        template.setTransmissionType(2);
        if (tips != null && tips.length() != 0) {
            APNPayload payload = new APNPayload();
            payload.setBadge(0);
            payload.setContentAvailable(1);
            payload.setSound("default");
            payload.setCategory(apns);
            if (isOpen) {
                //设置铃声
                payload.setSound("ringingWithCallComing.aif");

            }

            payload.setBadge(0);

            payload.setAlertMsg(new APNPayload.SimpleAlertMsg(tips));
            // 字典模式使用下者
            // payload.setAlertMsg(getDictionaryAlertMsg());
            template.setAPNInfo(payload);
        }
        return template;

    }

    /**
     * 发送给苹果锁屏信息
     *
     * @param msg           锁屏信息提示文本(应用内 客户看不到)
     * @param lockTips      锁屏信息提示标题
     * @param lockTitle     锁屏信息提示内容
     * @param devicen_token 设备标别
     * @return
     */
    private String iosApns(String msg, String lockTips, String lockTitle, String devicen_token) {
        try {
            IGtPush push = new IGtPush(url, appkey, master);
            APNTemplate t = new APNTemplate();
            APNPayload apnpayload = new APNPayload();
            apnpayload.setSound("");
            APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
            alertMsg.setTitle(msg);
            alertMsg.setBody(msg);
            alertMsg.setTitleLocKey(lockTitle);
            alertMsg.setActionLocKey(lockTips);

            //设置铃声
            /*apnpayload.setSound("ringingWithCallComing.aif");
            apnpayload.setBadge(1);*/
            apnpayload.setAlertMsg(alertMsg);

            t.setAPNInfo(apnpayload);
            SingleMessage sm = new SingleMessage();
            sm.setData(t);
            IPushResult ret0 = push.pushAPNMessageToSingle(appId, devicen_token, sm);

            return (String) ret0.getResponse().get("result");
        } catch (Exception e) {

        }

        return null;

    }


}