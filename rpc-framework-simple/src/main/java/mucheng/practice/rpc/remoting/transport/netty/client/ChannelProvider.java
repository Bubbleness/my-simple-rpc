package mucheng.practice.rpc.remoting.transport.netty.client;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mucheng
 * @date 2023/07/12 11:07:06
 * @description 存储和提供Channel对象
 */
@Slf4j
public class ChannelProvider {

    /**
     * 通道Channel缓存
     */
    private final Map<String, Channel> channelMap;

    public ChannelProvider() {
        channelMap = new ConcurrentHashMap<>();
    }


    /**
     * 根据请求地址来获取一个Channel对象
     *
     * @param inetSocketAddress 网络请求地址
     * @return Channel对象
     */
    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        // determine if there is a connection for the corresponding address
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            // if so, determine if the connection is available, and if so, get it directly
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        return null;
    }

    /**
     * 添加缓存
     *
     * @param inetSocketAddress 网络请求地址
     * @param channel           Channel对象
     */
    public void set(InetSocketAddress inetSocketAddress, Channel channel) {
        String key = inetSocketAddress.toString();
        channelMap.put(key, channel);
    }

    /**
     * 移除缓存
     *
     * @param inetSocketAddress 网络请求地址
     */
    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
        log.info("Channel map size :[{}]", channelMap.size());
    }
}
