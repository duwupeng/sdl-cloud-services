package com.talebase.cloud.ms.company.controller;

import com.talebase.cloud.base.ms.admin.domain.TCompany;
import com.talebase.cloud.base.ms.admin.dto.DCompany;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.company.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rong on 2016/11/27.
 */
@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping(value = "/serviceCompany/{companyId}")
    public ServiceResponse<DCompany> getCompany(@PathVariable("companyId") Integer companyId){
        DCompany company = companyService.getCompany(companyId);
        ServiceResponse<DCompany> response = new ServiceResponse<DCompany>(company);
        return response;
    }

    @PutMapping(value = "/serviceCompany/updateLogo")
    public ServiceResponse<DCompany> upComanyLogo(@RequestBody ServiceRequest<DCompany> request){
        DCompany company = request.getRequest();
        //获取旧的logo并传给os删除
        DCompany old = companyService.getCompany(company.getId());
        companyService.updateCompanyLogo(company);
        return new ServiceResponse<DCompany>(old);
    }

    @PutMapping(value = "/serviceCompany/insert")
    public ServiceResponse<Integer> insert(@RequestBody ServiceRequest<TCompany> request){
        return new ServiceResponse<Integer>(companyService.insert(request.getRequest()));
    }

    
}
