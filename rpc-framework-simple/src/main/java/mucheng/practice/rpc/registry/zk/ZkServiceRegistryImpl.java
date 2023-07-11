package mucheng.practice.rpc.registry.zk;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.registry.ServiceRegistry;
import mucheng.practice.rpc.registry.zk.util.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @author mucheng
 * @date 2023/07/11 17:40:53
 * @description 基于zookeeper的服务注册中心
 */
@Slf4j
public class ZkServiceRegistryImpl implements ServiceRegistry {

    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/"
                + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
