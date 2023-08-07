package mucheng.practice.rpc;

import mucheng.practice.rpc.annotation.RpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author mucheng
 * @date 2023/08/07 17:09:47
 * @description 主函数 - Netty的方式
 */
@RpcScan(basePackage = "mucheng.practice.rpc")
public class NettyClientMain {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext =
            new AnnotationConfigApplicationContext(NettyClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
