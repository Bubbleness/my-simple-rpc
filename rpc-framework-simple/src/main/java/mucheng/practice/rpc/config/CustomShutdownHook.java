package mucheng.practice.rpc.config;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.registry.zk.util.CuratorUtils;
import mucheng.practice.rpc.utils.concurrent.threadpool.ThreadPoolFactoryUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static mucheng.practice.rpc.constdata.GlobalConstData.NETTY_RPC_SERVER_PORT;

/**
 * @author mucheng
 * @date 2023/07/11 15:36:04
 * @description 服务器关闭后的处理类
 */
@Slf4j
public class CustomShutdownHook {

    private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

    public static CustomShutdownHook getCustomShutdownHook() {
        return CUSTOM_SHUTDOWN_HOOK;
    }

    /**
     * 注销所有的服务
     *
     * @return
     */
    public void clearAll() {
        log.info("addShutdownHook for clearAll");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                InetSocketAddress inetSocketAddress =
                        new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NETTY_RPC_SERVER_PORT);
                CuratorUtils.clearRegistry(CuratorUtils.getZkClient(), inetSocketAddress);
            } catch (UnknownHostException ignored) {
            }
            ThreadPoolFactoryUtil.shutDownAllThreadPool();
        }));
    }
}
