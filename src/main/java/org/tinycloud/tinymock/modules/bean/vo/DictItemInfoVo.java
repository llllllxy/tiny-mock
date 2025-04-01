package org.tinycloud.tinymock.modules.bean.vo;


import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2025-04-01 10:32
 */
@Getter
@Setter
public class DictItemInfoVo {
    /**
     * 字典码
     */
    private String dictKey;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 字典背景色
     */
    private String background;
}
