package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mucheng
 * @date 2023/08/01 20:28:10
 * @description rpc发生请求枚举类
 */
@AllArgsConstructor
@Getter
public enum RpcRequestTransportEnum {

    /**
     * 以Netty发送
     */
    NETTY("netty"),
    /**
     * 以Socket发送
     */
    SOCKET("socket");

    private final String name;
}
