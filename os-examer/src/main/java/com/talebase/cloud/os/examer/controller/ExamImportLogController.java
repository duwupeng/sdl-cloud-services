package com.talebase.cloud.os.examer.controller;

import com.google.gson.reflect.TypeToken;
import com.talebase.cloud.base.ms.examer.domain.TUserImportRecord;
import com.talebase.cloud.base.ms.examer.domain.TUserShowField;
import com.talebase.cloud.base.ms.examer.dto.DProjectTaskReq;
import com.talebase.cloud.base.ms.examer.dto.DTUserImportLogEx;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecord;
import com.talebase.cloud.base.ms.examer.dto.DUserImportRecordQueryReq;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldIsshow;
import com.talebase.cloud.base.ms.examer.enums.TUserShowFieldType;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.PageRequest;
import com.talebase.cloud.common.protocal.PageResponse;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.*;
import com.talebase.cloud.os.examer.service.ExamProjectService;
import com.talebase.cloud.os.examer.service.ExamService;
import org.apache.commons.io.FilenameUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by kanghong.zhao on 2016-12-7.
 */
@RestController
public class ExamImportLogController {

    @Autowired
    private ExamService examService;
    @Autowired
    private ExamProjectService projectService;

    @GetMapping("/examer/importLog/query")
    public ServiceResponse<PageResponse<DUserImportRecord>> queryImportLog(DUserImportRecordQueryReq req, PageRequest pageRequest){
        return examService.queryImportLog(req, pageRequest);
    }

    @GetMapping("/examer/failLog/export")
    public ServiceResponse exportFailLogs(HttpServletRequest request,String batchNo, HttpServletResponse servletResponse) throws IOException {
        DTUserImportLogEx ex = examService.queryImportLog(batchNo);

        List<String> headerStrs = toList(ex.getTitleJson());

        List<List<String>> contentsStrs = new ArrayList<>();
        for(TUserImportRecord record : ex.getFailRecords()){
            if(record.getDetailJson() != null && !record.getDetailJson().trim().equals("")){
                List<String> contentStrs = toList(record.getDetailJson());
                contentsStrs.add(contentStrs);
            }
        }

        Map map = new HashMap<>();
        map.put("headers", headerStrs);
        map.put("data", contentsStrs);

        InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/xls/grid_template.xls"));

        JxlsUtil.write(request,inputXML, servletResponse, map, "失败日志.xls", "sheet2!A1");
        return new ServiceResponse();
    }

    private List<String> toList(String json){
        return GsonUtil.fromJson(json, new TypeToken<List<String>>(){}.getType());
    }

    @GetMapping("/examer/demo/download")
    public ServiceResponse downloadDemo(HttpServletRequest request,DProjectTaskReq req, HttpServletResponse servletResponse) throws IOException {

        List<TUserShowField> fields = req == null ?
                examService.getUserFields(ServiceHeaderUtil.getRequestHeader().getCompanyId(), null, null) : examService.getUserFields(ServiceHeaderUtil.getRequestHeader().getCompanyId(), req.getProjectId(), req.getTaskId());

        List<String> headers = new ArrayList<>();
        List<String> remarks = new ArrayList<>();

        for(TUserShowField field : fields){
            if(field.getIsshow().equals(TUserShowFieldIsshow.DISPLAY.getValue())){
                String filedName = field.getFieldName();
                headers.add(filedName);

                if(field.getType() != null && field.getType().intValue() == TUserShowFieldType.DATE_TYPE.getValue()){
                    remarks.add("字段[" + filedName + "]请填入日期类型，格式为'年年年年-月月-日日'");
                }else if(field.getType() != null && field.getType().intValue() == TUserShowFieldType.SELECT_TYPE.getValue()){
//                    List<String> selectStrs = toList(gson, field.getSelectValue());
//                    remarks.add("字段[" + filedName + "]请在写入下列选项[" + StringUtil.toStrByComma(selectStrs) + "]");
                    remarks.add("字段[" + filedName + "]请在写入下列选项[" + field.getSelectValue() + "]");
                }
            }

        }

        Map map = new HashMap<>();
        map.put("headers", headers);
        map.put("remarks", remarks);

        InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream("/xls/dynamic_demo.xls"));
        JxlsUtil.write(request,inputXML, servletResponse, map, "导入模板.xls", "sheet2!A1");
        return new ServiceResponse();
    }

    @PostMapping("/examer/import")
    public ServiceResponse importExamers(@RequestParam("file")MultipartFile[] files, DProjectTaskReq req) throws Exception {
        MultipartFile file = files[0];
        //拆解xls成str数组
        List<List<String>> list = PoiUtil.readExcelForList(file.getInputStream(), FilenameUtils.getExtension(file.getOriginalFilename()));

        List<String> headers = list.get(0);

        Integer accountIdx = null;
        for(int i = 0; i < headers.size(); i++){
            if("帐号".equals(headers.get(i))){
                accountIdx = i;
                break;
            }
        }
        if(accountIdx == null)
            throw new WrappedException(BizEnums.ImportLack);

        List<List<String>> listItems = new ArrayList<>();

        for(int i = 1; i < list.size(); i++){
            List<String> items = list.get(i);
            if(items == null || items.size() == 0)
                continue;//空行跳过
            if(items.get(0).indexOf("备注") >= 0){
                break;
            }else if(StringUtil.isEmpty(items.get(accountIdx))){
                break;
            }else{
                listItems.add(items);
            }
        }

        if(listItems.size() == 0){
            throw new WrappedException(BizEnums.ImportLack);
        }

        ServiceHeader header =  ServiceHeaderUtil.getRequestHeader();

        //xls文件没大问题，则开异步线程对数据进行批评导入
        ExecutorService service = Executors.newFixedThreadPool(1);
        Future<?> future = service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> accounts = examService.importData(req.getProjectId(), req.getTaskId(), headers, listItems, header);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return new ServiceResponse();
    }

}
