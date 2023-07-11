package mucheng.practice.rpc.loadbalance.loadbalancer;

import mucheng.practice.rpc.loadbalance.AbstractLoadBalance;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @author mucheng
 * @date 2023/07/11 17:30:33
 * @description 负载均衡策略：随机选择策略
 */
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected String doSelect(List<String> serviceAddresses, MyRpcRequest rpcRequest) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
