package com.summer.service.impl;

import com.summer.dao.entity.AppVersionInfo;
import com.summer.dao.mapper.AppVersionInfoMapper;
import com.summer.api.service.IAppVersionInfoService;
import com.summer.pojo.vo.AppVersionInfoVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * app版本信息Service接口的实现类
 */
@Service
public class AppVersionInfoService implements IAppVersionInfoService {
    @Resource
    private AppVersionInfoMapper appVersionInfoDAO;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return appVersionInfoDAO.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(AppVersionInfo record) {
        return appVersionInfoDAO.insert(record);
    }

    @Override
    public int insertSelective(AppVersionInfo record) {
        return appVersionInfoDAO.insertSelective(record);
    }

    @Override
    public AppVersionInfo selectByPrimaryKey(Integer id) {
        return appVersionInfoDAO.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AppVersionInfo record) {
        return appVersionInfoDAO.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(AppVersionInfo record) {
        return appVersionInfoDAO.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(AppVersionInfo record) {
        return appVersionInfoDAO.updateByPrimaryKey(record);
    }

    @Override
    public AppVersionInfo selectAppVersionByType(Integer appType) {
        return appVersionInfoDAO.selectAppVersionByType(appType);
    }

    @Override
    public List<AppVersionInfoVo> selectByParam(Map<String, Object> param) {
        return appVersionInfoDAO.selectByParam(param);
    }

	@Override
	public List<Map<String, Object>> getAppVersionInfo(Integer appType) {
		return appVersionInfoDAO.getAppVersionInfo(appType);
	}
}
