package com.talebase.cloud.os;

import com.talebase.cloud.base.ms.admin.dto.DPermissionOfRole;
import com.talebase.cloud.base.ms.consume.domain.TAccount;
import com.talebase.cloud.base.ms.consume.dto.DAccountLineOperateCondition;
import com.talebase.cloud.base.ms.sms.TSmsInfo;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.ServiceHeaderUtil;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(classes = consumeServiceApplicationTests.class)
@WebAppConfiguration
public class consumeServiceApplicationTests {

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
	public void test_consumeOperate(){
//		TestRestTemplate template = new TestRestTemplate();
		DAccountLineOperateCondition dAccountLineOperateCondition = new DAccountLineOperateCondition();
		dAccountLineOperateCondition.setProjectId(34);
		dAccountLineOperateCondition.setPointVar(2);
		dAccountLineOperateCondition.setTaskId(3);
		dAccountLineOperateCondition.setSmsVar(4);
		dAccountLineOperateCondition.setRemark("remark");
		ServiceRequest request = new ServiceRequest();
		request.setRequest(dAccountLineOperateCondition);
		ServiceHeader serviceHeader = new ServiceHeader();
		serviceHeader.setCompanyId(21);
		serviceHeader.setOperatorName("admin");
		request.setRequestHeader(serviceHeader);
		MsInvoker msInvoker = new MsInvoker();
		RestTemplate restTemplate = new RestTemplate();
		msInvoker.setRestTemplate(restTemplate);
//		template.put("http://localhost:28006/consume/operate", request,serviceHeader);
		msInvoker.put("http://localhost:28006/consumecenter/operate", request, new ParameterizedTypeReference<ServiceResponse<String>>(){});
		//System.out.println(ret);
	}

	@Test
	public void test_qureyAccount(){
		MsInvoker msInvoker = new MsInvoker();
		RestTemplate restTemplate = new RestTemplate();
		msInvoker.setRestTemplate(restTemplate);
		ServiceRequest request = new ServiceRequest();
		ServiceResponse<TAccount> response = msInvoker.post("http://localhost:28006/consumecenter/qureyAccount/"+21,request, new ParameterizedTypeReference<ServiceResponse<TAccount>>(){});
		System.out.println(response.getResponse().getPointBalance());
	}

	@Test
	public void test_getPays(){
		MsInvoker msInvoker = new MsInvoker();
		RestTemplate restTemplate = new RestTemplate();
		msInvoker.setRestTemplate(restTemplate);
		ServiceRequest request = new ServiceRequest();
		ServiceHeader header = new ServiceHeader();
		PageRequest pageRequest = new PageRequest();
		pageRequest.setLimit(10);
		pageRequest.setPageIndex(1);
		pageRequest.setStart(1);
		header.setOperatorName("admin");
		request.setRequestHeader(header);
		request.setPageReq(pageRequest);
		ServiceResponse<PageResponseWithParam> response = msInvoker.post("http://localhost:28006/consumecenter/getPays",request, new ParameterizedTypeReference<ServiceResponse<PageResponseWithParam>>(){});
		System.out.println(response.getResponse().getResults().size());
	}

	@Test
	public void getConsumes(){

	}
	/*@Test
	public void test_mqReceive(){
		TestRestTemplate template = new TestRestTemplate();
		Boolean ret = template.postForObject("http://localhost:10005/sms/receivemq", "sss", Boolean.class);
		System.out.println(ret);
	}*/
}
