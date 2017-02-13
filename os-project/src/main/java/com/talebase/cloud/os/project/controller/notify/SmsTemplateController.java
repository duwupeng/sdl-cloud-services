package com.talebase.cloud.os.project.controller.notify;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.os.project.service.notify.SmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xia.li on 2016-11-29.
 */
@RestController
public class SmsTemplateController {
    @Autowired
    SmsTemplateService smsTemplateService;

    @PostMapping({"/smsTemplates"})
    public ServiceResponse<PageResponse<TNotifyTemplate>> getTemplates(PageRequest pageRequest) {
        return smsTemplateService.getTemplates(pageRequest);
    }

    @PostMapping({"/smsTemplate/check"})
    public ServiceResponse getTemplateName(DNotifyTemplate dNotifyTemplate) {
        return smsTemplateService.getTemplateByName(dNotifyTemplate);
    }

    @PostMapping({"/smsTemplate"})
    public ServiceResponse add(DNotifyTemplate dNotifyTemplate) {
        if (StringUtil.isEmpty(dNotifyTemplate.getContent())) {
            return new ServiceResponse(BizEnums.NO_TEMPLATE_CONTENT, true);
        }
        if (StringUtil.isEmpty(dNotifyTemplate.getName())) {
            return new ServiceResponse(BizEnums.NO_TEMPLATE_NAME, true);
        }
        return smsTemplateService.add(dNotifyTemplate);
    }

    @PutMapping("/smsTemplate")
    public ServiceResponse update(DNotifyTemplate dNotifyTemplate) {
        if (StringUtil.isEmpty(dNotifyTemplate.getContent())) {
            return new ServiceResponse(BizEnums.NO_TEMPLATE_CONTENT, true);
        }
        if (StringUtil.isEmpty(dNotifyTemplate.getName())) {
            return new ServiceResponse(BizEnums.NO_TEMPLATE_NAME, true);
        }
        return smsTemplateService.update(dNotifyTemplate);
    }

    @PutMapping("/smsTemplate/status/{id}")
    public ServiceResponse updateStatus(@PathVariable("id") Integer id, Boolean status) {
        return smsTemplateService.updateStatus(id, status);
    }

    @PostMapping("/smsTemplate/del")
    public ServiceResponse delete(String id) {
        return smsTemplateService.delete(id);
    }
}
