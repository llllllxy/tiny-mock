package org.tinycloud.tinymock.modules.bean.dto;

import lombok.Getter;
import lombok.Setter;
import org.tinycloud.tinymock.common.model.BasePageDto;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-04-2024/4/9 21:58
 */
@Getter
@Setter
public class StatisticQueryDto extends BasePageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String mockName;

    private Long projectId;

    private Long tenantId;

    private String today;

    private String month;

    private String yesterday;
}
