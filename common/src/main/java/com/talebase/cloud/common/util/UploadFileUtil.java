package com.talebase.cloud.common.util;

import java.net.URLDecoder;

/**
 * Created by daorong.li on 2016-11-29.
 * 文件上传处理
 */
public class UploadFileUtil {

    private static int IMAGE_FILESIZE = 20480000;//图片固定大小
    private static String IMAGE_FILETYPE = ",jif,bmp,jpg,jpeg,tif,png,pcx,ico,cur,";//图片上传类型
    private static String IMAGE_SAVEPATH ="";//图片保存路基

    /**
     * 获取文件类型
     * @param originalFilename
     * @return
     */
    public static String fileType(String originalFilename){
        //获取文件后缀
        if (originalFilename.lastIndexOf(".")>0){
           return originalFilename.substring(originalFilename.lastIndexOf(".")+1, originalFilename.length());
        }
        return "";
    }

    public static  boolean isImageType(String originalFilename){
        String fileType = ","+fileType(originalFilename)+",";
        if (IMAGE_FILETYPE.indexOf(fileType)>=0)
            return true;
        return false;
    }

    public static boolean isImageFileBeyoundSize(long size){
        if (size > IMAGE_FILESIZE)
            return true;
        return false;
    }

    public static String imageSavePath(String targetPath)throws Exception{
        //没有指定路径则使用taget路径保存图片
        if ("".equals(IMAGE_SAVEPATH))
            return URLDecoder.decode(targetPath,"utf-8");
        return  IMAGE_SAVEPATH;
    }

}
