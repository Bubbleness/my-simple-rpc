package mucheng.practice.rpc.remoting.transport.netty.client;

import mucheng.practice.rpc.remoting.dto.MyRpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mucheng
 * @date 2023/07/12 10:36:57
 * @description 服务器未处理的请求
 */
public class UnprocessedRequests {

    /**
     * 服务器未处理的请求
     */
    private static final Map<String, CompletableFuture<MyRpcResponse<Object>>>
            UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    /**
     * 往缓存中添加服务器未处理的请求
     *
     * @param requestId 请求ID
     * @param future    未处理的请求
     */
    public void put(String requestId, CompletableFuture<MyRpcResponse<Object>> future) {
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
    }

    /**
     * 完成一个未处理的请求
     *
     * @param rpcResponse 请求的响应结果
     */
    public void complete(MyRpcResponse<Object> rpcResponse) {
        CompletableFuture<MyRpcResponse<Object>> future =
                UNPROCESSED_RESPONSE_FUTURES.remove(rpcResponse.getRequestId());
        if (null != future) {
            future.complete(rpcResponse);
        } else {
            throw new IllegalStateException();
        }
    }
}
