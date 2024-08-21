package org.tinycloud.tinymock.modules.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-2024/8/21 23:15
 */
@TableName("t_project_member")
public class TProjectMember implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 拥有者租户ID
     */
    @TableField("create_tenant_id")
    private Long createTenantId;

    /**
     * 协作者租户ID
     */
    @TableField("member_tenant_id")
    private Long memberTenantId;

    /**
     * 项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 状态标志（0--启用1--禁用）
     */
    @TableField("status")
    private Integer status;

    /**
     * 删除标志（0--未删除1--已删除）
     */
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedAt;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTenantId() {
        return createTenantId;
    }

    public void setCreateTenantId(Long createTenantId) {
        this.createTenantId = createTenantId;
    }

    public Long getMemberTenantId() {
        return memberTenantId;
    }

    public void setMemberTenantId(Long memberTenantId) {
        this.memberTenantId = memberTenantId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TProjectMember{" +
                "id=" + id +
                ", createTenantId=" + createTenantId +
                ", memberTenantId=" + memberTenantId +
                ", projectId=" + projectId +
                ", status=" + status +
                ", delFlag=" + delFlag +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", remark=" + remark +
                "}";
    }
}
