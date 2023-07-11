package mucheng.practice.rpc.provider.impl;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.config.RpcServiceConfig;
import mucheng.practice.rpc.enums.RpcErrorMessageEnum;
import mucheng.practice.rpc.enums.ServiceRegistryEnum;
import mucheng.practice.rpc.exception.RpcException;
import mucheng.practice.rpc.extension.ExtensionLoader;
import mucheng.practice.rpc.provider.ServiceProvider;
import mucheng.practice.rpc.registry.ServiceRegistry;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static mucheng.practice.rpc.constdata.GlobalConstData.NETTY_RPC_SERVER_PORT;

/**
 * @author mucheng
 * @date 2023/07/11 17:15:02
 * @description zookeeper作为服务提供者
 */
@Slf4j
public class ZkServiceProviderImpl implements ServiceProvider {

    /**
     * 目标服务对象缓存
     * key: Rpc服务名称(interface name + version + group)
     * value: 服务对象
     */
    private final Map<String, Object> serviceMap;
    /**
     * 已经注册的服务
     */
    private final Set<String> registeredService;
    /**
     * 服务注册
     */
    private final ServiceRegistry serviceRegistry;

    public ZkServiceProviderImpl() {
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class)
                .getExtension(ServiceRegistryEnum.ZK.getName());
    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getRpcServiceName();
        if (registeredService.contains(rpcServiceName)) {
            return;
        }
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        log.info("Add service: {} and interfaces:{}", rpcServiceName,
                rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (null == service) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND);
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getRpcServiceName(),
                    new InetSocketAddress(host, NETTY_RPC_SERVER_PORT));
        } catch (UnknownHostException e) {
            log.error("occur exception when getHostAddress", e);
        }
    }
}
