package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.constant.GlobalConstant;
import org.tinycloud.tinymock.common.utils.DateUtils;
import org.tinycloud.tinymock.modules.bean.entity.TInviteesInfo;
import org.tinycloud.tinymock.modules.bean.entity.TMockAccessLog;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfo;
import org.tinycloud.tinymock.modules.bean.entity.TProjectInfo;
import org.tinycloud.tinymock.modules.bean.vo.DashboardQuantityVo;
import org.tinycloud.tinymock.modules.mapper.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-22 15:32
 */
@Service
public class DashboardService {

    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    @Autowired
    private MockInfoMapper mockInfoMapper;

    @Autowired
    private InviteesInfoMapper inviteesInfoMapper;

    @Autowired
    private MockAccessLogMapper mockAccessLogMapper;

    @Autowired
    private DashboardMapper dashboardMapper;


    public DashboardQuantityVo quantity() {
        Long tenantId = TenantHolder.getTenantId();
        Long projectQuantity = this.projectInfoMapper.selectCount(Wrappers.<TProjectInfo>lambdaQuery()
                .eq(TProjectInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TProjectInfo::getTenantId, tenantId));
        Long mockQuantity = this.mockInfoMapper.selectCount(Wrappers.<TMockInfo>lambdaQuery()
                .eq(TMockInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TMockInfo::getTenantId, tenantId));
        Long inviteesQuantity = this.inviteesInfoMapper.selectCount(Wrappers.<TInviteesInfo>lambdaQuery()
                .eq(TInviteesInfo::getDelFlag, GlobalConstant.NOT_DELETED)
                .eq(TInviteesInfo::getTenantId, tenantId));
        Long accessQuantity = this.mockAccessLogMapper.selectCount(Wrappers.<TMockAccessLog>lambdaQuery()
                .eq(TMockAccessLog::getTenantId, tenantId));
        DashboardQuantityVo vo = new DashboardQuantityVo();

        vo.setAccessQuantity(accessQuantity);
        vo.setMockQuantity(mockQuantity);
        vo.setInviteesQuantity(inviteesQuantity);
        vo.setProjectQuantity(projectQuantity);
        return vo;
    }

    public List<Map<String, Object>> topList() {
        Long tenantId = TenantHolder.getTenantId();
        String today = DateUtils.today();
        return this.dashboardMapper.topList(tenantId, today);
    }

    public Map<String, Object> chartsInfo() {
        Map<String, Object> result = new HashMap<>();
        Long tenantId = TenantHolder.getTenantId();

        List<String> dayList = new ArrayList<>();
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 格式化输出的日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dayList.add(currentDate.format(formatter));
        // 获取前七天的日期并格式化输出
        for (int i = 1; i < 7; i++) {
            LocalDate previousDate = currentDate.minusDays(i);
            dayList.add(previousDate.format(formatter));
        }
        // 倒序 ArrayList
        Collections.reverse(dayList);
        result.put("dayList", dayList);
        List<Map<String, Object>> list = this.dashboardMapper.countByDateList(tenantId, dayList);
        List<Long> dataList = list.stream().map(item -> {
            return Long.parseLong(item.get("accessCount").toString());
        }).collect(Collectors.toList());
        result.put("dataList", dataList);
        return result;
    }
}
