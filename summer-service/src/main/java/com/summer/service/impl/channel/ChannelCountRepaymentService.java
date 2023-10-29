package com.summer.service.impl.channel;

import com.alibaba.fastjson.JSONObject;
import com.summer.dao.entity.PlateformChannel;
import com.summer.dao.entity.dailystatics.DailyStatisticsChannelLend;
import com.summer.dao.mapper.OrderBorrowMapper;
import com.summer.dao.mapper.OrderRepaymentMapper;
import com.summer.dao.mapper.PlateformChannelMapper;
import com.summer.dao.mapper.dailystatistics.DailyStatisticsChannelLendDAO;
import com.summer.api.service.channel.IChannelCountRepaymentService;
import com.summer.util.Constant;
import com.summer.util.DateUtil;
import com.summer.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 渠道还款统计的实现类
 */
@Slf4j
@Service
public class ChannelCountRepaymentService implements IChannelCountRepaymentService {
    @Resource
    private PlateformChannelMapper plateformChannelMapper;
    @Resource
    private OrderRepaymentMapper orderRepaymentMapper;
    @Resource
    private OrderBorrowMapper orderBorrowMapper;
    @Resource
    private RedisUtil redisUtil;
    //逾期统计key前缀
    private final String CHANNEL_OVERDUE="channelOverdue_";

    /**
     * 每日还款统计
     */
    @Resource
    private DailyStatisticsChannelLendDAO dailyStatisticsChannelLendDAO;

    @Override
    public void channelRepaymentCount(Date time) {
        log.info("start invoke ChannelCountRepaymentService.channelRepaymentCount time={}", DateUtil.formatTimeYmdHms(time));
        try {
            //查询所有的渠道
            List<PlateformChannel> plateformChannels = plateformChannelMapper.findParams(null);
            //还款统计集合，用于批量修改和插入
            List<DailyStatisticsChannelLend> dailyStatisticsChannelLends = new ArrayList<>();
            if (null != plateformChannels && plateformChannels.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                Map<String, Object> paramMaps = paramMap(time, plateformChannels);
                /**统计待还用户*/
                List<Map<String, Object>> stayRepaymentCount = orderRepaymentMapper.findStayRepaymentCount(paramMaps);
                for (Map<String, Object> map : stayRepaymentCount) {
                    Integer channelId = (Integer) map.get("channelId");
                    DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
                    if (null != dailyStatisticsChannelLend) {
                        String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                        dailyStatisticsChannelLend.setChannelId(channelId);
                        //总待还量
                        dailyStatisticsChannelLend.setSumNumber(Integer.valueOf(sumNumber));
                        redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                    }
                }

                //统计老用户待还笔数
                List<Map<String, Object>> stayOldRepaymentCount = orderRepaymentMapper.findStayOldRepaymentCount(paramMaps);
                for (Map<String, Object> map : stayOldRepaymentCount) {
                    Integer channelId = (Integer) map.get("channelId");
                    DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
                    if (null != dailyStatisticsChannelLend) {
                        String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                        dailyStatisticsChannelLend.setChannelId(channelId);
                        //老用户待还笔数
                        dailyStatisticsChannelLend.setOldNumber(Integer.valueOf(sumNumber));
                        redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                    }
                }
                //统计新用户待还笔数
                List<Map<String, Object>> stayNewRepaymentCount = orderRepaymentMapper.findStayNewRepaymentCount(paramMaps);
                for (Map<String, Object> map : stayNewRepaymentCount) {
                    Integer channelId = (Integer) map.get("channelId");
                    DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
                    if (null != dailyStatisticsChannelLend) {
                        String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                        dailyStatisticsChannelLend.setChannelId(channelId);
                        //新用户待还笔数
                        dailyStatisticsChannelLend.setFirstBorrow(Integer.valueOf(sumNumber));
                        redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                    }
                }
                //设置已还款数量
                saveRepaymentCount(paramMaps, time);

                //把最终被赋值过的还款统计对象，添加进入还款统计的集合
                addDailyStatisticsChannelLend(plateformChannels, dailyStatisticsChannelLends);

                dailyStatisticsChannelLendDAO.insertBatchSelective(dailyStatisticsChannelLends);
            }
            for (int k = 0; k < plateformChannels.size(); k++) {
                redisUtil.del("channelReport_" + plateformChannels.get(k).getId());
            }
            log.info("ChannelCountRepaymentService.channelRepaymentCount---------------> 定时任务渠道还款统计，批量插入数据成功");
        } catch (Exception e) {
            log.error("ChannelCountRepaymentService.channelRepaymentCount---------------> 定时任务渠道还款统计异常 err={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 添加还款统计的对象
     *
     * @param plateformChannels
     * @param dailyStatisticsChannelLends
     */
    public void addDailyStatisticsChannelLend(List<PlateformChannel> plateformChannels, List<DailyStatisticsChannelLend> dailyStatisticsChannelLends) throws Exception {
        //获取最终的还款统计对象，批量插入数据库
        for (int k = 0; k < plateformChannels.size(); k++) {
            String dailyStatisticsChannelLendStr = redisUtil.get("channelReport_" + plateformChannels.get(k).getId());
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(dailyStatisticsChannelLendStr, DailyStatisticsChannelLend.class);
            dailyStatisticsChannelLends.add(dailyStatisticsChannelLend);
        }
    }


    /**
     * 统计出逾期的
     *
     * @param paramMaps
     */
    public void countOverdue(Map<String, Object> paramMaps) throws Exception {
        //总逾期量
        List<Map<String, Object>> repaymentCount = orderRepaymentMapper.findOverdueRepaymentCount(paramMaps);
        for (Map<String, Object> map : repaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                dailyStatisticsChannelLend.setChannelId(channelId);
                //逾期总数
                dailyStatisticsChannelLend.setOverdueNumber(Integer.valueOf(sumNumber));
                redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
            }
        }
        //老用户逾期量
        List<Map<String, Object>> oldOverdueRepaymentCount = orderRepaymentMapper.findOldOverdueRepaymentCount(paramMaps);
        for (Map<String, Object> map : oldOverdueRepaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                dailyStatisticsChannelLend.setChannelId(channelId);
                //老用户逾期量
                dailyStatisticsChannelLend.setOldOverdueNumber(Integer.valueOf(sumNumber));
                redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
            }
        }
        //新用户逾期量
        List<Map<String, Object>> newOverdueRepaymentCount = orderRepaymentMapper.findNewOverdueRepaymentCount(paramMaps);
        for (Map<String, Object> map : newOverdueRepaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                dailyStatisticsChannelLend.setChannelId(channelId);
                //设置新用户逾期量
                dailyStatisticsChannelLend.setFirstBorrowOverdue(Integer.valueOf(sumNumber));
                redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
            }
        }
    }

    /**
     * 已还款统计
     *
     * @param time
     */
    @Override
    public void repaymentCount(Date time) {
        log.info("start invoke ChannelCountRepaymentService.repaymentCount---------------> 定时任务渠道还款统计，已还款统计 time={}", DateUtil.formatTimeYmdHms(time));
        try {
            //查询所有的渠道
            List<PlateformChannel> plateformChannels = plateformChannelMapper.findParams(null);
            //还款统计集合，用于批量修改和插入
            List<DailyStatisticsChannelLend> dailyStatisticsChannelLends = new ArrayList<>();
            if (null != plateformChannels && plateformChannels.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                //统计时需要的参数
                Map<String, Object> paramMaps = paramMap(time, plateformChannels);
                //设置还款数量
                saveRepaymentCount(paramMaps, time);

                //把最终被赋值过的还款统计对象，添加进入还款统计的集合
                addDailyStatisticsChannelLend(plateformChannels, dailyStatisticsChannelLends);
                //修改已还数据
                for (DailyStatisticsChannelLend dailyStatisticsChannelLend : dailyStatisticsChannelLends) {
                    if (null != dailyStatisticsChannelLend && null != dailyStatisticsChannelLend.getId()) {
                        int state = dailyStatisticsChannelLendDAO.updateByPrimaryKeySelective(dailyStatisticsChannelLend);
                       // log.info("【【渠道还款统计】】---------state={},dailyStatisticsChannelLend={}",state,JSONObject.toJSONString(dailyStatisticsChannelLend));
                    }
                }
                for (int k = 0; k < plateformChannels.size(); k++) {
                    redisUtil.del("channelReport_" + plateformChannels.get(k).getId());
                }
                log.info("ChannelCountRepaymentService.repaymentCount---------------> 定时任务渠道还款统计，批量修改已还款数量数据成功");
            }
        } catch (Exception e) {
            log.error(" ChannelCountRepaymentService.repaymentCount---------------> 定时任务渠道还款统计，已还款统计 异常 errtime={}", DateUtil.formatTimeYmdHms(new Date()));
            e.printStackTrace();
        }
    }

    /**
     * 设置已还款数量
     *
     * @param paramMaps
     */
    public void saveRepaymentCount(Map<String, Object> paramMaps, Date time) throws Exception {
        //总已还款数量
        List<Map<String, Object>> repaymentCount = orderRepaymentMapper.findRepaymentCount(paramMaps);
        for (Map<String, Object> map : repaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {

                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置还款数量
                    dailyStatisticsChannelLend.setRepaymentNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置还款数量
                    dailyStatisticsChannelLend.setRepaymentNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
        //新用户已还数量
        List<Map<String, Object>> newRepaymentCount = orderRepaymentMapper.findNewRepaymentCount(paramMaps);
        for (Map<String, Object> map : newRepaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置新用户还款数量
                    dailyStatisticsChannelLend.setNewRepaymentNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置新用户还款数量
                    dailyStatisticsChannelLend.setNewRepaymentNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }

        //老用户已还数量
        List<Map<String, Object>> oldRepaymentCount = orderRepaymentMapper.findOldRepaymentCount(paramMaps);
        for (Map<String, Object> map : oldRepaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置老用户还款数量
                    dailyStatisticsChannelLend.setOldRepaymentNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置老用户还款数量
                    dailyStatisticsChannelLend.setOldRepaymentNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }

        //总续期数量
        List<Map<String, Object>> renewalCount = orderRepaymentMapper.findRenewalCount(paramMaps);
        for (Map<String, Object> map : renewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置总的续期数量
                    dailyStatisticsChannelLend.setRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置总的续期数量
                    dailyStatisticsChannelLend.setRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
        //新用户续期数量
        List<Map<String, Object>> newRenewalCount = orderRepaymentMapper.findNewRenewalCount(paramMaps);
        for (Map<String, Object> map : newRenewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置新用户续期数量
                    dailyStatisticsChannelLend.setNewRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置新用户续期数量
                    dailyStatisticsChannelLend.setNewRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
        //老用户续期数量
        List<Map<String, Object>> oldRenewalCount = orderRepaymentMapper.findOldRenewalCount(paramMaps);
        for (Map<String, Object> map : oldRenewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置老用户续期数量
                    dailyStatisticsChannelLend.setOldRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置老用户续期数量 (在生成该表的时使用)
                    dailyStatisticsChannelLend.setOldRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }

        //今日续期数量
        List<Map<String, Object>> todayRenewalCount = orderRepaymentMapper.findTodayRenewalCount(paramMaps);
        for (Map<String, Object> map : todayRenewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置今日续期数量
                    dailyStatisticsChannelLend.setTodayRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置今日续期数量 (在生成该表的时使用)
                    dailyStatisticsChannelLend.setTodayRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
        //提前续期数量
        List<Map<String, Object>> aheadRenewalCount = orderRepaymentMapper.findAheadRenewalCount(paramMaps);
        for (Map<String, Object> map : aheadRenewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置提前续期数量
                    dailyStatisticsChannelLend.setAheadRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置提前续期数量 (在生成该表的时使用)
                    dailyStatisticsChannelLend.setAheadRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }

        //新用户提前续期数量
        List<Map<String, Object>> newAheadRenewalCount = orderRepaymentMapper.findNewAheadRenewalCount(paramMaps);
        for (Map<String, Object> map : newAheadRenewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置提前续期数量
                    dailyStatisticsChannelLend.setNewAheadRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置提前续期数量 (在生成该表的时使用)
                    dailyStatisticsChannelLend.setNewAheadRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }

        //老用户提前续期数量
        List<Map<String, Object>> oldAheadRenewalCount = orderRepaymentMapper.findOldAheadRenewalCount(paramMaps);
        for (Map<String, Object> map : oldAheadRenewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置提前续期数量
                    dailyStatisticsChannelLend.setOldAheadRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置提前续期数量 (在生成该表的时使用)
                    dailyStatisticsChannelLend.setOldAheadRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
        //新用户当日续期数量
        List<Map<String, Object>> newTodayRenewalCount = orderRepaymentMapper.findNewTodayRenewalCount(paramMaps);
        for (Map<String, Object> map : newTodayRenewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置今日续期数量
                    dailyStatisticsChannelLend.setNewTodayRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置今日续期数量 (在生成该表的时使用)
                    dailyStatisticsChannelLend.setNewTodayRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
        //老用户当日续期数量
        List<Map<String, Object>> oldTodayRenewalCount = orderRepaymentMapper.findOldTodayRenewalCount(paramMaps);
        for (Map<String, Object> map : oldTodayRenewalCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelReport_" + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(time));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置今日续期数量
                    dailyStatisticsChannelLend.setOldTodayRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                } else {
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //设置今日续期数量 (在生成该表的时使用)
                    dailyStatisticsChannelLend.setOldTodayRenewalNumber(Integer.valueOf(sumNumber));
                    redisUtil.set("channelReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
    }

    /**
     * 获取统计的参数
     *
     * @param time
     * @param plateformChannels
     * @return
     */
    public Map<String, Object> paramMap(Date time, List<PlateformChannel> plateformChannels) throws Exception {
        //统计时需要的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nowTime", time);
        Integer[] channelIds = new Integer[plateformChannels.size()];
        for (int k = 0; k < plateformChannels.size(); k++) {
            //赋值给渠道id数组
            channelIds[k] = plateformChannels.get(k).getId();
            DailyStatisticsChannelLend dailyStatisticsChannelLend = new DailyStatisticsChannelLend();
            //设置渠道id
            dailyStatisticsChannelLend.setChannelId(channelIds[k]);
            dailyStatisticsChannelLend.setRepaymentTime(time);
            //把只有渠道id的还款统计实体类对象存入Redis
            redisUtil.set("channelReport_" + plateformChannels.get(k).getId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
        }
        //渠道id集合
        paramMap.put("channelIds", channelIds);
        return paramMap;
    }

    /**
     * 逾期统计
     *
     * @param time
     */
    @Override
    public void overdueCount(Date time) {
        log.info("start invoke ChannelCountRepaymentService.overdueCount---------------> 定时任务逾期统计 time={}", DateUtil.formatTimeYmdHms(time));
        try {
            //查询所有的渠道
            List<PlateformChannel> plateformChannels = plateformChannelMapper.findParams(null);
            //还款统计集合，用于批量修改和插入
            List<DailyStatisticsChannelLend> dailyStatisticsChannelLends = new ArrayList<>();
            if (null != plateformChannels && plateformChannels.size() > Constant.LIST_SIZE_LENTH_ZORE) {
                //统计时需要的参数
                Map<String, Object> paramMaps = paramOverMap(time, plateformChannels);
                //设置逾期数量
                saveOverdueCount(paramMaps, time);
                //把最终被赋值过的统计对象，添加进入还款统计的集合
                addDailyOverdue(plateformChannels, dailyStatisticsChannelLends);
                //修改已还数据
                for (DailyStatisticsChannelLend dailyStatisticsChannelLend : dailyStatisticsChannelLends) {
                    if (null != dailyStatisticsChannelLend && null != dailyStatisticsChannelLend.getId()) {
                        dailyStatisticsChannelLendDAO.updateByPrimaryKeySelective(dailyStatisticsChannelLend);
                    }
                }
                for (int k = 0; k < plateformChannels.size(); k++) {
                    redisUtil.del(CHANNEL_OVERDUE + plateformChannels.get(k).getId());
                }
                log.info("ChannelCountRepaymentService.overdueCount---------------> 定时任务渠道逾期统计，批量修改逾期数据成功");
            }
        } catch (Exception e) {
            log.error(" ChannelCountRepaymentService.overdueCount---------------> 定时任务渠道逾期统计，逾期 异常 errtime={}，err={}", DateUtil.formatTimeYmdHms(new Date()), e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 设置逾期数量
     *
     * @param paramMaps
     */
    public void saveOverdueCount(Map<String, Object> paramMaps, Date time) throws Exception {
        List<Map<String, Object>> repaymentCount = orderRepaymentMapper.findOverdueRepaymentCount(paramMaps);
        //今天统计的是昨天应还逾期的 所以需要查询昨天的
        Date date = DateUtil.dateSubtraction(time, -1);
        for (Map<String, Object> map : repaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get(CHANNEL_OVERDUE + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(date));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setRepaymentTime(null);
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //当日逾期数量
                    dailyStatisticsChannelLend.setOverdueNumber(Integer.valueOf(sumNumber));
                    dailyStatisticsChannelLend.setTotalOverdueCount(Integer.valueOf(sumNumber));
                    redisUtil.set(CHANNEL_OVERDUE + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
        //新用户逾期量
        List<Map<String, Object>> newOverdueRepaymentCount = orderRepaymentMapper.findNewOverdueRepaymentCount(paramMaps);
        for (Map<String, Object> map : newOverdueRepaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get(CHANNEL_OVERDUE + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(date));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setRepaymentTime(null);
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //新用户当日逾期数量
                    dailyStatisticsChannelLend.setFirstBorrowOverdue(Integer.valueOf(sumNumber));
                    dailyStatisticsChannelLend.setNewTotalOverdueCount(Integer.valueOf(sumNumber));
                    redisUtil.set(CHANNEL_OVERDUE + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }
        //老用户逾期量
        List<Map<String, Object>> oldOverdueRepaymentCount = orderRepaymentMapper.findOldOverdueRepaymentCount(paramMaps);
        for (Map<String, Object> map : oldOverdueRepaymentCount) {
            Integer channelId = (Integer) map.get("channelId");
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get(CHANNEL_OVERDUE + channelId), DailyStatisticsChannelLend.class);
            if (null != dailyStatisticsChannelLend) {
                String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(date));
                paramMap.put("channelId", channelId);
                DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                if (null != newdailyStatisticsChannelLend) {
                    dailyStatisticsChannelLend.setRepaymentTime(null);
                    dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                    dailyStatisticsChannelLend.setChannelId(channelId);
                    //老用户当日逾期数量
                    dailyStatisticsChannelLend.setOldOverdueNumber(Integer.valueOf(sumNumber));
                    dailyStatisticsChannelLend.setOldTotalOverdueCount(Integer.valueOf(sumNumber));
                    redisUtil.set(CHANNEL_OVERDUE + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                }
            }
        }

    }

    /**
     * 逾期总数统计
     *
     * @param time
     */
    @Override
    public void overdueTotal(Date time, Integer pasDay) {
        log.info("start invoke ChannelCountRepaymentService.overdueTotal---------------> 定时任务逾期总数统计 time={}", DateUtil.formatTimeYmdHms(time));
        try {
            //还款统计集合，用于批量修改和插入
            List<DailyStatisticsChannelLend> dailyStatisticsChannelLends = new ArrayList<>();
            int day = Constant.PASS_DAY;
            int daylen=0;
            if (null != pasDay) {
                day = pasDay;
                if (day==0){
                    daylen=1;
                }
            }

            //统计过去5天的逾期数量
            for (int i = day; i < daylen; i++) {
                //统计时需要的参数
                Map<String, Object> paramMap = new HashMap<>();
                Date date = DateUtil.dateSubtraction(time, i);
                //根据过去还款时间查询
                paramMap.put("repaymentTime", DateUtil.formatTimeYmd(date));
              //  log.info("【逾期逾期总数统计】------------ repaymentTime={}，day={},i={}",DateUtil.formatTimeYmd(date),day,i);
                List<DailyStatisticsChannelLend> dailyStatisticsChannelLends1 = dailyStatisticsChannelLendDAO.selectAllByParams(paramMap);
                //存入必要的参数
                Map<String, Object> paramMaps = paramOverdueMap(time, dailyStatisticsChannelLends1);
                //设置逾期数量
                overdueTotalCount(paramMaps, date);
                //把最终被赋值过的统计对象，添加进入还款统计的集合
                addOverdueChannelLend(dailyStatisticsChannelLends1, dailyStatisticsChannelLends);
                //修改逾期数据
                for (DailyStatisticsChannelLend dailyStatisticsChannelLend : dailyStatisticsChannelLends) {
                    if (null != dailyStatisticsChannelLend && null != dailyStatisticsChannelLend.getId()) {
                        int status = dailyStatisticsChannelLendDAO.updateByPrimaryKeySelective(dailyStatisticsChannelLend);
                       // log.info("【渠道【逾期总数统计】】--------------status={},dailyStatisticsChannelLend={}",status,JSONObject.toJSONString(dailyStatisticsChannelLend));
                    }
                }
                for (int k = 0; k < dailyStatisticsChannelLends1.size(); k++) {
                    redisUtil.del("channelOverdueReport_" + dailyStatisticsChannelLends1.get(k).getChannelId());
                }
            }
            log.info("ChannelCountRepaymentService.overdueTotal---------------> 定时任务渠道总逾期统计，批量修改逾期数据成功");
        } catch (Exception e) {
            log.error(" ChannelCountRepaymentService.overdueTotal---------------> 定时任务渠道总逾期统计，逾期 异常 errtime={}，err={}", DateUtil.formatTimeYmdHms(new Date()), e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 逾期总数统计
     *
     * @param paramMaps
     * @param date
     */
    public void overdueTotalCount(Map<String, Object> paramMaps, Date date) {
        log.info("【逾期总数统计overdueTotalCount】-------，date={}",DateUtil.formatTimeYmd(date));

        Integer[] channelIds = (Integer[]) paramMaps.get("channelIds");
        if (channelIds.length != 0) {
            paramMaps.put("repaymentTime", DateUtil.formatTimeYmd(date));
            List<Map<String, Object>> repaymentCount = orderRepaymentMapper.findTotalOverdueCount(paramMaps);
            //总逾期率
            for (Map<String, Object> map : repaymentCount) {
                Integer channelId = (Integer) map.get("channelId");
                DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelOverdueReport_" + channelId), DailyStatisticsChannelLend.class);
                if (null != dailyStatisticsChannelLend) {
                    String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("repaymentTime", DateUtil.formatTimeYmd(date));
                    paramMap.put("channelId", channelId);
                    DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                    if (null != newdailyStatisticsChannelLend) {
                        dailyStatisticsChannelLend.setRepaymentTime(null);
                        dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                        dailyStatisticsChannelLend.setChannelId(channelId);
                        //逾期总数
                        dailyStatisticsChannelLend.setTotalOverdueCount(Integer.valueOf(sumNumber));
                        redisUtil.set("channelOverdueReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                    }
                }
            }
            //新用户逾期量
            List<Map<String, Object>> newOverdueRepaymentCount = orderRepaymentMapper.findNewTotalOverdueCount(paramMaps);
            for (Map<String, Object> map : newOverdueRepaymentCount) {
                Integer channelId = (Integer) map.get("channelId");
                DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelOverdueReport_" + channelId), DailyStatisticsChannelLend.class);
                if (null != dailyStatisticsChannelLend) {
                    String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("repaymentTime", DateUtil.formatTimeYmd(date));
                    paramMap.put("channelId", channelId);
                    DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                    if (null != newdailyStatisticsChannelLend) {
                        dailyStatisticsChannelLend.setRepaymentTime(null);
                        dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                        dailyStatisticsChannelLend.setChannelId(channelId);
                        //新用户逾期总数量
                        dailyStatisticsChannelLend.setNewTotalOverdueCount(Integer.valueOf(sumNumber));
                        redisUtil.set("channelOverdueReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                    }
                }
            }
            //老用户逾期量
            List<Map<String, Object>> oldOverdueRepaymentCount = orderRepaymentMapper.findOldTotalOverdueCount(paramMaps);
            for (Map<String, Object> map : oldOverdueRepaymentCount) {
                Integer channelId = (Integer) map.get("channelId");
                DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(redisUtil.get("channelOverdueReport_" + channelId), DailyStatisticsChannelLend.class);
                if (null != dailyStatisticsChannelLend) {
                    String sumNumber = null == map.get("count") ? "0" : map.get("count").toString();
                    Map<String, Object> paramMap = new HashMap<>();
                    paramMap.put("repaymentTime", DateUtil.formatTimeYmd(date));
                    paramMap.put("channelId", channelId);
                    DailyStatisticsChannelLend newdailyStatisticsChannelLend = dailyStatisticsChannelLendDAO.selectByParams(paramMap);
                    if (null != newdailyStatisticsChannelLend) {
                        dailyStatisticsChannelLend.setRepaymentTime(null);
                        dailyStatisticsChannelLend.setId(newdailyStatisticsChannelLend.getId());
                        dailyStatisticsChannelLend.setChannelId(channelId);
                        //老用户总逾期数量
                        dailyStatisticsChannelLend.setOldTotalOverdueCount(Integer.valueOf(sumNumber));
                        redisUtil.set("channelOverdueReport_" + dailyStatisticsChannelLend.getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
                    }
                }
            }
        }
    }

    public Map<String, Object> paramOverdueMap(Date time, List<DailyStatisticsChannelLend> plateformChannels) throws Exception {
        //统计时需要的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nowTime", time);
        Integer[] channelIds = new Integer[plateformChannels.size()];
        for (int k = 0; k < plateformChannels.size(); k++) {
            //赋值给渠道id数组
            channelIds[k] = plateformChannels.get(k).getChannelId();
            DailyStatisticsChannelLend dailyStatisticsChannelLend = new DailyStatisticsChannelLend();
            //设置查出来的id
            dailyStatisticsChannelLend.setId(plateformChannels.get(k).getId());
            //设置渠道id
            dailyStatisticsChannelLend.setChannelId(channelIds[k]);
            dailyStatisticsChannelLend.setTotalOverdueCount(0);
            dailyStatisticsChannelLend.setNewTotalOverdueCount(0);
            dailyStatisticsChannelLend.setOldTotalOverdueCount(0);
            //把只有渠道id的还款统计实体类对象存入Redis
            redisUtil.set("channelOverdueReport_" + plateformChannels.get(k).getChannelId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
        }
        //渠道id集合
        paramMap.put("channelIds", channelIds);
        return paramMap;
    }

    /**
     * 添加被赋值过的逾期的对象
     *
     * @param dailyStatisticsChannelLends
     * @throws Exception
     */
    public void addOverdueChannelLend(List<DailyStatisticsChannelLend> dailyStatisticsChannelLends1, List<DailyStatisticsChannelLend> dailyStatisticsChannelLends) throws Exception {
        //获取最终的还款统计对象，批量插入数据库
        for (int k = 0; k < dailyStatisticsChannelLends1.size(); k++) {
            String dailyStatisticsChannelLendStr = redisUtil.get("channelOverdueReport_" + dailyStatisticsChannelLends1.get(k).getChannelId());
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(dailyStatisticsChannelLendStr, DailyStatisticsChannelLend.class);
            dailyStatisticsChannelLends.add(dailyStatisticsChannelLend);
        }
    }

    /**
     * 获取统计的参数
     *
     * @param time
     * @param plateformChannels
     * @return
     */
    public Map<String, Object> paramOverMap(Date time, List<PlateformChannel> plateformChannels) throws Exception {
        //统计时需要的参数
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("nowTime", time);
        Integer[] channelIds = new Integer[plateformChannels.size()];
        for (int k = 0; k < plateformChannels.size(); k++) {
            //赋值给渠道id数组
            channelIds[k] = plateformChannels.get(k).getId();
            DailyStatisticsChannelLend dailyStatisticsChannelLend = new DailyStatisticsChannelLend();
            //设置渠道id
            dailyStatisticsChannelLend.setChannelId(channelIds[k]);
            dailyStatisticsChannelLend.setRepaymentTime(time);
            //把只有渠道id的还款统计实体类对象存入Redis
            redisUtil.set(CHANNEL_OVERDUE + plateformChannels.get(k).getId(), JSONObject.toJSONString(dailyStatisticsChannelLend));
        }
        //渠道id集合
        paramMap.put("channelIds", channelIds);
        return paramMap;
    }

    /**
     * 添加还款统计的对象
     *
     * @param plateformChannels
     * @param dailyStatisticsChannelLends
     */
    public void addDailyOverdue(List<PlateformChannel> plateformChannels, List<DailyStatisticsChannelLend> dailyStatisticsChannelLends) throws Exception {
        //获取最终的还款统计对象，批量插入数据库
        for (int k = 0; k < plateformChannels.size(); k++) {
            String dailyStatisticsChannelLendStr = redisUtil.get(CHANNEL_OVERDUE + plateformChannels.get(k).getId());
            DailyStatisticsChannelLend dailyStatisticsChannelLend = JSONObject.parseObject(dailyStatisticsChannelLendStr, DailyStatisticsChannelLend.class);
            dailyStatisticsChannelLends.add(dailyStatisticsChannelLend);
        }
    }
}
