package org.tinycloud.tinymock.modules.bean.vo;


import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2025-04-01 10:30
 */
@Getter
@Setter
public class DictInfoVo {

    /**
     * 字典标识编码
     */
    private String dictCode;

    /**
     * 字典名称
     */
    private String dictName;

    private List<DictItemInfoVo> list;

    private HashMap<String, DictItemInfoVo> map;
}
