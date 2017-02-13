package com.talebase.cloud.ms.project.service;

import com.google.common.reflect.TypeToken;
import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.project.domain.*;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.base.ms.project.enums.TProjectScanEnable;
import com.talebase.cloud.base.ms.project.enums.TProjectStatus;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.ms.project.dao.ProjectMapper;
import com.talebase.cloud.ms.project.dao.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskService taskService;

    /**
     * 更新状态
     *
     * @param operatorName
     * @param projectId
     * @param newStatus
     */
    public void updateStatus(String operatorName, Integer projectId, Integer newStatus) {

        if (newStatus.equals(TProjectStatus.ENABLE.getValue()) || newStatus.equals(TProjectStatus.DISABLE.getValue())) {
            projectMapper.updateStatus(operatorName, projectId, newStatus);
        } else {
            throw new WrappedException(BizEnums.UnknownStatus);
        }

        if (newStatus.equals(TProjectStatus.DISABLE.getValue())) {
            taskMapper.updateStatusByProject(operatorName, projectId, TTaskStatus.DISABLE.getValue());
        }

    }

    /**
     * 删除
     *
     * @param operatorName
     * @param projectId
     */
    public void delete(String operatorName, Integer projectId) {
        if (hasExaminee(projectId)) {
            throw new WrappedException(BizEnums.HasExaminee);
        }

        projectMapper.updateStatus(operatorName, projectId, TProjectStatus.DELETE.getValue());
        taskMapper.updateStatusByProject(operatorName, projectId, TTaskStatus.DELETE.getValue());
    }

    /**
     * 已经有人参与考试
     *
     * @param projectId
     * @return
     */
    private boolean hasExaminee(Integer projectId) {
        List<TTaskProgress> progresses = taskMapper.getProgressByProject(projectId);
        for (TTaskProgress progress : progresses) {
            if (progress.getInNum() > 0) {
                return true;
            }
        }

        return false;
    }

    @Transactional(readOnly = true)
    public DTProjectEx getProject(Integer projectId) {

        DTProjectEx ex = projectMapper.get(projectId);
        if (ex != null) {
            List<TProjectAdmin> projectAdmins = projectMapper.getAdmins(projectId);
            ex.setProjectAdmins(projectAdmins);
        } else {
            throw new WrappedException(BizEnums.ProjectNotExist);
        }

        return ex;
    }

    @Transactional(readOnly = true)
    public DTProjectEx getProjectByTask(Integer taskId) {

        DTProjectEx ex = projectMapper.getByTask(taskId);
        if (ex != null) {
            List<TProjectAdmin> projectAdmins = projectMapper.getAdmins(ex.getId());
            ex.setProjectAdmins(projectAdmins);
        } else {
            throw new WrappedException(BizEnums.ProjectNotExist);
        }

        return ex;
    }

    @Transactional(readOnly = true)
    public List<TProjectAdmin> getProjectAdmins(Integer projectId) {
        List<TProjectAdmin> projectAdmins = projectMapper.getAdmins(projectId);
        return projectAdmins;
    }

    @Transactional(readOnly = true)
    public DProjectInEdit getProjectInEdit(Integer projectId) throws InvocationTargetException, IllegalAccessException {
        DProjectInEdit dto = new DProjectInEdit();
        TProject po = getProject(projectId);
        BeanConverter.copyProperties(dto, po);
        dto.setScanMax(po.getScanMax());
        dto.setScanNow(po.getScanNow());
//        dto = GsonUtil.fromJson(GsonUtil.toJson(po), new TypeToken<DProjectInEdit>(){}.getType());

        dto.setStartDateL(po.getStartDate() != null ? po.getStartDate().getTime() : null);
        dto.setEndDateL(po.getEndDate() != null ? po.getEndDate().getTime() : null);
        dto.setScanStartDateL(po.getScanStartDate() == null ? null : po.getScanStartDate().getTime());
        dto.setScanEndDateL(po.getScanEndDate() == null ? null : po.getScanEndDate().getTime());
        dto.setAdminAccount(po.getCreater());

//        dto.setStartDateStr(TimeUtil.tempDateSecond(dto.getStartDate()));
//        dto.setEndDateStr(TimeUtil.tempDateSecond(dto.getEndDate()));
//        dto.setScanStartDateStr(TimeUtil.tempDateSecond(dto.getScanStartDate()));
//        dto.setScanEndDateStr(TimeUtil.tempDateSecond(dto.getScanEndDate()));

        List<TProjectAdmin> projectAdmins = projectMapper.getAdmins(projectId);
        dto.setAdmins(projectAdmins);

        return dto;
    }

//    public List<DProject> findDProjects(Integer companyId, String orgCode){
//        List<TProject> pos = projectMapper.find(companyId, orgCode);
//        List<DProject> dtos = new ArrayList<>();
//
//        BeanUtil.convert(pos, dtos);
//
//        List<Integer> projectIds = new ArrayList<>();
//        for(TProject project : pos){
//            projectIds.add(project.getId());
//        }
//
//        List<DTask> dtoTasks = taskMapper.findDTaskByProjectIds(projectIds);
//
//        for(DProject dto : dtos){
//            for(DTask dtoTask : dtoTasks){
//                if(dto.getId().equals(dtoTask.getProjectId())){
//                    dto.getTasks().add(dtoTask);
//                }
//            }
//        }
//
//        return dtos;
//    }

    public Integer createProject(TProject project, List<TProjectAdmin> projectAdmins) {

        if (projectNameExists(project.getCompanyId(), project.getName(), null)) {
            throw new WrappedException(BizEnums.ProjectNameRepeat);
        }

        if (project.getStatus() == null)
            project.setStatus(TProjectStatus.ENABLE.getValue());

        projectMapper.insert(project);

        for (TProjectAdmin projectAdmin : projectAdmins) {
            projectAdmin.setProjectId(project.getId());
            projectMapper.insertProjectAdmin(projectAdmin);
        }

        projectMapper.insertProjectErrNotify(project.getId());

        return project.getId();
    }

    public void updateProject(TProject project, List<TProjectAdmin> projectAdmins) {

        if (projectNameExists(project.getCompanyId(), project.getName(), project.getId())) {
            throw new WrappedException(BizEnums.ProjectNameRepeat);
        }

        projectMapper.update(project);
        //全删全加
        projectMapper.deleteProjectAdmins(project.getId());
        for (TProjectAdmin projectAdmin : projectAdmins) {
            projectAdmin.setProjectId(project.getId());
            projectMapper.insertProjectAdmin(projectAdmin);
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<DProject> query(String account, Integer companyId, String orgCode, DProjectQueryReq queryReq, PageRequest pageRequest) throws InvocationTargetException, IllegalAccessException {

        List<DProject> dtos = new ArrayList<>();
        Integer total = projectMapper.queryTotal(account, companyId, orgCode, queryReq);

        if (total > 0) {//查询项目及相关任务
            List<DTProjectEx> pos = projectMapper.query(account, companyId, orgCode, queryReq, pageRequest);

            for (DTProjectEx po : pos) {
                DProject dto = new DProject();
                BeanConverter.copyProperties(dto, po);
//                dto = GsonUtil.fromJson(GsonUtil.toJson(po), new TypeToken<DProject>(){}.getType());
                dto.setStartDateL(po.getStartDate() != null ? po.getStartDate().getTime() : null);
                dto.setEndDateL(po.getEndDate() != null ? po.getEndDate().getTime() : null);
                dto.setScanEnable(po.getScanEnable());
                dtos.add(dto);
            }

            //查询项目下的任务
            List<Integer> projectIds = new ArrayList<>();
            for (TProject project : pos) {
                projectIds.add(project.getId());
            }

            List<DTTaskEx> dtoTasks = taskMapper.findTasksByProjectIds(projectIds);

            for (DProject dto : dtos) {
                for (DTTaskEx dtoTask : dtoTasks) {
                    if (dto.getId().equals(dtoTask.getProjectId())) {
                        DTask dt = new DTask();
                        BeanConverter.copyProperties(dt, dtoTask);
//                        dt = GsonUtil.fromJson(GsonUtil.toJson(dtoTask), new TypeToken<DTask>(){}.getType());
                        dt.setStartDateL(dtoTask.getStartDate().getTime());
                        dt.setEndDateL(dtoTask.getEndDate().getTime());
                        dt.setLatestStartDateL(dtoTask.getLatestStartDate().getTime());

                        dto.getTasks().add(dt);
                    }
                }
            }
        }

        PageResponse<DProject> pageResponse = new PageResponse(pageRequest, dtos, total);
        return pageResponse;
    }

    private boolean projectNameExists(Integer companyId, String name, Integer projectId) {
        return projectMapper.getProjectCntByName(companyId, name, projectId) > 0;
    }

    public void addErrMsgNum(Integer projectId, Integer addNum) {
        projectMapper.updateProjectErrNotify(projectId, addNum);
    }

    public void clearErrMsgNum(Integer projectId) {
        projectMapper.clearProjectErrNotify(projectId);
    }

    public List<DProjectSelect> findProjectSelectLists(String account, Integer companyId, String orgCode) {
        List<DProjectSelect> dtos = projectMapper.findSelect(account, companyId, orgCode);

        //查询项目下的任务
        List<Integer> projectIds = new ArrayList<>();
        for (DProjectSelect project : dtos) {
            projectIds.add(project.getId());
        }

        List<DTTaskEx> dtoTasks = taskMapper.findTasksByProjectIds(projectIds);

        for (DProjectSelect project : dtos) {
            for (DTTaskEx task : dtoTasks) {
                if (project.getId().equals(task.getProjectId())) {
                    DTaskSelect taskSelect = new DTaskSelect(task.getId(), task.getProjectId(), task.getName());
                    project.getTasks().add(taskSelect);
                }
            }
        }

        return dtos;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Integer copyProject(DProjectCopyReq copyReq, ServiceHeader header) throws InvocationTargetException, IllegalAccessException {

        DTProjectEx project = getProject(copyReq.getSourceProjectId());

        List<TProjectAdmin> projectAdmins = new ArrayList<>();
        for (TProjectAdmin projectAdmin : project.getProjectAdmins()) {
            if (projectAdmin.getAccount().equals(header.getOperatorName()))
                continue;//不把自己加到额外管理员里面
            TProjectAdmin tProjectAdmin = new TProjectAdmin();
            tProjectAdmin.setAccount(projectAdmin.getAccount());
            tProjectAdmin.setName(projectAdmin.getName());
            projectAdmins.add(tProjectAdmin);
        }
        project.setId(null);
        project.setName(copyReq.getName());
        project.setCreater(header.getOperatorName());
        project.setScanEnable(TProjectScanEnable.DISABLE.getValue());//复制的项目默认不开通扫码

        //先复制项目
        createProject(project, projectAdmins);

        //再复制任务
        List<DTTaskEx> dtoTasks = taskMapper.findTasksByProjectIds(Arrays.asList(copyReq.getSourceProjectId()));
        List<Integer> oriTaskIds = new ArrayList<>();
        for (DTTaskEx task : dtoTasks) {
            oriTaskIds.add(task.getId());
        }

        List<TTaskExaminer> taskExaminers = oriTaskIds.isEmpty() ? new ArrayList<>() : taskMapper.getTaskExaminers(oriTaskIds);
        List<List<TTaskExaminer>> tTaskExaminersOfTasks = new ArrayList<>();
        List<TTask> tasks = new ArrayList<>();

        for (DTTaskEx task : dtoTasks) {
            List<TTaskExaminer> tTaskExaminers = new ArrayList<>();
            tTaskExaminersOfTasks.add(tTaskExaminers);

            for (TTaskExaminer taskExaminer : taskExaminers) {
                if (taskExaminer.getTaskId().equals(task.getId())) {
                    TTaskExaminer tTaskExaminer = new TTaskExaminer();
                    tTaskExaminer.setExaminer(taskExaminer.getExaminer());
                    tTaskExaminer.setName(taskExaminer.getName());
                    tTaskExaminers.add(tTaskExaminer);
                }
            }

            TTask tt = new TTask();
            BeanConverter.copyProperties(tt, task);
//            tt = GsonUtil.fromJson(GsonUtil.toJson(task), new TypeToken<TTask>(){}.getType());
            tt.setExamTime(task.getExamTime());
            tt.setPageChangeLimit(task.getPageChangeLimit());

            tt.setProjectId(project.getId());
            tt.setCreater(header.getOperatorName());
            tt.setId(null);
            tasks.add(tt);
        }

        taskService.updateTasksByProject(header.getOperatorName(), project.getId(), tasks, tTaskExaminersOfTasks);

        return project.getId();
    }

    /**
     * 前端任务列表
     *
     * @param userId
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public List<DExamTaskResponse> queryTasksByUserId(Integer userId) throws InvocationTargetException, IllegalAccessException {
        List<DExamTaskResponse> dExamTaskResponses = new ArrayList<>();
        DExamTaskResponse dExamTaskResponse = null;
        List<DExamProjectTasks> dExamProjectTaskses = projectMapper.queryTasksByUserId(userId);//通过考生id查询所有项目和任务
        List<DExamTasks> dExamTaskses = null;
        boolean flag = false;
        Map<Integer, List<DExamTasks>> map = new HashMap();
        for (DExamProjectTasks dExamProjectTasks : dExamProjectTaskses) {//遍历所有项目和任务
            if (map.get(dExamProjectTasks.getProjectId()) == null) {//如果是不同项目的，则创建新的任务List
                dExamTaskses = new ArrayList<>();
                map.put(dExamProjectTasks.getProjectId(), dExamTaskses);
                dExamTaskResponse = new DExamTaskResponse();
                //过滤规则
                flag = saveDExamTaskes(dExamProjectTaskses.size(), dExamProjectTasks, dExamTaskses);
                if (!flag) {
                    continue;
                }
                //设置项目属性
                dExamTaskResponse.setProjectId(dExamProjectTasks.getProjectId());
                dExamTaskResponse.setDescription(dExamProjectTasks.getDescription());
                dExamTaskResponse.setProjectName(dExamProjectTasks.getProjectName());
                dExamTaskResponse.setProjectStartDate(dExamProjectTasks.getProjectStartDate());
                dExamTaskResponse.setProjectEndDate(dExamProjectTasks.getProjectEndDate());
                dExamTaskResponse.setdExamTasksList(dExamTaskses);
                dExamTaskResponses.add(dExamTaskResponse);
            } else {//如果是同一个项目的，则组装任务List
                dExamTaskses = map.get(dExamProjectTasks.getProjectId());
                flag = saveDExamTaskes(dExamProjectTaskses.size(), dExamProjectTasks, dExamTaskses);
                if (!flag) {
                    continue;
                }
            }
        }

        return dExamTaskResponses;
    }

    private boolean isInProjectTime(Date projectEndDate){
        if(projectEndDate == null)//没设置结束时间，则在项目时间内
            return true;
        if(projectEndDate.after(new Date()))//没到项目结束时间，则在项目时间内
            return true;

        return false;
    }

    /**
     * 判断规则：
     * 考生前台是否正常使用
     * 项目   产品    后台是否正常使用        未开始              答题中、已完成
     * 启用   启用                                                  
     * 禁用   禁用                            ⃝                      暂停
     * 启用   禁用                    （单个）⃝|（多个）      （单个）暂停|（多个）
     * ⃝ 表示项目、产品不可见
     * 暂停  表示项目产品可见，但是不可操作
     *
     * @param count
     * @param dExamProjectTasks
     * @param dExamTaskses
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private boolean saveDExamTaskes(Integer count, DExamProjectTasks dExamProjectTasks, List<DExamTasks> dExamTaskses) throws InvocationTargetException, IllegalAccessException {
        Date now = new Date();
        if (dExamProjectTasks.getProjectStartDate() != null && dExamProjectTasks.getProjectStartDate().after(now)) {//项目未开始
            return false;
        }
        DExamTasks dExamTasks = new DExamTasks();
        BeanConverter.copyProperties(dExamTasks, dExamProjectTasks);
//        dExamTasks = GsonUtil.fromJson(GsonUtil.toJson(dExamProjectTasks), new TypeToken<DExamTasks>(){}.getType());
        if (dExamProjectTasks.getProjectStatus() != TProjectStatus.DELETE.getValue()
                && dExamTasks.getTaskStatus() != TTaskStatus.DELETE.getValue()) {//两个都是删除暂时不处理
            if (dExamProjectTasks.getProjectStatus() == TProjectStatus.ENABLE.getValue()
                    && dExamTasks.getTaskStatus() == TTaskStatus.ENABLE.getValue() && isInProjectTime(dExamProjectTasks.getProjectEndDate())) {//两个都是启用添加且当前访问在项目时间内
                dExamTaskses.add(dExamTasks);
                if(dExamProjectTasks.getExamineeStatus().equals("未开始")){
                    if(dExamProjectTasks.getTaskStartDate().before(now) && dExamProjectTasks.getTaskLatestEndDate().after(now)){//考试已开始但未到最迟进场时间
                        dExamTasks.setCanAnswer(true);
                        dExamTasks.setTips("进入答题");
                    }else{
                        if(dExamProjectTasks.getTaskEndDate().before(now))
                            dExamTasks.setTips("考试已结束");
                        else if(dExamProjectTasks.getTaskStartDate().after(now))
                            dExamTasks.setTips("考试未开始");
                        else
                            dExamTasks.setTips("已超过最迟进场时间");
                    }
                }else if(dExamProjectTasks.getExamineeStatus().equals("答题中")){
                    if(dExamProjectTasks.getTaskEndDate().before(now))
                        dExamTasks.setTips("考试已结束");
                    else{
                        dExamTasks.setCanAnswer(true);
                        dExamTasks.setTips("继续答题");
                    }
                }else{
                    dExamTasks.setTips("已完成答题");
                }
            } else{
                if (dExamProjectTasks.getExamineeStatus().equals("未开始")) {//表示项目、产品不可见
                    return false;
                } else if (dExamProjectTasks.getExamineeStatus().equals("答题中") || dExamProjectTasks.getExamineeStatus().equals("已完成")) {
                    dExamTasks.setExamineeStatus("已停止");
                    dExamTasks.setTips("考试已过期/已停用");
                    dExamTaskses.add(dExamTasks);
                }
            }
//            else if (dExamProjectTasks.getProjectStatus() == TProjectStatus.DISABLE.getValue()
//                    && dExamTasks.getTaskStatus() == TTaskStatus.DISABLE.getValue()) {//两个都是禁用
//                if (dExamProjectTasks.getExamineeStatus().equals("未开始")) {//表示项目、产品不可见
//                    return false;
//                } else if (dExamProjectTasks.getExamineeStatus().equals("答题中") || dExamProjectTasks.getExamineeStatus().equals("已完成")) {
//                    dExamTasks.setExamineeStatus("已停止");
//                    dExamTaskses.add(dExamTasks);
//                }
//            } else if (dExamProjectTasks.getProjectStatus() == TProjectStatus.ENABLE.getValue()
//                    && dExamTasks.getTaskStatus() == TTaskStatus.DISABLE.getValue()) {
//                if (dExamProjectTasks.getExamineeStatus().equals("未开始")) {//如果有多个任务就继续,没有就跳出
//                    return false;
//                } else if ((dExamProjectTasks.getExamineeStatus().equals("答题中") || dExamProjectTasks.getExamineeStatus().equals("已完成"))) {
//                    dExamTasks.setExamineeStatus("已停止");
//                    dExamTaskses.add(dExamTasks);
//                }
//            }
        }
        return true;
    }

    public void createGroupAdmin(TAdmin admin) {
        projectMapper.deleteGroupAdmin(admin);
        projectMapper.insertGroupAdmin(admin);
    }
}
