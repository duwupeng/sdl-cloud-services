package com.talebase.cloud.os.project.controller;
import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.dto.DSubordinate;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.paper.domain.TPaper;
import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.base.ms.project.enums.TProjectStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.*;
import com.talebase.cloud.os.project.service.AdminService;
import com.talebase.cloud.os.project.service.ProjectService;
import com.talebase.cloud.os.project.service.QuestionService;
import com.talebase.cloud.os.project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TaskService taskService;

    @Value("${codePath}")
    private String downloadPath;

    @PostMapping("/project")
    public ServiceResponse<Integer> createProject(HttpServletRequest request,DProjectEditReq editReq){
        if (!StringUtils.isEmpty(editReq.getDescription())){
            int len = editReq.getDescription().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.TEXT_BEYOUND_LENGTH);
            }
        }
        vailDProjectEditReq(editReq);
        fillAdmins(editReq);
        return projectService.createProject(request,editReq);
    }

    private void fillAdmins(DProjectEditReq editReq){
        editReq.setProjectAdmins(new ArrayList<>());
        List<String> accountStrs = StringUtil.toStrListByComma(editReq.getAccountsStr());
        if(accountStrs.isEmpty())
            return;

        List<TAdmin> admins = adminService.findAdmins(accountStrs);
        for(TAdmin admin : admins){
            TProjectAdmin projectAdmin = new TProjectAdmin();
            projectAdmin.setName(admin.getName());
            projectAdmin.setAccount(admin.getAccount());
            editReq.getProjectAdmins().add(projectAdmin);
        }
    }

    @PutMapping("/project/{projectId}")
    public ServiceResponse modifyProject(HttpServletRequest httpServletRequest,DProjectEditReq editReq, @PathVariable("projectId") Integer projectId){
        if (!StringUtils.isEmpty(editReq.getDescription())){
            int len = editReq.getDescription().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.TEXT_BEYOUND_LENGTH);
            }
        }
        vailDProjectEditReq(editReq);
        fillAdmins(editReq);
        return projectService.updateProject(httpServletRequest,projectId, editReq);
    }

    @PutMapping("/project/status/{projectId}")
    public ServiceResponse updateProjectStatus(@PathVariable("projectId") Integer projectId, Integer newStatus){
        if(newStatus == null || (TProjectStatus.ENABLE.getValue() != newStatus && TProjectStatus.DISABLE.getValue() != newStatus))
            throw new WrappedException(BizEnums.UnknownStatus);

        return projectService.updateProjectStatus(projectId, newStatus);
    }

    @DeleteMapping("/project/{projectId}")
    public ServiceResponse deleteProject(@PathVariable("projectId") Integer projectId){

        //删之前准备好要修改试卷使用次数的试卷id
        ServiceResponse<DTasksInEdit> tasksInEdit = taskService.findTaskInEdit(projectId);
        List<Integer> paperIds = new ArrayList<>();
        for(DTaskInEdit task : tasksInEdit.getResponse().getTasks()){
            paperIds.add(task.getPaperId());
        }

        ServiceResponse serviceResponse = projectService.deleteProject(projectId);
        //修改试卷使用统计
        questionService.unUsePaper(paperIds);

        return serviceResponse;
    }

    @GetMapping("/projects")
    public ServiceResponse<PageResponseWithParam> queryProjects(DProjectQueryReq queryReq, PageRequest pageRequest){
        PageResponse<DProject> pageResponse = projectService.query(queryReq, pageRequest);

        queryReq.setSysTimeL(System.currentTimeMillis());
        PageResponseWithParam<DProject, DProjectQueryReq> resp = new PageResponseWithParam(pageRequest, queryReq, pageResponse.getResults(), pageResponse.getTotal());

        ServiceResponse serviceResponse = new ServiceResponse(resp);
        serviceResponse.setPermission(new HashMap<>());

        //权限名称待置换
        //todo
        if(ServiceHeaderUtil.getRequestHeader().getPermissions().contains(PermissionEnum.c99_5.name())){
            serviceResponse.getPermission().put("hasUpgradeTaskPermission", true);

            //查找出任务所用的试卷，并找出是否存在版本更新
            List<String> unicodes = new ArrayList<>();
            for(DProject project : pageResponse.getResults()){
                for(DTask task : project.getTasks()){
                    unicodes.add(task.getPaperUnicode());
                }
            }

            List<TPaper> papers = questionService.checkPaperHasNewVersion(unicodes);
            for(DProject project : pageResponse.getResults()){
                for(DTask task : project.getTasks()){
                    for(TPaper paper : papers) {
//                        if(task.getPaperUnicode().equals(paper.getUnicode()) && (task.getPaperVersion().compareTo(paper.getVersion()) == 1)){
                        if(task.getPaperUnicode().equals(paper.getUnicode()) && task.getPaperId() != paper.getId()) {
                            task.setHasUpgradeVersion(true);
                            break;
                        }
                    }
                }

            }
        }else{
            serviceResponse.getPermission().put("hasUpgradeVersionPermission", false);
        }

        return serviceResponse;
    }

    @GetMapping("/project/edit/{projectId}")
    public ServiceResponse<DProjectInEdit> getProjectInEdit(@PathVariable("projectId") Integer projectId){
        ServiceResponse<DProjectInEdit> serviceResponse = projectService.getProjectInEdit(projectId);

        TAdmin admin = adminService.getAdminByAccount(serviceResponse.getResponse().getAdminAccount());
        if(admin != null)
            serviceResponse.getResponse().setAdminName(admin.getName());

        return serviceResponse;
    }

    private void vailDProjectEditReq(DProjectEditReq req){

        if(StringUtil.isEmpty(req.getName())){
            throw new WrappedException(BizEnums.ProjectNameIsNull);
        }
        req.setName(req.getName().trim());//去首尾空格

        String now = TimeUtil.tempDateSecond(new Date());

        Date startDate = StringUtil.isEmpty(req.getStartDateStr()) ? null : TimeUtil.tempDateSecond(req.getStartDateStr());
        Date endDate = StringUtil.isEmpty(req.getEndDateStr()) ? null : TimeUtil.tempDateSecond(req.getEndDateStr());

        if(endDate != null){
            if(startDate == null){
                if(endDate.after(new Date()))
                    req.setStartDateStr(now);//有结束时间没开始时间，则补充开始时间为当前时间
            }else{//有开始时间，有结束时间，需要比较两个时间
                if(endDate.before(startDate)){//若结束时间比开始时间早报异常
                    throw new WrappedException(BizEnums.ProjectEndTimeStartTimeNotVail);
                }
            }
        }
        //可以开始/结束时间都不设置

        //若开通扫码，则需要检查扫码时间必须在项目时间之内
        if(req.getScanEnable()){
            Date scanStartDate = StringUtil.isEmpty(req.getScanStartDateStr())? null : TimeUtil.tempDateSecond(req.getScanStartDateStr());
            Date scanEndDate = StringUtil.isEmpty(req.getScanEndDateStr()) ? null : TimeUtil.tempDateSecond(req.getScanEndDateStr());

            if(scanEndDate != null){
                if(scanStartDate == null){
                    if(scanEndDate.after(new Date()))
                        req.setScanStartDateStr(now);//有结束时间没开始时间，则补充开始时间为当前时间
                }else{//有开始时间，有结束时间，需要比较两个时间
                    if(scanEndDate.before(scanStartDate)){//若结束时间比开始时间早报异常
                        throw new WrappedException(BizEnums.ProjectEndTimeStartTimeNotVail);
                    }
                }
            }

            //扫码时间需要包含在项目时间内
            if(endDate != null && scanEndDate != null && endDate.before(scanEndDate))
                throw new WrappedException(BizEnums.ProjectScanTimeNotInclude);
            if(startDate != null && scanStartDate != null && startDate.after(scanStartDate))
                throw new WrappedException(BizEnums.ProjectScanTimeNotInclude);
        }

        if(req.getScanMax() != null && req.getScanMax() < 0){
            throw new WrappedException(BizEnums.ScanMaxErr);
        }
    }

    @PostMapping("/project/copy")
    public ServiceResponse copyProject(DProjectCopyReq copyReq){

        if(StringUtil.isEmpty(copyReq.getName())){
            throw new WrappedException(BizEnums.ProjectNameIsNull);
        }
        copyReq.setName(copyReq.getName().trim());//去首尾空格

        ServiceResponse<Integer> serviceResponse = projectService.copyProject(copyReq, ServiceHeaderUtil.getRequestHeader());
        ServiceResponse<DTasksInEdit> tasksResponse = taskService.findTaskInEdit(copyReq.getSourceProjectId());
        List<Integer> paperUseIds = new ArrayList<>();
        for(DTaskInEdit task : tasksResponse.getResponse().getTasks()){
            paperUseIds.add(task.getPaperId());
        }
        questionService.usePaper(paperUseIds);
        return serviceResponse;
    }

    @GetMapping("/project/admins/rest")
    public ServiceResponse<List<DExamProjectTasks>> findProjectUnSelectAdmins(Integer projectId){
        Integer companyId = ServiceHeaderUtil.getRequestHeader().getCompanyId();
        String orgCode = ServiceHeaderUtil.getRequestHeader().getOrgCode();
        List<DSubordinate> list = adminService.getSubordinate(companyId,orgCode);
        if(projectId == null || projectId == 0)
            return new ServiceResponse(list);

        List<TProjectAdmin> projectAdmins = projectId == null ? new ArrayList<>() : projectService.findProjectAdmin(projectId);
        List<DSubordinate> restList = new ArrayList<>();
        List<String> accounts = new ArrayList<>();
        for(TProjectAdmin admin : projectAdmins){
            accounts.add(admin.getAccount());
        }

        for(DSubordinate sub : list){
            if(!accounts.contains(sub.getAccount()))
                restList.add(sub);
        }

        return new ServiceResponse(restList);
    }
    @GetMapping("/project/exportImage/{projectId}")
    public ServiceResponse exportImage(HttpServletResponse response,@PathVariable("projectId") Integer projectId){
       // ServiceResponse response = new ServiceResponse();
        String filename = "code"+projectId+".jpg";
        String filePath =downloadPath+filename;
        InputStream reader = null;
        OutputStream out = null;
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            //resp.setHeader("content-disposition", "attachment;fileName="+fileName);
            //如果图片名称是中文需要设置转码
            response.setHeader("content-disposition", "attachment;fileName="+ URLEncoder.encode(filename, "UTF-8"));
            // 读取文件
            reader = new FileInputStream(filePath);
            // 写入浏览器的输出流
            out = response.getOutputStream();

            while ((len = reader.read(bytes)) > 0) {
                out.write(bytes, 0, len);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
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
}
