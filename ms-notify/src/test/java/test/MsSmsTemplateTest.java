package test;

import com.talebase.cloud.base.ms.notify.dto.DNotifyTemplate;
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
 * Created by xia.li on 2016-11-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MsSmsTemplateTest {
    private int port = 28006;
    private String servicePathPre = "";

    @Before
    public void before() {}

    @Test
    public void test_get_getTemplates() {
        String servicePath = "http://localhost:" + port + "/smsTemplates"; //查询列表
        DNotifyTemplate d = new DNotifyTemplate();
        d.setCompanyId(1);
        PageRequest pageRequest = new PageRequest();
        pageRequest.setStart(0);
        pageRequest.setLimit(5);
        d.setCreator("admin");
        ServiceRequest<DNotifyTemplate> req = new ServiceRequest(new ServiceHeader(), d, pageRequest);

        RestTemplate restTemplate = new RestTemplate();
        MsInvoker msInvoker = new MsInvoker();
        msInvoker.setRestTemplate(restTemplate);
        ServiceResponse<PageResponse<DNotifyTemplate>> response = msInvoker.post(servicePath, req, new ParameterizedTypeReference<ServiceResponse<PageResponse<DNotifyTemplate>>>() {
        });
        System.out.print("asdasdasdasdasd::" + response.getResponse());

    }

    @Test //测试成功
    public void save(){
        String servicePath = "http://localhost:" + port + "/smsTemplate"; //创建模板
        DNotifyTemplate d = new DNotifyTemplate();
        d.setName("短信2");
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
    public void update(){
        String servicePath ="http://localhost:" + port + "/smsTemplate/status/79";    //修改模板
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
    public void delete(){
        String servicePath = "http://localhost:" + port + "/smsTemplate";//删除模板
        String id = "79,80";
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
    public void checkName(){
        String servicePath = "http://localhost:" + port + "/smsTemplate/check";//检查模板名
        DNotifyTemplate d = new DNotifyTemplate();
        d.setId(80);
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
