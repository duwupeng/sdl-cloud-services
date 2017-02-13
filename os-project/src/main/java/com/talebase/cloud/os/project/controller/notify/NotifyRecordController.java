package com.talebase.cloud.os.project.controller.notify;

import com.talebase.cloud.base.ms.consume.domain.TAccountLine;
import com.talebase.cloud.base.ms.examer.dto.DReSetPassword;
import com.talebase.cloud.base.ms.notify.dto.*;
import com.talebase.cloud.base.ms.notify.enumes.DNotifyRecordSendType;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.*;
import com.talebase.cloud.os.project.service.notify.NotifyRecordService;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-5.
 */

@RestController
public class NotifyRecordController {
    @Autowired
    NotifyRecordService notifyRecordService;

    @Value("${schedule.enable}")
    Boolean scheduleEnable;

    Integer times = 0;

    @PostMapping({"/notifyRecords"})
    public ServiceResponse<PageResponse<DNotifyRecord>> getEmails(DPageSearchData dPageSearchData, PageRequest pageRequest) {
        return notifyRecordService.get(dPageSearchData, pageRequest);
    }

    @GetMapping("/notifyRecord/select")
    public ServiceResponse getProjectSelect() {
        //下拉框
        return notifyRecordService.getSelect();
    }

    @PostMapping({"/notify/reSend/email"})
    public ServiceResponse reSendEmail(DPageSearchData dPageSearchData) {
        dPageSearchData.setSendType(0);
        List<Integer> idList = notifyRecordService.getIdList(dPageSearchData);
        List<DNotifyRecord> dNotifyRecords = notifyRecordService.updateTimes(idList);
        for (DNotifyRecord dNotifyRecord : dNotifyRecords) {
            notifyRecordService.sendMqByEmail(dNotifyRecord);
        }
        return new ServiceResponse();
    }

    @PostMapping({"/notify/reSend/sms"})
    public ServiceResponse reSendSms(DPageSearchData dPageSearchData) {
        dPageSearchData.setSendType(1);
        List<Integer> idList = notifyRecordService.getIdList(dPageSearchData);
        List<DNotifyRecord> dNotifyRecords = notifyRecordService.getNotifyRecords(idList);
        //此次需要发送的短信的条数
        Integer smsCount = 0;
        for (DNotifyRecord dNotifyRecord : dNotifyRecords) {
            smsCount += dNotifyRecord.getSendCount();
        }
        //去消费中心查询短信剩余量
        Integer smsBalance = notifyRecordService.getSmsBalance(ServiceHeaderUtil.getRequestHeader().getCompanyId());
        //如果剩余数不够扣当前发送量，则抛出异常
        if (smsCount != 0 && smsBalance - smsCount <= 0) {
            return new ServiceResponse(BizEnums.INSUFFICIENT_NUMBER_OF_REMAINING_SMS, true);
        } else {
            dNotifyRecords = notifyRecordService.updateTimes(idList);
            for (DNotifyRecord dNotifyRecord : dNotifyRecords) {
                TAccountLine tAccountLine = new TAccountLine();
                tAccountLine.setModifier(ServiceHeaderUtil.getRequestHeader().getOperatorName());
                tAccountLine.setProjectId(dNotifyRecord.getProjectId());
                tAccountLine.setTaskId(dNotifyRecord.getTaskId());
                tAccountLine.setType(1);
                tAccountLine.setSmsVar(dNotifyRecord.getSendCount());
                //如果短信剩余量足,则扣费
                notifyRecordService.deductSmsCount(tAccountLine);
                notifyRecordService.sendMqBySms(dNotifyRecord);
            }
        }

        return new ServiceResponse();
    }

    @PostMapping({"/notify/preview/examinee"})
    public ServiceResponse previewExaminee(DExamineePreview dExamineePreview) throws InvocationTargetException, IllegalAccessException {
        return notifyRecordService.previewExaminee(dExamineePreview);
    }

    @PostMapping({"/notify/preview/examiner"})
    public ServiceResponse previewExaminer(DExaminerPreview dExaminerPreview) throws InvocationTargetException, IllegalAccessException {
        return notifyRecordService.previewExaminer(dExaminerPreview);
    }

    @PostMapping({"/notify/toSend/examinee"})
    public ServiceResponse toSendExaminee(DReSetPassword dReSetPassword) throws InvocationTargetException, IllegalAccessException {
        return notifyRecordService.toSendExaminee(dReSetPassword);
    }

    @GetMapping({"/notify/toSend/examiner/{taskId}"})
    public ServiceResponse toSendExaminer(@PathVariable("taskId") Integer taskId) throws InvocationTargetException, IllegalAccessException {
        return notifyRecordService.toSendExaminer(taskId);
    }

    @PostMapping({"/notify/send/examinee"})
    public ServiceResponse sendExaminee(DExamineeSendRequest dExamineeSendRequest) throws InvocationTargetException, IllegalAccessException {
        if (!StringUtils.isEmpty(dExamineeSendRequest.getEmailContent())){
            int len = dExamineeSendRequest.getEmailContent().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.EMAIL_TEXT_BEYOUND_LENGTH);
            }
        }
        if (!StringUtils.isEmpty(dExamineeSendRequest.getSign())){
            int len = dExamineeSendRequest.getSign().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.SIGN_TEXT_BEYOUND_LENGTH);
            }
        }

        dExamineeSendRequest.setRoleId(1);
        //找考生
        DReSetPassword dReSetPassword = new DReSetPassword();
        BeanConverter.copyProperties(dReSetPassword, dExamineeSendRequest);
        List<DReceiverInfo> dReceiverInfos = notifyRecordService.getExamineeList(dReSetPassword);

        DSendNotifyRequest dSendNotifyRequest = new DSendNotifyRequest();
        BeanConverter.copyProperties(dSendNotifyRequest, dExamineeSendRequest);
        //组装数据
        DNotifyData dNotifyData = notifyRecordService.buildData(dReceiverInfos, dSendNotifyRequest);

        //此次需要发送的短信的条数
        Integer smsCount = dNotifyData.getSmsCount();
        Integer emailCount = dNotifyData.getEmailCount();
        if (emailCount <= 0 && smsCount <= 0) {
            return new ServiceResponse(BizEnums.NO_EMAIL_AND_SMS, true);
        }

        boolean fail = false;
        if (smsCount > 0) {
            //去消费中心查询短信剩余量
            Integer smsBalance = notifyRecordService.getSmsBalance(ServiceHeaderUtil.getRequestHeader().getCompanyId());

            //如果剩余数不够扣当前发送量，则抛出异常
            if (smsCount != 0 && smsBalance - smsCount <= 0) {
                return new ServiceResponse(BizEnums.INSUFFICIENT_NUMBER_OF_REMAINING_SMS, true);
            }

            //组装扣费信息
            TAccountLine tAccountLine = new TAccountLine();
            tAccountLine.setProjectId(dExamineeSendRequest.getProjectId());
            tAccountLine.setTaskId(dExamineeSendRequest.getTaskId());
            tAccountLine.setType(1);
            tAccountLine.setModifier(ServiceHeaderUtil.getRequestHeader().getOperatorName());
            tAccountLine.setSmsVar(smsCount);

            //如果短信剩余量足,则扣费
            ServiceResponse serviceResponse = notifyRecordService.deductSmsCount(tAccountLine);
            fail = serviceResponse.isBizError();
        }
        if (!fail) {//如果扣费无异常
            //保存日志
            List<Integer> ids = notifyRecordService.send(dNotifyData.getdNotifyRecords());

            //发送MQ
            notifyRecordService.sendMq(ids, dNotifyData.getdNotifyRecords());

            //另存为邮件模板
            if (dExamineeSendRequest.isWhetherSaveMail()) {
                notifyRecordService.saveMail(ServiceHeaderUtil.getRequestHeader(), dSendNotifyRequest);
            }
            //另存为短信模板
            if (dExamineeSendRequest.isWhetherSaveSms()) {
                notifyRecordService.saveSms(ServiceHeaderUtil.getRequestHeader(), dSendNotifyRequest);
            }
        } else {
            return new ServiceResponse(BizEnums.FAILED_DEDUCTIONS, true);
        }

        return new ServiceResponse();
    }

    @PostMapping({"/notify/send/examiner"})
    public ServiceResponse sendExaminer(DSendNotifyRequest dSendNotifyRequest) throws InvocationTargetException, IllegalAccessException {
        if (!StringUtils.isEmpty(dSendNotifyRequest.getEmailContent())){
            int len = dSendNotifyRequest.getEmailContent().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.EMAIL_TEXT_BEYOUND_LENGTH);
            }
        }
        if (!StringUtils.isEmpty(dSendNotifyRequest.getSign())){
            int len = dSendNotifyRequest.getSign().length();
            if (len > CommonParams.TEXTLENGTH){
                throw new WrappedException(BizEnums.SIGN_TEXT_BEYOUND_LENGTH);
            }
        }
        dSendNotifyRequest.setRoleId(0);
        //通过项目id找评卷人
        List<DReceiverInfo> dReceiverInfos = notifyRecordService.getExaminerList(dSendNotifyRequest.getTaskId(), StringUtil.toStrListByComma(dSendNotifyRequest.getIds()));

        //组装数据
        DNotifyData dNotifyData = notifyRecordService.buildData(dReceiverInfos, dSendNotifyRequest);

        //此次需要发送的短信的条数
        Integer smsCount = dNotifyData.getSmsCount();
        Integer emailCount = dNotifyData.getEmailCount();
        if (emailCount <= 0 && smsCount <= 0) {
            return new ServiceResponse(BizEnums.NO_EMAIL_AND_SMS, true);
        }
        boolean fail = false;
        if (smsCount > 0) {
            //去消费中心查询短信剩余量
            Integer smsBalance = notifyRecordService.getSmsBalance(ServiceHeaderUtil.getRequestHeader().getCompanyId());

            //如果剩余数不够扣当前发送量，则抛出异常
            if (smsCount != 0 && smsBalance - smsCount <= 0) {
                return new ServiceResponse(BizEnums.INSUFFICIENT_NUMBER_OF_REMAINING_SMS, true);
            }
            TAccountLine tAccountLine = new TAccountLine();
            tAccountLine.setModifier(ServiceHeaderUtil.getRequestHeader().getOperatorName());
            tAccountLine.setProjectId(dSendNotifyRequest.getProjectId());
            tAccountLine.setTaskId(dSendNotifyRequest.getTaskId());
            tAccountLine.setType(1);
            tAccountLine.setSmsVar(smsCount);
            //如果短信剩余量足,则扣费
            ServiceResponse serviceResponse = notifyRecordService.deductSmsCount(tAccountLine);
            fail = serviceResponse.isBizError();
        }
        if (!fail) {//如果扣费无异常
            //保存日志
            List<Integer> ids = notifyRecordService.send(dNotifyData.getdNotifyRecords());

            //发送MQ
            notifyRecordService.sendMq(ids, dNotifyData.getdNotifyRecords());

            //另存为邮件模板
            if (dSendNotifyRequest.isWhetherSaveMail()) {
                notifyRecordService.saveMail(ServiceHeaderUtil.getRequestHeader(), dSendNotifyRequest);
            }
            //另存为短信模板
            if (dSendNotifyRequest.isWhetherSaveSms()) {
                notifyRecordService.saveSms(ServiceHeaderUtil.getRequestHeader(), dSendNotifyRequest);
            }
        } else {
            return new ServiceResponse(BizEnums.FAILED_DEDUCTIONS, true);
        }
        return new ServiceResponse();
    }

    @GetMapping({"/notifyRecord/export/{sendType}"})
    public ServiceResponse export(HttpServletResponse response, @PathVariable("sendType") Integer sendType) {
        String exportStaticTemplateXls = "";
        if (sendType == DNotifyRecordSendType.MAIL.getValue()) {
            exportStaticTemplateXls = "/xls/static_export_email_template.xls";
        } else {
            exportStaticTemplateXls = "/xls/static_export_sms_template.xls";
        }

        List list = notifyRecordService.getExportList(sendType);
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        String fileName = "通知日志导出表-" + TimeUtil.formatDateForFileMinute(new Date()) + ".xls";
        try {
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf8"), "iso-8859-1"));
            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream(exportStaticTemplateXls));
            OutputStream os = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            Context context = PoiTransformer.createInitialContext();
            context.putVar("datas", list);
            JxlsHelper.getInstance().processTemplateAtCell(inputXML, bos, context, "静态sheet!A1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ServiceResponse();
    }


    /**
     * 定时任务
     * 30s重发且更新状态
     * 定时更新db状态
     */
    @Scheduled(fixedRate = 30000, initialDelay = 5000)
    protected void scheduleDelivered() {
        if (!scheduleEnable)
            return;
        //查询发送中的状态
        List<DNotifyRecord> dNotifyRecords = notifyRecordService.querySendding();
        //如果正常发送失败，这里定时再重发
        List<Integer> snedIds = new ArrayList<>();
        for (DNotifyRecord dNotifyRecord : dNotifyRecords) {
            snedIds.add(dNotifyRecord.getId());
        }
        //组装发送中的数据
        List<DNotifyRecordStatus> dNotifyRecordStatuses = notifyRecordService.bulidData(dNotifyRecords);
        List<Integer> updateIds = new ArrayList<>();
        for (DNotifyRecordStatus dNotifyRecordStatuse : dNotifyRecordStatuses) {
            updateIds.add(dNotifyRecordStatuse.getId());
        }
        //有更新数据才更新
        if (dNotifyRecordStatuses != null && dNotifyRecordStatuses.size() > 0) {
            //更新发送状态（要么不变，要么成功，要么失败）
            Integer count = notifyRecordService.updateRecordStatus(dNotifyRecordStatuses);
            //update t_user_exam发送状态
            if (updateIds != null && updateIds.size() > 0) {
                Integer count2 = notifyRecordService.updateExamineeStatus(updateIds);
            }

            if (count != null && count <= 0) {
                times++;
            }
        }
        //没有记录5次，查看是否有未发送的,重发
        if (times >= 5) {
            //发送MQ
            notifyRecordService.sendMq(snedIds, dNotifyRecords);
            times = 0;
        }


    }


}
