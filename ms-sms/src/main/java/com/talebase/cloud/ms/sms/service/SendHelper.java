package com.talebase.cloud.ms.sms.service;



import com.talebase.cloud.ms.sms.model.DSmsInfo;
import com.talebase.cloud.ms.sms.model.DSmsReport;
import com.talebase.cloud.ms.sms.model.DSmsSendResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class SendHelper {

	@Value("${sms.send.prefixUrl}")
	private String sendUrl;

	@Value("${sms.query.prefixUrl}")
	private String queryUrl;
	
	@Value("${sms.send.username}")
	private String username;

	@Value("${sms.send.password}")
	private String password;

	@Value("${sms.send.signature}")
	private String signature;

	// @Autowired
	RestTemplate restTemplate = new RestTemplate();

	Logger logger = Logger.getLogger(SendHelper.class);

	/**
	 * 发送短信
	 * @param smsInfo
	 * @return  
	 */
	public DSmsSendResult sendUseHaobo(DSmsInfo smsInfo) {
		String response = "";
		try {
			String url = formatSms(smsInfo);
			// 发送
			response = restTemplate.getForObject(url, String.class);
		}
		catch (Exception ex) {
			response = "";
		}
		
		DSmsSendResult result = new DSmsSendResult();
		result.setGuid(smsInfo.getGuid());
		result.setSendTime(new Date());
		
		Map<String, String> params = pareUrlParameter(response);
		
		if (params.get("id") != null) { // 成功标识，见文档
			result.setOk(true);
			result.setSendId(params.get("id"));
		}
		else {
			result.setOk(false);
		}
		
		return result;
	}

	protected String formatSms(DSmsInfo smsInfo) {

		String content = "";
		try {
			content = signature + smsInfo.getContent();
			byte[] contentBytes = content.getBytes("gb2312");
			content = bytesToHex(contentBytes);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
		String gettUrl = MessageFormat.format("{0}?un={1}&pw={2}&da={3}&sm={4}&dc=15&rd=1", sendUrl, username,
				password, smsInfo.getSendto(), content);
		return gettUrl;
	}
	
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/**
	 * 字节数据转hex字符串,如:0A0B0C.
	 * @param bytes
	 * @return
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	/**
	 * 解析参数
	 * @param parameters
	 * @return
	 */
	public static Map<String, String> pareUrlParameter(String parameters) {
		
		Map<String, String> paraMap = new HashMap<>();
		String[] items = parameters.split("&");
		for (String item : items) {
			String[] kv = item.split("=");
			if (kv.length >= 2) {
				paraMap.put(kv[0], kv[1]);
			}
		}
		return paraMap;
	}

	/**
	 * 向供应商发起查询请求，并解析返回结果
	 * @return
	 */
	public List<DSmsReport> queryStatus() {
		List<DSmsReport> reports = new ArrayList<>();
		String gettUrl = MessageFormat.format("{0}?un={1}&pw={2}", 
											queryUrl, username,	password);
		String response = restTemplate.getForObject(gettUrl, String.class);
		// 返回多行，一行一条记录
		BufferedReader reader =  new BufferedReader(new StringReader(response)); 
		String line;
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			while ((line = reader.readLine())!=null) {
				if (line.length() > 0) {
					Map<String, String> params = pareUrlParameter(line);

					DSmsReport report = new DSmsReport();
					report.setSendId(params.get("id"));
					report.setSendto(params.get("sa"));
//				report.setRecept(Integer.valueOf(params.get("rp")));
					try {
						report.setSendTime(df.parse(params.get("sd")));
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					try {
						report.setDeliverTime(df.parse(params.get("dd")));
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					reports.add(report);
				}
			}
			return reports;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reports;
	}
}
