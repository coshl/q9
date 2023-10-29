package com.summer.api.service;

import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.PlatformAuthority;
import com.summer.dao.entity.PlatformRole;
import com.summer.pojo.vo.PlatformUserRoleVo;

import java.util.List;
import java.util.Map;

public interface IPlateformRoleService {

    /**
     * 账号管理列表
     *
     * @param params
     * @return
     */
    PageInfo<PlatformUserRoleVo> findParams(Map<String, Object> params);

    /**
     * 角色列表,添加用户时需要
     *
     * @return
     */
    List<PlatformRole> findAll();

    /**
     * 查询一级权限列表
     *
     * @return
     */
    List<PlatformAuthority> findByAuthority();

    /**
     * 添加角色
     *
     * @param platformRole
     * @return
     */
    void insert(PlatformRole platformRole);

    /**
     * 修改角色
     *
     * @param platformRole
     * @return
     */
    void update(PlatformRole platformRole);

    /**
     * 根据id查询角色信息
     *
     * @param id
     * @return
     */
    PlatformRole findById(Integer id);
}
