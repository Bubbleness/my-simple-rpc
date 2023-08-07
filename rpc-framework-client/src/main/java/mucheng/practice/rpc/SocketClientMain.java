package mucheng.practice.rpc;

import mucheng.practice.rpc.config.RpcServiceConfig;
import mucheng.practice.rpc.proxy.RpcClientProxy;
import mucheng.practice.rpc.remoting.transport.RpcRequestTransport;
import mucheng.practice.rpc.remoting.transport.socket.SocketRpcClient;

/**
 * @author mucheng
 * @date 2023/08/07 17:11:09
 * @description 主函数 - Socket的方式
 */
public class SocketClientMain {

    public static void main(String[] args) {
        RpcRequestTransport rpcRequestTransport = new SocketRpcClient();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
        HelloMySimpleRpcService helloMySimpleRpcService = rpcClientProxy.getProxy(HelloMySimpleRpcService.class);
        String hello = helloMySimpleRpcService.sayHelloToRpc(
            new Hello("客户端以「Socket」方式调用远程服务", "哈哈哈哈"));
        System.out.println(hello);
    }
}
