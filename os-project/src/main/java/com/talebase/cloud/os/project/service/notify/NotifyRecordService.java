package com.talebase.cloud.os.project.service.notify;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.enums.TAdminStatus;
import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.examer.domain.TUserInfo;
import com.talebase.cloud.base.ms.examer.dto.DReSetPassword;
import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.*;
import com.talebase.cloud.base.ms.notify.enumes.DNotifyRecordSendType;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.base.ms.project.domain.TTaskExaminer;
import com.talebase.cloud.base.ms.project.dto.DProjectSelect;
import com.talebase.cloud.base.ms.project.dto.DTaskMarked;
import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bin.yang on 2016-11-29.
 */

@Service
public class NotifyRecordService {
    static final String SERVICE_NAME = "ms-notify";
    static final String EXAMMER_SERVICE_NAME = "ms-examer";
    static final String ADMIN_SERVICE_NAME = "ms-admin";
    static final String PROJECT_SERVICE_NAME = "ms-project";
    static final String CONSUMPTION_SERVICE_NAME = "ms-consumption";
    @Value("${examineeUrl}")
    String examineeUrl;

    @Value("${examinerUrl}")
    String examinerUrl;

    @Autowired
    MsInvoker invoker;
    @Autowired
    RabbitSmsSenderService rabbitSmsSenderService;
    @Autowired
    RabbitEmailSenderService rabbitEmailSenderService;

    /**
     * 查询日志列表
     *
     * @param dPageSearchData
     * @param pageRequest
     * @return
     */
    public ServiceResponse<PageResponse<DNotifyRecord>> get(DPageSearchData dPageSearchData, PageRequest pageRequest) {
        String servicePath = "http://" + SERVICE_NAME + "/notifyRecords";
        ServiceResponse response = invoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), dPageSearchData, pageRequest), new ParameterizedTypeReference<ServiceResponse<PageResponse<DNotifyRecord>>>() {
        });
        return response;
    }

    //获取下拉框内容
    public ServiceResponse getSelect() {
        String servicePath = "http://" + PROJECT_SERVICE_NAME + "/project/select";
        ServiceResponse response = invoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<List<DProjectSelect>>>() {
        });
        return response;
    }


    /**
     * 考生预览
     *
     * @param examineePreview
     * @return
     */
    public ServiceResponse previewExaminee(DExamineePreview examineePreview) throws InvocationTargetException, IllegalAccessException {
        DPreviewResponse dPreviewResponse = new DPreviewResponse();
        DReSetPassword dReSetPassword = new DReSetPassword();
        BeanConverter.copyProperties(dReSetPassword, examineePreview);
        List<DReceiverInfo> dReceiverInfos = getExamineeList(dReSetPassword);
        if (dReceiverInfos.size() <= 0) {
            return new ServiceResponse(BizEnums.NO_SEND_EXAMEE, true);
        }
        Integer pageIndex = examineePreview.getPageIndex() == null ? 0 : examineePreview.getPageIndex() - 1;
        if (pageIndex > dReceiverInfos.size()) {
            pageIndex = dReceiverInfos.size() - 1;
        }
        dPreviewResponse.setHasNext(pageIndex < dReceiverInfos.size() - 1);
        dPreviewResponse.setHasPrevious(pageIndex > 1);
        String path = "http://" + EXAMMER_SERVICE_NAME + "/examer/getUserById/" + dReceiverInfos.get(pageIndex).getId();
        ServiceResponse<TUserInfo> rp = invoker.get(path, new ParameterizedTypeReference<ServiceResponse<TUserInfo>>() {
        });
        DReceiverInfo dReceiverInfo = new DReceiverInfo();
        BeanConverter.copyProperties(dReceiverInfo, rp.getResponse());
        dPreviewResponse.setEmailReceiver(dReceiverInfo.getEmail());
        dPreviewResponse.setSmsReceiver(dReceiverInfo.getMobile());
        dPreviewResponse.setSubject(examineePreview.getSubject());
        dPreviewResponse.setSign(examineePreview.getSign());
        dPreviewResponse.setSmsContent(examineePreview.getSmsContent());
        dPreviewResponse.setEmailContent(replaceMailContent(examineePreview.getEmailContent(), examineePreview.getSign(), dReceiverInfo));
        dPreviewResponse.setSmsContent(replaceSmsContent(examineePreview.getSmsContent(), dReceiverInfo));
        dPreviewResponse.setTotalCount(dReceiverInfos.size());
        return new ServiceResponse(dPreviewResponse);
    }

    /**
     * 评卷人预览
     *
     * @param dExaminerPreview
     * @return
     */
    public ServiceResponse previewExaminer(DExaminerPreview dExaminerPreview) throws InvocationTargetException, IllegalAccessException {
        DPreviewResponse dPreviewResponse = new DPreviewResponse();
        List<String> ids = StringUtil.toStrListByComma(dExaminerPreview.getIds());
        if (ids.size() <= 0) {
            return new ServiceResponse(BizEnums.NO_SEND_EXAMER, true);
        }
        Integer pageIndex = dExaminerPreview.getPageIndex() == null ? 0 : dExaminerPreview.getPageIndex() - 1;
        if (pageIndex > ids.size()) {
            pageIndex = ids.size() - 1;
        }
        dPreviewResponse.setHasNext(pageIndex < ids.size() - 1);
        dPreviewResponse.setHasPrevious(pageIndex > 0);
        DAdmin dAdmin = new DAdmin();
        dAdmin.setCompanyId(ServiceHeaderUtil.getRequestHeader().getCompanyId());
        dAdmin.setAccount(ids.get(pageIndex));
        String path = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/getByAccountAndCompanyId";
        ServiceResponse<TAdmin> rp = invoker.post(path, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), dAdmin), new ParameterizedTypeReference<ServiceResponse<TAdmin>>() {
        });
        DReceiverInfo dReceiverInfo = new DReceiverInfo();
        BeanConverter.copyProperties(dReceiverInfo, rp.getResponse());
        dPreviewResponse.setEmailReceiver(dReceiverInfo.getEmail());
        dPreviewResponse.setSmsReceiver(dReceiverInfo.getMobile());
        dPreviewResponse.setSubject(dExaminerPreview.getSubject());
        dPreviewResponse.setSign(dExaminerPreview.getSign());
        dPreviewResponse.setSmsContent(dExaminerPreview.getSmsContent());
        dPreviewResponse.setEmailContent(replaceMailContent(dExaminerPreview.getEmailContent(), dExaminerPreview.getSign(), dReceiverInfo));
        dPreviewResponse.setSmsContent(replaceSmsContent(dExaminerPreview.getSmsContent(), dReceiverInfo));
        dPreviewResponse.setTotalCount(ids.size());
        return new ServiceResponse(dPreviewResponse);
    }


    /**
     * 去发送通知-考生
     *
     * @param dReSetPassword
     * @return
     */
    public ServiceResponse toSendExaminee(DReSetPassword dReSetPassword) {
        DToSendNotifyResponse dToSendNotifyResponse = new DToSendNotifyResponse();
        //获得总数，手机数，邮箱数。
        String servicePath = "http://" + EXAMMER_SERVICE_NAME + "/examer/project/counts";
        ServiceResponse<DReceiverStatistics> response = invoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), dReSetPassword), new ParameterizedTypeReference<ServiceResponse<DReceiverStatistics>>() {
        });
        dToSendNotifyResponse.setTotalCount(response.getResponse().getTotalCount());
        dToSendNotifyResponse.setEmailCount(response.getResponse().getHasEmailCount());
        dToSendNotifyResponse.setSmsCount(response.getResponse().getHasSmsCount());

        //获取邮件模板集合
        List<TNotifyTemplate> emailList = getEmailList();
        dToSendNotifyResponse.setEmailList(emailList);
        //获取短信模板集合
        List<TNotifyTemplate> smsList = getSmsList();
        dToSendNotifyResponse.setSmsList(smsList);
        return new ServiceResponse(dToSendNotifyResponse);
    }

    /**
     * 去发送通知-考官
     *
     * @param taskId
     * @return
     */
    public ServiceResponse toSendExaminer(Integer taskId) throws InvocationTargetException, IllegalAccessException {
        DToSendNotifyResponse dToSendNotifyResponse = new DToSendNotifyResponse();
        //获取某个任务下的所有考官
        List<String> accounts = new ArrayList<>();
        List<DReceiverInfo> dReceiverInfos = getExaminerList(taskId, accounts);
        if (dReceiverInfos.size() <= 0) {
            return new ServiceResponse(BizEnums.NO_SEND_EXAMER, true);
        }
        //计算有手机号码和邮箱的数量。
        Integer emailCount = 0;
        Integer smsCount = 0;
        for (DReceiverInfo dReceiverInfo : dReceiverInfos) {
            if (!StringUtil.isEmpty(dReceiverInfo.getEmail())) {
                emailCount++;
            }
            if (!StringUtil.isEmpty(dReceiverInfo.getMobile())) {
                smsCount++;
            }
        }
        dToSendNotifyResponse.setEmailCount(emailCount);
        dToSendNotifyResponse.setSmsCount(smsCount);
        dToSendNotifyResponse.setTotalCount(dReceiverInfos.size());
        dToSendNotifyResponse.setdReceiverInfos(dReceiverInfos);
        //获取邮件模板集合
        List<TNotifyTemplate> emailList = getEmailList();
        dToSendNotifyResponse.setEmailList(emailList);
        //获取短信模板集合
        List<TNotifyTemplate> smsList = getSmsList();
        dToSendNotifyResponse.setSmsList(smsList);

        return new ServiceResponse(dToSendNotifyResponse);
    }

    /**
     * 查询邮件模板列表
     *
     * @return
     */
    private List<TNotifyTemplate> getEmailList() {
        String servicePath = "http://" + SERVICE_NAME + "/mailTemplate/list";
        ServiceResponse<List<TNotifyTemplate>> response = invoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<List<TNotifyTemplate>>>() {
        });
        return response.getResponse();
    }

    /**
     * 查询短信模板列表
     *
     * @return
     */
    private List<TNotifyTemplate> getSmsList() {
        String servicePath = "http://" + SERVICE_NAME + "/smsTemplate/list";
        ServiceResponse<List<TNotifyTemplate>> response = invoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<List<TNotifyTemplate>>>() {
        });
        return response.getResponse();
    }

    /**
     * 批量发送通知
     *
     * @param readys
     * @return
     */
    public List<Integer> send(List<DNotifyRecord> readys) {
        String servicePath = "http://" + SERVICE_NAME + "/notifyRecord/addBatch";
        ServiceResponse<List<Integer>> response = invoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), readys), new ParameterizedTypeReference<ServiceResponse<List<Integer>>>() {
        });
        return response.getResponse();
    }

    /**
     * 查询重发的信息
     *
     * @param idList
     * @return
     */
    public List<DNotifyRecord> getNotifyRecords(List<Integer> idList) {
        String servicePath = "http://" + SERVICE_NAME + "/notifyRecord/reSend";
        ServiceResponse<List<DNotifyRecord>> response = invoker.post(servicePath, new ServiceRequest<>(new ServiceHeader(), idList), new ParameterizedTypeReference<ServiceResponse<List<DNotifyRecord>>>() {
        });
        return response.getResponse();
    }

    /**
     * 重发更新次数
     *
     * @param idList
     * @return
     */
    public List<DNotifyRecord> updateTimes(List<Integer> idList) {
        String path = "http://" + SERVICE_NAME + "/notifyRecord/update/times";
        ServiceResponse<List<DNotifyRecord>> serviceResponse = invoker.put(path, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), idList), new ParameterizedTypeReference<ServiceResponse<List<DNotifyRecord>>>() {
        });
        return serviceResponse.getResponse();
    }

    /**
     * 重发查询idList
     *
     * @param dPageSearchData
     * @return
     */
    public List<Integer> getIdList(DPageSearchData dPageSearchData) {
        String path = "http://" + SERVICE_NAME + "/notifyRecord/getIdList";
        ServiceResponse<List<Integer>> serviceResponse = invoker.post(path, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), dPageSearchData), new ParameterizedTypeReference<ServiceResponse<List<Integer>>>() {
        });
        return serviceResponse.getResponse();
    }


    /**
     * 组装数据
     */
    public DNotifyData buildData(List<DReceiverInfo> dReceiverInfos, DSendNotifyRequest dSendNotifyRequest) throws InvocationTargetException, IllegalAccessException {
        DNotifyData dNotifyData = new DNotifyData();
        List<DNotifyRecord> dNotifyRecords = new ArrayList<>();
        Integer emailCount = 0;
        Integer smsCount = 0;
        for (DReceiverInfo dReceiverInfo : dReceiverInfos) {//遍历用户
            DNotifyRecord saveDNotifyRecord = toSaveDNotifyRecord(dReceiverInfo, dSendNotifyRequest);
            if (dSendNotifyRequest.getSendType() == DNotifyRecordSendType.MAIL.getValue()) {//邮件
                if (StringUtil.isEmpty(dReceiverInfo.getEmail())) {
                    continue;
                }
                saveDNotifyRecord.setSendType(DNotifyRecordSendType.MAIL.getValue());
                saveDNotifyRecord.setSendContent(replaceMailContent(dSendNotifyRequest.getEmailContent(), dSendNotifyRequest.getSign(), dReceiverInfo));
                dNotifyRecords.add(saveDNotifyRecord);
                emailCount++;
            } else if (dSendNotifyRequest.getSendType() == DNotifyRecordSendType.SMS.getValue()) {//短信
                if (StringUtil.isEmpty(dReceiverInfo.getMobile())) {
                    continue;
                }
                saveDNotifyRecord.setSendType(DNotifyRecordSendType.SMS.getValue());
                //替换内容
                String content = replaceSmsContent(dSendNotifyRequest.getSmsContent(), dReceiverInfo);
                saveDNotifyRecord.setSendContent(content);
                dNotifyRecords.add(saveDNotifyRecord);
                //计算短信所需条数
                smsCount += SmsUtil.calculateSmsCount(content);
            } else if (dSendNotifyRequest.getSendType() == DNotifyRecordSendType.MAIL_AND_SMS.getValue()) {//邮件和短信
                DNotifyRecord saveDNotifyRecord2 = toSaveDNotifyRecord(dReceiverInfo, dSendNotifyRequest);
                if (!StringUtil.isEmpty(dReceiverInfo.getEmail())) {//邮箱为空，不发送邮件
                    saveDNotifyRecord.setSendType(DNotifyRecordSendType.MAIL.getValue());
                    saveDNotifyRecord.setSendContent(replaceMailContent(dSendNotifyRequest.getEmailContent(), dSendNotifyRequest.getSign(), dReceiverInfo));
                    dNotifyRecords.add(saveDNotifyRecord);
                    emailCount++;
                }
                if (!StringUtil.isEmpty(dReceiverInfo.getMobile())) {//手机为空，不发送短信
                    saveDNotifyRecord2.setSendType(DNotifyRecordSendType.SMS.getValue());
                    //替换内容
                    String content = replaceSmsContent(dSendNotifyRequest.getSmsContent(), dReceiverInfo);
                    saveDNotifyRecord2.setSendContent(content);
                    dNotifyRecords.add(saveDNotifyRecord2);
                    //计算短信所需条数
                    smsCount += SmsUtil.calculateSmsCount(content);
                }
            }

        }
        dNotifyData.setdNotifyRecords(dNotifyRecords);
        dNotifyData.setEmailCount(emailCount);
        dNotifyData.setSmsCount(smsCount);
        return dNotifyData;
    }

    private DNotifyRecord toSaveDNotifyRecord(DReceiverInfo dReceiverInfo, DSendNotifyRequest dSendNotifyRequest) throws InvocationTargetException, IllegalAccessException {
        DNotifyRecord saveDNotifyRecord = new DNotifyRecord();
        BeanConverter.copyProperties(saveDNotifyRecord, dSendNotifyRequest);
        saveDNotifyRecord.setName(dReceiverInfo.getName());
        saveDNotifyRecord.setEmail(dReceiverInfo.getEmail());
        saveDNotifyRecord.setMobile(dReceiverInfo.getMobile());
        saveDNotifyRecord.setPassword(dReceiverInfo.getPassword());
        saveDNotifyRecord.setAccount(dReceiverInfo.getAccount());

        return saveDNotifyRecord;
    }

    /**
     * 发送MQ
     *
     * @param idList
     * @param readys
     */
    public void sendMq(List<Integer> idList, List<DNotifyRecord> readys) {
        for (int i = 0; i < readys.size(); i++) {
            DNotifyRecord dNotifyRecord = readys.get(i);
            dNotifyRecord.setId(idList.get(i));
            if (dNotifyRecord.getSendType() == DNotifyRecordSendType.MAIL.getValue()) {
                sendMqByEmail(dNotifyRecord);
            } else {
                sendMqBySms(dNotifyRecord);
            }
        }

    }

    public void sendMqByEmail(DNotifyRecord dNotifyRecord) {
        List list = new ArrayList();
        DEmailLog record = new DEmailLog();
        record.setTableId(dNotifyRecord.getId());
        record.setTableName("t_notify_record");
        record.setEmail(dNotifyRecord.getEmail());
        record.setSendContent(dNotifyRecord.getSendContent());
        record.setSubject(dNotifyRecord.getSendSubject());
        list.add(record);
        //发送MQ
        rabbitEmailSenderService.sendEmail(list);
    }

    public void sendMqBySms(DNotifyRecord dNotifyRecord) {
        TSmsInfo smsInfo = new TSmsInfo();
        smsInfo.setGuid(dNotifyRecord.getId().toString());
        smsInfo.setTableName("t_notify_record");
        smsInfo.setSendto(dNotifyRecord.getMobile());
        smsInfo.setContent(dNotifyRecord.getSendContent());
        //发送MQ
        rabbitSmsSenderService.smsSender(smsInfo);
    }

    /**
     * 根据ids或者查询条件批量查询考生信息
     *
     * @param dReSetPassword
     * @return
     */
    public List<DReceiverInfo> getExamineeList(DReSetPassword dReSetPassword) throws InvocationTargetException, IllegalAccessException {
        String servicePath = "http://" + EXAMMER_SERVICE_NAME + "/examer/project/userList";
        ServiceResponse<List<TUserInfo>> rp = invoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), dReSetPassword), new ParameterizedTypeReference<ServiceResponse<List<TUserInfo>>>() {
        });
        List<TUserInfo> tUserInfos = rp.getResponse();
        List<DReceiverInfo> list = new ArrayList<>();
        for (TUserInfo tUserInfo : tUserInfos) {
            DReceiverInfo dReceiverInfo = new DReceiverInfo();
            BeanConverter.copyProperties(dReceiverInfo, tUserInfo);
            Integer taskCount = getExamineeTaskCount(tUserInfo.getId());
            dReceiverInfo.setTaskCount(taskCount);
            list.add(dReceiverInfo);
        }
        return list;
    }

    /**
     * 根据taskId查询条件批量查询考生信息
     *
     * @param taskId
     * @return
     */
    public List<DReceiverInfo> getExaminerList(Integer taskId, List<String> accounts) throws InvocationTargetException, IllegalAccessException {
        if (accounts.size() <= 0) {
            String servicePath = "http://" + PROJECT_SERVICE_NAME + "/task/examiners/{taskId}";
            ServiceResponse<List<TTaskExaminer>> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<TTaskExaminer>>>() {
            }, taskId);

            List<TTaskExaminer> tTaskExaminers = response.getResponse();
            accounts = new ArrayList<>();
            //根据id查询具体信息
            for (TTaskExaminer tTaskExaminer : tTaskExaminers) {
                accounts.add(tTaskExaminer.getExaminer());
            }
        }
        List<DReceiverInfo> list = new ArrayList<>();
        if (accounts.size() <= 0) {
            return list;
        }
        String path = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/getAdminsByAccounts";
        ServiceResponse<List<TAdmin>> rp1 = invoker.post(path, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), accounts), new ParameterizedTypeReference<ServiceResponse<List<TAdmin>>>() {
        });
        for (TAdmin tAdmin : rp1.getResponse()) {
            if(!tAdmin.getStatus().equals(TAdminStatus.EFFECTIVE.getValue())){
                continue;
            }
            DReceiverInfo dReceiverInfo = new DReceiverInfo();
            BeanConverter.copyProperties(dReceiverInfo, tAdmin);
            Integer taskCount = getExaminerTaskCount(tAdmin.getAccount());
            dReceiverInfo.setTaskCount(taskCount);
            list.add(dReceiverInfo);
        }

        return list;
    }

    /**
     * 根据id查询考生任务数量
     *
     * @param userId
     * @return
     */
    public Integer getExamineeTaskCount(Integer userId) throws InvocationTargetException, IllegalAccessException {
        String servicePath = "http://" + EXAMMER_SERVICE_NAME + "/examer/getTaskCount";
        ServiceResponse<Integer> rp = invoker.post(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), userId), new ParameterizedTypeReference<ServiceResponse<Integer>>() {
        });
        return rp.getResponse();
    }

    /**
     * 根据account查询评卷人任务数量
     *
     * @param account
     * @return
     */
    public Integer getExaminerTaskCount(String account) throws InvocationTargetException, IllegalAccessException {
        String servicePath = "http://" + PROJECT_SERVICE_NAME + "/tasks/mark/{companyId}/{operatorName}";
        ServiceResponse<DTaskMarked> rp = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DTaskMarked>>() {
        }, ServiceHeaderUtil.getRequestHeader().getCompanyId(), account);
        Integer taskCount = rp.getResponse().getTotalTaskNum() - rp.getResponse().getDoneTaskNum();
        return taskCount;
    }


    /**
     * 保存短信模板
     *
     * @param requestHeader
     * @param dSendNotifyRequest
     */
    public void saveSms(ServiceHeader requestHeader, DSendNotifyRequest dSendNotifyRequest) {
        String path = "http://" + SERVICE_NAME + "/smsTemplate";
        DNotifyTemplate dNotifyTemplate = new DNotifyTemplate();
        dNotifyTemplate.setCompanyId(requestHeader.getCompanyId());
        dNotifyTemplate.setName(dSendNotifyRequest.getNewSmsName());
        dNotifyTemplate.setContent(dSendNotifyRequest.getSmsContent());
        dNotifyTemplate.setWhetherDefault(dSendNotifyRequest.isWhetherDefaultSms());
        dNotifyTemplate.setCreator(requestHeader.getOperatorName());
        ServiceRequest<DNotifyTemplate> new_req = new ServiceRequest(requestHeader, dNotifyTemplate);
        invoker.post(path, new_req, new ParameterizedTypeReference<ServiceResponse<DNotifyRecord>>() {
        });
    }

    /**
     * 保存邮件模板
     *
     * @param requestHeader
     * @param dSendNotifyRequest
     */
    public void saveMail(ServiceHeader requestHeader, DSendNotifyRequest dSendNotifyRequest) {
        String path = "http://" + SERVICE_NAME + "/mailTemplate";
        DNotifyTemplate dNotifyTemplate = new DNotifyTemplate();
        dNotifyTemplate.setCompanyId(requestHeader.getCompanyId());
        dNotifyTemplate.setName(dSendNotifyRequest.getNewMailName());
        dNotifyTemplate.setSubject(dSendNotifyRequest.getSendSubject());
        dNotifyTemplate.setContent(dSendNotifyRequest.getEmailContent());
        dNotifyTemplate.setSign(dSendNotifyRequest.getSign());
        dNotifyTemplate.setCreator(requestHeader.getOperatorName());
        dNotifyTemplate.setWhetherDefault(dSendNotifyRequest.isWhetherDefaultMail());
        ServiceRequest<DNotifyTemplate> new_req = new ServiceRequest(requestHeader, dNotifyTemplate);
        invoker.post(path, new_req, new ParameterizedTypeReference<ServiceResponse<DNotifyRecord>>() {
        });
    }

    /**
     * 邮件替换内容
     *
     * @param content
     * @param sign
     * @param dReceiverInfo
     * @return
     */
    public String replaceMailContent(String content, String sign, DReceiverInfo dReceiverInfo) {
        if (!StringUtils.isBlank(sign)) {
            return replateParam(content + "</ br>" + sign, dReceiverInfo);
        } else {
            return replateParam(content, dReceiverInfo);
        }
    }

    /**
     * 短信替换内容
     *
     * @param content
     * @param dReceiverInfo
     * @return
     */
    public String replaceSmsContent(String content, DReceiverInfo dReceiverInfo) {
        return StringUtil.replaceHtml(replateParam(content, dReceiverInfo));
    }


    /**
     * 替换可变参数 $$name$$,$$account$$...
     *
     * @param content
     * @return
     */
    private String replateParam(String content, DReceiverInfo dReceiverInfo) {
        String password = "";
        try {
            password = Des3Util.des3DecodeCBC(dReceiverInfo.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String gender = StringUtil.isEmpty(dReceiverInfo.getGender()) ? "先生/女士" : dReceiverInfo.getGender().equals("男") ? "先生" : dReceiverInfo.getGender().equals(" 女") ? "女士" : "先生/女士";
        String s = content.replaceAll("\\$\\$姓名\\$\\$", dReceiverInfo.getName())
                .replaceAll("\\$\\$帐号\\$\\$", dReceiverInfo.getAccount())
                .replaceAll("\\$\\$密码\\$\\$", password)
                .replaceAll("\\$\\$考试地址\\$\\$", "<a href=\"" + examineeUrl + "\">" + examineeUrl + "</a>")
                .replaceAll("\\$\\$评卷地址\\$\\$", "<a href=\"" + examinerUrl + "\">" + examinerUrl + "</a>")
                .replaceAll("\\$\\$先生/女士\\$\\$", gender)
                .replaceAll("\\$\\$任务数量\\$\\$", dReceiverInfo.getTaskCount().toString());

        return s;
    }

    /**
     * 获取导出数据
     *
     * @param sendType
     * @return
     */
    public List<DNotifyRecordExport> getExportList(Integer sendType) {
        String path = "http://" + SERVICE_NAME + "/notifyRecord/list/" + sendType;
        ServiceResponse<List<DNotifyRecordExport>> serviceResponse = invoker.post(path, new ServiceRequest<Integer>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<List<DNotifyRecordExport>>>() {
        });
        return serviceResponse.getResponse();
    }

    /**
     * 获取邮件发送状态
     *
     * @param req
     * @return
     */
    public List<DEmailLog> getEmailsStatus(ServiceRequest req) {
        String SERVICE_NAME = "ms-email";
        String servicePath = "http://" + SERVICE_NAME + "/emails";
        ServiceResponse<List<DEmailLog>> response = invoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<List<DEmailLog>>>() {
        });
        return response.getResponse();
    }

    /**
     * 获取短信发送状态
     *
     * @param ids
     * @return
     */
    public List<TSmsInfo> getSmsStatus(String ids) {
        String SERVICE_NAME = "ms-sms";
        String servicePath = "http://" + SERVICE_NAME + "/sms/queryStatusList";
        ServiceResponse<List<TSmsInfo>> response = invoker.post(servicePath, ids, new ParameterizedTypeReference<ServiceResponse<List<TSmsInfo>>>() {
        });
        return response.getResponse();
    }

    /**
     * 更新状态
     *
     * @param dNotifyRecordStatuses
     */
    public Integer updateRecordStatus(List<DNotifyRecordStatus> dNotifyRecordStatuses) {
        String servicePath = "http://" + SERVICE_NAME + "/notifyRecord/update/status";
        ServiceResponse<Integer> response = invoker.put(servicePath, new ServiceRequest<>(new ServiceHeader(), dNotifyRecordStatuses), new ParameterizedTypeReference<ServiceResponse<Integer>>() {
        });
        return response.getResponse();
    }

    /**
     * 更新考生发送通知状态
     */
    public Integer updateExamineeStatus(List<Integer> ids) {
        List<DNotifyRecord> dNotifyRecords = getNotifyRecords(ids);
        String servicePath = "http://" + EXAMMER_SERVICE_NAME + "/examer/updateExamSendStatus";
        ServiceResponse<Integer> response = invoker.put(servicePath, new ServiceRequest<>(new ServiceHeader(), dNotifyRecords), new ParameterizedTypeReference<ServiceResponse<Integer>>() {
        });
        return response.getResponse();
    }

    /**
     * 查询所有发送中的记录
     *
     * @return
     */
    public List<DNotifyRecord> querySendding() {
        String servicePath = "http://" + SERVICE_NAME + "/notifyRecords/sendding";
        ServiceResponse<List<DNotifyRecord>> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<DNotifyRecord>>>() {
        });
        return response.getResponse();
    }


    /**
     * 查询短信剩余量
     *
     * @return
     */
    public Integer getSmsBalance(Integer companyId) {
        String servicePath = "http://" + CONSUMPTION_SERVICE_NAME + "/consume/queryAccount/" + companyId;
        ServiceResponse<TAccount> response = invoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<TAccount>>() {
        });
        return response.getResponse().getSmsBalance();
    }

    /**
     * 扣除短信条数
     *
     * @return
     */
    public ServiceResponse deductSmsCount(TAccountLine tAccountLine) {
        String servicePath = "http://" + CONSUMPTION_SERVICE_NAME + "/consume/operate";
        ServiceResponse<TAccountLine> response = invoker.put(servicePath, new ServiceRequest<>(ServiceHeaderUtil.getRequestHeader(), tAccountLine), new ParameterizedTypeReference<ServiceResponse<TAccountLine>>() {
        });
        return response;
    }


    /**
     * 组装查询状态后的数据
     *
     * @param dNotifyRecords
     * @return
     */
    public List<DNotifyRecordStatus> bulidData(List<DNotifyRecord> dNotifyRecords) {
        List<DNotifyRecordStatus> dNotifyRecordStatuses = new ArrayList<>();
        String emailIds = "";
        String smsIds = "";
        //遍历数据集
        for (DNotifyRecord dNotifyRecord : dNotifyRecords) {
            if (dNotifyRecord.getSendType() == DNotifyRecordSendType.MAIL.getValue()) {
                emailIds += "," + dNotifyRecord.getId();
            } else if (dNotifyRecord.getSendType() == DNotifyRecordSendType.SMS.getValue()) {
                smsIds += "," + dNotifyRecord.getId();
            }
        }

        if (!"".equals(emailIds)) {
            //查询邮件状态
            List emailList = queryEmailsStatus(emailIds.substring(1));
            dNotifyRecordStatuses.addAll(emailList);
        }
        if (!"".equals(smsIds)) {
            //查询短信状态
            List smsList = querySmsStatus(smsIds.substring(1));
            dNotifyRecordStatuses.addAll(smsList);
        }

        return dNotifyRecordStatuses;
    }


    /**
     * 查询邮件发送的状态
     *
     * @param emailIds
     * @return
     */
    private List<DNotifyRecordStatus> queryEmailsStatus(String emailIds) {
        List<DNotifyRecordStatus> dNotifyRecordStatuses = new ArrayList<>();
        DEmailLog dEmailLog = new DEmailLog();
        dEmailLog.setTableName("t_notify_record");
        dEmailLog.setTableIds(emailIds);
        ServiceRequest<DEmailLog> req = new ServiceRequest<DEmailLog>();
        req.setRequest(dEmailLog);
        List<DEmailLog> dEmailLogs = getEmailsStatus(req);
        for (DEmailLog dEmailLog1 : dEmailLogs) {
            DNotifyRecordStatus dNotifyRecordStatus = new DNotifyRecordStatus();
            dNotifyRecordStatus.setId(dEmailLog1.getTableId());
            dNotifyRecordStatus.setStatus(dEmailLog1.getStatus());
            dNotifyRecordStatus.setSendTime(dEmailLog1.getSendTime());
            dNotifyRecordStatus.setType(TNotifyTemplateMethod.MAIL.getValue());
            dNotifyRecordStatuses.add(dNotifyRecordStatus);
        }

        return dNotifyRecordStatuses;
    }

    /**
     * 查询短信发送的状态
     *
     * @param smsIds
     * @return
     */
    private List<DNotifyRecordStatus> querySmsStatus(String smsIds) {
        List<DNotifyRecordStatus> dNotifyRecordStatuses = new ArrayList<>();
        List<TSmsInfo> tSmsInfos = getSmsStatus(smsIds);
        for (TSmsInfo tSmsInfo : tSmsInfos) {
            DNotifyRecordStatus dNotifyRecordStatus = new DNotifyRecordStatus();
            dNotifyRecordStatus.setId(Integer.parseInt(tSmsInfo.getGuid()));
            dNotifyRecordStatus.setStatus(tSmsInfo.getStatus());
            dNotifyRecordStatus.setSendTime(new Timestamp(tSmsInfo.getSendTime() == null ? new Date().getTime() : tSmsInfo.getSendTime().getTime()));
            dNotifyRecordStatus.setType(TNotifyTemplateMethod.SMS.getValue());
            dNotifyRecordStatuses.add(dNotifyRecordStatus);
        }

        return dNotifyRecordStatuses;
    }

}
