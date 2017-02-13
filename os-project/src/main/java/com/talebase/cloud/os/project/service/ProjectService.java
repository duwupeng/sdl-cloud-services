package com.talebase.cloud.os.project.service;

import com.talebase.cloud.base.ms.project.domain.TProjectAdmin;
import com.talebase.cloud.base.ms.project.dto.*;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import com.talebase.cloud.os.project.util.QRCodeKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by kanghong.zhao on 2016-12-2.
 */
@Service
public class ProjectService {

    final static String SERVICE_NAME = "ms-project";
    private static Logger log = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    private MsInvoker msInvoker;

    @Value("${codePath}")
    private String codePath ;

    public ServiceResponse updateProjectStatus(Integer projectId, Integer newStatus){
        String servicePath = "http://" + SERVICE_NAME + "/project/status/" + projectId;
        ServiceResponse response = msInvoker.put(servicePath, new ServiceRequest<Integer>(ServiceHeaderUtil.getRequestHeader(),  newStatus), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse deleteProject(Integer projectId){
        String servicePath = "http://" + SERVICE_NAME + "/project/" + projectId;
        ServiceResponse response = msInvoker.delete(servicePath, new ServiceRequest<Integer>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        return response;
    }

    public ServiceResponse<Integer> createProject(HttpServletRequest request,DProjectEditReq editReq){
        String servicePath = "http://" + SERVICE_NAME + "/project";
        ServiceResponse<Integer> response = msInvoker.post(servicePath, new ServiceRequest<DProjectEditReq>(ServiceHeaderUtil.getRequestHeader(), editReq), new ParameterizedTypeReference<ServiceResponse<Integer>>(){});
        //生成二维码
        if(editReq.getScanEnable()){
            String companyDomain = ServiceHeaderUtil.getRequestHeader().getCompanyDomain();
            String data = "http://"+ companyDomain +"/exam/login?projectId="+response.getResponse();
            File logo = new File(codePath+"file/logo.png");

            log.info(String.format("logo path - > %s",logo.getAbsoluteFile()));

            BufferedImage image = QRCodeKit.createQRCodeWithLogo(data,1000,1000,logo);
            String imageBase64String = QRCodeKit.getImageBase64String(image);
            try {
                String imageName = "code"+response.getResponse()+".jpg";
                String imagePath = codePath+imageName;
                File file = new File(imagePath);
                file.getParentFile().mkdirs();

                log.info(String.format("image path - > %s",file.getParentFile()));

                if (!file.exists())
                    file.createNewFile();
                //ImageIO.write(image, "jpg", new File(imagePath));
                GenerateImage(imageBase64String,imagePath);
            }catch (Exception e){
                e.printStackTrace();
            }

            editReq.setScanImage(imageBase64String);
            String servicePathu = "http://" + SERVICE_NAME + "/project/" + response.getResponse();
            ServiceResponse responseu = msInvoker.put(servicePathu, new ServiceRequest<DProjectEditReq>(ServiceHeaderUtil.getRequestHeader(), editReq), new ParameterizedTypeReference<ServiceResponse<String>>(){});
        }

        return response;
    }

    public ServiceResponse<Integer> updateProject(HttpServletRequest request,Integer projectId, DProjectEditReq editReq) {
        String servicePath = "http://" + SERVICE_NAME + "/project/" + projectId;
        //生成二维码
        if (editReq.getScanEnable()) {
            String companyDomain = ServiceHeaderUtil.getRequestHeader().getCompanyDomain();
            String data = "http://" + companyDomain + "/exam/login?projectId=" + projectId;
            File logo = new File(codePath+"file/logo.png");

            BufferedImage image = QRCodeKit.createQRCodeWithLogo(data, 1000, 1000, logo);
            String imageBase64String = QRCodeKit.getImageBase64String(image);
            try {
                String imageName = "code" + projectId + ".jpg";
                String imagePath = codePath + imageName;
                File file = new File(imagePath);
                file.getParentFile().mkdirs();
                if (!file.exists())
                    file.createNewFile();
                //ImageIO.write(image, "jpg", new File(imagePath));
                GenerateImage(imageBase64String, imagePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

            editReq.setScanImage(imageBase64String);
           }
            ServiceResponse response = msInvoker.put(servicePath, new ServiceRequest<DProjectEditReq>(ServiceHeaderUtil.getRequestHeader(), editReq), new ParameterizedTypeReference<ServiceResponse<String>>() {
            });
            return response;

    }

    public PageResponse<DProject> query(DProjectQueryReq queryReq, PageRequest pageRequest){
        String servicePath = "http://" + SERVICE_NAME + "/projects/query";
        ServiceResponse<PageResponse<DProject>> response = msInvoker.post(servicePath, new ServiceRequest<DProjectQueryReq>(ServiceHeaderUtil.getRequestHeader(), queryReq, pageRequest), new ParameterizedTypeReference<ServiceResponse<PageResponse<DProject>>>(){});
        return response.getResponse();
    }

    public ServiceResponse<DProjectInEdit> getProjectInEdit(Integer projectId){
        String servicePath = "http://" + SERVICE_NAME + "/project/edit/" + projectId;
        ServiceResponse<DProjectInEdit> response = msInvoker.post(servicePath, new ServiceRequest<DProjectQueryReq>(ServiceHeaderUtil.getRequestHeader()), new ParameterizedTypeReference<ServiceResponse<DProjectInEdit>>(){});
        return response;
    }

    public ServiceResponse<Integer> copyProject(DProjectCopyReq copyReq, ServiceHeader serviceHeader){
        String servicePath = "http://" + SERVICE_NAME + "/project/copy";
        ServiceResponse<Integer> response = msInvoker.post(servicePath, new ServiceRequest(serviceHeader, copyReq), new ParameterizedTypeReference<ServiceResponse<Integer>>(){});
        return response;
    }

    public List<TProjectAdmin> findProjectAdmin(Integer projectId){
        String servicePath = "http://" + SERVICE_NAME + "/project/admins/{projectId}";
        ServiceResponse<List<TProjectAdmin>> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<List<TProjectAdmin>>>(){}, projectId);
        return response.getResponse();
    }

    public static boolean GenerateImage(String imgStr, String imgFilePath) {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
