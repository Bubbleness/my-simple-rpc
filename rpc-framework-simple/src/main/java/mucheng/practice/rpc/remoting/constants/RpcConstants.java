package mucheng.practice.rpc.remoting.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author mucheng
 * @date 2023/07/12 11:24:25
 * @description Rpc调用的相关常量
 */
public class RpcConstants {

    /**
     * 魔法数字
     */
    public static final byte[] MAGIC_NUMBER = {(byte) 'g', (byte) 'r', (byte) 'p', (byte) 'c'};
    /**
     * 默认编码
     */
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    /**
     * 版本信息
     */
    public static final byte VERSION = 1;
    /**
     * 总长度
     */
    public static final byte TOTAL_LENGTH = 16;
    /**
     * 请求类型
     */
    public static final byte REQUEST_TYPE = 1;
    /**
     * 响应类型
     */
    public static final byte RESPONSE_TYPE = 2;
    /**
     * 心跳请求类型
     */
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    /**
     * 心跳响应类型
     */
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;
    /**
     * 头部长度
     */
    public static final int HEAD_LENGTH = 16;
    /**
     * ping命令
     */
    public static final String PING = "ping";
    /**
     * pong命令
     */
    public static final String PONG = "pong";
    /**
     * 最大框架长度
     */
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;
}
