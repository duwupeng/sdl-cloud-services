package com.talebase.cloud.os.login.controller;

import com.talebase.cloud.base.ms.login.dto.DLoginRequest;
import com.talebase.cloud.base.ms.login.dto.DLoginUserInfo;
import com.talebase.cloud.base.ms.login.enumes.LoginTypeEnume;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangchunlin on 2016-12-12.
 */
@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    @Autowired
    MsInvoker msInvoker;

    @Autowired
    RedisTemplate redisTemplate;

    final static String ADMIN_SERVICE_NAME = "ms-admin";

    /**
     * 账号密码登录校验
     * @param dLoginRequest
     * @return ServiceResponse<String>
     */
    @PostMapping(value="/loginCheckForAccountPassword")
    public ServiceResponse<DLoginUserInfo> loginCheckForAccountPassword(DLoginRequest dLoginRequest) throws Exception {
        ServiceHeader header = ServiceHeaderUtil.getRequestHeader();
        dLoginRequest.setCompanyId(header.getCompanyId());
        ServiceRequest<DLoginRequest> req = new ServiceRequest<DLoginRequest>();
        req.setRequest(dLoginRequest);
        //校验账号密码
        ServiceResponse<DLoginUserInfo> cresponse = loginService.checkAccountAndPassword(req);
        if(!cresponse.isBizError()){
            String token = UUID.randomUUID().toString();
            cresponse.getResponse().setToken(token);
            ServiceHeader serviceHeader = new ServiceHeader();
            if(req.getRequest().getLoginType().intValue() == LoginTypeEnume.ADMIN.getValue()){
                serviceHeader.setOperatorId(cresponse.getResponse().getId());
                serviceHeader.setOperatorName(cresponse.getResponse().getAccount());

                List<String> permissionCodes = loginService.findPermissionsByOperatorId(cresponse.getResponse().getId());
                cresponse.getResponse().setPermissionCodes(permissionCodes);
                serviceHeader.setPermissions(permissionCodes);
                serviceHeader.setOrgCode(cresponse.getResponse().getOrgCode());
            }else{
                serviceHeader.setCustomerId(cresponse.getResponse().getId());
                serviceHeader.setCustomerName(cresponse.getResponse().getAccount());
            }
            serviceHeader.setCallerIP(dLoginRequest.getCallerIP());
//            serviceHeader.setOrgCode("code");
            serviceHeader.setToken(token);
            serviceHeader.setSeqId(0);
            serviceHeader.setCompanyId(cresponse.getResponse().getCompanyId());
            serviceHeader.setCallerFrom(CallerFrom.findByValue(dLoginRequest.getCallerFrom()));
            serviceHeader.setAccount(cresponse.getResponse().getAccount());
            serviceHeader.setCompanyDomain(header.getCompanyDomain());

            //get the permissions from admindb
            /*java.lang.String servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/permissions/operator/"+cresponse.getResponse().getId();
            ServiceResponse<List<String>> presponse = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<String>>>(){},new Object());
            serviceHeader.setPermissions(presponse.getResponse());*/

            //save it to redis
            String tokenKey = "token_" + token;
            redisTemplate.opsForValue().set(tokenKey,serviceHeader);
            redisTemplate.expire(tokenKey,120, TimeUnit.MINUTES);
        }
        return cresponse;
    }

    /**
     * 退出登录
     * @param token
     * @return ServiceResponse<String>
     */
    @PostMapping(value="/loginOut")
    public ServiceResponse<Boolean> loginOut(String token) throws InvocationTargetException, IllegalAccessException {
        ServiceResponse<Boolean> response = new ServiceResponse<Boolean>();
        redisTemplate.delete(token);
        response.setResponse(true);
        return response;
    }

    /**
     * 扫码登录校验
     * @param dLoginRequest
     */
    @PostMapping(value="/loginCheckForScanCode")
    public ServiceResponse<DLoginUserInfo> loginCheckForScanCode(DLoginRequest dLoginRequest) throws Exception {
        ServiceResponse<DLoginUserInfo> response = loginService.loginCheckForScanCode(dLoginRequest);
        return response;
    }

    /**
     * 扫码时校验二维码是否可用
     * @param projectId
     */
    @PostMapping(value="/checkScanCode")
    public ServiceResponse checkScanCode(Integer projectId) throws Exception {
        loginService.checkScanCode(projectId,"0");
        return new ServiceResponse();
    }
}
