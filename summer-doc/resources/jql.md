#### 一、每日还款统计 /v1.0/api/reportRepayment
###1 /queryReportInfoAll
    1.统计日期: reportDate String "2018-12-24"
    2.应还款笔总数: allRepaymentCount
    3.当前老用户应还款笔数: expireCountOld
    4.当前新用户应还笔数 expireCountNew
    5.当前用户正常还款笔数 normalRepaymentCount
    6.当前新用户正常还款笔数 normalCountNew
    7.当前老用户正常还款笔数 normalCountOld
    8.正常还款率 repayRate
    9.老用户正常还款率 repayRateOld
    10.新用户正常还款率 repayRateNew
    11.逾期3天内总还款率 repaymentRateS1CountAll
    12.新用户逾期3天内还款率 repaymentRateS1CountNew
    13.老用户逾期3天内还款率 repaymentRateS1CountOld
	13*.逾期7天内总还款率 repaymentRateS2CountAll
    14.新用户逾期7天内还款率  repaymentRateS2CountNew
    15.老用户逾期7天内还款率  repaymentRateS2CountOld
	15*.逾期30天内总还款率 repaymentRateS3CountAll
    16.新用户逾期30天内还款率 repaymentRateS3CountNew
    17.老用户逾期30天内还款率 repaymentRateS3CountOld
    18.当前应还款笔总金额 allRepaymentAmount
    19.当前老用户应还款金额 expireAmountOld
    20.当前新用户应还金额 expireAmountNew
    21.当前用户正常还款金额 normalRepaymentAmount
    22.当前新用户正常还款金额 normalAmountNew
    23.当前老用户正常还款金额 normalAmountOld
    24.当前正常还款率 按金额 repayAmountRate
    25.老用户正常还款率 按金额 repayAmountRateOld
    26.新用户正常还款率 按金额 repayAmountRateNew
	27*.用户逾期3天内还款率 按金额 repaymentRateS1AmountAll
    27.新用户逾期3天内还款率 按金额 repaymentRateS1AmountNew
    28.老用户逾期3天内还款率 按金额 repaymentRateS1AmountOld
	27*.用户逾期7天内还款率 按金额 repaymentRateS2AmountAll
    29.新用户逾期7天内还款率 按金额 repaymentRateS2AmountNew
	29*.老用户逾期7天内还款率 按金额 repaymentRateS2AmountOld
	30*.用户逾期30天内还款率 按金额 repaymentRateS3AmountAll
    30.新用户逾期30天内还款率 按金额 repaymentRateS3AmountNew
    31.老用户逾期30天内还款率 按金额repaymentRateS3AmountOld
    repaymentRateS3AmountOld
    32.渠道名称 channelId
    33.创建时间 createTime
    34.更新时间 updateTime
    注*（通过pageInfo获取列表对象，查询条件为json格式,reportDateStart报告开始时间，reportDateEnd报告结束时间，channelId渠道Id
	分页对象reportRepaymentPageInfo，title对象repaymentTitleVO）
	
###2、每日还款统计（笔数统计报表导出）	
URL /downloadDayStroke
###3、每日还款统计（金额统计报表导出）	
URL /downloadDayMoneyCount