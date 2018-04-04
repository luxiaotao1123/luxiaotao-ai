package com.wuai.company.config;

import com.alibaba.fastjson.JSONObject;
import com.wuai.company.enums.DataSourcesEnum;
import com.wuai.company.util.Iptools;
import com.wuai.company.util.JwtToken;
import com.wuai.company.util.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.wuai.company.enums.DataSourcesEnum.ANDROID_TEST;
import static com.wuai.company.enums.ResponseTypeEnum.RESET_LOAD_CODE;
import static com.wuai.company.util.JwtToken.HEADER;
import static com.wuai.company.util.JwtToken.ID;

/**
 * Shiro的配置中心
 * Created by Ness on 2017/6/1.
 */
@Configuration
public class ShiroConfig {

    private Logger logger = LoggerFactory.getLogger(ShiroConfig.class);

    @Bean
    public DefaultWebSecurityManager createSecurityManager(AuthorizingRealm realm) {

        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(realm);
        return manager;

    }


    @Bean(name = "securityManager")
    public LifecycleBeanPostProcessor createLifecyleBeanProcessor() {

        return new LifecycleBeanPostProcessor();

    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean creatShiroFactory(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap<>();
        filters.put("userAnonymous", new UserAnonymocusFilter());
        filters.put("userAuth", new UserAuthFilter());
        factoryBean.setFilters(filters);
        Map<String, String> chains = new HashMap<>();
        /**
         * 这里添加自定义权限 项目 controller的requestMapper
         *   userAnonymous--不需要权限校验的接口
         *   userAuth--需要权限校验的接口(默认结尾auth)
         * */
        chains.put("/**/auth", "userAuth");

        chains.put("/**", "userAnonymous");


        factoryBean.setFilterChainDefinitionMap(chains);


        return factoryBean;
    }


    private final String ERROR = "error";


    private class UserAnonymocusFilter extends AnonymousFilter {


        @Override
        protected boolean onPreHandle(ServletRequest request, ServletResponse req, Object mappedValue) {
            HttpServletRequest res = WebUtils.toHttp(request);

            if (!isAccess(res)) {
                return false;
            }

            return super.onPreHandle(request, req, mappedValue);
        }

    }

    private final Map<String, AccessRecord> ipIntercept = new ConcurrentHashMap<>(1000);

    private boolean isAccess(HttpServletRequest res) {
        String ip = Iptools.gainRealIp(res);


        if (StringUtils.isEmpty(ip)) {
            logger.warn("空Ip地址正在进行访问......");
            res.setAttribute(ERROR, Response.fail());
            return false;

        } else {
            AccessRecord accessRecord = ipIntercept.get(ip);

            long currentTime = System.currentTimeMillis();

            if (accessRecord == null) {
                accessRecord = new AccessRecord();
                accessRecord.setAccessTimes(0);
                accessRecord.setFirstAccessTime(currentTime);
            }
            long intervalTime = currentTime - accessRecord.getFirstAccessTime();

            if (intervalTime > ACCESS_INTERVAL_TIMES) {
                accessRecord.setAccessTimes(0);
                accessRecord.setFirstAccessTime(currentTime);
            }

            int count = accessRecord.getAccessTimes();

            accessRecord.setAccessTimes(++count);

            ipIntercept.put(ip, accessRecord);


            if (intervalTime < ACCESS_INTERVAL_TIMES && accessRecord.getAccessTimes() > ACCESS_FORBIDDEN_TIMES) {
                logger.warn("ip{} 时间间隔 {} 毫秒 访问路径={}, {} 次 ,已拦截 ", ip, intervalTime, res.getRequestURI(), accessRecord.getAccessTimes());
                res.setAttribute(ERROR, Response.fail());
                return false;

            }

            //web 运营管理系统 切换数据源
//            String type = res.getParameter("test");
//            if (type!=null&&type.equals(DataSourcesEnum.TEST.getStr())){
//                DbConfig.DataSourceHolder.setHolder(DataSourcesEnum.TEST.getStr());
//                return true;
//            }
//            if (type!=null&&type.equals(DataSourcesEnum.DEV.getStr())){
//                DbConfig.DataSourceHolder.setHolder(DataSourcesEnum.DEV.getStr());
//                return true;
//            }

            //切换数据源
//            String uid = res.getHeader("uid");
//
//             if (StringUtils.isNotEmpty(uid)&&uid.startsWith(ANDROID_TEST.getStr())) {
//                DbConfig.DataSourceHolder.setHolder(DataSourcesEnum.TEST.getStr());
//            } else {
//
//                DbConfig.DataSourceHolder.setHolder(DataSourcesEnum.DEV.getStr());
//            }


        }
        return true;
    }

    private final int ACCESS_FORBIDDEN_TIMES = 10;
    private final int ACCESS_INTERVAL_TIMES = 2000;


    /**
     * 访问记录
     */
    private class AccessRecord {
        /**
         * 第一次访问时间
         */
        private long firstAccessTime;

        /**
         * 访问的次数
         */
        private int accessTimes;

        public long getFirstAccessTime() {
            return firstAccessTime;
        }

        public void setFirstAccessTime(long firstAccessTime) {
            this.firstAccessTime = firstAccessTime;
        }

        public int getAccessTimes() {
            return accessTimes;
        }

        public void setAccessTimes(int accessTimes) {
            this.accessTimes = accessTimes;
        }

        public void setAccessTimes(Integer accessTimes) {
            this.accessTimes = accessTimes;
        }

        @Override
        public String toString() {
            return "AccessRecord{" +
                    "firstAccessTime=" + firstAccessTime +
                    ", accessTimes=" + accessTimes +
                    '}';
        }
    }


    private class UserAuthFilter extends UserFilter {


        @Override
        protected boolean isAccessAllowed(ServletRequest req, ServletResponse response, Object mappedValue) {


            HttpServletRequest request = WebUtils.toHttp(req);


            if (!isAccess(request)) {
                return false;
            }


            String token = request.getHeader("token");
            String uid = request.getHeader("uid");


            if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
                logger.warn("Ip={}没有token进行访问", Iptools.gainRealIp(request));
                request.setAttribute(ERROR, Response.fail());
                return false;
            }
            Integer id = JwtToken.parseToken(token, uid);

            if (id == null) {
                logger.warn("token={}解析id不正确", token);
                request.setAttribute(ERROR, Response.response(RESET_LOAD_CODE.toCode(), "需要重新登录"));
                return false;
            }


            //这里进行刷新token
            HttpServletResponse res = (HttpServletResponse) response;

            res.addHeader(HEADER, JwtToken.toToken(id, uid));
            request.setAttribute(ID, id);

            return true;

        }

        @Override
        protected boolean onAccessDenied(ServletRequest request,
                                         ServletResponse response) throws Exception {
            logger.error("紧急异常....");
            HttpServletRequest req = WebUtils.toHttp(request);
            HttpServletResponse res = WebUtils.toHttp(response);
            writeForbid(res, req == null ? Response.fail() : req.getAttribute(ERROR));
            return false;
        }

    }


    private void writeForbid(HttpServletResponse response, Object tips) {


        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");

        try {
            try (PrintWriter printWriter = response.getWriter()) {

                printWriter.write(JSONObject.toJSONString(tips));
                printWriter.flush();
            }


        } catch (IOException e) {
            logger.warn("获取写禁止数据流异常", e);
        }

    }

}
