package org.tinycloud.tinymock.modules.bean.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/11 22:06
 */
@Setter
@Getter
@ToString
public class ProjectInfoVo implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;

    private Long tenantId;

    private String projectName;

    private String introduce;

    private String path;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private String remark;
}
