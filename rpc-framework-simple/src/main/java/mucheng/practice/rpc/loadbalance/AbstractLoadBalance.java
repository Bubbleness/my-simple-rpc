package mucheng.practice.rpc.loadbalance;

import mucheng.practice.rpc.remoting.dto.MyRpcRequest;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author mucheng
 * @date 2023/07/11 17:26:57
 * @description 抽象负载均衡器类
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String selectServiceAddress(List<String> serviceAddresses, MyRpcRequest rpcRequest) {
        if (CollectionUtils.isEmpty(serviceAddresses)) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcRequest);
    }

    /**
     * 从服务器地址列表中选择一个服务地址
     *
     * @param serviceAddresses 服务器地址列表
     * @param rpcRequest       Rpc请求
     * @return 其中一个服务器地址列表
     */
    protected abstract String doSelect(List<String> serviceAddresses, MyRpcRequest rpcRequest);
}
