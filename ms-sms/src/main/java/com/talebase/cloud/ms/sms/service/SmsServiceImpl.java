package com.talebase.cloud.ms.sms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.talebase.cloud.base.ms.sms.TSmsInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.talebase.cloud.ms.sms.dao.SmsChannelMapper;
import com.talebase.cloud.ms.sms.dao.SmsMapper;
import com.talebase.cloud.ms.sms.model.SmsChannel;
import com.talebase.cloud.ms.sms.model.DSmsInfo;
import com.talebase.cloud.ms.sms.model.DSmsSendResult;

@EnableScheduling
@Service
public class SmsServiceImpl implements SmsService {

	@Autowired
	SmsMapper smsDao;

	@Autowired
	SmsChannelMapper smsChannelMapper;

	// @Autowired
	RestTemplate restTemplate = new RestTemplate();

	List<SmsChannel> smsChannels = new ArrayList<>();

	boolean isStopSchedule = false;

	@Value("${sms.send.maxTry}")
	int MAX_TRY_SEND = 1;

	@Value("${sms.send.callbackUrl}")
	String callbackUrl;

	@Value("${spring.application.name}")
	String server_name;

	@Value("${schedule.enable}")
	Boolean scheduleEnable;

	@Override
	public boolean sendSms(TSmsInfo smsInfo) {

		return false;
	}

	Logger logger = Logger.getLogger(SmsChannel.class);

	@Override
	public boolean postSms(TSmsInfo smsInfo) {

		logger.debug("postSms...");
		if (smsDao.getByGuid(smsInfo.getGuid()) != null) {
			// 已经存在重复GUID的短信
			// throw new Exception("");
			return false;
		}
		// 保存至数据库，定时任务会轮询
		smsInfo.setStatus(TSmsInfo.SMS_SENDING);
		smsInfo.setSendTime(new Date());
		return (smsDao.insert(smsInfo) != null);
	}

	@Override
	public TSmsInfo querySms(String guid) {

		return smsDao.getByGuid(guid);
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

	public void stopSchedule() {
		isStopSchedule = true;
	}

	public void starSchedule() {
		isStopSchedule = false;
	}

	/**
	 * 定时任务
	 */
	@Scheduled(fixedRateString = "${sms.scheduler.fixedRateMs}")
	protected void schedule() {

		// 停止?
		if (isStopSchedule) {
			return;
		}

		// 单线程分派短信，将来可以考虑线程池
		List<TSmsInfo> allUnsend = smsDao.findAllUnsend();
		for (TSmsInfo smsInfo : allUnsend) {
			dispatchSms(smsInfo);
		}

	}

	protected boolean dispatchSms(TSmsInfo smsInfo) {

		logger.debug("dispatchSms...");

		SmsChannel channel = selectChannel(smsInfo);
		if (channel == null) {
			return false;
		}

		onSending(smsInfo);
		// 发送
		DSmsInfo transSmsInfo = new DSmsInfo(smsInfo);
		transSmsInfo.setCallbackUrl(callbackUrl);
		Boolean ret = restTemplate.postForObject(channel.getServerUrl(), transSmsInfo, Boolean.class);
		// 发送计数
		smsInfo.setSendCount(smsInfo.getSendCount() + 1);
		if (!ret) {
			if (smsInfo.getSendCount() >= MAX_TRY_SEND) {
				onSendError(smsInfo);
			} else {
				onWaitResend(smsInfo);
			}
		}
		return ret;

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

	protected void onSending(TSmsInfo smsInfo) {
		smsInfo.setStatus(TSmsInfo.SMS_SENDING);
		smsInfo.setSendTime(new Date());
		updateStatus(smsInfo);
	}

	protected void onSendError(TSmsInfo smsInfo) {
		smsInfo.setStatus(TSmsInfo.SMS_SEND_FAIL);
		smsInfo.setSendTime(new Date());
		updateStatus(smsInfo);
		// 向消费方反馈结果
		feedbackStatus(smsInfo);
	}

	protected void onSendOk(TSmsInfo smsInfo) {
		smsInfo.setStatus(TSmsInfo.SMS_SENDING);
		smsInfo.setSendTime(new Date());
		updateStatus(smsInfo);
		// 向消费方反馈结果
		feedbackStatus(smsInfo);
	}

	protected void onWaitResend(TSmsInfo smsInfo) {
		smsInfo.setStatus(TSmsInfo.SMS_SENDING);
		smsInfo.setSendTime(new Date());
		updateStatus(smsInfo);
	}

	protected void updateStatus(TSmsInfo smsInfo) {
		smsDao.updateStatus(smsInfo);
	}

	protected void feedbackStatus(TSmsInfo smsInfo) {
		// MQ??

	}

	@Override
	public boolean handleSendResult(DSmsSendResult result) {

		TSmsInfo smsInfo = null;
		if (result.getGuid() != null && result.getGuid().length() > 0) {
			smsInfo = smsDao.getByGuid(result.getGuid());
		} else {
			smsInfo = smsDao.getBySendId(result.getSendId());
		}
		smsInfo.setSendTime(result.getSendTime());
		smsInfo.setSendId(result.getSendId());
		if (result.isOk()) {
			onSendOk(smsInfo);
		} else {
			if (smsInfo.getSendCount() >= MAX_TRY_SEND) {
				onSendError(smsInfo);
			} else {
				onWaitResend(smsInfo);
			}
		}
		return true;
	}
	@Override
	public int queryStatus(String uuid){
		TSmsInfo smsInfo = smsDao.getByGuid(uuid);
		if(smsInfo == null){
			return -1;
		}
		return smsInfo.getStatus();
	}

}
