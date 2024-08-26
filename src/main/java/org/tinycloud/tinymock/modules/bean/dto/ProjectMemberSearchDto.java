package org.tinycloud.tinymock.modules.bean.dto;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-2024/8/22 23:19
 */
@Data
public class ProjectMemberSearchDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    private String keyword;
}
