package org.tinycloud.tinymock.common.config.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


/**
 * <p>
 *   redisson配置类
 *   使用文档：https://github.com/redisson/redisson/wiki/8.-distributed-locks-and-synchronizers
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-03-07 15:47:38
 */
@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfig {

    @Autowired
    private RedissonProperties redissonProperties;

    /**
     * 单机模式自动装配
     *
     * @return RedissonClient
     */
    @Bean("redissonSingle")
    @ConditionalOnProperty(prefix = "redisson", name = "mode", havingValue = "single")
    public RedissonClient redissonSingle() {
        Config config = new Config();
        // useSingleServer 单机模式
        // useClusterServers 集群模式
        // useMasterSlaveServers 主从模式
        // useSentinelServers 哨兵模式
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redissonProperties.getAddress())
                .setTimeout(redissonProperties.getTimeout())
                .setConnectTimeout(redissonProperties.getConnectTimeout())
                .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
        if (StringUtils.hasText(redissonProperties.getPassword())) {
            serverConfig.setPassword(redissonProperties.getPassword());
        }
        // 单机模式下设置数据库编号
        serverConfig.setDatabase(0);
        return Redisson.create(config);
    }


    /**
     * 哨兵模式自动装配
     *
     * @return RedissonClient
     */
    @Bean("redissonSentinel")
    @ConditionalOnProperty(prefix = "redisson", name = "mode", havingValue = "sentinel")
    public RedissonClient redissonSentinel() {
        // 解析redis地址列表
        String[] addressArray = redissonProperties.getSentinelAddresses().split(",");
        Config config = new Config();
        SentinelServersConfig serverConfig = config.useSentinelServers()
                .addSentinelAddress(addressArray)
                .setMasterName(redissonProperties.getMasterName())
                .setTimeout(redissonProperties.getTimeout())
                .setConnectTimeout(redissonProperties.getConnectTimeout())
                .setMasterConnectionPoolSize(redissonProperties.getMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(redissonProperties.getSlaveConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize())
                .setSlaveConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
        if (StringUtils.hasText(redissonProperties.getPassword())) {
            serverConfig.setPassword(redissonProperties.getPassword());
        }
        // 哨兵模式下设置数据库编号
        serverConfig.setDatabase(0);
        return Redisson.create(config);
    }


    /**
     * 集群模式自动装配
     *
     * @return RedissonClient
     */
    @Bean("redissonCluster")
    @ConditionalOnProperty(prefix = "redisson", name = "mode", havingValue = "cluster")
    public RedissonClient redissonCluster() {
        // 解析redis地址列表
        String[] noteArray = redissonProperties.getNodes().split(",");
        Config config = new Config();
        ClusterServersConfig serverConfig = config.useClusterServers()
                .addNodeAddress(noteArray)
                .setTimeout(redissonProperties.getTimeout())
                .setConnectTimeout(redissonProperties.getConnectTimeout())
                .setMasterConnectionPoolSize(redissonProperties.getMasterConnectionPoolSize())
                .setSlaveConnectionPoolSize(redissonProperties.getSlaveConnectionPoolSize())
                .setMasterConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize())
                .setSlaveConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
        if (StringUtils.hasText(redissonProperties.getPassword())) {
            serverConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }

}
