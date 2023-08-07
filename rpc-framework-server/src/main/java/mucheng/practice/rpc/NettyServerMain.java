package mucheng.practice.rpc;

import mucheng.practice.rpc.annotation.RpcScan;
import mucheng.practice.rpc.config.RpcServiceConfig;
import mucheng.practice.rpc.impl.HelloMySimpleRpcServiceAgainImpl;
import mucheng.practice.rpc.remoting.transport.netty.server.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author mucheng
 * @date 2023/08/07 17:23:35
 * @description 主函数 - Netty的方式
 */
@RpcScan(basePackage = "mucheng.practice.rpc")
public class NettyServerMain {
    public static void main(String[] args) {
        // Register service via annotation
        AnnotationConfigApplicationContext applicationContext =
            new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");
        // Register service manually
        HelloMySimpleRpcService helloMySimpleRpcService = new HelloMySimpleRpcServiceAgainImpl();
        RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
            .group("RPC").version("1.0.0").service(helloMySimpleRpcService).build();
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }

}
