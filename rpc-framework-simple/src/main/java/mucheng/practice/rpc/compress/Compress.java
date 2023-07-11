package mucheng.practice.rpc.compress;

import mucheng.practice.rpc.extension.SPI;

/**
 * @author mucheng
 * @date 2023/07/11 17:18:59
 * @description 文件压缩类
 */
@SPI
public interface Compress {

    /**
     * 压缩字节数组
     *
     * @param bytes 字节数组
     * @return 压缩后的字节数组
     */
    byte[] compress(byte[] bytes);

    /**
     * 还原压缩后的字节数组
     *
     * @param bytes 字节数组
     * @return 压缩前的字节数组
     */
    byte[] decompress(byte[] bytes);
}
