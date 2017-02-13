package com.talebase.cloud.ms.admin.service;

import com.talebase.cloud.base.ms.admin.domain.TGroup;
import com.talebase.cloud.base.ms.admin.dto.DGroup;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.ms.admin.dao.GroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-11-25.
 */
@Service
public class GroupService {

    @Autowired
    private GroupMapper groupMapper;

    /**
     * 修改组名
     *
     * @param operatorName
     * @param companyId
     * @param groupId
     * @param newGroupName
     */
    public void updateGroupName(String operatorName, Integer companyId, Integer groupId, String newGroupName){
        checkGroupNameExists(companyId, newGroupName, groupId);
        Integer updateRows = groupMapper.updateGroupName(newGroupName, operatorName, companyId, groupId);
        if(updateRows == 0){
            throw new WrappedException(BizEnums.GroupNameUpdateNoResult);
        }
    }

    /**
     * 检查组名唯一
     * @param companyId
     * @param groupName
     */
    private void checkGroupNameExists(Integer companyId, String groupName, Integer groupId){
        if(groupNameExists(companyId, groupName) & !groupIdSame(companyId, groupName,groupId)){
            throw new WrappedException(BizEnums.GroupNameRepeat);
        }
    }

    public Boolean groupNameExists(Integer companyId, String groupName){
        return groupMapper.getCntByName(companyId, groupName) > 0;
    }
    public Boolean groupIdSame(Integer companyId, String groupName,Integer groupId){
        return groupMapper.updateNameSameId(companyId, groupName,groupId) > 0;
    }

    /**
     * 删除分组
     * 自动删除下级分组，若所删除的分组中有管理员则返回错误提示
     * @param operatorName
     * @param companyId
     * @param groupIds
     */
    public void deleteGroups(String operatorName, Integer companyId, List<Integer> groupIds){

        List<TGroup> groupsToDel = groupMapper.findGroupsByIds(companyId, groupIds);
        List<String> orgCodes = new ArrayList<>();
        for(TGroup group : groupsToDel){
            if(group.getParentId() == null || group.getParentId().equals(0)){
                throw new WrappedException(BizEnums.MainGroupDelete);
            }
            orgCodes.add(group.getOrgCode());
        }

        Integer adminCntInGroupToDel = groupMapper.findAdminCntByGroups(orgCodes);
        if(adminCntInGroupToDel > 0){
            throw new WrappedException(BizEnums.HasAdminGroupDelete);
        }

        groupMapper.updateStatusToDelete(orgCodes, companyId, operatorName);
    }

    /**
     * 增加下级分组
     * @param group
     * @return
     */
    public Integer addGroup(TGroup group){
        checkGroupNameExists(group.getCompanyId(), group.getName(),group.getId());

        TGroup parentGroup = null;
        if(group.getParentId() != 0){
            parentGroup = groupMapper.get(group.getParentId());
            if(parentGroup == null){
                throw new WrappedException(BizEnums.ParentGroupNotExists);
            }
            char h = '_';
            int i = 0;
            for(char c : parentGroup.getOrgCode().toCharArray()){
                if(c == h)
                    i++;
            }

            if(i >= 10){
                throw new WrappedException(BizEnums.MaxGroupErr);
            }
        }

        groupMapper.insert(group);
        group.setOrgCode((parentGroup == null ? "" : parentGroup.getOrgCode()) + group.getId() + "_");
        groupMapper.updateOrgCode(group.getId(), group.getOrgCode());

        return group.getId();
    }

    public List<TGroup> findGroups(Integer companyId){
        return groupMapper.findGroups(companyId);
    }

    /**
     * 查询分组(含管理员数量及数据权限过滤)
     * @param companyId
     * @return
     */
    public List<DGroup> findGroupsWithAdminCnt(Integer companyId,List<String> permissions,String orgCode){
        return groupMapper.findGroupsWithAdminCnt(companyId,permissions,orgCode);
    }

    public List<TGroup> findGroupsByOrgCode(Integer companyId, String orgCode){
        return groupMapper.findGroupsByOrgCode(companyId, orgCode);
    }

    public TGroup get(Integer groupId){
        return groupMapper.get(groupId);
    }

}
