package com.talebase.cloud.os.examer.controller;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.project.dto.DUseStatics;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.*;
import com.talebase.cloud.os.examer.service.ExamProjectService;
import com.talebase.cloud.os.examer.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by daorong.li on 2016-12-8.
 */
@RestController
public class ExamProjectController {

    @Autowired
    private ExamProjectService examProjectService;
    @Autowired
    private ExamService examService;

    @GetMapping(value = "/examers/project")
    public ServiceResponse<PageResponseWithParam> getProjectExamers(DUserExamPageRequest dUserExamPageRequest,PageRequest pageReq){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();

        dUserExamPageRequest.setCompanyId(serviceHeader.getCompanyId());
        //设置请求头数据
        ServiceRequest<DUserExamPageRequest> req = new ServiceRequest<DUserExamPageRequest>();
        req.setRequest(dUserExamPageRequest);
        req.setPageReq(pageReq);
        req.setRequestHeader(serviceHeader);

        PageResponse pageResponse = examProjectService.getProjectExamers(req);
        DUseStatics dUseStatics = examProjectService.getProjectAndTaskExportName(dUserExamPageRequest.getProjectId(),dUserExamPageRequest.getTaskId());
        dUserExamPageRequest.setProjectName(dUseStatics.getProjectName());
        dUserExamPageRequest.setTaskName(dUseStatics.getTaskName());
        //组装返回数据
        PageResponseWithParam<PageResponse,DUserExamPageRequest> param = new PageResponseWithParam<PageResponse,DUserExamPageRequest>(pageReq,dUserExamPageRequest);
        param.setResults(pageResponse.getResults());
        param.setTotal(pageResponse.getTotal());
        //返回数据
        ServiceResponse<PageResponseWithParam> page = new ServiceResponse<PageResponseWithParam>();
        page.setResponse(param);

        return page;
    }

    /**
     * 获取选择项目与产品下拉框
     *
     * @return
     */
/*    @GetMapping(value = "/examer/projectAndTask")
    public ServiceResponse<List<DProjectSelect>> getProjectAndTask(){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        ServiceRequest request = new ServiceRequest();
        request.setRequestHeader(serviceHeader);
        return  examProjectService.getProjectAndTask(request);
    }*/

    /**
     * 保存账号设定
     * @param dUserShowField
     * @return
     */
    @PostMapping(value = "/examer/project/saveAll")
    public ServiceResponse saveProjectAll(DUserShowField dUserShowField){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dUserShowField.setCompanyId(serviceHeader.getCompanyId());
        ServiceRequest<DUserShowField> request = new ServiceRequest<DUserShowField>();
        request.setRequest(dUserShowField);
        request.setRequestHeader(serviceHeader);
        return examProjectService.saveAll(request);
    }

    /**
     * 获取项目下显示的字段
     * @return
     */
    @GetMapping(value = "/examer/project/fields")
    public ServiceResponse<DUserShowFieldResponseList> getProjectExamersFields(DProjectTaskReq req){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        ServiceRequest<DProjectTaskReq> request = new ServiceRequest<DProjectTaskReq>();
        request.setRequest(req);
        request.setRequestHeader(serviceHeader);
        return examProjectService.getProjectExamersFields(serviceHeader.getCompanyId(),request);
    }

    /**
     * 更新选择账号密码
     * @param dReSetPassword
     * @return
     */
    @PostMapping(value = "/examer/project/reSetPassword")
    public ServiceResponse reSetPassword(DReSetPassword dReSetPassword){
        checkPassword(dReSetPassword);
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dReSetPassword.setCompanyId(serviceHeader.getCompanyId());
        ServiceRequest<DReSetPassword> request = new ServiceRequest<DReSetPassword>();
        request.setRequest(dReSetPassword);
        request.setRequestHeader(serviceHeader);
        return examProjectService.reSetPassword(request);
    }

    private void checkPassword(DReSetPassword dReSetPassword){
        if(!dReSetPassword.isRandom()){
            if (!StringUtil.isEmpty(dReSetPassword.getNewPassword())) {
                if (dReSetPassword.getNewPassword().length() < 6) {
                    throw new WrappedException(BizEnums.PasswordTooShort);
                }else if(!PasswordUtil.containLetterAndNumber(dReSetPassword.getNewPassword())){
                    throw new WrappedException(BizEnums.PasswordNotVail);
                }else if(PasswordUtil.containChinese(dReSetPassword.getNewPassword())){
                    throw new WrappedException(BizEnums.PasswordCannotHasChinese);
                }
            } else {//不设置随机则必须密码不为空
                throw new WrappedException(BizEnums.PasswordCannotBeBlank);
            }
        }
    }

    /**
     * 删除选择账号
     * @param dReSetPassword
     * @return
     */
    //@DeleteMapping(value = "/examer/project/del")
    @PostMapping(value = "/examer/project/del")
    public ServiceResponse del(DReSetPassword dReSetPassword){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dReSetPassword.setCompanyId(serviceHeader.getCompanyId());
        ServiceRequest<DReSetPassword> request = new ServiceRequest<DReSetPassword>();
        request.setRequest(dReSetPassword);
        request.setRequestHeader(serviceHeader);
        return examProjectService.del(request);
    }

    /**
     * 导出全部账号
     * @param dUserExamPageRequest
     */
    @GetMapping(value = "/examer/project/export/all")
    public void exportExcel(DUserExamPageRequest dUserExamPageRequest,
                            HttpServletResponse servletResponse,HttpServletRequest req) throws Exception{

        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dUserExamPageRequest.setCompanyId(serviceHeader.getCompanyId());
        ServiceRequest<DUserExamPageRequest> request = new ServiceRequest<DUserExamPageRequest>();
        request.setRequest(dUserExamPageRequest);
        request.setRequestHeader(serviceHeader);
        //获取导出字段列表
        DExportExamers dExportExamers = examProjectService.getExamUserInfos(request);
        DUseStatics dUseStatics = examProjectService.getProjectAndTaskExportName(dUserExamPageRequest.getProjectId(),dUserExamPageRequest.getTaskId());
        String exportName = dUseStatics.getProjectName() + "-" + dUseStatics.getTaskName() +"-帐号导出表-"+TimeUtil.formatDateForFileMinute(new Date())+".xls";

        List<String> headerStrs = dExportExamers.getHeaders();
        List<List<String>> data = dExportExamers.getData();

        Map map = new HashMap<>();
        map.put("headers", headerStrs);
        map.put("data", data);

        InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/xls/grid_template.xls"));
        JxlsUtil.write(req,inputXML, servletResponse, map, exportName, "sheet2!A1");
    }

    /**
     * 获取新增字段列表
     * @param dUserExamPageRequest
     * @return
     */
    @GetMapping(value = "/examer/project/newField")
    public ServiceResponse<Map<String,Object>> getFieldByNew(DReSetPassword dUserExamPageRequest){
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dUserExamPageRequest.setCompanyId(serviceHeader.getCompanyId());
        ServiceRequest<DReSetPassword> request = new ServiceRequest<DReSetPassword>();
        request.setRequest(dUserExamPageRequest);
        request.setRequestHeader(serviceHeader);
        return examProjectService.getFieldByNew(request);
    }

    /**
     * 获取修改账号
     * @param dUserExamPageRequest
     * @return
     */
    @GetMapping(value = "/examer/project/modifyField")
    public ServiceResponse<Map<String,Object>> getFieldByModify(DReSetPassword dUserExamPageRequest) {
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dUserExamPageRequest.setCompanyId(serviceHeader.getCompanyId());
        ServiceRequest<DReSetPassword> request = new ServiceRequest<DReSetPassword>();
        request.setRequest(dUserExamPageRequest);
        request.setRequestHeader(serviceHeader);
        return examProjectService.getFieldByNew(request);
    }

    /**
     * 获取账号信息(创建时获取存在账号信息用于合并)
     * @param dUserExamPageRequest
     * @return
     */
    @GetMapping(value = "/examer/project/userAccount")
    public ServiceResponse<Map<String,Object>> getUserAccount(DReSetPassword dUserExamPageRequest){
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        dUserExamPageRequest.setCompanyId(serviceHeader.getCompanyId());
        ServiceRequest<DReSetPassword> request = new ServiceRequest<DReSetPassword>();
        request.setRequest(dUserExamPageRequest);
        request.setRequestHeader(serviceHeader);
        return examProjectService.getUserAccount(request);
    }

    /**
     * 创建多个账号
     * @param createExamersReq
     * @return
     */
    @PostMapping(value = "/examers")
    public ServiceResponse<DCreateExamersNumResp> createExamers(DCreateExamersReq createExamersReq){

        if(StringUtil.isEmpty(createExamersReq.getAccountPre()))
            createExamersReq.setAccountPre("test");//前缀为空则默认填test前缀

        if(NumberUtil.isEmpty(createExamersReq.getAmount()) && (NumberUtil.isEmpty(createExamersReq.getStartNum()) || NumberUtil.isEmpty(createExamersReq.getEndNum()))){//三个都没值
            throw new WrappedException(BizEnums.CreateNoAmount);
        }

        if(!NumberUtil.isEmpty(createExamersReq.getStartNum()) && !NumberUtil.isEmpty(createExamersReq.getEndNum())){
            if(createExamersReq.getEndNum() < createExamersReq.getStartNum())
                throw new WrappedException(BizEnums.CreateNoAmount);
        }

        if(!StringUtil.isEmpty(createExamersReq.getPassword())){
            if (createExamersReq.getPassword().length() < 6) {
                throw new WrappedException(BizEnums.PasswordTooShort);
            }else if(!PasswordUtil.containLetterAndNumber(createExamersReq.getPassword())){
                throw new WrappedException(BizEnums.PasswordNotVail);
            }else if(PasswordUtil.containChinese(createExamersReq.getPassword())){
                throw new WrappedException(BizEnums.PasswordCannotHasChinese);
            }
        }

//        createExamersReq.setSuffix("_sys");//给我系统定义的默认后缀

        DCreateExamersResp resp = examService.createExamers(createExamersReq);
//        if(resp.getSuccAccs() != null && resp.getSuccAccs().size() > 0 && createExamersReq.getProjectId() != null && createExamersReq.getTaskId() != null){
//            //调用项目那边的接口，添加冗余表数据
//            examProjectService.joinExamersToTask(resp.getSuccAccs(), createExamersReq.getProjectId(), createExamersReq.getTaskId(), ServiceHeaderUtil.getRequestHeader());
//        }

        DCreateExamersNumResp numResp = new DCreateExamersNumResp(resp);
        return new ServiceResponse(numResp);
    }

    /**
     * 创建单个账号
     * @param jsonStr
     * @param projectId
     * @param taskId
     * @return
     */
    @PostMapping(value = "/examer")
    public ServiceResponse createExamer(String jsonStr, Integer projectId, Integer taskId){
        Map<String, String> map = GsonUtil.fromJson(jsonStr, new TypeToken<HashMap>(){}.getType());

        if(!map.containsKey("account"))
            throw new WrappedException(BizEnums.NoAccount);

        ServiceResponse resp = examProjectService.createUser(map, projectId, taskId);
        return new ServiceResponse<>();
    }

    @PutMapping(value = "/examer/{userId}")
    public ServiceResponse modifyExamer(String jsonStr, Integer projectId, Integer taskId, @PathVariable("userId") Integer userId){
        Map<String, String> map = GsonUtil.fromJson(jsonStr, new TypeToken<HashMap>(){}.getType());
        if(!map.containsKey("account"))
            throw new WrappedException(BizEnums.NoAccount);

        ServiceResponse<String> resp = examProjectService.modifyUser(userId, map, projectId, taskId);
        if(!StringUtil.isEmpty(resp.getResponse())){
            if(projectId != null && taskId != null){
                //调用项目那边的接口，添加冗余表数据
                examProjectService.joinExamersToTask(Arrays.asList(resp.getResponse()), projectId, taskId, ServiceHeaderUtil.getRequestHeader());
            }
        }

        return new ServiceResponse<>();
    }

}
