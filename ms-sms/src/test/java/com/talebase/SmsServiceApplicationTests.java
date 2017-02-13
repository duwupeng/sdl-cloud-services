package com.talebase;

import com.talebase.cloud.base.ms.sms.TSmsInfo;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.talebase.cloud.ms.sms.MsSmsApplication;

@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(classes = MsSmsApplication.class)
@WebAppConfiguration
public class SmsServiceApplicationTests {

	//@Test
	/*public void test_异步发送短信() {
		TestRestTemplate template = new TestRestTemplate();
		TSmsInfo smsInfo = new TSmsInfo();
		UUID uuid = UUID.randomUUID();
		smsInfo.setGuid(uuid.toString());
		smsInfo.setSendto("13719414210");
		smsInfo.setContent("你好world!");
		smsInfo.setPayCode("paycode");
		Boolean ret = template.postForObject("http://localhost:10005/sms/post", smsInfo, Boolean.class);
		assertTrue(ret);
	}*/
	@Test
	public void test_mqSend(){
		TestRestTemplate template = new TestRestTemplate();TSmsInfo smsInfo = new TSmsInfo();
		String ret = template.postForObject("http://localhost:27001/sms/sendSms", "", String.class);
		System.out.println(ret);
	}

	/*@Test
	public void test_mqReceive(){
		TestRestTemplate template = new TestRestTemplate();
		Boolean ret = template.postForObject("http://localhost:10005/sms/receivemq", "sss", Boolean.class);
		System.out.println(ret);
	}*/
}
