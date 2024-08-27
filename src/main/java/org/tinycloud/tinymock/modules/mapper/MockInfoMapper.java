package org.tinycloud.tinymock.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.tinycloud.tinymock.modules.bean.entity.TMockInfo;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/9 15:45
 */
@Repository
public interface MockInfoMapper extends BaseMapper<TMockInfo> {
    int commonInsert(String value);

    int deleteByProjectAndTenant(@Param("projectId") Long projectId);
}
