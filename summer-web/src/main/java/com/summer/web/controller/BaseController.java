package com.summer.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.PlatformUser;
import com.summer.dao.entity.UserInfo;
import com.summer.dao.mapper.IBackConfigParamsDao;
import com.summer.dao.mapper.PlatformUserMapper;
import com.summer.dao.mapper.UserInfoMapper;
import com.summer.util.Constant;
import com.summer.util.IpAdrressUtil;
import com.summer.util.RedisUtil;
import com.summer.util.StringUtil;
import com.summer.util.encrypt.AESDecrypt;
import com.summer.pojo.vo.BackConfigParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.*;

@Slf4j
public class BaseController {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private PlatformUserMapper platformUserMapper;
    @Resource
    private IBackConfigParamsDao backConfigParamsDao;

    @Value("${system.isWhiteList}")
    private String isWhiteList;

    @Value("${exporExcel_id}")
    private String exporExcelId;

    private static Logger LOG = LoggerFactory.getLogger(BaseController.class);
   //TODO 00 通用设置
    /**
     * 得到 session
     *
     * @return
     */
    protected HttpSession getSession(HttpServletRequest request) {
        return request.getSession();
    }


    protected Object errorMsg(String msg) {
        if (msg == null || msg.isEmpty()) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "1");
        map.put("message", msg);
        return map;
    }

    protected Object successMsg(String msg, Object obj) {
        if (msg == null || msg.isEmpty() || obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "200");
        map.put("message", msg);
        map.put("data", obj);
        return map;
    }

    protected Object successMsg(String msg) {
        if (msg == null || msg.isEmpty()) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "200");
        map.put("message", msg);
        return map;
    }

    /**
     * 获得request中的参数
     *
     * @param request
     * @return string object类型的map
     */
    public HashMap<String, Object> getParametersO(HttpServletRequest request) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes()).getRequest();
        }
        Map req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {
            Map<String, Object> p = new HashMap<String, Object>();
            Collection keys = req.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                Object value = req.get(key);
                Object v = null;
                if ((value.getClass().isArray())
                        && (((Object[]) value).length > 0)) {
                    if (((Object[]) value).length > 1) {
                        v = ((Object[]) value);
                    } else {
                        v = ((Object[]) value)[0];
                    }
                } else {
                    v = value;
                }
                if ((v != null) && ((v instanceof String)) && !"\"\"".equals(v)) {
                    String s = ((String) v).trim();
                    if (s.length() > 0) {
                        p.put(key, s);
                    }
                }
            }
            hashMap.putAll(p);
            // 读取cookie
            hashMap.putAll(ReadCookieMap(request));

        }
        return hashMap;
    }

    /**
     * 得到页面传递的参数封装成map
     */

    public Map<String, String> getParameters(HttpServletRequest request) {
        if (request == null) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        Map<String, String> p = new HashMap<String, String>();
        Map req = request.getParameterMap();
        if ((req != null) && (!req.isEmpty())) {

            Collection keys = req.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                Object value = req.get(key);
                Object v = null;
                if ((value.getClass().isArray())
                        && (((Object[]) value).length > 0)) {
                    v = ((Object[]) value)[0];
                } else {
                    v = value;
                }
                if ((v != null) && ((v instanceof String)) && !"\"\"".equals(v)) {
                    String s = (String) v;
                    if (s.length() > 0) {
                        p.put(key, s);
                    }
                }
            }
            //读取cookie
            p.putAll(ReadCookieMap(request));
            return p;
        }
        return p;
    }

    /**
     * 将cookie封装到Map里面
     *
     * @param request
     * @return
     */
    private static Map<String, String> ReadCookieMap(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<String, String>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }

    /**
     * 输出json
     *
     * @param response
     * @param
     */
    public void toObjectJson(HttpServletResponse response, String json) {
        PrintWriter out = null;
        try {
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    /**
     * 从redis获取到登录对象（后台用户）
     *
     * @return
     */
    public PlatformUser redisPlatformUser(HttpServletRequest request) {
        //判断用户是否登录
        String token = request.getHeader("Authorization");
        PlatformUser platformUser = null;
        if (redisUtil.hasKey(Constant.TOKEN + token)) {
            String user = redisUtil.get(Constant.TOKEN + token);
            platformUser = JSONObject.parseObject(user, PlatformUser.class);

            if (StringUtils.isNotBlank(isWhiteList)) {
                String[] isWhiteListArray = isWhiteList.split(",");
                for (int i = 0; i < isWhiteListArray.length; i++) {
                    if (isWhiteListArray[i].equals(platformUser.getPhoneNumber())) {
                        return platformUser;
                    }
                }
            }
            PlatformUser platformUsernew = platformUserMapper.selectByPrimaryKey(platformUser.getId());
            if (null != platformUsernew) {
                if (platformUser.getStatus() == 1) {
                    return null;
                }
            } else {
                return null;
            }
        }
        // 渠道方用户只能查询渠道转化
        if (platformUser != null && platformUser.getRoleId() == Constant.ROLEID_CHANNELUSER && !isChannelUrl(request)) {
            LOG.error("渠道方用户越权操作，phone：" + platformUser.getPhoneNumber() + "请求路径：" + request.getServletPath() + "请求ip:" + IpAdrressUtil.getIpAdrress(request));
            platformUser = null;
        }
        return platformUser;
    }

    private static final PathMatcher pathMatcher = new AntPathMatcher();

    private boolean isChannelUrl(HttpServletRequest request) {
        return pathMatcher.match("/v1.0/api/dailystatics/findChannelStatistics", request.getServletPath()) || pathMatcher.match("/v1.0/api/platformChannel/getChannelName", request.getServletPath());
    }

    /**
     * 从redis获取到登录对象（APP）
     *
     * @return
     */
    public UserInfo redisUser(HttpServletRequest request) {
        //判断用户是否登录
        String token = request.getHeader("Authorization");
        UserInfo userInfo = null;
        if (redisUtil.hasKey(Constant.APP_TOKEN_PREFIX + token)) {
            String user = redisUtil.get(Constant.APP_TOKEN_PREFIX + token);

            userInfo = JSONObject.parseObject(user, UserInfo.class);
            userInfo = userInfoMapper.selectByUserId(userInfo.getId());
            if (null != userInfo) {
                byte status = userInfo.getStatus();
                //如果用户已经被删除状态，就返回null登陆失效，让用户重新登陆就不能登陆了 ，因为没办法直接拿到移动端的token
                if (status == 2) {
                    return null;
                }
            }
        }
        return userInfo;
    }

    /**
     * 从redis获取到登录对象（APP）
     *
     * @return
     */
    public UserInfo redisUser(String token) {
        //判断用户是否登录
        UserInfo userInfo = null;
        if (redisUtil.hasKey(Constant.APP_TOKEN_PREFIX + token)) {
            String user = redisUtil.get(Constant.APP_TOKEN_PREFIX + token);

            userInfo = JSONObject.parseObject(user, UserInfo.class);
            userInfo = userInfoMapper.selectByUserId(userInfo.getId());
            if (null != userInfo) {
                byte status = userInfo.getStatus();
                //如果用户已经被删除状态，就返回null登陆失效，让用户重新登陆就不能登陆了 ，因为没办法直接拿到移动端的token
                if (status == 2) {
                    return null;
                }
            }
        }
        return userInfo;
    }

    protected UserInfo getLoginUser(HttpServletRequest request, String telephone) {
        String token = request.getHeader("Authorization");
        String userStr = redisUtil.get(Constant.APP_TOKEN_PREFIX + token);
        UserInfo user = null;
        if (StringUtils.isNotBlank(userStr)) {
            user = JSON.parseObject(userStr, UserInfo.class);
        }
        if (StringUtils.isBlank(telephone)) {
            return null;
        }
        List<UserInfo> userInfoList = userInfoMapper.selectSimple(telephone, null);
        if (CollectionUtils.isNotEmpty(userInfoList)) {
            return userInfoList.get(0);
        }
        return user;
    }

    /**
     * 返回登陆失效
     *
     * @param response
     * @param
     */
    public void returnLoginBad(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            HashMap<String, String> resMap = new HashMap<String, String>();
            resMap.put("code", "-2");
            resMap.put("message", "登陆失效，请重新登陆！");
            resMap.put("data", null);
            String json = JSONObject.toJSONString(resMap);
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    /**
     * 返回错误权限
     *
     * @param response
     * @param
     */
    public void returnErrorAuth(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            HashMap<String, String> resMap = new HashMap<String, String>();
            resMap.put("code", "-1");
            resMap.put("message", "对不起，您无权操作！");
            resMap.put("data", null);
            String json = JSONObject.toJSONString(resMap);
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        } finally {
            out.close();
        }
    }

    /**
     * 下载开关判断
     *
     * @return
     */
    public Integer downloadSwitch() {
        BackConfigParamsVo bySysKey = backConfigParamsDao.findBySysKey("download_switch");
        log.info("导出下载download_switch========================="+bySysKey);
        if (null != bySysKey) {
            Integer sysValue = bySysKey.getSysValue();
            //后台所有列表下载开关 0开启，1关闭
            if (sysValue != 0) {
                return sysValue;
            }
        } else {
            return 1;
        }
        return null;
    }

    /**
     * 判断登录手机号
     *
     * @param phoneNumber
     * @return
     */
    public boolean isExporExcel(String phoneNumber) {
        log.info("当前登录的手机号为{}",phoneNumber);
        if (exporExcelId.contains(",")) {
            String[] phone = exporExcelId.split(",");
            int count = StringUtil.countStr(exporExcelId, ",");
            for (int i = 0; i <= count; i++) {
                if (phone[i].equals(phoneNumber)) {
                    log.info("当前登录的状态false");
                    return false;
                }
            }
        } else if (phoneNumber.equals(exporExcelId)) {
            log.info("当前登录的状态false");
            return false;

        }
        log.info("当前登录的状态true");
        return true;
    }

    /**
     * 返回错误权限
     *
     * @param response
     * @param
     */
    public void downloadBad(HttpServletResponse response) {
        PrintWriter out = null;
        try {
            HashMap<String, String> resMap = new HashMap<String, String>();
            resMap.put("code", "-1");
            resMap.put("message", "下载失败，下载通道关闭！");
            resMap.put("data", null);
            String json = JSONObject.toJSONString(resMap);
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        } finally {
            out.close();
        }
    }
}
