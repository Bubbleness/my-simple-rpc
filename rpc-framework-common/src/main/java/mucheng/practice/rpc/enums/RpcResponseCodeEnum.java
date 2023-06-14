package mucheng.practice.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author mucheng
 * @date 2023/06/12 20:30:17
 * @description Rpc框架响应代码的枚举类
 */
@Getter
@ToString
@AllArgsConstructor
public enum RpcResponseCodeEnum {

    /**
     * 成功调用状态
     */
    SUCCESS(200, "Rpc invoke success!"),
    /**
     * 失败调用状态
     */
    FAIL(500, "Rpc invoke filed!");

    /**
     * 响应状态码
     */
    private final int code;
    /**
     * 响应消息
     */
    private final String message;
}
