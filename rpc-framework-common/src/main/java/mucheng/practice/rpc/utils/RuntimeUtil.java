package mucheng.practice.rpc.utils;

/**
 * @author mucheng
 * @date 2023/07/13 19:08:21
 * @description 运行时信息获取工具
 */
public class RuntimeUtil {

    /**
     * 获取CPU的核心数
     *
     * @return cpu的核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }

}
