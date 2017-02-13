package com.talebase.cloud.ms.paper.service;

import com.talebase.cloud.base.ms.paper.domain.TPaperRemark;
import com.talebase.cloud.base.ms.paper.dto.DPaperRemark;
import com.talebase.cloud.base.ms.paper.dto.DQuestionCount;
import com.talebase.cloud.base.ms.paper.dto.DUnicode;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.ms.paper.dao.PaperRemarkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du  on 2016-11-24.
 */

@Service
@Transactional(readOnly = true)
public class PaperRemarkService {

    @Autowired
    private PaperRemarkMapper paperRemarkMapper;

    @Transactional(readOnly = false)
    public DQuestionCount save(ServiceHeader serviceHeader, List<DPaperRemark> dPaperRemarks) throws InvocationTargetException, IllegalAccessException {
        DQuestionCount dQuestionCount = new DQuestionCount();
        List<DUnicode> list = new ArrayList<>();
        TPaperRemark tPaperRemark;
        TPaperRemark tPaperRemarkSaved;
        DUnicode dUnicode;
        for (DPaperRemark dPaperRemark : dPaperRemarks) {
            tPaperRemarkSaved = paperRemarkMapper.queryLatestByUnicode(dPaperRemark.getUnicode());
            tPaperRemark = changeDPaperRemarkToTPaperRemark(dPaperRemark);
            Integer version;
            dUnicode = new DUnicode();
            dUnicode.setUnicode(tPaperRemark.getUnicode());
            if (tPaperRemarkSaved == null) {
                version = 1;
            } else {
                if(tPaperRemarkSaved.equals(tPaperRemark)){
                    dUnicode.setId(tPaperRemarkSaved.getId());
                    list.add(dUnicode);
                    continue;
                }else{
                    version = tPaperRemarkSaved.getVersion() + 1;
                }
            }
            tPaperRemark.setVersion(version);
            tPaperRemark.setCreator(serviceHeader.getOperatorName());
            paperRemarkMapper.insert(tPaperRemark);
            dQuestionCount.setHasChange(true);
            dUnicode.setId(tPaperRemark.getId());
            list.add(dUnicode);
        }
        dQuestionCount.setUnicodeList(list);
        return dQuestionCount;
    }

    public DPaperRemark queryByUnicode(String unicode) throws InvocationTargetException, IllegalAccessException {
        TPaperRemark tPaperRemark = paperRemarkMapper.queryLatestByUnicode(unicode);
        DPaperRemark dPaperRemark = changeTPaperRemarkToDPaperRemark(tPaperRemark);
        return dPaperRemark;
    }
    public DPaperRemark queryById(Integer paperId) throws InvocationTargetException, IllegalAccessException {
        TPaperRemark tPaperRemark = paperRemarkMapper.queryById(paperId);
        DPaperRemark dPaperRemark = changeTPaperRemarkToDPaperRemark(tPaperRemark);
        return dPaperRemark;
    }
    @Transactional(readOnly = false)
    public ServiceResponse update(ServiceHeader serviceHeader, DPaperRemark dPaperRemark) throws InvocationTargetException, IllegalAccessException {
        TPaperRemark tPaperRemark = changeDPaperRemarkToTPaperRemark(dPaperRemark);
        Integer count = paperRemarkMapper.update(tPaperRemark);
        return new ServiceResponse(count);
    }

    private DPaperRemark changeTPaperRemarkToDPaperRemark(TPaperRemark tPaperRemark) throws InvocationTargetException, IllegalAccessException {
        DPaperRemark dPaperRemark = new DPaperRemark();
        dPaperRemark.setId(tPaperRemark.getId());
        dPaperRemark.setUnicode(tPaperRemark.getUnicode());
        dPaperRemark.setStartScore(tPaperRemark.getStartScore());
        dPaperRemark.setEndScore(tPaperRemark.getEndScore());
        dPaperRemark.setDescription(tPaperRemark.getDescription());
        dPaperRemark.setCreator(tPaperRemark.getCreator());
//        BeanConverter.copyProperties(dPaperRemark, tPaperRemark);
        return dPaperRemark;
    }

    private TPaperRemark changeDPaperRemarkToTPaperRemark(DPaperRemark dPaperRemark) throws InvocationTargetException, IllegalAccessException {
        TPaperRemark tPaperRemark = new TPaperRemark();
        tPaperRemark.setId(dPaperRemark.getId());
        tPaperRemark.setUnicode(dPaperRemark.getUnicode());
        tPaperRemark.setStartScore(dPaperRemark.getStartScore());
        tPaperRemark.setEndScore(dPaperRemark.getEndScore());
        tPaperRemark.setDescription(dPaperRemark.getDescription());
        tPaperRemark.setCreator(dPaperRemark.getCreator());
//        BeanConverter.copyProperties(tPaperRemark, dPaperRemark);
        return tPaperRemark;
    }
}