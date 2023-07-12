package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mucheng
 * @date 2023/07/12 11:50:51
 * @description 压缩类型枚举类
 */
@AllArgsConstructor
@Getter
public enum CompressTypeEnum {

    /**
     * gzip的压缩方式
     */
    GZIP((byte) 0x01, "gzip");

    /**
     * 标识代码
     */
    private final byte code;
    /**
     * 压缩方式名称
     */
    private final String name;

    /**
     * 根据code获取具体的压缩方式
     *
     * @param code 标识
     * @return 压缩方式的名称
     */
    public static String getName(byte code) {
        for (CompressTypeEnum c : CompressTypeEnum.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }
}
