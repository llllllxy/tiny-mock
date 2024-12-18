package org.tinycloud.tinymock.common.config.redisson;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {

    /**
     * 模式
     */
    private String mode;

    /**
     * 通用配置-连接超时时间，单位：毫秒
     */
    private int connectTimeout = 5000;

    /**
     * 通用配置-命令等待超时，单位：毫秒
     */
    private int timeout = 3000;

    /**
     * 通用配置-密码
     */
    private String password;

    /**
     * 单机模式-连接池大小
     */
    private int connectionPoolSize = 64;

    /**
     * 单机模式，redis地址
     */
    private String address;

    /**
     * 最小空闲连接数
     */
    private int connectionMinimumIdleSize = 10;

    /**
     * 哨兵模式，从节点连接池大小
     */
    private int slaveConnectionPoolSize = 64;

    /**
     * 哨兵模式，主节点连接池大小
     */
    private int masterConnectionPoolSize = 64;


    /**
     * 哨兵模式，redis地址列表，
     */
    private String sentinelAddresses;

    /**
     * 哨兵模式，主节点名字
     */
    private String masterName;

    /**
     * 集群模式-节点地址列表
     */
    private String nodes;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    public void setConnectionPoolSize(int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public int getConnectionMinimumIdleSize() {
        return connectionMinimumIdleSize;
    }

    public void setConnectionMinimumIdleSize(int connectionMinimumIdleSize) {
        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
    }

    public int getSlaveConnectionPoolSize() {
        return slaveConnectionPoolSize;
    }

    public void setSlaveConnectionPoolSize(int slaveConnectionPoolSize) {
        this.slaveConnectionPoolSize = slaveConnectionPoolSize;
    }

    public int getMasterConnectionPoolSize() {
        return masterConnectionPoolSize;
    }

    public void setMasterConnectionPoolSize(int masterConnectionPoolSize) {
        this.masterConnectionPoolSize = masterConnectionPoolSize;
    }

    public String getSentinelAddresses() {
        return sentinelAddresses;
    }

    public void setSentinelAddresses(String sentinelAddresses) {
        this.sentinelAddresses = sentinelAddresses;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }
}
