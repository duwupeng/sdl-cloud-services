package com.talebase.cloud.ms.project.test.dto;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.project.dto.DProject;
import com.talebase.cloud.base.ms.project.dto.DProjectQueryReq;
import com.talebase.cloud.base.ms.project.dto.DTask;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.PageResponseWithParam;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class TestDProject {

    public static void main(String[] args){
        List<DProject> projects = new ArrayList<>();

        for(int i = 1; i < 2; i++){
            List<DTask> tasks = new ArrayList<>();
            DProject project = new DProject();
            project.setName("项目" + i);
            project.setId(i);
            project.setStartDateL(new Date().getTime());
            project.setEndDateL(new Date().getTime());
            project.setStatus(1);
            project.setTasks(tasks);
            projects.add(project);

            for(int j = 1; j < 2; j++){
                DTask task = new DTask();
                task.setId(i* 10 + j);
                task.setName("任务" + i* 10 + j);
                task.setStatus(1);
                task.setStartDateL(new Date().getTime());
                task.setEndDateL(new Date().getTime());
                task.setFinishNum(0);
                task.setPreNum(0);
                task.setInNum(0);
                task.setMarkedNum(0);
                tasks.add(task);
            }
        }

        DProjectQueryReq req = new DProjectQueryReq();
        req.setProjectNameLike("项");
        req.setTaskNameLike("任");

        PageRequest pageRequest = new PageRequest();
        pageRequest.setStart(0);
        pageRequest.setLimit(10);
        pageRequest.setPageIndex(1);
        pageRequest.setSortFields("by id");

        PageResponseWithParam<DProject, DProjectQueryReq> pageResponse = new PageResponseWithParam(pageRequest, req, projects, 3);

        ServiceResponse sr = new ServiceResponse<>(pageResponse);

        System.out.println(GsonUtil.toJson(sr));

    }

}
