package com.wuai.company.user.util;

/**
 * Created by Administrator on 2017/7/1.
 */
public class CheckMsg {
    public static void main(String[] args) {
        String mobileNumber = "13857513104";//手机号码
        String code = "4297";//验证码
        try {
            Boolean str = MobileMessageCheck.checkMsg(mobileNumber,code);
            if(Boolean.TRUE.equals(str)){
                System.out.println("验证成功！");
            }else{
                System.out.println("验证失败！");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
