package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mucheng
 * @date 2023/07/11 17:13:33
 * @description 服务注册枚举类
 */
@AllArgsConstructor
@Getter
public enum ServiceRegistryEnum {

    /**
     * zookeeper注册中心
     */
    ZK("zk");

    /**
     * 注册中心名称
     */
    private final String name;
}
