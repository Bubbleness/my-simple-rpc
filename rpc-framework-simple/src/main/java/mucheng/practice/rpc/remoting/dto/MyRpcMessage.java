package mucheng.practice.rpc.remoting.dto;

import lombok.*;

/**
 * @author mucheng
 * @date 2023/07/12 11:22:17
 * @description Rpc消息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class MyRpcMessage {

    /**
     * Rpc消息类型
     */
    private byte messageType;
    /**
     * 序列化类型
     */
    private byte codec;
    /**
     * 压缩类型
     */
    private byte compress;
    /**
     * 请求ID
     */
    private int requestId;
    /**
     * 请求数据
     */
    private Object data;
}
