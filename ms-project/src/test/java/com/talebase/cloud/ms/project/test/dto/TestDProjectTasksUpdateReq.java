package com.talebase.cloud.ms.project.test.dto;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;
import com.talebase.cloud.base.ms.project.dto.DProjectTasksUpdateReq;
import com.talebase.cloud.common.util.GsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class TestDProjectTasksUpdateReq {

    public static void main(String[] args){

        DProjectTasksUpdateReq dto = new DProjectTasksUpdateReq();
        dto.setName("项目");
//        dto.setId(1);
        dto.setPaperId(1);
        dto.setDelayLimitTime(15);
        dto.setEndDateStr("2016-12-01 12:00:00");

        List<TTaskExaminer> examiners = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            TTaskExaminer examiner = new TTaskExaminer();
            examiner.setName("user" + i);
            examiner.setExaminer("examiner" + i);
            examiners.add(examiner);
        }

        dto.setExaminers(examiners);
        dto.setExamTime(60);
        dto.setFinishType(1);
        dto.setLatestStartDateStr("2016-12-01 12:30:00");
        dto.setPageChangeLimit(3);
        dto.setStartDateStr("2016-12-01 10:00:00");

        List<DProjectTasksUpdateReq> dtos = new ArrayList<>();
        dtos.add(dto);
        dtos.add(dto);
        dtos.add(dto);

        System.out.println(GsonUtil.toJson(dtos));
    }

}
