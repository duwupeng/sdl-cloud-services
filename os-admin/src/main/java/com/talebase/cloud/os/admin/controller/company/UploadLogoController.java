package com.talebase.cloud.os.admin.controller.company;

import com.talebase.cloud.base.ms.admin.dto.DCompany;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.common.util.TimeUtil;
import com.talebase.cloud.common.util.UploadFileUtil;
import com.talebase.cloud.os.admin.service.company.UploadLogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by daorong.li on 2016-11-28.
 */
@RestController
public class UploadLogoController {

    @Value("${file.attachmentPath}")
    String attachmentPath;

    @Autowired
    protected UploadLogoService uploadLogoService;

    //,@RequestParam("file") MultipartFile file
    @RequestMapping(value = "/company/upload", method = RequestMethod.POST)
    public ServiceResponse<DCompany> handleFileUpload(MultipartHttpServletRequest req) throws Exception {
        //获取当前操作用户信息
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        MultipartFile file = req.getFile("companyLogo");
        String industryId = req.getParameter("industryId");
        String industryName = req.getParameter("industryName");
        String companyName = req.getParameter("companyName");

        if (file != null && !file.isEmpty()) {
            if (!UploadFileUtil.isImageType(file.getOriginalFilename()))
                return new ServiceResponse<DCompany>(BizEnums.COMPANY_UPLOGO_TYPEERROR);
            if (UploadFileUtil.isImageFileBeyoundSize(file.getSize()))
                return new ServiceResponse<DCompany>(BizEnums.COMPANY_UPLOGO_SIZEEERROR);

            //时间戳作为上传名字
            String fileName = TimeUtil.getTimestamp() + "." + UploadFileUtil.fileType(file.getOriginalFilename());
            //String savePath = UploadFileUtil.imageSavePath(getTargetPath());
            String savePath = getSystemImagePath();

            File saveFile = new File(savePath);
            saveFile.mkdirs();
            /*if (!saveFile.isDirectory())
                saveFile.mkdirs(); */

            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(savePath,fileName)));
            out.write(file.getBytes());
            out.flush();
            out.close();

            DCompany dCompany = new DCompany();
            dCompany.setId(serviceHeader.getCompanyId());
            dCompany.setLogo(fileName);
            dCompany.setIndustryId(Integer.valueOf(industryId));
            dCompany.setIndustryName(industryName);
            dCompany.setName(companyName);

            ServiceResponse<DCompany> response = uploadLogoService.updateLogo(dCompany);
            DCompany company = response.getResponse();

            if (company != null) {
                File delFile = new File(savePath +File.separator+ company.getLogo());
                if (delFile.exists())
                    delFile.delete();
            }

            return new ServiceResponse<DCompany>();
        } else {
            DCompany dCompany = new DCompany();
            dCompany.setId(serviceHeader.getCompanyId());
            dCompany.setLogo(null);
            dCompany.setIndustryId(industryId == null ? null : Integer.valueOf(industryId));
            dCompany.setIndustryName(industryName);
            dCompany.setName(companyName);
            uploadLogoService.updateLogo(dCompany);
            //return new ServiceResponse<DCompany>(BizEnums.COMPANY_LOGO_NULL);
            return new ServiceResponse<DCompany>();
        }
    }


  /*  private String getTargetPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").getPath().replaceAll("/classes/", "") + "/upload/logo/";
        return path;
    }*/

    private String getSystemImagePath(){
        ServiceHeader serviceHeader = ServiceHeaderUtil.getRequestHeader();
        String path = attachmentPath +"companyLogo"+File.separator+serviceHeader.getCompanyId();
        return path;
    }
}
