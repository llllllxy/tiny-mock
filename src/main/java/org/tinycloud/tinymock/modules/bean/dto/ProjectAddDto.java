package org.tinycloud.tinymock.modules.bean.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

@Getter
@Setter
public class ProjectAddDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "项目名称不能为空")
    @Length(max = 64, min = 1, message = "项目名称不能超过64个字符")
    private String projectName;

    @NotEmpty(message = "项目路径不能为空")
    @Length(max = 32, min = 1, message = "项目路径不能超过32个字符")
    private String path;

    @Length(max = 255, min = 0, message = "项目介绍不能超过255个字符")
    private String introduce;

    @Length(max = 255, min = 0, message = "备注信息不能超过255个字符")
    private String remark;
}
