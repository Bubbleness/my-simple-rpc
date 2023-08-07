package mucheng.practice.rpc;

import mucheng.practice.rpc.annotation.RpcReference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;

/**
 * @author mucheng
 * @date 2023/08/07 17:03:21
 * @description 测试类控制器
 */
@Controller
public class HelloController {

    @RpcReference(version = "1.0.0", group = "RPC")
    private HelloMySimpleRpcService helloMySimpleRpcService;

    public void test() {
        String hello = this.helloMySimpleRpcService.sayHelloToRpc(
            new Hello("「客户端」调用「HelloMySimpleRpcService」服务", "客户端调用「sayHelloToRpc()」方法"));
        if (!StringUtils.equals(hello, "success")) {
            throw new RuntimeException("Rpc远程服务调用失败～");
        }
    }
}
