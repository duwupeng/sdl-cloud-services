package com.talebase.cloud.ms.paper.service;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.domain.TOption;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DItemType;
import com.talebase.cloud.base.ms.paper.enums.TOptionScoreRule;
import com.talebase.cloud.base.ms.paper.enums.TOptionType;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.paper.dao.OptionMapper;
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
public class OptionService {
    @Autowired
    private OptionMapper optionMapper;

    @Transactional(readOnly = false)
    public DQuestionCount save(ServiceHeader serviceHeader, List<DOption> dOptions) throws InvocationTargetException, IllegalAccessException {
        DQuestionCount dQuestionCount = new DQuestionCount();
        List<DUnicode> list = new ArrayList<>();
        BigDecimal totalScore = new BigDecimal(0);
        TOption tOption;
        TOption tOptionSaved;
        DUnicode dUnicode;
        for (DOption dOption : dOptions) {
            tOptionSaved = optionMapper.queryLatestByUnicode(dOption.getUnicode());
            tOption = changeDOptionToTOption(dOption);
            Integer version;
            dUnicode = new DUnicode();
            dUnicode.setUnicode(tOption.getUnicode());
            totalScore = totalScore.add(tOption.getScore());
            if (tOptionSaved == null) {
                version = 1;
            } else {
                if (tOptionSaved.equals(tOption)) {
                    dUnicode.setId(tOptionSaved.getId());
                    list.add(dUnicode);
                    continue;
                } else {
                    version = tOptionSaved.getVersion() + 1;
                }
            }
            tOption.setVersion(version);
            tOption.setCreator(serviceHeader.getOperatorName());
            optionMapper.insert(tOption);
            dQuestionCount.setHasChange(true);
            dUnicode.setId(tOption.getId());
            list.add(dUnicode);
        }
        dQuestionCount.setTotalCount(dOptions.size());
        dQuestionCount.setUnicodeList(list);
        dQuestionCount.setObjectiveCount(list.size());
        dQuestionCount.setTotalScore(totalScore);
        return dQuestionCount;
    }

    public ServiceResponse<List<DOption>> query(String unicodes) throws InvocationTargetException, IllegalAccessException {
        List<String> params = StringUtil.toStrListByComma(unicodes);
        List<TOption> tOptions = optionMapper.query(params);
        List<DOption> dOptions = new ArrayList<>();
        for (TOption tOption : tOptions) {
            DOption dOption = changeTOptionToDOption(tOption);
            dOptions.add(dOption);
        }
        return new ServiceResponse(dOptions);
    }

    public DOption queryByUnicode(String unicode) throws InvocationTargetException, IllegalAccessException {
        TOption tOption = optionMapper.queryLatestByUnicode(unicode);
        DOption dOption = changeTOptionToDOption(tOption);
        return dOption;
    }

    public DOption queryById(Integer id) throws InvocationTargetException, IllegalAccessException {
        TOption tOption = optionMapper.queryById(id);
        DOption dOption = changeTOptionToDOption(tOption);
        return dOption;
    }

    @Transactional(readOnly = false)
    public ServiceResponse update(ServiceHeader serviceHeader, DOption dOption) throws InvocationTargetException, IllegalAccessException {
        TOption tOption = changeDOptionToTOption(dOption);
        Integer count = optionMapper.update(tOption);
        return new ServiceResponse(count);
    }

    private DOption changeTOptionToDOption(TOption tOption) throws InvocationTargetException, IllegalAccessException {
        DOption dOption = new DOption();
        dOption.setCreator(tOption.getCreator());
        dOption.setId(tOption.getId());
        dOption.setUnicode(tOption.getUnicode());
//        BeanConverter.copyProperties(dOption, tOption);
        dOption.setType(tOption.getType());

        //题干
        DOptionStemSetting dOptionStemSetting = new DOptionStemSetting();
        dOptionStemSetting.setQuestion(tOption.getQuestion());
        List<DOptionItem> optionItems = GsonUtil.fromJson(tOption.getOptions(), new TypeToken<List<DOptionItem>>() {
        }.getType());
        dOptionStemSetting.setOptions(optionItems);
        dOption.setdOptionStemSetting(dOptionStemSetting);
        //分数
        DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
        dOptionScoreSetting.setScore(tOption.getScore());
        dOptionScoreSetting.setScoreRule(tOption.getScoreRule());
        if (tOption.getType() == TOptionType.SINGLE_CHOICE.getValue()) {//单选题
            dOptionScoreSetting.setAnswer(tOption.getAnswer());
        } else if (tOption.getType() == TOptionType.MULTIPLE_CHOICE.getValue()) {//多选题
            dOptionScoreSetting.setAnswers(GsonUtil.fromJson(tOption.getAnswer(), String[].class));
            if (tOption.getScoreRule() == TOptionScoreRule.PART_UNITY.getValue() || tOption.getScoreRule() == TOptionScoreRule.PART_AVG.getValue())
                dOptionScoreSetting.setSubScore(tOption.getSubScore());
        }

        dOption.setdOptionScoreSetting(dOptionScoreSetting);
        //样式
        DOptionStyleSetting dOptionStyleSetting = new DOptionStyleSetting();
        return dOption;
    }

    private TOption changeDOptionToTOption(DOption dOption) throws InvocationTargetException, IllegalAccessException {
        TOption tOption = new TOption();
        tOption.setCreator(dOption.getCreator());
        tOption.setId(dOption.getId());
        tOption.setUnicode(dOption.getUnicode());
        tOption.setType(dOption.getType());
//        BeanConverter.copyProperties(tOption, dOption);
        //设置题干
        DOptionStemSetting dOptionStemSetting = dOption.getdOptionStemSetting();
        tOption.setQuestion(dOptionStemSetting.getQuestion());
        tOption.setOptions(GsonUtil.toJson(dOptionStemSetting.getOptions()));
        //设置分数
        DOptionScoreSetting dOptionScoreSetting = dOption.getdOptionScoreSetting();
        tOption.setScore(dOptionScoreSetting.getScore());
        tOption.setScoreRule(dOptionScoreSetting.getScoreRule() == null ? 0 : dOptionScoreSetting.getScoreRule());
        if (dOption.getType() == TOptionType.SINGLE_CHOICE.getValue()) {//单选题
            tOption.setAnswer(dOptionScoreSetting.getAnswer());
        } else if (dOption.getType() == TOptionType.MULTIPLE_CHOICE.getValue()) {//多选题
            tOption.setAnswer(GsonUtil.toJson(dOptionScoreSetting.getAnswers()));
            if (tOption.getScoreRule() == TOptionScoreRule.PART_UNITY.getValue() || tOption.getScoreRule() == TOptionScoreRule.PART_AVG.getValue()) {//部分选中设置小分
                tOption.setSubScore(dOptionScoreSetting.getSubScore());
            }
        }
        //设置样式
        DOptionStyleSetting dOptionStyleSetting = dOption.getdOptionStyleSetting();
        tOption.setStyle(GsonUtil.toJson(dOptionStyleSetting));
        return tOption;
    }

}