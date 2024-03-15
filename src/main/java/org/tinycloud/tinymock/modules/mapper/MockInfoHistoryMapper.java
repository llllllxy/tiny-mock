package org.tinycloud.tinymock.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfoHistory;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/15 21:41
 */
@Repository
public interface MockInfoHistoryMapper extends BaseMapper<TMockInfoHistory> {

    Integer selectVersion(@Param("mockId") Long mockId, @Param("projectId")Long projectId);

}
