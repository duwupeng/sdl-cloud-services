package com.talebase.cloud.os.examer.service;

import com.talebase.cloud.base.ms.common.domain.TCode;
import com.talebase.cloud.base.ms.common.enumes.TypeEnume;
import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.base.ms.project.domain.TTask;
import com.talebase.cloud.base.ms.project.dto.DProjectSelect;
import com.talebase.cloud.base.ms.project.dto.DTaskInScore;
import com.talebase.cloud.base.ms.project.dto.DTaskMarked;
import com.talebase.cloud.base.ms.project.dto.DUseStatics;
import com.talebase.cloud.base.ms.project.enums.TTaskStatus;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.DateUtil;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.examer.config.conf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by daorong.li on 2016-12-8.
 */
@Service
public class ExamProjectService {
    @Autowired
    MsInvoker msInvoker;

    final static String EXAM_SERVICE_NAME = "ms-examer";
    final static String PROJECT_SERVICE_NAME = "ms-project";
    final static String COMMON_SERVICE_NAME = "ms-common";

    private static Logger log = LoggerFactory.getLogger(ExamProjectService.class);

    /**
     * 获取项目测试账号列表
     *
     * @param req
     * @return
     */
    public PageResponse getProjectExamers(ServiceRequest<DUserExamPageRequest> req) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project";
        ServiceResponse<PageResponse> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<PageResponse>>() {
        });

        PageResponse  pageResponse = response.getResponse();
        List<Map<String,Object>> resultList = pageResponse.getResults();
        boolean isShowName = false;
        if (resultList != null){
            for (int i=0;i<resultList.size();i++) {
                List<Map<String, Object>> userInfos = (List<Map<String, Object>>) resultList.get(i).get("userInfos");
                //判断是否存在name值
                if (userInfos != null && i ==0) {
                    for (int k = 0; k < userInfos.size(); k++) {
                        Map<String, Object> map = userInfos.get(k);
                        if (map.get("fieldKey").equals("name")) {
                            isShowName = true;
                            break;
                        }
                    }
                }
                //存在则跳出循环
                if (isShowName){
                    break;
                } else{//不存在则默认添加明称，为了前端不勾选name值时默认显示账号值
                    String account ="";
                    for (int k = 0; k < userInfos.size(); k++) {
                        Map<String, Object> map = userInfos.get(k);
                        if (map.get("fieldKey").equals("account")) {
                            account = (String) map.get("fieldValue");
                            break;
                        }
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("fieldKey","name");
                    map.put("fieldValue",account);
                    map.put("isexTension",0);
                    map.put("fieldName","姓名");
                    map.put("type",1);
                    userInfos.add(map);
                }

            }
        }
        return response.getResponse();
    }

    /**
     * 选择项目与产品下拉框
     *
     * @return
     */
/*    public ServiceResponse<List<DProjectSelect>> getProjectAndTask(ServiceRequest req) {
        String servicePath = "http://" + SERVICE_NAME + "/project/select";
        ServiceResponse<List<DProjectSelect>> response = msInvoker.post(servicePath,req,new ParameterizedTypeReference<ServiceResponse<List<DProjectSelect>>>(){});
        return  response;
    }*/
    public ServiceResponse saveAll(ServiceRequest<DUserShowField> request) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/exam/saveGlobalAll";
        ServiceResponse<List<DProjectSelect>> response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<List<DProjectSelect>>>() {
        });
        return response;
    }

    /**
     * 获取项目字段显示列表
     *
     * @param companyId
     * @return
     */
    public ServiceResponse<DUserShowFieldResponseList> getProjectExamersFields(Integer companyId, ServiceRequest<DProjectTaskReq> req) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project/fields/" + companyId;
        ServiceResponse<DUserShowFieldResponseList> response =
                msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<DUserShowFieldResponseList>>() {
                });
        return response;
    }

    public ServiceResponse reSetPassword(ServiceRequest<DReSetPassword> request) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project/reSetPassword/select";
        ServiceResponse response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        return response;
    }

/*    public ServiceResponse reSetAllPassword(ServiceRequest<DReSetPassword> request){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project/reSetPassword/all";
        ServiceResponse  response = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse>(){});
        return  response;
    }*/

    public ServiceResponse del(ServiceRequest<DReSetPassword> request) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project/del";
        ServiceResponse response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        return response;
    }

/*    public ServiceResponse delAll(ServiceRequest<DReSetPassword> request){
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project/delAll";
        ServiceResponse  response = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse>(){});
        return  response;
    }*/

    public ServiceResponse joinExamersToTask(List<String> accounts, Integer projectId, Integer taskId, ServiceHeader serviceHeader) {
//        String servicePath = "http://" + SERVICE_NAME + "/exam/saveGlobalAll";
//        ServiceResponse<List<DProjectSelect>> response = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse<List<DProjectSelect>>>(){});
//        return  response;
        //// TODO: 2016-12-12
        return null;
    }

    public DExportExamers getExamUserInfos(ServiceRequest<DUserExamPageRequest> request) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project/export/all";
        ServiceResponse<DExportExamers> response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<DExportExamers>>() {
        });
        return response.getResponse();
    }


    public ServiceResponse<Map<String, Object>> getFieldByNew(ServiceRequest<DReSetPassword> request) {
/*        ServiceRequest<DReSetPassword> request = new ServiceRequest<DReSetPassword>();
        request.setRequest(dReSetPassword);*/

        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project/newField";
        ServiceResponse<List<Map<String, Object>>> response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<List<Map<String, Object>>>>() {
        });
        List<Map<String, Object>> list = response.getResponse();
        for (Map<String, Object> m : list) {
            //行业数据需要从commonDb获取
            if (conf.Outreach_Field.equals(m.get("fieldKey"))) {
                String commonPath = "http://" + COMMON_SERVICE_NAME + "/common/type?type=" + TypeEnume.INDUSTRY.getType();
                ServiceResponse<List<TCode>> tCodeList = msInvoker.get(commonPath, new ParameterizedTypeReference<ServiceResponse<List<TCode>>>() {
                });
                List<TCode> list1 = tCodeList.getResponse();
                List<Map<String, String>> select = new ArrayList<Map<String, String>>();
                for (TCode t : list1) {
                    Map<String, String> valueMap = new HashMap<String, String>();
                    valueMap.put("name", t.getName());
                    select.add(valueMap);
                }
                m.put("select", select);
            }
        }

        Map<String, Object> reMap = new HashMap<String, Object>();
        reMap.put("fields", list);
        if (!StringUtils.isEmpty(request.getRequest().getUserId())) {
            reMap.put("fields", list);
        }

        ServiceResponse<Map<String, Object>> response1 = new ServiceResponse<Map<String, Object>>();
        response1.setResponse(reMap);
        return response1;
    }

    public ServiceResponse<Map<String, Object>> getUserAccount(ServiceRequest<DReSetPassword> request) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/project/userAccount";
        ServiceResponse<Map<String, Object>> response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<Map<String, Object>>>() {
        });
        return response;
    }

    public ServiceResponse createUser(Map map, Integer projectId, Integer taskId) {
        DEditExamerReq req = new DEditExamerReq(projectId, taskId, map);
        ServiceRequest<DEditExamerReq> request = new ServiceRequest();
        request.setRequest(req);
        request.setRequestHeader(ServiceHeaderUtil.getRequestHeader());
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer";
        ServiceResponse<String> response = msInvoker.post(servicePath, request, new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        return response;
    }

    public ServiceResponse modifyUser(Integer userId, Map map, Integer projectId, Integer taskId) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/" + userId;
        ServiceResponse<String> response = msInvoker.put(servicePath, new ServiceRequest(ServiceHeaderUtil.getRequestHeader(), new DEditExamerReq(projectId, taskId, map)), new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        return response;
    }

    /**
     * 根据考官查询评卷情况
     *
     * @param operatorName
     * @return
     */
    public DTaskMarked getTaskMarked(String operatorName, Integer companyId) {
        String servicePath = "http://" + PROJECT_SERVICE_NAME + "/tasks/mark/{companyId}/{operatorName}";
        ServiceResponse<DTaskMarked> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DTaskMarked>>() {
        }, companyId, operatorName);
        return response.getResponse();
    }

    /**
     * 根据考官获取任务分页列表
     *
     * @param operatorName
     * @param pageRequest
     * @return
     */
    public PageResponse<DTaskInScore> queryTasksInScore(String operatorName, Integer companyId, PageRequest pageRequest) {
        String servicePath = "http://" + PROJECT_SERVICE_NAME + "/tasks/examiner/{companyId}/{operatorName}?pageIndex={pageIndex}&limit={limit}";
        ServiceResponse<PageResponse<DTaskInScore>> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<PageResponse<DTaskInScore>>>() {
        }, companyId, operatorName, pageRequest.getPageIndex(), pageRequest.getLimit());
//        ServiceResponse<PageResponse<DTaskInScore>>  response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<PageResponse<DTaskInScore>>>(){}, operatorName, pageRequest.getPageIndex(), pageRequest.getLimit());
        return response.getResponse();
    }

    /**
     * 根据考官获取任务名称(并校验是否拥有数据权限)
     *
     * @return
     */
    public TTask checkTask(Integer taskId, ServiceHeader header) {
        String servicePath = "http://" + PROJECT_SERVICE_NAME + "/task/check/examiner";
        ServiceResponse<TTask> response = msInvoker.post(servicePath, new ServiceRequest<Integer>(header, taskId), new ParameterizedTypeReference<ServiceResponse<TTask>>() {
        });
        return response.getResponse();
    }

    /**
     * 获取导出名字
     *
     * @param projectId
     * @param taskId
     * @return
     */
    public DUseStatics getProjectAndTaskExportName(Integer projectId, Integer taskId) {
        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/projectAndTaskExportName/" + projectId + "/" + taskId;
        ServiceResponse<DUseStatics> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DUseStatics>>() {
        });
        return response.getResponse();
    }

    /**
     * 项目、任务 考试状态、时间检查
     */
    public TTask taskActiveCheck(Integer taskId) {
        TTask task = null;
        if (task == null || task.getStatus() != TTaskStatus.ENABLE.getValue()) {
            throw new WrappedException(BizEnums.TaskIsStop);
        }

        Date now = new Date();
        if (task.getStartDate() != null && !task.getStartDate().before(now)) {
            throw new WrappedException(BizEnums.TaskNotStart);
        }
        if (task.getEndDate() != null && task.getEndDate().before(now)) {
            throw new WrappedException(BizEnums.TaskIsEnd);
        }
        if (task.getLatestStartDate() != null && task.getLatestStartDate().before(now)) {
            throw new WrappedException(BizEnums.TaskTooLate);
        }

        return task;
    }

    public TUserExam getUserExam(Integer taskId, Integer customerId) {
        TUserExam userExam = null;
        if (userExam == null || userExam.getStatus() != TUserExamStatus.ENABLED.getValue()) {
            throw new WrappedException(BizEnums.NoUserExam);
        }
        return userExam;
    }

    public DUserExamPermission getUserExamPermission(Integer taskId) {

        String servicePath = "http://" + EXAM_SERVICE_NAME + "/examer/getUserExamPermission/{taskId}/{userId}";
        ServiceResponse<DUserExamPermission> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DUserExamPermission>>() {
        }, taskId, ServiceHeaderUtil.getRequestHeader().getCustomerId());

        DUserExamPermission userExamPermission = response.getResponse();

        Date now = new Date();
        log.info("userExamPermission: " + userExamPermission);
        log.info("now"+ now);
        log.info("TaskStartDate"+userExamPermission.getTaskStartDate());
        log.info("!userExamPermission.getTaskStartDate().before(now)" + !userExamPermission.getTaskStartDate().before(now));

        if (userExamPermission.getTaskStartDate() != null && !userExamPermission.getTaskStartDate().before(now)) {
            throw new WrappedException(BizEnums.TaskNotStart);
        }
        if (userExamPermission.getTaskEndDate() != null && userExamPermission.getTaskEndDate().before(now)) {
            throw new WrappedException(BizEnums.TaskIsEnd);
        }
        if (userExamPermission.getUserExamStatus() != TUserExamStatus.ENABLED.getValue()) {
            throw new WrappedException(BizEnums.NoUserExam);
        }
        if (userExamPermission.getExerciseEndTime() != null) {
            throw new WrappedException(BizEnums.UserExamIsFinish);
        }

        return userExamPermission;
    }
}
