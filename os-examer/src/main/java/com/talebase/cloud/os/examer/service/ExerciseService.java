package com.talebase.cloud.os.examer.service;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.dto.DTimerExercise;
import com.talebase.cloud.base.ms.paper.dto.DPageStyleSetting;
import com.talebase.cloud.base.ms.paper.dto.DWPage;
import com.talebase.cloud.base.ms.paper.dto.DWPaperResponse;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by eric.du on 2016-12-12.
 */
@Service
public class ExerciseService {
    @Autowired
    MsInvoker msInvoker;
    final static String EXAM_SERVICE_NAME = "ms-examer";
    final static String PAPER_SERVICE_NAME = "ms-paper";
    final static String EXERCISE_SERVICE_NAME = "ms-exercise";

    /**
     * 提交试卷
     * @param
     * @return
     */
    public ServiceResponse<String> submit(TExercise exercise,ServiceHeader header){
        String servicePath = "http://" + EXERCISE_SERVICE_NAME + "/exam/exercise/submit";
        ServiceResponse<String> response = msInvoker.post(servicePath, new ServiceRequest(header, exercise), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    /**
     * 获得源试卷
     * @param paperId
     * @return
     */
    public DWPaperResponse getPaper(Integer paperId){
        String servicePath = "http://" + PAPER_SERVICE_NAME + "/question/paper/"+paperId;
        ServiceResponse<DWPaperResponse> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<DWPaperResponse>>() {
        });
        if(response.getCode()==0){
            return response.getResponse();
        }else{
            return null;
        }
    }

    /**
     * 组装试卷,　打乱试卷顺序
     * @param dWPaperResponse
     * @return
     */
    public DWPaperResponse composePaper(DWPaperResponse dWPaperResponse){
        List<DWPage>  response = dWPaperResponse.getPages();
        List resp = new ArrayList<>();
        DWPage dWPageResponse = null;
        DPageStyleSetting dPageStyleSetting;
        for(int i=0;i<response.size();i++){
            dWPageResponse = response.get(i);
            dPageStyleSetting = dWPageResponse.getdPageStyleSetting();
            //如果是选择题，则需要打乱选项
            if(dPageStyleSetting.getOptionOrder()==1){
                List items =dWPageResponse.getItems();
                LinkedHashMap map = null;
                LinkedHashMap dOptionStemSetting=null;
                for(int j=0;j< items.size();j++){
                    map = (LinkedHashMap)items.get(j);
                    if((Integer)map.get("type") == 4||(Integer)map.get("type") == 5){
                        dOptionStemSetting = (LinkedHashMap)map.get("dOptionStemSetting");
                        List options =(List) dOptionStemSetting.get("options");
                        Collections.shuffle(options);
                    }
                }
            }
            //如果是选择题，则需要打乱选项，如果需要题目乱序
            if(dPageStyleSetting.getSubjectOrder()==1){
                List result = new ArrayList();
                List temp ;
                List items =dWPageResponse.getItems();
                LinkedHashMap map = null;
                int preInstructionIndex=0;
                for (int index=0; index< items.size();index++){
                    map = (LinkedHashMap)items.get(index);
                    if((Integer)map.get("type") == 3){
                        temp = items.subList(preInstructionIndex,index);
                        Collections.shuffle(temp);
                        result.addAll(temp);
                        result.add(map);
                        preInstructionIndex = index+1;
                    }
                }

                temp = items.subList(preInstructionIndex,items.size());
                Collections.shuffle(temp);
                result.addAll(temp);
                dWPageResponse.setItems(result);
            }
        }
        return  dWPaperResponse;
    }

    public Integer start(Integer taskId, Integer paperId){
        String servicePath = "http://" + EXERCISE_SERVICE_NAME + "/exam/exercise/start";
        TExercise exercise = new TExercise();
        exercise.setUserId(ServiceHeaderUtil.getRequestHeader().getCustomerId());
        exercise.setPaperId(paperId);
        exercise.setTaskId(taskId);
        return msInvoker.post(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), exercise), new ParameterizedTypeReference<ServiceResponse<Integer>>(){}).getResponse();
    }

    public List<DTimerExercise> getExerciseByTimer(Integer topicNum,Integer delaySecond){
        String servicePath = "http://" + EXERCISE_SERVICE_NAME + "/exam/exercise/timer/"+topicNum+"/"+delaySecond;
        ServiceResponse<List<DTimerExercise>> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<DTimerExercise>>>(){});
        return response.getResponse();
    }

}
