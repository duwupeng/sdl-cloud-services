package com.talebase.cloud.os.examer.controller;

import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.paper.dto.*;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.dto.DExamTaskResponse;
import com.talebase.cloud.base.ms.project.dto.DTaskInScore;
import com.talebase.cloud.base.ms.project.dto.DTaskMarked;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.examer.service.EvaluateService;
import com.talebase.cloud.os.examer.service.ExamProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by eric.du on 2016-12-19.
 */
@RestController
public class EvaluateController {

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private ExamProjectService projectService;

    /**
     * 根据考生获取列表
     *
     * @param dScoreReq
     * @return
     */
    @GetMapping(value = "/evaluate/examer/markList")
    public ServiceResponse<DScoreExamtMarkListResp> getMarkListByExamer(DScoreReq dScoreReq) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        return evaluateService.getMarkListByExamer(dScoreReq, serviceHeader.getOperatorName(), serviceHeader.getCompanyId());
    }

    /**
     * 获取按题打分列表
     *
     * @param dScoreReq
     * @return
     */
    @GetMapping(value = "/evaluate/stem/markList")
    public ServiceResponse<DScoreSubjectMarkListResp> getMarkListBySubject(DScoreReq dScoreReq) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        return evaluateService.getMarkListBySubject(dScoreReq, serviceHeader.getOperatorName(), serviceHeader.getCompanyId());
    }

    /**
     * 评卷人任务列表
     *
     * @return
     */
    @GetMapping(value = "/evaluate/taskList")
    public ServiceResponse<DExaminerTaskList> evaluateByExamer(PageRequest pageRequest) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
//        return evaluateService.getByExamer(taskId,paperId);

        if (pageRequest == null) {
            pageRequest = new PageRequest();
            pageRequest.setLimit(20);
        }

        PageResponse<DTaskInScore> pageResponse = projectService.queryTasksInScore(serviceHeader.getOperatorName(), serviceHeader.getCompanyId(), pageRequest);
        DTaskMarked taskMarked = projectService.getTaskMarked(serviceHeader.getOperatorName(), serviceHeader.getCompanyId());

        DExaminerTaskList resp = new DExaminerTaskList();
        resp.setPageResponse(pageResponse);
        resp.setTaskMarked(taskMarked);

        return new ServiceResponse(resp);
    }

    /**
     * 评卷任务详情
     *
     * @return
     */
    @GetMapping(value = "/evaluate/taskDetail/{taskId}")
    public ServiceResponse<DExamineeTaskDetail> taskDetail(@PathVariable("taskId") Integer taskId, PageRequest pageRequest) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
//        return evaluateService.getByExamer(taskId,paperId);

        if (pageRequest == null) {
            pageRequest = new PageRequest();
            pageRequest.setLimit(50);
        }

        TTask task = projectService.checkTask(taskId, serviceHeader);
        DTaskMarked taskMarked = projectService.getTaskMarked(serviceHeader.getOperatorName(), serviceHeader.getCompanyId());
        PageResponse<DExamineeInTask> pageResponse = evaluateService.queryTaskDetail(taskId, pageRequest);

        DExamineeTaskDetail detail = new DExamineeTaskDetail();
        detail.setTaskMarked(taskMarked);
        detail.setTaskName(task.getName());
        detail.setPaperId(task.getPaperId());
        detail.setExaminees(pageResponse);

        return new ServiceResponse(detail);
    }

    /**
     * 保存分数
     *
     * @param obj
     * @return
     */
    @PostMapping(value = "/exam/score/save")
    public ServiceResponse saveScore(DScoreJson obj) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        obj.setCreater(serviceHeader.getOperatorName());
        ServiceRequest<DScoreJson> request = new ServiceRequest<DScoreJson>();
        request.setRequest(obj);
        request.setRequestHeader(serviceHeader);
        return evaluateService.saveScore(request);
    }

    /**
     * 检查分数是否能保存
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/exam/score/check")
    public ServiceResponse checkScore(DScoreJson request) {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        request.setCreater(serviceHeader.getOperatorName());
        return evaluateService.checkScore(request);
    }

    /**
     * 根据考生id查询考试前端的任务列表
     *
     * @return
     */
    @GetMapping(value = "/exam/examer/{id}")
    public ServiceResponse<Map<String, List<DExamTaskResponse>>> getProjectAndTasks(@PathVariable("id") Integer id) {
        return evaluateService.getProjectAndTasks(id);
    }

}
