package com.wuai.company.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.wuai.company.enums.DataSourcesEnum;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ness on 2017/5/25.
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class DbConfig {


    @Bean(initMethod = "init", destroyMethod = "close", name = "dev")
    public DruidDataSource getDataSource(@Value("${jdbc.url}") String url,
                                         @Value("${jdbc.user}") String username,
                                         @Value("${jdbc.pass}") String password) throws SQLException {
        return createDruidData(url, username, password);
    }


 

    @Bean
    @Primary
    public AbstractDataSource creteDataSource(DruidDataSource dev ){
        creteRouting creteRouting = new creteRouting();
        Map<Object, Object> map = new HashMap<>();
        map.put(DataSourcesEnum.DEV.getStr(), dev);
      
        //缺省
        creteRouting.setDefaultTargetDataSource(dev);

        creteRouting.setTargetDataSources(map);
        return creteRouting;

    }


    public static class DataSourceHolder {
        private static ThreadLocal<String> holder = new ThreadLocal<>();
        public static void setHolder(String key) {
            holder.set(key);
        }
        static String getHolder() {
            return holder.get();
        }
    }

    public class creteRouting extends AbstractRoutingDataSource {
        @Override
        protected Object determineCurrentLookupKey() {
            return DataSourceHolder.getHolder();
        }
    }


    /**
     * 配置数据源
     * @param url   sql Url
     * @param username  账号
     * @param password  密码
     * @return
     * @throws SQLException
     */
    private DruidDataSource createDruidData(String url, String username, String password) throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setFilters("stat");
        dataSource.setMaxActive(50);
        dataSource.setInitialSize(10);
        dataSource.setMaxWait(60000);
        dataSource.setMinIdle(10);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 'x'");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setPoolPreparedStatements(true);
        List<String> chartSet = new ArrayList<>();
        chartSet.add("set names  utf8mb4;");
        dataSource.setConnectionInitSqls(chartSet);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(50);

        return dataSource;
    }


    @Bean(initMethod = "migrate")
    public Flyway getMigration(DataSource dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setValidateOnMigrate(Boolean.FALSE);
        flyway.setLocations("migration");
        return flyway;

    }


}
