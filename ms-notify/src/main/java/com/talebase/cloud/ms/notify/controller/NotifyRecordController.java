package com.talebase.cloud.ms.notify.controller;

import com.talebase.cloud.base.ms.notify.domain.TNotifyRecord;
import com.talebase.cloud.base.ms.notify.dto.*;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.StringUtil;
import com.talebase.cloud.ms.notify.service.NotifyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bin.yang on 2016-11-28.
 */

@RestController
public class NotifyRecordController {

    @Autowired
    NotifyRecordService notifyRecordService;

    @PostMapping({"/notifyRecords"})
    public ServiceResponse<PageResponse<DNotifyRecord>> getNotifyRecords(@RequestBody ServiceRequest<DPageSearchData> req) {
        ServiceHeader requestHeader = req.getRequestHeader();
        PageRequest pageReq = req.getPageReq();
        DPageSearchData dPageSearchData = req.getRequest();
        PageResponse<List<DNotifyRecord>> page = new PageResponse<List<DNotifyRecord>>();
        List list = notifyRecordService.getAll(requestHeader, pageReq, dPageSearchData, null);
        Integer count = notifyRecordService.getCount(requestHeader, dPageSearchData);
        page.setResults(list);
        page.setStart(pageReq.getStart());
        page.setTotal(count);
        page.setPageIndex(pageReq.getPageIndex());
        page.setLimit(pageReq.getLimit());
        return new ServiceResponse(page);
    }

    @PostMapping({"/notifyRecord/getIdList"})
    public ServiceResponse<List<Integer>> getIdList(@RequestBody ServiceRequest<DPageSearchData> req) {
        ServiceHeader requestHeader = req.getRequestHeader();
        DPageSearchData dPageSearchData = req.getRequest();
        PageRequest pageReq = new PageRequest();
        pageReq.setLimit(100000);
        pageReq.setPageIndex(1);
        List<Integer> ids = StringUtil.toIntListByComma(dPageSearchData.getIds());
        List<Integer> idList = new ArrayList<>();
        List<TNotifyRecord> list = notifyRecordService.getAll(requestHeader, pageReq, dPageSearchData, ids);
        for (TNotifyRecord tNotifyRecord : list) {
            idList.add(tNotifyRecord.getId());
        }
        return new ServiceResponse(idList);
    }

    @PostMapping({"/notifyRecord/reSend"})
    public ServiceResponse queryByIdLlist(@RequestBody ServiceRequest<List<Integer>> req) throws InvocationTargetException, IllegalAccessException {
        List<DNotifyRecord> dNotifyRecords = notifyRecordService.getByIdList(req.getRequest());
        return new ServiceResponse(dNotifyRecords);
    }

    @PostMapping({"/notifyRecord/add"})
    public ServiceResponse create(@RequestBody ServiceRequest<DNotifyRecord> req) throws InvocationTargetException, IllegalAccessException {
        Integer id = notifyRecordService.add(req.getRequestHeader(), req.getRequest());
        return new ServiceResponse(id);
    }

    @PostMapping({"/notifyRecord/addBatch"})
    public ServiceResponse addBatch(@RequestBody ServiceRequest<List<DNotifyRecord>> req) throws InvocationTargetException, IllegalAccessException {
        List<Integer> ids = notifyRecordService.addBatch(req.getRequestHeader(), req.getRequest());
        return new ServiceResponse(ids);
    }

    @PostMapping({"/notifyRecord/list/{sendType}"})
    public ServiceResponse getExportList(@RequestBody ServiceRequest req, @PathVariable("sendType") Integer sendType) throws InvocationTargetException, IllegalAccessException {
        List<DNotifyRecordExport> dNotifyRecords = notifyRecordService.getExportList(req.getRequestHeader(), sendType);
        return new ServiceResponse(dNotifyRecords);
    }

    @PutMapping({"/notifyRecord/update/times"})
    public ServiceResponse updateTimes(@RequestBody ServiceRequest<List<Integer>> req) throws InvocationTargetException, IllegalAccessException {
        List<DNotifyRecord> dNotifyRecords = notifyRecordService.updateTimes(req.getRequest());
        return new ServiceResponse(dNotifyRecords);
    }

    @PutMapping({"/notifyRecord/update/status"})
    public ServiceResponse updateStatus(@RequestBody ServiceRequest<List<DNotifyRecordStatus>> req) throws InvocationTargetException, IllegalAccessException {
        Integer count = notifyRecordService.updateStatus(req.getRequest());
        return new ServiceResponse(count);
    }

    @GetMapping({"/notifyRecords/sendding"})
    public ServiceResponse querySendding() throws InvocationTargetException, IllegalAccessException {
        List<DNotifyRecord> dNotifyRecords = notifyRecordService.querySendding();
        return new ServiceResponse(dNotifyRecords);
    }
}
