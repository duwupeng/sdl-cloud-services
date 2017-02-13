package com.talebase.cloud.gateway.filter;

import com.google.gson.Gson;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.ZuulFilter;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.PermissionEnum;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by eric on 16/11/7.
 */
public class AccessFilter extends ZuulFilter {

    @Autowired
    RedisTemplate redisTemplate;

    private static List<String> exerciseUrls = Arrays.asList(
            "GET-/osexamer/exam/examer/",
            "GET-/osexamer/exercise/item/",
            "POST-/osexamer/exercise/answer/",
            "DELETE-/osexamer/exercise/attachment/",
            "POST-/osexamer/exercise/attachment/upload/",
            "POST-/osexamer/exercise/flush/",
            "GET-/osexamer/examer/project/modifyField",
            "GET-/osexamer/examer/exercise/itemForPC/",
            "POST-/osexamer/examer/modifyUserForPerfect");

    private static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    private final static Integer NoPermissionCode = 403;

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info(String.format("%s request to %s parameters %s", request.getMethod(), request.getRequestURL().toString(), request.getParameterMap()));
        log.info(String.format("%s request to %s ", request.getMethod(),  request.getServletPath()));

        String servletPath = request.getServletPath();
        if (servletPath.contains("login") || servletPath.contains("/checkScanCode")) {
            String domainKey = "domain_" + request.getServerName();
            Integer companyId = (Integer) redisTemplate.opsForValue().get(domainKey);
            if (companyId == null || companyId == 0) {
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(NoPermissionCode);
            } else {
                ServiceHeader requestHeader = new ServiceHeader();
                requestHeader.setCompanyId(companyId);
                requestHeader.setCompanyDomain(request.getServerName());
                ctx.addZuulRequestHeader("Cookie", "SESSION=" + GsonUtil.toJson(requestHeader));
            }
            return null;
        }
        if(servletPath.contains("/company/initializationCompany")){//此链接跳过登录
            return null;
        }
        String accessToken = request.getParameter("accessToken");
        if (accessToken == null) {//有可能在body里，所以要用body的做法读多次
            try {
                String body = request.getReader().readLine();
                if (body.contains("accessToken")) {
                    String[] ss = body.split("&");
                    for (String p : ss) {
                        String[] keyValue = p.split("=");
                        if (keyValue[0].equals("accessToken") && keyValue.length == 2) {
                            accessToken = keyValue[1];
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (accessToken == null) {
            log.warn("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(NoPermissionCode);
            return null;
        } else {
            String tokenKey = "token_" + accessToken;
            ServiceHeader requestHeader = (ServiceHeader) redisTemplate.opsForValue().get(tokenKey);
            if (requestHeader == null) {//登录信息失效
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(NoPermissionCode);
                return null;
            }

            requestHeader.setSeqId();

            redisTemplate.opsForValue().set(tokenKey, requestHeader);//重新  set
            redisTemplate.expire(tokenKey, 120, TimeUnit.MINUTES);//续时

            String urlToCheck = checkUrl();
            boolean hasAuth = false;

            if (requestHeader.getOperatorId() == 0 && requestHeader.getCustomerId() != 0) {//考生
                hasAuth = checkUrl(exerciseUrls.toArray(new String[exerciseUrls.size()]), urlToCheck);
            } else {//非考生
                List<String> permissions = requestHeader.getPermissions();
                for (String permission : permissions) {
                    PermissionEnum permissionEnum = PermissionEnum.findByName(permission);
                    if (permissionEnum == null)
                        continue;
                    String[] urls = permissionEnum.urls.split(";");
                    hasAuth = checkUrl(urls, urlToCheck);
                    if (hasAuth)
                        break;
                }
            }

            if (!hasAuth) {
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(NoPermissionCode);
            }
            //ZuulRequestHeader
            try{
                ctx.addZuulRequestHeader("Cookie", "SESSION=" + URLEncoder.encode(GsonUtil.toJson(requestHeader),"UTF-8"));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        log.info("access token ok");
        return null;
    }

    private String checkUrl() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String servletPath = request.getServletPath();
        return request.getMethod() + "-" + servletPath;
    }

    private Boolean checkUrl(String[] urls, String urlToCheck) {
        Boolean hasAuth = false;
        for (String url : urls) {
            if (url.endsWith("/")) {
                if (urlToCheck.startsWith(url)) {
                    hasAuth = true;
                }
            } else {
                if (urlToCheck.equals(url)) {
                    hasAuth = true;
                }
            }
        }
        return hasAuth;
    }

}