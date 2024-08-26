package org.tinycloud.tinymock.modules.bean.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-2024/8/26 19:57
 */
@Data
public class ProjectMemberAddDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    @NotNull(message = "协作人ID不能为空")
    private Long memberTenantId;
}
