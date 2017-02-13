package test;

import com.talebase.cloud.base.ms.notify.dto.DNotifyRecord;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MsNotifyRecordTest {
    private int port = 28004;
    private String servicePathPre = "";

    @Before
    public void before() {
    }

    @Test
    public void get(){
        String servicePath = "http://localhost:" + port + "/notifyRecords";

        DNotifyRecord d = new DNotifyRecord();
        d.setCompanyId(1);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setStart(0);
        pageRequest.setLimit(2);
        ServiceRequest<DNotifyTemplate> req = new ServiceRequest(new ServiceHeader(), d, pageRequest);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<PageResponse<DNotifyRecord>> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<PageResponse<DNotifyRecord>>>() {
        });
        System.out.println("asdasdasdasdasd::" + response.getResponse().getTotal());

        System.out.println("----------------------------------------------");
    }

    @Test
    public void save() {
        String servicePath = "http://localhost:" + port + "/notifyRecord";
        DNotifyRecord d = new DNotifyRecord();
        d.setCompanyId(1);
        d.setProjectId(1);
        d.setTaskId(1);
        d.setRoleId(0);
        d.setAccount("zs");
        d.setName("张三");
        d.setSendSubject("通知");
        d.setSendContent("你爸爸叫你回家做饭啦");
        d.setEmail("daorong.li@talebase.com");
        d.setSendType(TNotifyTemplateMethod.MAIL.getValue());
        d.setSender("admin");
        ServiceRequest<DNotifyRecord> req = new ServiceRequest(new ServiceHeader(), d);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<DNotifyRecord> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<DNotifyRecord>>() {
        });
    }

    @Test
    public void update() {
        String servicePath = "http://localhost:" + port + "/notifyRecord/1";
        DNotifyRecord d = new DNotifyRecord();
        d.setId(1);
        d.setSendStatus(1);
        ServiceRequest<DNotifyRecord> req = new ServiceRequest(new ServiceHeader(), d);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<DNotifyRecord> response = msInvoker.put(servicePath, req, new ParameterizedTypeReference<ServiceResponse<DNotifyRecord>>() {
        });
    }

    @Test
    public void export() {
        String servicePath = "http://localhost:" + port + "/notifyRecord/list";
        ServiceRequest<DNotifyTemplate> req = new ServiceRequest(new ServiceHeader(),0);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<List<DNotifyRecord>>>() {});

        System.out.println(response != null);
    }

}
