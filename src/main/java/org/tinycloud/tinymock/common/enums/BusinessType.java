package org.tinycloud.tinymock.common.enums;

/**
 * <p>
 * 业务操作类型
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-04 14:59
 */
public enum BusinessType {
    /**
     * 其它
     */
    OTHER(0),

    /**
     * 新增
     */
    INSERT(1),

    /**
     * 修改
     */
    UPDATE(2),

    /**
     * 删除
     */
    DELETE(3),

    /**
     * 授权
     */
    GRANT(4),

    /**
     * 导出
     */
    EXPORT(5),

    /**
     * 导入
     */
    IMPORT(6),

    /**
     * 强退
     */
    FORCE(7),

    /**
     * 生成代码
     */
    GENCODE(8),

    /**
     * 清空数据
     */
    CLEAN(9),

    /**
     * 清空数据
     */
    ENABLE(10),

    /**
     * 清空数据
     */
    DISABLE(11),

    ;

    private Integer code;

    private BusinessType(Integer code) {
        this.code = code;
    }

    private BusinessType() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
