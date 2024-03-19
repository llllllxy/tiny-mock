package org.tinycloud.tinymock.common.config.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.tinycloud.tinymock.common.utils.DateUtils;
import org.tinycloud.tinymock.modules.service.SeqService;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/18 21:45
 */
@Slf4j
@Component
public class RedisIdWorkerSchedule {

    @Autowired
    private SeqService seqService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(initialDelay = 300 * 1000, fixedDelay = 300 * 1000)
    public void scheduled() {
        log.info("scheduled start >>> " + DateUtils.now());
        Set<String> keys = this.scanTargetKeys("icr:", 10L);
        // 更新redis内的缓存持久化到db
        for (String key : keys) {
            String value = this.stringRedisTemplate.opsForValue().get(key);
            if (StringUtils.hasText(value)) {
                String seqCode = transKey(key);
                this.seqService.updateSeqValue(seqCode, Long.parseLong(value));
            }
        }
        log.info("scheduled end >>> " + DateUtils.now());
    }


    /**
     * 将 icr:20240304:t_url_map 转换成 20240304_t_url_map
     *
     * @param input
     * @return
     */
    public static String transKey(String input) {
        // 截取第一个 ":" 后面的部分
        String afterFirstColon = input.substring(input.indexOf(":") + 1);
        // 使用 split 方法将字符串按 ":" 分割成数组
        String[] parts = afterFirstColon.split(":");
        // 拼接最后的字符串
        String output = parts[0] + "_" + parts[1];
        return output;
    }


    /**
     * 获取符合条件的key
     *
     * @param pattern 表达式
     * @return
     */
    public Set<String> scanTargetKeys(String pattern, Long scanCount) {
        Set<String> keys = new HashSet<>();
        this.scan(pattern, scanCount, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keys.add(key);
        });
        return keys;
    }


    /**
     * scan 实现
     *
     * @param pattern  表达式
     * @param consumer 对迭代到的key进行操作
     */
    public void scan(String pattern, Long scanCount, Consumer<byte[]> consumer) {
        this.stringRedisTemplate.executeWithStickyConnection((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(scanCount).match(pattern.concat("*")).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (Exception e) {
                log.info("scan Exception : ", e);
                throw new RuntimeException(e);
            }
        });
    }


}
