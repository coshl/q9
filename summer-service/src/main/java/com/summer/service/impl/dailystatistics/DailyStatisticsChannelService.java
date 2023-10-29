package com.summer.service.impl.dailystatistics;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.summer.dao.mapper.PlateformChannelMapper;
import com.summer.dao.mapper.PlateformChannelReportDAO;
import com.summer.dao.mapper.dailystatistics.DailyStatisticsChannelLendDAO;
import com.summer.dao.mapper.dailystatistics.DailyStatisticsChannelProductDAO;
import com.summer.pojo.vo.*;
import com.summer.api.service.dailystatistics.IDailyStatisticsChannelService;
import com.summer.util.Constant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 渠道统计
 */
@Service
public class DailyStatisticsChannelService implements IDailyStatisticsChannelService {
    @Resource
    private DailyStatisticsChannelProductDAO dailyStatisticsChannel;

    @Resource
    private DailyStatisticsChannelLendDAO channelLendDAO;

    @Resource
    private PlateformChannelReportDAO channelReportDAO;
    @Resource
    private PlateformChannelMapper plateformChannelMapper;

    @Override
    public Map<String, Object> findParams(Map<String, Object> params) {
        List<DailyStatisticsChannelProductVo> list = dailyStatisticsChannel.findParams(params);
        Integer registerSum = 0;
        Integer aplicationSum = 0;
        Integer loanNumberSum = 0;
        Integer registerNumberChannel = 0;
        Map<String, Object> paraMap = new HashMap<>();
        Object plateformUserId = params.get("plateformUserId");
        if (null != plateformUserId) {
            paraMap.put("plateformUserId", plateformUserId);
        }
        List<DailyStatisticsChannelProductVo> list2 = dailyStatisticsChannel.findParams(paraMap);
        for (DailyStatisticsChannelProductVo channelProductVo : list2) {
            registerSum += channelProductVo.getRegisterNumber();
            aplicationSum += channelProductVo.getPplicationNumber();
            loanNumberSum += channelProductVo.getLoanNumberChannel();
            registerNumberChannel += channelProductVo.getRegisterNumberChannel();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("registerSum", registerSum);
        map.put("aplicationSum", aplicationSum);
        map.put("loanNumberSum", loanNumberSum);
        map.put("registerNumberChannel", registerNumberChannel);
        map.put("list", new PageInfo<>(list));

        return map;
    }

    @Override
    public Map<String, Object> findChannelLend(Map<String, Object> params) {
        List<DailyStatisticsChannelLendVo> list = channelLendDAO.findParams(params);
        //总待还笔数
        int repaymentSum = 0;
        //新用户待还笔数
        int firstBorrowSum = 0;
        //老用户待还笔数
        int oldNumberSum = 0;
        //用户逾期总笔数
        int overdueNumberSum = 0;
        //新用户逾期笔数
        int firstBorrowOverdueSum = 0;
        //老用户逾期笔数
        int oldOverdueNumberSum = 0;
        /**根据条件统计*/
        Map<String, Object> paMap = new HashMap<>();
        paMap.put("channelName", params.get("channelName"));
        paMap.put("startTime", params.get("startTime"));
        paMap.put("endTime", params.get("endTime"));
        paMap.put("plateformUserId", params.get("plateformUserId"));
        List<DailyStatisticsChannelLendVo> listTotal = channelLendDAO.findParams(paMap);
        if (null != listTotal && listTotal.size() > 0) {
            for (DailyStatisticsChannelLendVo dailyStatisticsChannelLendVo : listTotal) {
                if (null != dailyStatisticsChannelLendVo.getSumNumber()) {
                    repaymentSum += dailyStatisticsChannelLendVo.getSumNumber();
                }
                if (null != dailyStatisticsChannelLendVo.getFirstBorrow()) {
                    firstBorrowSum += dailyStatisticsChannelLendVo.getFirstBorrow();
                }
                if (null != dailyStatisticsChannelLendVo.getOldNumber()) {
                    oldNumberSum += dailyStatisticsChannelLendVo.getOldNumber();
                }

                if (null != dailyStatisticsChannelLendVo.getOverdueNumberSum()) {
                    overdueNumberSum += dailyStatisticsChannelLendVo.getOverdueNumberSum();
                }
                if (null != dailyStatisticsChannelLendVo.getFirstBorrowOverdueSum()) {
                    firstBorrowOverdueSum += dailyStatisticsChannelLendVo.getFirstBorrowOverdueSum();
                }
                if (null != dailyStatisticsChannelLendVo.getOldOverdueNumberSum()) {
                    oldOverdueNumberSum += dailyStatisticsChannelLendVo.getOldOverdueNumberSum();
                }
            }
        } else {

            //待还笔数
            ChannelLendTotalVO stayRepaymentTotal = channelLendDAO.findStayTotal();
            if (null != stayRepaymentTotal) {
                repaymentSum = stayRepaymentTotal.getRepaymentSum();
                firstBorrowSum = stayRepaymentTotal.getFirstBorrowSum();
                oldNumberSum = stayRepaymentTotal.getOldNumberSum();
                //用户逾期总笔数
                overdueNumberSum = channelLendDAO.findOverdueTotal();
                //新用户逾期笔数
                firstBorrowOverdueSum = channelLendDAO.findNewOverdueTotal();
                //老用户逾期笔数
                oldOverdueNumberSum = channelLendDAO.findOldOverdueTotal();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        //总逾期率（新、老）
        String overdueSumRate = "0";
        //逾期率（新用户）
        String firstOverdueSumRate = "0";
        //逾期率（老用户）
        String oldOverdueSumRate = "0";

        if (0 != repaymentSum) {
            //总逾期
            float overrate = (float) overdueNumberSum * 100 / repaymentSum;
            overdueSumRate = decimalFormat.format(overrate);
        }
        if (0 != firstBorrowSum) {
            //新用户逾期
            float firstOverdueRate = (float) firstBorrowOverdueSum * 100 / firstBorrowSum;
            firstOverdueSumRate = decimalFormat.format(firstOverdueRate);
        }
        if (0 != oldNumberSum) {
            //老用户逾期
            float oldOverdueRate = (float) oldOverdueNumberSum * 100 / oldNumberSum;
            oldOverdueSumRate = decimalFormat.format(oldOverdueRate);
        }
       /* if (null!=stayRepaymentTotal){
            repaymentSum = stayRepaymentTotal.getRepaymentSum();
            firstBorrowSum = stayRepaymentTotal.getFirstBorrowSum();
            oldNumberSum = stayRepaymentTotal.getOldNumberSum();
        }*/

        Map<String, Object> map = new HashMap<>();
        //数总待还笔
        map.put("repaymentSum", repaymentSum);
        //新用户待还笔数
        map.put("firstBorrowSum", firstBorrowSum);
        //老用户待还笔数
        map.put("oldNumberSum", oldNumberSum);
        //逾期总数
        map.put("overdueNumberSum", overdueNumberSum);
        //新用户逾期
        map.put("firstBorrowOverdueSum", firstBorrowOverdueSum);
        //老用户逾期笔数
        map.put("oldOverdueNumberSum", oldOverdueNumberSum);
        //总逾期率
        map.put("overdueSumRate", overdueSumRate);
        //新用户逾期率
        map.put("firstOverdueSumRate", firstOverdueSumRate);
        //老用户逾期率
        map.put("oldOverdueSumRate", oldOverdueSumRate);
        map.put("list", new PageInfo<>(list));
        return map;
    }

    @Override
    public Map<String, Object> findMyChannelStatistics(Map<String, Object> params) {
        List<PlateformChannelReportVo> list = channelReportDAO.findParams(params);
        Integer registerCountSum = 0;
        Integer borrowApplyCountSum = 0;
        Integer loanCountSum = 0;
        for (PlateformChannelReportVo plateformChannelReportVo : list) {
            registerCountSum += plateformChannelReportVo.getRegisterCount();
            borrowApplyCountSum += plateformChannelReportVo.getBorrowApplyCount();
            loanCountSum += plateformChannelReportVo.getLoanCount();
        }
        //平均申请、放款率
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Map<String, Object> map = new HashMap<>();
        String averageApplicationRate = "0";
        String averageLoanRate = "0";
        if (registerCountSum != 0) {

            float borrowRate = (float) borrowApplyCountSum * 100 / registerCountSum;
            averageApplicationRate = decimalFormat.format(borrowRate);
            float loanRate = (float) loanCountSum * 100 / registerCountSum;
            averageLoanRate = decimalFormat.format(loanRate);

        }
        map.put("averageApplicationRate", averageApplicationRate);
        map.put("averageLoanRate", averageLoanRate);
        map.put("list", new PageInfo<>(list));
        return map;
    }

    @Override
    public PageInfo<PlateformChannelSideVo> findChannelStatistics(Map<String, Object> params) {
        PageHelper.startPage(params);
        List<PlateformChannelSideVo> list = channelReportDAO.findChannelSide(params);
        //计算过的集合
        ArrayList<PlateformChannelSideVo> plateformChannelSideList = new ArrayList<>();
        if (null != list && list.size() > Constant.LIST_SIZE_LENTH_ZORE) {
            for (PlateformChannelSideVo plateformChannelSideVo : list) {
                PlateformChannelSideVo newplateformChannelSideVo = new PlateformChannelSideVo();
                //设置id
                newplateformChannelSideVo.setId(plateformChannelSideVo.getId());
                //设置统计日期
                newplateformChannelSideVo.setReportTime(plateformChannelSideVo.getReportTime());
                //设置渠道名称
                newplateformChannelSideVo.setChannelName(plateformChannelSideVo.getChannelName());

                //设置扣量系数
                BigDecimal dedutionCoefficient = plateformChannelSideVo.getDedutionCoefficient();
                newplateformChannelSideVo.setDedutionCoefficient(dedutionCoefficient);
                //真实注册量
                int registerCount = plateformChannelSideVo.getDeductionRegisterCount();
                //真实申请量
                int borrowApplyCount = plateformChannelSideVo.getDeductionBorrowApplyCount();
                //真实成功量
                int borrowSucCount = plateformChannelSideVo.getDeductionBorrowSucCount();
                //如果小于6，就不用做扣量计算
                if (registerCount <= Constant.CHANNEL_COUNT) {
                    //获取真实注册量
                    newplateformChannelSideVo.setDeductionRegisterCount(registerCount);
                    //获取真实申请量
                    newplateformChannelSideVo.setDeductionBorrowApplyCount(borrowApplyCount);
                    //获取真实贷款成功量
                    newplateformChannelSideVo.setDeductionBorrowSucCount(borrowSucCount);
                    //获取真实通过率
                    newplateformChannelSideVo.setPassRate(plateformChannelSideVo.getPassRate());
                    newplateformChannelSideVo.setBlackTotal(plateformChannelSideVo.getBlackTotal());
                    BigDecimal blackTotal = new BigDecimal(plateformChannelSideVo.getBlackTotal());
                    BigDecimal register = new BigDecimal(registerCount);
                    if (registerCount != 0) {
                        double num = blackTotal.divide(register, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
                        newplateformChannelSideVo.setBlackTotalRate(new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    } else {
                        newplateformChannelSideVo.setBlackTotalRate(0.0);
                    }

                } else {
                    //计算扣量后的注册量
                    int numberRegistChannel = getNumberChannel(registerCount, dedutionCoefficient);
                    newplateformChannelSideVo.setDeductionRegisterCount(numberRegistChannel);

                    //判断真实申请量是否达到了6，也要开始扣量计算
                    if (borrowApplyCount > Constant.CHANNEL_COUNT) {
                        int numApplyChannel = getNumberChannel(borrowApplyCount, dedutionCoefficient);
                        newplateformChannelSideVo.setDeductionBorrowApplyCount(numApplyChannel);
                    } else {
                        //否则不用扣量
                        newplateformChannelSideVo.setDeductionBorrowApplyCount(borrowApplyCount);
                    }
                    //通过率为零
                    BigDecimal passRateZero = new BigDecimal(0);
                    BigDecimal change = new BigDecimal(100);
                    //判断申请成功量
                    if (borrowSucCount > Constant.CHANNEL_COUNT) {
                        //扣量过后的成功量
                        int borrowSucChannel = getNumberChannel(borrowSucCount, dedutionCoefficient);
                        newplateformChannelSideVo.setDeductionBorrowSucCount(borrowSucChannel);
                        //扣量过的成功数
                        BigDecimal channelSucc = new BigDecimal(borrowSucChannel);

                        //计算通过率 （成功人数大于6，就需要拿扣量过的 成功人数 除 扣量过后的注册量）
                        if (numberRegistChannel == 0) {
                            newplateformChannelSideVo.setPassRate(passRateZero);
                        } else {
                            //扣量过后的注册数
                            BigDecimal channelRegister = new BigDecimal(numberRegistChannel);
                            BigDecimal passRate = channelSucc.divide(channelRegister, 4, BigDecimal.ROUND_HALF_UP);
                            newplateformChannelSideVo.setPassRate(passRate.multiply(change));
                        }
                    } else {
                        //如果成功人数为0，直接通过率为0
                        if (borrowSucCount == 0) {
                            //放款成功人数
                            newplateformChannelSideVo.setDeductionBorrowSucCount(borrowSucCount);
                            newplateformChannelSideVo.setPassRate(passRateZero);
                        } else {
                            //就不用扣量计算
                            newplateformChannelSideVo.setDeductionBorrowSucCount(borrowSucCount);
                            //真实成功人数
                            BigDecimal channelSucce = new BigDecimal(borrowSucCount);
                            //如果扣量系数100，返回0说明全部被扣量
                            if (numberRegistChannel == 0) {
                                //通过率为0
                                newplateformChannelSideVo.setPassRate(passRateZero);
                            } else {
                                //扣量后的注册人数
                                BigDecimal channelRegisters = new BigDecimal(numberRegistChannel);
                                //计算通过率，成功人数小于6时，就直接除扣量后的注册数
                                BigDecimal passRate = channelSucce.divide(channelRegisters, 4, BigDecimal.ROUND_HALF_UP);
                                newplateformChannelSideVo.setPassRate(passRate.multiply(change));
                            }
                        }
                    }
                    int blackTotalCount = plateformChannelSideVo.getBlackTotal();
                    if (plateformChannelSideVo.getBlackTotal() > Constant.CHANNEL_COUNT) {
                        //命中黑名单率
                        blackTotalCount = getNumberChannel(plateformChannelSideVo.getBlackTotal(), dedutionCoefficient);
                    }

                    newplateformChannelSideVo.setBlackTotal(blackTotalCount);
                    BigDecimal blackTotal = new BigDecimal(blackTotalCount);
                    BigDecimal register = new BigDecimal(numberRegistChannel);
                    if (registerCount != 0) {
                        double num = blackTotal.divide(register, 4, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
                        newplateformChannelSideVo.setBlackTotalRate(new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                    } else {
                        newplateformChannelSideVo.setBlackTotalRate(0.0);
                    }
                }
                plateformChannelSideList.add(newplateformChannelSideVo);
            }
        }
        PageInfo<PlateformChannelSideVo> pageInfo = new PageInfo<>(list);
        pageInfo.setList(plateformChannelSideList);
        return pageInfo;
//        return new PageInfo<>(plateformChannelSideList);
    }

    /**
     * 获取扣量后的数量 ：渠道方
     *
     * @param realCount          真实数量
     * @param decreasePercentage 扣量系数
     * @return
     */
    public int getNumberChannel(int realCount, BigDecimal decreasePercentage) {
        //如果扣量系数为0，返回真实数据，表示不扣量，返回真实数据
        if (decreasePercentage.intValue() == 0) {
            return realCount;
        }
        //如果扣量系数为100，表示全部扣，返回0
        if (decreasePercentage.intValue() == 100) {
            return 0;
        }
        //扣量后的注册量：（真实注册量-5）* 扣量比例（:decreasePercentage）+5
        BigDecimal change = new BigDecimal(100);
        //真实注册量-5
        BigDecimal registerNumber = new BigDecimal((realCount - Constant.DECREASE_PERCENTAGE_FIVE));
        //数量 * （100-扣量比例） 除100（转换比例：因为比例在数据中按整数存的）
        BigDecimal registerNumberChannel = registerNumber.multiply(change.subtract(decreasePercentage)).divide(change, 2, BigDecimal.ROUND_HALF_UP);
        return registerNumberChannel.intValue() + Constant.DECREASE_PERCENTAGE_FIVE;
    }

    @Override
    public int updateCoefficient(Integer id, BigDecimal dedutionCoefficient) {
        return channelReportDAO.updateCoefficient(id, dedutionCoefficient);
    }


}
