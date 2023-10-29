package com.summer.web.controller.front;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.web.controller.BaseController;
import com.summer.dao.entity.PlatformBanner;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.UserInfo;
import com.summer.group.AddBanner;
import com.summer.group.GetBanner;
import com.summer.group.UpdateBanner;
import com.summer.dao.mapper.PlatformBannerMapper;
import com.summer.dao.mapper.UserInfoMapper;
import com.summer.pojo.dto.BannerDTO;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import com.summer.util.RedisUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ls
 * @Title: banner 启动页管理
 * @date 2019/1/26 16:33
 */
@RestController
@RequestMapping("/v1.0/api/user")
public class PlatformBannerController extends BaseController {
    @Resource
    private PlatformBannerMapper platformBannerMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 添加
     *
     * @return
     */
    @PostMapping("/addBanner")
    public String addBanner(@Validated(AddBanner.class)
                            @RequestBody BannerDTO bannerDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        // 超管
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        PlatformBanner record = new PlatformBanner();
        record.setEquementType(1);
        record.setType(bannerDTO.getType());
        record.setTitle(bannerDTO.getTitle());
        record.setSort(bannerDTO.getOrderBy());
        UserInfo userInfo = userInfoMapper.findByPhone(platformUser.getPhoneNumber());
        record.setAddPerson(userInfo.getRealName());
        record.setUrl(bannerDTO.getReurl());
        record.setReurl(bannerDTO.getReurl());
        record.setStatus(bannerDTO.getStatus());
        int i = platformBannerMapper.insertSelective(record);
        if (i > 0) {
            deleteIndexInfo();
            return CallBackResult.returnSuccessJson();
        }
        return CallBackResult.returnErrorJson();
    }

    /**
     * 删除
     *
     * @return
     */
    @GetMapping("/delBanner")
    public String delBanner(Integer id, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        // 超管
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        int i = platformBannerMapper.deleteByPrimaryKey(id);
        if (i > 0) {
            deleteIndexInfo();
            return CallBackResult.returnSuccessJson();
        }
        return CallBackResult.returnErrorJson();
    }

    /**
     * 修改
     *
     * @return
     */
    @PostMapping("/updateBanner")
    public String updateBanner(@Validated(UpdateBanner.class) @RequestBody BannerDTO bannerDTO, HttpServletRequest request) {
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        // 超管
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        PlatformBanner record = platformBannerMapper.selectByPrimaryKey(bannerDTO.getId());

        record.setEquementType(bannerDTO.getType());
        record.setTitle(bannerDTO.getTitle());
        record.setSort(bannerDTO.getOrderBy());
        record.setUrl(bannerDTO.getReurl());
        record.setReurl(bannerDTO.getReurl());
        record.setStatus(bannerDTO.getStatus());
        int i = platformBannerMapper.updateByPrimaryKeySelective(record);
        if (i > 0) {
            deleteIndexInfo();
            return CallBackResult.returnSuccessJson();
        }
        return CallBackResult.returnErrorJson();
    }


    /**
     * 查询
     *
     * @param bannerDTO
     * @return
     */
    @PostMapping("/getBanner")
    public String getBanner(@Validated(GetBanner.class) @RequestBody BannerDTO bannerDTO, HttpServletRequest request) {

        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.BUSINESS_ERROR, "参数非法");
        }
        // 超管
        if (Constant.ROLEID_SUPER != platformUser.getRoleId()) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, "没有该权限");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("title", bannerDTO.getTitle());
        map.put("type", bannerDTO.getType());
        map.put("pageNum", bannerDTO.getPageNum());
        map.put("pageSize", bannerDTO.getPageSize());
        PageHelper.startPage(map);
        List<PlatformBanner> platformBanners = platformBannerMapper.selectByParams(map);
        return CallBackResult.returnJson(new PageInfo<>(platformBanners));
    }

    public void deleteIndexInfo() {
        if (redisUtil.hasKey(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY)) {
            redisUtil.del(Constant.APP_VERSION + Constant.INDEX_REDIS_DATA_KEY);
        }
    }
}
