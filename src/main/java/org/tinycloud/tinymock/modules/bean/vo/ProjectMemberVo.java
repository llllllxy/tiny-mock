package org.tinycloud.tinymock.modules.bean.vo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-2024/8/21 23:25
 */
@Getter
@Setter
@ToString
public class ProjectMemberVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 拥有者租户ID
     */
    private Long createTenantId;

    /**
     * 协作者租户ID
     */
    private Long memberTenantId;

    /**
     * 协作者租户账户
     */
    private String memberTenantAccount;

    /**
     * 协作者租户名字
     */
    private String memberTenantName;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 备注
     */
    private String remark;

}
