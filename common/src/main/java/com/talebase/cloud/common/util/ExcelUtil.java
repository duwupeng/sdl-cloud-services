package com.talebase.cloud.common.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Eric
 * @date
 */
public class ExcelUtil<T> {

    public void generateExamSheetExcel(HSSFWorkbook workbook, String fileDir, String sheetName, String multiRow, int multiRowColIndex, String[] titleO, String[] titleT, List<T> datas, Class<T> clz) {
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setColumnWidth(0, 3200);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 9500);
        sheet.setColumnWidth(4, 5600);
        sheet.setColumnWidth(5, 5600);
        sheet.setColumnWidth(6, 9000);
        //头部 start
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 10);// 字体大小
        headFont.setBold(true);
        HSSFCellStyle headStyle = workbook.createCellStyle(); // 样式对象
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        headStyle.setFont(headFont);
        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        //新建文件
        FileOutputStream out = null;
        try {
            //添加表头
            Row row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
            for (int i = 0; i < titleO.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(titleO[i]);
                cell.setCellStyle(headStyle);
            }


            HSSFCellStyle style = workbook.createCellStyle(); // 样式对象
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//            style.setWrapText(true); // 换行

            Row rowData;
            Cell cell = null;
            int currentRow = 0;
            for (int i = 0; i < datas.size(); i++) {
                String methodName = "getAnswer";
                Method method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                String[] dataArrayT = (String[]) method.invoke(datas.get(i)); // 执行该get方法,即要插入的数据
                methodName = "getScore";
                method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                double[] dataArrayT1 = (double[]) method.invoke(datas.get(i));

                String dataT;
                for (int subjectRow = 0; subjectRow < dataArrayT.length; subjectRow++) {
                    currentRow++;
                    if (subjectRow == 0) {
                        rowData = sheet.createRow(currentRow);
                        for (int columnIndex = 0; columnIndex < titleO.length; columnIndex++) {  //遍历表头
                            String title = titleO[columnIndex];
                            String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1, title.length()); // 使其首字母大写;
                            methodName = "get" + UTitle;
                            method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                            dataT = method.invoke(datas.get(i)).toString(); // 执行该get方法,即要插入的数据
                            if (StringUtil.isEmpty(dataT)) {
                                dataT = " ";
                            }
                            if (columnIndex != multiRowColIndex && dataArrayT.length > 1
                                    && columnIndex != multiRowColIndex+1 ) {
                                sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow + dataArrayT.length - 1, columnIndex, columnIndex));
                            }

                            if (columnIndex == multiRowColIndex+1 && dataArrayT.length !=dataArrayT1.length) {
                                sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow + dataArrayT.length - 1, columnIndex, columnIndex));
                            }

                            cell = rowData.createCell(columnIndex);
                            cell.setCellStyle(style);
                            if (method.getReturnType().equals(double.class)) {
                                cell.setCellValue(Double.parseDouble(dataT));
                            } else {
                                if (columnIndex == multiRowColIndex) {
                                    cell.setCellValue(dataArrayT[subjectRow]);
                                } else if (columnIndex == multiRowColIndex+1) {
                                    cell.setCellValue(dataArrayT1[subjectRow]);
                                } else {
                                    cell.setCellValue(dataT);
                                }
                            }
                        }
                    } else {
                        rowData = sheet.createRow(currentRow);
                        for (int columnIndex = 0; columnIndex < titleO.length; columnIndex++) {  //遍历表头
                            cell = rowData.createCell(columnIndex);
                            cell.setCellStyle(style);
                            if (columnIndex == multiRowColIndex) {
                                cell.setCellValue(dataArrayT[subjectRow]);
                            }else if (columnIndex == multiRowColIndex+1 && dataArrayT1.length>1) {
                                cell.setCellValue(dataArrayT1[subjectRow]);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < titleT.length; i++) {
                cell = row.getCell(i);
                cell.setCellValue(titleT[i]);
            }
            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateOptionSheet(HSSFWorkbook workbook, String fileDir, String sheetName, String multiRow, int multiRowColIndex, String[] titleO, String[] titleT, List<T> datas, Class<T> clz) {
        String[] scoreRule = {"全部答对才给分","少选统一给分","少选按比例给分"};
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(scoreRule);
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setColumnWidth(0, 3200);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 1000);
        sheet.setColumnWidth(3, 9500);
        sheet.setColumnWidth(4, 5600);
        sheet.setColumnWidth(5, 5600);
        sheet.setColumnWidth(6, 9000);
        //头部 start
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 10);// 字体大小
        headFont.setBold(true);
        HSSFCellStyle headStyle = workbook.createCellStyle(); // 样式对象
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        headStyle.setFont(headFont);
        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        //新建文件
        FileOutputStream out = null;
        try {
            //添加表头
            Row row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
            for (int i = 0; i < titleO.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(titleO[i]);
                cell.setCellStyle(headStyle);
            }

            HSSFCellStyle style = workbook.createCellStyle(); // 样式对象
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//            style.setWrapText(true); // 换行

            Row rowData;
            Cell cell = null;
            int currentRow = 0;
            for (int i = 0; i < datas.size(); i++) {
                String methodName = "get" + multiRow;
                Method method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                String[] dataArrayT = (String[]) method.invoke(datas.get(i)); // 执行该get方法,即要插入的数据
                String dataT;
                int index = 1;
                for (int subjectRow = 0; subjectRow < dataArrayT.length; subjectRow++) {
                    currentRow++;
                    if (subjectRow == 0) {
                        rowData = sheet.createRow(currentRow);
                        for (int columnIndex = 0; columnIndex < titleO.length; columnIndex++) {  //遍历表头
                            String title = titleO[columnIndex];
                            String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1, title.length()); // 使其首字母大写;
                            methodName = "get" + UTitle;
                            method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                            dataT = method.invoke(datas.get(i)) == null ? " " : method.invoke(datas.get(i)).toString(); // 执行该get方法,即要插入的数据
                            if (columnIndex != (multiRowColIndex - 1) && columnIndex != multiRowColIndex && dataArrayT.length > 1) {
                                sheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow + dataArrayT.length - 1, columnIndex, columnIndex));
                            }
                            if (columnIndex == 6) {
                                CellRangeAddressList regions = new CellRangeAddressList(currentRow, currentRow + dataArrayT.length - 1, columnIndex, columnIndex);
                                HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
                                sheet.addValidationData(dataValidation);
                            }
                            cell = rowData.createCell(columnIndex);
                            cell.setCellStyle(style);
//                            if (method.getReturnType().equals(double.class)) {
//                                cell.setCellValue(Double.parseDouble(dataT));
//                            } else {
                                if (columnIndex == multiRowColIndex) {
                                    cell.setCellValue(dataArrayT[subjectRow]);
                                } else if (columnIndex == multiRowColIndex - 1) {
                                    cell.setCellValue(index);
                                } else {
                                    cell.setCellValue(dataT);
                                }
//                            }
                        }
                    } else {
                        rowData = sheet.createRow(currentRow);
                        for (int columnIndex = 0; columnIndex < titleO.length; columnIndex++) {  //遍历表头
                            cell = rowData.createCell(columnIndex);
                            cell.setCellStyle(style);
                            if (columnIndex == multiRowColIndex - 1) {
                                index++;//序号列专用
                                cell.setCellValue(index);
                            } else if (columnIndex == multiRowColIndex) {
                                cell.setCellValue(dataArrayT[subjectRow]);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < titleT.length; i++) {
                cell = row.getCell(i);
                cell.setCellValue(titleT[i]);
            }

            sheet.addMergedRegion(new CellRangeAddress(0, 0, multiRowColIndex - 1, multiRowColIndex));
            row.getCell(multiRowColIndex - 1).setCellValue(titleT[multiRowColIndex]);

            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void generateBlankSheet(HSSFWorkbook workbook,
                                   String fileDir,
                                   String sheetName,
                                   int multiRowColIndex,
                                   String[] titleO,
                                   String[] titleT,
                                   List<T> datas, Class<T> clz) {

        String[] scoreRule = {"完全一致", "仅顺序一致"};
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(scoreRule);
        Sheet sheet = workbook.createSheet(sheetName);
        sheet.setColumnWidth(0, 3200);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 1000);
        sheet.setColumnWidth(3, 2500);
        sheet.setColumnWidth(4, 5600);
        sheet.setColumnWidth(5, 5600);
        sheet.setColumnWidth(6, 9000);
        sheet.setColumnWidth(7, 9000);

        //头部 start
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 10);// 字体大小
        headFont.setBold(true);
        HSSFCellStyle headStyle = workbook.createCellStyle(); // 样式对象
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        headStyle.setFont(headFont);
        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        //新建文件
        FileOutputStream out = null;
        try {
            //添加表头
            Row row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
            for (int i = 0; i < titleO.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(titleO[i]);
                cell.setCellStyle(headStyle);
            }

            HSSFCellStyle style = workbook.createCellStyle(); // 样式对象
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
//            style.setWrapText(true); // 换行

            Row rowData;
            Cell cell = null;
            int currentRow = 0;
            for (int i = 0; i < datas.size(); i++) {
                String methodName = "getOptions";
                Method method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                String[] dataArrayT = (String[]) method.invoke(datas.get(i)); // 执行该get方法,即要插入的数据
                methodName = "getAnswers";
                method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                String[] dataArrayT1 = (String[]) method.invoke(datas.get(i));
                methodName = "getScores";
                method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                String[] dataArrayT2 = (String[]) method.invoke(datas.get(i));
                int index = 1;
                String dataT;
                for (int subjectRow = 0; subjectRow < dataArrayT.length; subjectRow++) {
                    currentRow++;
                    if (subjectRow == 0) {
                        rowData = sheet.createRow(currentRow);
                        for (int columnIndex = 0; columnIndex < titleO.length; columnIndex++) {  //遍历表头
                            String title = titleO[columnIndex];
                            String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1, title.length()); // 使其首字母大写;
                            methodName = "get" + UTitle;
                            method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                            dataT = method.invoke(datas.get(i)).toString(); // 执行该get方法,即要插入的数据
                            if (StringUtil.isEmpty(dataT)) {
                                dataT = " ";
                            }
                            if (columnIndex != (multiRowColIndex - 1)
                                    && columnIndex != multiRowColIndex
                                    && columnIndex != multiRowColIndex + 1
                                    && columnIndex != multiRowColIndex + 2
                                    && dataArrayT.length > 1) {
                                CellRangeAddress cellRangeAddresses = new CellRangeAddress(currentRow, currentRow + dataArrayT.length - 1, columnIndex, columnIndex);
                                sheet.addMergedRegion(cellRangeAddresses);
                                if (columnIndex == 6) {
                                    CellRangeAddressList regions = new CellRangeAddressList(currentRow, currentRow + dataArrayT.length - 1, columnIndex, columnIndex);
                                    HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
                                    sheet.addValidationData(dataValidation);
                                }
                            }

                            cell = rowData.createCell(columnIndex);
                            cell.setCellStyle(style);
                            if (method.getReturnType().equals(double.class)) {
                                cell.setCellValue(Double.parseDouble(dataT));
                            } else {
                                if (columnIndex == multiRowColIndex - 1) {
                                    cell.setCellValue(index);
                                } else if (columnIndex == multiRowColIndex) {
                                    cell.setCellValue(dataArrayT[subjectRow]);
                                } else if (columnIndex == multiRowColIndex + 1) {
                                    cell.setCellValue(dataArrayT1[subjectRow]);
                                } else if (columnIndex == multiRowColIndex + 2) {
                                    cell.setCellValue(dataArrayT2[subjectRow]);
                                } else {
                                    cell.setCellValue(dataT);
                                }
                            }
                        }
                    } else {
                        rowData = sheet.createRow(currentRow);
                        for (int columnIndex = 0; columnIndex < titleO.length; columnIndex++) {  //遍历表头
                            cell = rowData.createCell(columnIndex);
                            cell.setCellStyle(style);
                            if (columnIndex == multiRowColIndex - 1) {
                                index++;
                                cell.setCellValue(index);
                            } else if (columnIndex == multiRowColIndex) {
                                cell.setCellValue(dataArrayT[subjectRow]);
                            } else if (columnIndex == multiRowColIndex + 1) {
                                cell.setCellValue(dataArrayT1[subjectRow]);
                            } else if (columnIndex == multiRowColIndex + 2) {
                                cell.setCellValue(dataArrayT2[subjectRow]);
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < titleT.length; i++) {
                cell = row.getCell(i);
                cell.setCellValue(titleT[i]);
            }

            sheet.addMergedRegion(new CellRangeAddress(0, 0, multiRowColIndex - 1, multiRowColIndex));
            row.getCell(multiRowColIndex - 1).setCellValue(titleT[multiRowColIndex]);

            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void generateAttachmentSheet(HSSFWorkbook workbook,
                                        String fileDir,
                                        String sheetName,
                                        String[] titleO,
                                        String[] titleT,
                                        List<T> datas,
                                        Class<T> clz) {
        Sheet sheet = workbook.createSheet(sheetName);

        sheet.setColumnWidth(0, 3000);
        sheet.setColumnWidth(1, 9000);
        sheet.setColumnWidth(2, 3000);


        //头部 start
        HSSFFont headFont = workbook.createFont();
        headFont.setFontName("宋体");
        headFont.setFontHeightInPoints((short) 10);// 字体大小
        headFont.setBold(true);
        HSSFCellStyle headStyle = workbook.createCellStyle(); // 样式对象
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        headStyle.setFont(headFont);
        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框


        //新建文件
        FileOutputStream out = null;
        try {
            //添加表头
            Row row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
            for (int i = 0; i < titleO.length; i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(titleO[i]);
                cell.setCellStyle(headStyle);
            }


            HSSFCellStyle style = workbook.createCellStyle(); // 样式对象
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
            style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 水平
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setWrapText(true); // 换行

            Row rowData;
            Cell cell;
            for (int i = 0; i < datas.size(); i++) {
                rowData = sheet.createRow(i + 1);
                for (int columnIndex = 0; columnIndex < titleO.length; columnIndex++) {  //遍历表头
                    String title = titleO[columnIndex];
                    String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1, title.length()); // 使其首字母大写;
                    String methodName = "get" + UTitle;
                    Method method = clz.getDeclaredMethod(methodName); // 设置要执行的方法
                    String dataT = method.invoke(datas.get(i)).toString(); // 执行该get方法,即要插入的数据
                    if (StringUtil.isEmpty(dataT)) {
                        dataT = " ";
                    }
                    cell = rowData.createCell(columnIndex);
                    cell.setCellValue(dataT);
                    cell.setCellStyle(style);
                }
            }

            for (int i = 0; i < titleT.length; i++) {
                cell = row.getCell(i);
                cell.setCellValue(titleT[i]);
            }

            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


