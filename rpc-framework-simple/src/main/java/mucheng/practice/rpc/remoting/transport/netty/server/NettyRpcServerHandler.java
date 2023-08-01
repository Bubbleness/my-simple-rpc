package mucheng.practice.rpc.remoting.transport.netty.server;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.enums.CompressTypeEnum;
import mucheng.practice.rpc.enums.RpcResponseCodeEnum;
import mucheng.practice.rpc.enums.SerializationTypeEnum;
import mucheng.practice.rpc.factory.SingletonFactory;
import mucheng.practice.rpc.remoting.constants.RpcConstants;
import mucheng.practice.rpc.remoting.dto.MyRpcMessage;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;
import mucheng.practice.rpc.remoting.dto.MyRpcResponse;
import mucheng.practice.rpc.remoting.handler.RpcRequestHandler;

/**
 * @author mucheng
 * @date 2023/07/13 19:11:21
 * @description 自定义服务端的ChannelHandler来处理客户端发送的数据
 */
@Slf4j
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * Rpc请求处理器
     */
    private final RpcRequestHandler rpcRequestHandler;

    public NettyRpcServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            if (msg instanceof MyRpcMessage) {
                log.info("server receive msg: [{}] ", msg);
                byte messageType = ((MyRpcMessage) msg).getMessageType();
                MyRpcMessage rpcMessage = new MyRpcMessage();
                rpcMessage.setCodec(SerializationTypeEnum.HESSIAN.getCode());
                rpcMessage.setCompress(CompressTypeEnum.GZIP.getCode());
                if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
                    rpcMessage.setMessageType(RpcConstants.HEARTBEAT_RESPONSE_TYPE);
                    rpcMessage.setData(RpcConstants.PONG);
                } else {
                    MyRpcRequest rpcRequest = (MyRpcRequest) ((MyRpcMessage) msg).getData();
                    // Execute the target method (the method the client needs to execute)
                    // and return the method result
                    Object result = rpcRequestHandler.handle(rpcRequest);
                    log.info(String.format("server get result: %s", result.toString()));
                    rpcMessage.setMessageType(RpcConstants.RESPONSE_TYPE);
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        MyRpcResponse<Object> rpcResponse = MyRpcResponse.success(result, rpcRequest.getRequestId());
                        rpcMessage.setData(rpcResponse);
                    } else {
                        MyRpcResponse<Object> rpcResponse = MyRpcResponse.fail(RpcResponseCodeEnum.FAIL);
                        rpcMessage.setData(rpcResponse);
                        log.error("not writable now, message dropped");
                    }
                }
                ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } finally {
            //Ensure that ByteBuf is released, otherwise there may be memory leaks
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
