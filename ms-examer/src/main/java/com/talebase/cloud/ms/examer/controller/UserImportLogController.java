package com.talebase.cloud.ms.examer.controller;

import com.talebase.cloud.base.ms.examer.domain.TUserImportLog;
import com.talebase.cloud.base.ms.examer.domain.TUserImportRecord;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.DImportReq;
import com.talebase.cloud.base.ms.examer.dto.DTUserImportLogEx;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecord;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecordQueryReq;
import com.talebase.cloud.base.ms.examer.enums.*;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.*;
import com.talebase.cloud.ms.examer.service.UserFieldService;
import com.talebase.cloud.ms.examer.service.UserImportLogService;
import com.talebase.cloud.ms.examer.util.CUserInfo;
import com.talebase.cloud.ms.examer.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@RestController
public class UserImportLogController {

    @Autowired
    private UserImportLogService userImportLogService;
    @Autowired
    private UserFieldService userFieldService;

    @PostMapping("/examer/importLog/query")
    public ServiceResponse<PageResponse<DUserImportRecord>> query(@RequestBody ServiceRequest<DUserImportRecordQueryReq> serviceRequest) {
        PageResponse<DUserImportRecord> pageResponse = userImportLogService.query(serviceRequest.getRequest(), serviceRequest.getPageReq(), serviceRequest.getRequestHeader());
        for (DUserImportRecord record : pageResponse.getResults()) {
            record.setCreatedDateL(record.getCreatedDate().getTime());
        }
        return new ServiceResponse(pageResponse);
    }

    @PostMapping("/examer/failLog/export")
    public ServiceResponse<DTUserImportLogEx> export(@RequestBody ServiceRequest<String> serviceRequest) throws InvocationTargetException, IllegalAccessException {
        String batchNo = serviceRequest.getRequest();
        TUserImportLog log = batchNo == null ? userImportLogService.getLastBatchNo(serviceRequest.getRequestHeader().getOperatorName()) : userImportLogService.getByBatchNo(batchNo);

        if (log == null)
            throw new WrappedException(BizEnums.ImportLogNotFound);

        List<TUserImportRecord> records = userImportLogService.findByBatchNo(log.getBatchNo());

        DTUserImportLogEx ex = new DTUserImportLogEx();
        BeanConverter.copyProperties(ex, log);

        ex.setFailRecords(records);
        return new ServiceResponse(ex);
    }

    @PostMapping("/examer/import")
    public ServiceResponse<List<String>> importData(@RequestBody ServiceRequest<DImportReq> serviceRequest) throws Exception {

        DImportReq req = serviceRequest.getRequest();

        //准备匹配
        List<String> headerNames = req.getHeader();
        List<TUserShowField> fs = userFieldService.findUserFields(serviceRequest.getRequestHeader().getCompanyId(), req.getProjectId(), req.getTaskId());

        List<String> headerKeys = new ArrayList<>();

        Map<String, Integer> headerdMap = new HashMap<>();

        Set<String> accountSet = new HashSet<>();
        Set<String> conflictAccountSet = new HashSet<>();

        for (TUserShowField field : fs) {
            if (field.getIsshow() == TUserShowFieldIsshow.HIDDEN.getValue())
                continue;

            String key = field.getFieldKey();
            String name = field.getFieldName();

            for (int i = 0; i < headerNames.size(); i++) {
                if (name.equals(headerNames.get(i))) {
                    headerdMap.put(key, i);
                    break;
                }
            }
        }

        //定义出key，value，并找出存在冲突的账号
        List<Map<String, String>> fieldMaps = new ArrayList<>();
        List<CUserInfo> cUserInfos = new ArrayList<>();
        for (List<String> fieldStrs : serviceRequest.getRequest().getDetails()) {
            Map<String, String> fieldMap = new HashMap<>();
            for (Iterator<String> iterator = headerdMap.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                Integer idx = headerdMap.get(key);
                String value = "";
                if(idx < fieldStrs.size()){
                    value = fieldStrs.get(idx);
                }
                fieldMap.put(key, value);
            }
            String account = fieldMap.get(UserUtil.ACCOUNT);
            fieldMaps.add(fieldMap);

            CUserInfo cUserInfo = UserUtil.toUserInfo(fieldMap, fs, true);
            cUserInfos.add(cUserInfo);

            if (!StringUtil.isEmpty(account)) {
                if (!accountSet.contains(account)) {
                    accountSet.add(account);
                } else {
                    conflictAccountSet.add(account);
                }
            }
        }

//        if (!conflictAccountSet.isEmpty()) {
//            for (CUserInfo cUserInfo : cUserInfos) {
//                String account = cUserInfo.getUserInfo().getAccount();
//                if (!StringUtil.isEmpty(account) && conflictAccountSet.contains(account))
//                    cUserInfo.addErrTip(BizEnums.AccountConflictErr, UserUtil.ACCOUNT, "账号");
//            }
//        }

        //判断xls文件中账号冲突
        //以账号相同为前提
        //若姓名、手机号码相等的情况下，则为一致，都会处理成功；否则为重复冲突
        if (!conflictAccountSet.isEmpty()) {
            //先把存在冲突可能的账号分离出来
            Map<String, List<CUserInfo>> conflictUserMaps = new HashMap<>();
            for (CUserInfo cUserInfo : cUserInfos) {
                String account = cUserInfo.getUserInfo().getAccount();
                if (!StringUtil.isEmpty(account) && conflictAccountSet.contains(account)){
                    List<CUserInfo> conflictUsers = conflictUserMaps.get(account);
                    if(conflictUsers == null){
                        conflictUsers = new ArrayList<>();
                        conflictUserMaps.put(account, conflictUsers);
                    }
                    conflictUsers.add(cUserInfo);
                }
            }

            //每个账号里面判断是否存在冲突
            for(Iterator<String> iterator = conflictUserMaps.keySet().iterator();iterator.hasNext();){
                String account = iterator.next();
                List<CUserInfo> userInfos = conflictUserMaps.get(account);

                boolean conflictErrFlag = false;

                if(userInfos.size() <= 1){
                    continue;
                }

                CUserInfo userInfo1st = userInfos.get(0);
                for(int i = 1; i < userInfos.size(); i++){
                    CUserInfo userInfoTo = userInfos.get(i);
                    conflictErrFlag = !isEqual(userInfo1st.getUserInfo().getName(), userInfoTo.getUserInfo().getName()) ||
                            !isEqual(userInfo1st.getUserInfo().getMobile(), userInfoTo.getUserInfo().getMobile());
                    if(conflictErrFlag)
                        break;
                }

                if(conflictErrFlag){
                    for(CUserInfo cUserInfo : userInfos){
                        cUserInfo.addErrTip(BizEnums.AccountConflictErr, UserUtil.ACCOUNT, "账号");
                    }
                }
            }

        }


        userImportLogService.saveImport(cUserInfos, req.getProjectId(), req.getTaskId(), req.getHeader(),
                fs, fieldMaps, req.getDetails(), serviceRequest.getRequestHeader());

        return new ServiceResponse();
    }

    private boolean isEqual(String str1, String str2){
        if(StringUtil.isEmpty(str1) && StringUtil.isEmpty(str2))//两个都为空，相等
            return true;

        if(!StringUtil.isEmpty(str1) && !StringUtil.isEmpty(str2) && str1.equals(str2))
            return true;

        return false;
    }

}
