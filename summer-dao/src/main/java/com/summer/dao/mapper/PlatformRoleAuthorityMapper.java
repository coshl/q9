package com.summer.dao.mapper;

import com.summer.dao.entity.PlatformRoleAuthority;

public interface PlatformRoleAuthorityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformRoleAuthority record);

    int insertSelective(PlatformRoleAuthority record);

    PlatformRoleAuthority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformRoleAuthority record);

    int updateByPrimaryKey(PlatformRoleAuthority record);

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

}