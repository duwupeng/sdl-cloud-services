package com.talebase.cloud.ms.project.test.dto;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;
import com.talebase.cloud.base.ms.project.dto.DTaskExamier;
import com.talebase.cloud.base.ms.project.dto.DTaskInEdit;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class TestDTaskInEdit {

    public static void main(String[] args){
        List<DTaskInEdit> tasks = new ArrayList<>();

        for(int i = 1; i < 4; i++){
            DTaskInEdit task = new DTaskInEdit();
            task.setName("任务1");
            task.setId(i);
            task.setEndDateL(new Date().getTime());
            task.setStartDateL(new Date().getTime());
            task.setFinishType(1);
            task.setPageChangeLimit(3);
            task.setLatestStartDateL(new Date().getTime());
            task.setExamTime(60);
            task.setExaminers(new ArrayList<>());
            task.setPaperId(i);
            StringBuffer sb = new StringBuffer();
            for(int j = 0; j < 3; j++){
//                task.getExaminers().add("admin" + j + ", 张三" + j);
                TTaskExaminer taskExaminer = new TTaskExaminer();
                taskExaminer.setName("张三" + j);
                taskExaminer.setExaminer("admin" + j);
                task.getExaminers().add(new DTaskExamier(taskExaminer));
            }
            tasks.add(task);
        }

        System.out.println(GsonUtil.toJson(new ServiceResponse<>(tasks)));
    }

}
