package mucheng.practice.rpc.remoting.dto;

import lombok.*;
import mucheng.rpc.framework.enums.RpcResponseCodeEnum;

import java.io.Serializable;

/**
 * @author mucheng
 * @date 2023/06/12 20:27:05
 * @description Rpc框架响应实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MyRpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 715145628363022097L;

    /**
     * Rpc请求ID
     */
    private String requestId;
    /**
     * 响应代码
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应的数据
     */
    private T data;

    /**
     * 响应成功
     *
     * @param data      响应的数据
     * @param requestId 请求ID
     * @return Rpc响应实体
     */
    public static <T> MyRpcResponse<T> success(T data, String requestId) {
        MyRpcResponse<T> response = new MyRpcResponse<>();
        response.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    /**
     * 响应失败
     *
     * @param rpcResponseCodeEnum 响应状态枚举类
     * @return Rpc响应实体
     */
    public static <T> MyRpcResponse<T> fail(RpcResponseCodeEnum rpcResponseCodeEnum) {
        MyRpcResponse<T> response = new MyRpcResponse<>();
        response.setCode(rpcResponseCodeEnum.getCode());
        response.setMessage(rpcResponseCodeEnum.getMessage());
        return response;
    }
}
