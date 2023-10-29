package com.summer.api.service;


import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.PlatformUser;
import com.summer.pojo.vo.OperatorVo;
import com.summer.pojo.vo.PlatformUserVo;

import java.util.List;
import java.util.Map;

public interface IPlateformUserService {

    /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber
     * @return
     */
    PlatformUser findByPhoneNumber(String phoneNumber);

    /**
     * 添加用户信息
     *
     * @param platformUser
     */
    void save(PlatformUser platformUser);

    /**
     * 更新用户信息
     *
     * @param platformUser
     */
    int update(PlatformUser platformUser);

    /**
     * 验证用户名和密码
     *
     * @param platformUser
     * @return
     */
    //String userLogin(PlatformUser platformUser);

    /**
     * 账号管理列表
     *
     * @param params
     * @return
     */
    PageInfo<PlatformUserVo> findParams(Map<String, Object> params);

    /**
     * 删除用户
     *
     * @param params
     */
    void deleteByStatus(Map<String, Object> params);

    /**
     * 根据token取用户信息
     *
     * @param token
     * @return
     */
    String getUserByToken(String token);

    /**
     * 修改角色
     *
     * @param params
     */
    int updateByRole(Map<String, Object> params);

    /**
     * 通过角色id查询用户
     *
     * @param param
     * @return
     */
    List<OperatorVo> findByRoleId(Map<String, Object> param);

    /**
     * 查询催收人员列表
     *
     * @param roleId
     * @return
     */
    List<OperatorVo> findByCuishouRoleId(Integer roleId);

    /**
     * 查询最早添加的那个管理员
     *
     * @return
     */
    PlatformUser findFirstAdmin();

    PlatformUser findById(Integer id);
}
