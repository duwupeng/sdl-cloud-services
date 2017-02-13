package com.talebase.cloud.base.ms.project.dto;

import com.talebase.cloud.base.ms.examer.dto.DDataManagementResponse;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.common.protocal.PageResponse;

/**
 * Created by kanghong.zhao on 2016-12-3.
 */
public class DTTaskEx extends TTask {

    /**
     * 预考人数(考生账号已与任务关联)
     */
    Integer preNum;

    /**
     * 已进入考试人数
     */
    Integer inNum;

    /**
     * 交卷人数
     */
    Integer finishNum;

    /**
     * 已评卷人数
     */
    Integer markedNum;

    /**
     * 考生信息分页信息
     */
    PageResponse<DDataManagementResponse> pageResponse;

    public Integer getPreNum() {
        return preNum;
    }

    public void setPreNum(Integer preNum) {
        this.preNum = preNum;
    }

    public Integer getInNum() {
        return inNum;
    }

    public void setInNum(Integer inNum) {
        this.inNum = inNum;
    }

    public Integer getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(Integer finishNum) {
        this.finishNum = finishNum;
    }

    public Integer getMarkedNum() {
        return markedNum;
    }

    public void setMarkedNum(Integer markedNum) {
        this.markedNum = markedNum;
    }

    public PageResponse<DDataManagementResponse> getPageResponse() {
        return pageResponse;
    }

    public void setPageResponse(PageResponse<DDataManagementResponse> pageResponse) {
        this.pageResponse = pageResponse;
    }
}
