package com.summer.api.service;

import com.summer.dao.entity.PlatformRoleAuthority;
import com.summer.pojo.vo.PlatformRoleAuthorityVo;

import java.util.List;

public interface IPlatformRoleAuthorityService {
    /**
     * 根据角色id查询权限
     *
     * @param roleId
     * @return
     */
    PlatformRoleAuthority findByRoleId(Integer roleId);

    /**
     * 根据角色id删除角色权限记录
     *
     * @param roleId
     * @return
     */
    void deleteByRoleId(Integer roleId);

    /**
     * 设置角色权限
     *
     * @param roleId
     * @param ids
     */
    void setAuthority(Integer roleId, String ids);

    /**
     * 角色权限列表
     *
     * @param id
     * @return
     */
    List<PlatformRoleAuthorityVo> findRoleAuthority(Integer id);
}
