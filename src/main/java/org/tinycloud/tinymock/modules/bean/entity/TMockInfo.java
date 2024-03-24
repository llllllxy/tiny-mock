package org.tinycloud.tinymock.modules.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/9 15:09
 */
@Getter
@Setter
@TableName("t_mock_info")
public class TMockInfo implements Serializable {
    private static final long serialVersionUID = -1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("tenant_id")
    private Long tenantId;

    @TableField("project_id")
    private Long projectId;

    @TableField("mock_name")
    private String mockName;

    @TableField("method")
    private String method;

    @TableField("json_data")
    private String jsonData;

    @TableField("mockjs_flag")
    private Integer mockjsFlag;

    @TableField("url")
    private String url;

    @TableField("status")
    private Integer status;

    @TableField("del_flag")
    private Integer delFlag;

    @TableField("created_at")
    private Date createdAt;

    @TableField("updated_at")
    private Date updatedAt;

    @TableField("remark")
    private String remark;

    @TableField("delay")
    private Long delay;

    @TableField("http_code")
    private Integer httpCode;
}
