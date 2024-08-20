package org.tinycloud.tinymock.modules.bean.enums;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-08-20 13:56
 */
public enum OperateTypeEnum {

    ADD(1, "新增"),

    UPDATE(2, "更新"),

    DELETE(3, "删除");

    private final Integer code;
    private final String desc;

    OperateTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OperateTypeEnum get(Integer code) {
        for (OperateTypeEnum operateTypeEnum : OperateTypeEnum.values()) {
            if (operateTypeEnum.getCode().equals(code)) {
                return operateTypeEnum;
            }
        }
        return null;
    }

    public static String getDesc(Integer code) {
        for (OperateTypeEnum operateTypeEnum : OperateTypeEnum.values()) {
            if (operateTypeEnum.getCode().equals(code)) {
                return operateTypeEnum.getDesc();
            }
        }
        return null;
    }
}
