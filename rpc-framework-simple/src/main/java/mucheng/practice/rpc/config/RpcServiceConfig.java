package mucheng.practice.rpc.config;

import lombok.*;

/**
 * @author mucheng
 * @date 2023/07/11 14:39:26
 * @description Rpc服务的配置信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class RpcServiceConfig {

    /**
     * 提供的服务版本
     */
    private String version = "";

    /**
     * 服务所属组别：当接口有多个实现类时，按组区分
     */
    private String group = "";

    /**
     * 目标服务
     */
    private Object service;

    /**
     * 获取Rpc服务的名称
     *
     * @return Rpc服务名称
     */
    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    /**
     * 获取目标服务的名称
     *
     * @return 目标服务的名称
     */
    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
