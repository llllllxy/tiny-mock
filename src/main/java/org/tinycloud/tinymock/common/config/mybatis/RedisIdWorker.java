package org.tinycloud.tinymock.common.config.mybatis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.tinycloud.tinymock.common.utils.StrUtils;
import org.tinycloud.tinymock.modules.service.SeqService;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * <p>
 * 注： js Number 类型最大数值：9007199254740992，之后便会损失精度
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-04 17:46
 */
@Component
public class RedisIdWorker {

    /**
     * 缓存的失效时间，缓存30天
     */
    private static final long expireTime = 30 * 24 * 60 * 60;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Qualifier("asyncServiceExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private SeqService seqService;


    public Long nextId(String tableName) {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        // 获取今天是今年的第几天
        int dayOfYear = now.getDayOfYear();
        // 天数补零至三位数，如 64 -> 064，为啥要补到三位数，因为最多就只有365天
        String today = year + padWithZero(dayOfYear, 3);

        String keyPrefix = "icr:" + today + ":" + tableName;
        Long current = this.stringRedisTemplate.opsForValue().increment(keyPrefix);
        // 如果=1，说明redis里面没有，这时候要查一下数据库里有没有，有的话得刷到redis里面，防止redis里数据丢失
        if (current == 1L) {
            Long seqValue = this.seqService.getSeqValue(today + "_" + tableName);
            if (Objects.nonNull(seqValue)) {
                current = seqValue;
                this.stringRedisTemplate.opsForValue().set(keyPrefix, String.valueOf(current));
            }
        }

        // 每次都刷新一下缓存的时候，防止服务器时间被前移
        threadPoolTaskExecutor.execute(() -> {
            this.stringRedisTemplate.expire(keyPrefix, expireTime, TimeUnit.SECONDS);
        });
        String number = padWithZero(current, 9);
        // today-7位， number-9位（最大值999999999，一天这么多个ID也够用了，不够的话就再补充长度）
        // 相加共16位，如2024064000000090
        return Long.parseLong(today + number + StrUtils.randomNumber(3));
    }

    /**
     * 获取指定位数的数字字符串，不足补0
     *
     * @param nextIdValue 数字
     * @param numLen      最终总位数
     * @return 补零后的数字
     */
    private static String padWithZero(long nextIdValue, int numLen) {
        StringBuilder sb = new StringBuilder();
        String nextIdValueString = String.valueOf(nextIdValue);
        int needLen = numLen - nextIdValueString.length();
        for (int i = 0; i < needLen; i++) {
            sb.append("0");
        }
        return sb.append(nextIdValueString).toString();
    }
}
