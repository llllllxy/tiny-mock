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
 * @since 2025-03-27 14:17
 */
@Getter
@Setter
public class OperateLogQueryDto extends BasePageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 操作结果
     */
    private Integer operResult;

    /**
     * 业务名称
     */
    private String title;

    /**
     * 业务码
     */
    private String code;

    /**
     * 操作时间
     */
    private String startTime;

    /**
     * 操作时间
     */
    private String endTime;
}

