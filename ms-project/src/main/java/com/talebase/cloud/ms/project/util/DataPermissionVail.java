package com.talebase.cloud.ms.project.util;

import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;
import com.talebase.cloud.base.ms.project.dto.DTProjectEx;
import com.talebase.cloud.base.ms.project.enums.TProjectStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-3.
 */
public class DataPermissionVail {

    public static void vailProject(DTProjectEx project, String account, Integer companyId, String orgCode){
        if(project == null || TProjectStatus.DELETE.getValue() ==  project.getStatus()){
            throw new WrappedException(BizEnums.ProjectNotExist);
        }

        if(!project.getCompanyId().equals(companyId)){
            throw new WrappedException(BizEnums.ProjectNoPermission);
        }

        List<String> exAccounts = new ArrayList<>();
        for(TProjectAdmin admin : project.getProjectAdmins()){
            exAccounts.add(admin.getAccount());
        }

        if(!project.getOrgCode().startsWith(orgCode) && !exAccounts.contains(account)){
            throw new WrappedException(BizEnums.ProjectNoPermission);
        }
    }

}
