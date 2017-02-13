package com.talebase.cloud.ms.sms.model;

import com.talebase.cloud.base.ms.sms.TSmsInfo;

/**
 * 传输到子服务的短信信息
 * @author Aaron.Liao
 *
 */
public class DSmsInfo {
  
	private String content;
	private String sendto;
	private String guid;  
	private String callbackUrl;  


	public DSmsInfo() {
		
	}
	
	public DSmsInfo(TSmsInfo smsInfo) {
		content = smsInfo.getContent();
		sendto = smsInfo.getSendto();
		guid = smsInfo.getGuid();		
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendto() {
		return sendto;
	}

	public void setSendto(String sendto) {
		this.sendto = sendto;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
 
	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

}
