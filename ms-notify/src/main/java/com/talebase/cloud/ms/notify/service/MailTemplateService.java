package com.talebase.cloud.ms.notify.service;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.ms.notify.dao.MailTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-24.
 */

@Service
@Transactional(readOnly = true)
public class MailTemplateService {

    @Autowired
    MailTemplateMapper mailTemplateMapper;

    public List<TNotifyTemplate> getAll(ServiceHeader serviceHeader, PageRequest pageReq) {
        List<TNotifyTemplate> tNotifyTemplates = mailTemplateMapper.getAll(serviceHeader,pageReq,TNotifyTemplateMethod.MAIL.getValue());
        return tNotifyTemplates;
    }

    public Integer getCount(ServiceHeader serviceHeader) {
        return mailTemplateMapper.getCount(serviceHeader,TNotifyTemplateMethod.MAIL.getValue());
    }

    public List<TNotifyTemplate> getAll(Integer companyId, String creator) {
        return mailTemplateMapper.getTemplates(companyId, creator, TNotifyTemplateMethod.MAIL.getValue());
    }

    public Integer checkName(ServiceHeader serviceHeader,DNotifyTemplate dNotifyTemplate) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setId(dNotifyTemplate.getId());
        tNotifyTemplate.setCompanyId(serviceHeader.getCompanyId());
        tNotifyTemplate.setName(dNotifyTemplate.getName());
        tNotifyTemplate.setMethod(TNotifyTemplateMethod.MAIL.getValue());
        return mailTemplateMapper.getTemplateByName(tNotifyTemplate);
    }

    @Transactional(readOnly = false)
    public Integer add(ServiceHeader serviceHeader, DNotifyTemplate dNotifyTemplate) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setCompanyId(serviceHeader.getCompanyId());
        tNotifyTemplate.setName(dNotifyTemplate.getName());
        tNotifyTemplate.setContent(dNotifyTemplate.getContent());
        tNotifyTemplate.setSubject(dNotifyTemplate.getSubject());
        tNotifyTemplate.setSign(dNotifyTemplate.getSign());
        tNotifyTemplate.setMethod(TNotifyTemplateMethod.MAIL.getValue());
        if (dNotifyTemplate.isWhetherDefault()) {
            tNotifyTemplate.setWhetherDefault(1);
        } else {
            tNotifyTemplate.setWhetherDefault(0);
        }
        tNotifyTemplate.setCreator(serviceHeader.getOperatorName());
        mailTemplateMapper.insert(tNotifyTemplate);
        return tNotifyTemplate.getId();
    }

    @Transactional(readOnly = false)
    public Integer update(String operatorName, DNotifyTemplate dNotifyTemplate) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setId(dNotifyTemplate.getId());
        tNotifyTemplate.setName(dNotifyTemplate.getName());
        tNotifyTemplate.setContent(dNotifyTemplate.getContent());
        tNotifyTemplate.setSubject(dNotifyTemplate.getSubject());
        tNotifyTemplate.setSign(dNotifyTemplate.getSign());
        if (dNotifyTemplate.isWhetherDefault()) {
            tNotifyTemplate.setWhetherDefault(1);
        } else {
            tNotifyTemplate.setWhetherDefault(0);
        }
        tNotifyTemplate.setModifier(operatorName);

            return mailTemplateMapper.updateEmail(tNotifyTemplate);
    }

    @Transactional(readOnly = false)
    public Integer updateStatus(String operatorName, Integer id, Boolean status) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setId(id);
        if (status) {
            tNotifyTemplate.setStatus(1);
        } else {
            tNotifyTemplate.setStatus(0);
        }
        tNotifyTemplate.setModifier(operatorName);
        return mailTemplateMapper.updateStatus(tNotifyTemplate);
    }

    @Transactional(readOnly = false)
    public Integer delete(String operatorName, List<Integer> id) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setModifier(operatorName);
        return mailTemplateMapper.delete(operatorName, id);
    }

    public TNotifyTemplate getTemplateById(Integer id) {
        return mailTemplateMapper.getTemplateById(id);
    }

    @Transactional(readOnly = false)
    public Integer updateDefault(Integer companyId, Integer id) {
        return mailTemplateMapper.updateDefault(companyId, id, TNotifyTemplateMethod.MAIL.getValue());
    }
}