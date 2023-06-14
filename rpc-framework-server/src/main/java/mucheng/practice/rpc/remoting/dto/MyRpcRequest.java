package mucheng.practice.rpc.remoting.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author mucheng
 * @date 2023/06/12 20:17:55
 * @description Rpc框架请求实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MyRpcRequest implements Serializable {
    private static final long serialVersionUID = 9217106643742921736L;

    /**
     * Rpc请求ID
     */
    private String requestId;
    /**
     * 请求接口名称
     */
    private String interfaceName;
    /**
     * 请求方法名称
     */
    private String methodName;
    /**
     * 请求的参数
     */
    private Object[] parameters;
    /**
     * 请求的参数类型
     */
    private Class<?>[] paramTypes;
    /**
     * 接口服务版本
     */
    private String version;
    /**
     * 服务所属的组（用于处理一个接口有多个实现类的情况）
     */
    private String group;

    /**
     * 构建Rpc服务名称：接口名+组名+版本号
     *
     * @return Rpc服务名称
     */
    public String getRpcServiceName() {
        return this.getInterfaceName() + this.getGroup() + this.getVersion();
    }
}
