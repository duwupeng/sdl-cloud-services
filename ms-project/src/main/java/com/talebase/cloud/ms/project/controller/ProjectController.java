package com.talebase.cloud.ms.project.controller;

import com.google.common.reflect.TypeToken;
import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.SqlUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.project.service.ProjectService;
import com.talebase.cloud.ms.project.service.TaskService;
import com.talebase.cloud.ms.project.util.DataPermissionVail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskService taskService;

    @PutMapping("/project/status/{projectId}")
    public ServiceResponse updateProjectStatus(@RequestBody ServiceRequest<Integer> serviceRequest, @PathVariable("projectId") Integer projectId) {
        DataPermissionVail.vailProject(projectService.getProject(projectId), serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());
        projectService.updateStatus(serviceRequest.getRequestHeader().getOperatorName(), projectId, serviceRequest.getRequest());
        return new ServiceResponse();
    }

    @DeleteMapping("/project/{projectId}")
    public ServiceResponse deleteProject(@RequestBody ServiceRequest serviceRequest, @PathVariable("projectId") Integer projectId) {
        DataPermissionVail.vailProject(projectService.getProject(projectId), serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());
        projectService.delete(serviceRequest.getRequestHeader().getOperatorName(), projectId);
        return new ServiceResponse();
    }

    @PostMapping("/projects/query")
    public ServiceResponse<PageResponse<DProject>> findProjects(@RequestBody ServiceRequest<DProjectQueryReq> serviceRequest) throws InvocationTargetException, IllegalAccessException {

        String projectNameLike = serviceRequest.getRequest().getProjectNameLike();//保留转义前的字符串留到后面进行比较
        String taskNameLike = serviceRequest.getRequest().getTaskNameLike();

        if (!StringUtil.isEmpty(serviceRequest.getRequest().getProjectNameLike())) {
            serviceRequest.getRequest().setProjectNameLike(SqlUtil.coverBaifenhao(serviceRequest.getRequest().getProjectNameLike().trim()));
        }
        if (!StringUtil.isEmpty(serviceRequest.getRequest().getTaskNameLike())) {
            serviceRequest.getRequest().setTaskNameLike(SqlUtil.coverBaifenhao(serviceRequest.getRequest().getTaskNameLike().trim()));
        }

        PageResponse<DProject> pageResponse = projectService.query(serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode(),
                serviceRequest.getRequest(), serviceRequest.getPageReq());

        //若是根据产品名称查询，则过滤掉非相关的产品
        if(!StringUtil.isEmpty(taskNameLike)){
            for(DProject project : pageResponse.getResults()){
                List<DTask> tasks = new ArrayList<>();
                for(DTask t : project.getTasks()){
                    if(t.getName().contains(taskNameLike))
                        tasks.add(t);
                }
                project.setTasks(tasks);
            }
        }


        return new ServiceResponse<>(pageResponse);
    }

    @PutMapping("/project/{projectId}")
    public ServiceResponse modifyProject(@RequestBody ServiceRequest<DProjectEditReq> serviceRequest, @PathVariable("projectId") Integer projectId) throws InvocationTargetException, IllegalAccessException {
        DataPermissionVail.vailProject(projectService.getProject(projectId), serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());

        TProject project = new TProject();
        BeanConverter.copyProperties(project, serviceRequest.getRequest());
//        project = GsonUtil.fromJson(GsonUtil.toJson(serviceRequest.getRequest()), new TypeToken<TProject>(){}.getType());
        project.setScanMax(serviceRequest.getRequest().getScanMax());

        project.setModifier(serviceRequest.getRequestHeader().getOperatorName());
        project.setId(projectId);
        project.setCompanyId(serviceRequest.getRequestHeader().getCompanyId());

        toSqlDate(project, serviceRequest.getRequest());

        List<DTaskInEdit> tasks = taskService.findTaskInEditByProjectId(projectId);

        //编辑项目时，项目时间必须包含任务时间
        for (DTaskInEdit task : tasks) {
            if (project.getStartDate() != null && project.getStartDate().getTime() > task.getStartDateL()) {
                throw new WrappedException(BizEnums.ProjectTaskTimeNotInclude);
            }

            if (project.getEndDate() != null && project.getEndDate().getTime() < task.getEndDateL()) {
                throw new WrappedException(BizEnums.ProjectTaskTimeNotInclude);
            }
        }

        projectService.updateProject(project, serviceRequest.getRequest().getProjectAdmins());
        return new ServiceResponse();
    }

    @PostMapping("/project")
    public ServiceResponse<Integer> createProject(@RequestBody ServiceRequest<DProjectEditReq> serviceRequest) throws Exception {
        TProject project = new TProject();
        BeanConverter.copyProperties(project, serviceRequest.getRequest());
        project.setId(null);
        project.setScanMax(serviceRequest.getRequest().getScanMax());
//        project = GsonUtil.fromJson(GsonUtil.toJson(serviceRequest.getRequest()), new TypeToken<TProject>(){}.getType());

        project.setCompanyId(serviceRequest.getRequestHeader().getCompanyId());
        project.setCreater(serviceRequest.getRequestHeader().getOperatorName());
        project.setModifier(serviceRequest.getRequestHeader().getOperatorName());

        toSqlDate(project, serviceRequest.getRequest());

        Integer projectId = projectService.createProject(project, serviceRequest.getRequest().getProjectAdmins());
        return new ServiceResponse(projectId);
    }

    @PostMapping("/project/edit/{projectId}")
    public ServiceResponse<DProjectInEdit> getProjectInEdit(@RequestBody ServiceRequest serviceRequest, @PathVariable("projectId") Integer projectId) throws InvocationTargetException, IllegalAccessException {
        DataPermissionVail.vailProject(projectService.getProject(projectId), serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());
        return new ServiceResponse(projectService.getProjectInEdit(projectId));
    }

    private void toSqlDate(TProject po, DProjectEditReq dto) {
        po.setStartDate(SqlUtil.tempDateSecond(dto.getStartDateStr()));
        po.setEndDate(SqlUtil.tempDateSecond(dto.getEndDateStr()));
        po.setScanStartDate(SqlUtil.tempDateSecond(dto.getScanStartDateStr()));
        po.setScanEndDate(SqlUtil.tempDateSecond(dto.getScanEndDateStr()));
    }

    //内部服务接口
    @PutMapping("/project/errMsg/{projectId}")
    public ServiceResponse addErrNotify(@PathVariable("projectId") Integer projectId, @RequestBody ServiceRequest<Integer> serviceRequest) {
        projectService.addErrMsgNum(projectId, serviceRequest.getRequest());
        return new ServiceResponse();
    }

    //内部服务接口
    @DeleteMapping("/project/errMsg/{projectId}")
    public ServiceResponse clearErrNotify(@PathVariable("projectId") Integer projectId) {
        projectService.clearErrMsgNum(projectId);
        return new ServiceResponse();
    }

    //内部服务接口
    //查询project,task的下拉列表
    @PostMapping("/project/select")
    public ServiceResponse<List<DProjectSelect>> findProjectSelectLists(@RequestBody ServiceRequest serviceRequest) {
        List<DProjectSelect> dProjectSelectList = projectService.findProjectSelectLists(serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());
        return new ServiceResponse(dProjectSelectList);
    }

    //拷贝项目,拷贝项目、任务基础；管理员，评卷人；
    @PostMapping("/project/copy")
    public ServiceResponse<Integer> copyProject(@RequestBody ServiceRequest<DProjectCopyReq> serviceRequest) throws InvocationTargetException, IllegalAccessException {
        DataPermissionVail.vailProject(projectService.getProject(serviceRequest.getRequest().getSourceProjectId()), serviceRequest.getRequestHeader().getOperatorName(), serviceRequest.getRequestHeader().getCompanyId(), serviceRequest.getRequestHeader().getOrgCode());

        Integer projectId = projectService.copyProject(serviceRequest.getRequest(), serviceRequest.getRequestHeader());
        return new ServiceResponse(projectId);
    }

    //前端任务列表
    @GetMapping("/project/byUserId/{id}")
    public ServiceResponse<List<DExamTaskResponse>> queryTasksByUserId(@PathVariable("id") Integer id) throws InvocationTargetException, IllegalAccessException {
        List<DExamTaskResponse> dExamTaskResponses = projectService.queryTasksByUserId(id);

        return new ServiceResponse(dExamTaskResponses);
    }

    @GetMapping("/project/admins/{projectId}")
    public ServiceResponse<List<TProjectAdmin>> findProjectAdmins(@PathVariable("projectId") Integer projectId) {
        return new ServiceResponse<>(projectService.getProjectAdmins(projectId));
    }

    @PostMapping("/project/group/admin")
    public ServiceResponse addGroupAdmin(@RequestBody ServiceRequest<TAdmin> serviceRequest) {
        projectService.createGroupAdmin(serviceRequest.getRequest());
        return new ServiceResponse();
    }
}
