package mucheng.practice.rpc.remoting.transport;

import mucheng.practice.rpc.extension.SPI;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;

/**
 * @author mucheng
 * @date 2023/06/12 20:47:16
 * @description Rpc框架请求传输类
 */
@SPI
public interface RpcRequestTransport {

    /**
     * 发送Rpc请求并获取服务器的响应结果
     *
     * @param rpcRequest Rpc请求实体
     * @return 响应结果
     */
    Object sendRpcRequest(MyRpcRequest rpcRequest);
}

