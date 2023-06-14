package mucheng.practice.rpc.extension;

import java.lang.annotation.*;

/**
 * @author mucheng
 * @date 2023/06/12 20:47:01
 * @description SPI ：Service Provider Interface 服务提供接口，约定在
 * classpath下的 META-INF/services/ 目录里创建一个以服务接口命名的文件，然后文件里面记录的是此 jar 包提供的具体实现类的全限定名。
 * 当我们引用了某个jar包的时候就可以去找这个jar包的 META-INF/services/ 目录，再根据接口名找到文件，然后读取文件里面的内容去进行实现类的加载与实例化。
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPI {
}
