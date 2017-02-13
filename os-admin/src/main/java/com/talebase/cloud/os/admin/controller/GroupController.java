package com.talebase.cloud.os.admin.controller;

import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.os.admin.service.GroupService;
import com.talebase.cloud.base.ms.admin.dto.DGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroupCreateReq;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kanghongzhao on 16/11/27.
 */
@RestController
public class GroupController {
    
    @Autowired
    private GroupService groupService;

    @PutMapping("/group/name/{groupId}")
    public ServiceResponse updateGroupName( @PathVariable("groupId") Integer groupId, String newName){
        return groupService.updateGroupName(groupId, newName);
    }

    @DeleteMapping("/groups/delete")
    public ServiceResponse delete( String delIdsStr){
        return groupService.deleteGroups(StringUtil.toIntListByComma(delIdsStr));
    }

    @PostMapping("/group")
    public ServiceResponse add( DGroupCreateReq createReq){
        String name=createReq.getName();
        if(StringUtil.isEmpty(name)){
            throw new WrappedException(BizEnums.NameCannotNull);
        }
        return groupService.addGroup(createReq);
    }

    @GetMapping("/groups/query")
    public ServiceResponse<List<DGroup>> findByCompany( Integer companyId){
        ServiceResponse<List<DGroup>> serviceResponse = groupService.findByCompany(companyId);
        serviceResponse.setPermission(new HashMap<>());
        if(1 == 1){//有功能权限
            serviceResponse.getPermission().put("operatePermission", true);
        }else{
            serviceResponse.getPermission().put("operatePermission", false);
            for(int i = 0; i < serviceResponse.getResponse().size(); i++){
                DGroup group = serviceResponse.getResponse().get(i);
                if(!group.getOrgCode().startsWith(ServiceHeaderUtil.getRequestHeader().getOrgCode()))
                    group.setMemberNum(0);
            }
        }

        return serviceResponse;
    }
    
}
