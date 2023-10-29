package com.summer.web.util;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
@Slf4j
public class ExcelExportUtil {
    //表头
    private String title;
    //各个列的表头
    private String[] heardList;
    //各个列的元素key值
    private String[] heardKey;
    //需要填充的数据信息
    private List<Map> data;
    //字体大小
    private int fontSize = 12;
    //行高
    private int rowHeight = 30;
    //列宽
    private int columWidth = 200;
    //工作表
    private String sheetName = "sheet1";
    //文件
    private String fileName ="文件名称";

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getHeardList() {
        return heardList;
    }

    public void setHeardList(String[] heardList) {
        this.heardList = heardList;
    }

    public String[] getHeardKey() {
        return heardKey;
    }

    public void setHeardKey(String[] heardKey) {
        this.heardKey = heardKey;
    }

    public List<Map> getData() {
        return data;
    }

    public void setData(List<Map> data) {
        this.data = data;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public int getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public int getColumWidth() {
        return columWidth;
    }

    public void setColumWidth(int columWidth) {
        this.columWidth = columWidth;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    /**
     * 开始导出数据信息
     *
     */
    public byte[] exportExport(HttpServletResponse response) throws IOException {
        log.info("===============开始导出文件==============");
        //检查参数配置信息
        checkConfig();
        //创建工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建工作表
        HSSFSheet wbSheet = wb.createSheet(this.sheetName);
        //设置默认行宽
        wbSheet.setDefaultColumnWidth(20);

        // 标题样式（加粗，垂直居中）
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setBold(true);   //加粗
        fontStyle.setFontHeightInPoints((short)16);  //设置标题字体大小
        cellStyle.setFont(fontStyle);

        //在第0行创建rows  (表标题)
        HSSFRow title = wbSheet.createRow((int) 0);
        title.setHeightInPoints(30);//行高
        HSSFCell cellValue = title.createCell(0);
        cellValue.setCellValue(this.title);
        cellValue.setCellStyle(cellStyle);
        wbSheet.addMergedRegion(new CellRangeAddress(0,0,0,(this.heardList.length-1)));
        //设置表头样式，表头居中
        HSSFCellStyle style = wb.createCellStyle();
        //设置单元格样式
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) this.fontSize);
        style.setFont(font);
        //在第1行创建rows
        HSSFRow row = wbSheet.createRow((int) 1);
        //设置列头元素
        HSSFCell cellHead = null;
        for (int i = 0; i < heardList.length; i++) {
            cellHead = row.createCell(i);
            cellHead.setCellValue(heardList[i]);
            cellHead.setCellStyle(style);
        }
        //设置每格数据的样式 （字体红色）
        HSSFCellStyle cellParamStyle = wb.createCellStyle();
        HSSFFont ParamFontStyle = wb.createFont();
        cellParamStyle.setAlignment(HorizontalAlignment.CENTER);
        cellParamStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //ParamFontStyle.setColor(HSSFColor..index);   //设置字体颜色 (红色)
        ParamFontStyle.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle.setFont(ParamFontStyle);
        //设置每格数据的样式2（字体蓝色）
        HSSFCellStyle cellParamStyle2 = wb.createCellStyle();
        cellParamStyle2.setAlignment(HorizontalAlignment.CENTER);
        cellParamStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont ParamFontStyle2 = wb.createFont();
       // ParamFontStyle2.setColor(HSSFColor.BLUE.index);   //设置字体颜色 (蓝色)
        ParamFontStyle2.setFontHeightInPoints((short) this.fontSize);
        cellParamStyle2.setFont(ParamFontStyle2);
        //开始写入实体数据信息
        int a = 2;
        for (int i = 0; i < data.size(); i++) {
            HSSFRow roww = wbSheet.createRow((int) a);
            Map map = data.get(i);
            HSSFCell cell = null;
            for (int j = 0; j < heardKey.length; j++) {
                cell = roww.createCell(j);
                cell.setCellStyle(style);
                Object valueObject = map.get(heardKey[j]);
                String value = null;
                if (valueObject == null) {
                    valueObject = "";
                }
                if (valueObject instanceof String) {
                    //取出的数据是字符串直接赋值
                    value = (String) map.get(heardKey[j]);
                } else if (valueObject instanceof Integer) {
                    //取出的数据是Integer
                    value = String.valueOf(((Integer) (valueObject)));
                } else if (valueObject instanceof BigDecimal) {
                    //取出的数据是BigDecimal
                    value = String.valueOf(((BigDecimal) (valueObject)).floatValue());
                } else {
                    value = valueObject.toString();
                }
                //设置单个单元格的字体颜色
/*                if(heardKey[j].equals("ddNum") || heardKey[j].equals("sjNum")){
                    if((Long)map.get("ddNum")!=null){
                        if((Long)map.get("sjNum")==null){
                            cell.setCellStyle(cellParamStyle);
                        } else if((Long) map.get("ddNum") != (Long) map.get("sjNum")){
                            if ((Long) map.get("ddNum") > (Long) map.get("sjNum")) {
                                cell.setCellStyle(cellParamStyle);
                            }
                            if ((Long) map.get("ddNum") < (Long) map.get("sjNum")) {
                                cell.setCellStyle(cellParamStyle2);
                            }
                        }else {
                            cell.setCellStyle(style);
                        }
                    }
                }*/
                cell.setCellValue(Strings.isNullOrEmpty(value) ? "" : value);
            }
            a++;
        }

        //导出数据
        try {
            //response.setCharacterEncoding("UTF-8");
            String fileNames =this.fileName+DateFormatUtils.format(new Date(),"MM_dd")+".xls";
            response.setHeader("Content-disposition", "attachment;Filename="+new String(fileNames.getBytes("gbk"), "ISO-8859-1"));
            response.setContentType("application/msexcel;charset=GBK");
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            log.info("===============导出成功=================");
            outputStream.close();
            return wb.getBytes();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("导出Excel异常"+ex.getMessage());
            throw new IOException("导出Excel出现严重异常，异常信息：" + ex.getMessage());
        }

    }

    /**
     * 检查数据配置问题
     *
     * @throws IOException 抛出数据异常类
     */
    protected void checkConfig() throws IOException {
        if (heardKey == null || heardList.length == 0) {
            throw new IOException("列名数组不能为空或者为NULL");
        }

        if (fontSize < 0 || rowHeight < 0 || columWidth < 0) {
            throw new IOException("字体、宽度或者高度不能为负值");
        }

        if (Strings.isNullOrEmpty(sheetName)) {
            throw new IOException("工作表表名不能为NULL");
        }
    }

    public static void exportExcel(HttpServletResponse response,List<Map> map,String [] title,String [] properties,String sheetTitle) throws Exception {
        if(map!=null && map.size()>0){
            ExcelExportUtil excelExport2 = new ExcelExportUtil();
            excelExport2.setData(map);
            excelExport2.setHeardKey(properties);
            excelExport2.setFontSize(12);
            excelExport2.setSheetName(sheetTitle);//页标题
            excelExport2.setTitle(sheetTitle);//大标题
            excelExport2.setHeardList(title);//title
            excelExport2.setFileName(sheetTitle);
            excelExport2.exportExport(response);
        }

    }

    public static String changApptype(Integer appType){
        if(appType==0){
            return "未知";
        } else if(appType==1){
            return "安卓";
        }
        return "IOS";
    }

    public static String changCustomerTypeName(Integer type){
        if(type==1){
            return "老客";
        }
        return "新客";
    }

    public static String changeStatusName(Byte Status){
        StringBuilder statusName =new StringBuilder();
        if(Status==0){
            statusName.append("未认证");
        }else if(Status==1){
            statusName.append("身份证");
        } else if(Status==2) {
            statusName.append("身份证,个人信息");
        } else if(Status==3) {
            statusName.append("身份证,个人信息,运营商");
        } else if(Status==4) {
            statusName.append("身份证,个人信息,运营商,银行卡");
        }
        return statusName.toString();
    }

}
