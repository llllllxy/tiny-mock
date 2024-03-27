package org.tinycloud.tinymock.modules.bean.dto;

import lombok.Getter;
import lombok.Setter;
import org.tinycloud.tinymock.common.model.BasePageDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-27 11:31
 */
@Getter
@Setter
public class MockAccessLogQueryDto extends BasePageDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "接口id不能为空")
    private Long mockId;

}
