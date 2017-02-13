package com.talebase.cloud.ms.project.test.dto;

import com.talebase.cloud.base.ms.examer.dto.DTaskExamInfo;
import com.talebase.cloud.base.ms.examer.dto.DUserExamInfo;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2017-2-8.
 */
public class TestDTaskExamInfo {

    public static void main(String[] args){
        DTaskExamInfo examInfo = new DTaskExamInfo();
        examInfo.setProjectId(1);
        examInfo.setProjectName("项目名称");
        examInfo.setTaskId(1);
        examInfo.setTaskName("名称");
        System.out.println(GsonUtil.toJson(new ServiceResponse<>(examInfo)));

        List<DUserExamInfo> userInfos = new ArrayList<>();
        for(int i = 1; i <= 3; i++){
            DUserExamInfo userExamInfo = new DUserExamInfo();
            userExamInfo.setAccount("账号" + i);
            userExamInfo.setId(i);
            userExamInfo.setName("账号" + i);
            userInfos.add(userExamInfo);
        }
        System.out.println(GsonUtil.toJson(new ServiceResponse<>(userInfos)));
    }

}
