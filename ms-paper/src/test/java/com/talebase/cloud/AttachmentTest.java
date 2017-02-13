package com.talebase.cloud;

import com.talebase.cloud.base.ms.paper.dto.DAttachment;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.ServiceHeader;
import com.talebase.cloud.common.protocal.ServiceRequest;
import com.talebase.cloud.common.protocal.ServiceResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bin.yang on 2016-12-7.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class AttachmentTest {
    private int port = 28007;

    @Before
    public void before() {
    }

    @Test
    public void add() {
        String servicePath = "http://localhost:" + port + "/question/attachment";

        List<DAttachment> list = new ArrayList<>();
        DAttachment dAttachment = new DAttachment();
//        dAttachment.setQuestion("下图表达了什么意思？");
//        dAttachment.setFilePath("/upload");
//        dAttachment.setType(0);
//        dAttachment.setNo("2");
//        dAttachment.setVersion(1);
        list.add(dAttachment);

        DAttachment dAttachment1 = new DAttachment();
//        dAttachment1.setQuestion("word文档表达了什么意思？");
//        dAttachment1.setFilePath("/upload");
//        dAttachment1.setType(1);
//        dAttachment1.setNo("2");
//        dAttachment1.setVersion(1);
        list.add(dAttachment1);

        DAttachment dAttachment2 = new DAttachment();
//        dAttachment2.setQuestion("excel文档表达了什么意思？");
//        dAttachment2.setFilePath("/upload");
//        dAttachment2.setType(2);
//        dAttachment2.setNo("2");
//        dAttachment2.setVersion(1);
        list.add(dAttachment2);

        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");
        ServiceRequest<List<DAttachment>> req = new ServiceRequest(serviceHeader, list);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {});
    }
    @Test
    public void query() {
        String servicePath = "http://localhost:" + port + "/question/attachments";

        List list = new ArrayList<>();
//        DPageDetail dPageDetail = new DPageDetail();
//        dPageDetail.setSubjectNo("1");
//        dPageDetail.setSubjectVersion(1);
//        list.add(dPageDetail);

//        DPageDetail dPageDetail1 = new DPageDetail();
//        dPageDetail1.setSubjectNo("2");
//        dPageDetail1.setSubjectVersion(1);
//        list.add(dPageDetail1);
//
//        DPageDetail dPageDetail2 = new DPageDetail();
//        dPageDetail2.setSubjectNo("3");
//        dPageDetail2.setSubjectVersion(1);
//        list.add(dPageDetail2);


        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");
        ServiceRequest<List<DAttachment>> req = new ServiceRequest(serviceHeader, list);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<List<DAttachment>> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<List<DAttachment>>>() {});
        System.out.println("dadasdasdasdasd");
    }

    @Test
    public void queryById() {
        String servicePath = "http://localhost:" + port + "/question/attachment/1";
        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<DAttachment> response = msInvoker.get(servicePath, new ParameterizedTypeReference<ServiceResponse<DAttachment>>() {});
        System.out.println("dadasdasdasdasd");
    }

    @Test
    public void update() {
        String servicePath = "http://localhost:" + port + "/question/attachment";

        DAttachment dAttachment = new DAttachment();
        dAttachment.setId(1);
//        dAttachment.setQuestion("修改后：下图表达了什么意思？");
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setCompanyId(1);
        serviceHeader.setOperatorName("admin");
        ServiceRequest<DAttachment> req = new ServiceRequest(serviceHeader, dAttachment);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.put(servicePath,req, new ParameterizedTypeReference<ServiceResponse<String>>() {});
        System.out.println("dadasdasdasdasd");
    }

}
