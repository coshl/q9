package com.summer.dao.mapper;

import com.summer.dao.entity.PlateformChannelReport;
import com.summer.pojo.vo.PlateformChannelReportVo;
import com.summer.pojo.vo.PlateformChannelSideVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface PlateformChannelReportDAO {
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
    int insert(PlateformChannelReport record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(PlateformChannelReport record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    PlateformChannelReport selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(PlateformChannelReport record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(PlateformChannelReport record);

    /**
     * 我方渠道统计
     *
     * @param params
     * @return
     */
    List<PlateformChannelReportVo> findParams(Map<String, Object> params);

    /**
     * 渠道方统计
     *
     * @param params
     * @return
     */
    List<PlateformChannelSideVo> findChannelSide(Map<String, Object> params);

    /**
     * 修改系数
     *
     * @param id
     * @return
     */
    int updateCoefficient(@Param("id") Integer id, @Param("dedutionCoefficient") BigDecimal dedutionCoefficient);

    /**
     * 根据参数查询（渠道统计在使用）
     *
     * @param param
     * @return
     */
    PlateformChannelReport selectByParams(Map<String, Object> param);

    /**
     * 统计命中黑名单数
     *
     * @param param
     * @return
     */
    Integer countBlackSysout(Map<String, Object> param);
}