package com.talebase.cloud.base.ms.examer.dto;

import com.talebase.cloud.base.ms.project.dto.DTaskMarked;
import com.talebase.cloud.common.protocal.PageResponse;

/**
 * Created by kanghong.zhao on 2016-12-20.
 */
public class DExamineeTaskDetail {

    private PageResponse<DExamineeInTask> examinees;
    private DTaskMarked taskMarked;
    private String taskName;
    private Integer paperId;

    public PageResponse<DExamineeInTask> getExaminees() {
        return examinees;
    }

    public void setExaminees(PageResponse<DExamineeInTask> examinees) {
        this.examinees = examinees;
    }

    public DTaskMarked getTaskMarked() {
        return taskMarked;
    }

    public void setTaskMarked(DTaskMarked taskMarked) {
        this.taskMarked = taskMarked;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }
}
