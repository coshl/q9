package com.summer.service.impl;

import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.PlatformAuthority;
import com.summer.dao.entity.PlatformRole;
import com.summer.dao.mapper.PlatformAuthorityMapper;
import com.summer.dao.mapper.PlatformRoleMapper;
import com.summer.api.service.IPlateformRoleService;
import com.summer.pojo.vo.PlatformUserRoleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PlateformRoleService implements IPlateformRoleService {

    @Resource
    private PlatformRoleMapper platformRoleMapper;

    @Resource
    private PlatformAuthorityMapper platformAuthorityMapper;

    /**
     * 角色管理分页列表
     *
     * @param params
     * @return
     */
    @Override
    public PageInfo<PlatformUserRoleVo> findParams(Map<String, Object> params) {
        List<PlatformUserRoleVo> list = platformRoleMapper.findParams(params);
        return new PageInfo<>(list);
    }

    /**
     * 角色列表,添加用户时需要
     *
     * @return
     */
    @Override
    public List<PlatformRole> findAll() {
        return platformRoleMapper.findAll();
    }

    /**
     * 查询权限列表
     *
     * @return
     */
    @Override
    public List<PlatformAuthority> findByAuthority() {
        List<PlatformAuthority> list = platformAuthorityMapper.findByAuthority();
        List<PlatformAuthority> list2 = new ArrayList<>();
        for (PlatformAuthority platformAuthority : list) {
            if (platformAuthority.getParentId() == 0) {
                HashMap<String, Object> map = getLoad(list, platformAuthority.getId());
                platformAuthority.setList((List<PlatformAuthority>) map.get("list"));
                list2.add(platformAuthority);
            }
        }
        return list2;
    }

    /**
     * 添加角色和权限
     *
     * @param platformRole
     * @return
     */
    @Override
    public void insert(PlatformRole platformRole) {
        platformRoleMapper.insert(platformRole);
    }

    /**
     * 修改角色名称和权限
     *
     * @param platformRole
     * @return
     */
    @Override
    public void update(PlatformRole platformRole) {
        platformRoleMapper.updateByPrimaryKey(platformRole);
    }

    /**
     * 查询角色信息
     *
     * @param id
     * @return
     */
    @Override
    public PlatformRole findById(Integer id) {
        return platformRoleMapper.selectByPrimaryKey(id);
    }

    /**
     * 递归查找子节点
     *
     * @param list
     * @param parentId
     * @return
     */
    public HashMap<String, Object> getLoad(List<PlatformAuthority> list, int parentId) {
        List<PlatformAuthority> list2 = new ArrayList<>();
        for (PlatformAuthority platformAuthority : list) {
            if (platformAuthority.getParentId() == parentId) {
                HashMap<String, Object> map = getLoad(list, platformAuthority.getId());
                platformAuthority.setList((List<PlatformAuthority>) map.get("list"));
                list2.add(platformAuthority);
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("list", list2);
        return map;
    }

}
