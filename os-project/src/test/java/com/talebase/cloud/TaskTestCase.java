package com.talebase.cloud;

import com.talebase.cloud.base.ms.project.dto.DTTaskEx;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zhangchunlin on 2017-1-21.
 */
public class TaskTestCase {
    private int port = 27005;

    @Test
    public void 查询任务信息和考生信息(){
        String servicePath = "http://localhost:" + port + "/osproject/project/task/findTaskExAndDataManagement";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<DTTaskEx>>() {
        });
        System.out.println("response:" + response.getResponse());

    }

}
