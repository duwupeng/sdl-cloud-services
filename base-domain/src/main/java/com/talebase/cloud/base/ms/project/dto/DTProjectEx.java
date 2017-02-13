package com.talebase.cloud.base.ms.project.dto;

import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-3.
 */
public class DTProjectEx extends TProject{

    private String orgCode;

    private Integer errNum = 0;

    private List<TProjectAdmin> projectAdmins;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public Integer getErrNum() {
        return errNum;
    }

    public void setErrNum(Integer errNum) {
        this.errNum = errNum;
    }

    public List<TProjectAdmin> getProjectAdmins() {
        return projectAdmins;
    }

    public void setProjectAdmins(List<TProjectAdmin> projectAdmins) {
        this.projectAdmins = projectAdmins;
    }
}
