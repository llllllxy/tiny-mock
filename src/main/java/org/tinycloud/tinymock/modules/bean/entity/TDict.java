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
 * @since 2025-04-01 10:25
 */
@Getter
@Setter
@TableName("t_dict")
public class TDict implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 字典标识编码
     */
    @TableField("dict_code")
    private String dictCode;

    /**
     * 字典名称
     */
    @TableField("dict_name")
    private String dictName;

    /**
     * 字典码
     */
    @TableField("dict_key")
    private String dictKey;

    /**
     * 字典值
     */
    @TableField("dict_value")
    private String dictValue;

    /**
     * 字典背景色
     */
    @TableField("background")
    private String background;

    /**
     * 备注描述信息
     */
    @TableField("remark")
    private String remark;

    /**
     * 排序
     */
    @TableField("order_num")
    private Integer orderNum;

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
     * 创建人-对应t_user.id
     */
    @TableField("created_by")
    private Long createdBy;

    /**
     * 更新人-对应t_user.id
     */
    @TableField("updated_by")
    private Long updatedBy;
}
