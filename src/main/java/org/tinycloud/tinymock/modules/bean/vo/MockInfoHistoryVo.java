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
 * @since 2024-03-2024/3/17 16:02
 */
@Setter
@Getter
@ToString
public class MockInfoHistoryVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    private Long id;

    private Long mockId;

    private Integer version;

    private Long tenantId;

    private Long projectId;

    private String mockName;

    private String method;

    private String jsonData;

    private Integer mockjsFlag;

    private String url;

    private String remark;

    private Long delay;

    private Date createdAt;

    private String operator;

    private Long operatorId;

    private String operateTypeName;

    private Integer operateType;
}
