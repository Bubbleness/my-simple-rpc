package mucheng.practice.rpc.spring;

import java.lang.reflect.Field;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mucheng.practice.rpc.annotation.RpcReference;
import mucheng.practice.rpc.annotation.RpcService;
import mucheng.practice.rpc.config.RpcServiceConfig;
import mucheng.practice.rpc.enums.RpcRequestTransportEnum;
import mucheng.practice.rpc.extension.ExtensionLoader;
import mucheng.practice.rpc.factory.SingletonFactory;
import mucheng.practice.rpc.provider.ServiceProvider;
import mucheng.practice.rpc.provider.impl.ZkServiceProviderImpl;
import mucheng.practice.rpc.proxy.RpcClientProxy;
import mucheng.practice.rpc.remoting.transport.RpcRequestTransport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author mucheng
 * @date 2023/08/01 20:25:30
 * @description 在创建bean之前调用此方法来查看该类是否被注解标注
 */
@Slf4j
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;
    private final RpcRequestTransport rpcClient;

    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
        this.rpcClient = ExtensionLoader.getExtensionLoader(RpcRequestTransport.class)
            .getExtension(RpcRequestTransportEnum.NETTY.getName());
    }

    @SneakyThrows
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] is annotated with  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // get RpcService annotation
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            // build RpcServiceProperties
            RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                .group(rpcService.group())
                .version(rpcService.version())
                .service(bean).build();
            serviceProvider.publishService(rpcServiceConfig);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcServiceConfig rpcServiceConfig = RpcServiceConfig.builder()
                    .group(rpcReference.group())
                    .version(rpcReference.version()).build();
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceConfig);
                Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    // 把@RpcReference注解标注bean替换成代理类
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
        return bean;
    }
}
