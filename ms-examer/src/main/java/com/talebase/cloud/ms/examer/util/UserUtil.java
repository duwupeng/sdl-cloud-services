package com.talebase.cloud.ms.examer.util;

import com.talebase.cloud.base.ms.examer.domain.TUserImportRecord;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldIsextension;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldIsmandatory;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldIsshow;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldType;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.util.*;
import io.undertow.security.idm.Account;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kanghong.zhao on 2016-12-13.
 */
public class UserUtil {

    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String IDENTITYNUM = "identity_num";
    public static final String WORKYEARS = "work_years";
    public static final String HIGHESTEDUCATION = "highest_education";
    public static final String BIRTHDAY = "birthday";
    public static final String NAME = "name";
    public static final String INDUSTRYNAME = "industry_name";
    public static final String POLITICALSTATUS = "political_status";
    public static final String DEPTID = "dept_id";
    public static final String DEPTNAME = "dept_name";

    private static final List<String> needCamels = Arrays.asList(IDENTITYNUM, WORKYEARS, HIGHESTEDUCATION, INDUSTRYNAME, POLITICALSTATUS, DEPTID, DEPTNAME);


    //导入字段转化成保存的考生对象
    //仅检验数据格式问题
//    public static TUserInfo parseToUserInfo(TUserImportRecord record, List<String> fieldStrs, List<DFieldInx> fields) throws Exception {
//        Map<String, Object> oriFieldMap = new HashMap<>();//系统字段
//        List<DField> exFields = new ArrayList<>();//扩展字段
//
//        //遍历赋值
//        for(DFieldInx fIdx : fields){
//            String value = fieldStrs.get(fIdx.getIdx());
//            TUserShowField field = fIdx.getField();
//            String key = fIdx.getField().getFieldKey();
//
//            if(field.getIsshow() == TUserShowFieldIsshow.HIDDEN.getValue())//不显示的不管
//                continue;
//
////            if(field.getIsmandatory() == TUserShowFieldIsmandatory.MANDATORY.getValue())//必填的没填
////                if(StringUtil.isEmpty(value)){
////                    record.setRemark(field.getFieldName() + "是必填项");
////                    return null;
////                }
//
//            if("account".equals(field.getFieldKey())){//账号不能为空
//                if(StringUtil.isEmpty(value)){
//                    record.setRemark(field.getFieldName() + "为空");
//                    record.setExaminee("");
//                    return null;
//                }else{
//                    record.setExaminee(value);
//                }
//            }else if("password".equals(field.getFieldKey())) {//密码不为空时,检查是否大于6位且至少一位数字或者字母
//                if (!StringUtil.isEmpty(value)) {
//                    if (value.length() <= 6) {
//                        record.setRemark(BizEnums.PasswordTooShort.getMessage());
//                        return null;
//                    }else if(!PasswordUtil.containLetterAndNumber(value)){
//                        record.setRemark(BizEnums.PasswordNotVail.getMessage());
//                        return null;
//                    }else if(PasswordUtil.containChinese(value)){
//                        record.setRemark(BizEnums.PasswordCannotHasChinese.getMessage());
//                        return null;
//                    }
//                } else {//否则跳过，在service中再随机生成，因为需要判断是否已有账户
////                    oriFieldMap.put("password", geneRandomPwd());
//                    continue;
//                }
//            }else if("mobile".equals(field.getFieldKey())) {//手机号码规则
//                if (!StringUtil.isEmpty(value)) {
//                    if (!VailUtil.isMobile(value)) {
//                        try{
//                            //手机号码有可能被解析成科学计数法
//                            value = new BigDecimal(value).toPlainString();
//                        }catch (Exception e){}
//                        if(!VailUtil.isMobile(value)){
//                            record.setRemark(field.getFieldName() + "字段不是有效的手机号码");
//                            return null;
//                        }
//                    }
//                }
//            }else if("email".equals(field.getFieldKey())) {//邮箱规则且不能多于50个字符
//                if (!StringUtil.isEmpty(value)) {
//                    if (!VailUtil.isEmail(value)){
//                        record.setRemark(field.getFieldName() + "字段不是有效的邮箱格式");
//                        return null;
//                    }else if(value.length() > 50){
//                        record.setRemark(field.getFieldName() + "字段过长，超过50个字符");
//                        return null;
//                    }
//                }
//            }else if(INDUSTRYNAME.equals(field.getFieldKey())) {//身份证规则
//                if (!StringUtil.isEmpty(value)) {
//                    if (!IdcardValidator.isValidatedAllIdcard(value)){
//                        record.setRemark(field.getFieldName() + "字段不是有效的身份证格式");
//                        return null;
//                    }
//                }
//            }else if(WORKYEARS.equals(field.getFieldKey())){//工作年限需要数字类型
//                if (!StringUtil.isEmpty(value)) {
//                    try{
//                        int years = Integer.parseInt(value);
//                        if(years < 0 || years > 40){
//                            record.setRemark(field.getFieldName() + "字段内容不正确");
//                            return null;
//                        }
//                    }catch (Exception e){
//                        record.setRemark(field.getFieldName() + "字段必须为整数数字");
//                        return null;
//                    }
//                }
//            }
//
//            if(field.getType() == TUserShowFieldType.INPUT.getValue()){//输入框最大长度为50个字符
//                if(!StringUtil.isEmpty(value) && value.length() > 50){
//                    record.setRemark(field.getFieldName() + "字段过长，最大长度为50个字符");
//                    return null;
//                }
//            }else if(field.getType() == TUserShowFieldType.DATE_TYPE.getValue()){//日期格式的若不为空则需要检查是否符合yyyy-MM-dd
//                if(!StringUtil.isEmpty(value)){
//                    try{
//                        TimeUtil.formateDate(value);
//                    }catch (Exception e){
//                        record.setRemark(field.getFieldName() + "不符合yyyy-MM-dd日期格式");
//                        return null;
//                    }
//                }
//            }else if(field.getType() == TUserShowFieldType.SELECT_TYPE.getValue()){//选项类型，值若不为空则必须是选项之中的值
//                if(!StringUtil.isEmpty(value)){
//                    List<String> opts = StringUtil.toStrListByComma(field.getSelectValue());
//                    if(!opts.contains(value)){
//                        record.setRemark(field.getFieldName() + "字段传入非可选值");
//                        return null;
//                    }
//                }
//            }
//
//            if(WORKYEARS.equals(field.getFieldKey())){//转化成整形
//                oriFieldMap.put(field.getFieldKey(), Integer.parseInt(value));
//            }else if(BIRTHDAY.equals(field.getFieldKey())){//转化成TimeStamp
//                oriFieldMap.put(field.getFieldKey(), new Timestamp(TimeUtil.formateDate(value).getTime()));
//            }else{//常规处理
//                if(field.getIsextension() == TUserShowFieldIsextension.UNEXTENSION.getValue()){//放到map，稍后直接利用gson转成Tuserinfo
//                    oriFieldMap.put(field.getFieldKey(), value);
//                }else{//放到exList，稍后直接利用gson转成jsonStr
//                    exFields.add(new DField(field.getFieldKey(), field.getFieldName(), value));
//                }
//            }
//
//        }
//        //遍历赋值end
//
//        TUserInfo userInfo = GsonUtil.fromJson(GsonUtil.toJson(oriFieldMap), TUserInfo.class);
//        userInfo.setExtensionField(GsonUtil.toJson(exFields));
//
//        return userInfo;
//    }

    public static TUserInfo toUserInfo(Map<String, String> map, List<TUserShowField> fields){
        return toUserInfo(map, fields, true).getUserInfo();
    }

    public static CUserInfo culUserInfo(Map<String, String> map, List<TUserShowField> fields){
        return toUserInfo(map, fields, false);
    }

    public static CUserInfo toUserInfo(Map<String, String> map, List<TUserShowField> fields, boolean ingoreErr) {

        CUserInfo c = new CUserInfo();
        c.setIngoreErr(ingoreErr);

        Map<String, Object> oriFieldMap = new HashMap<>();//系统字段
        List<DField> exFields = new ArrayList<>();//扩展字段

        //遍历赋值
        for (TUserShowField field : fields) {

            String key = field.getFieldKey();
            String value = map.get(key);

            if(StringUtil.isEmpty(value))
                continue;

            if (field.getIsshow() == TUserShowFieldIsshow.HIDDEN.getValue())//不显示的不管
                continue;

            if (ACCOUNT.equals(key)) {//账号不能为空
                if (StringUtil.isEmpty(value)) {
                   c.addErrTip(BizEnums.NoAccount, field);
                }
            } else if (PASSWORD.equals(key)) {//密码不为空时,检查是否大于6位且至少一位数字或者字母
                if (!StringUtil.isEmpty(value)) {
                    if (value.length() < 6) {
                        c.addErrTip(BizEnums.PasswordTooShort, field);
                    }else if(!PasswordUtil.containLetterAndNumber(value)){
                        c.addErrTip(BizEnums.PasswordNotVail, field);
                    }else if(PasswordUtil.containChinese(value)){
                        c.addErrTip(BizEnums.PasswordCannotHasChinese, field);
                    }
                } else {//否则跳过，在service中再随机生成，因为需要判断是否已有账户
                    continue;
                }
            } else if (MOBILE.equals(key)) {//手机号码规则
                if (!StringUtil.isEmpty(value)) {
                    if (!VailUtil.isMobile(value)) {
                        try {
                            //手机号码有可能被解析成科学计数法
                            value = new BigDecimal(value).toPlainString();
                        } catch (Exception e) {
                        }
                        if (!VailUtil.isMobile(value)) {
                            c.addErrTip(BizEnums.MobileNotVail, field);
                        }
                    }
                }
            } else if (EMAIL.equals(key)) {//邮箱规则
                if (!StringUtil.isEmpty(value)) {
                    if (!VailUtil.isEmail(value)) {
                        c.addErrTip(BizEnums.EmailNotVail, field);
                    }
                }
            } else if (IDENTITYNUM.equals(field.getFieldKey())) {//身份证规则
                if (!StringUtil.isEmpty(value)) {
                    if (!IdcardValidator.isValidatedAllIdcard(value)) {
                        c.addErrTip(BizEnums.IDNotVail, field);
                    }
                } else if (WORKYEARS.equals(key)) {//工作年限需要数字类型
                    /*if (!StringUtil.isEmpty(value)) {
                        try {
                            int years = Integer.parseInt(value);
                            if (years < 0 || years > 60) {
                                c.addErrTip(BizEnums.WorkTimeNotVail, field);
                            }
                        } catch (Exception e) {
                            c.addErrTip(BizEnums.WorkTimeNotVail, field);
                        }
                    }*/
                }
            }

            if (field.getType() == TUserShowFieldType.INPUT.getValue()) {//输入框最大长度为50个字符
                if (!StringUtil.isEmpty(value) && value.length() > 50) {
                    c.addErrTip(BizEnums.InputTooLong, field);
                }
            } else if (field.getType() == TUserShowFieldType.DATE_TYPE.getValue()) {//日期格式的若不为空则需要检查是否符合yyyy-MM-dd
                if (!StringUtil.isEmpty(value)) {
                    try {
                        TimeUtil.formateDate(value);
                    } catch (Exception e) {
                        c.addErrTip(BizEnums.DateNotVail, field);
                    }
                }
            } else if (field.getType() == TUserShowFieldType.SELECT_TYPE.getValue()) {//选项类型，值若不为空则必须是选项之中的值
                if (!StringUtil.isEmpty(value)) {
                    List<String> opts = StringUtil.toStrListByComma(field.getSelectValue());
                    if (!opts.contains(value)) {
                        c.addErrTip(BizEnums.SelectNotVail, field);
                    }
                }
            }

            if (WORKYEARS.equals(key) && !StringUtil.isEmpty(value)) {//转化成整形
                //oriFieldMap.put(field.getFieldKey(), Integer.parseInt(value));
                oriFieldMap.put(field.getFieldKey(), value);
            } else if (BIRTHDAY.equals(key) && !StringUtil.isEmpty(value)) {//转化成TimeStamp
                try{
                    oriFieldMap.put(field.getFieldKey(), new Timestamp(TimeUtil.formateDate(value).getTime()));
                }catch (Exception e){
//                    c.addErrTip(BizEnums.DateNotVail, field);
                }
            } else {//常规处理
                if (field.getIsextension() == TUserShowFieldIsextension.UNEXTENSION.getValue()) {//放到map，稍后直接利用gson转成Tuserinfo
                    oriFieldMap.put(key, value);
                } else {//放到exList，稍后直接利用gson转成jsonStr
                    exFields.add(new DField(key, field.getFieldName(), value));
                }
            }
            //遍历赋值end

        }

        oriFieldMap = camelCase(oriFieldMap);

        TUserInfo userInfo = GsonUtil.fromJson(GsonUtil.toJson(oriFieldMap), TUserInfo.class);
        userInfo.setExtensionField(GsonUtil.toJson(exFields));

        c.setUserInfo(userInfo);

        return c;
    }

    public static Map<String, Object> camelCase(Map<String, Object> map){

        for(String key : needCamels){
            Object value = map.get(key);
            if(value != null){
                map.put(lineToHump(key), value);
            }
        }

        return map;
    }

    /**下划线转驼峰*/
    public static String lineToHump(String str){

        if(StringUtil.isEmpty(str)){
            return str;
        }

        while(str.indexOf("_") > 0){
            int index = str.indexOf("_");
            String letter = str.substring(index+1,index+2);
            str = str.replace("_"+letter,letter.toUpperCase());
        }

        return str;
    }

    public static List<FieldErrTip> checkNotNullFields(Map<String, String> map, List<TUserShowField> fields, boolean ingoreCheck){
        List<FieldErrTip> tips = new ArrayList<>();

        //账号的必填
        String accountVal = map.get(ACCOUNT);
        if(StringUtil.isEmpty(accountVal)){
            tips.add(new FieldErrTip(ACCOUNT, "账号", BizEnums.FieldCannotBeBlank));
        }

        //其他看场景检测必填
        if(!ingoreCheck){
            for(TUserShowField field : fields){
                if(field.getIsmandatory() == TUserShowFieldIsmandatory.MANDATORY.getValue()){
                    String key = field.getFieldKey();
                    if(ACCOUNT.equals(key))
                        continue;
                    String value = map.get(key);

                    if(StringUtil.isEmpty(value)){
                        tips.add(new FieldErrTip(field, BizEnums.FieldCannotBeBlank));
                    }
                }
            }
        }

        return tips;
    }

}
