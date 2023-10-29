/* https://github.com/12641561 */
package com.summer.dao.mapper;

import com.summer.dao.entity.OrderRepayment;
import com.summer.dao.entity.RepaymentInfo;
import com.summer.dao.entity.ReportRepayment;
import com.summer.pojo.vo.ReportRepaymentTitleVO;

import java.util.List;
import java.util.Map;

/**
 * 本文件由 https://github.com/12641561/mybatis-generator-core-chinese-1.3.5 自动生成
 *
 * @author tang code generator
 */
public interface ReportRepaymentDAO {
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
    int insert(ReportRepayment record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(ReportRepayment record);

    /**
     * 根据ID查询
     *
     * @param id 主键ID
     * @return 返回查询的结果
     */
    ReportRepayment selectByPrimaryKey(Integer id);

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(ReportRepayment record);

    /**
     * 根据ID修改所有字段(必须含ID）
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKey(ReportRepayment record);

    /**
     * 查询还款订单详情
     *
     * @param params
     * @return
     */
    List<RepaymentInfo> findByRepaymentReport(Map<String, Object> params);

    /**
     * 查询提前还款订单详情
     *
     * @param params
     * @return
     */
   Long findByRepaymentReport1(Map<String, Object> params);
    /**
     * 查询提前还款订单详情
     *
     * @param params
     * @return
     */
    Long findByRepaymentReport3(Map<String, Object> params);

    /**
     * 查询提前还款订单详情
     *
     * @param params
     * @return
     */
    Long findByRepaymentReport4(Map<String, Object> params);
    /**
     * 查询逾期还款订单详情
     *
     * @param params
     * @return
     */
    Long findByRepaymentReport2(Map<String, Object> params);

    /**
     * 查询当日还款订单详情
     *
     * @param params
     * @return
     */
    List<RepaymentInfo> findTodayRepaymentReport(Map<String, Object> params);

    List<RepaymentInfo> findByRepaymentReportTitle(Map<String, Object> params);

    /**
     * 查询每日还款报告
     *
     * @param params
     * @return
     */
    List<ReportRepayment> queryReportInfo(Map<String, Object> params);

    /**
     * 查询每日还款报告 channelid != null
     *
     * @param params
     * @return
     */
    List<ReportRepayment> queryReport(Map<String, Object> params);

    /**
     * 查询每日还款报告 channelid == null
     *
     * @param params
     * @return
     */
    List<ReportRepayment> queryReportByChannel(Map<String, Object> params);

    /**
     * 查询还款详情
     *
     * @param params
     * @return
     */
    List<OrderRepayment> findByRepaymentSmsRemind(Map<String, Object> params);

    /**
     * 查询本日二次及以上的续期成功的
     *
     * @param params
     * @return
     */
    List<RepaymentInfo> renewalToday(Map<String, Object> params);

    ReportRepaymentTitleVO findTitle(Map<String, Object> params);

    /**
     * 查询历史所有的还款记录
     *
     * @return
     */
    List<ReportRepayment> findByReportTime();
}