package com.talebase.cloud.ms.sms.model;

/**
 * 短信通道信息
 * @author Aaron.Liao
 *
 */
public class SmsChannel {

	protected int id;
	protected String serverName;
	protected String providerName;
	protected String serverUrl;
	protected int status;
	
	protected float weight;
	
	protected long sendTotal = 0;
	protected long successTotal = 0;
	protected long failureTotal = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
 
	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public long getSendTotal() {
		return sendTotal;
	}

	public void setSendTotal(long sendTotal) {
		this.sendTotal = sendTotal;
	}

	public long getSuccessTotal() {
		return successTotal;
	}

	public void setSuccessTotal(long successTotal) {
		this.successTotal = successTotal;
	}

	public long getFailureTotal() {
		return failureTotal;
	}

	public void setFailureTotal(long failureTotal) {
		this.failureTotal = failureTotal;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
 
}
