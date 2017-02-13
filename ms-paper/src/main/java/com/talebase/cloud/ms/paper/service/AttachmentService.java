package com.talebase.cloud.ms.paper.service;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.domain.TAttachment;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.paper.dao.AttachmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du  on 2016-11-24.
 */

@Service
@Transactional(readOnly = true)
public class AttachmentService {
    @Autowired
    private AttachmentMapper attachmentMapper;

    @Transactional(readOnly = false)
    public DQuestionCount save(ServiceHeader serviceHeader, List<DAttachment> dAttachments) throws InvocationTargetException, IllegalAccessException {
        List<DUnicode> list = new ArrayList<>();
        DQuestionCount dQuestionCount = new DQuestionCount();
        BigDecimal totalScore = new BigDecimal(0);
        TAttachment tAttachment;
        TAttachment tAttachmentSaved;
        DUnicode dUnicode;
        for (DAttachment dAttachment : dAttachments) {
//            Integer version = attachmentMapper.queryByUnicode(dAttachment.getUnicode(), dAttachment.getVersionType());
            tAttachmentSaved = attachmentMapper.queryLatestByUnicode(dAttachment.getUnicode());
            tAttachment = changeDAttachmentToTAttachment(dAttachment);
            Integer version;
            totalScore = totalScore.add(tAttachment.getScore());
            dUnicode = new DUnicode();
            dUnicode.setUnicode(tAttachment.getUnicode());
            if (tAttachmentSaved == null) {
                version = 1;
            } else {
                if (tAttachmentSaved.equals(tAttachment)) {
                    dUnicode.setId(tAttachmentSaved.getId());
                    list.add(dUnicode);
                    continue;
                } else {
                    version = tAttachmentSaved.getVersion() + 1;
                }
            }
            tAttachment.setVersion(version);
            tAttachment.setCreator(serviceHeader.getOperatorName());
            attachmentMapper.insert(tAttachment);
            dQuestionCount.setHasChange(true);
            dUnicode.setId(tAttachment.getId());
            list.add(dUnicode);
        }
        dQuestionCount.setTotalCount(dAttachments.size());
        dQuestionCount.setUnicodeList(list);
        dQuestionCount.setTotalScore(totalScore);
        dQuestionCount.setSubjectiveCount(dAttachments.size());
        return dQuestionCount;
    }

    public ServiceResponse<List<DAttachment>> query(String unicodes) throws Exception {
        List<String> params = StringUtil.toStrListByComma(unicodes);
        List<TAttachment> tAttachments = attachmentMapper.query(params);
        List<DAttachment> dAttachments = new ArrayList<>();
        for (TAttachment tAttachment : tAttachments) {
            DAttachment dAttachment = changeTAttamchmentToDAttachment(tAttachment);
            dAttachments.add(dAttachment);
        }
        return new ServiceResponse(dAttachments);
    }

    public DAttachment queryByUnicode(String unicode) throws InvocationTargetException, IllegalAccessException {
        TAttachment tAttachment = attachmentMapper.queryLatestByUnicode(unicode);
        DAttachment dAttachment = changeTAttamchmentToDAttachment(tAttachment);
        return dAttachment;
    }

    public DAttachment queryById(Integer id) throws InvocationTargetException, IllegalAccessException {
        TAttachment tAttachment = attachmentMapper.queryById(id);
        DAttachment dAttachment = changeTAttamchmentToDAttachment(tAttachment);
        return dAttachment;
    }

    @Transactional(readOnly = false)
    public ServiceResponse update(ServiceHeader serviceHeader, DAttachment dAttachment) throws InvocationTargetException, IllegalAccessException {
        TAttachment tAttachment = changeDAttachmentToTAttachment(dAttachment);
        Integer count = attachmentMapper.update(tAttachment);
        return new ServiceResponse(count);
    }

    private TAttachment changeDAttachmentToTAttachment(DAttachment dAttachment) throws InvocationTargetException, IllegalAccessException {
        TAttachment tAttachment = new TAttachment();
//        BeanConverter.copyProperties(tAttachment, dAttachment);
        tAttachment.setId(dAttachment.getId());
        tAttachment.setCreator(dAttachment.getCreator());
        tAttachment.setUnicode(dAttachment.getUnicode());
        //题干
        DAttachmentStemSetting dAttachmentStemSetting = dAttachment.getdAttachmentStemSetting();
        tAttachment.setQuestion(dAttachmentStemSetting.getQuestion());
        tAttachment.setType(dAttachment.getdAttachmentStemSetting().getType());
        //分数
        DAttachmentScoreSetting dAttachmentScoreSetting = dAttachment.getdAttachmentScoreSetting();
        tAttachment.setScore(dAttachmentScoreSetting.getScore());
        tAttachment.setScoreRule(dAttachmentScoreSetting.getScoreRule());
        return tAttachment;
    }

    private DAttachment changeTAttamchmentToDAttachment(TAttachment tAttachment) throws InvocationTargetException, IllegalAccessException {
        DAttachment dAttachment = new DAttachment();
//        BeanConverter.copyProperties(dAttachment, tAttachment);
        dAttachment.setCreator(tAttachment.getCreator());
        dAttachment.setId(tAttachment.getId());
        dAttachment.setUnicode(tAttachment.getUnicode());
        //设置题干
        DAttachmentStemSetting dAttachmentStemSetting = new DAttachmentStemSetting();
        dAttachmentStemSetting.setQuestion(tAttachment.getQuestion());
        dAttachmentStemSetting.setType(tAttachment.getType());
        dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);
        //设置分数
        DAttachmentScoreSetting dAttachmentScoreSetting = new DAttachmentScoreSetting();
        dAttachmentScoreSetting.setScore(tAttachment.getScore());
        dAttachmentScoreSetting.setScoreRule(tAttachment.getScoreRule());
        dAttachment.setdAttachmentScoreSetting(dAttachmentScoreSetting);
        return dAttachment;
    }
}