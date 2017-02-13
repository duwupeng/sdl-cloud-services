package com.talebase.cloud.ms.admin.controller;

import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroupCreateReq;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.admin.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PutMapping("/serviceAdmin/group/name/{groupId}")
    public ServiceResponse updateGroupName(@PathVariable("groupId") Integer groupId, @RequestBody ServiceRequest<String> serviceRequest){
        groupService.updateGroupName(serviceRequest.getRequestHeader().operatorName, serviceRequest.getRequestHeader().companyId,
                groupId, serviceRequest.getRequest());
        return new ServiceResponse();
    }

    @DeleteMapping("/serviceAdmin/groups/delete")
    public ServiceResponse delete(@RequestBody ServiceRequest<List<Integer>> serviceRequest){
        groupService.deleteGroups(serviceRequest.getRequestHeader().operatorName, serviceRequest.getRequestHeader().companyId, serviceRequest.getRequest());
        return new ServiceResponse();
    }

    @PostMapping("/serviceAdmin/group")
    public ServiceResponse add(@RequestBody ServiceRequest<DGroupCreateReq> serviceRequest){
        TGroup group = new TGroup();
        group.setParentId(serviceRequest.getRequest().getParentId());
        group.setName(serviceRequest.getRequest().getName());
        if(serviceRequest.getRequest().getParentId() != 0){
            group.setCreater(serviceRequest.getRequestHeader().operatorName);
            group.setModifier(serviceRequest.getRequestHeader().operatorName);
            group.setCompanyId(serviceRequest.getRequestHeader().companyId);
        }else{
            group.setCreater("system");
            group.setModifier("system");
            group.setCompanyId(serviceRequest.getRequest().getCompanyId());
        }
        return new ServiceResponse(groupService.addGroup(group));
    }

    @PostMapping("/serviceAdmin/groups/query")
    public ServiceResponse<List<DGroup> > findByCompany(@RequestBody ServiceRequest<Integer> serviceRequest){
        List<DGroup> groups = groupService.findGroupsWithAdminCnt(serviceRequest.getRequest(),serviceRequest.getRequestHeader().getPermissions(),serviceRequest.getRequestHeader().getOrgCode());


        for(int i = 0; i < groups.size(); i++){
            DGroup group = groups.get(i);
//            if(group.getOrgCode().startsWith(serviceRequest.getRequestHeader().orgCode))
//                group.setOperatePermission("1");
//            else
//                group.setMemberNum(0);

            if(i < (groups.size() - 1)){//若下一个的前缀是当前的组织代码，则下一个为当前这个的子元素
                if(groups.get(i + 1).getOrgCode().startsWith(group.getOrgCode())){
                    group.setHasSon(true);
                }
            }
        }


        return new ServiceResponse(groups);
    }

    @GetMapping("/serviceAdmin/group/{groupId}")
    public ServiceResponse<TGroup> get(@PathVariable("groupId") Integer groupId){
        return new ServiceResponse(groupService.get(groupId));
    }

}
