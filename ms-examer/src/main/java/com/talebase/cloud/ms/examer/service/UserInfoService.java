package com.talebase.cloud.ms.examer.service;


import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.domain.TUserExam;
import com.talebase.cloud.base.ms.examer.domain.TUserImportRecord;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.dto.*;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.base.ms.examer.enums.TUserExamType;
import com.talebase.cloud.base.ms.examer.enums.TUserImportRecordStatus;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldIsunique;
import com.talebase.cloud.base.ms.notify.dto.DReceiverStatistics;
import com.talebase.cloud.base.ms.project.domain.TProject;
import com.talebase.cloud.base.ms.project.enums.TProjectScanEnable;
import com.talebase.cloud.base.ms.project.enums.TProjectStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.Des3Util;
import com.talebase.cloud.common.util.PasswordUtil;
import com.talebase.cloud.common.util.PermissionEnum;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.examer.cfg.Config;
import com.talebase.cloud.ms.examer.dao.UserExamMapper;
import com.talebase.cloud.ms.examer.dao.UserFieldMapper;
import com.talebase.cloud.ms.examer.dao.UserInfoMapper;
import com.talebase.cloud.ms.examer.util.CUserInfo;
import com.talebase.cloud.ms.examer.util.DFieldInx;
import com.talebase.cloud.ms.examer.util.FieldErrTip;
import com.talebase.cloud.ms.examer.util.UserUtil;
import org.apache.ibatis.annotations.Param;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.jcajce.provider.symmetric.AES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@Service
public class UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserFieldMapper userFieldMapper;

    @Autowired
    private UserExamMapper userExamMapper;

    @Autowired
    private UserFieldService userFieldService;

    @Autowired
    private TaskProgressService taskProgressService;

    /**
     * 重置密码(更新选择账号的密码)
     *
     * @param dReSetPassword
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ServiceResponse reSetPassword(DReSetPassword dReSetPassword) throws Exception {
        /*if (StringUtils.isEmpty(dReSetPassword.getIds())){
            return new ServiceResponse(BizEnums.Exam_Repassword_Not_Found_Id);
        }*/
        String passWord = "";
        if (dReSetPassword.isRandom() || StringUtils.isEmpty(dReSetPassword.getNewPassword()))
            passWord = Des3Util.des3EncodeCBC(PasswordUtil.getRandomString(PasswordUtil.SIX_BITS));// 密码加密
        else
            passWord = Des3Util.des3EncodeCBC(dReSetPassword.getNewPassword());// 密码加密

        List<Integer> idsList = StringUtil.toIntListByComma(dReSetPassword.getIds());

        userInfoMapper.updatePassword(dReSetPassword, passWord, idsList);

        return new ServiceResponse();

    }

    /**
     * 考生修改密码
     *
     * @param dUpdatePassword
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ServiceResponse updateUserPassword(DUpdatePassword dUpdatePassword) throws Exception {
        TUserInfo tUserInfo = userInfoMapper.getUserById(dUpdatePassword.getUserId());
        if (tUserInfo == null || !tUserInfo.getPassword().equals(Des3Util.des3EncodeCBC(dUpdatePassword.getOldPassword())))
            throw new WrappedException(BizEnums.AdminLoginErr);
        if(tUserInfo.getId().intValue() != dUpdatePassword.getUserId().intValue()){
            throw new WrappedException(BizEnums.UpdatePasswordByOther);
        }
        if (dUpdatePassword.getNewPassword().length() < 6) {
            throw new WrappedException(BizEnums.PasswordTooShort);
        }else if(!PasswordUtil.containLetterAndNumber(dUpdatePassword.getNewPassword())){
            throw new WrappedException(BizEnums.PasswordNotVail);
        }else if(PasswordUtil.containChinese(dUpdatePassword.getNewPassword())){
            throw new WrappedException(BizEnums.PasswordCannotHasChinese);
        }
        dUpdatePassword.setNewPassword(Des3Util.des3EncodeCBC(dUpdatePassword.getNewPassword()));
        userInfoMapper.updateUserPassword(dUpdatePassword);
        return new ServiceResponse();

    }

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public ServiceResponse del(DReSetPassword dReSetPassword, ServiceHeader header) {

        List<Integer> idsList = StringUtil.toIntListByComma(dReSetPassword.getIds());

        Integer taskId = dReSetPassword.getTaskId() != null && dReSetPassword.getTaskId() > 0 ? dReSetPassword.getTaskId() : null;
        if (taskId == null && idsList.size() > 0) {
            taskId = userExamMapper.get(idsList.get(0)).getTaskId();
        }

        if (!header.getPermissions().contains(PermissionEnum.c99_2.name())) {//一般管理员需要对考生状态进行判断
            //查询出已经开考的考生数量
            Integer cntStartExam = userExamMapper.queryStartExamCnt(dReSetPassword, idsList);
            if (cntStartExam > 0) {
                throw new WrappedException(BizEnums.ExamerStartExamCannotBeDelete);
            }
        }else{//顾问删除测试账号的同时，删除答题记录
            userExamMapper.delStartExamExercise(dReSetPassword,idsList);
        }

        userExamMapper.delExamer(dReSetPassword, idsList);
        if (taskId != null) {
            taskProgressService.refreshTask(taskId);//执行删除动作后，直接刷新
        }

        return new ServiceResponse();
    }

    /**
     * 获取考生信息
     *
     * @param dUserRequest
     * @return
     */
    public ServiceResponse<TUserInfo> getUserByAccount(DUserRequest dUserRequest) {
        return new ServiceResponse<TUserInfo>(userInfoMapper.getUserByAccount(dUserRequest.getAccount(), dUserRequest.getCompanyId()));
    }

    /**
     * 获取考生信息
     *
     * @param mobile
     * @return
     */
    public ServiceResponse<TUserInfo> getUserByMobile(String mobile) {
        return new ServiceResponse<TUserInfo>(userInfoMapper.getUserByMobile(mobile));
    }

    /**
     * 获取考生信息
     *
     * @param id
     * @return
     */
    public ServiceResponse<TUserInfo> getUserById(Integer id) {
        return new ServiceResponse<TUserInfo>(userInfoMapper.getUserById(id));
    }

    /**
     * 获取导出测试账号
     *
     * @param dUserExamPageRequest
     * @return
     */
    public ServiceResponse<DExportExamers> getExamUserInfos(DUserExamPageRequest dUserExamPageRequest) throws Exception {
        //获取显示字段
        List<TUserShowField> fields = userFieldService.getField(dUserExamPageRequest.getCompanyId(),
                dUserExamPageRequest.getProjectId(),
                dUserExamPageRequest.getTaskId());

        List<String> fieldKeyList = new ArrayList<String>();
        List<String> fieldNameList = new ArrayList<String>();
        List<List<String>> data = new ArrayList<List<String>>();

        if (fields != null && fields.size() > 0) {
            //获取导出文件显示标题
            for (int i = 0; i < fields.size(); i++) {
                TUserShowField tUserShowField = fields.get(i);
                fieldKeyList.add(tUserShowField.getFieldKey());
                fieldNameList.add(tUserShowField.getFieldName());
            }

            if (fieldNameList.size() > 0) {
                fieldNameList.add("创建时间");
                fieldNameList.add("答题状态");
                fieldNameList.add("邮件发送状态");
                fieldNameList.add("短信发送状态");
            }
            //获取导出文件显示value
            List<Map<String, Object>> list = userFieldMapper.getAllProjectExamersByMap(dUserExamPageRequest, null);
            List<Map<String, Object>> mapList = userFieldService.matchField(list,
                    dUserExamPageRequest.getCompanyId(),
                    dUserExamPageRequest.getProjectId(),
                    dUserExamPageRequest.getTaskId(),
                    true);
            //获取显示字段的值
            if (mapList != null && mapList.size() > 0) {
                for (int i = 0; i < mapList.size(); i++) {
                    Map<String, Object> map = mapList.get(i);
                    //得到每行字段和value
                    List<Map<String, Object>> keyValueList = (List<Map<String, Object>>) map.get("userInfos");
                    List<String> value = new ArrayList<String>();
                    for (String key : fieldKeyList) {
                        for (Map<String, Object> mapping : keyValueList) {
                            //如果是显示字段则导出
                            if (key.equals(mapping.get("fieldKey"))) {
                                value.add(toValue(mapping.get("fieldValue")));
                            }
                        }
                    }

                    //加载导出data
                    if (!value.isEmpty()) {
                        value.add(toValue(map.get("createTime")));
                        if (((Integer) map.get("answerStatus")) == DUserExamPageRequest.NOBEGIN) {
                            value.add("未开始");
                        } else if (((Integer) map.get("answerStatus")) == DUserExamPageRequest.ANSWER) {
                            value.add("答题中");
                        } else if (((Integer) map.get("answerStatus")) == DUserExamPageRequest.COMPLETE) {
                            value.add("已完成");
                        }

                        if (((Integer) map.get("sendEmailStatus")) == DUserExamPageRequest.SENDING) {
                            value.add("发送中");
                        } else if (((Integer) map.get("sendEmailStatus")) == DUserExamPageRequest.FAILURE) {
                            value.add("发送失败");
                        } else if (((Integer) map.get("sendEmailStatus")) == DUserExamPageRequest.SUCCESS) {
                            value.add("发送成功");
                        } else {
                            value.add("未发送");
                        }

                        if (((Integer) map.get("sendSmsStatus")) == DUserExamPageRequest.SENDING) {
                            value.add("发送中");
                        } else if (((Integer) map.get("sendSmsStatus")) == DUserExamPageRequest.FAILURE) {
                            value.add("发送失败");
                        } else if (((Integer) map.get("sendSmsStatus")) == DUserExamPageRequest.SUCCESS) {
                            value.add("发送成功");
                        } else {
                            value.add("未发送");
                        }

                        data.add(value);
                    }
                }
            }
        }

        DExportExamers dExportExamers = new DExportExamers();
        dExportExamers.setData(data);
        dExportExamers.setHeaders(fieldNameList);

        ServiceResponse<DExportExamers> response = new ServiceResponse<DExportExamers>();
        response.setResponse(dExportExamers);
        return response;
    }

    public String toValue(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (o instanceof java.sql.Timestamp) {
            return sdf.format(o);
        }
        return (String) o;
    }

    public Integer getMaxAccountByPre(Integer companyId, String pre, String suffix) {
        return userInfoMapper.getMaxAccountByPreSubffix(companyId, pre, suffix);
    }

    /**
     * 创建多个考生
     *
     * @return
     */
    public DCreateExamersResp createUsers(List<TUserInfo> userInfos, Integer projectId, Integer taskId, ServiceHeader header) throws Exception {

        DCreateExamersResp resp = new DCreateExamersResp();

        for (TUserInfo userInfo : userInfos) {
            String password = StringUtil.isEmpty(userInfo.getPassword()) ? PasswordUtil.getRandomString(PasswordUtil.SIX_BITS) : userInfo.getPassword();
            userInfo.setPassword(Des3Util.des3EncodeCBC(password));

            TUserInfo oriUser = userInfoMapper.getUserByAccount(userInfo.getAccount(), header.getCompanyId());
            if (oriUser != null) {//已存在账号，不能新建
                resp.getFailAccs().add(userInfo.getAccount());
                continue;
            } else {//可创建
                userInfo.setCompanyId(header.getCompanyId());
                userInfo.setCreater(header.getOperatorName());

                try {
                    userInfoMapper.insert(userInfo);
                    if (projectId != null && taskId != null) {
//                        int cnt = userExamMapper.getCntForExamAndUser(userInfo.getId(), projectId, taskId);
//                        if(cnt == 0) {//原有的就不会插一条新的
                        saveUserExam(header, userInfo, projectId, taskId);
//                        }
                    }
                    resp.getSuccAccs().add(userInfo.getAccount());
                } catch (Exception e) {
                    resp.getFailAccs().add(userInfo.getAccount());
                }
            }
        }

        return resp;
    }

    /**
     * 保存考生考试信息
     *
     * @param userInfo
     * @param projectId
     * @param taskIds
     * @param header
     * @return
     * @throws Exception
     */
    public void saveUserExam(TUserInfo userInfo, Integer projectId, List<Integer> taskIds, ServiceHeader header) throws Exception {
        if (projectId != null && taskIds != null && taskIds.size() > 0) {
            for (Integer taskId : taskIds) {
                saveUserExam(header, userInfo, projectId, taskId);
            }
        }
        //保存完后修改扫码人数上限
        TProject tProject =userExamMapper.getProjectById(projectId);
        tProject.setScanNow((tProject.getScanNow() == null ?0:tProject.getScanNow()) + 1);
        userExamMapper.updateProjectById(tProject);
    }

    public Integer saveUserExam(ServiceHeader header, TUserInfo userInfo, Integer projectId, Integer taskId) {
        TUserExam userExam = new TUserExam();
        userExam.setCompanyId(userInfo.getCompanyId());
        userExam.setStatus(TUserExamStatus.ENABLED.getValue());
        userExam.setCreater(userInfo.getCreater());
        userExam.setProjectId(projectId);
        userExam.setTaskId(taskId);
        userExam.setType(TUserExamType.EXAM.getValue());
        userExam.setUserId(userInfo.getId());
        userExamMapper.insert(userExam);
        taskProgressService.joinTask(taskId);
        return userExam.getId();
    }

    /**
     * 仍需要对进来的数据进行数据库相关的检测，例如业务上定义的惟一性;
     * 若保存对象成功，返回用户名
     *
     * @return
     */
    public String saveUser(CUserInfo c, Integer projectId, Integer taskId, List<TUserShowField> fields, Map<String, String> map, ServiceHeader header) throws Exception {

        TUserInfo oriUser = userInfoMapper.getUserByAccount(c.getUserInfo().getAccount(), header.getCompanyId());
        if (oriUser != null)
            c.addErrTip(BizEnums.AccountExists, UserUtil.ACCOUNT, "账号");

        TUserInfo userInfo = c.getUserInfo();
        checkUnique(header.getCompanyId(), projectId, taskId, userInfo.getAccount(), fields, map, c.getErrTips());

        if (!c.getErrTips().isEmpty())//保存前先判断是否存在错误，是的话在这里报错
            throw new WrappedException(BizEnums.EditAccountErr, c.getErrMsg());

        if (StringUtil.isEmpty(userInfo.getName()))//姓名字段为空时自动使用账号字段填充
            userInfo.setName(userInfo.getAccount());

        if (StringUtil.isEmpty(userInfo.getPassword()))//创建时密码为空则自动补充随机密码
            userInfo.setPassword(PasswordUtil.getRandomString(PasswordUtil.SIX_BITS));
        userInfo.setPassword(Des3Util.des3EncodeCBC(userInfo.getPassword()));

        userInfoMapper.insert(userInfo);
        if (projectId != null && taskId != null) {
            saveUserExam(header, userInfo, projectId, taskId);
        }

        return userInfo.getAccount();
    }

    /**
     * 登录后创建用户
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    public Integer saveUserForPerfect(TUserInfo userInfo,Integer projectId) throws Exception {
        TUserInfo oriUser = userInfoMapper.getUserByAccount(userInfo.getAccount(), userInfo.getCompanyId());
        if (oriUser != null)
            throw new WrappedException(BizEnums.AccountExists);

        if (StringUtil.isEmpty(userInfo.getName()))//姓名字段为空时自动使用账号字段填充
            userInfo.setName(userInfo.getAccount());

        if (StringUtil.isEmpty(userInfo.getPassword()))//创建时密码为空则自动补充随机密码
            userInfo.setPassword(PasswordUtil.getRandomString(PasswordUtil.SIX_BITS));
        userInfo.setPassword(Des3Util.des3EncodeCBC(userInfo.getPassword()));

        userInfoMapper.insert(userInfo);
        return userInfo.getId();
    }

    /**
     *
     * @param projectId
     * @return
     */
    public boolean scanEnable(Integer projectId,String mobile) {
        TProject tProject =userExamMapper.getProjectById(projectId);
        boolean falg = false;
        if(tProject != null && tProject.getStatus().intValue() != TProjectStatus.ENABLE.getValue() && tProject.getScanEnable() != TProjectScanEnable.ENABLE.getValue())
            throw new WrappedException(BizEnums.ScanCodeDisable);
//            return false;//若项目不是启用状态或者项目没开通扫码，则返回false

        Date now = new Date();
        if(tProject.getScanStartDate() != null && now.before(tProject.getScanStartDate()))
            throw new WrappedException(BizEnums.ScanCodeDisable);
//            return false;//若当前在扫码有效期之前，则返回false
        if(tProject.getScanEndDate() != null && now.after(tProject.getScanEndDate()))
            throw new WrappedException(BizEnums.ScanCodePast);
//            return false;//若当前在扫码有效期之后，则返回false

        if(!"0".equals(mobile)){
            Integer count = getCntForExamAndUserByMobileAndProjectId(mobile,projectId);
            if(count > 0){
                falg = true;
            }else{
                int scanNow = tProject.getScanNow() == null ? 0 : tProject.getScanNow();
                int scanMax = tProject.getScanMax() == null ? Integer.MAX_VALUE : tProject.getScanMax();
                if(scanNow >= scanMax){
                    throw new WrappedException(BizEnums.ScanCodeOver);
                }
            }
        }else{
            falg = true;
        }
        return falg;
    }

    public String updateUser(CUserInfo c, Integer projectId, Integer taskId, List<TUserShowField> fields, Map<String, String> map, ServiceHeader header) {

        TUserInfo userInfo = c.getUserInfo();
        TUserInfo oriUser = userInfoMapper.getUser(userInfo.getId(), userInfo.getCompanyId());
        if (oriUser == null)
            c.addErrTip(BizEnums.UserIdNotExists, UserUtil.ACCOUNT, "账号");

        if (!oriUser.getAccount().equals(userInfo.getAccount()))
            c.addErrTip(BizEnums.AccountCannotModify, UserUtil.ACCOUNT, "账号");

        checkUnique(header.getCompanyId(), projectId, taskId, oriUser == null ? userInfo.getAccount() : oriUser.getAccount(), fields, map, c.getErrTips());

        if (!c.getErrTips().isEmpty())
            throw new WrappedException(BizEnums.EditAccountErr, c.getErrMsg());

        //userInfo.setPassword(null);//默认修改资料不会修改到密码
        userInfoMapper.update(userInfo);
        int resp = 0;
        if (projectId != null && taskId != null) {
            int cnt = userExamMapper.getCntForExamAndUser(userInfo.getId(), projectId, taskId);
            if (cnt == 0)
                resp = saveUserExam(header, userInfo, projectId, taskId);
        }

        return resp > 0 ? userInfo.getAccount() : "";
    }

    /**
     * 查找用户和项目的关系数
     * @param mobile
     * @param projectId
     * @return
     */
    public Integer getCntForExamAndUserByMobileAndProjectId(String mobile, Integer projectId){
        return userExamMapper.getCntForExamAndUserByMobileAndProjectId(mobile,projectId);
    }
    /**
     * 登录后修改用户
     *
     * @param c
     * @param projectId
     * @param taskId
     * @param fields
     * @param map
     * @param header
     * @return
     * @throws Exception
     */
    public String updateUserForPerfect(CUserInfo c, Integer projectId, Integer taskId, List<TUserShowField> fields, Map<String, String> map, ServiceHeader header) throws Exception {
        TUserInfo userInfo = c.getUserInfo();
        TUserInfo oriUser = userInfoMapper.getUser(userInfo.getId(), userInfo.getCompanyId());
        if (oriUser == null)
            c.addErrTip(BizEnums.UserIdNotExists, UserUtil.ACCOUNT, "账号");

        if (!oriUser.getAccount().equals(userInfo.getAccount()))
            c.addErrTip(BizEnums.AccountCannotModify, UserUtil.ACCOUNT, "账号");
        checkUnique(header.getCompanyId(), projectId, taskId, oriUser == null ? userInfo.getAccount() : oriUser.getAccount(), fields, map, c.getErrTips());

        if (!c.getErrTips().isEmpty())
            throw new WrappedException(BizEnums.EditAccountErr, c.getErrMsg());

        userInfo.setPassword(null);//默认修改资料不会修改到密码

        userInfoMapper.update(userInfo);
        int resp = 0;
        if (projectId != null && taskId != null) {
            int cnt = userExamMapper.getCntForExamAndUser(userInfo.getId(), projectId, taskId);
            if (cnt == 0)
                resp = saveUserExam(header, userInfo, projectId, taskId);
        }

        return resp > 0 ? userInfo.getAccount() : "";
    }

//    private void checkUnique(TUserInfo userInfo, Integer projectId, Integer taskId, List<TUserShowField> fields, Map<String, String> map, boolean notEmpty) throws Exception {
//        //这2个字段要做特殊的唯一性处理
//        List<String> keysSpecial = Arrays.asList("account", "name");
//
//        for (TUserShowField field : fields) {
//            String key = field.getFieldKey();
//
//            if (keysSpecial.contains(key)) {//如果是特殊处理的就留到特殊时候再处理
//                continue;
//            }
//
//            String value = map.get(key);
//            if (!StringUtil.isEmpty(value)) {//若不为空需要检查唯一性
//                int cnt = 0;
//                if ("mobile".equals(key)) {//手机需要全平台惟一
//                    cnt = userInfoMapper.getCntForUniqueField(key, value, userInfo.getCompanyId(), null, null, userInfo.getAccount());
//                } else {
//                    cnt = userInfoMapper.getCntForUniqueField(key, value, userInfo.getCompanyId(), projectId, taskId, userInfo.getAccount());
//                }
//
//                if (cnt > 0) {
//                    //字段唯一性校验不通过
//                    throw new WrappedException(BizEnums.FieldRepeat);
//                }
//            }
//
//            if (notEmpty && field.getIsmandatory() == 1 && (value == null || value.trim().equals(""))) {//非空校验
//                String fieldName = field.getFieldName();
//                throw new Exception(fieldName + "不能为空");
//            }
//        }
//    }

    public ServiceResponse<DReceiverStatistics> getReceiverStatistics(DReSetPassword dReSetPassword) {
        List<Integer> idList = StringUtil.toIntListByComma(dReSetPassword.getIds());
        DReceiverStatistics dReceiverStatistics = userInfoMapper.getReceiverStatistics(dReSetPassword, idList);
        ServiceResponse<DReceiverStatistics> response = new ServiceResponse<DReceiverStatistics>();
        response.setResponse(dReceiverStatistics);
        return response;
    }

    public ServiceResponse<List<TUserInfo>> getUserList(DReSetPassword dReSetPassword) {
        List<Integer> idList = StringUtil.toIntListByComma(dReSetPassword.getIds());
        List<TUserInfo> userList = userInfoMapper.getUserList(dReSetPassword, idList);
        ServiceResponse<List<TUserInfo>> response = new ServiceResponse<List<TUserInfo>>();
        response.setResponse(userList);
        return response;
    }

    //唯一性校验(账号的唯一性在外面校验)
    public void checkUnique(Integer companyId, Integer projectId, Integer taskId, String account, List<TUserShowField> fields, Map<String, String> map, List<FieldErrTip> errTips) {
        account = StringUtil.isEmpty(account) ? "" : account;

        for (TUserShowField field : fields) {
            String key = field.getFieldKey();
            if (field.getIsunique() == TUserShowFieldIsunique.UNUNIQUE.getValue()
                    || UserUtil.ACCOUNT.equals(key)
                    || key.contains(Config.FIELD_PREFIX))
                continue;//若不是唯一的字段或者是账号字段，则不管

            String value = map.get(key);
            if (!StringUtil.isEmpty(value)) {//若不为空需要检查唯一性
                int cnt = 0;
                if (UserUtil.MOBILE.equals(key)) {//手机需要全平台惟一
                    cnt = userInfoMapper.getCntForUniqueField(key, value, companyId, null, null, account);
                } else {
                    cnt = userInfoMapper.getCntForUniqueField(key, value, companyId, projectId, taskId, account);
                }

                if (cnt > 0) {
                    //字段唯一性校验不通过
                    errTips.add(new FieldErrTip(field, BizEnums.FieldRepeat));
                }
            }
        }
    }

    /**
     * 获取账号
     * @param companyId
     * @param account
     * @return
     */
    public  TUserInfo getByAccount(Integer companyId,String account){
        return  userInfoMapper.getByAccount(companyId,account);
    }

    /**
     * 获取账号
     * @param companyId
     * @param account
     * @return
     */
    public Map<String,Object> getByAccountToMap(Integer companyId,String account){
        return  userInfoMapper.getByAccountToMap(companyId,account);
    }
}
