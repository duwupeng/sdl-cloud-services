package com.talebase.cloud.ms.notify.controller;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.PermissionEnum;
import com.talebase.cloud.ms.notify.service.MailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-24.
 */
@RestController
public class MailTemplateController {

    @Autowired
    MailTemplateService mailTemplateService;

    @PostMapping({"/mailTemplates"})
    public ServiceResponse<PageResponse<TNotifyTemplate>> getTemplates(@RequestBody ServiceRequest req) {
        ServiceHeader requestHeader = req.getRequestHeader();
        PageRequest pageReq = req.getPageReq();
        PageResponse<List<TNotifyTemplate>> page = new PageResponse<List<TNotifyTemplate>>();
        List list = mailTemplateService.getAll(requestHeader, pageReq);
        Integer count = mailTemplateService.getCount(requestHeader);
        page.setResults(list);
        page.setPageIndex(pageReq.getPageIndex());
        page.setTotal(count);
        page.setLimit(pageReq.getLimit());
        return new ServiceResponse(page);
    }

    @PostMapping({"/mailTemplate/check"})
    public ServiceResponse checkName(@RequestBody ServiceRequest<DNotifyTemplate> req) {
        Integer cont = mailTemplateService.checkName(req.getRequestHeader(), req.getRequest());
        return new ServiceResponse(cont);
    }

    @PostMapping({"/mailTemplate/list"})//发送通知的模板选择列表
    public ServiceResponse<List<TNotifyTemplate>> getMails(@RequestBody ServiceRequest req) {
        List list = mailTemplateService.getAll(req.getRequestHeader().getCompanyId(), req.getRequestHeader().getOperatorName());
        return new ServiceResponse(list);
    }

    @PutMapping({"/mailTemplate"})
    public ServiceResponse update(@RequestBody ServiceRequest<DNotifyTemplate> req) {
        TNotifyTemplate tNotifyTemplate = mailTemplateService.getTemplateById(req.getRequest().getId());
        if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_1.name()) {
            if (!tNotifyTemplate.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_TEMPLATE, true);
            }
        }
        mailTemplateService.update(req.getRequestHeader().getOperatorName(), req.getRequest());
        if (req.getRequest().isWhetherDefault()) {
            mailTemplateService.updateDefault(req.getRequestHeader().getCompanyId(), req.getRequest().getId());
        }
        return new ServiceResponse();
    }

    @PutMapping({"/mailTemplate/status/{id}"})
    public ServiceResponse updateStatus(@PathVariable("id") Integer id, @RequestBody ServiceRequest<Boolean> req) {
        TNotifyTemplate tNotifyTemplate = mailTemplateService.getTemplateById(id);
        if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_1.name()) {
            if (!tNotifyTemplate.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_TEMPLATE, true);
            }
        }
        mailTemplateService.updateStatus(req.getRequestHeader().getOperatorName(), id, req.getRequest());
        return new ServiceResponse();
    }

    @DeleteMapping({"/mailTemplate"})
    public ServiceResponse delete(@RequestBody ServiceRequest<List<Integer>> req) {
        for (Integer id : req.getRequest()) {
            TNotifyTemplate tNotifyTemplate = mailTemplateService.getTemplateById(id);
            if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_1.name()) {
                if (!tNotifyTemplate.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                    return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_TEMPLATE, true);
                }
            }
        }
        mailTemplateService.delete(req.getRequestHeader().getOperatorName(), req.getRequest());
        return new ServiceResponse();
    }

    @PostMapping({"/mailTemplate"})
    public ServiceResponse create(@RequestBody ServiceRequest<DNotifyTemplate> req) {
        Integer id = mailTemplateService.add(req.getRequestHeader(), req.getRequest());
        if (req.getRequest().isWhetherDefault()) {
            mailTemplateService.updateDefault(req.getRequestHeader().getCompanyId(), id);
        }
        return new ServiceResponse();
    }
}
