package com.talebase.cloud.ms.notify.service;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.ms.notify.dao.SmsTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by xia.li on 2016-11-28.
 */
@Service
@Transactional(readOnly = true)
public class SmsTemplateService {
    @Autowired
    SmsTemplateMapper smsTemplateMapper;

    public SmsTemplateService() {
    }

    public List<TNotifyTemplate> getAll(ServiceHeader serviceHeader, PageRequest pageReq) {
        return smsTemplateMapper.getAll(serviceHeader, pageReq, TNotifyTemplateMethod.SMS.getValue());
    }

    public Integer getCount(ServiceHeader serviceHeader, PageRequest pageReq) {
        return smsTemplateMapper.getCount(serviceHeader,TNotifyTemplateMethod.SMS.getValue());
    }

    public List<TNotifyTemplate> getTemplates(Integer companyId, String creator) {
        return smsTemplateMapper.getTemplates(companyId, creator, TNotifyTemplateMethod.SMS.getValue());
    }

    public List<TNotifyTemplate> querySystemTemplates(Integer companyId, String creator) {
        return smsTemplateMapper.querySystemTemplates(companyId, creator);
    }

    public Integer checkName(ServiceHeader serviceHeader,DNotifyTemplate dNotifyTemplate) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setId(dNotifyTemplate.getId());
        tNotifyTemplate.setCompanyId(serviceHeader.getCompanyId());
        tNotifyTemplate.setName(dNotifyTemplate.getName());
        tNotifyTemplate.setMethod(TNotifyTemplateMethod.SMS.getValue());
        return smsTemplateMapper.getTemplateByName(tNotifyTemplate);
    }

    @Transactional(readOnly = false)
    public Integer add(ServiceHeader requestHeader, DNotifyTemplate dNotifyTemplate) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setCompanyId(dNotifyTemplate.getCompanyId() != null? dNotifyTemplate.getCompanyId():requestHeader.getCompanyId());
        tNotifyTemplate.setName(dNotifyTemplate.getName());
        tNotifyTemplate.setContent(dNotifyTemplate.getContent());
        tNotifyTemplate.setMethod(TNotifyTemplateMethod.SMS.getValue());
        if (dNotifyTemplate.isWhetherDefault()) {
            tNotifyTemplate.setWhetherDefault(1);
        }else{
            tNotifyTemplate.setWhetherDefault(0);
        }
        tNotifyTemplate.setCreator(dNotifyTemplate.getCreator() != null?dNotifyTemplate.getCreator():requestHeader.getOperatorName());
        tNotifyTemplate.setModifier(dNotifyTemplate.getModifier());
        smsTemplateMapper.insert(tNotifyTemplate);
        return tNotifyTemplate.getId();
    }

    @Transactional(readOnly = false)
    public Integer initializationTemplates(ServiceHeader requestHeader, TNotifyTemplate tNotifyTemplate) {
        smsTemplateMapper.insert(tNotifyTemplate);
        return tNotifyTemplate.getId();
    }

    @Transactional(readOnly = false)
    public Integer update(String operatorName, DNotifyTemplate dNotifyTemplate) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setId(dNotifyTemplate.getId());
        tNotifyTemplate.setName(dNotifyTemplate.getName());
        tNotifyTemplate.setContent(dNotifyTemplate.getContent());
        if (dNotifyTemplate.isWhetherDefault()) {
            tNotifyTemplate.setWhetherDefault(1);
        }else{
            tNotifyTemplate.setWhetherDefault(0);
        }
        tNotifyTemplate.setModifier(operatorName);
        return smsTemplateMapper.updateSms(tNotifyTemplate);
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
        return smsTemplateMapper.updateStatus(tNotifyTemplate);
    }

    @Transactional(readOnly = false)
    public Integer delete(String operatorName, List<Integer> id) {
        TNotifyTemplate tNotifyTemplate = new TNotifyTemplate();
        tNotifyTemplate.setModifier(operatorName);
        return smsTemplateMapper.delete(operatorName, id);
    }

    public TNotifyTemplate getTemplateById(Integer id) {
        return smsTemplateMapper.getTemplateById(id);
    }

    @Transactional(readOnly = false)
    public Integer updateDefault(Integer companyId, Integer id) {
        return smsTemplateMapper.updateDefault(companyId, id, TNotifyTemplateMethod.SMS.getValue());
    }
}
