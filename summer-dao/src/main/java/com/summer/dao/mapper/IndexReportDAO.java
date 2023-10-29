/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.IndexReport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface IndexReportDAO {
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
    int insert(IndexReport record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(IndexReport record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    IndexReport selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(IndexReport record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(IndexReport record);

    List<IndexReport> selectSimple(Map<String, Object> params);
    
    @Select("SELECT (select sum(recharge_amount) from recharge_record) - (select sum(totalConsumption) from total_expenses) AS currentMmoney")
    BigDecimal findTotalMoney();
    
    @Select("SELECT (select sum(recharge_amount) from recharge_record) - (select sum(totalMoney) from total_money) AS currentMmoney")
    BigDecimal findTotalMoney1();
    
    @Select("select sys_value from back_config_params where sys_key = 'tjms'")
    int selectFlag();
}