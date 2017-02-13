package com.talebase.cloud.os.project.controller.notify;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.CommonParams;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.os.project.service.notify.MailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bin.yang on 2016-11-24.
 */

@RestController
public class MailTemplateController {
    @Autowired
    MailTemplateService mailTemplateService;

    @PostMapping({"/mailTemplates"})
    public ServiceResponse<PageResponse<TNotifyTemplate>> getTemplates(PageRequest pageRequest) {
        return mailTemplateService.getTemplates(pageRequest);
    }

    @PostMapping({"/mailTemplate/check"})
    public ServiceResponse getTemplateName(DNotifyTemplate dNotifyTemplate) {
        ServiceResponse serviceResponse = mailTemplateService.getTemplateByName(dNotifyTemplate);
        return serviceResponse;
    }

    @PostMapping({"/mailTemplate"})
    public ServiceResponse create(DNotifyTemplate dNotifyTemplate) {
        if(StringUtil.isEmpty(dNotifyTemplate.getContent())){
            return new ServiceResponse(BizEnums.NO_TEMPLATE_CONTENT,true);
        }
        if(StringUtil.isEmpty(dNotifyTemplate.getName())){
            return new ServiceResponse(BizEnums.NO_TEMPLATE_NAME,true);
        }
        if(StringUtil.isEmpty(dNotifyTemplate.getSubject())){
            return new ServiceResponse(BizEnums.NO_TEMPLATE_SUBJECT,true);
        }

        if (!StringUtils.isEmpty(dNotifyTemplate.getContent())){
            int len = dNotifyTemplate.getContent().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.EMAIL_TEXT_BEYOUND_LENGTH);
            }
        }
        if (!StringUtils.isEmpty(dNotifyTemplate.getSign())){
            int len = dNotifyTemplate.getSign().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.SIGN_TEXT_BEYOUND_LENGTH);
            }
        }
        return mailTemplateService.add(dNotifyTemplate);
    }

    @PutMapping("/mailTemplate")
    public ServiceResponse update(DNotifyTemplate dNotifyTemplate) {
        if(StringUtil.isEmpty(dNotifyTemplate.getContent())){
            return new ServiceResponse(BizEnums.NO_TEMPLATE_CONTENT,true);
        }
        if(StringUtil.isEmpty(dNotifyTemplate.getName())){
            return new ServiceResponse(BizEnums.NO_TEMPLATE_NAME,true);
        }
        if(StringUtil.isEmpty(dNotifyTemplate.getSubject())){
            return new ServiceResponse(BizEnums.NO_TEMPLATE_SUBJECT,true);
        }
        if (!StringUtils.isEmpty(dNotifyTemplate.getContent())){
            int len = dNotifyTemplate.getContent().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.EMAIL_TEXT_BEYOUND_LENGTH);
            }
        }
        if (!StringUtils.isEmpty(dNotifyTemplate.getSign())){
            int len = dNotifyTemplate.getSign().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.SIGN_TEXT_BEYOUND_LENGTH);
            }
        }
        return mailTemplateService.update(dNotifyTemplate);
    }

    @PutMapping("/mailTemplate/status/{id}")
    public ServiceResponse updateStatus(@PathVariable("id") Integer id,boolean status) {
        return mailTemplateService.updateStatus(id,status);
    }

    @PostMapping("/mailTemplate/del")
    public ServiceResponse delete(String id) {
        return mailTemplateService.delete(id);
    }
}
