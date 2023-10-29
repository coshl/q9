package com.summer.service.impl;

import com.summer.dao.entity.PlatformRoleAuthority;
import com.summer.dao.mapper.PlatformAuthorityMapper;
import com.summer.dao.mapper.PlatformRoleAuthorityMapper;
import com.summer.api.service.IPlatformRoleAuthorityService;
import com.summer.pojo.vo.PlatformRoleAuthorityVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色权限Service
 */
@Service
public class PlatformRoleAuthorityService implements IPlatformRoleAuthorityService {

    @Resource
    private PlatformRoleAuthorityMapper roleAuthorityMapper;

    @Resource
    private PlatformAuthorityMapper platformAuthorityMapper;

    /**
     * 根据角色id查询权限
     *
     * @param roleId
     * @return
     */
    @Override
    public PlatformRoleAuthority findByRoleId(Integer roleId) {
        return roleAuthorityMapper.findByRoleId(roleId);
    }

    /**
     * 根据角色id删除角色权限记录
     *
     * @param roleId
     * @return
     */
    @Override
    public void deleteByRoleId(Integer roleId) {
        roleAuthorityMapper.deleteByRoleId(roleId);
    }

    /**
     * 设置角色权限
     *
     * @param roleId
     * @param ids
     */
    @Override
    public void setAuthority(Integer roleId, String ids) {
        // 删除该角色所有的权限
        roleAuthorityMapper.deleteByRoleId(roleId);
        if (ids != null) {
            // 添加新的权限
            String[] funcList = ids.split(",");
            for (String id : funcList) {
                PlatformRoleAuthority relation = new PlatformRoleAuthority();
                relation.setRoleId(roleId);
                relation.setAuthorityId(Integer.parseInt(id));
                this.roleAuthorityMapper.insert(relation);
            }
        }
    }

    /**
     * 角色权限列表
     *
     * @param id
     * @return
     */
    @Override
    public List<PlatformRoleAuthorityVo> findRoleAuthority(Integer id) {
        List<PlatformRoleAuthorityVo> authority = platformAuthorityMapper.findRoleAuthority(id);
        return authority;
    }
}
