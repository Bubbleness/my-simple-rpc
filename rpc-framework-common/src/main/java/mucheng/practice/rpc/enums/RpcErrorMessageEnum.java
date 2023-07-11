package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author mucheng
 * @date 2023/07/11 14:17:52
 * @description RPC框架调用失败消息枚举类
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcErrorMessageEnum {

    /**
     * 客户端连接服务端失败
     */
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接服务端失败"),
    /**
     * 服务调用失败
     */
    SERVICE_INVOCATION_FAILURE("服务调用失败"),
    /**
     * 没有找到指定的服务
     */
    SERVICE_CAN_NOT_BE_FOUND("没有找到指定的服务"),
    /**
     * 注册的服务没有实现任何接口
     */
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务没有实现任何接口"),
    /**
     * 返回结果错误！请求和返回的相应不匹配
     */
    REQUEST_NOT_MATCH_RESPONSE("返回结果错误！请求和返回的相应不匹配");

    /**
     * 提示文案
     */
    private final String message;
}
