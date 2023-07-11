package mucheng.practice.rpc.registry;

import mucheng.practice.rpc.extension.SPI;

import java.net.InetSocketAddress;

/**
 * @author mucheng
 * @date 2023/07/11 17:10:54
 * @description 服务注册类
 */
@SPI
public interface ServiceRegistry {

    /**
     * 服务注册
     *
     * @param rpcServiceName    Rpc服务名称
     * @param inetSocketAddress 服务器地址
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
