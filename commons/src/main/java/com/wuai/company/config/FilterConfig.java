package com.wuai.company.config;

import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wuai.company.enums.DataSourcesEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.wuai.company.enums.ResponseTypeEnum.RESET_LOAD_CODE;

/**
 * Web认证过滤器
 * Created by Ness on 2017/6/20.
 */

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean createMyFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new MyFilter());
        filterRegistrationBean.addUrlPatterns("/merchant/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean createCrossFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new CrossFilter());
        filterRegistrationBean.addUrlPatterns("/deploy/*");
        return filterRegistrationBean;
    }

    private class CrossFilter implements Filter {

        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
            HttpServletResponse response = (HttpServletResponse) res;
            response.addHeader("Access-Control-Allow-Origin", "*");
            //支持的http 动作
            response.addHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
            response.addHeader("Access-Control-Allow-Headers", "token,uid");
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() {

        }
    }

    private class MyFilter implements Filter {

        private final long EXPIRE_TIME = 30;
        private final LoadingCache<String, String> onlineCache = CacheBuilder.newBuilder().expireAfterAccess(EXPIRE_TIME, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return "";
            }
        });

        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

        }

        private final String TOKEN = "token";
        private final String LOAD = "load";

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            String[] split = req.getRequestURI().split("/");
            String name = split[split.length - 1];
            if (StringUtils.isNotEmpty(name)) {
                Cookie tokenCookie = WebUtils.getCookie(req, TOKEN);
                if (tokenCookie != null) {
                    String correctKey = onlineCache.getUnchecked(name);

                    if (StringUtils.isEmpty(correctKey) || !correctKey.equals(tokenCookie.getValue())) {
                        write((HttpServletResponse) response, com.wuai.company.util.Response.response(RESET_LOAD_CODE.toCode(), "用户登录不正确"));
                        return;
                    }

                    chain.doFilter(req, response);

                } else {

                    if (!req.getRequestURI().contains(LOAD)) {
                        write((HttpServletResponse) response, com.wuai.company.util.Response.response(RESET_LOAD_CODE.toCode(), "登录异常"));
                        return;
                    }

                    chain.doFilter(req, response);
                    HttpServletResponse res = (HttpServletResponse) response;
                    String value = UUID.randomUUID().toString();
                    onlineCache.put(name, value);
                    res.addCookie(new Cookie(TOKEN, value));
                }
            }

        }


        private void write(HttpServletResponse response, Object msg) throws IOException {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            try (PrintWriter writer = new PrintWriter(response.getOutputStream())) {
                writer.write(JSONObject.toJSONString(msg));
            }
        }


        @Override
        public void destroy() {

        }
    }

    public static void main(String[] args) {
    }
}
