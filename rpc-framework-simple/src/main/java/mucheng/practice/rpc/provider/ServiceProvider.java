package mucheng.practice.rpc.provider;

import mucheng.practice.rpc.config.RpcServiceConfig;

/**
 * @author mucheng
 * @date 2023/07/11 14:37:59
 * @description Rpc框架服务提供者 存储和提供服务对象
 */
public interface ServiceProvider {

    /**
     * 添加Rpc服务
     *
     * @param rpcServiceConfig Rpc服务相关配置
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * 根据Rpc服务名称获取服务对象
     *
     * @param rpcServiceName Rpc服务名称
     * @return 服务对象
     */
    Object getService(String rpcServiceName);

    /**
     * 发布Rpc服务
     *
     * @param rpcServiceConfig Rpc服务相关配置
     */
    void publishService(RpcServiceConfig rpcServiceConfig);
}
