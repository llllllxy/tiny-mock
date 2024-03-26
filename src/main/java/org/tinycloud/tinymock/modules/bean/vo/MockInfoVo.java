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
 * @since 2024-03-14 14:34
 */
@Setter
@Getter
@ToString
public class MockInfoVo implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;

    private Long tenantId;

    private Long projectId;

    private String mockName;

    private String method;

    private String jsonData;

    private Integer mockjsFlag;

    private String url;

    private Integer status;

    private Date createdAt;

    private Date updatedAt;

    private String remark;

    private Long delay;

    private Integer httpCode;
}
