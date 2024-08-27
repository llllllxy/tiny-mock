package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.modules.bean.dto.MockAccessLogQueryDto;
import org.tinycloud.tinymock.modules.bean.entity.TMockAccessLog;
import org.tinycloud.tinymock.modules.bean.vo.MockAccessLogVo;
import org.tinycloud.tinymock.modules.mapper.MockAccessLogMapper;

import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-27 11:29
 */
@Service
public class MockAccessLogService {

    @Autowired
    private MockAccessLogMapper mockAccessLogMapper;

    public PageModel<MockAccessLogVo> query(MockAccessLogQueryDto queryParam) {
        PageModel<MockAccessLogVo> responsePage = new PageModel<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<TMockAccessLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TMockAccessLog::getMockId, queryParam.getMockId());

        Page<TMockAccessLog> logPage = this.mockAccessLogMapper.selectPage(Page.of(queryParam.getPageNo(), queryParam.getPageSize()), queryWrapper);
        if (logPage != null && !CollectionUtils.isEmpty(logPage.getRecords())) {
            responsePage.setTotalPage(logPage.getPages());
            responsePage.setTotalCount(logPage.getTotal());
            responsePage.setRecords(logPage.getRecords().stream().map(x -> {
                return BeanConvertUtils.convertTo(x, MockAccessLogVo::new);
            }).collect(Collectors.toList()));
        }

        return responsePage;
    }
}
