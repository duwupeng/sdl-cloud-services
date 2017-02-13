package com.talebase.cloud.common.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by eric.du on 2016-12-2.
 */
public class PoiUtil{

        public static void main(String args[]) throws Exception {

            List<Map<Integer, String>> list2 = readExcel("E:/test1.xls");
//            List<Map<Integer, String>> list = readExcel("E:/test1.xlsx");
        }

        public static List<List<String>> readExcelForList(InputStream fis, String extension) throws Exception {
            Workbook wb = null;
            List<List<String>> list = null;
            try {
                wb = read(fis, extension);
                list = readWorkbookForList(wb);

                return list;
            } finally {
                if (null != wb) {
                    wb.close();
                }

                if (null != fis) {
                    fis.close();
                }
            }

    }

        /**
         * 读取excel文件（同时支持2003和2007格式）
         *
         * @param fileName
         *            文件名，绝对路径
         * @return list中的map的key是列的序号
         * @throws Exception
         *             io异常等
         */
        public static List<Map<Integer, String>> readExcel(String fileName) throws Exception {
            FileInputStream fis = null;
            Workbook wb = null;
            List<Map<Integer, String>> list = null;
            try {
                String extension = FilenameUtils.getExtension(fileName);

                fis = new FileInputStream(fileName);
                wb = read(fis, extension);
                list = readWorkbook(wb);

                return list;

            } finally {
                if (null != wb) {
                    wb.close();
                }

                if (null != fis) {
                    fis.close();
                }
            }

        }



        /**
         * 读取excel文件（同时支持2003和2007格式）
         *
         * @param fis
         *            文件输入流
         * @param extension
         *            文件名扩展名: xls 或 xlsx 不区分大小写
         * @return list中的map的key是列的序号
         * @throws Exception
         *             io异常等
         */
        public static Workbook read(InputStream fis, String extension) throws Exception {

            Workbook wb = null;
            List<Map<Integer, String>> list = null;
            try {

                if ("xls".equalsIgnoreCase(extension)) {
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equalsIgnoreCase(extension)) {
                    wb = new XSSFWorkbook(fis);
                } else {
                    throw new Exception("file is not office excel");
                }

                return wb;
            } finally {
                if (null != wb) {
                    wb.close();
                }
            }

        }

        protected static List<Map<Integer, String>> readWorkbook(Workbook wb) throws Exception {
            List<Map<Integer, String>> list = new LinkedList<Map<Integer, String>>();

            for (int k = 0; k < wb.getNumberOfSheets(); k++) {
                Sheet sheet = wb.getSheetAt(k);
                int rows = sheet.getPhysicalNumberOfRows();

                for (int r = 0; r < rows; r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    Map<Integer, String> map = new HashMap<Integer, String>();
                    int cells = row.getPhysicalNumberOfCells();

                    int idx = 0;
                    int physicalCells = 0;
                    while(physicalCells < cells){
                        Cell cell = row.getCell(idx++);
                        if (cell == null) {
                            map.put(idx, "");
                            continue;
                        }
                        String value = getCellValue(cell);
                        map.put(idx, value);
                        physicalCells++;
                    }

                    list.add(map);
                }

            }

            return list;
        }

    protected static List<List<String>> readWorkbookForList(Workbook wb) throws Exception {
        List<List<String>> list = new LinkedList<List<String>>();

        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            Sheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();

            for (int r = 0; r < rows; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
               List<String> str = new ArrayList<>();
                int cells = row.getPhysicalNumberOfCells();

                int idx = 0;
                int physicalCells = 0;
                while(physicalCells < cells){
                    Cell cell = row.getCell(idx++);
                    if (cell == null) {
                        str.add("");
                        continue;
                    }
                    String value = getCellValue(cell);
                    str.add(value);
                    physicalCells++;
                }

                list.add(str);
            }

        }

        return list;
    }


        protected static String getCellValue(Cell cell) {
            String value = null;

            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_FORMULA: // 公式
                case Cell.CELL_TYPE_NUMERIC: // 数字
                    if(HSSFDateUtil.isCellDateFormatted(cell)){
                        Date date = cell.getDateCellValue();
                        value = formatDate(date, "yyyy-MM-dd");
                    }else{
                        Object inputValue = null;// 单元格值
                        Long longVal = Math.round(cell.getNumericCellValue());
                        Double doubleVal = cell.getNumericCellValue();
                        if(Double.parseDouble(longVal + ".0") == doubleVal){   //判断是否含有小数位.0
                            inputValue = longVal;
                        }
                        else{
                            inputValue = doubleVal;
                        }
                        value = String.valueOf(inputValue);
                    }
//                    if (format == 14 || format == 31 || format == 57 || format == 58 || (format >= 176 && format <= 183)) {
//                        // 日期
//                        Date date = DateUtil.getJavaDate(doubleVal);
//                        value = formatDate(date, "yyyy-MM-dd");
//                    } else if (format == 20 || format == 32 || (format >= 184 && format <= 187)) {
//                        // 时间
//                        Date date = DateUtil.getJavaDate(doubleVal);
//                        value = formatDate(date, "HH:mm");
//                    } else {
//                        value = String.valueOf(doubleVal);
//                    }

                    break;
                case Cell.CELL_TYPE_STRING: // 字符串
                    value = cell.getStringCellValue();

                    break;
                case Cell.CELL_TYPE_BLANK: // 空白
                    value = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN: // Boolean
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_ERROR: // Error，返回错误码
                    value = String.valueOf(cell.getErrorCellValue());
                    break;
                default:
                    value = "";
                    break;
            }
            return value;
        }

        @SuppressWarnings("deprecation")
        private static String formatDate(Date d, String sdf) {
            String value = null;
            if (d.getSeconds() == 0 && d.getMinutes() == 0 && d.getHours() == 0) {
                value = TimeUtil.formatDateDay(d);
            } else {
                value = TimeUtil.formatDateSecond(d);
            }

            return value;
        }
}