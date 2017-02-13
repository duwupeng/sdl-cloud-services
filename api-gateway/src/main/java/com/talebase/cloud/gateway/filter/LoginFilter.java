package com.talebase.cloud.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.talebase.cloud.common.protocal.ServiceHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangchunlin on 16/12/15.
 */
public class LoginFilter extends ZuulFilter {

    @Autowired
    RedisTemplate redisTemplate;

    private static Logger log = LoggerFactory.getLogger(LoginFilter.class);

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

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
        String account = request.getParameter("account");
        String accessToken = request.getParameter("accessToken");
        //Gson gson = new Gson();
        //ServiceHeader serviceHeader = gson.fromJson(jsonStr,ServiceHeader.class);
        String[] paths = request.getRequestURL().toString().split("/");
        ServiceHeader rserviceHeader = (ServiceHeader)redisTemplate.opsForValue().get(account+"_header");
        if((paths.length > 3 && !"oslogin".equals(paths[3])) && (account == null
                || accessToken == null|| !redisTemplate.hasKey(account+"_header")) || !rserviceHeader.getToken().equals(accessToken)){
            log.info("未登录或者登录超时");
            return "未登录或者登录超时";
        }
        log.info("access token ok");
        return null;
    }

}