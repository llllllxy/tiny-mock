package org.tinycloud.tinymock.common.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>
 *     MybatisPlus配置
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-08 13:58
 */
@Configuration
@MapperScan({"org.tinycloud.tinymock.**.mapper"})
public class MybatisPlusConfig {

    private static final String WORKER_ID_KEY = "tinymock:snowflake:workerId";
    private static final String DATA_CENTER_ID_KEY = "tinymock:snowflake:dataCenterId";

    private static final long MAX_ID_VALUE = 31L; // workerId 和 dataCenterId 的最大值为 31


    /**
     * 分页拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(true);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

    @Bean
    public IdentifierGenerator idGenerator(@Autowired StringRedisTemplate stringRedisTemplate) {
        // 这样也会有问题，会造成id的浪费，在服务重启后，会浪费一个ID
        return new DefaultIdentifierGenerator(getWorkerId(stringRedisTemplate), getDataCenterId(stringRedisTemplate));
    }


    /**
     * 获取 workerId，如果 Redis 中没有，则初始化
     *
     * @param stringRedisTemplate StringRedisTemplate
     * @return workerId
     */
    private long getWorkerId(StringRedisTemplate stringRedisTemplate) {
        // 从 Redis 获取当前 workerId
        String workerIdStr = stringRedisTemplate.opsForValue().get(WORKER_ID_KEY);
        if (workerIdStr == null) {
            stringRedisTemplate.opsForValue().set(WORKER_ID_KEY, "0");
            return 0L;
        }
        long workerId = Long.parseLong(workerIdStr);

        // 每次递增 workerId
        workerId = (workerId + 1) % (MAX_ID_VALUE + 1); // 让 workerId 回绕

        // 保存新的 workerId 回 Redis
        stringRedisTemplate.opsForValue().set(WORKER_ID_KEY, String.valueOf(workerId));

        // 如果 workerId 达到上限，dataCenterId 增加 1
        if (workerId == 0) {
            incrementDataCenterId(stringRedisTemplate);
        }
        return workerId;
    }


    /**
     * 获取 dataCenterId，如果 Redis 中没有，则初始化
     *
     * @param stringRedisTemplate StringRedisTemplate
     * @return dataCenterId
     */
    private long getDataCenterId(StringRedisTemplate stringRedisTemplate) {
        // 从 Redis 获取当前 dataCenterId
        String dataCenterIdStr = stringRedisTemplate.opsForValue().get(DATA_CENTER_ID_KEY);
        if (dataCenterIdStr != null) {
            return Long.parseLong(dataCenterIdStr);
        } else {
            // Redis 中没有，则初始化 dataCenterId
            stringRedisTemplate.opsForValue().set(DATA_CENTER_ID_KEY, "0");
            return 0L;
        }
    }


    /**
     * 自增 dataCenterId，并更新 Redis
     *
     * @param stringRedisTemplate StringRedisTemplate
     */
    private void incrementDataCenterId(StringRedisTemplate stringRedisTemplate) {
        // 从 Redis 获取当前 dataCenterId
        String dataCenterIdStr = stringRedisTemplate.opsForValue().get(DATA_CENTER_ID_KEY);
        long dataCenterId = dataCenterIdStr != null ? Long.parseLong(dataCenterIdStr) : 0L;

        // 将 dataCenterId 增加 1
        dataCenterId = dataCenterId + 1L;

        // 如果 dataCenterId 超过最大值（31），则抛出异常
        if (dataCenterId > MAX_ID_VALUE) {
            throw new IllegalArgumentException("dataCenterId can't be greater than " + MAX_ID_VALUE + " or less than 0");
        }

        // 将新的 dataCenterId 存入 Redis
        stringRedisTemplate.opsForValue().set(DATA_CENTER_ID_KEY, String.valueOf(dataCenterId));
    }
}
