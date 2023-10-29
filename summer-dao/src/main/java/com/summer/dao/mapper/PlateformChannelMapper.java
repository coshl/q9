package com.summer.dao.mapper;

import com.summer.dao.entity.PlateformChannel;
import com.summer.pojo.dto.PlateformChannelParamDto;
import com.summer.pojo.vo.ChannelLinkVO;
import com.summer.pojo.vo.ChannelNameListVo;
import com.summer.pojo.vo.PlateformChannelListVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface PlateformChannelMapper {

    @CacheEvict(value="PlateformChannelMapper",allEntries=true)// 清空缓存
    int deleteByPrimaryKey(Integer id);

    @CacheEvict(value="PlateformChannelMapper",allEntries=true)// 清空缓存
    int insertSelective(PlateformChannelParamDto record);

    @Cacheable(value = "PlateformChannelMapper")
    PlateformChannel selectByPrimaryKey(Integer id);

    PlateformChannel selectByPrimaryKeyToUpdate(Integer id);

    @CacheEvict(value="PlateformChannelMapper",allEntries=true)// 清空缓存
    int updateByPrimaryKeySelective(PlateformChannelParamDto record);

    @CacheEvict(value="PlateformChannelMapper",allEntries=true)// 清空缓存
    int updateByPrimaryKey(PlateformChannel record);

    /**
     * 渠道列表
     *
     * @param params
     * @return
     */
    List<PlateformChannel> findParams(Map<String, Object> params);

    /**
     * 更新渠道状态
     *
     * @param id
     * @param status
     */
    @CacheEvict(value="PlateformChannelMapper",allEntries=true)// 清空缓存
    int updateByStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 添加渠道
     *
     * @param plateformChannel
     * @return
     */
    @CacheEvict(value="PlateformChannelMapper",allEntries=true)// 清空缓存
    void insert(PlateformChannel plateformChannel);

    @Cacheable(value = "PlateformChannelMapper")
    List<PlateformChannel> selectSimple(Map<String, Object> params);

    /**
     * 通过渠道编码名称查找
     *
     * @param ChannelName
     * @return
     */
    @Cacheable(value = "PlateformChannelMapper")
    PlateformChannel selectByChannelName(String ChannelName);

    /**
     * 通过账户查询渠道信息
     *
     * @param account
     * @return
     */
    @Cacheable(value = "PlateformChannelMapper")
    PlateformChannel selectByAccount(String account);

    /**
     * 根据id查询链接
     *
     * @param id
     * @return
     */
    @Cacheable(value = "PlateformChannelMapper")
    ChannelLinkVO findByChannelLink(Integer id);

    /**
     * 更新短链
     *
     */
    @CacheEvict(value="PlateformChannelMapper",allEntries=true)// 清空缓存
    int updateShortLink(ChannelLinkVO channelLinkVO);

    /**
     * 根据参数查找列表
     *
     * @param params
     * @return
     */
    @Cacheable(value = "PlateformChannelMapper")
    List<PlateformChannelListVo> findListParams(Map<String, Object> params);

    /**
     * 通过渠道编码查询
     *
     * @param channelCode
     * @return
     */
    @Cacheable(value = "PlateformChannelMapper")
    PlateformChannel findByChannelCode(String channelCode);

    List<ChannelNameListVo> findByChannelName(Map<String, Object> param);

    PlateformChannel findUnionLink();
}
