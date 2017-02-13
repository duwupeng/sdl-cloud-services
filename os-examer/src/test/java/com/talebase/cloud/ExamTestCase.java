package com.talebase.cloud;

import com.talebase.cloud.base.ms.project.dto.DTTaskEx;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

/**
 * Created by zhangchunlin on 2017-1-21.
 */
public class ExamTestCase {
    private int port = 27002;

    public static void main(String arg[]){
        System.out.println();
    }


    @Test
    public void 修改考生资料(){
        String servicePath = "http://localhost:" + port + "/osexamer/examer/modifyUserForPerfect";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath,"",new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        System.out.println("response:" + response.getResponse());

    }

    @Test
    public void 考生修改密码(){
        String servicePath = "http://localhost:" + port + "/osexamer/examer/updateUserPassword";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath,"",new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        System.out.println("response:" + response.getResponse());

    }

}
