package com.talbase.cloud.os.tests;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.dto.DExamItem;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Test;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by daorong.li on 2016-11-24.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
public class PaperTestCase {

    @Test
    public void genJSonStem(){
        System.out.println("-----------------卷子 json-----------------------");
        DPaper dPaper = new DPaper();
        dPaper.setName("初级考试");
        dPaper.setComment("这是一套检测小学生的题目");
        dPaper.setDuration(60);
        dPaper.setType(1);
        System.out.println(GsonUtil.toJson(dPaper));


        System.out.println("-----------------页面 json-----------------------");
        //页面 json
        DPage dPage = new DPage();
        DPageStyleSetting dPageStyleSetting = new DPageStyleSetting();
        dPageStyleSetting.setOptionOrder(1);
        dPageStyleSetting.setSubjectOrder(0);
        dPage.setdPageStyleSetting(dPageStyleSetting);

        DBlankStyleSetting dBlankStyleSetting = new DBlankStyleSetting();
        dBlankStyleSetting.setHeight(10);
        dBlankStyleSetting.setWidth(5);
        dBlankStyleSetting.setLimit("200");
//        dPage.setdBlankStyleSetting(dBlankStyleSetting);

        DOptionStyleSetting dOptionStyleSetting = new DOptionStyleSetting();
        dOptionStyleSetting.setOptionSetting(1);
//        dPage.setdOptionStyleSetting(dOptionStyleSetting);

        System.out.println(GsonUtil.toJson(dPage));

        System.out.println("-----------------说明 Json------------------------");

        //说明 json
        DInstruction dInstruction = new DInstruction();
        dInstruction.setComment("以下几题考研IQ");
        System.out.println(GsonUtil.toJson(dInstruction));
        dInstruction = new DInstruction();
        dInstruction.setComment("以下几题考研AQ");
        System.out.println(GsonUtil.toJson(dInstruction));

        System.out.println("--------------选择题 json----------------------");

        //选择题 json
        DOption dOption = new DOption();

        DOptionStemSetting dOptionStemSetting = new DOptionStemSetting();

        List<DOptionItem> dOptionItems = new ArrayList<DOptionItem>();
        DOptionItem dOptionItem = new DOptionItem();
        dOptionItem.setLabel("label0 ");
        dOptionItems.add(dOptionItem);

        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("label1");
        dOptionItems.add(dOptionItem);

        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("label3");
        dOptionItems.add(dOptionItem);

        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("label4");
        dOptionItems.add(dOptionItem);

        dOptionStemSetting.setOptions(dOptionItems);

        dOptionStemSetting.setQuestion("选择题题干");
//        dOptionStemSetting.setType(1);
        System.out.println(GsonUtil.toJson(dOptionStemSetting));

        System.out.println("-------------填空题 json--------------------------");

        //填空题 json
        DBlank dBlank = new DBlank();
        DBlankStemSetting  dBlankStemSetting = new DBlankStemSetting();
        String question = "今天是星期几?从今天算起第100天是星期几?";
        dBlankStemSetting.setQuestion(question);
        dBlankStemSetting.setType(1);
        dBlank.setdBlankStemSetting(dBlankStemSetting);
        System.out.println(GsonUtil.toJson(dBlankStemSetting));

        System.out.println("-----------上传题 json---------------------");
        //上传题 json
        DAttachment dAttachment = new DAttachment();
        DAttachmentStemSetting  dAttachmentStemSetting = new DAttachmentStemSetting();
        dAttachmentStemSetting.setQuestion("（），（），24678");

//        String[] filePaths = new String[]{"/home/data/a"};
//        dAttachmentStemSetting.setFilePaths(filePaths);
        dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);
        System.out.println(GsonUtil.toJson(dAttachmentStemSetting));

        System.out.println("-------------结束语 json ------------");
        //结束语 json
        DPaperRemark dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(new BigDecimal(0));
        dPaperRemark.setEndScore(new BigDecimal(5));
        dPaperRemark.setDescription("恭喜您！你不够聪明");
        System.out.println(GsonUtil.toJson(dPaperRemark));
        dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(new BigDecimal(6));
        dPaperRemark.setEndScore(new BigDecimal(20));
        dPaperRemark.setDescription("恭喜您！你有点聪明");
        System.out.println(GsonUtil.toJson(dPaperRemark));
        dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(new BigDecimal(21));
        dPaperRemark.setEndScore(new BigDecimal(50));
        dPaperRemark.setDescription("恭喜您！你挺聪明");
        System.out.println(GsonUtil.toJson(dPaperRemark));
        dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(new BigDecimal(51));
        dPaperRemark.setEndScore(new BigDecimal(60));
        dPaperRemark.setDescription("恭喜您！你非常聪明");
        System.out.println(GsonUtil.toJson(dPaperRemark));
    }


    @Test
    public void genJSonScore(){
        //选择题分数
        System.out.println("-------------选择题分数 json ------------");

        DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
        dOptionScoreSetting.setScoreRule(1);
        dOptionScoreSetting.setScore(new BigDecimal(5));

        List<DOptionScoreDetail>  dOptionScoreDetails = new ArrayList<DOptionScoreDetail>();

        DOptionScoreDetail  dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("99e802dd-b79");
        dOptionScoreDetail.setScore(new BigDecimal(5));
        dOptionScoreDetails.add(dOptionScoreDetail);

        dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("bddab098-2d7");
        dOptionScoreDetail.setScore(new BigDecimal(5));
        dOptionScoreDetails.add(dOptionScoreDetail);

        dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("c4610deb-b85");
        dOptionScoreDetail.setScore(new BigDecimal(5));
        dOptionScoreDetails.add(dOptionScoreDetail);

        dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("045b51a2-b1f");
        dOptionScoreDetail.setScore(new BigDecimal(5));
        dOptionScoreDetails.add(dOptionScoreDetail);
//        dOptionScoreSetting.setOptionScoreDetails(dOptionScoreDetails);

        System.out.println(GsonUtil.toJson(dOptionScoreSetting));
        System.out.println("-------------填空题分数 json ------------");

        //选择填空题分数
        DBlankScoreSetting dBlankScoreSetting = new DBlankScoreSetting();
        dBlankScoreSetting.setScoreRule(0);
        dBlankScoreSetting.setExplanation("数手指啊！");
        //*************************************
        List<DBlankScoreDetail> dBlankScoreDetails = new ArrayList<>();
        //---------------------------------------
        DBlankScoreDetail dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setSeqNo(1);
        dBlankScoreDetail.setAnswer("6");
        dBlankScoreDetail.setScore(new BigDecimal(5));
        dBlankScoreDetails.add(dBlankScoreDetail);
        //----------------------------------
        dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setSeqNo(2);
        dBlankScoreDetail.setAnswer("8");
        dBlankScoreDetail.setScore(new BigDecimal(5));
        dBlankScoreDetails.add(dBlankScoreDetail);
        dBlankScoreSetting.setdBlankScoreDetails(dBlankScoreDetails);
        System.out.println(GsonUtil.toJson(dBlankScoreSetting));
        System.out.println("-------------上传题目分数 json ------------");
        //上传题目分数
        DAttachmentScoreSetting  dAttachmentScoreSetting = new DAttachmentScoreSetting();
        dAttachmentScoreSetting.setScore(new BigDecimal(5));
        System.out.println(GsonUtil.toJson(dAttachmentScoreSetting));
    }

    @Test
    public void genJSonScoreStems(){

        List<DItem> dPaperStems  = new ArrayList<>();
        //卷
        DItem dPaperStem = new DItem();
        dPaperStem.setType(1);
        dPaperStem.setUnicode("P1215204600583-header-1");
        dPaperStems.add(dPaperStem);

        //页
        dPaperStem = new DItem();
        dPaperStem.setType(2);
        dPaperStem.setUnicode("P1215204600583-page-2");
        dPaperStems.add(dPaperStem);


        //说明
        dPaperStem = new DItem();
        dPaperStem.setType(3);
        dPaperStem.setUnicode("P1215204600583-instruction-3");
        dPaperStems.add(dPaperStem);


        //选择题
        dPaperStem = new DItem();
        dPaperStem.setType(4);
        dPaperStem.setUnicode("P1215204600583-option-4");
        dPaperStems.add(dPaperStem);


        //填空题

        dPaperStem = new DItem();
        dPaperStem.setType(5);
        dPaperStem.setUnicode("P1215204600583-blank-18");
        dPaperStems.add(dPaperStem);



//----------------------------第二页---------------------------------
        dPaperStem = new DItem();
        dPaperStem.setType(2);
        dPaperStem.setUnicode("P1215204600583-page-8");
        dPaperStems.add(dPaperStem);

        //说明
        dPaperStem = new DItem();
        dPaperStem.setType(3);
        dPaperStem.setUnicode("P1215204600583-instruction-9");
        dPaperStems.add(dPaperStem);

        //上传题
        dPaperStem = new DItem();
        dPaperStem.setType(6);
        dPaperStem.setUnicode("P1215204600583-attachment-13");
        dPaperStems.add(dPaperStem);
        //选择题
        dPaperStem = new DItem();
        dPaperStem.setType(4);
        dPaperStem.setUnicode("P1215204600583-option-11");
        dPaperStems.add(dPaperStem);


        //说明
        dPaperStem = new DItem();
        dPaperStem.setType(3);
        dPaperStem.setUnicode("P1215204600583-instruction-10");
        dPaperStems.add(dPaperStem);

        //填空题
        dPaperStem = new DItem();
        dPaperStem.setType(5);
        dPaperStem.setUnicode("P1215204600583-blank-17");
        dPaperStems.add(dPaperStem);

        //上传题
        dPaperStem = new DItem();
        dPaperStem.setType(6);
        dPaperStem.setUnicode("P1215204600583-attachment-6");
        dPaperStems.add(dPaperStem);


        //结束语
        dPaperStem = new DItem();
        dPaperStem.setType(7);
        dPaperStem.setUnicode("P1215204600583-remark-14");
        dPaperStems.add(dPaperStem);

        //结束语
        dPaperStem = new DItem();
        dPaperStem.setType(7);
        dPaperStem.setUnicode("P1215204600583-remark-15");
        dPaperStems.add(dPaperStem);

        dPaperStem = new DItem();
        dPaperStem.setType(7);
        dPaperStem.setUnicode("P1215204600583-remark-16");
        dPaperStems.add(dPaperStem);
        dPaperStem = new DItem();
        dPaperStem.setType(7);
        dPaperStem.setUnicode("P1215204600583-remark-7");
        dPaperStems.add(dPaperStem);
//-----------------------------------------


        System.out.println(GsonUtil.toJson(dPaperStems));
//        DAttachment dAttachment = new DAttachment();
//        dAttachment.setType(1);
//        dAttachment.setUnicode("aa");
//        dAttachment.setId(1);
//        DAttachmentStemSetting dAttachmentStemSetting =  new DAttachmentStemSetting();
//        dAttachmentStemSetting.setQuestion("asa");
//        dAttachmentStemSetting.setType(1);
//        dAttachment.setdAttachmentStemSetting(dAttachmentStemSetting);
//            List<DItem> items= new ArrayList<>();
//        items.add(dAttachment);
//        Gson gson =new Gson();
//        String listStr= gson.toJson(items);
//
//        System.out.println("---------------------");
//        items =gson.fromJson(listStr, new TypeToken<List<DItem>>() {}.getType());
//
//        DItem ditemAnother =items.get(0);
//        DAttachment attaAnother =(DAttachment)ditemAnother;
//
//        System.out.println(attaAnother);
    }

    @Test
    public void genJSonScoreExaItem(){
        System.out.println(GsonUtil.toJson(new ServiceResponse()));
//        List list = new ArrayList();
//        list.add(1);
//        list.add(2);
//        System.out.println(list.subList(1,2));
//        String[] strs = new String[]{"s","x"};
//        Gson gson =new Gson();
//        System.out.println(gson.toJson(strs));

//        DExamItem dExamItem = new DExamItem();
////        dExamItem.setExamerId(5);
//        dExamItem.setAnswers(new String[]{"maskcode1","maskcode2","maskcode3"});
//        dExamItem.setType(1);
////        dExamItem.setItemSeq(1);
//        System.out.println(gson.toJson(dExamItem));

    }

}
