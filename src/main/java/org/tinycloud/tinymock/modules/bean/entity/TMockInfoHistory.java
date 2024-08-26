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
 * mock数据历史记录表实体类
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-2024/3/15 21:38
 */
@Getter
@Setter
@TableName("t_mock_info_history")
public class TMockInfoHistory implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("mock_id")
    private Long mockId;

    @TableField("version")
    private Integer version;

    @TableField("tenant_id")
    private Long tenantId;

    @TableField("operator_id")
    private Long operatorId;

    @TableField("operate_type")
    private Integer operateType;

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

    @TableField("remark")
    private String remark;

    @TableField("delay")
    private Long delay;

    @TableField("created_at")
    private Date createdAt;

    @TableField("http_code")
    private Integer httpCode;
}
