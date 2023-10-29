package com.summer.api.service.channel;

import java.util.Date;

/**
 * 渠道还款统计Service接口类
 */
public interface IChannelCountRepaymentService {

    /**
     * 每天凌晨01：30定时生成渠道还款表
     *
     * @param time
     */
    void channelRepaymentCount(Date time);

    /**
     * 已还统计，定时，
     *
     * @param time
     */
    void repaymentCount(Date time);

    /**
     * 逾期统计
     *
     * @param time
     */
    void overdueCount(Date time);

    /**
     * 逾期总数统计
     *
     * @param tim
     */
    void overdueTotal(Date tim, Integer pasDay);
}
