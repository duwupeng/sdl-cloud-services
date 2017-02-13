package com.talebase.cloud.os.admin.controller;

import com.talebase.cloud.base.ms.admin.dto.DAdmin;
import com.talebase.cloud.base.ms.admin.enums.PermissionCode;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.admin.service.ExportService;
import com.talebase.cloud.base.ms.admin.dto.DUploadFileData;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by daorong.li on 2016-11-28.
 * 导出文件类
 */
@RestController
public class ExportController {
    public static final String exportStaticTemplateXls = "/xls/static_export_template.xls";

    @Autowired
    private ExportService exportService;

    @GetMapping(value = "/admin/export")
    public void export(HttpServletResponse response) throws IOException {
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        //获取当前用户操作权限
//        boolean canEdit  = serviceHeader.hasRight(PermissionCode.ADMIN_LIST_RIGHT.getCode());
//        if (!canEdit){
//            serviceHeader.setOrgCode("");
//        }

        DAdmin dAdmin = new DAdmin();
        dAdmin.setId(serviceHeader.getOperatorId());
        dAdmin.setOrgCode(serviceHeader.getOrgCode());
        ServiceRequest<DAdmin> req = new ServiceRequest<DAdmin>();
        req.setRequest(dAdmin);
        req.setRequestHeader(serviceHeader);

        List<DUploadFileData> fileDatas = exportService.getExportData(req);
        if (fileDatas != null){
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String("管理员列表.xls".getBytes("utf8"),"iso-8859-1"));

            InputStream inputXML = new BufferedInputStream(getClass().getResourceAsStream(exportStaticTemplateXls));
            OutputStream os = response.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);

            Context context = PoiTransformer.createInitialContext();
            context.putVar("users", fileDatas);

            JxlsHelper.getInstance().processTemplateAtCell(inputXML, bos, context, "静态sheet!A1");
        }
    }
}
