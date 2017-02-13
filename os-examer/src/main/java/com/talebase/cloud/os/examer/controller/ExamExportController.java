package com.talebase.cloud.os.examer.controller;

import com.talebase.cloud.base.ms.examer.dto.DExamAnswerItem;
import com.talebase.cloud.base.ms.examer.dto.DTaskExamInfo;
import com.talebase.cloud.base.ms.examer.dto.DUserExamInfo;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DItemType;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ExcelUtil;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.os.examer.service.ExamExportService;
import com.talebase.cloud.os.examer.vo.ExcelEntity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@RestController
public class ExamExportController {

    @Autowired
    private ExamExportService examExportService;
    @Autowired
    RedisTemplate redisTemplate;

    List<Integer> typeNotQuestion = Arrays.asList(2, 3);
    List<Integer> typeObj = Arrays.asList(4, 5, 6);

    /**
     * 试卷导出
     * @param paperId
     * @return
     */
    @GetMapping("/question/paper/export/{taskId}/{paperId}")
    public ServiceResponse export(HttpServletResponse response, @PathVariable("paperId") Integer paperId,
                                  @PathVariable("taskId") Integer taskId) throws Exception {
        //获得所有的考生
        List<DUserExamInfo> dUserExamInfos = examExportService.findExamers(taskId).getResponse();
        DTaskExamInfo dTaskExamInfo = examExportService.getTaskExamInfo(taskId).getResponse();
        //根据考生 获得打乱后的试卷以及客观题列表(),主观题,组装出数据列表
        //获得主观题打分列表
        Map<String,Map<String,double[]>> itemSubsOfAllUsers = examExportService.getScoreByTaskId(taskId);
        DUserExamInfo  dUserExamInfo;
        int userId;
        String hashKey;
        BoundHashOperations bop;
        DWPaperResponse dWPaperResponse;
        DExamAnswerItem dExamAnswerItem;
        String projectName = dTaskExamInfo.getTaskName();
        String productName = dTaskExamInfo.getProjectName();
        String account ;
        String name;
        String itemType="";
        int type;
        String jsonStr;
        DOption dOption;
        DBlank dBlank;
        DAttachment dAttachment;
        String stem="";
        ExcelEntity excelEntity = null ;
        List<ExcelEntity> excelList = new ArrayList<>();
        for (int i=0; i<dUserExamInfos.size();i++){
            dUserExamInfo = dUserExamInfos.get(i);
            account= dUserExamInfo.getAccount();
            name = dUserExamInfo.getName();
            userId = dUserExamInfo.getId();
            hashKey = "T" + taskId + "-P" + paperId + "-E" + userId;
            bop = redisTemplate.boundHashOps(hashKey);
            dWPaperResponse = (DWPaperResponse)bop.get("Paper");
            List<DWPage> pages = dWPaperResponse.getPages();
            int seqNo = 0;
            for (DWPage page : pages) {
                for (Object obj : page.getItems()) {
                    Map itemToMap = (Map) obj;
                    type = (Integer) itemToMap.get("type");
                    if (typeNotQuestion.contains(type) ) {//说明/分页符直接跳过
                        continue;
                    }
                    String answerKey = String.format("Q-%d-%d", type, itemToMap.get("id"));
                    Object answerObj = redisTemplate.boundHashOps(hashKey).get(answerKey);
                    if (answerObj == null){
                        continue;//当题没作答，跳过
                    }
                    dExamAnswerItem = (DExamAnswerItem) answerObj;
                    seqNo++;
                    if(type==4){
                        itemType="单选题";
                        jsonStr = GsonUtil.toJson(itemToMap);
                        dOption = GsonUtil.fromJson(jsonStr,DOption.class);
                        stem = dOption.getdOptionStemSetting().getQuestion();
                    }else if(type==5){
                        itemType="多选题";
                        jsonStr = GsonUtil.toJson(itemToMap);
                        dOption = GsonUtil.fromJson(jsonStr,DOption.class);
                        stem = dOption.getdOptionStemSetting().getQuestion();
                    }else if(type==6){
                        itemType="客观填空题";
                        jsonStr = GsonUtil.toJson(itemToMap);
                        dBlank = GsonUtil.fromJson(jsonStr,DBlank.class);
                        stem = dBlank.getdBlankStemSetting().getQuestion();
                        if(dBlank.getdBlankStemSetting().getType().intValue()==0){//主观填空题
                            itemType  = "主观填空题";
                            Map<String,double[]> itemSubs = itemSubsOfAllUsers.get(String.valueOf(userId));
                            double[] scores = itemSubs.get(String.valueOf(seqNo));
                            excelEntity = new ExcelEntity(projectName,
                                    productName,account,
                                    name,itemType,
                                    stem,
                                    dExamAnswerItem.getAnswers(),scores);
                            excelList.add(excelEntity);
                            continue;
                        }
                    }else if(type==7){
                        itemType="上传题";
                        jsonStr = GsonUtil.toJson(itemToMap);
                        dAttachment = GsonUtil.fromJson(jsonStr,DAttachment.class);
                        stem = dAttachment.getdAttachmentStemSetting().getQuestion();
                    }
                    if(typeObj.contains(type)){//"单选题" "多选题" "填空题"
                        excelEntity = new ExcelEntity(projectName,
                                productName,account,
                                name,itemType,
                                stem,
                                dExamAnswerItem.getAnswers(),dExamAnswerItem.getScores());
                    }else if(DItemType.ATTACHMENT.getValue()==type){//"上传题"
                        Map<String,double[]> itemSubs = itemSubsOfAllUsers.get(String.valueOf(userId));
                        double[] scores = itemSubs.get(String.valueOf(seqNo));
                        excelEntity = new ExcelEntity(projectName,
                                productName,account,
                                name,itemType,
                                stem,
                                dExamAnswerItem.getAnswers(),scores);
                    }
                    excelList.add(excelEntity);
                }
            }
        }

        //写出到excel
        HSSFWorkbook workbook = new HSSFWorkbook();
        createExcel(workbook, excelList,"详细试卷");
        //返回到输出流
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=" + new String("详细试卷".getBytes("utf8"), "iso-8859-1"));
        response.setContentType("application/octet-stream; charset=utf-8");
        workbook.write(output);
        output.close();
        return new ServiceResponse();
    }

    public static void main1(String[] args) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        List<ExcelEntity> excelList = new ArrayList<>();
        //单选题
        ExcelEntity excelEntity = new ExcelEntity("2017年中大春招水平考试",
                "初级技术人员水平考试","lijiajia",
                "李佳佳","单选题",
                "为什么你是这份工作的最佳人选？",
                new String[]{"你们需要可以生产出“效益”的人，而我的背景和经验可以证明我的能力。"},new double[]{2.0});
        excelList.add(excelEntity);

        //
        excelEntity = new ExcelEntity("2017年中大春招水平考试",
                "初级技术人员水平考试","lijiajia",
                "李佳佳","单选题",
                "你的优势是什么？",
                new String[]{"通过我们之间的交流，我觉得这里是一个很好的工作地点。"},new double[]{2.0});
        excelList.add(excelEntity);

        //
        excelEntity = new ExcelEntity("2017年中大春招水平考试",
                "初级技术人员水平考试","lijiajia",
                "李佳佳","多选题",
                "你的优势是什么？",
                new String[]{"行政许可的内容是国家许可的活动",
                        "行政许可是依申请的行政行为",
                        "行政许可的内容是国家一般禁止的活动"
                },new double[]{2.0});
        excelList.add(excelEntity);
        //
        excelEntity = new ExcelEntity("2017年中大春招水平考试",
                "初级技术人员水平考试","lijiajia",
                "李佳佳","客观填空题",
                "煤气的三大危害是（）、（）、（）。" ,
                new String[]{"爆炸","","着火"
                },new double[]{2.0,1.0,3.0});
        excelList.add(excelEntity);
       createExcel(workbook,excelList,"test.xls");
    }

    public static void createExcel(HSSFWorkbook workbook ,List<ExcelEntity> list, String fileName){
        String[] titleT = {"项目名称","产品名称","帐号","姓名","题型","题干","答案","实际得分"};
        String[] titleO = {"projectName","productName","account","name","type","stem","answer","score"};
        String multiRow = "Answer";
        new ExcelUtil<ExcelEntity>().generateExamSheetExcel(workbook,
                FileSystemView.getFileSystemView().getHomeDirectory().getPath()+"/"+fileName,
                "题目数据",multiRow,6,titleO,titleT,list,ExcelEntity.class);
    }
}
