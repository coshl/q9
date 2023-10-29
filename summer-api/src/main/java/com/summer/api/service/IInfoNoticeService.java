package com.summer.api.service;

import com.summer.dao.entity.InfoNotice;
import com.summer.pojo.vo.InfoNoticeVo;

import java.util.List;
import java.util.Map;

/**
 * APP公告信息Service接口
 */
public interface IInfoNoticeService {
    /**
     * 查询消息中心的公告信息
     *
     * @return
     */
    List<InfoNotice> findMsgCenterNotice();

    /**
     * 查询借款记录
     *
     * @param map
     * @return
     */
    List<InfoNotice> findLoanLog(Map<String, Object> map);

    /**
     * 查询需要展示的公告
     *
     * @return
     */
    List<InfoNoticeVo> findNotice();

    int updateSelect(InfoNotice notice);

    int insertSelect(InfoNotice notice);

    List<InfoNotice> selectNotice(Map<String, Object> map);
}
