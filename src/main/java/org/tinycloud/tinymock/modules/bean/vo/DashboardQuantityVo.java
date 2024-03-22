package org.tinycloud.tinymock.modules.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-02-29 16:27
 */
@Getter
@Setter
public class DashboardQuantityVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long projectQuantity;

    private Long mockQuantity;

    private Long accessQuantity;

    private Long inviteesQuantity;
}
