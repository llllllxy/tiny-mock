package org.tinycloud.tinymock.modules.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.tinycloud.tinymock.common.config.interceptor.TenantHolder;
import org.tinycloud.tinymock.common.model.PageModel;
import org.tinycloud.tinymock.common.utils.DateUtils;
import org.tinycloud.tinymock.modules.bean.dto.StatisticQueryDto;
import org.tinycloud.tinymock.modules.bean.vo.StatisticDataVo;
import org.tinycloud.tinymock.modules.mapper.StatisticMapper;

import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-04-2024/4/9 21:54
 */
@Service
public class StatisticService {

    @Autowired
    private StatisticMapper statisticMapper;

    public PageModel<StatisticDataVo> query(StatisticQueryDto dto) {
        PageModel<StatisticDataVo> responsePage = new PageModel<>(dto.getPageNo(), dto.getPageSize());
        dto.setToday(DateUtils.today());
        dto.setYesterday(DateUtils.getYesterday());
        dto.setMonth(DateUtils.format(new Date(), "yyyy-MM"));

        IPage<StatisticDataVo> page = this.statisticMapper.pageList(Page.of(dto.getPageNo(), dto.getPageSize()), dto);
        if (page != null && !CollectionUtils.isEmpty(page.getRecords())) {
            responsePage.setTotalPage(page.getPages());
            responsePage.setTotalCount(page.getTotal());
            responsePage.setRecords(page.getRecords());
        }
        return responsePage;
    }
}
