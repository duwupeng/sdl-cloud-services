package com.talebase.cloud.ms.examer.service;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.domain.*;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecord;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecordQueryReq;
import com.talebase.cloud.base.ms.examer.enums.TUserExamStatus;
import com.talebase.cloud.base.ms.examer.enums.TUserExamType;
import com.talebase.cloud.base.ms.examer.enums.TUserImportLogStatus;
import com.talebase.cloud.base.ms.examer.enums.TUserImportRecordStatus;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.Des3Util;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.PasswordUtil;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.examer.dao.UserExamMapper;
import com.talebase.cloud.ms.examer.dao.UserImportLogMapper;
import com.talebase.cloud.ms.examer.dao.UserImportRecordMapper;
import com.talebase.cloud.ms.examer.dao.UserInfoMapper;
import com.talebase.cloud.ms.examer.util.CUserInfo;
import com.talebase.cloud.ms.examer.util.DFieldInx;
import com.talebase.cloud.ms.examer.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@Service
public class UserImportLogService {

    @Autowired
    private UserImportLogMapper userImportLogMapper;
    @Autowired
    private UserImportRecordMapper userImportRecordMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserExamMapper userExamMapper;
    @Autowired
    private UserInfoService userInfoService;


    public PageResponse<DUserImportRecord> query(DUserImportRecordQueryReq queryReq, PageRequest pageReq, ServiceHeader serviceHeader){

        PageResponse<DUserImportRecord> pageResponse = new PageResponse<DUserImportRecord>(pageReq);
        int total = userImportRecordMapper.queryCnt(queryReq, serviceHeader);

        pageResponse.setTotal(total);
        if(total > 0){
            List<DUserImportRecord> records = userImportRecordMapper.query(queryReq, serviceHeader, pageReq);
            pageResponse.setResults(records);
        }

        return pageResponse;
    }

    public TUserImportLog getLastBatchNo(String account){
        return userImportLogMapper.getLastLog(account);
    }

    public TUserImportLog getByBatchNo(String batchNo){
        return userImportLogMapper.getByBatchNo(batchNo);
    }

    public List<TUserImportRecord> findByBatchNo(String batchNo){
        return userImportRecordMapper.findFailRecords(batchNo);
    }

    public Integer createLog(TUserImportLog log){
        return userImportLogMapper.insert(log);
    }

    public Integer saveFailImportRecord(TUserImportRecord record){
        userImportRecordMapper.insert(record);
        return record.getId();
    }

    public Integer endImportLog(TUserImportLog log){
        return userImportLogMapper.updateToEnd(log);
    }

    /**
     * 仍需要对进来的数据进行数据库相关的检测，例如业务上定义的惟一性;
     * 若保存对象成功，返回值>0；若判断出其他错误，返回值为0
     * @param record
     * @return
     */
    public Integer saveImportUser(TUserInfo userInfo, TUserImportRecord record, Integer projectId, Integer taskId, List<DFieldInx> uniqueFields, List<String> fieldStrs) throws Exception {

        //这2个字段要做特殊的唯一性处理
        List<String> keysSpecial = Arrays.asList("account", "name");

        for(DFieldInx dFieldInx : uniqueFields){
            if(keysSpecial.contains(dFieldInx.getField().getFieldKey())){//如果是特殊处理的就留到特殊时候再处理
                continue;
            }

            String value = fieldStrs.get(dFieldInx.getIdx());
            if(!StringUtil.isEmpty(value)){//若不为空需要检查唯一性
                int cnt = 0;
                if("mobile".equals(dFieldInx.getField().getFieldKey())){//手机需要全平台惟一
                    cnt = userInfoMapper.getCntForUniqueField(dFieldInx.getField().getFieldKey(), value, userInfo.getCompanyId(), null, null, userInfo.getAccount());
                }else{
                    cnt = userInfoMapper.getCntForUniqueField(dFieldInx.getField().getFieldKey(), value, userInfo.getCompanyId(), projectId, taskId, userInfo.getAccount());
                }

                if(cnt > 0){
                    //字段唯一性校验不通过
                    record.setStatus(TUserImportRecordStatus.FAIL.getValue());
                    record.setRemark(dFieldInx.getField().getFieldName() + "字段存在重复数据");
                }
            }
        }

        if(record.getStatus() == null || record.getStatus() == TUserImportRecordStatus.SUCCESS.getValue()){//检验没问题的继续进行特殊的唯一性校验
            TUserInfo oriUserInfo = userInfoMapper.getUserByAccount(userInfo.getAccount(), userInfo.getCompanyId());
            if(oriUserInfo != null){//已有账号，需要判断是重复还是需要更新
                if(!StringUtil.isEmpty(userInfo.getName()) && !StringUtil.isEmpty(oriUserInfo.getName())){
                    if(!userInfo.getName().equals(oriUserInfo.getName())){//同账号不同名判断重复账号
                        record.setStatus(TUserImportRecordStatus.FAIL.getValue());
                        record.setRemark("账号重复");
                    }
                }else if(!StringUtil.isEmpty(userInfo.getName())){//若传入的姓名为空字符串，则置姓名为null保证不更新姓名
                    userInfo.setName(null);
                }

                if(!StringUtil.isEmpty(userInfo.getMobile()) && !StringUtil.isEmpty(userInfo.getMobile())){
                    if(!userInfo.getMobile().equals(oriUserInfo.getMobile())){//同账号不同手机判断重复账号
                        record.setStatus(TUserImportRecordStatus.FAIL.getValue());
                        record.setRemark("账号重复");
                    }
                }else if(!StringUtil.isEmpty(userInfo.getMobile())){//若传入的手机号码为空字符串，则置手机号码为null保证不更新手机号码
                    userInfo.setMobile(null);
                }

                if(record.getStatus() == null || record.getStatus() == TUserImportRecordStatus.SUCCESS.getValue()){//重复检验通过，走更新接口
                    userInfo.setId(oriUserInfo.getId());
                    record.setRemark("账号" + userInfo.getAccount() + "更新成功");
                    record.setStatus(TUserImportRecordStatus.SUCCESS.getValue());

                    userInfoMapper.update(userInfo);
                }
            }else{
                if(StringUtil.isEmpty(userInfo.getName()))//姓名字段为空时自动使用账号字段填充
                    userInfo.setName(userInfo.getAccount());

                if(StringUtil.isEmpty(userInfo.getPassword()))//创建时密码为空则自动补充随机密码
                    userInfo.setPassword(PasswordUtil.getRandomString(PasswordUtil.SIX_BITS));
                userInfo.setPassword(Des3Util.des3EncodeCBC(userInfo.getPassword()));

                userInfoMapper.insert(userInfo);
                record.setRemark("账号" + userInfo.getAccount() + "导入成功");
                record.setStatus(TUserImportRecordStatus.SUCCESS.getValue());
            }
        }

        int respCode = 0;//0表示处理失败；1表示处理成功但不需要跨库添加学生任务的冗余表数据;2表示处理成功并要跨库处理学生任务的冗余表数据

        if(record.getStatus() == TUserImportRecordStatus.SUCCESS.getValue()){//成功的要判断是否插用户考试关系表
            if(projectId != null && taskId != null){
                int cnt = userExamMapper.getCntForExamAndUser(userInfo.getId(), projectId, taskId);
                if(cnt == 0) {//原有的就不会插一条新的
                    TUserExam userExam = new TUserExam();
                    userExam.setCompanyId(userInfo.getCompanyId());
                    userExam.setStatus(TUserExamStatus.ENABLED.getValue());
                    userExam.setCreater(userInfo.getCreater());
                    userExam.setProjectId(projectId);
                    userExam.setTaskId(taskId);
                    userExam.setType(TUserExamType.EXAM.getValue());
                    userExam.setUserId(userInfo.getId());
                    userExamMapper.insert(userExam);
                    respCode = 2;
                }else
                    respCode = 1;
            }else{
                respCode = 1;
            }
        }else{//失败的要保存明细的json用于导出
            record.setDetailJson(GsonUtil.toJson(fieldStrs));
            respCode = 0;
        }
        userImportRecordMapper.insert(record);
        return respCode;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveImport(List<CUserInfo> cUserInfos, Integer projectId, Integer taskId, List<String> headerNames, List<TUserShowField> fields, List<Map<String, String>> mapList, List<List<String>> fieldStrs, ServiceHeader header) throws Exception {
        //插入日志汇总记录
        String batchNo = System.currentTimeMillis() + "";
        TUserImportLog log = new TUserImportLog();
        log.setBatchNo(batchNo);
        log.setCompanyId(header.getCompanyId());
        log.setCreater(header.getOperatorName());
        log.setProjectId(projectId);
        log.setTaskId(taskId);
        log.setTitleJson(GsonUtil.toJson(headerNames));
        createLog(log);

        List<String> succAccs = new ArrayList<>();
        List<String> failAccs = new ArrayList<>();

        for(int i = 0; i < cUserInfos.size(); i++){
            CUserInfo c = cUserInfos.get(i);
            TUserInfo curUser = c.getUserInfo();
            curUser.setCompanyId(header.getCompanyId());
            curUser.setCreater(header.getOperatorName());
            TUserImportRecord record = new TUserImportRecord();
            Map<String, String> map = mapList.get(i);
            record.setBatchNo(log.getBatchNo());
            if(!StringUtil.isEmpty(curUser.getAccount())){
                record.setExaminee(curUser.getAccount());
                userInfoService.checkUnique(header.getCompanyId(), projectId, taskId, curUser.getAccount(), fields, map, c.getErrTips());
            }

            TUserInfo oriUser = userInfoMapper.getUserByAccount(curUser.getAccount(), header.getCompanyId());

            if(oriUser != null){//更新
                curUser.setPassword(null);//导入账号不会修改到密码

                if(!StringUtil.isEmpty(curUser.getMobile()) && !StringUtil.isEmpty(oriUser.getMobile()) && !curUser.getMobile().equals(oriUser.getMobile())){
                    c.addErrTip(BizEnums.AccountImportRepeatErr, UserUtil.MOBILE, "手机号码");
                }

                if(!StringUtil.isEmpty(curUser.getName()) && !StringUtil.isEmpty(oriUser.getName()) && !curUser.getName().equals(oriUser.getName())){
                    c.addErrTip(BizEnums.AccountImportRepeatErr, UserUtil.NAME, "姓名");
                }

                if(!c.getErrTips().isEmpty()){
                    record.setStatus(TUserImportRecordStatus.FAIL.getValue());
                    record.setDetailJson(GsonUtil.toJson(fieldStrs.get(i)));
                    record.setRemark(c.getErrMsg());
                }else{
                    curUser.setId(oriUser.getId());
                    userInfoMapper.update(curUser);
                    if (projectId != null && taskId != null) {
                        int cnt = userExamMapper.getCntForExamAndUser(curUser.getId(), projectId, taskId);
                        if (cnt == 0)
                            userInfoService.saveUserExam(header, curUser, projectId, taskId);
                    }

                    record.setStatus(TUserImportRecordStatus.SUCCESS.getValue());
                    record.setRemark(curUser.getAccount() + "更新成功");
                }
            }else{//创建
                if(!c.getErrTips().isEmpty()){
                    record.setStatus(TUserImportRecordStatus.FAIL.getValue());
                    record.setDetailJson(GsonUtil.toJson(fieldStrs.get(i)));
                    record.setRemark(c.getErrMsg());
                }else{
                    if (StringUtil.isEmpty(curUser.getName()))//姓名字段为空时自动使用账号字段填充
                        curUser.setName(curUser.getAccount());

                    if (StringUtil.isEmpty(curUser.getPassword()))//创建时密码为空则自动补充随机密码
                        curUser.setPassword(PasswordUtil.getRandomString(PasswordUtil.SIX_BITS));
                    curUser.setPassword(Des3Util.des3EncodeCBC(curUser.getPassword()));

                    curUser.setId(null);//保证没有传id

                    userInfoMapper.insert(curUser);
                    if (projectId != null && taskId != null) {
                        userInfoService.saveUserExam(header, curUser, projectId, taskId);
                    }

                    record.setStatus(TUserImportRecordStatus.SUCCESS.getValue());
                    record.setRemark(curUser.getAccount() + "创建成功");
                }
            }
            if(record.getStatus() == TUserImportRecordStatus.SUCCESS.getValue()){
                succAccs.add(curUser.getAccount() == null ? "" : curUser.getAccount());
            }else{
                failAccs.add(curUser.getAccount() == null ? "" : curUser.getAccount());
            }

            userImportRecordMapper.insert(record);
        }

        log.setStatus(TUserImportLogStatus._DONE.getValue());
        log.setSuccNum(succAccs.size());
        log.setFailNum(failAccs.size());
        endImportLog(log);
    }

}
