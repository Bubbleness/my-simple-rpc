package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mucheng
 * @date 2023/07/11 17:43:45
 * @description 负载均衡器枚举类
 */
@AllArgsConstructor
@Getter
public enum LoadBalanceEnum {

    /**
     * 负载均衡
     */
    LOAD_BALANCE("loadBalance");

    /**
     * 名称
     */
    private final String name;
}
