package com.talebase.cloud.os.project.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.examer.dto.DDataManagementRequest;
import com.talebase.cloud.base.ms.notify.dto.DTaskFinishType;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.paper.dto.DPaper;
import com.talebase.cloud.base.ms.paper.enums.TPaperStatus;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.os.project.service.AdminService;
import com.talebase.cloud.os.project.service.QuestionService;
import com.talebase.cloud.os.project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    private AdminService adminService;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/project/tasks/{projectId}")
    public ServiceResponse<DTasksInEdit> findTaskInEdit(@PathVariable("projectId") Integer projectId){
//        ServiceResponse<DTasksInEdit> response = taskService.findTaskInEdit(projectId);
//        List<DTaskInEdit> list = response.getResponse().getTasks();
//        if(list != null && list.size() > 0){
//            for(DTaskInEdit task : list){
//                if(task.getExamTime() == null){
//                    task.setLatestStartDateL(null);
//                }
//            }
//        }
//        for(response.getResponse().getTasks());
//        return response;
        return taskService.findTaskInEdit(projectId);
    }

    @PutMapping("/task/status/{taskId}")
    public ServiceResponse updateTaskStatus(@PathVariable("taskId") Integer taskId, Integer newStatus){
        return taskService.updateTaskStatus(taskId, newStatus);
    }

    @DeleteMapping("/task/{taskId}")
    public ServiceResponse deleteTask(@PathVariable("taskId") Integer taskId){
        TTask task = taskService.getTask(taskId);
        ServiceResponse serviceResponse = taskService.deleteTask(taskId);
        questionService.unUsePaper(Arrays.asList(task.getPaperId()));
        return serviceResponse;
    }

    @PutMapping("/project/tasks/{projectId}")
    public ServiceResponse updateTasks(@PathVariable("projectId") Integer projectId, String jsonStr){

        List<DProjectTasksUpdateReq> updateReqs = new ArrayList<>();

        try{
            updateReqs = GsonUtil.fromJson(jsonStr, new TypeToken<List<DProjectTasksUpdateReq>>(){}.getType());
        }catch (Exception e){
            throw new WrappedException(BizEnums.DataTypeErr);
        }

        //找到相关的账号并分配
        List<String> accounts = new ArrayList<>();
        List<Integer> paperIds = new ArrayList<>();

        for(DProjectTasksUpdateReq req : updateReqs){

            if(StringUtil.isEmpty(req.getName())){
                throw new WrappedException(BizEnums.TaskNameIsNull);
            }
            req.setName(req.getName().trim());

            if(req.getDelayLimitTime() != null && req.getDelayLimitTime() < 0)
                throw new WrappedException(BizEnums.TaskDelayLimitTimeSmallerThanZero);

            if(!Arrays.asList(DTaskFinishType.DeadLineType, DTaskFinishType.ExamTimeType).contains(req.getFinishType()))
                throw new WrappedException(BizEnums.TaskDelayLimitTimeSmallerThanZero);

            if(req.getExamTime() != null && req.getExamTime() < 0)
                throw new WrappedException(BizEnums.TaskExamTimeSmallerThanZero);

            if(req.getPageChangeLimit() != null && req.getPageChangeLimit() < 0)
                throw new WrappedException(BizEnums.TaskChangeLimitSmallerThanZero);

            if(req.getId() == null)//新建的才要试卷
                paperIds.add(req.getPaperId());

            if(req.getPaperId() == null)//没选择试卷
                throw new WrappedException(BizEnums.TaskPaperNotSelect);

            if(req.getExaminers() == null)
                continue;

            if(req.getExaminersArr() != null && !req.getExaminersArr().isEmpty()){
                for(String examiner : req.getExaminersArr()){
                    accounts.add(examiner);
                }
            }
        }
        List<TAdmin> admins = accounts.isEmpty() ? new ArrayList<>() : adminService.findAdmins(accounts);
        List<TPaper> papers = questionService.findPapers(paperIds);

        List<Integer> paperUsageIds = new ArrayList<>();

        for(DProjectTasksUpdateReq req : updateReqs){

            if(req.getExaminers() == null)
                req.setExaminers(new ArrayList<>());

            for(String account : req.getExaminersArr()){
                for(TAdmin admin : admins){
                    if(account.equals(admin.getAccount())){
                        TTaskExaminer examiner = new TTaskExaminer();
                        examiner.setName(admin.getName());
                        examiner.setExaminer(admin.getAccount());
                        req.getExaminers().add(examiner);
                        break;
                    }
                }
            }

            if(req.getId() == null){//新建的才放试卷冗余
                TPaper usePaper = null;
                for(TPaper paper : papers){
                    if(paper.getId().equals(req.getPaperId())){
                        usePaper = paper;
                        paperUsageIds.add(paper.getId());
                        break;
                    }
                }
                if(usePaper == null || usePaper.getStatus() != TPaperStatus.ENABLED.getValue())
                    throw new WrappedException(BizEnums.TaskPaperCannotUse);
                else{
                    req.setPaperUnicode(usePaper.getUnicode());
                    req.setPaperVersion(usePaper.getVersion());
                    req.setNeedMarkingNum(usePaper.getSubjectNum());
                    req.setPaperNum(usePaper.getTotalNum());
                }
            }

        }
        ServiceResponse serviceResponse = taskService.updateTasks(projectId, updateReqs);

        questionService.usePaper(paperUsageIds);//更新试卷次数

        return serviceResponse;
    }

    @GetMapping("/project/tasks/papers/{projectId}")
    public ServiceResponse<PageResponse<DPaper>> queryPapers(@PathVariable("projectId") Integer projectId, PageRequest pageRequest){
        //删之前准备好要修改试卷使用次数的试卷id
        ServiceResponse<DTasksInEdit> tasksInEdit = taskService.findTaskInEdit(projectId);
        List<Integer> paperIds = new ArrayList<>();
        for(DTaskInEdit task : tasksInEdit.getResponse().getTasks()){
            paperIds.add(task.getPaperId());
        }

        PageResponse<DPaper> pageResponse = questionService.queryPaperByPage(paperIds, pageRequest);
        return new ServiceResponse(pageResponse);
    }

    @PutMapping("/project/task/paper/{taskId}")
    public ServiceResponse updateTaskPaper(@PathVariable("taskId") Integer taskId){

        TTask task = taskService.getTask(taskId);
        Integer oldPaperId = task.getPaperId();

        List<TPaper> papers = questionService.checkPaperHasNewVersion(Arrays.asList(task.getPaperUnicode()));
        if(papers.isEmpty()){
            throw new WrappedException(BizEnums.TaskNewPaperNotFound);
        }
        TPaper newPaper = papers.get(0);
        if(newPaper.getId().equals(oldPaperId)){
            throw new WrappedException(BizEnums.TaskNewPaperNotFound);
        }

        TTask updateReq = task;
        updateReq.setId(task.getId());
        updateReq.setPaperUnicode(newPaper.getUnicode());
        updateReq.setPaperVersion(newPaper.getVersion().intValue());
        updateReq.setNeedMarkingNum(newPaper.getSubjectNum());
        updateReq.setPaperNum(newPaper.getTotalNum());
        updateReq.setPaperId(newPaper.getId());
        updateReq.setName(newPaper.getName());

        taskService.updatePaperVersion(updateReq);

        questionService.unUsePaper(Arrays.asList(oldPaperId));
        questionService.usePaper(Arrays.asList(newPaper.getId()));

        return new ServiceResponse();
    }

    @GetMapping("/project/task/findTaskExAndDataManagement")
    public ServiceResponse<DTTaskEx> findTaskExAndDataManagement(DDataManagementRequest dDataManagementRequest,PageRequest pageRequest){

        return taskService.findTaskExAndDataManagement(dDataManagementRequest,pageRequest);
    }

}
