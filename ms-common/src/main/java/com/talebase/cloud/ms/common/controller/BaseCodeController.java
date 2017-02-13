package com.talebase.cloud.ms.common.controller;

import com.talebase.cloud.base.ms.common.domain.TCode;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.common.service.BaseCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by daorong.li on 2016-12-1.
 * 字典表
 */
@RestController
public class BaseCodeController {

    @Autowired
    private BaseCodeService baseCodeService ;

    @GetMapping(value = "/common/type")
    public ServiceResponse<List<TCode>> getCodeByType(String type){
        return baseCodeService.getCodeByType(type);
    }


}
