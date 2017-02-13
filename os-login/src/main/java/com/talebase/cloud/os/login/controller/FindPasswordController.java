package com.talebase.cloud.os.login.controller;

import com.netflix.discovery.converters.Auto;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.os.login.service.FindPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by suntree.xu on 2016-12-15.
 */
@RestController
public class FindPasswordController {

    @Autowired
    FindPassService findPassService;

    @PostMapping("/login/findpass/{email}/{account}")
    public ServiceResponse findPass(@PathVariable("account") String account,@PathVariable("email") String email ) throws Exception {
        return findPassService.findPass(account,email);
    }



}
