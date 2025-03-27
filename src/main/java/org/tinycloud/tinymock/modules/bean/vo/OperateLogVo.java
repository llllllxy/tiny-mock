package org.tinycloud.tinymock.modules.bean.vo;

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
 * @since 2025-03-27 14:17
 */
@Getter
@Setter
public class OperateLogVo  implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键，内码
     */
    private Long id;

    /**
     * 操作码，默认为0，业务自定义
     */
    private String code;

    /**
     * 操作名称
     */
    private String title;

    /**
     * 业务类型（0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据）
     */
    private Integer businessType;

    /**
     * 请求方法名称
     */
    private String method;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 操作类别（0其它 1管理端用户 2租户端用户）
     */
    private Integer operatorType;

    /**
     * 操作人员
     */
    private Long operator;

    /**
     * 请求url
     */
    private String operUrl;

    /**
     * 操作地址IP
     */
    private String operIp;

    /**
     * 操作地点
     */
    private String operLocation;

    /**
     * 请求参数
     */
    private String operParam;

    /**
     * 返回参数
     */
    private String jsonResult;

    /**
     * 操作结果（0成功 1失败）
     */
    private Integer operResult;

    /**
     * 错误消息
     */
    private String errorMsg;

    /**
     * 操作时间
     */
    private String operateAt;

    /**
     * 消耗时间
     */
    private Long costTime;

    /**
     * 创建时间
     */
    private Date createdAt;

    private Integer delFlag;

    /**
     * 审计哈希
     */
    private String auditHash;
}
