package org.tinycloud.tinymock.modules.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/24 14:52
 */
@Repository
public interface DashboardMapper {

    List<Map<String, Object>> topList(@Param("tenantId") Long tenantId, @Param("today") String today);

    List<Map<String, Object>> countByDateList(@Param("tenantId") Long tenantId, @Param("dayList") List<String> dayList);
}
