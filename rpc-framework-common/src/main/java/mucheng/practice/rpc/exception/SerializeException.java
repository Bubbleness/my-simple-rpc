package mucheng.practice.rpc.exception;

/**
 * @author mucheng
 * @date 2023/07/12 15:13:02
 * @description 序列化异常处理
 */
public class SerializeException extends RuntimeException {

    public SerializeException(String message) {
        super(message);
    }
}
