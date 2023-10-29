package com.summer.service.impl;

import com.summer.dao.entity.Advice;
import com.summer.dao.mapper.AdviceDAO;
import com.summer.api.service.IAdviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ls
 * @Title:
 * @date 2019/3/18 11:06
 */
@Service
public class AdviceService implements IAdviceService {
    @Resource
    private AdviceDAO adviceDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return adviceDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Advice record) {
        return adviceDAO.insert(record);
    }

    @Override
    public int insertSelective(Advice record) {
        return adviceDAO.insertSelective(record);
    }

    @Override
    public Advice selectByPrimaryKey(Integer id) {
        return adviceDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Advice record) {
        return adviceDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Advice record) {
        return adviceDAO.updateByPrimaryKey(record);
    }

    @Override
    public List<Advice> selectByParams(Map<String, Object> map) {
        return adviceDAO.selectByParams(map);
    }
}
