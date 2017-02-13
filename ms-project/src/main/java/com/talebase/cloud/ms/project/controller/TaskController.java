package com.talebase.cloud.ms.project.controller;

import com.talebase.cloud.base.ms.examer.dto.DTaskExamInfo;
import com.talebase.cloud.base.ms.notify.dto.DTaskFinishType;
import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.base.ms.project.enums.TProjectStatus;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.SqlUtil;
import com.talebase.cloud.common.util.TimeUtil;
import com.talebase.cloud.ms.project.service.ProjectService;
import com.talebase.cloud.ms.project.service.TaskService;
import com.talebase.cloud.ms.project.util.DataPermissionVail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectService projectService;

    private void vail(Integer taskId, ServiceRequest serviceRequest){
        DataPermissionVail.vailProject(projectService.getProjectByTask(taskId), serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());
    }

    @PostMapping("/project/tasks/{projectId}")
    public ServiceResponse<DTasksInEdit> findTasksInEdit(@RequestBody ServiceRequest serviceRequest, @PathVariable("projectId") Integer projectId) throws InvocationTargetException, IllegalAccessException {
        DTProjectEx project = projectService.getProject(projectId);
        DataPermissionVail.vailProject(project, serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());

        return new ServiceResponse(new DTasksInEdit(taskService.findTaskInEditByProjectId(projectId), new Date().getTime(),
                project.getStartDate() == null ? null :project.getStartDate().getTime(), project.getEndDate() == null ? null : project.getEndDate().getTime()));
    }

    @PostMapping("/project/tasksForNoVail/{projectId}")
    public ServiceResponse<DTasksInEdit> findTasksInEditForNoVail(@RequestBody ServiceRequest serviceRequest, @PathVariable("projectId") Integer projectId) throws InvocationTargetException, IllegalAccessException {
        DTProjectEx project = projectService.getProject(projectId);

        return new ServiceResponse(new DTasksInEdit(taskService.findTaskInEditByProjectId(projectId), new Date().getTime(),
                project.getStartDate() == null ? null :project.getStartDate().getTime(), project.getEndDate() == null ? null : project.getEndDate().getTime()));
    }

    @GetMapping("/task/findTaskExById/{id}")
    public ServiceResponse<DTTaskEx> findTaskExById(@PathVariable("id") Integer id) throws InvocationTargetException, IllegalAccessException {
        return new ServiceResponse<>(taskService.findTaskExById(id));
    }

    @PutMapping("/task/status/{taskId}")
    public ServiceResponse updateTaskStatus(@RequestBody ServiceRequest<Integer> serviceRequest, @PathVariable("taskId") Integer taskId){
        vail(taskId, serviceRequest);
        taskService.updateStatus(serviceRequest.getRequestHeader().getOperatorName(), taskId, serviceRequest.getRequest());
        return new ServiceResponse();
    }

    @DeleteMapping("/task/{taskId}")
    public ServiceResponse deleteTask(@RequestBody ServiceRequest serviceRequest, @PathVariable("taskId") Integer taskId){
        vail(taskId, serviceRequest);
        taskService.delete(serviceRequest.getRequestHeader().getOperatorName(), taskId);
        return new ServiceResponse();
    }

    @PutMapping("/project/tasks/{projectId}")
    public ServiceResponse updateTasks(@RequestBody ServiceRequest<List<DProjectTasksUpdateReq>> serviceRequest, @PathVariable("projectId") Integer projectId) throws InvocationTargetException, IllegalAccessException {

        DTProjectEx project = projectService.getProject(projectId);
        DataPermissionVail.vailProject(project, serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());

        List<TTask> tasks = new ArrayList<>();
        List<List<TTaskExaminer>> examiners = new ArrayList<>();

        for(DProjectTasksUpdateReq req : serviceRequest.getRequest()){
            TTask task = new TTask();
            BeanConverter.copyProperties(task, req);
            task.setId(req.getId());
            task.setPaperId(req.getPaperId());
            task.setExamTime(req.getExamTime());
            task.setPageChangeLimit(req.getPageChangeLimit());

            task.setProjectId(projectId);
            task.setCompanyId(serviceRequest.getRequestHeader().getCompanyId());
            task.setCreater(serviceRequest.getRequestHeader().getOperatorName());
            task.setModifier(serviceRequest.getRequestHeader().getOperatorName());

            task.setStartDate(SqlUtil.tempDateSecond(req.getStartDateStr()));

            if(req.getFinishType() == DTaskFinishType.DeadLineType){//统一交卷，最晚开考时间=开始时间+延迟时间

                task.setEndDate(SqlUtil.tempDateSecond(req.getEndDateStr()));

                if(task.getStartDate() == null || task.getEndDate() == null)//统一交卷类型的开始时间和结束时间不能为null
                    throw new WrappedException(BizEnums.TaskTimeErr);

                if(task.getEndDate().before(task.getStartDate()))//开始时间不能晚于结束时间
                    throw new WrappedException(BizEnums.TasktEndTimeStartTimeNotVail);

                if(req.getDelayLimitTime() == null || req.getDelayLimitTime() == 0){
                    task.setLatestStartDate(task.getEndDate());
                }else{
                    task.setLatestStartDate(SqlUtil.tempDateSecondAddMinute(req.getStartDateStr(), req.getDelayLimitTime()));
                }
                task.setExamTime(null);

                if(task.getEndDate().before(task.getLatestStartDate()))//最迟开始时间不能晚于结束时间
                    throw new WrappedException(BizEnums.TasktEndTimeStartTimeNotVail);
            }else{//试卷时长时间交卷，结束时间=最迟开考时间+试卷时长
                task.setLatestStartDate(SqlUtil.tempDateSecond(req.getLatestStartDateStr()));
                if(task.getStartDate() == null || task.getLatestStartDate() == null)//试卷时长交卷类型的开始时间和最迟开考时间不能为null
                    throw new WrappedException(BizEnums.TaskTimeErr);

                task.setEndDate(SqlUtil.tempDateSecondAddMinute(req.getLatestStartDateStr(), req.getExamTime()));
            }

            if(project.getStartDate() != null && project.getEndDate() != null)//若项目有开始/结束时间，则任务时间必须在项目时间内
                if(project.getStartDate().after(task.getStartDate()) || project.getEndDate().before(task.getEndDate()))
                    throw new WrappedException(BizEnums.TaskProjectTimeNotInclude);

            //新建的任务状态跟随项目状态
            if(task.getId() == null)
                task.setStatus(project.getStatus() == TProjectStatus.ENABLE.getValue() ? TTaskStatus.ENABLE.getValue() : TTaskStatus.DISABLE.getValue());

            tasks.add(task);
            examiners.add(req.getExaminers() == null ? new ArrayList<TTaskExaminer>() : req.getExaminers());
        }
        taskService.updateTasksByProject(serviceRequest.getRequestHeader().getOperatorName(), projectId, tasks, examiners);
        return new ServiceResponse();
    }

    @GetMapping("/task/examiners/{taskId}")
    public ServiceResponse<List<TTaskExaminer>> findTTaskExaminers(@PathVariable("taskId") Integer taskId) {
        return new ServiceResponse(taskService.findTaskExaminersByTask(taskId));
    }

    /**
     * 根据评卷人查询任务分页列表
     * @param operatorName
     * @param pageRequest
     * @return
     */
    @GetMapping("/tasks/examiner/{companyId}/{operatorName}")
    public ServiceResponse<PageResponse<DTaskInScore>> findTasksByExaminer(@PathVariable("operatorName") String operatorName, @PathVariable("companyId") Integer companyId, PageRequest pageRequest) {
        return new ServiceResponse(taskService.findTasksByExaminer(operatorName, companyId, pageRequest));
    }

    /**
     * 评卷情况统计
     * @param operatorName
     * @return
     */
    @GetMapping("/tasks/mark/{companyId}/{operatorName}")
    public ServiceResponse<DTaskMarked> getTaskInMark(@PathVariable("operatorName") String operatorName, @PathVariable("companyId") Integer companyId) {
        return new ServiceResponse(taskService.getTaskInMark(operatorName, companyId));
    }

    @PostMapping("/task/check/examiner")
    public ServiceResponse<TTask> checkTaskForExaminer(@RequestBody ServiceRequest<Integer> serviceRequest){
        Integer taskId = serviceRequest.getRequest();

        TTask task = taskService.get(taskId);
        if(task == null || task.getStatus() == TTaskStatus.DELETE.getValue() || task.getCompanyId() != serviceRequest.getRequestHeader().getCompanyId())
            throw new  WrappedException(BizEnums.TaskNotExists);

        List<TTaskExaminer> examiners = taskService.findTaskExaminersByTask(taskId);
        boolean include = false;
        for(TTaskExaminer examiner : examiners){
            if(examiner.getExaminer().equals(serviceRequest.getRequestHeader().getOperatorName())){
                include = true;
                break;
            }
        }
        if(!include)
            throw new  WrappedException(BizEnums.TaskNoMarkPermission);

        return new ServiceResponse<>(task);
    }

    @PostMapping("/task/use/query")
    public ServiceResponse<List<DUseStatics>> findUseStatics(@RequestBody ServiceRequest<List<DUseStaticsQueryReq>> serviceRequest){
        return new ServiceResponse<>(taskService.findUseStatics(serviceRequest.getRequest(), serviceRequest.getRequestHeader()));
    }

    @GetMapping("/task/{taskId}")
    public ServiceResponse<TTask> getTask(@PathVariable("taskId") Integer taskId){
        TTask task = taskService.get(taskId);
        if(task == null || task.getStatus().equals(TTaskStatus.DELETE.getValue()))
            throw new WrappedException(BizEnums.TaskNotExist);
        return new ServiceResponse<>(task);
    }

    @PutMapping("/task/paper")
    public ServiceResponse updateTaskPaperVersion(@RequestBody ServiceRequest<TTask> serviceRequest){

        TTask updateReq = serviceRequest.getRequest();
        vail(updateReq.getId(), serviceRequest);

        updateReq.setModifier(serviceRequest.getRequestHeader().getOperatorName());

        taskService.updateTaskPaperVersion(serviceRequest.getRequest());
        return new ServiceResponse();
    }

    @PostMapping("/taskExam")
    public ServiceResponse<DTaskExamInfo> getTaskExamInfo(@RequestBody ServiceRequest<Integer> serviceRequest){

        Integer taskId = serviceRequest.getRequest();
        DTProjectEx project = projectService.getProjectByTask(taskId);
        DataPermissionVail.vailProject(project, serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());

        TTask task = taskService.get(taskId);

        DTaskExamInfo taskExamInfo = new DTaskExamInfo(project.getId(), project.getName(), task.getId(), task.getName());
        return new ServiceResponse<>(taskExamInfo);
    }

}
