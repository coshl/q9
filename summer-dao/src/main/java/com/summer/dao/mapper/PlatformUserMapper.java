package com.summer.dao.mapper;

import com.summer.dao.entity.MmanLoanCollectionPerson;
import com.summer.dao.entity.PlatformUser;
import com.summer.pojo.vo.OperatorVo;
import com.summer.pojo.vo.PlateformAuthorityDTO;
import com.summer.pojo.vo.PlatformUserVo;
import com.summer.pojo.vo.UserPhoneInfo;

import java.util.List;
import java.util.Map;

public interface PlatformUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlatformUser record);

    int insertSelective(PlatformUser record);

    PlatformUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlatformUser record);

    int updateByPrimaryKey(PlatformUser record);

    /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber
     * @return
     */
    PlatformUser findByPhoneNumber(String phoneNumber);

    /**
     * 查询账户管理列表
     *
     * @param params
     * @return
     */
    List<PlatformUserVo> findParams(Map<String, Object> params);

    List<PlatformUser> selectSimple(Map<String, Object> params);

    /**
     * 删除用户
     *
     * @param params
     */
    void deleteByStatus(Map<String, Object> params);

    /**
     * 修改角色
     *
     * @param params
     */
    int updateByRole(Map<String, Object> params);

    /**
     * 查询角色名称
     *
     * @param roleId
     * @return
     */
    PlatformUserVo findByRoleName(Integer roleId);

    /**
     * 该角色下所有权限
     *
     * @param roleId
     * @return
     */
    List<PlateformAuthorityDTO> findAuthority(Integer roleId);

    List<PlateformAuthorityDTO> findAuthorityNew(Integer roleId);

    List<MmanLoanCollectionPerson> findCollecterByCurrentUnCompleteCount(Map<String, Object> param);

    /**
     * 查询当前催收员今日派到手里的订单数(包括已完成的)
     *
     * @param mmanLoanCollectionPerson 催收员
     */
    Integer findTodayAssignedCount(MmanLoanCollectionPerson mmanLoanCollectionPerson);

    /**
     * 通过角色id查询用户
     *
     * @param param
     * @return
     */
    List<OperatorVo> findByRoleId(Map<String, Object> param);

    /**
     * 查询催收
     *
     * @param param
     * @return
     */
    List<OperatorVo> findByCuishouRoleId(Map<String, Object> param);

    /**
     * 查询最早添加的那个管理员
     *
     * @return
     */
    PlatformUser findFirstAdmin();

    List<UserPhoneInfo> findAllUser(String phone);
}