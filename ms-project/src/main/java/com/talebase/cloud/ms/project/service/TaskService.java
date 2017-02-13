package com.talebase.cloud.ms.project.service;

import com.google.common.reflect.TypeToken;
import com.talebase.cloud.base.ms.notify.dto.DTaskFinishType;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;
import com.talebase.cloud.base.ms.project.domain.TTaskProgress;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.ms.project.dao.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-3.
 */
@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 更新状态
     * @param operatorName
     * @param taskId
     * @param newStatus
     */
    public void updateStatus(String operatorName, Integer taskId, Integer newStatus){
        if(newStatus.equals(TTaskStatus.ENABLE.getValue()) || newStatus.equals(TTaskStatus.DISABLE.getValue())){
            taskMapper.updateStatus(operatorName, taskId, newStatus);
        }else{
            throw new WrappedException(BizEnums.UnknownStatus);
        }
    }

    /**
     * 删除
     * @param operatorName
     * @param taskId
     */
    public void delete(String operatorName, Integer taskId){
        if(hasExaminee(taskId)){
            throw new WrappedException(BizEnums.HasExaminee);
        }

        taskMapper.updateStatus(operatorName, taskId, TTaskStatus.DELETE.getValue());
    }

    /**
     * 已经有人参与考试
     * @param projectId
     * @return
     */
    private boolean hasExaminee(Integer projectId){
        TTaskProgress progress = taskMapper.getProgress(projectId);
        if(progress.getInNum() > 0){
            return true;
        }
        return false;
    }

    public List<DTaskInEdit> findTaskInEditByProjectId(Integer projectId) throws InvocationTargetException, IllegalAccessException {
        List<DTTaskEx> ddtaskexs = taskMapper.findTasksByProjectIds(Arrays.asList(projectId));
        List<DTaskInEdit> dTaskInEdits = new ArrayList<>();

        if(ddtaskexs.size() == 0)
            return dTaskInEdits;

        List<Integer> taskIds = new ArrayList<>();
        for(DTTaskEx ex : ddtaskexs){
            taskIds.add(ex.getId());
        }
        List<TTaskExaminer> taskExaminers = taskMapper.getTaskExaminers(taskIds);

        for(DTTaskEx ex : ddtaskexs){
            DTaskInEdit inEdit = new DTaskInEdit();
            BeanConverter.copyProperties(inEdit, ex);
            inEdit.setPageChangeLimit(ex.getPageChangeLimit());
            inEdit.setExamTime(ex.getExamTime());

//            inEdit = GsonUtil.fromJson(GsonUtil.toJson(ex), new TypeToken<DTaskInEdit>(){}.getType());
            inEdit.setStartDateL(ex.getStartDate().getTime());
            inEdit.setEndDateL(ex.getEndDate().getTime());
            inEdit.setLatestStartDateL(ex.getLatestStartDate().getTime());

            if(inEdit.getExamTime() != null && inEdit.getExamTime() > 0){
                inEdit.setFinishType(DTaskFinishType.ExamTimeType);
            }else{
                inEdit.setFinishType(DTaskFinishType.DeadLineType);
            }

            dTaskInEdits.add(inEdit);

            inEdit.setExaminers(new ArrayList<>());
            for(TTaskExaminer tTaskExaminer : taskExaminers){
                if(inEdit.getId().equals(tTaskExaminer.getTaskId())){
                    inEdit.getExaminers().add(new DTaskExamier(tTaskExaminer));
                }
            }
        }

        return dTaskInEdits;
    }

    public DTTaskEx findTaskExById(Integer id) throws InvocationTargetException, IllegalAccessException {
        return taskMapper.findTaskExById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateTasksByProject(String operatorName, Integer projectId, List<TTask> tasks, List<List<TTaskExaminer>> examinersList){

        //找出旧数据
        List<DTTaskEx> oldTasks = taskMapper.findTasksByProjectIds(Arrays.asList(projectId));

        //先清理旧数据
        deleteByProjectUpdate(operatorName, oldTasks, tasks);

        //更新旧数据或插入新数据
        for(int i = 0; i < tasks.size(); i++){
            TTask task = tasks.get(i);
            List<TTaskExaminer> examiners = examinersList.get(i);

            Integer taskId = task.getId();

            if(taskId == null){//插入新数据
                if(task.getStatus() == null)
                    task.setStatus(TTaskStatus.ENABLE.getValue());
                taskMapper.insert(task);
                taskId = task.getId();
                taskMapper.insertTaskProgress(taskId);
            }else {//更新旧数据

                boolean isOldTaskId = false;
                for(DTTaskEx oTask : oldTasks){
                    if(oTask.getId().equals(taskId)){
                        isOldTaskId = true;
                        break;
                    }
                }

                if(!isOldTaskId){//若传入非本项目的任务id，则不予处理
                    continue;
                }

                checkIfTaskStart(task, oldTasks);//已开始的任务对可更新的字段有限制
                taskMapper.update(task);
            }

            taskMapper.deleteTaskExaminers(taskId);

            for(TTaskExaminer taskExaminer : examiners){
                taskExaminer.setTaskId(taskId);
                taskMapper.insertTaskExaminer(taskExaminer);
            }
        }

    }

    private void deleteByProjectUpdate(String operatorName, List<DTTaskEx> tasksOld, List<TTask> tasks){
//        List<DTTaskEx> tasksOld = taskMapper.findTasksByProjectIds(Arrays.asList(projectId));

        //先删旧数据
        for(DTTaskEx exOld : tasksOld){
            boolean toDel = true;
            for(TTask task : tasks){
                if(exOld.getId().equals(task.getId())){
                    toDel = false;
                    break;
                }
            }

            if(toDel){
                delete(operatorName, exOld.getId());
            }
        }
    }

    public List<TTaskExaminer> findTaskExaminersByTask(Integer taskId){
        return taskMapper.getTaskExaminers(Arrays.asList(taskId));
    }

    private void checkIfTaskStart(TTask task, List<DTTaskEx> tasksOld){
        //检验已经开始的任务；若任务已开始，则只允许修改最大切换次数和评卷人，其他属性不能修改
        for(DTTaskEx taskEx : tasksOld){
            if(taskEx.getId().equals(task.getId())){
                if(taskEx.getStartDate().before(new Date())){
                    checkEqual(taskEx.getStartDate(), task.getStartDate());
                    checkEqual(taskEx.getEndDate(), task.getEndDate());
                    checkEqual(taskEx.getExamTime(), task.getExamTime());
                    checkEqual(taskEx.getLatestStartDate(), task.getLatestStartDate());
                    checkEqual(taskEx.getName(), task.getName());
                    break;
                }
            }
        }
    }

    private void checkEqual(Object obj1, Object obj2){
        if(obj1 == null && obj2 == null)
            return;

        if(obj1 != null && obj1.equals(obj2))
            return;

        throw new WrappedException(BizEnums.TaskCannotModifyAfterStart);
    }

    public PageResponse<DTaskInScore> findTasksByExaminer(String operatorName, Integer companyId, PageRequest pageRequest){
        PageResponse<DTaskInScore> pageResponse = new PageResponse(pageRequest);

        Integer total = taskMapper.findTasksNumByExaminer(operatorName, companyId);
        pageResponse.setTotal(total);
        if(total > 0){
            List<DTaskInScore> taskInScores = taskMapper.findTasksByExaminer(operatorName, companyId, pageRequest);
            pageResponse.setResults(taskInScores);
            for(DTaskInScore task : taskInScores){
                task.setProjectStartDateL(task.getProjectStartDate() != null ? task.getProjectStartDate().getTime() : null);
                task.setProjectEndDateL(task.getProjectEndDate() != null ? task.getProjectEndDate().getTime() : null);
            }
        }
        return pageResponse;
    }

    public DTaskMarked getTaskInMark(String operatorName, Integer companyId){
        return taskMapper.getTaskInMark(operatorName, companyId);
    }

    public TTask get(Integer taskId){
        return taskMapper.get(taskId);
    }

    public List<DUseStatics> findUseStatics(List<DUseStaticsQueryReq> reqs, ServiceHeader serviceHeader){
        return taskMapper.findUseStatics(reqs, serviceHeader);
    }

    public void updateTaskPaperVersion(TTask task){
        checkHasExaminee(task.getId());
        taskMapper.updatePaper(task);
    }

    private void checkHasExaminee(Integer taskId){
        if(hasExaminee(taskId)){
            throw new WrappedException(BizEnums.HasExaminee);
        }
    }

}
