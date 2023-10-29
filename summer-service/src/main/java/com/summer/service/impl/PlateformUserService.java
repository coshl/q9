package com.summer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.mapper.PlateformChannelMapper;
import com.summer.dao.mapper.PlatformUserMapper;
import com.summer.api.service.IPlateformUserService;
import com.summer.util.CallBackResult;
import com.summer.util.JwtUtil;
import com.summer.util.MD5Util;
import com.summer.util.RedisUtil;
import com.summer.pojo.vo.OperatorVo;
import com.summer.pojo.vo.PlatformUserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统用户Service
 */
@Service
public class PlateformUserService implements IPlateformUserService {

    @Resource
    private PlatformUserMapper platformUserMapper;

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private PlateformChannelMapper plateformChannelMapper;

    /**
     * 根据手机号查询用户信息
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public PlatformUser findByPhoneNumber(String phoneNumber) {
        return platformUserMapper.findByPhoneNumber(phoneNumber);
    }

    /**
     * 添加系统用户信息
     *
     * @param platformUser
     * @return
     */
    @Override
    public void save(PlatformUser platformUser) {
        platformUserMapper.insertSelective(platformUser);
    }

    /**
     * 更新系统用户信息
     *
     * @param platformUser
     */
    @Override
    public int update(PlatformUser platformUser) {
        return platformUserMapper.updateByPrimaryKeySelective(platformUser);

    }

    /**
     * 验证用户名和密码
     *
     * @param platformUser
     * @return
     */
    /*@Override
    public String userLogin(PlatformUser platformUser) {
        //根据手机号查询用户信息
        PlatformUser user = platformUserMapper.findByPhoneNumber(platformUser.getPhoneNumber());
        Date now = new Date();
        if (user == null) {
            //返回登录失败
            return CallBackResult.returnJson(9999, "手机号码错误或用户不存在");
        }
        //判断用户是否已经被禁用
        int status1 = user.getStatus();
        if (status1 == 1) {
            return CallBackResult.returnJson(9909, "账号或密码错误");
        }
        //判断用户是否被冻结
        PlateformChannel plateformChannel = plateformChannelMapper.selectByPrimaryKey(platformUser.getChannelId());
        if (null != plateformChannel) {
            int status = plateformChannel.getStatus();
            if (status == 2) {
                return CallBackResult.returnJson(9909, "账号或密码错误");
            }
        }
        // 判断验证码是否正确
        if (StringUtils.isNotBlank(platformUser.getCode())) {// 验证码不为空
            if (user == null || StringUtils.isEmpty(user.getCode())) {
                return CallBackResult.returnJson(999, "请获取验证码");
            } else if (now.compareTo(user.getLastCodeTime()) > 0) {
                return CallBackResult.returnJson(998, "验证码已失效，请重新获取");
            } else if (!platformUser.getCode().equals(user.getCode())) {
                return CallBackResult.returnJson(997, "验证码错误");
            }
        } else {
            return CallBackResult.returnJson(996, "输入验证码为空");
        }
        //判断用户名是否正确
//        if (!platformUser.getUserName().equals(user.getUserName())) {
//            // 如果不正确，返回登录失败
//            return CallBackResult.returnJson(400, "用户名或密码错误");
//        }
        //判断密码是否正确
        if (!(MD5Util.md5(platformUser.getPassword() + user.getSalt())).equals(user.getPassword())) {
            // 如果不正确，返回登录失败
            return CallBackResult.returnJson(400, "用户名或密码错误");
        }

        // 如果正确生成token。
        String token = JwtUtil.generateToken(null, null);
        return CallBackResult.returnJson(token);
    }*/

    /**
     * 账号管理分页列表
     *
     * @param params
     * @return
     */
    @Override
    public PageInfo<PlatformUserVo> findParams(Map<String, Object> params) {
        List<PlatformUserVo> list = platformUserMapper.findParams(params);
        return new PageInfo<>(list);
    }

    /**
     * 删除用户
     *
     * @param params
     */
    @Override
    public void deleteByStatus(Map<String, Object> params) {
        platformUserMapper.deleteByStatus(params);
    }

    /**
     * 根据token取用户信息
     *
     * @param token
     * @return
     */
    @Override
    public String getUserByToken(String token) {
        //根据token到redis中取用户信息
        String json = redisUtil.get(token);
        //取不到用户信息，登录已经过期，返回登录过期
        if (StringUtils.isBlank(json)) {
            return CallBackResult.returnJson(201, "用户登录已经过期");
        }
        //取到用户信息更新token的过期时间
        redisUtil.expire(token, 1800);
        //返回结果
        PlatformUser user = JSON.parseObject(json, PlatformUser.class);
        return CallBackResult.returnJson(user);
    }

    /**
     * 修改角色
     *
     * @param params
     */
    @Override
    public int updateByRole(Map<String, Object> params) {
        return platformUserMapper.updateByRole(params);
    }

    @Override
    public List<OperatorVo> findByRoleId(Map<String, Object> param) {
        return platformUserMapper.findByRoleId(param);
    }

    @Override
    public List<OperatorVo> findByCuishouRoleId(Integer roleId) {
        Map<String, Object> param = new HashMap<>();
        param.put("roleId", roleId);
        return platformUserMapper.findByCuishouRoleId(param);
    }

    @Override
    public PlatformUser findFirstAdmin() {
        return platformUserMapper.findFirstAdmin();
    }

    @Override
    public PlatformUser findById(Integer id) {
        return platformUserMapper.selectByPrimaryKey(id);
    }
}
