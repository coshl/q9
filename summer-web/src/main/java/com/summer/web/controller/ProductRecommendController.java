package com.summer.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.ProductRecommend;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.ProductRecommendDAO;
import com.summer.util.CallBackResult;
import com.summer.util.Constant;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ： ProductRecommendController
 * @Description ：产品推荐Controller
 * @Author：
 * @Date ：2020/2/11 20:01
 * @Version ：V1.0
 **/
@Validated
@RestController
@RequestMapping("/v1.0/api/product")
public class ProductRecommendController extends BaseController{
    @Resource
    private ProductRecommendDAO productRecommendDAO;
    /**
     * 新增推荐产品
     * @param request
     * @param jsonData
     * @return
     */
    @RequestMapping("/addProduct")
    public Object addProduct(HttpServletRequest request,@RequestBody String jsonData){
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        Map<String, Object> params = JSONObject.parseObject(jsonData);
        productRecommendDAO.insertSelective(params);
        return CallBackResult.returnJson(CallBackResult.SUCCESS,"产品添加成功");

    }

    /**
     * 删除推荐产品
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/deleteProduct")
    public Object deleteProduct(HttpServletRequest request,@NotNull(message = CallBackResult.PARAM_IS_EXCEPTION_MSG) Integer id){
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        productRecommendDAO.deleteByPrimaryKey(id);
        return CallBackResult.returnJson(CallBackResult.SUCCESS,CallBackResult.MSG_NO_CONTENT_SUCCESS);

    }

    /**
     * 修改推荐产品
     * @param request
     * @param jsonData
     * @return
     */
    @RequestMapping("/updateProduct")
    public Object updateProduct(HttpServletRequest request,@RequestBody String jsonData){
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }

        Map<String, Object> params = JSONObject.parseObject(jsonData);
        productRecommendDAO.updateByPrimaryKeySelective(params);
        return CallBackResult.returnJson(CallBackResult.SUCCESS,"修改成功");
    }

    /**
     * 后台管理查询推荐产品
     * @param request
     * @return
     */
    @RequestMapping("/selectProduct")
    public Object selectBackProduct(HttpServletRequest request){
        PlatformUser platformUser = redisPlatformUser(request);
        if (null == platformUser) {
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        // 超管
        if (platformUser.getRoleId() != Constant.ROLEID_SUPER ) {
            return CallBackResult.returnJson(Constant.RESULT_BAD_STATUS, Constant.UN_AUTH_VIST);
        }
        List<ProductRecommend> productRecommends = productRecommendDAO.findByParam(null);
        return CallBackResult.returnJson(CallBackResult.SUCCESS,CallBackResult.MSG_SUCCESS,productRecommends);
    }

    /**
     * App推荐产品
     * @param request
     * @return
     */
    @RequestMapping("/selectAppProduct")
    public Object selectAppProduct(HttpServletRequest request){
        UserInfo userInfo = redisUser(request);
        if (null == userInfo){
            return CallBackResult.returnJson(CallBackResult.LOGIN_IS_PAST, Constant.USER_MSG_NOT_EXIST);
        }
        List<ProductRecommend> productRecommends = productRecommendDAO.findByParam(null);
        return CallBackResult.returnJson(CallBackResult.SUCCESS,CallBackResult.MSG_SUCCESS,productRecommends);
    }
}
