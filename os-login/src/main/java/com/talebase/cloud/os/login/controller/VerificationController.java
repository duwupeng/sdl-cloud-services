package com.talebase.cloud.os.login.controller;

import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.os.login.bind.ImageInfo;
import com.talebase.cloud.os.login.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by suntree.xu on 2016-12-16.
 */

@RestController
public class VerificationController {

    @Autowired
    VerificationService verificationService;

    @PostMapping("/verification/send/{phoneNo}")
    public ServiceResponse sendVerifyCode(@PathVariable("phoneNo") String phoneNo){
        return verificationService.sendVerifyCode(phoneNo);
    }

    @GetMapping("/verification/getValidateCode")
    public ServiceResponse<ImageInfo> getValidateCode(){
        return verificationService.getValidateCode();
    }

}
