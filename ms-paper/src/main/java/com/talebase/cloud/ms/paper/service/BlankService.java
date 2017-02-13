package com.talebase.cloud.ms.paper.service;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.domain.TBlank;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.TBlankType;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.paper.dao.BlankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by eric.du on 2016-11-24.
 */

@Service
@Transactional(readOnly = true)
public class BlankService {
    @Autowired
    private BlankMapper blankMapper;

    @Transactional(readOnly = false)
    public DQuestionCount save(ServiceHeader serviceHeader, List<DBlank> dBlanks) throws InvocationTargetException, IllegalAccessException {
        DQuestionCount dQuestionCount = new DQuestionCount();
        List<DUnicode> list = new ArrayList<>();
        BigDecimal totalScore = new BigDecimal(0);
        Integer subjectiveCount = 0;
        Integer objectiveCount = 0;
        TBlank tBlank;
        TBlank tBlankSaved;
        DUnicode dUnicode;
        for (DBlank dBlank : dBlanks) {
            tBlankSaved = blankMapper.queryLatestByUnicode(dBlank.getUnicode());
            tBlank = changeDBlankToTBlank(dBlank);
            Integer version;
            totalScore = totalScore.add(tBlank.getScore() == null ? new BigDecimal(0) : tBlank.getScore());

            if (tBlank.getType() == TBlankType.SUBJECTIVE.getValue()) {
                subjectiveCount += 1;
            } else {
                objectiveCount += 1;
            }
            dUnicode = new DUnicode();
            dUnicode.setUnicode(tBlank.getUnicode());
            if (tBlankSaved == null) {
                version = 1;
            } else {
                if (tBlankSaved.equals(tBlank)) {
                    dUnicode.setId(tBlankSaved.getId());
                    list.add(dUnicode);
                    continue;
                } else {
                    version = tBlankSaved.getVersion() + 1;
                }
            }
            tBlank.setCreator(serviceHeader.getOperatorName());
            tBlank.setVersion(version);
            blankMapper.insert(tBlank);
            dQuestionCount.setHasChange(true);
            dUnicode.setId(tBlank.getId());
            list.add(dUnicode);
        }
        dQuestionCount.setTotalCount(dBlanks.size());
        dQuestionCount.setUnicodeList(list);
        dQuestionCount.setTotalScore(totalScore);
        dQuestionCount.setSubjectiveCount(subjectiveCount);
        dQuestionCount.setObjectiveCount(objectiveCount);
        return dQuestionCount;
    }

    public ServiceResponse<List<DBlank>> query(String unicodes) throws InvocationTargetException, IllegalAccessException {
        List<String> params = StringUtil.toStrListByComma(unicodes);
        List<TBlank> tBlanks = blankMapper.query(params);
        List<DBlank> dBlanks = new ArrayList<>();
        for (TBlank tBlank : tBlanks) {
            DBlank dBlank = changeTBlankToDBlank(tBlank);
            dBlanks.add(dBlank);
        }
        return new ServiceResponse(dBlanks);
    }

    public DBlank queryByUnicode(String unicode) throws InvocationTargetException, IllegalAccessException {
        TBlank tBlank = blankMapper.queryLatestByUnicode(unicode);
        DBlank dBlank = changeTBlankToDBlank(tBlank);
        return dBlank;
    }

    public DBlank queryById(Integer id) throws InvocationTargetException, IllegalAccessException {
        TBlank tBlank = blankMapper.queryById(id);
        DBlank dBlank = changeTBlankToDBlank(tBlank);
        return dBlank;
    }

    @Transactional(readOnly = false)
    public ServiceResponse update(ServiceHeader serviceHeader, DBlank dBlank) throws InvocationTargetException, IllegalAccessException {
        TBlank tBlank = changeDBlankToTBlank(dBlank);
        Integer count = blankMapper.update(tBlank);
        return new ServiceResponse(count);
    }

    private DBlank changeTBlankToDBlank(TBlank tBlank) throws InvocationTargetException, IllegalAccessException {
        DBlank dBlank = new DBlank();
//        BeanConverter.copyProperties(dBlank, tBlank);
        dBlank.setId(tBlank.getId());
        dBlank.setUnicode(tBlank.getUnicode());
        dBlank.setCreator(tBlank.getCreator());
        //设置题干
        DBlankStemSetting dBlankStemSetting = new DBlankStemSetting();
        dBlankStemSetting.setQuestion(tBlank.getQuestion());
        dBlankStemSetting.setType(tBlank.getType());
        dBlank.setdBlankStemSetting(dBlankStemSetting);
        //设置分数
        DBlankScoreSetting dBlankScoreSetting = GsonUtil.fromJson(tBlank.getAnswer(), DBlankScoreSetting.class);
        //设置分数明细
        List<DBlankScoreDetail> dBlankScoreDetails = dBlankScoreSetting.getdBlankScoreDetails();
        dBlankScoreSetting.setdBlankScoreDetails(dBlankScoreDetails);
        //设置填空个数
        dBlankStemSetting.setNumbers(dBlankScoreDetails.size());

        dBlankScoreSetting.setScore(tBlank.getScore());
        dBlankScoreSetting.setScoreRule(tBlank.getScoreRule());
        dBlank.setdBlankScoreSetting(dBlankScoreSetting);
        //设置样式
        DBlankStyleSetting dBlankStyleSetting = GsonUtil.fromJson(tBlank.getStyle(), DBlankStyleSetting.class);
        dBlank.setdBlankStyleSetting(dBlankStyleSetting);
        return dBlank;
    }

    private TBlank changeDBlankToTBlank(DBlank dBlank) throws InvocationTargetException, IllegalAccessException {
        TBlank tBlank = new TBlank();
//        BeanConverter.copyProperties(tBlank, dBlank);
        tBlank.setId(dBlank.getId());
        tBlank.setUnicode(dBlank.getUnicode());
        tBlank.setCreator(dBlank.getCreator());
        //填空提题干
        DBlankStemSetting dBlankStemSetting = dBlank.getdBlankStemSetting();
        tBlank.setQuestion(dBlankStemSetting.getQuestion());
        tBlank.setType(dBlankStemSetting.getType());
        //填空题分数
        DBlankScoreSetting dBlankScoreSetting = dBlank.getdBlankScoreSetting();
        tBlank.setScore(dBlankScoreSetting.getScore());
        tBlank.setScoreRule(dBlankScoreSetting.getScoreRule());
        //填空题答案明细
        List<DBlankScoreDetail> dBlankScoreDetails = dBlankScoreSetting.getdBlankScoreDetails();
        BigDecimal score = new BigDecimal(0);
        for (DBlankScoreDetail dBlankScoreDetail : dBlankScoreDetails) {
            //总分=各个空的分数之和
            score = score.add(dBlankScoreDetail.getScore());
        }
        tBlank.setStyle(GsonUtil.toJson(dBlank.getdBlankStyleSetting()));
        tBlank.setScore(score);
        tBlank.setAnswer(GsonUtil.toJson(dBlankScoreSetting));
        return tBlank;
    }
}