package org.tinycloud.tinymock.modules.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import org.tinycloud.tinymock.modules.bean.entity.TTenant;


/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2023-12-05 15:12
 */
@Repository
public interface TenantMapper extends BaseMapper<TTenant> {
}
