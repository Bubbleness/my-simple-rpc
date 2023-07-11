package mucheng.practice.rpc.registry.zk;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.enums.LoadBalanceEnum;
import mucheng.practice.rpc.enums.RpcErrorMessageEnum;
import mucheng.practice.rpc.exception.RpcException;
import mucheng.practice.rpc.extension.ExtensionLoader;
import mucheng.practice.rpc.loadbalance.LoadBalance;
import mucheng.practice.rpc.registry.ServiceDiscovery;
import mucheng.practice.rpc.registry.zk.util.CuratorUtils;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author mucheng
 * @date 2023/07/11 17:42:24
 * @description  基于zookeeper的服务发现中心
 */
@Slf4j
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {

    /**
     * 负载均衡器
     */
    private final LoadBalance loadBalance;

    public ZkServiceDiscoveryImpl() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class)
                .getExtension(LoadBalanceEnum.LOAD_BALANCE.getName());
    }

    @Override
    public InetSocketAddress lookupService(MyRpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        // 找当前Rpc服务接口的所有子节点
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (CollectionUtils.isEmpty(serviceUrlList)) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // 通过负载均衡器选择一个服务地址
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
