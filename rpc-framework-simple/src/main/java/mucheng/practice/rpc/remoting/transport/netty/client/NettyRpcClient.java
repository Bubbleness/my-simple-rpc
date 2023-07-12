package mucheng.practice.rpc.remoting.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.enums.CompressTypeEnum;
import mucheng.practice.rpc.enums.SerializationTypeEnum;
import mucheng.practice.rpc.enums.ServiceDiscoveryEnum;
import mucheng.practice.rpc.extension.ExtensionLoader;
import mucheng.practice.rpc.factory.SingletonFactory;
import mucheng.practice.rpc.registry.ServiceDiscovery;
import mucheng.practice.rpc.remoting.constants.RpcConstants;
import mucheng.practice.rpc.remoting.dto.MyRpcMessage;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;
import mucheng.practice.rpc.remoting.dto.MyRpcResponse;
import mucheng.practice.rpc.remoting.transport.RpcRequestTransport;
import mucheng.practice.rpc.remoting.transport.netty.codec.RpcMessageDecoder;
import mucheng.practice.rpc.remoting.transport.netty.codec.RpcMessageEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author mucheng
 * @date 2023/07/12 10:34:08
 * @description 基于Netty实现Rpc调用
 */
@Slf4j
public final class NettyRpcClient implements RpcRequestTransport {

    /**
     * 服务发现
     */
    private final ServiceDiscovery serviceDiscovery;
    /**
     * 服务器暂时未处理的请求
     */
    private final UnprocessedRequests unprocessedRequests;
    /**
     * 通道提供者
     */
    private final ChannelProvider channelProvider;
    /**
     * 引导Channel以供客户端使用
     */
    private final Bootstrap bootstrap;
    /**
     * 允许注册在事件循环期间进行处理以供以后选择的Channel
     */
    private final EventLoopGroup eventLoopGroup;

    public NettyRpcClient() {
        // initialize resources such as EventLoopGroup, Bootstrap
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                //  The timeout period of the connection.
                //  If this time is exceeded or the connection cannot be established, the connection fails.
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // If no data is sent to the server within 15 seconds, a heartbeat request is sent
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(new NettyRpcClientHandler());
                    }
                });
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class)
                .getExtension(ServiceDiscoveryEnum.ZK.getName());
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    /**
     * 连接服务器并获取通道，以便可以向服务器发送rpc消息
     *
     * @param inetSocketAddress 服务器地址
     * @return 通道channel
     */
    @SneakyThrows
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("The client has connected [{}] successful!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    @Override
    public Object sendRpcRequest(MyRpcRequest rpcRequest) {
        // build return value
        CompletableFuture<MyRpcResponse<Object>> resultFuture = new CompletableFuture<>();
        // get server address
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);
        // get  server address related channel
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            // put unprocessed request
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            MyRpcMessage rpcMessage = MyRpcMessage.builder().data(rpcRequest)
                    .codec(SerializationTypeEnum.HESSIAN.getCode())
                    .compress(CompressTypeEnum.GZIP.getCode())
                    .messageType(RpcConstants.REQUEST_TYPE).build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("client send message: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("Send failed:", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

    /**
     * 获取通道channel
     *
     * @param inetSocketAddress 服务器地址
     * @return Channel
     */
    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    /**
     * 关闭事件
     */
    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
