package com.talebase.cloud.ms.examer.controller;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.domain.TScore;
import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.ms.examer.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 打分
 * Created by daorong.li on 2016-12-19.
 */
@RestController
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping(value = "/exam/score/save")
    public ServiceResponse saveScore(@RequestBody ServiceRequest<DScoreJson> request){
        DScoreJson json = request.getRequest();
        return scoreService.save(json);
    }

    @PostMapping(value = "/exam/score/check")
    public ServiceResponse checkScore(@RequestBody ServiceRequest<DScoreJson> request){
        DScoreJson json = request.getRequest();
        return scoreService.checkScore(json);
    }


  /*  @PostMapping(value = "/exam/score/getExamListOfSubject")
    public ServiceResponse<List<DUserExam>> getExamListOfSubject(@RequestBody ServiceRequest<Map<String,String>> request) {
        Map<String, String> map = request.getRequest();
        List<DUserExam> list = scoreService.getExamListOfSubject(map);
        return new ServiceResponse<List<DUserExam>>(list);
    }*/

/*    @PostMapping(value = "/exam/score/getScoreListOfExamer")
    public ServiceResponse getScoreListOfExamer(@RequestBody ServiceRequest<Map<String,String>> request){
        Map<String, String> map = request.getRequest();
        List<TScore> list = scoreService.getScoreListOfExamer(map);
        return new ServiceResponse<List<TScore>>(list);
    }*/

    @GetMapping(value = "/exam/task/{taskId}")
    public ServiceResponse<PageResponse<DExamineeInTask>> getTaskDetail(@PathVariable("taskId") Integer taskId, PageRequest pageRequest){
        if(pageRequest == null){
            pageRequest = new PageRequest();
            pageRequest.setLimit(50);
        }
        PageResponse pageResponse = scoreService.getTaskDetail(taskId, pageRequest);
        return new ServiceResponse(pageResponse);
    }

    @GetMapping(value = "/exam/nextuser")
    public ServiceResponse<DNextExamUserResp> nextExcecise(DNextExamUserReq req){
        return new ServiceResponse(scoreService.nextExcecise(req));
    }

    /**
     * 拿到已评完主观题列表
     * @param taskId
     * @return
     */
    @GetMapping(value = "/exam/markedQz/{taskId}")
    public ServiceResponse<List<Integer>> getMarkedQz(@PathVariable("taskId") Integer taskId){
        return new ServiceResponse(scoreService.getMarkedQz(taskId));
    }

    /**
     * 根据任务以及题序拿考生
     * @param taskId
     * @param serialNo
     * @return
     */
    @GetMapping(value = "/exam/users/{taskId}/{serialNo}")
    public ServiceResponse<List<DNextQzResp>> getNextQzForUsers(@PathVariable("taskId") Integer taskId, @PathVariable("serialNo") Integer serialNo){
        return new ServiceResponse(scoreService.getNextQzForUsers(taskId, serialNo));
    }

    /**
     * 通过任务id获取考生得分情况
     * @param taskId
     * @return
     */
    @GetMapping(value = "/exam/getScoreByTaskId/{taskId}")
    public ServiceResponse<String> getScoreByTaskId(@PathVariable("taskId") Integer taskId){
        return new ServiceResponse<>(GsonUtil.toJson(scoreService.getScoreByTaskId(taskId)));
    }
}

