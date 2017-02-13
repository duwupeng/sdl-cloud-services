package com.talebase.cloud.ms.sms.service;

import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.ms.sms.dao.SmsChannelMapper;
import com.talebase.cloud.ms.sms.dao.SmsMapper;
import com.talebase.cloud.ms.sms.model.*;
import com.talebase.cloud.ms.sms.mq.bind.Barista;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by suntree.xu on 2016-11-30.
 */
@Repository
public class SmsSender {
    Logger logger = Logger.getLogger(SmsSender.class);
    List<SmsChannel> smsChannels = new ArrayList<>();
    @Autowired
    SmsChannelMapper smsChannelMapper;
    @Autowired
    SendHelper sendHelper;
    @Autowired
    SmsMapper smsDao;
    @Value("${sms.send.maxTry}")
    int MAX_TRY_SEND = 1;
    @Value("${spring.application.name}")
    String server_name;
    @Value("${schedule.enable}")
    Boolean scheduleEnable;


   /* @StreamListener(Barista.INPUT_CHANNEL_ANOTHER)
    public void receiverAnotherChannel(Message<Object> message){
        Object obj = message.getPayload();
        System.out.println("接受对象AnotherChannel:"+obj+"\n");
    }*/

    public void senSms(TSmsInfo smsInfo){

        boolean set = dispatchSms(smsInfo);

    }

    protected boolean dispatchSms(TSmsInfo smsInfo) {
        logger.debug("dispatchSms...");
        logger.info("send sms...");
        SmsChannel channel = selectChannel(smsInfo);
        if (channel == null) {
            logger.info("没有可用的供应商");
            return false;
        }
        DSmsInfo dsmsInfo = new DSmsInfo(smsInfo);
        DSmsSendResult dSmsSendResult = new DSmsSendResult();
        // 发送
/*        switch (channel.getServerName()){
            case "ms-sms-haobo":
                dSmsSendResult=sendHelper.sendUseHaobo(dsmsInfo);
        }*/
        if(channel.getProviderName().equals("昊博")){
            dSmsSendResult=sendHelper.sendUseHaobo(dsmsInfo);
        }else{
            //其他供应商，和数据库字段对应

        }
        // 发送计数
        smsInfo.setSendCount(smsInfo.getSendCount() + 1);
        smsInfo.setSendTime(dSmsSendResult.getSendTime());
        smsInfo.setSendId(dSmsSendResult.getSendId());
        smsInfo.setStatus(TSmsInfo.SMS_SENDING);
        smsDao.insert(smsInfo);
        return true;
    }
    /**
     * 选择发送通道
     *
     * @param smsInfo
     * @return
     */
    protected SmsChannel selectChannel(TSmsInfo smsInfo) {
        if (smsChannels == null || smsChannels.size() <= 0) {
            return null;// throw?
        }

        if (smsInfo.getChannelName() != null && smsInfo.getChannelName().length() > 0) {
            // 指定通道
            for (SmsChannel channel : smsChannels) {
                if (channel.getServerName().equals(smsInfo.getChannelName())) {
                    return channel;
                }
            }

        } else if (smsChannels.size() == 1) {
            // 仅1个通道
            return smsChannels.get(0);

        } else {
            // 根据权重分配
            float weightSum = 0.0f;
            long sendSum = 0;
            for (SmsChannel channel : smsChannels) {
                weightSum += channel.getWeight();
                sendSum += channel.getSendTotal();
            }

            if (weightSum == 0) {
                // 权重都设为0，罢工的节奏
                return null;
            }
            if (sendSum == 0) {
                // 都没有分派过，从第一个开始
                return smsChannels.get(0);
            }
            //
            for (SmsChannel channel : smsChannels) {
                if (channel.getSendTotal() / sendSum < channel.getWeight() / weightSum) {
                    return channel;
                }
            }

        }
        return null;
    }



    /**
     * 定时读取配置，获得最新的短信通道
     */
    @Scheduled(fixedRate = 60000)
    protected void loadChannels() {

        if(!scheduleEnable)
            return;
        // TODO:需要动态从配置文件读入，目前先hardcode
		/*
		 * logger.debug("updateChannels..."); SmsChannel channel = new
		 * SmsChannel();
		 * channel.setServerUrl("http://localhost:10006/sms/post");
		 * channel.setName("test-channel"); channel.setWeight(50);
		 *
		 */
        List<SmsChannel> channels = smsChannelMapper.findByGatewayName(server_name);
        for (SmsChannel newChannel : channels) {
            boolean isExists = false;
            for (SmsChannel oldChannel : smsChannels) {
                if (oldChannel.getServerName().equals(newChannel.getServerName())) {
                    isExists = true;
                    break;
                }
            }
            if (!isExists) {
                smsChannels.add(newChannel);
            }
        }

    }

    protected void updateStatus(TSmsInfo smsInfo) {
        smsDao.updateStatus(smsInfo);
    }

    /**
     * 定时任务
     */
    @Scheduled(fixedRateString = "${sms.scheduler.fixedRateMs}")
    protected void schedule() {

        if(!scheduleEnable)
            return;

        List<DSmsReport> reports = sendHelper.queryStatus();
        for (DSmsReport report : reports) {
            if(report.getSendId()!=null) {
                // 回填数据库，更改发送到达状态
                DSmsSendResult result = new DSmsSendResult();
                if (report.getRecept() != null && report.getRecept() == 0) {
                    result.setOk(true);
                } else {
                    result.setOk(false);
                }
                result.setSendId(report.getSendId());
                result.setSendTime(report.getSendTime());
                result.setDeliverTime(report.getDeliverTime());
                handleSendResult(result);
            }
        }

    }

    public boolean handleSendResult(DSmsSendResult result) {

        TSmsInfo smsInfo = new TSmsInfo();
        if (result.getGuid() != null && result.getGuid().length() > 0) {
            smsInfo = smsDao.getByGuid(result.getGuid());
        } else {
            smsInfo = smsDao.getBySendId(result.getSendId());
        }
        smsInfo.setSendTime(result.getSendTime());
        smsInfo.setSendId(result.getSendId());
        if (result.isOk()) {
            //onSendOk(smsInfo);
            smsInfo.setStatus(TSmsInfo.SMS_ARRIVAL);
            smsInfo.setSendTime(new Date());
            updateStatus(smsInfo);
        } else {
            smsInfo.setStatus(TSmsInfo.SMS_SEND_FAIL);
            smsInfo.setSendTime(new Date());
            updateStatus(smsInfo);
        }
        return true;
    }
}
