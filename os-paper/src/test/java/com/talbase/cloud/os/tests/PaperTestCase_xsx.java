package com.talbase.cloud.os.tests;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.common.util.GsonUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suntree.xu on 2016-12-19.
 */
public class PaperTestCase_xsx {

    @Test
    public void genJSonStem(){
        System.out.println("-----------------卷子 json-----------------------");
        DPaper dPaper = new DPaper();
        dPaper.setName("java初级工程师考试");
        dPaper.setComment("这是一套检测java初级工程师的题目");
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

//        DOptionStyleSetting dOptionStyleSetting = new DOptionStyleSetting();
//        dOptionStyleSetting.setOptionSetting(1);
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
        dOptionItem.setLabel("String");
        dOptionItems.add(dOptionItem);

        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("Double");
        dOptionItems.add(dOptionItem);

        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("Char");
        dOptionItems.add(dOptionItem);

        dOptionItem = new DOptionItem();
        dOptionItem.setLabel("Integer");
        dOptionItems.add(dOptionItem);

        dOptionStemSetting.setOptions(dOptionItems);

        dOptionStemSetting.setQuestion("Java中提供了名为（）的包装类来包装原始字符串类型。");
//        dOptionStemSetting.setType(0);
        System.out.println(GsonUtil.toJson(dOptionStemSetting));
        //函数生成
        List<String> option = new ArrayList<String>();
        option.add("toString（）");
        option.add("equals（）");
        option.add("compare（）");
        option.add("以上所有选项都不正确");
        System.out.println(createOptions(option,"java.lang包的（）方法比较两个对象是否相等，相等返回true",0));

        List<String> option1 = new ArrayList<String>();
        option1.add("Map继承List");
        option1.add("List中可以保存Map或List");
        option1.add("Map和List只能保存从数据库中取出的数据");
        option1.add("Map的value可以是List或Map");
        System.out.println(createOptions(option1,"关于Map和List，下面说法正确的是(  )",0));

        //多选题
        List<String> option2 = new ArrayList<String>();
        option2.add("Set");
        option2.add("Map");
        option2.add("HashMap");
        option2.add("List");
        System.out.println(createOptions(option2,"下面的集合中，（）不可以存储重复元素。",1));

        System.out.println("-------------填空题 json--------------------------");

        //填空题 json
        DBlank dBlank = new DBlank();
        DBlankStemSetting  dBlankStemSetting = new DBlankStemSetting();
        String[] questions = new String[]{"什么是封装？Java语言中的封装类有哪些？"};
        dBlankStemSetting.setQuestion(questions.toString());
        dBlankStemSetting.setType(1);
        dBlank.setdBlankStemSetting(dBlankStemSetting);
        System.out.println(GsonUtil.toJson(dBlankStemSetting));

        DBlank dBlank1 = new DBlank();
        DBlankStemSetting  dBlankStemSetting1 = new DBlankStemSetting();
        String[] questions1 = new String[]{"什么是泛型？使用泛型有什么优点？泛型List和普通List有什么区别？"};
        dBlankStemSetting1.setQuestion(questions1.toString());
        dBlankStemSetting1.setType(1);
        dBlank.setdBlankStemSetting(dBlankStemSetting1);
        System.out.println(GsonUtil.toJson(dBlankStemSetting1));

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
        dPaperRemark.setDescription("恭喜您！您得分偏低，对java基础知识还需要加深学习！");
        System.out.println(GsonUtil.toJson(dPaperRemark));
        dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(new BigDecimal(6));
        dPaperRemark.setEndScore(new BigDecimal(20));
        dPaperRemark.setDescription("恭喜您！您得分一般，对java基础知识有所了解，但还需要加强！");
        System.out.println(GsonUtil.toJson(dPaperRemark));
        dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(new BigDecimal(21));
        dPaperRemark.setEndScore(new BigDecimal(50));
        dPaperRemark.setDescription("恭喜您！你得分良好，基本符合初级java工程师的要求！");
        System.out.println(GsonUtil.toJson(dPaperRemark));
        dPaperRemark = new DPaperRemark();
        dPaperRemark.setStartScore(new BigDecimal(51));
        dPaperRemark.setEndScore(new BigDecimal(60));
        dPaperRemark.setDescription("恭喜您！你符合初级java工程师的要求！");
        System.out.println(GsonUtil.toJson(dPaperRemark));
    }


    /**
     * 生成选择题json
     * @param options
     * @param question
     * @param type
     * @return
     */
    public String createOptions(List<String> options,String question,int type){
        DOptionStemSetting dOptionStemSetting = new DOptionStemSetting();

        List<DOptionItem> dOptionItems = new ArrayList<DOptionItem>();
        for(String option:options){
            DOptionItem dOptionItem = new DOptionItem();
            dOptionItem.setLabel(option);
            dOptionItems.add(dOptionItem);
        }
        dOptionStemSetting.setOptions(dOptionItems);

        dOptionStemSetting.setQuestion(question);
//        dOptionStemSetting.setType(type);
       // System.out.println(gson.toJson(dOptionStemSetting));
        return GsonUtil.toJson(dOptionStemSetting);
    }


    @Test
    public void genJSonScore(){
        //选择题分数
        System.out.println("-------------选择题分数 json ------------");

        DOptionScoreSetting dOptionScoreSetting = new DOptionScoreSetting();
        dOptionScoreSetting.setScoreRule(2);
        dOptionScoreSetting.setScore(new BigDecimal(5));

        List<DOptionScoreDetail>  dOptionScoreDetails = new ArrayList<DOptionScoreDetail>();

        DOptionScoreDetail  dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("43a6ec7a-4e1");
        dOptionScoreDetail.setScore(new BigDecimal(0));
        dOptionScoreDetails.add(dOptionScoreDetail);

        dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("df0acbbf-cec");
        dOptionScoreDetail.setScore(new BigDecimal(3));
        dOptionScoreDetails.add(dOptionScoreDetail);

        dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("e8cf41d6-dfd");
        dOptionScoreDetail.setScore(new BigDecimal(3));
        dOptionScoreDetails.add(dOptionScoreDetail);

        dOptionScoreDetail = new DOptionScoreDetail();
        dOptionScoreDetail.setMaskCode("531619f3-870");
        dOptionScoreDetail.setScore(new BigDecimal(0));
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
        dBlankScoreDetail.setAnswer("泛型是对Java语言的数据类型系统的一种扩展，以支持创建可以按类型进行参数化的类。");
        dBlankScoreDetail.setScore(new BigDecimal(5));
        dBlankScoreDetails.add(dBlankScoreDetail);
        //----------------------------------
        dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setSeqNo(2);
        dBlankScoreDetail.setAnswer("提高Java程序的类型安全；消除强制类型转换；提高代码的重用率。");
        dBlankScoreDetail.setScore(new BigDecimal(5));
        dBlankScoreDetails.add(dBlankScoreDetail);

        dBlankScoreDetail = new DBlankScoreDetail();
        dBlankScoreDetail.setSeqNo(3);
        dBlankScoreDetail.setAnswer("泛型List可以实例化为只能存储某种特定类型的数据，普通List可以实例化为存储各种类型的数据。通过使用泛型List对象，可以规范集合对象中存储的数据类型，在获取集合元素时不用进行任何强制类型转换。");
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
        dPaperStem.setUnicode("P1219105151656-header-1");
        dPaperStems.add(dPaperStem);

        //页
        dPaperStem = new DItem();
        dPaperStem.setType(2);
        dPaperStem.setUnicode("P1219105151656-page-2");
        dPaperStems.add(dPaperStem);


        //说明
        dPaperStem = new DItem();
        dPaperStem.setType(3);
        dPaperStem.setUnicode("P1219105151656-instruction-3");
        dPaperStems.add(dPaperStem);


        //选择题
        dPaperStem = new DItem();
        dPaperStem.setType(4);
        dPaperStem.setUnicode("P1219105151656-option-5");
        dPaperStems.add(dPaperStem);

        dPaperStem = new DItem();
        dPaperStem.setType(4);
        dPaperStem.setUnicode("P1219105151656-option-6");
        dPaperStems.add(dPaperStem);

        dPaperStem = new DItem();
        dPaperStem.setType(4);
        dPaperStem.setUnicode("P1219105151656-option-7");
        dPaperStems.add(dPaperStem);

        //说明
        dPaperStem = new DItem();
        dPaperStem.setType(3);
        dPaperStem.setUnicode("P1219105151656-instruction-4");
        dPaperStems.add(dPaperStem);

        dPaperStem = new DItem();
        dPaperStem.setType(4);
        dPaperStem.setUnicode("P1219105151656-option-8");
        dPaperStems.add(dPaperStem);

        //填空题

        dPaperStem = new DItem();
        dPaperStem.setType(5);
        dPaperStem.setUnicode("P1219105151656-blank-10");
        dPaperStems.add(dPaperStem);

        dPaperStem = new DItem();
        dPaperStem.setType(5);
        dPaperStem.setUnicode("P1219105151656-blank-9");
        dPaperStems.add(dPaperStem);


        //上传题
        dPaperStem = new DItem();
        dPaperStem.setType(6);
        dPaperStem.setUnicode("P1219105151656-attachment-11");
        dPaperStems.add(dPaperStem);


        //结束语
        dPaperStem = new DItem();
        dPaperStem.setType(7);
        dPaperStem.setUnicode("P1219105151656-remark-12");
        dPaperStems.add(dPaperStem);

        //结束语
        dPaperStem = new DItem();
        dPaperStem.setType(7);
        dPaperStem.setUnicode("P1219105151656-remark-13");
        dPaperStems.add(dPaperStem);

        dPaperStem = new DItem();
        dPaperStem.setType(7);
        dPaperStem.setUnicode("P1219105151656-remark-14");
        dPaperStems.add(dPaperStem);
        dPaperStem = new DItem();
        dPaperStem.setType(7);
        dPaperStem.setUnicode("P1219105151656-remark-15");
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
