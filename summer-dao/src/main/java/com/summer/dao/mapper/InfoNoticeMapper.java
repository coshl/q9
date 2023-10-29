package com.summer.dao.mapper;

import com.summer.dao.entity.InfoNotice;
import com.summer.pojo.vo.InfoNoticeVo;

import java.util.List;
import java.util.Map;

/**
 * App公告消息Mapper接口
 */
public interface InfoNoticeMapper {
    /**
     * 根据参数查询公告信息
     *
     * @param map
     * @return
     */
    List<InfoNotice> findNoticeByParams(Map<String, Object> map);

    /**
     * 查询借款记录
     *
     * @param map
     * @return
     */
    List<InfoNotice> findLoanLog(Map<String, Object> map);

    List<InfoNoticeVo> findNotice();

    /**
     * 根据ID修改对应字段
     *
     * @param record 修改字段对象(必须含ID）
     * @return 返回更新成功的数量
     */
    int updateByPrimaryKeySelective(InfoNotice record);

    /**
     * 添加对象对应字段
     *
     * @param record 插入字段对象(必须含ID）
     * @return 返回添加成功的数量
     */
    int insertSelective(InfoNotice record);
}
