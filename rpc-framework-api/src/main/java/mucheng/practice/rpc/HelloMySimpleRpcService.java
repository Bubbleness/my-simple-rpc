package mucheng.practice.rpc;

/**
 * @author mucheng
 * @date 2023/08/07 16:59:44
 * @description 服务提供者对外提供的服务，接口的具体实现在server中
 */
public interface HelloMySimpleRpcService {

    /**
     * 服务提供者提供的服务
     *
     * @param hello 实体
     * @return 响应内容
     */
    String sayHelloToRpc(Hello hello);
}
