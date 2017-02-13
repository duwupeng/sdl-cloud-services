package com.talebase.cloud.os.examer.controller;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DItemType;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.examer.service.ConsumptionService;
import com.talebase.cloud.os.examer.service.ExamProjectService;
import com.talebase.cloud.os.examer.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kanghong.zhao on 2017-1-12.
 */

@RestController
public class ExercisesController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ExamProjectService examProjectService;

    @Autowired
    ExerciseService exerciseService;

    @Autowired
    ConsumptionService consumptionService;

    @Value("${schedule.enable}")
    Boolean scheduleEnable;

    private final static String PaperKey = "Paper";
    private final static String ChangeLimitKey = "ChangeLimit";
    private final static Integer TopicNum = 50; //查询记录数
    private final static Integer DelaySecond = 3;//延迟秒数
    private static List<DTimerExercise> timerList = new ArrayList<>();

    @Value("${file.attachmentPath}")
    String attachmentPath;

    @Value("${file.domain}")
    String fileDomain;

    List<Integer> typeObj = Arrays.asList(4, 5, 6);
    List<Integer> typeNotQuestion = Arrays.asList(2, 3);

    @GetMapping("/exercise/item/{taskId}/{paperId}/{seqNo}")
    public ServiceResponse nextItem(@PathVariable("taskId") Integer taskId, @PathVariable("paperId") Integer paperId, @PathVariable("seqNo") Integer seqNo) {
        if (seqNo == null) {
            return new ServiceResponse();
        }
        //拿试卷
        DWPaperResponseWithUserPermission paperWithPermission = getUserPaper(taskId, paperId);
        DWPaperResponse userPaper = paperWithPermission.paper;
        DUserExamPermission userExamPermission = paperWithPermission.userExamPermission;
//
//        //若有考试时长，则是开考时间+时长-当前时间；若无考试时长，则是结束时间-当前时间
        long examTimeRest = (userExamPermission.getTaskExamTime() == null || userExamPermission.getTaskExamTime() == 0) ?
                (userExamPermission.getTaskEndDate().getTime() - new Date().getTime()) : (userExamPermission.getTaskExamTime() * 60 * 1000 + userExamPermission.getExerciseStartTime().getTime() - new Date().getTime());

//        DWPaperResponse userPaper = dispatchedPaper(taskId, paperId);
//        DUserExamPermission userExamPermission = new DUserExamPermission();
//        userExamPermission.setTaskExamTime(null);
//        userExamPermission.setTaskEndDate(new Date(new Date().getTime() + 3600000));

//        DWPaperResponse userPaper = dispatchedPaper(taskId,paperId);
        //找出考生的答案
        Map map = findItem(seqNo, userPaper);
        if (map != null) {
            //返回拿题
            int itemId = (Integer) map.get("id");
            int type = (Integer) map.get("type");
            DExamItem dExamItem = convertToDExamItem(seqNo, map);
            DExamItemResponse dExamItemResponse = new DExamItemResponse();
            dExamItemResponse.setItem(dExamItem);
            dExamItemResponse.setName(userPaper.getdPaper().getName());
            dExamItemResponse.setSystemTime(new Date().getTime());
            //给到前端的结束时间提前3秒
            dExamItemResponse.setEndTime(((userExamPermission.getTaskExamTime() == null || userExamPermission.getTaskExamTime() == 0) ?
                    userExamPermission.getTaskEndDate().getTime() : (userExamPermission.getTaskExamTime() * 60 * 1000 + userExamPermission.getExerciseStartTime().getTime())) - 3000);
            //获得倒计时  TODO
            dExamItemResponse.setTimeLeft(examTimeRest + "");

            dExamItemResponse.setTotal(userPaper.getdPaper().getTotalNum());

            //获得hashKey
            String hashKey = geneUserExamKey(taskId, paperId,ServiceHeaderUtil.getRequestHeader().getCustomerId());
            BoundHashOperations bop = redisTemplate.boundHashOps(hashKey);
            List keys = (List) redisTemplate.boundHashOps(hashKey).get("answered");
            if (keys == null) {
                keys = new ArrayList<>();
            }
            dExamItemResponse.setFinished(keys.size());
            dExamItemResponse.setAnswered(keys);
            //设置考生答案
            String answerKey = String.format("Q-%d-%d", type, itemId);
            DExamAnswerItem dExamAnswerItem = (DExamAnswerItem) bop.get(answerKey);
            List<DOptionItem> dOptionItems;
            if (dExamAnswerItem != null) {
                String[] answers = dExamAnswerItem.getAnswers();
                //为选择题设置选项
                if (dExamItem.getType() == 4 || dExamItem.getType() == 5) {
                    dOptionItems = dExamItem.getdOptionItems();
                    for (DOptionItem dOptionItem : dOptionItems) {
                        for (String answer : answers) {
                            if (dOptionItem.getMaskCode().equals(answer)) {
                                dOptionItem.setAnswer(true);
                                break;
                            }
                        }
                    }
                } else {//为填空题或者附件题设置答案
                    dExamItem.setAnswers(answers);
                }
            }
            //组装返回包
            return new ServiceResponse(dExamItemResponse);
        }
        return new ServiceResponse();
    }

    private Map findItem(int seqNo, DWPaperResponse userPaper) {
        List<DWPage> pages = userPaper.getPages();
        List instructionPre = new ArrayList();
        int counter = 0;
        DWPage dWPage;
        List items;
        Map result = null;
        Map mapTemp;
        boolean hasFound = false;
        for (int i = 0; i < pages.size() && !hasFound; i++) {
            dWPage = pages.get(i);
            items = dWPage.getItems();
            for (int j = 0; j < items.size() && !hasFound; j++) {
                mapTemp = (Map) items.get(j);
                //如果是说明类型，则缓存起来
                if ((Integer) mapTemp.get("type") == 3 && counter <= seqNo) {
                    DInstruction dInstruction = GsonUtil.fromJson(GsonUtil.toJson(mapTemp), DInstruction.class);
                    instructionPre.add(dInstruction.getComment());
                }
                if ((Integer) mapTemp.get("type") != 2 && (Integer) mapTemp.get("type") != 3) {
                    counter++;
                    if (counter == seqNo) {
                        mapTemp.put("instructionPre", instructionPre);
                        result = mapTemp;
                        hasFound = true;
                    } else {
                        //清空说明题目
                        instructionPre = new ArrayList();
                    }
                }
            }
        }
//        List instructionPos = new ArrayList();
//        DInstruction dInstruction ;
//        for (int i = 0; i<pages.size();i++) {
//            dWPage =pages.get(i);
//            items = dWPage.getItems();
//            for(int j = 0;j<items.size(); j++){
//                //作为最后一题，需要拿出后面的说明
//                if(counter>=userPaper.getTotal()){
//                    mapTemp = (Map) items.get(j);
//                    dInstruction = GsonUtil.fromJson(GsonUtil.toJson(mapTemp),DInstruction.class);
//                    instructionPos.add(dInstruction.getComment());
//                }
//            }
//        }
//        result.put("instructionPost",instructionPos);
        return result;
    }

    //返回拿题
    private DExamItem convertToDExamItem(int seqNo, Map map) {
        List instructionPre = (List) map.get("instructionPre");
        List instructionPos = (List) map.get("instructionPost");
        map.remove("instructionPre");
        map.remove("instructionPost");


        DExamItem dExamItem = new DExamItem();
        dExamItem.setInstructionPre(instructionPre);
        dExamItem.setInstructionPos(instructionPos);
        int type = (Integer) map.get("type");
        dExamItem.setType(type);
        dExamItem.setId((Integer) map.get("id"));

        if (type == 4 || type == 5) { //选择题
            String jsonStr = GsonUtil.toJson(map);
            DOption dOption = GsonUtil.fromJson(jsonStr, DOption.class);
            dExamItem.setSeqNo(seqNo);
            dExamItem.setQuestion(dOption.getdOptionStemSetting().getQuestion());

            //原始试卷的答案应该先置为false
            List<DOptionItem> optms = dOption.getdOptionStemSetting().getOptions();
            for (DOptionItem dOptionItem : optms) {
                dOptionItem.setAnswer(false);
            }
            dExamItem.setdOptionItems(dOption.getdOptionStemSetting().getOptions());
        } else if (type == 6) {//填空题
            String jsonStr = GsonUtil.toJson(map);
            DBlank dBlank = GsonUtil.fromJson(jsonStr, DBlank.class);
            dExamItem.setSeqNo(seqNo);
            String[] answers = new String[dBlank.getdBlankStemSetting().getNumbers()];
            for (int i = 0; i < answers.length; i++) {
                answers[i] = "";
            }
            dExamItem.setAnswers(answers);
            dExamItem.setBlankLimit(dBlank.getdBlankStyleSetting()==null?"":dBlank.getdBlankStyleSetting().getLimit());
            dExamItem.setQuestion(dBlank.getdBlankStemSetting().getQuestion());
        } else if (type == 7) {//附件题
            String jsonStr = GsonUtil.toJson(map);
            DAttachment dAttachment = GsonUtil.fromJson(jsonStr, DAttachment.class);
            dExamItem.setSeqNo(seqNo);
            dExamItem.setQuestion(dAttachment.getdAttachmentStemSetting().getQuestion());
            //TODO
            dExamItem.setaTypes(dAttachment.getdAttachmentStemSetting().getType());
        }
        return dExamItem;
    }

    /**
     * 答题,保存题目
     *
     * @param taskId
     * @param paperId
     * @param seqNo
     * @param jsonStr
     * @return
     */
    @PostMapping("/exercise/answer/{taskId}/{paperId}/{seqNo}")
    public ServiceResponse nextSeq(@PathVariable("taskId") Integer taskId, @PathVariable("paperId") Integer paperId, @PathVariable("seqNo") Integer seqNo, String jsonStr) {
        //先检验是否还能答题
        checkAnswerStatus(taskId, paperId);
        //保存答题到redis
        answer(taskId, paperId, jsonStr);

        return new ServiceResponse();
    }

    @PostMapping("/exercise/flush/{taskId}/{paperId}")
    public ServiceResponse nextSeq(@PathVariable("taskId") Integer taskId, @PathVariable("paperId") Integer paperId, String jsonStr) {
        //先检验是否还能答题
//        checkAnswerStatus(taskId, paperId);
        Integer userId = ServiceHeaderUtil.getRequestHeader().getCustomerId();
        checkAnswerStatus(taskId, paperId);
        //保存答题到redis
        answer(taskId, paperId, jsonStr);
        return submit(taskId, paperId,userId,ServiceHeaderUtil.getRequestHeader(),BigDecimal.ZERO.intValue());
    }

    /**
     * 附件删除
     * @param taskId
     * @param paperId
     * @param seqNo
     * @param fileName
     * @return
     * @throws Exception
     */
    @DeleteMapping("/exercise/attachment/{taskId}/{paperId}/{seqNo}/{fileName}/{suffix}")
    public ServiceResponse attachmentDelete(@PathVariable int taskId,
                                            @PathVariable int paperId,
                                            @PathVariable int seqNo,
                                            @PathVariable String fileName,@PathVariable String suffix) throws Exception {
        File file = new File(attachmentPath + "answer" + File.separator + taskId + File.separator + paperId
                + File.separator + ServiceHeaderUtil.getRequestHeader().getCustomerId()
                + File.separator + seqNo+File.separator  + fileName+ "." +suffix);
        file.delete();
        return new ServiceResponse<>();
    }

    /**
     *附件下载
     * @param res
     * @param taskId
     * @param paperId
     * @param itemId
     * @param fileName
     * @throws Exception
     */
    @GetMapping("/exercise/attachment/download/{taskId}/{paperId}/{itemId}/{fileName}")
    public ServiceResponse attachmentDownload(HttpServletResponse res,
                                              @PathVariable int taskId,
                                              @PathVariable int paperId,
                                              @PathVariable int itemId,
                                              @PathVariable String fileName)  throws Exception {
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename="+fileName+".jpg");

        File file = new File(attachmentPath + "answer" + File.separator + taskId + File.separator + paperId
                + File.separator + ServiceHeaderUtil.getRequestHeader().getCustomerId()
                + File.separator + itemId+File.separator +fileName);
        InputStream reader = null;
        OutputStream out = null;
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            //resp.setHeader("content-disposition", "attachment;fileName="+fileName);
            //如果图片名称是中文需要设置转码
            res.setHeader("content-disposition", "attachment;fileName="+ URLEncoder.encode(fileName, "UTF-8"));
            // 读取文件
            reader = new FileInputStream(file);
            // 写入浏览器的输出流
            out = res.getOutputStream();

            while ((len = reader.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (out != null)
                    out.close();
            }catch (Exception t){
                t.printStackTrace();
            }
        }
        return new ServiceResponse();
    }

    /**
     * 附件上传
     * @param files
     * @param taskId
     * @param paperId
     * @param itemId
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/exercise/attachment/upload/{taskId}/{paperId}/{itemId}")
    public ServiceResponse<String> attachmentUpload(@RequestParam("file") MultipartFile[] files, @PathVariable int taskId,
                                                    @PathVariable int paperId, @PathVariable int itemId, HttpServletRequest request) throws Exception {

        //先检验是否还能答题
        checkAnswerStatus(taskId, paperId);

        if (files == null) {
            throw new WrappedException(BizEnums.EmptyFile);
        }

        MultipartFile file = files[0];
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new WrappedException(BizEnums.FileTooBig);
        }

        String filePath = attachmentPath;
        String relatePath = "answer" + File.separator + taskId + File.separator + paperId + File.separator + ServiceHeaderUtil.getRequestHeader().getCustomerId() + File.separator + itemId + File.separator + file.getOriginalFilename();
        relatePath = fileDomain + relatePath.replace(File.separator, "/");
        //文件保存到本地
        File localFile = null;
        try {
            String filename = new String(file.getOriginalFilename().getBytes("iso-8859-1"), "utf8");
            File dir = new File(filePath + "answer" + File.separator + taskId + File.separator + paperId + File.separator + ServiceHeaderUtil.getRequestHeader().getCustomerId() + File.separator + itemId);
            dir.mkdirs();
            localFile = new File(dir, filename);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localFile));
            out.write(file.getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //return"上传失败,"+e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            //return"上传失败,"+e.getMessage();
        }
        return new ServiceResponse<>(relatePath);
    }

    //发卷
    private DWPaperResponse dispatchedPaper(Integer taskId, Integer paperId) {
        String key = genePaperKey(taskId, paperId);
        //  examerKey
        String examerKey = geneUserExamKey(taskId, paperId,ServiceHeaderUtil.getRequestHeader().getCustomerId());
        BoundHashOperations bop = redisTemplate.boundHashOps(examerKey);

        DWPaperResponse oriPaper = null;
        if (!redisTemplate.hasKey(key)) {
            // Get paper from  paper module by paperId
            oriPaper = exerciseService.getPaper(paperId);
            if (oriPaper == null) {
                throw new WrappedException(BizEnums.NoPaper);
            }
            // save the original paper to redis
            redisTemplate.opsForValue().set(key, oriPaper);
        } else {
            oriPaper = (DWPaperResponse) redisTemplate.opsForValue().get(key);
        }
        DWPaperResponse userPaper;
        // compose the examer paper
        if (bop.hasKey(PaperKey)) {
            userPaper = (DWPaperResponse) bop.get(PaperKey);
        } else {
            userPaper = exerciseService.composePaper(oriPaper);
            bop.put(PaperKey, userPaper);
        }
        //返回卷子
        return userPaper;
    }

    private DWPaperResponseWithUserPermission getUserPaper(Integer taskId, Integer paperId) {
        //获取考生、考试的各种判断校验的状态
        DUserExamPermission userExamPermission = examProjectService.getUserExamPermission(taskId);

        boolean needCost = userExamPermission.getExerciseStartTime() == null;//若要发卷则需要扣费的标志
        DWPaperResponse userPaper = getUserPaperFromRedis(taskId, paperId);//从redis读出已缓存的试卷
        if (userPaper == null) {//未发卷或找不到试卷，需要发卷
            if (needCost) {//先去扣费和修改考试状态
                if (!userExamPermission.getTaskPaperId().equals(paperId)) {//试卷已更换
                    throw new WrappedException(BizEnums.PaperUpdate);
                }

                if (userExamPermission.getTaskStatus() != TTaskStatus.ENABLE.getValue()) {//任务状态禁用或者已删除，未发卷的不予考试
                    throw new WrappedException(BizEnums.TaskIsStop);
                }

                if (userExamPermission.getTaskLatestStartDate() != null && userExamPermission.getTaskLatestStartDate().before(new Date())) {//已超过最迟考试时间
                    throw new WrappedException(BizEnums.TaskTooLate);
                }

                consumptionService.cost(userExamPermission);//扣费
                exerciseService.start(taskId, paperId);//开考
            }
            userPaper = dispatchedPaper(taskId, paperId);
        }

        return new DWPaperResponseWithUserPermission(userPaper, userExamPermission);
    }

    private DWPaperResponse getUserPaperFromRedis(Integer taskId, Integer paperId) {
        Integer userId = ServiceHeaderUtil.getRequestHeader().getCustomerId();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(geneUserExamKey(taskId, paperId,userId));
        Object obj = hashOperations.get(PaperKey);
        return obj == null ? null : (DWPaperResponse) obj;
    }

    private Integer getChangeTime(Integer taskId, Integer paperId) {
        Integer userId = ServiceHeaderUtil.getRequestHeader().getCustomerId();
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(geneUserExamKey(taskId, paperId,userId));
        Object obj = hashOperations.get(ChangeLimitKey);
        return obj == null ? 0 : (Integer) obj;
    }
//    private DWPaperResponse getExamPaper(Integer taskId, Integer paperId){
//        Object obj = redisTemplate.opsForValue( ).get(genePaperKey(taskId, paperId));
//        return obj == null ? null : (DWPaperResponse)obj;
//    }

    private String geneUserExamKey(Integer taskId, Integer paperId,Integer userId) {
        /*return "T" + taskId + "-P" + paperId + "-E" + ServiceHeaderUtil.getRequestHeader().getCustomerId();*/
        return "T" + taskId + "-P" + paperId + "-E" + userId;
    }

    private String genePaperKey(Integer taskId, Integer paperId) {
        return "T" + taskId + "-P" + paperId;
    }

    private ServiceResponse submit(Integer taskId, Integer paperId, Integer userId, ServiceHeader header,Integer submitType) {

        String hashKey = geneUserExamKey(taskId, paperId,userId);
        DWPaperResponse userPaper = (DWPaperResponse) redisTemplate.boundHashOps(hashKey).get(PaperKey);

        BigDecimal objScore = BigDecimal.ZERO;
        int subCnt = userPaper.getdPaper().getSubjectNum();
        BigDecimal sujScore = userPaper.getdPaper().getSubjectNum() > 0 ? null : BigDecimal.ZERO;

        List<DWPage> pages = userPaper.getPages();
//        Integer seqNo = 0;
        for (DWPage page : pages) {
            for (Object obj : page.getItems()) {
                Map itemToMap = (Map) obj;
                int type = (Integer) itemToMap.get("type");
                if (typeNotQuestion.contains(type)) {//说明/分页符直接跳过
                    continue;
                }
//              seqNo++;//题目的话加序号
                if (!typeObj.contains(type)) {//非客观题题型直接跳过
                    continue;
                }
                String answerKey = String.format("Q-%d-%d", type, itemToMap.get("id"));
                Object answerObj = redisTemplate.boundHashOps(hashKey).get(answerKey);
                if (answerObj == null)
                    continue;//当题没作答，跳过
                DExamAnswerItem dExamAnswerItem = (DExamAnswerItem) answerObj;
//              String[] answersTocheck = dExamAnswerItem.getAnswers();
//              String jsonStr = GsonUtil.toJson(itemToMap);
                BigDecimal score = culScore(itemToMap, dExamAnswerItem, type);

                //保存打分结果
                redisTemplate.boundHashOps(hashKey).put(answerKey,dExamAnswerItem);

                objScore = objScore.add(score);
            }
        }

        List<DPaperRemark> dPaperRemarks = userPaper.getdPaperRemarks();
        String comment = null;
        for (DPaperRemark dPaperRemark : dPaperRemarks) {
            if (objScore.compareTo(dPaperRemark.getStartScore()) >= 0 && objScore.compareTo(dPaperRemark.getEndScore()) <= 0) {
                comment = dPaperRemark.getDescription();
                if (comment.contains("$$考试得分$$")) {
                    comment = comment.replace("$$考试得分$$", objScore + "");
                }
            }
        }

        TExercise exercise = new TExercise();
        exercise.setTaskId(taskId);
//        exercise.setUserId(ServiceHeaderUtil.getRequestHeader().getCustomerId());
        exercise.setUserId(userId);
        exercise.setObjScore(objScore);
        exercise.setSubScore(sujScore);
        exercise.setSubmitType(submitType);
        exerciseService.submit(exercise,header);
        return new ServiceResponse(comment);
    }

    private BigDecimal culScore(Map itemMap, DExamAnswerItem dExamAnswerItem, int type) {
        String jsonStr = GsonUtil.toJson(itemMap);
        String[] answersTocheck = dExamAnswerItem.getAnswers();
        double[] scores;
        if (type == 4 || type == 5) {
            DOption dOption = GsonUtil.fromJson(jsonStr, DOption.class);
            DOptionScoreSetting dOptionScoreSetting = dOption.getdOptionScoreSetting();
            if (type == 4) {
                String answer = dOptionScoreSetting.getAnswer();
                if (answersTocheck != null && answersTocheck.length > 0 && Arrays.asList(answersTocheck).contains(answer)) {
                    scores = new double[]{dOptionScoreSetting.getScore().doubleValue()};
                    dExamAnswerItem.setScores(scores);
                    return dOptionScoreSetting.getScore();
                }else{
                    scores = new double[]{0d};
                    dExamAnswerItem.setScores(scores);
                    return BigDecimal.ZERO;
                }
            } else {
                String[] answers = dOptionScoreSetting.getAnswers();
                List<String> answerList = Arrays.asList(answers);
                List<String> answerToCheckList =  Arrays.asList(answersTocheck);
                answerToCheckList = answerToCheckList.stream().filter(answer->!answer.equals("")).collect(Collectors.toList());
                if(answerToCheckList.size()==0){
                    scores = new double[]{0d};
                    dExamAnswerItem.setScores(scores);
                    return BigDecimal.ZERO;
                }
                boolean containAll = answerList.containsAll(answerToCheckList);
                if (!containAll) {//0分
                    scores = new double[]{0d};
                    dExamAnswerItem.setScores(scores);
                    return BigDecimal.ZERO;
                }

                if (answerList.size() == answerToCheckList.size()) {//全对
                    scores = new double[]{dOptionScoreSetting.getScore().doubleValue()};
                    dExamAnswerItem.setScores(scores);
                    return dOptionScoreSetting.getScore();
                } else {//部分对
                    if (dOptionScoreSetting.getScoreRule() == 2) {
                        scores = new double[]{BigDecimal.valueOf(dOptionScoreSetting.getScore().doubleValue() *
                                (answerToCheckList.size() / Double.valueOf(answerList.size()))).doubleValue()};
                        dExamAnswerItem.setScores(scores);
                        return BigDecimal.valueOf(dOptionScoreSetting.getScore().doubleValue() *
                                (answerToCheckList.size() / Double.valueOf(answerList.size())));
                    } else if (dOptionScoreSetting.getScoreRule() == 1) {
                        scores = new double[]{dOptionScoreSetting.getScore().doubleValue()};
                        dExamAnswerItem.setScores(scores);
                        return dOptionScoreSetting.getSubScore();
                    } else if (dOptionScoreSetting.getScoreRule() == 0) {
                        scores = new double[]{0d};
                        dExamAnswerItem.setScores(scores);
                        return BigDecimal.ZERO;
                    }
                }

            }
        } else if (type == 6) {//客观填空题判分
            DBlank dBlank = GsonUtil.fromJson(jsonStr, DBlank.class);
            List<DBlankScoreDetail> dBlankScoreDetails;
            dBlankScoreDetails = dBlank.getdBlankScoreSetting().getdBlankScoreDetails();
            String blankAnswer[];
            BigDecimal score = BigDecimal.ZERO;
            DBlankScoreDetail dBlankScoreDetail;
            if (dBlank.getdBlankStemSetting().getType() == 1) {//客观填空题
                if (dBlank.getdBlankScoreSetting().getScoreRule() == 0) {//完全一致才给分
                    scores = new double[dBlankScoreDetails.size()];
                    for (int i = 0; i < dBlankScoreDetails.size(); i++) {
                        dBlankScoreDetail = dBlankScoreDetails.get(i);
                        blankAnswer = dBlankScoreDetail.getAnswer().split("\\|\\|");
                        if (answersTocheck != null && answersTocheck.length > i) {
                            if (Arrays.asList(blankAnswer).contains(answersTocheck[i])) {
                                score = score.add(dBlankScoreDetail.getScore());
                                scores[i] = dBlankScoreDetail.getScore().doubleValue();
                            }else{
                                scores[i] =0.0d;
                            }
                        }
                    }
                    dExamAnswerItem.setScores(scores);
                }else if (dBlank.getdBlankScoreSetting().getScoreRule() == 1) {//仅顺序不一致的时候
                    scores = new double[dBlankScoreDetails.size()];
                    for (String answerTocheck : answersTocheck) {
                        for (int i = 0; i < dBlankScoreDetails.size(); i++) {
                            dBlankScoreDetail = dBlankScoreDetails.get(i);
                            blankAnswer = dBlankScoreDetail.getAnswer().split("\\|\\|");
                            if (Arrays.asList(blankAnswer).contains(answerTocheck)) {
                                dBlankScoreDetails.remove(i);
                                score = score.add(dBlankScoreDetail.getScore());
                                scores[i] = dBlankScoreDetail.getScore().doubleValue();
                            }else{
                                scores[i] =0.0d;
                            }
                        }
                    }
                    dExamAnswerItem.setScores(scores);
                }
                return score;
            }  else {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    private void answer(Integer taskId, Integer paperId, String jsonStr) {
        if (jsonStr != null) {
            String hashKey = geneUserExamKey(taskId, paperId,ServiceHeaderUtil.getRequestHeader().getCustomerId());
            BoundHashOperations bop = redisTemplate.boundHashOps(hashKey);
            //保存考生答案json
            List<DExamAnswerItem> dExamItems = GsonUtil.fromJson(jsonStr, new TypeToken<List<DExamAnswerItem>>() {
            }.getType());
            DExamAnswerItem dExamItem = null;
            String answerKey;
            String[] answers = null;
            List<Integer> answeredList = (List<Integer>) bop.get("answered");
            for (int i = 0; i < dExamItems.size(); i++) {
                dExamItem = dExamItems.get(i);
                answerKey = String.format("Q-%d-%d", dExamItem.getType(), dExamItem.getId());
                bop.put(answerKey, dExamItem);
                answers = dExamItem.getAnswers();
                if(dExamItem.getType().intValue()== DItemType.ATTACHMENT.getValue()){
                    dExamItem.setAnswers(Arrays.copyOfRange(answers, 1,answers.length));
                }
                if (answeredList == null) {
                    answeredList = new ArrayList<>();
                }
                if (answers != null && !answeredList.contains(dExamItem.getSeqNo())) {
                    answeredList.add(dExamItem.getSeqNo());
                }
            }
            boolean isEmpty = true;
            if (answers != null) {
                for (int j = 0; j < answers.length; j++) {
                    if (!answers[j].equals("")) {
                        isEmpty = false;
                    }
                }
                if (isEmpty) {
                    answeredList.remove(new Integer(dExamItem.getSeqNo()));
                }
            }
            bop.put("answered", answeredList);
        }
    }

    /**
     * 检查考生能否答题
     *
     * @param taskId
     * @param paperId
     */
    private void checkAnswerStatus(Integer taskId, Integer paperId) {
        //获取考生、考试的各种判断校验的状态
        DUserExamPermission userExamPermission = examProjectService.getUserExamPermission(taskId);
        if (userExamPermission.getExerciseStartTime() == null) {
            throw new WrappedException(BizEnums.NotInErr);
        }
        if (userExamPermission.getExerciseEndTime() != null) {
            throw new WrappedException(BizEnums.UserExamIsFinish);
        }
        if (userExamPermission.getTaskExamTime() != null && userExamPermission.getTaskExamTime() > 0) {
            if ((userExamPermission.getTaskExamTime() * 60 * 1000 + userExamPermission.getExerciseStartTime().getTime()) < new Date().getTime()) {
                throw new WrappedException(BizEnums.ExamTimeOver);
            }
        }
        if (userExamPermission.getTaskPageChangeLimit() != null && userExamPermission.getTaskPageChangeLimit() > 0) {
            if (getChangeTime(taskId, paperId) > userExamPermission.getTaskPageChangeLimit()) {
//                submit(taskId, paperId);//超过最大次数自动交卷
                throw new WrappedException(BizEnums.ExamTimeOver);
            }
        }
        if (!userExamPermission.getExercisePaperId().equals(paperId)) {
            throw new WrappedException(BizEnums.AnswerPaperErr);
        }
    }

    @PostMapping("/exercise/change/{taskId}/{paperId}")
    public ServiceResponse addChangeLimit(@PathVariable("taskId") Integer taskId, @PathVariable("paperId") Integer paperId) {
        Integer userId = ServiceHeaderUtil.getRequestHeader().getCustomerId();
        //获取考生、考试的各种判断校验的状态
        DUserExamPermission userExamPermission = examProjectService.getUserExamPermission(taskId);
        Integer limitNow = getChangeTime(taskId, paperId);
        BoundHashOperations bop = redisTemplate.boundHashOps(geneUserExamKey(taskId, paperId,userId));
        bop.put(ChangeLimitKey, limitNow + 1);
        if (userExamPermission.getTaskPageChangeLimit() != null && userExamPermission.getTaskPageChangeLimit() > 0) {
            if (getChangeTime(taskId, paperId) > userExamPermission.getTaskPageChangeLimit()) {
//                submit(taskId, paperId);//超过最大次数自动交卷
                throw new WrappedException(BizEnums.ExamTimeOver);
            }
        }
        return new ServiceResponse();
    }

    private class DWPaperResponseWithUserPermission {
        DWPaperResponse paper;
        DUserExamPermission userExamPermission;

        public DWPaperResponseWithUserPermission(DWPaperResponse paper, DUserExamPermission userExamPermission) {
            this.paper = paper;
            this.userExamPermission = userExamPermission;
        }
    }

    //定时器提交
    public void submit(DTimerExercise dTimerExercise) {
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.customerId = dTimerExercise.getUserId();
        serviceHeader.customerName =dTimerExercise.getAccount();
        serviceHeader.companyId =  dTimerExercise.getCompanyId();
        submit(dTimerExercise.getTaskId(), dTimerExercise.getPaperId(),dTimerExercise.getUserId(),serviceHeader,BigDecimal.ONE.intValue());
    }

//    private List<String> toList(String[] args) {
//        List<String> list = new ArrayList<>();
//        if (args != null) {
//            for (String str : args) {
//                list.add(str);
//            }
//        }
//        return list;
//    }

    /**
     * 定时提交试卷
     */
    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    protected void submitPaperTimer() {

        if (!scheduleEnable)
            return;

        try{
            if (timerList== null || timerList.isEmpty()) {
                timerList = exerciseService.getExerciseByTimer(TopicNum, DelaySecond);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (timerList != null && timerList.size() > 0) {
            for (int i = 0; i < timerList.size(); i++) {
                DTimerExercise dTimerExercise = timerList.get(i);
                try {
                    submit(dTimerExercise);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                timerList.remove(i);
            }
        }
    }
}
