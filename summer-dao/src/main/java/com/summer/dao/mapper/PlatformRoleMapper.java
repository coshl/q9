package com.summer.dao.mapper;

import com.summer.dao.entity.PlatformRole;
import com.summer.pojo.vo.PlatformUserRoleVo;

import java.util.List;
import java.util.Map;

public interface PlatformRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(PlatformRole record);

    /**
     * 查询角色信息
     *
     * @param id
     * @return
     */
    PlatformRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformRole record);

    /**
     * 添加角色
     *
     * @param record
     */
    void insert(PlatformRole record);

    /**
     * 修改角色
     *
     * @param record
     */
    void updateByPrimaryKey(PlatformRole record);

    /**
     * 角色列表,添加用户时需要
     *
     * @return
     */
    List<PlatformRole> findAll();

    /**
     * 通过角色名称获得角色id，添加用户时需要
     *
     * @return
     */
    PlatformRole selectByRoleName(String roleName);

    /**
     * 角色管理列表查询
     *
     * @param params
     * @return
     */
    List<PlatformUserRoleVo> findParams(Map<String, Object> params);
}