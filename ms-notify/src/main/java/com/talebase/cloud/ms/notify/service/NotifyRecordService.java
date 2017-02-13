package com.talebase.cloud.ms.notify.service;

import com.talebase.cloud.base.ms.notify.dto.*;
import com.talebase.cloud.base.ms.notify.domain.TNotifyRecord;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyRecordSendStatus;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.util.BeanConverter;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.common.util.TimeUtil;
import com.talebase.cloud.ms.notify.dao.NotifyRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bin.yang on 2016-11-28.
 */

@Service
@Transactional(readOnly = true)
public class NotifyRecordService {
    @Autowired
    NotifyRecordMapper notifyRecordMapper;

    public List<TNotifyRecord> getAll(ServiceHeader requestHeader, PageRequest pageReq, DPageSearchData dPageSearchData,List<Integer> ids) {
        List<TNotifyRecord> list = notifyRecordMapper.getAll(requestHeader.getOrgCode(), requestHeader.getCompanyId(), requestHeader.getOperatorName(), pageReq, dPageSearchData,ids);
        for (TNotifyRecord tNotifyRecord : list) {
            tNotifyRecord.setSendContent(StringUtil.replaceHtml(tNotifyRecord.getSendContent()));
        }
        return list;
    }

    public Integer getCount(ServiceHeader requestHeader, DPageSearchData dPageSearchData) {
        return notifyRecordMapper.getCount(requestHeader.getOrgCode(), requestHeader.getCompanyId(), requestHeader.getOperatorName(), dPageSearchData);
    }

    public List<DNotifyRecordExport> getExportList(ServiceHeader requestHeader, Integer sendType) throws InvocationTargetException, IllegalAccessException {
        List<DNotifyRecordExport> list = new ArrayList<>();
        List<TNotifyRecord> tNotifyRecords = notifyRecordMapper.getExportList(requestHeader.getOrgCode(), requestHeader.getCompanyId(), requestHeader.getOperatorName(), sendType);
        for (TNotifyRecord tNotifyRecord : tNotifyRecords) {
            DNotifyRecordExport dNotifyRecordExport = new DNotifyRecordExport();
            BeanConverter.copyProperties(dNotifyRecordExport, tNotifyRecord);
            dNotifyRecordExport.setSendDate(TimeUtil.tempDateSecond(new Date(tNotifyRecord.getSendDate().getTime())));
            if (tNotifyRecord.getSendStatus() == TNotifyRecordSendStatus.SENDING.getValue()) {
                dNotifyRecordExport.setSendStatus("发送中");
            } else if (tNotifyRecord.getSendStatus() == TNotifyRecordSendStatus.SEND_FAIL.getValue()) {
                dNotifyRecordExport.setSendStatus("发送失败");
            } else if (tNotifyRecord.getSendStatus() == TNotifyRecordSendStatus.SEND_SUCCESS.getValue()) {
                dNotifyRecordExport.setSendStatus("发送成功");
            }
            dNotifyRecordExport.setSendContent(StringUtil.replaceHtml(tNotifyRecord.getSendContent()));

            list.add(dNotifyRecordExport);
        }

        return list;
    }

    @Transactional(readOnly = false)
    public Integer add(ServiceHeader serviceHeader, DNotifyRecord dNotifyRecord) throws InvocationTargetException, IllegalAccessException {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        TNotifyRecord tNotifyRecord = new TNotifyRecord();
        BeanConverter.copyProperties(tNotifyRecord, dNotifyRecord);
        tNotifyRecord.setCompanyId(serviceHeader.getCompanyId());
        tNotifyRecord.setSendDate(timestamp);
        notifyRecordMapper.insert(tNotifyRecord);
        return tNotifyRecord.getId();
    }

    @Transactional(readOnly = false)
    public List<Integer> addBatch(ServiceHeader serviceHeader, List<DNotifyRecord> dNotifyRecords) throws InvocationTargetException, IllegalAccessException {
        List<Integer> ids = new ArrayList<>();
        for (DNotifyRecord dNotifyRecord : dNotifyRecords) {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            TNotifyRecord tNotifyRecord = new TNotifyRecord();
            BeanConverter.copyProperties(tNotifyRecord, dNotifyRecord);
            tNotifyRecord.setCompanyId(serviceHeader.getCompanyId());
            tNotifyRecord.setSender(serviceHeader.getOperatorName());
            tNotifyRecord.setSendDate(timestamp);
            notifyRecordMapper.insert(tNotifyRecord);
            ids.add(tNotifyRecord.getId());
        }
        return ids;
    }

    /**
     * 更新次数
     *
     * @param idList
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Transactional(readOnly = false)
    public List<DNotifyRecord> updateTimes(List<Integer> idList) throws InvocationTargetException, IllegalAccessException {
        Integer count = notifyRecordMapper.updateTimes(idList);
        return getByIdList(idList);
    }

    @Transactional(readOnly = false)
    public Integer updateStatus(List<DNotifyRecordStatus> dNotifyRecordStatuses) throws InvocationTargetException, IllegalAccessException {
        Integer count = 0;
        for (DNotifyRecordStatus dNotifyRecordStatus : dNotifyRecordStatuses) {
            count += notifyRecordMapper.updateStatus(dNotifyRecordStatus);
        }
        return count;
    }

    public List<DNotifyRecord> querySendding() throws InvocationTargetException, IllegalAccessException {
        List<DNotifyRecord> list = new ArrayList<>();
        List<TNotifyRecord> tNotifyRecords = notifyRecordMapper.querySendding();
        for (TNotifyRecord tNotifyRecord : tNotifyRecords) {
            DNotifyRecord dNotifyRecord = new DNotifyRecord();
            BeanConverter.copyProperties(dNotifyRecord, tNotifyRecord);
            dNotifyRecord.setSendDate(tNotifyRecord.getSendDate().getTime());
            list.add(dNotifyRecord);
        }
        return list;
    }

    public List<DNotifyRecord> getByIdList(List<Integer> idList) throws InvocationTargetException, IllegalAccessException {
        List<DNotifyRecord> list = new ArrayList<>();
        List<TNotifyRecord> tNotifyRecords = notifyRecordMapper.getById(idList);
        for (TNotifyRecord tNotifyRecord : tNotifyRecords) {
            DNotifyRecord dNotifyRecord = new DNotifyRecord();
            BeanConverter.copyProperties(dNotifyRecord, tNotifyRecord);
            dNotifyRecord.setSendDate(tNotifyRecord.getSendDate().getTime());
            list.add(dNotifyRecord);
        }
        return list;
    }
}
