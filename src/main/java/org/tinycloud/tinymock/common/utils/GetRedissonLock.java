package org.tinycloud.tinymock.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.tinycloud.tinymock.common.enums.CoreErrorCode;
import org.tinycloud.tinymock.common.exception.BusinessException;
import org.tinycloud.tinymock.common.exception.CoreException;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * <p>
 * redisson分布式锁工具类
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-18 10:51
 */
@Slf4j
public class GetRedissonLock {

    public static <T> T execute(Supplier<T> supplier, String lockName, int waitTime, int leaseTime) {
        RedissonClient redissonClient = SpringContextUtils.getBean(RedissonClient.class);
        boolean tryLock = false;
        RLock rLock = null;
        try {
            rLock = redissonClient.getLock(lockName);
            tryLock = rLock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new CoreException(CoreErrorCode.GET_REDISSON_LOCK_FAILED);
        }
        if (tryLock) {
            try {
                return supplier.get();
            } catch (Exception e) {
                log.error("execute failed：", e);
                if (e instanceof BusinessException) {
                    throw new BusinessException(((BusinessException) e).getCode(), ((BusinessException) e).getMessage());
                } else {
                    throw new RuntimeException(e);
                }
            } finally {
                if (Objects.nonNull(rLock) && rLock.isLocked() && rLock.isHeldByCurrentThread()) {
                    rLock.unlock();
                }
            }
        } else {
            throw new CoreException(CoreErrorCode.GET_REDISSON_LOCK_FAILED);
        }
    }

}
