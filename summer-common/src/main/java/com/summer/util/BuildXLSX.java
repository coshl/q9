package com.summer.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Desc:
 * Created by tl on 2018/12/20
 */
public class BuildXLSX {
    private static final Logger log = LoggerFactory.getLogger(BuildXLSX.class);

    public static void setFileDownloadHeader(HttpServletRequest request,
                                             HttpServletResponse response, String fileName) {
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        fileName = fileName + ".xlsx";
        String finalFileName = fileName;
        try {
            if (StringUtils.contains(userAgent, "firefox")) {// 火狐浏览器
                finalFileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                finalFileName = URLEncoder.encode(fileName, "UTF8");// 其他浏览器
            }
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + finalFileName + "\"");// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
        } catch (Exception e) {
            log.error("setFileDownloadHeader error", "BuildXLSX.setFileDownloadHeader", e);
        }
    }

    /**
     * @param xlsxParam 数据源
     * @param os        输出流
     * @since
     */
    @SuppressWarnings("resource")
    public static void buildExcel(XlsxParam xlsxParam, OutputStream os) throws Exception {
        // 声明一个工作薄
        XSSFWorkbook workBook = null;
        workBook = new XSSFWorkbook();
        buildSheet(workBook, xlsxParam, 0);
        //输出流
        workBook.write(os);
        os.flush();
        os.close();
    }

    public static void buildExcels(List<XlsxParam> params, OutputStream os) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        int size = 0;
        for (int i = 0; i < params.size(); i++) {
            XlsxParam xlsxParam = params.get(i);
            size += buildSheet(workbook, xlsxParam, size);
        }
        workbook.write(os);
        os.flush();
        os.close();
    }

    private static int buildSheet(XSSFWorkbook workBook, XlsxParam xlsxParam, int size) throws Exception {
        // 获取数据总条数
        List<?> dataList = xlsxParam.getDataList();
        int count = dataList.size();
        int rowMaxCount = xlsxParam.getRowMaxCount();
        String sheetTitle = xlsxParam.getSheetTitle();
        LinkedHashMap<String, String> titleMap = xlsxParam.getTitleMap();
        // 需要分多少个sheet
        int sheetCount = count % rowMaxCount > 0 ? count / rowMaxCount + 1 : count / rowMaxCount;
        // 拆分大的List为多个小的List
        List<List<?>> splitList = null;
        //if (dataList != null && !dataList.isEmpty()) {
        splitList = getSplitList(dataList, rowMaxCount, sheetCount);
        //} else {
        //throw new Exception("源数据不存在");
        //}
        //无数据时生成只带列名的表格
        if (splitList.size() == 0) {
            // 生成一个表格
            XSSFSheet sheet = workBook.createSheet();
            workBook.setSheetName(0, sheetTitle);
            //最新Excel列索引,从0开始
            int lastRowIndex = sheet.getLastRowNum();
            if (lastRowIndex > 0) {
                lastRowIndex++;
            }
            createTitle(sheet, sheetTitle, titleMap, lastRowIndex);
        }
        //循环dataList 看需要生成几个sheet
        for (int i = 0; i < splitList.size(); i++) {
            // 生成一个表格
            XSSFSheet sheet = workBook.createSheet();
            workBook.setSheetName(size, sheetTitle + "_" + (i + 1));
            size += 1;
            createTitle(sheet, sheetTitle, titleMap, 0);
            //最新Excel列索引,从0开始
            int lastRowIndex = sheet.getLastRowNum();
            //插入需导出的数据
            Class<? extends Object> clazz = null;
            List<?> subList = new ArrayList<>();
            subList = splitList.get(i);
            for (int j = 0; j < subList.size(); j++) {
                clazz = subList.get(0).getClass();
                XSSFRow row = sheet.createRow(j + lastRowIndex + 1);
                Iterator<String> colIteratorK = titleMap.keySet().iterator();
                int k = 0;
                while (colIteratorK.hasNext()) {
                    Object key = colIteratorK.next();
                    Method method = clazz.getMethod(getMethodName(key.toString()));
                    Object obj = method.invoke(subList.get(j));
                    row.createCell(k).setCellValue(obj == null ? "" : obj.toString());
                    k++;
                }
            }

        }
        return splitList.size();
    }

//    public static void buildExcels(List<?> dataList, int rowMaxCount,
//                                      String sheetTitle,LinkedHashMap<String,String> titleMap,
// OutputStream os) throws Exception{
//        XSSFWorkbook workBook = new XSSFWorkbook();
//    }

    /**
     * 添加表格名称和标题
     *
     * @param sheet
     * @param sheetTitle
     * @param titleMap
     * @param lastRowIndex
     */
    public static void createTitle(XSSFSheet sheet, String sheetTitle, LinkedHashMap<String,
            String> titleMap, int lastRowIndex) {
        if (sheetTitle != null) {
            // 合并单元格
            //参数：起始行号，终止行号， 起始列号，终止列号
            //CellRangeAddress（int firstRow, int lastRow, int firstCol, int lastCol）
            sheet.addMergedRegion(new CellRangeAddress(lastRowIndex, lastRowIndex, 0,
                    titleMap.size()));
            // 产生表格标题行
            XSSFCell cellMerged = sheet.createRow(lastRowIndex).createCell(lastRowIndex);
            cellMerged.setCellValue(new XSSFRichTextString(sheetTitle));
            lastRowIndex++;
        }
        // 创建表格列标题行
        XSSFRow titleRow = sheet.createRow(lastRowIndex);
        Iterator<String> colIteratorV = titleMap.values().iterator();
        int h = 0;
        while (colIteratorV.hasNext()) {
            Object value = colIteratorV.next();
            titleRow.createCell(h).setCellValue(value.toString());
            h++;
        }
    }

    /**
     * 分割list
     *
     * @param dataList    数据源
     * @param rowMaxCount 每个sheet最大记录条数
     * @param sheetCount  需要分多少个sheet
     * @return
     */
    public static List<List<?>> getSplitList(List<?> dataList, int rowMaxCount,
                                             int sheetCount) {
        List<List<?>> subList = new ArrayList<List<?>>();
        for (int i = 1; i <= sheetCount; i++) {
            if (i == 1) {
                // 第一个list
                if (dataList.size() >= rowMaxCount) {
                    subList.add(dataList.subList(0, rowMaxCount));
                } else {
                    subList.add(dataList.subList(0, dataList.size()));
                }
            } else if (i == sheetCount) {
                // 最后一个listn
                subList.add(dataList.subList((sheetCount - 1) * rowMaxCount, dataList.size()));
            } else {
                subList.add(dataList.subList((i - 1) * rowMaxCount, i * rowMaxCount));
            }
        }
        return subList;
    }

    /**
     * 获取方法名
     *
     * @param
     */
    private static String getMethodName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}