package org.egov.infra.config.redis;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import redis.clients.jedis.Protocol;
import redis.embedded.RedisServer;

public class EmbeddedRedisServer implements InitializingBean, DisposableBean, BeanDefinitionRegistryPostProcessor {

    private RedisServer redisServer;

    @Override
    public void afterPropertiesSet() throws Exception {
        redisServer = new RedisServer(Protocol.DEFAULT_PORT);
        redisServer.start();
    }

    @Override
    public void destroy() throws Exception {
        if (redisServer != null)
            redisServer.stop();
    }

    @Override
    public void postProcessBeanDefinitionRegistry(final BeanDefinitionRegistry registry) throws BeansException {
    }

    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}