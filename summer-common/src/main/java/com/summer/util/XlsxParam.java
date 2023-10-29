
package com.summer.util;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
public class XlsxParam {

    public XlsxParam() {
    }

    public XlsxParam(List<?> dataList, int rowMaxCount, String sheetTitle, LinkedHashMap<String,
            String> titleMap) {
        this.dataList = dataList;
        this.rowMaxCount = rowMaxCount;
        this.sheetTitle = sheetTitle;
        this.titleMap = titleMap;
    }

    public XlsxParam(List<?> dataList, String sheetTitle, LinkedHashMap<String, String> titleMap) {
        this.dataList = dataList;
        this.sheetTitle = sheetTitle;
        this.titleMap = titleMap;
    }

    /**
     * 导出源数据list
     */
    private List<?> dataList;

    /**
     * 每个工作簿最大行
     * 默认1000行
     */
    private int rowMaxCount = 1000;

    /**
     * 工作簿名称
     */
    private String sheetTitle;

    /**
     * 每个工作簿字段名
     */
    private LinkedHashMap<String, String> titleMap;

    public List<?> getDataList() {
        return dataList;
    }

    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }

    public int getRowMaxCount() {
        return rowMaxCount;
    }

    public void setRowMaxCount(int rowMaxCount) {
        this.rowMaxCount = rowMaxCount;
    }

    public String getSheetTitle() {
        return sheetTitle;
    }

    public void setSheetTitle(String sheetTitle) {
        this.sheetTitle = sheetTitle;
    }

    public LinkedHashMap<String, String> getTitleMap() {
        return titleMap;
    }

    public void setTitleMap(LinkedHashMap<String, String> titleMap) {
        this.titleMap = titleMap;
    }
}
