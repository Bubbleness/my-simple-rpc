package mucheng.practice.rpc.registry;

import mucheng.practice.rpc.extension.SPI;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;

import java.net.InetSocketAddress;

/**
 * @author mucheng
 * @date 2023/06/14 21:31:27
 * @description 服务发现类
 */
@SPI
public interface ServiceDiscovery {

    /**
     * 根据Rpc调用服务的接口名称查询服务
     *
     * @param rpcRequest Rpc请求
     * @return Socket套接字
     */
    InetSocketAddress lookupService(MyRpcRequest rpcRequest);
}
