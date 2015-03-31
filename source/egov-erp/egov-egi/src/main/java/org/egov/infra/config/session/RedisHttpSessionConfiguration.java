package org.egov.infra.config.session;

import org.egov.infra.config.redis.EmbeddedRedisServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableRedisHttpSession
@Profile("production")
public class RedisHttpSessionConfiguration {
    
    @Bean
    public EmbeddedRedisServer redisServer() {
        return new EmbeddedRedisServer();
    }
    
    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        final JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPoolConfig(redisPoolConfig());
        return jedisConnectionFactory;
    }

    @Bean
    public CookieHttpSessionStrategy cookieHttpSessionStrategy() {
        return new CookieHttpSessionStrategy();
    }
    
    @Bean
    public JedisPoolConfig redisPoolConfig() {
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(60000);
        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(1800000);
        jedisPoolConfig.setNumTestsPerEvictionRun(-1);
        jedisPoolConfig.setTestOnReturn(false);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
        return jedisPoolConfig;
    }
}