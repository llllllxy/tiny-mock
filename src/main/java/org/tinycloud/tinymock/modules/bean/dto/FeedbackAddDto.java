package org.tinycloud.tinymock.modules.bean.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-19 15:47
 */
@Getter
@Setter
public class FeedbackAddDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "类型不能为空")
    private Integer feedType;

    @NotEmpty(message = "电子邮箱不能为空")
    @Email(message = "电子邮箱格式不正确")
    @Length(max = 255, min = 0, message = "电子邮箱不能超过255个字符")
    private String email;

    @NotEmpty(message = "反馈内容不能为空")
    @Length(max = 255, min = 0, message = "反馈内容不能超过255个字符")
    private String content;
}
