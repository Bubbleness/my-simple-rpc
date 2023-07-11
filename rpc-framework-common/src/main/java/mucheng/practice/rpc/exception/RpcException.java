package mucheng.practice.rpc.exception;

import mucheng.practice.rpc.enums.RpcErrorMessageEnum;

/**
 * @author mucheng
 * @date 2023/07/11 14:16:58
 * @description 自定义RPC调用异常处理类
 */
public class RpcException extends RuntimeException{

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorMessageEnum rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
