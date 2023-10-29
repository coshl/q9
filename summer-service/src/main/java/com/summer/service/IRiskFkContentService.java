package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.dao.entity.RiskFkContent;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jobob
 * @since 2020-07-30
 */
public interface IRiskFkContentService extends IService<RiskFkContent> {
    // 查询最新的风控记录
    RiskFkContent getFirstByUserIdAndType(Integer userId, Integer type);
}
