package org.tinycloud.tinymock.common.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.tinycloud.tinymock.common.utils.RedisUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * <p>
 * MybatisPlus配置
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-08 13:58
 */
@Slf4j
@Configuration
@MapperScan({"org.tinycloud.tinymock.**.mapper"})
public class MybatisPlusConfig {

    private static final String WORK_NODE_MAP_KEY = "tinymock:worknode_map";

    private static long WORKER_ID;

    private static long DATACENTER_ID;

    @Autowired
    private RedisUtils redisUtils;

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

//    @Bean
//    public IdentifierGenerator idGenerator(@Autowired StringRedisTemplate stringRedisTemplate) {
//        // 初始化Redis脚本，设置脚本来源为classpath下的chooseWorkIdLua.lua文件
//        DefaultRedisScript redisScript = new DefaultRedisScript();
//        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/chooseWorkIdLua.lua")));
//        List<Long> luaResultList = null;
//        try {
//            // 设置脚本的返回类型为List类。
//            redisScript.setResultType(List.class);
//            // 执行Redis Lua脚本，获取结果。
//            luaResultList = (ArrayList) stringRedisTemplate.execute(redisScript, null);
//        } catch (Exception ex) {
//            log.error("Redis Lua 脚本获取 WorkId 失败", ex);
//        }
//        if (!CollectionUtils.isEmpty(luaResultList)) {
//            return new DefaultIdentifierGenerator(luaResultList.get(0), luaResultList.get(1));
//        } else {
//            return new DefaultIdentifierGenerator(0L, 0L);
//        }
//    }

    /**
     * <p>
     * 自定义ID生成器配置，手动给雪花算法赋datacenterId和workerId，依赖于redis进行节点发号
     * 需搭配下面的两个定时任务一起使用，有自动注册和清除机制
     * </p>
     *
     * @return IdentifierGenerator
     */
    @Bean
    public IdentifierGenerator idGenerator() {
        log.info("Initialization snowflake start!");

        Map<Object, Object> allIdMap = redisUtils.hmGet(WORK_NODE_MAP_KEY);
        List<Long> totalIdList;
        if (allIdMap == null || allIdMap.isEmpty()) {
            totalIdList = new ArrayList<>();
        } else {
            totalIdList = allIdMap.keySet().stream().map(key -> Long.parseLong(key.toString())).collect(Collectors.toList());
        }
        log.info("Initialization snowflake totalIdList:" + totalIdList);

        boolean condition = true;
        long datacenterId = 0L;
        long workerId = 0L;
        while (condition) {
            long nextTotalId;
            do {
                // 重新获取
                nextTotalId = ThreadLocalRandom.current().nextLong(IdExtractor.MAX_TOTAL_ID + 1);
            } while (totalIdList.contains(nextTotalId));

            datacenterId = IdExtractor.extractDatacenterId((int) nextTotalId);
            workerId = IdExtractor.extractWorkerId((int) nextTotalId);
            try {
                // 第二步、插入数据库，能插成功就行，
                redisUtils.hSet(WORK_NODE_MAP_KEY, String.valueOf(nextTotalId), System.currentTimeMillis());
                condition = false;
            } catch (Exception e) {
                log.error("Initialization snowflake Exception : ", e);
                throw e;
            }
        }
        log.info("Initialization snowflake end!");
        MybatisPlusConfig.WORKER_ID = workerId;
        MybatisPlusConfig.DATACENTER_ID = datacenterId;
        return new DefaultIdentifierGenerator(workerId, datacenterId);
    }


    /**
     * 节点自动注册刷新定时器
     * 延迟60秒，后续每120秒执行一次
     */
    @Scheduled(initialDelay = 1000 * 60, fixedDelay = 1000 * 120)
    public void workNodeRefreshJob() {
        log.info("workNodeRefresh job start in {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        long nextTotalId = IdExtractor.generateTotalId((int) DATACENTER_ID, (int) WORKER_ID);
        redisUtils.hSet(WORK_NODE_MAP_KEY, String.valueOf(nextTotalId), System.currentTimeMillis());
        log.info("workNodeRefresh job end in {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    /**
     * 节点自动清除定时器（24小时内未刷新的视为无效节点，进行清除）
     * 延迟120秒，后续每1小时执行一次
     */
    @Scheduled(initialDelay = 1000 * 120, fixedDelay = 1000 * 3600)
    public void workNodeCleanJob() {
        log.info("workNodeClean job start in {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        Map<Object, Object> allIdMap = redisUtils.hmGet(WORK_NODE_MAP_KEY);
        if (allIdMap == null || allIdMap.isEmpty()) {
            return;
        }
        allIdMap.forEach((hashKey, value) -> {
            long time = Long.parseLong(value.toString());
            // 一天的毫秒数（24小时 * 60分钟 * 60秒 * 1000毫秒）
            long oneDayMillis = 24 * 60 * 60 * 1000;
            // 已经一天时间没更新的，清除掉
            if (System.currentTimeMillis() - time > oneDayMillis) {
                redisUtils.hDel(WORK_NODE_MAP_KEY, hashKey);
            }
        });

        log.info("workNodeClean job end in {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
