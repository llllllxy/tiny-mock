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
 * 系统操作日志记录表实体类
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-04 14:41
 */
@Getter
@Setter
@TableName("t_operate_log")
public class TOperateLog implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键，内码
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 操作码，默认为0，业务自定义
     */
    @TableField(value = "code")
    private String code;

    /**
     * 操作名称
     */
    @TableField(value = "title")
    private String title;

    /**
     * 业务类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据）
     */
    @TableField(value = "business_type")
    private Integer businessType;

    /**
     * 请求方法名称
     */
    @TableField(value = "method")
    private String method;

    /**
     * 请求方式
     */
    @TableField(value = "request_method")
    private String requestMethod;

    /**
     * 操作类别（0其它 1管理端用户 2租户端用户）
     */
    @TableField(value = "operator_type")
    private Integer operatorType;

    /**
     * 操作人员
     */
    @TableField(value = "operator")
    private Long operator;

    /**
     * 请求url
     */
    @TableField(value = "oper_url")
    private String operUrl;

    /**
     * 操作地址IP
     */
    @TableField(value = "oper_ip")
    private String operIp;

    /**
     * 操作地点
     */
    @TableField(value = "oper_location")
    private String operLocation;

    /**
     * 请求参数
     */
    @TableField(value = "oper_param")
    private String operParam;

    /**
     * 返回参数
     */
    @TableField(value = "json_result")
    private String jsonResult;

    /**
     * 操作结果（0成功 1失败）
     */
    @TableField(value = "oper_result")
    private Integer operResult;

    /**
     * 错误消息
     */
    @TableField(value = "error_msg")
    private String errorMsg;

    /**
     * 操作时间
     */
    @TableField(value = "operate_at")
    private Date operateAt;

    /**
     * 消耗时间
     */
    @TableField(value = "cost_time")
    private Long costTime;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private Date createdAt;

    @TableField("del_flag")
    private Integer delFlag;
}
