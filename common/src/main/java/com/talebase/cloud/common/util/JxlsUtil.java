package com.talebase.cloud.common.util;

import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kanghong.zhao on 2016-11-23.
 */
public class JxlsUtil {

    public static void write(InputStream inputStream, OutputStream outputStream, Map<String, Object> data, String sheetCellAt) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);

        Context context = PoiTransformer.createInitialContext();

        for(Iterator<String> iterator = data.keySet().iterator(); iterator.hasNext();){
            String key = iterator.next();
            context.putVar(key, data.get(key));
        }

        JxlsHelper.getInstance().processTemplateAtCell(inputStream, bos, context, sheetCellAt);
    }

    public static void write(HttpServletRequest request,InputStream inputStream, HttpServletResponse response, Map<String, Object> data, String fileName, String sheetCellAt) throws IOException {
        String userAgent = request.getHeader("User-Agent");
        //针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("MSIE")||userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            //非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
        }
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        response.setCharacterEncoding("UTF-8");
        write(inputStream, response.getOutputStream(), data, sheetCellAt);
    }

    public static void write(File file, OutputStream outputStream, Map<String, Object> data, String sheetCellAt) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        write(inputStream, outputStream, data, sheetCellAt);
    }

    public static void write(HttpServletRequest request, File file, HttpServletResponse response, Map<String, Object> data, String filename, String sheetCellAt) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        write(request,inputStream, response, data, filename, sheetCellAt);
    }

}
