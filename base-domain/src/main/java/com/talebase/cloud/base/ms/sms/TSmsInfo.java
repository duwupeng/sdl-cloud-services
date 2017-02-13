package com.talebase.cloud.base.ms.sms;

import java.util.Date;

public class TSmsInfo {

	/**
	 * 短信状态枚举
	 * @author Aaron.Liao
	 *
	 */
	/*
	public enum SmsStatus {
		
		SMS_UNSEND(0),			// 未发送
		SMS_SEND_OK(1),			// 发送成功
		SMS_FAIL_AND_WAIT(2),	// 发送失败，在等重发
		SMS_SEND_FAIL(3),		// 确认发送失败
		SMS_PAY_FAIL(4),		// 消费失败
		SMS_ARRIVAL(5),			// 对方已收到短信
		SMS_SENDING(6);			// 发送中
		
		SmsStatus(int value) {
			this.value = value;
		}		
		
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return String.valueOf(value);
		}
				
		public int getInt() {
			return value;
		}
		private int value;
	}
	 */
	
	public static final int SMS_SEND_FAIL = 0;		// 确认发送失败
	public static final int SMS_ARRIVAL = 2;		// 对方已收到短信
	public static final int SMS_SENDING = 1;		// 发送中
	
	private String content;
	private String sendto;
	private String guid;
	private String channelName;
	private String sendId; //供应商对短信的标识
	private int status;
	private Date sendTime;
	private Date receivedTime;
	private boolean hasFeedback;
	private int sendCount = 0;
	private String tableName;

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	} 

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public boolean isHasFeedback() {
		return hasFeedback;
	}

	public void setHasFeedback(boolean hasFeedback) {
		this.hasFeedback = hasFeedback;
	}

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
