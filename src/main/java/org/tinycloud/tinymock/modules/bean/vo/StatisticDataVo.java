package org.tinycloud.tinymock.modules.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * </p>
 *
 * @author liuxingyu01
 * @since 2024-04-2024/4/9 21:56
 */
@Setter
@Getter
public class StatisticDataVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 接口名
     */
    private String mockName;

    /**
     * 项目名
     */
    private String projectName;

    /**
     * URL
     */
    private String url;

    /**
     * 今日点击量
     */
    private Long accessTodayNumber;

    /**
     * 今日独立IP数
     */
    private Long accessTodayIpNumber;

    /**
     * 昨日点击量
     */
    private Long accessYesterdayNumber;

    /**
     * 昨日独立IP数
     */
    private Long accessYesterdayIpNumber;

    /**
     * 当月点击量
     */
    private Long accessMonthNumber;

    /**
     * 当月独立IP数
     */
    private Long accessMonthIpNumber;

    /**
     * 总点击量
     */
    private Long accessTotalNumber;

    /**
     * 当月独立IP数
     */
    private Long accessTotalIpNumber;
}
