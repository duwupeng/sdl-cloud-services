package com.talebase.cloud.os.examer.controller;

import com.talebase.cloud.common.protocal.ServiceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bin.yang on 2016-12-20.
 */
@RestController
public class DataManagementController {

    @GetMapping("/examer/datas")
    public ServiceResponse getDatas(){

        return new ServiceResponse();
    }
}
