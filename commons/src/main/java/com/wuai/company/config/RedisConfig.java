package com.wuai.company.config;

import com.wuai.company.entity.NearbyBody;
import com.wuai.company.entity.Orders;
import com.wuai.company.entity.Response.NearBodyResponse;
import com.wuai.company.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.support.collections.RedisZSet;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ness on 2017/5/12.
 */
@Configuration
@Order(1)
public class RedisConfig {

    @Bean("orderHashRedisTemplate")
    public HashOperations<String, String, String> createOrderHashRedis(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.HASH).opsForHash();
    }

    @Bean("taskHashRedisTemplate")
    public HashOperations<String, String, Orders> createTaskTotalHashRedis(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.HASH).opsForHash();
    }

    @Bean("nearbyTemplate")
    public HashOperations<String, String, Object> createNearbyHashRedis(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.HASH).opsForHash();
    }
    @Bean("nearBodyTemplate")
    public HashOperations<String, String,  NearBodyResponse[]> createNearBodyTemplate(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.HASH).opsForHash();
    }
    @Bean("totalNearbyTemplate")
    public HashOperations<String, String, NearbyBody> createTotalNearbyTemplate(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.HASH).opsForHash();
    }
    @Bean("totalNearBodyTemplate")
    public HashOperations<String, String, NearBodyResponse> createTotalNearBodyTemplate(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.HASH).opsForHash();
    }

    @Bean("taskUserArrayRedisTemplate")
    public HashOperations<String, String, Orders[]> createTaskUserArrayHashRedis(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.HASH).opsForHash();
    }

    @Bean("userValueRedisTemplate")
    public ValueOperations<String, String> createUserValueRedis(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.STRING).opsForValue();

    }
    @Bean("msgValueTemplate")
    public ZSetOperations<String,String> createMsgRedis(JedisConnectionFactory factory){
        return createTemplateCache(factory,OperateEnum.STRING).opsForZSet();
    }
    @Bean("snatchUserTemplate")
    public HashOperations<String,String,User> createSnatchUserRedis(JedisConnectionFactory factory){
        return createTemplateCache(factory,OperateEnum.HASH).opsForHash();
    }
    @Bean("undoneRedisTemplate")
    public ZSetOperations<String,String> createUndoneRedis(JedisConnectionFactory factory){
        return createTemplateCache(factory,OperateEnum.STRING).opsForZSet();
    }
    @Bean("removeRedisTemplate")
    public RedisTemplate<String, String> createRemoveRedis(JedisConnectionFactory factory) {
        return createTemplateCache(factory, OperateEnum.STRING);
    }

    private enum OperateEnum {
        STRING, HASH
    }


    private RedisTemplate createTemplateCache(JedisConnectionFactory factory, OperateEnum operateEnum) {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
//        template.expire("",1, TimeUnit.DAYS);
        switch (operateEnum) {
            case HASH:
                template.setHashKeySerializer(new StringRedisSerializer());
                template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
                break;
            default:
                template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        }
        template.afterPropertiesSet();
        return template;
    }


}
