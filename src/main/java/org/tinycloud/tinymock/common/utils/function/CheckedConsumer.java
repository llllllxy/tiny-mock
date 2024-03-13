package org.tinycloud.tinymock.common.utils.function;

import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * <p>
 * 函数式接口
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-13 16:39
 */
@FunctionalInterface
public interface CheckedConsumer<T> extends Serializable {

    /**
     * Run the Consumer
     *
     * @param var1 T
     * @throws Throwable UncheckedException
     */

    @Nullable
    void accept(@Nullable T var1) throws Throwable;
}