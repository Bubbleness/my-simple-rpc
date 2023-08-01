package mucheng.practice.rpc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import mucheng.practice.rpc.spring.CustomScannerRegistrar;
import org.springframework.context.annotation.Import;

/**
 * @author mucheng
 * @date 2023/08/01 20:14:55
 * @description 扫描自定义注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
@Documented
public @interface RpcScan {

    String[] basePackage();
}
