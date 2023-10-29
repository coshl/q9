package com.summer.dao.mapper;

import com.summer.dao.entity.SourceUserLoginInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户登录日志Mapper接口
 */
public interface SourceUserLoginInfoMapper {

    /**
     * 根据手机号和h5编码查询
     *
     * @param phoneNumber
     * @param sourceH5Code
     * @return
     */
    public SourceUserLoginInfo findByPhoneAndH5Code(@Param("phoneNumber") String phoneNumber, @Param("sourceH5Code") String sourceH5Code);

    /**
     * 保存用户登录日志信息
     *
     * @param sourceUserLoginInfo
     * @return
     * @throws Exception
     */
    int save(SourceUserLoginInfo sourceUserLoginInfo) throws Exception;

    /**
     * 更新日志信息
     *
     * @param sourceUserLoginInfo
     * @return
     * @throws Exception
     */
    int update(SourceUserLoginInfo sourceUserLoginInfo) throws Exception;

    /**
     * 根据id查找
     *
     * @return
     */
    SourceUserLoginInfo findById(Long id);

    /**
     * 通过手机号码查询
     *
     * @return
     */
    SourceUserLoginInfo findByPhoneNumber(Map<String, Object> map);

    /**
     * 批量删除
     *
     * @return
     */
    int deleteAll(String[] ids);

    /**
     * 统计
     *
     * @param map
     * @return
     */
    Long count(Map<String, Object> map);

    /**
     * 根据手机号码更新
     *
     * @param sourceUserLoginInfo
     * @return
     */
    int updateByPhoneNumber(SourceUserLoginInfo sourceUserLoginInfo);

    /**
     * 通过手机号码查找
     *
     * @param phone
     * @return
     */
    List<SourceUserLoginInfo> findByPhone(String phone);

    /**
     * 根据渠道id查询
     *
     * @param channelId
     * @return
     */
    Integer findBySourceChannelCode(SourceUserLoginInfo sourceUserLoginInfo);
}
