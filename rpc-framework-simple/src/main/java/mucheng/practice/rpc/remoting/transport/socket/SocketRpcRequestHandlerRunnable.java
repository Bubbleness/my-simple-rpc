package mucheng.practice.rpc.remoting.transport.socket;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.factory.SingletonFactory;
import mucheng.practice.rpc.remoting.dto.MyRpcRequest;
import mucheng.practice.rpc.remoting.dto.MyRpcResponse;
import mucheng.practice.rpc.remoting.handler.RpcRequestHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author mucheng
 * @date 2023/07/11 16:24:23
 * @description Socket请求处理线程
 */
@Slf4j
public class SocketRpcRequestHandlerRunnable implements Runnable {

    /**
     * socket套接字
     */
    private final Socket socket;
    /**
     * Rpc请求处理器
     */
    private final RpcRequestHandler rpcRequestHandler;


    public SocketRpcRequestHandlerRunnable(Socket socket) {
        this.socket = socket;
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
    }

    @Override
    public void run() {
        log.info("server handle message from client by thread: [{}]", Thread.currentThread().getName());
        /**
         * Java 7引入了一种新的try语法，称为“try-with-resources”，
         * 允许在try语句中自动关闭实现了java.lang.AutoCloseable接口的资源。
         * 在这种语法中，try语句后面的括号中包含一个或多个资源对象，
         * 这些资源对象将在try语句块执行完毕后自动关闭。
         * 这种语法的目的是确保资源被正确关闭，从而避免资源泄漏。
         *
         * try (Resource1 resource1 = new Resource1();
         *      Resource2 resource2 = new Resource2()) {
         *     // 使用资源的代码
         * } catch (Exception e) {
         *     // 处理异常的代码
         * }
         */
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {

            // 从Socket中读取出Rpc请求
            MyRpcRequest rpcRequest = (MyRpcRequest) objectInputStream.readObject();
            // 从Rpc请求中获取调用的方法等信息
            Object result = rpcRequestHandler.handle(rpcRequest);
            // 将调用方法处理结果写会给Socket客户端
            objectOutputStream.writeObject(MyRpcResponse.success(result, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Occur exception:", e);
        }
    }
}
