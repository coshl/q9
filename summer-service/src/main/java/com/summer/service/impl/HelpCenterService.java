package com.summer.service.impl;

import com.summer.dao.entity.HelpCenter;
import com.summer.dao.mapper.HelpCenterMapper;
import com.summer.api.service.IHelpCenterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author ls
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2018/12/22 16:43
 */
@Service
public class HelpCenterService implements IHelpCenterService {
    @Resource
    private HelpCenterMapper helpCenterMapper;

    @Override
    public HelpCenter selectByPrimaryKey(int id) {
        return helpCenterMapper.selectByPrimaryKey(id);
    }

    @Override
    public int selectColumn() {
        return helpCenterMapper.selcetColumn();
    }


}
