package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.modules.bean.dto.OperateLogQueryDto;
import org.tinycloud.tinymock.modules.bean.entity.TOperateLog;
import org.tinycloud.tinymock.modules.bean.vo.OperateLogVo;
import org.tinycloud.tinymock.modules.mapper.OperateLogMapper;

import java.util.Objects;
import java.util.stream.Collectors;


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
            // 异步插入操作日志
            this.operateLogMapper.insert(operLog);
        });
    }

    public PageModel<OperateLogVo> query(OperateLogQueryDto queryParam) {
        PageModel<OperateLogVo> responsePage = new PageModel<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<TOperateLog> queryWrapper = new LambdaQueryWrapper<>();
        Long tenantId = TenantHolder.getTenantId();
        queryWrapper.eq(TOperateLog::getOperator, tenantId);

        queryWrapper.like(StringUtils.hasLength(queryParam.getTitle()), TOperateLog::getTitle, queryParam.getTitle());
        queryWrapper.like(StringUtils.hasLength(queryParam.getCode()), TOperateLog::getCode, queryParam.getCode());
        queryWrapper.eq(Objects.nonNull(queryParam.getOperResult()), TOperateLog::getOperResult, queryParam.getOperResult());

        queryWrapper.ge(StringUtils.hasLength(queryParam.getStartTime()), TOperateLog::getOperateAt, queryParam.getStartTime() + " 00:00:00");
        queryWrapper.le(StringUtils.hasLength(queryParam.getEndTime()), TOperateLog::getOperateAt, queryParam.getEndTime() + " 23:59:59");

        Page<TOperateLog> page = this.operateLogMapper.selectPage(Page.of(queryParam.getPageNo(), queryParam.getPageSize()), queryWrapper);

        if (page != null && !CollectionUtils.isEmpty(page.getRecords())) {
            responsePage.setTotalPage(page.getPages());
            responsePage.setTotalCount(page.getTotal());
            responsePage.setRecords(page.getRecords().stream().map(x -> {
                OperateLogVo vo = new OperateLogVo();
                BeanUtils.copyProperties(x, vo);
                return vo;
            }).collect(Collectors.toList()));
        }

        return responsePage;
    }
}
