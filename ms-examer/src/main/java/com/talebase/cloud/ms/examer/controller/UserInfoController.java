package com.talebase.cloud.ms.examer.controller;

import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldIsextension;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldIsshow;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldType;
import com.talebase.cloud.base.ms.notify.dto.DReceiverStatistics;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.*;
import com.talebase.cloud.ms.examer.service.UserExamService;
import com.talebase.cloud.ms.examer.service.UserFieldService;
import com.talebase.cloud.ms.examer.service.UserImportLogService;
import com.talebase.cloud.ms.examer.service.UserInfoService;
import com.talebase.cloud.ms.examer.util.CUserInfo;
import com.talebase.cloud.ms.examer.util.DField;
import com.talebase.cloud.ms.examer.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserFieldService userFieldService;
    @Autowired
    private UserExamService userExamService;

    /**
     * 管理员重置考生密码
     * @param request
     * @return
     */
    @PostMapping(value = "/examer/project/reSetPassword/select")
    public ServiceResponse reSetPassword(@RequestBody ServiceRequest<DReSetPassword> request) throws Exception {
        DReSetPassword dReSetPassword = request.getRequest();
        return userInfoService.reSetPassword(dReSetPassword);
    }

    /**
     * 考生修改密码
     * @param request
     * @return
     */
    @PostMapping(value = "/examer/user/updateUserPassword")
    public ServiceResponse updateUserPassword(@RequestBody ServiceRequest<DUpdatePassword> request) throws Exception {
        DUpdatePassword dUpdatePassword = request.getRequest();
        return userInfoService.updateUserPassword(dUpdatePassword);
    }

    /**
     * 修改密码
     *
     * @param request
     * @return
     */
/*    @PostMapping(value = "/examer/project/reSetPassword/all")
    public ServiceResponse reSetAllPassword(@RequestBody ServiceRequest<DReSetPassword> request) throws Exception {
        DReSetPassword dReSetPassword = request.getRequest();
        return userInfoService.reSetAllPassword(dReSetPassword);
    }*/

    /**
     * 删除选择账号密码
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/examer/project/del")
    public ServiceResponse del(@RequestBody ServiceRequest<DReSetPassword> request) {
        DReSetPassword dReSetPassword = request.getRequest();
        return userInfoService.del(dReSetPassword, request.getRequestHeader());
    }

    /**
     * 获取测试账号礼拜
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/examer/project/export/all")
    public ServiceResponse<DExportExamers> getExamUserInfos(@RequestBody ServiceRequest<DUserExamPageRequest> request)throws Exception {
        DUserExamPageRequest dReSetPassword = request.getRequest();
        return userInfoService.getExamUserInfos(dReSetPassword);
    }

    /**
     * 获取考生信息
     *
     * @param dUserRequest
     * @return
     */
    @PostMapping(value = "/examer/getUserInfo")
    public ServiceResponse<TUserInfo> getUserInfo(@RequestBody DUserRequest dUserRequest) {
        if(dUserRequest.getMobile() != null){
            return userInfoService.getUserByMobile(dUserRequest.getMobile());
        }else{
            return userInfoService.getUserByAccount(dUserRequest);
        }
    }

    /**
     * 根据ID获取考生信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/examer/getUserById/{id}")
    public ServiceResponse<TUserInfo> getUserById(@PathVariable("id") Integer id) {
        return userInfoService.getUserById(id);
    }


    /**
     * 创建多个账号
     *
     * @param serviceRequest
     * @return
     */
    @PostMapping(value = "/examers")
    public ServiceResponse<DCreateExamersResp> createExamers(@RequestBody ServiceRequest<DCreateExamersReq> serviceRequest) throws Exception {
        DCreateExamersReq req = serviceRequest.getRequest();

        int startNum = 0;
        int endNum = 0;

        if (NumberUtil.isEmpty(req.getAmount())) {//使用自定义数字创建
            startNum = req.getStartNum();
            endNum = req.getEndNum();
        } else {//使用数量创建
            Integer num = userInfoService.getMaxAccountByPre(serviceRequest.getRequestHeader().getCompanyId(), req.getAccountPre(), req.getSuffix());
            int maxNum = num != null ? num : 0;

            startNum = maxNum + 1;
            endNum = maxNum + req.getAmount();
        }

        List<TUserInfo> userInfos = new ArrayList<>();

        for (int i = startNum; i <= endNum; i++) {
            TUserInfo userInfo = new TUserInfo();
            userInfo.setAccount(req.getAccountPre() + i + req.getSuffix());
            userInfo.setPassword(req.getPassword());
            userInfo.setName(userInfo.getAccount());
            userInfos.add(userInfo);
        }

        DCreateExamersResp resp = userInfoService.createUsers(userInfos, req.getProjectId(), req.getTaskId(), serviceRequest.getRequestHeader());

        return new ServiceResponse(resp);
    }

    private static int getNumFromAccount(String account, String pre, String suffix) {
        int num = 0;

        try {
            String regEx = pre + "(.*)" + suffix;
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(account);

            if (m.find()) {
                num = Integer.parseInt(m.group(1));
            }
        } catch (Exception e) {
        }

        return num;
    }

//    public static void main(String[] args){
//        System.out.println(getNumFromAccount("test123__sys", "test", "_sys"));
//    }

    @PostMapping(value = "/examer")
    public ServiceResponse<String> createUser(@RequestBody ServiceRequest<DEditExamerReq> serviceRequest) throws Exception {
        DEditExamerReq req = serviceRequest.getRequest();
        List<TUserShowField> fs = userFieldService.findUserFields(serviceRequest.getRequestHeader().getCompanyId(), req.getProjectId(), req.getTaskId());

        CUserInfo cUserInfo = UserUtil.toUserInfo(serviceRequest.getRequest().getMap(), fs, true);
        cUserInfo.getUserInfo().setId(null);//保证没有传id
        cUserInfo.getUserInfo().setCompanyId(serviceRequest.getRequestHeader().getCompanyId());
        cUserInfo.getUserInfo().setCreater(serviceRequest.getRequestHeader().getOperatorName());

        String account = userInfoService.saveUser(cUserInfo, req.getProjectId(), req.getTaskId(), fs, serviceRequest.getRequest().getMap(), serviceRequest.getRequestHeader());
        return new ServiceResponse<>(account);
    }

    @PostMapping(value = "/examer/createUserForPerfect")
    public ServiceResponse<Integer> createUserForPerfect(@RequestBody ServiceRequest<DEditExamerReq> serviceRequest) throws Exception {
        DEditExamerReq req = serviceRequest.getRequest();
        List<TUserShowField> fs = userFieldService.findUserFields(Integer.valueOf(req.getMap().get("companyId")), req.getProjectId(),null);
        TUserInfo userInfo = UserUtil.toUserInfo(serviceRequest.getRequest().getMap(), fs);
        userInfo.setId(null);//保证没有传id
        userInfo.setCreater(userInfo.getMobile());
        userInfo.setCompanyId(Integer.valueOf(serviceRequest.getRequest().getMap().get("companyId")));

        Integer id = userInfoService.saveUserForPerfect(userInfo,req.getProjectId());
        ServiceResponse response = new ServiceResponse();
        response.setResponse(id);
        return response;
    }

    @PostMapping(value = "/examer/saveUserExam")
    public ServiceResponse<Integer> saveUserExam(@RequestBody ServiceRequest<DEditExamerReq> serviceRequest) throws Exception {
        DEditExamerReq req = serviceRequest.getRequest();
        TUserInfo userInfo = new TUserInfo();
        userInfo.setId(Integer.valueOf(req.getMap().get("userId")));//保证没有传id
        userInfo.setCreater(req.getMap().get("mobile"));
        userInfo.setCompanyId(Integer.valueOf(req.getMap().get("companyId")));
        List<TUserExam> list = userExamService.getUserExam(userInfo.getId());
        if(list == null || list.size() == 0){
            userInfoService.saveUserExam(userInfo, req.getProjectId(), req.getTaskIds(), serviceRequest.getRequestHeader());
        }
        ServiceResponse response = new ServiceResponse();
        return response;
    }

    @GetMapping(value = "/examer/scanEnable/{projectId}/{mobile}")
    public ServiceResponse scanEnable(@PathVariable("projectId") Integer projectId,@PathVariable("mobile") String mobile) throws Exception {
        userInfoService.scanEnable(projectId,mobile);
        ServiceResponse response = new ServiceResponse();
        return response;
    }

    @GetMapping(value = "/examer/getCntForExamAndUserByMobileAndProjectId/{mobile}/{projectId}")
    public ServiceResponse<Integer> getCntForExamAndUserByMobileAndProjectId(@PathVariable("mobile") String mobile,@PathVariable("projectId") Integer projectId) throws Exception {
        ServiceResponse<Integer> response = new ServiceResponse<Integer>(userInfoService.getCntForExamAndUserByMobileAndProjectId(mobile,projectId));
        return response;
    }


    @PutMapping(value = "/examer/{userId}")
    public ServiceResponse<String> modifyUser(@RequestBody ServiceRequest<DEditExamerReq> serviceRequest, @PathVariable("userId") Integer userId) throws Exception {
        DEditExamerReq req = serviceRequest.getRequest();
        List<TUserShowField> fs = userFieldService.findUserFields(serviceRequest.getRequestHeader().getCompanyId(), req.getProjectId(), req.getTaskId());
        CUserInfo cUserInfo = UserUtil.toUserInfo(serviceRequest.getRequest().getMap(), fs, true);
        cUserInfo.getUserInfo().setCompanyId(serviceRequest.getRequestHeader().getCompanyId());
        cUserInfo.getUserInfo().setCreater(serviceRequest.getRequestHeader().getOperatorName());
        if (cUserInfo.getUserInfo().getPassword() == null){
            cUserInfo.getUserInfo().setPassword(Des3Util.des3EncodeCBC(PasswordUtil.getRandomString(PasswordUtil.SIX_BITS)));//保证不改变密码
        }else{
            cUserInfo.getUserInfo().setPassword(Des3Util.des3EncodeCBC(cUserInfo.getUserInfo().getPassword()));//保证不改变密码
        }

        cUserInfo.getUserInfo().setId(userId);

        String account = userInfoService.updateUser(cUserInfo, req.getProjectId(), req.getTaskId(), fs, serviceRequest.getRequest().getMap(), serviceRequest.getRequestHeader());
        return new ServiceResponse<>(account);
    }

    /**
     * 修改用户资料
     *
     * @param serviceRequest
     * @return
     */
    @PostMapping(value = "/examer/modifyUserForPerfect")
    public ServiceResponse<String> modifyUserForPerfect(@RequestBody ServiceRequest<DEditExamerReq> serviceRequest) throws Exception {
        DEditExamerReq req = serviceRequest.getRequest();
        List<TUserShowField> fs = userFieldService.findUserFields(Integer.valueOf(req.getMap().get("companyId")), req.getProjectId(), req.getTaskId());
        CUserInfo cUserInfo = UserUtil.toUserInfo(serviceRequest.getRequest().getMap(), fs,true);
        cUserInfo.getUserInfo().setCreater(cUserInfo.getUserInfo().getName());
        cUserInfo.getUserInfo().setPassword(null);//保证不改变密码
        cUserInfo.getUserInfo().setId(Integer.valueOf(req.getMap().get("id")));
        cUserInfo.getUserInfo().setCompanyId(Integer.valueOf(req.getMap().get("companyId")));
        cUserInfo.getErrTips().addAll(UserUtil.checkNotNullFields(serviceRequest.getRequest().getMap(), fs,false));
        String account = userInfoService.updateUserForPerfect(cUserInfo, req.getProjectId(), req.getTaskId(), fs, serviceRequest.getRequest().getMap(), serviceRequest.getRequestHeader());
        return new ServiceResponse<>(account);
    }

//    public TUserInfo toUserInfo(Map<String, String> map, List<TUserShowField> fields) throws Exception {
//        Map<String, Object> oriFieldMap = new HashMap<>();//系统字段
//        List<DField> exFields = new ArrayList<>();//扩展字段
//
//        //遍历赋值
//        for (TUserShowField field : fields) {
//
//            String key = field.getFieldKey();
//            String value = map.get(key);
//
//            if (field.getIsshow() == TUserShowFieldIsshow.HIDDEN.getValue())//不显示的不管
//                continue;
//
////            if(field.getIsmandatory() == TUserShowFieldIsmandatory.MANDATORY.getValue())//必填的没填
////                if(StringUtil.isEmpty(value)){
////                    record.setRemark(field.getFieldName() + "是必填项");
////                    return null;
////                }
//
//            if ("account".equals(key)) {//账号不能为空
//                if (StringUtil.isEmpty(value)) {
//                    throw new WrappedException(BizEnums.NoAccount);
//                }
//            } else if ("password".equals(key)) {//密码不为空时,检查是否大于6位且至少一位数字或者字母
//                if (!StringUtil.isEmpty(value)) {
//                    if (value.length() < 6) {
//                        throw new WrappedException(BizEnums.PasswordTooShort);
//                    } else if (!PasswordUtil.containLetterAndNumber(value)) {
//                        throw new WrappedException(BizEnums.PasswordNotVail);
//                    } else if (PasswordUtil.containChinese(value)) {
//                        throw new WrappedException(BizEnums.PasswordCannotHasChinese);
//                    }
//                } else {//否则跳过，在service中再随机生成，因为需要判断是否已有账户
////                    oriFieldMap.put("password", geneRandomPwd());
//                    continue;
//                }
//            } else if ("mobile".equals(key)) {//手机号码规则
//                if (!StringUtil.isEmpty(value)) {
//                    if (!VailUtil.isMobile(value)) {
//                        try {
//                            //手机号码有可能被解析成科学计数法
//                            value = new BigDecimal(value).toPlainString();
//                        } catch (Exception e) {
//                        }
//                        if (!VailUtil.isMobile(value)) {
//                            throw new WrappedException(BizEnums.MobileNotVail);
//                        }
//                    }
//                }
//            } else if ("email".equals(key)) {//邮箱规则且不能多于50个字符
//                if (!StringUtil.isEmpty(value)) {
//                    if (!VailUtil.isEmail(value)) {
//                        throw new WrappedException(BizEnums.EmailNotVail);
//                    } else if (value.length() > 50) {
//                        throw new WrappedException(BizEnums.InputTooLong);
//                    }
//                }
//            } else if ("identityNum".equals(field.getFieldKey())) {//身份证规则
//                if (!StringUtil.isEmpty(value)) {
//                    if (!IdcardValidator.isValidatedAllIdcard(value)) {
//                        throw new WrappedException(BizEnums.IDNotVail);
//                    }
//                } else if ("workYears".equals(key)) {//工作年限需要数字类型
//                    if (!StringUtil.isEmpty(value)) {
//                        try {
//                            int years = Integer.parseInt(value);
//                            if (years < 0 || years > 40) {
//                                throw new WrappedException(BizEnums.WorkTimeNotVail);
//                            }
//                        } catch (Exception e) {
//                            throw new WrappedException(BizEnums.WorkTimeNotVail);
//                        }
//                    }
//                }
//
//                if (field.getType() == TUserShowFieldType.INPUT.getValue()) {//输入框最大长度为50个字符
//                    if (!StringUtil.isEmpty(value) && value.length() > 50) {
//                        throw new WrappedException(BizEnums.InputTooLong);
//                    }
//                } else if (field.getType() == TUserShowFieldType.DATE_TYPE.getValue()) {//日期格式的若不为空则需要检查是否符合yyyy-MM-dd
//                    if (!StringUtil.isEmpty(value)) {
//                        try {
//                            TimeUtil.formateDate(value);
//                        } catch (Exception e) {
//                            throw new WrappedException(BizEnums.DateNotVail);
//                        }
//                    }
//                } else if (field.getType() == TUserShowFieldType.SELECT_TYPE.getValue()) {//选项类型，值若不为空则必须是选项之中的值
//                    if (!StringUtil.isEmpty(value)) {
//                        List<String> opts = StringUtil.toStrListByComma(field.getSelectValue());
//                        if (!opts.contains(value)) {
//                            throw new WrappedException(BizEnums.SelectNotVail);
//                        }
//                    }
//                }
//
//                if ("workYears".equals(key)) {//转化成整形
//                    oriFieldMap.put(field.getFieldKey(), Integer.parseInt(value));
//                } else if ("birthday".equals(key)) {//转化成TimeStamp
//                    oriFieldMap.put(field.getFieldKey(), new Timestamp(TimeUtil.formateDate(value).getTime()));
//                } else {//常规处理
//                    if (field.getIsextension() == TUserShowFieldIsextension.UNEXTENSION.getValue()) {//放到map，稍后直接利用gson转成Tuserinfo
//                        oriFieldMap.put(key, value);
//                    } else {//放到exList，稍后直接利用gson转成jsonStr
//                        exFields.add(new DField(key, field.getFieldName(), value));
//                    }
//                }
//
//            }
//            //遍历赋值end
//
//        }
//        TUserInfo userInfo = GsonUtil.fromJson(GsonUtil.toJson(map), TUserInfo.class);
//        userInfo.setExtensionField(GsonUtil.toJson(exFields));
//
//        return userInfo;
//    }

    /**
     * 获取账号、手机、邮箱的统计数据
     *
     * @param serviceRequest
     * @return
     */
    @PostMapping(value = "/examer/project/counts")
    public ServiceResponse<DReceiverStatistics> getReceiverStatistics(@RequestBody ServiceRequest<DReSetPassword> serviceRequest) {
        DReSetPassword dReSetPassword = serviceRequest.getRequest();
        dReSetPassword.setCompanyId(serviceRequest.getRequestHeader().getCompanyId());
        return userInfoService.getReceiverStatistics(dReSetPassword);
    }

    /**
     * 获取查询用户列表
     *
     * @param serviceRequest
     * @return
     */
    @PostMapping(value = "/examer/project/userList")
    public ServiceResponse<List<TUserInfo>> getTUserInfo(@RequestBody ServiceRequest<DReSetPassword> serviceRequest) {
        DReSetPassword dReSetPassword = serviceRequest.getRequest();
        dReSetPassword.setCompanyId(serviceRequest.getRequestHeader().getCompanyId());
        return userInfoService.getUserList(dReSetPassword);
    }

}
