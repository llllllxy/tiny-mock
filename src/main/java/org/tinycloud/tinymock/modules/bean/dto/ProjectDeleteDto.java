package org.tinycloud.tinymock.modules.bean.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-11-05 10:50
 */
@Getter
@Setter
public class ProjectDeleteDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "项目id不能为空")
    private Long id;

    @NotEmpty(message = "项目名称不能为空")
    @Length(max = 64, min = 1, message = "项目名称不能超过64个字符")
    private String projectName;
}
