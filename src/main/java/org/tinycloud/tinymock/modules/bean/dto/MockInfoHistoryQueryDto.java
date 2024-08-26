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
 * @since 2024-03-2024/3/17 16:02
 */
@Getter
@Setter
public class MockInfoHistoryQueryDto extends BasePageDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "接口id不能为空")
    private Long mockId;
}
