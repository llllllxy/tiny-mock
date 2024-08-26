package org.tinycloud.tinymock.modules.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/20 21:46
 */
@Getter
@Setter
@TableName("t_invitees_info")
public class TInviteesInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 自增主键，内码
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;


    @TableField("tenant_id")
    private Long tenantId;

    @TableField("invitation_code")
    private String invitationCode;

    @TableField("invitees_tenant_id")
    private Long inviteesTenantId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    /**
     * 创建时间
     */
    @TableField(value = "updated_at")
    private Date updatedAt;

    /**
     * 删除标志（0--未删除1--已删除）
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    /**
     * 备注描述信息
     */
    @TableField(value = "remark")
    private String remark;
}
