package com.summer.dao.mapper;

import com.summer.dao.entity.PlatformAuthority;
import com.summer.pojo.vo.PlatformRoleAuthorityVo;

import java.util.List;

public interface PlatformAuthorityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformAuthority record);

    int insertSelective(PlatformAuthority record);

    PlatformAuthority selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformAuthority record);

    int updateByPrimaryKey(PlatformAuthority record);

    /**
     * 查询一级菜单权限列表
     *
     * @return
     */
    List<PlatformAuthority> findByAuthority();

    /**
     * 查询角色下的权限列表
     *
     * @param id
     */
    List<PlatformRoleAuthorityVo> findRoleAuthority(Integer id);
}