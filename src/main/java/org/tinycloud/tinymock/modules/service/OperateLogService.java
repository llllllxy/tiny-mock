package org.tinycloud.tinymock.modules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.modules.bean.entity.TOperateLog;
import org.tinycloud.tinymock.modules.mapper.OperateLogMapper;


/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-04 15:24
 */
@Service
public class OperateLogService {

    @Qualifier("asyncServiceExecutor")
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private OperateLogMapper operateLogMapper;

    public void insert(TOperateLog operLog) {
        this.threadPoolTaskExecutor.execute(() -> {
            // 插入操作日志
            this.operateLogMapper.insert(operLog);
        });
    }
}
