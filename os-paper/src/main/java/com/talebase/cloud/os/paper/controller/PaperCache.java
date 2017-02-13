package com.talebase.cloud.os.paper.controller;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DItemType;
import com.talebase.cloud.base.ms.paper.enums.DPaperMode;
import com.talebase.cloud.base.ms.paper.enums.TOptionType;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.SequenceUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.os.paper.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by eric.du on 2016-12-30.
 */
@Component
public class PaperCache {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    PaperService paperService;

    public static void assignOptionItemUniqueCode(List<DOptionItem> optionItems) {
            for (DOptionItem dOptionItem : optionItems) {
                if (StringUtil.isEmpty(dOptionItem.getMaskCode())) {
                    dOptionItem.setMaskCode(UUID.randomUUID().toString().substring(0, 12));
                }
        }

    }

    public String paperCreate() {
        //生成试卷内容计序器
        String uniCode = SequenceUtil.generateSequenceNo("P");
        redisTemplate.opsForValue().set(uniCode, 0);
        redisTemplate.opsForValue().set(uniCode + "-mode", DPaperMode.新建中);

        List<String> list = new ArrayList<>();

        String hashKey = uniCode + "-new";
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        hashOperations.put("sequences", list);

        return uniCode;
    }

    protected void cacheItems(BoundHashOperations<String, String, Object> hashOperations, String unitCode) {
        List codes = (List<String>) hashOperations.get("sequences");
        if (codes.stream().filter(code -> code != null && code.equals(unitCode)).count() == 0)
            codes.add(unitCode);
        hashOperations.put("sequences", codes);
    }

    public String selectKey(String paperUnicode) {
        // 判断目前的编辑模式, 如果当前的编辑模式为新建，则
        String mode = (String) redisTemplate.opsForValue().get(paperUnicode + "-mode");
        if (DPaperMode.新建中.name().equals(mode)) {
            paperUnicode = paperUnicode + "-new";
        } else {
            paperUnicode = paperUnicode + "-modify";
        }
        return paperUnicode;
    }

    public void switchMode(String paperUnicode) {
        // 判断目前的编辑模式, 如果当前的编辑模式为新建，则
        String mode = (String) redisTemplate.opsForValue().get(paperUnicode + "-mode");
        if (DPaperMode.新建中.name().equals(mode) || DPaperMode.修改中.name().equals(mode)) {
            return;
        } else if (DPaperMode.完成.name().equals(mode)) {
            redisTemplate.opsForValue().set(paperUnicode + "-mode", DPaperMode.修改中);
            String hashKey = paperUnicode + "-modify";
            BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);

            DPaper dPaper = (DPaper) hashOperations.get(paperUnicode + "-H-1");
            dPaper.setMode(DPaperMode.修改中.getValue());
            dPaper.setStatus(false);

            redisTemplate.opsForHash().put(paperUnicode + "-modify", paperUnicode + "-H-1", dPaper);
            paperService.updateMode(dPaper.getId(), DPaperMode.修改中, false);
        }
    }

    protected String updateScores(String hashKey, String jsonStr) {
        DPaper3Request items = GsonUtil.fromJson(jsonStr, DPaper3Request.class);
        List<DOption> options = items.getdOptions();
        List<DBlank> blanks = items.getdBlanks();
        List<DAttachment> attachments = items.getdAttachments();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        boolean flag = false;
        for (DOption dOption : options) {
            if (dOption.getdOptionScoreSetting().getScore() == null) {
                return dOption.getUnicode() + " 分数未设定";
            }
            DOption dOptionSaved = (DOption) hashOperations.get(dOption.getUnicode());
            if (dOption.getdOptionScoreSetting().equals(dOptionSaved.getdOptionScoreSetting())) {
                flag = true;
            }
            DOptionScoreSetting dOptionScoreSetting = dOption.getdOptionScoreSetting();
            List<DOptionItem> dOptionItems = dOptionSaved.getdOptionStemSetting().getOptions();
            for (DOptionItem dOptionItem : dOptionItems) {
                if (dOptionSaved.getType() == DItemType.SINGLE_CHOICE.getValue()) {
                    String answer = dOptionScoreSetting.getAnswer();
                    if (dOptionItem.getMaskCode().equals(answer)) {
                        dOptionItem.setAnswer(true);
                    }
                } else {
                    String[] answers = dOptionScoreSetting.getAnswers();
                    for (String answer : answers) {
                        if (dOptionItem.getMaskCode().equals(answer)) {
                            dOptionItem.setAnswer(true);
                        }
                    }
                }
            }
            dOptionSaved.setdOptionScoreSetting(dOptionScoreSetting);
            hashOperations.put(dOption.getUnicode(), dOptionSaved);
        }
        for (DBlank dBlank : blanks) {
            if (dBlank.getdBlankScoreSetting().getScore() == null) {
                return dBlank.getUnicode() + " 分数未设定";
            }
            DBlank dBlankSaved = (DBlank) hashOperations.get(dBlank.getUnicode());
            if (dBlank.getdBlankScoreSetting().equals(dBlank.getdBlankScoreSetting())) {
                flag = true;
            }
            dBlankSaved.setdBlankScoreSetting(dBlank.getdBlankScoreSetting());
            hashOperations.put(dBlank.getUnicode(), dBlankSaved);
        }
        for (DAttachment dAttachment : attachments) {
            if (dAttachment.getdAttachmentScoreSetting().getScore() == null) {
                return dAttachment.getUnicode() + " 分数未设定";
            }
            DAttachment dAttachmentSaved = (DAttachment) hashOperations.get(dAttachment.getUnicode());
            if (dAttachment.getdAttachmentScoreSetting().equals(dAttachmentSaved.getdAttachmentScoreSetting())) {
                flag = true;
            }
            dAttachmentSaved.setdAttachmentScoreSetting(dAttachment.getdAttachmentScoreSetting());
            hashOperations.put(dAttachmentSaved.getUnicode(), dAttachmentSaved);
        }
        hashOperations.put("totalScore", items.getScore());
        String paperUnicode = hashKey.split("-")[0];
//        String unitCode = paperUnicode + "-H-1";
        //如果有变化，且没有点完成，则进入修改中
        if (!flag) {
            switchMode(paperUnicode);
//            String mode = (String) redisTemplate.opsForValue().get(paperUnicode + "-mode");
//            if (DPaperMode.完成.name().equals(mode) || DPaperMode.修改中.name().equals(mode)) {
//                DPaper dPaperSaved = (DPaper) hashOperations.getOperations().opsForHash().get(hashKey, unitCode);
//                redisTemplate.opsForValue().set(paperUnicode + "-mode", DPaperMode.完成);
//                paperService.updateMode(dPaperSaved.getId(), DPaperMode.完成, true);
//                dPaperSaved.setStatus(true);
//                redisTemplate.opsForHash().put(hashKey, unitCode, dPaperSaved);
//            }
        }
        return "";
    }

    /**
     * 只是刷新到缓存
     *
     * @param paperUnicode
     * @param jsonStr
     * @param hashKey
     * @return
     */
    protected DPaper cachePaperHeader(String paperUnicode, String jsonStr, String hashKey) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        String unitCode;
        DPaper dPaper = GsonUtil.fromJson(jsonStr, DPaper.class);
        unitCode = String.format("%s-%s-%d", paperUnicode, "H", 1);
        dPaper.setType(1);
        dPaper.setUnicode(paperUnicode);
        dPaper.setCreator(serviceHeader.getOperatorName());
        dPaper.setStatus(false);
        dPaper.setMode(DPaperMode.新建中.getValue());

        int id = paperService.savePaperHeader(new ServiceRequest(serviceHeader, dPaper));
        if (id > 0) {
            dPaper.setId(id);
            hashOperations.put(unitCode, dPaper);

        } else {
            new ServiceResponse(BizEnums.PAPERSAVEFAILED.message);
        }
        return dPaper;
    }

    protected List<DPaperRemark> cacheRemarks(String paperUnicode, String jsonStr, String hashKey) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        List<DPaperRemark> dPaperRemarks = GsonUtil.fromJson(jsonStr, new TypeToken<List<DPaperRemark>>() {
        }.getType());
        DPaperRemark dPaperRemark;
        String unitCode = null;
        for (int i = 0; i < dPaperRemarks.size(); i++) {
            dPaperRemark = dPaperRemarks.get(i);
            unitCode = String.format("%s-%s-%d", paperUnicode, "R", i);
            dPaperRemark.setUnicode(unitCode);
            dPaperRemark.setType(8);
            dPaperRemark.setCreator(serviceHeader.getOperatorName());
        }
        hashOperations.put("remarks", dPaperRemarks);
        return dPaperRemarks;
    }
}
