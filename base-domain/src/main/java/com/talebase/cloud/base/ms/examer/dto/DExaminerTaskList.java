package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.project.dto.DTaskInScore;
import com.talebase.cloud.base.ms.project.dto.DTaskMarked;
import com.talebase.cloud.common.protocal.PageResponse;

/**
 * Created by kanghong.zhao on 2016-12-20.
 */
public class DExaminerTaskList {

    private PageResponse<DTaskInScore> pageResponse;
    private DTaskMarked taskMarked;

    public PageResponse<DTaskInScore> getPageResponse() {
        return pageResponse;
    }

    public void setPageResponse(PageResponse<DTaskInScore> pageResponse) {
        this.pageResponse = pageResponse;
    }

    public DTaskMarked getTaskMarked() {
        return taskMarked;
    }

    public void setTaskMarked(DTaskMarked taskMarked) {
        this.taskMarked = taskMarked;
    }
}
