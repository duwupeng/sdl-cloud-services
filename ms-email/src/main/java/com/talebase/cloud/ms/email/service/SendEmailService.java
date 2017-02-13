package com.talebase.cloud.ms.email.service;



import com.talebase.cloud.base.ms.common.domain.TEmailLog;
import com.talebase.cloud.base.ms.common.dto.DEmailLog;
import com.talebase.cloud.base.ms.common.enumes.TEmailLogStatus;
import com.talebase.cloud.ms.email.dao.EmailMapper;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.util.*;

/**
 * Created by daorong.li on 2016-11-15.
 */
@Service
public class SendEmailService {


    private JavaMailSender jSender =  new JavaMailSenderImpl() ;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Autowired
    private EmailMapper emailMapper;
    private static String mailTo ="service@talebase.com,system@talebase.com,system01@talebase.com,system02@talebase.com,system03@talebase.com,system04@talebase.com,system05@talebase.com,system06@talebase.com,system07@talebase.com,system08@talebase.com,system09@talebase.com,system10@talebase.com,system11@talebase.com,system12@talebase.com,system13@talebase.com,system14@talebase.com,system15@talebase.com";
    private static List<String>  sendList = new ArrayList<>();

    public  String getSender(){
        if (sendList.isEmpty()){
            String sender[] = mailTo.split(",");
            for (int i=0;i<sender.length;i++){
                sendList.add(sender[i]);
            }
        }
        String sender = sendList.get(0);
        sendList.remove(0);
        return sender;
    }

    /**
     * 发送邮件
     * @param emailList
     */
    public void sendEmail(List<DEmailLog> emailList){
        if (!emailList.isEmpty()){
            for (int i=0;i<emailList.size();i++){
                DEmailLog email = emailList.get(i);
                email.setSender(getSender());
                boolean flag = SendEmailByFtl(email);
                email.setStatus(flag? TEmailLogStatus.SUCCESS.getValue()
                        :TEmailLogStatus.FAILURE.getValue());
                saveEmail(email);
            }
        }
    }

    public List<TEmailLog> getEmails(DEmailLog dEmailLog){
        List<String> tableNames = new ArrayList<String>();
        List<String> tableIds = new ArrayList<String>();
        List<String> statuss = new ArrayList<String>();

        if (!StringUtils.isEmpty(dEmailLog.getTableNames())){
            String[] tableName = dEmailLog.getTableNames().split(",");
            for (int i=0;i<tableName.length;i++){
                tableNames.add(tableName[i]);
            }
        }
        if (!StringUtils.isEmpty(dEmailLog.getTableIds())){
            String[] tableId = dEmailLog.getTableIds().split(",");
            for (int i=0;i<tableId.length;i++){
                tableIds.add(tableId[i]);
            }
        }
        if (!StringUtils.isEmpty(dEmailLog.getStatuss())){
            String[] status = dEmailLog.getStatuss().split(",");
            for (int i=0;i<status.length;i++){
                statuss.add(status[i]);
            }
        }

        List<TEmailLog> list = emailMapper.getEmails(tableNames,tableIds,statuss);
        return list;
    }


    public void saveEmail(DEmailLog email){
        try{
            emailMapper.insert(email);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 通过freemarker发送邮件
     * @param email
     * @return
     */
    public boolean SendEmailByFtl(DEmailLog email){
        try{
            //设置邮件服务主机
            ((JavaMailSenderImpl)jSender).setHost("smtp.talebase.com");
            //发送者邮箱的用户名
            ((JavaMailSenderImpl)jSender).setUsername(email.getSender());
            //发送者邮箱的密码
            ((JavaMailSenderImpl)jSender).setPassword("T@leb@$e01!");

            //配置文件，用于实例化java.mail.session
            Properties pro = System.getProperties();

            //登录SMTP服务器,需要获得授权，网易163邮箱新近注册的邮箱均不能授权。
            //测试 sohu 的邮箱可以获得授权
            pro.put("mail.smtp.auth", "true");
            pro.put("mail.smtp.socketFactory.port", "25");
            pro.put("mail.smtp.socketFactory.fallback", "false");
            //通过文件获取信息
            ((JavaMailSenderImpl)jSender).setJavaMailProperties(pro);

            MimeMessage mimeMessage = jSender.createMimeMessage();
            MimeMessageHelper helper = getHelper(mimeMessage,email);

            freeMarkerConfigurer.setTemplateLoaderPath("classpath:templates");
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("email.ftl");//加载资源文件
            //将ftl模板转为html
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, getParaMOfTypeMap(email));
            helper.setText(html, true);

            //发送邮件
            Send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public MimeMessageHelper getHelper(MimeMessage mimeMessage,DEmailLog email){
        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"UTF-8");
            //基本设置.
            helper.setFrom(email.getSender());//发送者.
            helper.setTo(email.getEmail());//接收者.
            helper.setSubject(email.getSubject());//邮件主题.
            return helper;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取邮件参数
     * @param email
     * @return
     */
    public Map<String,Object> getParaMOfTypeMap(DEmailLog email){
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("content",email.getSendContent());
        return model;
    }

    /**
     * 发邮件
     * @param mimeMessage
     */
    public void Send(MimeMessage mimeMessage){
        jSender.send(mimeMessage);
    }
}
