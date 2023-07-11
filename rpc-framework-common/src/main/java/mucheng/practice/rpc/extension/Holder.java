package mucheng.practice.rpc.extension;

/**
 * @author mucheng
 * @date 2023/07/11 10:57:17
 * @description 以Holder结尾的类的作用并不局限于实现单例模式，而是一个通用的命名约定， 用来表示该类扮演了一个管理器、容器或者持有者的角色。
 */
public class Holder<T> {

    /**
     * 管理的值
     */
    private volatile T value;

    /**
     * 获取管理器中的值
     *
     * @return T类型的对象
     */
    public T get() {
        return value;
    }

    /**
     * 设置需要管理的值
     *
     * @param value 待设置的值
     */
    public void set(T value) {
        this.value = value;
    }
}
