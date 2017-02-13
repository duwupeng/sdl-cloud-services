package com.talebase.cloud.ms.exercise.controller;

import com.talebase.cloud.base.ms.examer.domain.TExercise;
import com.talebase.cloud.base.ms.examer.dto.DTimerExercise;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.exercise.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by erid.du on 2016-12-7.
 */
@RestController
public class PaperController {

    @Autowired
    private PaperService paperService;

    /**
     * 答题提交
     *
     * @param req
     * @return
     */
    @PostMapping(value = "/exam/exercise/submit")
    public ServiceResponse examSubmit(@RequestBody ServiceRequest<TExercise> req) throws Exception {
        TExercise tExercise = req.getRequest();
        return paperService.examSubmit(tExercise);
    }

    /**
     * 查询答题明细
     *
     * @param exerciseId
     * @return
     */
    @GetMapping(value = "/exam/exercise/{exerciseId}")
    public ServiceResponse getExamDetail(@PathVariable Integer exerciseId) throws Exception {
        return paperService.examDetail(exerciseId);
    }

    /**
     * @param exerciseId
     * @param req
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/exam/exercise/subScore/{exerciseId}")
    public ServiceResponse updateSubScore(@PathVariable Integer exerciseId, @RequestBody ServiceRequest<BigDecimal> req) throws Exception {
        return paperService.updateSubScore(exerciseId, req.getRequest());
    }

    /**
     * 通过项目id、任务id、考生id获取总分
     */
    @GetMapping(value = "/exam/exercise/{projectId}/{taskId}/{userId}")
    public ServiceResponse getTotalScore(@PathVariable("projectId") Integer projectId, @PathVariable("taskId") Integer taskId, @PathVariable("userId") Integer userId) throws Exception {
        return paperService.getTotalScore(projectId, taskId, userId);
    }

    /**
     * 开始考试
     * @param req
     * @return
     */
    @PostMapping(value = "/exam/exercise/start")
    public ServiceResponse<Integer> startExam(@RequestBody ServiceRequest<TExercise> req) throws Exception {
        TExercise exercise = req.getRequest();
        Integer exerciseId = paperService.examStart(exercise);
        return new ServiceResponse(exerciseId);
    }

    /**
     * 获取答题时间结束未交卷的记录
     * @param limit
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exam/exercise/timer/{limit}/{delaySecond}")
    public ServiceResponse<List<DTimerExercise>> getExerciseByTimer(@PathVariable("limit") Integer limit, @PathVariable("delaySecond") Integer delaySecond) throws Exception {
        return paperService.getExerciseByTimer(limit,delaySecond);
    }

}
