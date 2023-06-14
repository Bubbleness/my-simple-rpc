package mucheng.practice.rpc.remoting.transport.socket;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.registry.ServiceDiscovery;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;
import mucheng.practice.rpc.remoting.transport.RpcRequestTransport;

/**
 * @author mucheng
 * @date 2023/06/12 20:51:19
 * @description 以Socket的形式发送Rpc请求
 */
@AllArgsConstructor
@Slf4j
public class SocketRpcClient implements RpcRequestTransport {

    private final ServiceDiscovery serviceDiscovery;

    @Override
    public Object sendRpcRequest(MyRpcRequest rpcRequest) {
        return null;
    }
}