package org.tinycloud.tinymock.common.config.mybatis;

/**
 * <p>
 *     datacenterId和workerId提取器
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-12-19 17:46
 */
public class IdExtractor {

    // 总积ID最大值
    public static final int MAX_TOTAL_ID = 1023;

    /**
     * 提取 workerId 使用求余法
     */
    public static int extractWorkerId(int totalId) {
        // 取模 32
        return totalId % 32;
    }

    /**
     * 提取 datacenterId 使用整除法
     */
    public static int extractDatacenterId(int totalId) {
        // 整除 32
        return totalId / 32;
    }

    /**
     * 反向生成 totalId
     */
    public static int generateTotalId(int datacenterId, int workerId) {
        if (datacenterId < 0 || datacenterId > 31 || workerId < 0 || workerId > 31) {
            throw new IllegalArgumentException("datacenterId 和 workerId 必须在 0 到 31 之间");
        }
        return datacenterId * 32 + workerId;
    }

    public static void main(String[] args) {
        int totalId = generateTotalId(23, 9);
        System.out.println("Total ID: " + totalId);

        // 提取 workerId 和 datacenterId
        for (int i = 0; i <= 1023; i++) {
            int workerId = extractWorkerId(i);
            int datacenterId = extractDatacenterId(i);

            System.out.println(datacenterId + "  " + workerId);
        }
    }
}
