package com.summer.dao.mapper;

import com.summer.dao.entity.MmanLoanCollectionStatusChangeLog;

import java.util.HashMap;
import java.util.List;

public interface IMmanLoanCollectionStatusChangeLogDao {
    /**
     * 查询所有催收流转日志记录
     *
     * @return 查询到的催收流转日志记录
     */
    public List<MmanLoanCollectionStatusChangeLog> findAll(HashMap<String, Object> params);

    /**
     * 新增 MmanLoanCollectionStatusChangeLog
     *
     * @param mmanLoanCollectionStatusChangeLog
     */
    public void insert(MmanLoanCollectionStatusChangeLog mmanLoanCollectionStatusChangeLog);


    public List<MmanLoanCollectionStatusChangeLog> findListLog(String orderId);

    public int findOrderSingle(HashMap<String, Object> params);


    public Integer findSystemUpToS2Log(HashMap<String, String> params);
}
