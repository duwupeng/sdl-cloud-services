package com.talebase.cloud.os.paper.controller;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DItemType;
import com.talebase.cloud.base.ms.paper.enums.DPaperMode;
import com.talebase.cloud.base.ms.paper.enums.TBlankType;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.CommonParams;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设置题干
 */
@RestController
public class PaperSettingController extends PaperCache {

    /**
     * 新建卷首,题干,结束语
     *
     * @param jsonStr
     * @return
     */
    @PostMapping(value = "/question/stem/{paperUnicode}")
    public ServiceResponse cacheStem(@PathVariable("paperUnicode") String paperUnicode, String subject, String jsonStr) {

        if ("-1".equals(paperUnicode)) {
            subject = "header";
        }
        int len = 0;
        switch (subject) {
            //  插入数据库 ， 创建卷首， 模式为新建中，试卷状态为禁用
            case "header":
                DPaper dPaper = GsonUtil.fromJson(jsonStr, DPaper.class);
                len = dPaper.getComment().length();
                break;
            case "instruction":
                DInstruction dInstruction = GsonUtil.fromJson(jsonStr, DInstruction.class);
                len = dInstruction.getComment().length();
                break;
            default:
                break;
        }

        if (len > CommonParams.TEXTLENGTH){
            throw new WrappedException(BizEnums.TEXT_BEYOUND_LENGTH);
        }

        if ("-1".equals(paperUnicode)) {
            paperUnicode = paperCreate();
            subject = "header";
        } else {
            new ServiceResponse(BizEnums.PaperCreateReject.message);

        }
        switchMode(paperUnicode);
        String hashKey = selectKey(paperUnicode);
        Long sequence = redisTemplate.boundValueOps(paperUnicode).increment(1);

        switch (subject) {
            //  插入数据库 ， 创建卷首， 模式为新建中，试卷状态为禁用
            case "header":
                return new ServiceResponse(GsonUtil.toJson(cachePaperHeader(paperUnicode, jsonStr, hashKey)));
            //创建卷页
            case "page":
                return new ServiceResponse(GsonUtil.toJson(cachePage(paperUnicode, jsonStr, hashKey, sequence)));
            //创建说明
            case "instruction":
                return new ServiceResponse(GsonUtil.toJson(cacheInstruction(paperUnicode, jsonStr, hashKey, sequence)));
            //创建选择题
            case "option":
                return new ServiceResponse(GsonUtil.toJson(cacheOption(paperUnicode, jsonStr, hashKey, sequence)));
            //创建填空题
            case "blank":
                return new ServiceResponse(GsonUtil.toJson(cacheBlank(paperUnicode, jsonStr, hashKey, sequence)));
            //生成上传题
            case "attachment":
                return new ServiceResponse(GsonUtil.toJson(cacheAttachment(paperUnicode, jsonStr, hashKey, sequence)));
            //创建结束语
            case "remark":
                return new ServiceResponse(GsonUtil.toJson(cacheRemarks(paperUnicode, jsonStr, hashKey)));
            default:
                return new ServiceResponse(BizEnums.SubjectNotSurported.message);
        }
    }

    /**
     * 修改卷首,题干,结束语 直接针对题的unicode,直接覆盖
     *
     * @param jsonStr
     * @return
     */
    @PutMapping(value = "/question/stem/{paperUnicode}")
    public ServiceResponse updateCacheStem(@PathVariable("paperUnicode") String paperUnicode, String subject, String jsonStr, String unicode) {
        int len =0;
        switch (subject) {
            case "header":
                DPaper dPaper = GsonUtil.fromJson(jsonStr, DPaper.class);
                len = dPaper.getComment().length();
                break;
            case "instruction":
                DInstruction dInstruction = GsonUtil.fromJson(jsonStr, DInstruction.class);
                len = dInstruction.getComment().length();
                break;
            case "option":
                DOption dOption = GsonUtil.fromJson(jsonStr, DOption.class);
                len = dOption.getdOptionStemSetting().getQuestion().length();
                if (len > CommonParams.TEXTLENGTH){
                    throw new WrappedException(BizEnums.getCustomize(27003007,"题干的文字和样式已超出限制，请减少文字或样式设定。"));
                }
                List<DOptionItem> options = dOption.getdOptionStemSetting().getOptions();
                for (int i=0;i<options.size();i++){
                    DOptionItem dOptionItem = options.get(i);
                    len = dOptionItem.getLabel().length();
                    if (len > CommonParams.TEXTLENGTH){
                        throw new WrappedException(BizEnums.getCustomize(27003008,"第"+(i+1)+"个选项文字和样式已超出限制，请减少文字或样式设定。"));
                    }
                }
                break;
            case "blank":
                DBlank dBlank = GsonUtil.fromJson(jsonStr, DBlank.class);
                len = dBlank.getdBlankStemSetting().getQuestion().length();
                break;
            case "attachment":
                DAttachment dAttachment = GsonUtil.fromJson(jsonStr, DAttachment.class);
                len = dAttachment.getdAttachmentStemSetting().getQuestion().length();
                break;
            case "remark":
//                result = new ServiceResponse(GsonUtil.toJson(updateRemarksCache(hashKey, jsonStr)));
                break;
            default:
                break;
        }

        if (len > CommonParams.TEXTLENGTH){
            throw new WrappedException(BizEnums.TEXT_BEYOUND_LENGTH);
        }


        String hashKey = selectKey(paperUnicode);
        ServiceResponse result = null;
        switch (subject) {
            case "header":
                updatePaperCache(hashKey, jsonStr);
                break;
            case "page":
                result = new ServiceResponse(GsonUtil.toJson(updatePageCache(hashKey, jsonStr, unicode)));
                break;
            case "instruction":
                result = new ServiceResponse(GsonUtil.toJson(updateInstructionCache(hashKey, jsonStr, unicode)));
                break;
            case "option":
                result = new ServiceResponse(GsonUtil.toJson(updateOptionCache(hashKey, jsonStr, unicode)));
                break;
            case "blank":
                result = new ServiceResponse(GsonUtil.toJson(updateBlankCache(hashKey, jsonStr, unicode)));
                break;
            case "attachment":
                result = new ServiceResponse(GsonUtil.toJson(updateAttachmentCache(hashKey, jsonStr, unicode)));
                break;
            case "remark":
                result = new ServiceResponse(GsonUtil.toJson(updateRemarksCache(hashKey, jsonStr)));
                break;
            default:
                return new ServiceResponse(BizEnums.SubjectNotSurported.message);
        }
        return result == null ? new ServiceResponse() : result;
    }

    /**
     * 选择题,填空题,上传体设置分数, 此处为批量提交
     *
     * @param jsonStr
     * @return
     */
    @PostMapping(value = "/question/scores/{paperUnitCode}")
    public ServiceResponse updateCacheScore(@PathVariable String paperUnitCode, String subject, String jsonStr) {
        String msg;
        String hashKey = selectKey(paperUnitCode);
        msg = updateScores(hashKey, jsonStr);
        return new ServiceResponse(msg);
    }

    /**
     * 删除题目
     *
     * @param unicode
     * @return
     */
    @DeleteMapping(value = "/question/item/{unicode}")
    public ServiceResponse deleteCacheStem(@PathVariable String unicode) {
        String paperUnicode = unicode.split("-")[0];
        switchMode(paperUnicode);
        paperUnicode = selectKey(paperUnicode);
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(paperUnicode);
        hashOperations.delete(unicode);
        return new ServiceResponse();
    }

    @DeleteMapping(value = "/question/item/remark/{remarkUnicode}")
    protected ServiceResponse deleteRemark(@PathVariable String remarkUnicode) {
        String paperUnicode = remarkUnicode.split("-")[0];
        switchMode(paperUnicode);
        paperUnicode = selectKey(paperUnicode);
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(paperUnicode);
        List<DPaperRemark> dPaperRemarks = (List<DPaperRemark>) hashOperations.get("remarks");
        DPaperRemark dPaperRemark;
        for (int i = 0; i < dPaperRemarks.size(); i++) {
            dPaperRemark = dPaperRemarks.get(i);
            if (dPaperRemark.getUnicode().equals(remarkUnicode)) {
                dPaperRemarks.remove(i);
            }
        }
        hashOperations.put("remarks", dPaperRemarks);
        return new ServiceResponse();
    }


    private DAttachment cacheAttachment(String paperUnicode, String jsonStr, String hashKey, Long sequence) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        String unitCode;
        DAttachment dAttachment = GsonUtil.fromJson(jsonStr, DAttachment.class);
        unitCode = String.format("%s-%s-%d", paperUnicode, "A", sequence);
        dAttachment.setType(7);
        dAttachment.setUnicode(unitCode);
        dAttachment.setCreator(serviceHeader.getOperatorName());
        hashOperations.put(unitCode, dAttachment);
        cacheItems(hashOperations, unitCode);
        return dAttachment;
    }

    private DBlank cacheBlank(String paperUnicode, String jsonStr, String hashKey, Long sequence) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        String unitCode;
        DBlank dBlank = GsonUtil.fromJson(jsonStr, DBlank.class);
        unitCode = String.format("%s-%s-%d", paperUnicode, "B", sequence);
        dBlank.setUnicode(unitCode);
        dBlank.setType(DItemType.BLANK.getValue());
        dBlank.setCreator(serviceHeader.getOperatorName());
        hashOperations.put(unitCode, dBlank);
        cacheItems(hashOperations, unitCode);
        return dBlank;
    }

    /**
     * 设置题目顺序
     *
     * @param paperUnicode
     * @return
     */
    @PostMapping(value = "/question/sequence/{paperUnicode}")
    public ServiceResponse indexPaper(@PathVariable String paperUnicode, String jsonStr) {
        String hashKey = selectKey(paperUnicode);
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        List<String> newList = GsonUtil.fromJson(jsonStr, List.class);
        List<String> oldList = (List<String>) hashOperations.get("sequences");
        if (!newList.equals(oldList)) {
            switchMode(paperUnicode);
            hashOperations.put("sequences", newList);
        }
        return new ServiceResponse();
    }

    /**
     * 设置或者样式题形转换
     *
     * @param itemUnicode
     * @return
     */
    @PutMapping(value = "/question/stem/style/{itemUnicode}")
    public ServiceResponse changeStyle(@PathVariable("itemUnicode") String itemUnicode, String paperUnicode, String jsonStr) {
        DStemStyle dStemStyle = GsonUtil.fromJson(jsonStr, DStemStyle.class);
        switchMode(paperUnicode.split("-")[0]);
        if (dStemStyle.getType() == 1) {
            //选择题转单选,多选
            changeStemOrStyle(dStemStyle, paperUnicode, itemUnicode, "-O-");
        } else if (dStemStyle.getType() == 2) {
            //填空题转主观客观
            changeStemOrStyle(dStemStyle, paperUnicode, itemUnicode, "-B-");
        } else if (dStemStyle.getType() == 3) {
            // 上传题目类型
            changeStemOrStyle(dStemStyle, paperUnicode, itemUnicode, "-A-");
        }
        return new ServiceResponse();
    }

    private void changeStemOrStyle(DStemStyle dStemStyle, String paperUnicode, String itemUnicode, String questionType) {
        Set<String> allKeys = null;
        Set<String> sigleKeys = new HashSet<>();
        String hashKey = selectKey(paperUnicode);
        BoundHashOperations bop = redisTemplate.boundHashOps(hashKey);
        Set<String> boundHashOperations = bop.keys();
        if (dStemStyle.isChangeAllStyle() && !dStemStyle.isChangeAllType()) {
            //如果勾选了样式的【应用到同类题型】，没有勾选题型的 【应用到同类题型】
            //则更新所有所有同题型的样式，当前题的题型
            allKeys = boundHashOperations.stream().filter(item -> item.indexOf(questionType) != -1).collect(Collectors.toSet());
            changeQuestionStyle(allKeys, hashKey, dStemStyle, bop);
            sigleKeys.add(itemUnicode);
            changeQuestionType(sigleKeys, hashKey, dStemStyle, bop);
        } else if (!dStemStyle.isChangeAllStyle() && dStemStyle.isChangeAllType()) {
            //如果勾选了题型的【应用到同类题型】，没有勾选样式的 【应用到同类题型】
            //则更新所有所有同题型的题型，当前题的样式
            allKeys = boundHashOperations.stream().filter(item -> item.indexOf(questionType) != -1).collect(Collectors.toSet());
            changeQuestionType(allKeys, hashKey, dStemStyle, bop);
            sigleKeys.add(itemUnicode);
            changeQuestionStyle(sigleKeys, hashKey, dStemStyle, bop);
        } else if (dStemStyle.isChangeAllStyle() && dStemStyle.isChangeAllType()) {
            //如果两个都勾选了
            //则更新所有所有同题型的样式，题型
            allKeys = boundHashOperations.stream().filter(item -> item.indexOf(questionType) != -1).collect(Collectors.toSet());
            changeQuestionType(allKeys, hashKey, dStemStyle, bop);
            changeQuestionStyle(allKeys, hashKey, dStemStyle, bop);
        } else {
            //如果两个都没有勾选了
            //则只更新当前题的样式和题型
            sigleKeys.add(itemUnicode);
            changeQuestionType(sigleKeys, hashKey, dStemStyle, bop);
            changeQuestionStyle(sigleKeys, hashKey, dStemStyle, bop);
        }
    }

    /**
     * 更改样式
     *
     * @param keys
     * @param hashKey
     * @param dStemStyle
     * @param bop
     */
    private void changeQuestionStyle(Set<String> keys, String hashKey, DStemStyle dStemStyle, BoundHashOperations bop) {
        DOption dOption;
        DBlank dBlank;
        if (keys != null) {
            Iterator<String> it = keys.iterator();
            String key;
            while (it.hasNext()) {
                key = it.next();
                if (dStemStyle.getType() == 1) {
                    dOption = (DOption) bop.getOperations().opsForHash().get(hashKey, key);
                    if (dStemStyle.getdOptionStyleSetting() != null) {
                        dOption.setdOptionStyleSetting(new DOptionStyleSetting(dStemStyle.getdOptionStyleSetting()));
                    }
                    bop.put(key, dOption);
                } else if (dStemStyle.getType() == 2) {
                    dBlank = (DBlank) redisTemplate.boundHashOps(hashKey).getOperations().opsForHash().get(hashKey, key);
                    if (dStemStyle.getdBlankStyleSetting() != null) {
                        dBlank.setdBlankStyleSetting(dStemStyle.getdBlankStyleSetting());
                    }
                    bop.put(key, dBlank);
                }
            }
        }
    }

    /**
     * 更改题型
     *
     * @param keys
     * @param hashKey
     * @param dStemStyle
     * @param bop
     */
    private void changeQuestionType(Set<String> keys, String hashKey, DStemStyle dStemStyle, BoundHashOperations bop) {
        DOption dOption;
        DBlank dBlank;
        if (keys != null) {
            Iterator<String> it = keys.iterator();
            String key;
            while (it.hasNext()) {
                key = it.next();
                if (dStemStyle.getType() == 1) {
                    dOption = (DOption) bop.getOperations().opsForHash().get(hashKey, key);
                    if (dStemStyle.getIsSingle() == 0) {
                        if (dOption.getType() == DItemType.MULTIPLE_CHOICE.getValue()) {
                            dOption.setType(DItemType.SINGLE_CHOICE.getValue());
                            if (dOption.getdOptionScoreSetting() != null) {
                                dOption.getdOptionScoreSetting().setAnswer(GsonUtil.toJson(dOption.getdOptionScoreSetting().getAnswers()));
                            }
                        }
                    } else {
                        if (dOption.getType() == DItemType.SINGLE_CHOICE.getValue()) {
                            dOption.setType(DItemType.MULTIPLE_CHOICE.getValue());
                            if (dOption.getdOptionScoreSetting() != null) {
                                dOption.getdOptionScoreSetting().setAnswers(new String[]{dOption.getdOptionScoreSetting().getAnswer()});
                            }
                        }
                    }
                    bop.put(key, dOption);
                } else if (dStemStyle.getType() == 2) {
                    dBlank = (DBlank) redisTemplate.boundHashOps(hashKey).getOperations().opsForHash().get(hashKey, key);
                    dBlank.getdBlankStemSetting().setType(dStemStyle.getIsObjective());
                    bop.put(key, dBlank);
                } else if (dStemStyle.getType() == 3) {
                    DAttachment dAttachment = (DAttachment) bop.getOperations().opsForHash().get(hashKey, key);
                    if (!StringUtil.isEmpty(dStemStyle.getPctType())) {
                        dAttachment.getdAttachmentStemSetting().setType(dStemStyle.getPctType());
                    }
                    bop.put(key, dAttachment);
                }
            }
        }
    }

    //    @PutMapping(value="/question/style/{paperUnicode}")
//    public ServiceResponse typeSwich(@PathVariable String paperUnicode,int type,  String jsonStr){
//        switchMode(paperUnicode);
//        String hashKey = selectKey(paperUnicode);
//        BoundHashOperations<String, String, List<String>> boundHashOperations = redisTemplate.boundHashOps(hashKey);
//        Set<String> hashOperations = boundHashOperations.keys();
//        Iterator<String> it = hashOperations.iterator();
//        String key;
//        while(it.hasNext()){
//            key  = it.next();
//            //选择题类型
//            if(type==0&&key.indexOf("-O-") != -1){
//                DOption dOption = (DOption)boundHashOperations.getOperations().opsForHash().get(hashKey,key);
//                DOptionStyleSetting dOptionStyleSetting = GsonUtil.fromJson(jsonStr,DOptionStyleSetting.class);
//                dOption.setdOptionStyleSetting(dOptionStyleSetting);
//                boundHashOperations.getOperations().opsForHash().put(hashKey,key,dOption);
//            } else if (type==1&&key.indexOf("-B-") != -1) {//填空题类型
//                DBlank dBlank =(DBlank)boundHashOperations.getOperations().opsForHash().get(hashKey,key);
//                DBlankStyleSetting  dBlankStyleSetting =  GsonUtil.fromJson(jsonStr,DBlankStyleSetting.class);
//                dBlank.setdBlankStyleSetting(dBlankStyleSetting);
//                boundHashOperations.getOperations().opsForHash().put(hashKey,key,dBlank);
//            }else if(type==2&&key.indexOf("-A-") != -1){//附件类型
//                DAttachment dAttachment= (DAttachment)boundHashOperations.getOperations().opsForHash().get(hashKey,key);
//                DAttachmentStemSetting  dAttachmentStemSetting =  GsonUtil.fromJson(jsonStr,DAttachmentStemSetting.class);
//                dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);
//                boundHashOperations.getOperations().opsForHash().put(hashKey,key,dAttachment);
//            }
//        }
//        return new ServiceResponse();
//    }
    private DOption cacheOption(String paperUnicode, String jsonStr, String hashKey, Long sequence) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);

        DOption dOption = GsonUtil.fromJson(jsonStr, DOption.class);
        String unitCode = String.format("%s-%s-%d", paperUnicode, "O", sequence);
        dOption.setUnicode(unitCode);
        assignOptionItemUniqueCode(dOption.getdOptionStemSetting().getOptions());
        dOption.setCreator(serviceHeader.getOperatorName());
        hashOperations.put(unitCode, dOption);
        cacheItems(hashOperations, unitCode);
        return dOption;
    }

    private DInstruction cacheInstruction(String paperUnicode, String jsonStr, String hashKey, Long sequence) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        DInstruction dInstruction = GsonUtil.fromJson(jsonStr, DInstruction.class);
        String unitCode = String.format("%s-%s-%d", paperUnicode, "I", sequence);
        dInstruction.setUnicode(unitCode);
        dInstruction.setType(3);
        dInstruction.setCreator(serviceHeader.getOperatorName());
        hashOperations.put(unitCode, dInstruction);
        cacheItems(hashOperations, unitCode);
        return dInstruction;
    }

    private DPage cachePage(String paperUnicode, String jsonStr, String hashKey, Long sequence) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);

        String unitCode;
        DPage dPage = GsonUtil.fromJson(jsonStr, DPage.class);
        unitCode = String.format("%s-%s-%d", paperUnicode, "P", sequence);
        dPage.setUnicode(unitCode);
        dPage.setType(2);
        dPage.setCreator(serviceHeader.getOperatorName());
        hashOperations.put(unitCode, dPage);
        cacheItems(hashOperations, unitCode);
        return dPage;
    }


    private List<DPaperRemark> updateRemarksCache(String hashKey, String jsonStr) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        List<DPaperRemark> dPaperRemarks = GsonUtil.fromJson(jsonStr, new TypeToken<List<DPaperRemark>>() {
        }.getType());
        DPaperRemark dPaperRemark;
        String unitCode;
        for (int i = 0; i < dPaperRemarks.size(); i++) {
            dPaperRemark = dPaperRemarks.get(i);
            unitCode = String.format("%s-%s-%d", hashKey.split("-")[0], "R", i);
            dPaperRemark.setId(dPaperRemark.getId());
            dPaperRemark.setUnicode(unitCode);
            dPaperRemark.setType(8);
            dPaperRemark.setCreator(serviceHeader.getOperatorName());
        }
        hashOperations.put("remarks", dPaperRemarks);
        return dPaperRemarks;
    }

    private DAttachment updateAttachmentCache(String hashKey, String jsonStr, String unicode) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        DAttachment redisDAttachment = (DAttachment) hashOperations.getOperations().opsForHash().get(hashKey, unicode);
        DAttachment dAttachment = GsonUtil.fromJson(jsonStr, DAttachment.class);
        if (!redisDAttachment.getdAttachmentStemSetting().equals(dAttachment.getdAttachmentStemSetting())) {
            switchMode(hashKey.split("-")[0]);
            redisDAttachment.setdAttachmentStemSetting(dAttachment.getdAttachmentStemSetting());
            redisDAttachment.setModifier(serviceHeader.getOperatorName());
            hashOperations.put(unicode, redisDAttachment);
        }
        return dAttachment;
    }

    private DBlank updateBlankCache(String hashKey, String jsonStr, String unicode) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        DBlank redisDBlank = (DBlank) hashOperations.get(unicode);
        DBlank dBlank = GsonUtil.fromJson(jsonStr, DBlank.class);
        if (!redisDBlank.getdBlankStemSetting().equals(dBlank.getdBlankStemSetting())
                || !redisDBlank.getdBlankStyleSetting().equals(dBlank.getdBlankStyleSetting())) {
            switchMode(hashKey.split("-")[0]);
            redisDBlank.setdBlankStemSetting(dBlank.getdBlankStemSetting());
            redisDBlank.setdBlankStyleSetting(dBlank.getdBlankStyleSetting());
            redisDBlank.setModifier(serviceHeader.getOperatorName());
            hashOperations.put(unicode, redisDBlank);
        }
        return dBlank;
    }

    private DOption updateOptionCache(String hashKey, String jsonStr, String unicode) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        //更新前从redis读原来的数据
        DOption redisDOption = (DOption) hashOperations.get(unicode);
        //页面来的数据
        DOption dOption = GsonUtil.fromJson(jsonStr, DOption.class);
        if (!redisDOption.getdOptionStemSetting().equals(dOption.getdOptionStemSetting())
                || !redisDOption.getdOptionStyleSetting().equals(dOption.getdOptionStyleSetting())) {
            switchMode(hashKey.split("-")[0]);
            assignOptionItemUniqueCode(dOption.getdOptionStemSetting().getOptions());
            List<DOptionItem> redisOptionItems = redisDOption.getdOptionStemSetting().getOptions();
            List<DOptionItem> optionItems = dOption.getdOptionStemSetting().getOptions();
            for (DOptionItem redisDOptionItem : redisOptionItems) {
                for (DOptionItem dOptionItem : optionItems) {
                    if (redisDOptionItem.getMaskCode().equals(dOptionItem.getMaskCode())) {
                        dOptionItem.setAnswer(redisDOptionItem.isAnswer());
                    }
                }
            }
            //更改题干和选项
            redisDOption.setdOptionStemSetting(dOption.getdOptionStemSetting());
            //更改样式
            redisDOption.setdOptionStyleSetting(dOption.getdOptionStyleSetting());
            redisDOption.setModifier(serviceHeader.getOperatorName());
            hashOperations.put(unicode, redisDOption);
        }
        return dOption;
    }

    private DInstruction updateInstructionCache(String hashKey, String jsonStr, String unicode) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        DInstruction redisDInstruction = (DInstruction) hashOperations.getOperations().opsForHash().get(hashKey, unicode);
        DInstruction dInstruction = GsonUtil.fromJson(jsonStr, DInstruction.class);
        if (!redisDInstruction.equals(dInstruction)) {
            switchMode(hashKey.split("-")[0]);
            redisDInstruction.setComment(dInstruction.getComment());
            redisDInstruction.setFilePath(dInstruction.getFilePath());
            redisDInstruction.setModifier(serviceHeader.getOperatorName());
            hashOperations.put(unicode, redisDInstruction);
        }

        return dInstruction;
    }

    private DPage updatePageCache(String hashKey, String jsonStr, String unicode) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        DPage redisDPage = (DPage) hashOperations.get(unicode);
        DPage dPage = GsonUtil.fromJson(jsonStr, DPage.class);
        if (!redisDPage.getdPageStyleSetting().equals(dPage.getdPageStyleSetting())) {
            switchMode(hashKey.split("-")[0]);
            redisDPage.setdPageStyleSetting(dPage.getdPageStyleSetting());
            redisDPage.setModifier(serviceHeader.getOperatorName());
            hashOperations.put(unicode, redisDPage);
        }
        return dPage;
    }

    private void updatePaperCache(String paperUnicode, String jsonStr) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        String newUnicode = paperUnicode.split("-")[0];
        String unitCode = newUnicode + "-H-1";
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(paperUnicode);
        DPaper dPaperSaved = (DPaper) hashOperations.get(unitCode);
        DPaper dPaper = GsonUtil.fromJson(jsonStr, DPaper.class);
        if (!dPaperSaved.equals(dPaper)) {
            switchMode(newUnicode);
            dPaperSaved.setType(1);
            dPaperSaved.setUnicode(newUnicode);
            dPaperSaved.setModifier(serviceHeader.getOperatorName());
            dPaperSaved.setMode(DPaperMode.修改中.getValue());
            dPaperSaved.setStatus(false);
            dPaperSaved.setComment(dPaper.getComment());
            dPaperSaved.setDuration(dPaper.getDuration());
            dPaperSaved.setName(dPaper.getName());
            hashOperations.put(unitCode, dPaperSaved);
        }
    }
}
