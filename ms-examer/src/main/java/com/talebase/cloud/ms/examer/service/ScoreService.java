package com.talebase.cloud.ms.examer.service;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.domain.TScore;
import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.examer.dao.ScoreMapper;
import com.talebase.cloud.ms.examer.dao.UserExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-12-19.
 */
@Service
public class ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private UserExamMapper userExamMapper;

    @Autowired
    private TaskProgressService taskProgressService;


    /**
     * 保存打分记录
     * @param dScoreJson
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public ServiceResponse save(DScoreJson dScoreJson){
        String json = dScoreJson.getJsonStr();
        String scoreError = "",createError = "";

        if (!StringUtils.isEmpty(json)
                && !StringUtils.isEmpty(dScoreJson.getPaperId())){
            List<Integer> examIds = new ArrayList<>();
            List<Integer> examIdsNew = new ArrayList<>();//新增打分记录
            String creater  = dScoreJson.getCreater();

            List<DScoreJsonValue> jsonValues = GsonUtil.fromJson(json,new TypeToken<List<DScoreJsonValue>>() {
            }.getType());

            for (DScoreJsonValue dScoreJsonValue : jsonValues){
                int done =0;
                int total = 0;
                String attr[] = dScoreJsonValue.getScore();

                if (attr != null && attr.length >0){
                    //String attr[] = scroe.split(",");
                    total = attr.length;
                    String fullScroes[] = dScoreJsonValue.getFullScore();
                    for (int i=0;i<attr.length;i++){
                        if (attr[i] != null && !StringUtils.isEmpty(attr[i].trim())){
                            //Double fullScroe = Double.valueOf(fullScroes[i]);
                            //double doScore = 0.0d;
                            BigDecimal fullScroe =  new BigDecimal(fullScroes[i]);
                            BigDecimal zeroScore = new BigDecimal(0);
                            BigDecimal score = null;
                            try{
                                //doScore = Double.valueOf(attr[i].trim());
                                score = new BigDecimal(attr[i].trim());
                                if (score.compareTo(zeroScore) >= 0){
                                    ++done;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                scoreError +="考生号:"+dScoreJsonValue.getSeq()+",题号:"+dScoreJsonValue.getNumber()+",";
                                if (total >1){
                                    scoreError+= "第"+i+"个打分框,";
                                }
                                scoreError += "请输入数字类型";
                                scoreError +="\n";
                                score = new BigDecimal(0);
                            }
                            //打分大于判分的时候
                            if (score.compareTo(fullScroe) ==1){
                                String msg = "考生号:"+dScoreJsonValue.getSeq()+",题号:"+dScoreJsonValue.getNumber()+"打分大于满分";
                                throw new WrappedException(BizEnums.getCustomize(28008007,msg));
                            }
                            //打分小于零
                            if(zeroScore.compareTo(score)==1){
                                String msg = "考生号:"+dScoreJsonValue.getSeq()+",题号:"+dScoreJsonValue.getNumber()+",打分为负数";
                                throw new WrappedException(BizEnums.getCustomize(28008007,msg));
                            }
                        }
                    }
                }
                //没有打分,不保存;打分非数字类型的也不保存
                if (total == 0 || done ==0){
                    continue;
                }

                TUserExam tUserExam = scoreMapper.getTUserExamByTask(dScoreJson.getTaskId(),dScoreJsonValue.getUserId());
                if (tUserExam == null){
                    throw new WrappedException(BizEnums.ExamNotExistsToSave);
                }
                TScore tScore = scoreMapper.get(dScoreJsonValue.getNumber(),dScoreJson.getPaperId(),tUserExam.getId());
                String saveScroe = org.apache.commons.lang.StringUtils.join(dScoreJsonValue.getScore(),",");
                if (tScore == null){//新增
                    TScore obj = new TScore();
                    obj.setTotal(total);
                    obj.setDone(done);
                    obj.setCreater(creater);
                    obj.setExamId(tUserExam.getId());
                    obj.setSerialNo(dScoreJsonValue.getNumber());
                    obj.setPaperId(dScoreJson.getPaperId());
                    obj.setScore(saveScroe);
                    scoreMapper.insert(obj);

                    if (!examIds.contains(tUserExam.getId())){
                        examIds.add(tUserExam.getId());
                    }
                    if (!examIdsNew.contains(tUserExam.getId())){
                        examIdsNew.add(tUserExam.getId());
                    }
                }else if (tScore.getCreater().equals(creater)){//更新自己创建的
                    tScore.setScore(saveScroe);
                    tScore.setTotal(total);
                    tScore.setDone(done);
                    scoreMapper.updateScore(tScore);

                    if (!examIds.contains(tUserExam.getId())){
                        examIds.add(tUserExam.getId());
                    }
                    if (!examIdsNew.contains(tUserExam.getId())){
                        examIdsNew.add(tUserExam.getId());
                    }
                }else if (!tScore.getCreater().equals(creater)){
                    createError +="考生号:"+dScoreJsonValue.getSeq()+"，题号:"+dScoreJsonValue.getNumber()+",已被打分 \n";
                }
            }

            checkAndUpdateCompleteType(dScoreJson.getSubjectNum(),examIds, examIdsNew);
        }


        //存在打分非数字类型直接抛异常
        if (!"".equals(scoreError)){
            throw new WrappedException(BizEnums.getCustomize(28008005,"打分异常提示:\n" + scoreError));
        }
        //打分题目已被其他考官打分，返回提示信息给前台提示
        if (!"".equals(createError)){
            return  new ServiceResponse( BizEnums.getCustomize(28008006,"已打分数提示:\n" + createError + "您打的分数已过滤"));
        }

        return new ServiceResponse();
    }

    /**
     * 检测分数是否能保存
     * @param dScoreJson
     * @return
     */
    public ServiceResponse checkScore(DScoreJson dScoreJson){
        String json = dScoreJson.getJsonStr();
        if (!StringUtils.isEmpty(json) && !StringUtils.isEmpty(dScoreJson.getPaperId())) {
            String creater = dScoreJson.getCreater();
            List<DScoreJsonValue> jsonValues = GsonUtil.fromJson(json,new TypeToken<List<DScoreJsonValue>>() {
            }.getType());

            String message = "",scoreError="";
            for (DScoreJsonValue dScoreJsonValue : jsonValues){
                int done =0;
                //int total = 0;
                String attr[] = dScoreJsonValue.getScore();
                if (attr != null && attr.length >0){
                    //String attr[] = scroe.split(",");
                    //String fullScroes[] = dScoreJsonValue.getFullScore().split(",");
                    String fullScroes[] = dScoreJsonValue.getFullScore();
                    //total = attr.length;
                    for (int i=0;i<attr.length;i++){
                        if (attr[i] !=null && !StringUtils.isEmpty(attr[i].trim())){
//                            Double fullScroe = Double.valueOf(fullScroes[i]);
                            BigDecimal fullScroe =  new BigDecimal(fullScroes[i]);
                            BigDecimal zeroScore = new BigDecimal(0);
                            BigDecimal score = null;
                            try{
                                score = new BigDecimal(attr[i].trim());
                                if (score.compareTo(zeroScore) >= 0){
                                    ++done;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                                scoreError +="考生号:"+dScoreJsonValue.getSeq()+",题号:"+dScoreJsonValue.getNumber()+"";
                                scoreError+= "第"+i+"个打分框,";
                                scoreError += "请输入数字类型";
                                scoreError +="\n";
                                score = new BigDecimal(0);
                            }
                            //打分大于判分的时候
                            if (score.compareTo(fullScroe) ==1){
                                String msg = "考生号:"+dScoreJsonValue.getSeq()+",题号:"+dScoreJsonValue.getNumber()+",打分大于满分";
                                throw new WrappedException(BizEnums.getCustomize(28008007,msg));
                            }
                            //小于零分
                            if(zeroScore.compareTo(score) ==1){
                                String msg = "考生号:"+dScoreJsonValue.getSeq()+",题号:"+dScoreJsonValue.getNumber()+",打分为负数";
                                throw new WrappedException(BizEnums.getCustomize(28008007,msg));
                            }
                        }
                    }
                }
                TUserExam tUserExam = scoreMapper.getTUserExamByTask(dScoreJson.getTaskId(),dScoreJsonValue.getUserId());
                if (tUserExam == null){
                    throw new WrappedException(BizEnums.ExamNotExistsToSave);
                }
                TScore tScore = scoreMapper.get(dScoreJsonValue.getNumber(),dScoreJson.getPaperId(),tUserExam.getId());
                if (tScore != null
                        && !tScore.getCreater().equals(creater)
                        && done > 0){
                    message +="考生号:"+dScoreJsonValue.getSeq()+"，题号:"+dScoreJsonValue.getNumber()+" \n";
                }

            }

            String returnMsg = "";
            if (!"".equals(scoreError)){
                returnMsg += "打分异常提示:\n";
                returnMsg += scoreError;
            }

            if (!"".equals(message)){
                returnMsg += "打分过滤提示:\n";
                message += "已被其他考官打分，是否过滤";
                returnMsg+=message;
            }

            if (!"".equals(returnMsg)){
                throw new WrappedException(BizEnums.getCustomize(28008006,returnMsg));
            }

        }

        return new ServiceResponse();
    }

    /**
     * 更新评完分数的
     * @param subjectNum
     * @param examIds
     * @param examIdsNew 新打分的
     */
    private void checkAndUpdateCompleteType(Integer subjectNum,List<Integer> examIds, List<Integer> examIdsNew){
        if (examIds.size() >0){
            for (int i = 0;i<examIds.size();i++){
                List<TScore> list = scoreMapper.getCompleteScoreOfExamer(examIds.get(i));
                BigDecimal tatol = new BigDecimal(0);
                boolean canSave = true;
                if (subjectNum == list.size()){
                    for (TScore tScore : list){
                        //String score[] = tScore.getScore().split(",");
                        String score[] = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(tScore.getScore(),",");
                        for (String s : score){
                            if (StringUtils.isEmpty(s)){
                                canSave =false;
                                continue;
                            }
                            tatol = tatol.add(new BigDecimal(s));
                        }
                    }
                    //还有空没打完分
                    if (!canSave){
                        continue;
                    }
                    TUserExam tUserExam = userExamMapper.get(examIds.get(i));
                    TExercise tExercise = userExamMapper.getByUserIdAndTaskId(tUserExam.getUserId(),tUserExam.getTaskId());
                    scoreMapper.updateExerciseScore(tatol.setScale(1).doubleValue(),tUserExam.getUserId(),tUserExam.getTaskId());
                    if(examIdsNew.contains(examIds.get(i)) && StringUtils.isEmpty(tExercise.getSubScore())){//若完成评分且当次评分为一个新增的评分记录时，则记录该考生为已评卷
                        taskProgressService.markedTask(tUserExam.getTaskId());
                    }
                }
            }
        }
    }

/*    public List<DUserExam> getExamListOfSubject(Map<String,String> map){
        String taskId = map.get("taskId");
        String serial = map.get("serial");
        String paperId = map.get("paperId");
        return scoreMapper.getExamListOfSubject(paperId,serial,taskId);
    }*/

/*    public List<TScore> getScoreListOfExamer(Map<String,String> map){
        String examId = map.get("examId");
        return scoreMapper.geScoreListOfExamer(examId);
    }*/

    /**
     * 查询考生评分情况分页列表
     * @param taskId
     * @param pageRequest
     * @return
     */
    public PageResponse<DExamineeInTask> getTaskDetail(Integer taskId, PageRequest pageRequest){

        PageResponse<DExamineeInTask> pageResponse = new PageResponse(pageRequest);
        Integer total = scoreMapper.getTaskDetailNum(taskId);

        pageResponse.setTotal(total);
        if(total > 0){
            List<DExamineeInTask> list = scoreMapper.getTaskDetail(taskId, pageRequest);
            pageResponse.setResults(list);
        }

        return pageResponse;
    }

    public DNextExamUserResp nextExcecise(DNextExamUserReq req){
        TExercise currExercise = scoreMapper.getExercise(req.getTaskId(), req.getUserId());
        Integer seq = currExercise == null ? 0 : currExercise.getSerialNo() == null ? 0 : currExercise.getSerialNo();
        //获取下一个交卷记录
        TExercise nextExercise = null;
        //点击查看评卷情况考生，未评试题时，直接显示该考生
        if (currExercise != null
                && StringUtils.isEmpty(currExercise.getSubScore())
                && !StringUtils.isEmpty(currExercise.getSerialNo())
                && StringUtil.isEmpty(req.getPreOrNext())){
            nextExercise = currExercise;
        }
        //获取下一个未评考生
        if (nextExercise == null){
            nextExercise =   scoreMapper.getNextExamUser(req.getTaskId(), seq,DNextExamUserReq.INGORE_YES.equals(req.getIngoreMarke()), req.getPreOrNext());
        }
        //点击下一步或上一步
        if(!StringUtil.isEmpty(req.getPreOrNext())){
            if(nextExercise == null){//若为空则重头再找一次
                Integer sreach = 0;
                if("next".equals(req.getPreOrNext())){//往后找的
                    sreach = 0;
                }else{//重后往前找
                    sreach = scoreMapper.getMaxSerialNo(req.getTaskId(), false);
                    if (sreach != null){
                        ++sreach;
                    }
                }

                nextExercise = scoreMapper.getNextExamUser(req.getTaskId(), sreach, DNextExamUserReq.INGORE_YES.equals(req.getIngoreMarke()), req.getPreOrNext());
            }
            //向上或向下都找不到数据，则将自身字段返回
            if (nextExercise == null){
                nextExercise = scoreMapper.getNextExamUser(req.getTaskId(), seq, DNextExamUserReq.INGORE_YES.equals(req.getIngoreMarke()), "");
            }
        }

        if(nextExercise == null){
            seq = scoreMapper.getMaxSerialNo(req.getTaskId(), false);
            if(seq == null || seq == 0){
                throw new WrappedException(BizEnums.NoOneFinish);
            }
            throw new WrappedException(BizEnums.AllFinish);
        }

        TUserExam userExam = userExamMapper.getUserExamByTaskAndUser(nextExercise.getTaskId(), nextExercise.getUserId());

        if(userExam == null){
            throw new WrappedException(BizEnums.DataNotComplete);
        }

        List<TScore> scoreList = scoreMapper.getScoreListOfExamer(userExam.getId());

        DNextExamUserResp resp = new DNextExamUserResp();
        resp.setExercise(nextExercise);
        resp.setUserExam(userExam);
        resp.setScoreList(scoreList);

        return resp;
    }

    public List<Integer> getMarkedQz(Integer taskId) {
        Integer finishCnt = scoreMapper.getFinishNum(taskId);
        finishCnt = finishCnt == null ? 0 : finishCnt;
        List<Integer> markedQz = scoreMapper.getMarkedQz(taskId, finishCnt);
        return markedQz;
    }

    public List<DNextQzResp> getNextQzForUsers(Integer taskId, Integer serialNo) {
        return scoreMapper.getNextQzForUsers(taskId, serialNo);
    }

    public Map<String,Map<String,double[]>> getScoreByTaskId(Integer taskId){
        Map<String,Map<String,double[]>> mapMap = new HashMap<>();
        List<TUserExam> tUserExamsList = userExamMapper.getUserExamByTaskId(taskId,TUserExamStatus.DELETE.getValue());
        if (tUserExamsList != null && tUserExamsList.size()>0){
            for (int i=0;i<tUserExamsList.size();i++){
                TUserExam tUserExam = tUserExamsList.get(i);
                List<TScore> tScores = scoreMapper.getScoreByExamId(tUserExam.getId());

                Map<String,double[]> map = new HashMap<>();
                for (int k=0;k<tScores.size();k++){
                    TScore tScore = tScores.get(k);
                    String[] scoreAttr = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(tScore.getScore(),",");
                    double[] doubles = new double[scoreAttr.length];
                    for (int j=0;j<scoreAttr.length;j++){
                        double score = -1D;
                        if (!StringUtils.isEmpty(scoreAttr[j])){
                            score = Double.valueOf(scoreAttr[j]);
                        }
                        doubles[j] = score;
                    }
                    map.put(tScore.getSerialNo(),doubles);
                }

                mapMap.put(String.valueOf(tUserExam.getUserId()),map);
            }
        }
        return mapMap;
    }

}
