package com.talebase.cloud.os.examer.controller;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.login.dto.DLoginRequest;
import com.talebase.cloud.base.ms.login.dto.DLoginUserInfo;
import com.talebase.cloud.base.ms.project.dto.DUseStatics;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.examer.service.ExamService;
import com.talebase.cloud.os.examer.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daorong.li on 2016-12-6.
 */
@RestController
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 获取全局账号设置
     * @return
     */
    @GetMapping(value = "/examers")
    public ServiceResponse<DUserShowFieldResponseList> getExams(){
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        return examService.getGlobalExamers(serviceHeader.getCompanyId());
    }

    /**
     * 添加全局可选信息
     * @param dUserShowField
     * @return
     */
    @PostMapping(value = "/examer/add")
    public ServiceResponse<String>  addExamByOptional(DUserShowField dUserShowField){
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dUserShowField.setCompanyId(serviceHeader.getCompanyId());
        dUserShowField.setCreater(serviceHeader.getOperatorName());
        ServiceRequest<DUserShowField> request = new ServiceRequest<DUserShowField>();
        request.setRequest(dUserShowField);
        request.setRequestHeader(serviceHeader);
        return examService.addExamByOptional(request);
    }

    /**
     * 删除全局账号
     * @param dUserShowField
     * @return
     */
    @DeleteMapping(value = "/examer/del")
    public ServiceResponse del(DUserShowField dUserShowField){
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dUserShowField.setCompanyId(serviceHeader.getCompanyId());
        ServiceRequest<DUserShowField> request = new ServiceRequest<DUserShowField>();
        request.setRequest(dUserShowField);
        request.setRequestHeader(serviceHeader);
        return examService.del(request);
    }

    @PostMapping(value = "/examer/saveGlobalAll")
    public ServiceResponse saveGlobalAll(DUserShowField dUserShowField){
        //获取操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dUserShowField.setCompanyId(serviceHeader.getCompanyId());
        dUserShowField.setModifier(serviceHeader.getOperatorName());
        ServiceRequest<DUserShowField> request = new ServiceRequest<DUserShowField>();
        request.setRequest(dUserShowField);
        request.setRequestHeader(serviceHeader);
        return  examService.saveGlobalAll(request);
    }

    /**
     * 修改考生资料
     * @param jsonStr
     * @return
     */
    @PostMapping(value = "/examer/modifyUserForPerfect")
    public ServiceResponse modifyUserForPerfect(String jsonStr){
        DEditExamerReq dEditExamerReq = GsonUtil.fromJson(jsonStr,DEditExamerReq.class);
        ServiceRequest<DEditExamerReq> request = new ServiceRequest<DEditExamerReq>();
        request.setRequest(dEditExamerReq);
        request.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        Map<String, String> map = GsonUtil.fromJson(jsonStr, new TypeToken<HashMap>(){}.getType());
        dEditExamerReq.setMap(map);
        if(map.get("account") == null){
            map.put("account",map.get("mobile"));
        }
        return  userInfoService.modifyUserForPerfect(request);
    }

    /**
     * 考生修改密码
     * @param dUpdatePdassword
     */
    @PostMapping(value="/examer/updateUserPassword")
    public ServiceResponse<String> updateUserPassword(DUpdatePassword dUpdatePdassword) throws Exception {
        ServiceResponse<String> response = userInfoService.updateUserPassword(dUpdatePdassword);
        return response;
    }
}
