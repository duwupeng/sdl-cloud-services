package com.talebase.cloud.ms.examer.controller;

import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.ms.examer.service.UserFieldService;
import com.talebase.cloud.ms.examer.service.UserInfoService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@RestController
public class UserFieldController {
    @Autowired
    private UserFieldService userFieldService;
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping(value = "/examers/{companyId}")
    public ServiceResponse<DUserShowFieldResponseList> getGlobalExamers(@PathVariable("companyId") Integer companyId){
        return userFieldService.getGlobalExamers(companyId);
    }

    /**
     * 添加可选信息账号字段
     * @param request
     * @return
     */
    @PostMapping(value = "/exam/add")
    public ServiceResponse<String> addExamByOptional(@RequestBody ServiceRequest<DUserShowField> request){
        DUserShowField dUserShowField = request.getRequest();
        return  userFieldService.addExamByOptional(dUserShowField);
    }

    /**
     * 初始化公司全局字段
     * @return
     */
    @PostMapping(value = "/exam/iniCompanyUserField/{companyId}")
    public ServiceResponse<String> iniCompanyUserField(@PathVariable("companyId") Integer companyId){
        userFieldService.iniCompanyUserField(companyId);
        return  new ServiceResponse<>();
    }

    /**
     * 删除自定义字段
     * @param request
     * @return
     */
    @PostMapping(value = "/exam/del")
    public ServiceResponse del(@RequestBody ServiceRequest<DUserShowField> request){
        DUserShowField dUserShowField = request.getRequest();
        return  userFieldService.del(dUserShowField);
    }

    /**
     * 添加自定义字段
     * @param request
     * @return
     */
    @PostMapping(value = "/exam/saveGlobalAll")
    public ServiceResponse saveGlobalAll(@RequestBody ServiceRequest<DUserShowField> request){
        DUserShowField dUserShowField = request.getRequest();
        if (StringUtils.isEmpty(dUserShowField.getTaskId())
                && StringUtils.isEmpty(dUserShowField.getProjectId())){//保存全局字段
            return  userFieldService.saveGlobalAll(dUserShowField);
        }else{//保存项目字段设置
            return  userFieldService.saveAll(dUserShowField);
        }
    }

    /**
     * 根据项目任务查询字段(若没有定义则返回全局字段)
     * @param req
     * @param companyId
     * @return
     */
    @GetMapping(value = "/examer/fields/{companyId}")
    public ServiceResponse<List<TUserShowField>> findExamerFields(DProjectTaskReq req, @PathVariable("companyId") Integer companyId){
        List<TUserShowField> list = userFieldService.findUserFields(companyId, req.getProjectId(), req.getTaskId());
        return new ServiceResponse<>(list);
    }

    /**
     * 获取项目账号管理列表
     * @param req
     * @return
     */
    @PostMapping(value = "/examer/project")
    public ServiceResponse<PageResponse> getProjectExam(@RequestBody ServiceRequest<DUserExamPageRequest> req)throws Exception{
        PageResponse pageResponse = userFieldService.getProjectExam(req);
        return new ServiceResponse(pageResponse);
    }

    /**
     * 获取姓名字段显示列表
     * @param companyId
     * @return
     */
    @PostMapping(value ="/examer/project/fields/{companyId}" )
    public ServiceResponse<DUserShowFieldResponseList> getProjectExamerShowFields(@RequestBody ServiceRequest<DProjectTaskReq> request
            ,@PathVariable("companyId") Integer companyId){
        DProjectTaskReq req = request.getRequest();
        return userFieldService.getProjectExamerShowFields(companyId,req.getProjectId(),req.getTaskId());
    }

    /**
     * 获取新增时字段显示
     * @param request
     * @return
     */
    @PostMapping(value = "/examer/project/newField")
    public ServiceResponse<List<Map<String,Object>>> getFieldByNew(@RequestBody ServiceRequest<DReSetPassword> request)throws Exception{
        List<Map<String,Object>> list = userFieldService.getField(request);
        ServiceResponse<List<Map<String,Object>>> response = new ServiceResponse<List<Map<String,Object>>>();
        response.setResponse(list);
        return response;
    }


    /**
     * 获取新增时字段显示
     * @param request
     * @return
     */
    @PostMapping(value = "/examer/project/userAccount")
    public ServiceResponse<Map<String,Object>> getUserAccount(@RequestBody ServiceRequest<DReSetPassword> request)throws Exception{
        DReSetPassword dReSetPassword = request.getRequest();
        TUserInfo tUserInfo = userInfoService.getByAccount(request.getRequestHeader().getCompanyId(),dReSetPassword.getAccount());
        Integer userId = null;
        List<Map<String,Object>> list = new ArrayList<>();
        if (tUserInfo != null){
             list = userFieldService.getField(request);
             userId= tUserInfo.getId();
        }

        Map<String,Object> stringObjectMap = new HashMap<String,Object>();
        stringObjectMap.put("fields",list);
        stringObjectMap.put("userId",userId);
        ServiceResponse<Map<String,Object>> response = new ServiceResponse<Map<String,Object>>();
        response.setResponse(stringObjectMap);
        return response;
    }
}
