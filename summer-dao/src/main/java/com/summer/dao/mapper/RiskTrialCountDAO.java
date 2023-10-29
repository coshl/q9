/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.RiskTrialCount;
import com.summer.pojo.vo.RiskTrialCountVo;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface RiskTrialCountDAO {
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
    int insert(RiskTrialCount record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(RiskTrialCount record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    RiskTrialCount selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(RiskTrialCount record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(RiskTrialCount record);

    /**
     * 根据类型和规则的key查询当天记录的
     *
     * @param params
     * @return
     */
    RiskTrialCount findByCountType(Map<String, Object> params);

    List<RiskTrialCountVo> findByParam(Map<String, Object> params);

    /**
     * 命中风控数量
     */
    List<RiskTrialCountVo> selectRuleCount(Map<String, Object> map);
}