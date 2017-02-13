package com.talebase.cloud.ms.sms.service;

import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.ms.sms.model.DSmsSendResult;

public interface SmsService {
	/**
	 * 同步发送短信
	 * @param smsInfo
	 * @return
	 */
	boolean sendSms(TSmsInfo smsInfo);
	
	/**
	 * 异步发送短信
	 * @param smsInfo
	 * @return
	 */
	boolean postSms(TSmsInfo smsInfo);
	
	/**
	 * 查询指定短信
	 * @param guid
	 * @return
	 */
	TSmsInfo querySms(String guid);
	
	/**
	 * 处理子服务的发送结果
	 * @param result
	 * @return
	 */
	boolean handleSendResult(DSmsSendResult result);

	/**
	 * 根据uuid查询短信发送状态
	 * @param uuid
	 * @return
     */
	int queryStatus(String uuid);
}
