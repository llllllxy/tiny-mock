package org.tinycloud.tinymock.modules.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tinycloud.tinymock.modules.bean.dto.StatisticQueryDto;
import org.tinycloud.tinymock.modules.bean.vo.StatisticDataVo;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-04-2024/4/9 21:55
 */
@Repository
public interface StatisticMapper {
    IPage<StatisticDataVo> pageList(Page page, @Param("queryDto") StatisticQueryDto queryDto);
}
