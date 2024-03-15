package org.tinycloud.tinymock.modules.bean.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-03-15 13:57
 */
@Setter
@Getter
public class MockInfoAddDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    @NotEmpty(message = "接口名称不能为空")
    @Length(max = 32, min = 0, message = "接口名称不能超过32个字符")
    private String mockName;

    @NotEmpty(message = "接口路径不能为空")
    @Length(max = 64, min = 0, message = "接口路径不能超过64个字符")
    private String url;

    @NotEmpty(message = "请求方式不能为空")
    @Length(max = 32, min = 0, message = "请求方式不能超过32个字符")
    private String method;

    @NotEmpty(message = "接口数据不能为空")
    private String jsonData;

    @Min(value = 0, message = "返回延时最小为0")
    private Long delay;

    @Min(value = 0, message = "`是否开启mockjs模拟随机数据`参数错误")
    private Integer mockjsFlag;

    @Length(max = 255, min = 0, message = "备注信息不能超过255个字符")
    private String remark;
}
