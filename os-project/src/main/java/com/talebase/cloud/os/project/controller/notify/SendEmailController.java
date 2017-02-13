package com.talebase.cloud.os.project.controller.notify;

import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.base.ms.common.enumes.TEmailLogStatus;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.os.project.service.notify.EmailService;
import com.talebase.cloud.os.project.service.notify.RabbitEmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daorong.li on 2016-11-30.
 */
@RestController
public class SendEmailController {

    @Autowired
    private RabbitEmailSenderService rabbitEmailSenderService;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/sendEmail")
    public ServiceResponse sendEmail(HttpServletRequest request){
        List<DEmailLog> emailList = new ArrayList<DEmailLog>();

        StringBuffer sb  = new StringBuffer();
        sb.append("<h3>你好， 阿荣, 现在邀请你测试</h3>")
                .append("<p>账号：test001</p>")
                .append("<p>密码：123456</p>")
                .append("<p><a href=\"http://www.baidu.com\">http://www.baidu.com</a></p>");

        DEmailLog record = new DEmailLog();
        record.setTableId(1);
        record.setTableName("t_notify_record");
        record.setEmail("daorong.li@talebase.com");
        record.setSendContent(sb.toString());
        record.setSubject("测试标题");
        record.setSender("service@talebase.com");//不填系统默认是service@talebase.com
        emailList.add(record);
        rabbitEmailSenderService.sendEmail(emailList);
        return new ServiceResponse();
    }

    @GetMapping(value = "/getEmails")
    public ServiceResponse getEmails(HttpServletRequest request){
        DEmailLog log = new DEmailLog();
        log.setTableNames("t_notify_record,t_email_log");//查询多个表短信用逗号隔开
        log.setTableIds("1,2,3");//查询多个id用逗号隔开
        //查询成功或者失败都用逗号隔开 或者不设值
        log.setStatuss(TEmailLogStatus.FAILURE.getValue()+","+TEmailLogStatus.SUCCESS.getValue());

        ServiceRequest<DEmailLog> req = new ServiceRequest<DEmailLog>();
        req.setRequest(log);
        ServiceResponse<List<DEmailLog>>  list = emailService.getEmails(req);
        return  new ServiceResponse();
    }
}
