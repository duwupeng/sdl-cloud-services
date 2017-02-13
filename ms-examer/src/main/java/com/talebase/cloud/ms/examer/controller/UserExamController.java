package com.talebase.cloud.ms.examer.controller;

import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.DExaminerTaskListToNotify;
import com.talebase.cloud.base.ms.examer.dto.DUserExamInfo;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPermission;
import com.talebase.cloud.base.ms.notify.dto.DNotifyRecord;
import com.talebase.cloud.base.ms.project.dto.DUseStatics;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.ms.examer.service.UserExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhangchunlin on 2016-12-20.
 */
@RestController
public class UserExamController {
    @Autowired
    private UserExamService userExamService;

    /**
     * 获取考生考试信息
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/examer/getUserExam")
    public ServiceResponse<List<TUserExam>> getUserExam(@RequestBody Integer userId) {
        List<TUserExam> list = userExamService.getUserExam(userId);
        return new ServiceResponse<>(list);
    }

    /**
     * 获取考生未完成的任务总数(发送通知的参数$$任务数量$$)
     *
     * @return
     */
    @GetMapping(value = "/examer/getTaskCount/{companyId}/{userId}/{projectId}/{taskId}")
    public ServiceResponse<Integer> getTaskCount(
            @PathVariable("companyId") Integer companyId,
            @PathVariable("userId") Integer userId,
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId) {
        Integer count = userExamService.getTaskCount(companyId, userId, projectId, taskId);
        return new ServiceResponse<>(count);
    }

    /**
     * 获取考生未完成的任务列表(发送通知的参数$$任务列表$$)
     *
     * @return
     */
    @GetMapping(value = "/examer/getTaskList/{companyId}/{userId}/{projectId}/{taskId}")
    public ServiceResponse<DExaminerTaskListToNotify> getTaskList(
            @PathVariable("companyId") Integer companyId,
            @PathVariable("userId") Integer userId,
            @PathVariable("projectId") Integer projectId,
            @PathVariable("taskId") Integer taskId) {
        List<DExaminerTaskListToNotify> dExaminerTaskListToNotifies = userExamService.getTaskList(companyId, userId, projectId, taskId);
        return new ServiceResponse(dExaminerTaskListToNotifies);
    }

    /**
     * 获取导出名字
     *
     * @param projectId
     * @param taskId
     * @return
     */
    @GetMapping(value = "/examer/projectAndTaskExportName/{projectId}/{taskId}")
    public ServiceResponse<DUseStatics> getProjectAndTaskExportName(@PathVariable("projectId") Integer projectId,
                                                                    @PathVariable("taskId") Integer taskId) {
        DUseStatics dUseStatics = userExamService.getProjectAndTaskExportName(projectId, taskId);
        ServiceResponse<DUseStatics> response = new ServiceResponse<DUseStatics>();
        response.setResponse(dUseStatics);
        return response;
    }

    @GetMapping(value = "/examer/getUserExamPermission/{taskId}/{userId}")
    public ServiceResponse<DUserExamPermission> getUserExamPermission(@PathVariable("taskId") Integer taskId, @PathVariable("userId") Integer userId) {
        DUserExamPermission userExamPermission = userExamService.getUserExamPermission(taskId, userId);
        if (userExamPermission == null)
            throw new WrappedException(BizEnums.ExamNotExists);

        return new ServiceResponse(userExamPermission);
    }

    @PutMapping(value = "/examer/updateExamSendStatus")
    public ServiceResponse<DUserExamPermission> updateExamSendStatus(@RequestBody ServiceRequest<List<DNotifyRecord>> serviceRequest) {
        Integer count = userExamService.updateSendStatus(serviceRequest.getRequest());

        return new ServiceResponse(count);
    }

    @GetMapping(value = "/examer/examers/{taskId}")
    public ServiceResponse<List<DUserExamInfo>> findExamers(@PathVariable("taskId") Integer taskId) {
        List<DUserExamInfo> infos = userExamService.getUserExamInfos(taskId);
        return new ServiceResponse(infos);
    }

}
