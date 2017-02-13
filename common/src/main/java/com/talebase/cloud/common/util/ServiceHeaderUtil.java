package com.talebase.cloud.common.util;

import com.google.gson.Gson;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.ServiceHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by kanghong.zhao on 2016-11-29.
 */
public class ServiceHeaderUtil {

    public static final String SERVICEHEADER = "serviceHeader";

    public static HttpSession getSession() {
        HttpSession session = null;
        try {
            session = getRequest().getSession();
        } catch (Exception e) {
        }
        return session;
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest();
    }

    public static ServiceHeader getRequestHeader() {
        //test 环境
        Cookie[] cooks = ServiceHeaderUtil.getRequest().getCookies();
        ServiceHeader requestHeader = new ServiceHeader();
        if (cooks != null) {
            String requestHeaderStr = null;
            try {
                requestHeaderStr =   URLDecoder.decode(cooks[0].getValue(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            requestHeader = GsonUtil.fromJson(requestHeaderStr, ServiceHeader.class);
        }
//        else{
//            //dev环境先用
//            requestHeader = new ServiceHeader();
//            requestHeader.customerId = 42282;
//            requestHeader.customerName = "zhaokh";
//            requestHeader.operatorId = 0;
//            requestHeader.operatorName = "zhaokh";
//            requestHeader.callerFrom = CallerFrom.web;
//            requestHeader.callerIP = "127.0.0.1";
//            requestHeader.orgCode = "1_";
//            requestHeader.companyId = 1;
//            requestHeader.transactionId = "123456789";
//            requestHeader.setPermissions(Arrays.asList("Permission1", "Permission2", "Permission3", "xxxx"));
//        }

        return requestHeader;

//        if(getSession() == null)
//            return null;
//        return (RequestHeader)getSession().getAttribute(SERVICEHEADER);
    }

}
