package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mucheng
 * @date 2023/07/12 11:48:55
 * @description 序列化类型枚举
 */
@AllArgsConstructor
@Getter
public enum SerializationTypeEnum {

    /**
     * kyro序列化方式
     */
    KYRO((byte) 0x01, "kyro"),
    /**
     * proto stuff序列化方式
     */
    PROTOSTUFF((byte) 0x02, "protostuff"),
    /**
     * hessian序列化方式
     */
    HESSIAN((byte) 0X03, "hessian");

    /**
     * 标识代码
     */
    private final byte code;
    /**
     * 名称
     */
    private final String name;

    /**
     * 根据code获取具体的序列化方式
     *
     * @param code 标识
     * @return 序列化方式的名称
     */
    public static String getName(byte code) {
        for (SerializationTypeEnum c : SerializationTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }
}
