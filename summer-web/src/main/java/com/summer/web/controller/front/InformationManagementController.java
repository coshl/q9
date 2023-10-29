package com.summer.web.controller.front;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.InformationManagement;
import com.summer.dao.entity.PlatformUser;
import com.summer.group.AddInformationManagement;
import com.summer.group.GetInformationManagement;
import com.summer.group.UpdateInformationManagement;
import com.summer.dao.mapper.InformationManagementDAO;
import com.summer.pojo.dto.InformationManagementDTO;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ls
 * @Title: 信息管理
 * @date 2019/1/26 9:44
 */
@RestController
@RequestMapping("/v1.0/api/appManager")
public class InformationManagementController extends BaseController {
    @Resource
    private InformationManagementDAO informationManagementDAO;

    /**
     * 添加信息管理
     *
     * @return
     */
    @PostMapping("/addInformationManagement")
    public String addInformationManagement(@Validated(AddInformationManagement.class) @RequestBody InformationManagementDTO informationManagementDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        InformationManagement record = new InformationManagement();
        record.setType(informationManagementDTO.getType());
        record.setTitle(informationManagementDTO.getTitle());
        record.setAddPerson(platformUser.getUserName());
        record.setModifyPerson(platformUser.getUserName());
        record.setOrderBy(informationManagementDTO.getOrderBy());
        record.setContent(informationManagementDTO.getContent());
        int i = informationManagementDAO.insert(record);
        if (i > 0) {
            return CallBackResult.returnSuccessJson();
        }
        return CallBackResult.returnErrorJson();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @GetMapping("/deleteInformationManagement")
    public String deleteInformationManagement(Integer id, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        int i = informationManagementDAO.deleteByPrimaryKey(id);
        if (i > 0) {
            return CallBackResult.returnSuccessJson();
        } else {
            return CallBackResult.returnErrorJson();
        }
    }

    /**
     * 更新
     *
     * @param informationManagementDTO
     * @return
     */
    @PostMapping("/updateInformationManagement")
    public String updateInformationManagement(@Validated(UpdateInformationManagement.class) @RequestBody InformationManagementDTO informationManagementDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "参数非法");
        }
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        InformationManagement record = informationManagementDAO.selectByPrimaryKey(informationManagementDTO.getId());
        record.setType(informationManagementDTO.getType());
        record.setTitle(informationManagementDTO.getTitle());
        record.setModifyPerson(platformUser.getUserName());
        record.setOrderBy(informationManagementDTO.getOrderBy());
        record.setContent(informationManagementDTO.getContent());
        int update = informationManagementDAO.updateByPrimaryKeySelective(record);
        if (update > 0) {
            return CallBackResult.returnSuccessJson();
        }
        return CallBackResult.returnErrorJson();
    }

    /**
     * 根据条件查询
     *
     * @param informationManagementDTO
     * @return
     */
    @PostMapping("/getInformationManagement")
    public String getInformationManagement(@Validated(GetInformationManagement.class)
                                           @RequestBody InformationManagementDTO informationManagementDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("title", informationManagementDTO.getTitle());
        map.put("content", informationManagementDTO.getContent());
        map.put("addPerson", informationManagementDTO.getAddPerson());
        map.put("type", informationManagementDTO.getType());
        map.put("startTime", informationManagementDTO.getStartTime());
        map.put("endTime", informationManagementDTO.getEndTime());
        map.put("pageSize", informationManagementDTO.getPageSize());
        map.put("pageNum", informationManagementDTO.getPageNum());
        PageHelper.startPage(map);
        List<InformationManagement> informationManagements = informationManagementDAO.selectByParams(map);
        return CallBackResult.returnJson(new PageInfo<>(informationManagements));
    }
}
