package com.talebase.cloud.ms.examer.controller;

import com.talebase.cloud.base.ms.examer.dto.DDataManagementRequest;
import com.talebase.cloud.base.ms.examer.dto.DDataManagementResponse;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.examer.service.DataManagementService;
import com.talebase.cloud.ms.examer.service.UserFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bin.yang on 2016-12-21.
 */
@RestController
public class DataManagementController {

    @Autowired
    DataManagementService dataManagementService;
    @Autowired
    UserFieldService userFieldService;

    @PostMapping(value = "/examer/dataManagement")
    public ServiceResponse<PageResponse<DDataManagementResponse>> getList(@RequestBody ServiceRequest<DDataManagementRequest> req) throws Exception {

        PageResponse<DDataManagementResponse> pageResponse = new PageResponse(req.getPageReq());
        Integer count = dataManagementService.getCount(req.getRequestHeader().getCompanyId(), req.getRequest());
        pageResponse.setTotal(count);

        if(count > 0){
            List<DDataManagementResponse> dDataManagementResponseList = dataManagementService.getList(req.getPageReq(), req.getRequestHeader().getCompanyId(), req.getRequest());
            pageResponse.setResults(dDataManagementResponseList);

            List<Integer> ids = new ArrayList<>();
            for(DDataManagementResponse dd : dDataManagementResponseList){
                ids.add(dd.getId());
            }

            List<Map<String, Object>> list = userFieldService.getProjectExamByIds(ids, req.getRequestHeader(), req.getRequest().getProjectId(), req.getRequest().getTaskId());
            for(DDataManagementResponse dd : dDataManagementResponseList){
                for(Map<String, Object> map : list){
                    if(!map.containsKey("id")){
                        continue;
                    }

                    String value = (String)map.get("id");
                    if(dd.getId().toString().equals(value)){
                        dd.setUserInfos((List<Map<String,Object>>)map.get("userInfos"));
                        break;
                    }
                }
            }

        }else{
            pageResponse.setResults(new ArrayList<>());
        }

        return new ServiceResponse(pageResponse);
    }

}
