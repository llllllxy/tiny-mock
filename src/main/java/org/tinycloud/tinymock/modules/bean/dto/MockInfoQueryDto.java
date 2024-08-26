package org.tinycloud.tinymock.modules.bean.dto;

import lombok.Getter;
import lombok.Setter;
import org.tinycloud.tinymock.common.model.BasePageDto;

import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-14 14:30
 */
@Getter
@Setter
public class MockInfoQueryDto extends BasePageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "项目id不能为空")
    private Long projectId;

    private String mockName;

    private String url;
}
