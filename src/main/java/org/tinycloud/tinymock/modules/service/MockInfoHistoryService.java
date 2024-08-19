package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.modules.bean.dto.MockInfoHistoryQueryDto;
import org.tinycloud.tinymock.modules.bean.entity.TMailConfig;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfoHistory;
import org.tinycloud.tinymock.modules.bean.entity.TTenant;
import org.tinycloud.tinymock.modules.bean.vo.MockInfoHistoryVo;
import org.tinycloud.tinymock.modules.mapper.MockInfoHistoryMapper;
import org.tinycloud.tinymock.modules.mapper.TenantMapper;

import java.util.List;
import java.util.Map;
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

    @Autowired
    private TenantMapper tenantMapper;


    public PageModel<MockInfoHistoryVo> query(MockInfoHistoryQueryDto queryParam) {
        PageModel<MockInfoHistoryVo> responsePage = new PageModel<>(queryParam.getPageNo(), queryParam.getPageSize());
        LambdaQueryWrapper<TMockInfoHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TMockInfoHistory::getTenantId, TenantHolder.getTenantId());
        queryWrapper.eq(TMockInfoHistory::getMockId, queryParam.getMockId());

        Page<TMockInfoHistory> historyPage = this.mockInfoHistoryMapper.selectPage(Page.of(queryParam.getPageNo(), queryParam.getPageSize()), queryWrapper);

        if (historyPage != null && !CollectionUtils.isEmpty(historyPage.getRecords())) {
            List<Long> operatorIdList = historyPage.getRecords().stream().map(TMockInfoHistory::getOperatorId)
                    .distinct()
                    .collect(Collectors.toList());

            List<TTenant> tenantList = this.tenantMapper.selectList(Wrappers.<TTenant>lambdaQuery()
                    .select(TTenant::getId, TTenant::getTenantAccount)
                    .eq(TTenant::getDelFlag, GlobalConstant.NOT_DELETED)
                    .in(TTenant::getId, operatorIdList));

            Map<Long, TTenant> tenantMap = tenantList.stream()
                    .collect(Collectors.toMap(TTenant::getId, tenant -> tenant));
            // 等价于下面这种写法
            // Map<String, TTenant> tenantMap = tenantList.stream().collect(Collectors.toMap(person -> TTenant::getId, Function.identity()));

            responsePage.setTotalPage(historyPage.getPages());
            responsePage.setTotalCount(historyPage.getTotal());
            responsePage.setRecords(historyPage.getRecords().stream().map(x -> {
                MockInfoHistoryVo vo = new MockInfoHistoryVo();
                BeanUtils.copyProperties(x, vo);
                vo.setOperator(tenantMap.get(vo.getOperatorId()) == null ? "未知" : tenantMap.get(vo.getOperatorId()).getTenantAccount());
                return vo;
            }).collect(Collectors.toList()));
        }

        return responsePage;
    }
}
