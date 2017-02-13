package com.talebase.cloud.ms.consumption.controller;

import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.consume.dto.DAccountCondition;
import com.talebase.cloud.base.ms.consume.dto.DAccountConsumeResult;
import com.talebase.cloud.base.ms.consume.dto.DAccountPayResult;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.PageResponseWithParam;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.consumption.dao.AccountMapper;
import com.talebase.cloud.ms.consumption.service.ConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhangchunlin on 2017-1-12.
 */
@RestController
public class AccountController {

    @Autowired
    AccountMapper accountMapper;
    @Autowired
    ConsumeService consumeService;

    /**
     * 插入
     * @param serviceRequest
     * @return
     */
    @PutMapping(value = "/account/save")
    public ServiceResponse saveAccount(@RequestBody ServiceRequest<TAccount> serviceRequest){
        TAccount tAccount = serviceRequest.getRequest();
        accountMapper.save(tAccount);
        TAccountLine tAccountLine = new TAccountLine();
        tAccountLine.setPointVar(tAccount.getPointBalance());
        tAccountLine.setSmsVar(tAccount.getSmsBalance());
        tAccountLine.setType(2);
        tAccountLine.setModifier("system");
        tAccountLine.setRemark("公司初始化");
        consumeService.recharge(tAccount.getCompanyId(),tAccountLine);
        return new ServiceResponse();
    }
}
