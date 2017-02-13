package com.talebase.cloud.ms.examer.service;

import com.google.gson.Gson;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.DReSetPassword;
import com.talebase.cloud.base.ms.examer.dto.DUserExamPageRequest;
import com.talebase.cloud.base.ms.examer.dto.DUserShowField;
import com.talebase.cloud.base.ms.examer.dto.DUserShowFieldResponseList;
import com.talebase.cloud.base.ms.examer.enums.*;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.Des3Util;
import com.talebase.cloud.common.util.GsonUtil;
import com.talebase.cloud.common.util.NumberUtil;
import com.talebase.cloud.ms.examer.cfg.Config;
import com.talebase.cloud.ms.examer.dao.UserFieldMapper;
import com.talebase.cloud.ms.examer.dao.UserInfoMapper;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by daorong.li on 2016-12-7.
 */
@Service
public class UserFieldService {
    @Autowired
    private UserFieldMapper userFieldMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoService userInfoService;
    /**
     * 初始化公司全局字段
     * @param companyId
     * @return
     */
    public void iniCompanyUserField(Integer companyId){
        userFieldMapper.iniCompanyUserField(companyId);
    }
    /**
     * 获取全局账号数据列表
     * @param companyId
     * @return
     */
    public ServiceResponse<DUserShowFieldResponseList> getGlobalExamers(Integer companyId){
        //获取公司的全局列表
        List<TUserShowField> list = findGlobalExamers(companyId);
        DUserShowFieldResponseList dUserShowFieldResponseList = new DUserShowFieldResponseList();

        List<Map<String,Object>> optionalList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> requiredList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> uniqueList = new ArrayList<Map<String,Object>>();

        if (list != null){
            for (int i=0;i<list.size();i++){
                TUserShowField tUserShowField = list.get(i);
                //可选字段列表
                Map<String,Object> optional = new HashMap<String,Object>();
                optional.put("fieldKey",tUserShowField.getFieldKey());
                optional.put("fieldName",tUserShowField.getFieldName());
                optional.put("isextension",tUserShowField.getIsextension());
                optional.put("isshow",tUserShowField.getIsshow());
                optional.put("type",tUserShowField.getType());
                optional.put("selectValue",tUserShowField.getSelectValue());
                optionalList.add(optional);
                //加载显示字段
                if (tUserShowField.getIsshow() == TUserShowFieldIsshow.DISPLAY.getValue()){
                    Map<String,Object> required = new HashMap<String,Object>();
                    required.put("fieldKey",tUserShowField.getFieldKey());
                    required.put("fieldName",tUserShowField.getFieldName());
                    required.put("ismandatory",tUserShowField.getIsmandatory());//设置是否必填
                    requiredList.add(required);
                }
                //加载唯一字段
                /*if (tUserShowField.getIsmandatory() == TUserShowFieldIsunique.UNIQUE.getValue()){
                    Map<String,Object> unique = new HashMap<String,Object>();
                    unique.put("fieldKey",tUserShowField.getFieldKey());
                    unique.put("fieldName",tUserShowField.getFieldName());
                    unique.put("isunique",tUserShowField.getIsunique());
                    uniqueList.add(unique);
                }*/
                //账号、手机、邮件默认显示
                if (Config.ACCOUNT_FIELD.equals(tUserShowField.getFieldKey())
                        || Config.MOBILE_FIELD.equals(tUserShowField.getFieldKey())
                        || Config.EMAIL_FIELD.equals(tUserShowField.getFieldKey()) ){
                    Map<String,Object> unique = new HashMap<String,Object>();
                    unique.put("fieldKey",tUserShowField.getFieldKey());
                    unique.put("fieldName",tUserShowField.getFieldName());
                    unique.put("isunique",tUserShowField.getIsunique());
                    uniqueList.add(unique);
                }
            }
        }

        dUserShowFieldResponseList.setOptionalList(optionalList);
        dUserShowFieldResponseList.setRequiredList(requiredList);
        dUserShowFieldResponseList.setUniqueList(uniqueList);

        ServiceResponse<DUserShowFieldResponseList> response = new ServiceResponse<DUserShowFieldResponseList>();
        response.setResponse(dUserShowFieldResponseList);
        return response;
    }

    /**
     * 获取项目下的显示字段
     * @param companyId
     * @param projectId
     * @param taskId
     * @return
     */
    public ServiceResponse<DUserShowFieldResponseList> getProjectExamerShowFields(Integer companyId,Integer projectId,Integer taskId){
        //获取项目下面显示字段
        List<TUserShowField> list = userFieldMapper.findExamers(companyId, projectId, taskId);
        //获取公司的全局字段
        List<TUserShowField> globalList = findGlobalExamers(companyId);

        DUserShowFieldResponseList dUserShowFieldResponseList = new DUserShowFieldResponseList();

        List<Map<String,Object>> optionalList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> requiredList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> uniqueList = new ArrayList<Map<String,Object>>();

        String showKeys = "",
                requiredKey ="",
                uniqueKey="";

        //存在项目显示字段
        if (list != null && list.size() >0){
            for (int i=0;i<list.size();i++){
                TUserShowField tUserShowField = list.get(i);
                if (tUserShowField.getIsshow() == TUserShowFieldIsshow.DISPLAY.getValue()){
                    showKeys +=","+tUserShowField.getFieldKey()+",";
                }
                if (tUserShowField.getIsmandatory() == TUserShowFieldIsmandatory.MANDATORY.getValue()){
                    requiredKey += ","+tUserShowField.getFieldKey() +",";
                }
                if (tUserShowField.getIsunique() == TUserShowFieldIsunique.UNIQUE.getValue()){
                    uniqueKey += ","+tUserShowField.getFieldKey() +",";
                }
            }
        }

        if (globalList != null){
            for (int i=0;i<globalList.size();i++){
                TUserShowField tUserShowField = globalList.get(i);

                String key =","+tUserShowField.getFieldKey()+",";
                //可选字段列表
                Map<String,Object> optional = new HashMap<String,Object>();
                optional.put("fieldKey",tUserShowField.getFieldKey());
                optional.put("fieldName",tUserShowField.getFieldName());
                optional.put("isextension",tUserShowField.getIsextension());
                optional.put("type",tUserShowField.getType());
                optional.put("selectValue",tUserShowField.getSelectValue()==null?"":tUserShowField.getSelectValue());
                //显示字段
                if (showKeys.contains(key)){
                    optional.put("isshow",TUserShowFieldIsshow.DISPLAY.getValue());
                }else{
                    optional.put("isshow",TUserShowFieldIsshow.HIDDEN.getValue());
                }
                optionalList.add(optional);

                //必填字段,账号(account)字段写死为必填
                if (showKeys.contains(key) || Config.ACCOUNT_FIELD.equals(tUserShowField.getFieldKey())){
                    //添加显示字段
                    Map<String,Object> required = new HashMap<String,Object>();
                    required.put("fieldKey",tUserShowField.getFieldKey());
                    required.put("fieldName",tUserShowField.getFieldName());
                    //必填字段
                    if (requiredKey.contains(key) || Config.ACCOUNT_FIELD.equals(tUserShowField.getFieldKey())){
                        required.put("ismandatory",TUserShowFieldIsmandatory.MANDATORY.getValue());//设置必填
                    }else{
                        required.put("ismandatory",TUserShowFieldIsmandatory.UNMANDATORY.getValue());//设置不是必填
                    }
                    requiredList.add(required);
                }

                //账号、手机、邮件默认显示,唯一字段
                if (Config.ACCOUNT_FIELD.equals(tUserShowField.getFieldKey())
                        || Config.MOBILE_FIELD.equals(tUserShowField.getFieldKey())
                        || Config.EMAIL_FIELD.equals(tUserShowField.getFieldKey()) ){

                    Map<String,Object> unique = new HashMap<String,Object>();
                    unique.put("fieldKey",tUserShowField.getFieldKey());
                    unique.put("fieldName",tUserShowField.getFieldName());
                    //邮件是可选唯一字段
                    if (Config.EMAIL_FIELD.equals(tUserShowField.getFieldKey())){
                        if (uniqueKey.contains(key))
                            unique.put("isunique",TUserShowFieldIsunique.UNIQUE.getValue());
                        else
                            unique.put("isunique",TUserShowFieldIsunique.UNUNIQUE.getValue());
                    }else{
                        unique.put("isunique",TUserShowFieldIsunique.UNIQUE.getValue());
                    }
                    uniqueList.add(unique);
                }

            }
        }

        dUserShowFieldResponseList.setOptionalList(optionalList);
        dUserShowFieldResponseList.setRequiredList(requiredList);
        dUserShowFieldResponseList.setUniqueList(uniqueList);

        ServiceResponse<DUserShowFieldResponseList> response = new ServiceResponse<DUserShowFieldResponseList>();
        response.setResponse(dUserShowFieldResponseList);
        return response;
    }

    /**
     * 添加全局可选信息
     * @param dUserShowField
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public ServiceResponse<String>  addExamByOptional(DUserShowField dUserShowField){

        int suffix = 1;

        if (StringUtils.isEmpty(dUserShowField.getFieldName()) || StringUtils.isEmpty(dUserShowField.getFieldName().trim())){
            throw new WrappedException(BizEnums.EXAM_FIELD_ADD_FIELD_NULL);
        }

        if (dUserShowField.getFieldName().length() > Config.FIEL_LENGTH){
            throw new WrappedException(BizEnums.EXAM_FIELD_BEYOUND_LENGTH);
        }

        if (userFieldMapper.beyoundFieldNumber(Config.FIELD_PREFIX,dUserShowField.getCompanyId()) >= Config.FIELD_NUMBER){
            throw new WrappedException(BizEnums.EXAM_FIELD_BEYOUND_NUMBER);
        }

        if (isExistFildByFieldName(dUserShowField.getFieldName(),dUserShowField.getCompanyId())){
            throw new WrappedException(BizEnums.EXAM_FIELD_EXIST);
        }
        //获取最后一个自定义字段
        TUserShowField param= userFieldMapper.getTopOneUserShowFieldByCompanyId(Config.FIELD_PREFIX,dUserShowField.getCompanyId());
        //设置排序排序号
        if (param != null){
            int sortnum =  param.getSortnum() == null? suffix : param.getSortnum();
            //自定义字段后缀自动+1
            ++sortnum;
            dUserShowField.setSortnum(sortnum);
        }else{
            dUserShowField.setSortnum(suffix);
        }

        //获取最后一个自定义字段
        if (param != null && param.getFieldKey().matches("^[0-9]*[1-9][0-9]*$")){
            //判断
            suffix =  Integer.valueOf(param.getFieldKey());
            //自定义字段后缀自动+1
            ++suffix;
        }

        String fild = getSuffix(dUserShowField.getCompanyId(),suffix);

        dUserShowField.setFieldKey(fild);
        dUserShowField.setIsextension(TUserShowFieldIsextension.EXTENSION.getValue());//扩展字段
        dUserShowField.setIsshow(TUserShowFieldIsshow.HIDDEN.getValue());
        dUserShowField.setIsmandatory(TUserShowFieldIsmandatory.UNMANDATORY.getValue());
        dUserShowField.setIsunique(TUserShowFieldIsunique.UNUNIQUE.getValue());

        userFieldMapper.insert(dUserShowField);

        ServiceResponse<String> response = new ServiceResponse<String>();
        response.setResponse(fild);
        return response;
    }

    /**
     * 获取自定义字段的key值
     * 递归获取后缀值，直到没有重复为止
     * @param companyId
     * @param suffix
     * @return
     */
    public String getSuffix(Integer companyId,Integer suffix){
        List<DUserShowField> searchFieldByKey = userFieldMapper.searchFieldByKey(Config.FIELD_PREFIX+suffix,companyId);
        if (searchFieldByKey.size() >0){
            return getSuffix(companyId,++suffix);
        }else {
            return Config.FIELD_PREFIX+suffix;
        }
    }

    /**
     * 判断自定义字段名称是否已存在
     * @param filedName
     * @param companyId
     * @return
     */
    public boolean isExistFildByFieldName(String filedName,Integer companyId){
        List<DUserShowField> list = userFieldMapper.searchFieldByName(filedName,companyId);
        if (list.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * 删除自定义字段
     * @param dUserShowField
     * @return
     */
    @Transactional(readOnly = false)
    public ServiceResponse del(DUserShowField dUserShowField){

        if (StringUtils.isEmpty(dUserShowField.getFieldKey())){
            return new ServiceResponse(BizEnums.EXAM_FIELD_DEL_FIELD_NULL);
        }
        if (StringUtils.isEmpty(dUserShowField.getCompanyId())){
            return new ServiceResponse(BizEnums.EXAM_FIELD_DEL_COMPANY_NULL);
        }

        String fieldKey =  "\""+Config.FIELDKEY+"\""+":"+"\""+dUserShowField.getFieldKey()+"\"";

        Integer total = userFieldMapper.checkExtension(dUserShowField.getCompanyId(), TUserInfoStatus.DELETE.getValue(),fieldKey);
        if (total>0){
            return  new ServiceResponse(BizEnums.EXAM_FIELD_IN_USE);
        }
        userFieldMapper.del(dUserShowField.getFieldKey(),dUserShowField.getCompanyId()) ;
        return new ServiceResponse();
    }

    /**
     * 保存项目账号管理
     * @param dUserShowField
     * @return
     */
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public ServiceResponse saveAll(DUserShowField dUserShowField){
        //删除项目下自定义字段
        userFieldMapper.delProjectField(dUserShowField.getTaskId(),dUserShowField.getProjectId(),dUserShowField.getCompanyId());
        if (!StringUtils.isEmpty(dUserShowField.getShowKey())){
            String showKey[] = dUserShowField.getShowKey().split(",");
            for (int i=0;i<showKey.length;i++){
                DUserShowField dUserShowField1 = userFieldMapper.getProjectExam(showKey[i],dUserShowField.getCompanyId());
                dUserShowField1.setId(null);
                dUserShowField1.setIsmandatory(TUserShowFieldIsmandatory.UNMANDATORY.getValue());
                dUserShowField1.setIsunique(TUserShowFieldIsunique.UNUNIQUE.getValue());
                dUserShowField1.setIsshow(TUserShowFieldIsshow.DISPLAY.getValue());
                dUserShowField1.setTaskId(dUserShowField.getTaskId());
                dUserShowField1.setProjectId(dUserShowField.getProjectId());
                userFieldMapper.insert(dUserShowField1);
            }
        }

        if (!StringUtils.isEmpty(dUserShowField.getRequiredKey())){
            String requiredKey[] = dUserShowField.getRequiredKey().split(",");
            for (int i=0;i<requiredKey.length;i++){
                userFieldMapper.updateProjectExamMandatory(TUserShowFieldIsmandatory.MANDATORY.getValue(),
                        dUserShowField.getTaskId(),
                        dUserShowField.getProjectId(),
                        dUserShowField.getCompanyId(),
                        requiredKey[i]);
            }
        }

        if (!StringUtils.isEmpty(dUserShowField.getUniqueKey())){
            String uniqueKey[] = dUserShowField.getUniqueKey().split(",");
            for (int i=0;i<uniqueKey.length;i++){
                userFieldMapper.updateProjectExamUnique(TUserShowFieldIsunique.UNIQUE.getValue(),
                        dUserShowField.getTaskId(),
                        dUserShowField.getProjectId(),
                        dUserShowField.getCompanyId(),
                        uniqueKey[i]);
            }
        }
        return new ServiceResponse();
    }

    /**
     * 保存全局账号
     * @param dUserShowField
     * @return
     */
    @Transactional(readOnly = false ,rollbackFor = Exception.class)
    public ServiceResponse saveGlobalAll(DUserShowField dUserShowField){
        if (StringUtils.isEmpty(dUserShowField.getCompanyId())){
            throw new WrappedException(BizEnums.EXAM_FIELD_DEL_COMPANY_NULL);
        }
        //将全局账号设置
        userFieldMapper.updateByGlobalKeyStatus(TUserShowFieldIsshow.HIDDEN.getValue(),
                TUserShowFieldIsmandatory.UNMANDATORY.getValue(),
                TUserShowFieldIsunique.UNUNIQUE.getValue(),
                dUserShowField.getCompanyId());

        //更新显示字段
        if (!StringUtils.isEmpty(dUserShowField.getShowKey())){
            String[] showKey = dUserShowField.getShowKey().split(",");
            for (int i=0;i<showKey.length;i++){
                DUserShowField showField = new DUserShowField();
                showField.setCompanyId(dUserShowField.getCompanyId());
                showField.setModifier(dUserShowField.getModifier());
                showField.setFieldKey(showKey[i].trim());
                showField.setIsshow(TUserShowFieldIsshow.DISPLAY.getValue());
                userFieldMapper.updateByGlobalKey(showField);
            }
        }

        //更新必填字段
        if (!StringUtils.isEmpty(dUserShowField.getRequiredKey())){
            String[] requiredKey = dUserShowField.getRequiredKey().split(",");
            for (int i=0;i<requiredKey.length;i++){
                DUserShowField required = new DUserShowField();
                required.setCompanyId(dUserShowField.getCompanyId());
                required.setModifier(dUserShowField.getModifier());
                required.setFieldKey(requiredKey[i].trim());
                required.setIsmandatory(TUserShowFieldIsmandatory.MANDATORY.getValue());
                userFieldMapper.updateByGlobalKey(required);
            }
        }

        //更新唯一字段
        if (!StringUtils.isEmpty(dUserShowField.getUniqueKey())){
            String[] uniqueKey = dUserShowField.getUniqueKey().split(",");
            for (int i=0;i<uniqueKey.length;i++){
                DUserShowField required = new DUserShowField();
                required.setCompanyId(dUserShowField.getCompanyId());
                required.setModifier(dUserShowField.getModifier());
                required.setFieldKey(uniqueKey[i].trim());
                required.setIsunique(TUserShowFieldIsunique.UNIQUE.getValue());
                userFieldMapper.updateByGlobalKey(required);
            }
        }

        return new ServiceResponse();
    }

    public List<TUserShowField> findUserFields(Integer companyId, Integer projectId, Integer taskId){
        List<TUserShowField> fields = userFieldMapper.findExamers(companyId, projectId, taskId);
        if(fields.size() == 0 && (projectId != null || taskId != null)){
            fields = userFieldMapper.findExamers(companyId, null, null);
        }

        return fields;
    }

    private List<TUserShowField> findGlobalExamers(Integer companyId){
        return userFieldMapper.findExamers(companyId, null, null);
    }

    public PageResponse getProjectExam(ServiceRequest<DUserExamPageRequest> req)throws Exception{
        DUserExamPageRequest data = req.getRequest();
        Integer start = req.getPageReq().getStart();
        Integer limit = req.getPageReq().getLimit();

        Integer total = userFieldMapper.getCountProectExamers(data);
        List<Map<String,Object>> list = userFieldMapper.getProjectExamers(data,start,limit);
        List<Map<String,Object>> resultList = matchField(list,data.getCompanyId(),data.getProjectId(),data.getTaskId(),true);
        PageResponse page = new PageResponse();
        page.setTotal(total);
        page.setResults(resultList);

        return page;
    }

    /**
     * 获取显示字段列表
     * @param dataList
     * @param companyId
     * @param projectId
     * @param taskId
     * @param addSendStatus
     * @return
     */
    public List<Map<String,Object>> matchField(List<Map<String,Object>> dataList,
                                               Integer companyId,
                                               Integer projectId,
                                               Integer taskId,
                                               boolean addSendStatus) throws Exception{

        List<Map<String,Object>> resultLit = new ArrayList<Map<String,Object>>();

        if (dataList == null || dataList.size() <=0)
            return  resultLit;

        List<TUserShowField> list = getField(companyId,projectId,taskId);
        //没有要显示的字段
        if (list.size()<=0){
            return resultLit;
        }

        for (int i=0 ;i<dataList.size();i++){
            Map<String,Object> map = dataList.get(i);
            Map<String,Object> resultMap = new HashMap<String,Object>();
            List<Map<String,Object>> userInfos = match(map,list);
            resultMap.put("userInfos",userInfos);
            //是否加载发送状态
            if (addSendStatus){
                resultMap.put("sendEmailStatus",map.get("send_email_status"));
                resultMap.put("sendSmsStatus",map.get("send_sms_status"));
                resultMap.put("createTime",map.get("create_time"));
                if (map.get("exam_start_time") == null)
                    resultMap.put("answerStatus",DUserExamPageRequest.NOBEGIN);
                else if (map.get("exam_start_time") != null && map.get("exam_finished_time") == null )
                    resultMap.put("answerStatus",DUserExamPageRequest.ANSWER);
                else if (map.get("exam_finished_time") != null){
                    resultMap.put("answerStatus",DUserExamPageRequest.COMPLETE);
                }else{
                    resultMap.put("answerStatus",DUserExamPageRequest.NOBEGIN);
                }

            }
            //用户id
            resultMap.put("id",map.get("user_id"));
            resultLit.add(resultMap);
        }
        return resultLit;
    }


    /**
     * 匹配字段
     * @param map
     * @param list
     * @return
     */
    public List<Map<String,Object>> match(Map<String,Object> map, List<TUserShowField> list)throws Exception{
        List<Map<String,Object>> fieldList = new ArrayList<Map<String,Object>>();
        //已经存在字段
        Map<String,Object> existExtension = new HashMap<String,Object>();
        //重新封装需要显示的字段
        for (int i=0;i<list.size();i++){
            //加载字段map
            Map<String,Object> fieldMap = new HashMap<String ,Object>();
            TUserShowField tUserShowField = list.get(i);
            //匹配系统内置字段
            if (map.get(tUserShowField.getFieldKey()) != null &&
                    tUserShowField.getIsshow() == TUserShowFieldIsshow.DISPLAY.getValue()){

                fieldMap.put("fieldKey",tUserShowField.getFieldKey());
                fieldMap.put("fieldName",tUserShowField.getFieldName());
                fieldMap.put("type",tUserShowField.getType());
                //解密
                if(Config.PSSOWR_FIELD.equals(tUserShowField.getFieldKey())){
                    try{
                        String password =   Des3Util.des3DecodeCBC((String) map.get(tUserShowField.getFieldKey()));
                        fieldMap.put("fieldValue",password);
                    }catch (Exception e){
                        fieldMap.put("fieldValue",(String) map.get(tUserShowField.getFieldKey()));
                    }
                }else{
                    fieldMap.put("fieldValue",map.get(tUserShowField.getFieldKey()));
                }
                fieldMap.put("isexTension",TUserShowFieldIsextension.UNEXTENSION.getValue());
                fieldList.add(fieldMap);
                existExtension.put(tUserShowField.getFieldKey(),tUserShowField.getFieldKey());
            }

        }

        //匹配扩展字段
        String extensions = (String) map.get("extension_field");
        //匹配扩展字段(有对应测试用户)
        if (!StringUtils.isEmpty(extensions)){
            for (int i=0;i<list.size();i++){
                TUserShowField tUserShowField = list.get(i);
                //扩展字段才匹配
                if (tUserShowField.getIsextension()
                        != TUserShowFieldIsextension.EXTENSION.getValue())
                    continue;
                //将扩展字段转json匹配
                List<Map<String,Object>> extensionList = GsonUtil.fromJson(extensions,new ArrayList<Map<String,Object>>().getClass());
                //匹配扩展字段
                if (extensionList != null && extensionList.size()>0){
                    for (int j=0;j<extensionList.size();j++){
                        Map<String,Object> extensionMap =  extensionList.get(j);
                        //加载显示的扩展字段
                        if (extensionMap.get("fieldKey").equals(tUserShowField.getFieldKey())){
                            extensionMap.put("isexTension",TUserShowFieldIsextension.EXTENSION.getValue());
                            extensionMap.put("type",tUserShowField.getType());
                            fieldList.add(extensionMap);
                            existExtension.put(tUserShowField.getFieldKey(),tUserShowField.getFieldKey());
                        }
                    }
                }
            }
        }

        //匹配扩展字段（没有毒药测试用户，添加完用户后再添加的扩展字段）
        if (list != null && list.size() > 0){
            for (TUserShowField tUserShowField : list){
                if (tUserShowField.getIsshow() == TUserShowFieldIsshow.DISPLAY.getValue() &&
                        existExtension.get(tUserShowField.getFieldKey()) == null){
                    //加载字段map
                    Map<String,Object> fieldMap = new HashMap<String ,Object>();
                    fieldMap.put("fieldKey",tUserShowField.getFieldKey());
                    fieldMap.put("fieldName",tUserShowField.getFieldName());
                    fieldMap.put("type",tUserShowField.getType());
                    fieldMap.put("fieldValue","");
                    fieldMap.put("isexTension",tUserShowField.getIsextension());
                    fieldList.add(fieldMap);
                }
            }
        }


        return fieldList;
    }

    /**
     * 获取需要显示的字段
     * @param companyId
     * @param projectId
     * @param taskId
     * @return
     */
    public List<TUserShowField> getField(Integer companyId,Integer projectId,Integer taskId){
        List<TUserShowField> resultList = new ArrayList<TUserShowField>();
        //找出任务下的显示字段
        List<TUserShowField> list =  userFieldMapper.findExamers(companyId, projectId, taskId);
        //任务下没有设置显示字段,则查找公共的设置字段
        if (list == null || list.size()<=0){
            list =  userFieldMapper.findExamers(companyId, null, null);
        }
        //找出显示字段
        if (list != null){
            for (int i=0;i<list.size();i++){
                TUserShowField tUserShowField = list.get(i);
                //加载列表显示字段
                if (tUserShowField.getIsshow() == TUserShowFieldIsshow.DISPLAY.getValue()){
                    resultList.add(tUserShowField);
                }
            }
        }
        return resultList;
    }

    /**
     * 获取新增时显示填空字段
     * @param request
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getField(ServiceRequest<DReSetPassword> request)throws Exception{

        DReSetPassword dUserExamPageRequest = request.getRequest();
        ServiceHeader serviceHeader = request.getRequestHeader();

        List<TUserShowField> fields = getField(dUserExamPageRequest.getCompanyId(),
                dUserExamPageRequest.getProjectId(),
                dUserExamPageRequest.getTaskId());

        List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();

        Map<String,Object> tUserInfo = null;
        //获取修改字段
        if (!StringUtils.isEmpty(dUserExamPageRequest.getUserId()) || !StringUtils.isEmpty(dUserExamPageRequest.getAccount())){
            if (!StringUtils.isEmpty(dUserExamPageRequest.getUserId())){
                tUserInfo = userInfoMapper.get(dUserExamPageRequest.getUserId());
            }else if (!StringUtils.isEmpty(dUserExamPageRequest.getAccount())){
                tUserInfo = userInfoService.getByAccountToMap(serviceHeader.getCompanyId(),dUserExamPageRequest.getAccount());
            }
            if (tUserInfo !=null){
                String extensions = (String) tUserInfo.get("extension_field");
                //将扩展字段转json匹配
                List<Map<String,Object>> extensionList = GsonUtil.fromJson(extensions,new ArrayList<Map<String,Object>>().getClass());
                //匹配扩展字段
                if (extensionList != null && extensionList.size()>0){
                    for (int j=0;j<extensionList.size();j++){
                        Map<String,Object> extensionMap =  extensionList.get(j);
                        //加载显示的扩展字段
                        tUserInfo.put((String) extensionMap.get("fieldKey"),extensionMap.get("fieldValue"));
                    }
                }
            }

        }

       /* if (tUserInfo == null){
            throw  new WrappedException(BizEnums.EXAM_FIELD_DEL_NO_ACCOUNT);
        }*/

        if (fields != null && fields.size()>0){
            for (TUserShowField tUserShowField : fields){
                //加载显示字段
                if (tUserShowField.getIsshow() == TUserShowFieldIsshow.DISPLAY.getValue()){
                    Map<String,Object> fieldMap = new HashMap<String,Object>();
                    fieldMap.put("fieldKey",tUserShowField.getFieldKey());
                    fieldMap.put("fieldName",tUserShowField.getFieldName());
                    //账号字段默认必填
                    if (Config.ACCOUNT_FIELD.equals(tUserShowField.getFieldKey())){
                        fieldMap.put("ismandatory",TUserShowFieldIsmandatory.MANDATORY.getValue());
                    }else if (!NumberUtil.isEmpty(serviceHeader.getOperatorId())){//管理员登陆只有账号必填，其他不必填
                        fieldMap.put("ismandatory",TUserShowFieldIsmandatory.UNMANDATORY.getValue());
                    }else {//考生登录的按数据库必填显示
                        fieldMap.put("ismandatory",tUserShowField.getIsmandatory());
                    }

                    fieldMap.put("isunique",tUserShowField.getIsunique());
                    fieldMap.put("type",tUserShowField.getType());

                    if (tUserInfo != null){
                        if (Config.PSSOWR_FIELD.equals(tUserShowField.getFieldKey())){
                            try{
                                fieldMap.put("fieldValue",Des3Util.des3DecodeCBC(userInfoService.toValue(tUserInfo.get(tUserShowField.getFieldKey()))));
                            }catch (Exception e){
                                fieldMap.put("fieldValue",(userInfoService.toValue(tUserInfo.get(tUserShowField.getFieldKey()))));
                            }
                        }else{
                            fieldMap.put("fieldValue",userInfoService.toValue(tUserInfo.get(tUserShowField.getFieldKey())));
                        }
                    }
                    List<Map<String,String>> list = new ArrayList<Map<String,String>>();
                    if (!StringUtils.isEmpty(tUserShowField.getSelectValue())){
                        String attr[] = tUserShowField.getSelectValue().split(",");
                        for(String s : attr){
                            Map<String,String> valueMap = new HashMap<String,String>();
                            valueMap.put("name",s);
                            list.add(valueMap);
                        }
                    }
                    fieldMap.put("select",list);
                    results.add(fieldMap);
                }
            }
        }

        return results;
    }

    public List<Map<String,Object>> getProjectExamByIds(List<Integer> ids, ServiceHeader serviceHeader, Integer projectId, Integer taskId)throws Exception{
        List<Map<String,Object>> list = userFieldMapper.getAllProjectExamersByMap(new DUserExamPageRequest(), ids);
        List<Map<String,Object>> resultList = matchField(list, serviceHeader.getCompanyId(), projectId, taskId ,false);
        return resultList;
    }


}
