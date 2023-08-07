package mucheng.practice.rpc;

import mucheng.practice.rpc.config.RpcServiceConfig;
import mucheng.practice.rpc.impl.HelloMySimpleRpcServiceImpl;
import mucheng.practice.rpc.remoting.transport.socket.SocketRpcServer;

/**
 * @author mucheng
 * @date 2023/08/07 17:26:41
 * @description 主函数 - Socket的方式
 */
public class SocketServerMain {

    public static void main(String[] args) {
        HelloMySimpleRpcService helloMySimpleRpcService = new HelloMySimpleRpcServiceImpl();
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(helloMySimpleRpcService);
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
