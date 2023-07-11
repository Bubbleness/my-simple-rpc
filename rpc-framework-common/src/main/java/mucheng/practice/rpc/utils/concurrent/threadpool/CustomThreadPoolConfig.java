package mucheng.practice.rpc.utils.concurrent.threadpool;

import lombok.Data;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author mucheng
 * @date 2023/07/11 14:53:37
 * @description 线程池自定义配置类，可自行根据业务场景修改配置参数
 */
@Data
public class CustomThreadPoolConfig {

    /**
     * 核心线程数
     */
    private static final int DEFAULT_CORE_POOL_SIZE = 10;
    /**
     * 最大线程数
     */
    private static final int DEFAULT_MAXIMUM_POOL_SIZE_SIZE = 100;
    /**
     * 非核心线程空闲存活时间
     */
    private static final int DEFAULT_KEEP_ALIVE_TIME = 1;
    /**
     * 时间单位
     */
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;
    /**
     * 阻塞队列默认的容量
     */
    private static final int DEFAULT_BLOCKING_QUEUE_CAPACITY = 100;
    /**
     * 阻塞队列容量
     */
    private static final int BLOCKING_QUEUE_CAPACITY = 100;

    /**
     * 可配置参数
     */
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE_SIZE;
    private long keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
    private TimeUnit unit = DEFAULT_TIME_UNIT;
    // 使用有界队列
    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
}
