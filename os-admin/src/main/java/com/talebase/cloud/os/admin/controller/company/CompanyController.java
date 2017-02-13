package com.talebase.cloud.os.admin.controller.company;

import com.talebase.cloud.base.ms.admin.dto.*;
import com.talebase.cloud.base.ms.common.domain.TCode;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.examer.dto.DReSetPassword;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.PasswordUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.os.admin.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by rong on 2016/11/27.
 * 企业管理
 */
@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    /**
     * 通过id获取公司
     * @return
     */
    @GetMapping(value = "/company")
    public ServiceResponse<DCompanyAndIndustry> getCompany(){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        List<TCode> codeList = companyService.getIndustry();
        DCompany dCompany = companyService.getCompany(serviceHeader.getCompanyId());
        DCompanyAndIndustry obj = new DCompanyAndIndustry(dCompany,codeList);
        ServiceResponse<DCompanyAndIndustry> response = new ServiceResponse<DCompanyAndIndustry>(obj);
        return response;
    }

    /**
     * 管理员自己修改密码修改密码
     * @param admin
     * @return
     */
    @PutMapping(value = "/company")
    public ServiceResponse<DAdmin> reSetPassword(DAdmin admin){
        if(StringUtils.isEmpty(admin.getPassword())){
            throw new WrappedException(BizEnums.ADMIN_PASSWORD_NOEXIST);
        }
        if(StringUtils.isEmpty(admin.getOldPassword())){
            throw new WrappedException(BizEnums.ADMIN_OLDPASSWORD_NOEXIST);
        }
        if (admin.getPassword().length() <6){
            throw new WrappedException(BizEnums.Password_TooShort);
        }
        if (!PasswordUtil.containLetterAndNumber(admin.getPassword())){
            throw new WrappedException(BizEnums.Password_NotVail);
        }
        if (PasswordUtil.containChinese(admin.getPassword())){
            throw new WrappedException(BizEnums.Password_CannotHas_Chinese);
        }

        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        admin.setId(serviceHeader.getOperatorId());
        admin.setModifier(serviceHeader.getOperatorName());
        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(admin);
        return companyService.editAdminPassword(req);
    }


    @GetMapping(value = "/company/testAop/{companyId}")
    public ServiceResponse getCompanys( @PathVariable("companyId") Integer id){
        return companyService.getAdmins(id);
    }

    @PostMapping(value = "/company/initializationCompany")
    public ServiceResponse initializationCompany(DCompany dCompany, DcjAdmin cjAdmin, DbzAdmin bzDAdmin, TAccount tAccount){
        ServiceRequest<DCompany> req =new ServiceRequest<>();
        req.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        req.setRequest(dCompany);
        return companyService.initializationCompany(req,cjAdmin,bzDAdmin,tAccount);
    }
}
