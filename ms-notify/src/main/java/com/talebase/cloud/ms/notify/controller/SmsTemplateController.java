package com.talebase.cloud.ms.notify.controller;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.PermissionEnum;
import com.talebase.cloud.ms.notify.service.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by xia.li on 2016-11-28.
 */
@RestController
public class SmsTemplateController {
    @Autowired
    SmsTemplateService smsTemplateService;

    @PostMapping({"/smsTemplates"}) //查询列表
    public ServiceResponse<PageResponse<TNotifyTemplate>> getTemplates(@RequestBody ServiceRequest req) {
        ServiceHeader serviceHeader = req.getRequestHeader();
        PageRequest pageReq = req.getPageReq();
        PageResponse<List<TNotifyTemplate>> page = new PageResponse<List<TNotifyTemplate>>();
        List list = smsTemplateService.getAll(serviceHeader, pageReq);
        Integer count = smsTemplateService.getCount(serviceHeader, pageReq);
        page.setResults(list);
        page.setStart(pageReq.getStart());
        page.setTotal(count);
        page.setLimit(pageReq.getLimit());
        return new ServiceResponse(page);
    }

    @PostMapping({"/smsTemplate/initializationTemplates"})
    public ServiceResponse<Integer> initializationTemplates(@RequestBody ServiceRequest<DNotifyTemplate> req) {
        List<TNotifyTemplate> smsList = smsTemplateService.querySystemTemplates(0, "system");
        if(smsList != null && smsList.size() > 0){
            for(TNotifyTemplate template : smsList){
                template.setId(null);
                template.setCompanyId(req.getRequest().getCompanyId());
                template.setCreator(req.getRequest().getCreator());
                smsTemplateService.initializationTemplates(req.getRequestHeader(), template);
            }
        }
        return new ServiceResponse();
    }

    @PostMapping({"/smsTemplate/check"}) //检查模板名
    public ServiceResponse<DNotifyTemplate> checkName(@RequestBody ServiceRequest<DNotifyTemplate> req) {
        Integer cont = smsTemplateService.checkName(req.getRequestHeader(), req.getRequest());
        return new ServiceResponse(cont);
    }

    @PostMapping({"/smsTemplate/list"}) //发送通知的模板选择列表
    public ServiceResponse<DNotifyTemplate> getSmss(@RequestBody ServiceRequest req) {
        List list = smsTemplateService.getTemplates(req.getRequestHeader().getCompanyId(), req.getRequestHeader().getOperatorName());
        return new ServiceResponse(list);
    }

    @PutMapping({"/smsTemplate"})//修改模板
    public ServiceResponse update(@RequestBody ServiceRequest<DNotifyTemplate> req) {
        TNotifyTemplate tNotifyTemplate = smsTemplateService.getTemplateById(req.getRequest().getId());
        if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_1.name()) {
            if (!tNotifyTemplate.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_TEMPLATE, true);
            }
        }
        smsTemplateService.update(req.getRequestHeader().getOperatorName(), req.getRequest());
        if (req.getRequest().isWhetherDefault()) {
            smsTemplateService.updateDefault(req.getRequestHeader().getCompanyId(), req.getRequest().getId());
        }
        return new ServiceResponse();
    }

    @PutMapping({"/smsTemplate/status/{id}"})//修改状态
    public ServiceResponse updateStatus(@PathVariable("id") Integer id, @RequestBody ServiceRequest<Boolean> req) {
        TNotifyTemplate tNotifyTemplate = smsTemplateService.getTemplateById(id);
        if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_1.name()) {
            if (!tNotifyTemplate.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_TEMPLATE, true);
            }
        }
        smsTemplateService.updateStatus(req.getRequestHeader().getOperatorName(), id, req.getRequest());
        return new ServiceResponse();
    }

    @DeleteMapping({"/smsTemplate"})//删除模板
    public ServiceResponse delete(@RequestBody ServiceRequest<List<Integer>> req) {
        for (Integer id : req.getRequest()) {
            TNotifyTemplate tNotifyTemplate = smsTemplateService.getTemplateById(id);
            if (req.getRequestHeader().getOrgCode() != PermissionEnum.c99_1.name()) {
                if (!tNotifyTemplate.getCreator().equals(req.getRequestHeader().getOperatorName())) {
                    return new ServiceResponse(BizEnums.NO_PERMISSION_OPERATION_TEMPLATE, true);
                }
            }
        }
        smsTemplateService.delete(req.getRequestHeader().getOperatorName(), req.getRequest());
        return new ServiceResponse();
    }

    @PostMapping({"/smsTemplate"})//创建模板
    public ServiceResponse create(@RequestBody ServiceRequest<DNotifyTemplate> req) {
        Integer id = smsTemplateService.add(req.getRequestHeader(), req.getRequest());
        if (req.getRequest().isWhetherDefault()) {
            smsTemplateService.updateDefault(req.getRequestHeader().getCompanyId(), id);
        }
        return new ServiceResponse();
    }
}
