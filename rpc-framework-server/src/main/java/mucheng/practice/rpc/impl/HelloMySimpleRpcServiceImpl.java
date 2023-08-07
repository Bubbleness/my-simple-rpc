package mucheng.practice.rpc.impl;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.Hello;
import mucheng.practice.rpc.HelloMySimpleRpcService;
import mucheng.practice.rpc.annotation.RpcService;

/**
 * @author mucheng
 * @date 2023/08/07 17:16:45
 * @description HelloMySimpleRpcService的具体实现，对外提供服务接口的具体实现，需要注明Rpc的版本和群组
 */
@Slf4j
@RpcService(group = "1.0.0", version = "RPC")
public class HelloMySimpleRpcServiceImpl implements HelloMySimpleRpcService {

    static {
        System.out.println("HelloServiceImpl被创建");
    }

    @Override
    public String sayHelloToRpc(Hello hello) {
        log.info("HelloServiceImpl收到: [{}]", hello.getMessage());
        String result = "Hello description is: [{}]" + hello.getDescription();
        log.info("HelloServiceImpl返回: [{}]", result);
        return result;
    }

}
