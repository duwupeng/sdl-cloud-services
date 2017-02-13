package com.talebase.cloud.ms.project.test.dto;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.project.dto.DTasksUpdateReq;
import com.talebase.cloud.common.util.GsonUtil;

/**
 * Created by kanghong.zhao on 2016-12-1.
 */
public class TestDTasksUpdateReq {

    public static void main(String[] args){

        DTasksUpdateReq dto = new DTasksUpdateReq();

//        dto.setTaskIdsStr("1;2;3");
//        dto.setDelayLimitTimesStr("15;;20");
//        dto.setEndDatesStr("2016-01-01 12:00:00;;2016-01-01 14:00:00");
//        dto.setExaminersStr("examiner1,examiner2;examiner1,exmainer3;");
//        dto.setExamTimesStr(";60;");
//        dto.setLatestStartDatesStr(";2016-01-01 13:15:00;");
//        dto.setPageChangeLimitsStr("3;;5");
//        dto.setPaperIdsStr("1;2;3");
//        dto.setStartDatesStr("2016-01-01 12:00:00;2016-01-01 13:00:00;2016-01-01 14:00:00");
//        dto.setTaskNamesStr("任务1;任务2;任务3");

        System.out.println(GsonUtil.toJson(dto));
    }

}
