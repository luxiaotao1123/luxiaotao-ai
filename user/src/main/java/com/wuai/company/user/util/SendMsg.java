package com.wuai.company.user.util;

/**
 * Created by Administrator on 2017/7/1.
 */
public class SendMsg {
    public static void main(String[] args) {
        String mobileNumber = "13857513104";//接收验证码的手机号码
        try {
            Boolean str = MobileMessageSend.sendMsg(mobileNumber);
            if(Boolean.TRUE.equals(str)){
                System.out.println("发送成功！");
            }else{
                System.out.println("发送失败！");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
