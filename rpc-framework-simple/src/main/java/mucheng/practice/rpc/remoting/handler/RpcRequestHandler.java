package mucheng.practice.rpc.remoting.handler;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.exception.RpcException;
import mucheng.practice.rpc.factory.SingletonFactory;
import mucheng.practice.rpc.provider.ServiceProvider;
import mucheng.practice.rpc.provider.impl.ZkServiceProviderImpl;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author mucheng
 * @date 2023/07/11 16:30:18
 * @description Rpc请求处理器
 */
@Slf4j
public class RpcRequestHandler {

    /**
     * 服务提供者
     */
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * 处理rpcRequest：调用对应的方法，然后返回该方法
     *
     * @param rpcRequest Rpc请求
     * @return Rpc处理后的返回对象
     */
    public Object handle(MyRpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 调用Rpc服务获取返回结果
     *
     * @param rpcRequest Rpc请求
     * @param service    目标服务
     * @return 目标方法执行的结果
     */
    private Object invokeTargetMethod(MyRpcRequest rpcRequest, Object service) {
        Object result;
        try {
            // 根据Rpc请求中的方法名称获取到目标服务类的对应方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            // 通过反射调用对应的目标方法获取返回结果
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] successful invoke method:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
