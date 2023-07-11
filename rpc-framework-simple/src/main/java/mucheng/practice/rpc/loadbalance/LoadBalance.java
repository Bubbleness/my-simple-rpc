package mucheng.practice.rpc.loadbalance;

import mucheng.practice.rpc.extension.SPI;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;

import java.util.List;

/**
 * @author mucheng
 * @date 2023/07/11 17:25:18
 * @description 负载均衡
 */
@SPI
public interface LoadBalance {

    /**
     * 从现有服务地址列表中选择一个
     *
     * @param serviceUrlList 现有的服务地址列表
     * @param rpcRequest     Rpc请求
     * @return 目标服务器地址
     */
    String selectServiceAddress(List<String> serviceUrlList, MyRpcRequest rpcRequest);
}
