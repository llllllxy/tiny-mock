package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.modules.bean.dto.MockInfoHistoryQueryDto;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfoHistory;
import org.tinycloud.tinymock.modules.bean.vo.MockInfoHistoryVo;
import org.tinycloud.tinymock.modules.mapper.MockInfoHistoryMapper;

import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/17 15:59
 */
@Service
public class MockInfoHistoryService {

    @Autowired
    private MockInfoHistoryMapper mockInfoHistoryMapper;


    public PageModel<MockInfoHistoryVo> query(MockInfoHistoryQueryDto queryParam) {
        PageModel<MockInfoHistoryVo> responsePage = new PageModel<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<TMockInfoHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TMockInfoHistory::getTenantId, TenantHolder.getTenantId());
        queryWrapper.eq(TMockInfoHistory::getMockId, queryParam.getMockId());

        Page<TMockInfoHistory> logPage = this.mockInfoHistoryMapper.selectPage(Page.of(queryParam.getPageNo(), queryParam.getPageSize()), queryWrapper);

        if (logPage != null && !CollectionUtils.isEmpty(logPage.getRecords())) {
            responsePage.setTotalPage(logPage.getPages());
            responsePage.setTotalCount(logPage.getTotal());
            responsePage.setRecords(logPage.getRecords().stream().map(x -> {
                return BeanConvertUtils.convertTo(x, MockInfoHistoryVo::new);
            }).collect(Collectors.toList()));
        }

        return responsePage;
    }
}
