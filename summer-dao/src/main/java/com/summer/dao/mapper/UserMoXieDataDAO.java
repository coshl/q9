/* https://github.com/12641561 */
package com.summer.dao.mapper;


import com.summer.dao.entity.UserMoXieData;
import com.summer.dao.entity.UserMoXieDataWithBLOBs;
import com.summer.pojo.vo.UserMoXieDataVo;
import org.apache.ibatis.annotations.Param;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface UserMoXieDataDAO {
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
    int insert(UserMoXieDataWithBLOBs record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(UserMoXieDataWithBLOBs record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    UserMoXieDataWithBLOBs selectByPrimaryKey(Integer id);

    UserMoXieDataWithBLOBs selectDataByUserId(Integer userId);

    UserMoXieDataWithBLOBs selectByPhone(String phone);

    UserMoXieDataWithBLOBs selectByTaskId(String taskId);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(UserMoXieDataWithBLOBs record);

    /**
     * 根据ID修改字段（包含二进制大对象）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeyWithBLOBs(UserMoXieDataWithBLOBs record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(UserMoXieData record);

    /**
     * 不压缩直接存储运营商数据
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective1(UserMoXieDataVo record);
}
