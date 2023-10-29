package com.summer.api.service;

import com.github.pagehelper.PageInfo;
import com.summer.dao.entity.PlateformChannel;
import com.summer.pojo.dto.PlateformChannelDTO;
import com.summer.pojo.dto.PlateformChannelParamDto;
import com.summer.pojo.vo.ChannelLinkVO;
import com.summer.pojo.vo.ChannelStaffVo;
import com.summer.pojo.vo.PlateformChannelListVo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IPlateformChannelService {

    /**
     * 渠道列表
     *
     * @param params
     * @return
     */
    PageInfo<PlateformChannel> query(Map<String, Object> params);

    /**
     * 更新渠道状态
     *
     * @param plateformChannel
     */
    int updateByStatus(PlateformChannel plateformChannel);

    /**
     * 添加渠道
     *
     * @param plateformChannelDTO
     */
    int insertChannel(PlateformChannelDTO plateformChannelDTO, HttpServletRequest request);

    /**
     * 查询渠道人员统计
     *
     * @param params
     * @return
     */
    PageInfo<ChannelStaffVo> findParams(Map<String, Object> params);

    int deleteByPrimaryKey(Integer id);

    int insertSelective(PlateformChannelParamDto record);

    PlateformChannel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PlateformChannelParamDto record);

    int updateByPrimaryKey(PlateformChannel record);


    /**
     * 更新渠道状态
     *
     * @param id
     * @param status
     */
    int updateByStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 添加渠道
     *
     * @param plateformChannel
     * @return
     */
    void insert(PlateformChannel plateformChannel);

    List<PlateformChannel> selectSimple(Map<String, Object> params);

    /**
     * 通过渠道编码名称查找
     *
     * @param channelName
     * @return
     */
    PlateformChannel selectByChannelName(String channelName);

    /**
     * 通过账户查询渠道信息
     *
     * @param account
     * @return
     */
    PlateformChannel selectByAccount(String account);

    /**
     * 根据id查询链接
     *
     * @param id
     * @return
     */
    ChannelLinkVO findByChannelLink(Integer id);

    /**
     * 根据id获取短链链接
     *
     * @param id
     * @return
     */
    String getShortChannelLink(Integer id);

    /**
     * 根据参数查找列表
     *
     * @param params
     * @return
     */
    PageInfo<PlateformChannelListVo> findListParams(Map<String, Object> params);

    /**
     * 通过渠道编码查询
     *
     * @param channelCode
     * @return
     */
    PlateformChannel findByChannelCode(String channelCode);

    int saveChannelRule(PlateformChannelDTO plateformChannelDTO, Integer channelId);

    int updateChannelRule(PlateformChannelParamDto plateformChannelDTO);
}
