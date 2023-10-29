package com.summer.web.controller.admin;

import com.summer.api.service.RiskGongzhaiContentService;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.pojo.vo.BackConfigParamsVo;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jobob
 * @since 2020-07-31
 */
@RestController
@RequestMapping("/v1.0/api/riskGongzhaiContent")
public class RiskGongzhaiContentController extends BaseController {
    @Autowired
    private RiskGongzhaiContentService riskGongZhaiContentService;
    @Resource
    IBackConfigParamsDao backConfigParamsDao;


    /**
     * 共债开关列表
     * */
    @GetMapping("/gzSwitchList")
    public CallBackResult getGzTc(HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        Map<String,Object> res = new HashMap<>();
         BackConfigParamsVo lzgz =  backConfigParamsDao.findBySysKey("lzgz");
         BackConfigParamsVo zmgz =  backConfigParamsDao.findBySysKey("zmgz");
         BackConfigParamsVo gz101 =  backConfigParamsDao.findBySysKey("101gz");
         BackConfigParamsVo bdf =  backConfigParamsDao.findBySysKey("bdf");
         BackConfigParamsVo tcgz =  backConfigParamsDao.findBySysKey("tcgz");
         BackConfigParamsVo xpafgz =  backConfigParamsDao.findBySysKey("xpafgz");
         BackConfigParamsVo qjldgz =  backConfigParamsDao.findBySysKey("risk_qjld");
         BackConfigParamsVo besjgz =  backConfigParamsDao.findBySysKey("risk_besj");
         res.put("lzgz",lzgz);
         res.put("zmgz",zmgz);
         res.put("gz101",gz101);
         res.put("bdf",bdf);
         res.put("tcgz",tcgz);
         res.put("xpafgz",xpafgz);
         res.put("qjldgz",qjldgz);
         res.put("besjgz",besjgz);
        return CallBackResult.ok(res);
    }

    /**
     * 获取全景雷达数据共债报告
     *
     * @param userId
     */
    @GetMapping("/getLeiDaGongZhai")
    public CallBackResult<String> getLeiDaGongZhai(HttpServletRequest request, @RequestParam Integer userId,@RequestParam Integer status) {
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.fail(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }*/

        return riskGongZhaiContentService.queryLeiDa(userId,status);
    }

    /**
     * 获取擎天信用多头
     *
     * @param userId
     */
    @GetMapping("/getQinTianDuoTou")
    public CallBackResult<String> getQinTianDuoTou(HttpServletRequest request, @RequestParam Integer userId,@RequestParam Integer status) {
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.fail(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }*/
        return riskGongZhaiContentService.getQinTianDuoTou(userId,status);
    }

    /**
     * 获取探针C
     *
     * @param userId
     */
    @GetMapping("/getTanZhenC")
    public CallBackResult<String> getTanZhenC(HttpServletRequest request, @RequestParam Integer userId,@RequestParam Integer status) {
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.fail(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }*/
        return riskGongZhaiContentService.getTanZhenC(userId,status);
    }

    /**
     * 获取五花共债报告
     *
     * @param userId
     */
    @GetMapping("/getRadarGongZhai")
    public CallBackResult<String> getRadarGongZhai(HttpServletRequest request, @RequestParam Integer userId,@RequestParam Integer status) {
       /* PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.fail(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }*/
        return riskGongZhaiContentService.getRadarGongZhai(userId,status);
    }

    /**
     * 获取juguang共债报告
     *
     * @param userId
     */
    @GetMapping("/getJuGuangGongZhai")
    public CallBackResult<String> getJuGuangGongZhai(HttpServletRequest request, @RequestParam Integer userId,@RequestParam Integer status) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.fail(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        return riskGongZhaiContentService.getJuGuangGongZhai(userId,status);
    }

    /**
     * 获取payOrder共债报告
     *
     * @param userId
     */
    @GetMapping("/getPayOrderDept")
    public CallBackResult<String> getPayOrderDept(HttpServletRequest request, @RequestParam Integer userId,@RequestParam Integer status) {
        /*PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.fail(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER && platformUser.getRoleId() != Constant.ROLEID_REVIEWER) {
            return CallBackResult.fail(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }*/
        return riskGongZhaiContentService.getPayOrderDept(userId,status);
    }
}
