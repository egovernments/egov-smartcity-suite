/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 *
 */

package org.egov.infra.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

import java.time.Duration;
import java.util.List;

import static org.egov.infra.utils.ApplicationConstant.COLON;

@Configuration
public class RedisServerConfiguration {

    @Value("${redis.enable.embedded}")
    private boolean usingEmbeddedRedis;

    @Value("${redis.enable.sentinel}")
    private boolean sentinelEnabled;

    @Value("${redis.host.name}")
    private String redisHost;

    @Value("${redis.host.port}")
    private Integer redisPort;

    @Value("${redis.max.pool.size}")
    private Integer maxPoolSize;

    @Value("${redis.min.pool.size}")
    private Integer minPoolSize;

    @Value("${redis.sentinel.master.name}")
    private String sentinelMasterName;

    @Value("#{'${redis.sentinel.hosts}'.split(',')}")
    private List<String> sentinelHosts;

    @Bean
    @Conditional(RedisServerConfigCondition.class)
    public static EmbeddedRedisServer redisServer() {
        return new EmbeddedRedisServer();
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory;
        if (sentinelEnabled && !usingEmbeddedRedis) {
            RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
            sentinelConfig.master(sentinelMasterName);
            for (String host : sentinelHosts) {
                String[] hostConfig = host.split(COLON);
                sentinelConfig.sentinel(hostConfig[0].trim(), Integer.valueOf(hostConfig[1].trim()));
            }
            redisConnectionFactory = new JedisConnectionFactory(sentinelConfig, redisPoolConfig());
        } else {
            redisConnectionFactory = new JedisConnectionFactory(redisPoolConfig());
            redisConnectionFactory.setShardInfo(new JedisShardInfo(redisHost, redisPort, 15000));
        }
        return redisConnectionFactory;
    }

    @Bean
    public JedisPoolConfig redisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxPoolSize);
        poolConfig.setMaxIdle(maxPoolSize);
        poolConfig.setMinIdle(minPoolSize);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWaitMillis(Duration.ofSeconds(20).toMillis());
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setSoftMinEvictableIdleTimeMillis(Duration.ofMinutes(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(-1);
        return poolConfig;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
