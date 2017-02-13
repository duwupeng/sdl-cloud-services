package com.talebase.cloud;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.login.dto.DLoginRequest;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhangchunlin on 2016-12-12.
 */
public class LoginTestCase {
    private int port = 29001;

    @Test
    public void 账号密码登录(){
        String servicePath = "http://localhost:" + port + "/oslogin/loginCheckForAccountPassword";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        DLoginRequest dLoginRequest = new DLoginRequest();
        dLoginRequest.setCallerFrom(0);
        dLoginRequest.setAccount("zhaokh");
        dLoginRequest.setCompanyId(1);
        dLoginRequest.setLoginType(1);
        dLoginRequest.setPassword("ArtoRU5pXn8h4j2ECnYiHQ==");
        ServiceResponse response = msInvoker.post(servicePath, dLoginRequest,new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        System.out.println("response:" + response.getResponse());

    }

    @Test
    public void 扫码登录(){
        String servicePath = "http://localhost:" + port + "/oslogin/loginCheckForScanCode";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        DLoginRequest dLoginRequest = new DLoginRequest();
        dLoginRequest.setCallerFrom(1);
        dLoginRequest.setAccount("aaa");
        dLoginRequest.setCompanyId(1);
        dLoginRequest.setLoginType(0);
        dLoginRequest.setPassword("ArtoRU5pXn8h4j2ECnYiHQ==");
        ServiceResponse response = msInvoker.post(servicePath, dLoginRequest,new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        System.out.println("response:" + response.getResponse());

    }

    @Test
    public void 退出登录(){
        String servicePath = "http://localhost:" + port + "/oslogin/loginOut";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, "474f2408-2ad3-440c-90ef-5281e5a54149",new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        System.out.println("response:" + response.getResponse());

    }

}
