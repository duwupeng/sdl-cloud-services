package com.talebase.cloud.gateway.controllers;

import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Created by eric.du on 2016-12-1.
 */
@RestController
public class AuthController {

    @Autowired
    RedisTemplate redisTemplate;

//    @Autowired
//    MsInvoker msInvoker;

    final static java.lang.String  SERVICE_NAME = "ms-admin";

    @PostMapping(value = "/auth")
    public ServiceResponse auth (String  token,Integer operatorId,String buziCode){
//        String token ="token-1";
       //query user information
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCallerIP("111");
        serviceHeader.setCustomerId(121);
        serviceHeader.setOrgCode("code");
        serviceHeader.setTransactionId(UUID.randomUUID().toString());
        serviceHeader.setSeqId(0);

        //get the permissions from admindb
        java.lang.String servicePath = "http://" + SERVICE_NAME + "/serviceAdmin/permissions/operator/"+operatorId;
//        ServiceResponse<List<String>> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<String>>>(){},new Object());
//        serviceHeader.setPermissions(response.getResponse());
//
//        //save it to redis
//        redisTemplate.opsForValue().set(token, GsonUtil.toJson(serviceHeader));

        //return final result
        return new ServiceResponse<String>(token);
    }
}
