package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mucheng
 * @date 2023/07/11 11:53:49
 * @description 服务发现枚举类
 */
@AllArgsConstructor
@Getter
public enum ServiceDiscoveryEnum {

    /**
     * zookeeper 作为服务发现
     */
    ZK("zk");

    /**
     * 服务发现名称
     */
    private final String name;
}
