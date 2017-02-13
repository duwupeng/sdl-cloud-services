package com.talebase.cloud.os.login.service;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.enums.TAdminStatus;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.dto.DEditExamerReq;
import com.talebase.cloud.base.ms.examer.dto.DUserRequest;
import com.talebase.cloud.base.ms.examer.enums.TUserInfoStatus;
import com.talebase.cloud.base.ms.login.dto.DLoginRequest;
import com.talebase.cloud.base.ms.login.dto.DLoginUserInfo;
import com.talebase.cloud.base.ms.login.enumes.LoginTypeEnume;
import com.talebase.cloud.base.ms.project.dto.DTaskInEdit;
import com.talebase.cloud.base.ms.project.dto.DTasksInEdit;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.CallerFrom;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.Des3Util;
import com.talebase.cloud.common.util.PasswordUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.login.bind.CodeRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangchunlin on 2016-12-12.
 */
@Service
public class LoginService {
    @Autowired
    MsInvoker msInvoker;

    @Autowired
    RedisTemplate redisTemplate;

    final static String ADMIN_SERVICE_NAME = "ms-admin";
    final static String EXAM_SERVICE_NAME = "ms-examer";
    final static String PROJECT_SERVICE_NAME = "ms-project";
    final static String CONSUMPTION_SERVICE_NAME = "ms-consumption";

    /**
     * 校验账号密码
     * @param req
     * @return ServiceResponse<String>
     */
    public ServiceResponse<DLoginUserInfo> checkAccountAndPassword(ServiceRequest<DLoginRequest> req) throws Exception {
        ServiceResponse<DLoginUserInfo> response = new ServiceResponse<DLoginUserInfo>();
        if(req.getRequest().getLoginType().intValue() == LoginTypeEnume.ADMIN.getValue()){
            String servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/getByAccountAndCompanyId";
            String tservicePath = "http://" + CONSUMPTION_SERVICE_NAME + "/consume/getTips/"+req.getRequest().getCompanyId();
            ServiceRequest<DAdmin> areq = new ServiceRequest<DAdmin>();
            DAdmin dAdmin = new DAdmin();
            dAdmin.setCompanyId(req.getRequest().getCompanyId());
            dAdmin.setAccount(req.getRequest().getAccount());
            areq.setRequest(dAdmin);
            ServiceResponse<TAdmin> aresponse = msInvoker.post(servicePath,areq,new ParameterizedTypeReference<ServiceResponse<TAdmin>>(){});
            if(aresponse == null || aresponse.getResponse() == null || aresponse.getResponse().getStatus() != TAdminStatus.EFFECTIVE.getValue()){
                throw new WrappedException(BizEnums.AdminLoginErr);
            }
            ServiceResponse<String> treps = msInvoker.get(tservicePath,new ParameterizedTypeReference<ServiceResponse<String>>(){});
            if(aresponse.getResponse() == null || !Des3Util.des3EncodeCBC(req.getRequest().getPassword()).equals(aresponse.getResponse().getPassword())){
                throw new WrappedException(BizEnums.AdminLoginErr);
            }
            DLoginUserInfo dLoginUserInfo = new DLoginUserInfo();
            dLoginUserInfo.setId(aresponse.getResponse().getId());
            dLoginUserInfo.setAccount(aresponse.getResponse().getAccount());
            dLoginUserInfo.setCompanyId(aresponse.getResponse().getCompanyId());
            dLoginUserInfo.setName(aresponse.getResponse().getName());
            dLoginUserInfo.setOrgCode(aresponse.getResponse().getOrgCode());
            dLoginUserInfo.setTips(treps.getResponse());
            response.setResponse(dLoginUserInfo);
        }else{
            String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/getUserInfo/";
            DUserRequest dUserRequest = new DUserRequest();
            dUserRequest.setCompanyId(req.getRequest().getCompanyId());
            dUserRequest.setAccount(req.getRequest().getAccount());
            ServiceResponse<TUserInfo> uresponse = msInvoker.post(servicePath,dUserRequest,new ParameterizedTypeReference<ServiceResponse<TUserInfo>>(){});
            if(uresponse == null || uresponse.getResponse() == null || uresponse.getResponse().getStatus() != TUserInfoStatus.ENABLED.getValue()){
                throw new WrappedException(BizEnums.UserLoginDisable);
            }
            if(uresponse.getResponse() == null || !Des3Util.des3EncodeCBC(req.getRequest().getPassword()).equals(uresponse.getResponse().getPassword())){
                throw new WrappedException(BizEnums.UserLoginErr);
            }
            DLoginUserInfo dLoginUserInfo = new DLoginUserInfo();
            dLoginUserInfo.setId(uresponse.getResponse().getId());
            dLoginUserInfo.setAccount(uresponse.getResponse().getAccount());
            dLoginUserInfo.setCompanyId(uresponse.getResponse().getCompanyId());
            dLoginUserInfo.setName(uresponse.getResponse().getName());
            response.setResponse(dLoginUserInfo);
        }
        return response;
    }

    /**
     * 扫码时校验二维码是否可用,如果不抛异常就是正常
     * @param projectId
     */
    public void checkScanCode(Integer projectId,String mobile) throws InvocationTargetException, IllegalAccessException {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/scanEnable/"+projectId+"/"+mobile;
        msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<String>>(){});
        servicePath = "http://" + CONSUMPTION_SERVICE_NAME + "/consume/queryAccount/"+ServiceHeaderUtil.getRequestHeader().getCompanyId();
        ServiceResponse<TAccount> response = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<TAccount>>(){});
        if(response == null || response.getResponse() == null || response.getResponse().getPointBalance() - (response.getResponse().getPeraccountValid() == null ? 0 : response.getResponse().getPeraccountValid()) < 0){
            throw new WrappedException(BizEnums.ScanDisable);
        }
    }

    /**
     * 校验短信验证码
     * @param dLoginRequest
     * @return ServiceResponse<String>
     */
    public ServiceResponse<DLoginUserInfo> loginCheckForScanCode(DLoginRequest dLoginRequest) throws InvocationTargetException, IllegalAccessException {
        ServiceResponse<DLoginUserInfo> response = new ServiceResponse<DLoginUserInfo>();
        List<CodeRule> codeList = (List<CodeRule>)redisTemplate.opsForValue().get(dLoginRequest.getMobile());
        if(dLoginRequest.getVerificationCode() != null && !dLoginRequest.getVerificationCode().trim().equals("") && dLoginRequest.getMobile() != null
                && codeList != null && codeList.size() > 0
                && dLoginRequest.getVerificationCode().equals(codeList.get(codeList.size() - 1).getVerifyCode())){
            response.setMessage("校验通过");

            String token = UUID.randomUUID().toString();
            ServiceHeader serviceHeader = new ServiceHeader();
            serviceHeader.setOrgCode("code");
            serviceHeader.setToken(token);
            serviceHeader.setSeqId(0);
            serviceHeader.setCallerIP(dLoginRequest.getCallerIP());
            serviceHeader.setCallerFrom(CallerFrom.findByValue(dLoginRequest.getCallerFrom()));
            serviceHeader.setCompanyId(ServiceHeaderUtil.getRequestHeader().getCompanyId());
            //判断是否可以扫码登录，如果不抛异常就是正常
            checkScanCode(dLoginRequest.getProjectId(),dLoginRequest.getMobile());
            //查找用户是否存在
            String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/getUserInfo/";
            DUserRequest dUserRequest = new DUserRequest();
            dUserRequest.setMobile(dLoginRequest.getMobile());
            ServiceResponse<TUserInfo> uresponse = msInvoker.post(servicePath,dUserRequest,new ParameterizedTypeReference<ServiceResponse<TUserInfo>>(){});
            DLoginUserInfo dLoginUserInfo = new DLoginUserInfo();
            dLoginUserInfo.setToken(token);
            Integer userId = 0;
            if(uresponse != null && uresponse.getResponse() != null){
                serviceHeader.setCustomerId(uresponse.getResponse().getId());
                serviceHeader.setCustomerName(uresponse.getResponse().getName());

                dLoginUserInfo.setId(uresponse.getResponse().getId());
                dLoginUserInfo.setAccount(uresponse.getResponse().getAccount());
                dLoginUserInfo.setCompanyId(uresponse.getResponse().getCompanyId());
                dLoginUserInfo.setName(uresponse.getResponse().getName());
                userId = uresponse.getResponse().getId();
            }else {
                ServiceRequest<DEditExamerReq> serviceRequest = new ServiceRequest<DEditExamerReq>();
                DEditExamerReq dEditExamerReq = new DEditExamerReq();
                Map<String, String> map = new HashMap<>();
                map.put("account",dLoginRequest.getMobile());
                map.put("mobile",dLoginRequest.getMobile());
                map.put("creater",dLoginRequest.getMobile());
                map.put("companyId",ServiceHeaderUtil.getRequestHeader().getCompanyId().toString());
                dEditExamerReq.setMap(map);
                serviceRequest.setRequest(dEditExamerReq);
                dEditExamerReq.setProjectId(dLoginRequest.getProjectId());

                servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/createUserForPerfect";
                ServiceResponse<Integer> response1 = msInvoker.post(servicePath,serviceRequest,new ParameterizedTypeReference<ServiceResponse<Integer>>(){});

                serviceHeader.setCustomerName(dLoginRequest.getMobile());
                serviceHeader.setCustomerId(response1.getResponse());

                dLoginUserInfo.setId(response1.getResponse());
                dLoginUserInfo.setAccount(dLoginRequest.getMobile());
                dLoginUserInfo.setCompanyId(ServiceHeaderUtil.getRequestHeader().getCompanyId());
                dLoginUserInfo.setName(dLoginRequest.getMobile());
                userId = response1.getResponse();
            }
            servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/getCntForExamAndUserByMobileAndProjectId/" + dLoginRequest.getMobile() + "/" + dLoginRequest.getProjectId();
            ServiceResponse<Integer> userAndProjectCountResponse = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<Integer>>(){});
            if(userAndProjectCountResponse != null && userAndProjectCountResponse.getResponse() != null && userAndProjectCountResponse.getResponse() == 0){
                //计算项目下面的任务
                servicePath = "http://" + PROJECT_SERVICE_NAME + "/project/tasksForNoVail/" + dLoginRequest.getProjectId();
                ServiceRequest taskRequest = new ServiceRequest();
                ServiceHeader requestHeader = new ServiceHeader();
                requestHeader.setCompanyId(ServiceHeaderUtil.getRequestHeader().getCompanyId());
                requestHeader.setOrgCode("");
                requestHeader.setOperatorName("");
                taskRequest.setRequestHeader(requestHeader);
                ServiceResponse<DTasksInEdit> taskResponse = msInvoker.post(servicePath,taskRequest,new ParameterizedTypeReference<ServiceResponse<DTasksInEdit>>(){});
                List<Integer> taskList = new ArrayList<>();
                DEditExamerReq dEditExamerReq = new DEditExamerReq();
                if(taskResponse != null && taskResponse.getResponse() != null && taskResponse.getResponse().getTasks() != null && taskResponse.getResponse().getTasks().size() > 0){
                    List<DTaskInEdit> dTaskInEdits = taskResponse.getResponse().getTasks();
                    for(DTaskInEdit dTaskInEdit:dTaskInEdits){
                        taskList.add(dTaskInEdit.getId());
                    }
                    dEditExamerReq.setTaskIds(taskList);
                }
                ServiceRequest<DEditExamerReq> serviceRequest = new ServiceRequest<DEditExamerReq>();
                Map<String, String> map = new HashMap<>();
                map.put("account",dLoginRequest.getMobile());
                map.put("mobile",dLoginRequest.getMobile());
                map.put("creater",dLoginRequest.getMobile());
                map.put("userId",userId.toString());
                map.put("companyId",ServiceHeaderUtil.getRequestHeader().getCompanyId().toString());
                dEditExamerReq.setMap(map);
                serviceRequest.setRequest(dEditExamerReq);
                dEditExamerReq.setProjectId(dLoginRequest.getProjectId());
                servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/saveUserExam";
                ServiceResponse<Integer> response1 = msInvoker.post(servicePath,serviceRequest,new ParameterizedTypeReference<ServiceResponse<Integer>>(){});
            }
            serviceHeader.setAccount(dLoginUserInfo.getAccount());
            //get the permissions from admindb
            /*servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/permissions/operator/"+serviceHeader.getCustomerId();
            ServiceResponse<List<String>> presponse = msInvoker.get(servicePath,new ParameterizedTypeReference<ServiceResponse<List<String>>>(){},new Object());
            serviceHeader.setPermissions(presponse.getResponse());*/

            //save it to redis
            String tokenKey = "token_" + token;
            redisTemplate.opsForValue().set(tokenKey,serviceHeader);
            redisTemplate.expire(tokenKey,120, TimeUnit.MINUTES);

            response.setResponse(dLoginUserInfo);
        }else{
            response.setMessage("校验不通过");
            response.setBizError(true);
            response.setCode(-1);
        }
        return response;
    }

    public List<String> findPermissionsByOperatorId(Integer operatorId){
        String servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/permissions/admin/{operatorId}";
        ServiceResponse<List<String>> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<String>>>(){}, operatorId);
        return response.getResponse();
    }

}
