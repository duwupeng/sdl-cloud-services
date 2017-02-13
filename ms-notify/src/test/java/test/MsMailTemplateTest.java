package test;

import com.talebase.cloud.base.ms.notify.domain.TNotifyTemplate;
import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateMethod;
import com.talebase.cloud.base.ms.notify.enumes.TNotifyTemplateType;
import com.talebase.cloud.common.MsInvoker;
import com.talebase.cloud.common.protocal.*;
import com.talebase.cloud.common.util.StringUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by bin.yang on 2016-11-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MsMailTemplateTest {
    private int port = 28004;
    private String servicePathPre = "";

    @Before
    public void before() {
    }

    @Test
    public void test_get_getTemplates() {
        String servicePath = "http://localhost:" + port + "/mailTemplates";

        DNotifyTemplate d = new DNotifyTemplate();
        d.setStatus(1);
        d.setType(TNotifyTemplateType.CUSTOMIZE.getValue());
        d.setMethod(TNotifyTemplateMethod.MAIL.getValue());
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageIndex(1);
        pageRequest.setLimit(30);
        ServiceHeader serviceHeader = new ServiceHeader();
        serviceHeader.setOperatorName("zhaokh");
        serviceHeader.setOrgCode("1_");
        serviceHeader.setCompanyId(1);
        ServiceRequest<DNotifyTemplate> req = new ServiceRequest(serviceHeader, d, pageRequest);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<PageResponse<DNotifyTemplate>> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<PageResponse<DNotifyTemplate>>>() {
        });
        System.out.print("asdasdasdasdasd::" + response.getResponse());

    }

    @Test
    public void save() {
        String servicePath = "http://localhost:" + port + "/mailTemplate";
        DNotifyTemplate d = new DNotifyTemplate();
        d.setName("邮件1");
        //d.setCompanyId(12);
        d.setContent("创建测试");
        d.setWhetherDefault(true);
        ServiceHeader r = new ServiceHeader();
        r.setOperatorName("system");
        r.setCompanyId(12);
        ServiceRequest<DNotifyTemplate> req = new ServiceRequest(r, d);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<List<DNotifyTemplate>> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<List<DNotifyTemplate>>>() {
        });
    }

    @Test
    public void update() {
        String servicePath = "http://localhost:" + port + "/mailTemplate/status/73";
        ServiceHeader r = new ServiceHeader();
        r.setOperatorName("system2");
        ServiceRequest<DNotifyTemplate> req = new ServiceRequest(r, false);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.put(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
    }

    @Test
    public void delete() {
        String servicePath = "http://localhost:" + port + "/mailTemplate";
        String id = "73,74";
        ServiceHeader r = new ServiceHeader();
        r.setOperatorName("admin");
        ServiceRequest<DNotifyTemplate> req = new ServiceRequest(r, StringUtil.toIntListByComma(id));

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.delete(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {});
        System.out.println("adadad");
    }

    @Test
    public void checkName() {
        String servicePath = "http://localhost:" + port + "/mailTemplate/check";
        DNotifyTemplate d = new DNotifyTemplate();
        d.setId(73);
        d.setName("aaaaaa");
        d.setCreator("admin");
        ServiceRequest<DNotifyTemplate> req = new ServiceRequest(new ServiceHeader(), d);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<String>>() {
        });
        System.out.println("==asd=a=sda=sd=asd=as=da=sd=as=das==sad=sa=da=sda=d=:" + response.getResponse());
    }
}
