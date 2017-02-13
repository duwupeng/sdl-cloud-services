package com.talebase.cloud.os.login.service;

import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.common.exception.WrappedException;
import com.talebase.cloud.common.protocal.ServiceResponse;
import com.talebase.cloud.common.util.DateUtil;
import com.talebase.cloud.os.login.bind.CodeRule;
import com.talebase.cloud.os.login.bind.ImageInfo;
import com.talebase.cloud.os.login.bind.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by suntree.xu on 2016-12-16.
 */
@Service
public class VerificationService {

    @Value("${verifyCode_time}")
    String verifyCode_time ;
    @Value("${verifyCode_count}")
    String verifyCode_count ;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RabbitSmsSenderService rabbitSmsSenderService;

    public ServiceResponse sendVerifyCode(String phoneNo){
        ServiceResponse response = new ServiceResponse();
        //判断是否禁止发送验证码
        String[] verifyCode_timeArray = verifyCode_time.split(",");
        String[] verifyCode_countArray = verifyCode_time.split(",");
       // List<String> codeList = redisTemplate.
        List<CodeRule> codeList = (List<CodeRule>)redisTemplate.opsForValue().get(phoneNo);
        boolean isSend = false;
        if(codeList != null && codeList.size()>0){
            //按照规则判断
            for(int j=0;j<verifyCode_timeArray.length;j++){
                int minTime = Integer.parseInt(verifyCode_timeArray[j]);
                int count = Integer.parseInt(verifyCode_countArray[j]);
                int sendCount = 0;
                Date now = new Date();
                Date before = DateUtil.addMinute(now,-minTime);
                for(int i=0;i<codeList.size();i++){
                    if(codeList.get(i).getCreateTime().after(before)){
                        sendCount++;
                    }
                }
                if(sendCount<count){
                    //发送段短信，并记录在Redis
                    //sendSmsAndSave(codeList,phoneNo);
                    isSend = true;
                }else{
                    isSend = false;
                    response.setCode(1);
                    response.setBizError(true);
                    response.setMessage("发送短信过于频繁，请稍后尝试！");
                    break;
                }
            }
        }else{
            //发送段短信，并记录在Redis
            //sendSmsAndSave(codeList,phoneNo);
            isSend = true;
        }
        if(isSend){
            try{
                sendSmsAndSave(codeList,phoneNo);
            }catch (WrappedException e){
                response.setCode(e.getErrCode());
                response.setBizError(true);
                response.setMessage(e.getErrMsg());
            }
        }
        return response;
    };


    public void sendSmsAndSave(List<CodeRule> codeList,String phoneNo){
        TSmsInfo smsInfo = new TSmsInfo();
        smsInfo.setGuid(UUID.randomUUID().toString());
        smsInfo.setSendto(phoneNo);
        CodeRule codeRule = new CodeRule();
        codeRule.setCreateTime(new Date());
        if(codeList == null){
            codeList = new ArrayList<>();
        }
        if(codeList.size()==0){
            String code = createRandom(true,6);
            smsInfo.setContent("请输入验证码"+code+"完成手机验证（5分钟内有效）。如非本人操作请忽略。");
            codeRule.setVerifyCode(code);
            codeList.add(codeRule);

        }else{
            Date now = new Date();
            Date before = DateUtil.addMinute(now,-15);
            if(before.before(codeList.get(codeList.size()-1).getCreateTime())){
                smsInfo.setContent("请输入验证码"+codeList.get(codeList.size()-1).getVerifyCode()+"完成手机验证（5分钟内有效）。如非本人操作请忽略。");
                codeRule.setVerifyCode(codeList.get(codeList.size()-1).getVerifyCode());
                codeList.add(codeRule);
            }else{
                String code = createRandom(true,6);
                smsInfo.setContent("请输入验证码"+code+"完成手机验证（5分钟内有效）。如非本人操作请忽略。");
                codeRule.setVerifyCode(code);
                codeList.add(codeRule);
            }
        }
        rabbitSmsSenderService.smsSender(smsInfo);
        redisTemplate.opsForValue().set(phoneNo,codeList);
    }

/**
    * 创建指定数量的随机字符串
    * @param numberFlag 是否是数字
    * @param length
    * @return
 */
    public static String createRandom(boolean numberFlag, int length){
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                    }
                retStr += strTable.charAt(intR);
                }
            if (count >= 2) {
                bDone = false;
                }
            } while (bDone);
        return retStr;
    }

    public ServiceResponse<ImageInfo> getValidateCode(){
        ServiceResponse<ImageInfo> response = new ServiceResponse<ImageInfo>();
        ImageInfo imageInfo = new ImageInfo();
        ValidateCode validateCode = new ValidateCode();
        String code = validateCode.getCode();
        BufferedImage bufferedImage = validateCode.getBuffImg();
        BASE64Encoder encoder = new BASE64Encoder();
        String codeImage = encoder.encode(imageToBytes(bufferedImage,"jpeg"));
        imageInfo.setBase64Image(codeImage);
        imageInfo.setCode(code);
        response.setResponse(imageInfo);
        return response;
    }

    /**
     * 转换BufferedImage 数据为byte数组
     *
     * @param bImage
     * Image对象
     * @param format
     * image格式字符串.如"gif","png"
     * @return byte数组
     */
    public static byte[] imageToBytes(BufferedImage bImage, String format) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, format, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}

