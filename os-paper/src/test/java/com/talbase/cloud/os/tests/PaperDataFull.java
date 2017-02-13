package com.talbase.cloud.os.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric.du on 2016-12-23.
 */
public class PaperDataFull {
    @Test
    public void gen_设置题干查询(){
        DWPaper3 paper = new DWPaper3();
        System.out.println("卷页");

        DPage dPage = new DPage();
        DPageStyleSetting dpageStyleSetting = new DPageStyleSetting();
        dpageStyleSetting.setOptionOrder(1);
        dpageStyleSetting.setSubjectOrder(1);
        dPage.setdPageStyleSetting(dpageStyleSetting);
        dPage.setType(2);
        dPage.setUnicode("pageUniCode");
        paper.setItem(dPage);

        DInstruction dInstruction = new DInstruction();
        dInstruction.setUnicode("instructionUniCode1");
        dInstruction.setType(3);
        dInstruction.setComment("以下是单选题，每题只有一个答案，每题2分，共20分，多答或错答均不给分。");
        dInstruction.setFilePath("filePath");

        paper.setItem(dInstruction);

        DOption dOption = new DOption();
        dOption.setType(4);
        dOption.setUnicode("option-single");
        List<DOptionItem> dOptionItems = new ArrayList<>();
        DOptionItem  dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItems.add(dOptionItem);
        DOptionStemSetting  dOptionStemSetting = new DOptionStemSetting();
        dOptionStemSetting.setQuestion("单选择题");
        dOptionStemSetting.setOptions(dOptionItems);
        dOption.setdOptionStemSetting(dOptionStemSetting);

        DOptionStyleSetting dOptionStyleSetting = new DOptionStyleSetting();
        dOptionStyleSetting.setOptionSetting(1);
        dOption.setdOptionStyleSetting(dOptionStyleSetting);

        paper.setItem(dOption);

        dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setUnicode("instructionUniCode2");
        dInstruction.setComment("以下是多选题，每题至少有两个答案，每题5分，共40分，少答或错答均不给分。");
        dInstruction.setFilePath("filePath");

        paper.setItem(dInstruction);


        dOption = new DOption();
        dOption.setType(5);
        dOption.setUnicode("option-multiple");
        dOptionItems = new ArrayList<>();
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItems.add(dOptionItem);
        dOptionStemSetting = new DOptionStemSetting();
        dOptionStemSetting.setQuestion("多选择题");
        dOptionStemSetting.setOptions(dOptionItems);
        dOption.setdOptionStemSetting(dOptionStemSetting);

        dOptionStyleSetting = new DOptionStyleSetting();
        dOptionStyleSetting.setOptionSetting(5);
        dOption.setdOptionStyleSetting(dOptionStyleSetting);

        paper.setItem(dOption);

        dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setUnicode("instructionUniCode3");
        dInstruction.setComment("以下是客观填空题，每个空2分，共20分，不填或错填均不给分。");
        dInstruction.setFilePath("filePath");


        paper.setItem(dInstruction);

        DBlank dBlank = new DBlank();
        DBlankStemSetting  dBlankStemSetting = new DBlankStemSetting();
        dBlankStemSetting.setNumbers(1);
        dBlankStemSetting.setType(1);
        dBlankStemSetting.setQuestion("填空题客观");

        DBlankStyleSetting  dBlankStyleSetting = new DBlankStyleSetting();
        dBlankStyleSetting.setHeight(20);
        dBlankStyleSetting.setWidth(50);
        dBlankStyleSetting.setLimit("100");
        dBlank.setType(6);
        dBlank.setUnicode("dBlankUnicodeObjective");
        dBlank.setdBlankStemSetting(dBlankStemSetting);
        dBlank.setdBlankStyleSetting(dBlankStyleSetting);

        paper.setItem(dBlank);

        dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setUnicode("instructionUniCode4");
        dInstruction.setComment("以下是主观填空题，共10分。");
        dInstruction.setFilePath("filePath");
        paper.setItem(dInstruction);

        dBlank = new DBlank();
        dBlankStemSetting = new DBlankStemSetting();
        dBlankStemSetting.setNumbers(1);
        dBlankStemSetting.setType(1);
        dBlankStemSetting.setQuestion("填空题");

        dBlankStyleSetting = new DBlankStyleSetting();
        dBlankStyleSetting.setHeight(20);
        dBlankStyleSetting.setWidth(50);
        dBlankStyleSetting.setLimit("100");
        dBlank.setType(6);
        dBlank.setUnicode("dBlankUnicodeSubjective");
        dBlank.setdBlankStemSetting(dBlankStemSetting);
        dBlank.setdBlankStyleSetting(dBlankStyleSetting);

        paper.setItem(dBlank);

        dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setUnicode("instructionUniCode5");
        dInstruction.setComment("以下是上传题，共10分。");
        dInstruction.setFilePath("filePath");

        paper.setItem(dInstruction);

        DAttachment dAttachment = new DAttachment();
        dAttachment.setType(7);
        dAttachment.setUnicode("attachmentUnicode");
        DAttachmentStemSetting  dAttachmentStemSetting = new DAttachmentStemSetting();
        dAttachmentStemSetting.setQuestion("附件题目");
//        dAttachmentStemSetting.setFilePaths(new String[]{"path1","path2"});
//        dAttachmentStemSetting.setType(2);
        dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);


        paper.setItem(dAttachment);

        paper.setNumber(paper.getItems().size());
        paper.setName("试卷名称");

        ServiceResponse sp = new ServiceResponse(paper);
        System.out.println(GsonUtil.toJson(sp));
    }
    @Test
    public void gen_新建题干(){
        System.out.println("新建卷首");

        DPaper dpaper = new DPaper();
        dpaper.setType(1);
        dpaper.setComment("试卷备注");
        dpaper.setName("试卷名称");
        dpaper.setDuration(20);
        System.out.println(GsonUtil.toJson(dpaper));


        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("新建卷页");

        DPage dPage = new DPage();
        DPageStyleSetting dpageStyleSetting = new DPageStyleSetting();
        dpageStyleSetting.setOptionOrder(1);
        dpageStyleSetting.setSubjectOrder(1);
        dPage.setdPageStyleSetting(dpageStyleSetting);
        dPage.setType(2);
        System.out.println(GsonUtil.toJson(dPage));

        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("新建说明");
        DInstruction  dInstruction = new DInstruction();
        dInstruction.setComment("说明");
        dInstruction.setFilePath("filePath");
        dInstruction.setType(3);
        System.out.println(GsonUtil.toJson(dInstruction));


        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("新建单选");
        DOption dOption = new DOption();
        dOption.setType(4);
        DOptionStemSetting  dOptionStemSetting = new DOptionStemSetting();
        dOptionStemSetting.setQuestion("单选择题");
        List<DOptionItem> dOptionItems = new ArrayList<>();
        DOptionItem  dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItem.setMaskCode("maskCodeOne");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItem.setMaskCode("maskCodeTwo");
        dOptionItems.add(dOptionItem);
        dOptionStemSetting.setOptions(dOptionItems);

        DOptionStyleSetting dOptionStyleSetting = new DOptionStyleSetting();
        dOptionStyleSetting.setOptionSetting(1);

        dOption.setdOptionStemSetting(dOptionStemSetting);
        dOption.setdOptionStyleSetting(dOptionStyleSetting);
        System.out.println(GsonUtil.toJson(dOption));

        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("新建多选");
        dOption = new DOption();
        dOption.setType(5);
        dOptionStemSetting = new DOptionStemSetting();
        dOptionStemSetting.setQuestion("多选择题");
        dOptionItems = new ArrayList<>();
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItem.setMaskCode("maskCodeOne");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItem.setMaskCode("maskCodeTwo");
        dOptionItems.add(dOptionItem);
        dOptionStemSetting.setOptions(dOptionItems);
        dOption.setdOptionStemSetting(dOptionStemSetting);
        System.out.println(GsonUtil.toJson(dOption));

        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("新建填空");
        DBlank  dBlank = new DBlank();
        dBlank.setType(6);
        DBlankStemSetting dBlankStemSetting = new DBlankStemSetting();
        dBlankStemSetting.setNumbers(3);
        dBlankStemSetting.setQuestion("填空");
        dBlankStemSetting.setType(1);
        dBlank.setdBlankStemSetting(dBlankStemSetting);

        DBlankStyleSetting dBlankStyleSetting = new DBlankStyleSetting();
        dBlankStyleSetting.setHeight(50);
        dBlankStyleSetting.setLimit("10");
        dBlankStyleSetting.setWidth(51);
        dBlank.setdBlankStyleSetting(dBlankStyleSetting);
        System.out.println(GsonUtil.toJson(dBlank));


        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("新建附件");
        DAttachment  dAttachment = new DAttachment();
        dAttachment.setType(7);
        DAttachmentStemSetting dAttachmentStemSetting = new DAttachmentStemSetting();
        dAttachmentStemSetting.setQuestion("请上传附件作答");
        dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);
        System.out.println(GsonUtil.toJson(dAttachment));

        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("新建结束语");
        DPaperRemark  dPaperRemark = new DPaperRemark();
        dAttachment.setType(8);
        dPaperRemark.setStartScore(BigDecimal.valueOf(1));
        dPaperRemark.setEndScore(BigDecimal.valueOf(10));
        dPaperRemark.setDescription("恭喜您！已完成试卷答题。您本次客观题答题得分为：XX");

        System.out.println(GsonUtil.toJson(dPaperRemark));
    }



    @Test
    public void gen_修改题干(){
        System.out.println("修改卷首");
        DPaper dpaper = new DPaper();
        dpaper.setType(1);
        dpaper.setUnicode("headerCode");
        dpaper.setComment("试卷备注");
        dpaper.setName("试卷名称");
        dpaper.setDuration(20);
        System.out.println(GsonUtil.toJson(dpaper));


        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("修改卷页");

        DPage dPage = new DPage();
        dPage.setUnicode("pageCode");
        DPageStyleSetting dpageStyleSetting = new DPageStyleSetting();
        dpageStyleSetting.setOptionOrder(1);
        dpageStyleSetting.setSubjectOrder(1);
        dPage.setdPageStyleSetting(dpageStyleSetting);
        dPage.setType(2);
        System.out.println(GsonUtil.toJson(dPage));

        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("修改说明");
        DInstruction  dInstruction = new DInstruction();
        dInstruction.setUnicode("instructionCode");
        dInstruction.setComment("说明");
        dInstruction.setFilePath("filePath");
        dInstruction.setType(3);
        System.out.println(GsonUtil.toJson(dInstruction));


        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("修改单选");
        DOption dOption = new DOption();
        dOption.setUnicode("optionCode");
        dOption.setType(4);
        DOptionStemSetting  dOptionStemSetting = new DOptionStemSetting();
        dOptionStemSetting.setQuestion("单选择题");
        List<DOptionItem> dOptionItems = new ArrayList<>();
        DOptionItem  dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItem.setMaskCode("maskCodeOne");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItem.setMaskCode("maskCodeTwo");
        dOptionItems.add(dOptionItem);
        dOptionStemSetting.setOptions(dOptionItems);
        dOption.setdOptionStemSetting(dOptionStemSetting);
        System.out.println(GsonUtil.toJson(dOption));

        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("修改多选");
        dOption = new DOption();
        dOption.setUnicode("optionMultipleCode");
        dOption.setType(5);
        dOptionStemSetting = new DOptionStemSetting();
        dOptionStemSetting.setQuestion("多选择题");
        dOptionItems = new ArrayList<>();
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItem.setMaskCode("maskCodeOne");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItem.setMaskCode("maskCodeTwo");
        dOptionItems.add(dOptionItem);
        dOptionStemSetting.setOptions(dOptionItems);
        dOption.setdOptionStemSetting(dOptionStemSetting);
        System.out.println(GsonUtil.toJson(dOption));

        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("修改填空");
        DBlank  dBlank = new DBlank();
        dBlank.setUnicode("blankCode");
        dBlank.setType(6);
        DBlankStemSetting dBlankStemSetting = new DBlankStemSetting();
        dBlankStemSetting.setNumbers(3);
        dBlankStemSetting.setQuestion("填空");
        dBlankStemSetting.setType(1);
        dBlank.setdBlankStemSetting(dBlankStemSetting);

        DBlankStyleSetting dBlankStyleSetting = new DBlankStyleSetting();
        dBlankStyleSetting.setHeight(50);
        dBlankStyleSetting.setLimit("10");
        dBlankStyleSetting.setWidth(51);
        dBlank.setdBlankStyleSetting(dBlankStyleSetting);
        System.out.println(GsonUtil.toJson(dBlank));


        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("修改附件");
        DAttachment  dAttachment = new DAttachment();
        dAttachment.setUnicode("attachmentCode");
        dAttachment.setType(7);
        DAttachmentStemSetting dAttachmentStemSetting = new DAttachmentStemSetting();
        dAttachmentStemSetting.setQuestion("请上传附件作答");
        dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);
        System.out.println(GsonUtil.toJson(dAttachment));

        /***xxxxxxxxxxxxxxxxxxxx*****/
        System.out.println("修改结束语");
        DPaperRemark  dPaperRemark = new DPaperRemark();
        dPaperRemark.setUnicode("paperRemarkCode");
        dAttachment.setType(8);
        dPaperRemark.setStartScore(BigDecimal.valueOf(1));
        dPaperRemark.setEndScore(BigDecimal.valueOf(10));
        dPaperRemark.setDescription("恭喜您！已完成试卷答题。您本次客观题答题得分为：XX");

        System.out.println(GsonUtil.toJson(dPaperRemark));
    }



//    @Test
//    public void genPages_设置分数(){
//        /***xxxxxxxxxxxxxxxxxxxx*****/
//        System.out.println("设置单选题分数");
//        DOption dOption = new DOption();
//        dOption.setUnicode("optionUniCode");
//        dOption.setType(4);
//        DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
//        dOptionScoreSetting.setScore(BigDecimal.valueOf(10));
//        dOptionScoreSetting.setAnswer("maskCodeOne");
//        dOption.setdOptionScoreSetting(dOptionScoreSetting);
//        System.out.println(GsonUtil.toJson(dOption));
//
//        System.out.println("设置多选题分数");
//
//        dOption = new DOption();
//        dOption.setUnicode("optionUniCode");
//        dOption.setType(5);
//        dOptionScoreSetting = new DOptionScoreSetting();
//        dOptionScoreSetting.setScore(BigDecimal.valueOf(10));
//        dOptionScoreSetting.setScoreRule(1);
//        dOptionScoreSetting.setSubScore(BigDecimal.valueOf(3));
//        dOptionScoreSetting.setAnswers(new String[]{"maskCodeOne","maskCodeTwo"});
//        dOption.setdOptionScoreSetting(dOptionScoreSetting);
//        System.out.println(GsonUtil.toJson(dOption));
//
//        System.out.println("设置填空题分数");
//
//        DBlank  dBlank = new DBlank();
//        dBlank.setType(6);
//        dBlank.setUnicode("dBlankUnicode");
//
//        DBlankScoreSetting dBlankScoreSetting = new DBlankScoreSetting();
//        dBlankScoreSetting.setScore(BigDecimal.valueOf(5));
//        dBlankScoreSetting.setScoreRule(0);
//        dBlankScoreSetting.setExplanation("题目解析");
//        List<DBlankScoreDetail> dBlankScoreDetails = new ArrayList();
//
//        DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
//        dBlankScoreDetail.setAnswer("填空答案一");
//        dBlankScoreDetail.setScore(BigDecimal.valueOf(1));
//        dBlankScoreDetail.setSeqNo(1);
//        dBlankScoreDetails.add(dBlankScoreDetail);
//
//        dBlankScoreDetail = new DBlankScoreDetail();
//        dBlankScoreDetail.setAnswer("填空答案二");
//        dBlankScoreDetail.setScore(BigDecimal.valueOf(1));
//        dBlankScoreDetail.setSeqNo(2);
//        dBlankScoreDetails.add(dBlankScoreDetail);
//
//
//        dBlankScoreSetting.setdBlankScoreDetails(dBlankScoreDetails);
//        dBlank.setdBlankScoreSetting(dBlankScoreSetting);
//        System.out.println(GsonUtil.toJson(dBlank));
//
//
//        System.out.println("设置上传题分数");
//
//        DAttachment dAttachment = new DAttachment();
//        dAttachment.setType(7);
//        DAttachmentScoreSetting dAttachmentScoreSetting = new DAttachmentScoreSetting();
//        dAttachmentScoreSetting.setScore(BigDecimal.valueOf(5));
//        dAttachment.setdAttachmentScoreSetting(dAttachmentScoreSetting);
//        dAttachment.setUnicode("dAttachmentUnicode");
//        dAttachmentScoreSetting.setScoreRule("得分标准");
//        System.out.println(GsonUtil.toJson(dAttachment));
//
//
//        System.out.println(GsonUtil.toJson(new ServiceResponse<>()));
//
//    }
    @Test
    public void genPages_设置分数查询(){
        DWPaper2 page = new DWPaper2();
        DWPage  dWPageResponse  = new DWPage();

        DPageStyleSetting  dPageStyleSetting = new DPageStyleSetting();
        dPageStyleSetting.setSubjectOrder(1);
        dPageStyleSetting.setOptionOrder(1);

        dWPageResponse.setdPageStyleSetting(dPageStyleSetting);

        DPage dPage = new DPage();
        dPage.setUnicode("pageUnicode");
        dPage.setType(2);
        dWPageResponse.setItem(dPage);

        DOption dOption = new DOption();
        dOption.setUnicode("optionUniCode");
        dOption.setType(4);

//        DOptionStyleSetting dOptionStyleSetting = new DOptionStyleSetting();
//        dOptionStyleSetting.setOptionSetting(1);
//
//        dOption.setdOptionStyleSetting(dOptionStyleSetting);

        DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
        dOptionScoreSetting.setScore(BigDecimal.valueOf(10));
        dOptionScoreSetting.setScoreRule(3);
//        dOptionScoreSetting.setAnswer("maskCodeOne");
        dOption.setdOptionScoreSetting(dOptionScoreSetting);
//        List<DOptionScoreDetail>  dOptionScoreDetails= new ArrayList<>();
//        DOptionScoreDetail  dOptionScoreDetail = new DOptionScoreDetail();
//        dOptionScoreDetail.setMaskCode("optionMaskCode1");
//        dOptionScoreDetail.setScore(BigDecimal.valueOf(2));
//        dOptionScoreDetails.add(dOptionScoreDetail);
//        dOptionScoreDetail = new DOptionScoreDetail();
//        dOptionScoreDetail.setMaskCode("optionMaskCode2");
//        dOptionScoreDetail.setScore(BigDecimal.valueOf(2));
//        dOptionScoreDetails.add(dOptionScoreDetail);
//        dOption.setdOptionScoreSetting(dOptionScoreSetting);
        DOptionStemSetting dOptionStemSetting = new DOptionStemSetting();
//        dOptionStemSetting.setType(1);
        dOptionStemSetting.setQuestion("单选择题");
        List<DOptionItem> dOptionItems = new ArrayList<>();
        DOptionItem  dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItem.setMaskCode("maskCodeOne");
        dOptionItem.setAnswer(true);
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItem.setMaskCode("maskCodeTwo");
        dOptionItem.setAnswer(false);
        dOptionItems.add(dOptionItem);
        dOptionStemSetting.setOptions(dOptionItems);

        dOption.setdOptionStemSetting(dOptionStemSetting);

        dWPageResponse.setItem(dOption);



        dOption = new DOption();
        dOption.setUnicode("optionMultipleUniCode");
        dOption.setType(5);

//        DOptionStyleSetting dOptionStyleSetting = new DOptionStyleSetting();
//        dOptionStyleSetting.setOptionSetting(1);
//
//        dOption.setdOptionStyleSetting(dOptionStyleSetting);

        dOptionScoreSetting = new DOptionScoreSetting();
//        dOptionScoreSetting.setAnswers(new String[]{"maskCodeOne","maskCodeTwo"});
        dOptionScoreSetting.setScore(BigDecimal.valueOf(10));
        dOptionScoreSetting.setScoreRule(3);
        dOptionScoreSetting.setSubScore(BigDecimal.valueOf(3));
        dOption.setdOptionScoreSetting(dOptionScoreSetting);
//        List<DOptionScoreDetail>  dOptionScoreDetails= new ArrayList<>();
//        DOptionScoreDetail  dOptionScoreDetail = new DOptionScoreDetail();
//        dOptionScoreDetail.setMaskCode("optionMaskCode1");
//        dOptionScoreDetail.setScore(BigDecimal.valueOf(2));
//        dOptionScoreDetails.add(dOptionScoreDetail);
//        dOptionScoreDetail = new DOptionScoreDetail();
//        dOptionScoreDetail.setMaskCode("optionMaskCode2");
//        dOptionScoreDetail.setScore(BigDecimal.valueOf(2));
//        dOptionScoreDetails.add(dOptionScoreDetail);
//        dOption.setdOptionScoreSetting(dOptionScoreSetting);
         dOptionStemSetting = new DOptionStemSetting();
//        dOptionStemSetting.setType(1);
        dOptionStemSetting.setQuestion("多选择题");
         dOptionItems = new ArrayList<>();
          dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItem.setMaskCode("maskCodeOne");
        dOptionItem.setAnswer(true);
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItem.setMaskCode("maskCodeTwo");
        dOptionItem.setAnswer(false);
        dOptionItems.add(dOptionItem);
        dOptionStemSetting.setOptions(dOptionItems);

        dOption.setdOptionStemSetting(dOptionStemSetting);

        dWPageResponse.setItem(dOption);



        DBlank  dBlank = new DBlank();
        dBlank.setType(6);
        dBlank.setUnicode("dBlankUnicode");
        DBlankStemSetting dBlankStemSetting = new DBlankStemSetting();
        dBlankStemSetting.setNumbers(3);
        dBlankStemSetting.setQuestion("填空");
        dBlankStemSetting.setType(1);
        dBlank.setdBlankStemSetting(dBlankStemSetting);

//        DBlankStyleSetting dBlankStyleSetting = new DBlankStyleSetting();
//        dBlankStyleSetting.setHeight(50d);
//        dBlankStyleSetting.setLimit(10);
//        dBlankStyleSetting.setWidth(51d);
//        dBlank.setdBlankStyleSetting(dBlankStyleSetting);


        DBlankScoreSetting dBlankScoreSetting = new DBlankScoreSetting();
        dBlankScoreSetting.setScore(BigDecimal.valueOf(5));
        dBlankScoreSetting.setScoreRule(0);
        dBlankScoreSetting.setExplanation("题目解析");
        List<DBlankScoreDetail> dBlankScoreDetails = new ArrayList();

        DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setAnswer("填空答案一");
        dBlankScoreDetail.setScore(BigDecimal.valueOf(1));
        dBlankScoreDetail.setSeqNo(1);
        dBlankScoreDetails.add(dBlankScoreDetail);

        dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setAnswer("填空答案二");
        dBlankScoreDetail.setScore(BigDecimal.valueOf(1));
        dBlankScoreDetail.setSeqNo(2);
        dBlankScoreDetails.add(dBlankScoreDetail);

        dBlankScoreSetting.setdBlankScoreDetails(dBlankScoreDetails);
        dBlank.setdBlankScoreSetting(dBlankScoreSetting);

        dWPageResponse.setItem(dBlank);

        DInstruction dInstruction = new DInstruction();
        dInstruction.setUnicode("dInstructionUnicode");
        dInstruction.setType(3);
        dInstruction.setComment("说明");
        dInstruction.setFilePath("filePath");
        dWPageResponse.setItem(dInstruction);

        DAttachment dAttachment = new DAttachment();
        dAttachment.setType(7);
        dAttachment.setUnicode("dAttachmentUnicode");
        DAttachmentStemSetting dAttachmentStemSetting = new DAttachmentStemSetting();
        dAttachmentStemSetting.setQuestion("附件题目");

        DAttachmentScoreSetting dAttachmentScoreSetting = new DAttachmentScoreSetting();
        dAttachmentScoreSetting.setScore(BigDecimal.valueOf(5));
        dAttachment.setdAttachmentScoreSetting(dAttachmentScoreSetting);
        dAttachmentScoreSetting.setScoreRule("得分标准");
        dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);


        dWPageResponse.setItem(dAttachment);
        page.addPage(dWPageResponse);
        page.setUnicode("pageUnicode");
        page.setType(1);
        page.addPage(dWPageResponse);
        page.setStatus(1);
        page.setName("试卷名称");
        ServiceResponse sp = new ServiceResponse(page);
        System.out.println(GsonUtil.toJson(sp));
    }
    @Test
    public void genPages_设置分数提交_1(){
//        String jsonStr= "{\"dOptions\":[{\"dOptionScoreSetting\":{\"answers\":[\"567eaf0d-b46\",\"f802d691-e9f\"],\"score\":\"4\",\"scoreRule\":1},\"unicode\":\"P0110192925441-O-2\",\"type\":5},{\"dOptionScoreSetting\":{\"answers\":[\"4118e296-779\"],\"score\":\"5\",\"scoreRule\":0},\"unicode\":\"P0110192925441-O-4\",\"type\":5}],\"dAttachments\":[{\"dAttachmentScoreSetting\":{\"score\":\"11\"},\"unicode\":\"P0110192925441-B-3\",\"type\":7}],\"dBlanks\":[{\"dBlankScoreSetting\":{\"dBlankScoreDetails\":[{\"seqNo\":0,\"answer\":\"11\",\"score\":\"11\"},{\"seqNo\":1,\"answer\":\"22\",\"score\":\"22\"},{\"seqNo\":2,\"answer\":\"33\",\"score\":\"33\"}],\"score\":66,\"scoreRule\":\"1\",\"explanation\":\"55xx\"},\"unicode\":\"P0110192925441-B-3\",\"type\":6}]}";
//        DPaper3Request items = GsonUtil.fromJson(jsonStr,DPaper3Request.class);
        String jsonStr ="[{\"id\":\"\",\"startScore\":1,\"endScore\":11,\"description\":\"<p>恭喜您！已完成试卷答题。您本次客观题答题得分为：$$得分$$</p>\",\"unicode\":\"\"},{\"id\":\"\",\"startScore\":11.1,\"endScore\":100,\"description\":\"<p>恭喜您！已完成试卷答题。您本次客观题答题得分为：$$得分$$</p>\",\"unicode\":\"\"}]";
        GsonUtil.fromJson(jsonStr,new TypeToken<List<DPaperRemark>>(){}.getType());

    }

    @Test
    public void genPages_设置分数提交(){

        DPaper3Request  dPaper3Request = new DPaper3Request();
        DOption dOption = new DOption();
        dOption.setUnicode("optionUniCode");
        dOption.setType(4);
        DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
        dOptionScoreSetting.setScore(BigDecimal.valueOf(10));
        dOptionScoreSetting.setAnswer("maskCodeOne");
        dOption.setdOptionScoreSetting(dOptionScoreSetting);
        dPaper3Request.setdOption(dOption);

        dOption = new DOption();
        dOption.setUnicode("optionUniCode");
        dOption.setType(5);
        dOptionScoreSetting = new DOptionScoreSetting();
        dOptionScoreSetting.setScore(BigDecimal.valueOf(10));
        dOptionScoreSetting.setScoreRule(1);
        dOptionScoreSetting.setSubScore(BigDecimal.valueOf(3));
        dOptionScoreSetting.setAnswers(new String[]{"maskCodeOne","maskCodeTwo"});
        dOption.setdOptionScoreSetting(dOptionScoreSetting);
        dPaper3Request.setdOption(dOption);


        DBlank  dBlank = new DBlank();
        dBlank.setType(6);
        dBlank.setUnicode("dBlankUnicode");

        DBlankScoreSetting dBlankScoreSetting = new DBlankScoreSetting();
        dBlankScoreSetting.setScore(BigDecimal.valueOf(5));
        dBlankScoreSetting.setScoreRule(0);
        dBlankScoreSetting.setExplanation("题目解析");
        List<DBlankScoreDetail> dBlankScoreDetails = new ArrayList();

        DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setAnswer("填空答案一");
        dBlankScoreDetail.setScore(BigDecimal.valueOf(1));
        dBlankScoreDetail.setSeqNo(1);
        dBlankScoreDetails.add(dBlankScoreDetail);

        dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setAnswer("填空答案二");
        dBlankScoreDetail.setScore(BigDecimal.valueOf(1));
        dBlankScoreDetail.setSeqNo(2);
        dBlankScoreDetails.add(dBlankScoreDetail);


        dBlankScoreSetting.setdBlankScoreDetails(dBlankScoreDetails);
        dBlank.setdBlankScoreSetting(dBlankScoreSetting);
        dPaper3Request.setdBlank(dBlank);



        DAttachment dAttachment = new DAttachment();
        dAttachment.setType(7);
        DAttachmentScoreSetting dAttachmentScoreSetting = new DAttachmentScoreSetting();
        dAttachmentScoreSetting.setScore(BigDecimal.valueOf(5));
        dAttachment.setdAttachmentScoreSetting(dAttachmentScoreSetting);
        dAttachment.setUnicode("dAttachmentUnicode");
        dAttachmentScoreSetting.setScoreRule("得分标准");
        dPaper3Request.setdAttachment(dAttachment);
//        List unicodes  = new ArrayList();
//        unicodes.add("pageUnicode");
//        unicodes.add("instrumentUnicode");
//        unicodes.add("optionUnicode");
//        unicodes.add("blankUnicode");
//        unicodes.add("attachmentUnicode");
//
//        items.setUnitCodes(unicodes);
//        ServiceRequest sp = new ServiceRequest();
        System.out.println(GsonUtil.toJson(dPaper3Request));
    }



    @Test
    public void genPages_试卷预览(){
        DWPaper3 page = new DWPaper3();

        DInstruction dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setComment("以下是单选题，每题只有一个答案，每题2分，共20分，多答或错答均不给分。");
        dInstruction.setFilePath("filePath");

        DOptionStem dOptionStem = new DOptionStem();
        dOptionStem.setType(4);
        dOptionStem.setQuestion("单选择题");
        List<DOptionItem> dOptionItems = new ArrayList<>();
        DOptionItem  dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItems.add(dOptionItem);
        dOptionStem.setOptions(dOptionItems);
        dOptionStem.setdInstruction(dInstruction);
        page.setItem(dOptionStem);

        dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setComment("以下是多选题，每题至少有两个答案，每题5分，共40分，少答或错答均不给分。");
        dInstruction.setFilePath("filePath");


        dOptionStem = new DOptionStem();
        dOptionStem.setType(5);
        dOptionStem.setQuestion("多选择题");
        dOptionItems = new ArrayList<>();
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项一");
        dOptionItems.add(dOptionItem);
        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("选项二");
        dOptionItems.add(dOptionItem);
        dOptionStem.setOptions(dOptionItems);
        dOptionStem.setdInstruction(dInstruction);
        page.setItem(dOptionStem);

        dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setComment("以下是客观填空题，每个空2分，共20分，不填或错填均不给分。");
        dInstruction.setFilePath("filePath");

        DBlankStem dBlankStem = new DBlankStem();
        dBlankStem.setNumbers(3);
        dBlankStem.setQuestion("填空");
        dBlankStem.setType(6);
        dBlankStem.setBlankType(1);
        dBlankStem.setdInstruction(dInstruction);
        page.setItem(dBlankStem);

        dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setComment("以下是主观填空题，共10分。");
        dInstruction.setFilePath("filePath");

        dBlankStem = new DBlankStem();
        dBlankStem.setQuestion("填空");
        dBlankStem.setType(6);
        dBlankStem.setBlankType(2);
        dBlankStem.setdInstruction(dInstruction);
        page.setItem(dBlankStem);

        dInstruction = new DInstruction();
        dInstruction.setType(3);
        dInstruction.setComment("以下是上传题，共10分。");
        dInstruction.setFilePath("filePath");

        DAttachmentStem dAttachmentStem = new DAttachmentStem();
        dAttachmentStem.setType(7);
        dAttachmentStem.setQuestion("附件题目");
        dAttachmentStem.setdInstruction(dInstruction);
        page.setItem(dAttachmentStem);
        page.setNumber(page.getItems().size());
        page.setName("试卷名称");
        ServiceResponse sp = new ServiceResponse(page);
        System.out.println(GsonUtil.toJson(sp));
    }

    @Test
    public void genRemarks_结束语查询(){
        DWPaper4 page = new DWPaper4();

        DPaperRemark   dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(BigDecimal.valueOf(1));
        dPaperRemark.setEndScore(BigDecimal.valueOf(10));
        dPaperRemark.setDescription("F");
        dPaperRemark.setUnicode("remarkCode1");
        dPaperRemark.setId(1);
        List list = new ArrayList();
        list.add(dPaperRemark);

        dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(BigDecimal.valueOf(10));
        dPaperRemark.setEndScore(BigDecimal.valueOf(20));
        dPaperRemark.setDescription("E");
        dPaperRemark.setId(2);
        dPaperRemark.setUnicode("remarkCode2");
        list.add(dPaperRemark);

        page.setItems(list);
        page.setName("初级技术人员水平考试 试卷");
//        System.out.println(GsonUtil.toJson(page));
        System.out.println(GsonUtil.toJson(new ServiceResponse<>(page)));

    }

    @Test
    public void genRemarks_设置题干下一步(){
        List list  = new ArrayList();
        list.add("code1");
        list.add("code2");
        System.out.println(GsonUtil.toJson(list));
    }

}
