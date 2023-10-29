package com.summer.web.controller.userManager;

import com.alibaba.fastjson.JSONObject;
import com.summer.annotation.Log;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PlatformRole;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.PlatformRoleAuthorityMapper;
import com.summer.api.service.IPlateformRoleService;
import com.summer.api.service.IPlatformRoleAuthorityService;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.pojo.vo.PlatformRoleAuthorityVo;
import com.summer.pojo.vo.RoleListVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 角色管理Controller
 */
@RestController
@RequestMapping("/v1.0/api/role")
public class UserRoleController extends BaseController {

    @Resource
    private IPlateformRoleService plateformRoleService;

    @Resource
    private IPlatformRoleAuthorityService platformRoleAuthorityService;

    @Resource
    private PlatformRoleAuthorityMapper roleAuthorityMapper;

    /**
     * 角色列表，用于添加用户
     *
     * @return
     */
    @PostMapping("/findAll")
    public String findAll(HttpServletRequest request) throws Exception {
        // 获取登录用户
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 登录失效
        if ( Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        return CallBackResult.returnJson(plateformRoleService.findAll());
    }

    /**
     * 权限列表
     *
     * @return
     */
//    @PostMapping("/findByAuthority")
    public String findByAuthority() throws Exception {
        return CallBackResult.returnJson(plateformRoleService.findByAuthority());
    }

    /**
     * 加载角色下的权限信息
     *
     * @param jsonData
     * @return
     */
//    @PostMapping("/findRoleAuthority")
    public String findRoleAuthority(@RequestBody String jsonData) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer id = (Integer) params.get("id");
        List<PlatformRoleAuthorityVo> roleAuthority = platformRoleAuthorityService.findRoleAuthority(id);
        if (roleAuthority == null || roleAuthority.size() < 0) {
            return CallBackResult.returnJson(999, "角色列表为空或不存在");
        }
        return CallBackResult.returnJson(roleAuthority);
    }

    /**
     * 添加角色并赋予权限
     *
     * @param jsonData
     * @param request
     * @return
     */
    @Log(title = "添加角色")
//    @PostMapping("/insertRole")
    public String insertRole(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        String ids = (String) params.get("authorityList");
        String roleName = (String) params.get("roleName");
        Integer status = (Integer) params.get("status");
        //获取当前登录用户
        PlatformUser platformUser = redisPlatformUser(request);

        PlatformRole platformRole = new PlatformRole();
        platformRole.setRoleSuperId(platformUser.getRoleId());
        platformRole.setName(roleName);
        platformRole.setDescription(roleName);
        platformRole.setStatus(status);
        //添加角色
        plateformRoleService.insert(platformRole);
        // 添加权限
        platformRoleAuthorityService.setAuthority(platformRole.getId(), ids);
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_CREATED);
    }

    /**
     * 修改角色名称和权限
     *
     * @param jsonData
     * @param request
     * @return
     */
    @Log(title = "修改角色")
//    @PostMapping("/updateRole")
    public String updateRole(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        String ids = (String) params.get("authorityList");
        Integer id = (Integer) params.get("id");
        String roleName = (String) params.get("roleName");
        Integer status = (Integer) params.get("status");
        //获取当前登录用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        //查询角色信息
        PlatformRole platformRole = plateformRoleService.findById(id);
        platformRole.setRoleSuperId(platformUser.getId());
        platformRole.setName(roleName);
        platformRole.setStatus(status);
        //修改角色
        plateformRoleService.update(platformRole);
        if (!"".equals(ids) && ids != null) {
            // 设置权限
            platformRoleAuthorityService.setAuthority(platformRole.getId(), ids);
        } else {
            // 删除该角色所有的权限
            roleAuthorityMapper.deleteByRoleId(platformRole.getId());
        }
        return CallBackResult.returnJson(CallBackResult.SUCCESS, Constant.AUTH_UPDATE_SUCC);
    }

    /**
     * 删除角色
     *
     * @param jsonData
     * @param request
     * @return
     */
    @Log(title = "修改角色")
//    @PostMapping("/deleteRole")
    public String deleteRole(@RequestBody String jsonData, HttpServletRequest request) throws Exception {
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        Integer id = (Integer) params.get("id");
        //获取当前登录用户信息
        PlatformUser platformUser = redisPlatformUser(request);
        //查询角色信息
        PlatformRole platformRole = plateformRoleService.findById(id);
        platformRole.setRoleSuperId(platformUser.getId());
        platformRole.setStatus(2);
        //修改角色
        plateformRoleService.update(platformRole);
        // 删除角色权限记录
        platformRoleAuthorityService.deleteByRoleId(platformRole.getId());
        return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_NO_CONTENT_SUCCESS);
    }

    @GetMapping("/getRole")
    public Object getRoleList(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }

        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        List<PlatformRole> PlatformRole = plateformRoleService.findAll();
        if (CollectionUtils.isNotEmpty(PlatformRole)) {
            List<RoleListVo> roleListVos = new ArrayList<>();
            for (PlatformRole platformRole : PlatformRole) {
                RoleListVo roleListVo = new RoleListVo();
                roleListVo.setId(platformRole.getId());
                roleListVo.setDescription(platformRole.getDescription());
                roleListVos.add(roleListVo);
            }
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_SUCCESS, roleListVos);
        } else {
            return CallBackResult.returnJson(CallBackResult.SUCCESS, CallBackResult.MSG_DATA_EMPTY);
        }

    }
}
