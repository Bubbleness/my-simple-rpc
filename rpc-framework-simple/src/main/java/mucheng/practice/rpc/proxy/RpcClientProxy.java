package mucheng.practice.rpc.proxy;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.config.RpcServiceConfig;
import mucheng.practice.rpc.enums.RpcErrorMessageEnum;
import mucheng.practice.rpc.enums.RpcResponseCodeEnum;
import mucheng.practice.rpc.exception.RpcException;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;
import mucheng.practice.rpc.remoting.dto.MyRpcResponse;
import mucheng.practice.rpc.remoting.transport.RpcRequestTransport;
import mucheng.practice.rpc.remoting.transport.netty.client.NettyRpcClient;
import mucheng.practice.rpc.remoting.transport.socket.SocketRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author mucheng
 * @date 2023/07/13 20:48:42
 * @description Rpc客户端代理 动态代理类。动态代理对象调用方法时，实际上调用的是下面的invoke方法。
 * 正是因为有了动态代理，客户端调用远程方法就像调用本地方法一样（屏蔽了中间过程）
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private static final String INTERFACE_NAME = "interfaceName";

    /**
     * 用于向服务器发送请求
     */
    private final RpcRequestTransport rpcRequestTransport;
    /**
     * Rpc服务配置
     */
    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = new RpcServiceConfig();
    }

    /**
     * 获取代理类对象
     *
     * @param clazz 类型
     * @return T clazz类型的对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, this);
    }

    /**
     * This method is actually called when you use a proxy object to call a method.
     * The proxy object is the object you get through the getProxy method.
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("invoked method: [{}]", method.getName());
        MyRpcRequest rpcRequest = MyRpcRequest.builder().methodName(method.getName())
            .parameters(args)
            .interfaceName(method.getDeclaringClass().getName())
            .paramTypes(method.getParameterTypes())
            .requestId(UUID.randomUUID().toString())
            .group(rpcServiceConfig.getGroup())
            .version(rpcServiceConfig.getVersion())
            .build();
        MyRpcResponse<Object> rpcResponse = null;
        if (rpcRequestTransport instanceof NettyRpcClient) {
            CompletableFuture<MyRpcResponse<Object>> completableFuture = (CompletableFuture<MyRpcResponse<Object>>)
                rpcRequestTransport.sendRpcRequest(rpcRequest);
            rpcResponse = completableFuture.get();
        }
        if (rpcRequestTransport instanceof SocketRpcClient) {
            rpcResponse = (MyRpcResponse<Object>)rpcRequestTransport.sendRpcRequest(rpcRequest);
        }
        this.check(rpcResponse, rpcRequest);
        return rpcResponse.getData();
    }

    /**
     * 校验远程调用结果
     *
     * @param rpcResponse rpc响应结果
     * @param rpcRequest  rpc请求信息
     */
    private void check(MyRpcResponse<Object> rpcResponse, MyRpcRequest rpcRequest) {
        // 请求结果为null （rpc服务调用失败）
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,
                INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        // 请求和返回的响应不匹配
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorMessageEnum.REQUEST_NOT_MATCH_RESPONSE,
                INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        // 服务调用失败
        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCodeEnum.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_INVOCATION_FAILURE,
                INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
