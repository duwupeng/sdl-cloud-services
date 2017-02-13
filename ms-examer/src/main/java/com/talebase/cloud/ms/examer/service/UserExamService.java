package com.talebase.cloud.ms.examer.service;


import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.DExaminerTaskListToNotify;
import com.talebase.cloud.base.ms.examer.dto.DTaskExamInfo;
import com.talebase.cloud.base.ms.examer.dto.DUserExamInfo;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPermission;
import com.talebase.cloud.base.ms.notify.dto.DNotifyRecord;
import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.dto.DUseStatics;
import com.talebase.cloud.ms.examer.dao.UserExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@Service
public class UserExamService {

    @Autowired
    private UserExamMapper userExamMapper;

    /**
     * 获取考生考试信息
     *
     * @param userId
     * @return
     */
    public List<TUserExam> getUserExam(Integer userId) {
        return userExamMapper.getUserExam(userId);
    }

    /**
     * 获取考生未完成的任务总数(发送通知的参数$$任务数量$$)
     *
     * @param userId
     * @return
     */
    public Integer getTaskCount(Integer companyId, Integer userId, Integer projectId, Integer taskId) {
        return userExamMapper.getTaksCountToNotify(companyId, userId, projectId, taskId);
    }

    /**
     * 获取考生未完成的任务列表(发送通知的参数$$任务列表$$)
     *
     * @param userId
     * @return
     */
    public List<DExaminerTaskListToNotify> getTaskList(Integer companyId, Integer userId, Integer projectId, Integer taskId) {
        return userExamMapper.getTaksListToNotify(companyId, userId, projectId, taskId);
    }

    /**
     * 获取导出文件名称
     *
     * @param projectId
     * @param taskId
     * @return
     */
    public DUseStatics getProjectAndTaskExportName(Integer projectId, Integer taskId) {
        TProject tProject = userExamMapper.getProjectById(projectId);
        TTask tTask = userExamMapper.getTaskById(taskId);

        DUseStatics dUseStatics = new DUseStatics();
        dUseStatics.setProjectName(tProject.getName());
        dUseStatics.setTaskName(tTask.getName());

        return dUseStatics;
    }

    public DUserExamPermission getUserExamPermission(Integer taskId, Integer userId) {
        return userExamMapper.getUserExamPermission(taskId, userId);
    }

    public Integer updateSendStatus(List<DNotifyRecord> dNotifyRecords) {
        Integer count = 0;
        for (DNotifyRecord dNotifyRecord : dNotifyRecords) {
            Integer status = dNotifyRecord.getSendStatus() == 0 ? 2 : dNotifyRecord.getSendStatus() == 1 ? 3 : 1;
            count += userExamMapper.updateSendStatus(dNotifyRecord.getProjectId(), dNotifyRecord.getTaskId(),
                    dNotifyRecord.getAccount(), dNotifyRecord.getSendType(), status);
        }
        return count;
    }

    public List<DUserExamInfo> getUserExamInfos(Integer taskId) {
        return userExamMapper.getUserExamInfos(taskId);
    }

}
