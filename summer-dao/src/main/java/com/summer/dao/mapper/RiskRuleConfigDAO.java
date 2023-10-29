/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.RiskRuleConfig;
import java.util.List;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface RiskRuleConfigDAO {
    /**
     * 根据ID删除
     *
     * @param id 主键ID
     * @return 返回删除成功的数量
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加对象所有字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insert(RiskRuleConfig record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(RiskRuleConfig record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    RiskRuleConfig selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(RiskRuleConfig record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(RiskRuleConfig record);

    /**
     * 查询风控规则列表
     *
     * @return
     */
    List<RiskRuleConfig> findAllRiskRule();

    /**
     * 查询开启的
     *
     * @param status
     * @return
     */
    List<RiskRuleConfig> findRiskRuleByStatus(byte status);

    /**
     * 一键开启或关闭风控规则
     *
     * @param status
     * @return
     */
    int updateAllState(Byte status);

    /**
     * 根据key查询规则
     *
     * @return
     */
    RiskRuleConfig findByKey(String key);


    /**
     * 黑名单拦截数量，之谜分拦截数量，人工拒绝数量，统计出来
     * @return
     */
	//TotalNumberOfInterceptions findTotalIntercept();
}
