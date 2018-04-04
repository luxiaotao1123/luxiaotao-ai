package com.wuai.company.util;

import java.util.regex.Pattern;

/**
 * Created by Ness on 2016/12/5.
 */
public class Regular {
    // 姓名
    public static boolean checkNameMatch(String realName) {
        if (realName == null) {
            return false;
        }
        String reg = "^[\u4e00-\u9fa5]{2,25}$";
        return Pattern.compile(reg).matcher(realName).matches();
    }

    // 身份证
    public static boolean checkIdCardMatch(String idCard) {
        if (idCard == null) {
            return false;
        }
        String reg = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";
        return Pattern.compile(reg).matcher(idCard).matches();
    }

    // 银行卡号
    public static boolean checkBankCardMatch(String bankCard) {
        if (bankCard == null) {
            return false;
        }
        String reg = "^\\d{16,19}$";
        return Pattern.compile(reg).matcher(bankCard).matches();
    }

    // 手机号
    public static boolean checkPhone(String phone) {
        if (phone == null) {
            return false;
        }
//        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";

        String reg = "^1(4[57]|3[0-9]|5([0-3]|[5-9])|7([0-1]|[6-8])|8[0-9])\\d{8}$";
        return Pattern.compile(reg).matcher(phone).matches();
//        return phone.matches(reg);
    }



    // 昵称
    public static boolean checkNickName(String userName) {
        if (userName == null) {
            return false;
        }
        String reg = "^[a-zA-Z0-9\u4e00-\u9fa5]{1,16}||/w+$";
        return Pattern.compile(reg).matcher(userName).matches();
    }

    // QQ
    public static boolean checkQQ(String qq) {
        if (qq == null) {
            return false;
        }
        String reg = "^[1-9][0-9]{4,14}$";
        return Pattern.compile(reg).matcher(qq).matches();
    }

    //检测网址
    public static boolean checkUrl(String url) {
        if (url == null) {
            return false;
        }
        String reg = "^http(s)?://(\\w+\\.([\\w-]+\\.)+\\w+|localhost)(:\\d{1,6})?/(.*)?$";
        return Pattern.compile(reg).matcher(url).matches();
    }


    //检测邮箱
    public static boolean checkEmail(String email) {
        if (email == null) {
            return false;
        }
        String reg = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$$";
        return Pattern.compile(reg).matcher(email).matches();
    }
    //检验密码
    public static boolean checkPass(String pass){
        if (pass == null) {
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,18}$";
        return pass.matches(regex);
    }
    //检验密码
    public static boolean checkPayPass(String pass){
        if (pass == null) {
            return false;
        }
        String regex = "^[0-9]{6}";
        return pass.matches(regex);
    }

    public static void main(String[] args) {
        System.out.println(Regular.checkPayPass("16a054"));
//        System.out.println(Regular.checkPass("mengtianyu123"));
    }

}
