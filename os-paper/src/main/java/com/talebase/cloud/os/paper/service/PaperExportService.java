package com.talebase.cloud.os.paper.service;

import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.*;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ExcelUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.common.util.TimeUtil;
import com.talebase.cloud.os.paper.vo.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-21.
 */

@Service
public class PaperExportService {
    @Autowired
    MsInvoker msInvoker;
    final static String PAPER_SERVICE_NAME = "ms-paper";
    @Autowired
    PaperService paperService;

    public String export(HSSFWorkbook workbook, Integer paperId) {
        DPaperResponse dPaperResponse = getExportData(paperId);
        DPaper dPaper = dPaperResponse.getdPaper();
        List<DOption> dOptions = dPaperResponse.getdOptions();
        List<DBlank> dBlanks = dPaperResponse.getdBlanks();
        List<DAttachment> dAttachments = dPaperResponse.getdAttachments();
        List<SingleOption> singleOptions = new ArrayList<>();
        List<MultipleOption> multipleOptions = new ArrayList<>();
        List<ObjBlank> objBlanks = new ArrayList<>();
        List<SubBlank> subBlanks = new ArrayList<>();
        List<Attachment> attachments = new ArrayList<>();
        getOptions(singleOptions, multipleOptions, dOptions);
        getBlanks(objBlanks, subBlanks, dBlanks);
        getAttachments(attachments, dAttachments);
        String projectName = dPaper.getName().replaceAll("/", "").replace(":", "");
        String fileName = projectName + "题目导出表-" + TimeUtil.formatDateForFileMinute(new Date()) + ".xls";
        createSingleOptionExcel(singleOptions, fileName, "单选题", workbook);
        createMultipleOptionExcel(multipleOptions, fileName, "多选题", workbook);
        createObjBlankExcel(objBlanks, fileName, "客观填空题", workbook);
        createSubBlankExcel(subBlanks, fileName, "主观填空题", workbook);
        createAttachmentExcel(attachments, fileName, "上传题", workbook);
        return fileName;

    }

    public static void main1(String[] args) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        String fileName = "上传题" + "题目导出表-" + TimeUtil.formatDateForFileMinute(new Date()) + ".xls";
//        List<SingleOption> singleOptions = new ArrayList<>();
//        SingleOption singleOption =  new SingleOption();
//        singleOption.setAnswer("4");
//        singleOption.setTitle("为什么你是这份工作的最佳人选？" );
//        singleOption.setType("单选题");
//        singleOption.setIndex("position");
//        singleOption.setScore(BigDecimal.valueOf(2));
//        singleOption.setOptions(new String[]{"我干过不少这种职位，我的经验将帮助我胜任这一岗位。",
//                "我干什么都很出色。",
//                "通过我们之间的交流，我觉得这里是一个很好的工作地点。",
//                "你们需要可以生产出“效益”的人，而我的背景和经验可以证明我的能力。"});
//        singleOptions.add(singleOption);
//
//        createSingleOptionExcel(singleOptions, fileName, "单选题", workbook);

//        List<ObjBlank> objBlanks = new ArrayList<>();
//        ObjBlank objBlank = new ObjBlank();
//        objBlank.setType("客观填空题");
//        objBlank.setTitle("煤气的三大危害是（）、（）、（）。");
//        objBlank.setOptions(new String[]{"","",""});
//        objBlank.setAnswers(new String[]{"爆炸","中毒","着火"});
//        objBlank.setScores(new String[]{"2","2","2"});
//        objBlank.setIndex("position");
//        objBlank.setScoreRule("仅顺序一致");
//        objBlank.setExplanation("");
//        objBlanks.add(objBlank);

//        createObjBlankExcel(objBlanks, fileName, "客观填空题", workbook);
        List<Attachment> dAttachments = new ArrayList<>();
        Attachment attachment = new Attachment();
        attachment.setTitle("请上传你觉得不错的作品，上传张数控制在3张内，要求：是自己最近三年独立完成的作品；命名格式要求：作品名-年月日-姓名");
        attachment.setType("上传题");
        attachment.setScore(BigDecimal.valueOf(5));
        dAttachments.add(attachment);

        createAttachmentExcel(dAttachments, fileName, "上传题", workbook);

    }

    public static void createSingleOptionExcel(List<SingleOption> list, String fileName, String sheetName, HSSFWorkbook workbook) {

        String[] titleT = {"题型", "题目标题", "选项序号", "题目选项", "答案", "本题分数"};
        String[] titleO = {"type", "title", "index", "options", "answer", "score"};
        String multiRows = "Options";
        int multiRowColIndexs = 3;
        new ExcelUtil<SingleOption>().generateOptionSheet(workbook,
                FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/" + fileName,
                sheetName, multiRows, multiRowColIndexs, titleO, titleT, list, SingleOption.class);
    }

    public static void createMultipleOptionExcel(List<MultipleOption> list, String fileName, String sheetName, HSSFWorkbook workbook) {

        String[] titleT = {"题型", "题目标题", "选项序号", "题目选项", "答案", "本题分数", "分数设置", "少选统一给分"};
        String[] titleO = {"type", "title", "index", "options", "answer", "score", "scoreSet", "scoreLess"};
        String multiRows = "Options";
        int multiRowColIndexs = 3;
        new ExcelUtil<MultipleOption>().generateOptionSheet(workbook,
                FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/" + fileName,
                sheetName, multiRows, multiRowColIndexs, titleO, titleT, list, MultipleOption.class);
    }

    public static void createObjBlankExcel(List<ObjBlank> list, String fileName, String sheetName, HSSFWorkbook workbook) {

        String[] titleT = {"题型", "题目标题", "选项序号", "题目选项", "答案", "本题分数", "系统评分标准", "题目解析"};
        String[] titleO = {"type", "title", "index", "options", "answers", "scores", "scoreRule", "explanation"};
        int multiRowColIndexs = 3;
        new ExcelUtil<ObjBlank>().generateBlankSheet(workbook,
                FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/" + fileName,
                sheetName, multiRowColIndexs, titleO, titleT, list, ObjBlank.class);
    }

    public static void createSubBlankExcel(List<SubBlank> list, String fileName, String sheetName, HSSFWorkbook workbook) {

        String[] titleT = {"题型", "题目标题", "选项序号", "题目选项", "答案", "本题分数", "系统评分标准", "题目解析"};
        String[] titleO = {"type", "title", "index", "options", "answers", "scores", "scoreRule", "explanation"};
        int multiRowColIndexs = 3;
        new ExcelUtil<SubBlank>().generateBlankSheet(workbook,
                FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/" + fileName,
                sheetName, multiRowColIndexs, titleO, titleT, list, SubBlank.class);
    }

    public static void createAttachmentExcel(List<Attachment> list, String fileName, String sheetName, HSSFWorkbook workbook) {

        String[] titleT = {"题型", "题目标题", "本题分数"};
        String[] titleO = {"type", "title", "score"};
        new ExcelUtil<Attachment>().generateAttachmentSheet(workbook,
                FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "/" + fileName,
                sheetName, titleO, titleT, list, Attachment.class);
    }

    private void getOptions(List<SingleOption> singleOptions, List<MultipleOption> multipleOptions, List<DOption> dOptions) {
        for (DOption dOption : dOptions) {
            if (dOption.getType() == DItemType.SINGLE_CHOICE.getValue()) {
                SingleOption singleOption = new SingleOption();
                singleOption.setType("单选题");
                singleOption.setIndex("position");
                singleOption.setTitle(StringUtil.replaceHtml(dOption.getdOptionStemSetting().getQuestion()));
                //设置选项
                List<DOptionItem> dOptionItems = dOption.getdOptionStemSetting().getOptions();
                String[] options = new String[dOptionItems.size()];
                for (int i = 0; i < dOptionItems.size(); i++) {
                    DOptionItem dOptionItem = dOptionItems.get(i);
                    options[i] = StringUtil.replaceHtml(dOptionItem.getLabel());
                    //设置答案
                    if (dOptionItem.isAnswer()) {
                        singleOption.setAnswer(String.valueOf(i + 1));
                    } else {
                        singleOption.setAnswer(" ");
                    }
                }
                singleOption.setOptions(options);
                //设置分数
                DOptionScoreSetting dOptionScoreSetting = dOption.getdOptionScoreSetting();
                singleOption.setScore(dOptionScoreSetting.getScore());

                singleOptions.add(singleOption);
            } else if (dOption.getType() == DItemType.MULTIPLE_CHOICE.getValue()) {
                MultipleOption multipleOption = new MultipleOption();
                multipleOption.setType("多选题");
                multipleOption.setIndex("position");
                multipleOption.setTitle(StringUtil.replaceHtml(dOption.getdOptionStemSetting().getQuestion()));
                //设置选项
                List<DOptionItem> dOptionItems = dOption.getdOptionStemSetting().getOptions();
                String[] options = new String[dOptionItems.size()];
                String answer = "";
                for (int i = 0; i < dOptionItems.size(); i++) {
                    DOptionItem dOptionItem = dOptionItems.get(i);
                    options[i] = StringUtil.replaceHtml(dOptionItem.getLabel());
                    if (dOptionItem.isAnswer()) {
                        answer = answer + (i + 1) + ",";
                    }
                }
                multipleOption.setOptions(options);

                //设置答案
                multipleOption.setAnswer(answer.substring(0, answer.length() - 1) + " ");

                //设置分数
                DOptionScoreSetting dOptionScoreSetting = dOption.getdOptionScoreSetting();
                //该题总分
                multipleOption.setScore(dOptionScoreSetting.getScore());

                //设置计分规则
                if (dOption.getdOptionScoreSetting().getScoreRule() == TOptionScoreRule.ALL.getValue()) {
                    multipleOption.setScoreSet("全部答对才给分");
                } else if (dOption.getdOptionScoreSetting().getScoreRule() == TOptionScoreRule.PART_UNITY.getValue()) {
                    multipleOption.setScoreSet("少选统一给分");
                    multipleOption.setScoreLess(dOptionScoreSetting.getSubScore());
                } else {
                    multipleOption.setScoreSet("少选按比例给分");
                    multipleOption.setScoreLess(dOptionScoreSetting.getSubScore());
                }

                multipleOptions.add(multipleOption);
            }
        }
    }

    private void getBlanks(List<ObjBlank> objBlanks, List<SubBlank> subBlanks, List<DBlank> dBlanks) {
        for (DBlank dBlank : dBlanks) {
            if (dBlank.getdBlankStemSetting().getType() == TBlankType.OBJECTIVE.getValue()) {
                ObjBlank objBlank = new ObjBlank();
                objBlank.setType("客观填空题");
                objBlank.setIndex("position");
                objBlank.setTitle(StringUtil.replaceHtml(dBlank.getdBlankStemSetting().getQuestion()));
                //设置分数明细
                List<DBlankScoreDetail> dBlankScoreDetails = dBlank.getdBlankScoreSetting().getdBlankScoreDetails();
                String[] options = new String[dBlankScoreDetails.size()];
                String[] scores = new String[dBlankScoreDetails.size()];
                String[] answers = new String[dBlankScoreDetails.size()];
                for (int i = 0; i < dBlankScoreDetails.size(); i++) {
                    DBlankScoreDetail dBlankScoreDetail = dBlankScoreDetails.get(i);
                    answers[i] = dBlankScoreDetail.getAnswer();
                    scores[i] = String.valueOf(dBlankScoreDetail.getScore());
                    options[i] = " ";
                }
                objBlank.setOptions(options);
                objBlank.setScores(scores);
                objBlank.setAnswers(answers);
                //设置计分规则
                String scoreRule = "";
                if (dBlank.getdBlankScoreSetting().getScoreRule() == TBlankScoreRule.IN_FULL_ACCORD.getValue()) {
                    scoreRule = "完全一致";
                } else if (dBlank.getdBlankScoreSetting().getScoreRule() == TBlankScoreRule.SEQUENTIAL_INCONSISTENCY.getValue()) {
                    scoreRule = "仅顺序不一致";
                } else {
                    scoreRule = "仅供参考";
                }
                objBlank.setScoreRule(scoreRule);
                objBlank.setExplanation(dBlank.getdBlankScoreSetting().getExplanation());

                objBlanks.add(objBlank);
            } else {
                SubBlank subBlank = new SubBlank();
                subBlank.setType("主观填空题");
                subBlank.setIndex("position");
                subBlank.setTitle(StringUtil.replaceHtml(dBlank.getdBlankStemSetting().getQuestion()));
                //设置分数明细
                List<DBlankScoreDetail> dBlankScoreDetails = dBlank.getdBlankScoreSetting().getdBlankScoreDetails();
                String[] options = new String[dBlankScoreDetails.size()];
                String[] scores = new String[dBlankScoreDetails.size()];
                String[] answers = new String[dBlankScoreDetails.size()];
                for (int i = 0; i < dBlankScoreDetails.size(); i++) {
                    DBlankScoreDetail dBlankScoreDetail = dBlankScoreDetails.get(i);
                    answers[i] = dBlankScoreDetail.getAnswer();
                    scores[i] = String.valueOf(dBlankScoreDetail.getScore());
                    options[i] = "";
                }
                subBlank.setOptions(options);
                subBlank.setScores(scores);
                subBlank.setAnswers(answers);
                //设置计分规则
                String scoreRule = "";
                if (dBlank.getdBlankScoreSetting().getScoreRule() == TBlankScoreRule.IN_FULL_ACCORD.getValue()) {
                    scoreRule = "完全一致";
                } else if (dBlank.getdBlankScoreSetting().getScoreRule() == TBlankScoreRule.SEQUENTIAL_INCONSISTENCY.getValue()) {
                    scoreRule = "仅顺序不一致";
                } else {
                    scoreRule = "仅供参考";
                }
                subBlank.setScoreRule(scoreRule);
                subBlank.setExplanation(dBlank.getdBlankScoreSetting().getExplanation());

                subBlanks.add(subBlank);
            }
        }
    }

    private void getAttachments(List<Attachment> attachments, List<DAttachment> dAttachments) {
        for (DAttachment dAttachment : dAttachments) {
            Attachment attachment = new Attachment();
            attachment.setType("上传题");
            attachment.setTitle(StringUtil.replaceHtml(dAttachment.getdAttachmentStemSetting().getQuestion()));
            attachment.setScore(dAttachment.getdAttachmentScoreSetting().getScore());
            attachments.add(attachment);
        }
    }

    private DPaperResponse getExportData(Integer paperId) {
        String servicePath = "http://" + PAPER_SERVICE_NAME + "/question/paper/forExport/" + paperId;
        ServiceResponse<DPaperResponse> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DPaperResponse>>() {
        });
        return response.getResponse();
    }
}
