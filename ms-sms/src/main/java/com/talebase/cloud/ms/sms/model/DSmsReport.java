package com.talebase.cloud.ms.sms.model;

import java.util.Date;

/**
 * 短信发送状态报告
 * 
 * @author Aaron.Liao
 *
 */
public class DSmsReport {

	private String sendId; // 短信编号(供应商方)
	private String sendto; // 接收方手机号
	private Integer recept; // 状态报告错误码;0表示成功
	private Date sendTime; // 发送时间
	private Date deliverTime; // 送达时间

	public String getSendId() {
		return sendId;
	}

	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	public String getSendto() {
		return sendto;
	}

	public void setSendto(String sendto) {
		this.sendto = sendto;
	}

	public Integer getRecept() {
		return recept;
	}

	public void setRecept(Integer recept) {
		this.recept = recept;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

}
