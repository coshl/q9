/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.ProductRecommend;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 * @author tang code generator
 */
public interface ProductRecommendDAO {
    /** 
     * 根据ID删除
     * @param id 主键ID
     * @return 返回删除成功的数量
     */
    int deleteByPrimaryKey(Integer id);

    /** 
     * 添加对象所有字段
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insert(ProductRecommend record);

    /** 
     * 添加对象对应字段
     * @param record
     * @return 返回添加成功的数量
     */
    int insertSelective(Map<String,Object> record);

    /** 
     * 根据ID查询
     * @param id 主键ID
     * @return 返回查询的结果
     */
    ProductRecommend selectByPrimaryKey(Integer id);

    /** 
     * 根据ID修改对应字段
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(Map<String,Object> record);

    /** 
     * 根据ID修改所有字段(必须含ID）
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(ProductRecommend record);

    /**
     * 按参数查询
     * @param param
     * @return
     */
    List<ProductRecommend> findByParam(Map<String,Object> param);

}