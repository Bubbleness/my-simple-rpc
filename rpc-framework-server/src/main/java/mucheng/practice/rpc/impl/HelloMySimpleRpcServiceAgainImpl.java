package mucheng.practice.rpc.impl;

import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.Hello;
import mucheng.practice.rpc.HelloMySimpleRpcService;

/**
 * @author mucheng
 * @date 2023/08/07 17:21:18
 * @description
 */
@Slf4j
public class HelloMySimpleRpcServiceAgainImpl implements HelloMySimpleRpcService {

    static {
        System.out.println("「HelloMySimpleRpcServiceAgainImpl」被创建");
    }

    @Override
    public String sayHelloToRpc(Hello hello) {
        log.info("「HelloMySimpleRpcServiceAgainImpl」收到: [{}]", hello.getMessage());
        String result = "Hello description is: [{}]" + hello.getDescription();
        log.info("「HelloMySimpleRpcServiceAgainImpl」返回: [{}]", result);
        return result;
    }
}
