package com.summer.service.impl;


import com.summer.dao.entity.InfoNotice;
import com.summer.dao.mapper.InfoNoticeMapper;
import com.summer.api.service.IInfoNoticeService;
import com.summer.util.Constant;
import com.summer.pojo.vo.InfoNoticeVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP公告信息Service接口实现类
 */
@Service
public class InfoNoticeService implements IInfoNoticeService {

    @Resource
    private InfoNoticeMapper infoNoticeMapper;

    @Override
    public List<InfoNotice> findMsgCenterNotice() {
        Map<String, Object> map = new HashMap<>();
//        map.put("status", Constant.INDEX_NOTICE_STATUS);
        map.put("noticeType", Constant.INDEX_NOTICE_MSG_TYPE);
        return infoNoticeMapper.findNoticeByParams(map);
    }

    @Override
    public List<InfoNotice> findLoanLog(Map<String, Object> map) {
        return infoNoticeMapper.findLoanLog(map);
    }

    @Override
    public List<InfoNoticeVo> findNotice() {
        return infoNoticeMapper.findNotice();
    }

    @Override
    public int updateSelect(InfoNotice notice) {
        return infoNoticeMapper.updateByPrimaryKeySelective(notice);
    }

    @Override
    public int insertSelect(InfoNotice notice) {
        return infoNoticeMapper.insertSelective(notice);
    }

    @Override
    public List<InfoNotice> selectNotice(Map<String, Object> map) {
        return infoNoticeMapper.findNoticeByParams(map);
    }
}
