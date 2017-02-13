package com.talebase.cloud.ms.examer.service;

import com.talebase.cloud.base.ms.examer.dto.DDataManagementRequest;
import com.talebase.cloud.base.ms.examer.dto.DDataManagementResponse;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.examer.dao.DataManagementMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bin.yang on 2016-12-21.
 */
@Service
public class DataManagementService {

    @Autowired
    DataManagementMapper dataManagementMapper;

    public List<DDataManagementResponse> getList(PageRequest pageRequest, Integer companyId, DDataManagementRequest dDataManagementRequest){
        List<DDataManagementResponse> dDataManagements = dataManagementMapper.query(pageRequest,companyId,dDataManagementRequest);
        return dDataManagements;
    }

    public Integer getCount(Integer companyId, DDataManagementRequest dDataManagementRequest){
        Integer count = dataManagementMapper.queryCount(companyId,dDataManagementRequest);
        return count;
    }
}
