package com.summer.service.impl;

import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.api.service.IBackConfigParamsService;
import com.summer.util.BackConfigParams;
import com.summer.pojo.vo.BackConfigParamsVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class BackConfigParamsService implements IBackConfigParamsService {
    @Resource
    IBackConfigParamsDao backConfigParamsDao;

    @Override
    public List<BackConfigParams> findParams(HashMap<String, Object> params) {
        return backConfigParamsDao.findParams(params);
    }

    @Override
    public int updateValue(List<BackConfigParams> list) {
        return backConfigParamsDao.updateValue(list);
    }

    @Override
    public BackConfigParamsVo findBySysKey(String syskey) {
        return backConfigParamsDao.findBySysKey(syskey);
    }

    @Override
    public int updateBySyskey(BackConfigParams backConfigParams) {
        return backConfigParamsDao.updateBySyskey(backConfigParams);
    }
}
