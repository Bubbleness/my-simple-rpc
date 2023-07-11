package mucheng.practice.rpc.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.config.CustomShutdownHook;
import mucheng.practice.rpc.config.RpcServiceConfig;
import mucheng.practice.rpc.factory.SingletonFactory;
import mucheng.practice.rpc.provider.ServiceProvider;
import mucheng.practice.rpc.provider.impl.ZkServiceProviderImpl;
import mucheng.practice.rpc.utils.concurrent.threadpool.ThreadPoolFactoryUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import static mucheng.practice.rpc.constdata.GlobalConstData.NETTY_RPC_SERVER_PORT;

/**
 * @author mucheng
 * @date 2023/07/11 14:25:05
 * @description Socket服务端 接受Rpc请求
 */
@Slf4j
public class SocketRpcServer {

    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;


    public SocketRpcServer() {
        threadPool = ThreadPoolFactoryUtil.createCustomThreadPoolIfAbsent("socket-server-rpc-pool");
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }

    /**
     * 注册Rpc服务
     *
     * @param rpcServiceConfig Rpc服务配置
     */
    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    /**
     *
     *
     * @return
     */
    public void start() {
        
        try (ServerSocket server = new ServerSocket()) {
            String host = InetAddress.getLocalHost().getHostAddress();
            server.bind(new InetSocketAddress(host, NETTY_RPC_SERVER_PORT));
            // 清除之前设置的zk注册信息
            CustomShutdownHook.getCustomShutdownHook().clearAll();
            Socket socket;
            while ((socket = server.accept()) != null) {
                log.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("occur IOException:", e);
        }
    }
}
