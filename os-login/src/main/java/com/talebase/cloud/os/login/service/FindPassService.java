package com.talebase.cloud.os.login.service;

import com.talebase.cloud.base.ms.admin.domain.TAdmin;
import com.talebase.cloud.base.ms.admin.enums.TAdminStatus;
import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.exception.BizEnums;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.Des3Util;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suntree.xu on 2016-12-15.
 */
@Service
public class FindPassService {

    final static String ADMIN_SERVICE_NAME = "ms-admin";
    @Autowired
    MsInvoker msInvoker;
    @Autowired
    RabbitEmailSenderService rabbitEmailSenderService;

    public ServiceResponse findPass(String account,String email) throws Exception {
        ServiceResponse response = new ServiceResponse();
        String servicePath = "http://" + ADMIN_SERVICE_NAME + "/serviceAdmin/getAdminsByAccounts";
        ServiceRequest<List<String>> request = new ServiceRequest<List<String>>();
        List<String> array = new ArrayList<String>();
        array.add(account);
        request.setRequest(array);
        //根据账号查询用户信息
        ServiceHeader header = ServiceHeaderUtil.getRequestHeader();
        request.setRequestHeader(header);
        ServiceResponse<List<TAdmin>> adminServiceResponseList = msInvoker.post(servicePath,request,new ParameterizedTypeReference<ServiceResponse<List<TAdmin>>>(){});
        List<TAdmin> admins = new ArrayList<>();

        if(adminServiceResponseList.getResponse() != null && adminServiceResponseList.getResponse().size() > 0){
            for(TAdmin admin : adminServiceResponseList.getResponse()){
                if(admin.getStatus().equals(TAdminStatus.EFFECTIVE.getValue()))
                    admins.add(admin);
            }
        }

        if(admins.size() == 0){
            throw new WrappedException(BizEnums.AdminLoginDisable);
        }else {
            TAdmin admin = admins.get(0);
            if(admin.getEmail().equals(email)){
                //发送邮件
                sendEmail(admin);
                response.setMessage("已发送密码到指定邮箱，请注意查收");
                response.setBizError(false);
                return response;
            }else{
                throw new WrappedException(BizEnums.EmailDisable);
            }
        }
    }

    /**
     * 采用mq发送邮件
     * @param tAdmin
     */
    public void sendEmail(TAdmin tAdmin) throws Exception {
    List<DEmailLog> emailList = new ArrayList<DEmailLog>();

    StringBuffer sb  = new StringBuffer();
    sb.append("<h3>尊敬的").append(tAdmin.getAccount()).append(":</h3>")
            .append("<p>您好！以下是您的帐号信息，请您妥善保管。谢谢！<p>")
            .append("<p>-------------------------------------------------------<p>")
            .append("<p>帐号：").append(tAdmin.getAccount()).append("</p>")
            .append("<p>密码：").append(Des3Util.des3DecodeCBC(tAdmin.getPassword())).append("</p>")
            .append("<p>姓名：").append(tAdmin.getName()).append("</p>")
            .append("<p>-------------------------------------------------------<p>");
    DEmailLog record = new DEmailLog();
    record.setTableId(1);
    record.setTableName("t_notify_record");
    record.setEmail(tAdmin.getEmail());
    record.setSendContent(sb.toString());
    record.setSubject("找回密码");
    emailList.add(record);
    rabbitEmailSenderService.sendEmail(emailList);
}

}