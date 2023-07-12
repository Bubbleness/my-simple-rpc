package mucheng.practice.rpc.remoting.transport.netty.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.enums.CompressTypeEnum;
import mucheng.practice.rpc.enums.SerializationTypeEnum;
import mucheng.practice.rpc.factory.SingletonFactory;
import mucheng.practice.rpc.remoting.constants.RpcConstants;
import mucheng.practice.rpc.remoting.dto.MyRpcMessage;
import mucheng.practice.rpc.remoting.dto.MyRpcResponse;

import java.net.InetSocketAddress;

/**
 * @author mucheng
 * @date 2023/07/12 11:11:02
 * @description 自定义客户端ChannelHandler来处理服务器发送的数据
 */
@Slf4j
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 服务器未处理的请求
     */
    private final UnprocessedRequests unprocessedRequests;
    /**
     * Netty客户端
     */
    private final NettyRpcClient nettyRpcClient;

    public NettyRpcClientHandler() {
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        this.nettyRpcClient = SingletonFactory.getInstance(NettyRpcClient.class);
    }

    /**
     * 读取服务器发送的消息
     *
     * @param ctx 通道处理上下文
     * @param msg 消息对象
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            log.info("Client receive msg: [{}]", msg);
            if (msg instanceof MyRpcMessage) {
                MyRpcMessage tmp = (MyRpcMessage) msg;
                byte messageType = tmp.getMessageType();
                if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                    log.info("heart [{}]", tmp.getData());
                } else if (messageType == RpcConstants.RESPONSE_TYPE) {
                    MyRpcResponse<Object> rpcResponse = (MyRpcResponse<Object>) tmp.getData();
                    unprocessedRequests.complete(rpcResponse);
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 调用ChannelHandlerContext.fireUserEventTriggered(Object)
     * 以转发到ChannelPipeline中的下一个ChannelInboundHandler
     *
     * @param ctx 通道处理上下文
     * @param evt 事件
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // 当Channel空闲时，由IdleStateHandler触发的用户事件
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            // 进入空闲等待
            if (state == IdleState.WRITER_IDLE) {
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                MyRpcMessage rpcMessage = new MyRpcMessage();
                rpcMessage.setCodec(SerializationTypeEnum.PROTOSTUFF.getCode());
                rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
                rpcMessage.setMessageType(RpcConstants.HEARTBEAT_REQUEST_TYPE);
                rpcMessage.setData(RpcConstants.PING);
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 处理客户端消息发生异常时调用
     *
     * @param ctx   通道处理上下文
     * @param cause 异常
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("client catch exception：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
