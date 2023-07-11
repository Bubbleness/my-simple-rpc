package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mucheng
 * @date 2023/07/11 16:20:01
 * @description Rpc配置枚举类
 */
@AllArgsConstructor
@Getter
public enum RpcConfigEnum {

    /**
     * Rpc的配置文件路径
     */
    RPC_CONFIG_PATH("rpc.properties"),
    /**
     * zookeeper的配置文件路径
     */
    ZK_ADDRESS("rpc.zookeeper.address");

    private final String propertyValue;
}
