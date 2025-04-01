package org.tinycloud.tinymock.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.tinycloud.tinymock.modules.bean.entity.TDict;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2025-04-01 10:28
 */
@Repository
public interface DictMapper extends BaseMapper<TDict> {
    List<Map<String, Object>> getDictList();
}
