package org.tinycloud.tinymock.modules.bean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-2024/8/22 23:17
 */
@Data
public class TenantInfoChooseVo  implements Serializable {
    private static final long serialVersionUID = -1L;

    private Long id;

    private String tenantAccount;

    private String tenantName;
}
