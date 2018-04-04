package com.wuai.company.user.auth;


import com.wuai.company.entity.AdminUser;
import com.wuai.company.entity.MerchantUser;
import com.wuai.company.entity.User;
import com.wuai.company.user.dao.UserDao;
import com.wuai.company.util.JwtToken;
import com.wuai.company.util.Regular;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wuai.company.util.JwtToken.HEADER;

//import com.wuai.company.entity.User;

/**
 * 自定义的realm实现
 */
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    private Logger logger = LoggerFactory.getLogger(MyRealm.class);


    private Map<String, Integer> phone2UuidMaps = new ConcurrentHashMap<>();


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String userPhone = (String) authenticationToken.getPrincipal();
        logger.info("用户{} 开始进行校验登录", userPhone);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        Matcher matcher = pattern.matcher(userPhone);
        if (Regular.checkPhone(userPhone)) {
            User user = getUserByPhone(userPhone);
            AuthenticationInfo authenticationInfo = null;
            if (user != null) {
                authenticationInfo = new SimpleAuthenticationInfo(userPhone, user.getLoadPass(), user.getLoadName());
                phone2UuidMaps.put(userPhone, user.getId());

            }
            logger.info("用户{} 结束进行校验登录", userPhone);
            return authenticationInfo;
        }else if (matcher.find()){
            AdminUser adminUser = getAdminByName(userPhone);
            AuthenticationInfo authenticationInfo = null;
            if (adminUser != null) {
                authenticationInfo = new SimpleAuthenticationInfo(userPhone, adminUser.getPassword(),adminUser.getUsername());
                phone2UuidMaps.put(userPhone, adminUser.getId());
            }
            logger.info("用户{} 结束进行校验登录", userPhone);
            return authenticationInfo;
        }
        else {
            MerchantUser merchantUser = getMerchantByName(userPhone);
            AuthenticationInfo authenticationInfo = null;
            if (merchantUser != null) {
                authenticationInfo = new SimpleAuthenticationInfo(userPhone, merchantUser.getPassword(), merchantUser.getUsername());
                phone2UuidMaps.put(userPhone, merchantUser.getId());

            }
            logger.info("用户{} 结束进行校验登录", userPhone);
            return authenticationInfo;
        }

    }

    /**
     * 根据用户的手机查找用户信息
     *
     * @param phone 用户的手机
     * @return
     */
    private User getUserByPhone(String phone) {

        return userDao.findUserOneByPhone(phone);
    }

    /**
     * 根据商家用户名 查找用户信息
     *
     * @param name 商家用户名
     * @return
     */
    private MerchantUser getMerchantByName(String name) {
        return userDao.findMerchantByName(name);
    }
    /**
     *
     *运营管理系统
     * @param name
     * @return
     */
    private AdminUser getAdminByName(String name) {
        return userDao.findAdminByName(name);
    }
    /**
     * 校验登录
     *
     * @param phone 用户手机
     * @param pass  用户密码
     * @param uid   用户手机标识
     * @return
     */
    public boolean load(String phone, String pass, String uid, HttpServletResponse response) {
        UsernamePasswordToken token = new UsernamePasswordToken(phone, pass);
        try {
            SecurityUtils.getSubject().login(token);
//            JwtToken.toToken(phone2UuidMaps.get(phone),uid);
            response.addHeader(HEADER, JwtToken.toToken(phone2UuidMaps.get(phone), uid));
            phone2UuidMaps.remove(phone);

        } catch (Exception e) {

            logger.warn("用户{},登录失败", phone, e);
            return false;
        }
        return true;
    }

    public boolean msgLoad(String username,HttpServletResponse response){
        try{
            response.addHeader(HEADER, JwtToken.toToken(phone2UuidMaps.get(username), username));
            phone2UuidMaps.remove(username);
        }catch (Exception e){
            logger.warn("用户{},登录失败", username, e);
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        String line ="1aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        Pattern pattern = Pattern.compile("^[A-Za-z][A-Za-z0-9]+$");

        Matcher matcher = pattern.matcher(line);


        System.out.println(matcher.find());
    }

}
