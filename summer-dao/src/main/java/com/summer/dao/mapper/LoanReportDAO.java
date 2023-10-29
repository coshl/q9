/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.LoanReport;
import com.summer.pojo.vo.LoanReportVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface LoanReportDAO {
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
    int insert(LoanReport record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(LoanReport record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    LoanReport selectByPrimaryKey(Integer id);

    /**
     * 根据条件查询LoanReport
     *
     * @param params
     * @return
     */
    List<LoanReportVO> findParams(Map<String, Object> params);

    /**
     * 根据条件查询贷款总额
     *
     * @param params
     * @return
     */
    Map<String, Object> findLoanMoneySum(Map<String, Object> params);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(LoanReport record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(LoanReport record);

    LoanReport selectByReportDate(String reportDate);



    Map<String, Object> getReport(@Param("begin") String begin, @Param("end") String end);
}