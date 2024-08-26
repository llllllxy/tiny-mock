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
 * @since 2024-03-27 11:31
 */
@Setter
@Getter
@ToString
public class MockAccessLogVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -1L;

    private Date accessTime;

    private String accessIp;

    private String accessAddress;

    private String accessUserAgent;

    private Date createdAt;
}
