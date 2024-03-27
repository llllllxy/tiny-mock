package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.enums.TenantErrorCode;
import org.tinycloud.tinymock.common.exception.TenantException;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.common.utils.BeanConvertUtils;
import org.tinycloud.tinymock.modules.bean.dto.MockInfoAddDto;
import org.tinycloud.tinymock.modules.bean.dto.MockInfoEditDto;
import org.tinycloud.tinymock.modules.bean.dto.MockInfoQueryDto;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfo;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfoHistory;
import org.tinycloud.tinymock.modules.bean.vo.MockInfoVo;
import org.tinycloud.tinymock.modules.mapper.MockInfoHistoryMapper;
import org.tinycloud.tinymock.modules.mapper.MockInfoMapper;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-14 11:26
 */
@Service
public class MockInfoService {

    @Autowired
    private MockInfoMapper mockInfoMapper;

    @Autowired
    private MockInfoHistoryMapper mockInfoHistoryMapper;

    public PageModel<MockInfoVo> query(MockInfoQueryDto queryParam) {
        PageModel<MockInfoVo> responsePage = new PageModel<>(queryParam.getPageNo(), queryParam.getPageSize());

        LambdaQueryWrapper<TMockInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED);
        queryWrapper.eq(TMockInfo::getTenantId, TenantHolder.getTenantId());
        queryWrapper.eq(TMockInfo::getProjectId, queryParam.getProjectId());
        queryWrapper.like(StringUtils.hasLength(queryParam.getUrl()), TMockInfo::getUrl, queryParam.getUrl());
        queryWrapper.like(StringUtils.hasLength(queryParam.getMockName()), TMockInfo::getMockName, queryParam.getMockName());

        Page<TMockInfo> logPage = this.mockInfoMapper.selectPage(Page.of(queryParam.getPageNo(), queryParam.getPageSize()), queryWrapper);

        if (logPage != null && !CollectionUtils.isEmpty(logPage.getRecords())) {
            responsePage.setTotalPage(logPage.getPages());
            responsePage.setTotalCount(logPage.getTotal());
            responsePage.setRecords(logPage.getRecords().stream().map(x -> {
                return BeanConvertUtils.convertTo(x, MockInfoVo::new);
            }).collect(Collectors.toList()));
        }

        return responsePage;
    }

    public MockInfoVo detail(Long id) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(
                Wrappers.<TMockInfo>lambdaQuery().eq(TMockInfo::getTenantId, TenantHolder.getTenantId())
                        .eq(TMockInfo::getId, id)
                        .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED));
        return BeanConvertUtils.convertTo(mockInfo, MockInfoVo::new);
    }

    public Boolean delete(Long id) {
        // 逻辑删除
        LambdaUpdateWrapper<TMockInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TMockInfo::getId, id);
        wrapper.set(TMockInfo::getDelFlag, GlobalConstant.DELETED);
        int rows = this.mockInfoMapper.update(null, wrapper);
        return rows > 0;
    }


    public Boolean enable(Long id) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getId, id));
        if (Objects.isNull(mockInfo)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NOT_EXIST);
        }
        if (mockInfo.getStatus().equals(GlobalConstant.ENABLED)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_IS_ENABLE);
        }
        LambdaUpdateWrapper<TMockInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TMockInfo::getId, id);
        wrapper.set(TMockInfo::getStatus, GlobalConstant.ENABLED);
        int rows = this.mockInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    public Boolean disable(Long id) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getId, id));
        if (Objects.isNull(mockInfo)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NOT_EXIST);
        }
        if (mockInfo.getStatus().equals(GlobalConstant.DISABLED)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_IS_DISABLE);
        }
        LambdaUpdateWrapper<TMockInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TMockInfo::getId, id);
        wrapper.set(TMockInfo::getStatus, GlobalConstant.DISABLED);
        int rows = this.mockInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    public Boolean add(MockInfoAddDto dto) {
        TMockInfo tMockInfo = new TMockInfo();
        tMockInfo.setTenantId(TenantHolder.getTenantId());
        tMockInfo.setDelFlag(GlobalConstant.NOT_DELETED);
        tMockInfo.setStatus(GlobalConstant.ENABLED);
        tMockInfo.setRemark(dto.getRemark());
        tMockInfo.setMockName(dto.getMockName());
        tMockInfo.setMethod(dto.getMethod());
        tMockInfo.setDelay(dto.getDelay());
        tMockInfo.setUrl(dto.getUrl());
        tMockInfo.setJsonData(dto.getJsonData());
        tMockInfo.setProjectId(dto.getProjectId());
        tMockInfo.setMockjsFlag(dto.getMockjsFlag());
        tMockInfo.setHttpCode(dto.getHttpCode());
        int rows = this.mockInfoMapper.insert(tMockInfo);
        return rows > 0;
    }

    @Transactional
    public Boolean edit(MockInfoEditDto dto) {
        TMockInfo mockInfo = this.mockInfoMapper.selectOne(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getId, dto.getId()));
        if (Objects.isNull(mockInfo)) {
            throw new TenantException(TenantErrorCode.TENANT_MOCKINFO_NOT_EXIST);
        }
        // 插入旧的数据到历史版本表中
        this.saveHistory(mockInfo);

        LambdaUpdateWrapper<TMockInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(TMockInfo::getId, dto.getId());
        wrapper.set(StringUtils.hasLength(dto.getRemark()), TMockInfo::getRemark, dto.getRemark());
        wrapper.set(TMockInfo::getMockName, dto.getMockName());
        wrapper.set(TMockInfo::getMethod, dto.getMethod());
        wrapper.set(Objects.nonNull(dto.getDelay()), TMockInfo::getDelay, dto.getDelay());
        wrapper.set(Objects.nonNull(dto.getHttpCode()), TMockInfo::getHttpCode, dto.getHttpCode());
        wrapper.set(TMockInfo::getUrl, dto.getUrl());
        wrapper.set(TMockInfo::getJsonData, dto.getJsonData());
        wrapper.set(TMockInfo::getMockjsFlag, dto.getMockjsFlag());

        int rows = this.mockInfoMapper.update(null, wrapper);
        return rows > 0;
    }

    public void saveHistory(TMockInfo mockInfo) {
        Integer version = this.mockInfoHistoryMapper.selectVersion(mockInfo.getId(), mockInfo.getProjectId());
        if (Objects.isNull(version)) {
            version = 1;
        } else {
            version = version + 1;
        }
        TMockInfoHistory tMockInfoHistory = new TMockInfoHistory();
        tMockInfoHistory.setMockId(mockInfo.getId());
        tMockInfoHistory.setTenantId(mockInfo.getTenantId());
        tMockInfoHistory.setProjectId(mockInfo.getProjectId());
        tMockInfoHistory.setVersion(version);
        tMockInfoHistory.setMockName(mockInfo.getMockName());
        tMockInfoHistory.setMethod(mockInfo.getMethod());
        tMockInfoHistory.setDelay(mockInfo.getDelay());
        tMockInfoHistory.setMockjsFlag(mockInfo.getMockjsFlag());
        tMockInfoHistory.setJsonData(mockInfo.getJsonData());
        tMockInfoHistory.setUrl(mockInfo.getUrl());
        tMockInfoHistory.setRemark(mockInfo.getRemark());
        tMockInfoHistory.setHttpCode(mockInfo.getHttpCode());
        this.mockInfoHistoryMapper.insert(tMockInfoHistory);
    }
}
