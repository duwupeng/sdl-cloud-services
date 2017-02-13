package com.talebase.cloud.ms.examer.test.dto;

import com.talebase.cloud.base.ms.examer.dto.DUserExamPermission;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.base.ms.project.enums.TProjectStatus;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;

import java.util.Date;

/**
 * Created by kanghong.zhao on 2017-1-15.
 */
public class DUserExamPermissionTest {

    public static void main(String[] args){
        DUserExamPermission dUserExamPermission = new DUserExamPermission();

        dUserExamPermission.setProjectId(1);
        dUserExamPermission.setTaskId(1);
        dUserExamPermission.setExamerAdmin("examer1");
        dUserExamPermission.setExerciseEndTime(new Date());
        dUserExamPermission.setExercisePaperId(1);
        dUserExamPermission.setExerciseStartTime(new Date());
        dUserExamPermission.setProjectAdmin("admin");
        dUserExamPermission.setProjectName("项目1");
        dUserExamPermission.setProjectStatus(TProjectStatus.ENABLE.getValue());
        dUserExamPermission.setExercisePaperId(1);
//        dUserExamPermission.setExerciseStartTime(new Date());
        dUserExamPermission.setExerciseEndTime(new Date());
        dUserExamPermission.setTaskEndDate(new Date());
        dUserExamPermission.setTaskExamTime(60);
        dUserExamPermission.setTaskLatestStartDate(new Date());
        dUserExamPermission.setTaskName("考试任务1");
        dUserExamPermission.setTaskPageChangeLimit(3);
        dUserExamPermission.setTaskPaperId(1);
        dUserExamPermission.setTaskStatus(TTaskStatus.ENABLE.getValue());
        dUserExamPermission.setUserExamStatus(TUserExamStatus.ENABLED.getValue());
        dUserExamPermission.setTaskStartDate(new Date());

        ServiceResponse<DUserExamPermission> serviceResponse = new ServiceResponse<>(dUserExamPermission);
        System.out.println(GsonUtil.toJson(serviceResponse));
    }


}
