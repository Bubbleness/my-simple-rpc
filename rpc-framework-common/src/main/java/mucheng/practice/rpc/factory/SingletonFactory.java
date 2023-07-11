package mucheng.practice.rpc.factory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mucheng
 * @date 2023/07/11 15:21:18
 * @description 获取单例对象的工厂类
 */
public final class SingletonFactory {

    /**
     * 单例对象缓存
     */
    private static final Map<String, Object> OBJECT_MAP = new ConcurrentHashMap<>();

    private SingletonFactory() {
    }

    /**
     * 获取类的单例
     *
     * @param clazz 类型
     * @return clazz类的实例
     */
    public static <T> T getInstance(Class<T> clazz) {

        if (clazz == null) {
            throw new IllegalArgumentException();
        }
        String key = clazz.toString();
        if (OBJECT_MAP.containsKey(key)) {
            // ConcurrentHashMap读的过程不会加锁
            return clazz.cast(OBJECT_MAP.get(key));
        } else {
            // ConcurrentHashMap写的过程会加锁，保证只有一个线程可以写成功，也就保证了单例
            return clazz.cast(OBJECT_MAP.computeIfAbsent(key, k -> {
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }));
        }
    }
}
