package com.talebase.cloud.os.examer.service;

import com.talebase.cloud.base.ms.examer.domain.TScore;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.paper.enums.DItemType;
import com.talebase.cloud.base.ms.project.dto.DExamTaskResponse;
import com.talebase.cloud.base.ms.project.dto.DTaskMarked;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.os.examer.config.conf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

/**
 * Created by eric.du on 2016-12-19.
 */
@Service
public class EvaluateService {
    final static String EXAM_SERVICE_NAME = "ms-examer";
    final static String PROJECT_SERVICE_NAME = "ms-project";
    final static String PAPER_SERVICE_NAME = "ms-paper";

    @Autowired
    MsInvoker msInvoker;

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    RedisTemplate redisTemplate;



    public List<Integer> getMarkedQz(Integer taskId){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/markedQz/"+taskId;
        ServiceResponse<List<Integer>> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<Integer>>>() {
        });
        return response.getResponse();
    }


    public List<DNextQzResp> getNextQzForUsers(Integer taskId,int seqNo){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/users/"+taskId+"/"+seqNo;
        ServiceResponse<List<DNextQzResp>> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<DNextQzResp>>>() {
        });
        return response.getResponse();
    }
    /**
     *按试题评卷
     * @param taskId
     * @param paperId
     * @param itemHashKey
     * @return
     */
    public  List<DEvaluateSubject.ExamerAnswers> getStemsBySeqNo( DEvaluateItemDetail dEvaluateItemDetail,List<DNextQzResp> dNextQzResps, int taskId,int paperId,String  itemHashKey){
        List<DEvaluateSubject.ExamerAnswers>  dExamerAnswers=new ArrayList<>();
        DExamAnswerItem dExamAnswerItem;
        DEvaluateSubject.ExamerAnswers dExamerAnswer;
        DNextQzResp dNextQzResp;
        String hashKey;
        for(int i=0;i<dNextQzResps.size();i++){
            dNextQzResp = dNextQzResps.get(i);
            hashKey =  "T"+taskId+ "-"+"P"+paperId+"-E"+dNextQzResp.getUserId();
            dExamAnswerItem = (DExamAnswerItem)redisTemplate.boundHashOps(hashKey).get(itemHashKey);
            dExamerAnswer = new DEvaluateSubject().new ExamerAnswers();
            List<Map<String,Object>> listMap = new ArrayList<>();
            if (dExamAnswerItem != null)
                for (String s : dExamAnswerItem.getAnswers()){
                    Map<String,Object> answerMap = new HashMap<>();
                    if ((Integer)dEvaluateItemDetail.getType()==7){
                        answerMap.put("type",0);
                    }else{
                        answerMap.put("type",3);
                    }
                    answerMap.put("answer",s);
                    listMap.add(answerMap);
                }
            //dExamerAnswer.setAnswers();
            dExamerAnswer.setAnswers(listMap);
            dExamerAnswer.setExamerId(dNextQzResp.getUserExamId());
            dExamerAnswer.setSeq(dNextQzResp.getSerialNo());
            dExamerAnswer.setSpaceNum(dEvaluateItemDetail.getSpaceNum());
            dExamerAnswers.add(dExamerAnswer);
            dExamerAnswer.setMarkScore(dNextQzResp);
        }
        return dExamerAnswers;
    }

    /**
     * 根据考生获得所有题目
     * @param taskId
     * @param paperId
     * @return
     */
    public DEvaluateExamer getStemsByExamer(int taskId,int paperId,Integer userId){
        //获取源试卷
        String hashKey = "T" + taskId + "-P" + paperId + "-E" + userId;
        BoundHashOperations<String, String, Object> hashOperations = redisTemplate.boundHashOps(hashKey);
        DWPaperResponse dWPaperResponse = (DWPaperResponse)hashOperations.get("Paper");
        //不存在考生做题数据
        if (dWPaperResponse == null){
            throw new WrappedException(BizEnums.ExamNotExistsToSave);
        }
        //初始化返回包
        DEvaluateExamer dEvaluateExamer  = new DEvaluateExamer();
        //dEvaluateExamer.setExamerId(examerId);
//        dEvaluateExamer.setSeq(dExamineeInTask.getSerialNo());
        dEvaluateExamer.setExamerId(userId);
        List<DEvaluateItemDetail> dItemDetails = new ArrayList<>();

        //找题目
        List<DWPage> pages = dWPaperResponse.getPages();
        DWPage dWPage;
        List items;
        Map mapTemp;
        DBlank dBlank = null;
        DAttachment dAttachment = null;
        String jsonStr = null;
        DExamAnswerItem answer;
        int counter=0;
        DEvaluateItemDetail dEvaluateItemDetail = null;
        for (int i = 0; i<pages.size();i++){
            dWPage =pages.get(i);
            items = dWPage.getItems();
            for(int j = 0;j<items.size(); j++){
                mapTemp = (Map) items.get(j);
                if((Integer)mapTemp.get("type")!=2&&(Integer)mapTemp.get("type")!=3){
                    counter++;
                }
                //只返回主观选择题
                if((Integer)mapTemp.get("type")==6){
                    jsonStr = GsonUtil.toJson(mapTemp);
                    dBlank = GsonUtil.fromJson(jsonStr,DBlank.class);
                    if(dBlank.getdBlankStemSetting().getType()==0){

                        dEvaluateItemDetail = new DEvaluateItemDetail();
                        dEvaluateItemDetail.setSeqNo(counter);
                        dEvaluateItemDetail.setType(6);
                        dEvaluateItemDetail.setStem(dBlank.getdBlankStemSetting().getQuestion());
                        dEvaluateItemDetail.setId(dBlank.getId());

                        String answerKey = String.format("Q-%d-%d", DItemType.BLANK.getValue(),(Integer)mapTemp.get("id"));
                        answer= (DExamAnswerItem)hashOperations.get(answerKey);
                        List<Map<String,Object>> listMap = new ArrayList<>();
                        if(answer!=null){
                            //dEvaluateItemDetail.setAnswers(answer.getAnswers());
                            for(String s : answer.getAnswers()){
                                Map<String,Object> mapObject = new HashMap<>();
                                mapObject.put("answer",s);
                                mapObject.put("type",3);
                                listMap.add(mapObject);
                            }
                            dEvaluateItemDetail.setAnswers(listMap);
                        }else{
                            //dEvaluateItemDetail.setAnswers(new String[dBlank.getdBlankStemSetting().getNumbers()]);
                            dEvaluateItemDetail.setAnswers(listMap);
                        }
                        dEvaluateItemDetail.setSpaceNum(dBlank.getdBlankStemSetting().getNumbers());
                        dEvaluateItemDetail.setReferenceAnswer(dBlank.getdBlankScoreSetting().getExplanation()==null?"":dBlank.getdBlankScoreSetting().getExplanation());

                        List<DBlankScoreDetail> dBlankScoreDetails = dBlank.getdBlankScoreSetting().getdBlankScoreDetails();
                        List<String> listScores = new ArrayList();
                        DBlankScoreDetail dBlankScoreDetail;
                        for (int k=0; k<dBlankScoreDetails.size();k++){
                            dBlankScoreDetail= dBlankScoreDetails.get(k);
                            listScores.add(dBlankScoreDetail.getScore().doubleValue()+"");
                        }
                        dEvaluateItemDetail.setScores(listScores);
                        dItemDetails.add(dEvaluateItemDetail);
                        //设置长宽高
                        dEvaluateItemDetail.setdBlankStyleSetting(dBlank.getdBlankStyleSetting());
                    }
                    //只返回附件上传题
                }else if((Integer)mapTemp.get("type")==7){
                    jsonStr  = GsonUtil.toJson(mapTemp);
                    dAttachment = GsonUtil.fromJson(jsonStr,DAttachment.class);
                    dEvaluateItemDetail = new DEvaluateItemDetail();
                    dEvaluateItemDetail.setSeqNo(counter);
                    dEvaluateItemDetail.setType(7);
                    dEvaluateItemDetail.setStem(dAttachment.getdAttachmentStemSetting().getQuestion());
                    dEvaluateItemDetail.setId(dAttachment.getId());
                    dEvaluateItemDetail.setScore(dAttachment.getdAttachmentScoreSetting().getScore());
                    List<String> scroesList = new ArrayList<String>();
                    scroesList.add(dAttachment.getdAttachmentScoreSetting().getScore().toString());
                    dEvaluateItemDetail.setScores(scroesList);
                    String answerKey = String.format("Q-%d-%d", DItemType.ATTACHMENT.getValue(),(Integer)mapTemp.get("id"));
                    answer= (DExamAnswerItem)hashOperations.getOperations().opsForHash().get(hashKey, answerKey);
                    dEvaluateItemDetail.setReferenceAnswer(dAttachment.getdAttachmentScoreSetting().getScoreRule()==null?"":dAttachment.getdAttachmentScoreSetting().getScoreRule());
                    List<Map<String,Object>> listMap = new ArrayList<>();
                    if(answer!=null){
                        for (String s : answer.getAnswers()){
                            Map<String,Object> answerMap = new HashMap<String,Object>();
                            answerMap.put("answer",s);
                            answerMap.put("type",0);
                            listMap.add(answerMap);
                        }
                        dEvaluateItemDetail.setAnswers(listMap);
                    }else{
                        dEvaluateItemDetail.setAnswers(listMap);
                    }
                    //dEvaluateItemDetail.setFilePath(Arrays.asList(answer.getAnswers()));
                    dItemDetails.add(dEvaluateItemDetail);
                }
            }
        }
        dEvaluateExamer.setdItemDetails(dItemDetails);

        return dEvaluateExamer;
    }

    /**
     *  按题
     * @param taskId
     * @param paperId
     * @param seqNo
     * @param previousOrNext
     * @param filter 是否全部
     * @return
     */
    private DEvaluateSubject getdEvaluateSubject( Integer taskId, Integer paperId,  int seqNo,  int previousOrNext, int filter) {
        //1. 找出主观题列表
        LinkedHashMap<String,Map> seqToItem = evaluateService.getSeqAndItemHashKeyMap(taskId,paperId);

        //2. 获取已答主观题列表. 需不需要过滤已做完
        if(filter==1){
            List<Integer> seqAnswered = evaluateService.getMarkedQz(taskId);
            for (int i=0;i<seqAnswered.size();i++){
                seqToItem.remove(String.valueOf(seqAnswered.get(i)));
                //显示未评题目时，如果当前题目已评完，则获取下一个
                if (seqNo == seqAnswered.get(i) && previousOrNext ==0){
                    previousOrNext = 1;//获取下一题
                }
            }
        }
        if(seqToItem.size()==0){
            throw new WrappedException(BizEnums.AllFinish);
        }
        //3. 过滤列表，计算出下一步或者上一步的序号
        Integer seq = getSeq(seqNo, previousOrNext, seqToItem);

        //4. 根据序号找出题目
        DEvaluateItemDetail dEvaluateItemDetail = getItemFromOriginalPaper(seqToItem, seq);
        if(dEvaluateItemDetail==null){
            throw new WrappedException(BizEnums.AllFinish);
        }
        //5. 组装出题目key
        String itemHashKey = String.format("Q-%d-%d",dEvaluateItemDetail.getType(),dEvaluateItemDetail.getId());

        //6. 获得交卷考生
        List<DNextQzResp> dNextQzResps = evaluateService.getNextQzForUsers(taskId,seq);

        if(dNextQzResps.size()==0){
            throw new WrappedException(BizEnums.NoOneFinish);
        }
        //7. 获得每个考生的答案
        List<DEvaluateSubject.ExamerAnswers> examerAnswers = evaluateService.getStemsBySeqNo(dEvaluateItemDetail,dNextQzResps,taskId,paperId,itemHashKey);

        //8. 组装数据返回
        DEvaluateSubject dEvaluateSubject = new DEvaluateSubject();
        dEvaluateSubject.setExamerAnswers(examerAnswers);
        dEvaluateSubject.setdItemDetail(dEvaluateItemDetail);
        return dEvaluateSubject;
    }

    /**
     * 获取源试卷的原题
     * @param seqToItem
     * @param seq
     * @return
     */
    private DEvaluateItemDetail getItemFromOriginalPaper(LinkedHashMap<String, Map> seqToItem, int seq) {
        DBlank dBlank = null;
        DAttachment dAttachment;
        String jsonStr = null;
        DExamAnswerItem answer;
        DEvaluateItemDetail dEvaluateItemDetail = null;
        Map mapTemp = null;
        Iterator it = seqToItem.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String,Map> key = (Map.Entry<String,Map>)it.next();
            String ky = key.getKey();
            if (Integer.valueOf(ky)==seq){
                mapTemp = key.getValue();
                break;
            }
        }

        if(mapTemp==null){
            return null;
        }
        if(((Integer)mapTemp.get("type")).intValue()==6){
            jsonStr = GsonUtil.toJson(mapTemp);
            dBlank = GsonUtil.fromJson(jsonStr,DBlank.class);
            if(dBlank.getdBlankStemSetting().getType()==0){
                dEvaluateItemDetail = new DEvaluateItemDetail();
                dEvaluateItemDetail.setSeqNo(seq);
                dEvaluateItemDetail.setType(6);
                dEvaluateItemDetail.setId(dBlank.getId());
                dEvaluateItemDetail.setStem(dBlank.getdBlankStemSetting().getQuestion());
                dEvaluateItemDetail.setReferenceAnswer(dBlank.getdBlankScoreSetting().getExplanation()==null?"":dBlank.getdBlankScoreSetting().getExplanation());
                dEvaluateItemDetail.setSpaceNum(dBlank.getdBlankStemSetting().getNumbers());
                List<DBlankScoreDetail> dBlankScoreDetails = dBlank.getdBlankScoreSetting().getdBlankScoreDetails();
                List<String> listScores = new ArrayList();
                DBlankScoreDetail dBlankScoreDetail;
                for (int k=0; k<dBlankScoreDetails.size();k++){
                    dBlankScoreDetail= dBlankScoreDetails.get(k);
                    listScores.add(dBlankScoreDetail.getScore().doubleValue()+"");
                }
                dEvaluateItemDetail.setScores(listScores);
                dEvaluateItemDetail.setScore(dBlank.getScore());
                dEvaluateItemDetail.setdBlankStyleSetting(dBlank.getdBlankStyleSetting());
            }
        }else if(((Integer)mapTemp.get("type")).intValue()==7){
            dEvaluateItemDetail = new DEvaluateItemDetail();
            jsonStr  = GsonUtil.toJson(mapTemp);
            dAttachment = GsonUtil.fromJson(jsonStr,DAttachment.class);
            dEvaluateItemDetail.setSeqNo(seq);
            dEvaluateItemDetail.setType(7);
            dEvaluateItemDetail.setId(dAttachment.getId());
            dEvaluateItemDetail.setStem(dAttachment.getdAttachmentStemSetting().getQuestion());
            dEvaluateItemDetail.setReferenceAnswer(dAttachment.getdAttachmentScoreSetting().getScoreRule()==null?"":dAttachment.getdAttachmentScoreSetting().getScoreRule());
            dEvaluateItemDetail.setScore(dAttachment.getdAttachmentScoreSetting().getScore());
            List<String> listScores = new ArrayList();
            String tenpScore = dAttachment.getdAttachmentScoreSetting().getScore().doubleValue()+"";
            listScores.add(tenpScore);
            dEvaluateItemDetail.setScores(listScores);
        }
        return dEvaluateItemDetail;
    }

    /**
     *获取主观题列表的上一步或者下一步的序号
     * @param seqNo            不知道当前题号时候，默认为0, 并且 previousOrNext==1. 表明拿第一道评卷题
     * @param previousOrNext  0, 当前题； 1： 下一题; -1 :上一题
     * @param seqToItem
     * @return
     */
    public int getSeq( int seqNo,  int previousOrNext, LinkedHashMap<String, Map> seqToItem) {
        if (previousOrNext == 0){
            return  seqNo;
        }

        Integer seqCircle =  getSeqOfCircle(seqNo,previousOrNext,seqToItem);
        if(seqCircle==null){
            Iterator it = seqToItem.keySet().iterator();
            int previousSeq=0;
            int seq = 0;
            while (it.hasNext()){
                //seq = (Integer)it.next();
                seq = Integer.valueOf(it.next().toString());
                if(previousOrNext==-1){//上一步
                    if(seq<seqNo) {
                        previousSeq =seq;
                    }else if(seq==seqNo){
                        break;
                    }
                }else if(previousOrNext==1){//下一步
                    if(seq>seqNo){
                        break;
                    }
                }
            }

            if(previousOrNext==-1){
                return previousSeq;
            }else{
                return seq;
            }
        }else{
            return seqCircle;
        }
    }
    public Integer getSeqOfCircle( int seqNo,  int previousOrNext, LinkedHashMap<String, Map > seqToItem) {
        Set<String> set= seqToItem.keySet();
        Object[] obj = set.toArray();
        int minSeq =  Integer.valueOf((String) obj[0]);
        int maxSeq =  Integer.valueOf((String) obj[set.size()-1]);
        if((seqNo==minSeq || seqNo < minSeq) && previousOrNext==-1 ){
            return maxSeq;
        }
        if((seqNo==maxSeq || seqNo > maxSeq) && previousOrNext==1 ){
            return minSeq;
        }
        return null;
    }
    /**
     * 按题目获取打分列表：
     * 1.获取试题对应的考试答题列表；
     * 2.匹配考生打分
     * @param dScoreReq
     * @return
     */
    public ServiceResponse<DScoreSubjectMarkListResp> getMarkListBySubject(DScoreReq dScoreReq, String operater, Integer companyId) {
        //获取统计数据
        DTaskMarked dTaskMarked = getTaskInMark(operater, companyId);
        //获取试卷信息
        TPaper tPaper =  getPaper(dScoreReq.getPaperId());
        int upOrNext = StringUtils.isEmpty(dScoreReq.getButtonType())?0:conf.Button_Up.equals(dScoreReq.getButtonType())?-1:1;
        if (StringUtils.isEmpty(dScoreReq.getNumber())){
            upOrNext = 1;//查找下一个
        }
        //是否过滤
        int  filter = (DScoreReq.SHOW_PART.equals(dScoreReq.getShowType()) || StringUtils.isEmpty(dScoreReq.getShowType()))?1:0;
        //获取考生考试情况
        DEvaluateSubject dEvaluateSubject = null;

        try{
            //获取试卷对应的考试打分情况
            dEvaluateSubject = getdEvaluateSubject(dScoreReq.getTaskId(),
                    dScoreReq.getPaperId(),
                    StringUtils.isEmpty(dScoreReq.getNumber()) ? 0 : dScoreReq.getNumber(),
                    upOrNext,
                    filter);
        }catch (WrappedException we){
            ServiceResponse<DScoreSubjectMarkListResp> scoreSubjectMarkListRespServiceResponse = new ServiceResponse<DScoreSubjectMarkListResp>();
            //设置题目列表
            DScoreSubjectMarkListResp dScoreSubjectMarkListResp = new DScoreSubjectMarkListResp();
            dScoreSubjectMarkListResp.setExamerAnswerses(new ArrayList<DExamerAnswers>());
            dScoreSubjectMarkListResp.setTaskMarked(dTaskMarked);
            dScoreSubjectMarkListResp.setPaperName(tPaper.getName());
            dScoreSubjectMarkListResp.setNumber(0);//题目序号
            dScoreSubjectMarkListResp.setType(0);//题目类型
            dScoreSubjectMarkListResp.setStem("");//题干
            dScoreSubjectMarkListResp.setReferenceAnswer("");//标准
            scoreSubjectMarkListRespServiceResponse.setCode(0);
            scoreSubjectMarkListRespServiceResponse.setMessage(we.getErrMsg());
            scoreSubjectMarkListRespServiceResponse.setResponse(dScoreSubjectMarkListResp);
            return scoreSubjectMarkListRespServiceResponse;
        }catch (Exception e){
            e.printStackTrace();
            ServiceResponse<DScoreSubjectMarkListResp> scoreSubjectMarkListRespServiceResponse = new ServiceResponse<DScoreSubjectMarkListResp>();
            DScoreSubjectMarkListResp dScoreSubjectMarkListResp = new DScoreSubjectMarkListResp();
            dScoreSubjectMarkListResp.setExamerAnswerses(new ArrayList<DExamerAnswers>());
            dScoreSubjectMarkListResp.setTaskMarked(dTaskMarked);
            dScoreSubjectMarkListResp.setPaperName(tPaper.getName());
            dScoreSubjectMarkListResp.setNumber(0);//题目序号
            dScoreSubjectMarkListResp.setType(0);//题目类型
            dScoreSubjectMarkListResp.setStem("");//题干
            scoreSubjectMarkListRespServiceResponse.setCode(0);
            scoreSubjectMarkListRespServiceResponse.setMessage(e.getMessage());
            scoreSubjectMarkListRespServiceResponse.setResponse(dScoreSubjectMarkListResp);
            return scoreSubjectMarkListRespServiceResponse;
        }

        //空列表,没打分时，每个得分空格都显示空的
        List<String> nullScoreList = new ArrayList<>();
        if (dEvaluateSubject.getdItemDetail().getScores() != null){
            for (int i = 0; i < dEvaluateSubject.getdItemDetail().getScores().size(); i++) {
                nullScoreList.add("");
            }
        }else {
            nullScoreList.add("");
        }

        //返回列表
        List<DExamerAnswers> returnList = new ArrayList<DExamerAnswers>();
        //获得所有考生
        List<DEvaluateSubject.ExamerAnswers> examerAnswers = dEvaluateSubject.getExamerAnswers();
        //匹配考生打分
        for (DEvaluateSubject.ExamerAnswers examerAnswers1 : examerAnswers) {
            //是否过滤已打分题目
            boolean filterType = true;
            DExamerAnswers resultObj = new DExamerAnswers();
            resultObj.setSeqNo(examerAnswers1.getSeq());
            resultObj.setAnswers(examerAnswers1.getAnswers());
            resultObj.setUserId(examerAnswers1.getMarkScore().getUserId());
            resultObj.setFullScore(dEvaluateSubject.getdItemDetail().getScores());
            DNextQzResp dNextQzResp = examerAnswers1.getMarkScore();

            if (StringUtils.isEmpty(dNextQzResp.getScore())){
                resultObj.setScore(nullScoreList);
                filterType = false;
            }else{
                //String[] str = dNextQzResp.getScore().split(",");
                String str[] = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(dNextQzResp.getScore(),",");
                List<String> scoreList = new ArrayList<>();
                for (String score : str){
                    scoreList.add(score);
                    if (StringUtils.isEmpty(score)){
                        filterType = false;
                    }
                }
                resultObj.setScore(scoreList);
            }
            //非当前考官打的分，不能编辑
            if (!StringUtils.isEmpty(dNextQzResp.getScoreCreater())
                    && !operater.equals(dNextQzResp.getScoreCreater())){
                resultObj.setCanScore(false);
            }else{
                resultObj.setCanScore(true);
            }
            //过滤已做题目
            if (filter ==1){
                if (!filterType){//加载未打完分的题目
                    returnList.add(resultObj);
                }
            }else{
                returnList.add(resultObj);
            }

        }

        //设置题目列表
        DScoreSubjectMarkListResp dScoreSubjectMarkListResp = new DScoreSubjectMarkListResp();
        dScoreSubjectMarkListResp.setNumber(dEvaluateSubject.getdItemDetail().getSeqNo());//题目序号
        dScoreSubjectMarkListResp.setType(dEvaluateSubject.getdItemDetail().getType());//题目类型
        dScoreSubjectMarkListResp.setStem(dEvaluateSubject.getdItemDetail().getStem());//题干
        dScoreSubjectMarkListResp.setReferenceAnswer(dEvaluateSubject.getdItemDetail().getReferenceAnswer());//标准
        dScoreSubjectMarkListResp.setExamerAnswerses(returnList);
        dScoreSubjectMarkListResp.setTaskMarked(dTaskMarked);
        dScoreSubjectMarkListResp.setPaperName(tPaper.getName());
        dScoreSubjectMarkListResp.setdBlankStyleSetting(dEvaluateSubject.getdItemDetail().getdBlankStyleSetting());
        return new ServiceResponse<DScoreSubjectMarkListResp>(dScoreSubjectMarkListResp);
    }

    /**
     * 按试题获取未评试题
     * (用递归获取，
     * 因为界面上点击下一题或上一题时要显示获取未评题目，
     * 递归获取直到获取到未做考生位置，如果全做则返回空列表)
     *
     * @param dScoreReq
     * @return
     */
    /*private DScoreSubjectMarkListResp getMarkListBySubjectOfPart(DScoreReq dScoreReq,
                                                                 DScoreSubjectMarkListResp resp,
                                                                 List<DExamerAnswers> answersesList,
                                                                 Integer unicode,
                                                                 String creater) {
        //存在考生未打分的则返回考生打分列表
//        if (answersesList != null && answersesList.size() > 0) {
//            return resp;
//        }
        int upOrNext = StringUtils.isEmpty(dScoreReq.getButtonType())?0:conf.Button_Up.equals(dScoreReq.getButtonType())?-1:1;
        if (StringUtils.isEmpty(dScoreReq.getNumber())){
            upOrNext = 1;//查找下一个
        }

        DEvaluateSubject dEvaluateSubject = getdEvaluateSubject(dScoreReq.getTaskId(),
                dScoreReq.getPaperId(),
                StringUtils.isEmpty(dScoreReq.getNumber())?0:dScoreReq.getNumber(),
                upOrNext,1);

        //返回列表
        List<DExamerAnswers> returnList = new ArrayList<>();
        //获得所有考生
        List<DEvaluateSubject.ExamerAnswers> examerAnswers = dEvaluateSubject.getExamerAnswers();
        //
//        Map<String, Map<Integer, List<String>>> mapMap = getExamListByPart(dScoreReq.getTaskId(),
//                dScoreReq.getPaperId(),
//                dEvaluateSubject.getdItemDetail().getSeqNo());
//
//        Map<Integer, List<String>> cMap = mapMap.get("complete");
//        Map<Integer, List<String>> dMap = mapMap.get("done");
//        Map<Integer, List<String>> createrMap = mapMap.get("creater");


        //空列表,没打分时，每个得分空格都显示空的
        List<String> nullScoreList = new ArrayList<>();
        if (dEvaluateSubject.getdItemDetail().getAnswers() != null){
            for (int i = 0; i < dEvaluateSubject.getdItemDetail().getScores().size(); i++) {
                nullScoreList.add("");
            }
        }else{
            nullScoreList.add("");
        }


        //存在考生未评分时组装列表
//        if (examerAnswers != null && examerAnswers.size() > cMap.size()) {
        for (DEvaluateSubject.ExamerAnswers examerAnswers1 : examerAnswers) {
            //过滤已完成的
//                if (cMap.get(examerAnswers1.getExamerId()) == null) {
            DExamerAnswers resultObj = new DExamerAnswers();
            resultObj.setSeqNo(examerAnswers1.getSeq());
            resultObj.setAnswers(examerAnswers1.getAnswers());
            resultObj.setUserId(examerAnswers1.getExamerId());
            resultObj.setFullScore(dEvaluateSubject.getdItemDetail().getScores());
            //本题得分
//                    if (dMap.get(examerAnswers1.getExamerId()) != null) {
//                        resultObj.setScore(dMap.get(examerAnswers1.getExamerId()));
//                    } else {//没打分则显示空空字符串
//                        resultObj.setScore(nullScoreList);
//                    }
            DNextQzResp dNextQzResp = examerAnswers1.getMarkScore();
            if (StringUtils.isEmpty(dNextQzResp.getScore())){
                resultObj.setScore(nullScoreList);
            }else{
                String[] str = dNextQzResp.getScore().split(",");
                List<String> scoreList = new ArrayList<>();
                for (String score : str){
                    scoreList.add(score);
                }
                resultObj.setScore(scoreList);
            }
//                    resultObj.setFilePath(examerAnswers1.getFilePath());
            //获取打分考官
//                    List<String> list = createrMap.get(examerAnswers1.getExamerId());
//                    if (list != null && list.size() > 0
//                            && !list.get(0).equals(creater)) {
//                        resultObj.setCanScore(false);
//                    } else {
//                        resultObj.setCanScore(true);
//                    }
            if (!StringUtils.isEmpty(dNextQzResp.getScoreCreater())
                    && !creater.equals(dNextQzResp.getScoreCreater())){
                resultObj.setCanScore(false);
            }else{
                resultObj.setCanScore(true);
            }
            returnList.add(resultObj);
        }
//            }
//        }

        //设置题目列表
        DScoreSubjectMarkListResp dScoreSubjectMarkListResp = new DScoreSubjectMarkListResp();
        dScoreSubjectMarkListResp.setNumber(dEvaluateSubject.getdItemDetail().getSeqNo());//题目序号
        dScoreSubjectMarkListResp.setType(dEvaluateSubject.getdItemDetail().getType());//题目类型
        dScoreSubjectMarkListResp.setStem(dEvaluateSubject.getdItemDetail().getStem());//题干
        dScoreSubjectMarkListResp.setReferenceAnswer(dEvaluateSubject.getdItemDetail().getReferenceAnswer());//标准
        dScoreSubjectMarkListResp.setExamerAnswerses(returnList);
        return dScoreSubjectMarkListResp;
        //递归
//        return getMarkListBySubjectOfPart(dScoreReq,
//                dScoreSubjectMarkListResp,
//                returnList,
//                dEvaluateSubject.getdItemDetail().getSeqNo(),
//                creater);
    }*/

    /**
     * 根据题目获取所有考生打分情况
     *
     * @param dScoreReq
     * @return
     */
    /*private DScoreSubjectMarkListResp getMarkListBySubjectOfAll(DScoreReq dScoreReq, String creater) {

        int upOrNext = StringUtils.isEmpty(dScoreReq.getButtonType())?0:conf.Button_Up.equals(dScoreReq.getButtonType())?-1:1;
        if (StringUtils.isEmpty(dScoreReq.getNumber())){
            upOrNext = 1;//查找下一个
        }

        DEvaluateSubject dEvaluateSubject = getdEvaluateSubject(dScoreReq.getTaskId(),
                dScoreReq.getPaperId(),
                StringUtils.isEmpty(dScoreReq.getNumber())?0:dScoreReq.getNumber(),
                upOrNext,0); ;

        //空列表,没打分时，每个得分空格都显示空的
        List<String> nullScoreList = new ArrayList<>();
        if (dEvaluateSubject.getdItemDetail().getScores() != null){
            for (int i = 0; i < dEvaluateSubject.getdItemDetail().getScores().size(); i++) {
                nullScoreList.add("");
            }
        }else {
            nullScoreList.add("");
        }

        //返回列表
        List<DExamerAnswers> returnList = new ArrayList<>();
        //获得所有考生
        List<DEvaluateSubject.ExamerAnswers> examerAnswers = dEvaluateSubject.getExamerAnswers();
//        Map<String, Map<Integer, List<String>>> allMap = getExamListByAll(dScoreReq.getTaskId(),
//                dScoreReq.getPaperId(),
//                dEvaluateSubject.getdItemDetail().getSeqNo());
//
//        Map<Integer, List<String>> scoreMap = allMap.get("score");
//        Map<Integer, List<String>> createrMap = allMap.get("creater");

        for (DEvaluateSubject.ExamerAnswers examerAnswers1 : examerAnswers) {
            DExamerAnswers resultObj = new DExamerAnswers();
            resultObj.setSeqNo(examerAnswers1.getSeq());
            resultObj.setAnswers(examerAnswers1.getAnswers());
            resultObj.setUserId(examerAnswers1.getMarkScore().getUserId());
            resultObj.setFullScore(dEvaluateSubject.getdItemDetail().getScores());
            DNextQzResp dNextQzResp = examerAnswers1.getMarkScore();
            //本题得分
//            if (allMap.get(examerAnswers1.getExamerId()) != null) {
//                resultObj.setScore(scoreMap.get(examerAnswers1.getExamerId()));
//            } else {//没打分则显示空空字符串
//                resultObj.setScore(nullScoreList);
//            }
            if (StringUtils.isEmpty(dNextQzResp.getScore())){
                resultObj.setScore(nullScoreList);
            }else{
                String[] str = dNextQzResp.getScore().split(",");
                List<String> scoreList = new ArrayList<>();
                for (String score : str){
                    scoreList.add(score);
                }
                resultObj.setScore(scoreList);
            }
//            resultObj.setFilePath(examerAnswers1.getFilePath());

//            List<String> createrL = createrMap.get(examerAnswers1.getExamerId());
//            if (createrL != null && createrL.size() > 0
//                    && !createrL.get(0).equals(creater)) {
//                resultObj.setCanScore(false);
//            } else {
//                resultObj.setCanScore(true);
//            }
            if (!StringUtils.isEmpty(dNextQzResp.getScoreCreater())
                    && !creater.equals(dNextQzResp.getScoreCreater())){
                resultObj.setCanScore(false);
            }else{
                resultObj.setCanScore(true);
            }

            returnList.add(resultObj);
        }

        //设置题目列表
        DScoreSubjectMarkListResp dScoreSubjectMarkListResp = new DScoreSubjectMarkListResp();
        dScoreSubjectMarkListResp.setNumber(dEvaluateSubject.getdItemDetail().getSeqNo());//题目序号
        dScoreSubjectMarkListResp.setType(dEvaluateSubject.getdItemDetail().getType());//题目类型
        dScoreSubjectMarkListResp.setStem(dEvaluateSubject.getdItemDetail().getStem());//题干
        dScoreSubjectMarkListResp.setReferenceAnswer(dEvaluateSubject.getdItemDetail().getReferenceAnswer());//标准
        dScoreSubjectMarkListResp.setExamerAnswerses(returnList);

        return dScoreSubjectMarkListResp;
    }*/

    /**
     * 根据题目获取全部打分情况
     *
     * @param taskId
     * @param paperId
     * @param serial_
     * @return
     */
    /*private Map<String, Map<Integer, List<String>>> getExamListByAll(Integer taskId, Integer paperId, Integer serial_) {

        Map<Integer, List<String>> integerListMap = new HashMap<>();
        Map<Integer, List<String>> createrMap = new HashMap<>();

        List<DUserExam> list = getExamListOfSubject(taskId, paperId, serial_);
        if (list != null && list.size() > 0) {
            for (DUserExam dUserExam : list) {
                String attr[] = dUserExam.getScore().split(",");
                List<String> score = new ArrayList<>();
                for (String s : attr) {
                    score.add(s);
                }
                integerListMap.put(dUserExam.getId(), score);


                if (dUserExam.getDone() > 0) {
                    List<String> createrL = new ArrayList<>();
                    createrL.add(dUserExam.getScoreCreater());
                    createrMap.put(dUserExam.getId(), createrL);
                }
            }
        }

        Map<String, Map<Integer, List<String>>> resultMap = new HashMap<>();
        resultMap.put("score", integerListMap);
        resultMap.put("creater", createrMap);

        return resultMap;
    }*/

/*
    private Map<String, Map<Integer, List<String>>> getExamListByPart(Integer taskId, Integer paperId, Integer serial_) {
        Map<String, Map<Integer, List<String>>> map = new HashMap<>();
        Map<Integer, List<String>> cMap = new HashMap<>();
        Map<Integer, List<String>> dMap = new HashMap<>();
        Map<Integer, List<String>> creater = new HashMap<>();

        List<DUserExam> list = getExamListOfSubject(taskId, paperId, serial_);
        if (list != null && list.size() > 0) {
            for (DUserExam dUserExam : list) {
                String attr[] = dUserExam.getScore().split(",");
                List<String> score = new ArrayList<>();

                for (String s : attr) {
                    score.add(s);
                }
                //完成评分的
                if (dUserExam.getTotal() == dUserExam.getDone()) {
                    cMap.put(dUserExam.getId(), score);
                } else if (dUserExam.getTotal() > dUserExam.getDone()) {
                    dMap.put(dUserExam.getId(), score);
                }
                //本题打分考官
                if (dUserExam.getDone() > 0) {
                    List<String> createrList = new ArrayList<>();
                    createrList.add(dUserExam.getScoreCreater());
                    creater.put(dUserExam.getId(), createrList);
                }
            }
        }

        map.put("complete", cMap);
        map.put("done", dMap);
        map.put("creater", creater);
        return map;
    }*/

    /**
     * 根据题号获取对应考生打分情况
     *
     * @param taskId
     * @param paperId
     * @param serial_
     * @return
     */
    /*private List<DUserExam> getExamListOfSubject(Integer taskId, Integer paperId, Integer serial_) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/score/getExamListOfSubject";

        Map<String, String> map = new HashMap<>();
        map.put("taskId", taskId.toString());
        map.put("serial", serial_.toString());
        map.put("paperId", paperId.toString());

        ServiceRequest<Map<String, String>> request = new ServiceRequest<Map<String, String>>();
        request.setRequest(map);
        ServiceResponse<List<DUserExam>> response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<List<DUserExam>>>() {
        });
        return response.getResponse();
    }*/

    /**
     * 根据考生获取评卷列表
     *
     * @param dScoreReq
     * @return
     */
    public ServiceResponse<DScoreExamtMarkListResp> getMarkListByExamer(DScoreReq dScoreReq, String operater, Integer companyId) {
        DTaskMarked dTaskMarked = getTaskInMark(operater, companyId);
        TPaper tPaper =  getPaper(dScoreReq.getPaperId());
        if (DScoreReq.SHOW_PART == dScoreReq.getShowType()) {//仅显示未完成
            try{
                DScoreExamtMarkListResp dScoreExamtMarkListResp = getMarkListByExamerOfPart(dScoreReq, operater);
                dScoreExamtMarkListResp.setTaskMarked(dTaskMarked);
                dScoreExamtMarkListResp.setPaperName(tPaper.getName());
                return new ServiceResponse<DScoreExamtMarkListResp>(dScoreExamtMarkListResp);
            }catch (WrappedException we){
                DScoreExamtMarkListResp dScoreExamtMarkListResp = new DScoreExamtMarkListResp();
                dScoreExamtMarkListResp.setTaskMarked(dTaskMarked);
                dScoreExamtMarkListResp.setPaperName(tPaper.getName());
                dScoreExamtMarkListResp.setSubjectResps(new ArrayList<DScoreSubjectResp>());
                ServiceResponse<DScoreExamtMarkListResp> scoreExamtMarkListRespServiceResponse = new ServiceResponse<DScoreExamtMarkListResp>();
                scoreExamtMarkListRespServiceResponse.setResponse(dScoreExamtMarkListResp);
                scoreExamtMarkListRespServiceResponse.setCode(0);
                scoreExamtMarkListRespServiceResponse.setMessage(we.getErrMsg());
                return scoreExamtMarkListRespServiceResponse;
            }catch (Exception e){
                e.printStackTrace();
                DScoreExamtMarkListResp dScoreExamtMarkListResp = new DScoreExamtMarkListResp();
                dScoreExamtMarkListResp.setTaskMarked(dTaskMarked);
                dScoreExamtMarkListResp.setPaperName(tPaper.getName());
                dScoreExamtMarkListResp.setSubjectResps(new ArrayList<DScoreSubjectResp>());
                ServiceResponse<DScoreExamtMarkListResp> scoreExamtMarkListRespServiceResponse = new ServiceResponse<DScoreExamtMarkListResp>();
                scoreExamtMarkListRespServiceResponse.setResponse(dScoreExamtMarkListResp);
                scoreExamtMarkListRespServiceResponse.setCode(0);
                scoreExamtMarkListRespServiceResponse.setMessage(e.getMessage());
                return scoreExamtMarkListRespServiceResponse;
            }
        } else {
            try{
                DScoreExamtMarkListResp dScoreExamtMarkListResp = getMarkListByExamerOfAll(dScoreReq, operater);
                dScoreExamtMarkListResp.setTaskMarked(dTaskMarked);
                dScoreExamtMarkListResp.setPaperName(tPaper.getName());
                return new ServiceResponse<DScoreExamtMarkListResp>(dScoreExamtMarkListResp);
            }catch (WrappedException we){
                DScoreExamtMarkListResp dScoreExamtMarkListResp = new DScoreExamtMarkListResp();
                dScoreExamtMarkListResp.setTaskMarked(dTaskMarked);
                dScoreExamtMarkListResp.setPaperName(tPaper.getName());
                dScoreExamtMarkListResp.setSubjectResps(new ArrayList<DScoreSubjectResp>());
                ServiceResponse<DScoreExamtMarkListResp> scoreExamtMarkListRespServiceResponse = new ServiceResponse<DScoreExamtMarkListResp>();
                scoreExamtMarkListRespServiceResponse.setResponse(dScoreExamtMarkListResp);
                scoreExamtMarkListRespServiceResponse.setCode(0);
                scoreExamtMarkListRespServiceResponse.setMessage(we.getErrMsg());
                return scoreExamtMarkListRespServiceResponse;
            }catch (Exception e){
                e.printStackTrace();
                DScoreExamtMarkListResp dScoreExamtMarkListResp = new DScoreExamtMarkListResp();
                dScoreExamtMarkListResp.setTaskMarked(dTaskMarked);
                dScoreExamtMarkListResp.setPaperName(tPaper.getName());
                dScoreExamtMarkListResp.setSubjectResps(new ArrayList<DScoreSubjectResp>());
                ServiceResponse<DScoreExamtMarkListResp> scoreExamtMarkListRespServiceResponse = new ServiceResponse<DScoreExamtMarkListResp>();
                scoreExamtMarkListRespServiceResponse.setResponse(dScoreExamtMarkListResp);
                scoreExamtMarkListRespServiceResponse.setCode(0);
                scoreExamtMarkListRespServiceResponse.setMessage(e.getMessage());
                return scoreExamtMarkListRespServiceResponse;
            }

        }

    }

    /**
     * 根据考生获取未完成打分题目:
     * 1.获取当前考生或下一个没有打完分的考生信息；
     * 2.获取考生对应答卷信息；
     * 3.过滤已打完分的试题；
     * 4.匹配考生打分以及能否评分
     * @param dScoreReq
     * @param creater
     * @return
     */
    private DScoreExamtMarkListResp getMarkListByExamerOfPart(DScoreReq dScoreReq,String creater) {
        String buttonType = dScoreReq.getButtonType();
        Integer userId = 0;
        if (!StringUtils.isEmpty(dScoreReq.getUserId()) ){
            userId = dScoreReq.getUserId();
        }
        //按试卷查询转换为按考生查询时默认从第一个考生获取
        if (userId ==0){
            buttonType = conf.Button_Next;
        }
        //获取下一个考生
        ServiceResponse<DNextExamUserResp> serviceResponse = nextExcecise(userId,
                dScoreReq.getTaskId(),
                buttonType ,
                DNextExamUserReq.INGORE_YES);
        //获取下一个或上一个考生时发生异常
        if (serviceResponse.getCode() >0){
            DScoreExamtMarkListResp dScoreExamtMarkListResp = new DScoreExamtMarkListResp();
            dScoreExamtMarkListResp.setSubjectResps(new ArrayList<DScoreSubjectResp>());
            return dScoreExamtMarkListResp;
        }

        DNextExamUserResp nextExcecise =serviceResponse.getResponse();
        //获取考生试卷
        DEvaluateExamer dEvaluateExamer = getStemsByExamer(dScoreReq.getTaskId(),dScoreReq.getPaperId(),nextExcecise.getUserExam().getUserId());
        //题目列表
        List<DScoreSubjectResp> dScoreSubjectResps = new ArrayList<DScoreSubjectResp>();
        List<DEvaluateItemDetail> dItemDetails = dEvaluateExamer.getdItemDetails();

        //空列表,没打分时，每个得分空格都显示空的
        Map<String, Map<String, List<String>>> resultMap = getPartScoreMap(nextExcecise.getScoreList());
        Map<String, List<String>> cMap = resultMap.get("complete");
        Map<String, List<String>> dMap = resultMap.get("done");
        Map<String, List<String>> createrMap = resultMap.get("creater");

        //匹配考生打分情况
        if (dItemDetails.size() > cMap.size()) {
            for (DEvaluateItemDetail dEvaluateItemDetail : dItemDetails) {
                if (cMap.get(String.valueOf(dEvaluateItemDetail.getSeqNo())) == null) {//有未完成打分的题目才遍历
                    DScoreSubjectResp dScoreSubjectResp = new DScoreSubjectResp();
                    dScoreSubjectResp.setFullScore(dEvaluateItemDetail.getScores());//题目总分
                    dScoreSubjectResp.setAnswers(dEvaluateItemDetail.getAnswers());
                    //dScoreSubjectResp.setFilePath(dEvaluateItemDetail.getFilePath());
                    dScoreSubjectResp.setNumber(dEvaluateItemDetail.getSeqNo());
                    dScoreSubjectResp.setType(dEvaluateItemDetail.getType());
                    dScoreSubjectResp.setReferenceAnswer(dEvaluateItemDetail.getReferenceAnswer());
                    dScoreSubjectResp.setStem(dEvaluateItemDetail.getStem());
                    dScoreSubjectResp.setdBlankStyleSetting(dEvaluateItemDetail.getdBlankStyleSetting());

                    if (dMap.get(String.valueOf(dEvaluateItemDetail.getSeqNo())) != null) {//已打过分
                        dScoreSubjectResp.setScore(dMap.get(String.valueOf(dEvaluateItemDetail.getSeqNo())));
                    } else {//未打过分
                        List<String> nullScroeList = new ArrayList<>();
                        if (dEvaluateItemDetail.getScores() != null){
                            for (int i = 0; i < dEvaluateItemDetail.getScores().size(); i++) {
                                nullScroeList.add("");
                            }
                        }else{
                            nullScroeList.add("");
                        }
                        dScoreSubjectResp.setScore(nullScroeList);
                    }
                    //非当前操作人评分的，不能评分
                    List<String> createrList = createrMap.get(String.valueOf(dEvaluateItemDetail.getSeqNo()));
                    if (createrList != null && createrList.size() > 0
                            && !createrList.get(0).equals(creater)) {
                        dScoreSubjectResp.setCanScore(false);
                    } else {
                        dScoreSubjectResp.setCanScore(true);
                    }
                    dScoreSubjectResps.add(dScoreSubjectResp);
                }
            }
        }

        DScoreExamtMarkListResp dScoreExamtMarkListResp = new DScoreExamtMarkListResp();
        dScoreExamtMarkListResp.setSeqNo(nextExcecise.getExercise().getSerialNo());
        dScoreExamtMarkListResp.setUserId(nextExcecise.getUserExam().getUserId());
        dScoreExamtMarkListResp.setSubjectResps(dScoreSubjectResps);

        return dScoreExamtMarkListResp;
    }

    /**
     * 根据考生获取全部题目:
     * 1.获取当前考生或下一个考生信息；
     * 2.获取考生对应答卷信息；
     * 3.匹配考生打分以及能否评分
     * @param dScoreReq
     * @return
     */
    private DScoreExamtMarkListResp getMarkListByExamerOfAll(DScoreReq dScoreReq, String creater) {
        String buttonType = dScoreReq.getButtonType();
        Integer userId = 0;
        if (!StringUtils.isEmpty(dScoreReq.getUserId()) ){
            userId = dScoreReq.getUserId();
        }
        //按试卷查询转换为按考生查询时默认从第一个考生获取
        if (userId ==0){
            buttonType = conf.Button_Next;
        }
        //获取下一个考生
        ServiceResponse<DNextExamUserResp> serviceResponse = nextExcecise(userId,
                dScoreReq.getTaskId(),
                buttonType ,
                DNextExamUserReq.INGORE_NO);

        //获取下一个或上一个考生时发生异常
        if (serviceResponse.getCode() >0){
            DScoreExamtMarkListResp dScoreExamtMarkListResp = new DScoreExamtMarkListResp();
            dScoreExamtMarkListResp.setSubjectResps(new ArrayList<DScoreSubjectResp>());
            return dScoreExamtMarkListResp;
        }

        DNextExamUserResp nextExcecise =   serviceResponse.getResponse();
        //获取试卷
        DEvaluateExamer dEvaluateExamer = getStemsByExamer(dScoreReq.getTaskId(),dScoreReq.getPaperId(),nextExcecise.getUserExam().getUserId());

        //题目列表
        List<DScoreSubjectResp> dScoreSubjectResps = new ArrayList<DScoreSubjectResp>();
        List<DEvaluateItemDetail> dItemDetails = dEvaluateExamer.getdItemDetails();

        //空列表,没打分时，每个得分空格都显示空的
        Map<String, Map<String, List<String>>> resultMap = getAllScoreMap(nextExcecise.getScoreList());
        Map<String, List<String>> scoreMap = resultMap.get("score");
        Map<String, List<String>> createrMap = resultMap.get("creater");
        //匹配考生答题打分
        for (DEvaluateItemDetail dEvaluateItemDetail : dItemDetails) {
            DScoreSubjectResp dScoreSubjectResp = new DScoreSubjectResp();
            dScoreSubjectResp.setFullScore(dEvaluateItemDetail.getScores());//题目总分
            dScoreSubjectResp.setAnswers(dEvaluateItemDetail.getAnswers());
            //dScoreSubjectResp.setFilePath(dEvaluateItemDetail.getFilePath());
            dScoreSubjectResp.setNumber(dEvaluateItemDetail.getSeqNo());
            dScoreSubjectResp.setType(dEvaluateItemDetail.getType());
            dScoreSubjectResp.setReferenceAnswer(dEvaluateItemDetail.getReferenceAnswer());
            dScoreSubjectResp.setStem(dEvaluateItemDetail.getStem());
            dScoreSubjectResp.setdBlankStyleSetting(dEvaluateItemDetail.getdBlankStyleSetting());

            if (scoreMap.get(String.valueOf(dEvaluateItemDetail.getSeqNo())) != null) {//已打过分
                dScoreSubjectResp.setScore(scoreMap.get(String.valueOf(dEvaluateItemDetail.getSeqNo())));
            } else {
                List<String> nullScroeList = new ArrayList<>();
                if (dEvaluateItemDetail.getAnswers() != null){
                    for (int i = 0; i < dEvaluateItemDetail.getScores().size(); i++) {
                        nullScroeList.add("");
                    }
                }else{
                    nullScroeList.add("");
                }
                dScoreSubjectResp.setScore(nullScroeList);
            }

            List<String> createrL = createrMap.get(String.valueOf(dEvaluateItemDetail.getSeqNo()));
            //非当前考官打分的题目，当前考官不能打分
            if (createrL != null && createrL.size() > 0
                    && !createrL.get(0).equals(creater)) {
                dScoreSubjectResp.setCanScore(false);
            } else {
                dScoreSubjectResp.setCanScore(true);
            }

            dScoreSubjectResps.add(dScoreSubjectResp);
        }

        DScoreExamtMarkListResp dScoreExamtMarkListResp = new DScoreExamtMarkListResp();
        dScoreExamtMarkListResp.setSeqNo(nextExcecise.getExercise().getSerialNo());
        dScoreExamtMarkListResp.setUserId(dEvaluateExamer.getExamerId());
        dScoreExamtMarkListResp.setSubjectResps(dScoreSubjectResps);

        return dScoreExamtMarkListResp;
    }

    /**
     * 获取所有打分题目
     * @param tScores
     * @return
     */
    private Map<String, Map<String, List<String>>> getAllScoreMap(List<TScore> tScores) {
        Map<String, List<String>> map = new HashMap<>();
        Map<String, List<String>> creater = new HashMap<>();

        //得分列表
        //List<TScore> tScores = getScoreListOfExam(examId);
        if (tScores != null && tScores.size() > 0) {
            for (TScore tScore : tScores) {
                String attr[] = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(tScore.getScore(),",");
                //org.apache.commons.lang.StringUtils.splitPreserveAllTokens()
                List<String> score = new ArrayList<>();
                for (String s : attr) {
                    score.add(s);
                }
                map.put(tScore.getSerialNo(), score);

                if (tScore.getDone() > 0) {
                    List<String> createrL = new ArrayList<>();
                    createrL.add(tScore.getCreater());
                    creater.put(tScore.getSerialNo(), createrL);
                }
            }
        }

        Map<String, Map<String, List<String>>> resultMap = new HashMap<>();
        resultMap.put("score", map);
        resultMap.put("creater", creater);

        return resultMap;
    }

    /**
     * 封装考生评完分、评分中、评分人列表
     * @param tScores
     * @return
     */
    public Map<String, Map<String, List<String>>> getPartScoreMap(List<TScore> tScores) {
        Map<String, Map<String, List<String>>> resultMap = new HashMap<>();
        Map<String, List<String>> cMap = new HashMap<>();
        Map<String, List<String>> dMap = new HashMap<>();
        Map<String, List<String>> creatMap = new HashMap<>();

        //得分列表
        //List<TScore> tScores = getScoreListOfExam(examId);
        if (tScores != null && tScores.size() > 0) {
            for (TScore tScore : tScores) {
                String attr[] = org.apache.commons.lang.StringUtils.splitPreserveAllTokens(tScore.getScore(),",");
                List<String> score = new ArrayList<>();
                for (String s : attr) {
                    score.add(s);
                }
                if (tScore.getTotal() == tScore.getDone()) {
                    cMap.put(tScore.getSerialNo(), score);
                } else if (tScore.getTotal() > tScore.getDone()) {
                    dMap.put(tScore.getSerialNo(), score);
                }

                if (tScore.getDone() > 0) {
                    List<String> creater = new ArrayList<>();
                    creater.add(tScore.getCreater());
                    creatMap.put(tScore.getSerialNo(), creater);
                }
            }
        }

        resultMap.put("complete", cMap);
        resultMap.put("done", dMap);
        resultMap.put("creater", creatMap);

        return resultMap;
    }

    /**
     * 获取考生分数列表
     *
     * @param examId
     * @return
     */
/*    private List<TScore> getScoreListOfExam(Integer examId) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/score/getScoreListOfExamer";
        Map<String, String> map = new HashMap<>();
        map.put("examId", examId.toString());
        ServiceRequest<Map<String, String>> request = new ServiceRequest<Map<String, String>>();
        request.setRequest(map);
        ServiceResponse<List<TScore>> response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<List<TScore>>>() {
        });
        return response.getResponse();
    }*/


    /**
     * 根据任务id查询考生评卷明细分页列表
     *
     * @param taskId taskId
     * @return
     */
    public PageResponse<DExamineeInTask> queryTaskDetail(Integer taskId, PageRequest pageRequest) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/task/{taskId}?pageIndex={pageIndex}&limit={limit}";

        ServiceResponse<PageResponse<DExamineeInTask>> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<PageResponse<DExamineeInTask>>>() {
        }, taskId, pageRequest.getPageIndex(), pageRequest.getLimit());
        return response.getResponse();
    }


    /**
     * 根据试卷ID获得题目数
     *
     * @param paperId
     * @return
     */
    public ServiceResponse<Integer> getSubjectNbByPaperId(Integer paperId,Integer taskId) {
        String hashKey = "T" + taskId + "-P" + paperId;
        DWPaperResponse dWPaperResponse = (DWPaperResponse)redisTemplate.opsForValue().get(hashKey);
        List<DWPage> pages = dWPaperResponse.getPages();
        DWPage dWPage;
        List items;
        Map mapTemp = null;
        int counter=0;
        String jsonStr;
        DBlank dBlank;
        boolean hasFound=false;
        for (int i = 0; i<pages.size()&&!hasFound;i++){
            dWPage =pages.get(i);
            items = dWPage.getItems();
            for(int j = 0;j<items.size()&&!hasFound; j++){
                mapTemp = (Map) items.get(j);
                if((Integer)mapTemp.get("type")==6){
                    jsonStr = GsonUtil.toJson(mapTemp);
                    dBlank = GsonUtil.fromJson(jsonStr,DBlank.class);
                    if(dBlank.getdBlankStemSetting().getType()==0){
                        counter++;
                    }
                }
                if((Integer)mapTemp.get("type")==7){
                    counter++;
                }
            }
        }
        return new ServiceResponse<>(counter);
    }


    /**
     * 获得题目序号与题目的hashKey的映射
     * 找出主观题列表
     * @param paperId
     * @return
     */
    public LinkedHashMap<String,Map> getSeqAndItemHashKeyMap(Integer taskId,Integer paperId) {
        String mappingKey = String.format("T%d-P%d-S",taskId,paperId);
        LinkedHashMap<String,Map>  seqToItem = new LinkedHashMap<>();
        if(!redisTemplate.hasKey(mappingKey)){
            String hashKey = "T" + taskId + "-P" + paperId;
            DWPaperResponse dWPaperResponse = (DWPaperResponse)redisTemplate.opsForValue().get(hashKey);
            List<DWPage> pages = dWPaperResponse.getPages();
            DWPage dWPage;
            List items;
            Map mapTemp = null;
            Integer counter=0;
            Integer type=null;
            String jsonStr = null;
            DBlank dBlank;
            for (int i = 0; i<pages.size();i++){
                dWPage =pages.get(i);
                items = dWPage.getItems();
                for(int j = 0;j<items.size(); j++){
                    mapTemp = (Map) items.get(j);
                    type = (Integer) mapTemp.get("type");
                    if((Integer)mapTemp.get("type")!=2&&(Integer)mapTemp.get("type")!=3){
                        counter++;
                    }
                    if(type.intValue()==6 || type.intValue()==7){
                        if(type.intValue()==6){
                            jsonStr = GsonUtil.toJson(mapTemp);
                            dBlank = GsonUtil.fromJson(jsonStr,DBlank.class);
                            if(dBlank.getdBlankStemSetting().getType().intValue()==1){
                                continue;
                            }
                        }
                        seqToItem.put(String.valueOf(counter),mapTemp);
                    }
                }
            }
            redisTemplate.opsForValue().set(mappingKey,seqToItem);
        }else{
            seqToItem = (LinkedHashMap<String,Map>)redisTemplate.opsForValue().get(mappingKey);
        }

        return seqToItem;
    }
    /**
     * 保存分数
     *
     * @param request
     * @return
     */
    public ServiceResponse saveScore(ServiceRequest<DScoreJson> request) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/score/save";
        ServiceResponse<Integer> subjectNum = getSubjectNbByPaperId(request.getRequest().getPaperId(),request.getRequest().getTaskId());
        request.getRequest().setSubjectNum(subjectNum.getResponse());
        ServiceResponse response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<DScoreJson>>() {
        });
        return response;
    }

    /**
     * 检查分数
     *
     * @param request
     * @return
     */
    public ServiceResponse checkScore(DScoreJson request) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/score/check";
        ServiceRequest<DScoreJson> request1 = new ServiceRequest<DScoreJson>();
        request1.setRequest(request);
        ServiceResponse response = msInvoker.post(servicePath, request1, new ParameterizedTypeReference<ServiceResponse<DScoreJson>>() {
        });
        return response;
    }

    /**
     * 获取任务分数
     *
     * @param companyId
     * @return
     */
    public DTaskMarked getTaskInMark(String operater, Integer companyId) {
        String servicePath = "http://" + PROJECT_SERVICE_NAME + "/tasks/mark/" + companyId + "/" + operater;
        ServiceResponse<DTaskMarked> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DTaskMarked>>() {
        });
        return response.getResponse();
    }

    /**
     * 根据考生id查询考试前端的任务列表
     *
     * @return
     */
    public ServiceResponse<Map<String, List<DExamTaskResponse>>> getProjectAndTasks(Integer userId) {
        String servicePath = "http://" + PROJECT_SERVICE_NAME + "/project/byUserId/{id}";
        ServiceResponse<List<DExamTaskResponse>> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<DExamTaskResponse>>>() {
        }, userId);
        Map<String, List<DExamTaskResponse>> map = new HashMap<>();
        map.put("dExamTaskResponses", response.getResponse());
        return new ServiceResponse<>(map);
    }

    /**
     * 获取下一个考生
     * @param userId
     * @param taskId
     * @param preOrNext
     * @param ingoreMarke
     * @return
     */
    private ServiceResponse<DNextExamUserResp> nextExcecise(Integer userId,Integer taskId,String preOrNext,String ingoreMarke){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/nextuser?userId="+userId+"&taskId="+taskId+"&preOrNext="+preOrNext+"&ingoreMarke="+ingoreMarke;
        ServiceResponse<DNextExamUserResp> response = msInvoker.getHandleCodeSelf(servicePath, new ParameterizedTypeReference<ServiceResponse<DNextExamUserResp>>() {});
        return response;
    }

    /**
     * 获取试卷
     * @param paperId
     * @return
     */
    private TPaper getPaper(Integer paperId){
        String servicePath = "http://" + PAPER_SERVICE_NAME + "/question/paper/simple/"+paperId;
        ServiceResponse<TPaper> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<TPaper>>() {});
        return response.getResponse();
    }
}
